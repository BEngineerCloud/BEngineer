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
public class MyFileBean {
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
		if(filelist == null || filelist.size() == 0) {
			makeDir("image", owner);
			makeDir("video", owner);
			makeDir("music", owner);
			makeDir("document", owner);
			makeDir("etc", owner);
			filelist = sqlSession.selectList("bengineer.myfile", dto);
		}
		model.addAttribute("list", filelist);
		model.addAttribute("fileaddress", owner + "/");
		return "beFiles/beList";
	}
	@RequestMapping(value="upload.do", method=RequestMethod.POST)
	public String upload(MultipartHttpServletRequest multi, String fileaddress, Model model) throws Exception{
		MultipartFile mf = multi.getFile("save");
		String orgName = mf.getOriginalFilename();
		File copy = new File(fileaddress + "/" + orgName);
		mf.transferTo(copy);
		model.addAttribute("alert", "업로드가 완료되었습니다.");
		return "error"; 
	}
	private void makeDir(String name, String owner) {
		FileDTO dto = new FileDTO();
		dto.setOwner(owner);
		dto.setFileaddress(owner + "/" + name);
		dto.setFilename(name);
		makeDir(dto);
		File file = new File(dto.getFileaddress());
	}
	private void makeDir(FileDTO dto) {sqlSession.insert("bengineer.makedir", dto);}
}
