<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- /webapp/WEB-INF/view/user/main.jsp --%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 정보</title>
</head>
<body>
	<h2>환영합니다. ${sessionScope.loginUser.username }님</h2><hr color="black;">
	<a href="mypage.shop?id=${loginUser.userid }">My page</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<a href="logout.shop">로그아웃</a>
	<hr color="black;">
</body>
</html>