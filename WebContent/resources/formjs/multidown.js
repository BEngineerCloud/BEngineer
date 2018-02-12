$(function(){
	$("#multidownform").submit(function(){ // 여러 파일 다운로드 버튼 클릭시
		var form = document.getElementById("multidownform"); // 폼 받아오기
		initMove();
		initCopy();
		if(document.getElementById("multidowntext").type == "hidden"){ // 폼이 숨겨진 상태일 때 폼 보이고 이동 취소
			hinder(); // 다른 폼 닫기
			initFiles();
			showMultiSel();
			return false;
		}
		form.file_ref.value = clickedfile.join();
		if(!form.file_ref.value){ // 업로드할 파일 미선택시
			alert('업로드할 파일을 선택해주세요');
			return false;
		}
	});
});
$(function(){
	$("#cancelmultidown").click(function(){ // 다중선택 취소시
		initMultiSel();
		initFiles();
		initFileSel();
	});
});
function initMultiSel(){
	var form = document.getElementById("multidownform");
	clickedfile = new Array();
	clickedImportant = new Array();
	if(form != null){
		form.file_ref.value = "";
		form.file_fref.value = "";
		form.multimove_flag.value = 0;
		form.multicopy_flag.value = 0;
		form.submitmultidown.value = "여러 파일 선택하기";
		document.getElementById("multidowntext").type = "hidden";
		document.getElementById("cancelmultidown").type = "hidden";
		initMultiMove();
		initMultiCopy();
	}
}
function showMultiSel(){
	var form = document.getElementById("multidownform");
	if(form != null){
		document.getElementById("multidowntext").type = "text";
		document.getElementById("cancelmultidown").type = "button";
		setMultiMove();
		setMultiCopy();
		form.submitmultidown.value = "다운로드";
	}
}