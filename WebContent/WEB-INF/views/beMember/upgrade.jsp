<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<link rel='stylesheet' href='/BEngineer/resources/css/style.css'>
	<script src='/BEngineer/resources/js/jquery-1.8.2.min.js' type='text/javascript'></script>
<script src='/BEngineer/resources/pagectrl/menu.js' type='text/javascript'></script>
<script src='/BEngineer/resources/pagectrl/search.js' type='text/javascript'></script>
<script type="text/javascript">
	$(function(){
		$("#change").submit(function(){
			var form = document.getElementById("change");
			if(form.chmod.value == 0 ) {
				alert("용량을 선택해주세요");
				return false;
			}
		});
	});
	var font_test = Array(
			<c:forEach var="dto" items="${font}">
				'${dto.filename}',
			</c:forEach>
			''
		);
</script>
<style type="text/css">
#back {position:relative;}
#in {position:absolute;
left:250px;
top:200px;
}
</style>
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
	<c:if test="${space != null && space !=''}">
		전체 사용량
		<img src="data:image/png;base64,${space}" style="width:100%;" />
	</c:if>
</div>
<div id="back">
<div id="in">
<table>
<c:choose>
    <c:when test="${giga2 == 2 }">
    <tr><td>${giga} Byte/1 GB</td></tr><tr><td>현재 사용 가능용량${1024*1024*1024-giga} Byte</td></tr>
    </c:when>
    <c:otherwise> 
        <tr><td>${giga} Byte/${giga2*10} GB</td></tr><tr><td>현재 사용 가능용량 :  <fmt:formatNumber value="${(giga2*10*1024*1024*1024-giga)/1024/1024}" pattern="##.#"/>MByte</td></tr>
    </c:otherwise>
</c:choose>
</table>
<form action="/BEngineer/beMember/change.do" method="post" id="change">
<table>
	<tr><td>10GB<input type="radio" name="chmod" value="1"></td><td>50000원</td></tr> 
	<tr><td>30GB<input type="radio" name="chmod" value="3"></td><td>100000원</td></tr>
	<tr><td>기간 1년</td></tr>
	<tr><td>계좌</td><td>
	<select name="account" size="1">
	<option>선택</option>
	<option value="s">신한은행</option>
	<option valur="n">NH농협</option>
	<option value="w">우리은행</option>
	<option valur="h">하나은행</option>
	<option value="i">IBK기업은행</option>
	<option valur="k">KB국민은행</option>
	</select>
	</td>
	<td><input type="text" size="3" name="account1"/>-<input type="text" size="3" name="account2"/>-<input type="text" size="3" name="account3"/>-<input type="text" size="3" name="account4"/></td>
	</tr>
</table>
	<input type="hidden" name="id" value="${id}"/>
	<input type="submit" value="결제"/>
</form>
</div>
</div>
<div id="etc">
	<center>
		<a href="/BEngineer/inquiry/inList.do?id=${sessionScope.id}" style="text-decoration:none">문의내역</a>
        <a href="/BEngineer/beMember/beboard.do?id=${sessionScope.id}" style="text-decoration:none">공지사항</a>
        <a href="/BEngineer/beMember/upgrade.do?id=${sessionScope.id}" style="text-decoration:none">유료전환</a>
     </center>
</div>
