$(function(){
	$("#copyform").submit(function(){ //파일/폴더 복사할 때
		var form = document.getElementById("copyform"); //copyform 받아오기
		
		//submitcopy 값이 복사일 시 복사할 파일/폴더 session으로 등록
		if(form.submitcopy.value=="복사"){ 
			window.location="/BEngineer/beFiles/beFilesession.do?folder="+folder_ref+"&ref="+form.select_flag.value+"&file_flag=copy";				
			return false;
		}
	});
});

$(function(){
	$("#copycancel").click(function(){ //파일/폴더 복사 취소 버튼 클릭시
		window.location = "/BEngineer/beFiles/beCancel.do?folder="+folder_ref;
	});
});

function initCopy(){ //복사 버튼 숨기기
	var form = document.getElementById("copyform");
	if(form != null){
		form.submitcopy.type="hidden";
	}
}

function setCopy(num){ //복사 form 설정
	var form = document.getElementById("copyform");
	if(form != null){
		form.folder_ref.value = folder_ref;
		form.select_flag.value = num;
		form.submitcopy.type = "submit";
	}
}