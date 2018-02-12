<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style type="text/css">
html,body{
	margin:0;
	padding:0;
	height:100%;
}
div#glayLayer{
	display:none;
	position:fixed;
	left:0;
	top:0;
	height:100%;
	width:100%;
	background:black;
	filter:alpha(opacity=60);
	opacity: 0.60;
}
* html div#glayLayer{
	position:absolute;
}
#overLayer{
	display:none;
	position: absolute;
	top:10%;
	left:20%;
	max-height:80%;
	width:60%
}
* html #overLayer{
	position: absolute;
}
</style>

<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.min.js"></script>
<script type="text/javascript" src="http://code.jquery.com/jquery-3.2.0.min.js" ></script>
<script type="text/javascript">
	var clickedfile = new Array();
	var clickedImportant = new Array(); //여러 파일/폴더 선택 시 중요폴더가 포함되어있는지 알기 위해
	
	$(function(){
		$("#files > div").click(function(){ // 파일 클릭시
			var filename = $(this).text();
			var ref = $(this).attr("name");
			var orgname = document.getElementById(ref+ "orgname"); // 원 파일명 저장되어있는 인풋의 값 가져오기
			$("font#filename").text(orgname.value); // 주소부분에 표시
			var important =document.getElementById(ref + "important");
			var type = document.getElementById(ref + "type"); // 파일타입 저장되어있는 인풋의 값 가져오기
			var moveform = document.getElementById("moveform"); // moveform 가져오기
			var copyform = document.getElementById("copyform"); // copyform 가져오기
			var imageform = document.getElementById("imageform"); //imageform 가져오기
			
			imageform.imageview.type="button";
			imageform.selImageview.value=ref;
			
			if(important.value!=-1 && copyform.submitcopy.value!="복사하기"){ // 기본폴더는 important가 -1이므로 기본폴더가 아닐 시 이동버튼 나타냄
				if(moveform.submitmove.value=="이동"){ // form.submitmove 값이 이동일 시
					moveform.submitmove.type = "submit"; // form.submitmove 타입을 submit으로 설정
				}
			}
			else{ //기본 폴더일시 이동버튼 숨기기
				moveform.submitmove.type="hidden";
			}
			
			if(moveform.submitmove.value == "이동"){ // moveform.submitmove 값이 '이동'일 시
				moveform.select_flag.value=ref;		// movefrom.select_flag의 값에 클릭된 fileform.ref 대입
			}else if(moveform.submitmove.value == "이동하기"){ // moveform.submitmove 값이 '이동하기'일 시
				moveform.folder_ref.value=ref;			  // moveform.folder_ref 값에 클릭된 fileform.ref 대입
			}
			
			//현재 파일 이동하기 클릭 후 다른폴더를 선택했을 때 이동하기 버튼이 나오게하기 위해서
			if(moveform.folder_ref.value!=0){ 	   // moveform.folder_ref 값이 0이 아닐시
				if(type.value=="dir"&&(moveform.ref.value!=moveform.folder_ref.value)){
					moveform.submitmove.type="submit"; // moveform.submitmove 타입을 submit으로 설정
				}
				else{ // 클릭한 객체가 폴더가 아니고 파일인 경우 이동하기 버튼을 숨긴다.
					moveform.submitmove.type="hidden";
				}
			}
			
			if(copyform.submitcopy.value=="복사"&&moveform.submitmove.type!="이동하기" && moveform.movecancel.type!=="button"){ // form.submitcopy 값이 복사일 시
				copyform.submitcopy.type = "submit"; // form.submitcopy 타입을 submit으로 설정
			}
			
			if(copyform.submitcopy.value == "복사"){ // copyform.submitcopy 값이 '이동'일 시
				copyform.select_flag.value=ref;		// copyfrom.select_flag의 값에 클릭된 fileform.ref 대입
			}else if(copyform.submitcopy.value == "복사하기"){ // copyform.submitcopy 값이 '이동하기'일 시
				copyform.folder_ref.value=ref;			  // copyform.folder_ref 값에 클릭된 fileform.ref 대입
			}
			
			//현재 파일 복사하기 클릭 후 다른폴더를 선택했을 때 이동하기 버튼이 나오게하기 위해서
			if(copyform.folder_ref.value!=0){ 	   // moveform.folder_ref 값이 0이 아닐시
				if(type.value=="dir"&&(copyform.ref.value!=copyform.folder_ref.value)){
					copyform.submitcopy.type="submit"; // moveform.submitmove 타입을 submit으로 설정
				}
				else{ // 클릭한 객체가 폴더가 아니고 파일인 경우 이동하기 버튼을 숨긴다.
					copyform.submitcopy.type="hidden";
				}
			}
			
			form = document.getElementById("multidownform");
			if(type.value=="dir"&&(form.multimove.value=="이동하기" || form.multicopy.value=="복사하기")){
					form.file_fref.value=ref;
			}else{
				form.file_fref.value="";
			}
			if(document.getElementById("multidowntext").type == "text"){
				var index = clickedfile.indexOf(ref);
				var flag = 0;
				moveform.submitmove.type="hidden";
				copyform.submitcopy.type="hidden";
				imageform.imageview.type="hidden";
				if(index == -1 ){
					clickedfile.push(ref);
					clickedImportant.push(important.value);
					$(this).css("background-color","#6666dd"); // 클릭파일 색 바꾸기
				}else{
					clickedfile.splice(index, 1);
					clickedImportant.splice(index, 1);
					$(this).css("background-color","#ff6666"); // 클릭파일 색 바꾸기
				}
				
				if(form.file_fref.value=="" ||(form.multimove.value=="이동" && form.multimove.type=="button")){
					for(var i=0; i<clickedImportant.length; i++){ // 클릭한 파일/폴더 중에 중요폴더가 포함됐는지 검사
						if(clickedImportant[i]==-1){
							flag=1;
						}
					}
				}
				if(flag==1){
					form.multimove.type="hidden";
					multimoveflag = 0;
				}else{
					form.multicopy.type="button";
					form.multimove.type="button";
				}
			}else{
				hinder(); // 업로드 입력, 폴더생성 입력 취소
				form.file_ref.value = ref;
				document.getElementById("throwtotrashcan").type = "button";
				$("#files > div").css("background-color","#ff6666"); // 모든 파일 선택 취소
				$(this).css("background-color","#6666dd"); // 클릭파일 색 바꾸기
				setForm(type, ref);
				if(type.value == "dir"){
					var form = document.getElementById("folderdownform");
					form.file_ref.value = ref;
					form.submitfolderdown.type = "submit";
				}
				form = document.getElementById("sharecheckform");
				form.file.value = ref;
				form.submitsharecheck.type = "submit";
				document.getElementById("unsharediv").style.display = "block";
				document.getElementById("unshareform").file_ref.value = ref;
				if(important.value != -1){
					document.getElementById("throwtotrashcan").type = "button";
					document.getElementById("changeownerdiv").style.display = "block";
					document.getElementById("changeownerform").file_ref.value = ref;
				}
				if(type.value == ".txt"){
					form = document.getElementById("rewritetextform");
					form.filenum.value = ref;
					form.submitrewritetext.type = "submit";
				}			
			}
		});
	});
	$(function(){
		$("#files > div").dblclick(function(){ // 파일 더블클릭시
			var ref = $(this).attr("name");
			window.location = "/BEngineer/beFiles/beDownload.do?file_ref=" + ref;
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
		$("#throwtotrashcan").click(function(){ // 지우기 클릭 시
			var form = document.getElementById("multidownform");
			if(document.getElementById("multidowntext").type != "hidden"){
				form.file_ref.value = clickedfile.join();
			}	
			window.location = "/BEngineer/beFiles/throwToTrashcan.do?file_ref=" + form.file_ref.value + "&folder=" + ${folder_ref };
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
		$("#uploadform").submit(function(){ // 업로드 버튼 클릭시
			var form = document.getElementById("uploadform"); // 폼 받아오기
			if(form.save.type == "hidden"){ // 폼이 숨겨진 상태일 때 폼 보이고 이동 취소
				hinder(); // 다른 폼 닫기
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
		$("#multidownform").submit(function(){ // 여러 파일 다운로드 버튼 클릭시
			var form = document.getElementById("multidownform"); // 폼 받아오기
			var moveform = document.getElementById("moveform");
			var copyform = document.getElementById("copyform");
			var imageform = document.getElementById("imageform");
			form.file_ref.value = clickedfile.join();
			moveform.submitmove.type="hidden";
			moveform.movecancel.type="hidden"
			moveform.submitmove.value="이동";
			copyform.submitcopy.type="hidden";
			copyform.copycancel.type="hidden"
			copyform.submitcopy.value="복사";
			imageform.imageview.type="hidden";
			if(document.getElementById("multidowntext").type == "hidden" ){ // 폼이 숨겨진 상태일 때 폼 보이고 이동 취소
				hinder(); // 다른 폼 닫기
				document.getElementById("multidowntext").type = "text";
				document.getElementById("cancelmultidown").type = "button";
				document.getElementById("multimove").type = "button";
				document.getElementById("multicopy").type = "button";
				form.submitmultidown.value = "다운로드";
				document.getElementById("throwtotrashcan").type = "button";
				$("#files > div").css("background-color","#ff6666"); // 모든 파일 선택 취소
				return false;
			}
			if(!form.file_ref.value){ // 업로드할 파일 미선택시
				alert('업로드할 파일을 선택해주세요');
				return false;
			}
		});
	});
	$(function(){
		$("#multimove").click(function(){ // 내 파일보기 버튼 클릭 시 페이지 이동
			var form = document.getElementById("multidownform"); // 폼 받아오기
			form.multicopy_flag.value = 0;
			form.multimove_flag.value = 1;
		
			if($(this).val()=="이동"){
				form.file_ref.value = clickedfile.join();
				if(form.file_ref.value==""){
					alert("이동할 폴더를 선택해주세요.");
					return false;
				}
				window.location="/BEngineer/beFiles/beFilesession.do?folder="+${folder_ref}+"&ref="+form.file_ref.value+"&file_flag=multimove";
			}else{
				window.location = "/BEngineer/beFiles/beMultimove.do?file_fref="+form.file_fref.value;
			}
		});
	});
	$(function(){
		$("#multicopy").click(function(){ // 내 파일보기 버튼 클릭 시 페이지 이동
			var form = document.getElementById("multidownform"); // 폼 받아오기
			form.multicopy_flag.value = 1;
			form.multimove_flag.value = 0;
			form.file_ref.value = clickedfile.join();
			
			if($(this).val()=="복사"){
				form.file_ref.value = clickedfile.join();
				if(form.file_ref.value==""){
					alert("복사할 폴더를 선택해주세요.");
					return false;
				}
				window.location="/BEngineer/beFiles/beFilesession.do?folder="+${folder_ref}+"&ref="+form.file_ref.value+"&file_flag=multicopy";
			}else{
				window.location = "/BEngineer/beFiles/beMulticopy.do?file_fref="+form.file_fref.value;
			}
		});
	});
	
	$(function(){
		$("#changenameform").submit(function(){ // 폴더명/파일명 변경 버튼 클릭시
			var moveform = document.getElementById("moveform"); // moveform 가져오기
			var copyform = document.getElementById("copyform"); // copyform 가져오기
			moveform.submitmove.type="hidden";
			copyform.submitcopy.type="hidden";
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
		$("#moveform").submit(function(){ // 파일/폴더 이동 버튼 클릭시
			var form = document.getElementById("moveform"); // moveform 받아오기, *$(this)사용
			if(form.submitmove.value=="이동"){ // submitmove 값이 이동일 시
				window.location="/BEngineer/beFiles/beFilesession.do?folder="+${folder_ref}+"&ref="+form.select_flag.value+"&file_flag=move";
				return false;
			}
		});
	});
	$(function(){
		$("#copyform").submit(function(){ // 파일/폴더 복사 버튼 클릭시
			var form = document.getElementById("copyform"); // copyform 받아오기, *$(this)사용
			if(form.submitcopy.value=="복사"){ // submitcopy 값이 이동일 시
				window.location="/BEngineer/beFiles/beFilesession.do?folder="+${folder_ref}+"&ref="+form.select_flag.value+"&file_flag=copy";				
				return false;
			}
		});
	});
	$(function(){
		$("#movecancel").click(function(){ // 파일/폴더 이동 취소 버튼 클릭시
			window.location = "/BEngineer/beFiles/beCancel.do?folder="+${folder_ref};
		});
	});
	$(function(){
		$("#copycancel").click(function(){ // 파일/폴더 이동 취소 버튼 클릭시
			window.location = "/BEngineer/beFiles/beCancel.do?folder="+${folder_ref};
		});
	});
	$(function(){
		$("#shareform").submit(function(){ // 공유 버튼 클릭시
			var moveform = document.getElementById("moveform"); // moveform 가져오기
			var copyform = document.getElementById("copyform"); // copyform 가져오기
			moveform.submitmove.type="hidden";
			copyform.submitcopy.type="hidden";
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
			form.enddate.min = today;
			if(form.enddate.type == "hidden"){ // 폼이 숨겨진 상태일 때 폼 보이고 이동 취소
				hinder(); // 다른 폼 닫기
				document.getElementById("text").type = "text";
				form.enddate.type = "date";
				form.rw.hidden = false;
				form.submitshare.type = "submit";
				return false;
			}
			if(!form.enddate.value){ // 공유기한 미입력시
				alert('공유기한을 입력해주세요');
				return false;
			}
		});
	});
	$(function(){
		$("#unshareform").submit(function(){ // 공유 해제 버튼 클릭시
			var moveform = document.getElementById("moveform"); // moveform 가져오기
			var copyform = document.getElementById("copyform"); // copyform 가져오기
			moveform.submitmove.type="hidden";
			copyform.submitcopy.type="hidden";
			var form = document.getElementById("unshareform"); // 폼 받아오기
			var select = document.getElementById("unshareselect");
			if(select.hidden == true){ // 폼이 숨겨진 상태일 때 폼 보이고 이동 취소
				hinder(); // 다른 폼 닫기
				$("#files > div").css("background-color","#ff6666"); // 모든 파일 선택 취소
				document.getElementById("unsharediv").style.display = "block";
				select.hidden = false;
				return false;
			}
			if(select.value == 2 && !form.nickname.value.trim()){ // 업로드할 파일 미선택시
				alert('공유 해제할 사람의 닉네임을 입력해주세요');
				return false;
			}
			form.nickname.value = form.nickname.value.trim();
		});
	});
	$(function(){
		$("#changeownerform").submit(function(){ // 주인 바꾸기 버튼 클릭시
			var moveform = document.getElementById("moveform"); // moveform 가져오기
			var copyform = document.getElementById("copyform"); // copyform 가져오기
			moveform.submitmove.type="hidden";
			copyform.submitcopy.type="hidden";
			var form = document.getElementById("changeownerform"); // 폼 받아오기
			if(form.nickname.type == "hidden"){ // 폼이 숨겨진 상태일 때 폼 보이고 이동 취소
				hinder(); // 다른 폼 닫기
				$("#files > div").css("background-color","#ff6666"); // 모든 파일 선택 취소
				document.getElementById("changeownerdiv").style.display = "block";
				form.nickname.type = "text";
				return false;
			}
			if(!form.nickname.value.trim() && !form.nickname.value.trim() == ""){ // 업로드할 파일 미선택시
				alert('파일은 넘겨줄 상대의 닉네임을 입력해주세요');
				return false;
			}
			form.nickname.value = form.nickname.value.trim();
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
		form = document.getElementById("changenameform"); // 폴더생성 폼
		form.name.type = "hidden";
		form.submitchangename.type = "hidden";
		form = document.getElementById("shareform");
		form.enddate.type = "hidden";
		form.rw.hidden = true;
		form.submitshare.type = "hidden";
		document.getElementById("text").type = "hidden";
		form = document.getElementById("folderdownform");
		form.submitfolderdown.type = "hidden";
		clickedfile = new Array();
		clickedImportant = new Array();
		form = document.getElementById("multidownform");
		form.file_ref.value = "";
		form.submitmultidown.value = "여러 파일 선택하기";
		document.getElementById("multidowntext").type = "hidden";
		document.getElementById("cancelmultidown").type = "hidden";
		document.getElementById("throwtotrashcan").type = "hidden";
		form = document.getElementById("sharecheckform");
		form.submitsharecheck.type = "hidden";
		document.getElementById("files").style.height = "75%";
		
	}
	function setForm(type, ref){
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
	
	$(function(){
		$("#hotlist").click(function(){
			window.location = "/BEngineer/beFiles/hotlist.do?num=0";
		});
	});
	
	$(function(){
		$("body").append("<div id='glayLayer' ></div><div id='overLayer' style='overflow-y:auto;'></div>");
		
		$("#glayLayer").click(function(){
			$(this).hide()
			$("#overLayer").hide();
		});
		
		$("#imageview").click(function(){
			var imageform = document.getElementById("imageform");
			$("#glayLayer").show();
			$("#overLayer").show().html("<img src='"+$("#" + imageform.selImageview.value + " > div > a.modal").attr("href")+"'style=\"width:100%; \" />");
			return false;
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
<div id="button1" style="height:5%; width:100%; background-color:#ffff99; float:left;">
<!-- 파일업로드 폼 -->
	<div style="height:5%; width:relative; margin:0; float:left;">
		<form action="/BEngineer/beFiles/fileupload.do" id="uploadform" method="post" enctype="multipart/form-data">
			<input type="hidden" name="folder" value="${folder_ref }" />
			<input type="hidden" name="save" />
			<input type="hidden" name="filename" value="파일 이름"/>
			<input type="submit" value="업로드" />
		</form>
	</div>
	<!-- 다수 파일 다운로드 폼 -->
	<div style="height:5%; width:relative; margin:0; float:left;">
		<div style="height:100%; width:relative; margin-top:5; float:left;">
			<input type="hidden" id="multidowntext" style="background-color:transparent; border:0px; text-color:black; width:230px;" value="다운로드할 파일/폴더를 선택해주세요" disabled/>
		</div>
		<div style="height:100%; width:relative; margin:0; float:left;">
			<form action="/BEngineer/beFiles/beDownload.do" id="multidownform" method="post">
				<input type="hidden" name="file_ref" />
				<input type="hidden" name="file_fref" value="${folder_ref }"/>
				<input type="hidden" name="multimove_flag" value=0/>
				<input type="hidden" name="multicopy_flag" value=0/>
				<div style="height:100%; width:relative; float:left;">
					<input type="submit" name="submitmultidown" value="여러 파일 선택하기"/>
				</div>
				<div style="height:100%; width:relative; float:left;">
					<input type="hidden" id="multimove" value="이동" />
				</div>
				<div style="height:100%; width:relative; float:left;">
					<input type="hidden" id="multicopy" value="복사" />
				</div>
				<div style="height:100%; width:relative; float:left;">
					<input type="hidden" id="throwtotrashcan" value="지우기" />
				</div>
				<div style="height:100%; width:relative; float:left;">
					<input type="hidden" id="cancelmultidown" value="취소" />
				</div>
			</form>
		</div>
	</div>
</div>
<div id="button1_1" style="height:5%; width:100%; background-color:#eeee88; float:left;">
<!-- 파일/폴더 이동 폼 -->
	<div style="height:5%; width:relative; margin:0; float:left;">
		<form id="moveform" method="post" action="/BEngineer/beFiles/beMove.do">
			<input type="hidden" name="ref"/>
			<c:if test="${file_flag eq 'move' }">
				<div style="height:5%; width:relative; margin:0; float:left;">
				<input type="submit"  id="submitmove"name="submitmove" value="이동하기"/>
				<input type="hidden" name="select_flag" value="${ref }"/>
				<input type="hidden" name="folder_ref" value="${folder_ref }"/>
				</div>
				<div style="height:5%; width:relative; margin:0; float:left;">
				<input type="button"  id="movecancel" name="movecancel" value="이동 취소"/>
				</div>
			</c:if>
			<c:if test="${file_flag ne 'move' }">
				<div style="height:5%; width:relative; margin:0; float:left;">
				<input type="hidden"  id="submitmove"name="submitmove" value="이동"/>
				<input type="hidden" name="select_flag"/>
				<input type="hidden" name="folder_ref"/>
				</div>
				<div style="height:5%; width:relative; margin:0; float:left;">
				<input type="hidden"  id="movecancel" name="movecancel" value="이동 취소"/>
				</div>
			</c:if>
		</form>
	</div>
<!-- 파일/폴더 복사 폼 -->
	<div style="height:5%; width:relative; margin:0; float:left;">
		<form id="copyform" method="post" action="/BEngineer/beFiles/beCopy.do">
			<input type="hidden" name="ref"/>
			<c:if test="${file_flag eq 'copy' }">
				<div style="height:5%; width:relative; margin:0; float:left;">
				<input type="submit"  id="submitcopy"name="submitcopy" value="복사하기"/>
				<input type="hidden" name="select_flag" value="${ref }"/>
				<input type="hidden" name="folder_ref" value="${folder_ref }"/>
				</div>
				<div style="height:5%; width:relative; margin:0; float:left;">
				<input type="button"  id="copycancel" name="copycancel" value="복사 취소"/>
				</div>
			</c:if>
			<c:if test="${file_flag ne 'copy' }">
				<div style="height:5%; width:relative; margin:0; float:left;">
				<input type="hidden"  id="submitcopy"name="submitcopy" value="복사"/>
				<input type="hidden" name="select_flag"/>
				<input type="hidden" name="folder_ref"/>
				</div>
				<div style="height:5%; width:relative; margin:0; float:left;">
				<input type="hidden"  id="copycancel" name="copycancel" value="복사 취소"/>
				</div>
			</c:if>
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
				<input type="hidden" name="enddate" />
				<select name="rw" hidden style="height:25px;">
					<option value="0">보기만 허용</option>
					<option value="1">쓰기도 허용</option>
				</select>
				<input type="hidden" name="submitshare" value="공유하기"/>
			</form>
		</div>
	</div>
<!-- 폴더 다운로드 폼 -->
	<div style="height:5%; width:relative; margin:0; float:left;">
		<form id="folderdownform" method="post" action="/BEngineer/beFiles/beDownload.do">
			<input type="hidden" name="file_ref" />
			<input type="hidden" name="submitfolderdown" value="폴더 다운로드"/>
		</form>
	</div>
	<!-- 이미지보기 폼 -->
	<div style="height:5%; width:relative; margin:0; float:left;">
		<form id="imageform" method="post">
			<input type="hidden" id="imageview" name="imageview " value="이미지 보기"/>
			<input type="hidden" name="selImageview"/>
		</form>
	</div>
	<!-- 공유중인 사람 확인 폼 -->
	<div style="height:5%; width:relative; margin:0; float:left;">
		<form id="sharecheckform" action="lookSharedPeople.do" method="post">
			<input type="hidden" name="file" />
			<input type="hidden" name="submitsharecheck" value="공유 중인 사람 보기"/>
		</form>
	</div>
	<!-- 공유 해제 폼 -->
	<div style="height:5%; width:relative; margin:0; float:left; display:none" id="unsharediv">
		<div style="height:100%; width:relative; float:left;">
			<select id="unshareselect" hidden style="height:25px">
				<option value="1">모든 사람의 공유 해제</option>
				<option value="2">한 사람의 공유 해제</option>
			</select>
		</div>
		<div style="height:100%; width:relative; float:left;">
			<input type="hidden" id="unsharetext" style="top:2px; background-color:transparent; border:0px; text-color:black; width:140px; height:25px;" value="해제할 사람의 닉네임 : " disabled/>
		</div>
		<div style="height:100%; width:relative; float:left;">
			<form id="unshareform" action="/BEngineer/beFiles/unshare.do" method="get"> 
				<input type="hidden" name="nickname" />
				<input type="hidden" name="file_ref" />
				<input type="hidden" name="folder" value="${folder_ref }"/>
				<input type="submit" name="submitunshare" value="공유 해제하기"/>
			</form>
		</div>
	</div>
	<!-- 주인 바꾸기 폼 -->
	<div style="height:5%; width:relative; margin:0; float:left; display:none" id="changeownerdiv">
		<div style="height:100%; width:relative; float:left;">
			<input type="hidden" id="changeownertext" style="top:2px; background-color:transparent; border:0px; text-color:black; width:140px; height:25px;" value="넘겨줄 사람의 닉네임 : " disabled/>
		</div>
		<div style="height:100%; width:relative; float:left;">
			<form id="changeownerform" action="/BEngineer/beFiles/changeowner.do" method="get"> 
				<input type="hidden" name="nickname" />
				<input type="hidden" name="file_ref" />
				<input type="hidden" name="folder" value="${folder_ref }"/>
				<input type="submit" name="submitchangeowner" value="주인 바꾸기"/>
			</form>
		</div>
	</div>
</div>
<div id="address" style="height:5%; width:100%; background-color:#99ffff; float:left;">
	<c:set var="num" value="0" />
	<!-- 폴더경로 보여주기 -->
	<c:forEach var="addr" items="${folderaddress }">
		<c:if test="${orgaddress[num] != null }">
			<c:if test="${orgaddress[num] eq orgaddress[0] }">
				<a href="/BEngineer/beFiles/beMyList.do?folder=${orgaddress[num] }">${addr }</a> /
			</c:if>
			<c:if test="${orgaddress[num] ne orgaddress[0] }">
				<a href="/BEngineer/beFiles/beImagePreview.do?folder=${orgaddress[num] }">${addr }</a> /
			</c:if>
		</c:if>
		<c:if test="${orgaddress[num] == null }">
			${folderaddress[num] } /
		</c:if>
		<c:set var="num" value="${num + 1 }" />
	</c:forEach>
	<!-- 선택파일 보여주기용 -->
	<font id="filename"></font>
</div>
<div id="button2" style="height:75%; width:10%; background-color:#ff99ff; float:left;">
	<input type="button" id="myfile" value="내 파일"/>
	<input type="button" id="mysharedfile" value="공유 파일"/>
	<input type="button" id="mytrashcan" value="휴지통"/>
	<input type="button" id="hotlist" value="즐겨찾기"/>
	button2
	<c:if test="${gra != null && gra !=''}">
		<img src="data:image/png;base64,${gra}" style="height:50%; width:80%;" />
	</c:if>
	<c:if test="${space != null && space !=''}">
		<img src="data:image/png;base64,${space}" style="width:100%;" />
	</c:if>
</div>
<!-- 파일들 창 -->
<div id="files" style="height:75%; width:90%; background-color:#999999; float:left; overflow-y:scroll;">
	<c:forEach var="file" items="${list }">
		<div class="file" id="${file.num }" name="${file.num }" style="height:20%; width:10%; margin:1%;  box-sizing:border-box;   boxbackground-color #ff6666; float:left; overflow:hidden; "><input type="hidden" id="${file.num }orgname" value="${file.orgname }" style="border:0; background:transparent; cursor:default;" disabled/>
			<div id="navi" style="height:100%; width:100%;">		
				<a href="http://192.168.0.143/BEngineer/beFiles/beImageview.do?imageName=${file.orgname }" class="modal" onclick="return false;"><img src="http://192.168.0.143/BEngineer/beFiles/beImageview.do?imageName=${file.orgname }" style="width: 100%; height:85%;" /></a><br/>
				${file.filename }
			</div>
		</div>
		<input type="hidden" id="${file.num }type" value="${file.filetype }"/>
		<input type="hidden" id="${file.num }important" value="${file.important }"/>
	</c:forEach>
	<input type="hidden" id="file_folderref" value="${list.get(0).folder_ref }"/>
</div>
<div id="etc" style="height:10%; width:100%; background-color:#5f7f89; float:left;">
	etc
</div>
<script>
	if("${file_flag}"=="move"){
		var fileform = document.getElementById("${ref}"); // 클릭되어있는 fileform 가져오기
		var moveform = document.getElementById("moveform");
		 // moveform.ref에 fileform.ref 대입하기
		fileform.style.border="dotted"; // fileform의 테두리를 점선으로 설정
		moveform.ref.value="${ref}";
		moveform.submitmove.value="이동하기" 		// moveform.submitmove 값을 확인으로 설정
		moveform.submitmove.type="hidden"; 	// moveform.submitmove 타입을 '숨김'으로 설정
		moveform.movecancel.type="button"; 	// moveform.movecancel 타입을 button으로 설정
	}else if("${file_flag}"=="copy"){
		var fileform = document.getElementById("${ref}"); // 클릭되어있는 fileform 가져오기
		var copyform = document.getElementById("copyform");
		 // moveform.ref에 fileform.ref 대입하기
		fileform.style.border="dotted"; // fileform의 테두리를 점선으로 설정
		copyform.ref.value="${ref}";
		copyform.submitcopy.value="복사하기" 		// moveform.submitmove 값을 확인으로 설정
		copyform.movecancel.type="button"; 	// moveform.movecancel 타입을 button으로 설정	
	}else if("${file_flag}"=="multimove"){
		var moveform = document.getElementById("moveform");
		var copyform = document.getElementById("copyform");
		var ref = "${ref}";
		var refArray = ref.split(',');
		var form = document.getElementById("multidownform");
		
		moveform.submitmove.type="hidden";
		copyform.submitcopy.type="hidden";
		form.multicopy.type="hidden";
		form.file_ref.value="${ref}";
		form.submitmultidown.value = "다운로드";
		form.multimove_flag.value = 1;
		form.multicopy_flag.value = 0;
		document.getElementById("multidowntext").type = "text";
		document.getElementById("cancelmultidown").type = "button";
		document.getElementById("throwtotrashcan").type = "button";
		document.getElementById("multimove").value = "이동하기";
		document.getElementById("multimove").type = "button";
		
		for(var i = 0; i < refArray.length; i++){
			var formEx = document.getElementById(refArray[i]);
			formEx.style.border="dotted";
		}
	}else if("${file_flag}"=="multicopy"){
		var moveform = document.getElementById("moveform");
		var copyform = document.getElementById("copyform");
		var ref = "${ref}";
		var refArray = ref.split(',');
		var form = document.getElementById("multidownform");
		
		moveform.submitmove.type="hidden";
		copyform.submitcopy.type="hidden";
		form.multicopy.type="hidden";
		form.file_ref.value="${ref}";
		form.submitmultidown.value = "다운로드";
		form.multimove_flag.value = 1;
		form.multicopy_flag.value = 0;
		document.getElementById("multidowntext").type = "text";
		document.getElementById("cancelmultidown").type = "button";
		document.getElementById("throwtotrashcan").type = "button";
		document.getElementById("multicopy").value = "복사하기";
		document.getElementById("multicopy").type = "button";
		
		for(var i = 0; i < refArray.length; i++){
			var formEx = document.getElementById(refArray[i]);
			formEx.style.border="dotted";
		}
	}
</script>