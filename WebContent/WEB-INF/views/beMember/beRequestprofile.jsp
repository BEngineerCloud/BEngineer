<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>beRequestprofile</title>
<head>
		<script type="text/javascript" src="https://static.nid.naver.com/js/naverLogin_implicit-1.0.3.js" charset="utf-8"></script>
		<script type="text/javascript" src="http://code.jquery.com/jquery-3.2.0.min.js"></script>
	</head>
	<body>	
		<script type="text/javascript">
			var naver_id_login = new naver_id_login("y_lBHf8AUAN_aZPACmtg", "http://192.168.0.11:7070/cop/bbs/naverOath.do");
  			
			// 네이버 사용자 프로필 조회
  			naver_id_login.get_naver_userprofile("naverSignInCallback()");

  			// 네이버 사용자 프로필 조회 이후 프로필 정보를 처리할 callback function
			function naverSignInCallback() {
				var id = naver_id_login.getProfileData('id');
  				var email = naver_id_login.getProfileData('email');
  				var nickname = naver_id_login.getProfileData('nickname');
    			var birthday = naver_id_login.getProfileData('birthday');
    			var gender = naver_id_login.getProfileData('gender');   
    			window.location = "/BEngineer/beMaintemp.do?id="+id+"&email="+email+"&nickname"+nickname+"&birthday"+birthday+"&gender"+gender;
  			}
		</script>
	</body>
</html>