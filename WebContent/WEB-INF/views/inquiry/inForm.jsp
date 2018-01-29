<%@ page contentType="text/html; charset=UTF-8"%>
    <h2>dd${Id}</h2>
<form name="inForm" action="inPro.do" method="post">
<table border="1">
  <tr>
    <td  width="110">제목</td>
    <td  width="300">
    <input type="text" size="40" maxlength="30" name="title"><br/></td>
  </tr>
  
  <tr>
    <td  width="110">문의 내 용</td>
    <td  width="300" >
     <textarea name="content" rows="13" cols="40"></textarea><br/></td>
  </tr>
  
  <!-- <tr>
  	<td><input type="file" size="40" maxlength="30" name="title">첨부파일<br/></td>
  </tr> -->
  
  <tr>
  <td  width="100">작성자</td>
   <!-- <td  width="300"> <input type="text" size="40" maxlength="30" name="title"><br/></td>-->
     <td>${Id}<input type="hidden" value="${Id}" name="Id"><br/></td> 
  </tr>
  
  <tr>
	<td><input type="submit" value="작성완료"/></td>
  </tr>
</table>
</form>