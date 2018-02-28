$(function(){
	$("#shareform").submit(function(){ // 공유 버튼 클릭시
		var form = document.getElementById("shareform"); // 폼 받아오기
		if(form.enddate.type == "hidden"){ // 폼이 숨겨진 상태일 때 폼 보이고 이동 취소
			hinder(); // 다른 폼 닫기
			showShare();
			reopen();
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
		unshareSelect();
	});
});
$(function(){
	$("#unshareform").submit(function(){ // 공유 해제 버튼 클릭시
		var form = document.getElementById("unshareform"); // 폼 받아오기
		var select = document.getElementById("unshareselect");
		if(select != null){
			if(select.hidden == true){ // 폼이 숨겨진 상태일 때 폼 보이고 이동 취소
				hinder(); // 다른 폼 닫기
				initFiles();
				select.hidden = false;
				showUnshare();
				reopen();
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
function initShare(){
	var form = document.getElementById("shareform"); // 폼 받아오기
	if(form != null){
		form.enddate.type = "hidden";
		form.rw.hidden = true;
		form.submitshare.type = "hidden";
		var text = document.getElementById("text");
		if(text != null){
			text.type = "hidden";
		}
	}
}
function initShareCheck(){
	var form = document.getElementById("sharecheckform");
	if(form != null){
		form.submitsharecheck.type = "hidden";
	}
}
function initUnshare(){
	var form = document.getElementById("unshareform");
	var select = document.getElementById("unshareselect");
	if(form != null){
		form.submitunshare.type = "hidden";
		if(select != null){
			select.hidden = true;
			form.nickname.type = "hidden";
		}
	}
}
function setShare(num){
	var form = document.getElementById("shareform"); // 폼 받아오기
	if(form != null){
		form.ref.value = num;
		form.submitshare.type = "submit";
	}
}
function setShareCheck(num){
	var form = document.getElementById("sharecheckform");
	if(form != null){
		form.file.value = num;
		form.submitsharecheck.type = "submit";
	}
}
function setUnshare(num){
	var form = document.getElementById("unshareform");
	if(form != null){
		form.file_ref.value = num;
		form.submitunshare.type = "submit";
	}
}
function showUnshare(){
	var form = document.getElementById("unshareform");
	var select = document.getElementById("unshareselect");
	if(form != null){
		form.submitunshare.type = "submit";
		if(select != null){
			if(select.value == 1){
				document.getElementById("unsharetext").type = "hidden";
				form.nickname.type = "hidden";
			}else{
				document.getElementById("unsharetext").type = "text";
				form.nickname.type = "text";
			}
		}
	}
}
function showShare(num){
	var form = document.getElementById("shareform"); // 폼 받아오기
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
	if(form != null){
		form.enddate.min = today;
		form.enddate.type = "date";
		form.rw.hidden = false;
		form.submitshare.type = "submit";
		var text = document.getElementById("text");
		if(text != null){
			text.type = "text";
		}
	}
}
function unshareSelect(){
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
}