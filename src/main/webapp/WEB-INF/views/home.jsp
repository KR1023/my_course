<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Home</title>
<script type="text/javascript" src="/javascript/jquery/jquery-3.7.1.min.js"></script>
</head>
<body>
<h1>Home</h1>
<button type="button" id="logout_btn">로그아웃</button>
</body>
<script type="text/javascript">
	$("#logout_btn").click(() => {
		$.ajax({
			url: '/logout',
			async: true,
			type: 'GET',
			success: (data, textStatus, jqXHR) => {
				console.log(data);
				console.log(textStatus);
				console.log(jqXHR);
				if(jqXHR.status === 200){
					alert("로그아웃 되었습니다.");
					location.replace("/login");
				}
			},
			error: (data, error) => {
				console.error(data);	
			}
		});
	});
	
	
</script>
</html>