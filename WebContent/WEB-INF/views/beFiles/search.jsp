<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<h4>${file}</h4> 
<c:forEach var="search" items="${file}">
${search.num }
</c:forEach>
