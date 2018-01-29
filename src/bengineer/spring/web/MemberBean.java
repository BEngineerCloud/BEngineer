package bengineer.spring.web;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import bengineer.spring.board.BoardDTO;

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
	
	@RequestMapping("beAddinfopro.do")
	public String beAddinfopro(String nickname, HttpSession session) {
		MemberDTO memberDTO = (MemberDTO)sqlSession.selectOne("bengineer.beSelectmember", session.getAttribute("id"));
		memberDTO.setNickname(nickname);
		sqlSession.update("bengineer.beUpdatenickname", memberDTO);
		session.setAttribute("nickname", memberDTO.getNickname());
		return "beMember/beAddinfopro";
	}

	@RequestMapping(value="beChecknickname.do")
	public String beChecknickname(Model model, String nickname ) {
		Integer check = (Integer)sqlSession.selectOne("bengineer.beChecknickname",nickname);
		model.addAttribute("check",check);
		model.addAttribute("nickname",nickname);
		return "beMember/beChecknickname";
	}
	//유저 공지사항읽기
	@RequestMapping("beboard.do")
	public String beboard(Model model,HttpSession session,MemberDTO dto) { 
		List list = sqlSession.selectList("board.List");
		model.addAttribute("list",list);
		session.setAttribute("Id", dto.getId());
		return "beMember/beboard";
	}
	//문의내역읽기
	@RequestMapping("beread.do")
	public String updateForm(Model model,int num) { 
		BoardDTO con = (BoardDTO)sqlSession.selectOne("board.read",num);	
		model.addAttribute("con",con);
		return "beMember/beread";
	}
}