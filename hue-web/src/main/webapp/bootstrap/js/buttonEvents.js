$(document).ready(function() {

	$('.light').on('change', function() {
		// if ($(this).prop('checked')) {
		// do what needed here
		var lightId = this.id;
		$.get('togglelight', {
			'lightid' : lightId
		}, function(resp) {

		}).fail(function() {
			alert('request failed!');
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
});