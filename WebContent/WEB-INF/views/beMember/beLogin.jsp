<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.security.SecureRandom" %>
<%@ page import="java.math.BigInteger" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="http://code.jquery.com/jquery-3.2.0.min.js"></script>
<script type="text/javascript" src="https://static.nid.naver.com/js/naveridlogin_js_sdk_2.0.0.js" charset="utf-8"></script>
<title>NaverLogin</title>
</head>
<script type="text/javascript">
$(function(){
	var userEmail = getCookie("userEmail");
	$("#email").val(userEmail);
	
	if($("#email").val()!=""){
		$("#emailSave").attr("checked",true);
	}
	
	if($("#email").val()==""){
		$("#email").val("메일아아디");
	}

$("#beLogin").submit(function(){
	if($("#email").val()=="" || $("#email").val()=="메일아이디"){
		alert("메일주소를 입력하세요.");
		return false;
	}
	
	if($("#pw").val()=="" || $("#pw").val()=="비밀번호"){
		alert("비밀번호를 입력하세요.");
		return false;
	}
	return true;
});

$("#emailSave").change(function(){
	if($("#emailSave").is(":checked")){ //email 저장하기 체크했을 경우
		var userEmail = $("#email").val();
		setCookie("userEmail",userEmail,7);
		
	}else{
		setCookie("userEmail",'',-1);
	}
});

$("#email").blur(function(){
	if($("#emailSave").is(":checked")){
		var userEmail = $("#email").val();
		setCookie("userEmail",userEmail,7);
	}
});

function setCookie(cName, cValue, cDay){
    var expire = new Date();
    expire.setDate(expire.getDate() + cDay);
    cookies = cName + '=' + encodeURIComponent(cValue) + '; path=/ '; // 한글 깨짐을 막기위해 escape(cValue)를 합니다.
    if(typeof cDay != 'undefined') cookies += ';expires=' + expire.toGMTString() + ';';
    document.cookie = cookies;
}

// 쿠키 가져오기
function getCookie(cName) {
    cName = cName + '=';
    var cookieData = document.cookie;
    var start = cookieData.indexOf(cName);
    var cValue = '';
    if(start != -1){
        start += cName.length;
        var end = cookieData.indexOf(';', start);
        if(end == -1)end = cookieData.length;
        cValue = cookieData.substring(start, end);
    }
    return decodeURIComponent(cValue);
}
});
</script>

 <body topmargin="0" bottommargin="0" leftmargin="0" rightmargin="0">
 <form id="beLogin" name="beLogin" method="post" action="/BEngineer/beMember/beLoginpro.do">
<div id="Logo1" style="height:41%; width:100%; float:left; text-align:center">
	<div id="Logo2" style="width:100%; float:left; text-align:center; margin-top: 11%;">
		<img src="\BEngineer\image\beCloudLogo.png" style="width: 31%;"/>
	</div>
</div>

<div id="Login-input" style="height:20%;width:100%; float:left; ">
	<div id="Login-input2" style="width:53%; height:100%; float:left;  text-align:center">
		<div id="Login-input3" style="width:100%; float:left; margin-left:33%; margin-top: 5%">
			<input type="text" name="email" id="email" style="border-color:black; width:28%; height:20%"  value="아이디"><br/><br/>
			<input type="password" name="pw" id="pw" style="border-color:black; width:28%; height:20%"  value="비밀번호"><br/>
			<div id="Login-input4" style="width:28%; float:left; margin-left:36%; text-align:left;">
				<input type=checkbox id="emailSave" name="emailSave" value="T" style="border:none;"/><font size="2">아이디 기억하기</font>
				<font size="2" style="margin-left: 36%"><a href="/BEngineer/beMember/beJoinmember.do" style="text-decoration:none">회원가입</a></font>
			</div>
		</div>
	</div>
	<div id="Pw-input" style="width:47%; height:100%; float:left;  ">
		<div id="Pw-input2" style="width:100%; float:left; margin-top: 5.5%">
			<input type="submit" style="border-color:black; width:16%; height:54%;background-color:#FFFFFF; font-size:20pt"  value="Login">
		</div>
	</div>
</div>
</form>
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