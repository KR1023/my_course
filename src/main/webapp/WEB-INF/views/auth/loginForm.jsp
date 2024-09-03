<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>login</title>
</head>
<body>
<h1>login</h1>
<form action="/login" method="POST">
	<input type="email" name="email" placeholder="email"/><br />
	<input type="password" name="password" placeholder="password"/><br />
	<button type="submit">Submit</button>
</form>
<a href="/register">signup</a>
</body>
</html>