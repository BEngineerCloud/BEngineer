$(function(){
	$("#copyform").submit(function(){ // 파일/폴더 복사 버튼 클릭시
		var form = document.getElementById("copyform"); // copyform 받아오기, *$(this)사용
		if(form.submitcopy.value=="복사"){ // submitcopy 값이 이동일 시
			window.location="/BEngineer/beFiles/beFilesession.do?folder="+folder_ref+"&ref="+form.select_flag.value+"&file_flag=copy";				
			return false;
		}
	});
});
$(function(){
	$("#copycancel").click(function(){ // 파일/폴더 이동 취소 버튼 클릭시
		window.location = "/BEngineer/beFiles/beCancel.do?folder="+folder_ref;
	});
});
function initCopy(){
	var form = document.getElementById("copyform");
	if(form != null){
		form.submitcopy.type="hidden";
	}
}
function setCopy(num){
	var form = document.getElementById("copyform");
	if(form != null){
		form.folder_ref.value = folder_ref;
		form.select_flag.value = num;
		form.submitcopy.type = "submit";
	}
}