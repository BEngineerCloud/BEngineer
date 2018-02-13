$(function(){
	$("#moveform").submit(function(){ // 파일/폴더 이동 버튼 클릭시
		var form = document.getElementById("moveform"); // moveform 받아오기, *$(this)사용
		if(form.submitmove.value=="이동"){ // submitmove 값이 이동일 시
			window.location="/BEngineer/beFiles/beFilesession.do?folder="+folder_ref+"&ref="+form.select_flag.value+"&file_flag=move";
			return false;
		}
	});
});
$(function(){
	$("#movecancel").click(function(){ // 파일/폴더 이동 취소 버튼 클릭시
		window.location = "/BEngineer/beFiles/beCancel.do?folder="+folder_ref;
	});
});
function initMove(){
	var form = document.getElementById("moveform");
	if(form != null){
		form.submitmove.type="hidden";
		form.movecancel.type="hidden";
	}
}
function setMove(num){
	var form = document.getElementById("moveform");
	var important = document.getElementById(num + "important");
	var index = 0;
	if(writeList != null){
		index = writeList.indexOf(num);
	}
	if(form != null && important.value != -1 && index != -1){
		form.folder_ref.value = folder_ref;
		form.select_flag.value = num;
		form.submitmove.type = "submit";
	}
}