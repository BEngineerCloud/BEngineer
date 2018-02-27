package bengineer.spring.web;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.mybatis.spring.SqlSessionTemplate;
import org.rosuda.REngine.Rserve.RConnection;
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
	private static long scanrate = 60000L;
	public static long getScanrate() {
		return scanrate;
	}

	public static void setScanrate(long scanrate) {
		MainBean.scanrate = scanrate;
	}
	@Autowired
	private SqlSessionTemplate sqlSession = null;
	
	@RequestMapping("beMain.do") // 메인페이지 이동
	public String main(HttpSession session, Model model) {
		
		//id 세션값이 없으면 로그인 페이지로 이동
		if(loginCheck(session)) { 
			return "redirect:/beMember/beLogin.do";
		}else {
			String id = (String)session.getAttribute("id");
			model.addAttribute("content", returnWC2(id));
			FileBean filebean = new FileBean();
			model.addAttribute("space", filebean.viewSpace(id, sqlSession));
			List font = sqlSession.selectList("bengineer.font", id);
	 		model.addAttribute("font",font);	// 검색에 필요한 파일목록들
			return "beMain";
		}
	}
	
	@RequestMapping(value="beMaintemp.do") //네이버아이디 로그인을 했을 시 beMain.do로 이동
	public String beMaintemp(MemberDTO dto, HttpSession session, Model model) {
		String email = dto.getEmail();
		
		//받아온 메일 아이디로 멤버가 존재하는 지 체크
		Integer check = (Integer)sqlSession.selectOne("bengineer.beCheckmailid",email);
		Integer impose = (Integer)sqlSession.selectOne("bengineer.imposeMember", email);
		String view= "redirect:/beMain.do"; //기본은 beMain.do로 이동
		if(impose==1) {
			view="redirect:/imposeMember.do?email=" + email;
		}else {
			if(check!=1) { //일치하는 멤버가 없을 시
				sqlSession.insert("bengineer.beInsertmember", dto); //네이버회원 정보를 insert
			}else {
				String id = sqlSession.selectOne("bengineer.beSelectid",email); //해당하는 회원id를 받아옴.
				
				if(!id.equals(dto.getId())) { //회원id와 넘겨받은 네이버회원 id가 다를 경우
					
					//FileBean에 makecode함수를 사용해야하는데 makecode함수에서 sql문을 사용하기 때문에 Filebean객체를 생성한 후 sqlSession도 설정해준다.
					FileBean filebean = new FileBean();
					filebean.setSqlSession(sqlSession);
					
					Integer folderCheck = (Integer)sqlSession.selectOne("bengineer.beCheckfolder",id); // id에 해당하는 폴더가 있는지
					
					// 아이디에 해당하는 폴더가 있으면 폴더의 소유자를 네이버회원 ID로 변경
					if(folderCheck==1) { 
						filebean.changeIdDirName(id, dto.getId()); //파일명 바꾸기
						sqlSession.update("begineer.updateowner",dto.getId()); //파일소유자 변경
					}
					sqlSession.update("bengineer.beUpdatemember2",dto); //네이버회원 ID로 정보 변경
				}
			}
			session.setAttribute("id", dto.getId()); //id 세션설정
			session.setAttribute("nickname", dto.getNickname()); //닉네임 세션 설정
			session.setMaxInactiveInterval(60); //세션유효시간 60초
		}
		return view;
	}
	
	@RequestMapping("imposeMember.do") // 메인페이지
	public String imposeMember(Model model, String email) {
		ImposeDTO member = sqlSession.selectOne("manager.imposeCause", email);
		model.addAttribute("cause", member.getCause());
		model.addAttribute("date", member.getEndDay());
		return "beMember/imposeMember";
	}
	
	@RequestMapping("beLogout.do") //로그아웃 페이지 이동
	public String beLogout(HttpSession session) {
		session.invalidate(); //모든 세션 삭제
		return "redirect:/beMain.do";
	}
	
	@RequestMapping("shortcut.do")
	public String shortcut(HttpSession session, Model model, String filename, int hitcount) {
		if(loginCheck(session)) {return "redirect:/beMember/beLogin.do";}
		String id = (String)session.getAttribute("id");
		FileDTO dto = new FileDTO();
		dto.setFilename(filename);
		dto.setHitcount(hitcount);
		dto.setOwner(id);
		List filelist = sqlSession.selectList("bengineer.shortcut", dto);
		if(filelist.size() == 1) {
			dto = (FileDTO)filelist.get(0);
			if(dto.getFiletype().equals("dir")) {
				return "redirect:/beFiles/beMyList.do?folder=" + dto.getNum();
			}else {
				return "redirect:/beFiles/beDownload.do?file_ref=" + dto.getNum();
			}
		}else {
			model.addAttribute("list", filelist);
		}
		List folderaddress = new ArrayList(); // 폴더 경로를 하나씩 저장하기 위한 리스트
		List orgaddress = new ArrayList(); // 폴더주소에 저장된 각각의 폴더에 대한 실제 경로를 하나씩 저장하기 위한 리스트
		folderaddress.add("조건에 맞는 파일이 다수 있습니다.");
		orgaddress.add(null);
		model.addAttribute("folderaddress", folderaddress);
		model.addAttribute("orgaddress", orgaddress);
		model.addAttribute("folder_ref", -5);
		model.addAttribute("folder", 0); // 상위폴더로 이동하기 위해
		model.addAttribute("movefile_Ref",0);
		model.addAttribute("movefile_FRef",0);
		FileBean filebean = new FileBean();
		model.addAttribute("space", filebean.viewSpace(id, sqlSession));
		List font = sqlSession.selectList("bengineer.font", id);
 		model.addAttribute("font",font);	// 검색에 필요한 파일목록들
		return "beFiles/beList";
	}
	public static boolean loginCheck(HttpSession session) { // 로그인 체크용 메서드, 세션에 nickname 세션이 정상적으로 있지 않을 경우 true  
		String id = (String)session.getAttribute("id");
		return id == null || id.equals("null") || id.equals("");
	}
	private String returnWC2(String id) {
		RConnection r = null;
		String retStr = "";
		List filelist = sqlSession.selectList("bengineer.getfilesforhits", id); // 내 파일, 공유 파일 중 클릭수가 10이상인 파일 목록 가져오기  클릭 수 내림차순  
		if(filelist.size() == 0) {
			return "자주 찾는 파일이 없습니다.";
		}
		String Fname = "c("; // 파일 이름
		String Fhit = "c("; // 파일 클릭 수
		for(int i = 0; i < filelist.size(); i++) {
			FileDTO dto = (FileDTO)filelist.get(i);
			int hit = dto.getHitcount();
			String filename = dto.getFilename();
			Fname += "'" + filename + "'";
			Fhit += hit;
			if(i != filelist.size() - 1) {
				Fname += ",";
				Fhit += ",";
			}else {
				Fname += ")";
				Fhit += ")";
			}
		}
		if(Fhit.equals("c()")) {
			return "자주 찾는 파일이 없습니다.";
		}
		try {
			r = new RConnection();
			r.eval("library(wordcloud2)");
			r.eval("library(htmltools)");
			r.eval("Fname <- " + Fname);
			r.eval("Fhit <- " + Fhit);
			r.eval("table <- as.table(Fhit)"); // 파일 클릭 수 벡터를 표로 변환
			r.eval("names(table) <- Fname"); // 변환한 표의 컬럼에 파일이름 입력
			r.eval("my_cloud <- wordcloud2(table, size = 0.8, color = 'random-light')"); // 워드클라우드 생성
			r.eval("Sys.sleep(0.5)"); // 생성에 시간이 걸릴것을 고려하여 잠깐 대기
			r.eval("my_path <- renderTags(my_cloud)");
			retStr = r.eval("my_path$html").asString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			r.close();
		}
		return retStr;
	}
	public void run(){
		//File bengineer = new File("d:/PM/BEngineer");
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
			/*
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
			}*/
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(calendar.MONTH, -1);
			List trashlist = sqlSession.selectList("bengineer.oldtrash", calendar.getTime());
			if(trashlist.size() > 0) {
				for(int i = 0; i < trashlist.size(); i++) {
					TrashcanDTO trash = (TrashcanDTO)trashlist.get(i);
					int num = trash.getFilenum();
					FileDTO dto = (FileDTO)sqlSession.selectOne("bengineer.getaddr", num);
					String address = "d:/PM/BEngineer/" + dto.getOwner() + "/betrashcan/" + dto.getNum() + ".zip";
					sqlSession.delete("bengineer.deletetrash", num);
					sqlSession.delete("bengineer.deletefile", num);
					File trashfile = new File(address);
					trashfile.delete();
				}
			}
			sqlSession.delete("bengineer.deleteoldkey");
			sqlSession.delete("bengineer.deleteunshared");
			try {
				sleep(scanrate);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
