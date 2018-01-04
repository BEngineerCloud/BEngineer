package bengineer.spring.web;

import java.io.File;
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
	public String myFile(HttpSession session, Model model) {
		if(MainBean.loginCheck(session)) {
			return "redirect:/beLogin.do";
		}
		String owner = (String)session.getAttribute("nickname");
		FileDTO dto = new FileDTO();
		dto.setOwner(owner);
		dto.setFileaddress(owner + "/%");
		List filelist = sqlSession.selectList("bengineer.myfile", dto);
		boolean mkdirch = true;
		if(filelist == null || filelist.size() == 0) {
			if(!makeDir("image", owner)) {mkdirch = false;}
			if(!makeDir("video", owner)) {mkdirch = false;}
			if(!makeDir("music", owner)) {mkdirch = false;}
			if(!makeDir("document", owner)) {mkdirch = false;}
			if(!makeDir("etc", owner)) {mkdirch = false;}
			filelist = sqlSession.selectList("bengineer.myfile", dto);
		}
		if(!mkdirch) {
			model.addAttribute("alert", "������ �����ϴ� ���� ������ �߻��߽��ϴ�.");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}
		model.addAttribute("list", filelist);
		model.addAttribute("fileaddress", owner + "/");
		return "beFiles/beList";
	}
	@RequestMapping(value="fileupload.do", method=RequestMethod.POST)
	public String upload(MultipartHttpServletRequest multi, String fileaddress, String filename, HttpSession session, Model model) {
		if(MainBean.loginCheck(session)) {
			return "redirect:/beLogin.do";
		}
		try {
			MultipartFile mf = multi.getFile("save");
			String orgName = mf.getOriginalFilename();
			String owner = (String)session.getAttribute("nickname");
			String filetype = orgName.substring(orgName.lastIndexOf("."));
			File copy = new File("d:/PM/BEngineer/" + fileaddress + orgName);
			if(copy.exists()) {
				model.addAttribute("alert", "�ش� ��ο� ������ �̸��� ������ �����մϴ�.");
				model.addAttribute("location", "history.go(-1)");
				return "beFiles/alert";
			}
			mf.transferTo(copy);
			long filesize = copy.length();
			FileDTO dto = new FileDTO();
			dto.setFileaddress(fileaddress + orgName);
			dto.setFilename(filename);
			dto.setOwner(owner);
			dto.setFilesize(filesize);
			dto.setFiletype(filetype);
			sqlSession.insert("bengineer.upload", dto);
			model.addAttribute("alert", "���ε尡 �Ϸ�Ǿ����ϴ�.");
			model.addAttribute("location", "\"/BEngineer/beFiles/beMyList.do\"");
			return "beFiles/alert";
		}catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("alert", "���ε� ���� ������ �߻��߽��ϴ�.");
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
}
