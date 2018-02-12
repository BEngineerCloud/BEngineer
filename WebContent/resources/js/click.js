$(function(){
	$("#files > div").click(function(){ // 파일 클릭시
		var filename = $(this).text();
		var ref = $(this).attr("name");
		var important =document.getElementById(ref + "important");
		var orgname = document.getElementById(ref + "orgname"); // 원 파일명 저장되어있는 인풋의 값 가져오기
		$("font#filename").text(orgname.value); // 주소부분에 표시
		var type = document.getElementById(ref + "type"); // 파일타입 저장되어있는 인풋의 값 가져오기
		var moveform = document.getElementById("moveform"); // moveform 가져오기
		var copyform = document.getElementById("copyform"); // copyform 가져오기
		
		if(important.value!=-1){ // 기본폴더는 important가 -1이므로 기본폴더가 아닐 시 이동버튼 나타냄
			if(moveform.submitmove.value=="이동"){ // form.submitmove 값이 이동일 시
				moveform.submitmove.type = "submit"; // form.submitmove 타입을 submit으로 설정
			}
		}
		else{ //기본 폴더일시 이동버튼 숨기기
			moveform.submitmove.type="hidden";
		}
		
		
		if(moveform.submitmove.value == "이동"){ // moveform.submitmove 값이 '이동'일 시
			moveform.select_flag.value=ref;		// movefrom.select_flag의 값에 클릭된 fileform.ref 대입
		}else if(moveform.submitmove.value == "이동하기"){ // moveform.submitmove 값이 '이동하기'일 시
			moveform.folder_ref.value=ref;			  // moveform.folder_ref 값에 클릭된 fileform.ref 대입
		}
		
		//현재 파일 이동하기 클릭 후 다른폴더를 선택했을 때 이동하기 버튼이 나오게하기 위해서
		if(moveform.folder_ref.value!=0){ 	   // moveform.folder_ref 값이 0이 아닐시
			if(type.value=="dir"&&(moveform.ref.value!=moveform.folder_ref.value)){
				moveform.submitmove.type="submit"; // moveform.submitmove 타입을 submit으로 설정
			}
			else{ // 클릭한 객체가 폴더가 아니고 파일인 경우 이동하기 버튼을 숨긴다.
				moveform.submitmove.type="hidden";
			}
		}

		if(copyform.submitcopy.value=="복사"){ // form.submitcopy 값이 복사일 시
			copyform.submitcopy.type = "submit"; // form.submitcopy 타입을 submit으로 설정
		}
		
		if(copyform.submitcopy.value == "복사"){ // copyform.submitcopy 값이 '이동'일 시
			copyform.select_flag.value=ref;		// copyfrom.select_flag의 값에 클릭된 fileform.ref 대입
		}else if(copyform.submitcopy.value == "복사하기"){ // copyform.submitcopy 값이 '이동하기'일 시
			copyform.folder_ref.value=ref;			  // copyform.folder_ref 값에 클릭된 fileform.ref 대입
		}
		
		//현재 파일 복사하기 클릭 후 다른폴더를 선택했을 때 이동하기 버튼이 나오게하기 위해서
		if(copyform.folder_ref.value!=0){ 	   // moveform.folder_ref 값이 0이 아닐시
			if(type.value=="dir"&&(copyform.ref.value!=copyform.folder_ref.value)){
				copyform.submitcopy.type="submit"; // moveform.submitmove 타입을 submit으로 설정
			}
			else{ // 클릭한 객체가 폴더가 아니고 파일인 경우 이동하기 버튼을 숨긴다.
				copyform.submitcopy.type="hidden";
			}
		}
		
		form = document.getElementById("multidownform");
		if(type.value=="dir"&&(form.multimove.value=="이동하기" || form.multicopy.value=="복사하기")){
			form.file_fref.value=ref;
		}else{
			form.file_fref.value="";
		}
		if(document.getElementById("multidowntext").type == "text"){
			var index = clickedfile.indexOf(ref);
			var flag = 0;
			moveform.submitmove.type="hidden";
			copyform.submitcopy.type="hidden";
			if(index == -1){
				clickedfile.push(ref);
				clickedImportant.push(important.value);
				$(this).css("background-color","#6666dd"); // 클릭파일 색 바꾸기
			}else{
				clickedfile.splice(index, 1);
				clickedImportant.splice(index, 1);
				$(this).css("background-color","#ff6666"); // 클릭파일 색 바꾸기
			}
			if(form.file_fref.value=="" ||(form.multimove.value=="이동" && form.multimove.type=="button")){
				for(var i=0; i<clickedImportant.length; i++){ // 클릭한 파일/폴더 중에 중요폴더가 포함됐는지 검사
					if(clickedImportant[i]==-1){
						flag=1;
					}
				}
			}
			if(flag==1){
				form.multimove.type="hidden";
				multimoveflag = 0;
				document.getElementById("throwtotrashcan").type = "hidden";
			}else{
				form.multicopy.type="button";
				form.multimove.type="button";
				document.getElementById("throwtotrashcan").type = "button";
			}
		}else{
			hinder(); // 업로드 입력, 폴더생성 입력 취소
			form.file_ref.value = ref;
			$("#files > div").css("background-color","#ff6666"); // 모든 파일 선택 취소
			$(this).css("background-color","#6666dd"); // 클릭파일 색 바꾸기
			setForm(type, ref);
			if(type.value == "dir"){
				var form = document.getElementById("folderdownform");
				form.file_ref.value = ref;
				form.submitfolderdown.type = "submit";
			}
			form = document.getElementById("sharecheckform");
			form.file.value = ref;
			form.submitsharecheck.type = "submit";
			document.getElementById("unsharediv").style.display = "block";
			document.getElementById("unshareform").file_ref.value = ref;
			if(important.value != -1){
				document.getElementById("throwtotrashcan").type = "button";
				document.getElementById("changeownerdiv").style.display = "block";
				document.getElementById("changeownerform").file_ref.value = ref;
			}
			if(type.value == ".txt"){
				form = document.getElementById("rewritetextform");
				form.filenum.value = ref;
				form.submitrewritetext.type = "submit";
			}
		}
	});
});