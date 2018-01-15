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
	public String autoArrangefile(HttpSession session) {
		String owner = (String)session.getAttribute("id");
		String filePath=""; //파일경로
		String fileName=""; //파일이름
		String fileType=""; //파일확장자
		String moveFolder=""; //이동할 파일폴더
		File path = new File("d:/PM/BEngineer/" + owner); //사용자폴더 경로
		File[] list = path.listFiles(); //사용자폴더 안에 모든 파일/폴더 받아오기
		
		if(list.length > 0){
		    for(int i=0; i < list.length; i++){
		    	if(list[i].isFile()) { //파일인지 판단, 파일이면
		    		fileName=list[i].getName();
		    		filePath=list[i].getPath();
		    		fileType = fileName.substring(fileName.lastIndexOf("."));
		    		System.out.println(fileType);
		    		moveFolder = checkFile(fileType); //파일확장자 범주에 들어가는 폴더이름
		    		boolean success = true;
		    		success = moveFile(filePath, fileName, moveFolder); //파일 이동
		    		if(success) {
		    			System.out.println("성공");
		    		}
		    	}
		    }
		}
		return "beMain";
	}

	// 파일타입 확인용 리스트들
	private static List image = new ArrayList();
	private static List video = new ArrayList();
	private static List music = new ArrayList();
	private static List document = new ArrayList();
	private static List etc = new ArrayList();
	public FileBean2() {
		image.add(".jpeg");
		image.add(".jpg");
		image.add(".png");
		image.add(".gif");
		image.add(".bmp");
		video.add(".webm");
		video.add(".mpeg4");
		video.add(".3gpp");
		video.add(".mov");
		video.add(".avi");
		video.add(".mpegps");
		video.add(".wmv");
		video.add(".flv");
		video.add(".ogg");
		music.add(".mp3");
		music.add(".mpeg");
		music.add(".wav");
		document.add(".txt");
		document.add(".doc");
		document.add(".docx");
		document.add(".xls");
		document.add(".xlsx");
		document.add(".ppt");
		document.add(".pptx");
		etc.add(".zip");
		etc.add(".rar");
		etc.add(".tar");
		etc.add(".gzip");
		etc.add(".css");
		etc.add(".html");
		etc.add(".php");
		etc.add(".c");
		etc.add(".cpp");
		etc.add(".h");
		etc.add(".hpp");
		etc.add(".js");
		image.add(".dxf");
		image.add(".ai");
		image.add(".psd");
		document.add(".pdf");
		document.add(".xps");
		image.add(".eps");
		etc.add(".ps");
		image.add(".svg");
		image.add(".tiff");
		etc.add(".ttf");
	}
	private String checkFile(String filetype) { // 파일타입 확인용 메서드
		String result = null;
		if(image.contains(filetype)) {
			result = "image";
		}else if(music.contains(filetype)) {
			result = "music";
		}else if(video.contains(filetype)) {
			result = "video";
		}else if(document.contains(filetype)) {
			result = "document";
		}else if(etc.contains(filetype)) {
			result = "etc";
		}else {
			result = "none";
		}
		return result;
	}
	private boolean moveFile(String filePath, String fileName, String moveFolder) {
		boolean is_Move = false;
		String filePathtemp=filePath.substring(0, filePath.lastIndexOf(fileName));
		if(moveFolder.equals("image")) {
			is_Move = nioFilemove(filePath, filePathtemp+"image/"+fileName);
		}else if(moveFolder.equals("music")) {
			is_Move = nioFilemove(filePath, filePathtemp+"music/"+fileName);
		}else if(moveFolder.equals("video")) {
			is_Move = nioFilemove(filePath, filePathtemp+"video/"+fileName);
		}else if(moveFolder.equals("document")) {
			is_Move = nioFilemove(filePath, filePathtemp+"document/"+fileName);
		}else {
			is_Move = nioFilemove(filePath, filePathtemp+"etc/"+fileName);
		}
		return is_Move;
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
