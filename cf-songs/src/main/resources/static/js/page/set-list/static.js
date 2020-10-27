function reloadSetSongContainerIfPresent(setId){
	var songContainer = '.song-page-container[id="' + setId +'"]';
	if($(songContainer).length){
		$.ajax({
			url: contextpath + 'setlist/details/' + setId + '?view=dynamic/song-container',
			method: "GET",
			beforeSend: function(data){
				$(songContainer).html($('.loading'));
			},
			success: function(data) {
				$(songContainer).html(data);
				slick();
			}
		});
	}	
}

function reloadSetContainerIfPresent(setId){
	var setContainer = '#appendSetList_' + setId;
	if($(setContainer).length){
		$.ajax({
			url: contextpath + 'setlist/details/' + setId + '?view=dynamic/set-list-container',
			method: "GET",
			beforeSend: function(data){
				$(setContainer).html($('.loading'));
			},
			success: function(data) {
				$(setContainer).html(data);
				if (loggedInUser != 'anonymousUser'){
					initializeSortableSetListTable(setContainer + ' .set-table');
				}
			}
		});
	}	
}


function localSetKeyChange(setListContainer){
	var setId = $(setListContainer).data('target');
	var songContainer = '.song-page-container[id="' + setId +'"]';
	var songKeys = [] ;
	$(setListContainer).find('.set-song-row').each(function(){
		songKeys.push($(this).find('select[name="defaultKey"]').val());
	});
	$.ajax({
		url: contextpath + 'setlist/details/' + setId + '?view=dynamic/song-container',
		method: "GET",
		data : {keys: songKeys},
		beforeSend: function(jqxhr,settings){
			window.history.replaceState('transposed', 'transposeSetList', '/setlist/' + setId + '?' + settings.url.split('song-container&')[1]);
			$(songContainer).html($('.loading'));
		},
		success: function(data, textStatus, jqXHR) {
			$(songContainer).html(data);
			slick();
		}
	});
}