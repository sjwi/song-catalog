var ignoreFocusSelector = "i, .song-title-link, .zoom-min, .zoom-plus, .zoom-val, .zoom-val span, .zoom-btn, img, audio,.slide-arrow,svg,path";
$(document).ready(function(){
	$(document).on('click','.song-page-container:not(.modal-page-container)', function(e){
		if ($(location).attr('href').includes('/setlists')){
			if (!$(e.target).is(ignoreFocusSelector)){
				if ($(this).hasClass('focused-page')){
					$('#overlay').removeClass("overlay");
					$(this).removeClass('focused-page');
					unlockScroll();
				} else {
					$('#overlay').addClass("overlay");
					$(this).addClass('focused-page');
					lockScroll();
				}
			}
		}
		else if ($(location).attr('href').includes('/setlist')){
			if (!$(e.target).is(ignoreFocusSelector)){
				const container = $(window).width() > 900? window: $('#lock')[0];
				const scrollHeight = $(window).width() > 900? $('body').prop('scrollHeight'): $(container).prop('scrollHeight');

				var isScrolledToBottom = $(container).scrollTop() + $(container).height() >= scrollHeight - 60

				if (isScrolledToBottom){
					$('.song-page').each(function() {
						$(this)[0].scrollTo({
							top: 0,
							behavior: 'smooth'
						});
					});
					container.scrollTo({
						top: 0,
						behavior: 'smooth'
					});
				}
				else {
					container.scrollTo({
						top: scrollHeight,
						behavior: 'smooth'
					});
				}
			}
		}
	});
	$(document).on('click','#overlay', function(e){
		$('#overlay').removeClass("overlay");
		$('.song-page-container').removeClass('focused-page');
		$('.song-page-container').addClass('unfocused-page');
		unlockScroll();
	});
});

