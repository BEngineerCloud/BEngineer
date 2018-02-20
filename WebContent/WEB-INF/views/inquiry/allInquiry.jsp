<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %><!-- 날짜표현하기위해.. -->
<style type="text/css">
#back {position:relative;}
#in2 {position:absolute;
bottom:220px;
} 
</style>
<body bgcolor="#00D8F0">
<div id="back">
	<div id="in1" style="float:right; height:100%; width:20%;"><input name="logout" type="button" value="로그아웃" onClick="location.href='http://localhost:8080/BEngineer/manager/logout.do'" style="width:100px; height:25px; color:#000000; background-color:#B7F0B1;"/>
	</br><a href="/BEngineer/manager/mMain.do">메인</a>
    </br><a href="/BEngineer/board/list.do?id=${sessionScope.Id }">공지사항</a>
    </br><a href="/BEngineer/manager/imposeForm.do">유저제재</a>
    </br><a href="/BEngineer/manager/charge.do">서버구동현황</a>
	</div>

<div id="in2" style="float:top; height:80%; width:80%;">
<table border="1">
	<tr>
	<td align="center">글번호</td>
	<td align="center">제목</td>
	<td align="center">작성자</td>
	<td align="center">작성일</td>
	</tr>
	<c:forEach var="all" items="${allList}">
	<c:choose>
	   <c:when test="${empty all.reply}">
	<tr>
	<td width="50" align="center">${all.num}</td> 
	<td width="200"><a href="/BEngineer/inquiry/replyForm.do?num=${all.num}">${all.title}</a></td> 
	<!-- <td>${board.content}</td>  -->
	<td width="100">${all.id}</td> 
	<td>
	<fmt:formatDate value="${all.reg_date}" pattern="MM-dd HH:mm"/>
	</td><br/>
	</c:when>
	<c:otherwise></c:otherwise>
	</c:choose>
	</tr>
	</c:forEach>   
</table>
</div>
</div>
</body>

