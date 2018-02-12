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