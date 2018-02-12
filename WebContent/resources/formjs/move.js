$(function(){
	$("#moveform").submit(function(){ // 파일/폴더 이동 버튼 클릭시
		/*var form = document.getElementById("moveform"); // moveform 받아오기, *$(this)사용
		var copyform = document.getElementById("copyform"); // moveform 받아오기, *$(this)사용
		copyform.submitcopy.type="hidden";
		if(form.submitmove.value=="이동"){ // submitmove 값이 이동일 시
			var fileform = document.getElementById(form.select_flag.value); // 클릭되어있는 fileform 가져오기
			 // moveform.ref에 fileform.ref 대입하기
			fileform.style.border="dotted"; // fileform의 테두리를 점선으로 설정
			form.ref.value=form.select_flag.value;
			form.submitmove.value="이동하기" 		// moveform.submitmove 값을 확인으로 설정
			form.submitmove.type="hidden"; 	// moveform.submitmove 타입을 '숨김'으로 설정
			form.movecancel.type="button"; 	// moveform.movecancel 타입을 button으로 설정				
			return false;
		}
		if(form.submitmove.value=="이동하기"){
			// 다른 폴더로 이동한 후에 처음에 선택한 파일/폴더의 ref 값이 없을 시
			if(form.ref.value==0){
				form.ref.value=form.select_flag.value;
			}
		}*/
		return false;
	});
});
$(function(){
	$("#movecancel").click(function(){ // 파일/폴더 이동 취소 버튼 클릭시
		/*var form = document.getElementById("moveform"); // moveform 받아오기, *$(this)사용
		if(form.ref.value!=0){ 
			var fileform = document.getElementById(form.ref.value); // 클릭되어있는 fileform 가져오기
			fileform.style.border="none"; //fileform의 테두리를 '없음'으로 설정
			fileform.style.backgroundColor="#ff6666";
			
			if(form.folder_ref.value!=0){ // 이동버튼을 클릭한 뒤 다른 파일 클릭한 후 취소버튼 클릭 시
				var fileform2 = document.getElementById(form.folder_ref.value);
				fileform2.style.backgroundColor="#ff6666";
			}	
		}
		form.select_flag.value="";
		form.ref.value=""; 				// moveform.ref 값을 빈칸으로 설정
		form.folder_ref.value=""; 		// moveform.folder_ref 값을 빈칸으로 설정
		form.submitmove.value="이동" // moveform.submitmove 값을 이동으로 설정 		
		form.submitmove.type="hidden"; 	// moveform.subitmove 타입을 submit으로 설정
		form.movecancel.type="hidden"; 	//moveform.movecancel 타입을 '숨김'으로 설정
		moveDotted();*/
		return false;
	});
});
function initMove(){
	var form = document.getElementById("moveform");
	if(form != null){
		form.submitmove.type="hidden";
		form.movecancel.type="hidden"
		form.submitmove.value="이동";
	}
}
function setMove(){
	var form = document.getElementById("moveform");
	if(form != null){
	}
}