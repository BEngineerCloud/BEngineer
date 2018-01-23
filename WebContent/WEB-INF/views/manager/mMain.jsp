<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<script type="text/javascript" src="http://code.jquery.com/jquery-3.2.0.min.js" ></script>
<script type="text/javascript">
	$(function(){
		$("form").submit(function(){ // 내 파일보기 버튼 클릭 시 페이지 이동
			var id = $(this).attr("id");
			var form = document.getElementById(id);
			form.filtering.value = form.filtering.value.trim();
			if(!form.filtering.value || form.filtering.value == ""){
				alert("필터링 단어를 입력해주세요");
				return false;
			}
		});
	});
</script>
<%
 try{
   if(session.getAttribute("Id")==null){%>
    	response.sendRedirect("login.jsp"); <!-- 수정할것 -->
        <%}else{%>
    	<h2>로그인</h2>    
    	<input name="logout" type="button" value="로그아웃" onClick="location.href='logout.do'"/>	
        <a href="/BEngineer/inquiry/allInquiry.do">문의내역</a>
        <a href="/BEngineer/board/list.do?id=<%=session.getAttribute("Id")%>">공지사항</a>
        <a href="/BEngineer/manager/impose.do">유저제재</a>
        <a href="/BEngineer/manager/">유료전환</a>
        <form id="addfilterform" action="/BEngineer/manager/addFilter.do">
        	파일명 필터 추가하기 : 
        	<input type="text" name="filtering" />
        	<input type="submit" value="필터 추가하기" />
        </form>
        <form id="removefilterform" action="/BEngineer/manager/removeFilter.do">
        	파일명 필터 해제하기 : 
        	<input type="text" name="filtering" />
        	<input type="submit" value="필터 해제하기" />
        </form>

         <%}
 }catch(NullPointerException e){}
 %>

 
    