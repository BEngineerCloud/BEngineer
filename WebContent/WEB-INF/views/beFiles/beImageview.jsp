<%@ page contentType="images/jpg" %>
 
<%@ page import="java.util.*,java.io.*" %>
<%
	String owner = (String)request.getAttribute("owner");
	String imageName = (String)request.getAttribute("imageName");
	BufferedInputStream bis = null;
	BufferedOutputStream bos = null;
 
	String imagePath = "";
 
	//해당 아이디에 맞는 이미지를 찾는 조건문 간단하게 if로 구현 하였지만 기타 방식으로 데이터를 가져오면 됩니다.
	//+${owner}+"/image/"+${imageName};
	imagePath = "D:/PM/BEngineer/"+owner+"/image/"+imageName;

	File file = new File(imagePath);
	int size = (int)file.length();
 
	out.clear();
	bos = new BufferedOutputStream(response.getOutputStream());
	byte b[] = new byte[2048];
	int read = 0;
	if( size>0 && file.isFile() ) {
    	bis = new BufferedInputStream(new FileInputStream(file));
    	while((read=bis.read(b))!=-1 ) {
        	bos.write(b,0,read);
   	 	}
	} 
	bis.close(); 
	bos.close();
%>