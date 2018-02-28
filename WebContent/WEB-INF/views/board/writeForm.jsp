<%@ page contentType="text/html; charset=UTF-8"%>
<style type="text/css">
#back {position:relative;}
#in2 {position:absolute;
left:180px;
} 
</style>
<body bgcolor="#00D8F0">
<div style="float:right; height:100%; width:20%;"><input name="logout" type="button" value="로그아웃" onClick="location.href='logout.do'" style="width:100px; height:25px; color:#000000; background-color:#B7F0B1;"/>
</br><a href="/BEngineer/manager/mMain.do">메인</a>
   </br><a href="/BEngineer/board/list.do?id=${sessionScope.Id }">공지사항</a>
   </br><a href="/BEngineer/manager/imposeForm.do">유저제재</a>
   </br><a href="/BEngineer/manager/charge.do">서버구동현황</a>
</div>
<div id="back" style="float:right; height:100%; width:80%;">
	<div id="in1" style="height:10%; width:100%;">
	<form name="writeform" action="writePro.do" method="post">
	<table border="1" bgcolor="#FFFFFF">
	  <tr><td  width="90">제목</td><td  width="300">
	    <input type="text" size="40" maxlength="30" name="title"><br/></td></tr>
	  
	  <tr><td  width="90">내 용</td><td  width="300" >
		<textarea name="content" rows="13" cols="34"></textarea><br/></td></tr>
	  
	  <tr><td  width="90">작성자</td>
		<td>${Id}<input type="hidden" value="${Id}" name="id"><br/></td> </tr>
	  </table>
	 </div>
	 
	 <div id="in2" style="right:100px; height:100%; width:20%; top:280px;">
		<input type="submit" value="작성완료" style="width:80px; height:30px; color:#000000; background-color:#1DDB16;"/>
	 </div>
</form>
</div>
</body>