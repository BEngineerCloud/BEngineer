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
	var tttc = document.getElementById("throwtotrashcan");
	if(tttc != null){
		tttc.type = "hidden";
	}
}
function setThrowToTrashcan(num){
	var tttc = document.getElementById("throwtotrashcan");
	var important = document.getElementById(num + "important");
	if(tttc != null && important.value != -1){
		tttc.type = "button";
		var form = document.getElementById("multidownform");
		if(form != null){
			form.file_ref.value = num;
		}
	}
}