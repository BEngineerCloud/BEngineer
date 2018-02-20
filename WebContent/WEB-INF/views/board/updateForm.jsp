<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style type="text/css">
#back {position:relative;}
#in3 {position:absolute;
left:320px;
} 
</style>
<body bgcolor="#00D8F0">
<div id="back">
<div id="in1" style="float:right; height:100%; width:20%;"><input name="logout" type="button" value="로그아웃" onClick="location.href='http://localhost:8080/BEngineer/manager/logout.do'" style="width:100px; height:25px; color:#000000; background-color:#B7F0B1;"/>
</br><a href="/BEngineer/manager/mMain.do">메인</a>
   </br><a href="/BEngineer/board/list.do?id=${sessionScope.Id }">공지사항</a>
   </br><a href="/BEngineer/manager/imposeForm.do">유저제재</a>
   </br><a href="/BEngineer/manager/charge.do">서버구동현황</a>
</div>
<div id="in2">
<form name="updateform" action="update.do" method="post">
<table border="1" bgcolor="#FFFFFF">
  <tr><td width="90" bgcolor="#ABF200">제목</td>
  <td width="150"><input type="text" size="40" maxlength="30" name="title" value="${con.title}"></td></tr>
  
  <tr><td width="90" bgcolor="#ABF200">내 용</td><td  width="300" >
	<textarea name="content" rows="13" cols="35" >${con.content}</textarea><br/></td></tr>

  <tr><td width="90" bgcolor="#ABF200">작성자</td><td  width="300">${con.id}<br/></td></tr>
  
  <input type="hidden" name="num" value="${con.num}">
</table>
</div>
<div id="in3">
<input type="submit" value="작성완료" style="width:80px; height:25px; background-color:#ABF200;"/>
</div>
</form>
</body>