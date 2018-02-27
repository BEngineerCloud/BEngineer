<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>   
<!-- 정보수정 페이지 --> 

<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>beAddInfo</title>
	<script type="text/javascript" src="http://code.jquery.com/jquery-3.2.0.min.js"></script>
	
	<!-- javascript 유효성검사 script -->
	<script type="text/javascript" src="https://cdn.jsdelivr.net/jquery.validation/1.16.0/jquery.validate.min.js"></script>
	</head>
	
	<!-- 미입력시 뜨는 validate의 색상,크기 설정 -->
	<style type="text/css">
		label.error{
			color:red;
			font-size: 10pt;
		}
	</style>

	<script type="text/javascript">
		$(function(){
			$("#confirmNickname").click(function(){ //닉네임 중복검사 버튼을 클릭했을 시
					
				//닉네임 값이 공백일 시 포커스를 주고 문구를 띄워준다.	
				if($("#nickname").val()==""){
					$("#nickname").focus();
					alert("닉네임을 입력해주세요.");
					return false;
				}
					
				//해당하는 페이지를 정해진 크기의 창으로 띄운다.
				url = "/BEngineer/beMember/beChecknickname.do?nickname="+$("#nickname").val(); //닉네임 값을 같이 넘겨준다.
				open(url, "confirm",  "toolbar=no, location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=300, height=200");
			});
			
			$("#nickname").keydown(function(){ //닉네임 입력창에 키를 눌렀을 시
				var nickname = $("#nickname").val(); //닉네임 값 대입
				
				$("#confirmNickname").prop("disabled",true); //닉네임 중복검사 버튼 비활성화
				
				//닉네임의 길이가 2글자 이상 10글자 이하일 경우에만 중복검사 버튼 활성화
				if(nickname.length>1 && nickname.length<11)
					$("#confirmNickname").prop("disabled",false);
			});
			
			$("#beAddinfo").validate({ //정보수정창 validate
				rules:{ //규칙
					newPw:{required:false},
					confirmNewpw:{required:false},
					nickname:{required:true, rangelength:[2,10]} //최소길이가 2, 최대길이가 10인지
				},
				messages:{ //규칙을 위반했을 시 메시지
					nickname:{
						required:"닉네임을 입력하세요.",
						rangelength:"2글자 이상, 10글자 이하여야 합니다."
					}
				}
				
			});
		
			$("#beAddinfo").submit(function(){ //정보수정창을 전송할 때
				var regNewpw = /^(?=.*[a-zA-Z])(?=.*[!@#$%^~*+=-\|;,./?])(?=.*[0-9]).{8,20}$/; //비밀번호 정규식
				
				if($("#newPw").val()!=""){ //새 비밀번호 값이 존재할 때
					if(!regNewpw.test($("#newPw").val())) { //새 비밀번호가 유효성 검사에 맞지 않을 시 새 비밀번호 창에 포커스를 주고 문구 띄우기
						$("#newPw").focus(); 
						alert("영문자,숫자,특수문자 조합으로 8~20자리 사이의 비밀번호를 입력해주세요."); 
				      	return false; 
				 	}else{ //새 비밀번호가 유효성 검사에 맞을 시 새 비밀번호 창에 값 입력
				 		var newPw1 =$("#newPw").val();
				 		$("#pw").val(newPw1);
				 	}
				}
				
				if($("#newPw").val()!=$("#confirmNewpw").val()){ //비밀번호 확인 값이 일치하지 않을 시
					alert("새 비밀번호가 일치하지 않습니다.");
					return false;
				}
				
				if($("#nickname").val()==""){ //닉네임 값이 입력되지 않았을 시
					alert("닉네임을 입력해주세요.");
					return false;
				}
				
				if($("#checkConfirmnickname").val()==""){ //닉네임 중복검사를 하지 않았을 시
					alert("닉네임 중복검사를 해주세요.");
					return false;
				}
				return true;
			});
			
			$("#cancel").click(function(){ //취소버튼을 클릭했을 시
				window.location = "/BEngineer/beMain.do";
			});
			
			$("#delete").click(function(){ //탈퇴버튼을 클릭했을 시
				var is_Delete = confirm("정말 탈퇴하시겠습니까?"); //문구를 띄어 확인을 받는다.
				
				//확인 버튼을 누르면 탈퇴작업을 수행한다.
				if(is_Delete==true)
					window.location = "/BEngineer/beMember/beDeletemember.do?email=${memberDTO.email}";
			});
		});
	</script>
	<body topmargin="0" bottommargin="0" leftmargin="0" rightmargin="0">
		<form id="beAddinfo" name="beAddinfo" method="post" action="/BEngineer/beMember/beAddinfopro.do">
			
			<!-- 로고, 문구 -->
			<div  align="center" style="height:33%; width:100%; float:left;">
				<div style="height:12%; width:30%;  margin-top: 5%">
					<img src="\BEngineer\image\beCloudLogo.png" style="width: 50%;"/><br/><br/><hr color="black"><br/>
					<font size="5">추가 정보 입력</font>
				</div>
			</div>
			
			<!-- 정보수정 입력 -->
			<div align="center" style="height:52%; width:100%; float:left;">
				<div align="left" style="height:50%; width:30%;">
					<div id="Addcontents3" style="height:20%; width:35%; float:left;">
						<div align="center" style="height:20%; width:100%; float:left; ">
							<font size="4">이메일 아이디</font>
						</div>
					</div>
					
					<div style="height:20%; width:65%; float:left;">
						<div style="height:20%; width:100%; float:left; ">
							<font size="4">${memberDTO.email}</font>
						</div>
					</div>
					
					<div style="height:20%; width:35%; float:left;">
						<div align="center" style="height:20%; width:100%; float:left; ">
							<font size="3">새 비밀번호(필수입력 x)</font>
						</div>
					</div>
					
					<div style="height:20%; width:65%; float:left;">
						<div style="height:60%; width:100%; float:left; ">
						<input type="hidden" id="pw" name="pw" value="${memberDTO.pw }"/>
							<input type="password" id="newPw" name="newPw" style="border-color:black; width:50%; height:100%" required="required"/>
							<label class="error" for="newPw" generated="true" style="display:none;">에러태그</label>
						</div>
					</div>
					
					<div style="height:20%; width:35%; float:left;">
						<div align="center" style="height:20%; width:100%; float:left; ">
							<font size="3">새 비밀번호 확인</font>
						</div>
					</div>
					
					<div style="height:20%; width:65%; float:left;">
						<div style="height:60%; width:100%; float:left; ">
							<input type="password" id="confirmNewpw" name="confirmNewpw" style="border-color:black; width:50%; height:100%" required="required"/>
							<label class="error" for="confirmNewpw" generated="true" style="display:none;">에러태그</label>
						</div>
					</div>
					
					<hr color="black"><br/>
					
					<div style="height:70%; width:35%; float:left;">
						<div align="center" style="height:20%; width:100%; float:left; margin-top: 5%">
							<font size="4">닉네임</font>
						</div>
						
						<div align="center" style="height:20%; width:100%; float:left; margin-top: 9.5%">
							<font size="4">성별</font>
						</div>
						
						<div align="center" style="height:20%; width:100%; float:left; margin-top: 9.5%">
							<font size="4">생년월일</font>
						</div>
					</div>
					
					<div style="height:70%; width:65%; float:left;">
						<div style="height:20%; width:100%; float:left; margin-top: 2.3%">
							<input type="text" id="nickname" name="nickname" style="border-color:black; width:50%; height:90%" required="required"/>
							<input type="button" id="confirmNickname" style="height:100%; border-color: black; background-color:#FFFFFF;"value="중복검사"><br/>
							<label class="error" for="nickname" generated="true" style="display:none;">에러태그</label>
							<input type="hidden" id="checkConfirmnickname" value="">
						</div>	
						
						<div style="height:20%; width:100%; float:left; margin-top: 6%; margin-left:20%">
							<c:if test="${memberDTO.gender=='M'}">
			    				<font size="4">남성</font>
			   				</c:if>
			   				<c:if test="${memberDTO.gender=='W'}">
			    				<font size="4">여성</font>
			   				</c:if>
						</div>
						
						<div style="height:20%; width:100%; float:left; margin-top: 6%; margin-left:20%">
			    			<font size="4">${memberDTO.birthday}</font>
						</div>
					</div>
				</div>
				
				<div align="center" id="Addcontents2" style="height:50%; width:30%;">
					<hr color="black"><br/>
				
					<!-- 수정, 취소, 탈퇴버튼 -->
					<input type="submit" style="border-color:black;  width:12%; height:16%;background-color:#FFFFFF; font-size:16pt"  value="확인">
					&emsp;<input type="button" id="cancel" style="border-color:black;  width:12%; height:16%;background-color:#FFFFFF; font-size:16pt"  value="취소" >
					&emsp;<input type="button" id="delete" style="border-color:black;  width:12%; height:16%;background-color:#FFFFFF; font-size:16pt"  value="탈퇴" >
				</div>
			</div>
			
			<div align="center" style="height:15%; width:100%; float:left; background-color:#5f7f89;">
				<center>
					<a href="/BEngineer/inquiry/inList.do?id=${sessionScope.id}" style="text-decoration:none">문의내역</a>
        			<a href="/BEngineer/beMember/beboard.do?id=${sessionScope.id}" style="text-decoration:none">공지사항</a>
        			<a href="/BEngineer/beMember/upgrade.do?id=${sessionScope.id}" style="text-decoration:none">유료전환</a>
     			</center>
     		</div>
		</form>
	</body>
</html>