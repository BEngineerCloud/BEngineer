$(function(){
	$("#beLogo").click(function(){ // 로고 클릭시 메인으로 이동
		window.location = "/BEngineer/beMain.do";
	});
});
$(function(){
	$("#myfile").click(function(){ // 내 파일보기 버튼 클릭 시 페이지 이동
		window.location = "/BEngineer/beFiles/beMyList.do?folder=0";
	});
});
$(function(){
	$("#mysharedfile").click(function(){
		window.location = "/BEngineer/beFiles/beSharedList.do?folder=0";
	});
});
$(function(){
	$("#mytrashcan").click(function(){
		window.location = "/BEngineer/beFiles/beTrashcan.do?folder=0";
	});
});
$(function(){
	$("#addinfodiv > #addinfo").click(function(){
		window.location = "/BEngineer/beMember/beAddinfo.do";
	});
});

$(function(){
	$("#logoutdiv > #logout").click(function(){
		window.location = "/BEngineer/beLogout.do";
	});
});
$(function(){
	$("#hotlist").click(function(){
		window.location = "/BEngineer/beFiles/hotlist.do";
	});
});