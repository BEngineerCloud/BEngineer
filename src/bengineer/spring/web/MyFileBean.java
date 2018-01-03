package bengineer.spring.web;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("beFiles/*")
public class MyFileBean {
	@Autowired
	private SqlSessionTemplate sqlSession = null;
	@RequestMapping("beList.do")
	public String myFile(HttpSession session, Model model) {
		if(MainBean.loginCheck(session)) {
			return "redirect:/beInit/beLogin";
		}
		String owner = (String)session.getAttribute("nickname");
		FileDTO dto = new FileDTO();
		dto.setOwner(owner);
		dto.setFileaddress(owner + "/%");
		List filelist = sqlSession.selectList("myfile", dto);
		if(filelist == null || filelist.size() == 0) {
			makeDir("image", owner);
			makeDir("video", owner);
			makeDir("music", owner);
			makeDir("document", owner);
			makeDir("etc", owner);
			filelist = sqlSession.selectList("myfile", dto);
		}
		model.addAttribute("list", filelist);
		System.out.println(filelist.size());
		return "beFiles/beList";
	}
	private void makeDir(String name, String owner) {
		FileDTO dto = new FileDTO();
		dto.setOwner(owner);
		dto.setFileaddress(owner + "/" + name);
		dto.setFilename(name);
		File file = new File(dto.getFileaddress());
	}
	private void makeDir(FileDTO dto) {sqlSession.insert("makedir", dto);}
}
