<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.security.SecureRandom" %>
<%@ page import="java.math.BigInteger" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="https://static.nid.naver.com/js/naveridlogin_js_sdk_2.0.0.js" charset="utf-8"></script>
<title>NaverLogin</title>
</head>
 <body topmargin="0" bottommargin="0" leftmargin="0" rightmargin="0">
<div id="Logo1" style="height:41%; width:100%; float:left; text-align:center">
	<div id="Logo2" style="width:100%; float:left; text-align:center; margin-top: 11%;">
		<img src="\BEngineer\image\beCloudLogo.png" style="width: 31%;"/>
	</div>
</div>
<div id="Login-input" style="height:20%;width:100%; float:left; ">
	<div id="Login-input2" style="width:53%; height:100%; float:left;  text-align:center">
		<div id="Login-input3" style="width:100%; float:left; margin-left:33%; margin-top: 5%">
			<input type="text" name="mailId" style="border-color:black; width:28%; height:20%"  value="아이디"><br/><br/>
			<input type="password" name="pw" style="border-color:black; width:28%; height:20%"  value="비밀번호"><br/>
			<div id="Login-input4" style="width:28%; float:left; margin-left:36%; text-align:left;">
				<input type=checkbox name="autoLogin" value="T" style="border:none;"/><font size="2">아이디 기억하기</font>
			</div>
		</div>
	</div>
	<div id="Pw-input" style="width:47%; height:100%; float:left;  ">
		<div id="Pw-input2" style="width:100%; float:left; margin-top: 5.5%">
			<input type="submit" style="border-color:black; width:16%; height:54%;background-color:#FFFFFF; font-size:20pt"  value="Login">
		</div>
	</div>
</div>
<div id="naverIdLogin" style="height:24%; width:100%; float:left;text-align:center"></div> 
	<!-- 네이버아디디로로그인 초기화 Script -->
	<script type="text/javascript">
		var naverLogin = new naver.LoginWithNaverId(
			{
				clientId: "34hVzh3bD__JqK11Q4sG",
				callbackUrl: "http://192.168.0.153/BEngineer/beMember/beRequestprofile.do",
				isPopup: false, /* 팝업을 통한 연동처리 여부 */
				loginButton: {color: "green", type: 3, height: 60} /* 로그인 버튼의 타입을 지정 */
			}
		);
	
		/* 설정정보를 초기화하고 연동을 준비 */
		naverLogin.init();
	</script>
<div align="center" id="Bottom" style="height:15%; width:100%; float:left; background-color:#5f7f89;"></div>
</body>
</html>