<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.io.InputStream, java.util.Properties" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
<title>register</title>
<link rel="stylesheet" href="/css/register.css" />
<script type="text/javascript" src="/javascript/jquery/jquery-3.7.1.min.js"></script>
<script type="text/javascript" src="/javascript/crypto/crypto-js.min.js"></script>

</head>
<body>
<div class="register">
    <div class="container">
        <div class="nav">
            <img src="images/arrow/arrow_back.svg" alt="arrow_back" onClick="backToLogin()"/>
        </div>
        <div class="header">
            <img src="images/event_note.svg" alt="logo" />
            <span>MyCourse 사용자 등록</span>
        </div>
        <form id="reg_form" action="/user" method="POST">
            <input type="email" name="email" placeholder="email" />
            <p id="err_email" class="err_msg" ></p>
            <input type="text" name="name" placeholder="name" maxLength="10" />
            <p id="err_name" class="err_msg"></p>
            <input type="password" name="password" placeholder="password"/>
            <p id="err_pwd" class="err_msg"></p>
            <input type="text" name="phone" placeholder="phone(01012341234)"/>
            <p id="err_phone" class="err_msg"></p>
            <button type="button" onclick="register()">signup</button>
        </form>
    </div>
</div>
<script type="text/javascript">
	let form = document.getElementById("reg_form");
	
	let backToLogin = () => {
		// console.log(window.location);
		location.href = "/login";
	};
	
	let checkErrors = {
		"email" : true,
		"name" : true,
		"password" : true,
		"phone" : true
	};
	
	let checkEmail = () => {
		const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/i;
		let test = emailRegex.test(form.email.value);
		
		if(!test){
			checkErrors = {...checkErrors, "email" : true};
			$("#err_email").text("올바른 이메일 형식을 입력해 주세요.");
		}else if(test){
			checkErrors = {...checkErrors, "email" : false};
			$("#err_email").text('');
			
			$.ajax({
				url: "/check",
				type: "POST",
				aysnc: true,
				contentType: "application/json",
				data: form.email.value,
				success: (data, textStatus, jqXHR) => {
					if(jqXHR.status === 200){
						if(data === "empty"){
							checkErrors = {...checkErrors, "email" : false};
							$("#err_email").text("사용 가능한 이메일입니다.");
						}else{
							checkErrors = {...checkErrors, "email" : true};
							$("#err_email").text("이미 존재하는 이메일입니다.");
						}
					}
				},
				error: (data, error) => {
					console.error(data);	
					console.error(error);
					if(error){
						alert("서버에 에러가 발생했습니다. 잠시 후에 다시 시도해 주세요.");
					}
				}
			});
		}
	}
	
	form.email.addEventListener("blur", () => {
		checkEmail();
	});
	
	let checkName = () => {
		const nameRegex = /^[가-힣a-zA-Z]{2,20}$/;
		let test = nameRegex.test(form.name.value);
		
		if(!test){
			checkErrors = {...checkErrors, "name" : true};
			$("#err_name").text("2~20자의 한글, 영문을 입력해 주세요.");
		}else if(test){
			checkErrors = {...checkErrors, "name" : false};
			$("#err_name").text("");
		}
	}
	
	form.name.addEventListener("blur", () => {
		checkName();
	});
	
	let checkPassword = () => {
		const passwordRegex = /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*?_]).{8,16}$/;
		let test = passwordRegex.test(form.password.value);
		
		if(!test){
			checkErrors = {...checkErrors, "password" : true};
			$("#err_pwd").text("8 ~ 16자의 대소문자 및 특수문자를 포함하여 입력해 주세요.");
		}else if(test){
			checkErrors = {...checkErrors, "password" : false};
			$("#err_pwd").text('');
		}
	}
	
	form.password.addEventListener("blur", () => {
		checkPassword();
	});
	
	let checkPhone = () => {
		const phoneRegex = /^01(0|1|6|7|8|9)?([0-9]{3,4})?([0-9]{4})$/;
		let test = phoneRegex.test(form.phone.value);
		
		if(!test){
			checkErrors = {...checkErrors, "phone" : true};
			$("#err_phone").text("올바른 휴대폰 번호를 입력해 주세요.");
		}else if(test){
			checkErrors = {...checkErrors, "phone" : false};
			$("#err_phone").text("");
		}
		
	}
	
	form.phone.addEventListener("blur", () => {
		checkPhone();
	});
	
	
	
	
	let register = () => {
		const key = "<%=aesKey%>";
		const iv =  "<%=iv%>";
		
		let email = form.email.value;
		let name = form.name.value;
		let password = CryptoJS.AES.encrypt(form.password.value, CryptoJS.enc.Utf8.parse(key), {
			iv: CryptoJS.enc.Utf8.parse(iv),
			padding: CryptoJS.pad.Pkcs7,
			mode: CryptoJS.mode.CBC
		}).toString();
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
		
		if(form.password.value === '' || form.password.value.length === 0){
			alert("비밀번호를 입력해 주세요.");
			return;
		}
		
		if(phone === '' || phone.length === 0){
			alert("전화번호를 입력해 주세요.");
			return;
		}
		
		for(let key in checkErrors){
			if(checkErrors[key]){
				alert("오류 항목을 확인해 주세요.");
				return;
			}
		}
		
		$.ajax({
			url: "/user",
			data: reqJson,
			type: "post",
			async: false,
			contentType: "application/json",
			success: (data,textStatus,jqXHR) => {
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