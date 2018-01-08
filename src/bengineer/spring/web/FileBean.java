package bengineer.spring.web;

import java.io.File;
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
	public String myFile(HttpSession session, Model model, String folder) {
		if(MainBean.loginCheck(session)) {return "redirect:/BEngineer/beMember/beLogin.do";} // 로그인 세션 없을 시 리디렉트
		String owner = (String)session.getAttribute("nickname");
		FileDTO dto = new FileDTO();
		dto.setOwner(owner);
		String fileaddress = "";
		if(folder == null) {
			fileaddress = owner; // 폴더값이 안 들어왔을 경우 닉네임과 같은 이름의 기본폴더로 이동
		}else {
			fileaddress = folder;
		}
		dto.setFileaddress(fileaddress + "/%"); // 지정된 폴더 내의 파일을 모두 검색
		List filelist = sqlSession.selectList("bengineer.myfile", dto);
		boolean mkdirch = true;
		if(folder == null && filelist.size() == 0) { // 기본 폴더에 아무런 파일 / 폴더가 없을 시 자동으로 폴더 생성
			if(!makeDir("image", owner)) {mkdirch = false;}
			if(!makeDir("video", owner)) {mkdirch = false;}
			if(!makeDir("music", owner)) {mkdirch = false;}
			if(!makeDir("document", owner)) {mkdirch = false;}
			if(!makeDir("etc", owner)) {mkdirch = false;}
			filelist = sqlSession.selectList("bengineer.myfile", dto);
			if(!mkdirch) { // 하나라도 오류 발생시
				model.addAttribute("alert", "폴더를 생성하는 도중 오류가 발생했습니다.");
				model.addAttribute("location", "history.go(-1)");
				return "beFiles/alert";
			}
		}
		model.addAttribute("list", filelist);
		model.addAttribute("fileaddress", fileaddress + "/");
		List folderaddress = new ArrayList(); // 폴더 경로를 하나씩 저장하기 위한 리스트
		String [] address = fileaddress.split("/");
		List orgaddress = new ArrayList(); // 폴더주소에 저장된 각각의 폴더에 대한 전체 경로를 하나씩 저장하기 위한 리스트
		if(address.length < 5) {
			String orgaddr = "";
			for(int i = 0; i < address.length; i++) {
				folderaddress.add(address[i]);
				orgaddr += address[i];
				orgaddress.add(orgaddr);
				orgaddr += "/";
			}
		}else {
			String orgaddr = "";
			folderaddress.add(owner);
			orgaddress.add(owner);
			folderaddress.add("..."); // 폴더 경로가 5개를 넘어길 시 기본폴더와 가장 위의 3개를 제외하고 생략
			orgaddress.add(null);
			int num = address.length;
			folderaddress.add(address[num - 3]);
			for(int i = 0; i < num - 3; i++) {
				orgaddr += address[i] + "/";
			}
			orgaddr += address[num - 3];
			orgaddress.add(orgaddr);
			folderaddress.add(address[num - 2]);
			orgaddr += "/" + address[num - 2];
			orgaddress.add(orgaddr);
			folderaddress.add(address[num - 1]);
			orgaddr += "/" + address[num - 1];
			orgaddress.add(orgaddr);
		}
		model.addAttribute("folderaddress", folderaddress);
		model.addAttribute("orgaddress", orgaddress);
		model.addAttribute("write", true);
		return "beFiles/beList";
	}
	@RequestMapping(value="fileupload.do", method=RequestMethod.POST) // 업로드 페이지
	public String upload(MultipartHttpServletRequest multi, String fileaddress, String filename, HttpSession session, Model model) {
		if(MainBean.loginCheck(session)) {return "redirect:/BEngineer/beMember/beLogin.do";} // 비 로그인 상태시 로그인 창으로 리디렉트
		try {
			MultipartFile mf = multi.getFile("save");
			String orgName = mf.getOriginalFilename();
			String owner = (String)session.getAttribute("nickname");
			String filetype = orgName.substring(orgName.lastIndexOf(".")); // 확장자
			String contentType = checkFile(filetype); // 확장자를 확인하여 파일타입 확인
			if(contentType.equals("none")) { // 업로드 할 수 없는 종류의 파일일 때
				model.addAttribute("alert", "업로드 할 수 없는 종류의 파일입니다.");
				model.addAttribute("location", "history.go(-1)");
				return "beFiles/alert";
			}
			boolean typech = true;
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
			File copy = new File("d:/PM/BEngineer/" + fileaddress + orgName);
			if(copy.exists()) { // 같은 이름의 파일이 존재할 때
				model.addAttribute("alert", "해당 경로에 같은 이름의 파일이 존재합니다.");
				model.addAttribute("location", "history.go(-1)");
				return "beFiles/alert";
			}
			mf.transferTo(copy);
			long filesize = copy.length();
			FileDTO dto = new FileDTO();
			dto.setFileaddress(fileaddress + orgName);
			if(filename == null || filename.equals("")) {
				dto.setFilename(orgName);
			}else {
				dto.setFilename(filename);
			}
			dto.setOwner(owner);
			dto.setFilesize(filesize);
			dto.setFiletype(filetype);
			sqlSession.insert("bengineer.upload", dto);
			model.addAttribute("alert", "파일 업로드 완료");
			model.addAttribute("location", "\"/BEngineer/beFiles/beMyList.do?folder=" + fileaddress.substring(0, fileaddress.lastIndexOf("/")) + "\"");
			return "beFiles/alert";
		}catch(Exception e) { // 오류 발생시
			e.printStackTrace();
			model.addAttribute("alert", "업로드 도중 오류가 발생했습니다.");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}
	}
	@RequestMapping("createFolder.do") // 폴더 만들기 페이지
	public String folder(HttpSession session, Model model, String foldername, String folderaddress) {
		if(MainBean.loginCheck(session)) {return "redirect:/BEngineer/beMember/beLogin.do";} // 로그인 체크
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
		String owner = (String)session.getAttribute("nickname");
		if(makeDir(foldername, folderaddress, owner)) { // 폴더 생성 완료시
			model.addAttribute("alert", "폴더 생성 완료");
			model.addAttribute("location", "\"/BEngineer/beFiles/beMyList.do?folder=" + folderaddress.substring(0, folderaddress.lastIndexOf("/")) + "\"");
			return "beFiles/alert";
		}else { // 폴더생성 오류시
			model.addAttribute("alert", "폴더를 생성하는 도충 오류가 발생했습니다.");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}
	}
	private boolean makeDir(String name, String owner) { // 기본폴더생성용 메서드 성공시 true
		FileDTO dto = new FileDTO();
		dto.setOwner(owner);
		dto.setFileaddress(owner + "/" + name);
		dto.setFilename(name);
		sqlSession.insert("bengineer.makedir", dto);
		File file = new File("d:/PM/BEngineer/" + dto.getFileaddress());
		return file.mkdirs();
	}
	private boolean makeDir(String name, String address, String owner) { // 일반 폴더 생성용 메서드 성공시 true, address는 폴더경로에 /까지
		FileDTO dto = new FileDTO();
		dto.setOwner(owner);
		dto.setFileaddress(address + name);
		dto.setFilename(name);
		sqlSession.insert("bengineer.makedir", dto);
		File file = new File("d:/PM/BEngineer/" + dto.getFileaddress());
		return file.mkdir();
	}
	@RequestMapping("beDownload.do") // 파일 다운로드
	public ModelAndView download(String fileaddress) {
		File file = new File("d:/PM/BEngineer/" + fileaddress);
		ModelAndView mv = new ModelAndView("download","downloadFile",file);
		return mv;
	}
	// 파일타입 확인용 리스트 들
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
