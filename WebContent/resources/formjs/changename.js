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
	$("#changenameform > input[name='name']").focus(function(){ // 폴더이름 창 클릭시 초기화
		if($(this).val() == '폴더 이름'){
			$(this).val("")
		}
		if($(this).val() == '파일 이름'){
			$(this).val("")
		}
		return;
	});
});
function initChangeName(){
	var form = document.getElementById("changenameform");
	if(form != null){
		form.name.type = "hidden";
		form.submitchangename.type = "hidden";
	}
}
function setChangeName(num, type){
	var form = document.getElementById("changenameform");
	var index = 0;
	if(writeList != null){
		index = writeList.indexOf(num);
	}
	var important = document.getElementById(num + "important");
	if(form != null && index != -1 && important.value != -1){
		if(type.value == "dir"){
			form.action = "/BEngineer/beFiles/changeFolderName.do";
			form.name.value = "폴더 이름";
			form.submitchangename.value = "폴더명 변경";
		}else{
			form.action = "/BEngineer/beFiles/changeFileName.do";
			form.name.value = "파일 이름";
			form.submitchangename.value = "파일명 변경";
		}
		form.ref.value = num;
		form.submitchangename.type = "submit";
	}
}