<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
	String loginEmail = (String)session.getAttribute("loginEmail");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Header</title>
<link rel="stylesheet" href="/css/header.css" />
<script type="text/javascript" src="/javascript/jquery/jquery-3.7.1.min.js"></script>
</head>
<body>
<div class="header">
<header>
    <div class="logo">
        <img src="images/event_note.svg" />
        <span>MyCourse</span>
    </div>
    <div class="account">
    	<c:choose>
    		<c:when test="${empty loginEmail }">
    			<a class="login" href="/login">로그인</a>
    		</c:when>
    		<c:otherwise>
    			<a class="login" href="/logout">로그아웃</a>
    		</c:otherwise>
    	</c:choose>
        
    </div>
</header>
</div>
</body>
</html>