<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>

<title>beChecknickname</title>
</head>
<body>
<c:if test="${check!=0 }">
	${nickname }은 이미 사용중입니다.
</c:if>
</body>
</html>