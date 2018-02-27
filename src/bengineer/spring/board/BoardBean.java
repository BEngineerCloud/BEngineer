package bengineer.spring.board;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import bengineer.spring.board.BoardDTO;
import bengineer.spring.manager.ManagerDTO;

import org.apache.catalina.User;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/board/")
public class BoardBean {
	@Autowired
	private SqlSessionTemplate sqlSession = null;
	// 목록
	@RequestMapping("list.do")
	public String list(Model model,HttpSession session,ManagerDTO dto) { 
		List list = sqlSession.selectList("board.List");
		model.addAttribute("list",list);
		session.setAttribute("id", dto.getId());
		return "/board/list";
		
	}
	// 공지사항 작성폼
	@RequestMapping("writeForm.do")
	public String writeForm(HttpSession session,ManagerDTO dto) { 
		session.setAttribute("id", dto.getId());
		return "/board/writeForm"; 
	}
	// 작성완료
	@RequestMapping("writePro.do")
	public String writePro(BoardDTO dto,ManagerDTO dto2,HttpSession session,Model model) { 
		String manager = (String)session.getAttribute("Id");
		model.addAttribute("manager",manager);
		session.setAttribute("id", dto2.getId());
		sqlSession.insert("board.write",dto);
		return "forward:/board/list.do";
	}
	// 읽고
	@RequestMapping("updateForm.do")
	public String updateForm(Model model,HttpSession session,int num) { 
		BoardDTO con = (BoardDTO)sqlSession.selectOne("board.read",num);	
		model.addAttribute("con",con);
		return "/board/updateForm";
	}
	// 수정
	@RequestMapping("update.do")
	public String update(BoardDTO dto,Model model,int num) {
		sqlSession.update("board.updateBoard",dto);
		model.addAttribute("con",num);
		return "redirect:/board/list.do"; 
	}
	// 삭제
	@RequestMapping("delete.do")
	public String delete(BoardDTO dto,int num){ 
		sqlSession.delete("board.deleteBoard",dto);
		return "redirect:/board/list.do";
	}

}