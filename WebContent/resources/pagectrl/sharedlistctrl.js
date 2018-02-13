$(function(){
	$("#files > div").click(function(){ // 파일 클릭시
		var filename = $(this).text();
		var ref = $(this).attr("name");
		var important =document.getElementById(ref + "important");
		var orgname = document.getElementById(ref + "orgname"); // 원 파일명 저장되어있는 인풋의 값 가져오기
		var type = document.getElementById(ref + "type"); // 파일타입 저장되어있는 인풋의 값 가져오기
		setAddress(ref, orgname.value);
		var multiCheck = document.getElementById("multidowntext");
		if(multiCheck != null && multiCheck.type == "text"){
			var index = clickedFile.indexOf(ref);
			var checkwrite = writeList.indexOf(ref);
			if(index == -1){
				clickedFile.push(ref);
				if(important.value == -1 || checkwrite == -1){
					clickedImportant.push(1)
				}
				selectFile(ref);
			}else{
				clickedFile.splice(index, 1);
				if(important.value == -1 || checkwrite != -1){
					clickedImportant.splice(0, 1);
				}
				disselectFile(ref);
			}
			if(clickedImportant.size() < clickedFile.size()){
				hideMultiMove();
			}else{
				setMultiMove();
				setMultiCopy();
			}
			selectFile(ref);
			return;
		}
		hinder();
		initFiles();
		selectFile(ref);
		setRewriteText(ref);
		setChangeName(ref, type);
		setFolderDown(ref, type);
		setShareCheck(ref);
		setUnshare(ref);
		setCopy(ref);
		setMove(ref);
	});
});
$(function(){
	$("#files > div").dblclick(function(){ // 파일 더블클릭시
		var ref = $(this).attr("name");
		var type = document.getElementById(ref + "type"); // 파일타입 저장되어있는 인풋의 값 가져오기
		var important = document.getElementById(ref + "important");
		var orgname = document.getElementById(ref + "orgname"); // 원 파일명 저장되어있는 인풋의 값 가져오기
		if(type.value == "dir"){
			if(important.value==-1 && orgname.value=="image"){ 
				window.location = "/BEngineer/beFiles/beImagePreview.do?folder="+ref;	 
			}else{
				window.location = "/BEngineer/beFiles/beMyList.do?folder=" + ref; 
			}
		}else{
			window.location = "/BEngineer/beFiles/beDownload.do?file_ref=" + ref;
		}
	});
});
function hinder(){ // 모든 폼 초기화 함수
	initUpload();
	initFolderCreate();
	initMultiSel();
	initCopy();
	initMove();
	initWriteText();
	initRewriteText();
	hideWriteText();
	initChangeName();
	initFolderDown();
	initShareCheck();
	initUnshare();
}
function initFiles(){
	$("#files > div").css("background-color","#ff6666"); // 모든 파일 선택 취소
}
function initFileSel(){
	$("#files > div").css("border","none"); // 모든 파일 선택 취소
}
function selectFile(num){
	$("div[name='" + num + "']").css("background-color","#6666dd"); // 클릭파일 색 바꾸기
}
function disselectFile(num){
	$("div[name='" + num + "']").css("background-color","#ff6666"); // 클릭파일 색 바꾸기
}
function setAddress(num, orgname){
	var text = orgname + "  (";
	text = text + document.getElementById(num + "date").value + "까지 ";
	var index = writeList.indexOf(num);
	if(index == -1){
		text = text + "읽기 가능)";
	}else{
		text = text + "쓰기 가능)";
	}
	$("font#filename").text(text); // 주소부분에 표시
}