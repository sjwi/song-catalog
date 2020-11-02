$(document).on('change','.set-list-container select[name="defaultKey"]',function(e){
	changeKeyForSetSong($(this).val(),$(this).closest('.set-song-row').data('setsongid'),$(this).closest('.set-song-row').data('setid'));
});