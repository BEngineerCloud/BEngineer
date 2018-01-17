package bengineer.spring.web;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import bengineer.spring.manager.ImposeDTO;


@Controller
public class MainBean {
	@Autowired
	private SqlSessionTemplate sqlSession = null;
	@RequestMapping("beMain.do") // 메인페이지
	public String main(HttpSession session) {
		session.setAttribute("id", "id"); // 테스트용임시세션등록
		session.setAttribute("nickname", "nickname");
		if(loginCheck(session)) {
			return "redirect:/beMember/beLogin.do";
		}else {
			return "beMain";
		}
	}
	
	@RequestMapping(value="beMaintemp.do") // 메인페이지
	public String beMaintemp(MemberDTO dto, HttpSession session, Model model) {
		System.out.println(dto.getEmail());
		String email = dto.getEmail();
		Integer check = (Integer)sqlSession.selectOne("bengineer.beChecklogin",dto.getId());
		Integer impose = (Integer)sqlSession.selectOne("bengineer.imposeMember", email);
		String veiw= "redirect:/beMain.do";
		if(impose==1) {
			veiw="redirect:/beMember/imposeMember.do";
		}else {
		if(check!=1) {
			sqlSession.insert("bengineer.beInsertmember", dto);
		}
		session.setAttribute("id", dto.getId()); // 테스트용임시세션등록
		session.setAttribute("nickname", dto.getNickname());
		}
		return veiw;
	}
	@RequestMapping("imposeMember.do") // 메인페이지
	public String imposeMember() {return "beMember/imposeMember";}
	
	@RequestMapping("beLogout.do") // 메인페이지
	public String beLogout(HttpSession session) {
		session.invalidate();
		return "redirect:/beMain.do";
	}
	
	public static boolean loginCheck(HttpSession session) { // 로그인 체크용 메서드, 세션에 nickname 세션이 정상적으로 있지 않을 경우 true  
		String id = (String)session.getAttribute("id");
		return id == null || id.equals("null") || id.equals("");
	}
}
