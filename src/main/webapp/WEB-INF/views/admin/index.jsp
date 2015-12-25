<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>管理系统</title>
<script>
	//整体返回
	function parentonlick() {
		location.href = "login";
	}
	function skip(path) {
		$('#right').load(path);
	}
</script>
<link rel="stylesheet" href="../css/admin/style.css">
<link type="text/css" rel="stylesheet" href="../css/admin/reset_pc.css" />
<link type="text/css" rel="stylesheet" href="../css/admin/admin.css" />
<!-- 图标CSS 然后css调用fonts目录下的文件 -->
<link type="text/css" rel="stylesheet"
	href="../css/font-awesome.min.css" />

<link type="text/css" rel="stylesheet" href="../css/tablestyle.css" />
<link type="text/css" rel="stylesheet" href="../css/style.css" />
<link type="text/css" rel="stylesheet" href="../css/tips.css" />
<script type="text/javascript" src="../js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="../js/admin/menu.js"></script>
</head>
<body>
	<div class="top">
		<!-- <div class="logo"></div>
		<div class="title"></div> -->

	</div>
	<div class="main">
		<div class="left">
			<ul class="page-sidebar-menu">
				<li>
					<a href="#"><i class="fa fa-table"></i><span>活动管理</span><span class="arrow"></span></a>
					<ul class="sub-menu">
						<li><a href="#" onclick="javascript:skip('../activity/cdkey')">活动码</a></li>
					</ul>
				</li>
				<li><a href="#"><i class="fa fa-table"></i><span>账号管理</span><span
						class="arrow"></span></a>
					<ul class="sub-menu">
						<li><a href="../admin/logout">退出</a></li>
					</ul></li>
			</ul>
		</div>
		<div class="right" id="right"></div>
	</div>
	<div class="foot"></div>
</body>
</html>