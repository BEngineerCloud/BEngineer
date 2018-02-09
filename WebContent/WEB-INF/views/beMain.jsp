<%@ page contentType="text/html; charset=UTF-8"%> 
   	<link rel='stylesheet' href='/BEngineer/resources/css/nv.d3.css'>
   	<link rel='stylesheet' href='/BEngineer/resources/css/rNVD3.css'>
	<script src='/BEngineer/resources/js/jquery-1.8.2.min.js' type='text/javascript'></script>
	<script src='/BEngineer/resources/js/d3.v3.min.js' type='text/javascript'></script>
    <script src='/BEngineer/resources/js/nv.d3.min-new.js' type='text/javascript'></script>
    <script src='/BEngineer/resources/js/fisheye.js' type='text/javascript'></script>
    <script src="/BEngineer/resources/wordcloud2/htmlwidgets-0.8/htmlwidgets.js?ver=2"></script>
	<link href="/BEngineer/resources/wordcloud2/wordcloud2-0.0.1/wordcloud.css" rel="stylesheet" />
	<script src="/BEngineer/resources/wordcloud2/wordcloud2-0.0.1/wordcloud2-all.js"></script>
	<script src="/BEngineer/resources/wordcloud2/wordcloud2-0.0.1/hover.js"></script>
	<script src="/BEngineer/resources/wordcloud2/wordcloud2-binding-0.2.0/wordcloud2.js?ver=2"></script>
<script src='/BEngineer/resources/js/menu.js' type='text/javascript'></script>
<body topmargin="0" bottommargin="0" leftmargin="0" rightmargin="0">
<div id="logo" style="height:10%; width:15%; float:left;">
	<img id="beLogo" src="/BEngineer/image/beCloudLogo.png" style="width: 100%; height:100%; cursor:pointer"/>
</div>
<div id="search" style="height:10%; width:70%; background-color:#99ff99; float:left;">
	search
</div>
<div align="center" id="logout" style="height:10%; width:15%;float:left;">
	<div style="height:30%; width:100%;float:left;  margin-top: 3%"> 
		<font size="4">'${sessionScope.nickname}' 님 환영합니다.</font>
	</div>
	<div align="right" id="addinfodiv" style="height:70%; width:50%;float:left; margin-top: 3%">
		<input type="button" id="addinfo" style="height:61%; border-color: black; background-color:#FFFFFF; font-size:11pt"value="회원정보 관리"/>
	</div>
	<div align="left"id="logoutdiv" style="height:70%; width:50%;float:left; margin-top: 3%">
		&emsp;
		<input type="button" id="logout" style="height:61%; border-color: black; background-color:#FFFFFF; font-size:11pt"value="로그아웃"/>
	</div>
</div>
<div id="button1" style="height:5%; width:100%; background-color:#ffff99; float:left;">
	button1
</div>
<div id="address" style="height:5%; width:100%; background-color:#99ffff; float:left;">
	address
</div>
<div id="button2" style="height:80%; width:10%; background-color:#ff99ff; float:left;">
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
	button2
	<c:if test="${space != null && space !=''}">
		<img src="data:image/png;base64,${space}" style="width:100%;" />
	</c:if>
</div>
<div id="files" style="height:80%; width:90%; background-color:#999999; float:left; overflow:scroll;">
	${ content }
</div>
<div id="etc" style="height:10%; width:100%; background-color:#5f7f89; float:left;">
	etc
		<a href="/BEngineer/inquiry/inList.do?id=${sessionScope.id}">문의내역</a>
        <a href="/BEngineer/beMember/beboard.do?id=${sessionScope.id}">공지사항</a>
        <a href="/BEngineer/beMember/upgrade.do?id=${sessionScope.id}">유료전환</a>
        <a href="">사이트맵</a>
</div>
</body>