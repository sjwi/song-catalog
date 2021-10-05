function allowDrop(e){
	e.preventDefault();
}
function dragAbEntry(e){
	var row = $(e.target).closest('.ab-metadata');
	e.dataTransfer.setData("abId",$(row).data('target'));
	e.dataTransfer.setDragImage($(row)[0],200,30);
}
function dropAbEntryToGroup(e, groupId){
	dragCtr = 0;
	$('#addressBookGroupContainer_' + groupId).removeClass('hover-background');
	addAbEntryToGroup(e.dataTransfer.getData("abId"),groupId);
}
function deleteAddressBookItem(id,type){
	if (confirm("Are you sure you want to delete this Address Book " + type + "?")){
		$.ajax({
			url: contextpath + 'addressbook/' + type + '/delete/' + id,
			method: "DELETE",
			success: function() {
				location.reload();
			},
			error: function(data){
				alertWithFade('danger','Unable to delete ' + type);
			}
		});	
	}
}
function addAbEntryToGroup(entryId,groupId){
	$.ajax({
		url: contextpath + 'addressbook/add-member-to-group',
		method: "POST",
		data: {entryId: entryId,groupId: groupId},
		success: function() {
			reloadGroupContainer(groupId);
		},
		error: function(data){
			alertWithFade('danger','Unable to add member to group');
		}
	});	
}
function reloadGroupContainer(id){
	$.ajax({
		url: contextpath + 'addressbook/group/' + id,
		method: "GET",
		data: {view: 'dynamic/ab-group-container'},
		beforeSend: function(){
			$("#appendAbGroup_" + id).css("opacity",".5");
		},
		success: function(data) {
			$("#appendAbGroup_" + id).html(data);
		},
		error: function(data){
			alertWithFade('danger','Unable to add member to group');
		},
		complete: function(data){
			$("#appendAbGroup_" + id).css("opacity","1");
		}
	});	
}

function removeEntryFromGroup(entryId, groupId){
	$.ajax({
		url: contextpath + 'addressbook/remove-member-from-group',
		method: "POST",
		data: {entryId:entryId,groupId:groupId},
		success: function() {
			reloadGroupContainer(groupId);
			alertWithFade('warning','Member removed from group. <a href="javascript:void(0)" onclick="addAbEntryToGroup(' + entryId + ',' + groupId + ')"> undo </a>');
		},
		error: function(data){
			alertWithFade('danger','Unable to remove member');
		}
	});	
}