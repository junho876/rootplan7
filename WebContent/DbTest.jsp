<%@page import="dao.ConnectDB"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>


<%

ConnectDB conn =new ConnectDB();
out.println("접속"+conn.getConnection());
	

%>



</body>
</html>