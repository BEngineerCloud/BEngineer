$(function(){
	$("#files").click(function(e){
		var target = $(e.target).parent().attr("class");
		var parent = $(e.target).parent().parent().parent().attr("class");
		var check = false;
		var filename = null;
		var ref = null;
		if(target == 'file'){
			filename = $(e.target).parent().text();
			ref = $(e.target).parent().attr("name");
			check = true;
		}else if(parent == 'file'){
			filename = $(e.target).parent().parent().parent().text();
			ref = $(e.target).parent().parent().parent().attr("name");
			check = true;
		}
		if(check){
			var important =document.getElementById(ref + "important");
			var orgname = document.getElementById(ref + "orgname"); // 원 파일명 저장되어있는 인풋의 값 가져오기
			var type = document.getElementById(ref + "type"); // 파일타입 저장되어있는 인풋의 값 가져오기
			setAddress(ref, orgname.value);
			var multiCheck = document.getElementById("multidowntext");
			if(multiCheck != null && multiCheck.type == "text"){
				var index = clickedFile.indexOf(ref);
				if(index == -1){
					clickedFile.push(ref);
					if(important.value <= -1){
						clickedImportant.push(1)
					}
					selectFile(ref);
				}else{
					clickedFile.splice(index, 1);
					if(important.value <= -1){
						clickedImportant.splice(0, 1);
					}
					disselectFile(ref);
				}
				if(clickedImportant.length > 0){
					initMultiMove();
				}else{
					setMultiMove();
					setMultiCopy();
				}
				return;
			}
			hinder();
			initFiles();
			selectFile(ref);
			setChangeName(ref, type);
			setShareCheck(ref);
			setShare(ref);
			setUnshare(ref);
			setCopy(ref);
			setMove(ref);
			setThrowATrash(ref);
			setChangeOwner(ref);
			setImage(ref);
			open(e.clientX, e.clientY);
		}else{
			hinder();
		}
	});
});
$(function(){
	$("#files > div").dblclick(function(){ // 파일 더블클릭시
		var ref = $(this).attr("name");
		window.location = "/BEngineer/beFiles/beDownload.do?file_ref=" + ref;
	});
});
$(function(){
	$("#backupbutton").click(function(){ // 파일 백업클릭시
		if(confirm("백업 하시겠습니까? 기존의 백업 정보가 덮어씌워집니다. \n\r (파일의 양에 따라 시간이 다소 소요될 수 있습니다.)")){
			window.location = "/BEngineer/beFiles/beBackUp.do";
		}
	});
});
$(function(){
	$("#rollbackbutton").click(function(){ // 파일 복원 클릭시
		if(confirm("백업 시점으로 복원 하시겠습니까? 백업 이후 저장/수정된 모든 파일정보가 사라집니다. \n\r (파일의 양에 따라 시간이 다소 소요될 수 있습니다.)")){
			window.location = "/BEngineer/beFiles/beRollBack.do";
		}
	});
});
function hinder(){ // 모든 폼 초기화 함수
	initUpload();
	initMultiSel();
	initCopy();
	initMove();
	initChangeName();
	initShare();
	initShareCheck();
	initUnshare();
	initChangeOwner();
	initThrowToTrashcan();
	initImage();
	initThrowATrash();
	close();
}
function initFiles(){
	$("#files > div").css("background-color","#ffffff"); // 모든 파일 선택 취소
}
function initFileSel(){
	$("#files > div").css("border","none"); // 모든 파일 선택 취소
}
function selectFile(num){
	$("div[name='" + num + "']").css("background-color","aqua"); // 클릭파일 색 바꾸기
}
function disselectFile(num){
	$("div[name='" + num + "']").css("background-color","#ffffff"); // 클릭파일 색 바꾸기
}
function setAddress(num, orgname){
	var text = orgname;
	$("font#filename").text(text); // 주소부분에 표시
}
function star(num){
	var boo = $('input:checkbox[id=\'check' + num + '\']').is(":checked");	// 체크여부 체크o true , x false
	if(boo==true){
		alert("즐겨찾기 추가");
		window.location = "/BEngineer/beFiles/hot.do?num=" + num;			
	}else{
		alert("즐겨찾기 헤제");
		window.location = "/BEngineer/beFiles/exhot.do?num=" + num;			
	}
}
function open(x, y){
	$("#button1_1").css("width", "15%");
	var toplimit = $("body").innerHeight();
	var leftlimit = $("body").innerWidth();
	var height = $("#button1_1").height();
	var width = $("#button1_1").width();
	if(x + width + 5 > leftlimit){
		x = x - width - 10;
	}
	if(y + height + 5 > toplimit){
		y = y - height - 10;
	}
	$("#button1_1").css({
		"top": (y + 5),
		"left": (x + 5),
		"position": "absolute"
	});
	$("#button1_1").show("slow");
}
function reopen(){
	$("#button1_1").css("width", "20%").show("fast");
}
function close(){
	$("#button1_1").css("display", "none");
}
$(function(){
	$("body").append("<div id='glayLayer' ></div><div id='overLayer' style='overflow-y:auto;'></div>");
	
	$("#glayLayer").click(function(){
		$(this).hide()
		$("#overLayer").hide();
	});
	
	$("#imageview").click(function(){
		hinder();
		var imageform = document.getElementById("imageform");
		$("#glayLayer").show();
		$("#overLayer").show().html("<img src='"+$("#" + imageform.selImageview.value + " > div > a.modal").attr("href")+"'style=\"width:100%; \" />");
		return false;
	});
});
function setImage(num){
	var form = document.getElementById("imageform"); //imageform 가져오기
	if(form != null){
		form.selImageview.value = num;
		form.imageview.type = "button";
	}
}
function initImage(){
	var form = document.getElementById("imageform"); //imageform 가져오기
	if(form != null){
		form.imageview.type = "hidden";
	}
}