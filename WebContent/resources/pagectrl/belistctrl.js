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
			if(index == -1){
				clickedFile.push(ref);
				if(important.value <= -1){
					clickedImportant.push(1)
				}
				selectFile(ref);
			}else{
				clickedFile.splice(index, 1);
				if(important.value <= -1){
					clickedImportant.splice(0, 1);
				}
				disselectFile(ref);
			}
			if(clickedImportant.length > 0){
				initMultiMove();
			}else{
				setMultiMove();
				setMultiCopy();
			}
			return;
		}
		hinder();
		initFiles();
		selectFile(ref);
		setRewriteText(ref);
		setChangeName(ref, type);
		setFolderDown(ref, type);
		setShareCheck(ref);
		setShare(ref);
		setUnshare(ref);
		setCopy(ref);
		setMove(ref);
		setThrowToTrashcan(ref);
		setChangeOwner(ref);
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
			}
		}else{ // 파일일 때 해당 파일 다운로드
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
	initChangeOwner();
	initThrowToTrashcan();
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
	var text = orgname;
	$("font#filename").text(text); // 주소부분에 표시
}
function star(num){
	var boo = $('input:checkbox[id="check"]').is(":checked");	// 체크여부 체크o true , x false
	if(boo==true){
		alert("즐겨찾기 추가");
		window.location = "/BEngineer/beFiles/hot.do?num=" + num;			
	}
	if(boo==false){
		alert("즐겨찾기 헤제");
		window.location = "/BEngineer/beFiles/exhot.do?num=" + num;			
	}
}