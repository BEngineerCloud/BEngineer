$(function(){
	$("#folderform").submit(function(){ // 폴더생성 버튼 클릭시
		var form = document.getElementById("folderform"); // 폼 받아오기
		if(form.foldername.type == "hidden"){ // 폼이 숨겨진 상태일 때 폼 보이고 이동 취소
			hinder(); // 다른 폼 닫기
			initFiles();
			showFolderCreate();
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
	$("#folderform > input[name='foldername']").focus(function(){ // 폴더이름 창 클릭시 초기화
		if($(this).val() == '폴더 이름'){
			$(this).val("")
		}
	});
});
function initFolderCreate(){
	var form = document.getElementById("folderform");
	if(form != null){
		form.foldername.type = "hidden";
		form.foldername.value = "폴더 이름";
	}
}
function showFolderCreate(){
	var form = document.getElementById("folderform");
	if(form != null){
		form.foldername.type = "text";
	}
}