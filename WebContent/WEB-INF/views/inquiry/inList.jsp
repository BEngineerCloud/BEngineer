<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<title>문의내역</title>
<h4>문의내역</h4>
<table>
<tr>
<td align="center">글번호</td>
<td align="center">제목</td>
<td align="center">작성자</td>
<td align="center">작성일</td>
</tr>
<c:forEach var="i" items="${list}">
<tr>
<td width="30" align="center">${inList.num}</td> 
<td width="100"><a href="/BEngineer/inquiry/inRead.do?num=${inList.num}">${inList.title}</a></td> 
<!-- <td>${board.content}</td>  -->
<td width="50">${inList.id}</td> 
<td>${inList.reg_date}</td><br/>
</tr>

</c:forEach>   
<tr><td></td><td></td><td></td>
<td><input name="inList" type="button" value="문의하기" onClick="location.href='inForm.do?id=${Id}'"/>
</td></tr>
</table>