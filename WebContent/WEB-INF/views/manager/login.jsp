<%@ page contentType="text/html; charset=UTF-8"%>
<script type="text/javascript" src="http://code.jquery.com/jquery-3.2.0.min.js" ></script>
<script type="text/javascript">
$(function(){
	$("#login").submit(function(){ // 필터링 단어 추가 / 해제 시
		if(!login.id.value || !login.pw.value){
			alert("다시 입력해주세요");
		return false;
		}else{
			submit();
		}
	});
});
</script>
<style>	 h2{color:Green;} </style>
<h2 align="center">Manager Login</h2>
<form action="loginPro.do" id="login">
<table align="center">
<tr><td>&nbsp;I D : </td><td><input type="text" name="id"/></td></tr>
<tr><td>PW : </td><td><input type="password" name="pw"/></td></tr>
<tr><td  align="center" colspan="2"><input type="submit" value="LOGIN"  style="width:90px; height:25px; color:#000000; background-color:#B7F0B1;"/></td></tr>
<tr><td  align="center" colspan="2"><input name="user" type="button" value="사용자전환" onClick="location.href='http://localhost:8080/BEngineer/beMain.do'" style="width:90px; height:25px; color:#000000; background-color:#B7F0B1;"/></td></tr>
</table>
</form>

