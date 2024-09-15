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
<link rel="stylesheet" href="/css/quill/quill.snow.css" />
<script type="text/javascript" src="/javascript/quill/quill.js"></script>
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
				<div class="container">
					<span>${notice.username }</span>
					<c:if test="${userAuth == 'manager' or userAuth == 'admin' }">
						<button onClick="moveToUpdate(${notice.noticeId})">수정</button>
					</c:if>
				</div>
			</div>
			<div class="createdDate">
				<span>${createdDt }</span>
				<c:if test="${userAuth == 'manager' or userAuth == 'admin' }">
						<button onClick="deleteNotice(${notice.noticeId})">삭제</button>
				</c:if>
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

const moveToUpdate = (noticeId) => {
	location.href = "/notice/update/"+noticeId;
}

const deleteNotice = (noticeId) => {
	if(confirm("공지사항을 삭제하시겠습니까?")){
		$.ajax({
			url: "/api/notice/"+noticeId,
			type: "DELETE",
			async: false,
			success: (data, textStatus, jqXHR) => {
				if(jqXHR.status === 200){
					alert("공지사항이 삭제되었습니다.");
					location.href = "/notice";
				}
			},
			error: (data) => {
				console.error(data);
				alert("내부적인 오류가 발생했습니다. 잠시 후에 다시 시도해 주세요.");
			}
		});	
	}
}
</script>
</html>