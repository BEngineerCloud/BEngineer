package bengineer.spring.web;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
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
	
	@RequestMapping("beAddinfo.do") //수정정보 페이지로 이동
	public String beAddinfo(Model model,HttpSession session) {
		if(MainBean.loginCheck(session)) {return "redirect:/beMember/beLogin.do";} // 로그인 세션 없을 시 리디렉트
		
		//세션 id로 회원memberDTO 받아오기
		MemberDTO memberDTO = (MemberDTO)sqlSession.selectOne("bengineer.beSelectmember", session.getAttribute("id"));
		
		model.addAttribute("memberDTO",memberDTO); //beAddinfo 페이지에 memberDTO의 정보 넘겨주기
		return "beMember/beAddinfo";
	}
	
	@RequestMapping("beAddinfopro.do") //수정완료 페이지로 이동
	public String beAddinfopro(String nickname, String pw, HttpSession session) {
		
		//세션 id로 회원memberDTO 받아오기
		MemberDTO memberDTO = (MemberDTO)sqlSession.selectOne("bengineer.beSelectmember", session.getAttribute("id"));
		
		//새 닉네임, 새 비밀번호로 DB 수정 후 세션으로 다시 설정
		memberDTO.setNickname(nickname); 
		memberDTO.setPw(pw);
		sqlSession.update("bengineer.beUpdatenickname", memberDTO);
		session.setAttribute("nickname", memberDTO.getNickname());
		sqlSession.update("bengineer.updatefilename", memberDTO);
		
		return "beMember/beAddinfopro";
	}

	@RequestMapping(value="beChecknickname.do") //닉네임 중복검사 페이지로 이동
	public String beChecknickname(Model model, String nickname ) {
		Integer check = (Integer)sqlSession.selectOne("bengineer.beChecknickname",nickname); //해당하는 닉네임이 존재하는지 확인
		
		//존재하는지 여부와 닉네임 정보 넘겨주기
		model.addAttribute("check",check);
		model.addAttribute("nickname",nickname);
		
		return "beMember/beChecknickname";
	}
	
	//유저 공지사항읽기
	@RequestMapping("beboard.do")
	public String beboard(Model model,HttpSession session,MemberDTO dto) { 
		if(MainBean.loginCheck(session)) {return "redirect:/beMember/beLogin.do";} // 로그인 세션 없을 시 리디렉트
		List list = sqlSession.selectList("board.List");
		model.addAttribute("list",list);
		session.setAttribute("Id", dto.getId());
		String id = (String)session.getAttribute("id");
		List font = sqlSession.selectList("bengineer.font", id);
 		model.addAttribute("font",font);	// 검색에 필요한 파일목록들
		return "beMember/beboard";
	}
	
	//문의내역읽기
	@RequestMapping("beread.do")
	public String updateForm(Model model, HttpSession session, int num) { 
		if(MainBean.loginCheck(session)) {return "redirect:/beMember/beLogin.do";} // 로그인 세션 없을 시 리디렉트
		BoardDTO con = (BoardDTO)sqlSession.selectOne("board.read",num);	
		model.addAttribute("con",con);
		String id = (String)session.getAttribute("id");
		List font = sqlSession.selectList("bengineer.font", id);
 		model.addAttribute("font",font);	// 검색에 필요한 파일목록들
		return "beMember/beread";
	}
	
	@RequestMapping(value="beCheckemail.do") //이메일 인증 페이지로 이동
	public String beCheckmailid(Model model, String email, HttpSession session ) {
		Integer check = (Integer)sqlSession.selectOne("bengineer.beCheckmailid",email); //해당하는 이메일과 일치하는 멤버가 있는지 확인
		
		//'authocode'세션이 존재하면 삭제
		if(session.getAttribute("authcode")!=null) { 
			session.removeAttribute("authcode");
		}
		
		//'email'세션이 존재하면 삭제
		if(session.getAttribute("email")!=null) {
			session.removeAttribute("email");
		}
		
		//이메일에 해당하는 멤버가 존재하는지 여부와 메일주소 정보 넘겨주기
		model.addAttribute("check",check);
		model.addAttribute("mailid",email);
		
		return "beMember/beCheckemail";
	}
	
	@RequestMapping(value="beAuthemail.do") //메일인증링크 
	public String beAuthemail(Model model, String email, HttpSession session) {
		
		//FileBean에 makecode함수를 사용해야하는데 makecode함수에서 sql문을 사용하기 때문에 Filebean객체를 생성한 후 sqlSession도 설정해준다.
		FileBean filebean = new FileBean();
		filebean.setSqlSession(sqlSession);
		
		//authcode 임의로 8문자 생성
		String authcode="";
		authcode = filebean.makecode(8);
		
		sendEmail(email,authcode,"메일인증"); //인증코드를 포함한 링크를 메일로 보내기
		
		//authcode와 email 세션 설정 후 데이터로 넘겨주기
		session.setAttribute("authcode", authcode); 
		session.setAttribute("email", email);
		model.addAttribute("mailid",email);
		model.addAttribute("authcode",authcode);
		
		return "beMember/beCheckemail";
	}
	
	@RequestMapping(value="beConfirmemail.do") //메일인증 여부 확인
	public String beConfirmemail(Model model, String authcode, HttpSession session) {
		String email = (String)session.getAttribute("email");
		
		if(authcode.equals(session.getAttribute("authcode"))) { //넘겨받은 인증코드가 세션으로 설정된 인증코드와 일치하면 confirmEmail을 0으로 설정하고 이메일과 같이 넘겨준다.
			model.addAttribute("confirmEmail",0);
			model.addAttribute("email",email);
		}else { //일치하지 않으면 confirmEmail을 1로 설정해주고 보내준다.
			model.addAttribute("confirmEmail",1);
		}
		
		return "beMember/beConfirmemail";
	}
	
	@RequestMapping(value="beDeletemember.do") 
	public String beDeletemember(Model model, HttpSession session, String email) {
		String id = (String)session.getAttribute("id");
		sqlSession.delete("bengineer.beDeletemember",email); //회원탈퇴
		sqlSession.delete("bengineer.deleteFiles",id); //회원 파일목록 삭제
		sqlSession.delete("bengineer.deleteBackupFiles",id); //회원 백업파일목록 삭제
		
		//실제 파일이 저장되있는 경로에서 실제 파일 삭제
		File file= new File("D://PM/BEngineer/"+id);
		if(file.exists()) {
			try {
				FileUtils.deleteDirectory(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		session.invalidate(); //세션지우기
		
		model.addAttribute("alert", "탈퇴되었습니다.");
		model.addAttribute("location", "\"/BEngineer/beMember/beLogin.do\"");
		
		return "beFiles/alert";
	}
	
	public void sendEmail(String email, String authcode, String flag) { //이메일 보내기
		String username = "loser4kku@gmail.com"; //메일 보낼 사용자 아이디
		String password = "bengineer"; //메일 보낼 사용자 비밀번호
		String host = "smtp.gmail.com"; //메일 서버
		String subject = ""; //메일 제목
		String fromName = "BEngineer 관리자"; // 보낼사람 이름
		String from = "loser4kku@gmail.com"; //보낼사람 이메일
		String to1 = email; //받는사람 이메일
		String content = ""; //내용
		
		if(flag.equals("메일인증")) { //메일인증일 시
			subject = "BEngineer 메일인증링크"; 
			content = "http://192.168.0.153/BEngineer/beMember/beConfirmemail.do?authcode="+authcode; //내용
		}
		else if(flag.equals("임시비밀번호")) { //임시비밀번호 발급일 시
			subject = "BEngineer 임시비밀번호 발급";
			content = "로그인 후 꼭 정보를 수정해주세요. [ "+authcode+" ]"; //내용
		}
		
		try {
			
			//메일 작업처리를 위해 세션을 생성해야하는데 SMTP와 관련된 세션을 생성하기 위해서 SMTP 서버와 관련된 정보 지정해야 함.
			//Properies 클래스를 사용해 세션을 생성할 때 필요한 값 지정, 설정
			Properties props = new Properties();
			props.put("mail.smtp.starttls.enable","true"); //암호화 메일전송을 위한 프로토콜 사용여부
			props.put("mail.transport.protocol","smtp"); //SMTP 전송서버 이용
			props.put("mail.smtp.host",host); //이메일을 발송해줄 SMTP 서버
			props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); //소켓설정
			props.put("mail.smtp.port","465"); //포트
			props.put("mail.smtp.user",from); //사용자
			props.put("mail.smtp.auth","true"); //사용자인증
			
			Session mailSession = Session.getInstance(props,
					new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(username, password);
						}
			}); //Properties 객체로 메일세션 객체 생성(사용자 아이디와 비밀번호를 통한 인증)
			
			Message msg = new MimeMessage(mailSession); //메시지 객체 생성
			msg.setFrom(new InternetAddress(from, MimeUtility.encodeText(fromName,"UTF-8","B")));
			
			InternetAddress[] address1 = { new InternetAddress(to1)}; 
			msg.setRecipients(Message.RecipientType.TO, address1); //보낼 이메일 설정(다수 설정 가능)
			msg.setSubject(subject); //메시지 제목설정
			msg.setSentDate(new java.util.Date()); //메시지 날짜 설정
			msg.setContent(content,"text/html;charset=UTF-8"); //메시지 내용 설정
		
			Transport.send(msg); //메시지 보내기
			
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