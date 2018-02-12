$(function(){
	$("#copyform").submit(function(){ // 파일/폴더 복사 버튼 클릭시
		/*var form = document.getElementById("copyform"); // copyform 받아오기, *$(this)사용
		var moveform = document.getElementById("moveform"); // moveform 받아오기, *$(this)사용
		moveform.submitmove.type="hidden";
		if(form.submitcopy.value=="복사"){ // submitcopy 값이 이동일 시
			var fileform = document.getElementById(form.select_flag.value); // 클릭되어있는 fileform 가져오기
			 // copyform.ref에 fileform.ref 대입하기
			fileform.style.border="dotted"; // fileform의 테두리를 점선으로 설정
			form.ref.value=form.select_flag.value;
			form.submitcopy.value="복사하기" 		// copyform.submitcopy 값을 확인으로 설정
			//form.submitcopy.type="hidden"; 	// copyform.submitmove 타입을 '숨김'으로 설정
			form.copycancel.type="button"; 	// copyform.copycancel 타입을 button으로 설정				
			return false;
		}
		if(form.submitcopy.value=="복사하기"){
			// 다른 폴더로 이동한 후에 처음에 선택한 파일/폴더의 ref 값이 없을 시
			if(form.ref.value==0){
				form.ref.value=form.select_flag.value;	
			}
			if(form.folder_ref.value==0) //이부분부터 복사하면 자기자신에 덮어씌여진다.
				form.folder_ref.value = form.select_flag.value;
		}*/
		return false;
	});
});
$(function(){
	$("#copycancel").click(function(){ // 파일/폴더 이동 취소 버튼 클릭시
		/*var form = document.getElementById("copyform"); // moveform 받아오기, *$(this)사용
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
		form.submitcopy.value="복사" // moveform.submitmove 값을 이동으로 설정 		
		form.submitcopy.type="hidden"; 	// moveform.subitmove 타입을 submit으로 설정
		form.copycancel.type="hidden"; 	//moveform.movecancel 타입을 '숨김'으로 설정
		copyDotted();*/
		return false;
	});
});
function initCopy(){
	var form = document.getElementById("copyform");
	if(form != null){
		form.submitcopy.type="hidden";
		form.copycancel.type="hidden"
		form.submitcopy.value="복사";
	}
}
function setCopy(){
	var form = document.getElementById("copyform");
	if(form != null){
	}
}