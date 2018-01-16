package bengineer.spring.manager;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.apache.catalina.User;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/manager/")
public class ManagerBean {
	
	@Autowired
	private SqlSessionTemplate sqlSession = null;

	// 매니저 로그인창
	@RequestMapping("login.do")
	public String main() { return "/manager/login"; }
	// 로그인체크
	@RequestMapping("loginPro.do")
	public String loginPro(ManagerDTO dto,HttpSession session) {
	
		Integer check = (Integer)sqlSession.selectOne("loginCheck",dto); //manager.
		String view = "redirect:/manager/login.do";
		//System.out.println(check);
		if(check==1) {
			session.setAttribute("Id", dto.getId());
			view="redirect:/manager/mMain.do";
		}
		return view;	
	}	
	// 로그아웃
	@RequestMapping("logout.do")
	public String logout(HttpSession session) { 
		session.invalidate();
		//return "manager/logout";
		return "redirect:/manager/login.do"; 
	}
	
	// 메인창
	@RequestMapping("mMain.do")
	public String mMain(ManagerDTO dto,HttpSession session,String Id,ManagerDTO m) {
		String ss = (String)session.getAttribute("Id");
		if(ss==null) {
			return "redirect:/manager/login.do";
		}else{
			return "/manager/mMain";
		}
	}
	
}

