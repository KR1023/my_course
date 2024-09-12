<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MyCourse</title>
<link rel="stylesheet" href="/css/index.css" />
<link rel="stylesheet" href="/css/course_manage.css" />

</head>
<body>
<div class="course_manage">
	<div class="wrapper">
		<jsp:include page="../../common/header.jsp" />
		<hr />
		<jsp:include page="../../common/navigation.jsp" />
		<div class="manage_container">
			<h3>강의 관리</h3>
			<div class="management">
				<button onClick="moveAddCoursePage()">추가</button>
			</div>
			<div class="list">
				<table>
					<tr>
						<td width="7%" >강의번호</td>
						<td width="28%">강의명</td>
						<td width="8%">수강인원</td>
						<td width="8%">신청자</td>
						<td width="10%">강사</td>
						<td width="10%">등록일</td>
						<td width="10%">마감일</td>
						<td>수정/삭제</td>
					</tr>
					<c:forEach var="course" items="${ list.content}">
						<tr>
							<td>${course.id }</td>
							<td class="course_name">${course.courseName }</td>
							<td>${course.maxAttendee}</td>
							<td>5</td>
							<td>${course.userEmail }</td>
							<fmt:parseDate value="${course.createdDt }" pattern="yyyy-MM-dd'T'HH:mm" var="parsedCreated" type="both" />
							<fmt:parseDate value="${course.closeDt }" pattern="yyyy-MM-dd" var="parsedClosing" type="both" />
							<td><fmt:formatDate pattern="yy-MM-dd" value="${parsedCreated }"/></td>
							<td><fmt:formatDate pattern="yy-MM-dd" value="${parsedClosing }"/></td>
							<td><button class="update_btn">수정</button><button class="delete_btn">삭제</button></td>
						</tr>
					</c:forEach>
				</table>
			</div>
			<div class="paging">
                    <a href="/course/manage?page=0" id="first_page"><img src="/images/arrow/double_arrow_left.svg"></a>
                    <c:if test="${list.first == false }">
                    	<a href="/course/manage?page=${list.number - 1}" id="prev_page"><img src="/images/arrow/arrow_back.svg"></a>
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
                    				<a href="/course/manage?page=${i }" class="current_page">${i + 1}</a>
                    			</c:when>
                    			<c:otherwise>
                    				<a href="/course/manage?page=${i }">${i + 1}</a>
                    			</c:otherwise>
                    		</c:choose>
                	</c:forEach>
                	<c:if test="${ list.last == false}">
                		<a href="/course/manage?page=${list.number + 1}" id="next_page"><img src="/images/arrow/arrow_forward.svg"></a>
                	</c:if>
                    <a href="/course/manage?page=${lastPage}" id="last_page"><img src="/images/arrow/double_arrow_right.svg"></a>
                </div>
		</div>
	</div>
</div>
</body>
<script type="text/javascript">
let moveAddCoursePage = () => {
	location.href = "/course/add";
}
</script>
</html>