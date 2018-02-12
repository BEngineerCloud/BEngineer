$(function(){
	$("#uploadform").submit(function(){ // 업로드 버튼 클릭시
		var form = document.getElementById("uploadform"); // 폼 받아오기
		if(form.save.type == "hidden"){ // 폼이 숨겨진 상태일 때 폼 보이고 이동 취소
			hinder(); // 다른 폼 닫기
			initFiles();
			setUpload();
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
	$("#uploadform > input[name='filename']").focus(function(){ // 파일이름 창 클릭시 초기화
		if($(this).val() == '파일 이름'){
			$(this).val("")
		}
	});
});
function initUpload(){
	var form = document.getElementById("uploadform");
	if(form != null){
		form.save.type = "hidden";
		form.filename.type = "hidden";
		form.filename.value = "파일 이름";
	}
}
function setUpload(){
	var form = document.getElementById("uploadform");
	if(form != null){
		form.save.type = "file";
		form.filename.type = "text";
	}
}