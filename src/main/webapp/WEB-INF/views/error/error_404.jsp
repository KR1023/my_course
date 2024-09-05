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
</head>
<body>
<div class="container">
	<div class="message">
		<span>요청하신 페이지를 찾을 수 없습니다.</span><br />
		<a href="/test">돌아 가기</a>
	</div>
	
	
</div>
</body>
</html>