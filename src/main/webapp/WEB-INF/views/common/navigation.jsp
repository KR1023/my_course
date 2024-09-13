<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
	String loginEmail = (String)session.getAttribute("loginEmail");
	String auth = (String)session.getAttribute("userAuth");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Navigation</title>
<link rel="stylesheet" href="/css/navigation.css" />
<link rel="stylesheet" href="/css/index.css" />
<script type="text/javascript" src="/javascript/jquery/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
let inputName;

</script>
</head>
<body>
<div class="nav">
<nav>
	<div class="category" id="category_btn">
		<img src="/images/menu.svg" alt="category" />
    	<div class="category_container" id="cat_container">
			<a href="/course">강의</a>
			<a href="/notice">공지사항</a>
			<c:if test="${userAuth != null and userAuth != 'normal' }">
				<a href="/course/manage">강의 관리</a>
			</c:if>
			<c:if test="${userAuth != null and (userAuth == 'manager' or userAuth == 'admin')}">
				<a href="/instructor/manage">강사 관리</a>
			</c:if>
		</div>
   	</div>
    <div class="search">
    	<input id="course_name" type="text" name="course_name" placeholder="찾으시는 강의를 입력해 주세요." onkeydown="keyDown(this)"/>
        <img src="/images/search.svg" alt="search" onClick="findCourses()"/>
    </div>
</nav>
</div>
</body>
<script type="text/javascript">
var showCategory = false;


var target = document.getElementById("category_btn");

window.addEventListener("click", (e) => {
	showCategory = false;
	target.children[0].src = "/images/menu.svg";
	$("#cat_container")
		.css("opacity", "0")
		.css("visibility", "hidden");
})


$("#category_btn").click((e) => {
	e.stopPropagation();
	// var categoryTop = window.pageYOffset + target.getBoundingClientRect().top;
    // var categoryLeft = window.pageXOffset + target.getBoundingClientRect().left;
    
    var categoryTop = target.getBoundingClientRect().x;
    var categoryLeft = target.getBoundingClientRect().y;
    
    if(!showCategory){
    	target.children[0].src = "/images/close.svg";
        $("#cat_container")
            .css("top", `${categoryTop}px`)
            .css("left", `${categoryLeft}px`)
            .css("opacity", "1")
            .css("visibility", "visible");
        showCategory = true;
    }else if(showCategory){
    	target.children[0].src = "/images/menu.svg";
        $("#cat_container")
            .css("opacity", "0")
            .css("visibility", "hidden");
        showCategory = false;
    }
});

let findCourses = () => {
	inputName = $("#course_name").val();
	
	const nameLength = inputName.length;
	
	if(nameLength > 0 && nameLength < 2){
		alert("두 자 이상 입력해 주세요.");
		return;
	}else{
		const { pathname } = window.location;
		console.log(pathname);
		if(pathname === '/course'){
			location.href = '/course?courseName=' + inputName;
		}else if(pathname === '/course/manage'){
			location.href = '/course/manage?courseName=' + inputName;
		}
	}
};

let keyDown = () => {
	const { keyCode } = event;
	
	if(keyCode === 13){
		findCourses();
	}
}

</script>
</html>