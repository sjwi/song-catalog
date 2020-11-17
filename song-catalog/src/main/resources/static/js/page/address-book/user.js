var maxResized = false;
var minResized = false;
var dragCtr = 0;
$(document).ready(function(){
	//group container
	$(document).on('dragenter','.ab-group-container',function(e){
		e.preventDefault();
		dragCtr++;
		$(this).addClass('hover-background');
	});
	$(document).on('dragleave','.ab-group-container',function(e){
		dragCtr--;
		if (dragCtr === 0){
			$(this).removeClass('hover-background');
		}
	});
	$(document).on('click','.ab-group-container .remove-entry',function(){
		removeEntryFromGroup($(this).closest('.entry-metadata').data('target'), $(this).closest('.set-list-container').data('target'));
	});
	$(document).on('click','.ab-group-container .edit-contact-btn',function(){
		editEntry($(this).closest('.entry-metadata').data('target'));
	});
	$(document).on('click','.ab-group-container .delete-group-btn',function(){
		deleteAddressBookItem($(this).closest('.set-list-container').data('target'),'group');
	})
	//entries
	$(document).on('click','.delete-abEntry-btn',function(){
		deleteAddressBookItem($(this).closest('.ab-metadata').data('target'),'entry');
	});
	$(document).on('click','.add-abEntry-to-group',function(){
		addAbEntryToGroupFromModal($(this).closest('.ab-metadata').data('target'));
	});
	$(document).on('click','.edit-abEntry-btn, #addressBookBody .song-row',function(e){
		if (!$(e.target).is("a.ignore-prop")){
			editEntry($(this).closest('.ab-metadata').data('target'));
		}
	});
	//tab
	$('.ab-tab-selector').on('click', function(){
		if (!$(this).hasClass('focused')){
			$('.ab-tab-selector').removeClass('focused');
			$(this).addClass('focused');
			if ($(this).hasClass("ab-entries-tab")){
				$('#addressBookEntriesContainer').show();
				$('#addressBookGroupContainer').hide();
			} else {
				$('#addressBookEntriesContainer').hide();
				if (!$('.min-container').data('groups-loaded')){
					$('#addressBookGroupContainer').appendTo('.min-container');
					$('.min-container').data('groups-loaded',true);
				}
				$('#addressBookGroupContainer').show();
			}
		}
	});
	//resize logic
	$(window).resize(function() {
		var width = $(window).width();
		if (width > 991 && !maxResized) {
			$('#addressBookEntriesContainer').show();
			$('#addressBookGroupContainer').show();
			$('#addressBookGroupContainer').appendTo($('.address-book.setlist-section').find('.container'));
			$('.min-container').data('groups-loaded',false);
			maxResized = true;
			minResized = false;
		} if (width < 992 && !minResized) {
			maxResized = false;
			$('#addressBookEntriesContainer').show();
			$('.min-container').find('#addressBookGroupContainer').hide();
			$('.ab-tab-selector').removeClass('focused');
			$('.ab-entries-tab').addClass('focused');
			minResized = true;
		}
	});
});