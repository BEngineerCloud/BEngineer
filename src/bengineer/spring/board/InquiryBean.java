package bengineer.spring.board;

import java.io.File;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
@RequestMapping("/inquiry/")
public class InquiryBean {
	@Autowired
	private SqlSessionTemplate sqlSession = null;
	// member 문의내역
	@RequestMapping("inList.do")
	public String list(Model model,HttpSession session,MemberDTO dto,String id) { 
		List list = sqlSession.selectList("board.inList",dto.getId());
		model.addAttribute("inList",list);
		session.setAttribute("Id", dto.getId());
		return "/inquiry/inList";
	}
	// member 문의하기
	@RequestMapping("inForm.do")
	public String writeForm(HttpSession session,InquiryDTO dto,String Id) { 
		session.setAttribute("Id", dto.getId());
		return "/inquiry/inForm";
	}
	// member 문의완료
	@RequestMapping(value="inPro.do",method=RequestMethod.POST)
	public String inPro(InquiryDTO dto,HttpSession session,Model model,String Id,MultipartHttpServletRequest request)throws Exception { 
		model.addAttribute("Id",dto.getId());
		sqlSession.insert("board.inquiry",dto);
		try {
		MultipartFile uploadFile = request.getFile("upload");
		String fileName = uploadFile.getOriginalFilename();
		String filename = fileName;
		sqlSession.update("board.inquiry2",filename);
		File saveFile = new File("D://PM//app//BEngineer//WebContent//inquiryImg//"+fileName);
		uploadFile.transferTo(saveFile);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "forward:/inquiry/inList.do";
	}
	// member 내문의목록보기
	@RequestMapping("inRead.do")
	public String inRead(int num,InquiryDTO dto,Model model ) { 
		InquiryDTO re = (InquiryDTO)sqlSession.selectOne("board.reply",num);
		model.addAttribute("re",re);
		return "/inquiry/inRead";
	}
	//manager 전체문의목록
	@RequestMapping("allInquiry.do")
	public String allInquiry(Model model) {
		List allList = sqlSession.selectList("manager.List");
		model.addAttribute("allList",allList);
		return "/inquiry/allInquiry";
	}
	//manager 답변작성
	@RequestMapping("replyForm.do")
	public String replyForm(InquiryDTO dto,int num,Model model) {
		InquiryDTO re = (InquiryDTO)sqlSession.selectOne("manager.read",num);
		model.addAttribute("re",re);
		return "/inquiry/replyForm";
	}
	//manager 답변
	@RequestMapping("reply.do")
	public String reply(InquiryDTO dto,int num) {
		sqlSession.update("manager.reply",dto);
		return "redirect:/inquiry/allInquiry.do";
	}
}
