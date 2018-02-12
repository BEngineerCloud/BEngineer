$(function(){
	$("#multicopy").click(function(){ // 내 파일보기 버튼 클릭 시 페이지 이동
		/*var form = document.getElementById("multidownform"); // 폼 받아오기
		form.multicopy_flag.value = 1;
		form.multimove_flag.value = 0;
		if($(this).val()=="복사"){
			form.file_ref.value = clickedfile.join();
			var ref = form.file_ref.value;
			if(ref=="") {
				alert('복사할 파일을 선택해주세요');
				return false;
			}
			var refArray = ref.split(',');
			
			for(var i = 0; i < refArray.length; i++){
				var formEx = document.getElementById(refArray[i]);
				formEx.style.border="dotted";
			}
			form.multimove.type="hidden";
		}
		if($(this).val()=="복사"){
			$(this).val("복사하기");
			return false;
		}else{
			if(form.file_fref.value=="") //이부분부터 복사하면 자기자신에 덮어씌여진다.
				form.file_fref.value = form.file_ref.value;
			window.location = "/BEngineer/beFiles/beMulticopy.do?file_ref="+form.file_ref.value+"&file_fref="+form.file_fref.value;
		}*/
		return false;
	});
});
function initMultiCopy(){
	var multicopy = document.getElementById("multicopy");
	if(multicopy != null){
		multicopy.value="복사";
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