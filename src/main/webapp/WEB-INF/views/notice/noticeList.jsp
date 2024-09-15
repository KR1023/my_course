<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MyCourse</title>
<link rel="stylesheet" href="/css/notice_list.css" />
</head>
<body>
<div class="notice_list">
	<div class="wrapper">
		<jsp:include page="../common/header.jsp" />
		<hr />
		<jsp:include page="../common/navigation.jsp" />
		<h2>공지사항</h2>
		<c:if test="${userAuth == 'manager' or userAuth == 'admin' }">
			<p class="add_btn_container"><button onClick="addNoticePage()">공지사항 추가</button><p>
		</c:if>
		<div class="list">
			<table>
				<tr>
					<td width="10%" >공지번호</td>
					<td width="50%">제목</td>
					<td width="20%">작성자</td>
					<td width="25%">작성일</td>
				</tr>
				<c:forEach var="notice" items="${ list.content}">
					<tr onClick="viewNotice(${notice.noticeId})">
						<td>${notice.noticeId }</td>
						<td class="course_name">${notice.title }</td>
						<td>${notice.username }</td>
						<fmt:parseDate value="${notice.createdDt }" pattern="yyyy-MM-dd'T'HH:mm" var="parsedCreated" type="both" />
						<fmt:formatDate pattern="yy-MM-dd" value="${parsedCreated }" var="createdDt"/>
						<td>${createdDt }</td>
					</tr>
				</c:forEach>
			</table>
			<c:if test="${list.totalElements == 0 }">
				<p class="no_contents">등록된 공지사항이 없습니다.</p>
			</c:if>
		</div>
		
		<div class="paging">
				<c:choose>
					<c:when test="${list.totalElements == 0}">
						<a href="/notice?page=0" id="first_page"><img src="/images/arrow/double_arrow_left.svg"></a>
						1
						<a href="/notice?page=${lastPage}" id="last_page"><img src="/images/arrow/double_arrow_right.svg"></a>
					</c:when>
					<c:otherwise>
					
						<c:choose>
							<c:when test="${noticeName != null}">
								<a href="/notice?page=0&noticeName=${noticeName }" id="first_page"><img src="/images/arrow/double_arrow_left.svg"></a>
                    			<c:if test="${list.first == false }">
                    				<a href="/notice?page=${list.number - 1}&noticeName=${noticeName}" id="prev_page"><img src="/images/arrow/arrow_back.svg"></a>
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
		                    				<a href="/notice?page=${i }&noticeName=${noticeName}" class="current_page">${i + 1}</a>
		                    			</c:when>
		                    			<c:otherwise>
		                    				<a href="/notice?page=${i }&noticeName=${noticeName }">${i + 1}</a>
		                    			</c:otherwise>
		                    		</c:choose>
			                	</c:forEach>
			                	<c:if test="${ list.last == false}">
			                		<a href="/notice?page=${list.number + 1}&noticeName=${noticeName }" id="next_page"><img src="/images/arrow/arrow_forward.svg"></a>
			                	</c:if>
		                	    <a href="/notice?page=${lastPage}&noticeName=${noticeName}" id="last_page"><img src="/images/arrow/double_arrow_right.svg"></a>
							</c:when>
							<c:otherwise>
								<a href="/notice?page=0" id="first_page"><img src="/images/arrow/double_arrow_left.svg"></a>
		                    	<c:if test="${list.first == false }">
		                    		<a href="/notice?page=${list.number - 1}" id="prev_page"><img src="/images/arrow/arrow_back.svg"></a>
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
		                    				<a href="/notice?page=${i }" class="current_page">${i + 1}</a>
		                    			</c:when>
		                    			<c:otherwise>
		                    				<a href="/notice?page=${i }">${i + 1}</a>
		                    			</c:otherwise>
		                    		</c:choose>
			                	</c:forEach>
			                	<c:if test="${ list.last == false}">
			                		<a href="/notice?page=${list.number + 1}" id="next_page"><img src="/images/arrow/arrow_forward.svg"></a>
			                	</c:if>
		                	    <a href="/notice?page=${lastPage}" id="last_page"><img src="/images/arrow/double_arrow_right.svg"></a>
							</c:otherwise>
						</c:choose>
					
						</c:otherwise>
					</c:choose>
        	</div>
	</div>
</div>
</body>
<script type="text/javascript">
const viewNotice = noticeId => {
	location.href = "/notice/"+noticeId;
};

const addNoticePage = () => {
	location.href = "/notice/add";
};

</script>
</html>