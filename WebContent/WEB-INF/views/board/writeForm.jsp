<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!-- http://localhost:8080/BEngineer/board/writeForm.do -->
<title>�ۼ�</title>
<form name="writeform" action="writePro.do" method="post">
<table border="1">
  <tr>
    <td  width="90">����</td>
    <td  width="300">
    <input type="text" size="40" maxlength="30" name="title"><br/></td>
  </tr>
  
  <tr>
    <td  width="90">�� ��</td>
    <td  width="300" >
     <textarea name="content" rows="13" cols="40"></textarea><br/></td>
  </tr>
  
  <tr>
  <td  width="90">�ۼ���</td>
   <!-- <td  width="300"> <input type="text" size="40" maxlength="30" name="title"><br/></td>-->
     <td>${Id}<input type="hidden" value="${Id}" name="id"><br/></td> 
  </tr>
  
  <tr>
	<td><input type="submit" value="�ۼ��Ϸ�"/></td>
  </tr>
</table>
</form>

