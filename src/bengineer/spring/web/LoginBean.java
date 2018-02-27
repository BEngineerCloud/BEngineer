package bengineer.spring.web;
//**로그인 Bean**//
import javax.servlet.http.HttpSession;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller()
@RequestMapping("/beMember/*")
public class LoginBean {
	@Autowired
	private SqlSessionTemplate sqlSession = null;
	
	@RequestMapping("beLogin.do") //로그인 페이지 이동
	public String beLogin() {return "beMember/beLogin";}
	
	@RequestMapping("beLoginpro.do") //일반 로그인을 했을 시 정보가 맞는지 확인
	public String beLoginpro(MemberDTO dto,Model model, HttpSession session) {
		String email = dto.getEmail();
		
		//받아온 메일 아이디로 멤버가 존재하는 지 체크
		Integer check = (Integer)sqlSession.selectOne("bengineer.beCheckmailid",email);
		Integer impose = (Integer)sqlSession.selectOne("bengineer.imposeMember", email);
		String view = "beFiles/alert";

		if(impose==1) {
			view="redirect:/imposeMember.do?email=" + email;
		}else if(check==1) { //일치하는 멤버가 존재할 시
			String pw = sqlSession.selectOne("bengineer.beSelectpw",email); //비밀번호를 받아옴.
			if(pw.equals(dto.getPw())) { //받아온 비밀번호와 멤버의 비밀번호가 일치할 시
				dto = sqlSession.selectOne("bengineer.beSelectmember2",email); //멤버DTO를 받아옴.
				
				//받아온 멤버DTO의 아이디와 닉네임을 세션으로 설정
				session.setAttribute("id", dto.getId()); 
				session.setAttribute("nickname", dto.getNickname());
				
				//'로그인 성공'이라는 문구와 함께 메인페이지로 이동
				model.addAttribute("alert", "로그인 성공!!");
				model.addAttribute("location", "\"/BEngineer/beMain.do\"");
			}else {
				//'비밀번호가 틀렸습니다'라는 문구와 함께 다시 로그인페이지로 이동
				model.addAttribute("alert", "비밀번호가 틀렸습니다!!");
				model.addAttribute("location", "history.go(-1)");
			}
		}else { //일치하는 멤버가 존재하지 않을 시
			//'가입된 메일주소가 맞는지 확인해주십시오.'라는 문구와 함께 다시 로그인페이지로 이동
			model.addAttribute("alert", "가입된 메일주소가 맞는지 확인해주십시오.");
			model.addAttribute("location", "history.go(-1)");
		}
		
		return view;
	}
	
	@RequestMapping("beRequestprofile.do") //네이버아이디로그인 시 네이버에서 받아온 회원정보를 전달
	public String beRequestprofile() {return "beMember/beRequestprofile";}
	
	@RequestMapping("beCollback.do") //콜백페이지를 호출했을 시 로그인페이지로 이동
	public String beCollback() {return "beMember/beLogin";}
	
	
	@RequestMapping("beJoinmember.do") //회원가입 페이지로 이동
	public String beJoinmember(Model model, @RequestParam(value="email", defaultValue="0") String email, @RequestParam(value="checkConfirmEmail", defaultValue="0") String checkConfirmEmail) {
	//이메일주소를 인증받고나서 다시 호출할 때 이메일주소의 값과 이메일주소인증을 체크를 해서 보내줘야되기 때문에 메서드 변수로 email, checkConfirmEmail이 쓰이며 아무값도 넘겨주지 않을 시 기본값으로 '0'을 받는다.	
		
		//받아온 email변수와 checkConfirmEmail 변수가 둘 다 '0'일 시 초기화해서 회원가입페이지에 같이 보낸다.
		if(email.equals("0") && checkConfirmEmail.equals("0")) {
			model.addAttribute("email",email);
			model.addAttribute("checkConfirmEmail",checkConfirmEmail);
		}
		
		return "beMember/beJoinmember";
	}	
		
	@RequestMapping("beJoinmemberpro.do") //회원가입 정보를 DB에 insert
	public String beJoinmemberpro(MemberDTO dto, HttpSession session) {
		
		//FileBean에 makecode함수를 사용해야하는데 makecode함수에서 sql문을 사용하기 때문에 Filebean객체를 생성한 후 sqlSession도 설정해준다.
		FileBean filebean = new FileBean();
		filebean.setSqlSession(sqlSession);
		
		String id="";
		id = filebean.makecode(8); //일반회원가입 시 임의로 id를 만들어 준다. (네이버아이디로그인을 통해 로그인 할 경우에는 네이버에 고유한 id가  DB에 insert된다.)
		
		Integer checkId = (Integer)sqlSession.selectOne("bengineer.beChecklogin",id); //생성한 id문자열이 중복되는 지 확인
		
		//중복일 시 중복이 안되는 id문자열을 생성할 때까지 반복한다.
		while(checkId>0) {
			id = filebean.makecode(8);
			checkId = (Integer)sqlSession.selectOne("bengineer.beChecklogin",id);
		}
		
		dto.setId(id); //dto의 id를 설정
		sqlSession.insert("bengineer.beInsertmember2", dto); //회원가입 정보를 insert
		
		return "beMember/beJoinmemberpro";
	}	
	
	//비밀번호 찾기
	@RequestMapping("beSearchpw.do")
	public String beSearchpw() { return "beMember/beSearchpw"; }
		
	//회원정보 받아서 임시비밀번호를 메일로 보냄
	@RequestMapping("beSearchpwpro.do")
	public String beSearchpwpro(MemberDTO dto, Model model) {
			
		//FileBean에 makecode함수를 사용해야하는데 makecode함수에서 sql문을 사용하기 때문에 FileBean객체를 생성한 후 sqlSession도 설정해준다.
		FileBean filebean = new FileBean();
		filebean.setSqlSession(sqlSession);
		
		MemberBean memberbean = new MemberBean();
		
		Integer checkPw = (Integer)sqlSession.selectOne("bengineer.beSearchpw",dto); //받아온 메일아이디와 닉네임 정보로 회원이 존재하는 지 검사
			
		//회원이 존재하면 임시비밀번호를 생성한 후 메일로 보내고 sql update
		if(checkPw>0) { 
			String pw = "";
			pw = filebean.makecode(10);
			memberbean.sendEmail(dto.getEmail(), pw, "임시비밀번호");
				
			dto.setPw(pw);
			sqlSession.update("bengineer.beUpdatepw",dto);
				
			model.addAttribute("alert", "임시비밀번호를 메일로 보냈습니다.");
			model.addAttribute("location", "\"/BEngineer/beMember/beLogin.do\"");
		}else {
			model.addAttribute("alert", "일치하는 회원정보가 없습니다.");
			model.addAttribute("location", "history.go(-1)");
		}
		
		return "beFiles/alert"; 
	}
}
