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
	// 제제
	@RequestMapping("imposePro.do")
	public String imposePro(ImposeDTO dto,String email,int term) { 
		sqlSession.insert("manager.impose",dto);
		return "redirect:/manager/mMain.do";
	}
	/*
	@RequestMapping("imposePro.do")
	public String imposePro(MemberDTO dto,String email,int chmod) {
		sqlSession.update("manager.impose",dto);
		return "redirect:/manager/mMain.do";		
	}
	*/
	/*
	 	@RequestMapping("userInfo.do")
	public String userInfo(Model model,
			@RequestParam(value="id",defaultValue="guest") String id) {	// 값이 넘어오면 id 로 안넘어오면 guest로 검색
		//TestDTO testDTO = (TestDTO)sqlSession.selectOne("test.selectOne",id);
		TestDTO testDTO = (TestDTO)sqlSession.selectOne("test.selectIf",id);	
		model.addAttribute("testDTO",testDTO);
		return "/test/userInfo"; 
	}	
	 */
}
