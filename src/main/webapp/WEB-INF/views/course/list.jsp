<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>MyCourse</title>
<link rel="stylesheet" href="/css/index.css" />
<link rel="stylesheet" href="/css/main.css" />
<script type="text/javascript" src="/javascript/jquery/jquery-3.7.1.min.js"></script>
</head>
<body>
    <div class="main">
        <div class="wrapper">
            <jsp:include page="../common/header.jsp" />
            <hr />
            <nav>
                <div class="category" id="category_btn">
                    <img src="./images/menu.svg" alt="category" />
                    <div class="category_container" id="cat_container">
			            <p>강의</p>
			            <p>공지사항</p>
			        </div>
                </div>
                <div class="search">
                    <input type="text" name="course_name" placeholder="찾으시는 강의를 입력해 주세요."/>
                    <img src="./images/search.svg" alt="search" />
                </div>
            </nav>
            <div class="option">
                <span>인기순</span>
                <span>최신순</span>
            </div>
            <div class="courseList">
	            <c:choose>
	            	<c:when test="${list.totalElements == 0}">
	            	등록된 강좌가 없습니다.
	            	</c:when>
	            	<c:otherwise>
	            		<c:forEach var="course" items="${list.content }">
	            			<div class="course" onClick="getCourse(${course.id })">
			                    <img src="./images/dashboard.svg" alt="course_img" />
			                    <div class="desc">
			                        <p>${course.courseName }</p>
			                        <p>수강 인원 : ${course.maxAttendee }</p>
			                        <p>
			                        	<fmt:parseDate value="${course.createdDt }" pattern="yyyy-MM-dd'T'HH:mm" var="parsedTime" type="both" />
			                        	등록일 : <fmt:formatDate value="${parsedTime}" pattern="YYYY-MM-dd" />
			                        </p>
			                    </div>
	                		</div>
	            		</c:forEach>
	            	</c:otherwise>
	            </c:choose>
	            
                <div class="paging">
                	
                    <a href="/course?page=0" id="first_page"><img src="./images/arrow/double_arrow_left.svg"></a>
                    <c:if test="${list.first == false }">
                    	<a href="/course?page=${list.number - 1}" id="prev_page"><img src="./images/arrow/arrow_back.svg"></a>
                    </c:if>
                    <fmt:parseNumber var="section" value="${list.number / 10 }" integerOnly="true" />
                    <fmt:parseNumber var="pageCheck" value="${list.totalPages / 10 }" integerOnly="true" />
                    
                	<c:set var="start" value="${section * 10}"/>
                	<c:set var="end" value="${(section * 10) + 9}" />
                	<c:set var="lastPage" value="${list.totalPages - 1}" />
                	
					<c:if test="${section ==  pageCheck}">
						<c:set var="end" value="${list.totalPages - 1}" />
					</c:if>	

                    <c:forEach var="i" begin="${start}" end="${end}" step="1">
                    		<c:choose>
                    			<c:when test="${(i ) == list.number}">
                    				<a href="/course?page=${i }" class="current_page">${i + 1}</a>
                    			</c:when>
                    			<c:otherwise>
                    				<a href="/course?page=${i }">${i + 1}</a>
                    			</c:otherwise>
                    		</c:choose>
                	</c:forEach>
                	<c:if test="${ list.last == false}">
                		<a href="/course?page=${list.number + 1}" id="next_page"><img src="./images/arrow/arrow_forward.svg"></a>
                	</c:if>
                    <a href="/course?page=${lastPage}" id="last_page"><img src="./images/arrow/double_arrow_right.svg"></a>
                </div>
            </div>
            <!-- <footer>
            	footer
        	</footer> -->
        </div>
       
        
    </div>
</body>
<script type="text/javascript">
    var showCategory = false;

    var target = document.getElementById("category_btn");
    
	window.addEventListener("click", (e) => {
		showCategory = false;
		target.children[0].src = "images/menu.svg";
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
        	target.children[0].src = "images/close.svg";
            $("#cat_container")
	            .css("top", `${categoryTop}px`)
	            .css("left", `${categoryLeft}px`)
	            .css("opacity", "1")
	            .css("visibility", "visible");
            showCategory = true;
        }else if(showCategory){
        	target.children[0].src = "images/menu.svg";
            $("#cat_container")
	            .css("opacity", "0")
	            .css("visibility", "hidden");
            showCategory = false;
        }
    });
    
    let getCourse = (courseId) => {
    	console.log(courseId);
    	$.ajax({
    		url: '/api/course/' + courseId,
    		type: "GET",
    		async: false,
    		success: (data, textStatus, jqXHR) => {
    			if(jqXHR.status === 200){
    				console.log(data);
    			}
    		},
    		error: (data, error) => {
    			console.error(data);
    			console.error(error);
    		}
    	});
    }
</script>
</html>