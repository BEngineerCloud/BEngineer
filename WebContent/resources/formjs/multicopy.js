$(function(){
	$("#multicopy").click(function(){ //다수 파일/폴더 복사 버튼 클릭 시
		var form = document.getElementById("multidownform");
		
		//복사,이동을 구분해주는 플래그 변수
		form.multicopy_flag.value = 1;
		form.multimove_flag.value = 0;
		
		//선택한 파일/폴더들 multidownform ref 대입
		form.file_ref.value = clickedFile.join();
	
		if($(this).val()=="복사"){ //값이 '복사'일 경우 선택한 파일/복사들을 세션으로 등록
			window.location="/BEngineer/beFiles/beFilesession.do?folder="+folder_ref+"&ref="+form.file_ref.value+"&file_flag=multicopy";
		}else{ //'복사'가 아닐 경우 복사 하기
			window.location = "/BEngineer/beFiles/beMulticopy.do?file_fref="+form.file_fref.value;
		}
	});
});

function initMultiCopy(){ //다수 파일/폴더 복사 버튼 숨기기
	var multicopy = document.getElementById("multicopy");
	if(multicopy != null){
		multicopy.type="hidden";
	}
}

function setMultiCopy(){ //다수 파일/폴더 form 설정
	var multicopy = document.getElementById("multicopy");
	if(multicopy != null){
		multicopy.type="button";
	}
}