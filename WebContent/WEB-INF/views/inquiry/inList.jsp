<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<title>문의내역</title>
<h4>${Id}님의 문의내역입니다.</h4>
<table>
<tr>
<td align="center">글번호</td>
<td align="center">제목</td>
<td align="center">작성자</td>
<td align="center" width="100">작성일</td>
<td align="center">답변</td>
</tr>
<c:forEach var="inList" items="${inList}">
<tr>
<td width="30" align="center">${inList.num}</td> 
<td width="100" align="center"><a href="/BEngineer/inquiry/inRead.do?num=${inList.num}">${inList.title}</a></td> 
<!-- <td>${board.content}</td>  -->
<td width="50" align="center">${inList.id}</td> 
<td width="200" align="center"><fmt:formatDate value="${inList.reg_date}" pattern="MM-dd HH:mm"/></td><br/>
<c:choose>
    <c:when test="${empty inList.reply}">
        <td>미답변</td>
    </c:when>
    <c:otherwise>
        <td>답변완료</td>
    </c:otherwise>
</c:choose>
</tr>

</c:forEach>   
<tr><td></td><td></td><td></td>
<td><input name="inList" type="button" value="문의하기" onClick="location.href='inForm.do?id=${Id}'"/>
</td></tr>
</table>