$(function(){
	$("#throwtotrashcan").click(function(){ // 지우기 클릭 시
		var form = document.getElementById("multidownform");
		if(document.getElementById("multidowntext").type != "hidden"){
			form.file_ref.value = clickedfile.join();
		}
		window.location = "/BEngineer/beFiles/throwToTrashcan.do?file_ref=" + form.file_ref.value + "&folder=" + folder_ref;
	});
});
function initThrowToTrashcan(){
	var form = document.getElementById("writetextform");
	if(form != null){
		form.filename.value = "";
		form.orgname.value = "";
		form.content.value = "";
		form.orgname.readOnly = false;
	}
}
function setThrowToTrashcan(num){
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