<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %><!-- 날짜표현하기위해.. -->
<div id="in1" style="float:right; height:100%; width:20%;"><input name="logout" type="button" value="로그아웃" onClick="location.href='logout.do'" style="width:100px; height:25px; color:#000000; background-color:#B7F0B1;"/>
</br><a href="/BEngineer/manager/mMain.do">메인</a>
</br><a href="/BEngineer/inquiry/allInquiry.do">문의내역</a>
</br><a href="/BEngineer/manager/imposeForm.do">유저제재</a>
</br><a href="/BEngineer/manager/charge.do">서버구동현황</a>
</div>
<div>
<body bgcolor="#00D8F0">
<table border="1" bgcolor="#FFFFFF">
	<input name="write" type="button" value="공지사항작성" onClick="location.href='writeForm.do?id=${Id}'"  style="width:100px; height:30px; color:#000000; background-color:#1DDB16; font-family:맑은고딕;"/>
	<tr><td align="center" width="50" bgcolor="#5AAEFF">글번호</td>
	<td align="center" width="150" bgcolor="#5AAEFF">제목</td>
	<td align="center" width="80" bgcolor="#5AAEFF">작성자</td>
	<td align="center" bgcolor="#5AAEFF" colspan="2">작성일</td></tr>
	
	<c:forEach var="board" items="${list}">	
	<tr><td width="30" align="center">${board.num-1}</td> 
	<td width="100"><a href="/BEngineer/board/updateForm.do?num=${board.num}">${board.title}</a></td> 
	<!-- <td>${board.content}</td>  -->
	<td width="50">${board.id}</td> 
	<td><fmt:formatDate value="${board.reg_date}" pattern="MM-dd HH:mm"/></td><br/>
	<td><input name="delete" type="button" value="삭제" onClick="location.href='delete.do?num=${board.num}'"/></td></tr>
	</c:forEach>   
</table>
</body>