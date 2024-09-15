<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:parseDate value="${notice.createdDt }" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDatetime" type="both" />
<fmt:formatDate pattern="yyyy년 MM월 dd일" value="${parsedDatetime }" var="createdDt"/>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MyCourse</title>
<link rel="stylesheet" href="/css/notice_detail.css" />
</head>
<body>
<div class="notice_detail">
	<div class="wrapper">
		<jsp:include page="../common/header.jsp" />
		<hr />
		<nav>
			<img src="/images/arrow/arrow_back.svg" alt="go_back" onClick="goBack()"/>
		</nav>
		<div class="detail_container">
			<div class="title">
				<span>${notice.title}</span>
				<span>${notice.username }</span>
			</div>
			<div class="createdDate">
				<p>${createdDt }</p>
			</div>
			<hr style="margin-top: 35px;"/>
			<div class="notice_content">
				${notice.content }
			</div>
		</div>
		
	</div>
</div>
</body>
<script type="text/javascript">
const goBack = () => {
	let referrer = document.referrer;
	location.replace(referrer);
}
</script>
</html>