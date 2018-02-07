<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<h2>유저제제</h2>
<form action="/BEngineer/manager/imposePro.do" method="post">
	제제할 회원<input type="text" name="email"/><br/>
	제제사유<input type="text" name="cause" value="x"/><br/>
	기간 <input type="text" name="term"/>일<br/>
		 <input type="submit" value="확인"/>
</form>
제제당한 회원 :</br>

<c:forEach var="impose" items="${list}">
<form action="/BEngineer/manager/imposeCancle.do?email=${impose.email}" method="post">
<tr>
<td width="50">${impose.email}</td> 
<td>
<input type="submit" value="제제취소"/></br>
</td>
</tr>
</form>
</c:forEach>  

	
	
	