<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<title>beCheckmailid3</title>
</head>
<script type="text/javascript" src="http://code.jquery.com/jquery-3.2.0.min.js"></script>
<script type="text/javascript" src="https://cdn.jsdelivr.net/jquery.validation/1.16.0/jquery.validate.min.js"></script>
<script type="text/javascript">
$(function(){
	 $(document).ready(function(){
		 if("${confirmEmail }"==0){
				alert("메일인증이 완료되었습니다.");
				window.location = "/BEngineer/beMember/beJoinmember.do?mailid="+"${mailid }"+"&checkConfirmEmail=true";
		}else{
			alert("메일인증에 실패했습니다.");
			window.location="/BEngineer/beMember/beJoinmember.do"
			
		}
    });
});
</script>
<body>
</body>
</html>
