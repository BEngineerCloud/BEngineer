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
		
		//imageview의 레이아웃, 정해진 크기를 벗어난 그림이면 자동으로 스크롤을 만들어준다.
		$("body").append("<div id='glayLayer' ></div><div id='overLayer' style='overflow-y:auto;'></div>");
		
		//imageview 밖의 화면을 클릭하면 이미지화면 끄기
		$("#glayLayer").click(function(){
			$(this).hide()
			$("#overLayer").hide();
		});
		
		//imageview 버튼을 클릭했을 때 정해진 크기에 맞게 보여주기
		$("#image").click(function(){
			$("#glayLayer").show();
			$("#overLayer").show().html("<img src=\"/BEngineer/inquiryImg/${re.filename }\" style=\"width:100%; \" />");
			return false;
		});
	});
</script>
	<!-- 이미지를 클릭해서 보여줄 스타일 -->
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
<body topmargin="0" bottommargin="0" leftmargin="0" rightmargin="0">
<div id="logo" style="height:10%; width:15%; float:left;"> 
	<img src="/BEngineer/image/beCloudLogo.png" id="beLogo" style="width: 100%; height:100%; cursor:pointer"/>
</div>
<!-- 검색창 -->
<div align="center" style="height:10%; width:61%; float:left;">
	<div style="margin-top:2%">
		<form id="search" method="post">
			<input type="text" id="searchword"style="height:38%; width:20%; border-color: black; background-color:#FFFFFF;"/>
			<input type="submit" value="검  색" style="height:41%; border-color: black; background-color:#FFFFFF;"/>
		</form>
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
<input name="inList" type="button" value="목록" onClick="location.href='inList.do?id=${sessionScope.id}'" style="margin:1%;"/><br />
	<table border="1" style="float:left; margin:1%;">
	  <tr><td width="90">제목</td><td width="330">${re.title}</td></tr>
	 
	  <tr><td width="90" style="text-align:top;">내 용</td>
	    <td width="330">${re.content}</td></tr>
	    
	  <tr><td width="90">파일</td>
	    <td width="330"><img src="/BEngineer/inquiryImg/${re.filename }" width="250" height="250" id="image" style="cursor:pointer;"/></td></tr>
	  
	  <tr><td width="90">답변</td>
	    <td width="330">${re.reply}</td></tr>
	</table>
</div>
<div id="etc">
		<center>
		<a href="/BEngineer/inquiry/inList.do?id=${sessionScope.id}" style="text-decoration:none">문의내역</a>
        <a href="/BEngineer/beMember/beboard.do?id=${sessionScope.id}" style="text-decoration:none">공지사항</a>
        <a href="/BEngineer/beMember/upgrade.do?id=${sessionScope.id}" style="text-decoration:none">유료전환</a>
     </center>
</div>
</body>
