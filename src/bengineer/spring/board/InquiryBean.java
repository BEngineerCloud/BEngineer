package bengineer.spring.board;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import bengineer.spring.board.BoardDTO;
import bengineer.spring.manager.ManagerDTO;
import bengineer.spring.web.MemberDTO;

import org.apache.catalina.User;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/inquiry/")
public class InquiryBean {
	@Autowired
	private SqlSessionTemplate sqlSession = null;
	// ���
	@RequestMapping("inList.do")
	public String list(Model model,HttpSession session,MemberDTO dto) { 
		List list = sqlSession.selectList("board.List");
		model.addAttribute("inList",list);
		session.setAttribute("Id", dto.getId());
		return "/inquiry/inList";
	}
	// �ۼ�
	@RequestMapping("inForm.do")
	public String writeForm(HttpSession session,InquiryDTO dto,String Id) { 
		session.setAttribute("Id", dto.getId());
		sqlSession.insert("board.inquiry",dto);
		return "/inquiry/inForm";
	}
	//
	@RequestMapping("inPro.do")
	public String writePro(BoardDTO dto,HttpSession session,Model model) { 
		//String manager = (String)session.getAttribute("Id");
		//model.addAttribute("manager",manager);
		//sqlSession.insert("board.write",dto);
		//return "/inquiry/inPro";
		return "redirect:/inquiry/inList.do";
	}
}
