<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>

<%
 try{
   if(session.getAttribute("Id")==null){%>
    	response.sendRedirect("login.jsp"); <!-- �����Ұ� -->
        <%}else{%>
    	<h2>�α���</h2>    
    	<input name="logout" type="button" value="�α׾ƿ�" onClick="location.href='logout.do'"/>	
        <a href="">���ǳ���</a>
        <a href="/BEngineer/board/list.do?id=<%=session.getAttribute("Id")%>">��������</a>
        <a href="/BEngineer/manager/impose.do">��������</a>
        <a href="">������ȯ</a>

         <%}
 }catch(NullPointerException e){}
 %>

 
    