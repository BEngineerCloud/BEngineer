<%@ page contentType="text/html; charset=UTF-8"%>
<body bgcolor="#00D8F0">
	<div id="in1" style="float:right; height:100%; width:20%;"><input name="logout" type="button" value="로그아웃" onClick="location.href='http://localhost:8080/BEngineer/manager/logout.do'" style="width:100px; height:25px; color:#000000; background-color:#B7F0B1;"/>
	</br><a href="/BEngineer/manager/mMain.do">메인</a>
    </br><a href="/BEngineer/board/list.do?id=${sessionScope.Id }">공지사항</a>
    </br><a href="/BEngineer/manager/imposeForm.do">유저제재</a>
    </br><a href="/BEngineer/manager/charge.do">서버구동현황</a>
	</div>
	<div>
	<table border="1">
	  <tr><td  width="90">제목</td><td  width="330">${re.title}<br/></td></tr> 
	  <tr><td  width="90">내 용</td><td  width="330" >${re.content}<br/></td></tr>
	  <tr><td  width="90">작성자</td><td  width="330">${re.id}<br/></td></tr>
	</table>
	<form name="replyform" action="reply.do" method="post">
	<input type="hidden" name="num" value="${re.num}">
	  <tr><td><textarea name="reply" rows="13" cols="40" ></textarea></td></tr></br>
	  <tr><td><input type="submit" value="답변완료" style="width:80px; height:30px; color:#000000; background-color:#1DDB16;"/></td></tr>  
	</form>
	</div>   
</body>