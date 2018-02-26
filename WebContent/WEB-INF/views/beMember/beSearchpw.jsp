<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>beAddInfo</title>
</head>
<style type="text/css">
label.error{
	color:red;
	font-size: 10pt;
}
</style>
<script type="text/javascript" src="http://code.jquery.com/jquery-3.2.0.min.js"></script>
<script type="text/javascript" src="https://cdn.jsdelivr.net/jquery.validation/1.16.0/jquery.validate.min.js"></script>

<script type="text/javascript">
$(function(){
	$("#beSearchpw").submit(function(){
		var regEmail = /([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;
		
		if($("#email").val()==""){
			$("#email").focus();
			alert("메일아이디를 입력해주세요.");
			return false;
		}else{
			if(!regEmail.test($("#email").val())) { // 이메일주소 유효성 검사
		      	alert("이메일 주소가 유효하지 않습니다"); 
		      	$("#email").focus(); 
		      	return false; 
		 	} 
		}
		
		if($("#nickname").val()==""){
			$("#nickname").focus();
			alert("닉네임을 입력해주세요.");
			return false;
		}
		
		return true;
	});
	
	$("#cancel").click(function(){
		window.location = "/BEngineer/beMain.do";
	});
});
</script>
<body topmargin="0" bottommargin="0" leftmargin="0" rightmargin="0">
<form id="beSearchpw" name="beSearchpw" method="post" action="/BEngineer/beMember/beSearchpwpro.do">
<div  align="center" id="Addtitle" style="height:47%; width:100%; float:left;">
	<div id="Addtitle2" style="height:12%; width:30%;  margin-top: 10%">
		<img src="\BEngineer\image\beCloudLogo.png" style="width: 50%;"/><br/><br/><hr color="black"><br/>
		<font size="5">비밀번호 찾기</font>
	</div>
</div>
<div align="center" id="Addcontents" style="height:38%; width:100%; float:left;">
	<div align="left" id="Addcontents2" style="height:50%; width:30%;">
		<div id="Addcontents3" style="height:50%; width:35%; float:left;">
			<div align="center" style="height:20%; width:100%; float:left;">
				<font size="4">이메일 아이디</font>
			</div>
		</div>
		<div id="Addcontents4" style="height:50%; width:65%; float:left;">
			<div style="height:100%; width:100%; float:left; ">
				<input type="text" name="email" id="email" style="border-color:black; height:40%; width:50%" ><br/><br/>
			</div>
		</div>
		<div id="Addcontents3" style="height:50%; width:35%; float:left;">
			<div align="center" style="height:20%; width:100%; float:left; ">
				<font size="4">닉네임</font>
			</div>
		</div>
		<div id="Addcontents4" style="height:50%; width:65%; float:left;">
			<div style="height:100%; width:100%; float:left; ">
				<input type="text" name="nickname" id="nickname" style="border-color:black; height:40%; width:50%"><br/><br/>
			</div>
		</div>
	</div>
	
	<div align="center" id="Addcontents2" style="height:50%; width:30%;">
	<hr color="black"><br/>
		<input type="submit" style="border-color:black;  width:12%; height:20%;background-color:#FFFFFF; font-size:16pt"  value="확인">
		&emsp;<input type="button" id="cancel" style="border-color:black;  width:12%; height:20%;background-color:#FFFFFF; font-size:16pt"  value="취소" >
	</div>
	
</div>
<div align="center" style="height:15%; width:100%; float:left; background-color:#5f7f89;">
</div>
</form>
</body>
</html>