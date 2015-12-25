<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>GM管理系统</title>
<script type="text/javascript" src="http://apps.bdimg.com/libs/jquery/2.1.1/jquery.min.js"></script>
<script type="text/javascript" src="../js/admin/login.js"></script>
</head>
<body>
	<div align="center">
		<table>
			<tr>
				<th>用户名</th>
				<td><input type="text" name="name" id="loginname" /></td>
			</tr>
			<tr>
				<th>密码</th>
				<td><input type="password" name="pwd" id="password" /></td>
			</tr>
			<tr>
				<td colspan="2" align="center">
					<button id="loginBtn" onclick="login()">登录</button>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>
