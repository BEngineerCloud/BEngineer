<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src='/BEngineer/resources/js/jquery-1.8.2.min.js' type='text/javascript'></script>
<script type="text/javascript">
	$(function(){
		
		//imageview의 레이아웃, 정해진 크기를 벗어난 그림이면 자동으로 스크롤을 만들어준다.
		$("body").append("<div id='glayLayer' ></div><div id='overLayer' style='overflow-y:auto;'></div>");
		
		//imageview 밖의 화면을 클릭하면 이미지화면 끄기
		$("#glayLayer").click(function(){
			$(this).hide()
			$("#overLayer").hide();
		});
		
		//imageview 버튼을 클릭했을 때 정해진 크기에 맞게 보여주기
		$("#image").click(function(){
			$("#glayLayer").show();
			$("#overLayer").show().html("<img src=\"/BEngineer/inquiryImg/${re.filename }\" style=\"width:100%; \" />");
			return false;
		});
	});
</script>
	<!-- 이미지를 클릭해서 보여줄 스타일 -->
	<style type="text/css">
		html,body{
			margin:0;
			padding:0;
			height:100%;
		}
		
		div#glayLayer{
			display:none;
			position:fixed;
			left:0;
			top:0;
			height:100%;
			width:100%;
			background:black;
			filter:alpha(opacity=60);
			opacity: 0.60;
		}
		
		* html div#glayLayer{
			position:absolute;
		}
		
		#overLayer{
			display:none;
			position: absolute;
			top:10%;
			left:20%;
			max-height:80%;
			width:60%
		}
		
		* html #overLayer{
			position: absolute;
		}
	</style>
<body bgcolor="#00D8F0">
	<div id="in1" style="float:right; height:100%; width:20%;"><input name="logout" type="button" value="로그아웃" onClick="location.href='logout.do'" style="width:100px; height:25px; color:#000000; background-color:#B7F0B1;"/>
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
	  <c:if test="${re.filename != null }">
	  <tr><td width="90">파일</td>
	    <td width="330"><img src="/BEngineer/inquiryImg/${re.filename }" width="250" height="250" id="image" style="cursor:pointer;"/></td></tr>
	  </c:if>
	</table>
	<form name="replyform" action="reply.do" method="post">
	<input type="hidden" name="num" value="${re.num}">
	  <tr><td><textarea name="reply" rows="13" cols="40" ></textarea></td></tr></br>
	  <tr><td><input type="submit" value="답변완료" style="width:80px; height:30px; color:#000000; background-color:#1DDB16;"/></td></tr>  
	</form>
	</div>   
</body>