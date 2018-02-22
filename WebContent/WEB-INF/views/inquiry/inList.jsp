<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<style type="text/css">
#in {position:absolute;
bottom:50px;
} 
</style>
<div id="logo" style="height:10%; width:15%; float:left;"> 
	<img src="/BEngineer/image/beCloudLogo.png" id="beLogo" style="width: 100%; height:100%; cursor:pointer"/>
</div>
<!-- 검색창 -->
<div align="center" style="height:10%; width:61%; float:left;">
	<div style="margin-top:2%">
	<input type="text" id="searchword"style="height:38%; width:20%; border-color: black; background-color:#FFFFFF;"/>
	<input type="button" id="search" value="검  색" style="height:41%; border-color: black; background-color:#FFFFFF;"/>
	</div>
</div>
<div style="height:10%; width:12%; float:left;">
	<div style="height:100%; width:100%;  text-align:center; margin-top:16%"> 
		<font size="4">'${sessionScope.nickname}' 님 환영합니다.</font>
	</div>
</div>
<div style="height:10%; width:12%; float:left;">
	<div align="left" style="height:100%; width:100%;float:left;  text-align:center; margin-top:13%"> 
		<input type="button" id="addinfo" style="height:45%; border-color: black; background-color:#FFFFFF; font-size:100%; "value="회원정보 관리"/>
		&nbsp;
		<input type="button" id="logout" style="height:45%; border-color: black; background-color:#FFFFFF; font-size:100%;"value="로그아웃"/>
	</div>
</div>
<div id="button2" style="height:75%; width:10%; background-color:#ff99ff; float:left;">
	<input type="button" id="myfile" value="내 파일"/>
	<input type="button" id="mysharedfile" value="공유 파일"/>
	<input type="button" id="mytrashcan" value="휴지통"/>
	<input type="button" id="hotlist" value="즐겨찾기"/>
</div>
<!-- @@ -->
<div id="in" style="height:75%; width:90%;">
<table align="center" border="1">
<tr bgcolor="#5AAEFF">
<td align="center">제목</td>
<td align="center">작성자</td>
<td align="center" width="100">작성일</td>
<td align="center">답변</td>
</tr>
<c:forEach var="inList" items="${inList}">
<tr> 
<td width="200" align="center"><a href="/BEngineer/inquiry/inRead.do?num=${inList.num}">${inList.title}</a></td> 
<td width="50" align="center">${inList.id}</td> 
<td width="120" align="center"><fmt:formatDate value="${inList.reg_date}" pattern="MM-dd HH:mm"/></td><br/>
<c:choose>
    <c:when test="${empty inList.reply}">
        <td bgcolor="#FF0000">미답변</td>
    </c:when>
    <c:otherwise>
        <td  bgcolor="#00D8FF">답변완료</td>
    </c:otherwise>
</c:choose>
</tr>
</c:forEach>
</table>
<table align="center"><td><input name="inList" type="button" value="문의하기" onClick="location.href='inForm.do?id=${Id}'"/></table>
</div>

<div id="etc" style="height:10%; width:100%; background-color:#5f7f89; float:left;">
	etc
		<a href="/BEngineer/inquiry/inList.do?id=${sessionScope.id}">문의내역</a>
        <a href="/BEngineer/beMember/beboard.do?id=${sessionScope.id}">공지사항</a>
        <a href="/BEngineer/beMember/upgrade.do?id=${sessionScope.id}">유료전환</a>
        <a href="">사이트맵</a>
</div>
