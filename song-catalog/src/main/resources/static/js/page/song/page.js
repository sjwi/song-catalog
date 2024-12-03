var ignoreFocusSelector = ".song-title-link, .zoom-min, .zoom-plus, .zoom-val, .zoom-val span, img, audio,.slide-arrow,svg,path";
$(document).ready(function(){
	if (!$(location).attr('href').includes('/song')){
		$(document).on('click','.song-page-container:not(.modal-page-container)', function(e){
			if (!$(e.target).is(ignoreFocusSelector)){
				if ($(this).hasClass('focused-page')){
					$('#overlay').removeClass("overlay");
					$(this).removeClass('focused-page');
					unlockScroll();
					$('.back-btn').addClass('showme');
				} else {
					$('.back-btn').removeClass('showme');
					$('#overlay').addClass("overlay");
					$(this).addClass('focused-page');
					lockScroll();
				}
			}
		});
		$(document).on('click','#overlay', function(e){
			$('#overlay').removeClass("overlay");
			$('.song-page-container').removeClass('focused-page');
			$('.song-page-container').addClass('unfocused-page');
			unlockScroll();
		});
	}
});

