<%@page import="java.util.List"%>
<%@page import="dto.CustomerInfo"%>
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

CustomerInfo info =new CustomerInfo("한글","한글22", "333", "1");


conn.insertCustomer(info);

List<CustomerInfo>  list=conn.getList();

for(CustomerInfo row : list){
	out.println(row.toString());
}

%>



</body>
</html>