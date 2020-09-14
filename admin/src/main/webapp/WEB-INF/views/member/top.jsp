<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<title>회원관리</title>
	<link rel="stylesheet" type="text/css" 	href="${pageContext.request.contextPath}/resources/css/style.css" />
</head>
<c:set var = "member_no" value = "${sedto.member_no}"/>
<body>
	<div align="center">
	<table border="1" width="800" height="600">
		<tr height="10%">
			<td align="center">
				<a href = "main.do">홈으로</a> |
				<c:if test="${empty sedto}">
					<a href = "member_login.do">로그인</a> |
					<a href = "member_input.do">회원가입</a> |
				</c:if>
				<c:if test="${not empty sedto}">
					<a href = "member_logout.do">로그아웃</a> |
					<a href = "member_mypage.do">마이페이지</a> |
					<!-- position 비교해서 버스, 일반, 호텔관리자 출력 -->
					<c:if test = "${sedto.position == 0}">
						<a href = "member_admin.do">관리자 페이지</a> |
						<a href = "member_list.do">회원목록</a> |
						<a href = "member_list.do?mode=find">회원찾기</a> |		
					</c:if >
					<c:if test = "${sedto.position == 1}">
						<a href = "#">버스 관리자 페이지</a> |
						<a href = "#">버스목록</a> |
					</c:if >
					<c:if test = "${sedto.position == 2}">
						<a href = "#">호텔 관리자 페이지</a> |
						<a href = "#">호텔목록</a> | 
					</c:if>
				</c:if>
				<a href = "#">게시판</a>
			</td>
 		</tr>
 		<tr height="80%">
 			<td>