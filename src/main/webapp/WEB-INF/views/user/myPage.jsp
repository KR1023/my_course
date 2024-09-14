<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.io.InputStream, java.util.Properties" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

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

<fmt:parseDate value="${user.createdDt }" pattern="yyyy-MM-dd'T'HH:mm:ss" var="parsedDt"/>
<fmt:formatDate pattern="yyyy-MM-dd" value="${parsedDt }" var="createdDt"/>
							
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>myCourse</title>
<link rel="stylesheet" href="/css/my_page.css" />
<script type="text/javascript" src="/javascript/jquery/jquery-3.7.1.min.js"></script>
<script type="text/javascript" src="/javascript/crypto/crypto-js.min.js"></script>
</head>
<body>
<div class="my_page">
	<div class="wrapper">
		<jsp:include page="../common/header.jsp" />
		<hr />
		<div class="greeting">
			<p>안녕하세요, ${user.name }님!</p>
		</div>
		<div class="info_container">
			<div class="tab_chooser">
				<div class="tab"><span>내 정보</span></div>
				<div class="tab"><span>수강 목록</span></div>
			</div>
			<div id="contents" class="content_container">
				<header>
					<p>내 정보</p>
				</header>
				<div class="user_container">
					<form id="user_form">
						<div class="user_email">
							<span>이메일</span>
							<input type="email" name="email" value="${user.email }" disabled />
							<p class="err_msg"></p>
						</div>			
						<div class="user_name">
							<span>이름</span>
							<input type="text" name="name" value="${user.name }" placeholder="이름"/>
							<p id="err_name" class="err_msg"></p>
						</div>
						<div class="user_pwd">
							<span>비밀번호</span>
							<input type="password" name="password"/>
							<p id="err_pwd" class="err_msg"></p>
						</div>			
						<div class="user_phone">
							<span>전화번호</span>
							<input type="text" name="phone" value="${user.phone }" placeholder="전화번화(01012341234)"/>
							<p id="err_phone" class="err_msg"></p>
						</div>
						<div class="user_auth">
							<span>권한</span>
							<input type="text" value="${user.auth }" disabled/>
							<p class="err_msg"></p>
						</div>
						<div class="user_created">
							<span>가입일</span>
							<input type="text" value="${createdDt }" disabled/>
							<p class="err_msg"></p>
						</div>
					</form>
				</div>
				<div class="btn_container">
					<button id="update_user" onClick="updateUser()">수정하기</button>
					<button id="withdrawal">탈퇴하기</button>
				</div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
let form = document.getElementById("user_form");

console.log(form);
let checkErrors = {
		"name" : true,
		"password" : true,
		"phone" : true
	};

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
	
	let password = form.password.value;
	
	let test = passwordRegex.test(password);
	
	if(password.length > 0){
		if(!test){
			checkErrors = {...checkErrors, "password" : true};
			$("#err_pwd").text("8 ~ 16자의 대소문자 및 특수문자를 포함하여 입력해 주세요.");
		}else if(test){
			checkErrors = {...checkErrors, "password" : false};
			$("#err_pwd").text('');
		}		
	}else if(password.length === 0){
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

let updateUser = () => {
	
	if(confirm("정보를 수정하시겠습니까?")){
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
		
		
		let reqJson = ''; 
		
		if(form.password.value.length === 0){
			reqJson = JSON.stringify({"name":name, "phone":phone});
		}else{
			reqJson = JSON.stringify({"name":name, "password":password, "phone":phone});
		}
			
		
		if(email === '' || email.length === 0){
			alert("이메일을 입력해 주세요.");
			return;
		}
		
		if(name === '' || name.length === 0){
			alert("이름을 입력해 주세요.");
			return;
		}else if(name.length > 0){
			checkErrors = {...checkErrors, name: false}
		}
		
		if(phone === '' || phone.length === 0){
			alert("전화번호를 입력해 주세요.");
			return;
		}else if(phone.length > 0){
			checkErrors = {...checkErrors, phone: false}
		}
		
		for(let key in checkErrors){
			if(checkErrors[key]){
				alert("오류 항목을 확인해 주세요.");
				return;
			}
		}
		
		$.ajax({
			url: "/user/${user.email}",
			data: reqJson,
			type: "PUT",
			async: false,
			contentType: "application/json",
			success: (data,textStatus,jqXHR) => {
				if(jqXHR.status === 200){
					alert("회원정보가 수정되었습니다..");
					location.reload(true);
				}
			},
			error: (data, error) => {
				console.error(data);
				console.error(error);
				alert("내부적인 오류가 발생했습니다. 잠시 후에 다시 시도해 주세요.");
			}
		});	
	}
}

</script>
</body>
</html>