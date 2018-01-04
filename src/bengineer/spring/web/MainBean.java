package bengineer.spring.web;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainBean {
	@Autowired
	private SqlSessionTemplate sqlSession = null;
	@RequestMapping("beMain.do")
	public String main(HttpSession session) {
		session.setAttribute("nickname", "test");
		if(loginCheck(session)) {
			return "redirect:/beLogin.do";
		}else {
			return "beMain";
		}
	}
	public static boolean loginCheck(HttpSession session) {
		String id = (String)session.getAttribute("nickname");
		return id == null || id.equals("null") || id.equals("");
	}
}
