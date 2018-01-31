package bengineer.spring.web;

import javax.servlet.http.HttpSession;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller()
@RequestMapping("/beMember/*")
public class LoginBean {
	@Autowired
	private SqlSessionTemplate sqlSession = null;
	
	@RequestMapping("beLogin.do")
	public String beLogin() {return "beMember/beLogin";}
	
	@RequestMapping("beLoginpro.do")
	public String beLoginpro(MemberDTO dto,Model model, HttpSession session) {
		String email = dto.getEmail();
		Integer check = (Integer)sqlSession.selectOne("bengineer.beChecklogin",email);

		if(check==1) {
			String pw = sqlSession.selectOne("bengineer.beSelectpw",email);
			if(pw.equals(dto.getPw())) {
				dto = sqlSession.selectOne("bengineer.beSelectmember2",email);
				session.setAttribute("id", dto.getId()); 
				session.setAttribute("nickname", dto.getNickname());
				model.addAttribute("alert", "로그인 성공!!");
				model.addAttribute("location", "\"/BEngineer2/beMain.do\"");
			}else {
				model.addAttribute("alert", "비밀번호가 틀렸습니다!!");
				model.addAttribute("location", "history.go(-1)");
			}
		}else {
			model.addAttribute("alert", "가입된 메일주소가 맞는지 확인해주십시오.");
			model.addAttribute("location", "history.go(-1)");
		}
		return "beFiles/alert";
	}
	
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
