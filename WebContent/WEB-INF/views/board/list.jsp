<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %><!-- 날짜표현하기위해.. -->

<h2>${Id}</h2>
<table border="1">
<!-- <a href="/BEngineer/board/writeForm.do?id="${ID}">공지사항작성</a>  -->
<input name="write" type="button" value="공지사항작성" onClick="location.href='writeForm.do?id=${Id}'"/>
<tr>
<td align="center">글번호</td>
<td align="center">제목</td>
<td align="center">작성자</td>
<td align="center">작성일</td>
</tr>
<c:forEach var="board" items="${list}">
<tr>
<td width="30" align="center">${board.num}</td> 
<td width="100"><a href="/BEngineer/board/updateForm.do?num=${board.num}">${board.title}</a></td> 
<!-- <td>${board.content}</td>  -->
<td width="50">${board.id}</td> 
<td>
<fmt:formatDate value="${board.reg_date}" pattern="MM-dd HH:mm"/>
</td><br/>
<td>
<input name="delete" type="button" value="삭제" onClick="location.href='delete.do?num=${board.num}'"/>
</td>
</tr>

</c:forEach>   
</table>

<!-- <input name="delete" type="button" value="삭제" onClick="javascript:open_win_noresizable('delete.do?num=${board.num}')"> -->
    
    
   


