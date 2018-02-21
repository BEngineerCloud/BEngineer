<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!-- 콜백 페이지 -->
<!doctype html>
<html lang="ko">
	<script type="text/javascript" src="http://code.jquery.com/jquery-3.2.0.min.js"></script>
		
	<!-- 네이버아이디로그인 정보를 받는 javascript -->
	<script type="text/javascript" src="https://static.nid.naver.com/js/naverLogin_implicit-1.0.3.js" charset="utf-8"></script>
	
	<body>	
		<script type="text/javascript">
		
			//네이버 아이디로그인 시 정보제공을 동의 받는 화면
			var naver_id_login = new naver_id_login("y_lBHf8AUAN_aZPACmtg", "http://192.168.0.11:7070/cop/bbs/naverOath.do");
  			
  			naver_id_login.get_naver_userprofile("naverSignInCallback()"); // 네이버 사용자 프로필 조회

			function naverSignInCallback() { // 네이버 사용자 프로필 조회 이후 프로필 정보를 처리할 callback function
				alert(naver_id_login.getProfileData('email'));
    			alert(naver_id_login.getProfileData('id'));
    			alert(naver_id_login.getProfileData('gender'));
    			alert(naver_id_login.getProfileData('birthday'));
    			alert(naver_id_login.getProfileData('nickname'));
  			}
		</script>
	</body>
</html>

