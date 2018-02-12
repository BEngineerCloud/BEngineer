	//초성검색 @@@@@@@@@@
	var font_cho = Array(
	'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ',
	'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ',
	'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ' );
	var font_jung = Array(
	'ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ',
	'ㅔ', 'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ', 'ㅙ',
	'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ',
	'ㅠ', 'ㅡ', 'ㅢ', 'ㅣ' );
	var font_jong = Array(
	'', 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ', 'ㄹ',
	'ㄺ', 'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ', 'ㅁ',
	'ㅂ', 'ㅄ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ' );
	//테스트 시작
	$(function(){
		$("#search").click(function(){
			var inputStr = document.getElementById("searchword").value;
			var result ="";
			//입력된 문자열 길이만큼 반복 - [1]반복문
			for(k = 0; k < inputStr.length; k++){
				var inputStr2 = inputStr.substring(k, k+1);			//입력한 단어 글자 단위로 나눠 담기
				var inputCho = searchCho(inputStr2.charCodeAt(0));	//입력한 단어 초성 나누기	
				var forLength = 0;	
				var checkArr = result.split(",");	// 조회된결과를 배열로 나눔
				var arrStr = "";
				//최초 조회시... 
				if(result == "" && k == 0){
					forLength = font_test.length;
				//두번째 조회 부터...
				}else{
					forLength = checkArr.length;
					result = "";
				}
				// 비교대상 배열의 길이만큼 반복 - [2]반복문
				for(i = 0 ; i < forLength ; i++){	
					//최초 조회시... 
					if(k == 0){
						arrStr = font_test[i];
					//두번째 조회 부터...
					}else{
						arrStr = checkArr[i];
					}
					//배열 값의 길이만큼 반복 - [3]반복문 
					//단, j는 [1]반복문의 현재값으로 초기화 
					for(j =  k; j < arrStr.length ; j++){
						//이전 검색된 문자
						var beforeStr = arrStr.charCodeAt(j);
						var beforeCho = searchCho(arrStr.charCodeAt(j));
						var beforeInput = inputStr2;
						if(k > 0){
							beforeStr = arrStr.charCodeAt(j-1);
							beforeCho = searchCho(arrStr.charCodeAt(j-1));	
							beforeInput = inputStr.substring(k-1, k);
						}				
						//한글이면
						if(escape(inputStr2.charCodeAt(0)).length > 4  && result.indexOf(arrStr) < 0 ){
							var Cho = searchCho(arrStr.charCodeAt(j));	//조회 대상 배열의 값 초성 나누기	
							//초성만 입력한 경우이면..
							if(inputCho >= 0){
								if(arrStr.charCodeAt(j) == inputStr2.charCodeAt(0)){
									if(font_cho[beforeCho] == beforeInput ||  beforeStr == beforeInput.charCodeAt(0)){
										result += arrStr + ",";
									}
								}
							//초성인 경우...
							}else{
								if(font_cho[Cho] == inputStr2){
									if(font_cho[beforeCho] == beforeInput ||  beforeStr == beforeInput.charCodeAt(0)){
										result += arrStr + ",";
									}
								}
							}		
						//영어면
						}else{
							//대문자로 변환뒤 비교
							if(inputStr2.toUpperCase().charCodeAt(0) == arrStr.toUpperCase().charCodeAt(j)){
								if(result.indexOf(arrStr) < 0 ){
									result += arrStr + ",";	
								}
							}
						}
					} //[3]반복문 종
				}//[2]반복문 종료
			}//[1]반복문 종료
			if(result == ""){
				result = "검색된 단어가 없습니다.";
			}
			alert(result)
			window.location = "/BEngineer/beFiles/searchForm.do?result=" + result; 
			//alert("검색결과  : " + result);
		 });
		
	});
	// 초성 나누기 return : 초성 배열 index
	function searchCho(str){
		CompleteCode = str;
		UniValue = CompleteCode - 0xAC00;
		var Jong = UniValue % 28;
		var Jung = ( ( UniValue - Jong ) / 28 ) % 21;
		var Cho = parseInt((( UniValue - Jong ) / 28 ) / 21);
		return Cho;
	}