<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>Bootstrap Example</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
<script
	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
<link
	href="https://gitcdn.github.io/bootstrap-toggle/2.2.0/css/bootstrap-toggle.min.css"
	rel="stylesheet">
<script
	src="https://gitcdn.github.io/bootstrap-toggle/2.2.0/js/bootstrap-toggle.min.js"></script>
<script type="text/javascript" src="bootstrap/js/buttonEvents.js"></script>
<link rel="stylesheet" type="text/css" href="bootstrap/style.css">
</head>
<body>

	<div class="container-fluid">
		<c:import url="/getlights"></c:import>
		<h2>Hue Light Control</h2>
		<br />
		<ul class="nav nav-tabs">
			<li class="active"><a data-toggle="tab" href="#home">Home</a></li>
			<li><a data-toggle="tab" href="#lights">Lights</a></li>
			<li><a data-toggle="tab" href="#scenes">Scenes</a></li>
			<li><a data-toggle="tab" href="#groups">Groups</a></li>
		</ul>

		<div class="tab-content">
			<div id="home" class="tab-pane fade in active">
				<h3>HOME</h3>
				<p>Welcome!</p>
			</div>
			<div id="lights" class="tab-pane fade">
				<br />
				<p></p>
				<div class="row">

					<c:forEach items="${lightlist}" var="light" varStatus="status">
						<div class="col-sm-6 col-md-4">
							<c:choose>
								<c:when test="${light.getState().getOn()}">
									<input type="checkbox" checked class="light"
										id="${light.getId()}" data-toggle="toggle"
										data-on="<br/>${light.getName()}"
										data-off="<br/>${light.getName()}" data-width="180"
										data-height="75" data-size="normal" data-style="ios"
										data-onstyle="success" data-offstyle="default">
								</c:when>
								<c:when test="${!light.getState().getOn()}">
									<input type="checkbox" class="light" id="${light.getId()}"
										data-toggle="toggle" data-on="<br/>${light.getName()}"
										data-off="<br/>${light.getName()}" data-width="180"
										data-height="75" data-size="normal" data-style="ios"
										data-onstyle="success" data-offstyle="default">
								</c:when>
							</c:choose>
						</div>
					</c:forEach>
				</div>
			</div>
			<div id="scenes" class="tab-pane fade">
				<h3>scene</h3>
				<p>show scenes here</p>
			</div>
			<div id="groups" class="tab-pane fade">
				<h3>group</h3>
				<p>show groups here</p>
			</div>
		</div>
	</div>

</body>
</html>
