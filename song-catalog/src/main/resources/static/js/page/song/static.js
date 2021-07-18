if (!focusedSlickPage){
	var focusedSlickPage = 0;
}
function slick(){
	$('.slick-container').off();
	$('.slick-container').slick({
		arrows: true,
		useTransform: false,
		initialSlide: focusedSlickPage,
		useCSS: false,
		speed: 200,
		prevArrow: $('.previous-slide'),
		nextArrow: $('.next-slide')
	});

	$(".slick-container").on("beforeChange", function (e, slick, currentSlide, nextSlide){
		focusedSlickPage = nextSlide;
	});
}