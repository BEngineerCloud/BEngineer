<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<h2>${re.num}����</h2>
<table border="1">
  <tr>
    <td  width="90">����</td>
    <td  width="330">${re.title}<br/></td>
  </tr>
  
  <tr>
    <td  width="90">�� ��</td>
    <td  width="330" >${re.content}<br/></td>
  </tr>
  
  <tr>
  <td  width="90">�ۼ���</td>
    <td  width="330">${re.id}<br/></td>
  </tr>
  
</table>
<form name="replyform" action="reply.do" method="post">
<input type="hidden" name="num" value="${re.num}">
	
  <tr>
  <td><textarea name="reply" rows="13" cols="40" ></textarea></td>
  </tr>
  <tr>	
	<td><input type="submit" value="�亯�Ϸ�"/></td>
  </tr>
  
</form>   
    