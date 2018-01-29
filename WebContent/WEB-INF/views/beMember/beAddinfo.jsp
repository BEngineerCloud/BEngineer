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
	$("#confirmNickname").click(function(){
			if($("#nickname").val()==""){
				$("#nickname").focus();
				alert("닉네임을 입력해주세요.");
				return false;
			}
			
			url = "/BEngineer/beMember/beChecknickname.do?nickname="+$("#nickname").val();
			open(url, "confirm",  "toolbar=no, location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=300, height=200");
			
	});
	
	
	$("#nickname").keydown(function(){
		$("#confirmNickname").prop("disabled",true);
		var nickname = $("#nickname").val();
		if(nickname.length>1 && nickname.length<11)
			$("#confirmNickname").prop("disabled",false);
	});
	
	$("#beAddinfo").validate({
		rules:{
			nickname:{required:true, rangelength:[2,10]}
		},
		messages:{
			nickname:{
				required:"닉네임을 입력하세요.",
				rangelength:"2글자 이상, 10글자 이하여야 합니다."
			}
		}
		
	});

	
	$("#beAddinfo").submit(function(){
		var regNewpw = /^(?=.*[a-zA-Z])(?=.*[!@#$%^~*+=-])(?=.*[0-9]).{8,20}$/;
		
		if($("#newPw").val()!=""){ // 새 비밀번호 값이 존재할 때
			if(!regNewpw.test($("#newPw").val())) { // 새 비밀번호 유효성 검사
		      	alert("영문자,숫자,특수문자 조합으로 8~20자리 사이의 비밀번호를 입력해주세요."); 
		      	$("#newPw").focus(); 
		      	return false; 
		 	}else{
		 		var newPw1 =$("#newPw").val();
		 		$("#newPw").val(newPw1);
		 	}
		}
		
		if($("#newPw").val()==$("#confirmNewpw").val()){
			alert("새 비밀번호가 일치하지 않습니다.");
			return false;
		}
		
		if($("#nickname").val()==""){
			alert("닉네임을 입력해주세요.");
			return false;
		}
		
		if($("#checkConfirmnickname").val()==""){
			alert("닉네임 중복검사를 해주세요.");
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
<form id="beAddinfo" name="beAddinfo" method="post" action="/BEngineer/beMember/beAddinfopro.do">
<div  align="center" id="Addtitle" style="height:33%; width:100%; float:left;">
	<div id="Addtitle2" style="height:12%; width:30%;  margin-top: 5%">
		<img src="\BEngineer\image\beCloudLogo.png" style="width: 50%;"/><br/><br/><hr color="black"><br/>
		<font size="5">추가 정보 입력</font>
	</div>
</div>
<div align="center" id="Addcontents" style="height:52%; width:100%; float:left;">
	<div align="left" id="Addcontents2" style="height:50%; width:30%;">
		<div id="Addcontents3" style="height:20%; width:35%; float:left;">
			<div align="center" style="height:20%; width:100%; float:left; ">
				<font size="4">이메일 아이디</font>
			</div>
		</div>
		<div id="Addcontents4" style="height:20%; width:65%; float:left;">
			<div style="height:20%; width:100%; float:left; ">
				<font size="4">${memberDTO.email}</font>
			</div>
		</div>
		<div id="Addcontents3" style="height:20%; width:35%; float:left;">
			<div align="center" style="height:20%; width:100%; float:left; ">
				<font size="3">새 비밀번호(필수입력 x)</font>
			</div>
		</div>
		<div id="Addcontents4" style="height:20%; width:65%; float:left;">
			<div style="height:60%; width:100%; float:left; ">
			<input type="hidden" id="pw" name="pw" value="${memberDTO.pw }"/>
				<input type="text" id="newPw" name="newPw" style="border-color:black; width:50%; height:100%" required="required"/>
				<label class="error" for="newPw" generated="true" style="display:none;">에러태그</label>
			</div>
		</div>
		<div id="Addcontents3" style="height:20%; width:35%; float:left;">
			<div align="center" style="height:20%; width:100%; float:left; ">
				<font size="3">새 비밀번호 확인</font>
			</div>
		</div>
		<div id="Addcontents4" style="height:20%; width:65%; float:left;">
			<div style="height:60%; width:100%; float:left; ">
				<input type="text" id="confirmNewpw" name="confirmNewpw" style="border-color:black; width:50%; height:100%" required="required"/>
				<label class="error" for="confirmNewpw" generated="true" style="display:none;">에러태그</label>
			</div>
		</div>
		<hr color="black"><br/>
		<div id="Addcontents3" style="height:70%; width:35%; float:left;">
			<div align="center" style="height:20%; width:100%; float:left; margin-top: 5%">
				<font size="4">닉네임</font>
			</div>
			<div align="center" style="height:20%; width:100%; float:left; margin-top: 9.5%">
				<font size="4">성별</font>
			</div>
			<div align="center" style="height:20%; width:100%; float:left; margin-top: 9.5%">
				<font size="4">생년월일</font>
			</div>
		</div>
		<div id="Addcontents4" style="height:70%; width:65%; float:left;">
			<div style="height:20%; width:100%; float:left; margin-top: 2.3%">
				<input type="text" id="nickname" name="nickname" style="border-color:black; width:50%; height:90%" required="required"/>
				<input type="button" id="confirmNickname" style="height:100%; border-color: black; background-color:#FFFFFF;"value="중복검사"><br/>
				<label class="error" for="nickname" generated="true" style="display:none;">에러태그</label>
				<input type="hidden" id="checkConfirmnickname" value="">
			</div>	
			<div style="height:20%; width:100%; float:left; margin-top: 6%; margin-left:20%">
				<c:if test="${memberDTO.gender=='M'}">
    				<font size="4">남성</font>
   				</c:if>
   				<c:if test="${memberDTO.gender=='W'}">
    				<font size="4">여성</font>
   				</c:if>
			</div>
			<div style="height:20%; width:100%; float:left; margin-top: 6%; margin-left:20%">
    			<font size="4">${memberDTO.birthday}</font>
			</div>
		</div>
	</div>
	
	<div align="center" id="Addcontents2" style="height:50%; width:30%;">
	<hr color="black"><br/>
		<input type="submit" style="border-color:black;  width:12%; height:16%;background-color:#FFFFFF; font-size:16pt"  value="확인">
		&emsp;<input type="button" id="cancel" style="border-color:black;  width:12%; height:16%;background-color:#FFFFFF; font-size:16pt"  value="취소" >
	</div>
	
</div>
<div align="center" id="Addsubmit" style="height:15%; width:100%; float:left; background-color:#5f7f89;">
</div>
</form>
</body>
</html>