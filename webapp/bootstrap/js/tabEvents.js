$(document).ready(function() {

	$('a[data-toggle="tab"]').on('shown.bs.tab', function(e) {
		var target = $(e.target).attr("href");
		if (target == '#lights') {
			$.ajax({
				url : "getlights",
				method : "GET",
				beforeSend : function() {
					$('#lights').empty();
					$('#lights').append('</br><div id="loading_lights" class="loading" style="display:none;">' + 
    				'Getting lights. Please wait....<br>' +
					'</div>');
					$('#loading_lights').show();
				},
				success : function(resp) {
					printLights(resp);
				},
				error : function() {
					alert("retrieve lights request failed.");
				}
			});
		} else if (target == '#scenes') {
			$.ajax({
				url : "getscenes",
				method : "GET",
				data : {
					v2 : true
				},
				beforeSend : function() {
					$('#scenes').empty();
					$('#scenes').append('</br><div id="loading_scenes" class="loading" style="display:none;">' + 
    				'Getting scenes. Please wait....<br>' +
					'</div>');
					$('#loading_scenes').show();
				},
				success : function(resp) {
					printScenes(resp);
				},
				error : function() {
					alert("retrieve scenes request failed.");
				}
			});
		} else if (target == '#groups') {
			$.ajax({
				url : "getgroups",
				method : "GET",
				beforeSend : function() {
					$('#groups').empty();
					$('#groups').append('</br><div id="loading_groups" class="loading" style="display:none;">' + 
    				'Getting groups. Please wait....<br>' +
					'</div>');
					$('#loading_groups').show();
				},
				success : function(resp) {
					printGroups(resp);
				},
				error : function() {
					alert("retrieve groups request failed.");
				}
			});
		} else if (target == '#schedules') {
			$.ajax({
				url : "getschedules",
				method : "GET",
				beforeSend : function() {
					$('#schedules').empty();
					$('#schedules').append('</br><div id="loading_schedules" class="loading" style="display:none;">' + 
    				'Getting schedules. Please wait....<br>' +
					'</div>');
					$('#loading_schedules').show();
				},
				success : function(resp) {
					printSchedules(resp);
				},
				error : function() {
					alert("retrieve schedules request failed.");
				}
			});
		} else if (target == '#sensors') {
			$.ajax({
				url : "getsensors",
				method : "GET",
				beforeSend : function() {
					$('#sensors').empty();
					$('#sensors').append('</br><div id="loading_sensors" class="loading" style="display:none;">' + 
    				'Getting sensors. Please wait....<br>' +
					'</div>');
					$('#loading_sensors').show();
				},
				success : function(resp) {
					printSensors(resp);
				},
				error : function() {
					alert("retrieve sensors request failed.");
				}
			});
		}
	});
})
