<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<h2>${con.num}����</h2>
<form name="updateform" action="update.do" method="post">
<table border="1">
  <tr>
    <td  width="90">����</td>
    <td  width="330">
    <input type="text" size="40" maxlength="30" name="title" value="${con.title}"><br/></td>
  </tr>
  
  <tr>
    <td  width="90">�� ��</td>
    <td  width="330" >
     <textarea name="content" rows="13" cols="40" >${con.content}</textarea><br/></td>
  </tr>
  
  <tr>
  <td  width="90">�ۼ���</td>
    <td  width="330">${con.id}<br/></td>
  </tr>
  
  <tr>
	<td><input type="submit" value="�ۼ��Ϸ�"/></td>
  </tr>
  <input type="hidden" name="num" value="${con.num}">
</table>
</form>   
    
    