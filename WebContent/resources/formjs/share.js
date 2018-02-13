$(function(){
	$("#shareform").submit(function(){ // 공유 버튼 클릭시
		var moveform = document.getElementById("moveform"); // moveform 가져오기
		var copyform = document.getElementById("copyform"); // copyform 가져오기
		moveform.submitmove.type="hidden";
		copyform.submitcopy.type="hidden";
		var date = new Date();
		var today = date.getFullYear() + "-";
		var month = (date.getMonth() + 1);
		if(month < 10){
			today = today + "0" + month + "-";
		}else{
			today = today + month + "-";
		}
		var day = date.getDate();
		if(day < 10){
			today = today + "0" + day;
		}else{
			today = today + day;
		}
		var form = document.getElementById("shareform"); // 폼 받아오기
		form.enddate.min = today;
		if(form.enddate.type == "hidden"){ // 폼이 숨겨진 상태일 때 폼 보이고 이동 취소
			hinder(); // 다른 폼 닫기
			document.getElementById("text").type = "text";
			form.enddate.type = "date";
			form.rw.hidden = false;
			form.submitshare.type = "submit";
			return false;
		}
		if(!form.enddate.value){ // 공유기한 미입력시
			alert('공유기한을 입력해주세요');
			return false;
		}
	});
});
$(function(){
	$("#unshareselect").change(function(){
		var form = document.getElementById("unshareform"); // 폼 받아오기
		var select = document.getElementById("unshareselect");
		if(form != null && select != null){
			if(select.value == 1){
				document.getElementById("unsharetext").type = "hidden";
				form.nickname.type = "hidden";
			}else{
				document.getElementById("unsharetext").type = "text";
				form.nickname.type = "text";
			}
		}
	});
});
$(function(){
	$("#unshareform").submit(function(){ // 공유 해제 버튼 클릭시
		var moveform = document.getElementById("moveform"); // moveform 가져오기
		var copyform = document.getElementById("copyform"); // copyform 가져오기
		moveform.submitmove.type="hidden";
		copyform.submitcopy.type="hidden";
		var form = document.getElementById("unshareform"); // 폼 받아오기
		var select = document.getElementById("unshareselect");
		if(select != null){
			if(select.hidden == true){ // 폼이 숨겨진 상태일 때 폼 보이고 이동 취소
				hinder(); // 다른 폼 닫기
				$("#files > div").css("background-color","#ff6666"); // 모든 파일 선택 취소
				document.getElementById("unsharediv").style.display = "block";
				select.hidden = false;
				return false;
			}
			if(select.value == 2 && !form.nickname.value.trim()){ // 업로드할 파일 미선택시
				alert('공유 해제할 사람의 닉네임을 입력해주세요');
				return false;
			}
			form.nickname.value = form.nickname.value.trim();
		}
	});
});
function initShareCheck(){
	var form = document.getElementById("sharecheckform");
	if(form != null){
		form.submitsharecheck.type = "hidden";
	}
}
function setShareCheck(num){
	var form = document.getElementById("sharecheckform");
	if(form != null){
		form.file.value = num;
		form.submitsharecheck.type = "submit";
	}
}
function initUnshare(){
	var form = document.getElementById("unshareform");
	if(form != null){
		form.submitunshare.type = "hidden";
	}
}
function setUnshare(num){
	var form = document.getElementById("unshareform");
	if(form != null){
		form.file_ref.value = num;
		form.submitunshare.type = "submit";
	}
}