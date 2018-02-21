<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- 이메일 인증확인 페이지 -->
<html>
	<head>
	<title>beConfirmemail</title>
	</head>
	
	<script type="text/javascript" src="http://code.jquery.com/jquery-3.2.0.min.js"></script>
	
	<script type="text/javascript">
		$(function(){
			
			 //confirmEmail이 0 일시 beJoinmember페이지로 mailid와 checkConfirmEmail 값을 보낸다.
			 if("${confirmEmail }"==0){
					alert("메일인증이 완료되었습니다.");
					window.location = "/BEngineer/beMember/beJoinmember.do?mailid="+"${mailid }"+"&checkConfirmEmail=true";
			}else{ //confirmEmail이 0 이 아닐 시
				alert("메일인증에 실패했습니다.");
				window.location="/BEngineer/beMember/beJoinmember.do"		
			}  
		});
	</script>
</html>
