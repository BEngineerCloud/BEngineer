<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<link rel='stylesheet' href='/BEngineer/resources/css/style.css'>
<script src='/BEngineer/resources/js/jquery-1.8.2.min.js' type='text/javascript'></script>
<script src='/BEngineer/resources/pagectrl/menu.js' type='text/javascript'></script>
<script src='/BEngineer/resources/pagectrl/search.js' type='text/javascript'></script>
<style type="text/css">
#back {position:relative;}
#in {position:absolute;
left:10px;
top:5px;
}
</style>
<script type="text/javascript">
	//비교대상 목록?
	var font_test = Array(
		<c:forEach var="dto" items="${font}">
			'${dto.filename}',
		</c:forEach>
		''
	);
	$(function(){
		$("#inForm").submit(function(){
			var form = document.getElementById("inForm");
			if(!form.title.value || form.title.value == ""){
				alert("제목을 입력해주세요");
				form.title.focus();
				return false;
			}
			if(!form.content.value || form.content.value == ""){
				alert("내용을 입렧해주세요");
				form.content.focus();
				return false;
			}
		});
	});
</script>
<body topmargin="0" bottommargin="0" leftmargin="0" rightmargin="0">
<div id="logo" style="height:10%; width:15%; float:left;"> 
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
</div>
<div id="address">
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
</div>
<div id="files">
<form id="inForm" action="inPro.do" method="post" enctype="multipart/form-data">
<table border="1" >
	<tr>
		<td  width="110" bgcolor="#ABF200">제목</td>
		<td  width="300"><input type="text" size="40" maxlength="30" name="title"></td>
	</tr>
	<tr>
		<td  width="110" bgcolor="#ABF200">문의 내 용</td>
		<td  width="300" ><textarea name="content" rows="13" cols="40"></textarea></td>
	</tr>
	<tr>
		<td  width="100" bgcolor="#ABF200">파일첨부</td>
		<td><input type="file" name="upload"></td>
	</tr>
     	<!-- <input type="hidden" name="filename" value="chicken3.PNG">  -->
	<tr>
		<td  width="100" bgcolor="#ABF200">작성자</td>
		<td>${sessionScope.nickname}<input type="hidden" value="${sessionScope.id}" name="Id"></td>
	</tr>
	<tr>
		<td colspan="2" align="center"><input type="submit" value="문의완료" style="background-color:#ABF200;"/></td>
	</tr>     
</table>
</form>
</div>
<div id="etc">
		<a href="/BEngineer/inquiry/inList.do?id=${sessionScope.id}">문의내역</a>
        <a href="/BEngineer/beMember/beboard.do?id=${sessionScope.id}">공지사항</a>
        <a href="/BEngineer/beMember/upgrade.do?id=${sessionScope.id}">유료전환</a>
</div>
</body>
