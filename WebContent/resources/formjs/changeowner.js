$(function(){
	$("#changeownerform").submit(function(){ // 주인 바꾸기 버튼 클릭시
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
function initChangeOwner(){
	var form = document.getElementById("changeownerform");
	if(form != null){
		form.filename.value = "";
		form.orgname.value = "";
		form.content.value = "";
		form.orgname.readOnly = false;
	}
}
function setChangeOwner(num){
	var form = document.getElementById("changeownerform");
	var index = 0;
	if(writeList != null){
		index = writeList.indexOf(num);
	}
	var filetype = document.getElementById(num + "type");
	if(form != null && index != -1 && filetype.value == ".txt"){
		form.filenum.value = num;
		form.submitrewritetext.type = "submit";
	}
}