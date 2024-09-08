<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MyCourse</title>
<link rel="stylesheet" href="/css/addCourse.css" />
<script type="module">
import returnDateString from '/javascript/date/returnDateString.js';

const today = new Date();
const tYear = today.getFullYear();
const tMonth = today.getMonth() + 1;
const tDay = today.getDate();

const dateStr = returnDateString(today);

const todayStr = `${'${tYear}'}-${'${tMonth}'}-${'${tDay}'}`;

window.addEventListener("load", () => {
	$("#sel_date").val(dateStr);
	$("#sel_date").attr("min", dateStr);
});
</script>
</head>
<body>
<div class="add_course">
	<div class="wrapper">
		<jsp:include page="../common/header.jsp" />
		<hr />
		<nav>
			<img src="/images/arrow/arrow_back.svg" alt="go_back" onClick="goBack()"/>
		</nav>
		<h2>강좌 등록</h2>
		<div class="input_container">
			<div class="thumbnail">
				<p>썸네일</p>
				<img src="/images/dashboard.svg" />
				<button id="img_upload_btn">이미지 업로드</button>
				<span>파일 이름 : dashboard.svg</span>
			</div>
			<div class="course_info">
				<div class="title">
					<span>강좌명</span>
					<input type="text" placeholder="강좌명"/>
				</div>
				<div class="attendee">
					<span>수강 인원</span>
					<select id="sel_max">
						<c:forEach var="i" begin="1" end="50">
							<option>${i }</option>
						</c:forEach>
					</select>
<!-- 					<input type="text" placeholder="수강 인원"/> -->
				</div>
				<div class="closing_date">
					<span>신청 마감일</span>
					<input id="sel_date" type="date" placeholder="마감일"/>
				</div>
				<div class="btn_container">
					<button id="add_btn" onClick="addCourse()">강좌등록</button>
					<button id="cancel_btn">등록취소</button>
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
};

let addCourse = () => {
	console.log($("#sel_max").val());
	console.log($("#sel_date").val());
};

</script>
</html>