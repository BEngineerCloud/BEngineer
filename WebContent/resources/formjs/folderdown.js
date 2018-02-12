function initFolderDown(){
	var form = document.getElementById("folderdownform");
	if(form != null){
		form.submitfolderdown.type = "hidden";
	}
}
function setFolderDown(num, type){
	var form = document.getElementById("folderdownform");
	if(form != null){
		if(type.value == "dir"){
			form.file_ref.value = num;
			form.submitfolderdown.type = "submit";
		}else{
			initFolderDown();
		}
	}
}