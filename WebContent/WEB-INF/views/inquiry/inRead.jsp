<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<link rel='stylesheet' href='/BEngineer/resources/css/style.css'>
<script src='/BEngineer/resources/js/jquery-1.8.2.min.js' type='text/javascript'></script>
<script src='/BEngineer/resources/pagectrl/menu.js' type='text/javascript'></script>
<script src='/BEngineer/resources/pagectrl/search.js' type='text/javascript'></script>
<style type="text/css">
#back {position:relative;}
#in {position:absolute;
left:10px;
top:5px;
}
</style>
<script type="text/javascript">
	//비교대상 목록?
	var font_test = Array(
		<c:forEach var="dto" items="${font}">
			'${dto.filename}',
		</c:forEach>
		''
	);
</script>
<body topmargin="0" bottommargin="0" leftmargin="0" rightmargin="0">
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
	<div align="left"style="height:100%; width:100%;float:left;  text-align:center; margin-top:13%"> 
		<input type="button" id="addinfo" style="height:45%; border-color: black; background-color:#FFFFFF; font-size:100%; "value="회원정보 관리"/>
		&nbsp;
		<input type="button" id="logout" style="height:45%; border-color: black; background-color:#FFFFFF; font-size:100%;"value="로그아웃"/>
	</div>
</div>
<div id="button1">
</div>
<div id="address">
</div>
<div id="button2">
	<input type="button" id="myfile" value="내 파일"/>
	<input type="button" id="mysharedfile" value="공유 파일"/>
	<input type="button" id="mytrashcan" value="휴지통"/>
	<input type="button" id="hotlist" value="즐겨찾기"/>
	<form action="/BEngineer/beFiles/beRecentFiles.do" method="post">
		<select name="weeks">
			<option value="1">1주 이내</option>
			<option value="2">2주 이내</option>
			<option value="3">3주 이내</option>
			<option value="4">4주 이내</option>
		</select>
		<input type="submit" value="최근 파일"/>
	</form>
</div>
<div id="files">
<input name="inList" type="button" value="목록" onClick="location.href='inList.do?id=${sessionScope.id}'" style="margin:1%;"/><br />
	<table border="1" style="float:left; margin:1%;">
	  <tr><td width="90">제목</td><td width="330">${re.title}</td></tr>
	 
	  <tr><td width="90" style="text-align:top;">내 용</td>
	    <td width="330">${re.content}</td></tr>
	    
	  <tr><td width="90">파일</td>
	    <td width="330"><img src="/BEngineer/inquiryImg/${re.filename }" width="50" height="50"/></td></tr>
	  
	  <tr><td width="90">답변</td>
	    <td width="330">${re.reply}</td></tr>
	</table>
</div>
<div id="etc">
		<a href="/BEngineer/inquiry/inList.do?id=${sessionScope.id}">문의내역</a>
        <a href="/BEngineer/beMember/beboard.do?id=${sessionScope.id}">공지사항</a>
        <a href="/BEngineer/beMember/upgrade.do?id=${sessionScope.id}">유료전환</a>
</div>
</body>
