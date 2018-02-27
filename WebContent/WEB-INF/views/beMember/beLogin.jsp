<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.security.SecureRandom" %>
<%@ page import="java.math.BigInteger" %>
<!-- 로그인 페이지 -->

<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>beLogin</title>
	<script type="text/javascript" src="http://code.jquery.com/jquery-3.2.0.min.js"></script>

	<!-- 네이버아이디로그인 javascript -->
	<script type="text/javascript" src="https://static.nid.naver.com/js/naveridlogin_js_sdk_2.0.0.js" charset="utf-8"></script>
	</head>
	
	<script type="text/javascript">
		$(function(){
	
			//"userEmail"쿠키를 받아와서 대입, 해당하는 쿠키가 없을 시 null값 대입
			var userEmail = getCookie("userEmail"); 
			$("#email").val(userEmail);
		
			if($("#email").val()!=""){ //메일아이디 입력값이 null이 아닐 시
				$("#emailSave").attr("checked",true); //'메일아이디 기억하기' 체크박스에 체크
			}
		
			if($("#email").val()==""){ //메일아이디 입력값이 null일 시
				$("#email").val("메일아이디"); //기본내용 입력
			}
	
			//메일아이디의 포커스를 주었을 때 기본내용이 들어가져 있으면 내용지우기
			$("#email").focus(function(){
				if($("#email").val()=="메일아이디")
					$("#email").val("");
			});
			
			//비밀번호의 포커스를 주었을 때 비밀번호 내용 지우기
			$("#pw").focus(function(){
				$("#pw").val("");
			});
			
			$("#beLogin").submit(function(){ //로그인 정보를 전송할 때
			
				//메일아이디 입력값이 빈칸이거나 기본내용과 같을 시 문구 띄우기
				if($("#email").val()=="" || $("#email").val()=="메일아이디"){ 
					alert("메일주소를 입력하세요."); 
					return false;
				}
		
				//비밀번호 입력값이 빈칸이거나 기본내용과 같을 시 문구 띄우기
				if($("#pw").val()=="" || $("#pw").val()=="비밀번호"){
					alert("비밀번호를 입력하세요.");
					return false;
				}
			
				return true; //이상없으면 전송
			});
	
			$("#emailSave").change(function(){ //체크박스가 체크가 변경됐을 시
				if($("#emailSave").is(":checked")){ //체크박스가 체크됐을 경우
				
					//메일아이디 입력값을 쿠키로 설정
					var userEmail = $("#email").val(); 
					setCookie("userEmail",userEmail,7);
				}else{ //체크박스의 체크가 해제됐을 경우
					setCookie("userEmail",'',-1); //쿠키 삭제, -1은 기한에 해당하며 -1을 줬을 시 쿠키가 만료되어 삭제
				}
			});
	
			$("#email").blur(function(){ //메일아이디 입력창에서 포커스가 벗어났을 경우
				if($("#emailSave").is(":checked")){ //체크박스가 체크됐을 경우
				
					//메일아이디 입력값을 쿠키로 설정
					var userEmail = $("#email").val(); 
					setCookie("userEmail",userEmail,7);
				}
			});
	
			function setCookie(cName, cValue, cDay){ //쿠키 설정
	    		var expire = new Date(); //만료 날짜를 위한 Date 객체 생성
	    		expire.setDate(expire.getDate() + cDay); //현재 날짜에 받아온 기간을 더해줘서 설정
	    	
	    		//쿠키 변수에 받아온 값을 문자깨짐 현상을 방지하고자 특수문자까지 같이 인코딩하는 encodeURIComponent 함수를 사용해서 더해주고 
	    		//쿠키를 모든 홈페이지에 적용하기 위해 path=/옵션을 사용해준다.
	    		cookies = cName + '=' + encodeURIComponent(cValue) + '; path=/ ';
	    	
	    		//만료날짜데이터를 제대로 받아왔을 경우 쿠키에 만료날짜를 설정해주고 홈페이지의 쿠키로 설정
	    		if(typeof cDay != 'undefined') cookies += ';expires=' + expire.toGMTString() + ';';
	    			document.cookie = cookies;
			}
	
			function getCookie(cName) { //변수에 해당하는 쿠키를 가져온다.
	    		cName = cName + '=';
	    		var cookieData = document.cookie;
	    		var start = cookieData.indexOf(cName); //받아온 변수에 해당하는 인덱스를 가져와서 대입
	    		var cValue = '';
	    	
		    	if(start != -1){ //인덱스가 존재할 시
		    		
		    		//cValue에 설정했던 쿠키에서 값부분만 받아와서 대입
		        	start += cName.length; 
		        	var end = cookieData.indexOf(';', start);
		        	if(end == -1)end = cookieData.length;
		        		cValue = cookieData.substring(start, end);
		    	}
		    	
		    	return decodeURIComponent(cValue); //원래값으로 디코드 해준 뒤 반환
			}
		});
	</script>

	<body topmargin="0" bottommargin="0" leftmargin="0" rightmargin="0">
		<form id="beLogin" name="beLogin" method="post" action="/BEngineer/beMember/beLoginpro.do">
		
			<!-- 로고 -->
			<div style="height:41%; width:100%; float:left; text-align:center">
				<div style="width:100%; float:left; text-align:center; margin-top: 11%;">
					<img src="\BEngineer\image\beCloudLogo.png" style="width: 31%;"/>
				</div>
			</div>
	
			<!-- 로그인정보 입력 -->
			<div style="height:20%;width:100%; float:left;">
				<div style="width:53%; height:100%; float:left;  text-align:center">
					<div style="width:100%; float:left; margin-left:33%; margin-top: 5%">
						<input type="text" name="email" id="email" style="border-color:black; width:28%; height:20%"  value="아이디"><br/><br/>
						<input type="password" name="pw" id="pw" style="border-color:black; width:28%; height:20%"  value="비밀번호"><br/>
					
						<div style="width:28%; float:left; margin-left:36%; text-align:left;">
							<input type=checkbox id="emailSave" name="emailSave" value="T" style="border:none;"/><font size="2">아이디 기억하기</font>
							<font size="2" style="margin-left: 5%"><a href="/BEngineer/beMember/beSearchpw.do" style="text-decoration:none">비밀번호 찾기/</a></font>
							<font size="2" ><a href="/BEngineer/beMember/beJoinmember.do" style="text-decoration:none">회원가입</a></font>
						</div>
					</div>
				</div>
				<div style="width:47%; height:100%; float:left;">
					<div style="width:100%; float:left; margin-top: 5.5%">
						<input type="submit" style="border-color:black; width:16%; height:54%;background-color:#FFFFFF; font-size:20pt"  value="Login">
					</div>
				</div>
			</div>
		</form>
		
		<!-- 네이버아이디로그인 -->
		<div id="naverIdLogin" style="height:24%; width:100%; float:left;text-align:center"></div>
		
		<!-- 하단 -->
		<div align="center" style="height:15%; width:100%; float:left; background-color:#5f7f89;"></div>
	</body>

	<!-- 네이버아디디로로그인 초기화 Script -->
	<script type="text/javascript">
		var naverLogin = new naver.LoginWithNaverId(
			{
				clientId: "34hVzh3bD__JqK11Q4sG", //서비스 이용 개발자 아이디
				callbackUrl: "http://192.168.0.153/BEngineer/beMember/beRequestprofile.do", //로그인 정보결과를 보내줄 페이지
				isPopup: false, //팝업을 통한 연동처리 여부
				loginButton: {color: "green", type: 3, height: 60} //로그인 버튼의 타입 지정
			}
		);
		
		naverLogin.init(); //설정정보를 초기화하고 연동을 준비 
	</script>
</html>