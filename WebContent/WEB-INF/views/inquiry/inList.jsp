<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<title>���ǳ���</title>
<h4>${Id}���� ���ǳ����Դϴ�.</h4>
<table>
<tr>
<td align="center">�۹�ȣ</td>
<td align="center">����</td>
<td align="center">�ۼ���</td>
<td align="center" width="100">�ۼ���</td>
<td align="center">�亯</td>
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
        <td>�̴亯</td>
    </c:when>
    <c:otherwise>
        <td>�亯�Ϸ�</td>
    </c:otherwise>
</c:choose>
</tr>

</c:forEach>   
<tr><td></td><td></td><td></td>
<td><input name="inList" type="button" value="�����ϱ�" onClick="location.href='inForm.do?id=${Id}'"/>
</td></tr>
</table>