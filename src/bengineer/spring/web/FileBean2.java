package bengineer.spring.web;

import org.rosuda.REngine.*;
import org.rosuda.REngine.Rserve.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.nio.channels.FileChannel;
import java.nio.file.*;

import javax.servlet.http.HttpSession;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
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
		FileBean filebean = new FileBean(); //FileBean 객체 생성
		
		if(!filebean.checkSpace(session, sqlSession)) {
			model.addAttribute("alert", "사용할 수 있는 용량을 초과했습니다. 용량을 확보해주세요");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}
		
		String owner = (String)session.getAttribute("id");
		String filePath=""; //파일경로
		String fileName=""; //파일이름
		String fileType=""; //파일확장자
		String moveFolder=""; //이동할 파일폴더
		boolean exist_file = false; //파일이 존재하는지 여부
		Integer moveParent = 0;
		File path = new File("d:/PM/BEngineer/" + owner); //사용자폴더 경로
		File[] list = path.listFiles(); //사용자폴더 안에 모든 파일/폴더 받아오기
		FileDTO dto = new FileDTO();
		dto.setOwner(owner);
		
		if(list.length > 0){ //파일들이 존재할 경우
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
		    		
		    		if( moveParent!=0) { //파일이동이 완료되면
		    			
		    			//이동한 파일의 folder_ref 수정
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
		
		if(moveParent!=0) { //모든 파일이동이 완료되면
			model.addAttribute("alert", "자동정리가 완료되었습니다.");
		}else { //자동정리할 파일이 없거나 중복된 이름이 있을 시
			if(!exist_file)  model.addAttribute("alert", "자동정리할 파일이 존재하지 않습니다.");
			else model.addAttribute("alert", "이미 같은 이름의 파일/폴더가 존재합니다.");
		}
			model.addAttribute("location", "\"/BEngineer/beFiles/beMyList.do?folder=0"+ "\"");
			return "beFiles/alert";	
	}
	
	@RequestMapping("beFilesession.do") //파일세션 설정
	public String beFilesession(HttpSession session, int folder, String ref, String file_flag, Model model) {
		
		//받아온 file플래그 함수와 파일ref 세션으로 설정
		session.setAttribute("file_flag", file_flag);
		session.setAttribute("ref", ref);
		
		FileDTO filedto = new FileDTO();
		int folder_ref = folder;
		filedto = sqlSession.selectOne("bengineer.getaddr",folder_ref); //해당하는 fileDTO 받아오기
		if(filedto == null) 
			model.addAttribute("location","\"/BEngineer/beFiles/beSharedList.do?folder=0\"");
		else if(filedto.getFilename().equals("image") && filedto.getImportant()==-1) //파일이 기본이미지 폴더일 시
			model.addAttribute("location","\"/BEngineer/beFiles/beImagePreview.do?folder="+folder+ "\"");
		else
			model.addAttribute("location","\"/BEngineer/beFiles/beMyList.do?folder="+folder+ "\"");
		return "beFiles/location";	
	}
	
	@RequestMapping("beMove.do") //파일/폴더 이동
	public String beMove(HttpSession session, Model model, int folder_ref) {
		if(MainBean.loginCheck(session)) {return "redirect:/beMember/beLogin.do";} // 비 로그인 상태시 로그인 창으로 리디렉트
		FileBean filebean = new FileBean(); //FileBean 객체 생성
		filebean.setSqlSession(sqlSession);
		if(!filebean.checkSpace(session, sqlSession)) {
			model.addAttribute("alert", "사용할 수 있는 용량을 초과했습니다. 용량을 확보해주세요");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}

		//받아온 파일ref로 원래 폴더 리스트 받아오기
		List originalAddr = null;
		String refTemp = (String)session.getAttribute("ref");
		Integer ref = Integer.parseInt(refTemp);
		originalAddr = filebean.getAddr(ref); //ref를 인자로 폴더 리스트 받아오기
		
		//원래 폴더 경로 대입
		String originalPath = "d:/PM/BEngineer/";
		FileDTO dto = new FileDTO();
		for(int i = originalAddr.size() - 1; i >= 0; i--) {
			dto = (FileDTO)originalAddr.get(i);
			originalPath += dto.getOrgname();
			if(i!=0) originalPath+="/";
		}
		
		//받아온 파일folder_ref로 이동할 폴더 리스트 받아오기
		List newAddr = null;
		newAddr = filebean.getAddr(folder_ref);
		
		//이동할 폴더 경로 대입
		String newPath = "d:/PM/BEngineer/";
		for(int i = newAddr.size() - 1; i >= 0; i--) {
			dto = (FileDTO)newAddr.get(i);
			newPath += dto.getOrgname() + "/";
		}
		
		//자신의 파일정보 받아와서 dto 설정하고 newPath에 더해주기
		dto = (FileDTO)originalAddr.get(0);
		String orgname = dto.getOrgname();
		String originOwner = dto.getOwner(); 
		newPath += orgname;
		int num = dto.getNum();
		dto.setNum(num);
		
		//자신의 filedto의 folder_ref의 이동할 filedto의 foder_ref를 대입 
		int folderref = ((FileDTO)newAddr.get(0)).getNum();
		String newOwner = ((FileDTO)newAddr.get(0)).getOwner();
		dto.setFolder_ref(folderref);
		
		File file = new File(originalPath); //원래경로의 파일/폴더 객체 생성
		boolean is_Move = false; //이동 가능 여부의 변수
		int flag = 0; //이동할 수 없는 이유 변수
		
		if(originOwner.equals(newOwner)) { //이동할 파일/폴더와 이동할 곳의 파일/폴더의 소유자가 같을 경우
			if(file.isFile()) { // 이동하려는 파일/폴더가 파일일 경우
				String filetype = orgname.substring(orgname.lastIndexOf("."));
				String newFolder = ((FileDTO)newAddr.get(0)).getOrgname();
				String result = filebean.checkFile(filetype);
			
				// 이동할 위치의 폴더가 기본폴더인 경우
				if(newFolder.equals("image")||newFolder.equals("music")||newFolder.equals("video")||newFolder.equals("document")||newFolder.equals("etc")) {
					if(!newFolder.equals(result)) { //기본폴더의 파일형식과 이동할려는 파일의 형식이 맞지 않을 시
						is_Move = false;
						flag = 1;
					}
					else { //형식이 맞으면 이동
						is_Move = nioFilemove(originalPath,newPath);
					}
				}else { //이동할 위치의 폴더가 기본폴더가 아닌 경우
					is_Move = nioFilemove(originalPath,newPath);
				}
			}else { // 이동하려는 파일/폴더가 폴더일 경우
				String newFolder = ((FileDTO)newAddr.get(0)).getOrgname();
				boolean is_Check = checkFolder(file, newFolder); //폴더 안에 파일/폴더까지 이동하기 위해 검사
				if(newFolder.equals("image")||!is_Check) { //복사될 폴더가 기본이미지폴더이거나 이동 할 수 없는 형식이 포함되있을 경우
					is_Move = false;
					flag = 1;
				}else { //이동를 할 수 있는 경우 이동
					is_Move = nioFilemove(originalPath,newPath);
				}
			}
		}else { //소유자가 다를 경우
			is_Move = false;
			flag = 1;
		}
		
		if(is_Move) { //이동이 완료되었으면 DB 수정
			sqlSession.update("bengineer.changeref",dto); //이동한 파일/폴더의 folder_ref 수정
			model.addAttribute("alert", "파일/폴더 이동이 완료되었습니다.");
		}
		else {
			if(flag==1) { //형식이 맞지않을 경우
				model.addAttribute("alert", "해당폴더에 이동할수 없는 파일의 형식입니다.");
			}else { //중복된 이름이 존재할 경우
				model.addAttribute("alert", "이미 같은 이름의 파일/폴더가 존재합니다.");
			}	
		}
		model.addAttribute("location", "\"/BEngineer/beFiles/beMyList.do?folder=0"+ "\"");
		
		//세션을 사용하고나면 다음 파일 작업을 위해 세션을 삭제한다.
		session.removeAttribute("ref");
		session.removeAttribute("file_flag");
		
		return "beFiles/alert";
	}
	
	@RequestMapping("beCopy.do") //파일/폴더 복사
	public String beCopy(HttpSession session, Model model, int folder_ref) {
		if(MainBean.loginCheck(session)) {return "redirect:/beMember/beLogin.do";} // 비 로그인 상태시 로그인 창으로 리디렉트
		FileBean filebean = new FileBean(); //FileBean 객체 생성
		filebean.setSqlSession(sqlSession);
		if(!filebean.checkSpace(session, sqlSession)) {
			model.addAttribute("alert", "사용할 수 있는 용량을 초과했습니다. 용량을 확보해주세요");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}
		
		//받아온 파일ref로 원래 폴더 리스트 받아오기
		List originalAddr = null;
		String refTemp = (String)session.getAttribute("ref");
		Integer ref = Integer.parseInt(refTemp);
		originalAddr = filebean.getAddr(ref);
		String originalPath = "d:/PM/BEngineer/";
		
		//받아온 파일folder_ref로 이동할 폴더 리스트 받아오기
		List newAddr = null;
		newAddr = filebean.getAddr(folder_ref);
		String newPath = "d:/PM/BEngineer/";
		FileDTO dto = new FileDTO();
		String isFolderPath = "d:/PM/BEngineer/";
		
		FileDTO isFolderdto = new FileDTO();
		int count=2; //중복되지 않은 이름을 설정하기 위해
		boolean is_Copy = false; //복사 가능 여부의 변수
		
		//원래 폴더 경로 대입
		for(int i = originalAddr.size() - 1; i >= 0; i--) {
			isFolderdto = (FileDTO)originalAddr.get(i);
			isFolderPath += isFolderdto.getOrgname();
			if(i!=0) isFolderPath+="/";
		}
				
		File isFolderfile = new File(isFolderPath);
		if((isFolderfile.isFile())) { //복사팔 파일/폴더가 파일인 경우
			FileDTO dto2 = (FileDTO)originalAddr.get(0);
			String orgname = dto2.getOrgname();
			String filetype = orgname.substring(orgname.lastIndexOf("."));
			String newFolder = ((FileDTO)newAddr.get(0)).getOrgname();
			String result = filebean.checkFile(filetype);
			
			//복사할 곳의 폴더가 기본폴더일 때 형식이 맞지 않으면 문구를 띄우고 복사를 하지 않고 세션을 제거한 후 beMyList로 이동한다.
			if(newFolder.equals("image")||newFolder.equals("music")||newFolder.equals("video")||newFolder.equals("document")||newFolder.equals("etc")) {
				if(!newFolder.equals(result)) {
					model.addAttribute("alert", "해당폴더에 복사할수 없는 폴더의 형식입니다.");
					model.addAttribute("location", "\"/BEngineer/beFiles/beMyList.do?folder=0"+ "\"");
					session.removeAttribute("ref");
					session.removeAttribute("file_flag");
					return "beFiles/alert";
				}
			}
		}else { //복사팔 파일/폴더가 폴더인 경우
			String newFolder = ((FileDTO)newAddr.get(0)).getOrgname();
			boolean is_Check = checkFolder(isFolderfile, newFolder); //폴더 안에 파일/폴더까지 복사하기 위해 검사
			
			//복사될 폴더가 기본이미지폴더이거나 복사를 할 수 없는 형식이 포함되있을 경우 문구를 띄우고 세션을 제거한 후 beMyList로 이동한다.
			if(newFolder.equals("image")||!is_Check) { 
				model.addAttribute("alert", "해당폴더에 복사할수 없는 폴더의 형식입니다.");
				model.addAttribute("location", "\"/BEngineer/beFiles/beMyList.do?folder=0"+ "\"");
				session.removeAttribute("ref");
				session.removeAttribute("file_flag");
				return "beFiles/alert";
			}
		}
		
		if(ref==folder_ref) { //자기 위치에 복사하려할 때
			for(int i = originalAddr.size() - 1; i >= 0; i--) {
				dto = (FileDTO)originalAddr.get(i);
				originalPath += dto.getOrgname();
				if(i!=0) originalPath+="/";
			}
			
			for(int i = newAddr.size() - 1; i >= 1; i--) {
				dto = (FileDTO)newAddr.get(i);
				newPath += dto.getOrgname() + "/";
			}
			
		}else { //다른 위치에 복사하려 할 때
			for(int i = originalAddr.size() - 1; i >= 0; i--) {
				dto = (FileDTO)originalAddr.get(i);
				originalPath += dto.getOrgname();
				if(i!=0) originalPath+="/";
			}
			
			for(int i = newAddr.size() - 1; i >= 0; i--) {
				dto = (FileDTO)newAddr.get(i);
				newPath += dto.getOrgname() + "/";
			}
		}
		
		dto = (FileDTO)originalAddr.get(0);
		String orgname = dto.getOrgname();
		String filename = dto.getFilename();
		
		File file = new File(originalPath);
		if(file.isFile()) { //파일인 경우 filename, orgname 둘 다 복사본을 붙여준다.
			if(orgname.lastIndexOf(".")!=-1) {
				String orgFname = orgname.substring(0,orgname.lastIndexOf("."));
				String orgFtype = orgname.substring(orgname.lastIndexOf("."));
				String copyOrgname = orgFname + "-복사본" + orgFtype;
				dto.setOrgname(copyOrgname);
				orgname = copyOrgname;
				newPath += copyOrgname;
			}else {
				String copyOrgname = orgname + "-복사본";
				dto.setOrgname(copyOrgname);
				orgname = copyOrgname;
				newPath += copyOrgname;
			}
			
			if(filename.lastIndexOf(".")!=-1) {
				String fileFname = filename.substring(0,filename.lastIndexOf("."));
				String fileFtype = filename.substring(filename.lastIndexOf("."));
				String copyFilename = fileFname + "-복사본" + fileFtype;
				filename = copyFilename;
				dto.setFilename(copyFilename);
			}else {
				String copyFilename = filename + "-복사본";
				filename = copyFilename;
				dto.setFilename(copyFilename);
			}
		}else { //폴더인 경우
			String copyOrgname = orgname + "-복사본";
			dto.setOrgname(copyOrgname);
			orgname = copyOrgname;
			newPath += copyOrgname;
			String copyFilename = filename + "-복사본";
			filename = copyFilename;
			dto.setFilename(copyFilename);
		} 
		
		while(!is_Copy) { //복사본의 이름이 존재할 때 존재하지 않는 이름으로 생성할 때까지 반복
			int folderref=0;
			if(ref==folder_ref) // 자기 위치에 복사하면 자기 위치보다 상위위치의 folder_ref를 받아온다. 
				folderref = ((FileDTO)newAddr.get(0)).getFolder_ref();
			else
				folderref = ((FileDTO)newAddr.get(0)).getNum();
			dto.setFolder_ref(folderref);
			
			//복사될 곳의 파일 소유자을 설정해준다.
			String newOwner = ((FileDTO)newAddr.get(0)).getOwner(); 
			dto.setOwner(newOwner); 
			
			int orginsub = 0; //복사할 디렉터리의 넘버
			orginsub = ((FileDTO)originalAddr.get(0)).getNum();
			
			int flag = 0; //복사할 수 없는 이유 변수
		
			if(file.isFile()) { // 복사하려는 파일/폴더가 파일일 경우
				String filetype = orgname.substring(orgname.lastIndexOf("."));
				String newFolder = ((FileDTO)newAddr.get(0)).getOrgname();
				String result = filebean.checkFile(filetype);
		
				// 복사할 위치의 폴더가 기본폴더인 경우
				if(newFolder.equals("image")||newFolder.equals("music")||newFolder.equals("video")||newFolder.equals("document")||newFolder.equals("etc")) {
					if(!newFolder.equals(result)) { //형식에 맞지않으면 복사가 불가능하고 다음 반복문을 진행하지 않음
						is_Copy = false;
						flag = 1;
					}
					else { //형식에 맞을경우 복사 
						is_Copy = nioFilecopy(originalPath,newPath);
					}
				}else { //기본폴더가 아닐 경우 복사
					is_Copy = nioFilecopy(originalPath,newPath);
				}
			}else { //폴더일 경우 복사
				is_Copy = nioFilecopy(originalPath,newPath);
			}
		
			if(is_Copy) { //복사가 완료되었을 경우 DB수정 및 하위 파일/폴더까지 DB 수정
				sqlSession.insert("bengineer.copyfile",dto);
				FileDTO copydto = (FileDTO)sqlSession.selectOne("bengineer.newCopyfiles",dto);
				sqlCopy(orginsub,copydto);
				model.addAttribute("alert", "파일/폴더 복사가 완료되었습니다.");
			}
			else { //복사가 안된 경우
				newPath = newPath.substring(0,(newPath.lastIndexOf("/")+1));
				if(flag==1) { //이동할 수 없는 형식인 경우
					model.addAttribute("alert", "해당폴더에 복사할수 없는 파일의 형식입니다.");
				}else { //이름이 중복된 경우
					if(file.isFile()) { //파일인 경우에 이름 설정
						if(orgname.lastIndexOf(".")!=-1) {
							String orgFname = orgname.substring(0,(orgname.lastIndexOf("본")+1));
							String orgFtype = orgname.substring(orgname.lastIndexOf("."));
							String copyOrgname = orgFname + count + orgFtype;
							dto.setOrgname(copyOrgname);
							orgname = copyOrgname;
							newPath += copyOrgname;
						}else {
							String orgFname = orgname.substring(0,(orgname.lastIndexOf("본")+1));
							String copyOrgname = orgFname + count;
							dto.setOrgname(copyOrgname);
							orgname = copyOrgname;
							newPath += copyOrgname;
						}
						
						if(filename.lastIndexOf(".")!=-1) {
							String fileFname = filename.substring(0,(filename.lastIndexOf("본")+1));
							String fileFtype = filename.substring(filename.lastIndexOf("."));
							String copyFilename = fileFname + count + fileFtype;
							filename = copyFilename;
							dto.setFilename(copyFilename);
						}else {
							String fileFname = filename.substring(0,(filename.lastIndexOf("본")+1));
							String copyFilename = fileFname + count;
							filename = copyFilename;
							dto.setFilename(copyFilename);
						}
					}else { //폴더인 경우 이름 설정
						String orgFname = orgname.substring(0,(orgname.lastIndexOf("본")+1));
						String copyOrgname = orgFname + count;
						dto.setOrgname(copyOrgname);
						orgname = copyOrgname;
						newPath += copyOrgname;
						String fileFname = filename.substring(0,(filename.lastIndexOf("본")+1));
						String copyFilename = fileFname + count;
						filename = copyFilename;
						dto.setFilename(copyFilename);
					}
					count++; 
				}	
			}
		}
		model.addAttribute("location", "\"/BEngineer/beFiles/beMyList.do?folder=0"+ "\"");
		
		//세션을 사용하고나면 다음 파일 작업을 위해 세션을 삭제한다.
		session.removeAttribute("ref");
		session.removeAttribute("file_flag");
		
		return "beFiles/alert";
	}
	
	@RequestMapping("beMultimove.do") //여러파일/폴더 이동
	public String beMultimove(int file_fref, HttpSession session,  Model model) {
		if(MainBean.loginCheck(session)) {return "redirect:/beMember/beLogin.do";} // 비 로그인 상태시 로그인 창으로 리디렉트
		FileBean filebean = new FileBean(); //FileBean 객체 생성
		filebean.setSqlSession(sqlSession);
		if(!filebean.checkSpace(session, sqlSession)) {
			model.addAttribute("alert", "사용할 수 있는 용량을 초과했습니다. 용량을 확보해주세요");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}
		
		boolean is_Move = false; //이동 가능 여부의 변수
		int flag = 0; //이동할 수 없는 이유 변수
		
		//이동할 파일/폴더들을 배열에 대입
		String ref = (String)session.getAttribute("ref");
		String files[] = ref.split(",");
		
		FileDTO dto = new FileDTO();
		
		for(int i = 0; i < files.length; i++) { //기본폴더에 옮길 때 올바른 형식의 파일/폴더들이 옮겨지는지 검사
			int filenum = 0;
			filenum = Integer.parseInt(files[i]);
			FileDTO dto2 = new FileDTO();
			
			//받아온 파일ref로 원래 폴더 리스트 받아오기
			List originalAddr = null;
			originalAddr = filebean.getAddr(filenum);
			
			//원래 폴더 경로 대입
			String originalPath = "d:/PM/BEngineer/";
			for(int j = originalAddr.size() - 1; j >= 0; j--) {
				dto = (FileDTO)originalAddr.get(j);
				originalPath += dto.getOrgname();
				if(j!=0) originalPath+="/";
			}
			
			File file = new File(originalPath);
			dto2 = (FileDTO)originalAddr.get(0);
			String orgname = dto2.getOrgname(); 
			String originOwner = dto.getOwner(); //원 파일/폴더 소유자
			
			//받아온 파일file_fref로 이동할 폴더 리스트 받아오기
			List newAddr = null;
			newAddr = filebean.getAddr(file_fref);
			
			dto2 = (FileDTO)newAddr.get(0);
			String neworgname = dto2.getOrgname();
			String newOwner = dto.getOwner(); //이동할 폴더 소유자
			if(originOwner.equals(newOwner)) { //이동할 파일/폴더와 이동할 곳의 파일/폴더의 소유자가 같을 경우
				if(file.isFile()) { // 이동하려는 파일/폴더가 파일일 경우
					String filetype = orgname.substring(orgname.lastIndexOf("."));
					String result = filebean.checkFile(filetype);
				
					// 이동할 위치의 폴더가 기본폴더인 경우
					if(neworgname.equals("image")||neworgname.equals("music")||neworgname.equals("video")||neworgname.equals("document")||neworgname.equals("etc")) {
						if(!neworgname.equals(result)) { //기본폴더의 파일형식과 이동할려는 파일의 형식이 맞지 않을 시
							is_Move = false;
							flag = 1;
							break;
						}
					}
				}else { // 이동하려는 파일/폴더가 폴더일 경우
					boolean is_Check = checkFolder(file, neworgname); //폴더 안에 파일/폴더까지 이동하기 위해 검사
					if(neworgname.equals("image")||!is_Check) { //이동될 폴더가 기본이미지폴더이거나 이동 할 수 없는 형식이 포함되있을 경우
						is_Move = false;
						flag = 1;
						break;
					}
				}
			}else { //소유자가 다를 경우
				is_Move = false;
				flag = 1;
				break;
			}
		}
		
		if(flag == 0) { //선택한 여러 파일/폴더 모두 다 이동할 수 있을 시
			for(int i = 0; i < files.length; i++) { //선택한 파일/폴더들이 모두 이동할 때까지 반복
				int filenum = 0;
				filenum = Integer.parseInt(files[i]);
				
				//받아온 파일ref로 원래 폴더 리스트 받아오기
				List originalAddr = null;
				originalAddr = filebean.getAddr(filenum);
				String originalPath = "d:/PM/BEngineer/";
				
				//원래 폴더 경로 대입
				for(int j = originalAddr.size() - 1; j >= 0; j--) {
					dto = (FileDTO)originalAddr.get(j);
					originalPath += dto.getOrgname();
					if(j!=0) originalPath+="/";
				}
			
				//받아온 파일file_fref로 이동할 폴더 리스트 받아오기
				List newAddr = null;
				newAddr = filebean.getAddr(file_fref);
				String newPath = "d:/PM/BEngineer/";
			
				//이동할 폴더 경로 대입
				for(int j = newAddr.size() - 1; j >= 0; j--) {
					dto = (FileDTO)newAddr.get(j);
					newPath += dto.getOrgname() + "/";
				}
			
				dto = (FileDTO)originalAddr.get(0);
				String orgname = dto.getOrgname();
				newPath += orgname;
				int num = dto.getNum();
				dto.setNum(num);
				int folderref = ((FileDTO)newAddr.get(0)).getNum();
				dto.setFolder_ref(folderref);
			
				is_Move = nioFilemove(originalPath,newPath); //파일 이동
			
				if(is_Move) //파일 이동이 완료되면 DB 수정
					sqlSession.update("bengineer.changeref",dto);
				else //파일 이동이 실패하면 반복문을 나온다.
					break;
			}
		}
		
		if(is_Move) //이동이 완료되었으면 완료문구보내기
			model.addAttribute("alert", "파일/폴더 이동이 완료되었습니다.");
		else {
			if(flag==1) { //형식이 맞지않을 경우
				model.addAttribute("alert", "해당폴더에 이동할수 없는 파일의 형식입니다.");
			}else { //중복된 이름이 존재할 경우
				model.addAttribute("alert", "이미 같은 이름의 파일/폴더가 존재합니다.");
			}	
		}
		
		model.addAttribute("location", "\"/BEngineer/beFiles/beMyList.do?folder=0"+ "\"");
		
		//세션을 사용하고나면 다음 파일 작업을 위해 세션을 삭제한다.
		session.removeAttribute("ref");
		session.removeAttribute("file_flag");
		return "beFiles/alert";
	}
	
	@RequestMapping("beMulticopy.do") // 여러파일/폴더 복사
	public String beMulticopy(int file_fref, HttpSession session,  Model model) {
		if(MainBean.loginCheck(session)) {return "redirect:/beMember/beLogin.do";} // 비 로그인 상태시 로그인 창으로 리디렉트
		FileBean filebean = new FileBean(); //FileBean 객체 생성
		filebean.setSqlSession(sqlSession);
		if(!filebean.checkSpace(session, sqlSession)) {
			model.addAttribute("alert", "사용할 수 있는 용량을 초과했습니다. 용량을 확보해주세요");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}
		
		boolean is_Copy = false; //복사 가능 여부의 변수
		
		//복사할 파일/폴더들을 배열에 대입
		String ref = (String)session.getAttribute("ref");
		String files[] = ref.split(",");
		
		int flag = 0; //복사할 수 없는 이유 변수
		int count = 2; //중복되지 않은 이름을 설정하기 위해
		FileDTO dto = new FileDTO();
		List originalAddr = null;
		String originalPath = "d:/PM/BEngineer/";
		List newAddr = null;
		String newPath = "d:/PM/BEngineer/";

		for(int i = 0; i < files.length; i++) { // 기본폴더에 옮길 때 올바른 형식의 파일/폴더들이 옮겨지는지 검사
			int filenum = 0;
			filenum = Integer.parseInt(files[i]);
			FileDTO dto2 = new FileDTO();
			
			//받아온 파일ref로 원래 폴더 리스트 받아오기
			List originalAddr2 = null;
			originalAddr2 = filebean.getAddr(filenum);
			
			//원래 폴더 경로 대입
			String isFolderPath = "d:/PM/BEngineer/";
			for(int j = originalAddr2.size() - 1; j >= 0; j--) {
				dto = (FileDTO)originalAddr2.get(j);
				isFolderPath += dto.getOrgname();
				if(j!=0) isFolderPath+="/";
			}
			File file = new File(isFolderPath);
			dto2 = (FileDTO)originalAddr2.get(0);
			String orgname = dto.getOrgname();
			
			//받아온 파일file_fref로 이동할 폴더 리스트 받아오기
			List newAddr2 = null;
			newAddr2 = filebean.getAddr(file_fref);
			
			dto2 = (FileDTO)newAddr2.get(0);
			String neworgname = dto2.getOrgname();
			
			if(file.isFile()) { // 복사하려는 파일/폴더가 파일인 경우
				FileDTO dto3 = (FileDTO)originalAddr.get(0);
				String orgname3 = dto3.getOrgname();
				String filetype = orgname3.substring(orgname3.lastIndexOf("."));
				String result = filebean.checkFile(filetype);
				
				//복사할 곳의 폴더가 기본폴더일 때 형식이 맞지 않으면 문구를 띄우고 복사를 하지 않고 세션을 제거한 후 beMyList로 이동한다.
				if(neworgname.equals("image")||neworgname.equals("music")||neworgname.equals("video")||neworgname.equals("document")||neworgname.equals("etc")) {
					if(!neworgname.equals(result)) { 
						model.addAttribute("alert", "해당폴더에 복사할수 없는 폴더의 형식입니다.");
						model.addAttribute("location", "\"/BEngineer/beFiles/beMyList.do?folder=0"+ "\"");
						session.removeAttribute("ref");
						session.removeAttribute("file_flag");
						return "beFiles/alert";
					}
				}
			}else { //복사팔 파일/폴더가 폴더인 경우
				boolean is_Check = checkFolder(file, neworgname); //폴더 안에 파일/폴더까지 복사하기 위해 검사
				
				//복사될 폴더가 기본이미지폴더이거나 복사를 할 수 없는 형식이 포함되있을 경우 문구를 띄우고 세션을 제거한 후 beMyList로 이동한다.
				if(neworgname.equals("image")||!is_Check) {
					model.addAttribute("alert", "해당폴더에 복사할수 없는 폴더의 형식입니다.");
					model.addAttribute("location", "\"/BEngineer/beFiles/beMyList.do?folder=0"+ "\"");
					session.removeAttribute("ref");
					session.removeAttribute("file_flag");
					return "beFiles/alert";
				}
			}
		}
		
		if(ref.equals(file_fref)) { // 자기자신의 복사하려할 때
			for(int i = 0; i < files.length; i++) { //선택한 파일/폴더들의 복사본이름 설정 후 복사 반복
				int filenum = 0;
				filenum = Integer.parseInt(files[i]);
				
				dto = null;
				
				//받아온 filenum으로 복사할 폴더 리스트 받아오기
				originalAddr = null;
				originalAddr = filebean.getAddr(filenum);
				originalPath = "d:/PM/BEngineer/";
				
				//원래 폴더 경로 대입
				for(int j = originalAddr.size() - 1; j >= 0; j--) {
					dto = (FileDTO)originalAddr.get(j);
					originalPath += dto.getOrgname();
					if(j!=0) originalPath+="/";
				}
			
				//받아온 filenum으로 복사될 폴더 리스트 받아오기
				newAddr = null;
				newAddr = filebean.getAddr(filenum);
				newPath = "d:/PM/BEngineer/";
			
				//복사될 폴더 경로 대입
				for(int j = newAddr.size() - 1; j >= 1; j--) {
					dto = (FileDTO)newAddr.get(j);
					newPath += dto.getOrgname() + "/";
				}
				
				dto = (FileDTO)originalAddr.get(0);
				String orgname = dto.getOrgname();
				String filename = dto.getFilename();
				File file = new File(originalPath);
				
				if(file.isFile()) { // 파일인 경우 filename, orgname 둘 다 복사본을 붙여준다.
					if(orgname.lastIndexOf(".")!=-1) {
						String orgFname = orgname.substring(0,orgname.lastIndexOf("."));
						String orgFtype = orgname.substring(orgname.lastIndexOf("."));
						String copyOrgname = orgFname + "-복사본" + orgFtype;
						dto.setOrgname(copyOrgname);
						orgname = copyOrgname;
						newPath += copyOrgname;
					}else {
						String copyOrgname = orgname + "-복사본";
						dto.setOrgname(copyOrgname);
						orgname = copyOrgname;
						newPath += copyOrgname;
					}
					
					if(filename.lastIndexOf(".")!=-1) {
						String fileFname = filename.substring(0,filename.lastIndexOf("."));
						String fileFtype = filename.substring(filename.lastIndexOf("."));
						String copyFilename = fileFname + "-복사본" + fileFtype;
						filename = copyFilename;
						dto.setFilename(copyFilename);
					}else {
						String copyFilename = filename + "-복사본";
						filename = copyFilename;
						dto.setFilename(copyFilename);
					}
				}else { // 폴더인 경우
					String copyOrgname = orgname + "-복사본";
					dto.setOrgname(copyOrgname);
					orgname = copyOrgname;
					newPath += copyOrgname;
					String copyFilename = filename + "-복사본";
					filename = copyFilename;
					dto.setFilename(copyFilename);
				} 
				
				while(!is_Copy) { //복사본의 이름이 존재할 때 존재하지 않는 이름으로 생성할 때까지 반복
					int folderref=0;
					folderref = ((FileDTO)newAddr.get(0)).getFolder_ref();
					dto.setFolder_ref(folderref);
					
					//복사될 곳의 파일 소유자을 설정해준다.
					String newOwner = ((FileDTO)newAddr.get(0)).getOwner(); 
					dto.setOwner(newOwner); 
					
					int orginsub = 0; //복사할 디렉터리의 넘버
					orginsub = ((FileDTO)originalAddr.get(0)).getNum();
					
					is_Copy = nioFilecopy(originalPath,newPath); //복사함수 실행
					
					if(is_Copy) { //복사가 완료되었을 경우 DB수정 및 하위 파일/폴더까지 DB 수정
						sqlSession.insert("bengineer.copyfile",dto);
						FileDTO copydto = (FileDTO)sqlSession.selectOne("bengineer.newCopyfiles",dto);
						sqlCopy(orginsub,copydto);
					}
					else { //복사가 안된 경우 다시 이름 설정 후 반복
						newPath = newPath.substring(0,(newPath.lastIndexOf("/")+1));
						if(file.isFile()) { // 파일인 경우
							if(orgname.lastIndexOf(".")!=-1) {
								String orgFname = orgname.substring(0,(orgname.lastIndexOf("본")+1));
								String orgFtype = orgname.substring(orgname.lastIndexOf("."));
								String copyOrgname = orgFname + count + orgFtype;
								dto.setOrgname(copyOrgname);
								orgname = copyOrgname;
								newPath += copyOrgname;
							}else {
								String orgFname = orgname.substring(0,(orgname.lastIndexOf("본")+1));
								String copyOrgname = orgFname + count;
								dto.setOrgname(copyOrgname);
								orgname = copyOrgname;
								newPath += copyOrgname;
							}
							
							if(filename.lastIndexOf(".")!=-1) {
								String fileFname = filename.substring(0,(filename.lastIndexOf("본")+1));
								String fileFtype = filename.substring(filename.lastIndexOf("."));
								String copyFilename = fileFname + count + fileFtype;
								filename = copyFilename;
								dto.setFilename(copyFilename);
							}else {
								String fileFname = filename.substring(0,(filename.lastIndexOf("본")+1));
								String copyFilename = fileFname + count;
								filename = copyFilename;
								dto.setFilename(copyFilename);
							}
						}else { // 폴더인 경우
							String orgFname = orgname.substring(0,(orgname.lastIndexOf("본")+1));
							String copyOrgname = orgFname + count;
							dto.setOrgname(copyOrgname);
							orgname = copyOrgname;
							newPath += copyOrgname;
							String fileFname = filename.substring(0,(filename.lastIndexOf("본")+1));
							String copyFilename = fileFname + count;
							filename = copyFilename;
							dto.setFilename(copyFilename);
						}
						count++; 
					}
				}
				is_Copy = false;
			}
		}else { //다른 위치에 복사를 할 시
			for(int i = 0; i < files.length; i++) { //선택한 파일/폴더들의 복사본이름 설정 후 복사 반복
				int filenum = 0;
				filenum = Integer.parseInt(files[i]);
				FileDTO dto2 = new FileDTO();
				
				//받아온 filenum으로 원래 폴더 리스트 받아오기
				originalAddr = filebean.getAddr(filenum);
				
				//원래 폴더 경로 대입
				for(int j = originalAddr.size() - 1; j >= 0; j--) {
					dto = (FileDTO)originalAddr.get(j);
					originalPath += dto.getOrgname();
					if(j!=0) originalPath+="/";
				}
				File file = new File(originalPath);
				dto2 = (FileDTO)originalAddr.get(0);
				String orgname = dto.getOrgname();
				
				//받아온 file_fref로 원래 폴더 리스트 받아오기
				newAddr = filebean.getAddr(file_fref);
				dto2 = (FileDTO)newAddr.get(0);
				String neworgname = dto2.getOrgname();
				
				if(file.isFile()) { // 복사하려는 파일/폴더가 파일일 경우
					String filetype = orgname.substring(orgname.lastIndexOf("."));
					String result = filebean.checkFile(filetype);
				
					// 복사할 위치의 폴더가 기본폴더인 경우
					if(neworgname.equals("image")||neworgname.equals("music")||neworgname.equals("video")||neworgname.equals("document")||neworgname.equals("etc")) {
						if(!neworgname.equals(result)) {
							is_Copy = false;
							flag = 1;
							break;
						}
					}
				}
			}
			
			if(flag == 0) { // 선택한 여러 파일/폴더 모두 다 복사할 수 있을 시
				for(int i = 0; i < files.length; i++) { //선택한 파일/복사들 모두 복사할 때까지 
					int filenum = 0;
					filenum = Integer.parseInt(files[i]);
					dto = null;
					
					//받아온 filenum으로 복사할 폴더 리스트 받아오기
					originalAddr = null;
					originalAddr = filebean.getAddr(filenum);
					originalPath = "d:/PM/BEngineer/";
					
					//원래 폴더 경로 대입
					for(int j = originalAddr.size() - 1; j >= 0; j--) {
						dto = (FileDTO)originalAddr.get(j);
						originalPath += dto.getOrgname();
						if(j!=0) originalPath+="/";
					}
				
					//받아온 file_fref으로 복사될 폴더 리스트 받아오기
				    newAddr = null;
					newAddr = filebean.getAddr(file_fref);
					newPath = "d:/PM/BEngineer/";
				
					//복사될 폴더 경로 대입
					for(int j = newAddr.size() - 1; j >= 0; j--) {
						dto = (FileDTO)newAddr.get(j);
						newPath += dto.getOrgname() + "/";
					}
				
					dto = (FileDTO)originalAddr.get(0);
					String orgname = dto.getOrgname();
					String filename = dto.getFilename();
					File file = new File(originalPath);
					
					if(file.isFile()) { // 파일인 경우
						if(orgname.lastIndexOf(".")!=-1) {
							String orgFname = orgname.substring(0,orgname.lastIndexOf("."));
							String orgFtype = orgname.substring(orgname.lastIndexOf("."));
							String copyOrgname = orgFname + "-복사본" + orgFtype;
							dto.setOrgname(copyOrgname);
							orgname = copyOrgname;
							newPath += copyOrgname;
						}else {
							String copyOrgname = orgname + "-복사본";
							dto.setOrgname(copyOrgname);
							orgname = copyOrgname;
							newPath += copyOrgname;
						}
						
						if(filename.lastIndexOf(".")!=-1) {
							String fileFname = filename.substring(0,filename.lastIndexOf("."));
							String fileFtype = filename.substring(filename.lastIndexOf("."));
							String copyFilename = fileFname + "-복사본" + fileFtype;
							filename = copyFilename;
							dto.setFilename(copyFilename);
						}else {
							String copyFilename = filename + "-복사본";
							filename = copyFilename;
							dto.setFilename(copyFilename);
						}
					}else { // 폴더인 경우
						String copyOrgname = orgname + "-복사본";
						dto.setOrgname(copyOrgname);
						orgname = copyOrgname;
						newPath += copyOrgname;
						String copyFilename = filename + "-복사본";
						filename = copyFilename;
						dto.setFilename(copyFilename);
					} 
					while(!is_Copy) { //복사본 이름이 중복되지 않을 때까지 반복
						int folderref=0;
						folderref = ((FileDTO)newAddr.get(0)).getNum();
						int orginsub = 0; //복사할 디렉터리의 넘버
						orginsub = ((FileDTO)originalAddr.get(0)).getNum();
						String newOwner = ((FileDTO)newAddr.get(0)).getOwner(); //
						dto.setOwner(newOwner); //
						dto.setFolder_ref(folderref);
						is_Copy = nioFilecopy(originalPath,newPath);
					
						if(is_Copy) {
							sqlSession.insert("bengineer.copyfile",dto);
							FileDTO copydto = (FileDTO)sqlSession.selectOne("bengineer.newCopyfiles",dto);
							sqlCopy(orginsub,copydto);
						}
						else {
							newPath = newPath.substring(0,(newPath.lastIndexOf("/")+1));
							
							if(file.isFile()) { // 파일인 경우
								if(orgname.lastIndexOf(".")!=-1) {
									String orgFname = orgname.substring(0,(orgname.lastIndexOf("본")+1));
									String orgFtype = orgname.substring(orgname.lastIndexOf("."));
									String copyOrgname = orgFname + count + orgFtype;
									dto.setOrgname(copyOrgname);
									orgname = copyOrgname;
									newPath += copyOrgname;
								}else {
									String orgFname = orgname.substring(0,(orgname.lastIndexOf("본")+1));
									String copyOrgname = orgFname + count;
									dto.setOrgname(copyOrgname);
									orgname = copyOrgname;
									newPath += copyOrgname;
								}
									
								if(filename.lastIndexOf(".")!=-1) {
									String fileFname = filename.substring(0,(filename.lastIndexOf("본")+1));
									String fileFtype = filename.substring(filename.lastIndexOf("."));
									String copyFilename = fileFname + count + fileFtype;
									filename = copyFilename;
									dto.setFilename(copyFilename);
								}else {
									String fileFname = filename.substring(0,(filename.lastIndexOf("본")+1));
									String copyFilename = fileFname + count;
									filename = copyFilename;
									dto.setFilename(copyFilename);
								}
							}else { // 폴더인 경우
								String orgFname = orgname.substring(0,(orgname.lastIndexOf("본")+1));
								String copyOrgname = orgFname + count;
								dto.setOrgname(copyOrgname);
								orgname = copyOrgname;
								newPath += copyOrgname;
								String fileFname = filename.substring(0,(filename.lastIndexOf("본")+1));
								String copyFilename = fileFname + count;
								filename = copyFilename;
								dto.setFilename(copyFilename);
							}	
							count++;	
						}
					}
					is_Copy = false;
				}
			}
			
		}	
		
		if(flag==1) { //형식이 맞지 않은 경우
			model.addAttribute("alert", "해당폴더에 복사할수 없는 파일의 형식입니다.");
		}else { //복사가 완료됐을 경우
			model.addAttribute("alert", "파일 복사가 완료되었습니다..");
		}	
		
		model.addAttribute("location", "\"/BEngineer/beFiles/beMyList.do?folder=0"+ "\"");
		
		//세션을 사용하고나면 다음 파일 작업을 위해 세션을 삭제한다.
		session.removeAttribute("ref");
		session.removeAttribute("file_flag");
		return "beFiles/alert";
	}
	
	@RequestMapping("beImagePreview.do") //기본이미지 폴더 이동 페이지
	public String beImagePreview(HttpSession session, Model model, int folder) {
		if(MainBean.loginCheck(session)) {return "redirect:/beMember/beLogin.do";} // 비 로그인 상태시 로그인 창으로 리디렉트
		FileBean filebean = new FileBean(); //FileBean 객체 생성
		filebean.setSqlSession(sqlSession);
		if(!filebean.checkSpace(session, sqlSession)) {
			model.addAttribute("alert", "사용할 수 있는 용량을 초과했습니다. 용량을 확보해주세요");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}
		
		sqlSession.update("bengineer.setsize",folder);
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
				folder_ref = (int)sqlSession.selectOne("bengineer.getref", dto); 
				address_ref = filebean.getAddr(folder_ref);
			}else {
				folder_ref = folder;
				address_ref = filebean.getAddr(folder_ref);
			}
		}
		List filelist = sqlSession.selectList("bengineer.getfiles", folder_ref);
		sqlSession.update("bengineer.hit", folder);
		model.addAttribute("list", filelist);
		List folderaddress = new ArrayList(); 
		List orgaddress = new ArrayList(); 
		if(address_ref.size() < 5) {
			for(int i = address_ref.size() - 1; i >= 0; i--) {
				dto = (FileDTO)address_ref.get(i);
				folderaddress.add(dto.getFilename());
				orgaddress.add(dto.getNum());
			}
		}else {
			folderaddress.add(nickname);
			orgaddress.add(0);
			folderaddress.add("..."); 
			orgaddress.add(null);
			for(int i = 2; i >= 0; i--) {
				dto = (FileDTO)address_ref.get(i);
				folderaddress.add(dto.getFilename());
				orgaddress.add(dto.getNum());
			}
		}
		if(!owner.equals(dto.getOwner())) {
			model.addAttribute("alert", "잘못된 접근입니다.");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}
		
		int imagenum = sqlSession.selectOne("bengineer.imageNum", owner);
		FileDTO dto2 = new FileDTO();
		dto2.setOwner(owner);
		dto2.setFolder_ref(imagenum);
		List imageList = sqlSession.selectList("bengineer.imageList", dto2);
		model.addAttribute("imageList",imageList);
		model.addAttribute("folderaddress", folderaddress);
		model.addAttribute("orgaddress", orgaddress);
		model.addAttribute("folder_ref", folder_ref);
		model.addAttribute("folder",folder); // 상위폴더로 이동하기 위해
		List font = sqlSession.selectList("bengineer.font", owner);
 		model.addAttribute("font",font);	// 검색에 필요한 파일목록들

		model.addAttribute("space", filebean.viewSpace(owner, sqlSession));
		return "beFiles/beImagePreview";
	}
	
	@RequestMapping("beImageview.do")
	public String beImageview(String imageName,Model model, HttpSession session) {
		String owner = (String)session.getAttribute("id");
		model.addAttribute("owner",owner);
		model.addAttribute("imageName",imageName);
		return "beFiles/beImageview";
	}
	
	@RequestMapping("beCancel.do") //선택한 파일/폴더들 세션 제거
	public String beCancel(Model model, int folder, HttpSession session) {
		
		//세션 제거
		session.removeAttribute("ref");
		session.removeAttribute("file_flag");
		
		FileDTO filedto = new FileDTO();
		int folder_ref = folder;
		filedto = sqlSession.selectOne("bengineer.getaddr",folder_ref);
		
		if(filedto == null) 
			model.addAttribute("location","\"/BEngineer/beFiles/beSharedList.do?folder=0\"");
		else if(filedto.getFilename().equals("image") && filedto.getImportant()==-1) //기본 이미지폴더일 경우
			model.addAttribute("location","\"/BEngineer/beFiles/beImagePreview.do?folder="+folder+ "\"");
		else
			model.addAttribute("location","\"/BEngineer/beFiles/beMyList.do?folder="+folder+ "\"");
		return "beFiles/location";
	}
	
	
	@RequestMapping("hot.do")
	public String hot(int num,Model model) {
		String a = "history.go(-1)";
		model.addAttribute("location",a);
		sqlSession.update("bengineer.hot",num);
		return "beFiles/location";
	}
	@RequestMapping("exhot.do")
	public String exhot(int num,Model model) {
		String a = "history.go(-1)";
		model.addAttribute("location",a);
		sqlSession.update("bengineer.exhot",num);
		return "beFiles/location";
	}
	@RequestMapping("hotlist.do")
	public String hotlist(HttpSession session,Model model) {
		if(MainBean.loginCheck(session)) {return "redirect:/beMember/beLogin.do";}
		FileBean filebean = new FileBean();
		if(!filebean.checkSpace(session, sqlSession)) {
			model.addAttribute("alert", "사용할 수 있는 용량을 초과했습니다. 용량을 확보해주세요");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}
		List folderaddress = new ArrayList();
		List orgaddress = new ArrayList();
		String owner = (String) session.getAttribute("id");
		List list = sqlSession.selectList("bengineer.hotlist",owner);
		folderaddress.add("즐겨찾기");
		orgaddress.add(null);
		model.addAttribute("list",list);
		model.addAttribute("folderaddress", folderaddress);
		model.addAttribute("orgaddress", orgaddress);
		model.addAttribute("folder_ref", -5);
		model.addAttribute("folder", 0); // 상위폴더로 이동하기 위해
		List font = sqlSession.selectList("bengineer.font", owner);
 		model.addAttribute("font",font);	// 검색에 필요한 파일목록들
		return "beFiles/beList";
	}
 	@RequestMapping("searchForm.do")
    public String searchForm(HttpSession session, String result,Model model) {
		if(MainBean.loginCheck(session)) {return "redirect:/beMember/beLogin.do";}
		FileBean filebean = new FileBean();
		if(!filebean.checkSpace(session, sqlSession)) {
			model.addAttribute("alert", "사용할 수 있는 용량을 초과했습니다. 용량을 확보해주세요");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}
		String id = (String)session.getAttribute("id");
       int i = 0;
       int m = 0; // 각 검색된 단어들의 시작위치를 표시
       int count = StringUtils.countOccurrencesOf(result,",");   // result에 ','갯수
       int comma[] = new int[count];	// ',' 갯수만큼
       String[] str = new String[count];
       if(result.length() > 0){
          int l = 0 ;
          for(i=0;i<count;i++) {
        	  l = result.indexOf(",", l + 1);
	          comma[i]=l;
        	  if(i==0) {
        		  str[i]=result.substring(m,comma[i]);
        	  }else {
		          str[i]=result.substring(m+1,comma[i]);
        	  }
        	  m=comma[i];
          }
       }
      if(count>0) { // 검색어가 있을때
 	  java.util.List<String> list = new ArrayList<String>(Arrays.asList(str));
 	  ListDTO ldto = new ListDTO();
 	  ldto.setList(list);
 	  ldto.setString(id);
 	  List filelist = sqlSession.selectList("bengineer.searchfiles", ldto);
		List folderaddress = new ArrayList();
		List orgaddress = new ArrayList();
		folderaddress.add("검색결과");
		orgaddress.add(null);
 	  model.addAttribute("list",filelist);
		model.addAttribute("folderaddress", folderaddress);
		model.addAttribute("orgaddress", orgaddress);
      }else {
  		List folderaddress = new ArrayList();
  		List orgaddress = new ArrayList();
  		folderaddress.add("검색결과가 없습니다.");
  		orgaddress.add(null);
   	  model.addAttribute("list", null);
  		model.addAttribute("folderaddress", folderaddress);
  		model.addAttribute("orgaddress", orgaddress);
      }
      model.addAttribute("folder_ref", -5);
      model.addAttribute("folder", 0); // 상위폴더로 이동하기 위해
      model.addAttribute("space", filebean.viewSpace(id, sqlSession));
      List font = sqlSession.selectList("bengineer.font", id);
      model.addAttribute("font",font);	// 검색에 필요한 파일목록들
 	  return "beFiles/beList";

 	}
 	
 	//기본 폴더 형식에 맞을시 파일 이동
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
	
 	//파일 이동
	public boolean nioFilemove(String originFilePath, String moveFilePath) { //파일이동, NIO방식
		File newfile = new File(moveFilePath);
		Path originPath = Paths.get(originFilePath);
		Path movePath = Paths.get(moveFilePath);
		
		if(newfile.exists()) { //이동할 곳의 파일/폴더가 이미 존재할 경우
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
	
	//파일 복사
	public boolean nioFilecopy(String originFilePath, String moveFilePath) { //�뙆�씪�씠�룞, NIO諛⑹떇
		File newfile = new File(moveFilePath);
		File orginfile = new File(originFilePath);
		Path originPath = Paths.get(originFilePath);
		Path movePath = Paths.get(moveFilePath);
		
		if(newfile.exists()) { //복사할 곳의 파일/폴더가 이미 존재할 경우
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
			Files.copy(originPath, movePath, StandardCopyOption.REPLACE_EXISTING);
			
			//복사할 파일/폴더가 폴더인 경우 하위 파일/폴더까지 복사
			if(orginfile.isDirectory())
				copys(orginfile,newfile);
		}catch(IOException e) {
			e.printStackTrace();
			return false;
		}
		if(Files.exists(movePath,new LinkOption[] {})) {
			return true;
		}else {
			System.out.println("파일복사에 실패했습니다.");
			return false;
		}	
	}
	
	//하위 파일/폴더 복사
	public  void copys(File selectFile, File copyFile) { //복사할 디렉토리, 복사될 디렉토리
        File[] ff = selectFile.listFiles();  //복사할 디렉토리안의 폴더와 파일들을 불러옴
        
        for (File file : ff) { //파일/폴더들이 존재할 경우
        	File temp = new File(copyFile.getAbsolutePath() +"\\"+ file.getName());  //디렉토리 내에서 복사할 폴더,파일들을 순차적으로 선택
          
        	if (file.isDirectory()){ //만약 파일이 아니고 폴더라면
        		temp.mkdirs();          //복사될 위치에 똑같이 폴더를 생성
        		copys(file, temp);      //폴더의 내부를 다시 살펴봄
        	}else{                   //만약 파일이면 복사작업을 진행
        		FileInputStream fis = null;
        		FileOutputStream fos = null;
        		
        		try {
        			fis = new FileInputStream(file);
        			fos = new FileOutputStream(temp);
        			byte[] b = new byte[4096];  
        			int cnt = 0;
            
        			while ((cnt = fis.read(b)) != -1) {  //복사할 파일에서 데이터를 읽고
        				fos.write(b, 0, cnt);            //복사될 위치의 파일에 데이터를 씀
        			}
        		}catch (Exception e) {
        			e.printStackTrace();
        		} finally {
        			try {
        				//사용이 끝나면 input,outputStream을 닫아준다.
        				fis.close();
        				fos.close();
        			} catch (IOException e) {
        				e.printStackTrace();
        			}
        		}
        	}
        }
   }
	
	public  boolean checkFolder(File selectFolder, String copyFolder) { //복사할 디렉토리, 복사될 디렉토리
        File[] ff = selectFolder.listFiles();  //복사할 디렉토리안의 폴더와 파일들을 불러옴
        FileBean filebean = new FileBean(); //Filebean 객체 생성
        boolean is_Check = true; //복사가 완료됐는지 여부
        
        for (File file : ff) { //복사할 디렉토리안의 파일/폴더가 존재할 시
        	if(file.isFile()) { //파일인 경우 기본폴더 안에 옮길 시 형식에 맞는지 확인
        		if(copyFolder.equals("image")||copyFolder.equals("music")||copyFolder.equals("video")||copyFolder.equals("document")||copyFolder.equals("etc")) {
        			String filename = file.getName();
            		String filetype = filename.substring(filename.lastIndexOf("."));
            		String result = filebean.checkFile(filetype);
            		
            		//기본폴더 안에 맞는 형식이 아닐경우 복사가 실패했다고 리턴하기위해 is_Check를 false로 설정
        			if(!copyFolder.equals(result)) { 
    					is_Check=false;
    					break;
    				}
        		}
        	}
        }
        return is_Check; 
   }
	
	public void sqlCopy(int num, FileDTO dto) { //하위 파일/폴더까지 복사하여 DB insert
		List<FileDTO> list = sqlSession.selectList("bengineer.subCopyfiles", num); //복사 폴더를 참조하고 있는 파일리스트를 불러온다.
		for(FileDTO copydto : list) { //파일리스트가 존재하면
			
			//복사될 폴더의 folder_ref로 설정 후 insert
			copydto.setFolder_ref(dto.getNum()); 
			sqlSession.insert("bengineer.copyfile",copydto);
			
			sqlCopy(copydto.getNum(),copydto); //자신의 하위폴더가 있는지 다시 함수호출해서 설정, 재귀함수
		}
	}
}
