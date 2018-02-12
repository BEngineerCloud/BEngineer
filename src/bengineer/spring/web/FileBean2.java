package bengineer.spring.web;

import org.rosuda.REngine.*;
import org.rosuda.REngine.Rserve.*;
import java.io.File;
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
		FileBean filebean = new FileBean();
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
		boolean exist_file = false;
		Integer moveParent = 0;
		File path = new File("d:/PM/BEngineer/" + owner); //사용자폴더 경로
		File[] list = path.listFiles(); //사용자폴더 안에 모든 파일/폴더 받아오기
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
	
	@RequestMapping("beMove.do") //�뙆�씪/�뤃�뜑 �씠�룞
	public String beMove(HttpSession session, Model model, int ref, int folder_ref) {
		if(MainBean.loginCheck(session)) {return "redirect:/beMember/beLogin.do";} // 鍮� 濡쒓렇�씤 �긽�깭�떆 濡쒓렇�씤 李쎌쑝濡� 由щ뵒�젆�듃
		FileBean filebean = new FileBean();
		if(!filebean.checkSpace(session, sqlSession)) {
			model.addAttribute("alert", "사용할 수 있는 용량을 초과했습니다. 용량을 확보해주세요");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}
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
		model.addAttribute("location", "\"/BEngineer/beFiles/beMyList.do?folder=0"+ "\"");
		return "beFiles/alert";
	}
	@RequestMapping("beCopy.do") //�뙆�씪/�뤃�뜑 �씠�룞
	public String beCopy(HttpSession session, Model model, int ref, int folder_ref) {
		if(MainBean.loginCheck(session)) {return "redirect:/beMember/beLogin.do";} // 鍮� 濡쒓렇�씤 �긽�깭�떆 濡쒓렇�씤 李쎌쑝濡� 由щ뵒�젆�듃
		FileBean filebean = new FileBean();
		if(!filebean.checkSpace(session, sqlSession)) {
			model.addAttribute("alert", "사용할 수 있는 용량을 초과했습니다. 용량을 확보해주세요");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}
		filebean.setSqlSession(sqlSession);
		List originalAddr = null;
		originalAddr = filebean.getAddr(ref);
		String originalPath = "d:/PM/BEngineer/";
		List newAddr = null;
		newAddr = filebean.getAddr(folder_ref);
		String newPath = "d:/PM/BEngineer/";
		FileDTO dto = new FileDTO();
		int count=2;
		boolean is_Copy = false;
		if(ref==folder_ref) { // 자기 위치에 복사하려할 때
			for(int i = originalAddr.size() - 1; i >= 0; i--) {
				dto = (FileDTO)originalAddr.get(i);
				originalPath += dto.getOrgname();
				if(i!=0) originalPath+="/";
			}
			
			for(int i = newAddr.size() - 1; i >= 1; i--) {
				dto = (FileDTO)newAddr.get(i);
				newPath += dto.getOrgname() + "/";
			}
			
		}else {
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
		//filename, orgname 둘 다 복사본을 붙여준다.
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
		
		while(!is_Copy) {
			int folderref=0;
			if(ref==folder_ref) // 자기 위치에 복사하면 자기 위치보다 상위위치의 folder_ref를 받아온다. 
				folderref = ((FileDTO)newAddr.get(0)).getFolder_ref();
			else
				folderref = ((FileDTO)newAddr.get(0)).getNum();
			
			dto.setFolder_ref(folderref);
			int flag = 0;
		
			if(file.isFile()) { // 복사하려는 파일/폴더가 파일일 경우
				String filetype = orgname.substring(orgname.lastIndexOf("."));
				String newFolder = ((FileDTO)newAddr.get(0)).getOrgname();
				String result = filebean.checkFile(filetype);
		
				// 복사할 위치의 폴더가 기본폴더인 경우
				if(newFolder.equals("image")||newFolder.equals("music")||newFolder.equals("video")||newFolder.equals("document")||newFolder.equals("etc")) {
					if(!newFolder.equals(result)) {
						is_Copy = false;
						flag = 1;
					}
					else {
					is_Copy = nioFilecopy(originalPath,newPath);
					}
				}else {
					is_Copy = nioFilecopy(originalPath,newPath);
				}
			}else {
				is_Copy = nioFilecopy(originalPath,newPath);
			}
		
			if(is_Copy) {
				sqlSession.insert("bengineer.copyfile",dto);
				model.addAttribute("alert", "파일/폴더 복사가 완료되었습니다.");
			}
			else {
				newPath = newPath.substring(0,(newPath.lastIndexOf("/")+1));
				if(flag==1) {
					model.addAttribute("alert", "해당폴더에 복사할수 없는 파일의 형식입니다.");
				}else {
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
		}
		model.addAttribute("location", "\"/BEngineer/beFiles/beMyList.do?folder=0"+ "\"");
		return "beFiles/alert";
	}
	
	@RequestMapping("beMultimove.do") // 여러파일/폴더 선택
	public String beMultimove(String file_ref,int file_fref, HttpSession session,  Model model) {
		if(MainBean.loginCheck(session)) {return "redirect:/beMember/beLogin.do";} 
		FileBean filebean = new FileBean();
		if(!filebean.checkSpace(session, sqlSession)) {
			model.addAttribute("alert", "사용할 수 있는 용량을 초과했습니다. 용량을 확보해주세요");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}
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
				newPath += orgname;
				int num = dto.getNum();
				dto.setNum(num);
				int folderref = ((FileDTO)newAddr.get(0)).getNum();
				dto.setFolder_ref(folderref);
			
				is_Move = nioFilemove(originalPath,newPath);
			
				if(is_Move)
					sqlSession.update("bengineer.changeref",dto);
				else 
					break;
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
	
	@RequestMapping("beMulticopy.do") // 여러파일/폴더 선택
	public String beMulticopy(String file_ref,String file_fref, HttpSession session,  Model model) {
		if(MainBean.loginCheck(session)) {return "redirect:/beMember/beLogin.do";} 
		FileBean filebean = new FileBean();
		if(!filebean.checkSpace(session, sqlSession)) {
			model.addAttribute("alert", "사용할 수 있는 용량을 초과했습니다. 용량을 확보해주세요");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}
		filebean.setSqlSession(sqlSession);
		boolean is_Copy = false;
		String files[] = file_ref.split(",");
		int flag = 0;
		int count = 2;
		FileDTO dto = new FileDTO();
		List originalAddr = null;
		String originalPath = "d:/PM/BEngineer/";
		List newAddr = null;
		String newPath = "d:/PM/BEngineer/";

		if(file_ref.equals(file_fref)) { // 자기자신의 복사하려할 때
			for(int i = 0; i < files.length; i++) {
				int filenum = 0;
				filenum = Integer.parseInt(files[i]);
				
				dto = null;
				originalAddr = null;
				originalAddr = filebean.getAddr(filenum);
				originalPath = "d:/PM/BEngineer/";
				
				for(int j = originalAddr.size() - 1; j >= 0; j--) {
					dto = (FileDTO)originalAddr.get(j);
					originalPath += dto.getOrgname();
					if(j!=0) originalPath+="/";
				}
			
				newAddr = null;
				newAddr = filebean.getAddr(filenum);
				newPath = "d:/PM/BEngineer/";
			
				for(int j = newAddr.size() - 1; j >= 1; j--) {
					dto = (FileDTO)newAddr.get(j);
					newPath += dto.getOrgname() + "/";
				}
				
				dto = (FileDTO)originalAddr.get(0);
				//filename, orgname 둘 다 복사본을 붙여준다.
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
				
				while(!is_Copy) {
					int folderref=0;
					folderref = ((FileDTO)newAddr.get(0)).getFolder_ref();
					dto.setFolder_ref(folderref);
				
					is_Copy = nioFilecopy(originalPath,newPath);
					if(is_Copy) 
						sqlSession.insert("bengineer.copyfile",dto);
						
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
		}else {
			int file_fref2 = Integer.parseInt(file_fref);
			for(int i = 0; i < files.length; i++) { // 기본폴더에 옮길 때 올바른 형식의 파일/폴더들이 옮겨지는지 검사
				int filenum = 0;
				filenum = Integer.parseInt(files[i]);
				FileDTO dto2 = new FileDTO();
				
				originalAddr = filebean.getAddr(filenum);
				
				for(int j = originalAddr.size() - 1; j >= 0; j--) {
					dto = (FileDTO)originalAddr.get(j);
					originalPath += dto.getOrgname();
					if(j!=0) originalPath+="/";
				}
				File file = new File(originalPath);
				dto2 = (FileDTO)originalAddr.get(0);
				String orgname = dto.getOrgname();
				
				newAddr = filebean.getAddr(file_fref2);
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
				for(int i = 0; i < files.length; i++) {
					int filenum = 0;
					filenum = Integer.parseInt(files[i]);
					dto = null;
					originalAddr = null;
					originalAddr = filebean.getAddr(filenum);
					originalPath = "d:/PM/BEngineer/";
					
					for(int j = originalAddr.size() - 1; j >= 0; j--) {
						dto = (FileDTO)originalAddr.get(j);
						originalPath += dto.getOrgname();
						if(j!=0) originalPath+="/";
					}
				
					 newAddr = null;
					newAddr = filebean.getAddr(file_fref2);
					newPath = "d:/PM/BEngineer/";
				
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
					while(!is_Copy) {
						int folderref=0;
						folderref = ((FileDTO)newAddr.get(0)).getNum();
						
						dto.setFolder_ref(folderref);
						is_Copy = nioFilecopy(originalPath,newPath);
					
						if(is_Copy) 
							sqlSession.insert("bengineer.copyfile",dto);
					
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
			if(flag==1) {
				model.addAttribute("alert", "해당폴더에 복사할수 없는 파일의 형식입니다.");
			}else {
				model.addAttribute("alert", "파일 복사가 완료되었습니다..");
			}	
		
		
		model.addAttribute("location", "\"/BEngineer/beFiles/beMyList.do?folder=0"+ "\"");
		return "beFiles/alert";
	}
	
	@RequestMapping("beMultilist.do") // 여러 파일/폴더 선택 후 내파일 이동시
	public String beMultilist(HttpSession session, Model model, int folder,@RequestParam(value="multifile_Ref", defaultValue="0") String multifile_Ref, @RequestParam(value="multifile_FRef", defaultValue="0") int multifile_FRef, int flag) {
		if(MainBean.loginCheck(session)) {return "redirect:/beMember/beLogin.do";} // 濡쒓렇�씤 �꽭�뀡 �뾾�쓣 �떆 由щ뵒�젆�듃
		FileBean filebean = new FileBean();
		if(!filebean.checkSpace(session, sqlSession)) {
			model.addAttribute("alert", "사용할 수 있는 용량을 초과했습니다. 용량을 확보해주세요");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}
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
		if(flag==1) {
			model.addAttribute("multifile_Ref",multifile_Ref); 	// 이동할 파일/폴더
			if(multifile_FRef==0) {
				model.addAttribute("multifile_FRef",folder);  // 이동될 위치
			}else {
				model.addAttribute("multifile_FRef",multifile_FRef);  // 이동될 위치
			}
			model.addAttribute("multifile_CRef","");
			model.addAttribute("multifile_CFRef",0);
		}else {
			model.addAttribute("multifile_CRef",multifile_Ref); 	// 이동할 파일/폴더
			if(multifile_FRef==0) {
				model.addAttribute("multifile_CFRef",folder);  // 이동될 위치
			}else {
				model.addAttribute("multifile_CFRef",multifile_FRef);  // 이동될 위치
			}
			model.addAttribute("multifile_Ref","");
			model.addAttribute("multifile_FRef",0);
		}
		model.addAttribute("movefile_Ref",0); 	// 이동할 파일/폴더
		model.addAttribute("movefile_FRef",0);  // 이동될 위치
		model.addAttribute("copyfile_Ref",0); 	// 이동할 파일/폴더
		model.addAttribute("copyfile_FRef",0);  // 이동될 위치
		//@@ model.addAttribute("space", filebean.viewSpace(owner, sqlSession));
		return "beFiles/beList";
	}
	
	
	@RequestMapping("beCopyList.do") // 여러 파일/폴더 선택 후 내파일 이동시
	public String beCopyList(HttpSession session, Model model, int folder,@RequestParam(value="copyfile_Ref", defaultValue="0") int copyfile_Ref, @RequestParam(value="copyfile_FRef", defaultValue="0") int copyfile_FRef) {
		if(MainBean.loginCheck(session)) {return "redirect:/beMember/beLogin.do";} // 濡쒓렇�씤 �꽭�뀡 �뾾�쓣 �떆 由щ뵒�젆�듃
		FileBean filebean = new FileBean();
		if(!filebean.checkSpace(session, sqlSession)) {
			model.addAttribute("alert", "사용할 수 있는 용량을 초과했습니다. 용량을 확보해주세요");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}
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
		model.addAttribute("copyfile_Ref",copyfile_Ref);
		model.addAttribute("copyfile_FRef",copyfile_FRef);
		model.addAttribute("movefile_Ref",0); 	// 이동할 파일/폴더
		model.addAttribute("movefile_FRef",0);  // 이동될 위치
		//@@ model.addAttribute("space", filebean.viewSpace(owner, sqlSession));
		return "beFiles/beList";
	}
	
	@RequestMapping("beImagePreview.do") // 여러 파일/폴더 선택 후 내파일 이동시
	public String beImagePreview(HttpSession session, Model model, int folder) {
		if(MainBean.loginCheck(session)) {return "redirect:/beMember/beLogin.do";} 
		FileBean filebean = new FileBean();
		if(!filebean.checkSpace(session, sqlSession)) {
			model.addAttribute("alert", "사용할 수 있는 용량을 초과했습니다. 용량을 확보해주세요");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}
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

		//@@ model.addAttribute("space", filebean.viewSpace(owner, sqlSession));
		return "beFiles/beImagePreview";
	}
	
	// 02.06 수정한부분
		@RequestMapping("beImageview.do")
		public String beImageview(String imageName,Model model, HttpSession session) {
			String owner = (String)session.getAttribute("id");
			model.addAttribute("owner",owner);
			model.addAttribute("imageName",imageName);
			return "beFiles/beImageview";
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
	public boolean nioFilecopy(String originFilePath, String moveFilePath) { //�뙆�씪�씠�룞, NIO諛⑹떇
		File newfile = new File(moveFilePath);
		
		Path originPath = Paths.get(originFilePath);
		Path movePath = Paths.get(moveFilePath);
		if(newfile.exists()) {
			return false;
		}
		if(originPath==null) {
			throw new IllegalArgumentException("orginPath�뿉 留욌뒗 �뜲�씠�꽣 �삎�떇�쓣 �꽔�뼱二쇱꽭�슂.");
		}
		if(movePath==null) {
			throw new IllegalArgumentException("orginPath�뿉 留욌뒗 �뜲�씠�꽣 �삎�떇�쓣 �꽔�뼱二쇱꽭�슂.");
		}
		if(!Files.exists(originPath, new LinkOption[] {})) {
			throw new IllegalArgumentException("orginFile�씠 議댁옱�븯吏� �븡�뒿�땲�떎."+originPath.toString());
			
		}
		try {
			Files.copy(originPath, movePath, StandardCopyOption.REPLACE_EXISTING);
		}catch(IOException e) {
			e.printStackTrace();
			return false;
		}
		if(Files.exists(movePath,new LinkOption[] {})) {
			return true;
		}else {
			System.out.println("�뙆�씪�씠�룞�뿉 �떎�뙣�뻽�뒿�땲�떎.");
			return false;
		}	
	}
	@RequestMapping("hot.do")
	public String hot(int num) {
		System.out.println("num : " + num);
		//sqlSession.update("bengineer.hot",num);
		return "beFiles/hotlist";
	}
	@RequestMapping("exhot.do")
	public String exhot(int num) {
		System.out.println("num : " + num);
		sqlSession.update("bengineer.exhot",num);
		return "beFiles/hotlist";
	}
	@RequestMapping("hotlist.do")
	public String hotlist(HttpSession session,String owner,Model model) {
		if(MainBean.loginCheck(session)) {return "redirect:/beMember/beLogin.do";}
		FileBean filebean = new FileBean();
		if(!filebean.checkSpace(session, sqlSession)) {
			model.addAttribute("alert", "사용할 수 있는 용량을 초과했습니다. 용량을 확보해주세요");
			model.addAttribute("location", "history.go(-1)");
			return "beFiles/alert";
		}
		List folderaddress = new ArrayList();
		List orgaddress = new ArrayList();
		List list = sqlSession.selectList("bengineer.hotlist",owner);
		folderaddress.add("즐겨찾기");
		orgaddress.add(null);
		model.addAttribute("list",list);
		model.addAttribute("folderaddress", folderaddress);
		model.addAttribute("orgaddress", orgaddress);
		model.addAttribute("folder_ref", -5);
		model.addAttribute("folder", 0); // 상위폴더로 이동하기 위해
		model.addAttribute("movefile_Ref",0);
		model.addAttribute("movefile_FRef",0);
		return "beFiles/hotlist";
	}
 	@RequestMapping("searchForm.do")
    public String searchForm(HttpSession session, String result,Model model,String filename) {
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
		FileDTO dto = new FileDTO();
		dto.setFilename(filename);
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
 	  List filelist = sqlSession.selectList("bengineer.searchfiles", list);
		List folderaddress = new ArrayList();
		List orgaddress = new ArrayList();
		folderaddress.add("검색");
		orgaddress.add(null);
 	  model.addAttribute("list",filelist);
		model.addAttribute("folderaddress", folderaddress);
		model.addAttribute("orgaddress", orgaddress);
		model.addAttribute("folder_ref", -5);
		model.addAttribute("folder", 0); // 상위폴더로 이동하기 위해
		model.addAttribute("movefile_Ref",0);
		model.addAttribute("movefile_FRef",0);
		model.addAttribute("space", filebean.viewSpace(id, sqlSession));
      }
 	  return "beFiles/beList";

 	}
}
