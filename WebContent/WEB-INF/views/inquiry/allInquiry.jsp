<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %><!-- 날짜표현하기위해.. -->
<table border="1">
<h3>${allList.size()}개의 문의가 있습니다.</h3>
<tr>
<td align="center">글번호</td>
<td align="center">제목</td>
<td align="center">작성자</td>
<td align="center">작성일</td>
<td align="center">답변</td>
</tr>
<c:forEach var="all" items="${allList}">
<tr>
<td width="50" align="center">${all.num}</td> 
<td width="200"><a href="/BEngineer/inquiry/replyForm.do?num=${all.num}">${all.title}</a></td> 
<!-- <td>${board.content}</td>  -->
<td width="100">${all.id}</td> 
<td>
<fmt:formatDate value="${all.reg_date}" pattern="MM-dd HH:mm"/>
</td><br/>
<c:choose>
    <c:when test="${empty all.reply}">
        <td>미답변</td>
    </c:when>
    <c:otherwise>
        <td>답변완료</td>
    </c:otherwise>
</c:choose>
</tr>
</c:forEach>   
</table>


