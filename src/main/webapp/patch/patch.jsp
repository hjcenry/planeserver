<%@page import="com.kidbear._36.manager.junzhu.JunZhu"%>
<%@page import="com.kidbear._36.util.redis.Redis"%>
<%@page import="com.kidbear._36.util.hibernate.HibernateUtil"%>
<%@page import="com.kidbear._36.manager.building.BuildingInfo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
		JunZhu junZhu = HibernateUtil.find(JunZhu.class, 1);
		junZhu.coin = 99999999;
		junZhu.food = 99999999;
		junZhu.iron = 99999999;
		junZhu.yuanbao = 999999;
		junZhu.soldierNum = 0;
		HibernateUtil.save(junZhu);
	%>
	元宝改为<%=junZhu.yuanbao%>
	金币改为<%=junZhu.coin%>
	事务改为<%=junZhu.food%>
	精铁改为<%=junZhu.iron%>
</body>
</html>