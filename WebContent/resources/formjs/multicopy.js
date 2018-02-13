$(function(){
	$("#multicopy").click(function(){ // 내 파일보기 버튼 클릭 시 페이지 이동
		var form = document.getElementById("multidownform"); // 폼 받아오기
		form.multicopy_flag.value = 1;
		form.multimove_flag.value = 0;
		form.file_ref.value = clickedfile.join();
	
		if($(this).val()=="복사"){
			window.location="/BEngineer/beFiles/beFilesession.do?folder="+folder_ref+"&ref="+form.file_ref.value+"&file_flag=multicopy";
		}else{
			window.location = "/BEngineer/beFiles/beMulticopy.do?file_fref="+form.file_fref.value;
		}
	});
});
function initMultiCopy(){
	var multicopy = document.getElementById("multicopy");
	if(multicopy != null){
		multicopy.type="hidden";
	}
}
function setMultiCopy(){
	var multicopy = document.getElementById("multicopy");
	if(multicopy != null){
		multicopy.type="button";
	}
}
function hideMultiCopy(){
	var multicopy = document.getElementById("multicopy");
	if(multicopy != null){
		multicopy.type="hidden";
	}
}