<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" 
    import="java.util.List"
    
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
	<head>
		<script type="text/javascript">
		function doFunction(clicked_id){
    		/* alert(clicked_id); */
			window.location.replace("togglelight?lightid=" + clicked_id);
		}
		</script>
		
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
						<h2><a href="#tab1">Tab 1</a></h2>
						<p>This content appears on tab 1.</p>
				
						<p>										
						<c:forEach items="${hueLightList}" var="light">
							<c:choose>
								<c:when test="${light.getState().getOn()}">
							    	<button id="${light.getId()}" style="background-color:green" onclick="doFunction(this.id)">Light: <c:out value="${light.getId()}"/>  ON </button><br/>         					
								</c:when>
								<c:when test="${!light.getState().getOn()}">
							    	<button id="${light.getId()}" style="background-color:red" onclick="doFunction(this.id)">Light: <c:out value="${light.getId()}"/> OFF </button><br/>         					
								</c:when>
							</c:choose>
						</c:forEach> 
					
					</section>
	
					<section id="tab2">
						<h2><a href="#tab2">Tab 2</a></h2>
						<p>This content appears on tab 2.</p>
						
						
					</section>
	
					<section id="tab3">
						<h2><a href="#tab3">Tab 3</a></h2>
						<p>This content appears on tab 3.</p>
						
						
					</section>			
			</div>

			<footer>
				<p>
					footer
				</p>
			</footer>
		</div>
	</body>
</html>
 