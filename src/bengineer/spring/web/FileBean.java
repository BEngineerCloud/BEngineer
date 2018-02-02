package bengineer.spring.web;

import bengineer.spring.filter.*;

import org.rosuda.REngine.*;
import org.rosuda.REngine.Rserve.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.mybatis.spring.SqlSessionTemplate;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/beFiles/")
public class FileBean {
	@Autowired
	private SqlSessionTemplate sqlSession = null;
	public void setSqlSession(SqlSessionTemplate sqlSession) {
		this.sqlSession = sqlSession;
	}
	@RequestMapping("beMyList.do") // 내 파일 보기
	public String myFile(HttpSession session, Model model, int folder, @RequestParam(value="movefile_Ref", defaultValue="0") int movefile_Ref, @RequestParam(value="movefile_FRef", defaultValue="0") int movefile_FRef) throws RserveException, REXPMismatchException {
		if(folder < 0) {return "redirect:/beFiles/beRecentFiles.do?weeks=" + -folder;}
		if(MainBean.loginCheck(session)) {return "redirect:/beMember/beLogin.do";} // 로그인 세션 없을 시 리디렉트
		String owner = (String)session.getAttribute("id");
		String nickname = (String)session.getAttribute("nickname");
		File file = new File("d:/PM/BEngineer/" + owner);
		FileDTO dto = new FileDTO();
		dto.setOwner(owner);
		int folder_ref = 0;
		List address_ref = null;
		//	
		
		RConnection r = new RConnection();
		if(file.exists()) {
			if(folder == 0) {
				dto.setOrgname(owner);
				dto.setFolder_ref(0);
				folder_ref = (int)sqlSession.selectOne("bengineer.getref", dto); // 폴더값이 안 들어왔을 경우 닉네임과 같은 이름의 기본폴더로 이동
				address_ref = getAddr(folder_ref);
			}else {
				folder_ref = folder;
				address_ref = getAddr(folder_ref);
			}
		}else { // 닉네임 폴더가 없을 때 닉네임 폴더 밑 기본폴더 생성 
			dto.setOrgname(owner);
			dto.setFilename(nickname);
			dto.setFolder_ref(0);
			sqlSession.insert("bengineer.makebasedir", dto);
			folder_ref = (int)sqlSession.selectOne("bengineer.getref", dto);
			address_ref = getAddr(folder_ref);
			boolean mkdirch = true;
			if(!makeBaseDir("image", address_ref)) {mkdirch = false;}
			if(!makeBaseDir("video", address_ref)) {mkdirch = false;}
			if(!makeBaseDir("music", address_ref)) {mkdirch = false;}
			if(!makeBaseDir("document", address_ref)) {mkdirch = false;}
			if(!makeBaseDir("etc", address_ref)) {mkdirch = false;}
			if(!mkdirch) { // 하나라도 오류 발생시
				sqlSession.delete("bengineer.deletefile", folder_ref);
				model.addAttribute("alert", "폴더를 생성하는 도중 오류가 발생했습니다.");
				model.addAttribute("location", "history.go(-1)");
				return "beFiles/alert";
			}
		}
		List filelist = sqlSession.selectList("bengineer.getfiles", folder_ref);
		sqlSession.update("bengineer.hit", folder);
		model.addAttribute("list", filelist);
		List folderaddress = new ArrayList(); // 폴더 경로를 하나씩 저장하기 위한 리스트
		List orgaddress = new ArrayList(); // 폴더주소에 저장된 각각의 폴더에 대한 실제 경로를 하나씩 저장하기 위한 리스트
		if(address_ref.size() < 5) {
			for(int i = address_ref.size() - 1; i >= 0; i--) {
				dto = (FileDTO)address_ref.get(i);
				folderaddress.add(dto.getFilename());
				orgaddress.add(dto.getNum());
			}
		}else {
			folderaddress.add(nickname);
			orgaddress.add(0);
			folderaddress.add("..."); // 폴더 경로가 5개를 넘어길 시 기본폴더와 가장 위의 3개를 제외하고 생략
			orgaddress.add(null);
			for(int i = 2; i >= 0; i--) {
				dto = (FileDTO)address_ref.get(i);
				folderaddress.add(dto.getFilename());
				orgaddress.add(dto.getNum());
			}
		}
		if(!owner.equals(dto.getOwner())) {
			model.addAttribute("alert", "잘못된 접근입니다.");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}
		model.addAttribute("folderaddress", folderaddress);
		model.addAttribute("orgaddress", orgaddress);
		model.addAttribute("folder_ref", folder_ref);
		model.addAttribute("folder",folder); // 상위폴더로 이동하기 위해
		model.addAttribute("movefile_Ref",movefile_Ref);
		model.addAttribute("movefile_FRef",movefile_FRef);
		
		List list = sqlSession.selectList("bengineer.size",folder_ref);
		if(list.size()!=0) {
			//r.eval("library(base64enc)");
			r.eval("png('rjava.png')");
		
			String Fsize ="c(";
			for(int i=0; i<list.size();i++) {
				FileDTO file1 = (FileDTO)list.get(i);
				if(i==list.size()-1) {
					Fsize+=file1.getFilesize()+")";
				}else {
					Fsize+=file1.getFilesize()+",";
				}
			}
			r.eval("Fsize<-"+Fsize);
			//System.out.println(Fsize);
			r.eval("barplot(Fsize, names='크기', col=rainbow(20))");
			//r.eval("barplot(Fsize,names='크기',col=rainbow(20))");
			r.eval("dev.off()");
			REXP image = r.eval("r<-readBin('rjava.png', 'raw', 100*100)");
			/*
			r.eval("encoded_png<-sprintf(\"<img src='data:image/png;base64,%s'/>\", base64encode(\"rjava.png\"))");
			r.eval("encoded_png");*/
			model.addAttribute("gra",Base64.getEncoder().encodeToString(image.asBytes())); 
		}
		r.close();
		return "beFiles/beList";
	}
	@RequestMapping("beSharedList.do") // 내 공유파일 보기
	public String shareFile(HttpSession session, Model model, int folder) {
		if(MainBean.loginCheck(session)) {return "redirect:/beMember/beLogin.do";} // 로그인 세션 없을 시 리디렉트
		String id = (String)session.getAttribute("id");
		if(folder == 0) {
			List filelist = sqlSession.selectList("bengineer.mysharedfile", id);
			model.addAttribute("list", filelist);
			List folderaddress = new ArrayList(); // 폴더 경로를 하나씩 저장하기 위한 리스트
			List orgaddress = new ArrayList(); // 폴더주소에 저장된 각각의 폴더에 대한 전체 경로를 하나씩 저장하기 위한 리스트
			folderaddress.add("내 공유 파일");
			orgaddress.add(0);
			model.addAttribute("folderaddress", folderaddress);
			model.addAttribute("orgaddress", orgaddress);
			model.addAttribute("folder_ref", folder);
			model.addAttribute("write", false);
			model.addAttribute("basedir", false);
			return "beFiles/beSharedList";
		}else {
			List address_ref = getShareAddr(folder, id);
			if(address_ref == null) {
				model.addAttribute("alert", "접근권한이 없습니다.");
				model.addAttribute("location", "history.go(-1)");
				return "beFiles/alert";
			}
			sqlSession.update("bengineer.hit", folder);
			KeyDTO kdto = (KeyDTO)address_ref.remove(address_ref.size() - 1);
			if(kdto.getRw() == 0) {
				model.addAttribute("write", false);
			}else {
				model.addAttribute("write", true);
			}
			String end = kdto.getEnddate().toString();
			end = end.substring(0, end.lastIndexOf("."));
			model.addAttribute("enddate", end);
			List filelist = sqlSession.selectList("bengineer.getfiles", folder);
			model.addAttribute("list", filelist);
			List folderaddress = new ArrayList(); // 폴더 경로를 하나씩 저장하기 위한 리스트
			List orgaddress = new ArrayList(); // 폴더주소에 저장된 각각의 폴더에 대한 실제 경로를 하나씩 저장하기 위한 리스트
			FileDTO dto = new FileDTO();
			if(address_ref.size() < 4) {
				folderaddress.add("내 공유 파일");
				orgaddress.add(0);
				for(int i = address_ref.size() - 1; i >= 0; i--) {
					dto = (FileDTO)address_ref.get(i);
					folderaddress.add(dto.getFilename());
					orgaddress.add(dto.getNum());
				}
			}else {
				folderaddress.add("내 공유 파일");
				orgaddress.add(0);
				folderaddress.add("..."); // 폴더 경로가 5개를 넘어길 시 기본폴더와 가장 위의 3개를 제외하고 생략
				orgaddress.add(null);
				for(int i = 2; i >= 0; i--) {
					dto = (FileDTO)address_ref.get(i);
					folderaddress.add(dto.getFilename());
					orgaddress.add(dto.getNum());
				}
			}
			if(dto.getImportant() == -1) {
				model.addAttribute("basedir", true);
			}else {
				model.addAttribute("basedir", false);
			}
			model.addAttribute("folderaddress", folderaddress);
			model.addAttribute("orgaddress", orgaddress);
			model.addAttribute("folder_ref", folder);
			return "beFiles/beSharedList";
		}
	}
	@RequestMapping("beTrashcan.do") // 내 삭제파일 보기
	public String trashcan(HttpSession session, Model model, int folder) {
		if(MainBean.loginCheck(session)) {return "redirect:/beMember/beLogin.do";} // 로그인 세션 없을 시 리디렉트
		String id = (String)session.getAttribute("id");
		if(folder == 0) {
			List filelist = sqlSession.selectList("bengineer.mytrashcan", id);
			model.addAttribute("list", filelist);
			List folderaddress = new ArrayList(); // 폴더 경로를 하나씩 저장하기 위한 리스트
			List orgaddress = new ArrayList(); // 폴더주소에 저장된 각각의 폴더에 대한 전체 경로를 하나씩 저장하기 위한 리스트
			folderaddress.add("휴지통");
			orgaddress.add(0);
			model.addAttribute("folderaddress", folderaddress);
			model.addAttribute("orgaddress", orgaddress);
			model.addAttribute("folder_ref", folder);
			model.addAttribute("write", false);
			return "beFiles/beTrashcan";
		}else {
			List address_ref = getTrashAddr(folder);
			if(address_ref == null) {
				model.addAttribute("alert", "잘못된 접근입니다.");
				model.addAttribute("location", "history.go(-1)");
				return "beFiles/alert";
			}
			List filelist = sqlSession.selectList("bengineer.mytrashcanfolder", folder);
			model.addAttribute("list", filelist);
			List folderaddress = new ArrayList(); // 폴더 경로를 하나씩 저장하기 위한 리스트
			List orgaddress = new ArrayList(); // 폴더주소에 저장된 각각의 폴더에 대한 실제 경로를 하나씩 저장하기 위한 리스트
			FileDTO dto = new FileDTO();
			if(address_ref.size() < 4) {
				folderaddress.add("휴지통");
				orgaddress.add(0);
				for(int i = address_ref.size() - 1; i >= 0; i--) {
					dto = (FileDTO)address_ref.get(i);
					folderaddress.add(dto.getFilename());
					orgaddress.add(dto.getNum());
				}
			}else {
				folderaddress.add("휴지통");
				orgaddress.add(0);
				folderaddress.add("..."); // 폴더 경로가 5개를 넘어길 시 기본폴더와 가장 위의 3개를 제외하고 생략
				orgaddress.add(null);
				for(int i = 2; i >= 0; i--) {
					dto = (FileDTO)address_ref.get(i);
					folderaddress.add(dto.getFilename());
					orgaddress.add(dto.getNum());
				}
			}
			model.addAttribute("folderaddress", folderaddress);
			model.addAttribute("orgaddress", orgaddress);
			model.addAttribute("folder_ref", folder);
			return "beFiles/beTrashcan";
		}
	}
	@RequestMapping("beRecentFiles.do") // 내 파일 보기
	public String recentFile(HttpSession session, Model model, int weeks) {
		if(MainBean.loginCheck(session)) {return "redirect:/beMember/beLogin.do";} // 로그인 세션 없을 시 리디렉트
		if(weeks > 4) {
			model.addAttribute("alert", "잘못된 접근입니다.");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}
		String owner = (String)session.getAttribute("id");
		FileDTO dto = new FileDTO();
		dto.setOwner(owner);
		Date date = new Date(System.currentTimeMillis());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(calendar.DATE, -(7 * weeks));
		Timestamp updatedate = new Timestamp(calendar.getTimeInMillis());
		dto.setUpdatedate(updatedate);
		List filelist = sqlSession.selectList("bengineer.getrecentfiles", dto);
		model.addAttribute("list", filelist);
		List folderaddress = new ArrayList(); // 폴더 경로를 하나씩 저장하기 위한 리스트
		List orgaddress = new ArrayList(); // 폴더주소에 저장된 각각의 폴더에 대한 실제 경로를 하나씩 저장하기 위한 리스트
		folderaddress.add("내 최근 파일");
		orgaddress.add(-weeks);
		model.addAttribute("folderaddress", folderaddress);
		model.addAttribute("orgaddress", orgaddress);
		model.addAttribute("folder_ref", -weeks);
		model.addAttribute("folder", 0); // 상위폴더로 이동하기 위해
		model.addAttribute("movefile_Ref",0);
		model.addAttribute("movefile_FRef",0);
		return "beFiles/beList";
	}
	@RequestMapping(value="fileupload.do", method=RequestMethod.POST) // 업로드 페이지
	public String upload(MultipartHttpServletRequest multi, int folder, String filename, HttpSession session, Model model) {
		if(MainBean.loginCheck(session)) {return "redirect:/beMember/beLogin.do";} // 비 로그인 상태시 로그인 창으로 리디렉트
		if(!checkPower((String)session.getAttribute("id"), folder)) {
			model.addAttribute("alert", "권한이 없습니다.");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}
		try {
			MultipartFile mf = multi.getFile("save");
			String orgname = mf.getOriginalFilename();
			String filetype = orgname.substring(orgname.lastIndexOf(".")); // 확장자
			String contentType = checkFile(filetype); // 확장자를 확인하여 파일타입 확인
			if(contentType.equals("none")) { // 업로드 할 수 없는 종류의 파일일 때
				model.addAttribute("alert", "업로드 할 수 없는 종류의 파일입니다.");
				model.addAttribute("location", "history.go(-1)");
				return "beFiles/alert";
			}
			// 파일명 길이 확인
			if(filename.length() > 90) {
				model.addAttribute("alert", "파일명은 한글로 30글자, 영어,숫자 또는 특수문자로 90글자까지 가능합니다.");
				model.addAttribute("location", "history.go(-1)");
				return "beFiles/alert";
			}
			if(FilenameFilter.nameFilter(orgname, filename)) {
				model.addAttribute("alert", "파일명에 포함될 수 없는 단어가 포함되어 있습니다.");
				model.addAttribute("location", "history.go(-1)");
				return "beFiles/alert";
			}
			boolean typech = true;
			List address_ref = getAddr(folder);
			String fileaddress = "";
			FileDTO dto = new FileDTO();
			for(int i = address_ref.size() - 1; i >= 0; i--) {
				dto = (FileDTO)address_ref.get(i);
				fileaddress += dto.getOrgname() + "/";
			}
			String owner = dto.getOwner();
			// 기본폴더에 올릴 수 없는 파일일 때
			if(fileaddress.startsWith(owner + "/image/") && !contentType.equals("image")) {
				typech = false;
			}else if(fileaddress.startsWith(owner + "/music/") && !contentType.equals("music")) {
				typech = false;
			}else if(fileaddress.startsWith(owner + "/video/") && !contentType.equals("video")) {
				typech = false;
			}else if(fileaddress.startsWith(owner + "/document/") && !contentType.equals("document")) {
				typech = false;
			}else if(fileaddress.startsWith(owner + "/etc/") && !contentType.equals("etc")) {
				typech = false;
			}
			if(!typech) {
				model.addAttribute("alert", "해당 폴더에 업로드 할 수 없는 종류의 파일입니다.");
				model.addAttribute("location", "history.go(-1)");
				return "beFiles/alert";
			}
			if(filename == null || filename.equals("") || filename.equals("파일 이름")) {
				dto.setFilename(orgname);
			}else {
				dto.setFilename(filename);
			}
			dto.setOwner(owner);
			dto.setFolder_ref(folder);
			dto.setOrgname(orgname);
			File copy = new File("d:/PM/BEngineer/" + fileaddress + orgname);
			if(copy.exists()) { // 같은 이름의 파일이 존재할 때 덮어쓰기
				long orgsize = copy.length();
				copy.delete();
				mf.transferTo(copy);
				long filesize = copy.length();
				dto.setFilesize(filesize);
				sqlSession.update("bengineer.updatefile", dto);
				uploadsize(filesize - orgsize, address_ref);
				model.addAttribute("alert", "파일 덮어쓰기 완료");
			}else {
				dto.setFiletype(filetype);
				mf.transferTo(copy);
				long filesize = copy.length();
				dto.setFilesize(filesize);
				sqlSession.insert("bengineer.upload", dto);
				uploadsize(filesize, address_ref);
				model.addAttribute("alert", "파일 업로드 완료");
			}
			if(owner.equals(session.getAttribute("id"))) {
				model.addAttribute("location", "\"/BEngineer/beFiles/beMyList.do?folder=" + folder + "\"");
			}else {
				model.addAttribute("location", "\"/BEngineer/beFiles/beSharedList.do?folder=" + folder + "\"");
			}
			return "beFiles/alert";
		}catch(Exception e) { // 오류 발생시
			e.printStackTrace();
			model.addAttribute("alert", "업로드 도중 오류가 발생했습니다.");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}
	}
	@RequestMapping("createFolder.do") // 폴더 만들기 페이지
	public String folder(HttpSession session, Model model, String foldername, int folder) {
		if(MainBean.loginCheck(session)) {return "redirect:/beMember/beLogin.do";} // 로그인 체크
		if(!checkPower((String)session.getAttribute("id"), folder)) {
			model.addAttribute("alert", "권한이 없습니다.");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}
		if(FilenameFilter.nameFilter(foldername)) {
			model.addAttribute("alert", "폴더명에 포함될 수 없는 단어가 포함되어 있습니다.");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}
		// 폴더명에 포함될 수 없는 문자들 체크
		int check = 0;
		check += foldername.indexOf("\\");
		check += foldername.indexOf("/");
		check += foldername.indexOf(":");
		check += foldername.indexOf("*");
		check += foldername.indexOf("?");
		check += foldername.indexOf("\"");
		check += foldername.indexOf("<");
		check += foldername.indexOf(">");
		check += foldername.indexOf("|");
		if(check > -9) {
			model.addAttribute("alert", "\\\\, /, :, *, ?, \", <, >, | 의 문자들은 폴더 명에 사용할 수 없습니다.");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}
		// 폴더명 길이 확인
		if(foldername.length() > 90) {
			model.addAttribute("alert", "폴더명은 한글로 30글자, 영어,숫자 또는 특수문자로 90글자까지 가능합니다.");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}
		List address_ref = getAddr(folder);
		if(makeDir(foldername, address_ref)) { // 폴더 생성 완료시
			model.addAttribute("alert", "폴더 생성 완료");
			FileDTO dto = (FileDTO)address_ref.get(0);
			String owner = dto.getOwner();
			if(owner.equals(session.getAttribute("id"))) {
				model.addAttribute("location", "\"/BEngineer/beFiles/beMyList.do?folder=" + folder + "\"");
			}else {
				model.addAttribute("location", "\"/BEngineer/beFiles/beSharedList.do?folder=" + folder + "\"");
			}
			return "beFiles/alert";
		}else { // 폴더생성 오류시
			model.addAttribute("alert", "폴더를 생성하는 도충 오류가 발생했습니다.");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}
	}
	@RequestMapping("beDownload.do") // 파일 다운로드
	public ModelAndView download(String file_ref, HttpSession session) {
		if(MainBean.loginCheck(session)) {
			ModelAndView mav = new ModelAndView();
			mav.setViewName("redirect:/beMember/beLogin.do");
			return mav;
		} // 로그인 체크
		String [] files = file_ref.split(",");
		String fileaddress = "";
		File file = null;
		if(files.length == 1) {
			int filenum = 0;
			try {
				 filenum = Integer.parseInt(files[0]);
			}catch(Exception e) {
				ModelAndView mav = new ModelAndView();
				mav.setViewName("beFiles/alert");
				mav.addObject("alert", "유효하지 않은 접근입니다.");
				mav.addObject("location", "history.go(-1)");
				return mav;
			}
			List address_ref = getAddr(filenum);
			FileDTO dto = new FileDTO();
			for(int i = address_ref.size() - 1; i >= 0; i--) {
				dto = (FileDTO)address_ref.get(i);
				fileaddress += dto.getOrgname();
				if(i != 0) {
					fileaddress += "/";
				}
			}
			fileaddress = "d:/PM/BEngineer/" + fileaddress;
			if(dto.getFiletype().equals("dir")) {
				List filelist = getFilelist(fileaddress);
				if(filelist == null || filelist.size() == 0) {
					ModelAndView mav = new ModelAndView();
					mav.setViewName("beFiles/alert");
					mav.addObject("alert", "다운로드할 파일이 없습니다.");
					mav.addObject("location", "history.go(-1)");
					return mav;
				}else if(filelist.get(0) instanceof Boolean) {
					ModelAndView mav = new ModelAndView();
					mav.setViewName("beFiles/alert");
					mav.addObject("alert", (String)filelist.get(1));
					mav.addObject("location", "history.go(-1)");
					return mav;
				}
				Date time = new Date(System.currentTimeMillis());
				SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmssZ");
				String zipname = "d:/PM/BEngineer/downtemp/" + dto.getFilename() + format.format(time).substring(0, 12) + ".zip";
				file = zipFiles(fileaddress, zipname, filelist);
			}else {
				file = new File(fileaddress);
			}
			sqlSession.update("bengineer.hit", filenum);
		}else if(files.length > 1) {
			int filenum = 0;
			try {
				 filenum = Integer.parseInt(files[0]);
			}catch(Exception e) {
				ModelAndView mav = new ModelAndView();
				mav.setViewName("beFiles/alert");
				mav.addObject("alert", "유효하지 않은 접근입니다.");
				mav.addObject("location", "history.go(-1)");
				return mav;
			}
			List address_ref = getAddr(filenum);
			FileDTO dto = new FileDTO();
			for(int i = address_ref.size() - 1; i > 0; i--) {
				dto = (FileDTO)address_ref.get(i);
				fileaddress += dto.getOrgname();
				if(i != 0) {
					fileaddress += "/";
				}
			}
			fileaddress = "d:/PM/BEngineer/" + fileaddress;
			List filelist = new ArrayList();
			for(int i = 0; i < files.length; i++) {
				try {
					 filenum = Integer.parseInt(files[i]);
				}catch(Exception e) {
					ModelAndView mav = new ModelAndView();
					mav.setViewName("beFiles/alert");
					mav.addObject("alert", "유효하지 않은 접근입니다.");
					mav.addObject("location", "history.go(-1)");
					return mav;
				}
				dto = (FileDTO)sqlSession.selectOne("bengineer.getaddr", filenum);
				if(dto.getFiletype().equals("dir")) {
					List add = getFilelist(fileaddress, "/" + dto.getOrgname());
					if(add != null && !(add.get(0) instanceof Boolean)) {
						filelist.addAll(add);
					}
				}else {
					filelist.add(dto.getOrgname());
				}
				if(filelist == null || filelist.size() == 0) {
					ModelAndView mav = new ModelAndView();
					mav.setViewName("beFiles/alert");
					mav.addObject("alert", "다운로드할 파일이 없습니다.");
					mav.addObject("location", "history.go(-1)");
					return mav;
				}
			}
			Date time = new Date(System.currentTimeMillis());
			SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmssZ");
			String name = dto.getFilename();
			int index = name.lastIndexOf("."); 
			if(index != -1) {
				name = name.substring(0, index) + "외";
			}else {
				name += "외";
			}
			String zipname = "d:/PM/BEngineer/downtemp/" + name + format.format(time).substring(0, 12) + ".zip";
			file = zipFiles(fileaddress, zipname, filelist);
		}else {
			ModelAndView mav = new ModelAndView();
			mav.setViewName("beFiles/alert");
			mav.addObject("alert", "유효하지 않은 접근입니다.");
			mav.addObject("location", "history.go(-1)");
			return mav;
		}
		if(file == null) {
			ModelAndView mav = new ModelAndView();
			mav.setViewName("beFiles/alert");
			mav.addObject("alert", "다운로드 중 오류가 발생했습니다.");
			mav.addObject("location", "history.go(-1)");
			return mav;
		}
		ModelAndView mv = new ModelAndView("download","downloadFile",file);
		return mv;
	}
	@RequestMapping("changeFolderName.do") // 폴더명 변경 페이지
	public String changefolder(HttpSession session, Model model, String name, int ref, int folder) { // orgaddress는 원 폴더 명까지 포함
		if(MainBean.loginCheck(session)) {return "redirect:/beMember/beLogin.do";} // 로그인 체크
		// 폴더명에 포함될 수 없는 문자들 체크
		int check = 0;
		check += name.indexOf("\\");
		check += name.indexOf("/");
		check += name.indexOf(":");
		check += name.indexOf("*");
		check += name.indexOf("?");
		check += name.indexOf("\"");
		check += name.indexOf("<");
		check += name.indexOf(">");
		check += name.indexOf("|");
		if(check > -9) {
			model.addAttribute("alert", "\\\\, /, :, *, ?, \", <, >, | 의 문자들은 폴더 명에 사용할 수 없습니다.");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}
		// 폴더명 길이 확인
		if(name.length() > 90) {
			model.addAttribute("alert", "폴더명은 한글로 30글자, 영어,숫자 또는 특수문자로 90글자까지 가능합니다.");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}
		if(FilenameFilter.nameFilter(name)) {
			model.addAttribute("alert", "폴더명에 포함될 수 없는 단어가 포함되어 있습니다.");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}
		List address_ref = getAddr(ref);
		FileDTO dto = (FileDTO)address_ref.get(0);
		if(dto.getImportant() == -1) {
			model.addAttribute("alert", "기본 폴더의 이름은 바꿀 수 없습니다.");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}
		if(changeDirName(name, address_ref)) { // 폴더 생성 완료시
			model.addAttribute("alert", "폴더명 변경 완료");
			dto = (FileDTO)address_ref.get(0);
			String owner = dto.getOwner();
			if(owner.equals(session.getAttribute("id"))) {
				model.addAttribute("location", "\"/BEngineer/beFiles/beMyList.do?folder=" + folder + "\"");
			}else {
				model.addAttribute("location", "\"/BEngineer/beFiles/beSharedList.do?folder=" + folder + "\"");
			}
			return "beFiles/alert";
		}else { // 폴더생성 오류시
			model.addAttribute("alert", "폴더의 이름을 변경하는 도충 오류가 발생했습니다.");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}
	}
	@RequestMapping("changeFileName.do")
	public String changeFileName(HttpSession session, Model model, String name, int ref, int folder) {
		if(MainBean.loginCheck(session)) {return "redirect:/beMember/beLogin.do";} // 로그인 체크
		// 파일명 길이 확인
		if(name.length() > 90) {
			model.addAttribute("alert", "파일명은 한글로 30글자, 영어,숫자 또는 특수문자로 90글자까지 가능합니다.");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}
		if(FilenameFilter.nameFilter(name)) {
			model.addAttribute("alert", "파일명에 포함될 수 없는 단어가 포함되어 있습니다.");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}
		FileDTO dto = sqlSession.selectOne("bengineer.getaddr", ref);
		dto.setFilename(name);
		sqlSession.update("bengineer.changename", dto);
		model.addAttribute("alert", "파일명 변경 완료");
		String owner = dto.getOwner();
		if(owner.equals(session.getAttribute("id"))) {
			model.addAttribute("location", "\"/BEngineer/beFiles/beMyList.do?folder=" + folder + "\"");
		}else {
			model.addAttribute("location", "\"/BEngineer/beFiles/beSharedList.do?folder=" + folder + "\"");
		}
		return "beFiles/alert";
	}
	@RequestMapping("shareFile.do")
	public String shareFile(HttpSession session, Model model, int ref, String enddate, int rw) throws Exception {
		if(MainBean.loginCheck(session)) {return "redirect:/beMember/beLogin.do";} // 로그인 체크
		String owner = (String)session.getAttribute("id");
		FileDTO dto = sqlSession.selectOne("bengineer.getaddr", ref);
		if(!owner.equals(dto.getOwner())) {
			model.addAttribute("alert", "본인의 파일만 공유할 수 있습니다.");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}
		KeyDTO kdto = new KeyDTO();
		kdto.setFilenum(ref);
		kdto.setEnddate(Timestamp.valueOf(enddate + " 23:59:59"));
		kdto.setRw(rw);
		String keycheck = (String)sqlSession.selectOne("bengineer.keycheck", kdto);
		if(keycheck == null) {
			kdto.setShare_key(makecode(20));
			sqlSession.insert("bengineer.insertkey", kdto);
			keycheck = kdto.getShare_key();
		}
		InetAddress local = InetAddress.getLocalHost();
		model.addAttribute("alert", "공유 URL : " + local.getHostAddress() + "/BEngineer/beFiles/getSharedFile.do?share_key=" + keycheck);
		model.addAttribute("location", "history.go(-1)");
		return "beFiles/alert";
	}
	@RequestMapping("getSharedFile.do")
	public String getSharedFile(HttpSession session, Model model, String share_key) {
		if(MainBean.loginCheck(session)) {return "redirect:/beMember/beLogin.do";} // 로그인 체크
		String id = (String)session.getAttribute("id");
		KeyDTO kdto = (KeyDTO)sqlSession.selectOne("bengineer.open", share_key);
		int filenum = kdto.getFilenum();
		FileDTO dto = (FileDTO)sqlSession.selectOne("bengineer.getaddr", filenum);
		if(id.equals(dto.getOwner())) {
			model.addAttribute("alert", "니 파일입니다.");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}
		if(kdto != null) {
			ShareDTO sdto = new ShareDTO();
			sdto.setId(id);
			sdto.setShare_key(share_key);
			sqlSession.insert("bengineer.getsharedfile", sdto);
			model.addAttribute("alert", "공유되었습니다.");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}else {
			model.addAttribute("alert", "유효하지 않은 키입니다.");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}
	}
	@RequestMapping("throwToTrashcan.do")
	public String throwToTrashcan(HttpSession session, Model model, String file_ref, int folder) {
		if(MainBean.loginCheck(session)) {return "redirect:/beMember/beLogin.do";} // 로그인 체크
		String id = (String)session.getAttribute("id");
		String [] files = file_ref.split(",");
		String result = "";
		String owner = "";
		int filenum = 0;
		if(files.length == 1) {
			try {
				 filenum = Integer.parseInt(files[0]);
			}catch(Exception e) {
				model.addAttribute("alert", "유효하지 않은 접근입니다.");
				model.addAttribute("location", "history.go(-1)");
				return "beFiles/alert";
			}
			FileDTO dto = (FileDTO)sqlSession.selectOne("bengineer.getaddr", filenum);
			owner = dto.getOwner();
			if(!owner.equals(id)) {
				model.addAttribute("alert", "타인의 파일은 지울 수 없습니다.");
				model.addAttribute("location", "history.go(-1)");
				return "beFiles/alert";
			}
			if(dto.getFiletype().equals("dir")) {
				List subfiles = settrash(owner, filenum);
				TrashHolder trashcan = (TrashHolder)subfiles.remove(subfiles.size() - 1);
				List check = sqlSession.selectList("bengineer.checkall1", subfiles);
				if(check != null && check.size() != 0) {
					model.addAttribute("alert", "기본폴더는 지울 수 없습니다.");
					model.addAttribute("location", "history.go(-1)");
					return "beFiles/alert";
				}
				check = sqlSession.selectList("bengineer.checkall2", subfiles);
				if(check != null && check.size() != 0) {
					model.addAttribute("confirm", "휴지통에 넣기엔 파일이 너무 큽니다. 완전 삭제하시겠습니까?(주의 : 예를 클릭할 시 모든 파일이 완전 삭제됩니다.)");
					model.addAttribute("location", "\"/BEngineer/beFiles/deleteFile.do?folder=" + folder + "&file_ref=" + file_ref + "\"");
					return "beFiles/confirm";
				}
				List trashlist = trashcan.getTrashList();
				for(int i = 0; i < trashlist.size(); i++) {
					ziptrash(owner, (Trash)trashlist.get(i));
				}
				sqlSession.update("bengineer.throwalltotrashcan", subfiles);
				result = "파일들을 휴지통에 버렸습니다.";
			}else {
				if(dto.getFilesize() > 10 * 1024 * 1024) {
					model.addAttribute("confirm", "휴지통에 넣기엔 파일이 너무 큽니다. 완전 삭제하시겠습니까?");
					model.addAttribute("location", "\"/BEngineer/beFiles/deleteFile.do?folder=" + folder + "&file_ref=" + file_ref + "\"");
					return "beFiles/confirm";
				}else {
					TrashHolder trashcan = (TrashHolder)settrash(owner, dto.getNum()).get(1);
					ziptrash(owner, (Trash)trashcan.getTrashList().get(0));
					sqlSession.update("bengineer.throwtotrashcan", filenum);
					result = "파일을 휴지통에 버렸습니다.";
				}
			}
		}else if(files.length > 1) {
			List subfiles = new ArrayList();
			TrashHolder trashcan = new TrashHolder();
			FileDTO dto = null;
			for(int i = 0; i < files.length; i++) {
				try {
					 filenum = Integer.parseInt(files[i]);
				}catch(Exception e) {
					model.addAttribute("alert", "유효하지 않은 접근입니다.");
					model.addAttribute("location", "history.go(-1)");
					return "beFiles/alert";
				}
				List address_ref = getAddr(filenum);
				dto = (FileDTO)address_ref.get(0);
				owner = dto.getOwner();
				if(!owner.equals(id)) {
					model.addAttribute("alert", "타인의 파일은 지울 수 없습니다.");
					model.addAttribute("location", "history.go(-1)");
					return "beFiles/alert";
				}
				String fileaddress = "d:/PM/BEngineer/";
				for(int j = address_ref.size() - 1; j > 0; j--) {
					dto = (FileDTO)address_ref.get(j);
					fileaddress += dto.getOrgname();
					if(j != 1) {
						fileaddress += "/";
					}
				}
				if(dto.getFiletype().equals("dir")) {
					subfiles.addAll(settrash(owner, filenum));
					trashcan.addallTrash((TrashHolder)subfiles.remove(subfiles.size() - 1));
				}else {
					subfiles.add(filenum);
					trashcan.addTrash(fileaddress, dto.getOrgname(), dto.getNum());
				}
			}
			List check = sqlSession.selectList("bengineer.checkall1", subfiles);
			if(check != null && check.size() != 0) {
				model.addAttribute("alert", "기본폴더는 지울 수 없습니다.");
				model.addAttribute("location", "history.go(-1)");
				return "beFiles/alert";
			}
			check = sqlSession.selectList("bengineer.checkall2", subfiles);
			if(check != null && check.size() != 0) {
				model.addAttribute("confirm", "휴지통에 넣기엔 파일이 너무 큽니다. 완전 삭제하시겠습니까?(주의 : 예를 클릭할 시 모든 파일이 완전 삭제됩니다.)");
				model.addAttribute("location", "\"/BEngineer/beFiles/deleteFile.do?folder=" + folder + "&file_ref=" + file_ref + "\"");
				return "beFiles/confirm";
			}
			List trashlist = trashcan.getTrashList();
			for(int i = 0; i < trashlist.size(); i++) {
				ziptrash(owner, (Trash)trashlist.get(i));
			}
			sqlSession.update("bengineer.throwalltotrashcan", subfiles);
			result = "파일들을 휴지통에 버렸습니다.";
		}else {
			model.addAttribute("alert", "잘못된 접근입니다.");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}
		model.addAttribute("alert", result);
		if(owner.equals((String)session.getAttribute("id"))) {
			model.addAttribute("location", "\"/BEngineer/beFiles/beMyList.do?folder=" + folder + "\"");
		}else {
			model.addAttribute("location", "\"/BEngineer/beFiles/beSharedList.do?folder=" + folder + "\"");
		}
		return "beFiles/alert";
	}
	@RequestMapping("repairFile.do")
	public String repairFile(HttpSession session, Model model, String file_ref, int folder) {
		if(MainBean.loginCheck(session)) {return "redirect:/beMember/beLogin.do";} // 로그인 체크
		String [] files = file_ref.split(",");
		String result = "";
		String owner = "";
		int filenum = 0;
		List repairList = new ArrayList();
		TrashHolder trashcan = new TrashHolder();
		List moveList = new ArrayList();
		FileDTO dto = new FileDTO();
		if(files.length == 1) {
			try {
				 filenum = Integer.parseInt(files[0]);
			}catch(Exception e) {
				model.addAttribute("alert", "잘못된 접근입니다.");
				model.addAttribute("location", "history.go(-1)");
				return "beFiles/alert";
			}
			List address_ref = getTrashAddr(filenum);
			dto = (FileDTO)address_ref.get(0);
			owner = dto.getOwner();
			if(!owner.equals((String)session.getAttribute("id"))){
				model.addAttribute("alert", "권한이 없습니다.");
				model.addAttribute("location", "history.go(-1)");
				return "beFiles/alert";
			}
			if(address_ref.size() == 1) { // 기존 폴더가 아직 존재하는 경우
				repairList = settrash(owner, filenum);
				trashcan = (TrashHolder)repairList.remove(repairList.size() - 1);
			}else { // 기존 폴더도 삭제된 경우 기본 화면에 복원
				String fileaddress = "d:/PM/BEngineer/";
				fileaddress += owner;
				result += "기본 폴더에 ";
				moveList.add(filenum);
				repairList = settrash(owner, filenum, fileaddress, trashcan);
			}
			if(dto.getFiletype().equals("dir")) {
				result += "파일들을 복구하였습니다.";
			}else {
				result += "파일을 복구하였습니다.";
			}
		}else if(files.length > 1) {
			for(int i = 0; i < files.length; i++) {
				try {
					 filenum = Integer.parseInt(files[i]);
				}catch(Exception e) {
					model.addAttribute("alert", "잘못된 접근입니다.");
					model.addAttribute("location", "history.go(-1)");
					return "beFiles/alert";
				}
				List address_ref = getTrashAddr(filenum);
				dto = (FileDTO)address_ref.get(0);
				if(i == 0) {
					owner = dto.getOwner();
				}
				if(!owner.equals((String)session.getAttribute("id"))){
					model.addAttribute("alert", "권한이 없습니다.");
					model.addAttribute("location", "history.go(-1)");
					return "beFiles/alert";
				}
				if(address_ref.size() == 1) { // 기존 폴더가 아직 존재하는 경우
					repairList.addAll(settrash(owner, filenum));
					trashcan.addallTrash((TrashHolder)repairList.remove(repairList.size() - 1));
				}else { // 기존 폴더도 삭제된 경우 기본 화면에 복원
					String fileaddress = "d:/PM/BEngineer/";
					fileaddress += owner;
					moveList.add(filenum);
					repairList.addAll(settrash(owner, filenum, fileaddress, trashcan));
				}
				result = "파일들을 복구하였습니다.";
			}
		}else {
			model.addAttribute("alert", "잘못된 접근입니다.");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}
		List trashlist = trashcan.getTrashList();
		for(int i = trashlist.size() - 1; i >= 0; i--) {
			Trash trash = (Trash)trashlist.get(i);
			String fileaddress = trash.getPath();
			String name = trash.getName();
			if(name.lastIndexOf(".") > -1) {
				String rename = rename(fileaddress, name);
				if(!rename.equals(name)) {
					dto.setNum(trash.getNum());
					dto.setOrgname(rename);
					sqlSession.update("bengineer.newname", dto);
				}
				unzipFile(fileaddress + "/" + rename, "d:/PM/BEngineer/" + owner + "/beTrashcan/" + trash.getNum() + ".zip");
			}else {
				File check = new File(fileaddress + "/" + name);
				if(check.exists()) {
					dto = (FileDTO)sqlSession.selectOne("bengineer.getaddr", trash.getNum());
					int num = (int)sqlSession.selectOne("bengineer.getfoldernum", dto);
					dto.setFolder_ref(num);
					sqlSession.update("bengineer.changeref", dto);
					dto.setFolder_ref(dto.getNum());
					sqlSession.delete("bengineer.deletefile", dto);
				}else {
					check.mkdir();
				}
			}
			sqlSession.delete("bengineer.deletetrash", trash.getNum());
		}
		ListDTO ldto = new ListDTO();
		dto.setOwner(owner);
		dto.setOrgname(owner);
		int folder_ref = sqlSession.selectOne("bengineer.getparentnum", dto);
		ldto.setNum(folder_ref);
		ldto.setList(moveList);
		sqlSession.update("bengineer.repairfiles", repairList);
		sqlSession.delete("bengineer.deletetrashes", repairList);
		if(moveList.size() > 0) {
			sqlSession.update("bengineer.movefiles", ldto);
		}
		model.addAttribute("alert", result);
		model.addAttribute("location", "\"/BEngineer/beFiles/beTrashcan.do?folder=" + folder + "\"");
		return "beFiles/alert";
	}
	@RequestMapping("deleteFile.do")
	public String deleteFile(HttpSession session, Model model, String file_ref, int folder) {
		if(MainBean.loginCheck(session)) {return "redirect:/beMember/beLogin.do";} // 로그인 체크
		String [] files = file_ref.split(",");
		String result = "";
		String owner = "";
		String location = "";
		int filenum = 0;
		List deleteList = new ArrayList();
		TrashHolder trashcan = new TrashHolder();
		FileDTO dto = new FileDTO();
		if(files.length == 1) {
			try {
				 filenum = Integer.parseInt(files[0]);
			}catch(Exception e) {
				model.addAttribute("alert", "잘못된 접근입니다.");
				model.addAttribute("location", "history.go(-1)");
				return "beFiles/alert";
			}
			if(!checkPower((String)session.getAttribute("id"), filenum)) { // 권한 체크
				model.addAttribute("alert", "권한이 없습니다.");
				model.addAttribute("location", "history.go(-1)");
				return "beFiles/alert";
			}
			List address_ref = getAddr(filenum);
			dto = (FileDTO)address_ref.get(0);
			owner = dto.getOwner();
			if(dto.getImportant() == 1) { // 휴지통에서 삭제하는 경우
				String fileaddress = "trashcan/";
				deleteList = settrash(owner, filenum, fileaddress, trashcan);
				location = "\"/BEngineer/beFiles/beTrashcan.do?folder=" + folder + "\"";
			}else { // 내 파일에서 바로 삭제하는 경우
				deleteList = settrash(owner, filenum);
				trashcan = (TrashHolder)deleteList.remove(deleteList.size() - 1);
				location = "\"/BEngineer/beFiles/beMyList.do?folder=" + folder + "\"";
			}
			result = "파일/폴더를 완전히 삭제하였습니다.";
		}else if(files.length > 1) {
			for(int i = 0; i < files.length; i++) {
				try {
					 filenum = Integer.parseInt(files[i]);
				}catch(Exception e) {
					model.addAttribute("alert", "잘못된 접근입니다.");
					model.addAttribute("location", "history.go(-1)");
					return "beFiles/alert";
				}
				if(!checkPower((String)session.getAttribute("id"), filenum)) { // 권한 체크
					model.addAttribute("alert", "권한이 없습니다.");
					model.addAttribute("location", "history.go(-1)");
					return "beFiles/alert";
				}
				List address_ref = getAddr(filenum);
				dto = (FileDTO)address_ref.get(0);
				if(i == 0) {
					owner = dto.getOwner();
				}
				if(dto.getImportant() == 1) { // 휴지통에서 삭제하는 경우
					String fileaddress = "trashcan/";
					fileaddress += owner;
					deleteList.addAll(settrash(owner, filenum, fileaddress, trashcan));
					location = "\"/BEngineer/beFiles/beTrashcan.do?folder=" + folder + "\"";
				}else { // 내 파일에서 바로 삭제하는 경우
					deleteList.addAll(settrash(owner, filenum));
					trashcan.addallTrash((TrashHolder)deleteList.remove(deleteList.size() - 1));
					location = "\"/BEngineer/beFiles/beMyList.do?folder=" + folder + "\"";
				}
				result = "파일들을 완전히 삭제하였습니다.";
			}
		}else {
			model.addAttribute("alert", "잘못된 접근입니다.");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}
		List trashlist = trashcan.getTrashList();
		for(int i = 0; i < trashlist.size(); i++) {
			Trash trash = (Trash)trashlist.get(i);
			String fileaddress = trash.getPath();
			if(fileaddress.startsWith("trashcan/")) {
				File file = new File("d:/PM/BEngineer/" + owner + "/betrashcan/" + trash.getNum() + ".zip");
				file.delete();
			}else {
				String name = trash.getName();
				File file = new File(fileaddress + "/" + name);
				file.delete();
			}
			sqlSession.delete("bengineer.deletetrash", trash.getNum());
		}
		sqlSession.delete("bengineer.deletefiles", deleteList);
		sqlSession.delete("bengineer.deletetrashes", deleteList);
		model.addAttribute("alert", result);
		model.addAttribute("location", location);
		return "beFiles/alert";
	}
	@RequestMapping("lookSharedPeople.do")
	public String lookSharedPeople(HttpSession session, Model model, int file) {
		List idlist = getSharePeopleAddr(file);
		String result = "";
		for(int i = 0; i < idlist.size(); i++) {
			KeyDTO kdto = (KeyDTO)idlist.get(i);
			String nickname = sqlSession.selectOne("bengineer.getnickname", kdto.getShare_key());
			if(nickname == null) {
				continue;
			}
			result += nickname;
			result += "(" + kdto.getEnddate() + "까지 ";
			if(kdto.getRw() == 0) {
				result += "읽기";
			}else {
				result += "쓰기";
			}
			result += " 가능)";
			if(i != idlist.size() - 1) {
				result += "\\n\\r";
			}
		}
		if(result.equals("")) {
			result = "공유중인 사람이 없습니다.";
		}
		model.addAttribute("alert", result);
		model.addAttribute("location", "history.go(-1)");
		return "beFiles/alert";
	}
	@RequestMapping("writeText.do")
	public String writeText(HttpSession session, Model model, int folder, String filename, String orgname, String content) {
		if(MainBean.loginCheck(session)) {return "redirect:/beMember/beLogin.do";} // 로그인 체크
		String id = (String)session.getAttribute("id");
		FileDTO dto = new FileDTO();
		int ret = folder;
		if(folder <= 0) {
			dto.setOwner(id);
			dto.setOrgname(id);
			folder = sqlSession.selectOne("bengineer.getparentnum", dto);
		}
		if(!checkPower(id, folder)) {
			model.addAttribute("alert", "권한이 없습니다.");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}
		if(FilenameFilter.nameFilter(orgname, filename)) {
			model.addAttribute("alert", "파일명에 포함될 수 없는 단어가 포함되어 있습니다.");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}
		List address_ref = getAddr(folder);
		dto = (FileDTO)address_ref.get(0);
		String owner = dto.getOwner();
		if(dto.getImportant() == -1) {
			String foldername = dto.getOrgname();
			if(!foldername.equals("document") && !foldername.equals(owner)) {
				model.addAttribute("alert", "파일을 생성할 수 없는 위치입니다.");
				model.addAttribute("location", "history.go(-1)");
				return "beFiles/alert";
			}
		}
		String address = "d:/PM/BEngineer/";
		for(int i = address_ref.size() - 1; i >= 0; i--) {
			dto = (FileDTO)address_ref.get(i);
			address += dto.getOrgname() + "/";
		}
		orgname += ".txt";
		long orgsize = 0;
		File textfile = new File(address + orgname);
		if(textfile.exists()) {
			orgsize = textfile.length();
			textfile.delete();
		}
		dto = writeTextFile(address, orgname, content);
		dto.setOwner(owner);
		dto.setFilename(filename);
		dto.setFiletype(".txt");
		dto.setFolder_ref(folder);
		if(orgsize == 0) {
			sqlSession.insert("bengineer.upload", dto);
			model.addAttribute("alert", "텍스트 파일을 생성하였습니다.");
		}else {
			sqlSession.update("bengineer.updatefile", dto);
			model.addAttribute("alert", "텍스트 파일을 수정하였습니다.");
		}
		if(owner.equals((String)session.getAttribute("id"))) {
			model.addAttribute("location", "\"/BEngineer/beFiles/beMyList.do?folder=" + ret + "\"");
		}else {
			model.addAttribute("location", "\"/BEngineer/beFiles/beSharedList.do?folder=" + ret + "\"");
		}
		return "beFiles/alert";
	}
	@RequestMapping("rewriteText.do")
	public String rewriteText(HttpSession session, Model model, int filenum) {
		if(MainBean.loginCheck(session)) {return "redirect:/beMember/beLogin.do";} // 로그인 체크
		if(!checkPower((String)session.getAttribute("id"), filenum)) {
			model.addAttribute("alert", "권한이 없습니다.");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}
		List address_ref = getAddr(filenum);
		FileDTO dto = (FileDTO)address_ref.get(0);
		String owner = dto.getOwner();
		String address = "d:/PM/BEngineer/";
		for(int i = address_ref.size() - 1; i >= 0; i--) {
			dto = (FileDTO)address_ref.get(i);
			address += dto.getOrgname();
			if(i != 0) {
				address += "/";
			}
		}
		File text = new File(address);
		String content = "";
		FileReader reader = null;
		BufferedReader buffered = null;
		try {
			reader = new FileReader(text);
			buffered = new BufferedReader(reader);
			while(buffered.ready()) {
				content += buffered.readLine() + "\\r\\n";
			}
			buffered.close();
			reader.close();
		}catch(Exception e) {e.printStackTrace();}finally {
			if(reader != null) {try {reader.close();}catch(Exception e){}}
			if(buffered != null) {try {buffered.close();}catch(Exception e){}}
		}
		model.addAttribute("textcontent", content);
		model.addAttribute("textname", dto.getFilename());
		model.addAttribute("textorgname", dto.getOrgname().substring(0, dto.getOrgname().lastIndexOf(".")));
		return "forward:/beFiles/beMyList.do?folder=0";
	}
	@RequestMapping("unshare.do")
	public String unshare(HttpSession session, Model model, int file_ref, String nickname, int folder) {
		if(MainBean.loginCheck(session)) {return "redirect:/beMember/beLogin.do";} // 로그인 체크
		FileDTO dto = (FileDTO)sqlSession.selectOne("bengineer.getaddr", file_ref);
		String owner = dto.getOwner();
		String id = (String)session.getAttribute("id");
		if(owner.equals(id)){
			if(nickname == null || nickname.equals("")) {
				sqlSession.delete("bengineer.unshareall", file_ref);
				model.addAttribute("alert", "파일의 모든 공유를 해제하였습니다.");
				model.addAttribute("location", "\"/BEngineer/beFiles/beMyList.do?folder=" + folder + "\"");
			}else {
				id = sqlSession.selectOne("bengineer.getid", nickname);
				if(id == null) {
					model.addAttribute("alert", "존재하지 않는 회원입니다.");
					model.addAttribute("location", "\"/BEngineer/beFiles/beMyList.do?folder=" + folder + "\"");
					return "beFiles/alert";
				}
				ShareDTO sdto = new ShareDTO();
				sdto.setId(id);
				sdto.setNum(file_ref);
				sqlSession.delete("bengineer.unshare", sdto);
				model.addAttribute("alert", "파일의 공유를 해제하였습니다.");
				model.addAttribute("location", "\"/BEngineer/beFiles/beMyList.do?folder=" + folder + "\"");
			}
		}else {
			ShareDTO sdto = new ShareDTO();
			sdto.setId(id);
			sdto.setNum(file_ref);
			sqlSession.delete("bengineer.unshare", sdto);
			model.addAttribute("alert", "파일의 공유를 해제하였습니다.");
			model.addAttribute("location", "\"/BEngineer/beFiles/beSharedList.do?folder=" + folder + "\"");
		}
		return "beFiles/alert";
	}
	@RequestMapping("changeowner.do")
	public String changeowner(HttpSession session, Model model, int file_ref, String nickname, int folder) {
		if(MainBean.loginCheck(session)) {return "redirect:/beMember/beLogin.do";} // 로그인 체크
		FileDTO dto = (FileDTO)sqlSession.selectOne("bengineer.getaddr", file_ref);
		if(dto.getImportant() == -1){
			model.addAttribute("alert", "기본 폴더는 넘겨줄 수 없습니다.");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}
		String owner = dto.getOwner();
		String id = (String)session.getAttribute("id");
		if(!owner.equals(id)){
			model.addAttribute("alert", "본인의 파일만 넘겨줄 수 있습니다.");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}
		String newowner = sqlSession.selectOne("bengineer.getid", nickname);
		if(newowner == null) {
			model.addAttribute("alert", "존재하지 않는 회원입니다.");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}else if(newowner.equals(owner)){
			model.addAttribute("alert", "너입니다.");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}
		List address_ref = getAddr(file_ref);
		String orgaddress = "d:/PM/BEngineer/";
		for(int i = address_ref.size() - 1; i >= 0; i--) {
			dto = (FileDTO)address_ref.get(i);
			orgaddress += dto.getOrgname();
			if(i != 0) {
				orgaddress += "/";
			}
		}
		String newaddress = "d:/PM/BEngineer/" + newowner + "/" + dto.getOrgname();
		FileBean2 fb2 = new FileBean2();
		if(fb2.nioFilemove(orgaddress, newaddress)) {
			dto.setOwner(newowner);
			dto.setOrgname(newowner);
			dto.setNum(file_ref);
			int folder_ref = sqlSession.selectOne("bengineer.getparentnum", dto);
			dto.setFolder_ref(folder_ref);
			sqlSession.update("bengineer.changeref", dto);
			List numlist = subfiles(file_ref);
			ListDTO ldto = new ListDTO();
			ldto.setList(numlist);
			ldto.setString(newowner);
			sqlSession.update("bengineer.changeowners", ldto);
			model.addAttribute("alert", "파일을 " + nickname + "에게 넘겨주었습니다.");
			model.addAttribute("location", "\"/BEngineer/beFiles/beMyList.do?folder=" + folder + "\"");
		}else {
			model.addAttribute("alert", "파일을  넘겨주는 데에 실패했습니다.");
			model.addAttribute("location", "history.go(-1)");
		}
		return "beFiles/alert";
	}
	public String makecode(int num) {
		String code = "";
		for(int i = 0; i < num; i++) {
			int k = (int)(Math.random() * 62);
			char c = '0';
			if(k < 10) {
				c = (char)(k + 48);
			}else if(k < 36) {
				c = (char)(k + 55);
			}else {
				c = (char)(k + 61);
			}
			code += c;
		}
		int check = sqlSession.selectOne("bengineer.checksharekey", code);
		if(check > 0) {code = makecode(num);}
		return code;
	}
	private boolean makeBaseDir(String name, List address_ref) { // 기본폴더생성용 메서드 성공시 true
		FileDTO dto = new FileDTO();
		FileDTO ref = (FileDTO)address_ref.get(0);
		dto.setFolder_ref(ref.getNum());
		dto.setOwner(ref.getOwner());
		dto.setFilename(name);
		dto.setOrgname(name);
		sqlSession.insert("bengineer.makebasedir", dto);
		String address = "";
		for(int i = 0; i < address_ref.size(); i++) {
			ref = (FileDTO)address_ref.get(i);
			address = "/" + ref.getOrgname() + address;
		}
		File file = new File("d:/PM/BEngineer" + address + "/" + name);
		return file.mkdirs();
	}
	private boolean makeDir(String name, List address_ref) { // 폴더생성용 메서드 성공시 true
		FileDTO dto = new FileDTO();
		FileDTO ref = (FileDTO)address_ref.get(0);
		dto.setFolder_ref(ref.getNum());
		dto.setOwner(ref.getOwner());
		dto.setFilename(name);
		dto.setOrgname(name);
		sqlSession.insert("bengineer.makedir", dto);
		String address = "";
		for(int i = 0; i < address_ref.size(); i++) {
			ref = (FileDTO)address_ref.get(i);
			address = "/" + ref.getOrgname() + address;
		}
		File file = new File("d:/PM/BEngineer" + address + "/" + name);
		return file.mkdirs();
	}
	public List getAddr(int folder_ref) {
		List address = new ArrayList();
		return getAddr(address, folder_ref);
	}
	private List getAddr(List address, int folder_ref) { // 폴더 실 주소 확인을 위한 메서드
		FileDTO folder = (FileDTO)sqlSession.selectOne("bengineer.getaddr", folder_ref);
		if(folder == null) {return null;}
		address.add(folder);
		folder_ref = folder.getFolder_ref();
		if(folder_ref == 0) {
			return address;
		}else {
			return getAddr(address, folder_ref);
		}
	}
	public List getTrashAddr(int folder_ref) {
		List address = new ArrayList();
		return getTrashAddr(address, folder_ref);
	}
	private List getTrashAddr(List address, int folder_ref) { // 폴더 실 주소 확인을 위한 메서드
		FileDTO folder = (FileDTO)sqlSession.selectOne("bengineer.getaddr", folder_ref);
		if(folder == null) {return null;}
		if(folder.getImportant() == 1) {
			address.add(folder);
			folder_ref = folder.getFolder_ref();
			if(folder_ref == 0) {
				return address;
			}else {
				return getTrashAddr(address, folder_ref);
			}
		}else {
			return address;
		}
	}
	public List getShareAddr(int folder_ref, String id) {
		List address = new ArrayList();
		return getShareAddr(address, folder_ref, id);
	}
	private List getShareAddr(List address, int folder_ref, String id) { // 폴더 실 주소 확인을 위한 메서드
		FileDTO folder = (FileDTO)sqlSession.selectOne("bengineer.getaddr", folder_ref);
		if(folder == null) {return null;}
		address.add(folder);
		ShareDTO sdto = new ShareDTO();
		sdto.setId(id);
		sdto.setNum(folder_ref);
		List keylist = sqlSession.selectList("bengineer.getkey", sdto);
		KeyDTO kdto = null;
		if(keylist.size() > 1) {
			for(int i = 0; i < keylist.size(); i++) {
				KeyDTO temp = (KeyDTO)keylist.get(i);
				if(kdto == null || temp.getRw() > kdto.getRw()) {
					kdto = temp;
				}
			}
		}else {
			kdto = (KeyDTO)keylist.get(0);
		}
		if(kdto != null) {
			address.add(kdto);
			return address;
		}else {
			if(folder_ref == 0) {
				return null;
			}else {
				folder_ref = folder.getFolder_ref();
				return getShareAddr(address, folder_ref, id);
			}
		}
	}
	private List getSharePeopleAddr(int file) {
		List result = new ArrayList();
		FileDTO dto = (FileDTO)sqlSession.selectOne("bengineer.getaddr", file);
		result.addAll(sqlSession.selectList("bengineer.getsharedpeople", file));
		if(dto.getFolder_ref() != 0) {
			result.addAll(getSharePeopleAddr(dto.getFolder_ref()));
		}
		return result;
	}
	private boolean checkPower(String id, int folder_ref) {
		FileDTO dto = (FileDTO)sqlSession.selectOne("bengineer.getaddr", folder_ref);
		if(dto != null) {if(id.equals(dto.getOwner())) {return true;}}
		List address_ref = getShareAddr(folder_ref, id);
		if(address_ref == null) {return false;}
		KeyDTO kdto = (KeyDTO)address_ref.get(address_ref.size() - 1);
		if(kdto.getRw() == 0) {return false;}else {return true;}
	}
	public boolean changeIdDirName(String orgId, String newId) {
		FileDTO dto = new FileDTO();
		dto.setOwner(orgId);
		dto.setOrgname(orgId);
		int num = sqlSession.selectOne("bengineer.getparentnum", dto);
		List address_ref = getAddr(num);
		dto = (FileDTO)address_ref.get(0);
		return changeDirName(dto.getFilename(), newId, address_ref);
	}
	private boolean changeDirName(String foldername, List address_ref) { // 폴더명 바꾸기 메서드 성공시 true, address는 폴더경로에 /까지
		return changeDirName(foldername, foldername, address_ref);
	}
	private boolean changeDirName(String foldername, String orgname, List address_ref) { // 폴더명 바꾸기 메서드 성공시 true, address는 폴더경로에 /까지
		FileDTO dto = new FileDTO();
		String folderaddress = "";
		String newaddress = "";
		for(int i = address_ref.size() - 1; i >= 0; i--) {
			dto = (FileDTO)address_ref.get(i);
			folderaddress += dto.getOrgname();
			if(i != 0) {
				newaddress += dto.getOrgname();
				folderaddress += "/";
			}
			newaddress += "/";
		}
		newaddress += foldername;
		dto.setFilename(foldername);
		dto.setOrgname(orgname);
		sqlSession.update("bengineer.changename", dto);
		File orgfolder = new File("d:/PM/BEngineer/" + folderaddress);
		File newfolder = new File("d:/PM/BEngineer/" + newaddress);
		return orgfolder.renameTo(newfolder);
	}
	// 파일타입 확인용 메서드
	public String checkFile(String filetype) { // 파일타입 확인용 메서드
		String result = null;
		ImagetypeFilter image = new ImagetypeFilter();
		MusictypeFilter music = new MusictypeFilter();
		VideotypeFilter video = new VideotypeFilter();
		DocumenttypeFilter document = new DocumenttypeFilter();
		EtctypeFilter etc = new EtctypeFilter();
		if(image.typeFilter(filetype)) {
			result = "image";
		}else if(music.typeFilter(filetype)) {
			result = "music";
		}else if(video.typeFilter(filetype)) {
			result = "video";
		}else if(document.typeFilter(filetype)) {
			result = "document";
		}else if(etc.typeFilter(filetype)) {
			result = "etc";
		}else {
			result = "none";
		}
		return result;
	}
	private File zipFiles(String path, String zipName, List files) { // 파일들 압축용 메서드 path는 압축파일을 만들 실제 경로, files는 압축할 파일들의 path 기준의 상대 경로 
		int size = 1024;
		byte[] buf = new byte[size];
		FileInputStream fis = null;
		FileOutputStream fos = null;
		ZipArchiveOutputStream zos = null;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		File file = null;
		try {
			fos = new FileOutputStream(zipName);
			bos = new BufferedOutputStream(fos);
			zos = new ZipArchiveOutputStream(bos);
			for(int i = 0; i < files.size(); i++) {
				zos.setEncoding("UTF-8");
				String filename = (String)files.get(i);
				if(filename.indexOf(".") != -1) {
					zos.putArchiveEntry(new ZipArchiveEntry(filename));
					fis = new FileInputStream(path + "/" + files.get(i));
					bis = new BufferedInputStream(fis, size);
					for(int j = 0; j != -1; j = bis.read(buf, 0, size)) {
						zos.write(buf, 0, j);
					}
					bis.close();
					fis.close();
				}else {
					zos.putArchiveEntry(new ZipArchiveEntry(filename + "/"));
				}
				zos.closeArchiveEntry();
			}
			zos.close();
			bos.close();
			fos.close();
			file = new File(zipName);
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			if(bis != null) {try{bis.close();}catch(IOException i) {}}
			if(fis != null) {try{fis.close();}catch(IOException i) {}}
			if(zos != null) {try{zos.close();}catch(IOException i) {}}
			if(bos != null) {try{bos.close();}catch(IOException i) {}}
			if(fos != null) {try{fos.close();}catch(IOException i) {}}
		}
		return file;
	}
	private boolean unzipFile(String path, String zipName) { // 파일들 압축용 메서드 path는 압축파일을 만들 실제 경로, files는 압축할 파일들의 path 기준의 상대 경로
		boolean result = false;
		int size = 1024;
		byte[] buf = new byte[size];
		FileInputStream fis = null;
		FileOutputStream fos = null;
		ZipArchiveInputStream zis = null;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		File file = null;
		ZipArchiveEntry zipEntry = null;
		try {
			fis = new FileInputStream(zipName);
			bis = new BufferedInputStream(fis);
			zis = new ZipArchiveInputStream(bis);
			zipEntry = zis.getNextZipEntry();
			fos = new FileOutputStream(path);
			bos = new BufferedOutputStream(fos);
			for(int j = 0; j != -1; j = zis.read(buf, 0, size)) {
				bos.write(buf, 0, j);
			}
			bos.close();
			fos.close();
			zis.close();
			bis.close();
			fis.close();
			result = true;
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			if(bos != null) {try{bos.close();}catch(IOException i) {}}
			if(fos != null) {try{fos.close();}catch(IOException i) {}}
			if(zis != null) {try{zis.close();}catch(IOException i) {}}
			if(bis != null) {try{bis.close();}catch(IOException i) {}}
			if(fis != null) {try{fis.close();}catch(IOException i) {}}
		}
		File trash = new File(zipName);
		trash.delete();
		return result;
	}
	private FileDTO writeTextFile(String address, String orgname, String content) {
		FileDTO dto = new FileDTO();
		dto.setOrgname(orgname);
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		try {
			fos = new FileOutputStream(address + orgname);
			bos = new BufferedOutputStream(fos);
			byte [] contents = content.getBytes();
			bos.write(contents);
			bos.close();
			fos.close();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(bos != null) {try {bos.close();}catch(Exception e) {}}
			if(fos != null) {try {fos.close();}catch(Exception e) {}}
		}
		File newFile = new File(address + orgname);
		dto.setFilesize(newFile.length());
		return dto;
	}
	private List getFilelist(String dirPath) {return getFilelist(dirPath, "");}
	private List getFilelist(String dirPath, String pre) { // 폴더 내의 모든 파일의 상대 주소를 리스트로 돌려주는 메서드
		List result = new ArrayList();
		File file = new File(dirPath + pre);
		if(!file.exists()) {
			result.add(false);
			result.add("폴더가 존재하지 않습니다.");
			return result;
		}
		if(!file.isDirectory()) {
			result.add(false);
			result.add("폴더가 존재하지 않습니다.");
			return result;
		}
		String [] names = file.list();
		if(names.length == 0) {
			result.add(pre);
			return result;
		}
		for(int i = 0; i < names.length; i++) {
			String path = dirPath + pre + "/" + names[i];
			File files = new File(path);
			if(files.isDirectory()) {
				List add = getFilelist(dirPath, pre + "/" + names[i]);
				if(add != null && !(add.get(0) instanceof Boolean)) {
					result.addAll(add);
				}
			}else {
				result.add(pre + "/" + names[i]);
			}
		}
		return result;
	}
	private List subfiles(int folder) {
		List result = new ArrayList();
		FileDTO dto = (FileDTO)sqlSession.selectOne("bengineer.getaddr", folder);
		if(dto.getFiletype().equals("dir")) {
			List files = sqlSession.selectList("bengineer.getfiles", folder);
			int filenum = 0;
			if(files != null && files.size() > 0) {
				for(int i = 0; i < files.size(); i++) {
					dto = (FileDTO)files.get(i);
					filenum = dto.getNum();
					List add = subfiles(filenum);
					if(add != null && add.size() != 0) {
						result.addAll(add);
					}
				}
			}
			result.add(folder);
		}else {
			result.add(dto.getNum());
		}
		return result;
	}
	private List settrash(String id, int folder) {
		List address_ref = getAddr(folder);
		FileDTO dto = new FileDTO();
		String fileaddress = "d:/PM/BEngineer/";
		for(int i = address_ref.size() - 1; i > 0; i--) {
			dto = (FileDTO)address_ref.get(i);
			fileaddress += dto.getOrgname();
			if(i != 0) {
				fileaddress += "/";
			}
		}
		TrashHolder trashcan = new TrashHolder();
		List result = settrash(id, folder, fileaddress, trashcan);
		result.add(trashcan);
		return result;
	}
	private List settrash(String id, int folder, String path, TrashHolder trashcan) {
		List result = new ArrayList();
		FileDTO dto = (FileDTO)sqlSession.selectOne("bengineer.getaddr", folder);
		if(dto.getFiletype().equals("dir")) {
			List files = sqlSession.selectList("bengineer.gettrashes", folder);
			int filenum = 0;
			String foldername = dto.getOrgname();
			if(files != null && files.size() > 0) {
				for(int i = 0; i < files.size(); i++) {
					dto = (FileDTO)files.get(i);
					filenum = dto.getNum();
					List add = settrash(id, filenum, path + "/" + foldername, trashcan);
					if(add != null && add.size() != 0) {
						result.addAll(add);
					}
				}
			}
			trashcan.addTrash(path, foldername, folder);
			result.add(folder);
		}else {
			trashcan.addTrash(path, dto.getOrgname(), dto.getNum());
			result.add(dto.getNum());
		}
		return result;
	}
	private void ziptrash(String id, Trash trash) {
		String path = trash.getPath();
		String filename = trash.getName();
		int filenum = trash.getNum();
		String canpath = "d:/PM/BEngineer/" + id + "/beTrashcan";
		File trashcan = new File(canpath);
		if(!trashcan.exists()) {trashcan.mkdirs();}
		File trashfile = new File(path + "/" + filename);
		if(!trashfile.isDirectory()) {
			List file = new ArrayList();
			file.add(filename);
			zipFiles(path, canpath + "/" + filenum + ".zip", file);
		}
		trashfile.delete();
		sqlSession.insert("bengineer.inserttrash", filenum);
	}
	private String rename(String fileaddress, String name) {
		String path = fileaddress + "/" + name;
		File check = new File(path);
		int num = 0;
		int index = name.lastIndexOf(".");
		while(check.exists()) {
			num++;
			path = fileaddress + "/" + name.substring(0, index) + num + name.substring(index);
			check = new File(path);
		}
		if(num != 0) {
			name = name.substring(0, index) + num + name.substring(index);
		}
		return name;
	}
	public void uploadsize(long filesize, List address_ref) {
		ListDTO ldto = new ListDTO();
		ldto.setLongnum(filesize);
		ldto.setList(address_ref);
		sqlSession.update("bengineer.uploadsize", ldto);
	}
}
