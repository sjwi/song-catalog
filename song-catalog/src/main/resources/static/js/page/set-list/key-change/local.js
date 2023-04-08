$(document).on('change','.set-list-container select[name="defaultKey"]',function(e){
	e.preventDefault()
	selectedLabel = $('option:selected', this).text()
	let container = $(this).closest('.set-list-container')
	let setId = $(container).data('target')
	let setSongId = $(this).closest('.set-song-row').data('setsongid')
	updateSetListState(setId, setSongId, $(this).val(), selectedLabel.endsWith("*"))
})