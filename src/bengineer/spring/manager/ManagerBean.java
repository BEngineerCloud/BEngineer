package bengineer.spring.manager;

import java.util.Base64;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import bengineer.spring.filter.*;
import bengineer.spring.web.FileDTO;
import bengineer.spring.web.MainBean;

import org.apache.catalina.User;
import org.mybatis.spring.SqlSessionTemplate;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.Rserve.RConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.rosuda.REngine.*;
import org.rosuda.REngine.Rserve.*;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
@Controller
@RequestMapping("/manager/")
public class ManagerBean {
	
	@Autowired
	private SqlSessionTemplate sqlSession = null;

	// 매니저 로그인창
	@RequestMapping("login.do")
	public String main() { return "/manager/login"; }
	// 로그인체크
	@RequestMapping("loginPro.do")
	public String loginPro(ManagerDTO dto,HttpSession session) {
		Integer check = (Integer)sqlSession.selectOne("loginCheck",dto);
		String view = "redirect:/manager/login.do";
		if(check==1) {		// 로그인 체크, id,pw 일치시 1 반환
			session.setAttribute("Id", dto.getId());
			view="redirect:/manager/mMain.do";
		}
		return view;
	}	
	// 로그아웃
	@RequestMapping("logout.do")
	public String logout(HttpSession session) { 
		session.invalidate();
		return "redirect:/manager/login.do"; 
	}
	
	// 메인창
	@RequestMapping("mMain.do")
	public String mMain(ManagerDTO dto,HttpSession session,String Id,ManagerDTO m, Model model) {
		String ss = (String)session.getAttribute("Id");
		FilenameFilter namefilterlist = new FilenameFilter();
		model.addAttribute("namefilterings", namefilterlist.getFilterings());
		ImagetypeFilter imagefilterlist = new ImagetypeFilter();
		model.addAttribute("imagefilterings", imagefilterlist.getFilterings());
		VideotypeFilter videofilterlist = new VideotypeFilter();
		model.addAttribute("videofilterings", videofilterlist.getFilterings());
		MusictypeFilter musicfilterlist = new MusictypeFilter();
		model.addAttribute("musicfilterings", musicfilterlist.getFilterings());
		DocumenttypeFilter documentfilterlist = new DocumenttypeFilter();
		model.addAttribute("documentfilterings", documentfilterlist.getFilterings());
		EtctypeFilter etcfilterlist = new EtctypeFilter();
		model.addAttribute("etcfilterings", etcfilterlist.getFilterings());
		model.addAttribute("scanrate", MainBean.getScanrate());
		if(ss==null) {
			return "redirect:/manager/login.do";
		}else{
			return "/manager/mMain";
		}
	}
	@RequestMapping("addNameFilter.do")
	public String addNameFilter(HttpSession session, Model model, String filtering) {
		String ss = (String)session.getAttribute("Id");
		if(ss==null) {
			return "redirect:/manager/login.do";
		}
		FilenameFilter filterlist = new FilenameFilter();
		if(filterlist.nameFilter(filtering)) {
			model.addAttribute("alert", "\\'" + filtering + "\\'은/는 이미 필터링 단어로 등록되어있습니다.");
		}else {
			filterlist.addFilterings(filtering);
			model.addAttribute("alert", "\\'" + filtering + "\\'을/를 필터링 단어로 추가하였습니다.");
		}
		model.addAttribute("location", "\"/BEngineer/manager/mMain.do\"");
		return "beFiles/alert";
	}
	@RequestMapping("removeNameFilter.do")
	public String removeNameFilter(HttpSession session, Model model, String filtering) {
		String ss = (String)session.getAttribute("Id");
		if(ss==null) {
			return "redirect:/manager/login.do";
		}
		FilenameFilter filterlist = new FilenameFilter();
		if(filterlist.removeFilterings(filtering)) {
			model.addAttribute("alert", "\\'" + filtering + "\\'을/를 필터링 단어에서 해제하였습니다.");
		}else {
			model.addAttribute("alert", "\\'" + filtering + "\\'은/를 필터링 단어가 아닙니다.");
		}
		model.addAttribute("location", "\"/BEngineer/manager/mMain.do\"");
		return "beFiles/alert";
	}
	@RequestMapping("addImageFilter.do")
	public String addImageFilter(HttpSession session, Model model, String filtering) {
		String ss = (String)session.getAttribute("Id");
		if(ss==null) {
			return "redirect:/manager/login.do";
		}
		ImagetypeFilter filterlist = new ImagetypeFilter();
		if(!filtering.startsWith(".")) {
			filtering = "." + filtering;
		}
		if(filterlist.typeFilter(filtering)) {
			model.addAttribute("alert", "\\'" + filtering + "\\'은/는 이미 그림파일 확장자로 등록되어있습니다.");
		}else {
			filterlist.addFilterings(filtering);
			model.addAttribute("alert", "\\'" + filtering + "\\'을/를  그림파일 확장자로 추가하였습니다.");
		}
		model.addAttribute("location", "\"/BEngineer/manager/mMain.do\"");
		return "beFiles/alert";
	}
	@RequestMapping("removeImageFilter.do")
	public String removeImageFilter(HttpSession session, Model model, String filtering) {
		String ss = (String)session.getAttribute("Id");
		if(ss==null) {
			return "redirect:/manager/login.do";
		}
		ImagetypeFilter filterlist = new ImagetypeFilter();
		if(!filtering.startsWith(".")) {
			filtering = "." + filtering;
		}
		if(filterlist.removeFilterings(filtering)) {
			model.addAttribute("alert", "\\'" + filtering + "\\'을/를  그림파일 확장자에서 해제하였습니다.");
		}else {
			model.addAttribute("alert", "\\'" + filtering + "\\'은/는 그림파일 확장자가 아닙니다.");
		}
		model.addAttribute("location", "\"/BEngineer/manager/mMain.do\"");
		return "beFiles/alert";
	}
	@RequestMapping("addVideoFilter.do")
	public String addVideoFilter(HttpSession session, Model model, String filtering) {
		String ss = (String)session.getAttribute("Id");
		if(ss==null) {
			return "redirect:/manager/login.do";
		}
		VideotypeFilter filterlist = new VideotypeFilter();
		if(!filtering.startsWith(".")) {
			filtering = "." + filtering;
		}
		if(filterlist.typeFilter(filtering)) {
			model.addAttribute("alert", "\\'" + filtering + "\\'은/는 이미 영상파일 확장자로 등록되어있습니다.");
		}else {
			filterlist.addFilterings(filtering);
			model.addAttribute("alert", "\\'" + filtering + "\\'을/를 영상파일 확장자로 추가하였습니다.");
		}
		model.addAttribute("location", "\"/BEngineer/manager/mMain.do\"");
		return "beFiles/alert";
	}
	@RequestMapping("removeVideoFilter.do")
	public String removeVideoFilter(HttpSession session, Model model, String filtering) {
		String ss = (String)session.getAttribute("Id");
		if(ss==null) {
			return "redirect:/manager/login.do";
		}
		VideotypeFilter filterlist = new VideotypeFilter();
		if(!filtering.startsWith(".")) {
			filtering = "." + filtering;
		}
		if(filterlist.removeFilterings(filtering)) {
			model.addAttribute("alert", "\\'" + filtering + "\\'을/를 영상파일 확장자에서 해제하였습니다.");
		}else {
			model.addAttribute("alert", "\\'" + filtering + "\\'은/는 영상파일 확장자가 아닙니다.");
		}
		model.addAttribute("location", "\"/BEngineer/manager/mMain.do\"");
		return "beFiles/alert";
	}
	@RequestMapping("addMusicFilter.do")
	public String addMusicFilter(HttpSession session, Model model, String filtering) {
		String ss = (String)session.getAttribute("Id");
		if(ss==null) {
			return "redirect:/manager/login.do";
		}
		MusictypeFilter filterlist = new MusictypeFilter();
		if(!filtering.startsWith(".")) {
			filtering = "." + filtering;
		}
		if(filterlist.typeFilter(filtering)) {
			model.addAttribute("alert", "\\'" + filtering + "\\'은/는 이미 음악파일 확장자로 등록되어있습니다.");
		}else {
			filterlist.addFilterings(filtering);
			model.addAttribute("alert", "\\'" + filtering + "\\'을/를 음악파일 확장자로 추가하였습니다.");
		}
		model.addAttribute("location", "\"/BEngineer/manager/mMain.do\"");
		return "beFiles/alert";
	}
	@RequestMapping("removeMusicFilter.do")
	public String removeMusicFilter(HttpSession session, Model model, String filtering) {
		String ss = (String)session.getAttribute("Id");
		if(ss==null) {
			return "redirect:/manager/login.do";
		}
		MusictypeFilter filterlist = new MusictypeFilter();
		if(!filtering.startsWith(".")) {
			filtering = "." + filtering;
		}
		if(filterlist.removeFilterings(filtering)) {
			model.addAttribute("alert", "\\'" + filtering + "\\'을/를 음악파일 확장자에서 해제하였습니다.");
		}else {
			model.addAttribute("alert", "\\'" + filtering + "\\'은/는 음악파일 확장자가 아닙니다.");
		}
		model.addAttribute("location", "\"/BEngineer/manager/mMain.do\"");
		return "beFiles/alert";
	}
	@RequestMapping("addDocumentFilter.do")
	public String addDocumentFilter(HttpSession session, Model model, String filtering) {
		String ss = (String)session.getAttribute("Id");
		if(ss==null) {
			return "redirect:/manager/login.do";
		}
		DocumenttypeFilter filterlist = new DocumenttypeFilter();
		if(!filtering.startsWith(".")) {
			filtering = "." + filtering;
		}
		if(filterlist.typeFilter(filtering)) {
			model.addAttribute("alert", "\\'" + filtering + "\\'은/는 이미 문서파일 확장자로 등록되어있습니다.");
		}else {
			filterlist.addFilterings(filtering);
			model.addAttribute("alert", "\\'" + filtering + "\\'을/를 문서파일 확장자로 추가하였습니다.");
		}
		model.addAttribute("location", "\"/BEngineer/manager/mMain.do\"");
		return "beFiles/alert";
	}
	@RequestMapping("removeDocumentFilter.do")
	public String removeDocumentFilter(HttpSession session, Model model, String filtering) {
		String ss = (String)session.getAttribute("Id");
		if(ss==null) {
			return "redirect:/manager/login.do";
		}
		DocumenttypeFilter filterlist = new DocumenttypeFilter();
		if(!filtering.startsWith(".")) {
			filtering = "." + filtering;
		}
		if(filterlist.removeFilterings(filtering)) {
			model.addAttribute("alert", "\\'" + filtering + "\\'을/를 문서파일 확장자에서 해제하였습니다.");
		}else {
			model.addAttribute("alert", "\\'" + filtering + "\\'은/는 문서파일 확장자가 아닙니다.");
		}
		model.addAttribute("location", "\"/BEngineer/manager/mMain.do\"");
		return "beFiles/alert";
	}
	@RequestMapping("addEtcFilter.do")
	public String addEtcFilter(HttpSession session, Model model, String filtering) {
		String ss = (String)session.getAttribute("Id");
		if(ss==null) {
			return "redirect:/manager/login.do";
		}
		EtctypeFilter filterlist = new EtctypeFilter();
		if(!filtering.startsWith(".")) {
			filtering = "." + filtering;
		}
		if(filterlist.typeFilter(filtering)) {
			model.addAttribute("alert", "\\'" + filtering + "\\'은/는 이미 기타파일 확장자로 등록되어있습니다.");
		}else {
			filterlist.addFilterings(filtering);
			model.addAttribute("alert", "\\'" + filtering + "\\'을/를 기타파일 확장자로 추가하였습니다.");
		}
		model.addAttribute("location", "\"/BEngineer/manager/mMain.do\"");
		return "beFiles/alert";
	}
	@RequestMapping("removeEtcFilter.do")
	public String removeEtcFilter(HttpSession session, Model model, String filtering) {
		String ss = (String)session.getAttribute("Id");
		if(ss==null) {
			return "redirect:/manager/login.do";
		}
		EtctypeFilter filterlist = new EtctypeFilter();
		if(!filtering.startsWith(".")) {
			filtering = "." + filtering;
		}
		if(filterlist.removeFilterings(filtering)) {
			model.addAttribute("alert", "\\'" + filtering + "\\'을/를 기타파일 확장자에서 해제하였습니다.");
		}else {
			model.addAttribute("alert", "\\'" + filtering + "\\'은/는 기타파일 확장자가 아닙니다.");
		}
		model.addAttribute("location", "\"/BEngineer/manager/mMain.do\"");
		return "beFiles/alert";
	}
	@RequestMapping("setScanrate.do")
	public String setScanrate(HttpSession session, Model model, long scanrate) {
		String ss = (String)session.getAttribute("Id");
		if(ss==null) {
			return "redirect:/manager/login.do";
		}
		MainBean.setScanrate(scanrate);
		model.addAttribute("alert", "삭제 주기를 " + scanrate + "ms로 변경합니다.");
		model.addAttribute("location", "\"/BEngineer/manager/mMain.do\"");
		return "beFiles/alert";
	}
	//서버 사용량 확인
	@RequestMapping("charge.do")
	public String charge(Model model) { 
		List list = sqlSession.selectList("manager.server");	// 개인별 총 사용량
		int all = sqlSession.selectOne("manager.all");			// 전체 사용량
		model.addAttribute("list",list);
		model.addAttribute("all",all);
		RConnection r = null;
		try {
			r = new RConnection();
			r.eval("png('rr.png')");
			String Fsize ="c(";
			for(int i=list.size()-1; i>=0;i--) {		// 내림차순
				FileDTO file1 = (FileDTO)list.get(i);
				if(i==0) {
					Fsize+=file1.getFilesize()+")";
				}else {
					Fsize+=file1.getFilesize()+",";
				}
			}
			r.eval("Fsize<-"+Fsize);
			r.eval("par(bg = 'transparent')");		// 배경 투명색
			r.eval("barplot(Fsize,col=rainbow(7),horiz=T)");	// 7가지색,가로막대
			r.eval("dev.off()");
			REXP image = r.eval("r<-readBin('rr.png', 'raw', 50*50)");
			model.addAttribute("rr",Base64.getEncoder().encodeToString(image.asBytes()));
		}catch(Exception e) {e.printStackTrace();}finally {
			r.close();
		}
		return "/manager/charge"; 
	}
}
