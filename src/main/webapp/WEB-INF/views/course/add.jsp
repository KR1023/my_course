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
				<img id="uploaded_image" src="/images/dashboard.svg" />
				<button id="img_upload_btn" onClick="uploadImage()">이미지 업로드</button>
				<span id="uploaded_filename">파일을 업로드해 주세요.</span>
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
let fileId = null;

let goBack = () => {
	let referrer = document.referrer;
	location.replace(referrer);
};

let uploadImage = () => {
	const formData = new FormData();
	const fileInput = document.createElement('input');
	
    fileInput.setAttribute("type", "file");
    fileInput.setAttribute("accept", ".jpg,.jpeg,.png");
    fileInput.setAttribute("multiple", false);
    fileInput.click();
    
    
    
    fileInput.addEventListener('change', () => {
    	let file = fileInput.files[0];
    	formData.append("files", file);
    	
    	$.ajax({
    		url: "/image",
    		type: "POST",
    		contentType: false,
    		processData: false,
    		data: formData,
    		success: (data, textStatus, jqXHR) => {
    			if(jqXHR.status === 200){
    				$("#uploaded_image").attr("src", data.refFilePath);
    				$("#uploaded_filename").text(data.filename);
    				fileId = data.fileId;
    			}
    		},
    		error: (data, error) => {
    			console.error(data);
    			console.error(error);
    		}
    	});
    });
    

}

const imageHandler = () => {
	const input = document.createElement("input");
	input.setAttribute('type', 'file');
	input.setAttribute('accept', 'image/*');
	input.click();
	
	input.addEventListener('change', () => {
		const file = input.files[0];
		const formData = new FormData();
		formData.append('files', file);
		
		$.ajax({
			url: '/image',
			type: 'POST',
			contentType: false,
			processData: false,
			async: false,
			data: formData,
			success: (data, textStatus, jqXHR) => {
    			if(jqXHR.status === 200){
    				const imgUrl = data.refFilePath;
    				const editor = quill;
    				
    				const range = editor.getSelection();
    				editor.insertEmbed(range.index, 'image', imgUrl);
    			}
    		},
    		error: (data, error) => {
    			console.error(data);
    			console.error(error);
    		}
		})
	})
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
		toolbar: {
			container: toolbarOptions,
			handlers: {
				image: imageHandler
			}
		},
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
			"fileId": fileId,
			"userEmail": userEmail
	};
	
	if(confirm("강의를 등록하시겠습니까?")){
		$.ajax({
			url: "/api/course",
			type: "POST",
			async: false,
			contentType: "application/json",
			data: JSON.stringify(reqJson),
			success: (data, textStatus, jqXHR) => {
				if(jqXHR.status === 200){
					alert("강의가 등록되었습니다.");
					location.replace("/course/" + data.id);
				}
			},
			error: (data, error) => {
				console.error(data);
				console.error(error);
			}
		});	
	}else{
		return;
	}
	
};


let cancelAddition = () => {
	if(confirm("등록을 취소하시겠습니까?")){
		location.href = "/course";
	}
}

</script>
</html>