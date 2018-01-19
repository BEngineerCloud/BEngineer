package bengineer.spring.web;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.nio.channels.FileChannel;
import java.nio.file.*;

import javax.servlet.http.HttpSession;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/beFiles/")
public class FileBean2 {
	
	
	@Autowired
	private SqlSessionTemplate sqlSession = null;
	
	@RequestMapping("autoArrangefile.do") //파일 자동정리
	public String autoArrangefile(HttpSession session, Model model) {
		if(MainBean.loginCheck(session)) {return "redirect:/beMember/beLogin.do";} // 비 로그인 상태시 로그인 창으로 리디렉트
		String owner = (String)session.getAttribute("id");
		String filePath=""; //파일경로
		String fileName=""; //파일이름
		String fileType=""; //파일확장자
		String moveFolder=""; //이동할 파일폴더
		File path = new File("d:/PM/BEngineer/" + owner); //사용자폴더 경로
		File[] list = path.listFiles(); //사용자폴더 안에 모든 파일/폴더 받아오기
		FileBean filebean = new FileBean();
		FileDTO dto = new FileDTO();
		dto.setOwner(owner);
		if(list.length > 0){
		    for(int i=0; i < list.length; i++){
		    	if(list[i].isFile()) { //파일인지 판단, 파일이면
		    		fileName=list[i].getName();
		    		dto.setOrgname(fileName);
		    		filePath=list[i].getPath();
		    		fileType = fileName.substring(fileName.lastIndexOf("."));
		    		moveFolder = filebean.checkFile(fileType); //파일확장자 범주에 들어가는 폴더이름	
		    		Integer moveParent = moveFile(filePath, fileName, moveFolder, owner); //파일 이동
		    		if( moveParent!=null) {
		    			dto.setNum(moveParent);
		    			sqlSession.update("bengineer.autoarrange",dto);
		    		}
		    	}
		    }
		}
		model.addAttribute("alert", "자동정리가 완료되었습니다.");
		model.addAttribute("location", "\"/BEngineer/beFiles/beMyList.do?folder=0"+ "\"");
		return "beFiles/alert";
	}
	
	@RequestMapping("beMove.do") //파일/폴더 이동
	public String beMove(HttpSession session, Model model, int ref, int folder_ref) {
		if(MainBean.loginCheck(session)) {return "redirect:/beMember/beLogin.do";} // 비 로그인 상태시 로그인 창으로 리디렉트
		String owner = (String)session.getAttribute("id");
		FileBean filebean = new FileBean();
		filebean.setSqlSession(sqlSession);
		List originalAddr = null;
		originalAddr = filebean.getAddr(ref);
		String originalPath = "d:/PM/BEngineer/";
		FileDTO dto = new FileDTO();
		for(int i = originalAddr.size() - 1; i >= 0; i--) {
			dto = (FileDTO)originalAddr.get(i);
			originalPath += dto.getOrgname();
			if(i!=0) originalPath+="/";
		}
		List newAddr = null;
		newAddr = filebean.getAddr(folder_ref);
		String newPath = "d:/PM/BEngineer/";
		for(int i = newAddr.size() - 1; i >= 0; i--) {
			dto = (FileDTO)newAddr.get(i);
			newPath += dto.getOrgname() + "/";
		}
		dto = (FileDTO)originalAddr.get(0);
		String orgname = dto.getOrgname();
		newPath += orgname;
		int num = dto.getNum();
		dto.setNum(num);
		int folderref = ((FileDTO)newAddr.get(0)).getNum();
		dto.setFolder_ref(folderref);
		
		boolean is_Move = false;
		is_Move = nioFilemove(originalPath,newPath);
		
		if(is_Move) {
			sqlSession.update("bengineer.changeref",dto);
		}
		model.addAttribute("alert", "파일/폴더 이동이 완료되었습니다.");
		model.addAttribute("location", "\"/BEngineer/beFiles/beMyList.do?folder=0"+ "\"");
		return "beFiles/alert";
	}
	
	private int moveFile(String filePath, String fileName, String moveFolder, String owner) {
		boolean is_Move = false;
		HashMap<String, String> moveP = new HashMap<String, String>();
		moveP.put("owner", owner);
		String filePathtemp=filePath.substring(0, filePath.lastIndexOf(fileName));
		if(moveFolder.equals("image")) {
			is_Move = nioFilemove(filePath, filePathtemp+"image/"+fileName);
			moveP.put("orgname","image");
		}else if(moveFolder.equals("music")) {
			is_Move = nioFilemove(filePath, filePathtemp+"music/"+fileName);
			moveP.put("orgname","music");
		}else if(moveFolder.equals("video")) {
			is_Move = nioFilemove(filePath, filePathtemp+"video/"+fileName);
			moveP.put("orgname","video");
		}else if(moveFolder.equals("document")) {
			is_Move = nioFilemove(filePath, filePathtemp+"document/"+fileName);
			moveP.put("orgname","document");
		}else {
			is_Move = nioFilemove(filePath, filePathtemp+"etc/"+fileName);
			moveP.put("orgname","etc");
		}
		return (Integer)sqlSession.selectOne("bengineer.getparentnum",moveP);
	}
	
	private boolean nioFilemove(String originFilePath, String moveFilePath) { //파일이동, NIO방식
		Path originPath = Paths.get(originFilePath);
		Path movePath = Paths.get(moveFilePath);
		
		if(originPath==null) {
			throw new IllegalArgumentException("orginPath에 맞는 데이터 형식을 넣어주세요.");
		}
		if(movePath==null) {
			throw new IllegalArgumentException("orginPath에 맞는 데이터 형식을 넣어주세요.");
		}
		if(!Files.exists(originPath, new LinkOption[] {})) {
			throw new IllegalArgumentException("orginFile이 존재하지 않습니다."+originPath.toString());
		}
		try {
			Files.move(originPath, movePath, StandardCopyOption.REPLACE_EXISTING);
		}catch(IOException e) {
			e.printStackTrace();
			return false;
		}
		if(Files.exists(movePath,new LinkOption[] {})) {
			return true;
		}else {
			System.out.println("파일이동에 실패했습니다.");
			return true;
		}	
	}
}
