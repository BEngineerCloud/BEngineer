<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<title>beChecknickname</title>
</head>
<body>
	<c:if test="${check!=0 }">
	<form name="beCheckNickname" method="post" action="/BEngineer/beMember/beChecknickname.do">
		<div align="center" style="margin-top: 6%">
			<div  style="width:25%;float:left; margin-top: 2%">
				<img src="\BEngineer\image\beCloudImage.png" style="width: 50%; margin-left: 25%"/>
			</div>
			<div align="left" style="width:75%;float:left;">
				<font size="5">닉네임 중복검사</font>
			</div>
			<div style="float:center">
				<br/><br/><hr color="black">
				<div style="margin-top: 6%">
					<font size="3">"${nickname }" 은(는) 이미 사용중입니다.<br/>다른 닉네임을 사용해주세요.</font>
				</div>
				<div style="margin-top: 5%">
					<input type="text" name="nickname" size="10" maxlength="12" name="id" style="border-color:black; width:50%; height:15%">
       				<input type="submit"style="height:16%; border-color: black; background-color:#FFFFFF;"value="중복검사">
				</div>
			</div>
		</div>
	</form>
	</c:if>
	
	<c:if test="${check==0 }">
		<div align="center" style="margin-top: 6%">
			<div  style="width:25%;float:left; margin-top: 2%">
				<img src="\BEngineer\image\beCloudImage.png" style="width: 50%; margin-left: 25%"/>
			</div>
			<div align="left" style="width:75%;float:left;">
				<font size="5">닉네임 중복검사</font>
			</div>
			<div style="float:center">
				<br/><br/><hr color="black">
				<div style="margin-top: 6%">
					<font size="3">"${nickname }" 은(는) 사용하실 수 있습니다.<br/>확인 버튼을 눌러주세요.</font>
				</div>
				<div style="margin-top: 5%">
					<input type="button"id="useNickname" style=" width:30%;height:15%; border-color: black; background-color:#FFFFFF;font-size:11pt" value="확 인">
				</div>
			</div>
		</div>
	</c:if>
</body>
</html>
<script type="text/javascript" src="http://code.jquery.com/jquery-3.2.0.min.js"></script>
<script type="text/javascript">
$(function(){
	$("#useNickname").click(function(){
			$("#nickname",opener.document).val("${nickname }"); // jQuery 방식 1
			self.close();
			
	});
});
</script>