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
				lastUpdatedTime = undefined;
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

function pollForSetListUpdate(setListId) {
	if (isOnline() && !document.hidden){
		$.ajax({
			url: contextpath + "setlist/getSetListObject/" + setListId,
			method: "GET",
			success: function(setList){
				var newUpdatedTime = setList.lastModifiedOn;
				if (lastUpdatedTime == undefined){
					lastUpdatedTime = newUpdatedTime;
				} else {
					if (lastUpdatedTime != newUpdatedTime) {
						reloadSetSongContainerIfPresent(setListId);
						reloadSetContainerIfPresent(setListId);
						lastUpdatedTime = newUpdatedTime;
					}
				}
			}
		});
	}
}


function localSetKeyChange(setId, setSongId, key, capo) {
	var songContainer = '.song-page-container[id="' + setId +'"]';
	if (!capo || capo == "" || capo == "No Capo")
		capo = null
	let data = {}
	data[setSongId] = {
		key: key,
		capo: capo
	}
	let url = contextpath + 'setlist/state/' + setId
	$(songContainer).html($('.loading'));
	$.post({
		url: url,
		contentType: "application/json; charset=utf-8",
		method: "POST",
		data : JSON.stringify(data),
		beforeSend: function(jqxhr,settings){
		},
		success: function(data, textStatus, jqXHR) {
			reloadSetContainerIfPresent(setId)
			reloadSetSongContainerIfPresent(setId)
		}
	});
}
