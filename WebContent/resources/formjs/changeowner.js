$(function(){
	$("#changeownerform").submit(function(){ // 주인 바꾸기 버튼 클릭시
		var form = document.getElementById("changeownerform"); // 폼 받아오기
		if(form.nickname.type == "hidden"){ // 폼이 숨겨진 상태일 때 폼 보이고 이동 취소
			hinder(); // 다른 폼 닫기
			initFiles();
			showChangeOwner();
			return false;
		}
		if(!form.nickname.value.trim() && !form.nickname.value.trim() == ""){ // 업로드할 파일 미선택시
			alert('파일은 넘겨줄 상대의 닉네임을 입력해주세요');
			return false;
		}
		form.nickname.value = form.nickname.value.trim();
	});
});
function initChangeOwner(){
	var form = document.getElementById("changeownerform");
	if(form != null){
		form.nickname.type = "hidden";
		form.submitchangeowner.type = "hidden";
		form.submitchangeowner.value = "파일 주인 바꾸기";
	}
}
function setChangeOwner(num){
	var form = document.getElementById("changeownerform");
	var important = document.getElementById(num + "important");
	if(form != null && important.value > -1){
		form.file_ref.value = num;
		form.submitchangeowner.type = "submit";
	}
}
function showChangeOwner(){
	var form = document.getElementById("changeownerform");
	if(form != null){
		form.nickname.type = "text";
		form.submitchangeowner.value = "파일 보내기";
	}
}