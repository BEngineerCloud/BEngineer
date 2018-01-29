<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<title>beCheckmailid</title>
</head>
<script type="text/javascript" src="http://code.jquery.com/jquery-3.2.0.min.js"></script>
<script type="text/javascript" src="https://cdn.jsdelivr.net/jquery.validation/1.16.0/jquery.validate.min.js"></script>
<script type="text/javascript">
$(function(){
	$("#beCheckMailid").submit(function(){
		var regEmail = /([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;
		if(!regEmail.test($("#email").val())) { // 이메일주소 유효성 검사
	      	alert("이메일 주소가 유효하지 않습니다"); 
	      	$("#email").focus(); 
	      	return false; 
	 	}	 	
	
		return true;
	});
	
	$("#authMailid").click(function(){
		alert("인증번호를 발송하였습니다.");
		window.location = "/Bengineer/beMember/beAuthemail.do?email="+"${mailid }";
	});
	
	$("#useMailid").click(function(){
		if($("#authcode_2").val()==""){
			$("#authcode_2").focus();
			alert("인증문자를 입력해주세요.");
			return false;
		}
		
		if("${authcode }"==$("#authcode_2").val()){
			$("#email",opener.document).val("${mailid}"); 
			$("#checkConfirmEmail",opener.document).val("true"); 
			alert("메일인증이 완료되었습니다.")
			self.close();
		}else{
			alert("인증문자를 다시입력해주세요.");
			$("#authcode_2").val()="";
			$("#authcode_2").focus();
			return false;
		}
	});
});
</script>
<body>
	<c:if test="${check!=0 &&null eq authcode}">
	<form id="beCheckMailid" name="beCheckMailid" method="post" action="/Bengineer/beMember/beCheckmailid.do">
		<div align="center" style="margin-top: 6%">
			<div  style="width:35%;float:left;">
				<img src="\Bengineer\image\beCloudImage.png" style="width: 35%; margin-left: 45%"/>
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
	
	<c:if test="${check==0 && null eq authcode}">
		<div align="center" style="margin-top: 6%">
			<div  style="width:35%;float:left;">
				<img src="\Bengineer\image\beCloudImage.png" style="width: 35%; margin-left: 45%"/>
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
	<c:if test="${null ne authcode}">
		<div align="center" style="margin-top: 6%">
			<div  style="width:35%;float:left;">
				<img src="\Bengineer\image\beCloudImage.png" style="width: 35%; margin-left: 45%"/>
			</div>
			<div align="left" style="width:65%;float:left;">
				<font size="5">메일아이디 인증</font>
			</div>
			<div style="float:center">
				<br/><br/><hr color="black">
				<div style="margin-top: 6%">
					<font size="3">"${mailid }"로 <br/>인증 문자를 보냈습니다.<br/>인증 문자를 입력해주세요.</font>
				</div>
				<div style="margin-top: 5%">
					<input type="text" name="authcode_2" id="authcode_2" size="10" style="border-color:black; width:50%; height:15%">
					<input type="button"  id="useMailid" style="height:16%; border-color: black; background-color:#FFFFFF;"value="메일인증">
				</div>
			</div>
		</div>
	</c:if>
</body>
</html>
