function addScrollListener(id, size, delay, elem){
	if (!elem)
		elem = window
	if (!delay && size) {
		delay = Math.abs(size);
	} else {
		delay = 0;
	}
	if (!size){
		size = '-102px';
	} else {
		size = size.toString() + 'px';
	}
	var prevScrollpos = $(elem).scrollTop();
	var velocityThreshold = 5;
	if ($(elem).scrollTop() != 0) {
		$(id).css('top',size);
		$('.sticky-top-nav, .top-nav').css('top',size);
	}
	$(document).ready(function(){
		$(elem).on('scroll',function(e){
			var currentScrollPos  = $(elem).scrollTop();
			var velocity = Math.abs(prevScrollpos - currentScrollPos);
			if ((prevScrollpos > currentScrollPos && velocity > velocityThreshold) || $(elem).scrollTop() <= delay) {
				$(id).css('top','0');
				$('.sticky-top-nav, .top-nav').css('top','0');
				if (slideAlertUp && $('.add-songs-alert').hasClass('slide-down') && checkedCacheSize() != 0)
					slideAlertUp()
			} else if (velocity > velocityThreshold) {
				$(id).css('top',size);
				$('.sticky-top-nav, .top-nav').css('top',size);
				if (slideAlertDown && !$('.add-songs-alert').hasClass('slide-down') && checkedCacheSize() != 0)
					slideAlertDown()
			}
			prevScrollpos = currentScrollPos;	
		});
	})
}

function hideCategoriesOnScrollListener(id, scrollElem){
	var prevScrollpos_1 = $('.song-section').scrollTop();
	$(document).ready(function(){
		$(scrollElem).on('scroll',function(e){
			var currentScrollPos_1 = $(scrollElem).scrollTop();
			if (prevScrollpos_1 > currentScrollPos_1) { 
				$(id).slideDown('fast');
				if (slideAlertUp && $('.add-songs-alert').hasClass('slide-down') && checkedCacheSize() != 0)
					slideAlertUp()
			} else {
				$(id).slideUp('fast');
				if (slideAlertDown && !$('.add-songs-alert').hasClass('slide-down') && checkedCacheSize() != 0)
					slideAlertDown()
			}
			prevScrollpos_1 = currentScrollPos_1;	
		});
	})
}