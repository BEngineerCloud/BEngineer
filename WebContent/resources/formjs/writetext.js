$(function(){
	$("#writetextbutton").click(function(){ // 텍스트 파일 작성 클릭 시
		hinder();
		initWriteText()
		setWriteText();
	});
});
$(function(){
	$("#canclewritetext").click(function(){ // 텍스트 파일 작성 클릭 시
		hinder();
	});
});
$(function(){
	$("#writetextform").submit(function(){ // 텍스트 쓰기 버튼 클릭시
		var form = document.getElementById("writetextform"); // 폼 받아오기
		if(!form.filename.value.trim() && !form.filename.value.trim() == ""){ // 업로드할 파일 미선택시
			alert('파일 별명을 입력해주세요');
			form.filename.focus();
			return false;
		}
		if(!form.orgname.value.trim() && !form.orgname.value.trim() == ""){ // 업로드할 파일 미선택시
			alert('파일명을 입력해주세요');
			form.orgname.focus();
			return false;
		}
		var orgname = form.orgname.value.trim() + ".txt";
		var check = $("input[value='" + orgname + "']").attr("type");
		if(check == "text"){
			if(!confirm('파일을 덮어 쓰시겠습니까?')){
				return false;
			}
		}
		if(!form.content.value.trim() && !form.content.value.trim() == ""){ // 업로드할 파일 미선택시
			alert('내용을 입력해주세요');
			form.content.focus();
			return false;
		}
		form.filename.value = form.filename.value.trim();
		form.orgname.value = form.orgname.value.trim();
	});
});
function initWriteText(){
	var form = document.getElementById("writetextform");
	if(form != null){
		form.filename.value = "";
		form.orgname.value = "";
		form.content.value = "";
		form.orgname.readOnly = false;
	}
}
function initRewriteText(){
	var form = document.getElementById("rewritetextform");
	if(form != null){
		form.submitrewritetext.type = "hidden";
	}
}
function setWriteText(){
	document.getElementById("writetextdiv").style.display = "block";
	document.getElementById("files").style.height = "40%";
}
function setRewriteText(num){
	var form = document.getElementById("rewritetextform");
	var index = 0;
	if(writeList != null){
		index = writeList.indexOf(num);
	}
	var filetype = document.getElementById(num + "type");
	if(form != null && index != -1 && filetype.value == ".txt"){
		form.filenum.value = num;
		form.submitrewritetext.type = "submit";
	}
}
function hideWriteText(){
	document.getElementById("writetextdiv").style.display = "none";
	document.getElementById("files").style.height = "80%";
}
function reWriteText(name, orgname, content){
	var form = document.getElementById("writetextform");
	form.filename.value = name;
	form.orgname.value = orgname;
	form.content.value = content;
	form.orgname.readOnly = true;
	setWriteText();
}