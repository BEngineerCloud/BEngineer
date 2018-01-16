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
	
	@RequestMapping("impose.do")
	public String impose() { return "/manager/impose";}	
	@RequestMapping("imposeForm.do")
	public String imposeForm() { return "/manager/imposeForm";}
	// 제제
	@RequestMapping("imposePro.do")
	public String imposePro(ImposeDTO dto,String email,int term,String cause) { 
		Integer check = (Integer)sqlSession.selectOne("check",dto);
		if(check==1) {
		sqlSession.update("manager.update",dto);
		}else {
		sqlSession.insert("manager.insert",dto);
		}
		return "redirect:/manager/mMain.do";
	}

}
