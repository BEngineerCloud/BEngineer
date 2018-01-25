<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript" src="http://code.jquery.com/jquery-3.2.0.min.js" ></script>
<script type="text/javascript">
var clickedfile = new Array();
$(function(){
	$("#files > div").click(function(){ // 파일 클릭시
		var filename = $(this).text();
		var ref = $(this).attr("name");
		var orgname = document.getElementById(ref); // 원 파일명 저장되어있는 인풋의 값 가져오기
		$("font#filename").text(orgname.value); // 주소부분에 표시
		var type = document.getElementById(ref + "type"); // 파일타입 저장되어있는 인풋의 값 가져오기
		form = document.getElementById("multidownform");
		if(document.getElementById("multidowntext").type == "text"){
			var index = clickedfile.indexOf(ref);
			if(index == -1){
				clickedfile.push(ref);
				$(this).css("background-color","#6666dd"); // 클릭파일 색 바꾸기
			}else{
				clickedfile.splice(index, 1);
				$(this).css("background-color","#ff6666"); // 클릭파일 색 바꾸기
			}
			form.file_ref.value = clickedfile.join();
		}else{
			hinder(); // 업로드 입력, 폴더생성 입력 취소
			form.submitmultidown.type = "submit";
			$("#files > div").css("background-color","#ff6666"); // 모든 파일 선택 취소
			$(this).css("background-color","#6666dd"); // 클릭파일 색 바꾸기
			setForm(type, ref);
			if(type.value == "dir"){
				var form = document.getElementById("folderdownform");
				form.file_ref.value = ref;
				form.submitfolderdown.type = "submit";
			}
		}
	});
});
	$(function(){
		$("#files > div").dblclick(function(){ // 파일 더블클릭시
			var ref = $(this).attr("name");
			var type = document.getElementById(ref + "type"); // 파일타입 저장되어있는 인풋의 값 가져오기
			if(type.value == "dir"){ // 폴더일 때 해당 폴더로 이동
				window.location = "/BEngineer/beFiles/beSharedList.do?folder=" + ref;
			}
			if(type.value != "dir"){ // 파일일 때 해당 파일 다운로드
				window.location = "/BEngineer/beFiles/beDownload.do?file_ref=" + ref;
			}
		});
	});
	$(function(){
		$("#myfile").click(function(){ // 내 파일보기 버튼 클릭 시 페이지 이동
			window.location = "/BEngineer/beFiles/beMyList.do?folder=0";
		});
	});
	$(function(){
		$("#mysharedfile").click(function(){
			window.location = "/BEngineer/beFiles/beSharedList.do?folder=0";
		});
	});
	$(function(){
		$("#beLogo").click(function(){ // 로고 클릭시 메인으로 이동
			window.location = "/BEngineer/beMain.do";
		});
	});
	$(function(){
		$("#mytrashcan").click(function(){
			window.location = "/BEngineer/beFiles/beTrashcan.do?folder=0";
		});
	});
	$(function(){
		$("#cancelmultidown").click(function(){ // 다중선택 취소시
			clickedfile = new Array();
			var form = document.getElementById("multidownform");
			form.file_ref.value = "";
			form.submitmultidown.value = "여러 파일 선택하기";
			document.getElementById("multidowntext").type = "hidden";
			document.getElementById("cancelmultidown").type = "hidden";
			$("#files > div").css("background-color","#ff6666"); // 모든 파일 선택 취소
		});
	});
	$(function(){
		$("#addinfodiv > #addinfo").click(function(){
			window.location = "/BEngineer/beMember/beAddinfo.do";
		});
	});

	$(function(){
		$("#logoutdiv > #logout").click(function(){
			window.location = "/BEngineer/beLogout.do";
		});
	});
	$(function(){
		$("#multidownform").submit(function(){ // 여러 파일 다운로드 버튼 클릭시
			var form = document.getElementById("multidownform"); // 폼 받아오기
			if(document.getElementById("multidowntext").type == "hidden"){ // 폼이 숨겨진 상태일 때 폼 보이고 이동 취소
				hinder(); // 다른 폼 닫기
				document.getElementById("multidowntext").type = "text";
				document.getElementById("cancelmultidown").type = "button";
				form.submitmultidown.type = "submit";
				form.submitmultidown.value = "다운로드";
				$("#files > div").css("background-color","#ff6666"); // 모든 파일 선택 취소
				return false;
			}
			if(!form.file_ref.value){ // 업로드할 파일 미선택시
				alert('업로드할 파일을 선택해주세요');
				return false;
			}
		});
	});

</script>
<c:if test="${write }"><!-- 쓰기권한이 있을 때 -->
	<script type="text/javascript">
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
				var orgname = form.save.value;
				var index = orgname.lastIndexOf("\\");
				orgname = orgname.substring(index + 1);
				var check = $("input[value='" + orgname + "']").attr("type");
				if(check == "text"){
					if(!confirm("폴더 안에 같은 이름의 파일이 존재합니다. 덮어 쓰시겠습니까?")){
						return false;
					}
				}
				form.filename.value = form.filename.value.trim();
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
				var orgname = form.foldername.value.trim();
				var check = $("input[value='" + orgname + "']").attr("type");
				if(check == "text"){
					alert("이미 해당 위치에 같은 이름의 폴더가 존재합니다.");
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
					form.submitchangename.type="submit";
					return false;
				}
				if(!form.name.value.trim() || form.name.value == "폴더 이름" || form.name.value == "파일 이름"){ // 폴더 명 미입력시
					alert('생성할 폴더의 이름을 입력해주세요');
					return false;
				}
				var orgname = form.name.value.trim();
				if(form.submitchangename.value == "폴더명 변경"){
					var check = $("input[value='" + orgname + "']").attr("type");
					if(check == "text"){
						alert("이미 해당 위치에 같은 이름의 폴더가 존재합니다.");
						return false;
					}
				}else if(form.submitchangename.value == "파일명 변경"){
					var check = document.getElementById(orgname);
					if(check != null){
						alert("이미 해당 위치에 같은 이름의 파일이 존재합니다.");
						return false;
					}
				}
				form.name.value = form.name.value.trim(); // 폴더명 앞뒤의 공백문자 삭제
			});
		});
		$(function(){
			$("#writetextbutton").click(function(){ // 텍스트 파일 작성 클릭 시
				hinder();
				document.getElementById("writetextdiv").style.display = "block";
				document.getElementById("files").style.height = "35%";
			});
		});
		$(function(){
			$("#canclewritetext").click(function(){ // 텍스트 파일 작성 클릭 시
				hinder();
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
		function hinder(){ // 모든 폼 닫기 함수
			var form = document.getElementById("uploadform"); // 파일업로드 폼
			form.save.type = "hidden";
			form.filename.type = "hidden";
			form = document.getElementById("folderform"); // 폴더생성 폼
			form.foldername.type = "hidden";
			form = document.getElementById("changenameform"); // 폴더생성 폼
			form.name.type = "hidden";
			form.submitchangename.type = "hidden";
			form = document.getElementById("folderdownform");
			form.submitfolderdown.type = "hidden";
			clickedfile = new Array();
			form = document.getElementById("multidownform");
			form.file_ref.value = "";
			form.submitmultidown.value = "여러 파일 선택하기";
			document.getElementById("multidowntext").type = "hidden";
			document.getElementById("cancelmultidown").type = "hidden";
			document.getElementById("files").style.height = "75%";
			document.getElementById("writetextdiv").style.display = "none";
		}
		function setForm(filename, ref){
			var type = document.getElementById(filename + "type"); // 파일타입 저장되어있는 인풋의 값 가져오기
			var form = document.getElementById("changenameform");
			form.ref.value = ref;
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
			form.ref.value = ref;
			form.submitshare.type = "submit";
		}
	</script>
</c:if>
<c:if test="${!write }">
	<script>
		function hinder(){
			form = document.getElementById("folderdownform");
			form.submitfolderdown.type = "hidden";
			clickedfile = new Array();
			form = document.getElementById("multidownform");
			form.file_ref.value = "";
			form.submitmultidown.value = "여러 파일 다운로드";
			document.getElementById("multidowntext").type = "hidden";
			document.getElementById("cancelmultidown").type = "hidden";
		}
		function setForm(filename, ref){}
	</script>
</c:if>
<body topmargin="0" bottommargin="0" leftmargin="0" rightmargin="0">
<div id="logo" style="height:10%; width:15%; background-color:#ff9999; float:left;">
	<img src="/BEngineer/image/beCloudLogo.png" id="beLogo" style="width: 100%; height:100%; cursor:pointer"/>
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
	</c:if>
	<!-- 다수 파일 다운로드 폼 -->
	<div style="height:5%; width:relative; margin:0; float:left;">
		<div style="height:100%; width:relative; margin-top:5; float:left;">
			<input type="hidden" id="multidowntext" style="background-color:transparent; border:0px; text-color:black; width:230px;" value="다운로드할 파일/폴더를 선택해주세요" disabled/>
		</div>
		<div style="height:100%; width:relative; margin:0; float:left;">
			<form action="/BEngineer/beFiles/beDownload.do" id="multidownform" method="post">
				<input type="hidden" name="file_ref" />
				<input type="submit" name="submitmultidown" value="여러 파일 선택하기"/>
				<input type="hidden" id="cancelmultidown" value="취소" />
			</form>
		</div>
	</div>
	<c:set var="names" value="array(etc, image, music, video)"/>
	<c:if test="${!basedir || !names.contains(folderaddress.get(1)) }">
		<c:if test="${write }"><!-- 쓰기권한이 있을 때 -->
			<!-- 파일생성 폼 -->
			<div style="height:5%; width:relative; margin:0; float:left;">
				<input type="button" value="텍스트 파일 만들기" id="writetextbutton"/>
			</div>
		</c:if>
	</c:if>
</div>
<div id="button1_1" style="height:5%; width:100%; background-color:#eeee88; float:left;">
	<c:if test="${write }"><!-- 쓰기권한이 있을 때 -->
		<!-- 폴더명 변경 폼 -->
		<div style="height:5%; width:relative; margin:0; float:left;">
			<form id="changenameform" method="post">
				<input type="hidden" name="folder" value="${folder_ref }" />
				<input type="hidden" name="ref" />
				<input type="hidden" name="name" />
				<input type="hidden" name="submitchangename" />
			</form>
		</div>
	</c:if>
	<!-- 폴더 다운로드 폼 -->
	<div style="height:5%; width:relative; margin:0; float:left;">
		<form id="folderdownform" method="post" action="/BEngineer/beFiles/beDownload.do">
			<input type="hidden" name="file_ref" />
			<input type="hidden" name="submitfolderdown" value="폴더 다운로드"/>
		</form>
	</div>
</div>
<div id="address" style="height:5%; width:100%; background-color:#99ffff; float:left;">
	<c:set var="num" value="0" />
	<!-- 폴더경로 보여주기 -->
	<c:forEach var="addr" items="${folderaddress }">
		<c:if test="${orgaddress[num] != null }">
			<a href="/BEngineer/beFiles/beSharedList.do?folder=${orgaddress[num] }">${addr }</a> /
		</c:if>
		<c:if test="${orgaddress[num] == null }">
			${folderaddress[num] } /
		</c:if>
		<c:set var="num" value="${num + 1 }" />
	</c:forEach>
	<!-- 선택파일 보여주기용 -->
	<font id="filename"></font><c:if test="${folder_ref != 0 }">  (${enddate }까지 <c:if test="${write }">쓰기</c:if><c:if test="${!write }">읽기</c:if> 가능)</c:if>
</div>
<div id="button2" style="height:75%; width:10%; background-color:#ff99ff; float:left;">
	<input type="button" id="myfile" value="내 파일"/>
	<input type="button" id="mysharedfile" value="공유 파일"/>
	<input type="button" id="mytrashcan" value="휴지통"/>
	button2
</div>
<!-- 파일들 창 -->
<div id="files" style="height:75%; width:90%; background-color:#999999; float:left; overflow-y:scroll;">
	<c:forEach var="file" items="${list }">
		<div class="file" name="${file.num }" style="height:100; width:100; margin:1%; background-color:#ff6666; float:left; overflow:hidden">${file.filename }<input type="text" id="${file.num }" value="${file.orgname }" style="border:0; background:transparent; cursor:default; width:100%;" disabled/></div>
		<input type="hidden" id="${file.num }type" value="${file.filetype }"/>
	</c:forEach>
</div>
<c:if test="${write }">
	<!-- text파일 쓰기용 창 -->
	<div id="writetextdiv" style="height:40%; width:90%; background-color:#ffff99; float:left; overflow-y:scroll; display:none">
		<form action="/BEngineer/beFiles/writeText.do" id="writetextform" method="post">
			<input type="hidden" name="folder" value="${folder_ref }"/>
			<div style="height:8%; width:50%; float:left; text-align:left;">
				파일 별명 : <input type="text" name="filename"/>
			</div>
			<div style="height:8%; width:50%; float:left; text-align:left;">
				파일명 : <input type="text" name="orgname"/>
			</div>
			<div style="height:84%; width:100%; float:left; text-align:left;">
				<textarea name="content" cols="100" rows="20"></textarea>
			</div>
			<div style="height:8%; width:50%; float:left; text-align:left;">
				<input type="submit" value="작성완료"/>
			</div>
			<div style="height:8%; width:50%; float:left; text-align:left;">
				<input type="button" value="취소" id="canclewritetext"/>
			</div>
		</form>
	</div>
</c:if>
<div id="etc" style="height:10%; width:100%; background-color:#5f7f89; float:left;">
	etc
</div>