 
$(document).ready(function() {
		$('a#tab1').click(function(event) {
			$('#container').load('index.jsp');			
			setTimeout(function() {
				    window.scrollTo(0, 0);
			}, 1);			
		});

		$('a#tab2').click(function(event) {		
			setTimeout(function() {
			  if(location.hash) {
			    window.scrollTo(0, 0);
			  }
			}, 1);
		});
 
		$('a#tab3').click(function(event) {	 
			setTimeout(function() {
			  if(location.hash) {
			    window.scrollTo(0, 0);
			  }
			}, 1);
		});
});