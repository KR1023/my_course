<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
		<h1>강좌명 : ${course.courseName}</h1>
		<h2>최대 수강인원 : ${course.maxAttendee }</h2>
		<h2>내용 : ${course.content }</h2>
		<fmt:parseDate value="${course.createdDt }" pattern="yyyy-MM-dd'T'HH:mm:ss" var="parsedDatetime" type="both" />
		<h2>등록일 : <fmt:formatDate pattern="yyyy년 MM월 dd일" value="${parsedDatetime }" /></h2>
		<h2>등록 강사 이메일 : ${course.userEmail }</h2>
	</div>
</div>
</body>
</html>