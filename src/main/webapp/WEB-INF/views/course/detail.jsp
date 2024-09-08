<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:parseDate value="${course.createdDt }" pattern="yyyy-MM-dd'T'HH:mm:ss" var="parsedDatetime" type="both" />
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
					</table>
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
	let goBack = () => {
		let referrer = document.referrer;
		location.replace(referrer);
	} 
</script>
</html>