<%@ page contentType="images/jpg" %>
<%@ page import="java.util.*,java.io.*" %>
<%
	//*** 로컬 이미지 썸네일로 보여주는 페이지 ***//    
	
	BufferedInputStream bis = null;
	BufferedOutputStream bos = null;
 
	String owner = (String)request.getAttribute("owner"); //회원
	String imageName = (String)request.getAttribute("imageName"); //이미지이름
	
	//이미지 경로
	String imagePath = "";
	imagePath = "D:/PM/BEngineer/"+owner+"/image/"+imageName;

	File file = new File(imagePath); //이미지 경로에 해당하는 파일 객체 생성
	int size = (int)file.length();
 
	out.clear(); //버퍼의 내용을 지움
	bos = new BufferedOutputStream(response.getOutputStream()); 
	
	byte b[] = new byte[2048];
	int read = 0;
	
	//파일이 존재할 때 파일을 읽어들이고 출력
	if( size>0 && file.isFile() ) {
    	bis = new BufferedInputStream(new FileInputStream(file));
    	while((read=bis.read(b))!=-1 ) { //byte b크기만큼 읽어들였을 때 파일이 존재하면
        	bos.write(b,0,read); //byte b크기만큼 read데이터를 0위치에서부터 출력
   	 	}
	} 
	
	//인풋, 아웃풋 스트림 닫기(*열려 있으면 다른 사이트에서 해당 회원 이미지를 이용할때 문제가 생긴다.)
	bis.close(); 
	bos.close();
%>