$(function(){
	$("#files > div").click(function(){ // 파일 클릭시
		var filename = $(this).text();
		var ref = $(this).attr("name");
		var important =document.getElementById(ref + "important");
		var orgname = document.getElementById(ref + "orgname"); // 원 파일명 저장되어있는 인풋의 값 가져오기
		$("font#filename").text(orgname.value); // 주소부분에 표시
		var type = document.getElementById(ref + "type"); // 파일타입 저장되어있는 인풋의 값 가져오기
		var moveform = document.getElementById("moveform"); // moveform 가져오기
		var copyform = document.getElementById("copyform"); // copyform 가져오기
		
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
			if(index == -1){
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
				document.getElementById("throwtotrashcan").type = "hidden";
			}else{
				form.multicopy.type="button";
				form.multimove.type="button";
				document.getElementById("throwtotrashcan").type = "button";
			}
		}else{
			hinder(); // 업로드 입력, 폴더생성 입력 취소
			form.file_ref.value = ref;
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
		var type = document.getElementById(ref + "type"); // 파일타입 저장되어있는 인풋의 값 가져오기
		var important =document.getElementById(ref + "important");
		var orgname = document.getElementById(ref + "orgname"); // 원 파일명 저장되어있는 인풋의 값 가져오기
		if(type.value == "dir"){ // 폴더일 때 해당 폴더로 이동
			if(important.value==-1 && orgname.value=="image"){ 
				window.location = "/BEngineer/beFiles/beImagePreview.do?folder="+ref;	 
			}else{
				window.location = "/BEngineer/beFiles/beMyList.do?folder=" + ref; 
				// 파일/폴더를 선택 한 후 다른 폴더 경로로 들어갈 때 movefile_Ref, movefile_FRef에 값을 대입
			}
		}
		else // 파일일 때 해당 파일 다운로드
			window.location = "/BEngineer/beFiles/beDownload.do?file_ref=" + ref;
	});
});
$(function(){
	$("#cancelmultidown").click(function(){ // 다중선택 취소시
		window.location = "/BEngineer/beFiles/beCancel.do?folder="+${folder_ref};
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
	$("#unshareselect").change(function(){
		var form = document.getElementById("unshareform"); // 폼 받아오기
		var select = document.getElementById("unshareselect");
		if(select.value == 1){
			document.getElementById("unsharetext").type = "hidden";
			form.nickname.type = "hidden";
		}else{
			document.getElementById("unsharetext").type = "text";
			form.nickname.type = "text";
		}
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
		form.file_ref.value = clickedfile.join();
		var moveform = document.getElementById("moveform")
		var copyform = document.getElementById("copyform");
		moveform.submitmove.type="hidden";
		moveform.movecancel.type="hidden"
		moveform.submitmove.value="이동";
		copyform.submitcopy.type="hidden";
		copyform.copycancel.type="hidden"
		copyform.submitcopy.value="복사";
		if(document.getElementById("multidowntext").type == "hidden"){ // 폼이 숨겨진 상태일 때 폼 보이고 이동 취소
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
			alert(1);
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
		form.file_ref.value = clickedfile.join();
	
		if($(this).val()=="이동"){
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
			window.location="/BEngineer/beFiles/beFilesession.do?folder="+${folder_ref}+"&ref="+form.file_ref.value+"&file_flag=multicopy";
		}else{
			window.location = "/BEngineer/beFiles/beMulticopy.do?file_fref="+form.file_fref.value;
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
	$("#writetextform").submit(function(){ // 텍스트 쓰기 버튼 클릭시
		var form = document.getElementById("writetextform"); // 폼 받아오기
		if(!form.filename.value.trim() && !form.filename.value.trim() == ""){ // 업로드할 파일 미선택시
			alert('파일 별명을 입력해주세요');
			return false;
		}
		if(!form.orgname.value.trim() && !form.orgname.value.trim() == ""){ // 업로드할 파일 미선택시
			alert('파일명을 입력해주세요');
			return false;
		}
		var orgname = form.orgname.value.trim() + ".txt";
		var check = $("input[value='" + orgname + "']").attr("type");
		if(check == "text"){
			if(!confirm('파일을 덮어 쓰시겠습니까?')){
				return false;
			}
		}
		if(!form.content.value.trim() && !form.content.value.trim() == ""){ // 업로드할 파일 미선택시
			alert('파일명을 입력해주세요');
			return false;
		}
		form.filename.value = form.filename.value.trim();
		form.orgname.value = form.orgname.value.trim();
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
	document.getElementById("writetextdiv").style.display = "none";
	form = document.getElementById("writetextform");
	form.filename.value = "";
	form.orgname.value = "";
	form.orgname.readOnly = false;
	form.content.value = "";
	document.getElementById("unsharediv").style.display = "none";
	document.getElementById("unshareselect").hidden = true;
	document.getElementById("unshareselect").value = "1";
	document.getElementById("unsharetext").type = "hidden";
	document.getElementById("unshareform").nickname.type = "hidden";
	document.getElementById("changeownerdiv").style.display = "none";
	document.getElementById("changeownerform").nickname.type = "hidden";
	form = document.getElementById("rewritetextform");
	form.submitrewritetext.type = "hidden";
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
		window.location = "/BEngineer/beFiles/hotlist.do";
	});
});
//초성검색 @@@@@@@@@@
var font_cho = Array(
'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ',
'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ',
'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ' );
var font_jung = Array(
'ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ',
'ㅔ', 'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ', 'ㅙ',
'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ',
'ㅠ', 'ㅡ', 'ㅢ', 'ㅣ' );
var font_jong = Array(
'', 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ', 'ㄹ',
'ㄺ', 'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ', 'ㅁ',
'ㅂ', 'ㅄ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ' );
//테스트 시작
$(function(){
	$("#search").click(function(){
		var inputStr = document.getElementById("searchword").value;
		var result ="";
		//입력된 문자열 길이만큼 반복 - [1]반복문
		for(k = 0; k < inputStr.length; k++){
			var inputStr2 = inputStr.substring(k, k+1);			//입력한 단어 글자 단위로 나눠 담기
			var inputCho = searchCho(inputStr2.charCodeAt(0));	//입력한 단어 초성 나누기	
			var forLength = 0;	
			var checkArr = result.split(",");	// 조회된결과를 배열로 나눔
			var arrStr = "";
			//최초 조회시... 
			if(result == "" && k == 0){
				forLength = font_test.length;
			//두번째 조회 부터...
			}else{
				forLength = checkArr.length;
				result = "";
			}
			// 비교대상 배열의 길이만큼 반복 - [2]반복문
			for(i = 0 ; i < forLength ; i++){	
				//최초 조회시... 
				if(k == 0){
					arrStr = font_test[i];
				//두번째 조회 부터...
				}else{
					arrStr = checkArr[i];
				}
				//배열 값의 길이만큼 반복 - [3]반복문 
				//단, j는 [1]반복문의 현재값으로 초기화 
				for(j =  k; j < arrStr.length ; j++){
					//이전 검색된 문자
					var beforeStr = arrStr.charCodeAt(j);
					var beforeCho = searchCho(arrStr.charCodeAt(j));
					var beforeInput = inputStr2;
					if(k > 0){
						beforeStr = arrStr.charCodeAt(j-1);
						beforeCho = searchCho(arrStr.charCodeAt(j-1));	
						beforeInput = inputStr.substring(k-1, k);
					}				
					//한글이면
					if(escape(inputStr2.charCodeAt(0)).length > 4  && result.indexOf(arrStr) < 0 ){
						var Cho = searchCho(arrStr.charCodeAt(j));	//조회 대상 배열의 값 초성 나누기	
						//초성만 입력한 경우이면..
						if(inputCho >= 0){
							if(arrStr.charCodeAt(j) == inputStr2.charCodeAt(0)){
								if(font_cho[beforeCho] == beforeInput ||  beforeStr == beforeInput.charCodeAt(0)){
									result += arrStr + ",";
								}
							}
						//초성인 경우...
						}else{
							if(font_cho[Cho] == inputStr2){
								if(font_cho[beforeCho] == beforeInput ||  beforeStr == beforeInput.charCodeAt(0)){
									result += arrStr + ",";
								}
							}
						}		
					//영어면
					}else{
						//대문자로 변환뒤 비교
						if(inputStr2.toUpperCase().charCodeAt(0) == arrStr.toUpperCase().charCodeAt(j)){
							if(result.indexOf(arrStr) < 0 ){
								result += arrStr + ",";	
							}
						}
					}
				} //[3]반복문 종
			}//[2]반복문 종료
		}//[1]반복문 종료
		if(result == ""){
			result = "검색된 단어가 없습니다.";
		}
		alert(result)
		window.location = "/BEngineer/beFiles/searchForm.do?result=" + result; 
		//alert("검색결과  : " + result);
	 });
	
});
// 초성 나누기 return : 초성 배열 index
function searchCho(str){
	CompleteCode = str;
	UniValue = CompleteCode - 0xAC00;
	var Jong = UniValue % 28;
	var Jung = ( ( UniValue - Jong ) / 28 ) % 21;
	var Cho = parseInt((( UniValue - Jong ) / 28 ) / 21);
	return Cho;
}