<%@ page contentType="text/html; charset=UTF-8"%>
<input name="inList" type="button" value="목록" onClick="http://localhost:8080/BEngineer/inquiry/inList.do'"/>
<table>
  <tr>
    <td  width="90">제목</td>
    <td  width="330">
    ${re.title}<br/></td>
  </tr>
  <tr><td></td><td>==============================</td></tr>
  <tr>
    <td  width="90">내 용</td>
    <td  width="330" >${re.content}
     <br/></td>
  </tr>
  
  <tr><td></td><td>==============================</td></tr>
  </tr>
    <tr>
    <td  width="90">답변</td>
    <td  width="330" >${re.reply}
     <br/></td>
  </tr>
</table>
