<%@ page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript" src="http://code.jquery.com/jquery-3.2.0.min.js" ></script>
<script type="text/javascript">
	$(function(){
		$(".filterform").submit(function(){ // 필터링 단어 추가 / 해제 시
			var id = $(this).attr("id");
			var form = document.getElementById(id);
			form.filtering.value = form.filtering.value.trim();
			if(!form.filtering.value || form.filtering.value == ""){
				alert("필터링 단어를 입력해주세요");
				return false;
			}
		});
	});
	$(function(){
		$("#setscanrateform").submit(function(){ // 삭제 주기 변경 시
			var form = document.getElementById("setscanrateform");
			if(!form.scanrate.value || form.scanrate.value == ""){
				alert("변경할 스캔 주기를 입력해주세요");
				return false;
			}
		});
	});
	$(function(){
		$("#checknamefilter").click(function(){
			var div = document.getElementById("namefilter");
			if(div.style.display == "none"){
				div.style.display = "block";
				$(this).val("파일명 필터 닫기");
			}else{
				div.style.display = "none";
				$(this).val("파일명 필터 확인하기");
			}
		});
	});
	$(function(){
		$("#checkimagefilter").click(function(){
			var div = document.getElementById("imagefilter");
			if(div.style.display == "none"){
				div.style.display = "block";
				$(this).val("그림파일 확장자 필터 닫기");
			}else{
				div.style.display = "none";
				$(this).val("그림파일 확장자 필터 확인하기");
			}
		});
	});
	$(function(){
		$("#checkvideofilter").click(function(){
			var div = document.getElementById("videofilter");
			if(div.style.display == "none"){
				div.style.display = "block";
				$(this).val("동영상 파일 확장자 필터 닫기");
			}else{
				div.style.display = "none";
				$(this).val("동영상 파일 확장자 필터 확인하기");
			}
		});
	});
	$(function(){
		$("#checkmusicfilter").click(function(){
			var div = document.getElementById("musicfilter");
			if(div.style.display == "none"){
				div.style.display = "block";
				$(this).val("음악파일 확장자 필터 닫기");
			}else{
				div.style.display = "none";
				$(this).val("음악파일 확장자 필터 확인하기");
			}
		});
	});
	$(function(){
		$("#checkdocumentfilter").click(function(){
			var div = document.getElementById("documentfilter");
			if(div.style.display == "none"){
				div.style.display = "block";
				$(this).val("문서파일 확장자 필터 닫기");
			}else{
				div.style.display = "none";
				$(this).val("문서파일 확장자 필터 확인하기");
			}
		});
	});
	$(function(){
		$("#checketcfilter").click(function(){
			var div = document.getElementById("etcfilter");
			if(div.style.display == "none"){
				div.style.display = "block";
				$(this).val("기타파일 확장자 필터 닫기");
			}else{
				div.style.display = "none";
				$(this).val("기타파일 확장자 필터 확인하기");
			}
		});
	});
</script>
		<table>
    	<input name="logout" type="button" value="로그아웃" onClick="location.href='logout.do'"/>	
        <a href="/BEngineer/inquiry/allInquiry.do">문의내역</a>
        <a href="/BEngineer/board/list.do?id=${sessionScope.Id }">공지사항</a>
        <a href="/BEngineer/manager/imposeForm.do">유저제재</a>
        <a href="/BEngineer/manager/charge.do">서버구동현황</a>
        <form id="addnamefilterform" class="filterform" action="/BEngineer/manager/addNameFilter.do">
        	파일명 필터 추가하기 : 
        	<input type="text" name="filtering" />
        	<input type="submit" value="필터 추가하기" />
        </form>
        <form id="removenamefilterform" class="filterform" action="/BEngineer/manager/removeNameFilter.do">
        	파일명 필터 해제하기 : 
        	<input type="text" name="filtering" />
        	<input type="submit" value="필터 해제하기" />
        </form>
		<input type="button" value="파일명 필터 확인하기" id="checknamefilter"/>
		<div id="namefilter" style="display:none;">
			<c:forEach var="a" items="${namefilterings }">
				${a }<br />
			</c:forEach>
		</div>
        <form id="addimagefilterform" class="filterform" action="/BEngineer/manager/addImageFilter.do">
        	그림파일 확장자 필터 추가하기 : 
        	<input type="text" name="filtering" />
        	<input type="submit" value="필터 추가하기" />
        </form>
        <form id="removeimagefilterform" class="filterform" action="/BEngineer/manager/removeImageFilter.do">
        	그림파일 확장자 필터 해제하기 : 
        	<input type="text" name="filtering" />
        	<input type="submit" value="필터 해제하기" />
        </form>
		<input type="button" value="그림파일 확장자 필터 확인하기" id="checkimagefilter"/>
		<div id="imagefilter" style="display:none;">
			<c:forEach var="a" items="${imagefilterings }">
				${a }<br />
			</c:forEach>
		</div>
        <form id="addvideofilterform" class="filterform" action="/BEngineer/manager/addVideoFilter.do">
        	동영상 파일 확장자 필터 추가하기 : 
        	<input type="text" name="filtering" />
        	<input type="submit" value="필터 추가하기" />
        </form>
        <form id="removevideofilterform" class="filterform" action="/BEngineer/manager/removeVideoFilter.do">
        	동영상 파일 확장자 필터 해제하기 : 
        	<input type="text" name="filtering" />
        	<input type="submit" value="필터 해제하기" />
        </form>
		<input type="button" value="동영상 파일 확장자 필터 확인하기" id="checkvideofilter"/>
		<div id="videofilter" style="display:none;">
			<c:forEach var="a" items="${videofilterings }">
				${a }<br />
			</c:forEach>
		</div>
        <form id="addmusicfilterform" class="filterform" action="/BEngineer/manager/addMusicFilter.do">
        	음악파일 확장자 필터 추가하기 : 
        	<input type="text" name="filtering" />
        	<input type="submit" value="필터 추가하기" />
        </form>
        <form id="removemusicfilterform" class="filterform" action="/BEngineer/manager/removeMusicFilter.do">
        	음악파일 확장자 필터 해제하기 : 
        	<input type="text" name="filtering" />
        	<input type="submit" value="필터 해제하기" />
        </form>
		<input type="button" value="음악파일 확장자 필터 확인하기" id="checkmusicfilter"/>
		<div id="musicfilter" style="display:none;">
			<c:forEach var="a" items="${musicfilterings }">
				${a }<br />
			</c:forEach>
		</div>
        <form id="adddocumentfilterform" class="filterform" action="/BEngineer/manager/addDocumentFilter.do">
        	문서파일 확장자 필터 추가하기 : 
        	<input type="text" name="filtering" />
        	<input type="submit" value="필터 추가하기" />
        </form>
        <form id="removedocumentfilterform" class="filterform" action="/BEngineer/manager/removeDocumentFilter.do">
        	문서파일 확장자 필터 해제하기 : 
        	<input type="text" name="filtering" />
        	<input type="submit" value="필터 해제하기" />
        </form>
		<input type="button" value="문서파일 확장자 필터 확인하기" id="checkdocumentfilter"/>
		<div id="documentfilter" style="display:none;">
			<c:forEach var="a" items="${documentfilterings }">
				${a }<br />
			</c:forEach>
		</div>
        <form id="addetcfilterform" class="filterform" action="/BEngineer/manager/addEtcFilter.do">
        	기타파일 확장자 필터 추가하기 : 
        	<input type="text" name="filtering" />
        	<input type="submit" value="필터 추가하기" />
        </form>
        <form id="removeetcfilterform" class="filterform" action="/BEngineer/manager/removeEtcFilter.do">
        	기타파일 확장자 필터 해제하기 : 
        	<input type="text" name="filtering" />
        	<input type="submit" value="필터 해제하기" />
        </form>
		<input type="button" value="기타파일 확장자 필터 확인하기" id="checketcfilter"/>
		<div id="etcfilter" style="display:none;">
			<c:forEach var="a" items="${etcfilterings }">
				${a }<br />
			</c:forEach>
		</div><br />
		현재 더미 / 임시파일 삭제 주기 : ${scanrate }ms
        <form id="setscanrateform" action="/BEngineer/manager/setScanrate.do">
        	더미 / 임시파일 삭제 주기 변경하기(최소 5000ms) : 
        	<input type="number" name="scanrate" min="5000" />
        	<input type="submit" value="변경하기" />
        </form>
</table>