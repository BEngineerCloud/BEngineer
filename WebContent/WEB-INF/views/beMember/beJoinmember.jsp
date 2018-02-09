<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>beJoinmember</title>
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
	$("#confirmEmail").click(function(){
		// 이메일 정규식
		var regEmail = /([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;
		
		if($("#email").val()==""){
			$("#email").focus();
			alert("메일아이디를 입력해주세요.");
			return false;
		}

		if($("#checkConfirmEmail").val()=="true" ){
			alert("메일인증이 완료되었습니다.");
			return false;
		}
		
		if(!regEmail.test($("#email").val())) { // 이메일주소 유효성 검사
		      alert("이메일 주소가 유효하지 않습니다"); 
		      $("#email").focus(); 
		      return false; 
		 } 
	
		url = "/BEngineer/beMember/beCheckemail.do?email="+$("#email").val();
		open(url, "confirm",  "toolbar=no, location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=450, height=250");
		
	});
	
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
	
	$("#beJoinmember").validate({
		rules:{
			email:{required:true},
			pw:{required:true},
			confirmPw:{required:true, equalTo:"#pw"},
			nickname:{required:true, rangelength:[2,10]},
			gender:{required:true},
			birth:{required:true, number:true, minlength:6}
		},
		messages:{
			email:{
				required:"메일아이디 입력하세요."
			},
			pw:{
				required:"비밀번호를 입력하세요."
			},
			confirmPw:{
				required:"",
				equalTo:"비밀번호가 일치하지 않습니다."
			},
			nickname:{
				required:"닉네임을 입력하세요.",
				rangelength:"2글자 이상, 10글자 이하여야 합니다."
			},
			gender:{
				required:"성별을 선택하세요."
			},
			birth:{
				required:"생년월일을 입력하세요.",
				number:"숫자만입력하세요.",
				minlength:"6자리를 입력하세요."
			}
		}
	});

	$("#beJoinmember").submit(function(){
		var regNewpw = /^(?=.*[a-zA-Z])(?=.*[!@#$%^~*+=-])(?=.*[0-9]).{8,20}$/;
		
		if($("#email").val()==""){
			alert("메일아이디를 입력하세요.");
			return false;
		}
		
		if($("#pw").val()==""){
			alert("비밀번호를 입력하세요.");
			return false;
		}
		
		if($("#pw").val()!=""){ // 새 비밀번호 값이 존재할 때
			if(!regNewpw.test($("#pw").val())) { // 새 비밀번호 유효성 검사
		      	alert("영문자,숫자,특수문자 조합으로 8~20자리 사이의 비밀번호를 입력해주세요."); 
		      	$("#pw").focus(); 
		      	return false; 
		 	}
		}
		
		if($("#pw").val()!=$("#confirmPw").val()){
			alert("비밀번호가 일치하지 않습니다.");
			return false;
		}
		
		if($("#checkConfirmMailid").val()==""){
			alert("메일인증을 해주세요.");
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
		
		if(!$('input:radio[name=gender]').is(':checked')){
			alert("성별을 선택하세요.");
			return false;
		}
		
		if($("#birthday").val()==""){
			alert("생년월일을 입력해주세요.");
			return false;
		}else{
			var birthTemp = $("#birthday").val();
			birthTemp = birthTemp.substring(2);
			birthTemp2 = birthTemp.substring(0,2);
			birthTemp3 = birthTemp.substring(2);
			
			if(birthTemp2<=0 || birthTemp2 >=13){
				alert("월을 다시 입력해주세요.");
				return false;
			}else{
				if(birthTemp2==1 || birthTemp2 ==3 || birthTemp2 ==5 || birthTemp2 ==7 || birthTemp2 ==8 || birthTemp2 ==10 || birthTemp2 ==12){
					if(birthTemp3<=0 || birthTemp3>31){
						alert("일을 다시 입력해주세요.");
						return false;
					}
				}else if(birthTemp2==2){
					if(birthTemp3<=0 || birthTemp3>28){
						alert("일을 다시 입력해주세요.");
						return false;
					}
				}else{
					if(birthTemp3<=0 || birthTemp3>30){
						alert("일을 다시 입력해주세요.");
						return false;
					}
				}
			}
			
			birthday2 = birthTemp2 + "-"+birthTemp3;
			$("#birthday").val(birthday2); 
		}
		return true;
	});
	$("#cancel").click(function(){
		window.location = "/BEngineer/beMain.do";
	});
});
</script>
<body topmargin="0" bottommargin="0" leftmargin="0" rightmargin="0">
<form id="beJoinmember" name="beJoinmember" method="post" action="/BEngineer/beMember/beJoinmemberpro.do">
<div  align="center" id="Addtitle" style="height:30%; width:100%; float:left;">
	<div id="Addtitle2" style="height:12%; width:40%;  margin-top: 3%">
		<img src="\BEngineer\image\beCloudLogo.png" style="width: 40%;"/><br/><br/><hr color="black"><br/>
		<font size="5">회원 가입</font>
	</div>
</div>
<div align="center" id="Addcontents" style="height:55%; width:100%; float:left;">
	<div align="left" id="Addcontents2" style="height:50%; width:40%;;">
		<div id="Addcontents3" style="height:23%; width:35%; float:left;">
			<div align="center" style="height:20%; width:100%; float:left; ">
				<font size="4">이메일 아이디</font>
			</div>
		</div>
		<div id="Addcontents4" style="height:23%; width:65%; float:left;">
			<div style="height:64%; width:100%; float:left; ">
				<input type="text" id="email" name="email" style="border-color:black; width:45%; height:90%" required="required"/>
				<input type="button" id="confirmEmail" style="height:100%; border-color: black; background-color:#FFFFFF;"value="메일인증"><br/>
				<label class="error" for="email" generated="true" style="display:none;">에러태그</label>
				<input type="hidden" id="checkConfirmEmail" value="">
			</div>
		</div>
		<div id="Addcontents3" style="height:23%; width:35%; float:left;">
			<div align="center" style="height:20%; width:100%; float:left; ">
				<font size="4">비밀번호</font>
			</div>
		</div>
		<div id="Addcontents4" style="height:23%; width:65%; float:left;">
			<div style="height:64%; width:70%; float:left; ">
				<input type="password" id="pw" name="pw" style="border-color:black; width:45%; height:90%" required="required"/><br/>
				<label class="error" for="pw" generated="true" style="display:none;">에러태그</label>			
			</div>
		</div>
		<div id="Addcontents3" style="height:23%; width:35%; float:left;">
			<div align="center" style="height:20%; width:100%; float:left; ">
				<font size="4">비밀번호 확인</font>
			</div>
		</div>
		<div id="Addcontents4" style="height:23%; width:65%; float:left;">
			<div style="height:64%; width:70%; float:left; ">
				<input type="password" id="confirmPw" name="confirmPw" style="border-color:black; width:45%; height:90%" required="required"/><br/>
				<label class="error" for="confirmPw" generated="true" style="display:none;">에러태그</label>			</div>
		</div>
		<hr color="black"><br/>
		<div id="Addcontents3" style="height:70%; width:35%; float:left;">
			<div align="center" style="height:20%; width:100%; float:left; margin-top: 2%">
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
			<div style="height:20%; width:100%; float:left; margin-top: 0.5%">
				<input type="text" id="nickname" name="nickname" style="border-color:black; width:30%; height:90%" required="required"/>
				<input type="button" id="confirmNickname" style="height:100%; border-color: black; background-color:#FFFFFF;"value="중복검사"><br/>
				<label class="error" for="nickname" generated="true" style="display:none;">에러태그</label>
				<input type="hidden" id="checkConfirmnickname" value="">
			</div>	
			<div style="height:20%; width:100%; float:left; margin-top: 6%;">
				<input type=radio id="gender" name="gender" value="M" /><Font size="4">남성</Font>&emsp;
				<input type=radio id="gender" name="gender" value="W"/><Font size="4">여성</Font><br/>
				<label class="error" for="gender" generated="true" style="display:none;">에러태그</label>
			</div>
			<div style="height:20%; width:100%; float:left; margin-top: 5%;">
    			<input type="text" id="birthday" maxlength="6" name="birthday" style="border-color:black; width:20%; height:90%" required="required"/>&nbsp;
    			<Font size="2">ex)생년월일이 '1999.12.31'인 경우 '991231'형식으로 적어주세요.</Font>
    			<br/>
    			<label class="error" for="birthday" generated="true" style="display:none;">에러태그</label>
			</div>
		</div>
	</div>
	<div align="center" id="Addcontents2" style="height:50%; width:40%; ">
	<br/><br/><hr color="black"><br/>
		<input type="submit" style="border-color:black;  width:12%; height:16%;background-color:#FFFFFF; font-size:16pt"  value="확인">
		&emsp;<input type="button" id="cancel" style="border-color:black;  width:12%; height:16%;background-color:#FFFFFF; font-size:16pt"  value="취소" >
	</div>
	
</div>
<div align="center" id="Addsubmit" style="height:15%; width:100%; float:left; background-color:#5f7f89;">
</div>
</form>
</body>
<script>
if("${email}"!="0" && "${email}"!=null && "${checkConfirmEmail}"!="0" && "${checkConfirmEmail}"!=null){
	 $("#email").val("${email}"); 
	 $("#checkConfirmEmail").val("true"); 
}
</script>
</html>