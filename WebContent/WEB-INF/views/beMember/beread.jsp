<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<input name="beboard" type="button" value="���" onClick="location.href='beboard.do'"/>
<table>
  <tr>
    <td  width="90">����</td>
    <td  width="330">
    ${con.title}<br/></td>
  </tr>
  <tr><td></td><td>==============================</td></tr>
  <tr>
    <td  width="90">�� ��</td>
    <td  width="330" >${con.content}
     <br/></td>
  </tr>
  
  <tr><td></td><td>==============================</td></tr>
  </tr>
  <!-- 
  <tr>
  <td width="90">���</td> 
  <td><textarea name="rep" rows="2" cols="40"></textarea></td>
  </tr>
  -->
</table>