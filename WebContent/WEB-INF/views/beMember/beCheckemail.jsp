<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- 메일주소 중복확인&인증 페이지 -->
<html>
	<head>
	<title>beCheckmailid</title>
	<script type="text/javascript" src="http://code.jquery.com/jquery-3.2.0.min.js"></script>
	</head>
	
	<script type="text/javascript">
		$(function(){
			$("#beCheckMailid").submit(function(){ //메일인증 버튼을 눌러 전송할 때
				
				//이메일 정규식
				var regEmail = /([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;
				
				//메일주소 유효성 검사를 하고 형식이 맞지않을 시 문구를 띄우고 포커스
				if(!regEmail.test($("#email").val())) { //유효성 검사
					$("#email").focus(); 
					alert("이메일 주소가 유효하지 않습니다"); 
			      	return false; 
			 	}	 	
				return true;
			});
			
			$("#authMailid").click(function(){ //메일인증 버튼을 클릭했을 때
				alert("인증링크를 발송하였습니다."); 
				window.location = "/BEngineer/beMember/beAuthemail.do?email="+"${mailid }"; //메일아이디값을 인증페이지로 보내기
			});
			
			$("#useMailid").click(function(){ //닫기 버튼을 클릭했을 때
				self.close(); //자신의 창을 닫는다.
			});
		});
	</script>
	<body>
	
		<!-- check 값이 0이 아니고 authcode 값이 null일 시, 메일주소가 이미 등록이 되어있을 시 -->
		<c:if test="${check!=0 &&null eq authcode}">
			<form id="beCheckMailid" name="beCheckMailid" method="post" action="/BEngineer/beMember/beCheckemail.do">
				<div align="center" style="margin-top: 6%">
					<div  style="width:35%;float:left;">
						<img src="\BEngineer\image\beCloudImage.png" style="width: 35%; margin-left: 45%"/>
					</div>
					
					<div align="left" style="width:65%;float:left;">
						<font size="5">메일아이디 인증</font>
					</div>
					
					<div style="float:center">
						<br/><br/><hr color="black">
						
						<div style="margin-top: 6%">
							<font size="3">"${mailid }" 은(는) <br/>이미 사용중입니다.<br/>다른 이메일아이디를 사용해주세요.</font>
						</div>
						
						<div style="margin-top: 5%">
							<input type="text" name="email" id="email" size="10" style="border-color:black; width:50%; height:15%">
		       				<input type="submit"  style="height:16%; border-color: black; background-color:#FFFFFF;"value="메일인증">
						</div>
					</div>
				</div>
			</form>
		</c:if>
		
		
		<!-- check 값이 0이고 authcode 값이 null일 시, 메일주소 인증 창으로 이동할 시 -->
		<c:if test="${check==0 && null eq authcode}">
			<div align="center" style="margin-top: 6%">
				<div  style="width:35%;float:left;">
					<img src="\BEngineer\image\beCloudImage.png" style="width: 35%; margin-left: 45%"/>
				</div>
				
				<div align="left" style="width:65%;float:left;">
					<font size="5">메일아이디 인증</font>
				</div>
				
				<div style="float:center">
					<br/><br/><hr color="black">
					
					<div style="margin-top: 6%">
						<font size="3">"${mailid }" 은(는) <br/>인증을 하시면 사용하실 수 있습니다.<br/>인증버튼을 눌러주세요.</font>
					</div>
					
					<div style="margin-top: 5%">
						<input type="button"id="authMailid" style=" width:30%;height:15%; border-color: black; background-color:#FFFFFF;font-size:11pt" value="메일인증">
					</div>
				</div>
			</div>
		</c:if>
		
		<!-- authcode 값이 null이 아닐 시, 메일주소 인증링크를 보내고 난 후의 창으로 이동할 때 -->
		<c:if test="${null ne authcode}">
			<div align="center" style="margin-top: 6%">
				<div  style="width:35%;float:left;">
					<img src="\BEngineer\image\beCloudImage.png" style="width: 35%; margin-left: 45%"/>
				</div>
				
				<div align="left" style="width:65%;float:left;">
					<font size="5">메일아이디 인증</font>
				</div>
				
				<div style="float:center">
					<br/><br/><hr color="black">
					
					<div style="margin-top: 6%">
						<font size="3">"${mailid }"로 <br/>인증 링크를 보냈습니다.<br/>인증 링크를 눌러주세요.</font>
					</div>
					
					<div style="margin-top: 5%">
						<input type="button"  id="useMailid" style="height:16%; border-color: black; background-color:#FFFFFF;"value="닫  기">
					</div>
				</div>
			</div>
		</c:if>
	</body>
</html>
