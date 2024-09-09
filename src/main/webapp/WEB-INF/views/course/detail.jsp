<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:parseDate value="${course.createdDt }" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDatetime" type="both" />
<fmt:parseDate value="${course.closeDt }" pattern="yyyy-MM-dd" var="parsedCloseDatetime" type="both" />

<%
	String loginEmail = (String)session.getAttribute("loginEmail");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MyCourse</title>
<link rel="stylesheet" href="/css/detail.css" />
</head>
<body>
<div class="detail">
	<div class="wrapper">
		<jsp:include page="../common/header.jsp" />
		<hr />
		<nav>
			<img src="/images/arrow/arrow_back.svg" alt="go_back" onClick="goBack()"/>
		</nav>
		<div class="course_detail">
			<div class="title">
				<p>${course.courseName}<p>
			</div>
			<div class="course_info">
				<img src="/images/dashboard.svg" alt="image"/>
				<div class="info">
					<table>
						<tr>
							<td class="col_1">강사</td>
							<td class="col_2">${course.userEmail}</td>
						</tr>
						<tr>
							<td class="col_1">총 인원</td>
							<td class="col_2">${course.maxAttendee}</td>
						</tr>
						<tr>
							<td class="col_1">등록일</td>
							<td class="col_2"><fmt:formatDate pattern="yyyy년 MM월 dd일" value="${parsedDatetime }" /></td>
						</tr>
						<tr>
							<td class="col_1">마감일</td>
							<td class="col_2"><fmt:formatDate pattern="yyyy년 MM월 dd일" value="${parsedCloseDatetime }" /></td>
						</tr>
					</table>
					<div class="apply">
						<button id="enroll_btn" type="button" onClick="enroll()">수강 신청</button>
					</div>
				</div>
			</div>
			<hr />
			<div class="course_content">
				<h2>강의 소개</h2>
				<div class="content">
					${course.content }
				</div>
			</div>
		</div>
		
	</div>
</div>
</body>
<script type="text/javascript">

	window.addEventListener("load", () => {
		let courseId = ${course.id};
		let loginEmail = "<%= loginEmail %>";
		
		let reqJson = JSON.stringify({"courseId": courseId, "userEmail": loginEmail});
		
			$.ajax({
				url: "/api/enroll/check",
				method: "POST",
				async: false,
				contentType: "application/json",
				data: reqJson,
				success: (data, textStatus, jqXHR) => {
					if(jqXHR.status === 200){
						if(data === "alreadyEnrolled"){
							$("#enroll_btn")
								.text("수강 취소")
								.attr("onClick", "cancelCourse()");
							/*
							$("#enroll_btn").css("display", "none");
							let parent = document.getElementsByClassName("apply");
							let p = document.createElement("p");
							p.innerText = "이미 수강 신청한 강좌입니다.";
							parent[0].append(p);
							*/
							// $("#enroll_btn").attr("disabled", true);
						}else if(data === "overloaded"){
							$("#enroll_btn").css("display", "none");
							let parent = document.getElementsByClassName("apply");
							let p = document.createElement("p");
							p.innerText = "정원이 초과되었습니다.";
							parent[0].append(p);
						}else if(data === "closed"){
							$("#enroll_btn").css("display", "none");
							let parent = document.getElementsByClassName("apply");
							let p = document.createElement("p");
							p.innerText = "마감되었습니다.";
							parent[0].append(p);
						}
					}
				},
				error: (data, error) => {
					alert("오류가 발생했습니다. 잠시 후에 다시 시도해 주세요.");	
					console.error(data);
					console.error(error);
				}
			});
	});
	
	let goBack = () => {
		let referrer = document.referrer;
		location.replace(referrer);
	} 
	
	let enroll = () => {
		let courseId = ${course.id};
		let loginEmail = "<%= loginEmail %>";
		
		let reqJson = JSON.stringify({"courseId": courseId, "userEmail": loginEmail});
		$.ajax({
			url: "/api/enroll",
			type: "POST",
			contentType: "application/json",
			async: true,
			data : reqJson,
			success: (data, textStatus, jqXHR) => {
				if(jqXHR.status === 200){
					if(data === 'needToLogin'){
						if(confirm("로그인이 필요합니다. 로그인 페이지로 이동하시겠습니까?")){
							location.replace("/login");						
						}
					}else if(data === "enrolled"){
						alert("수강신청이 완료되었습니다!");
						location.reload(true);
					}else if(data === "overloaded"){
						alert("정원이 초과되었습니다.");
						location.reload(true);
					}else if(data === "alreadyEnrolled"){
						alert("이미 신청한 강좌입니다!");
						location.reload(true);
					}
					
				}
			},
			error: (data, error) => {
				alert("오류가 발생했습니다. 잠시 후에 다시 시도해 주세요.");
				console.error(data);
				console.error(error);
			}
		});
	}
	
	let cancelCourse = () => {
		let courseId = ${course.id};
		let loginEmail = "<%= loginEmail %>";
		
		let reqJson = JSON.stringify({"courseId": courseId, "userEmail": loginEmail});
		
		$.ajax({
			url: "/api/enroll/cancel",
			type: "POST",
			contentType: "application/json",
			aysnc: true,
			data: reqJson,
			success: (data, textStatus, jqXHR) => {
				if(jqXHR.status === 200){
					if(data === "deleteSucceeded"){
						alert("신청이 취소되었습니다.");
						location.reload(true);
					}else if(data === "userNotFound"){
						alert("오류가 발생했습니다. 다시 로그인해 주세요.");
						location.reload(true);
					}
				}
			},
			error: (data, error) => {
				alert("오류가 발생했습니다. 잠시 후에 다시 시도해 주세요.");
				console.error(data);
				console.error(error);
			}
		});
	}
</script>
</html>