package bengineer.spring.web;

import javax.servlet.http.HttpSession;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller()
@RequestMapping("/beMember/*")
public class MemberBean {
	@Autowired
	private SqlSessionTemplate sqlSession = null;
	@RequestMapping("beLogin.do")
	public String beLogin() {return "beMember/beLogin";}
	
	@RequestMapping("beRequestprofile.do")
	public String beRequestprofile() {return "beMember/beRequestprofile";}
	
	@RequestMapping("beAddinfo.do")
	public String beAddinfo(Model model,HttpSession session) {
		MemberDTO memberDTO = (MemberDTO)sqlSession.selectOne("bengineer.beSelectmember", session.getAttribute("id"));
		model.addAttribute("memberDTO",memberDTO);
		return "beMember/beAddinfo";
	}

	@RequestMapping(value="beChecknickname.do")
	public String beChecknickname(Model model, String nickname ) {
		Integer check = (Integer)sqlSession.selectOne("bengineer.beChecknickname",nickname);
		model.addAttribute("check",check);
		model.addAttribute("nickname",nickname);
		return "beMember/beChecknickname";
	}
}
