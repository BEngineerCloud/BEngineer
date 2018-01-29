<%@ page contentType="text/html; charset=UTF-8"%>
<input name="beboard" type="button" value="목록" onClick="location.href='beboard.do'"/>
<table>
  <tr>
    <td  width="90">제목</td>
    <td  width="330">
    ${con.title}<br/></td>
  </tr>
  <tr><td></td><td>==============================</td></tr>
  <tr>
    <td  width="90">내 용</td>
    <td  width="330" >${con.content}
     <br/></td>
  </tr>
  
  <tr><td></td><td>==============================</td></tr>
  </tr>
  <!-- 
  <tr>
  <td width="90">댓글</td> 
  <td><textarea name="rep" rows="2" cols="40"></textarea></td>
  </tr>
  -->
</table>