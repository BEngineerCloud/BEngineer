<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.security.SecureRandom" %>
<%@ page import="java.math.BigInteger" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>NaverLogin</title>
</head>
 <body topmargin="0" bottommargin="0" leftmargin="0" rightmargin="0">
<div id="Logo1" style="height:41%; width:100%; float:left; text-align:center">
	<div id="Logo2" style="width:100%; float:left; text-align:center; margin-top: 9%">
		<img src="image\beCloudLogo.png" style="width: 35%;"/>
	</div>
</div>
<div id="Login-input" style="height:20%;width:100%; float:left; ">
	<div id="Login-input2" style="width:53%; height:100%; float:left;  text-align:center">
		<div id="Login-input3" style="width:100%; float:left; margin-left:33%; margin-top: 5%">
			<input type="text" name="mailId" style="border-color:black; width:28%; height:22%"  value="아이디"><br/><br/>
			<input type="password" name="pw" style="border-color:black; width:28%; height:22%"  value="비밀번호"><br/>
			<div id="Login-input4" style="width:28%; float:left; margin-left:36%; text-align:left;">
				<input type=checkbox name="autoLogin" value="T" style="border:none;"/><font size="2">아이디 기억하기</font>
			</div>
		</div>
	</div>
	<div id="Pw-input" style="width:47%; height:100%; float:left;  ">
		<div id="Pw-input2" style="width:100%; float:left; margin-top: 5.5%">
			<input type="submit" style="border-color:black; width:18%; height:56%;background-color:#FFFFFF; font-size:20pt"  value="Login">
		</div>
	</div>
</div>
<div id="NaverLogin" style="height:39%; width:100%; float:left;text-align:center">
	


  <%
    String clientId = "bpa9S2vfjTvjLm8zimTe";//애플리케이션 클라이언트 아이디값";
    String redirectURI = URLEncoder.encode("beCollback.do", "UTF-8");
    SecureRandom random = new SecureRandom();
    String state = new BigInteger(130, random).toString();
    String apiURL = "https://nid.naver.com/oauth2.0/authorize?response_type=code";
    apiURL += "&client_id=" + clientId;
    apiURL += "&redirect_uri=" + redirectURI;
    apiURL += "&state=" + state;
    session.setAttribute("state", state);
 %>
 <a href="<%=apiURL%>"><img src="image\naverLogin.PNG" style="width: 18%; height:18%"/>
  
  </div>
  </body>

</html>