<%@ page contentType="text/html; charset=UTF-8"%> 
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.min.js"></script>
<script type="text/javascript">
$(function(){
	$("#myfile").click(function(){
		window.location = "/BEngineer/beFiles/beMyList.do?folder=0";
	});
});
$(function(){
	$("#mysharedfile").click(function(){
		window.location = "/BEngineer/beFiles/beSharedList.do?folder=0";
	});
});
$(function(){
	$("#mytrashcan").click(function(){
		window.location = "/BEngineer/beFiles/beTrashcan.do?folder=0";
	});
});
$(function(){
	$("#beLogo").click(function(){
		window.location = "/BEngineer/beMain.do";
	});
});

$(function(){
	$("#addinfodiv > #addinfo").click(function(){
		window.location = "/BEngineer/beMember/beAddinfo.do?";
	});
});

$(function(){
	$("#logoutdiv > #logout").click(function(){
		window.location = "/BEngineer/beLogout.do";
	});
});
$(function(){
	$("#hotlist").click(function(){
		window.location = "/BEngineer/beFiles/hotlist.do?num=0";
	});
});

</script>
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
</div>
<div id="files" style="height:80%; width:90%; background-color:#999999; float:left; overflow:scroll;">
	files
</div>
<div id="etc" style="height:10%; width:100%; background-color:#5f7f89; float:left;">
	etc
		<a href="/BEngineer/inquiry/inList.do?id=<%=session.getAttribute("Id")%>">문의내역</a>
        <a href="/BEngineer/beMember/beboard.do?id=<%=session.getAttribute("Id")%>">공지사항</a>
        <a href="">사이트맵</a>
</div>
</body>