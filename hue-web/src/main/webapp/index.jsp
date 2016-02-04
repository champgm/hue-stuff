<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" 
    import="java.util.List"
    
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
	<head>
		<script type="text/javascript" src="http://code.jquery.com/jquery-2.2.0.min.js"></script>
		<script type="text/javascript" src="js/buttonEvents.js"></script>
				
		<meta charset="utf-8">

		<!-- Always force latest IE rendering engine (even in intranet) & Chrome Frame
		Remove this if you use the .htaccess -->
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">

		<title>index</title>
		<meta name="description" content="">
		<meta name="author" content="Zhao,Na">

		<meta name="viewport" content="width=device-width; initial-scale=1.0">

		<!-- Replace favicon.ico & apple-touch-icon.png in the root of your domain and delete these references -->
		<link rel="shortcut icon" href="/favicon.ico">
		<link rel="apple-touch-icon" href="/apple-touch-icon.png">
		
		<link rel="stylesheet" type="text/css" href="style.css">
	</head>

	<body>
		<c:import url="/getlights"></c:import> 
		<div>
			<header>
				<h1>Hue Light Control</h1>
			</header>
			
			<div id="tabs">
					<section id="tab1">
						<h2><a href="#tab1">Lights</a></h2>
						<p></p>
				
						<p>										
						<c:forEach items="${lightlist}" var="light" varStatus="status">	
						
							<c:choose>
								<c:when test="${!light.getState().getOn()}">
									<button class="light-toggler" id="${light.getId()}">
							    	<img src="images/gray_light_icon.png" alt="" width=90px height=90px/><c:out value="${light.getName()}"/></button>       					
								</c:when>
								<c:when test="${light.getState().getOn()}">
									<button class="light-toggler on" id="${light.getId()}">
							    	<img src="images/gray_light_icon.png" alt="" width=90px height=90px/><c:out value="${light.getName()}"/></button>       					
								</c:when>
							</c:choose>
								
							<c:if test="${status.count % 3 == 0}"> 
								<br />
							</c:if>
							
						</c:forEach> 
						
					</section>
	
					<section id="tab2">
						<h2><a href="#tab2">Scenes</a></h2>
						<p>This content appears on tab 2.</p>
						
						
					</section>
	
					<section id="tab3">
						<h2><a href="#tab3">Tab 3</a></h2>
						<p>This content appears on tab 3.</p>
						
						
					</section>			
			</div>

			<footer>
				<p>
					
				</p>
			</footer>
		</div>
	</body>
</html>
 