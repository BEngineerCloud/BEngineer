package bengineer.spring.web;

import javax.servlet.http.HttpSession;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller()
@RequestMapping("/beMember/*")
public class LoginBean {
	@Autowired
	private SqlSessionTemplate sqlSession = null;
	
	@RequestMapping("beLogin.do")
	public String beLogin() {return "beMember/beLogin";}
	
	@RequestMapping("beLoginpro.do")
	public String beLoginpro() {return "beMember/beLoginpro";}
	
	@RequestMapping("beRequestprofile.do")
	public String beRequestprofile() {return "beMember/beRequestprofile";}
	
	@RequestMapping("beCollback.do")
	public String beCollback() {return "beMember/beLogin";}
	
	//회원가입
	@RequestMapping("beJoinmember.do")
	public String beJoinmember() {return "beMember/beJoinmember";}	
		
	@RequestMapping("beJoinmemberpro.do")
	public String beJoinmemberpro(MemberDTO dto, HttpSession session) {
		FileBean filebean = new FileBean();
		filebean.setSqlSession(sqlSession);
			
		String id="";
		id = filebean.makecode(8);
		dto.setId(id);
			
		sqlSession.insert("bengineer.beInsertmember2", dto);
		return "beMember/beJoinmemberpro";
	}	
}
