$(document).ready(function() {
	$(document).on('click','.delete-btn',function(e){
		$('#deleteSongModal').find('.modal-title').html("Delete " + $(this).closest('.song-metadata').data('name'));
		$('#deleteSongModal').find('.modal-message').html("Are you sure you want to permanently remove <b>"+ $(this).closest('.song-metadata').data('name') +"</b> from the song catalog?\nThis will also remove it from all setlists and delete any versions.")
		$('#deleteSongModal').find('.song-id').val($(this).closest('.song-metadata').data('target'));
		$('#deleteSongModal').find('.song-name').val($(this).closest('.song-metadata').data('name'));
		$('#deleteSongModal').modal('show');
	});
	
	$(document).on('click','.add-to-set-btn',function(e){
		if ($(this).closest('.song-metadata').data('related') == "0"){
			addSongToSet($(this).closest('.song-metadata').data('target'));
		} else {
			addSongToSet($(this).closest('.song-metadata').data('related'));
		}
	});
	$(document).on('click','.edit-song-btn',function(e){
		showEditSongModal($(this).closest('.song-metadata').data('target'));
	});
	$(document).on('click','.version-control-btn',function(e){
		openVersionControlModal($(this).closest('.song-metadata').data('target'));
	});
});