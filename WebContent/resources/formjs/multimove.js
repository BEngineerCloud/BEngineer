$(function(){
	$("#multimove").click(function(){ //다수 파일/폴더 이동 버튼 클릭 시
		var form = document.getElementById("multidownform"); 
		
		//복사,이동을 구분해주는 플래그 변수
		form.multicopy_flag.value = 0;
		form.multimove_flag.value = 1;
		
		//선택한 파일/폴더들 multidownform ref 대입
		form.file_ref.value = clickedFile.join();
	
		if($(this).val()=="이동"){ //값이 '이동'일 경우 선택한 파일/복사들을 세션으로 등록
			window.location="/BEngineer/beFiles/beFilesession.do?folder="+folder_ref+"&ref="+form.file_ref.value+"&file_flag=multimove";
		}else{ //'이동'이 아닐 경우 이동 하기
			window.location = "/BEngineer/beFiles/beMultimove.do?file_fref="+form.file_fref.value;
		}
	});
});

function initMultiMove(){ //다수 파일/폴더 이동 버튼 숨기기
	var multimove = document.getElementById("multimove");
	if(multimove != null){
		multimove.type = "hidden";
	}
}

function setMultiMove(){ //다수 파일/폴더 form 설정
	var multimove = document.getElementById("multimove");
	if(multimove != null){
		multimove.type = "button";
	}
}