package bengineer.spring.web;

import org.rosuda.REngine.*;
import org.rosuda.REngine.Rserve.*;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import bengineer.spring.board.BoardDTO;
import bengineer.spring.manager.ManagerDTO;

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
		boolean exist_file = false;
		Integer moveParent = 0;
		File path = new File("d:/PM/BEngineer/" + owner); //사용자폴더 경로
		File[] list = path.listFiles(); //사용자폴더 안에 모든 파일/폴더 받아오기
		FileBean filebean = new FileBean();
		FileDTO dto = new FileDTO();
		dto.setOwner(owner);
		if(list.length > 0){
		    for(int i=0; i < list.length; i++){
		    	if(list[i].isFile()) { //파일인지 판단, 파일이면
		    		exist_file =true;
		    		fileName=list[i].getName();
		    		dto.setOrgname(fileName);
		    		filePath=list[i].getPath();
		    		long filesize = list[i].length();
		    		fileType = fileName.substring(fileName.lastIndexOf("."));
		    		moveFolder = filebean.checkFile(fileType); //파일확장자 범주에 들어가는 폴더이름	
		    		moveParent = moveFile(filePath, fileName, moveFolder, owner); //파일 이동
		    		if( moveParent!=0) {
		    			dto.setNum(moveParent);
		    			sqlSession.update("bengineer.autoarrange",dto);
		    			dto.setFilesize(filesize);
		    			sqlSession.update("bengineer.updatesize",dto);
		    		}
		    		else
		    			break;
		    	}
		    }
		}
		if(moveParent!=0) {
			model.addAttribute("alert", "자동정리가 완료되었습니다.");
		}
		else {
			if(!exist_file)  model.addAttribute("alert", "자동정리할 파일이 존재하지 않습니다.");
			else model.addAttribute("alert", "이미 같은 이름의 파일/폴더가 존재합니다.");
		}
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
		long filesize = dto.getFilesize();
		newPath += orgname;
		int num = dto.getNum();
		dto.setNum(num);
		int folderref = ((FileDTO)newAddr.get(0)).getNum();
		dto.setFolder_ref(folderref);
		File file = new File(originalPath);
		boolean is_Move = false;
		int flag = 0;
		
		if(file.isFile()) { // 이동하려는 파일/폴더가 파일일 경우
			String filetype = orgname.substring(orgname.lastIndexOf("."));
			String newFolder = ((FileDTO)newAddr.get(0)).getOrgname();
			String result = filebean.checkFile(filetype);
		
			// 이동할 위치의 폴더가 기본폴더인 경우
			if(newFolder.equals("image")||newFolder.equals("music")||newFolder.equals("video")||newFolder.equals("document")||newFolder.equals("etc")) {
				if(!newFolder.equals(result)) {
					is_Move = false;
					flag = 1;
				}
				else {
					is_Move = nioFilemove(originalPath,newPath);
				}
			}else {
				is_Move = nioFilemove(originalPath,newPath);
			}
		}else {
			is_Move = nioFilemove(originalPath,newPath);
		}
		
		if(is_Move) {
			sqlSession.update("bengineer.changeref",dto);
			model.addAttribute("alert", "파일/폴더 이동이 완료되었습니다.");
		}
		else {
			if(flag==1) {
				model.addAttribute("alert", "해당폴더에 이동할수 없는 파일의 형식입니다.");
			}else {
				model.addAttribute("alert", "이미 같은 이름의 파일/폴더가 존재합니다.");
			}	
		}
		filebean.uploadsize(-filesize, originalAddr);
		filebean.uploadsize(filesize, newAddr);
		model.addAttribute("location", "\"/BEngineer/beFiles/beMyList.do?folder=0"+ "\"");
		return "beFiles/alert";
	}
	@RequestMapping("beMultimove.do") // 여러파일/폴더 선택
	public String beMultimove(String file_ref,int file_fref, HttpSession session,  Model model) {
		if(MainBean.loginCheck(session)) {return "redirect:/beMember/beLogin.do";} 
		FileBean filebean = new FileBean();
		filebean.setSqlSession(sqlSession);
		boolean is_Move = false;
		String files[] = file_ref.split(",");
		int flag = 0;
		FileDTO dto = new FileDTO();
		
		for(int i = 0; i < files.length; i++) { // 기본폴더에 옮길 때 올바른 형식의 파일/폴더들이 옮겨지는지 검사
			int filenum = 0;
			filenum = Integer.parseInt(files[i]);
			FileDTO dto2 = new FileDTO();
			
			List originalAddr = null;
			originalAddr = filebean.getAddr(filenum);
			String originalPath = "d:/PM/BEngineer/";
			for(int j = originalAddr.size() - 1; j >= 0; j--) {
				dto = (FileDTO)originalAddr.get(j);
				originalPath += dto.getOrgname();
				if(j!=0) originalPath+="/";
			}
			File file = new File(originalPath);
			dto2 = (FileDTO)originalAddr.get(0);
			String orgname = dto.getOrgname();
			
			
			List newAddr = null;
			newAddr = filebean.getAddr(file_fref);
			dto2 = (FileDTO)newAddr.get(0);
			String neworgname = dto2.getOrgname();
			
			if(file.isFile()) { // 이동하려는 파일/폴더가 파일일 경우
				String filetype = orgname.substring(orgname.lastIndexOf("."));
				String result = filebean.checkFile(filetype);
			
				// 이동할 위치의 폴더가 기본폴더인 경우
				if(neworgname.equals("image")||neworgname.equals("music")||neworgname.equals("video")||neworgname.equals("document")||neworgname.equals("etc")) {
					if(!neworgname.equals(result)) {
						is_Move = false;
						flag = 1;
						break;
					}
				}
			}
		}
		
		if(flag == 0) { // 선택한 여러 파일/폴더 모두 다 이동할 수 있을 시
			for(int i = 0; i < files.length; i++) {
				int filenum = 0;
				filenum = Integer.parseInt(files[i]);
			
				List originalAddr = null;
				originalAddr = filebean.getAddr(filenum);
				String originalPath = "d:/PM/BEngineer/";
				
				for(int j = originalAddr.size() - 1; j >= 0; j--) {
					dto = (FileDTO)originalAddr.get(j);
					originalPath += dto.getOrgname();
					if(j!=0) originalPath+="/";
				}
			
				List newAddr = null;
				newAddr = filebean.getAddr(file_fref);
				String newPath = "d:/PM/BEngineer/";
			
				for(int j = newAddr.size() - 1; j >= 0; j--) {
					dto = (FileDTO)newAddr.get(j);
					newPath += dto.getOrgname() + "/";
				}
			
				dto = (FileDTO)originalAddr.get(0);
				String orgname = dto.getOrgname();
				long filesize = dto.getFilesize();
				newPath += orgname;
				int num = dto.getNum();
				dto.setNum(num);
				int folderref = ((FileDTO)newAddr.get(0)).getNum();
				dto.setFolder_ref(folderref);
			
				is_Move = nioFilemove(originalPath,newPath);
			
				if(is_Move) {
					sqlSession.update("bengineer.changeref",dto);
					filebean.uploadsize(-filesize, originalAddr);
					filebean.uploadsize(filesize, newAddr);
				}else { 
					break;
				}
			}
		}
		
		if(is_Move)
			model.addAttribute("alert", "파일/폴더 이동이 완료되었습니다.");
		else {
			if(flag==1) {
				model.addAttribute("alert", "해당폴더에 이동할수 없는 파일의 형식입니다.");
			}else {
				model.addAttribute("alert", "이미 같은 이름의 파일/폴더가 존재합니다.");
			}	
		}
		
		model.addAttribute("location", "\"/BEngineer/beFiles/beMyList.do?folder=0"+ "\"");
		return "beFiles/alert";
	}
	
	@RequestMapping("beMultilist.do") // 여러 파일/폴더 선택 후 내파일 이동시
	public String beMultilist(HttpSession session, Model model, int folder,@RequestParam(value="multifile_Ref", defaultValue="0") String multifile_Ref, @RequestParam(value="multifile_FRef", defaultValue="0") int multifile_FRef) {
		if(MainBean.loginCheck(session)) {return "redirect:/beMember/beLogin.do";} // 濡쒓렇�씤 �꽭�뀡 �뾾�쓣 �떆 由щ뵒�젆�듃
		FileBean filebean = new FileBean();
		filebean.setSqlSession(sqlSession);
		String owner = (String)session.getAttribute("id");
		String nickname = (String)session.getAttribute("nickname");
		File file = new File("d:/PM/BEngineer/" + owner);
		FileDTO dto = new FileDTO();
		dto.setOwner(owner);
		int folder_ref = 0;
		List address_ref = null;
		if(file.exists()) {
			if(folder == 0) {
				dto.setOrgname(owner);
				dto.setFolder_ref(0);
				folder_ref = (int)sqlSession.selectOne("bengineer.getref", dto); // �뤃�뜑媛믪씠 �븞 �뱾�뼱�솕�쓣 寃쎌슦 �땳�꽕�엫怨� 媛숈� �씠由꾩쓽 湲곕낯�뤃�뜑濡� �씠�룞
				address_ref = filebean.getAddr(folder_ref);
			}else {
				folder_ref = folder;
				address_ref = filebean.getAddr(folder_ref);
			}
		}
		List filelist = sqlSession.selectList("bengineer.getfiles", folder_ref);
		sqlSession.update("bengineer.hit", folder);
		model.addAttribute("list", filelist);
		List folderaddress = new ArrayList(); // �뤃�뜑 寃쎈줈瑜� �븯�굹�뵫 ���옣�븯湲� �쐞�븳 由ъ뒪�듃
		List orgaddress = new ArrayList(); // �뤃�뜑二쇱냼�뿉 ���옣�맂 媛곴컖�쓽 �뤃�뜑�뿉 ���븳 �떎�젣 寃쎈줈瑜� �븯�굹�뵫 ���옣�븯湲� �쐞�븳 由ъ뒪�듃
		if(address_ref.size() < 5) {
			for(int i = address_ref.size() - 1; i >= 0; i--) {
				dto = (FileDTO)address_ref.get(i);
				folderaddress.add(dto.getFilename());
				orgaddress.add(dto.getNum());
			}
		}else {
			folderaddress.add(nickname);
			orgaddress.add(0);
			folderaddress.add("..."); // �뤃�뜑 寃쎈줈媛� 5媛쒕�� �꽆�뼱湲� �떆 湲곕낯�뤃�뜑�� 媛��옣 �쐞�쓽 3媛쒕�� �젣�쇅�븯怨� �깮�왂
			orgaddress.add(null);
			for(int i = 2; i >= 0; i--) {
				dto = (FileDTO)address_ref.get(i);
				folderaddress.add(dto.getFilename());
				orgaddress.add(dto.getNum());
			}
		}
		if(!owner.equals(dto.getOwner())) {
			model.addAttribute("alert", "�옒紐삳맂 �젒洹쇱엯�땲�떎.");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}
		model.addAttribute("folderaddress", folderaddress);
		model.addAttribute("orgaddress", orgaddress);
		model.addAttribute("folder_ref", folder_ref);
		model.addAttribute("folder",folder); // 상위폴더로 이동하기 위해 
		model.addAttribute("write", true);
		
		model.addAttribute("multifile_Ref",multifile_Ref); 	// 이동할 파일/폴더
		if(multifile_FRef==0) {
			model.addAttribute("multifile_FRef",folder);  // 이동될 위치
		}else {
			model.addAttribute("multifile_FRef",multifile_FRef);  // 이동될 위치
		}
		model.addAttribute("movefile_Ref",0); 	// 이동할 파일/폴더
		model.addAttribute("movefile_FRef",0);  // 이동될 위치
		return "beFiles/beList";
	}
	
	private int moveFile(String filePath, String fileName, String moveFolder, String owner) {
		boolean is_Move = false;
		HashMap<String, String> moveP = new HashMap<String, String>();
		moveP.put("owner", owner);
		String filePathtemp=filePath.substring(0, filePath.lastIndexOf(fileName));
		if(moveFolder.equals("image")) {
			is_Move = nioFilemove(filePath, filePathtemp+"image/"+fileName);
			if(is_Move)
				moveP.put("orgname","image");
		}else if(moveFolder.equals("music")) {
			is_Move = nioFilemove(filePath, filePathtemp+"music/"+fileName);
			if(is_Move)
				moveP.put("orgname","music");
		}else if(moveFolder.equals("video")) {
			is_Move = nioFilemove(filePath, filePathtemp+"video/"+fileName);
			if(is_Move)
				moveP.put("orgname","video");
		}else if(moveFolder.equals("document")) {
			is_Move = nioFilemove(filePath, filePathtemp+"document/"+fileName);
			if(is_Move)
				moveP.put("orgname","document");
		}else {
			is_Move = nioFilemove(filePath, filePathtemp+"etc/"+fileName);
			if(is_Move)
				moveP.put("orgname","etc");
		}
		if(is_Move)
			return  (Integer)sqlSession.selectOne("bengineer.getparentnum",moveP);
		else
			return 0;
	}
	
	public boolean nioFilemove(String originFilePath, String moveFilePath) { //파일이동, NIO방식
		File newfile = new File(moveFilePath);
		
		Path originPath = Paths.get(originFilePath);
		Path movePath = Paths.get(moveFilePath);
		if(newfile.exists()) {
			System.out.println("이미 존재하는 파일입니다.");
			return false;
		}
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
	/*
	@RequestMapping("size.do")
	public String size() throws RserveException, REXPMismatchException { 
		FileDTO file = (FileDTO)sqlSession.selectOne("bengineer.mainSize","test3");
		RConnection r = new RConnection();
		r.eval("png('ajava.png')");
		double Fsize =file.getFilesize();
		System.out.println(Fsize);
		r.eval("Fsize<-"+Fsize);
		r.eval("barplot(Fsize,names='크기',col=rainbow(20))");
		r.eval("dev.off()");
		byte [] img = r.eval("r=readBin('ajava.png','raw',700*400)").asBytes();
		return "beFiles/size";
	}
	*/
	@RequestMapping("hotlist.do")
	public String hotlist(int num) {
		System.out.println(num);
		//sqlSession.update("bengineer.hot",num);
		return "beFiles/hotlist"; 
	}
	
}
