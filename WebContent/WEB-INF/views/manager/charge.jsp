<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<style type="text/css">
#back {position:relative;}
#in2 {position:absolute;
top:40px;
left:220px;
}
#in3 {position:absolute;
top:5px;
left:370px;
}  
</style>
<body bgcolor="#00D8F0">
<div id="back">
<div id="in1" style="float:right; height:100%; width:20%;"><input name="logout" type="button" value="로그아웃" onClick="location.href='http://localhost:8080/BEngineer/manager/logout.do'" style="width:100px; height:25px; color:#000000; background-color:#B7F0B1;"/>
</br><a href="/BEngineer/manager/mMain.do">메인</a>
</br><a href="/BEngineer/inquiry/allInquiry.do">문의내역</a>
</br><a href="/BEngineer/board/list.do?id=${sessionScope.Id }">공지사항</a>
</br><a href="/BEngineer/manager/imposeForm.do">유저제재</a>
</div>
<div id="in2" style="float:center; height:100%; width:100%;">
<table>
<tr><td width="100" bgcolor="#ABF200">사용자</td><td align="right" bgcolor="#0054FF">용량</td></tr>
<c:forEach var="list" items="${list}">
<tr><td width="100" bgcolor="#ABF200">${list.owner }</td>
<c:choose>
    <c:when test="${list.filesize  == 0}"><td align="right" bgcolor="#FFE400">0</td></c:when>
    <c:when test="${list.filesize  > 10000}">
    <td align="right" bgcolor="#FF0000"><fmt:formatNumber value="${list.filesize/1024}" pattern="##.#"/> MByte</td> <!-- 소숫점한자리까지 --></c:when>
    <c:otherwise><td align="right" bgcolor="#FF5E00"> ${list.filesize} KByte</td></c:otherwise>
</c:choose>
</td></tr>
</c:forEach>   
<tr><td width="100" bgcolor="#ABF200">전체</td><td align="right" bgcolor="#0054FF">${all }MByte</td></tr>
</table>
</div>
<div id="in3">
<img src="data:image/png;base64,${rr}" style="height:43%; width:80%;"/>
</div>
</div>
</body>