<%@ page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<body bgcolor="#00D8F0">
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
<style>	 
	h1{color:blue;}
</style>
<div>
	<div><h1>BEngineerCloud</h1></div>
	<div style="float:right; height:10%; width:20%; bgcolor:#000000;"><input name="logout" type="button" value="로그아웃" onClick="location.href='logout.do'" style="width:100px; height:25px; color:#000000; background-color:#B7F0B1;"/>
	</br><a href="/BEngineer/inquiry/allInquiry.do">문의내역</a>
    </br><a href="/BEngineer/board/list.do?id=${sessionScope.Id }">공지사항</a>
    </br><a href="/BEngineer/manager/imposeForm.do">유저제재</a>
    </br><a href="/BEngineer/manager/charge.do">서버구동현황</a>
	</div>
</div>

       <form id="addnamefilterform" class="filterform" action="/BEngineer/manager/addNameFilter.do">
       	<tr><td>파일명 필터 추가하기 :</td> 
       	<td><input type="text" name="filtering" /></td>
       	<td><input type="submit" value="필터 추가하기" style="width:100px; background-color:#1DDB16;"/></td></tr>
       </form>
       <form id="removenamefilterform" class="filterform" action="/BEngineer/manager/removeNameFilter.do">
       	<tr><td>파일명 필터 해제하기 :</td> 
       	<td><input type="text" name="filtering" /></td>
       	<td><input type="submit" value="필터 해제하기" style="width:100px; background-color:#1DDB16;"/></td></tr>
       </form>
	<input type="button" value="파일명 필터 확인하기" id="checknamefilter" style="width:210px; height:30px; color:#000000; background-color:#FFE400;"/>
	<div id="namefilter" style="display:none;">
		<c:forEach var="a" items="${namefilterings }">
			${a }<br />
		</c:forEach>
	</div>
       <form id="addimagefilterform" class="filterform" action="/BEngineer/manager/addImageFilter.do">
       	<tr><td>그림파일 확장자 필터 추가하기 : </td>
       	<td><input type="text" name="filtering" /></td>
       	<td><input type="submit" value="필터 추가하기" style="width:100px; background-color:#1DDB16;"/></td></tr>
       </form>
       <form id="removeimagefilterform" class="filterform" action="/BEngineer/manager/removeImageFilter.do">
       	<tr><td>그림파일 확장자 필터 해제하기 :</td> 
       	<td><input type="text" name="filtering" /></td>
       	<td><input type="submit" value="필터 해제하기" style="width:100px; background-color:#1DDB16;"/></td></tr>
       </form>
	<input type="button" value="그림파일 확장자 필터 확인하기" id="checkimagefilter" style="width:210px; height:30px; color:#000000; background-color:#FFE400;"/>
	<div id="imagefilter" style="display:none;">
		<c:forEach var="a" items="${imagefilterings }">
			${a }<br />
		</c:forEach>
	</div>
       <form id="addvideofilterform" class="filterform" action="/BEngineer/manager/addVideoFilter.do">
       	<tr><td>동영상 파일 확장자 필터 추가하기 : </td>
       	<td><input type="text" name="filtering" /></td>
       	<td><input type="submit" value="필터 추가하기" style="width:100px; background-color:#1DDB16;"/></td></tr>
       </form>
       <form id="removevideofilterform" class="filterform" action="/BEngineer/manager/removeVideoFilter.do">
       	<tr><td>동영상 파일 확장자 필터 해제하기 : </td>
       	<td><input type="text" name="filtering" /></td>
       	<td><input type="submit" value="필터 해제하기" style="width:100px; background-color:#1DDB16;"/></td></tr>
       </form>
	<input type="button" value="동영상 파일 확장자 필터 확인하기" id="checkvideofilter" style="width:210px; height:30px; color:#000000; background-color:#FFE400;"/>
	<div id="videofilter" style="display:none;">
		<c:forEach var="a" items="${videofilterings }">
			${a }<br />
		</c:forEach>
	</div>
       <form id="addmusicfilterform" class="filterform" action="/BEngineer/manager/addMusicFilter.do">
       	<tr><td>음악파일 확장자 필터 추가하기 : </td>
       	<td><input type="text" name="filtering" /></td>
       	<td><input type="submit" value="필터 추가하기" style="width:100px; background-color:#1DDB16;"/></td></tr>
       </form>
       <form id="removemusicfilterform" class="filterform" action="/BEngineer/manager/removeMusicFilter.do">
       	<tr><td>음악파일 확장자 필터 해제하기 :</td> 
       	<td><input type="text" name="filtering" /></td>
       	<td><input type="submit" value="필터 해제하기" style="width:100px; background-color:#1DDB16;"/></td></tr>
       </form>
	<input type="button" value="음악파일 확장자 필터 확인하기" id="checkmusicfilter" style="width:210px; height:30px; color:#000000; background-color:#FFE400;"/>
	<div id="musicfilter" style="display:none;">
		<c:forEach var="a" items="${musicfilterings }">
			${a }<br />
		</c:forEach>
	</div>
       <form id="adddocumentfilterform" class="filterform" action="/BEngineer/manager/addDocumentFilter.do">
       	<tr><td>문서파일 확장자 필터 추가하기 : </td>
       	<td><input type="text" name="filtering" /></td>
       	<td><input type="submit" value="필터 추가하기" style="width:100px; background-color:#1DDB16;"/></td></tr>
       </form>
       <form id="removedocumentfilterform" class="filterform" action="/BEngineer/manager/removeDocumentFilter.do">
       	<tr><td>문서파일 확장자 필터 해제하기 : </td>
       	<td><input type="text" name="filtering" /></td>
       	<td><input type="submit" value="필터 해제하기" style="width:100px; background-color:#1DDB16;"/></td></tr>
       </form>
	<input type="button" value="문서파일 확장자 필터 확인하기" id="checkdocumentfilter" style="width:210px; height:30px; color:#000000; background-color:#FFE400;"/>
	<div id="documentfilter" style="display:none;">
		<c:forEach var="a" items="${documentfilterings }">
			${a }<br />
		</c:forEach>
	</div>
       <form id="addetcfilterform" class="filterform" action="/BEngineer/manager/addEtcFilter.do">
       	<tr><td>기타파일 확장자 필터 추가하기 : </td>
        <td><input type="text" name="filtering" /></td>
       	<td><input type="submit" value="필터 추가하기" style="width:100px; background-color:#1DDB16;"/></td></tr>
       </form>
       <form id="removeetcfilterform" class="filterform" action="/BEngineer/manager/removeEtcFilter.do">
       	<tr><td>기타파일 확장자 필터 해제하기 :</td> 
       	<td><input type="text" name="filtering" /></td>
       	<td><input type="submit" value="필터 해제하기" style="width:100px; background-color:#1DDB16;"/></td></tr>
       </form>
	<input type="button" value="기타파일 확장자 필터 확인하기" id="checketcfilter" style="width:210px; height:30px; color:#000000; background-color:#FFE400;"/>
	<div id="etcfilter" style="display:none;">
		<c:forEach var="a" items="${etcfilterings }">
			${a }<br />
		</c:forEach>
	</div><br />
	현재 더미 / 임시파일 삭제 주기 : ${scanrate }ms
       <form id="setscanrateform" action="/BEngineer/manager/setScanrate.do">
       	<tr><td>더미 / 임시파일 삭제 주기 변경하기(최소 5000ms) : </td>
       	<td><input type="number" name="scanrate" min="5000" /></td>
       	<td><input type="submit" value="변경하기" style="width:100px; background-color:#1DDB16;"/></td></tr>
       </form>

</body>