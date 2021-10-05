function initializeSortableSetListTable(element){
	if ($(element).hasClass('ui-sortable')){
		$(element).sortable('destroy');
	}
	$(element).sortable({
		items: 'tr',
		handle: '.set-grip',
		cursor: 'grabbing',
		axis: 'y',
		delay: 50,
		update: function() {
			var sortedSongs = [];
			var setId = $(this).data('setid');
			var songContainer = '.song-page-container[id="' + setId +'"]';
			var counter = 1;
			$(this).find('tr').each(function(){
				sortedSongs.push($(this).data('setsongid'));
				$(this).attr("data-sort",counter);
				counter++;
			});
			$.ajax({
				type : "POST",
				url : contextpath + 'setlist/sort/' + setId,
				data : {
					sortedSongs: sortedSongs
				},
				beforeSend: function(){
					if($(songContainer).length){
						$(songContainer).html($('.loading').clone());
					}
				},
				success : function(data) {
					if($(songContainer).length){
						$(songContainer).html(data);
						focusedSlickPage = 0;
						slick();
					}
					lastUpdatedTime = undefined;
				},
				error : function(e) {
					alertWithFade('danger','Unable to sort set list.');
				}
			}); 
		}
	});
	$(element).find('.set-song-row td').each(function(){
		$(this).css('width', $(this)[0].clientWidth +'px');
	});
}

function quickAddSongToSet(songId, setId, key, sort){
	if (!sort){
		sort = 0;
	}
	$.ajax({
		url: contextpath + 'setlist/add-song',
		method: "POST",
		data: {
			song : songId,
			setListKey : key,
			setList : setId,
			sort : sort
		},
		beforeSend: function(){
			$('#appendSetList_' + setId).find('.set-list-container').children().addClass('loading');
		},
		success: function(data) {
			var setContainer = '#appendSetList_' + setId;
			$(setContainer).html(data);
			initializeSortableSetListTable(setContainer + ' .set-table');
			reloadSetSongContainerIfPresent(setId);
		},
		error: function(data){
			alertWithFade('danger','Something went wrong! Unable to add song to set')
		},
		complete: function(data){
			$('#appendSetList_' + setId).find('.set-list-container').children().removeClass('loading');
		}
	});	
}
	
function deleteSongFromSet(songId, setSongId, setId, key, songName, sort){
	$.ajax({
		url: contextpath + 'setlist/remove-song',
		method: "POST",
		data: {
			song : setSongId,
			setList : setId
		},
		beforeSend: function(){
			$('#appendSetList_' + setId).find('.set-list-container').children().addClass('loading');
		},
		success: function(data) {
			$('#appendSetList_' + setId).html(data);
			alertWithFade('warning',songName + ' removed from set. <a href="#" data-dismiss="alert" onclick="quickAddSongToSet(' + songId +','+ setId+ ',\'' + key + '\',' + sort + ')"> Undo</a>');
			initializeSortableSetListTable('#appendSetList_' + setId + ' .set-table');
			reloadSetSongContainerIfPresent(setId);
		},
		error: function(data){
			alertWithFade('danger','Something went wrong! Unable to remove song from set')
		},
		complete: function(data){
			$('#appendSetList_' + setId).find('.set-list-container').children().removeClass('loading');
		}
	});	
}

function changeKeyForSetSong(key,songId,setId){
	$.ajax({
		url: contextpath + 'setlist/change-key',
		method: "POST",
		data: {
			songId : songId,
			defaultKey : key
		},
		success: function(data) {
			reloadSetSongContainerIfPresent(setId);
		},
		error: function(data){
			alertWithFade('danger','Something went wrong! Unable to update key for song.')
		}
	});	
}
