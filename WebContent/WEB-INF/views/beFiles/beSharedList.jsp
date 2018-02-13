<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript" src="http://code.jquery.com/jquery-3.2.0.min.js" ></script>
<script src='/BEngineer/resources/pagectrl/menu.js' type='text/javascript'></script>
<script src='/BEngineer/resources/pagectrl/search.js' type='text/javascript'></script>
<script src='/BEngineer/resources/pagectrl/sharedlistctrl.js' type='text/javascript'></script>
<script src='/BEngineer/resources/formjs/upload.js' type='text/javascript'></script>
<script src='/BEngineer/resources/formjs/createfolder.js' type='text/javascript'></script>
<script src='/BEngineer/resources/formjs/multidown.js' type='text/javascript'></script>
<script src='/BEngineer/resources/formjs/multicopy.js' type='text/javascript'></script>
<script src='/BEngineer/resources/formjs/multimove.js' type='text/javascript'></script>
<script src='/BEngineer/resources/formjs/copy.js' type='text/javascript'></script>
<script src='/BEngineer/resources/formjs/move.js' type='text/javascript'></script>
<script src='/BEngineer/resources/formjs/writetext.js' type='text/javascript'></script>
<script src='/BEngineer/resources/formjs/changename.js' type='text/javascript'></script>
<script src='/BEngineer/resources/formjs/folderdown.js' type='text/javascript'></script>
<script src='/BEngineer/resources/formjs/share.js' type='text/javascript'></script>
<script type="text/javascript">
var clickedFile = new Array();
var clickedImportant = new Array(); //여러 파일/폴더 선택 시 중요폴더가 포함되어있는지 알기 위해
var orgList = new Array(); // 파일/폴더 경로 리스트 받아오기
<c:forEach items="${orgaddress}" var="item">
	orgList.push("${item}");
</c:forEach>
//비교대상 목록?
var font_test = new Array();
<c:forEach var="font" items="${font}">
	font_test.push("${font.filename}");
</c:forEach>
var writeList = new Array(); // 파일/폴더 경로 리스트 받아오기
<c:forEach items="${writelist}" var="item">
	writeList.push("${item}");
</c:forEach>
var folder_ref = '${folder_ref}';
</script>
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
	<c:if test="${write }">
		<!-- 파일업로드 폼 -->
		<div style="height:5%; width:relative; margin:0; float:left;">
			<form action="/BEngineer/beFiles/fileupload.do" id="uploadform" method="post" enctype="multipart/form-data">
				<input type="hidden" name="folder" value="${folder_ref }" />
				<input type="hidden" name="save" />
				<input type="hidden" name="filename" value="파일 이름"/>
				<input type="submit" name="submitbutton" value="업로드" />
			</form>
		</div>
		<!-- 폴더생성 폼 -->
		<div style="height:5%; width:relative; margin:0; float:left;">
			<form action="/BEngineer/beFiles/createFolder.do" id="folderform" method="post">
				<input type="hidden" name="folder" value="${folder_ref }" />
				<input type="hidden" name="foldername" value="폴더 이름"/>
				<input type="submit" value="폴더 생성" />
			</form>
		</div>
	</c:if>
	<!-- 다수 파일 다운로드 폼 -->
	<div style="height:5%; width:relative; margin:0; float:left;">
		<div style="height:100%; width:relative; margin-top:5; float:left;">
			<input type="hidden" id="multidowntext" style="background-color:transparent; border:0px; text-color:black; width:230px;" value="다운로드할 파일/폴더를 선택해주세요" disabled/>
		</div>
		<div style="height:100%; width:relative; margin:0; float:left;">
			<form action="/BEngineer/beFiles/beDownload.do" id="multidownform" method="post">
				<input type="hidden" name="file_ref" />
				<input type="hidden" name="file_fref"/>
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
					<input type="hidden" id="cancelmultidown" value="취소" />
				</div>
			</form>
		</div>
	</div>
	<c:if test="${write && document }">
		<!-- 파일생성 폼 -->
		<div style="height:5%; width:relative; margin:0; float:left;">
			<input type="button" value="텍스트 파일 만들기" id="writetextbutton"/>
		</div>
	</c:if>
</div>
<div id="button1_1" style="height:5%; width:100%; background-color:#eeee88; float:left;">
	<!-- 텍스트 파일 수정 폼 -->
	<div style="height:5%; width:relative; margin:0; float:left;">
		<form id="rewritetextform" action="/BEngineer/beFiles/rewriteText.do" method="post">
			<input type="hidden" name="filenum" />
			<input type="hidden" name="submitrewritetext" value="내용 수정하기"/>
		</form>
	</div>
	<!-- 폴더명 변경 폼 -->
	<div style="height:5%; width:relative; margin:0; float:left;">
		<form id="changenameform" method="post">
			<input type="hidden" name="folder" value="${folder_ref }" />
			<input type="hidden" name="ref" />
			<input type="hidden" name="name" />
			<input type="hidden" name="submitchangename" />
		</form>
	</div>
	<!-- 폴더 다운로드 폼 -->
	<div style="height:5%; width:relative; margin:0; float:left;">
		<form id="folderdownform" method="post" action="/BEngineer/beFiles/beDownload.do">
			<input type="hidden" name="file_ref" />
			<input type="hidden" name="submitfolderdown" value="폴더 다운로드"/>
		</form>
	</div>
	<!-- 공유중인 사람 확인 폼 -->
	<div style="height:5%; width:relative; margin:0; float:left;">
		<form id="sharecheckform" action="/BEngineer/beFiles/lookSharedPeople.do" method="post">
			<input type="hidden" name="file" />
			<input type="hidden" name="submitsharecheck" value="공유 중인 사람 보기"/>
		</form>
	</div>
	<!-- 공유 해제 폼 -->
	<div style="height:5%; width:relative; margin:0; float:left;">
		<div style="height:100%; width:relative; float:left;">
			<form id="unshareform" action="/BEngineer/beFiles/unshare.do" method="post">
				<input type="hidden" name="file_ref" />
				<input type="hidden" name="folder" value="${folder_ref }"/>
				<input type="hidden" name="submitunshare" value="공유 해제하기"/>
			</form>
		</div>
	</div>
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
	<font id="filename"></font>
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
	<c:set var="fnum" value="0" />
	<c:forEach var="file" items="${list }">
		<div class="file" name="${file.num }" style="height:100; width:100; margin:1%; background-color:#ff6666; float:left; overflow:hidden">${file.filename }<input type="text" id="${file.num }orgname" value="${file.orgname }" style="border:0; background:transparent; cursor:default; width:100%;" disabled/></div>
		<input type="hidden" id="${file.num }type" value="${file.filetype }"/>
		<input type="hidden" id="${file.num }important" value="${file.important }"/>
		<input type="hidden" id="${file.num }date" value="${datelist.get(fnum) }"/>
		<c:set var="fnum" value="${fnum + 1 }" />
	</c:forEach>
</div>
<!-- text파일 쓰기용 창 -->
<div id="writetextdiv" style="height:40%; width:90%; background-color:#ffff99; float:left; overflow-y:scroll; display:none">
	<form action="/BEngineer/beFiles/writeText.do" id="writetextform" method="post">
		<input type="hidden" name="folder" value="${folder_ref }"/>
		<div style="height:8%; width:100%; float:left; text-align:left;">
			파일 별명 : <input type="text" name="filename"/>
			파일명 : <input type="text" name="orgname"/>
		</div>
		<div style="height:84%; width:100%; float:left; text-align:left;">
			<textarea name="content" cols="100" rows="20"></textarea>
		</div>
		<div style="height:8%; width:100%; float:left; text-align:left;">
			<input type="submit" value="작성완료"/>
			<input type="button" value="취소" id="canclewritetext"/>
		</div>
	</form>
</div>
<div id="etc" style="height:10%; width:100%; background-color:#5f7f89; float:left;">
	etc
</div>
<c:if test="${textcontent != null}">
	<script>
		reWriteText('${textname}', '${textorgname}', '${textcontent}');
	</script>
</c:if>