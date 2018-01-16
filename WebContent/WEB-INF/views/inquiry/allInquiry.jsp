<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %><!-- ��¥ǥ���ϱ�����.. -->

<h2>${allList.size()}���� ���ǰ� �ֽ��ϴ�.</h2>
<table border="1">
<tr>
<td align="center">�۹�ȣ</td>
<td align="center">����</td>
<td align="center">�ۼ���</td>
<td align="center">�ۼ���</td>
<td align="center">�亯</td>
</tr>
<c:forEach var="all" items="${allList}">
<tr>
<td width="30" align="center">${all.num}</td> 
<td width="100"><a href="/BEngineer/inquiry/replyForm.do?num=${all.num}">${all.title}</a></td> 
<!-- <td>${board.content}</td>  -->
<td width="50">${all.id}</td> 
<td>
<fmt:formatDate value="${all.reg_date}" pattern="MM-dd HH:mm"/>
</td><br/>
<c:choose>
    <c:when test="${empty all.reply}">
        <td>�̴亯</td>
    </c:when>
    <c:otherwise>
        <td>�亯�Ϸ�</td>
    </c:otherwise>
</c:choose>
</tr>
</c:forEach>   
</table>


