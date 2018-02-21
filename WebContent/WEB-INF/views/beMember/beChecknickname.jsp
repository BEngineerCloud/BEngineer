<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- 닉네임 중복검사 페이지 -->
<html>
	<head>
	<title>beChecknickname</title>
	</head>
	
	<script type="text/javascript" src="http://code.jquery.com/jquery-3.2.0.min.js"></script>

	<body>
		
		<!-- check 값이 0이 아닐 시 -->
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
		
		<!-- check 값이 0일 시 -->
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
	
	<script type="text/javascript">
		$(function(){
			
			//닉네임 중복검사가 완료되어 확인 버튼을 클릭했을 시 부모페이지의 닉네임 값에 중복검사가 완료된 닉네임 값과 checkConfirmnickname 값에 true 값을 대입
			$("#useNickname").click(function(){
					$("#nickname",opener.document).val("${nickname}"); 
					$("#checkConfirmnickname",opener.document).val("true");  
					self.close();
					
			});
		});
	</script>
</html>