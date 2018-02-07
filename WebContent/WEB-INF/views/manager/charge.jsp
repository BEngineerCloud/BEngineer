<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<Table>
<tr><td width="100">사용자</td><td align="right">용량</td></tr>
<c:forEach var="list" items="${list}">
<tr><td width="100">${list.owner }</td>
<c:choose>
    <c:when test="${list.filesize  == 0}">
    <td align="right">x</td>
    </c:when>
    <c:when test="${list.filesize  > 10000}">
    <td align="right"><fmt:formatNumber value="${list.filesize/1024}" pattern="##.#"/> MByte</td> <!-- 소숫점한자리까지 -->
    </c:when>
    <c:otherwise>
        <td align="right"> ${list.filesize} KByte</td>
    </c:otherwise>
</c:choose>
</tr>
</td></tr>
</c:forEach>   
<tr><td width="100">전체</td><td align="right">${all }MByte</td></tr>
</Table>

