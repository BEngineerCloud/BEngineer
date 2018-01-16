<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>

<%
 try{
   if(session.getAttribute("Id")==null){%>
    	response.sendRedirect("login.jsp"); <!-- 수정할것 -->
        <%}else{%>
    	<h2>로그인</h2>    
    	<input name="logout" type="button" value="로그아웃" onClick="location.href='logout.do'"/>	
        <a href="/BEngineer/inquiry/allInquiry.do">문의내역</a>
        <a href="/BEngineer/board/list.do?id=<%=session.getAttribute("Id")%>">공지사항</a>
        <a href="/BEngineer/manager/impose.do">유저제재</a>
        <a href="/BEngineer/manager/">유료전환</a>

         <%}
 }catch(NullPointerException e){}
 %>

 
    