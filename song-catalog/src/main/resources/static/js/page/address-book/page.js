$(document).ready(function(){
	if (isMobile){
		$('.set-list-container .action-container').css({"max-height": "40px"});
	}
	if (window.length < 900){
		addScrollListener('#addressBookEntries');
	}
});