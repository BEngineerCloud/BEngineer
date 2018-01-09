<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript" src="http://code.jquery.com/jquery-3.2.0.min.js" ></script>
<script type="text/javascript">
	$(function(){
		$("#files > div").click(function(){ // 파일 클릭시
			hinder(); // 업로드 입력, 폴더생성 입력 취소
			$("#files > div").css("background-color","#ff6666"); // 모든 파일 선택 취소
			$(this).css("background-color","#6666dd"); // 클릭파일 색 바꾸기
			var filename = $(this).text();
			var orgname = document.getElementById(filename); // 원 파일명 저장되어있는 인풋의 값 가져오기
			$("font#filename").text(orgname.value); // 주소부분에 표시
			var filename = $(this).text();
			var type = document.getElementById(filename + "type"); // 파일타입 저장되어있는 인풋의 값 가져오기
			var form = document.getElementById("changenameform"); 
			form.ref.value = $(this).attr("name");
			if(type.value == "dir"){ // 폴더일 때 해당 폴더로 이동
				form.action = "/BEngineer/beFiles/changeFolderName.do";
				form.name.value = "폴더 이름";
				form.submitchangename.value = "폴더명 변경";
			}
			if(type.value != "dir"){ // 폴더일 때 해당 폴더로 이동
				form.action = "/BEngineer/beFiles/changeFileName.do";
				form.name.value = "파일 이름";
				form.submitchangename.value = "파일명 변경";
			}
			form.submitchangename.type = "submit";
			form = document.getElementById("shareform");
			form.ref.value = $(this).attr("name");
			form.submitshare.type = "submit";
		});
	});
	$(function(){
		$("#files > div").dblclick(function(){ // 파일 더블클릭시
			var filename = $(this).text();
			var type = document.getElementById(filename + "type"); // 파일타입 저장되어있는 인풋의 값 가져오기
			if(type.value == "dir"){ // 폴더일 때 해당 폴더로 이동
				window.location = "/BEngineer/beFiles/beMyList.do?folder=" + $(this).attr("name");
			}
			if(type.value != "dir"){ // 파일일 때 해당 파일 다운로드
				window.location = "/BEngineer/beFiles/beDownload.do?file_ref=" + $(this).attr("name");
			}
		});
	});
	$(function(){
		$("#myfile").click(function(){ // 내 파일보기 버튼 클릭 시 페이지 이동
			window.location = "/BEngineer/beFiles/beMyList.do?folder=0";
		});
	});
	$(function(){
		$("#uploadform").submit(function(){ // 업로드 버튼 클릭시
			var form = document.getElementById("uploadform"); // 폼 받아오기
			if(form.save.type == "hidden"){ // 폼이 숨겨진 상태일 때 폼 보이고 이동 취소
				hinder(); // 다른 폼 닫기
				$("#files > div").css("background-color","#ff6666"); // 모든 파일 선택 취소
				form.save.type = "file";
				form.filename.type = "text";
				return false;
			}
			if(!form.save.value){ // 업로드할 파일 미선택시
				alert('업로드할 파일을 선택해주세요');
				return false;
			}
		});
	});
	$(function(){
		$("#folderform").submit(function(){ // 폴더생성 버튼 클릭시
			var form = document.getElementById("folderform"); // 폼 받아오기
			if(form.foldername.type == "hidden"){ // 폼이 숨겨진 상태일 때 폼 보이고 이동 취소
				hinder(); // 다른 폼 닫기
				$("#files > div").css("background-color","#ff6666"); // 모든 파일 선택 취소
				form.foldername.type = "text";
				return false;
			}
			if(!form.foldername.value.trim() || form.foldername.value == "폴더 이름"){ // 폴더 명 미입력시
				alert('생성할 폴더의 이름을 입력해주세요');
				return false;
			}
			form.foldername.value = form.foldername.value.trim(); // 폴더명 앞뒤의 공백문자 삭제
		});
	});
	$(function(){
		$("#changenameform").submit(function(){ // 폴더명/파일명 변경 버튼 클릭시
			var form = document.getElementById("changenameform"); // 폼 받아오기
			if(form.name.type == "hidden"){ // 폼이 숨겨진 상태일 때 폼 보이고 이동 취소
				hinder(); // 다른 폼 닫기
				form.name.type = "text";
				return false;
			}
			if(!form.name.value.trim() || form.name.value == "폴더 이름" || form.name.value == "파일 이름"){ // 폴더 명 미입력시
				alert('생성할 폴더의 이름을 입력해주세요');
				return false;
			}
			form.name.value = form.name.value.trim(); // 폴더명 앞뒤의 공백문자 삭제
		});
	});
	$(function(){
		$("#shareform").submit(function(){ // 공유 버튼 클릭시
			var date = new Date();
			var today = date.getFullYear() + "-";
			var month = (date.getMonth() + 1);
			if(month < 10){
				today = today + "0" + month + "-";
			}else{
				today = today + month + "-";
			}
			var day = date.getDate();
			if(day < 10){
				today = today + "0" + day;
			}else{
				today = today + day;
			}
			var form = document.getElementById("shareform"); // 폼 받아오기
			form.term.min = today;
			if(form.term.type == "hidden"){ // 폼이 숨겨진 상태일 때 폼 보이고 이동 취소
				hinder(); // 다른 폼 닫기
				document.getElementById("text").type = "text";
				form.term.type = "date";
				form.randw.hidden = false;
				form.submitshare.type = "submit";
				return false;
			}
			if(!form.term.value){ // 공유기한 미입력시
				alert('공유기한을 입력해주세요');
				return false;
			}
		});
	});
	$(function(){
		$("input[name='filename']").focus(function(){ // 파일이름 창 클릭시 초기화
			if($(this).val() == '파일 이름'){
				$(this).val("")
			}
		});
	});
	$(function(){
		$("input[name='foldername']").focus(function(){ // 폴더이름 창 클릭시 초기화
			if($(this).val() == '폴더 이름'){
				$(this).val("")
			}
		});
	});
	$(function(){
		$("input[name='name']").focus(function(){ // 폴더이름 창 클릭시 초기화
			if($(this).val() == '폴더 이름'){
				$(this).val("")
			}
			if($(this).val() == '파일 이름'){
				$(this).val("")
			}
		});
	});
	$(function(){
		$("#beLogo").click(function(){ // 로고 클릭시 메인으로 이동
			window.location = "/BEngineer/beMain.do";
		});
	});
	function hinder(){ // 모든 폼 닫기 함수
		var form = document.getElementById("uploadform"); // 파일업로드 폼
		form.save.type = "hidden";
		form.filename.type = "hidden";
		form = document.getElementById("folderform"); // 폴더생성 폼
		form.foldername.type = "hidden";
		form = document.getElementById("changenameform"); // 폴더생성 폼
		form.name.type = "hidden";
		form.submitchangename.type = "hidden";
		form = document.getElementById("shareform");
		form.term.type = "hidden";
		form.randw.hidden = true;
		form.submitshare.type = "hidden";
		document.getElementById("text").type = "hidden";
	}
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
	<c:if test="${write }"><!-- 쓰기권한이 있을 때 -->
		<!-- 파일업로드 폼 -->
		<div style="height:5%; width:relative; margin:0; float:left;">
			<form action="/BEngineer/beFiles/fileupload.do" id="uploadform" method="post" enctype="multipart/form-data">
				<input type="hidden" name="folder" value="${folder_ref }" />
				<input type="hidden" name="save" />
				<input type="hidden" name="filename" value="파일 이름"/>
				<input type="submit" value="업로드" />
			</form>
		</div>
		<!-- 폴더생성 폼 -->
		<div style="height:5%; width:relative; margin:0; float:left;">
			<form action="/BEngineer/beFiles/createFolder.do" id="folderform" method="post">
				<input type="hidden" name="folder" value="${folder_ref }" />
				<input type="hidden" name="foldername" value="폴더 이름"/>
				<input type="submit" value="폴더 생성" />
			</form>
		</div>
		<!-- 폴더명 변경 폼 -->
		<div style="height:5%; width:relative; margin:0; float:left;">
			<form id="changenameform" method="post">
				<input type="hidden" name="folder" value="${folder_ref }" />
				<input type="hidden" name="ref" />
				<input type="hidden" name="name" />
				<input type="hidden" name="submitchangename" />
			</form>
		</div>
		<!-- 공유 폼 -->
		<div style="height:5%; width:relative; margin:0; float:left;">
			<div style="height:100%; width:relative; margin-top:5; float:left;">
				<input type="hidden" id="text" style="background-color:transparent; border:0px; text-color:black; width:70px;" value="공유 기한 : " disabled/>
			</div>
			<div style="height:100%; width:relative; margin:0; float:left;">
				<form action="/BEngineer/beFiles/shareFile.do" id="shareform" method="post">
					<input type="hidden" name="ref" />
					<input type="hidden" name="term" />
					<select name="randw" hidden style="height:25px;">
						<option value="0">보기만 허용</option>
						<option value="1">쓰기도 허용</option>
					</select>
					<input type="hidden" name="submitshare" value="공유하기"/>
				</form>
			</div>
		</div>
	</c:if>
	button1
</div>
<div id="address" style="height:5%; width:100%; background-color:#99ffff; float:left;">
	<c:set var="num" value="0" />
	<!-- 폴더경로 보여주기 -->
	<c:forEach var="addr" items="${folderaddress }">
		<c:if test="${orgaddress[num] != null }">
			<a href="/BEngineer/beFiles/beMyList.do?folder=${orgaddress[num] }">${addr }</a> /
		</c:if>
		<c:if test="${orgaddress[num] == null }">
			${folderaddress[num] } /
		</c:if>
		<c:set var="num" value="${num + 1 }" />
	</c:forEach>
	<!-- 선택파일 보여주기용 -->
	<font id="filename"></font>
</div>
<div id="button2" style="height:80%; width:10%; background-color:#ff99ff; float:left;">
	<input type="button" id="myfile" value="내 파일보기"/>
	button2
</div>
<!-- 파일들 창 -->
<div id="files" style="height:80%; width:90%; background-color:#999999; float:left; overflow:scroll;">
	<c:forEach var="file" items="${list }">
		<div class="file" name="${file.num }" style="height:10%; width:10%; margin:1%; background-color:#ff6666; float:left; overflow:hidden">${file.filename }<input type="text" id="${file.filename }" value="${file.orgname }" style="border:0; background:transparent; cursor:default; width:100%;" disabled/></div>
		<input type="hidden" id="${file.filename }type" value="${file.filetype }"/>
	</c:forEach>
</div>
<div id="etc" style="height:10%; width:100%; background-color:#5f7f89; float:left;">
	etc
</div>