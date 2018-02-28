<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<body bgcolor="#00D8F0">
<div id="in1" style="float:right; height:100%; width:20%;"><input name="logout" type="button" value="로그아웃" onClick="location.href='logout.do'" style="width:100px; height:25px; color:#000000; background-color:#B7F0B1;"/>
	</br><a href="/BEngineer/manager/mMain.do">메인</a>
	</br><a href="/BEngineer/inquiry/allInquiry.do">문의내역</a>
	</br><a href="/BEngineer/board/list.do?id=${sessionScope.Id }">공지사항</a>
	</br><a href="/BEngineer/manager/charge.do">서버구동현황</a>
</div>
<div id="in2">
	<form action="/BEngineer/manager/imposePro.do" method="post">
	<tr><td width="100">제제할 회원 : </td><td width="100"><input type="text" name="email"/></td></tr>
	</br><tr><td width="100">제제사유</br></td><td width="100"><textarea name="cause" value="x" rows="13" cols="35"></textarea></td></tr>
	</br><tr><td width="100">기간 : </td><td width="20"><input type="text" name="term" style="width:30px;"/>일</td></tr>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<input type="submit" value="제제" style="width:50px; height:30px; color:#000000; background-color:#1DDB16; align:right;"/></form>
	<tr><td>제제당한 회원 </td></tr>
	<c:forEach var="impose" items="${list}">
	<form action="/BEngineer/manager/imposeCancle.do?email=${impose.email}" method="post">
	<table>
	<tr><td width="50">${impose.email}</td><td><input type="submit" value="제제취소"  style="width:80px; height:25px; color:#000000; background-color:#5AAEFF;"/></td></tr>
	</table></form>
	</c:forEach>
</div>  
</body>