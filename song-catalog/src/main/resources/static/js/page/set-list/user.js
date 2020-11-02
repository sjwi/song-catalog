var currentSetRow;
var dragCtr = 0;
$(document).ready(function(){
	$(document).on('dragenter','.set-list-container .set-song-row',function(e){
		e.preventDefault();
		$('.set-table-hover').css('box-shadow','none !important');
		$(this).addClass('set-row-hover');
		if (currentSetRow != this){
			$(currentSetRow).removeClass('set-row-hover');
		}
		currentSetRow = this;
	});
	$(document).on('dragenter','.set-list-container',function(e){
		e.preventDefault();
		dragCtr++;
		$(this).addClass('hover-background');
		if (!$(e.target).is('div.grip-container,td,span.set-row-title,span.close-dark,select,img,button')){
			$('table.set-table tr:last').addClass('set-table-hover');
			$(currentSetRow).removeClass('set-row-hover');
		} else {
			$('table.set-table tr:last').removeClass('set-table-hover');
		}
	});
	$(document).on('dragleave','.set-list-container',function(e){
		dragCtr--;
		if (dragCtr === 0){
			$(this).removeClass('hover-background');
			$(this).find('table.set-table tr:last').removeClass('set-table-hover');
		}
	});
	
	$(document).on('click','.set-list-container .set-table .close',function(e){
		deleteSongFromSet($(e.target).closest('.set-song-row').data('target'),
				$(e.target).closest('.set-song-row').data('setsongid'),
				$(e.target).closest('.set-song-row').data('setid'),
				$(e.target).closest('.set-song-row').data('key'),
				$(e.target).closest('.set-song-row').data('songname'),
				$(e.target).closest('.set-song-row').data('sort'));
	});

	$(document).on('click','.set-list-container .edit-song-btn',function(e){
		showEditSongModal($(this).closest('.song-metadata').data('target'),$(this).closest('.song-metadata').data('setsongid'));
	});
	$(document).on('click','.set-list-container .delete-set',function(e){
		$('#deleteSetModal').find('.modal-title').html('Delete set <b>' + $(this).closest('.set-list-container').data('setname')  + '</b>');
		$('#deleteSetModal').find('.set-id').val($(this).closest('.set-list-container').data('target'));
		$('#deleteSetModal').find('.modal-message').html('Are you sure you want to permenantly remove <b>' + $(this).closest('.set-list-container').data('setname') + '</b>? This action cannot be undone.');
		$('#deleteSetModal').modal('show');
	});
	$('table.set-table').each(function(){
		initializeSortableSetListTable(this);
	});
});