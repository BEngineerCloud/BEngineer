<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %><!-- ��¥ǥ���ϱ�����.. -->

<h2>${Id}</h2>
<table border="1">
<!-- <a href="/BEngineer/board/writeForm.do?id="${ID}">���������ۼ�</a>  -->
<input name="write" type="button" value="���������ۼ�" onClick="location.href='writeForm.do?id=${Id}'"/>
<tr>
<td align="center">�۹�ȣ</td>
<td align="center">����</td>
<td align="center">�ۼ���</td>
<td align="center">�ۼ���</td>
</tr>
<c:forEach var="board" items="${list}">
<tr>
<td width="30" align="center">${board.num}</td> 
<td width="100"><a href="/BEngineer/board/updateForm.do?num=${board.num}">${board.title}</a></td> 
<!-- <td>${board.content}</td>  -->
<td width="50">${board.id}</td> 
<td>
<fmt:formatDate value="${board.reg_date}" pattern="MM-dd HH:mm"/>
</td><br/>
<td>
<input name="delete" type="button" value="����" onClick="location.href='delete.do?num=${board.num}'"/>
</td>
</tr>

</c:forEach>   
</table>

<!-- <input name="delete" type="button" value="����" onClick="javascript:open_win_noresizable('delete.do?num=${board.num}')"> -->
    
    
   


