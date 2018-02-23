$(function(){
	$("#moveform").submit(function(){ //파일/폴더 이동할 때
		var form = document.getElementById("moveform"); //moveform 받아오기
		
		//submitmove 값이 이동일 시 이동할 파일/폴더 session으로 등록
		if(form.submitmove.value=="이동"){ 
			window.location="/BEngineer/beFiles/beFilesession.do?folder="+folder_ref+"&ref="+form.select_flag.value+"&file_flag=move";
			return false;
		}
	});
});

$(function(){
	$("#movecancel").click(function(){ //파일/폴더 이동 취소버튼 클릭 시
		window.location = "/BEngineer/beFiles/beCancel.do?folder="+folder_ref;
	});
});

function initMove(){ //이동 버튼 숨기기
	var form = document.getElementById("moveform");
	if(form != null){
		form.submitmove.type="hidden";
	}
}

function setMove(num){ //이동 form 설정
	var form = document.getElementById("moveform");
	var important = document.getElementById(num + "important");
	var index = 0;
	
	if(writeList != null){
		index = writeList.indexOf(num);
	}
	
	if(form != null && important.value > -1 && index != -1){
		form.folder_ref.value = folder_ref;
		form.select_flag.value = num;
		form.submitmove.type = "submit";
	}
}