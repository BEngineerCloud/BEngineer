<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript" src="http://code.jquery.com/jquery-3.2.0.min.js" ></script>
<script src='/BEngineer/resources/js/menu.js' type='text/javascript'></script>
<script type="text/javascript">
	function setForm(filename, ref){}
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
	//비교대상 목록?
	var font_test = Array(
		<c:forEach var="font" items="${font}">
			'${font.filename}',
		</c:forEach>
		''
	);
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
</script>
<body topmargin="0" bottommargin="0" leftmargin="0" rightmargin="0">
<div id="logo" style="height:10%; width:15%; background-color:#ff9999; float:left;">
	<img src="/BEngineer/image/beCloudLogo.png" id="beLogo" style="width: 100%; height:100%; cursor:pointer"/>
</div>
<!-- 검색창 -->
<div style="height:10%; width:70%; background-color:#99ff99; float:left;">
	<input type="text" id="searchword"/>
	<input type="button" id="search" value="검색"/>
</div>
<div align="center" id="logout" style="height:10%; width:15%;float:left;">
	<div style="height:30%; width:100%;float:left;  margin-top: 3%"> 
		<font size="4">'${sessionScope.nickname}' 님 환영합니다.</font>
	</div>
	<div align="right" id="addinfodiv" style="height:70%; width:50%;float:left; margin-top: 3%">
		<input type="button" id="addinfo" style="height:61%; border-color: black; background-color:#FFFFFF; font-size:11pt"value="회원정보 관리"/>
	</div>
	<div align="left"id="logoutdiv" style="height:70%; width:50%;float:left; margin-top: 3%">
		&emsp;
		<input type="button" id="logout" style="height:61%; border-color: black; background-color:#FFFFFF; font-size:11pt"value="로그아웃"/>
	</div>
</div>
<div id="button1" style="height:5%; width:100%; background-color:#ffff99; float:left;">
</div>
<div id="button1_1" style="height:5%; width:100%; background-color:#eeee88; float:left;">
</div>
<div id="address" style="height:5%; width:100%; background-color:#99ffff; float:left;">
	<c:set var="num" value="0" />
	<!-- 폴더경로 보여주기 -->
	<c:forEach var="addr" items="${folderaddress }">
		<c:if test="${orgaddress[num] != null }">
			<a href="/BEngineer/beFiles/beSharedList.do?folder=${orgaddress[num] }">${addr }</a> /
		</c:if>
		<c:if test="${orgaddress[num] == null }">
			${folderaddress[num] } /
		</c:if>
		<c:set var="num" value="${num + 1 }" />
	</c:forEach>
	<!-- 선택파일 보여주기용 -->
	<font id="filename"></font><c:if test="${folder_ref != 0 }">  (${enddate }까지 읽기 가능)</c:if>
</div>
<div id="button2" style="height:75%; width:10%; background-color:#ff99ff; float:left;">
	<input type="button" id="myfile" value="내 파일"/>
	<input type="button" id="mysharedfile" value="공유 파일"/>
	<input type="button" id="mytrashcan" value="휴지통"/>
	<input type="button" id="hotlist" value="즐겨찾기"/>
	<form action="/BEngineer/beFiles/beRecentFiles.do" method="post">
		<select name="weeks">
			<option value="1">1주 이내</option>
			<option value="2">2주 이내</option>
			<option value="3">3주 이내</option>
			<option value="4">4주 이내</option>
		</select>
		<input type="submit" value="최근 파일"/>
	</form>
	button2
	<c:if test="${space != null && space !=''}">
		<img src="data:image/png;base64,${space}" style="width:100%;" />
	</c:if>
</div>
<!-- 파일들 창 -->
<div id="files" style="height:75%; width:90%; background-color:#999999; float:left; overflow-y:scroll;">
	<c:forEach var="file" items="${list }">
		<div class="file" name="${file.num }" style="height:100; width:100; margin:1%; background-color:#ff6666; float:left; overflow:hidden">${file.filename }<input type="text" id="${file.num }" value="${file.orgname }" style="border:0; background:transparent; cursor:default; width:100%;" disabled/></div>
		<input type="hidden" id="${file.num }type" value="${file.filetype }"/>
	</c:forEach>
</div>
<c:if test="${write }">
	<!-- text파일 쓰기용 창 -->
	<div id="writetextdiv" style="height:40%; width:90%; background-color:#ffff99; float:left; overflow-y:scroll; display:none">
		<form action="/BEngineer/beFiles/writeText.do" id="writetextform" method="post">
			<input type="hidden" name="folder" value="${folder_ref }"/>
			<div style="height:8%; width:50%; float:left; text-align:left;">
				파일 별명 : <input type="text" name="filename"/>
			</div>
			<div style="height:8%; width:50%; float:left; text-align:left;">
				파일명 : <input type="text" name="orgname"/>
			</div>
			<div style="height:84%; width:100%; float:left; text-align:left;">
				<textarea name="content" cols="100" rows="20"></textarea>
			</div>
			<div style="height:8%; width:50%; float:left; text-align:left;">
				<input type="submit" value="작성완료"/>
			</div>
			<div style="height:8%; width:50%; float:left; text-align:left;">
				<input type="button" value="취소" id="canclewritetext"/>
			</div>
		</form>
	</div>
</c:if>
<div id="etc" style="height:10%; width:100%; background-color:#5f7f89; float:left;">
	etc
</div>