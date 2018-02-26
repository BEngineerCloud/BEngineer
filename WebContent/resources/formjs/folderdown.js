function initFolderDown(){
	var form = document.getElementById("folderdownform");
	if(form != null){
		form.submitfolderdown.type = "hidden";
	}
}
function setFolderDown(num){
	var form = document.getElementById("folderdownform");
	if(form != null){
		form.file_ref.value = num;
		form.submitfolderdown.type = "submit";
	}
}