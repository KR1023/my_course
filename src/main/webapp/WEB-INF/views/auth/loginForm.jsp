<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.io.InputStream, java.util.Properties" %>
    
<%
	InputStream input = application.getClassLoader().getResourceAsStream("config.properties");

	Properties properties = new Properties();
	
	if(input != null){
		properties.load(input);		
	}else{
		throw new RuntimeException("config.properties is not found");
	}
	
	String aesKey = properties.getProperty("AES.KEY");
	String iv = properties.getProperty("AES.IV");
	
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>login</title>
<script type="text/javascript" src="/javascript/jquery/jquery-3.7.1.min.js"></script>
<script type="text/javascript" src="/javascript/crypto/crypto-js.min.js"></script>

</head>
<body>
<h1>login</h1>
<form action="/login" method="POST" id="login_form">
	<input type="email" name="email" placeholder="email" onkeydown="keyDown(this)"/><br />
	<input type="password" name="password" placeholder="password" onkeydown="keyDown(this)"/><br />
	<button type="button" onClick="login()" >로그인</button>
</form>
<a href="/register">signup</a>
</body>
<script type="text/javascript">
	let form = $("#login_form")[0];
	
	let login = () => {
		const key = "<%=aesKey%>";
		const iv =  "<%=iv%>";
		
		let email = form.email.value;
		let password = form.password.value;
		let encryptedPwd = CryptoJS.AES.encrypt(form.password.value, CryptoJS.enc.Utf8.parse(key), {
			iv: CryptoJS.enc.Utf8.parse(iv),
			padding: CryptoJS.pad.Pkcs7,
			mode: CryptoJS.mode.CBC
		}).toString();
		
		let reqJson = JSON.stringify({"email": email, "password" : encryptedPwd});
		
		if(email === '' || email.length === 0){
			alert("이메일을 입력해 주세요.");
			return;
		}
		
		if(password === '' || password.length === 0){
			alert("비밀번호를 입력해 주세요.");
			return;
		}
		
		$.ajax({
			type: "POST",
			url: "/login",
			async: false,
			contentType: "application/json",
			data: reqJson,
			success: (data, textStatus, jqXHR) => {
				let referrer = document.referrer;
				if(jqXHR.status === 200){
					if(data === "loginSuccess")
						// location.replace("/test");
						location.replace(referrer);
					else if(data === "failed")
						alert("이메일 혹은 비밀번호를 확인해 주세요.");
				}
			},
			error: (data, error) => {
				console.error(data);
				console.error(error);
			}
		});
	}
	
	let keyDown = () => {
		let keyCode = event.keyCode;
		if(keyCode === 13)
			login();
	}
</script>
</html>