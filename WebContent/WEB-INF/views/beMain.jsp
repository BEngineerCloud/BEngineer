<%@ page contentType="text/html; charset=UTF-8"%>
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.min.js"></script>
<script type="text/javascript">
	$(function(){
		$("#myfile").click(function(){
			window.location = "/BEngineer/beFiles/beMyList.do";
		});
	});
</script>
<body topmargin="0" bottommargin="0" leftmargin="0" rightmargin="0">
<div id="logo" style="height:10%; width:15%; float:left;">
	<img src="image\beCloudLogo.png" style="width: 100%; height:100%"/>
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
	address
</div>
<div id="button2" style="height:80%; width:10%; background-color:#ff99ff; float:left;">
	<input type="button" id="myfile" value="내 파일보기"/>
	button2
</div>
<div id="files" style="height:80%; width:90%; background-color:#999999; float:left; overflow:scroll;">
	files
</div>
<div id="etc" style="height:10%; width:100%; background-color:#000000; float:left;">
	etc
</div>
</body>