$(document).on('change','.set-list-container select[name="defaultKey"]',function(e){
	localSetKeyChange($(this).closest('.set-list-container'));
})