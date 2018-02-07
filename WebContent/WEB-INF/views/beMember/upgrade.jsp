<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript" src="http://code.jquery.com/jquery-3.2.0.min.js" ></script>
<script type="text/javascript">
	$(function(){
		$("#change").submit(function(){
			var form = document.getElementById("change");
			if(form.chmod.value == 0 ) {
				alert("용량을 선택해주세요");
				return false;
			}
		});
	});
</script>
<table>
<c:choose>
    <c:when test="${empty giga2}">
    <tr><td>${giga} Byte/1 GB</td></tr><tr><td>현재 사용 가능용량${1000000000-giga} Byte</td></tr>
    </c:when>
    <c:otherwise>
        <tr><td>${giga} Byte/${giga2*10} GB</td></tr><tr><td>현재 사용 가능용량 : ${giga2*10000000000-giga} Byte</td></tr>
    </c:otherwise>
</c:choose>
</table>
<form action="/BEngineer/beMember/change.do" method="post" id="change">
<table>
	<tr><td>10GB<input type="radio" name="chmod" value="1"></td><td>50000원</td></tr> 
	<tr><td>30GB<input type="radio" name="chmod" value="3"></td><td>100000원</td></tr>
	<tr><td>기간 1년</td></tr>
	<tr><td>계좌</td><td>
	<select name="account" size="1">
	<option>선택</option>
	<option value="s">신한은행</option>
	<option valur="n">NH농협</option>
	<option value="w">우리은행</option>
	<option valur="h">하나은행</option>
	<option value="i">IBK기업은행</option>
	<option valur="k">KB 국민은행</option>
	</select>
	</td>
	<td><input type="text" size="3" name="account1"/>-<input type="text" size="3" name="account2"/>-<input type="text" size="3" name="account3"/>-<input type="text" size="3" name="account4"/></td>
	</tr>
</table>
	<input type="hidden" name="id" value="${id}"/>
	<input type="submit" value="결제"/>
</form>
