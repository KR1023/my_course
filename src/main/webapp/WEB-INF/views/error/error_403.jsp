<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Error</title>
<style>
body{
	padding: 0;
	margin: 0;
}
	.container{
		position: fixed;
		left: 0;
		top: 0;
		width: 100%;
		height: 100%;
		display: flex;
		justify-content: center;
		align-items: center;
	}
	
	.container .message{
		font-size: 24px;
	}
	
</style>
<script type="text/javascript" src="/javascript/jquery/jquery-3.7.1.min.js"></script>
</head>
<body>
<div class="container">
	<div class="message">
		<span>접근 권한이 없습니다.</span><br />
		<a href="/test" id="history_back">메인 페이지</a>
	</div>
</div>
</body>
<script type="text/javascript">
	$("#history_back").click((e) => {
		e.preventDefault();
		location.href = '/course';	
	});
</script>
</html>