<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
    <h2>dd${Id}</h2>
<form name="inForm" action="inPro.do" method="post">
<table border="1">
  <tr>
    <td  width="110">����</td>
    <td  width="300">
    <input type="text" size="40" maxlength="30" name="title"><br/></td>
  </tr>
  
  <tr>
    <td  width="110">���� �� ��</td>
    <td  width="300" >
     <textarea name="content" rows="13" cols="40"></textarea><br/></td>
  </tr>
  
  <!-- <tr>
  	<td><input type="file" size="40" maxlength="30" name="title">÷������<br/></td>
  </tr> -->
  
  <tr>
  <td  width="100">�ۼ���</td>
   <!-- <td  width="300"> <input type="text" size="40" maxlength="30" name="title"><br/></td>-->
     <td>${Id}<input type="hidden" value="${Id}" name="Id"><br/></td> 
  </tr>
  
  <tr>
	<td><input type="submit" value="�ۼ��Ϸ�"/></td>
  </tr>
</table>
</form>