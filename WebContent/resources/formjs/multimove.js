$(function(){
	$("#multimove").click(function(){ // 내 파일보기 버튼 클릭 시 페이지 이동
		var form = document.getElementById("multidownform"); // 폼 받아오기
		form.multicopy_flag.value = 0;
		form.multimove_flag.value = 1;
		form.file_ref.value = clickedfile.join();
	
		if($(this).val()=="이동"){
			window.location="/BEngineer/beFiles/beFilesession.do?folder="+folder_ref+"&ref="+form.file_ref.value+"&file_flag=multimove";
		}else{
			window.location = "/BEngineer/beFiles/beMultimove.do?file_fref="+form.file_fref.value;
		}
	});
});
function initMultiMove(){
	var multimove = document.getElementById("multimove");
	if(multimove != null){
		multimove.type = "hidden";
	}
}
function setMultiMove(){
	var multimove = document.getElementById("multimove");
	if(multimove != null){
		multimove.type = "button";
	}
}