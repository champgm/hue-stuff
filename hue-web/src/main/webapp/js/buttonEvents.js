// When the page is fully loaded.
$(document).ready(function() {
	 $('button.light-toggler').click(function(event){
	        $(this).toggleClass('on');
  	
    	var lightID = this.id;    	
    	// Basic JQuery Ajax GET request. We need to pass 3 arguments:
    	// 		1. The servlet url that we will make the request to.
    	//		2. The GET data (in this case the light ID).
    	//		3. A function that will be triggered as soon as the request is successful.
    	// Optionally, you can also chain a method that will handle the possibility
    	// of a failed request.
    	$.get('togglelight', {"lightid": lightID},
            function(resp) { // on success
    			// handling (pass light state to the page for rendering?)
    				
            })
            .fail(function() { // on failure
                alert("Request failed.");
            });
    });
	 
	 $(document).on('click', "button.scene", function(event) {
		 var sceneID = this.id;
	//	 alert('activate scenes: ' + sceneID);
		 $.get('activatescene', {'sceneid': sceneID},
				function(resp) {
		 
		 })
		 .fail(function() {
			 alert('Request failed.');
		 });
		 
	 });
});