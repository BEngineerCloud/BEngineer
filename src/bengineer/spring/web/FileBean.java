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

@Controller
@RequestMapping("/beFiles/")
public class FileBean {
	@Autowired
	private SqlSessionTemplate sqlSession = null;
	@RequestMapping("beMyList.do")
	public String myFile(HttpSession session, Model model, String folder) {
		if(MainBean.loginCheck(session)) {return "redirect:/BEngineer/beMember/beLogin.do";}
		String owner = (String)session.getAttribute("nickname");
		FileDTO dto = new FileDTO();
		dto.setOwner(owner);
		String fileaddress = "";
		if(folder == null) {
			fileaddress = owner;
		}else {
			fileaddress = folder;
		}
		dto.setFileaddress(fileaddress + "/%");
		List filelist = sqlSession.selectList("bengineer.myfile", dto);
		boolean mkdirch = true;
		if(folder == null && filelist.size() == 0) {
			if(!makeDir("image", owner)) {mkdirch = false;}
			if(!makeDir("video", owner)) {mkdirch = false;}
			if(!makeDir("music", owner)) {mkdirch = false;}
			if(!makeDir("document", owner)) {mkdirch = false;}
			if(!makeDir("etc", owner)) {mkdirch = false;}
			filelist = sqlSession.selectList("bengineer.myfile", dto);
			if(!mkdirch) {
				model.addAttribute("alert", "폴더를 생성하는 도중 오류가 발생했습니다.");
				model.addAttribute("location", "history.go(-1)");
				return "beFiles/alert";
			}
		}
		model.addAttribute("list", filelist);
		model.addAttribute("fileaddress", fileaddress + "/");
		List folderaddress = new ArrayList();
		String [] address = fileaddress.split("/");
		List orgaddress = new ArrayList();
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
			folderaddress.add("...");
			orgaddress.add(null);
			int num = address.length;
			folderaddress.add(address[num - 2]);
			for(int i = 0; i < num - 3; i++) {
				orgaddr += address[i] + "/";
			}
			orgaddr += address[num - 2];
			orgaddress.add(orgaddr);
			folderaddress.add(address[num - 1]);
			orgaddr += "/" + address[num - 1];
			orgaddress.add(orgaddr);
			folderaddress.add(address[num]);
		}
		model.addAttribute("folderaddress", folderaddress);
		model.addAttribute("write", true);
		return "beFiles/beList";
	}
	@RequestMapping(value="fileupload.do", method=RequestMethod.POST)
	public String upload(MultipartHttpServletRequest multi, String fileaddress, String filename, HttpSession session, Model model) {
		if(MainBean.loginCheck(session)) {
			return "redirect:/BEngineer/beMember/beLogin.do";
		}
		try {
			MultipartFile mf = multi.getFile("save");
			String orgName = mf.getOriginalFilename();
			String owner = (String)session.getAttribute("nickname");
			String filetype = orgName.substring(orgName.lastIndexOf("."));
			File copy = new File("d:/PM/BEngineer/" + fileaddress + orgName);
			if(copy.exists()) {
				model.addAttribute("alert", "해당 경로에 동일한 이름의 파일이 존재합니다.");
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
			String contentType = checkFile(filetype);
			boolean typech = true;
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
			if(contentType.equals("none")) {
				model.addAttribute("alert", "업로드 할 수 없는 종류의 파일입니다.");
				model.addAttribute("location", "history.go(-1)");
				return "beFiles/alert";
			}
			sqlSession.insert("bengineer.upload", dto);
			model.addAttribute("alert", "업로드가 완료되었습니다.");
			model.addAttribute("location", "\"/BEngineer/beFiles/beMyList.do\"");
			return "beFiles/alert";
		}catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("alert", "업로드 도중 오류가 발생했습니다.");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}
	}
	private boolean makeDir(String name, String owner) {
		FileDTO dto = new FileDTO();
		dto.setOwner(owner);
		dto.setFileaddress(owner + "/" + name);
		dto.setFilename(name);
		sqlSession.insert("bengineer.makedir", dto);
		File file = new File("d:/PM/BEngineer/" + dto.getFileaddress());
		return file.mkdirs();
	}
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
	private String checkFile(String filetype) {
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
