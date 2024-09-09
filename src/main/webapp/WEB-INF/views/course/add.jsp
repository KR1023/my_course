<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
	String userEmail = (String)session.getAttribute("loginEmail");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MyCourse</title>
<link rel="stylesheet" href="/css/addCourse.css" />
<link rel="stylesheet" href="/css/quill/quill.snow.css" />
<script type="text/javascript" src="/javascript/quill/quill.js"></script>
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
					<input id="course_name" type="text" placeholder="강좌명"/>
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
					<input id="sel_date" type="date" required placeholder="마감일"/>
				</div>
				<div class="btn_container">
					<button id="add_btn" onClick="addCourse()">강좌등록</button>
					<button id="cancel_btn" onClick="cancelAddition()">등록취소</button>
				</div>
			</div>
		</div>
		<div class="content_container">
			<h3>강의 내용</h3>
			<div class="editor">
				<div id="quill_editor" class="quill_editor"></div>
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



const toolbarOptions = [
	[{size: ['small', false, 'large', 'huge']}],
	['bold', 'italic', 'underline', 'strike', 
		{'color': ['black','white', 'red', 'blue', 'yellow', 'green', 'pink', 'orange']}, 
		{'background': ['black','white', 'red', 'blue', 'yellow', 'green', 'pink', 'orange']},
	],
	[{'indent': '-1'}, {'indent': '+1'}, {'list': 'ordered'}, {'list': 'bullet'}],
	[{'header': [1, 2, 3, 4, 5]}],
	['blockquote', 'code-block'],
	['link', 'image'],
	['clean']

];

const quill = new Quill("#quill_editor", {
	modules: {
		toolbar: toolbarOptions,
		history: {
			delay: 1000,
			userOnly: true,
			maxStack: 50
		}
	},
	theme: 'snow'
});

/*
quill.on('text-change', (delta, oldDelta, source) => {
	if(source === 'user'){
		console.log(quill);
	}
});
*/

let addCourse = () => {
	const userEmail = "<%= userEmail %>";
	
	const date = new Date();
	const today = `${'${date.getFullYear()}${date.getMonth()}${date.getDate()}'}`;
	
	console.log(quill.root.innerHTML);
	const courseName = $("#course_name").val();
	const maxAttendee = $("#sel_max").val();
	const closingDt = $("#sel_date").val();
	
	const selectedDate = new Date(closingDt);
	const checkDate = `${'${selectedDate.getFullYear()}${selectedDate.getMonth()}${selectedDate.getDate()}'}`;
	
	if(courseName === ''){
		alert("강좌명을 입력해 주세요.");
		return;
	}
		
	
	if((parseInt(checkDate) < parseInt(today)) || closingDt === ''){
		alert("마감일을 확인해 주세요.");
		return;
	}
	
	const reqJson = {
			"courseName": courseName,
			"maxAttendee": maxAttendee,
			"closingDt": closingDt.replaceAll("-", ""),
			"content": quill.root.innerHTML,
			"userEmail": userEmail
	};
	
	$.ajax({
		url: "/api/course",
		type: "POST",
		async: false,
		contentType: "application/json",
		data: JSON.stringify(reqJson),
		success: (data, textStatus, jqXHR) => {
			if(jqXHR.status === 200){
				location.replace("/course/" + data.id);
			}
		},
		error: (data, error) => {
			console.error(data);
			console.error(error);
		}
	});
};


let cancelAddition = () => {
	if(confirm("등록을 취소하시겠습니까?")){
		location.href = "/course";
	}
}

</script>
</html>