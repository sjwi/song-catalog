function initializeArchiveTable(selector){
    $(selector).DataTable({
            language: {
            search:'',
                searchPlaceholder: "Search archive...",
                emptyTable: "No set lists could be found"
            },
            searching:true,
            paging:true,
            "pageLength": 10,
            lengthChange: true,
            info: true,
            "order": [ 1, "desc" ]
    });
}
function initializeFrequencyTable(selector){
	var table = $(selector).DataTable({
          searching:false,
          language: {
            emptyTable: "No songs could be found"
          },
		  paging:true,
		  "pageLength": 10,
		  lengthChange: false,
		  info: false,
          "order": [ 1, "desc" ],
          dom: 'frtiBp',
          buttons: [{
            extend: 'excelHtml5',
            text: '<svg style="padding-bottom:3px" fill="#fff" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 32 32" width="18px" height="18px"><path d="M 18 5 L 18 7 L 23.5625 7 L 11.28125 19.28125 L 12.71875 20.71875 L 25 8.4375 L 25 14 L 27 14 L 27 5 Z M 5 9 L 5 27 L 23 27 L 23 14 L 21 16 L 21 25 L 7 25 L 7 11 L 16 11 L 18 9 Z"/></svg> Excel',
            className: 'btn btn-primary btn-sm float-left mt-1 mr-1 pr-3 pl-2',
            titleAttr: 'Export to Excel',
            title: '',
            filename: orgName + '_Frequently_Played_Songs_' + moment().format('MMDDYYYY'),
            }]
    });
    table.buttons().container()
    .appendTo( $('.container.freq-container', table.table().container() ) );
}