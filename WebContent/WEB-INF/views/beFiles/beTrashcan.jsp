<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript" src="http://code.jquery.com/jquery-3.2.0.min.js" ></script>
<script type="text/javascript">
	var clickedfile = new Array();
	$(function(){
		$("#files > div").click(function(){ // 파일 클릭시
			var filename = $(this).text();
			var orgname = document.getElementById(filename); // 원 파일명 저장되어있는 인풋의 값 가져오기
			$("font#filename").text(orgname.value); // 주소부분에 표시
			var filename = $(this).text();
			var ref = $(this).attr("name");
			var type = document.getElementById(filename + "type"); // 파일타입 저장되어있는 인풋의 값 가져오기
			form = document.getElementById("multiform");
			if(document.getElementById("multitext").type == "text"){
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
				form.file_ref.value = ref;
				document.getElementById("submitdelete").type = "button";
				$("#files > div").css("background-color","#ff6666"); // 모든 파일 선택 취소
				$(this).css("background-color","#6666dd"); // 클릭파일 색 바꾸기
			}
		});
	});
	$(function(){
		$("#files > div").dblclick(function(){ // 파일 더블클릭시
			var filename = $(this).text();
			var type = document.getElementById(filename + "type"); // 파일타입 저장되어있는 인풋의 값 가져오기
			if(type.value == "dir"){ // 폴더일 때 해당 폴더로 이동
				window.location = "/BEngineer/beFiles/beTrashcan.do?folder=" + $(this).attr("name");
			}
			if(type.value != "dir"){ // 폴더일 때 해당 폴더로 이동
				if(confirm("파일을 복구하시겠습니까?(주의 : 기존의 폴더가 삭제된 경우 기본 폴더에 복구됩니다.)")){
					window.location = "/BEngineer/beFiles/repairFile.do?file_ref=" + $(this).attr("name") + "&folder=" + ${folder_ref};
				}
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
		$("#mytrashcan").click(function(){
			window.location = "/BEngineer/beFiles/beTrashcan.do?folder=0";
		});
	});
	$(function(){
		$("#beLogo").click(function(){ // 로고 클릭시 메인으로 이동
			window.location = "/BEngineer/beMain.do";
		});
	});
	$(function(){
		$("#cancelmulti").click(function(){ // 다중선택 취소시
			clickedfile = new Array();
			var form = document.getElementById("multiform");
			form.file_ref.value = "";
			form.submitmulti.value = "여러 파일 선택하기";
			document.getElementById("multitext").type = "hidden";
			document.getElementById("cancelmulti").type = "hidden";
			document.getElementById("submitdelete").type = "hidden";
			$("#files > div").css("background-color","#ff6666"); // 모든 파일 선택 취소
		});
	});
	$(function(){
		$("#submitdelete").click(function(){ // 지우기 클릭 시
			var form = document.getElementById("multiform");
			if(!form.file_ref.value){ // 업로드할 파일 미선택시
				alert('삭제할 파일을 선택해주세요');
				return false;
			}
			window.location = "/BEngineer/beFiles/deleteFile.do?file_ref=" + form.file_ref.value + "&folder=" + ${folder_ref };
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
		$("#multiform").submit(function(){ // 여러 파일 선택하기 버튼 클릭시
			var form = document.getElementById("multiform"); // 폼 받아오기
			if(document.getElementById("multitext").type == "hidden"){ // 폼이 숨겨진 상태일 때 폼 보이고 이동 취소
				hinder(); // 다른 폼 닫기
				document.getElementById("multitext").type = "text";
				document.getElementById("cancelmulti").type = "button";
				document.getElementById("submitdelete").type = "button";
				form.submitmulti.value = "복구";
				$("#files > div").css("background-color","#ff6666"); // 모든 파일 선택 취소
				return false;
			}
			if(!form.file_ref.value){ // 업로드할 파일 미선택시
				alert('복구할 파일을 선택해주세요');
				return false;
			}
		});
	});
	function hinder(){ // 모든 폼 닫기 함수
		form = document.getElementById("multiform");
		form.file_ref.value = "";
		form.submitmulti.value = "여러 파일 선택하기";
		document.getElementById("multitext").type = "hidden";
		document.getElementById("cancelmulti").type = "hidden";
		document.getElementById("submitdelete").type = "hidden";
	}
</script>
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
	<!-- 다수 파일 선택 폼 -->
	<div style="height:5%; width:relative; margin:0; float:left;">
		<div style="height:100%; width:relative; margin-top:5; float:left;">
			<input type="hidden" id="multitext" style="background-color:transparent; border:0px; text-color:black; width:230px;" value="다운로드할 파일/폴더를 선택해주세요" disabled/>
		</div>
		<div style="height:100%; width:relative; margin:0; float:left;">
			<form action="/BEngineer/beFiles/repairFile.do" id="multiform" method="post">
				<input type="hidden" name="file_ref" />
				<input type="hidden" name="folder" value="${folder_ref }"/>
				<div style="height:100%; width:relative; float:left;">
					<input type="submit" name="submitmulti" value="여러 파일 선택하기"/>
				</div>
				<div style="height:100%; width:relative; float:left;">
					<input type="hidden" id="cancelmulti" value="취소" />
				</div>
				<div style="height:100%; width:relative; float:left;">
					<input type="hidden" id="submitdelete" value="지우기" />
				</div>
			</form>
		</div>
	</div>
</div>
<div id="address" style="height:5%; width:100%; background-color:#99ffff; float:left;">
	<c:set var="num" value="0" />
	<!-- 폴더경로 보여주기 -->
	<c:forEach var="addr" items="${folderaddress }">
		<c:if test="${orgaddress[num] != null }">
			<a href="/BEngineer/beFiles/beTrashcan.do?folder=${orgaddress[num] }">${addr }</a> /
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
	<input type="button" id="myfile" value="내 파일"/>
	<input type="button" id="mysharedfile" value="공유 파일"/>
	<input type="button" id="mytrashcan" value="휴지통"/>
	button2
</div>
<!-- 파일들 창 -->
<div id="files" style="height:80%; width:90%; background-color:#999999; float:left; overflow-y:scroll;">
	<c:forEach var="file" items="${list }">
		<div class="file" name="${file.num }" style="height:100; width:100; margin:1%; background-color:#ff6666; float:left; overflow:hidden">${file.filename }<input type="text" id="${file.filename }" value="${file.orgname }" style="border:0; background:transparent; cursor:default; width:100%;" disabled/></div>
		<input type="hidden" id="${file.filename }type" value="${file.filetype }"/>
	</c:forEach>
</div>
<div id="etc" style="height:10%; width:100%; background-color:#5f7f89; float:left;">
	etc
</div>