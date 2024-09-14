<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MyCourse</title>
<link rel="stylesheet" href="/css/user_manage.css" />
</head>
<body>
<div class="user_manage">
	<div class="wrapper">
		<jsp:include page="../../common/header.jsp" />
		<hr />
		<jsp:include page="../../common/navigation.jsp" />
		<div class="manage_container">
			<h3>사용자 관리</h3>
			<div class="list">
			<table>
				<tr>
					<td width="25%" >이메일</td>
					<td width="15%">이름</td>
					<td width="20%">전화번호</td>
					<td width="15%">권한</td>
					<td width="20%">가입일</td>
					<td width="5%"></td>
				</tr>
				<c:forEach var="user" items="${ list.content}">
					<tr>
						<td>${user.email }</td>
						<td>${user.name }</td>
						<td>${user.phone}</td>
						<td>
							<select id="auth_selector" onchange="selectAuth(this, '${user.email }')">
								<option value="normal" ${user.auth == 'normal' ? 'selected="selected"' : '' }>수강생</option>
								<option value="instructor" ${user.auth == 'instructor' ? 'selected="selected"' : '' }>강사</option>
								<option value="manager" ${user.auth == 'manager' ? 'selected="selected"' : '' }>관리자</option>
							</select>
						</td>
						<fmt:parseDate value="${user.createdDt }" pattern="yyyy-MM-dd'T'HH:mm" var="parsedCreated" type="both" />
						<fmt:formatDate pattern="yy-MM-dd" value="${parsedCreated }" var="createdDt"/>
						<td>${createdDt }</td>
						<td>
							<button class="delete_btn" onClick="deleteUser('${user.email}')">삭제</button>
						</td>
					</tr>
				</c:forEach>
				</table>
				<c:if test="${list.totalElements == 0 }">
					<div class="no_contents">
						<p>등록된 강의가 없습니다.</p>
					</div>
				</c:if>
			</div>
			<div class="paging">
				<c:choose>
					<c:when test="${list.totalElements == 0}">
						<a href="/user/manage?page=0" id="first_page"><img src="/images/arrow/double_arrow_left.svg"></a>
						1
						<a href="/user/manage?page=${lastPage}" id="last_page"><img src="/images/arrow/double_arrow_right.svg"></a>
					</c:when>
					<c:otherwise>
						<a href="/user/manage?page=0" id="first_page"><img src="/images/arrow/double_arrow_left.svg"></a>
                    	<c:if test="${list.first == false }">
                    		<a href="/user/manage?page=${list.number - 1}" id="prev_page"><img src="/images/arrow/arrow_back.svg"></a>
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
                    				<a href="/user/manage?page=${i }" class="current_page">${i + 1}</a>
                    			</c:when>
                    			<c:otherwise>
                    				<a href="/user/manage?page=${i }">${i + 1}</a>
                    			</c:otherwise>
                    		</c:choose>
	                	</c:forEach>
	                	<c:if test="${ list.last == false}">
	                		<a href="/user/manage?page=${list.number + 1}" id="next_page"><img src="/images/arrow/arrow_forward.svg"></a>
	                	</c:if>
                	    <a href="/user/manage?page=${lastPage}" id="last_page"><img src="/images/arrow/double_arrow_right.svg"></a>
					</c:otherwise>
				</c:choose>
        	</div>
		</div>
		
	</div>
</div>
</body>
<script type="text/javascript">
const deleteUser = (email) => {
	if(confirm("사용자를 삭제하시겠습니까?")){
		$.ajax({
			url: "/user/"+email,
			type: "DELETE",
			async: false,
			success: (data, textStatus, jqXHR) => {
				if(jqXHR.status === 200){
					alert("삭제되었습니다.");
					location.reload(true);
				}
			},
			error: (data) => {
				console.error(data);
				alert("내부적인 오류가 발생했습니다. 잠시 후에 다시 시도해 주세요.");
			}
		});
	}
};

function selectAuth(e, email){
	const selectedVal = e.value;
	
	$.ajax({
		url:"/user/auth/"+email,
		type: "PUT",
		async: false,
		contentType: "application/json",
		data: selectedVal,
		success: (data, textStatus, jqXHR) => {
			console.log(data);
			if(jqXHR.status === 200){
				location.reload(true);
			}
		},
		error: (data, error) => {
			console.error(data);
			console.error(error);
			alert("내부적인 오류가 발생했습니다. 잠시 후에 다시 시도해 주세요.");
		}
	});
}

</script>
</html>