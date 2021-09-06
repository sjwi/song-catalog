function addScrollListener(id, size, delay){
	if (!size){
		size = '-102px';
	} else {
		size = size.toString() + 'px';
	}
	if (!delay){
		delay = 0;
	}
	var prevScrollpos = window.pageYOffset;
	var velocityThreshold = 25;
	$(document).ready(function(){
		$(window).on('scroll',function(e){
			var currentScrollPos = window.pageYOffset;
			var velocity = Math.abs(prevScrollpos - currentScrollPos);
			if ((prevScrollpos > currentScrollPos && velocity > velocityThreshold) || $(window).scrollTop() <= delay) {
				$(id).css('top','0');
				$('.sticky-top-nav, .top-nav').css('top','0');
			} else if (velocity > velocityThreshold && $(window).scrollTop() > delay) {
				$(id).css('top',size);
				$('.sticky-top-nav, .top-nav').css('top',size);
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
			} else {
				$(id).slideUp('fast');
			}
			prevScrollpos_1 = currentScrollPos_1;	
		});
	})
}