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
<script type="text/javascript" src="bootstrap/js/buttonEvents.js"></script>
<script type="text/javascript" src="bootstrap/js/resultsPrinter.js"></script>
<script type="text/javascript" src="bootstrap/js/tabEvents.js"></script>

<link rel="stylesheet" type="text/css" href="bootstrap/style.css">
</head>
<body>

	<div class="container-fluid">
		<h2>Hue Light Control</h2>
		<br />
		<ul class="nav nav-tabs" id="mainTab">
			<li class="active"><a data-toggle="tab" href="#home">Home</a></li>
			<li><a data-toggle="tab" href="#lights">Lights</a></li>
			<li><a data-toggle="tab" href="#scenes">Scenes</a></li>
			<li><a data-toggle="tab" href="#groups">Groups</a></li>
			<li><a data-toggle="tab" href="#schedules">Schedules</a></li>
			<li><a data-toggle="tab" href="#sensors">Sensors</a></li>
		</ul>
		<div class="tab-content">
			<div id="home" class="tab-pane fade in active">
				<h3>HOME</h3>
				<p>Welcome!</p>
			</div>
			<div id="lights" class="tab-pane fade">
				<br />
				<div id="light_row" class="row"></div>
			</div>
			<div id="scenes" class="tab-pane fade">
				<br />
				<div id="scene_row" class="row"></div>
			</div>
			<div id="groups" class="tab-pane fade">
				<br />
				<div id="group_row" class="row"></div>
			</div>
			<div id="schedules" class="tab-pane fade">
				<br />
				<div class="list-group"></div>
			</div>
			<div id="sensors" class="tab-pane fade">
				<br />
				<div class="list-group"></div>
			</div>
		</div>
	</div>
</body>
</html>