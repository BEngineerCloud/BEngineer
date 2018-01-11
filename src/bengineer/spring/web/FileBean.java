package bengineer.spring.web;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/beFiles/")
public class FileBean {
	@Autowired
	private SqlSessionTemplate sqlSession = null;
	@RequestMapping("beMyList.do") // 내 파일 보기
	public String myFile(HttpSession session, Model model, int folder) {
		if(MainBean.loginCheck(session)) {return "redirect:/beMember/beLogin.do";} // 로그인 세션 없을 시 리디렉트
		String owner = (String)session.getAttribute("nickname");
		File file = new File("d:/PM/BEngineer/" + owner);
		FileDTO dto = new FileDTO();
		dto.setOwner(owner);
		int folder_ref = 0;
		List address_ref = null;
		if(file.exists()) {
			if(folder == 0) {
				dto.setFilename(owner);
				dto.setFolder_ref(0);
				folder_ref = (int)sqlSession.selectOne("bengineer.getref", dto); // 폴더값이 안 들어왔을 경우 닉네임과 같은 이름의 기본폴더로 이동
				address_ref = getAddr(folder_ref);
			}else {
				folder_ref = folder;
				address_ref = getAddr(folder_ref);
			}
		}else { // 닉네임 폴더가 없을 때 닉네임 폴더 밑 기본폴더 생성 
			dto.setFilename(owner);
			dto.setFolder_ref(0);
			sqlSession.insert("bengineer.makedir", dto);
			folder_ref = (int)sqlSession.selectOne("bengineer.getref", dto);
			address_ref = getAddr(folder_ref);
			boolean mkdirch = true;
			if(!makeDir("image", address_ref)) {mkdirch = false;}
			if(!makeDir("video", address_ref)) {mkdirch = false;}
			if(!makeDir("music", address_ref)) {mkdirch = false;}
			if(!makeDir("document", address_ref)) {mkdirch = false;}
			if(!makeDir("etc", address_ref)) {mkdirch = false;}
			if(!mkdirch) { // 하나라도 오류 발생시
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
			folderaddress.add(owner);
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
		model.addAttribute("folder_ref", folder_ref);
		model.addAttribute("write", true);
		return "beFiles/beList";
	}
	@RequestMapping("beSharedList.do") // 내 파일 보기
	public String shareFile(HttpSession session, Model model, int folder) {
		if(MainBean.loginCheck(session)) {return "redirect:/beMember/beLogin.do";} // 로그인 세션 없을 시 리디렉트
		String nickname = (String)session.getAttribute("nickname");
		if(folder == 0) {
			List filelist = sqlSession.selectList("bengineer.mysharedfile", nickname);
			model.addAttribute("list", filelist);
			List folderaddress = new ArrayList(); // 폴더 경로를 하나씩 저장하기 위한 리스트
			List orgaddress = new ArrayList(); // 폴더주소에 저장된 각각의 폴더에 대한 전체 경로를 하나씩 저장하기 위한 리스트
			folderaddress.add("내 공유 파일");
			orgaddress.add(0);
			model.addAttribute("folderaddress", folderaddress);
			model.addAttribute("orgaddress", orgaddress);
			model.addAttribute("folder_ref", folder);
			model.addAttribute("write", false);
			return "beFiles/beSharedList";
		}else {
			List address_ref = getShareAddr(folder, nickname);
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
			model.addAttribute("enddate", kdto.getEnddate());
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
			model.addAttribute("folderaddress", folderaddress);
			model.addAttribute("orgaddress", orgaddress);
			model.addAttribute("folder_ref", folder);
			return "beFiles/beSharedList";
		}
	}
	@RequestMapping(value="fileupload.do", method=RequestMethod.POST) // 업로드 페이지
	public String upload(MultipartHttpServletRequest multi, int folder, String filename, HttpSession session, Model model) {
		if(MainBean.loginCheck(session)) {return "redirect:/beMember/beLogin.do";} // 비 로그인 상태시 로그인 창으로 리디렉트
		try {
			MultipartFile mf = multi.getFile("save");
			String orgname = mf.getOriginalFilename();
			String owner = (String)session.getAttribute("nickname");
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
			boolean typech = true;
			List address_ref = getAddr(folder);
			String fileaddress = "";
			FileDTO dto = new FileDTO();
			for(int i = address_ref.size() - 1; i >= 0; i--) {
				dto = (FileDTO)address_ref.get(i);
				fileaddress += dto.getFilename() + "/";
			}
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
				copy.delete();
				mf.transferTo(copy);
				long filesize = copy.length();
				dto.setFilesize(filesize);
				sqlSession.update("bengineer.updatefile", dto);
				model.addAttribute("alert", "파일 덮어쓰기 완료");
			}else {
				dto.setFiletype(filetype);
				mf.transferTo(copy);
				long filesize = copy.length();
				dto.setFilesize(filesize);
				sqlSession.insert("bengineer.upload", dto);
				model.addAttribute("alert", "파일 업로드 완료");
			}
			model.addAttribute("location", "\"/BEngineer/beFiles/beMyList.do?folder=" + folder + "\"");
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
		String owner = (String)session.getAttribute("nickname");
		if(makeDir(foldername, address_ref)) { // 폴더 생성 완료시
			model.addAttribute("alert", "폴더 생성 완료");
			model.addAttribute("location", "\"/BEngineer/beFiles/beMyList.do?folder=" + folder + "\"");
			return "beFiles/alert";
		}else { // 폴더생성 오류시
			model.addAttribute("alert", "폴더를 생성하는 도충 오류가 발생했습니다.");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}
	}
	@RequestMapping("beDownload.do") // 파일 다운로드
	public ModelAndView download(int file_ref) {
		List address_ref = getAddr(file_ref);
		String fileaddress = "";
		FileDTO dto = new FileDTO();
		for(int i = address_ref.size() - 1; i >= 0; i--) {
			dto = (FileDTO)address_ref.get(i);
			fileaddress += dto.getOrgname();
			if(i != 0) {
				fileaddress += "/";
			}
		}
		File file = new File("d:/PM/BEngineer/" + fileaddress);
		sqlSession.update("bengineer.hit", file_ref);
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
		List address_ref = getAddr(ref);
		if(changeDirName(name, address_ref)) { // 폴더 생성 완료시
			model.addAttribute("alert", "폴더명 변경 완료");
			model.addAttribute("location", "\"/BEngineer/beFiles/beMyList.do?folder=" + folder + "\"");
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
		FileDTO dto = sqlSession.selectOne("bengineer.getaddr", ref);
		dto.setFilename(name);
		sqlSession.update("bengineer.changename", dto);
		model.addAttribute("alert", "파일명 변경 완료");
		model.addAttribute("location", "\"/BEngineer/beFiles/beMyList.do?folder=" + folder + "\"");
		return "beFiles/alert";
	}
	@RequestMapping("shareFile.do")
	public String shareFile(HttpSession session, Model model, int ref, String enddate, int rw) throws Exception {
		if(MainBean.loginCheck(session)) {return "redirect:/beMember/beLogin.do";} // 로그인 체크
		String owner = (String)session.getAttribute("nickname");
		FileDTO dto = sqlSession.selectOne("bengineer.getaddr", ref);
		if(!owner.equals(dto.getOwner())) {
			model.addAttribute("alert", "본인의 파일만 공유할 수 있습니다.");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}
		KeyDTO kdto = new KeyDTO();
		kdto.setFilenum(ref);
		kdto.setEnddate(Date.valueOf(enddate));
		kdto.setRw(rw);
		String keycheck = (String)sqlSession.selectOne("bengineer.keycheck", kdto);
		if(keycheck == null) {
			kdto.setShare_key(makecode());
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
		String nickname = (String)session.getAttribute("nickname");
		KeyDTO kdto = (KeyDTO)sqlSession.selectOne("bengineer.open", share_key);
		if(kdto != null) {
			ShareDTO sdto = new ShareDTO();
			sdto.setNickname(nickname);
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
	private String makecode() {
		String code = "";
		for(int i = 0; i < 20; i++) {
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
		if(check > 0) {code = makecode();}
		return code;
	}
	private boolean makeDir(String name, List address_ref) { // 기본폴더생성용 메서드 성공시 true
		FileDTO dto = new FileDTO();
		FileDTO ref = (FileDTO)address_ref.get(0);
		dto.setFolder_ref(ref.getNum());
		dto.setOwner(ref.getOwner());
		dto.setFilename(name);
		sqlSession.insert("bengineer.makedir", dto);
		String address = "";
		for(int i = 0; i < address_ref.size(); i++) {
			ref = (FileDTO)address_ref.get(i);
			address = "/" + ref.getFilename() + address;
		}
		File file = new File("d:/PM/BEngineer" + address + "/" + name);
		return file.mkdirs();
	}
	private List getAddr(int folder_ref) {
		List address = new ArrayList();
		return getAddr(address, folder_ref);
	}
	private List getAddr(List address, int folder_ref) { // 폴더 실 주소 확인을 위한 메서드
		FileDTO folder = (FileDTO)sqlSession.selectOne("bengineer.getaddr", folder_ref);
		address.add(folder);
		folder_ref = folder.getFolder_ref();
		if(folder_ref == 0) {
			return address;
		}else {
			return getAddr(address, folder_ref);
		}
	}
	private List getShareAddr(int folder_ref, String nickname) {
		List address = new ArrayList();
		return getShareAddr(address, folder_ref, nickname);
	}
	private List getShareAddr(List address, int folder_ref, String nickname) { // 폴더 실 주소 확인을 위한 메서드
		FileDTO folder = (FileDTO)sqlSession.selectOne("bengineer.getaddr", folder_ref);
		address.add(folder);
		ShareDTO sdto = new ShareDTO();
		sdto.setNickname(nickname);
		sdto.setNum(folder_ref);
		KeyDTO kdto = sqlSession.selectOne("bengineer.getkey", sdto);
		if(kdto != null) {
			address.add(kdto);
			return address;
		}else {
			if(folder_ref == 0) {
				return null;
			}else {
				folder_ref = folder.getFolder_ref();
				return getShareAddr(address, folder_ref, nickname);
			}
		}
	}
	private boolean changeDirName(String foldername, List address_ref) { // 폴더명 바꾸기 메서드 성공시 true, address는 폴더경로에 /까지
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
		dto.setOrgname(foldername);
		sqlSession.update("bengineer.changename", dto);
		File orgfolder = new File("d:/PM/BEngineer/" + folderaddress);
		File newfolder = new File("d:/PM/BEngineer/" + newaddress);
		return orgfolder.renameTo(newfolder);
	}
	// 파일타입 확인용 리스트들
	private static List image = new ArrayList();
	private static List video = new ArrayList();
	private static List music = new ArrayList();
	private static List document = new ArrayList();
	private static List etc = new ArrayList();
	public FileBean() {
		image.add(".jpeg");
		image.add(".jpg");
		image.add(".png");
		image.add(".gif");
		image.add(".bmp");
		video.add(".webm");
		video.add(".mpeg4");
		video.add(".3gpp");
		video.add(".mov");
		video.add(".avi");
		video.add(".mpegps");
		video.add(".wmv");
		video.add(".flv");
		video.add(".ogg");
		music.add(".mp3");
		music.add(".mpeg");
		music.add(".wav");
		document.add(".txt");
		document.add(".doc");
		document.add(".docx");
		document.add(".xls");
		document.add(".xlsx");
		document.add(".ppt");
		document.add(".pptx");
		etc.add(".zip");
		etc.add(".rar");
		etc.add(".tar");
		etc.add(".gzip");
		etc.add(".css");
		etc.add(".html");
		etc.add(".php");
		etc.add(".c");
		etc.add(".cpp");
		etc.add(".h");
		etc.add(".hpp");
		etc.add(".js");
		image.add(".dxf");
		image.add(".ai");
		image.add(".psd");
		document.add(".pdf");
		document.add(".xps");
		image.add(".eps");
		etc.add(".ps");
		image.add(".svg");
		image.add(".tiff");
		etc.add(".ttf");
	}
	private String checkFile(String filetype) { // 파일타입 확인용 메서드
		String result = null;
		if(image.contains(filetype)) {
			result = "image";
		}else if(music.contains(filetype)) {
			result = "music";
		}else if(video.contains(filetype)) {
			result = "video";
		}else if(document.contains(filetype)) {
			result = "document";
		}else if(etc.contains(filetype)) {
			result = "etc";
		}else {
			result = "none";
		}
		return result;
	}
}
