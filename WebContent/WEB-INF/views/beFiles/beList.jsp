<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.min.js"></script>
<script type="text/javascript">
	$(function(){
		$("#files > div").click(function(){
			$("#files > div").css("background-color","#ff6666");
			$(this).css("background-color","#6666dd");
			$("font#filename").text($(this).text());
		});
	});
	$(function(){
		$("#myfile").click(function(){
			window.location = "/BEngineer/beFiles/beMyList.do";
		});
	});
	$(function(){
		$("#uploadform").submit(function(){
			var file = document.getElementById("uploadfile");
			if(file.type == "hidden"){
				file.type = "file";
				return false;
			}
		});
	});
</script>
<body topmargin="0" bottommargin="0" leftmargin="0" rightmargin="0">
<div id="logo" style="height:10%; width:15%; background-color:#ff9999; float:left;">
	<img src="/BEngineer/image\beCloudLogo.png" style="width: 100%; height:100%"/>
</div>
<div id="search" style="height:10%; width:70%; background-color:#99ff99; float:left;">
	search
</div>
<div id="logout" style="height:10%; width:15%; background-color:#9999ff; float:left;">
	logout
</div>
<div id="button1" style="height:5%; width:100%; background-color:#ffff99; float:left;">
	<div style="height:5%; width:relative; margin:0; float:left;">
		<form action="/BEngineer/beFiles/fileupload.do" id="uploadform" method="post" enctype="multiport/form-data">
			<input type="hidden" name="fileaddress" value="${fileaddress }" />
			<input type="hidden" name="save" id="uploadfile" />
			<input type="submit" value="업로드" />
		</form>
	</div>
	button1
</div>
<div id="address" style="height:5%; width:100%; background-color:#99ffff; float:left;">
	${sessionScope.nickname } / <font id="filename"></font>
</div>
<div id="button2" style="height:80%; width:10%; background-color:#ff99ff; float:left;">
	<input type="button" id="myfile" value="내 파일보기"/>
	button2
</div>
<div id="files" style="height:80%; width:90%; background-color:#999999; float:left; overflow:scroll;">
	<c:forEach var="file" items="${list }">
		<div class="file" name="${file.fileaddress }" style="height:10%; width:10%; margin:1%; background-color:#ff6666; float:left;">
			${file.filename }
		</div>
	</c:forEach>
</div>
<div id="etc" style="height:10%; width:100%; background-color:#000000; float:left;">
	etc
</div>