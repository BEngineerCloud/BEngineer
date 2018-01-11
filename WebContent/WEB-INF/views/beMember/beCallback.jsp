<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!doctype html>
<html lang="ko">
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
				alert(naver_id_login.getProfileData('email'));
    			alert(naver_id_login.getProfileData('id'));
    			alert(naver_id_login.getProfileData('gender'));
    			alert(naver_id_login.getProfileData('birthday'));
    			alert(naver_id_login.getProfileData('nickname'));
  			}
		</script>
	</body>
</html>

