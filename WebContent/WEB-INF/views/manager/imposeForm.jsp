<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<h2>유저제제</h2>
<table>
<form action="/BEngineer/manager/imposePro.do" method="post">
<tr><td width="100">제제할 회원</td><td width="100"><input type="text" name="email"/></td></tr>
<tr><td width="100">제제사유</td><td width="100"><input type="text" name="cause" value="x"/></td></tr>
<tr><td width="100">기간 </td><td width="20"><input type="text" name="term"/>일</td></tr>
<tr><td><input type="submit" value="확인"/></td></tr>
</form>

<tr><td>제제당한 회원 :</td></tr>
<c:forEach var="impose" items="${list}">
<form action="/BEngineer/manager/imposeCancle.do?email=${impose.email}" method="post">
<tr><td width="50">${impose.email}</td><td><input type="submit" value="제제취소"/></td></tr>
</form>
</c:forEach>  
</table>
	
	
	