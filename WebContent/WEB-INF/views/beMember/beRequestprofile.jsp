<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!-- 네이버아이디로그인 정보를 받는 페이지 -->

<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
	<title>beRequestprofile</title>
	</head>
	<script type="text/javascript" src="http://code.jquery.com/jquery-3.2.0.min.js"></script>
	
	<!-- 네이버아이디로그인 정보를 받는 javascript -->
	<script type="text/javascript" src="https://static.nid.naver.com/js/naverLogin_implicit-1.0.3.js" charset="utf-8"></script>
	
	<script type="text/javascript">
	
		//네이버 아이디로그인 시 정보제공을 동의 받는 화면
		var naver_id_login = new naver_id_login("34hVzh3bD__JqK11Q4sG", "http://192.168.0.11:7070/cop/bbs/naverOath.do");
  			
  		naver_id_login.get_naver_userprofile("naverSignInCallback()"); //네이버 사용자 프로필 조회

  		function naverSignInCallback() { //네이버 사용자 프로필 조회 이후 프로필 정보를 처리할 callback function
			var id = naver_id_login.getProfileData('id');
  			var email = naver_id_login.getProfileData('email');
  			var nickname = naver_id_login.getProfileData('nickname');
    		var birthday = naver_id_login.getProfileData('birthday');
    		var gender = naver_id_login.getProfileData('gender');  
    		
    		//프로필 정보들을 beMaintemp.do로 보낸다.
    		window.location = "/BEngineer/beMaintemp.do?id="+id+"&email="+email+"&nickname="+nickname+"&birthday="+birthday+"&gender="+gender;
  		}
	</script>	
</html>