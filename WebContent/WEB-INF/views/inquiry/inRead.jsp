<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<input name="inList" type="button" value="���" onClick="http://localhost:8080/BEngineer/inquiry/inList.do'"/>
<table>
  <tr>
    <td  width="90">����</td>
    <td  width="330">
    ${re.title}<br/></td>
  </tr>
  <tr><td></td><td>==============================</td></tr>
  <tr>
    <td  width="90">�� ��</td>
    <td  width="330" >${re.content}
     <br/></td>
  </tr>
  
  <tr><td></td><td>==============================</td></tr>
  </tr>
    <tr>
    <td  width="90">�亯</td>
    <td  width="330" >${re.reply}
     <br/></td>
  </tr>
</table>
