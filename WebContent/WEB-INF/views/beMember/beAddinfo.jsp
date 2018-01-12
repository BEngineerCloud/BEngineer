<%@ page language="java" contentType="text/html; charset=UTF-8"%>
    
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>beAddInfo</title>
</head>
<script type="text/javascript" src="http://code.jquery.com/jquery-3.2.0.min.js"></script>
<script type="text/javascript">
$(function(){
	$("#confirmNickname").click(function(){
			if($("#nickname").val()==""){
				$("#nickname").focus();
				alert("닉네임을 입력해주세요.");
				return;
			}
			
			url = "/BEngineer/beMember/beChecknickname.do?nickname="+$("#nickname").val();
			open(url, "confirm",  "toolbar=no, location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=300, height=200");
			
	});
});
</script>
<body topmargin="0" bottommargin="0" leftmargin="0" rightmargin="0">
<form id="frm">
<div  align="center" id="Addtitle" style="height:33%; width:100%; float:left;">
	<div id="Addtitle2" style="height:12%; width:30%;  margin-top: 5%">
		<img src="\BEngineer\image\beCloudLogo.png" style="width: 50%;"/><br/><br/><hr color="black"><br/>
		<font size="5">추가 정보 입력</font>
	</div>
</div>
<div align="center" id="Addcontents" style="height:52%; width:100%; float:left;">
	<div align="left" id="Addcontents2" style="height:50%; width:30%; margin-top: 1%;">
		<div id="Addcontents3" style="height:100%; width:35%; float:left;">
			<div align="center" style="height:20%; width:100%; float:left; ">
				<font size="4">이메일 아이디</font><br/><br/><hr color="black"><br/>
			</div>
			<div align="center" style="height:20%; width:100%; float:left; margin-top: 20%">
				<font size="4">닉네임</font>
			</div>
			<div align="center" style="height:20%; width:100%; float:left; margin-top: 5%">
				<font size="4">생년월일</font>
			</div>
			<div align="center" style="height:20%; width:100%; float:left; margin-top: 5%">
				<font size="4">성별</font><br/><br/><hr color="black"><br/>
			</div>
		</div>
		<div id="Addcontents4" style="height:100%; width:65%; float:left;">
			<div style="height:20%; width:100%; float:left; ">
				<font size="4">${memberDTO.email}</font><br/><br/><hr color="black"><br/>
			</div>
			<div style="height:20%; width:100%; float:left; margin-top: 10.7%">
				<input type="text" id="nickname" style="border-color:black; width:70%; height:65%"/>
				<input type="button" id="confirmNickname" style="height:68%; border-color: black; background-color:#FFFFFF;"value="중복검사">
			</div>
			<div style="height:20%; width:100%; float:left; margin-top: 2.8%">
				<input type="text" name="year" style="border-color:black; width:15%; height:65%">
				<font size="4">년</font>&emsp;
				<input type="text" name="month" style="border-color:black; width:15%; height:65%">
				<font size="4">월</font>&emsp;
				<input type="text" name="date" style="border-color:black; width:15%; height:65%">
				<font size="4">일</font>
			</div>
			<div style="height:20%; width:100%; float:left;margin-top: 2.7%">
					<input type=radio name="sex" value="male" /><Font size="4">남성</Font>&emsp;
					<input type=radio name="sex" value="female"/><Font size="4">여성</Font><br/><br/><hr color="black"><br/>
			</div>
		</div>
	</div>
	<div align="center" id="Addcontents2" style="height:50%; width:30%; margin-top: 2%">
		<input type="submit" style="border-color:black;  width:12%; height:16%;background-color:#FFFFFF; font-size:16pt"  value="확인">
		&emsp;<input type="submit" style="border-color:black;  width:12%; height:16%;background-color:#FFFFFF; font-size:16pt"  value="취소">
	</div>
	
</div>
<div align="center" id="Addsubmit" style="height:15%; width:100%; float:left; background-color:#5f7f89;">
</div>
</form>
</body>
</html>