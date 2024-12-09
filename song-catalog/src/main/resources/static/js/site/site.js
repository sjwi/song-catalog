var mousedown = false;
var typingTimer;
var doneTypingInterval = 500; 
var BAD_ENROLLMENT_TOKEN = "Your enrollment token has already been used or is invalid.";
var BAD_RESET_TOKEN = "Your password reset token has expired or is invalid.";
var isMobile = false; 
var currentScroll=0;
$(document).on('mousedown', function(e){
	mousedown = true;
});
$(document).on('mousedown', function(){
	mousedown = false;
});
$(document).on('click touch',function(e){
	if ($(e.target).is('.open-nav,.open-nav>span,.nav-item .nav-link:not(.export),#sideNav')){
		$('#sideNav').addClass('slide-in');
		$('.scroll-lock').css('pointer-events','none');
	} else {
		$('#sideNav').removeClass('slide-in');
		$('.scroll-lock').css('pointer-events','auto');
	}
})

const ignoreSongClickedSelector = ".action-btn,.song-dropdown-btn,.dropdown-menu, li, .dropdown-item,.grip-container,img,.round,.round>*";

$(document).ready(function(e){
	if(/(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|ipad|iris|kindle|Android|Silk|lge |maemo|midp|mmp|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows (ce|phone)|xda|xiino/i.test(navigator.userAgent) 
		|| /1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i.test(navigator.userAgent.substr(0,4))) { 
		isMobile = true;
	}
	if (alertOnLoad){
		alertWithFade('warning',window[alertOnLoad]);
	}
	$(document).on('keydown','textarea',function(e){
		if(e.keyCode===9){var v=this.value,s=this.selectionStart,e=this.selectionEnd;this.value=v.substring(0, s)+'\t'+v.substring(e);this.selectionStart=this.selectionEnd=s+1;return false;}
	});
	$(document).on('shown.bs.modal','.modal', function() {
		$(this).find('.autofocus').focus();
		lockScroll();
	});
	$(document).on('hide.bs.modal','.modal', function() {
		unlockScroll();
	});
	$(document).on('click', '.next-slide', function() {
		var container = $('.multi-song-container');
		var itemWidth = $('.multi-song-container>div').outerWidth(true); // Get the width of one item (including margin)
		var currentScroll = container.scrollLeft(); // Get current scroll position
    
		container.scrollLeft(currentScroll + itemWidth);
	});
	$(document).on('click', '.previous-slide', function() {
		var container = $('.multi-song-container');
		var itemWidth = $('.multi-song-container>div').outerWidth(true);
		var currentScroll = container.scrollLeft();
		
		container.scrollLeft(currentScroll - itemWidth);
	});
	$(document).on('click thouchstart','.audio-box',function(){
		var songRecording = $(this).data('recording');
		if (songRecording != null){
			s_id = 'spa_' + songRecording
			$(this).replaceWith('<audio id="' + songRecording  + '" class="song-page-audio" controls><source src="/audio/' + songRecording + '" type="audio/mpeg"></audio>');
			$('#' + s_id)[0].play();
		}
	});
	$(document).on('click','.set-list-container',function(e){
		if($(e.target).is('td, td span') && !$(e.target).is(ignoreTdAction)){
			var sortNum = $(e.target).closest('.set-song-row').attr('data-sort') - 1
			var container = $('.multi-song-container')
			var itemWidth = $('.multi-song-container>div').outerWidth(true);

			container.scrollLeft(itemWidth * sortNum);
		}
	});
	$(document).on('click', '.version-container', function(){
		if ($(this).hasClass('new-version')){
			openVersionControlModal($(this).data('target'),true);
		} else {
			var sortNum = $(this).data('target')
			var container = $('.multi-song-container')
			var itemWidth = $('.multi-song-container>div').outerWidth(true);

			container.scrollLeft(itemWidth * sortNum);
		}
	});

	const flyout = $('#songFlyoutPanel');

	$(document).on('click', '.home-page .song-row', function(e) {
		$('.song-row').removeClass('selected')
		$(this).addClass('selected');
		if (!$(e.target).is(ignoreSongClickedSelector)){
			id = $(e.target).closest('.song-metadata').data('target')
			$('#songFlyoutPanel').load(contextpath + 'song/' + id + '?view=modal/dynamic/song-flyout', bindSongContainerScroll);
			const currentState = history.state;
			if (!currentState?.flyoutOpen) {
				history.pushState({ flyoutOpen: true }, '', '#song');
			}
			flyout.addClass('open');
			window.localStorage.setItem('focusedFlyout', id);
		}
	});

	if (window.localStorage.getItem('focusedFlyout')){
		id = window.localStorage.getItem('focusedFlyout');
		$('#songRow_' + id).click();
	}

	let isProgrammaticBack = false;

	$(window).on('popstate', function (event) {
		if (!event.originalEvent.state || !event.originalEvent.state.flyoutOpen) {
		  $('.song-row').removeClass('selected')
			if (!isProgrammaticBack) {
				flyout.removeClass('trans-right')
				setTimeout(function() {
					flyout.addClass('trans-right')
				},100)
			} else {
				isProgrammaticBack = false
			}
			flyout.removeClass('open');
			window.localStorage.removeItem('focusedFlyout', id);
			window.localStorage.removeItem('focusedFlyoutIdx');
		}
	});

	$(document).on('click', '#songCloseFlyout', function() {
		flyout.removeClass('open');
		isProgrammaticBack = true
		history.back();
		$('.song-row').removeClass('selected')
		window.localStorage.removeItem('focusedFlyout', id);
		window.localStorage.removeItem('focusedFlyoutIdx');
	});

	$(document).on("click", '.home-nav', function(){
		window.localStorage.removeItem("home-focus")
		window.location.href = contextpath;
	})

	bindSongContainerScroll()
	focusSong(FOCUSED_SCROLL_PAGE)
});

function focusSong(sp) {
	$('.multi-song-container').each(function(idx, container){
		var itemWidth = $(container).children('div').outerWidth(true);
		$(this).css('scroll-behavior', 'auto');
		container.scrollLeft = itemWidth * sp;
		$(this).css('scroll-behavior', 'smooth');
	})
}
function getFocusedSetIdx() {
	var container = $('.song-page-container.setlist .multi-song-container')
	var itemWidth = $(container).children('div').outerWidth(true);
	var scrollLeft = $(container).scrollLeft();
	return Math.round(scrollLeft / itemWidth)
}

function getFocusedSongPage(container) {
	var itemWidth = $(container).children('div').outerWidth(true);
    var scrollLeft = $(container).scrollLeft();
    var focusedIndex = Math.round(scrollLeft / itemWidth);
    var focusedChild = $(container).children('div').eq(focusedIndex).find('.song-page');
    return focusedChild;
}

function transposeSong(selection) {
	var key = $(selection).html();
	$('.transpose-key').text(key);

	var container = $(selection).closest('.song-action-container').siblings('.song-page-container').find('.multi-song-container');
	console.log(container)
	var selectedSlide = getFocusedSongPage(container);

	var pageId = $(selectedSlide).data('target');
	$.ajax({
		url: contextpath + 'song/details/' + pageId +  '?key=' + key + '&view=dynamic/single-song-page',
		method: "GET",
		success: function(data) {
			$(selectedSlide).replaceWith(data);
			$(selectedSlide).attr('data-key',key);
			//$('.download-focused-song').attr('onclick','downloadFile(\'song\',' + pageId + ',"' + key + '")');
		}
	});	
}

var lastScrollLeft = 0;
var songInView
function bindSongContainerScroll() {
	$('.multi-song-container').off('scroll').on('scroll', function() {
		const children = Array.from(this.children);
		var width = this.scrollWidth
		var parentWidth = this.offsetWidth;
		var scrollLeft = $(this).scrollLeft();
		var itemWidth = $(this).children('div').outerWidth(true);

		if (scrollLeft > lastScrollLeft && scrollLeft > 0) {
			// scrolling right
			var left = false
			if (scrollLeft > 0) {
				$(this).siblings('.previous-slide').show()
			}
			if ($(this).scrollLeft() > width - 2 * itemWidth || scrollLeft >= width - parentWidth){
				$(this).siblings('.next-slide').hide()
			} else {
				$(this).siblings('.next-slide').show()
			}
		} else if (scrollLeft < lastScrollLeft || scrollLeft <= 0) {
			var left = true
			// scrolling left
			if (scrollLeft < width - parentWidth - 1)
				$(this).siblings('.next-slide').show()
			console.log(scrollLeft)
			console.log(itemWidth)
			if (scrollLeft < itemWidth - 1 || scrollLeft <= 0){
				$(this).siblings('.previous-slide').hide()
			} else {
				$(this).siblings('.previous-slide').show()
			}
		}

		const idx = left? Math.floor(scrollLeft / parentWidth): Math.ceil(scrollLeft / parentWidth)
		const currentSong = children[idx];

		nowInView = $(currentSong).find('.song-page').data('target')

		if (nowInView != songInView) {
			$('.version-container.selected').removeClass('selected');
			$('#versionContainer_' + idx).addClass('selected');
			if (window.location.pathname.includes("song")){
				window.history.replaceState('song', 'Song Catalog', contextpath + 'song/' + nowInView);
			}
			//bleh
			key = $(currentSong).find('.song-page').data('key');
			related = $(currentSong).find('.song-page').data('related');
			updateSongActions(nowInView, key, related);

			songInView = nowInView

			window.localStorage.setItem('focusedFlyoutIdx', idx);
		}

		lastScrollLeft = scrollLeft;
  });
	if (window.localStorage.getItem('focusedFlyoutIdx')){
		focusSong(window.localStorage.getItem('focusedFlyoutIdx'));
	}
}

function updateSongActions(nextSongId, key, related) {
	$('.transpose-key').text(key);
	$('.add-song-to-set-ic').attr('onclick','addSongToSet(' + nextSongId + ')');
	$('.edit-focused-song').attr('onclick','showEditSongModal(' + nextSongId + ')');
	$('.download-focused-song').attr('onclick','downloadFile(\'song\',' + nextSongId + ',"' + key + '")');
	$('.delete-focused-song').attr('onclick','deleteSong(' + nextSongId + ',' +  related + ')');
}

function notImplimented(){
	alertWithFade('danger','This feature hasn\'t been implimented yet :(');
}

function setCookie(cname, cvalue, exdays) {
  const d = new Date();
  d.setTime(d.getTime() + (exdays*24*60*60*1000));
  let expires = "expires="+ d.toUTCString();
  document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
}

function getCookie(cname) {
  let name = cname + "=";
  let decodedCookie = decodeURIComponent(document.cookie);
  let ca = decodedCookie.split(';');
  for(let i = 0; i <ca.length; i++) {
    let c = ca[i];
    while (c.charAt(0) == ' ') {
      c = c.substring(1);
    }
    if (c.indexOf(name) == 0) {
      return c.substring(name.length, c.length);
    }
  }
  return "";
}

function checkCookie(cookieName) {
	return getCookie(cookieName) != "";
}

function lockScroll(){
	$('body, body>.scroll-lock').attr('style','overflow: hidden !important');
}
function unlockScroll(){
	$('body, body>.scroll-lock').attr('style','');
}
function isOnline() { 
    return ( navigator.onLine) 
}
function resetSongPageAudio(recording){
	var audioClone = $('.audio-box-template').clone();
	$(audioClone).removeClass('audio-box-template');
	$(audioClone).addClass('audio-box');
	$('.song-page-audio').replaceWith(audioClone);
	if (typeof recording != 'undefined'){
		$('.audio-box').show();
	} else {
		$('.audio-box').hide();
	}
}

function uploadFile(fd,id){
	$.ajax({
        url: contextpath +  'song/add/recording/' + id,
        type: 'POST',
        data: fd,
        contentType: false,
        processData: false,
        success: function(response){
        	location.reload();
        },
        error: function(){
        	alertWithFade('danger','Unable to upload file');
        }
    });	
}
function copyTextToClipboard(text, displayAlert = true){
	var targetId = "_hiddenCopyText_";
	var target = document.createElement("textarea");
	target.style.position = "absolute";
	target.style.left = "-9999px";
	target.style.top = "0";
	target.id = targetId;
	document.body.appendChild(target);
    target.textContent = text;
    var currentFocus = document.activeElement;
    target.focus();
    target.setSelectionRange(0, target.value.length);
    document.execCommand("copy");
    $(target).remove();
    if (currentFocus && typeof currentFocus.focus === "function") {
        currentFocus.focus();
    }
	if (displayAlert){
		alertWithFade('warning',text + ' copied to clipboard.');
	}
}
function logAction(action) {
	$.ajax({
		url: contextpath +  'log-user-action',
		type: 'POST',
		data: {
			action: action
		}
	});	
}
Date.prototype.toDateInputValue = (function() {
    var local = new Date(this);
    local.setMinutes(this.getMinutes() - this.getTimezoneOffset());
    return local.toJSON().slice(0,10);
});

jQuery(function($) {

  var _oldShow = $.fn.show;

  $.fn.show = function(speed, oldCallback) {
	return $(this).each(function() {
	  var obj         = $(this),
		  newCallback = function() {
			if ($.isFunction(oldCallback)) {
			  oldCallback.apply(obj);
			}
			obj.trigger('afterShow');
		  };

	  // you can trigger a before show if you want
	  obj.trigger('beforeShow');

	  // now use the old function to show the element passing the new callback
	  _oldShow.apply(obj, [speed, newCallback]);
	});
  }
});

(function($){
    $.fn.focusTextToEnd = function(){
        this.focus();
        var $thisVal = this.val();
        this.val('').val($thisVal);
        return this;
    }
}(jQuery));
