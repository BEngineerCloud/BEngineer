<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>	
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %><!-- ��¥ǥ���ϱ�����.. -->
<table>
<tr>
<td align="center" >�۹�ȣ</td>
<td align="center">����</td>
<td align="center">�ۼ���</td>
<td align="center">�ۼ���</td>
</tr>
<c:forEach var="board" items="${list}">
<tr>
<td width="100" align="center">${board.num}</td> 
<td width="100"><a href="/BEngineer/beMember/beread.do?num=${board.num}">${board.title}</a></td> 
<!-- <td>${board.content}</td>  -->
<td width="50">${board.id}</td> 
<td><fmt:formatDate value="${board.reg_date}" pattern="MM-dd HH:mm"/></td><br/>
</tr>
</c:forEach>   
</table>
    