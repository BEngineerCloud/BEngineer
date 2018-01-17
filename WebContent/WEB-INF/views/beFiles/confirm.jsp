<%@ page contentType="text/html; charset=UTF-8"%>
<script>
	if(confirm('${confirm}')){
		window.location = ${location};
	}else{
		history.go(-1);
	}
</script>