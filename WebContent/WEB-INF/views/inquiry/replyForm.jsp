<%@ page contentType="text/html; charset=UTF-8"%>
<table border="1">
  <tr>
    <td  width="90">제목</td>
    <td  width="330">${re.title}<br/></td>
  </tr>
  
  <tr>
    <td  width="90">내 용</td>
    <td  width="330" >${re.content}<br/></td>
  </tr>
  
  <tr>
  <td  width="90">작성자</td>
    <td  width="330">${re.id}<br/></td>
  </tr>
  
</table>
<form name="replyform" action="reply.do" method="post">
<input type="hidden" name="num" value="${re.num}">
	
  <tr>
  <td><textarea name="reply" rows="13" cols="40" ></textarea></td>
  </tr>
  <tr>	
	<td><input type="submit" value="작성"/></td>
  </tr>
  
</form>   
    