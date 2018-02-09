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
	@RequestMapping("beMain.do") // 메인페이지
	public String main(HttpSession session, Model model) {
		session.setAttribute("id", "test9");  //@@@
		session.setAttribute("nickname", "test9");
		if(loginCheck(session)) {
			return "redirect:/beMember/beLogin.do";
		}else {
			String id = (String)session.getAttribute("id");
			//@@ model.addAttribute("content", returnWC2(id));
			FileBean filebean = new FileBean();
			//@@ model.addAttribute("space", filebean.viewSpace(id, sqlSession));
			return "beMain";
		}
	}
	
	@RequestMapping(value="beMaintemp.do") // 메인페이지
	public String beMaintemp(MemberDTO dto, HttpSession session, Model model) {
		String email = dto.getEmail();
		Integer check = (Integer)sqlSession.selectOne("bengineer.beCheckmailid",email);
		Integer impose = (Integer)sqlSession.selectOne("bengineer.imposeMember", email);
		String veiw= "redirect:/beMain.do";
		if(impose==1) {
			veiw="redirect:/beMember/imposeMember.do";
		}else {
			if(check!=1) {
				System.out.println(1);
				sqlSession.insert("bengineer.beInsertmember", dto);
			}else {
				String id = sqlSession.selectOne("bengineer.beSelectid",email);
				if(!id.equals(dto.getId())) {
					FileBean filebean = new FileBean();
					filebean.setSqlSession(sqlSession);
					Integer folderCheck = (Integer)sqlSession.selectOne("bengineer.beCheckfolder",id); // id에 해당하는 폴더가 있는지
					if(folderCheck==1) { // 아이디에 해당하는 폴더가 있으면 새로운 아이디로 수정
						filebean.changeIdDirName(id, dto.getId());
						sqlSession.update("begineer.updateowner",dto.getId());
					}
					sqlSession.update("bengineer.beUpdatemember2",dto);
				}
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
		//@@ model.addAttribute("space", filebean.viewSpace(id, sqlSession));
		return "beFiles/beList";
	}
	public static boolean loginCheck(HttpSession session) { // 로그인 체크용 메서드, 세션에 nickname 세션이 정상적으로 있지 않을 경우 true  
		String id = (String)session.getAttribute("id");
		return id == null || id.equals("null") || id.equals("");
	}
	/* @@@
	private String returnWC2(String id) {
		RConnection r = null;
		String retStr = "";
		List filelist = sqlSession.selectList("bengineer.getfilesforhits", id);
		if(filelist.size() == 0) {
			return "자주 찾는 파일이 없습니다.";
		}
		String Fname = "c(";
		String Fhit = "c(";
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
			r.eval("table <- as.table(Fhit)");
			r.eval("names(table) <- Fname");
			r.eval("my_cloud <- wordcloud2(table, size = 1.1, color = 'random-light')");
			r.eval("Sys.sleep(0.7)");
			r.eval("my_path <- renderTags(my_cloud)");
			retStr = r.eval("my_path$html").asString();
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		} finally {
			r.close();
		}
		return retStr;
	}
	@@@ */
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
