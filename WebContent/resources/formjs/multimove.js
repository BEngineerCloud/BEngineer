$(function(){
	$("#multimove").click(function(){ // 내 파일보기 버튼 클릭 시 페이지 이동
		/*var form = document.getElementById("multidownform"); // 폼 받아오기
		form.multicopy_flag.value = 0;
		form.multimove_flag.value = 1;
		if($(this).val()=="이동"){
			form.file_ref.value = clickedfile.join();
			var ref = form.file_ref.value;
			if(ref=="") {
				alert('이동할 파일을 선택해주세요');
				return false;
			}
			var refArray = ref.split(',');
			
			for(var i = 0; i < refArray.length; i++){
				var formEx = document.getElementById(refArray[i]);
				formEx.style.border="dotted";
			}
			form.multicopy.type="hidden";
		}
		
		if($(this).val()=="이동"){
			$(this).val("이동하기");
			document.getElementById("multimove").type="hidden";
			return false;
		}else{
			if(form.file_fref.value==""){
				alert("이동할 폴더를 선택해주세요.");
				return false;
			}
			window.location = "/BEngineer/beFiles/beMultimove.do?file_ref="+form.file_ref.value+"&file_fref="+form.file_fref.value;
		}*/
		return false;
	});
});
function initMultiMove(){
	var multimove = document.getElementById("multimove");
	if(multimove != null){
		multimove.type = "hidden";
		multimove.value = "이동";
	}
}
function setMultiMove(){
	var multimove = document.getElementById("multimove");
	if(multimove != null){
		multimove.type = "button";
	}
}
function hideMultiMove(){
	var multimove = document.getElementById("multimove");
	if(multimove != null){
		multimove.type = "hidden";
	}
}