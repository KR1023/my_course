<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MyCourse</title>
<link rel="stylesheet" href="/css/notice_add.css" />
<link rel="stylesheet" href="/css/quill/quill.snow.css" />
<script type="text/javascript" src="/javascript/quill/quill.js"></script>
</head>
<body>
<div class="notice_add">
	<div class="wrapper">
		<jsp:include page="../common/header.jsp" />
		<hr />
		<nav>
			<img src="/images/arrow/arrow_back.svg" alt="go_back" onClick="goBack()"/>
		</nav>
		<h2>공지사항 수정</h2>
		<div class="add_container">
			<div class="title">
				<span>제목</span>
				<input id="title" type="text" name="title" value="${notice.title }" placeholder="제목을 입력해 주세요." />
				<div class="btn_container">
					<button onClick="updateNotice()">수정</button>
					<button onClick="cancelUpdating()">취소</button>
				</div>
			</div>
			<div class="content">
				<p>내용</p>
				<div class="editor">
					<div id="quill_editor" class="quill_editor"></div>
				</div>
			</div>
		</div>
	</div>
</div>
</body>
<script type="text/javascript">

$(window).on("load", () => {
	quill.root.innerHTML = '${notice.content}';
});

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

const updateNotice = () => {
	if(confirm("공지사항을 수정하시겠습니까?")){
		let title = $("#title").val();
		
		if(title.length === 0){
			alert("제목을 입력해 주세요.");
			return;
		}
		
		const reqJson = JSON.stringify({"title": title, "content": quill.root.innerHTML});
		
		$.ajax({
			url: "/api/notice/"+${notice.noticeId},
			type: "PUT",
			contentType: "application/json",
			data: reqJson,
			async: false,
			cache: false,
			success: (data, textStatus, jqXHR) => {
				if(jqXHR.status === 200){
					alert("공지사항이 수정되었습니다.");
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

const cancelUpdating = () => {
	if(confirm("수정을 취소하시겠습니까?")){
		location.href = "/notice";
	}
		
		
}
</script>
</html>