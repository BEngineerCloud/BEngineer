<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel='stylesheet' href='/BEngineer/resources/css/style.css'>
<script type="text/javascript" src="http://code.jquery.com/jquery-3.2.0.min.js" ></script>
<script src='/BEngineer/resources/pagectrl/menu.js' type='text/javascript'></script>
<script src='/BEngineer/resources/pagectrl/search.js' type='text/javascript'></script>
<script src='/BEngineer/resources/pagectrl/previewctrl.js' type='text/javascript'></script>
<script src='/BEngineer/resources/formjs/upload.js' type='text/javascript'></script>
<script src='/BEngineer/resources/formjs/multidown.js' type='text/javascript'></script>
<script src='/BEngineer/resources/formjs/multicopy.js' type='text/javascript'></script>
<script src='/BEngineer/resources/formjs/multimove.js' type='text/javascript'></script>
<script src='/BEngineer/resources/formjs/copy.js' type='text/javascript'></script>
<script src='/BEngineer/resources/formjs/move.js' type='text/javascript'></script>
<script src='/BEngineer/resources/formjs/writetext.js' type='text/javascript'></script>
<script src='/BEngineer/resources/formjs/changename.js' type='text/javascript'></script>
<script src='/BEngineer/resources/formjs/share.js' type='text/javascript'></script>
<script src='/BEngineer/resources/formjs/throwtotrash.js' type='text/javascript'></script>
<script src='/BEngineer/resources/formjs/changeowner.js' type='text/javascript'></script>
<script type="text/javascript">
	var clickedFile = new Array();
	var clickedImportant = new Array(); //여러 파일/폴더 선택 시 중요폴더가 포함되어있는지 알기 위해
	var orgList = new Array(); // 파일/폴더 경로 리스트 받아오기
	<c:forEach items="${orgaddress}" var="item">
		orgList.push("${item}");
	</c:forEach>
	//비교대상 목록?
	var font_test = Array(
		<c:forEach var="font" items="${font}">
			'${font.filename}',
		</c:forEach>
		''
	);
	var folder_ref = '${folder_ref}';
	var writeList = null;
</script>
<style type="text/css">
html,body{
	margin:0;
	padding:0;
	height:100%;
}
div#glayLayer{
	display:none;
	position:fixed;
	left:0;
	top:0;
	height:100%;
	width:100%;
	background:black;
	filter:alpha(opacity=60);
	opacity: 0.60;
}
* html div#glayLayer{
	position:absolute;
}
#overLayer{
	display:none;
	position: absolute;
	top:10%;
	left:20%;
	max-height:80%;
	width:60%
}
* html #overLayer{
	position: absolute;
}
</style>
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.min.js"></script>
<script type="text/javascript" src="http://code.jquery.com/jquery-3.2.0.min.js" ></script>
<body topmargin="0" bottommargin="0" leftmargin="0" rightmargin="0">
<div id="logo" style="height:10%; width:15%; background-color:#ff9999; float:left;">
	<img src="/BEngineer/image/beCloudLogo.png" id="beLogo" style="width: 100%; height:100%; cursor:pointer"/>
</div>
<!-- 검색창 -->
<div align="center" style="height:10%; width:61%; float:left;">
	<div style="margin-top:2%">
	<input type="text" id="searchword"style="height:38%; width:20%; border-color: black; background-color:#FFFFFF;"/>
	<input type="button" id="search" value="검  색" style="height:41%; border-color: black; background-color:#FFFFFF;"/>
	</div>
</div>
<div style="height:10%; width:12%; float:left;">
	<div style="height:100%; width:100%;  text-align:center; margin-top:16%"> 
		<font size="4">'${sessionScope.nickname}' 님 환영합니다.</font>
	</div>
</div>
<div style="height:10%; width:12%; float:left;">
	<div align="left"style="height:100%; width:100%;float:left;  text-align:center; margin-top:13%"> 
		<input type="button" id="addinfo" style="height:45%; border-color: black; background-color:#FFFFFF; font-size:100%; "value="회원정보 관리"/>
		&nbsp;
		<input type="button" id="logout" style="height:45%; border-color: black; background-color:#FFFFFF; font-size:100%;"value="로그아웃"/>
	</div>
</div>
<div id="button1">
<!-- 파일업로드 폼 -->
	<div style="height:5%; width:relative; margin:0; float:left;">
		<form action="/BEngineer/beFiles/fileupload.do" id="uploadform" method="post" enctype="multipart/form-data">
			<input type="hidden" name="folder" value="${folder_ref }" />
			<input type="hidden" name="save" />
			<input type="hidden" name="filename" value="파일 이름"/>
			<input type="submit" value="업로드" />
		</form>
	</div>
	<!-- 다수 파일 다운로드 폼 -->
	<div style="height:5%; width:relative; margin:0; float:left;">
		<div style="height:100%; width:relative; margin-top:5; float:left;">
			<input type="hidden" id="multidowntext" style="background-color:transparent; border:0px; text-color:black; width:230px;" value="다운로드할 파일/폴더를 선택해주세요" disabled/>
		</div>
		<div style="height:100%; width:relative; margin:0; float:left;">
			<form action="/BEngineer/beFiles/beDownload.do" id="multidownform" method="post">
				<input type="hidden" name="file_ref" />
				<input type="hidden" name="file_fref" value="${folder_ref }"/>
				<input type="hidden" name="multimove_flag" value=0/>
				<input type="hidden" name="multicopy_flag" value=0/>
				<div style="height:100%; width:relative; float:left;">
					<input type="submit" name="submitmultidown" value="여러 파일 선택하기"/>
				</div>
				<div style="height:100%; width:relative; float:left;">
					<input type="hidden" id="multimove" value="이동" />
				</div>
				<div style="height:100%; width:relative; float:left;">
					<input type="hidden" id="multicopy" value="복사" />
				</div>
				<div style="height:100%; width:relative; float:left;">
					<input type="hidden" id="throwtotrashcan" value="지우기" />
				</div>
				<div style="height:100%; width:relative; float:left;">
					<input type="hidden" id="cancelmultidown" value="취소" />
				</div>
			</form>
		</div>
	</div>
</div>
<div id="button1_1">
	<!-- 이미지보기 폼 -->
	<div style="height:relative; width:100%; margin:0; padding:0; capping:0; float:left;">
		<form id="imageform" method="post">
			<input type="hidden" id="imageview" name="imageview " value="이미지 보기"/>
			<input type="hidden" name="selImageview"/>
		</form>
	</div>
	<br />
	<!-- 단일 지우기 폼 -->
	<div style="height:relative; width:100%; margin:0; padding:0; capping:0; float:left;">
		<form id="throwatrashform" method="post" action="/BEngineer/beFiles/throwToTrashcan.do">
			<input type="hidden" name="file_ref" />
			<input type="hidden" name="submitthrow" value="지우기"/>
		</form>
	</div>
	<br />
<!-- 폴더명 변경 폼 -->
	<div style="height:relative; width:100%; margin:0; padding:0; capping:0; float:left;">
		<form id="changenameform" method="post">
			<input type="hidden" name="folder" value="${folder_ref }" />
			<input type="hidden" name="ref" />
			<input type="hidden" name="name" />
			<input type="hidden" name="submitchangename" />
		</form>
	</div>
	<br />
<!-- 파일/폴더 이동 폼 -->
	<div style="height:relative; width:100%; margin:0; padding:0; capping:0; float:left;">
		<form id="moveform" method="post" action="/BEngineer/beFiles/beMove.do">
			<input type="hidden" name="ref"/>
			<c:if test="${file_flag eq 'move' }">
				<div>
				<input type="submit"  id="submitmove"name="submitmove" value="이동하기"/>
				<input type="hidden" name="select_flag" value="${ref }"/>
				<input type="hidden" name="folder_ref" value="${folder_ref }"/>
				</div>
				<div>
				<input type="button"  id="movecancel" name="movecancel" value="이동 취소"/>
				</div>
			</c:if>
			<c:if test="${file_flag ne 'move' }">
				<div>
				<input type="hidden"  id="submitmove"name="submitmove" value="이동"/>
				<input type="hidden" name="select_flag"/>
				<input type="hidden" name="folder_ref"/>
				</div>
				<div>
				<input type="hidden"  id="movecancel" name="movecancel" value="이동 취소"/>
				</div>
			</c:if>
		</form>
	</div>
	<br />
<!-- 파일/폴더 복사 폼 -->
	<div style="height:relative; width:100%; margin:0; padding:0; capping:0; float:left;">
		<form id="copyform" method="post" action="/BEngineer/beFiles/beCopy.do">
			<input type="hidden" name="ref"/>
			<c:if test="${file_flag eq 'copy' }">
				<div>
				<input type="submit"  id="submitcopy"name="submitcopy" value="복사하기"/>
				<input type="hidden" name="select_flag" value="${ref }"/>
				<input type="hidden" name="folder_ref" value="${folder_ref }"/>
				</div>
				<div>
				<input type="button"  id="copycancel" name="copycancel" value="복사 취소"/>
				</div>
			</c:if>
			<c:if test="${file_flag ne 'copy' }">
				<div>
				<input type="hidden"  id="submitcopy"name="submitcopy" value="복사"/>
				<input type="hidden" name="select_flag"/>
				<input type="hidden" name="folder_ref"/>
				</div>
				<div>
				<input type="hidden"  id="copycancel" name="copycancel" value="복사 취소"/>
				</div>
			</c:if>
		</form>	
	</div>
	<br />
<!-- 공유 폼 -->
	<div style="height:relative; width:100%; margin:0; padding:0; capping:0; float:left;">
		<div>
			<input type="hidden" id="text" style="background-color:transparent; border:0px; text-color:black; width:70px;" value="공유 기한 : " disabled/>
		</div>
		<div>
			<form action="/BEngineer/beFiles/shareFile.do" id="shareform" method="post">
				<input type="hidden" name="ref" />
				<input type="hidden" name="enddate" />
				<select name="rw" hidden style="height:25px;">
					<option value="0">보기만 허용</option>
					<option value="1">쓰기도 허용</option>
				</select>
				<input type="hidden" name="submitshare" value="공유하기"/>
			</form>
		</div>
	</div>
	<br />
	<!-- 공유중인 사람 확인 폼 -->
	<div style="height:relative; width:100%; margin:0; padding:0; capping:0; float:left;">
		<form id="sharecheckform" action="lookSharedPeople.do" method="post">
			<input type="hidden" name="file" />
			<input type="hidden" name="submitsharecheck" value="공유 중인 사람 보기"/>
		</form>
	</div>
	<br />
	<!-- 공유 해제 폼 -->
	<div style="height:relative; width:100%; margin:0; padding:0; capping:0; float:left;">
		<div>
			<select id="unshareselect" hidden style="height:25px">
				<option value="1">모든 사람의 공유 해제</option>
				<option value="2">한 사람의 공유 해제</option>
			</select>
		</div>
		<div>
			<input type="hidden" id="unsharetext" style="top:2px; background-color:transparent; border:0px; text-color:black; width:140px; height:25px;" value="해제할 사람의 닉네임 : " disabled/>
		</div>
		<div>
			<form id="unshareform" action="/BEngineer/beFiles/unshare.do" method="get"> 
				<input type="hidden" name="nickname" />
				<input type="hidden" name="file_ref" />
				<input type="hidden" name="folder" value="${folder_ref }"/>
				<input type="hidden" name="submitunshare" value="공유 해제하기"/>
			</form>
		</div>
	</div>
	<br />
	<!-- 주인 바꾸기 폼 -->
	<div style="height:relative; width:100%; margin:0; padding:0; capping:0; float:left;">
		<div>
			<input type="hidden" id="changeownertext" style="top:2px; background-color:transparent; border:0px; text-color:black; width:140px; height:25px;" value="넘겨줄 사람의 닉네임 : " disabled/>
		</div>
		<div>
			<form id="changeownerform" action="/BEngineer/beFiles/changeowner.do" method="get"> 
				<input type="hidden" name="nickname" />
				<input type="hidden" name="file_ref" />
				<input type="hidden" name="folder" value="${folder_ref }"/>
				<input type="hidden" name="submitchangeowner" value="주인 바꾸기"/>
			</form>
		</div>
	</div>
</div>
<div id="address">
	<c:set var="num" value="0" />
	<!-- 폴더경로 보여주기 -->
	<c:forEach var="addr" items="${folderaddress }">
		<c:if test="${orgaddress[num] != null }">
			<c:if test="${orgaddress[num] eq orgaddress[0] }">
				<a href="/BEngineer/beFiles/beMyList.do?folder=${orgaddress[num] }">${addr }</a> /
			</c:if>
			<c:if test="${orgaddress[num] ne orgaddress[0] }">
				<a href="/BEngineer/beFiles/beImagePreview.do?folder=${orgaddress[num] }">${addr }</a> /
			</c:if>
		</c:if>
		<c:if test="${orgaddress[num] == null }">
			${folderaddress[num] } /
		</c:if>
		<c:set var="num" value="${num + 1 }" />
	</c:forEach>
	<!-- 선택파일 보여주기용 -->
	<font id="filename"></font>
</div>
<div id="button2">
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
	<c:if test="${gra != null && gra !=''}">
		각 파일/폴더 용량
		<img src="data:image/png;base64,${gra}" style="height:50%; width:80%;" />
	</c:if>
	<c:if test="${space != null && space !=''}">
		전체 사용량
		<img src="data:image/png;base64,${space}" style="width:100%;" />
	</c:if>
</div>
<!-- 파일들 창 -->
<div id="files">
	<c:forEach var="file" items="${list }">
		<div class="file" id="${file.num }" name="${file.num }" style="height:20%; width:10%; margin:1%;  box-sizing:border-box;   boxbackground-color #ff6666; float:left; overflow:hidden; "><input type="hidden" id="${file.num }orgname" value="${file.orgname }" style="border:0; background:transparent; cursor:default;" readOnly/>
			<div id="navi" style="height:100%; width:100%;">		
				<a href="http://192.168.0.153/BEngineer/beFiles/beImageview.do?imageName=${file.orgname }" class="modal" onclick="return false;"><img src="http://192.168.0.153/BEngineer/beFiles/beImageview.do?imageName=${file.orgname }" style="width: 100%; height:85%;" /></a><br/>
				${file.filename }
			</div>
		</div>
		<input type="hidden" id="${file.num }type" value="${file.filetype }"/>
		<input type="hidden" id="${file.num }important" value="${file.important }"/>
	</c:forEach>
	<c:if test="${list.size() > 0 }">
		<input type="hidden" id="file_folderref" value="${list.get(0).folder_ref }"/>
	</c:if>
</div>
<div id="etc">
	etc
</div>
<script>
var width = $("body").width() * 0.8;
var height = $("body").height() * 0.8;
if("${file_flag}"=="move"){
	open(width, height);
	var fileform = document.getElementById("${sessionScope.ref}"); // 클릭되어있는 fileform 가져오기
	var moveform = document.getElementById("moveform");
	 // moveform.ref에 fileform.ref 대입하기
	fileform.style.border="dotted"; // fileform의 테두리를 점선으로 설정
	moveform.ref.value="${sessionScope.ref}";
	moveform.submitmove.value="이동하기" 		// moveform.submitmove 값을 확인으로 설정
	moveform.submitmove.type="hidden"; 	// moveform.submitmove 타입을 '숨김'으로 설정
	moveform.movecancel.type="button"; 	// moveform.movecancel 타입을 button으로 설정
}else if("${file_flag}"=="copy"){
	open(width, height);
	var fileform = document.getElementById("${sessionScope.ref}"); // 클릭되어있는 fileform 가져오기
	var copyform = document.getElementById("copyform");
	 // moveform.ref에 fileform.ref 대입하기
	fileform.style.border="dotted"; // fileform의 테두리를 점선으로 설정
	copyform.ref.value="${sessionScope.ref}";
	copyform.submitcopy.value="복사하기" 		// moveform.submitmove 값을 확인으로 설정
	copyform.copycancel.type="button"; 	// moveform.movecancel 타입을 button으로 설정	
}else if("${file_flag}"=="multimove"){
	var moveform = document.getElementById("moveform");
	var copyform = document.getElementById("copyform");
	var ref = "${sessionScope.ref}";
	var refArray = ref.split(',');
	var form = document.getElementById("multidownform");
	
	moveform.submitmove.type="hidden";
	copyform.submitcopy.type="hidden";
	form.file_ref.value="${sessionScope.ref}";
	
	form.multimove_flag.value = 1;
	form.multicopy_flag.value = 0;
	form.submitmultidown.value = "다운로드";
	document.getElementById("multidowntext").type = "text";
	document.getElementById("cancelmultidown").type = "button";
	document.getElementById("throwtotrashcan").type = "button";
	document.getElementById("multimove").value = "이동하기";
	document.getElementById("multimove").type = "button";
	
	for(var i = 0; i < refArray.length; i++){
		var formEx = document.getElementById(refArray[i]);
		formEx.style.border="dotted";
	}	
}else if("${file_flag}"=="multicopy"){
	var moveform = document.getElementById("moveform");
	var copyform = document.getElementById("copyform");
	var ref = "${sessionScope.ref}";
	var refArray = ref.split(',');
	var form = document.getElementById("multidownform");
	
	moveform.submitmove.type="hidden";
	copyform.submitcopy.type="hidden";
	form.multicopy.type="hidden";
	form.file_ref.value="${sessionScope.ref}";
	form.submitmultidown.value = "다운로드";
	form.multimove_flag.value = 1;
	form.multicopy_flag.value = 0;
	document.getElementById("multidowntext").type = "text";
	document.getElementById("cancelmultidown").type = "button";
	document.getElementById("throwtotrashcan").type = "button";
	document.getElementById("multicopy").value = "복사하기";
	document.getElementById("multicopy").type = "button";	
	
	for(var i = 0; i < refArray.length; i++){
		var formEx = document.getElementById(refArray[i]);
		formEx.style.border="dotted";
	}
}
</script>