var ignoreFocusSelector = ".song-title-link, .zoom-min, .zoom-plus, .zoom-val, .zoom-val span, img, audio,.slide-arrow,svg,path";
$(document).ready(function(){
	$(document).on('click','.song-page-container:not(.modal-page-container)', function(e){
		if ($(location).attr('href').includes('/setlists')){
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
		}
		else if ($(location).attr('href').includes('/setlist')){
			if (!$(e.target).is(ignoreFocusSelector)){
				if ($(this).hasClass('scroll-focused')){
					$(this).removeClass('scroll-focused');
					$(this).removeClass('mh-none');
					$('.set-list-page>.row')[0].scrollTo({
						top: 0,
						behavior: 'smooth'
					});
				}
				else {
					$('.set-list-page>.row')[0].scrollTo({
						top: $('.set-list-page>.row')[0].scrollHeight,
						behavior: 'smooth'
					});
					$(this).addClass('scroll-focused');
					$(this).addClass('mh-none');
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

