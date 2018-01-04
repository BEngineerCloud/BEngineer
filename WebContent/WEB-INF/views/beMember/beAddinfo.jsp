<%@ page language="java" contentType="text/html; charset=UTF-8"%>
    
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>beAddInfo</title>
</head>
<body topmargin="0" bottommargin="0" leftmargin="0" rightmargin="0">
<div  align="center" id="Addtitle" style="height:33%; width:100%; float:left;">
	<div id="Addtitle2" style="height:12%; width:30%;  margin-top: 6%">
		<img src="image\beCloudLogo.png" style="width: 45%;"/><br/><br/><hr color="black"><br/>
		<font size="5">추가 정보 입력</font>
	</div>
</div>
<div align="center" id="Addcontents" style="height:52%; width:100%; float:left;">
	<div align="left" id="Addcontents2" style="height:50%; width:30%; margin-top: 1%;">
		<div id="Addcontents3" style="height:100%; width:35%; float:left;">
			<div align="center" style="height:15%; width:100%; float:left;">
				<font size="4">이메일 아이디</font><br/><br/><hr color="black"><br/>
			</div>
			<div align="center" style="height:15%; width:100%; float:left;">
				<br/><br/><font size="4">닉네임</font>
			</div>
			<div align="center" style="height:15%; width:100%; float:left;">
				<br/><br/><br/><font size="4">생년월일</font>
			</div>
			<div align="center" style="height:15%; width:100%; float:left;">
				<br/><br/><br/><br/><font size="4">성별</font><br/><br/><hr color="black"><br/>
			</div>
		</div>
		<div id="Addcontents4" style="height:100%; width:65%; float:left;">
			<div style="height:15%; width:100%; float:left;">
				<font size="4">이메일 아이디</font><br/><br/><hr color="black"><br/>
			</div>
			<div style="height:15%; width:100%; float:left; ">
				<br/><br/><input type="text" name="nickname" style="border-color:black; width:70%; height:85%">
			</div>
			<div style="height:15%; width:100%; float:left;">
				<br/><br/><br/><input type="text" name="birth" style="border-color:black; width:70%; height:85%">
			</div>
			<div style="height:15%; width:100%; float:left;">
				<br/><br/><br/><br/><input type=radio name="sex" value="male" /><Font size="4">남성</Font>
					<input type=radio name="sex" value="female"/><Font size="4">여성</Font><br/><br/><hr color="black"><br/>
			</div>
		</div>
	</div>
	<div align="center" id="Addcontents2" style="height:50%; width:30%;">
		<input type="submit" style="border-color:black; margin-top:4%; width:12%; height:16%;background-color:#FFFFFF; font-size:16pt"  value="확인">
		&emsp;<input type="submit" style="border-color:black; margin-top:3%; width:12%; height:16%;background-color:#FFFFFF; font-size:16pt"  value="취소">
	</div>
</div>
<div align="center" id="Addsubmit" style="height:15%; width:100%; float:left; background-color:#5f7f89;">
</div>
</body>
</html>