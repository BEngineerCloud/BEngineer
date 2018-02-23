<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel='stylesheet' href='/BEngineer/resources/css/style.css'>
<script type="text/javascript" src="http://code.jquery.com/jquery-3.2.0.min.js" ></script>
<script src='/BEngineer/resources/pagectrl/menu.js' type='text/javascript'></script>
<script type="text/javascript">
	var clickedfile = new Array();$(function(){
		$("#files").click(function(e){
			var target = $(e.target).attr("class");
			var parent = $(e.target).parent().attr("class");
			var check = false;
			var filename = null;
			var ref = null;
			if(target == 'file'){
				filename = $(e.target).text();
				ref = $(e.target).attr("name");
				check = true;
			}else if(parent == 'file'){
				filename = $(e.target).parent().text();
				ref = $(e.target).parent().attr("name");
				check = true;
			}
			if(check){
				var orgname = document.getElementById(ref); // 원 파일명 저장되어있는 인풋의 값 가져오기
				$("font#filename").text(orgname.value); // 주소부분에 표시
				var type = document.getElementById(ref + "type"); // 파일타입 저장되어있는 인풋의 값 가져오기
				form = document.getElementById("multiform");
				if(document.getElementById("multitext").type == "text"){
					var index = clickedfile.indexOf(ref);
					if(index == -1){
						clickedfile.push(ref);
						$("div[name='" + ref + "']").css("background-color","aqua"); // 클릭파일 색 바꾸기
					}else{
						clickedfile.splice(index, 1);
						$("div[name='" + ref + "']").css("background-color","#ffffff"); // 클릭파일 색 바꾸기
					}
					form.file_ref.value = clickedfile.join();
				}else{
					hinder(); // 업로드 입력, 폴더생성 입력 취소
					form.file_ref.value = ref;
					form.submitmultirepair.type = "submit";
					document.getElementById("submitdelete").type = "button";
					$("#files > div").css("background-color","#ffffff"); // 모든 파일 선택 취소
					$("div[name='" + ref + "']").css("background-color","aqua"); // 클릭파일 색 바꾸기
				}
			}else{
				hinder();
			}
		});
	});
	$(function(){
		$("#files > div").dblclick(function(){ // 파일 더블클릭시
			var ref = $(this).attr("name");
			var type = document.getElementById(ref + "type"); // 파일타입 저장되어있는 인풋의 값 가져오기
			if(type.value == "dir"){ // 폴더일 때 해당 폴더로 이동
				window.location = "/BEngineer/beFiles/beTrashcan.do?folder=" + ref;
			}
			if(type.value != "dir"){ // 폴더일 때 해당 폴더로 이동
				if(confirm("파일을 복구하시겠습니까?(주의 : 기존의 폴더가 삭제된 경우 기본 화면에 복구됩니다.)")){
					window.location = "/BEngineer/beFiles/repairFile.do?file_ref=" + ref + "&folder=" + ${folder_ref};
				}
			}
		});
	});
	$(function(){
		$("#cancelmulti").click(function(){ // 다중선택 취소시
			clickedfile = new Array();
			var form = document.getElementById("multiform");
			form.file_ref.value = "";
			form.submitmultirepair.type = "hidden";
			document.getElementById("multichoice").type = "button";
			document.getElementById("multitext").type = "hidden";
			document.getElementById("cancelmulti").type = "hidden";
			document.getElementById("submitdelete").type = "hidden";
			$("#files > div").css("background-color","#ffffff"); // 모든 파일 선택 취소
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
		$("#multichoice").click(function(){ // 여러 파일 선택 클릭 시
			var form = document.getElementById("multiform"); // 폼 받아오기
			hinder(); // 다른 폼 닫기
			document.getElementById("multitext").type = "text";
			document.getElementById("cancelmulti").type = "button";
			document.getElementById("submitdelete").type = "button";
			document.getElementById("multichoice").type = "hidden";
			form.submitmultirepair.type = "submit";
			$("#files > div").css("background-color","#ffffff"); // 모든 파일 선택 취소
		});
	});
	$(function(){
		$("#multiform").submit(function(){ // 여러 파일 선택하기 버튼 클릭시
			var form = document.getElementById("multiform"); // 폼 받아오기
			if(!form.file_ref.value){ // 복구할 파일 미선택시
				alert('복구할 파일을 선택해주세요');
				return false;
			}
		});
	});
	function hinder(){ // 모든 폼 닫기 함수
		form = document.getElementById("multiform");
		form.file_ref.value = "";
		form.submitmultirepair.type = "hidden";
		document.getElementById("multitext").type = "hidden";
		document.getElementById("cancelmulti").type = "hidden";
		document.getElementById("submitdelete").type = "hidden";
		document.getElementById("multichoice").type = "button";
	}
	$(function(){
		$("#hotlist").click(function(){
			window.location = "/BEngineer/beFiles/hotlist.do";
		});
	});
</script>
<body topmargin="0" bottommargin="0" leftmargin="0" rightmargin="0">
<div id="logo" style="height:10%; width:15%; background-color:#ff9999; float:left;">
	<img src="/BEngineer/image/beCloudLogo.png" id="beLogo" style="width: 100%; height:100%; cursor:pointer"/>
</div>
<!-- 검색창 -->
<div align="center" style="height:10%; width:61%; float:left;">
	<div style="margin-top:2%">
	<input type="text" id="searchword"style="height:38%; width:20%; border-color: black; background-color:#FFFFFF;"/>
	<input type="button" id="search" value="검  색" style="height:41%; border-color: black; background-color:#FFFFFF;"/>
	</div>
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
<div id="button1">
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
					<input type="button" id="multichoice" value="여러 파일 선택하기"/>
				</div>
				<div style="height:100%; width:relative; float:left;">
					<input type="hidden" id="cancelmulti" value="취소" />
				</div>
				<div style="height:100%; width:relative; float:left;">
					<input type="hidden" name="submitmultirepair" value="복구하기"/>
				</div>
				<div style="height:100%; width:relative; float:left;">
					<input type="hidden" id="submitdelete" value="완전 삭제하기" />
				</div>
			</form>
		</div>
	</div>
</div>
<div id="address">
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
	전체 사용량
	<c:if test="${space != null && space !=''}">
		<img src="data:image/png;base64,${space}" style="width:100%;" />
	</c:if>
</div>
<!-- 파일들 창 -->
<div id="files">
	<c:forEach var="file" items="${list }">
		<div class="file" name="${file.num }" >${file.filename }<input type="text" id="${file.num }" value="${file.orgname }" style="border:0; background:transparent; cursor:default; width:100%;" readOnly/></div>
		<input type="hidden" id="${file.num }type" value="${file.filetype }"/>
	</c:forEach>
</div>
<div id="etc">
		<a href="/BEngineer/inquiry/inList.do?id=${sessionScope.id}">문의내역</a>
        <a href="/BEngineer/beMember/beboard.do?id=${sessionScope.id}">공지사항</a>
        <a href="/BEngineer/beMember/upgrade.do?id=${sessionScope.id}">유료전환</a>
</div>