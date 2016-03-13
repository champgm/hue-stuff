$(document).ready(function() {

	$('.light').on('change', function() {
		// if ($(this).prop('checked')) {
		// do what needed here
		var lightId = this.id;
		$.get('togglelight', {
			'lightid' : lightId
		}, function(resp) {

		}).fail(function() {
			alert('light toggle request failed!');
		})
		// }
		// else {
		// do what needed here
		// }
	});
	
	$('.group').on('change', function() {
		// if ($(this).prop('checked')) {
		// do what needed here
		var groupId = this.id;
		$.get('togglegroup', {
			'groupid' : groupId
		}, function(resp) {

		}).fail(function() {
			alert('request failed!');
		})
		// }
		// else {
		// do what needed here
		// }
	});

	$('.btn-default').on('click', function() {
		$(this).css('color','red');
		var sceneId = this.id;
		$.get('activatescene', {
			'sceneid' : sceneId
		}, function(resp) {

		}).fail(function() {
			alert('scene activation request failed');
		})
	})	
	
});