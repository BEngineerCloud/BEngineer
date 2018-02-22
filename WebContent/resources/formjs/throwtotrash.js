$(function(){
	$("#throwtotrashcan").click(function(){ // 지우기 클릭 시
		var form = document.getElementById("multidownform");
		var text = document.getElementById("multidowntext");
		if(form != null && text != null && text.type != "hidden"){
			form.file_ref.value = clickedFile.join();
		}
		window.location = "/BEngineer/beFiles/throwToTrashcan.do?file_ref=" + form.file_ref.value + "&folder=" + folder_ref;
	});
});
function initThrowToTrashcan(){
	var tttc = document.getElementById("throwtotrashcan");
	if(tttc != null){
		tttc.type = "hidden";
	}
}
function setThrowToTrashcan(){
	var tttc = document.getElementById("throwtotrashcan");
	if(tttc != null){
		tttc.type = "button";
	}
}
function initThrowATrash(){
	var form = document.getElementById("throwatrashform");
	if(form != null){
		form.submitthrow.type = "hidden";
	}
}
function setThrowATrash(num){
	var form = document.getElementById("throwatrashform");
	var important = document.getElementById(num + "important");
	if(form != null && important.value > -1){
		form.file_ref.value = num;
		form.submitthrow.type = "submit";
	}
}