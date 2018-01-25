package bengineer.spring.web;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import bengineer.spring.manager.ImposeDTO;


@Controller
public class MainBean extends Thread{
	public MainBean() {
		start();
	}
	@Autowired
	private SqlSessionTemplate sqlSession = null;
	@RequestMapping("beMain.do") // 메인페이지
	public String main(HttpSession session) {
		if(loginCheck(session)) {
			return "redirect:/beMember/beLogin.do";
		}else {
			return "beMain";
		}
	}
	
	@RequestMapping(value="beMaintemp.do") // 메인페이지
	public String beMaintemp(MemberDTO dto, HttpSession session, Model model) {
		String email = dto.getEmail();
		Integer check = (Integer)sqlSession.selectOne("bengineer.beChecklogin",dto.getId());
		Integer impose = (Integer)sqlSession.selectOne("bengineer.imposeMember", email);
		String veiw= "redirect:/beMain.do";
		if(impose==1) {
			veiw="redirect:/beMember/imposeMember.do";
		}else {
		if(check!=1) {
			sqlSession.insert("bengineer.beInsertmember", dto);
		}
		session.setAttribute("id", dto.getId()); // 테스트용임시세션등록
		session.setAttribute("nickname", dto.getNickname());
		}
		return veiw;
	}
	@RequestMapping("imposeMember.do") // 메인페이지
	public String imposeMember() {return "beMember/imposeMember";}
	
	@RequestMapping("beLogout.do") // 메인페이지
	public String beLogout(HttpSession session) {
		session.invalidate();
		return "redirect:/beMain.do";
	}
	
	public static boolean loginCheck(HttpSession session) { // 로그인 체크용 메서드, 세션에 nickname 세션이 정상적으로 있지 않을 경우 true  
		String id = (String)session.getAttribute("id");
		return id == null || id.equals("null") || id.equals("");
	}
	public void run(){
		File bengineer = new File("d:/PM/BEngineer");
		while(true) {
			if(sqlSession == null) {
				try {
					sleep(1000L);
				}catch(Exception e) {
					e.printStackTrace();
				}
				continue;
			}
			Date date = new Date(System.currentTimeMillis());
			SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmssZ");
			long time = Long.parseLong(format.format(date).substring(0, 12));
			File [] folders = bengineer.listFiles();
			for(int i = 0; i < folders.length; i++) {
				File folder = folders[i];
				if(folder.getName().equals("downtemp")) {
					File [] tempfiles = folder.listFiles();
					for(int j = 0; j < tempfiles.length; j++) {
						File tempfile = tempfiles[j];
						String tempfilename = tempfile.getName();
						tempfilename = tempfilename.substring(0, tempfilename.lastIndexOf("."));
						int num = tempfilename.length();
						long temptime = Long.parseLong(tempfilename.substring(num - 12, num));
						if(time - temptime > 10000) {
							tempfile.delete();
						}
					}
				}else {
					File trashcan = new File(folder.getAbsolutePath() + "/betrashcan");
					File [] trashes = trashcan.listFiles();
					if(trashes != null) {
						for(int j = 0; j < trashes.length; j++) {
							File trash = trashes[j];
							String trashnum = trash.getName();
							trashnum = trashnum.substring(0, trashnum.lastIndexOf("."));
							int num = Integer.parseInt(trashnum);
							FileDTO check = (FileDTO)sqlSession.selectOne("bengineer.getaddr", num);
							if(check == null) {
								trash.delete();
								sqlSession.delete("bengineer.deletetrash", num);
							}else {
								Date deletedate = (Date)sqlSession.selectOne("bengineer.checktrash", num);
								long deletetime = Long.parseLong(format.format(deletedate).substring(0, 12));
								if(time - deletetime > 100000000) {
									trash.delete();
									sqlSession.delete("bengineer.deletetrash", num);
									sqlSession.delete("bengineer.deletefile", num);
								}
							}
						}
					}
				}
			}
			sqlSession.delete("bengineer.deleteoldkey");
			try {
				sleep(60000L);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
