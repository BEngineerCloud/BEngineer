<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.min.js"></script>
<script type="text/javascript">
	$(function(){
		$("#files > div").click(function(){
			$("#files > div").css("background-color","#ff6666");
			$(this).css("background-color","#6666dd");
			var filename = $(this).text();
			var orgname = document.getElementById(filename);
			$("font#filename").text(orgname.value);
		});
	});
	$(function(){
		$("#files > div").dblclick(function(){
			var filename = $(this).text();
			var type = document.getElementById(filename + "type");
			if(type.value == "dir"){
				window.location = "/BEngineer/beFiles/beMyList.do?folder=" + $(this).attr("name");
			}
		});
	});
	$(function(){
		$("#myfile").click(function(){
			window.location = "/BEngineer/beFiles/beMyList.do";
		});
	});
	$(function(){
		$("#uploadform").submit(function(){
			var form = document.getElementById("uploadform");
			if(form.save.type == "hidden"){
				form.save.type = "file";
				form.filename.type = "text";
				return false;
			}
			if(!form.save.value){
				alert('업로드할 파일을 선택해주세요');
				return false;
			}
		});
	});
	$(function(){
		$("input[name='filename']").focus(function(){
			if($(this).val() == '파일이름'){
				$(this).val("")
			}
		});
	});
	$(function(){
		$("#beLogo").click(function(){
			window.location = "/BEngineer/beMain.do";
		});
	});
</script>
<body topmargin="0" bottommargin="0" leftmargin="0" rightmargin="0">
<div id="logo" style="height:10%; width:15%; background-color:#ff9999; float:left;">
	<img src="/BEngineer/image/beCloudLogo.png" id="beLogo" style="width: 100%; height:100%; cursor:pointer"/>
</div>
<div id="search" style="height:10%; width:70%; background-color:#99ff99; float:left;">
	search
</div>
<div id="logout" style="height:10%; width:15%; background-color:#9999ff; float:left;">
	logout
</div>
<div id="button1" style="height:5%; width:100%; background-color:#ffff99; float:left;">
	<c:if test="${write }">
		<div style="height:5%; width:relative; margin:0; float:left;">
			<form action="/BEngineer/beFiles/fileupload.do" id="uploadform" method="post" enctype="multipart/form-data">
				<input type="hidden" name="fileaddress" value="${fileaddress }" />
				<input type="hidden" name="save" />
				<input type="hidden" name="filename" value="파일이름"/>
				<input type="submit" value="업로드" />
			</form>
		</div>
	</c:if>
	button1
</div>
<div id="address" style="height:5%; width:100%; background-color:#99ffff; float:left;">
	<c:forEach var="num" begin="0" end="4" step="1">
		<a href="/BEngineer/beFiles/beMyList.do?folder=${orgaddress[num] }">${folderaddress[num] }</a> / 
	</c:forEach>
	<font id="filename"></font>
</div>
<div id="button2" style="height:80%; width:10%; background-color:#ff99ff; float:left;">
	<input type="button" id="myfile" value="내 파일보기"/>
	button2
</div>
<div id="files" style="height:80%; width:90%; background-color:#999999; float:left; overflow:scroll;">
	<c:forEach var="file" items="${list }">
		<div class="file" name="${file.fileaddress }" style="height:10%; width:10%; margin:1%; background-color:#ff6666; float:left;">${file.filename }<input type="text" id="${file.filename }" value="${file.fileaddress.substring(file.fileaddress.lastIndexOf("/") + 1) }" style="border:0; background:transparent; cursor:default; width:100%;" disabled/></div>
		<input type="hidden" id="${file.filename }type" value="${file.filetype }"/>
	</c:forEach>
</div>
<div id="etc" style="height:10%; width:100%; background-color:#5f7f89; float:left;">
	etc
</div>