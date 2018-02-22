package bengineer.spring.web;

import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeUtility;

import bengineer.spring.board.BoardDTO;
import bengineer.spring.web.MemberDTO;

@Controller()
@RequestMapping("/beMember/*")
public class MemberBean {
	@Autowired
	private SqlSessionTemplate sqlSession = null;
	
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
	
	@RequestMapping(value="beCheckemail.do")
	public String beCheckmailid(Model model, String email, HttpSession session ) {
		Integer check = (Integer)sqlSession.selectOne("bengineer.beCheckmailid",email);
		if(session.getAttribute("authcode")!=null) {
			session.removeAttribute("authcode");
		}
		if(session.getAttribute("email")!=null) {
			session.removeAttribute("email");
		}
		model.addAttribute("check",check);
		model.addAttribute("mailid",email);
		return "beMember/beCheckemail";
	}
	
	@RequestMapping(value="beAuthemail.do")
	public String beAuthemail(Model model, String email, HttpSession session) {
		FileBean filebean = new FileBean();
		filebean.setSqlSession(sqlSession);
		
		String authcode="";
		authcode = filebean.makecode(8);
		sendEmail(email,authcode);
		session.setAttribute("authcode", authcode); 
		session.setAttribute("email", email);
		model.addAttribute("mailid",email);
		model.addAttribute("authcode",authcode);
		return "beMember/beCheckemail";
	}
	
	//메일인증 확인
	@RequestMapping(value="beConfirmemail.do") 
	public String beConfirmemail(Model model, String authcode, HttpSession session) {
		String email = (String)session.getAttribute("email");
		if(authcode.equals(session.getAttribute("authcode"))) {
			model.addAttribute("confirmEmail",0);
			model.addAttribute("email",email);
		}else {
			model.addAttribute("confirmEmail",1);
		}
		return "beMember/beConfirmemail";
	}
	
	public void sendEmail(String email, String authcode) {
		String username = "loser4kku@gmail.com";
		String password = "bengineer";
		String host = "smtp.gmail.com"; //메일 서버
		String subject = "BEngineer 메일인증링크";
		String fromName = "BEngineer 관리자"; // 보낼사람 이름
		String from = "loser4kku@gmail.com"; //보낼사람 이메일
		String to1 = email;
		String content = "http://192.168.0.153/BEngineer/beMember/beConfirmemail.do?authcode="+authcode; //내용
		
		try {
			Properties props = new Properties();
			props.put("mail.smtp.starttls.enable","true");
			props.put("mail.transport.protocol","smtp");
			props.put("mail.smtp.host",host);
			props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.port","465");
			props.put("mail.smtp.user",from);
			props.put("mail.smtp.auth","true");
			
			Session mailSession = Session.getInstance(props,
					new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(username, password);
						}
			});
			Message msg = new MimeMessage(mailSession);
			msg.setFrom(new InternetAddress(from, MimeUtility.encodeText(fromName,"UTF-8","B")));
			
			InternetAddress[] address1 = { new InternetAddress(to1)};
			msg.setRecipients(Message.RecipientType.TO, address1);
			msg.setSubject(subject);
			msg.setSentDate(new java.util.Date());
			msg.setContent(content,"text/html;charset=UTF-8");
		
			Transport.send(msg);
		}catch(javax.mail.MessagingException e) {
			e.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	@RequestMapping("upgrade.do")
	public String upgrade(HttpSession session,Model model,String id) { 
		Integer giga = (Integer)sqlSession.selectOne("bengineer.giga",id);
		Integer chmod = (Integer)sqlSession.selectOne("bengineer.chmod",id);
		model.addAttribute("id",id);
		model.addAttribute("giga",giga);
		model.addAttribute("giga2",chmod);
		return "/beMember/upgrade";
	}
	@RequestMapping("change.do")
	public String change(MemberDTO dto) {
		sqlSession.update("bengineer.change",dto);
		sqlSession.update("bengineer.change2");
		return "/beMember/change";	
	}
}