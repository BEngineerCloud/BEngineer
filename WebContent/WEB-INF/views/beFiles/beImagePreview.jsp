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
	var orgList = new Array(); // 파일/폴더 경로 리스트 받아오기
	<c:forEach items="${orgaddress}" var="item">
		orgList.push("${item}");
	</c:forEach>
	
	$(function(){
		$("#files > div").click(function(){ // 파일 클릭시
			var filename = $(this).text();
			var ref = $(this).attr("name");
			var orgname = document.getElementById(ref); // 원 파일명 저장되어있는 인풋의 값 가져오기
			$("font#filename").text(orgname.value); // 주소부분에 표시
			var important =document.getElementById(ref + "important");
			var type = document.getElementById(ref + "type"); // 파일타입 저장되어있는 인풋의 값 가져오기
			var moveform = document.getElementById("moveform"); // moveform 가져오기
			var copyform = document.getElementById("copyform"); // copyform 가져오기
			var imageform = document.getElementById("imageform"); //imageform 가져오기
			
			imageform.imageview.type="button";
			imageform.selImageview.value=ref;
			
			if(important.value!=-1){ // 기본폴더는 important가 -1이므로 기본폴더가 아닐 시 이동버튼 나타냄
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
			
			if(copyform.submitcopy.value=="복사"){ // form.submitcopy 값이 복사일 시
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
			}
		});
	});
	$(function(){
		$("#files > div").dblclick(function(){ // 파일 더블클릭시
			var ref = $(this).attr("name");
			var type = document.getElementById(ref + "type"); // 파일타입 저장되어있는 인풋의 값 가져오기
			var moveform = document.getElementById("moveform");
			var copyform = document.getElementById("copyform");
			var multidownform = document.getElementById("multidownform");
			
			if(type.value == "dir"){ // 폴더일 때 해당 폴더로 이동
				if(moveform.select_flag.value!=0 && moveform.folder_ref.value!=0){
					if(moveform.select_flag.value==moveform.folder_ref.value){ // 이동버튼 클릭 후 자기자신을 더블클릭할 때
						alert("이동할 폴더를 다시 선택해주세요.");
						return false;
					}
					window.location = "/BEngineer/beFiles/beMyList.do?folder=" + ref+"&movefile_Ref="+moveform.select_flag.value+"&movefile_FRef="+moveform.folder_ref.value; 
					// 파일/폴더를 선택 한 후 다른 폴더 경로로 들어갈 때 movefile_Ref, movefile_FRef에 값을 대입
				}else if(copyform.select_flag.value!=0 && copyform.folder_ref.value!=0){
					if(copyform.select_flag.value==copyform.folder_ref.value){ // 이동버튼 클릭 후 자기자신을 더블클릭할 때
						alert("복사할 폴더를 다시 선택해주세요.");
						return false;
					}
					window.location = "/BEngineer/beFiles/beCopyList.do?folder=" + ref+"&copyfile_Ref="+copyform.select_flag.value+"&copyfile_FRef="+copyform.folder_ref.value; 
					// 파일/폴더를 선택 한 후 다른 폴더 경로로 들어갈 때 copyfile_Ref, copyfile_FRef에 값을 대입
				}
				else if(multidownform.file_ref.value!="" && multidownform.file_fref.value!=0){
					var multiRef = multidownform.file_ref.value;
					var multiRefArray = multiRef.split(',');
					
					var flag = 0;
					for(var i = 0; i < multiRefArray.length; i++){
						if(multiRefArray[i]==multidownform.file_fref.value)
							flag = 1;
					}
					if(flag==1){ // 이동버튼 클릭 후 자기자신을 더블클릭할 때
						alert("이동할 폴더를 다시 선택해주세요.");
						return false;
					}
					
					if(multidownform.multimove_flag.value !=0)
						window.location = "/BEngineer/beFiles/beMultilist.do?folder=" + ref+"&multifile_Ref="+multidownform.file_ref.value+"&multifile_FRef="+multidownform.file_fref.value+"&flag="+1;
					
					else if(multidownform.multicopy_flag.value !=0)
						window.location = "/BEngineer/beFiles/beMultilist.do?folder=" + ref+"&multifile_Ref="+multidownform.file_ref.value+"&multifile_FRef="+multidownform.file_fref.value+"&flag="+2;
				}
				else
					window.location = "/BEngineer/beFiles/beMyList.do?folder=" + ref;
			}
			else // 파일일 때 해당 파일 다운로드
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
			moveform.submitmove.type="hidden";
			moveform.movecancel.type="hidden"
			moveform.submitmove.value="이동";
			copyform.submitcopy.type="hidden";
			copyform.copycancel.type="hidden"
			copyform.submitcopy.value="복사";
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
				var ref = form.file_ref.value;
				if(ref=="") {
					alert('이동할 파일을 선택해주세요');
					return false;
				}
				var refArray = ref.split(',');
				
				for(var i = 0; i < refArray.length; i++){
					var formEx = document.getElementById(refArray[i]);
					formEx.style.border="dotted";
				}
				form.multicopy.type="hidden";
			}
			
			if($(this).val()=="이동"){
				$(this).val("이동하기");
				document.getElementById("multimove").type="hidden";
				return false;
			}else{
				if(form.file_fref.value==""){
					alert("이동할 폴더를 선택해주세요.");
					return false;
				}
				window.location = "/BEngineer/beFiles/beMultimove.do?file_ref="+form.file_ref.value+"&file_fref="+form.file_fref.value;
			}
		});
	});
	$(function(){
		$("#multicopy").click(function(){ // 내 파일보기 버튼 클릭 시 페이지 이동
			var form = document.getElementById("multidownform"); // 폼 받아오기
			form.multicopy_flag.value = 1;
			form.multimove_flag.value = 0;
			if($(this).val()=="복사"){
				form.file_ref.value = clickedfile.join();
				var ref = form.file_ref.value;
				if(ref=="") {
					alert('복사할 파일을 선택해주세요');
					return false;
				}
				var refArray = ref.split(',');
				
				for(var i = 0; i < refArray.length; i++){
					var formEx = document.getElementById(refArray[i]);
					formEx.style.border="dotted";
				}
				form.multimove.type="hidden";
			}
			if($(this).val()=="복사"){
				$(this).val("복사하기");
				return false;
			}else{
				if(form.file_fref.value=="") //이부분부터 복사하면 자기자신에 덮어씌여진다.
					form.file_fref.value = form.file_ref.value;
				window.location = "/BEngineer/beFiles/beMulticopy.do?file_ref="+form.file_ref.value+"&file_fref="+form.file_fref.value;
			}
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
		$("#moveform").submit(function(){ // 파일/폴더 이동 버튼 클릭시
			var form = document.getElementById("moveform"); // moveform 받아오기, *$(this)사용
			var copyform = document.getElementById("copyform"); // moveform 받아오기, *$(this)사용
			copyform.submitcopy.type="hidden";
			if(form.submitmove.value=="이동"){ // submitmove 값이 이동일 시
				var fileform = document.getElementById(form.select_flag.value); // 클릭되어있는 fileform 가져오기
				 // moveform.ref에 fileform.ref 대입하기
				fileform.style.border="dotted"; // fileform의 테두리를 점선으로 설정
				form.ref.value=form.select_flag.value;
				form.submitmove.value="이동하기" 		// moveform.submitmove 값을 확인으로 설정
				form.submitmove.type="hidden"; 	// moveform.submitmove 타입을 '숨김'으로 설정
				form.movecancel.type="button"; 	// moveform.movecancel 타입을 button으로 설정				
				return false;
			}
			if(form.submitmove.value=="이동하기"){
				// 다른 폴더로 이동한 후에 처음에 선택한 파일/폴더의 ref 값이 없을 시
				if(form.ref.value==0){
					form.ref.value=form.select_flag.value;
				}
			}
		});
	});
	$(function(){
		$("#copyform").submit(function(){ // 파일/폴더 복사 버튼 클릭시
			var form = document.getElementById("copyform"); // copyform 받아오기, *$(this)사용
			var moveform = document.getElementById("moveform"); // moveform 받아오기, *$(this)사용
			moveform.submitmove.type="hidden";
			if(form.submitcopy.value=="복사"){ // submitcopy 값이 이동일 시
				var fileform = document.getElementById(form.select_flag.value); // 클릭되어있는 fileform 가져오기
				 // copyform.ref에 fileform.ref 대입하기
				fileform.style.border="dotted"; // fileform의 테두리를 점선으로 설정
				form.ref.value=form.select_flag.value;
				form.submitcopy.value="복사하기" 		// copyform.submitcopy 값을 확인으로 설정
				//form.submitcopy.type="hidden"; 	// copyform.submitmove 타입을 '숨김'으로 설정
				form.copycancel.type="button"; 	// copyform.copycancel 타입을 button으로 설정				
				return false;
			}
			if(form.submitcopy.value=="복사하기"){
				// 다른 폴더로 이동한 후에 처음에 선택한 파일/폴더의 ref 값이 없을 시
				if(form.ref.value==0){
					form.ref.value=form.select_flag.value;	
				}
				if(form.folder_ref.value==0) //이부분부터 복사하면 자기자신에 덮어씌여진다.
					form.folder_ref.value = form.select_flag.value;
			}
		});
	});
	$(function(){
		$("#movecancel").click(function(){ // 파일/폴더 이동 취소 버튼 클릭시
			var form = document.getElementById("moveform"); // moveform 받아오기, *$(this)사용
			if(form.ref.value!=0){ 
				var fileform = document.getElementById(form.ref.value); // 클릭되어있는 fileform 가져오기
				fileform.style.border="none"; //fileform의 테두리를 '없음'으로 설정
				fileform.style.backgroundColor="#ff6666";
				
				if(form.folder_ref.value!=0){ // 이동버튼을 클릭한 뒤 다른 파일 클릭한 후 취소버튼 클릭 시
					var fileform2 = document.getElementById(form.folder_ref.value);
					fileform2.style.backgroundColor="#ff6666";
				}	
			}
			form.select_flag.value="";
			form.ref.value=""; 				// moveform.ref 값을 빈칸으로 설정
			form.folder_ref.value=""; 		// moveform.folder_ref 값을 빈칸으로 설정
			form.submitmove.value="이동" // moveform.submitmove 값을 이동으로 설정 		
			form.submitmove.type="hidden"; 	// moveform.subitmove 타입을 submit으로 설정
			form.movecancel.type="hidden"; 	//moveform.movecancel 타입을 '숨김'으로 설정
			moveDotted();
			return false;
		});
	});
	$(function(){
		$("#copycancel").click(function(){ // 파일/폴더 이동 취소 버튼 클릭시
			var form = document.getElementById("copyform"); // moveform 받아오기, *$(this)사용
			if(form.ref.value!=0){ 
				var fileform = document.getElementById(form.ref.value); // 클릭되어있는 fileform 가져오기
				fileform.style.border="none"; //fileform의 테두리를 '없음'으로 설정
				fileform.style.backgroundColor="#ff6666";
				
				if(form.folder_ref.value!=0){ // 이동버튼을 클릭한 뒤 다른 파일 클릭한 후 취소버튼 클릭 시
					var fileform2 = document.getElementById(form.folder_ref.value);
					fileform2.style.backgroundColor="#ff6666";
				}	
			}
			form.select_flag.value="";
			form.ref.value=""; 				// moveform.ref 값을 빈칸으로 설정
			form.folder_ref.value=""; 		// moveform.folder_ref 값을 빈칸으로 설정
			form.submitcopy.value="복사" // moveform.submitmove 값을 이동으로 설정 		
			form.submitcopy.type="hidden"; 	// moveform.subitmove 타입을 submit으로 설정
			form.copycancel.type="hidden"; 	//moveform.movecancel 타입을 '숨김'으로 설정
			copyDotted();
			return false;
		});
	});
	$(function(){
		$("#uppermovesubmit").click(function(){ // 상위폴더 이동 버큰 클릭시
			window.location = "/BEngineer/beFiles/beMyList.do?folder=" + 0;	
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
	function multiSelect(){
		var multifile_Ref = "${multifile_Ref}";
		var multifile_FRef = "${multifile_FRef}";
		var multifile_CRef = "${multifile_CRef}";
		var multifile_CFRef = "${multifile_CFRef}"; 
		multifile_FRef = Number(multifile_FRef);
		multifile_CFRef = Number(multifile_CFRef);
		
		if(multifile_Ref!="" || multifile_FRef!=0){
			var multiform = document.getElementById("multidownform");
			multiform.submitmultidown.value = "다운로드";
			multiform.file_ref.value=multifile_Ref;
			multiform.file_fref.value=multifile_FRef;
			multiform.multimove_flag.value = 1;
			multiform.multicopy_flag.value = 0;
			document.getElementById("multidowntext").type = "text";
			document.getElementById("cancelmultidown").type = "button";
			document.getElementById("throwtotrashcan").type = "button";
			document.getElementById("multimove").value = "이동하기";
			document.getElementById("multimove").type = "button";
		}
		
		if(multifile_Ref!=""){
			var multiArray = multifile_Ref.split(',');
			for(var i = 0; i < multiArray.length; i++){
				document.getElementById(multiArray[i]).style.border="dotted";
			}
		}
		
		if(multifile_CRef!="" || multifile_CFRef!=0){
			var multiform = document.getElementById("multidownform");
			multiform.submitmultidown.value = "다운로드";
			multiform.file_ref.value=multifile_CRef;
			multiform.file_fref.value=multifile_CFRef;
			multiform.multicopy_flag.value = 1;
			multiform.multimove_flag.value = 0;
			document.getElementById("multidowntext").type = "text";
			document.getElementById("cancelmultidown").type = "button";
			document.getElementById("throwtotrashcan").type = "button";
			document.getElementById("multicopy").value = "복사하기";
			document.getElementById("multicopy").type = "button";
		}
		
		if(multifile_CRef!=""){
			var multiArray = multifile_CRef.split(',');
			for(var i = 0; i < multiArray.length; i++){
				document.getElementById(multiArray[i]).style.border="dotted";
			}
		}
	}
	
	function moveDotted(){ // 클릭한 파일/폴더 점선 유지
		var movefile_Ref = "${movefile_Ref}";
		var moveform = document.getElementById("moveform");
		
		if(movefile_Ref!=0 && moveform.submitmove.value=="이동하기"){
			var fileform = document.getElementById(movefile_Ref);
			fileform.style.border="dotted";
		}else{
			var fileform = document.getElementById(movefile_Ref);
			fileform.style.border="none";
		}
	}
	
	function copyDotted(){ // 클릭한 파일/폴더 점선 유지
		var copyfile_Ref = "${copyfile_Ref}";
		var copyform = document.getElementById("copyform");

		if(copyfile_Ref!=0 && copyform.submitcopy.value=="복사하기"){
			var fileform2 = document.getElementById(copyfile_Ref);
			fileform2.style.border="dotted";
		}else{
			var fileform2 = document.getElementById(copyfile_Ref);
			fileform2.style.border="none";
		}
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
				<input type="hidden" name="file_fref"/>
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
			<c:if test="${movefile_Ref!=0 || movefile_FRef!=0 }">
				<div style="height:5%; width:relative; margin:0; float:left;">
				<input type="submit"  id="submitmove"name="submitmove" value="이동하기"/>
				<input type="hidden" name="select_flag" value="${movefile_Ref }"/>
				<c:if test="${movefile_FRef!=0 }">
				<input type="hidden" name="folder_ref" value="${movefile_FRef }"/>
				</c:if>
				<c:if test="${movefile_FRef==0 }">
				<input type="hidden" name="folder_ref" value="${folder }"/>
				</c:if>
				</div>
				<div style="height:5%; width:relative; margin:0; float:left;">
				<input type="button"  id="movecancel" name="movecancel" value="이동 취소"/>
				</div>
			</c:if>
			<c:if test="${movefile_Ref==0 && movefile_FRef==0 }">
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
			<c:if test="${copyfile_Ref!=0 || copyfile_FRef!=0 }">
				<div style="height:5%; width:relative; margin:0; float:left;">
				<input type="submit"  id="submitcopy"name="submitcopy" value="복사하기"/>
				<input type="hidden" name="select_flag" value="${copyfile_Ref }"/>
				<c:if test="${copyfile_FRef!=0 }">
				<input type="hidden" name="folder_ref" value="${copyfile_FRef }"/>
				</c:if>
				<c:if test="${copyfile_FRef==0 }">
				<input type="hidden" name="folder_ref" value="${folder }"/>
				</c:if>
				</div>
				<div style="height:5%; width:relative; margin:0; float:left;">
				<input type="button"  id="copycancel" name="copycancel" value="복사 취소"/>
				</div>
			</c:if>
			<c:if test="${copyfile_Ref==0 && copyfile_FRef==0 }">
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
<!-- 상위폴더 이동 폼 -->
	<div style="height:5%; width:relative; margin:0; float:left;">
		<form id="uppermoveform" method="post">
			<input type="hidden" id="folder_ref" value="${folder_ref }"/>
			<c:if test="${folder!=0 && orgaddress.size()>1}">
				<input type="button" id="uppermovesubmit" name="uppermovesubmit" value="상위 폴더 이동"/>
			</c:if>
			<c:if test="${folder==0 || orgaddress.size()==1}">
				<input type="hidden" id="uppermovesubmit" name="uppermovesubmit" value="상위 폴더 이동"/>
			</c:if>
		</form>
	</div>
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
<div id="button2" style="height:75%; width:10%; background-color:#ff99ff; float:left;">
	<input type="button" id="myfile" value="내 파일"/>
	<input type="button" id="mysharedfile" value="공유 파일"/>
	<input type="button" id="mytrashcan" value="휴지통"/>
	<input type="button" id="hotlist" value="즐겨찾기"/>
	button2
	<c:if test="${gra != null && gra !=''}">
		<img src="data:image/png;base64,${gra}" style="height:50%; width:80%;" />
	</c:if>
</div>
<!-- 파일들 창 -->
<div id="files" style="height:75%; width:90%; background-color:#999999; float:left; overflow-y:scroll;">
	<c:forEach var="file" items="${list }">
		<div class="file" id="${file.num }" name="${file.num }" style="height:20%; width:10%; margin:1%; background-color:#ff6666; float:left; overflow:hidden"><input type="text" id="${file.num }" value="${file.orgname }" style="border:0; background:transparent; cursor:default;" disabled/>
			<div id="navi" style="height:100%; width:100%;">		
				<a href="http://192.168.0.143/BEngineer/beFiles/beImageview.do?imageName=${file.orgname }" class="modal" onclick="return false;"><img src="http://192.168.0.143/BEngineer/beFiles/beImageview.do?imageName=${file.orgname }" style="width: 100%; height:89%;""alt="샹제리아" /></a>
			</div>
		</div>
		<input type="hidden" id="${file.num }type" value="${file.filetype }"/>
		<input type="hidden" id="${file.num }important" value="${file.important }"/>
	</c:forEach>
</div>
<div id="etc" style="height:10%; width:100%; background-color:#5f7f89; float:left;">
	etc
</div>