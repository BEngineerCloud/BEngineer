<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="logo" style="height:10%; width:15%; background-color:#ff9999; float:left;">
	logo
</div>
<div id="search" style="height:10%; width:70%; background-color:#99ff99; float:left;">
	search
</div>
<div id="logout" style="height:10%; width:15%; background-color:#9999ff; float:left;">
	logout
</div>
<div id="button1" style="height:5%; width:100%; background-color:#ffff99; float:left;">
	button1
</div>
<div id="address" style="height:5%; width:100%; background-color:#99ffff; float:left;">
	${sessionScope.nickname } / 
</div>
<div id="button2" style="height:80%; width:10%; background-color:#ff99ff; float:left;">
	<input type="button" value="내 파일보기" onClick="javascript:window.location='/BEngineer/beFiles/beList.do'"/>
	button2
</div>
<div id="files" style="height:80%; width:90%; background-color:#999999; float:left; overflow:scroll;">
	<c:forEach var="file" items="${list }">
		<div class="file" style="height:10%; width:10%; margin:1%; border:1px; background-color:#ff6666; float:left;">${file.filename }</div>
	</c:forEach>
</div>
<div id="etc" style="height:10%; width:100%; background-color:#000000; float:left;">
	etc
</div>