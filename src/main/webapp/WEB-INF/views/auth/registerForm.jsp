<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>register</title>
<script type="text/javascript" src="/jquery/jquery-3.7.1.min.js"></script>
</head>
<body>
<h1>register</h1>
<form id="reg_form" action="/user" method="POST">
	<input type="email" name=email placeholder="email"/><br />
	<input type="text" name="name" placeholder="name" maxLength="10" /><br />
	<input type="password" name="password" placeholder="password"/><br />
	<input type="text" name="phone" placeholder="phone"/><br />
	<button type="button" onclick="register()">signup</button>
	<a href="/login">login</a>
</form>
<script type="text/javascript">
	let form = document.getElementById("reg_form");
	
	let register = () => {
		let email = form.email.value;
		let name = form.name.value;
		let password = form.password.value;
		let phone = form.phone.value;
		
		let reqJson = JSON.stringify({"email":email, "name":name, "password":password, "phone":phone});
		
		if(email === '' || email.length === 0){
			alert("이메일을 입력해 주세요.");
			return;
		}
		
		if(name === '' || name.length === 0){
			alert("이름을 입력해 주세요.");
			return;
		}
		
		if(password === '' || password.length === 0){
			alert("비밀번호를 입력해 주세요.");
			return;
		}
		
		
		$.ajax({
			url: "/user",
			data: reqJson,
			type: "post",
			async: false,
			contentType: "application/json",
			success: (data,textStatus,jqXHR) => {
				console.log(data);
				console.log(textStatus);
				console.log(jqXHR);
				if(jqXHR.status === 201){
					alert("회원가입이 완료되었습니다.");
					location.replace("/login");
				}
			},
			error: (data, error) => {
				if(data.status === 409)
					alert("이미 존재하는 이메일입니다.");
			}
		});
	}
	
</script>
</body>
</html>