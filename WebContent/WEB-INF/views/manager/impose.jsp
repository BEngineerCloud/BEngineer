<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>	
<h2>회원 정보 검색&확인</h2>
	<form action="/BEngineer/manager/imposePro.do" method="post">
		ID검색 <input type="text" name="email"/>
		기간 <input type="text" name="term"/>
			 <input type="submit" value="검색"/><br/>
	</form>
