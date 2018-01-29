<%@ page contentType="text/html; charset=UTF-8"%>
<h2>유저제제</h2>
	<form action="/BEngineer/manager/imposePro.do" method="post">
		제제할 회원<input type="text" name="email"/><br/>
		제제사유<input type="text" name="cause" value="x"/><br/>
		기간 <input type="text" name="term"/>일<br/>
			 <input type="submit" value="확인"/>
	</form>
