package bengineer.spring.manager;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import bengineer.spring.board.BoardDTO;
import bengineer.spring.manager.ImposeDTO;
import bengineer.spring.web.MemberDTO;

import org.apache.catalina.User;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/manager/")
public class ImposeDAO {
	@Autowired
	private SqlSessionTemplate sqlSession = null;
	// 제제 실행폼
	@RequestMapping("imposeForm.do")
	public String imposeForm(Model model) { 
		List list = sqlSession.selectList("manager.imposelist");
		model.addAttribute("list",list);
		return "/manager/imposeForm";
	}
	@RequestMapping("imposePro.do")
	public String imposePro(ImposeDTO dto,String email,int term,String cause) { 
		Integer check = (Integer)sqlSession.selectOne("check",dto);
		if(check==1) {	// 이미 제제되었는데 추가로 제제할때
		sqlSession.update("manager.update",dto);
		}else {			// 새로운 유저 제제할때
		sqlSession.insert("manager.insert",dto);
		}
		return "redirect:/manager/imposeForm.do";
	}
	@RequestMapping("imposeCancle.do")
	public String imposeCancle(String email){
		sqlSession.delete("manager.imposeCancle",email);	// 제제 만료시 해당 회원 이메일 속성에서 삭제
		return "redirect:/manager/imposeForm.do";
	}
}
