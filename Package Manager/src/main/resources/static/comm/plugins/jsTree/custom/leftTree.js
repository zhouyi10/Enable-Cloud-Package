$(function(){
//	$("#leftTreeNav").jstree({
//        'core' : {  
//            "multiple" : false,  
//            'data' : treeData,  
//            'dblclick_toggle': false          //禁用tree的双击展开  
//        },  
//        "plugins" : ["search"]  
//	});
	 $('#tree_1').jstree({
         "core" : { 
           'data' : treeData, 
             "themes" : {
                 "responsive": false
             }            
         },
         "types" : {
             "default" : {
                 "icon" : "fa fa-folder icon-state-warning icon-lg"
             },
             "file" : {
                 "icon" : "fa fa-file icon-state-warning icon-lg"
             }
         },
         "plugins": ["types"]
     });

     $('#tree_1').on('select_node.jstree', function(e,data) { 
         var link = $('#' + data.selected).find('a');
         if (link.attr("href") != "#" && link.attr("href") != "javascript:;" && link.attr("href") != "") {
             if (link.attr("target") == "_blank") {
                 link.attr("href").target = "_blank";
             }
             document.location.href = link.attr("href");
             return false;
         }
     });
});