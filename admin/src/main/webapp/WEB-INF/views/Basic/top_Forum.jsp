<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<%@ include file="head.jsp" %>
  <body>
    
  <nav class="navbar navbar-expand-lg navbar-dark ftco_navbar bg-dark ftco-navbar-light" id="ftco-navbar">
    <div class="container">
      <a class="navbar-brand" href="index">DProject</a>
      <div class="collapse navbar-collapse" id="ftco-nav">
        <ul class="navbar-nav ml-auto">
          <li class="nav-item"><a href="index" class="nav-link">Home</a></li>
          <li class="nav-item"><a href="bus_main.do" class="nav-link">Bus</a></li>
          <li class="nav-item"><a href="hotel" class="nav-link">Hotels</a></li>
          <li class="nav-item"><a href="restaurant" class="nav-link">Restaurant</a></li>
          <li class="nav-item active"><a href="board_list.do" class="nav-link">Forum</a></li>
         </ul>
      </div>
      
      <div class="header-btns" id="ftco-nav">
      	<ul class="navbar-nav">
      	<li class="nav-item cta"><a href="contact" class="nav-link"><span>Login</span></a>
      		<ul class="submenu">
      			<li><a href="blog.html" class="submenu-1-first">My Page</a></li>
      			<li><a href="blog_details.html" class="submenu-1-second">Wish List</a></li>
                <li><a href="blog_details.html" class="submenu-1-third">Sign Up</a></li>
      		</ul>
      	</li>
      	</ul>
      </div>
      </div>
  </nav>
  
  	<div class="hero-wrap js-fullheight"
		style="background-image: url('${pageContext.request.contextPath}/resources/images/bgi_3.jpg'); height:70%;">
		<div class="container">
			<div
				class="row no-gutters slider-text js-fullheight align-items-center justify-content-center"
				data-scrollax-parent="true">
				<div class="col-md-9-1 ftco-animate text-center"
					data-scrollax=" properties: { translateY: '70%' }">
					<p class="breadcrumbs"
						data-scrollax="properties: { translateY: '30%', opacity: 1.6 }"
						style="margin-top: 170px">
						<span class="mr-2"><a href="index">Home</a></span> <span>Forum</span>
					</p>
					<h1 class="mb-3 bread"
						data-scrollax="properties: { translateY: '30%', opacity: 1.6 }">Forum</h1>
				</div>
			</div>
		</div>
	</div>