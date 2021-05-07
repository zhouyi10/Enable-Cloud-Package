/**
 * 
 */
(function($){
	var streeSelect = {
		//inputWarpClass外层类名  ，streeWarpId放置tree的层，Datas数据(此处为url，取消data里面的注释部分即可使用)
		//treePlugins单选多选的展现方式      selectType为选择方式（单选false或者多选true）
		// selectData已选数据数组
		init : function(inputWarpClass,streeWarpId,Datas,selectType, selectData){
			this.streeInitCheckbox(inputWarpClass,streeWarpId,Datas,selectType, selectData);
			this.inputClick(inputWarpClass);
		},
		inputClick:function(inputWarpClass){
			$(inputWarpClass).find('div.out1').on("click",function(e){
				e.stopPropagation();
				$(inputWarpClass).find('.stree_warp').toggle();
			}).on("focus", function(){
				this.blur();
			});
			
		},
		streeInitCheckbox:function(inputWarpClass,streeWarpId,Datas,selectType, selectData){
			var plugins=[];
			if (selectType) {
				plugins=["wholerow","checkbox","types","changed"];
			} else{
				plugins=["wholerow","types","changed"];
			}
			$(streeWarpId)
			.jstree({
	            'plugins': plugins,
			    'checkbox': {
				     "three_state": selectType //父子级不关联选中
				 },
	            'core': {
	            	"multiple": selectType,//单选
	                "themes" : {
	                    "responsive": false
	                },
	                "check_callback" : true,
	                'data': Datas
	            },
	            "types" :{
	                "default" : {
	                    "icon" : "fa fa-folder icon-state-warning icon-lg"
	                },
	                "file" : {
	                    "icon" : "fa fa-file icon-state-warning icon-lg"
	                }
	            } 
	        }).bind("load_node.jstree", function(e, data) {
	        	if (selectData != null) {
	        		// 设置已选数据
	        		var ref = $(streeWarpId).jstree(true);
	        		for (var i = 0; i < selectData.length; i++) {
	        			var node = ref.get_node(selectData[i]);
	        			if (node) {
	        				ref.check_node(node);
	        			}
	        		}
	        		
	        	}
            }).on("changed.jstree", function (e, data) {
            	if (data.action = "select_node" && data.node != null) {
            		jsTreeChecked(data.node, streeWarpId);
            	}
        		
            });
	        //取消按钮关闭下拉树
	        $(".streeControl_box .streeCancel").on('click',function(){
        		$(inputWarpClass).find('.stree_warp').hide();
        	})
        	 //点击下拉树以外部分关闭下拉树
	        $(inputWarpClass).siblings().on('click',function(event){
	        	$(inputWarpClass).find('.stree_warp').hide();
	        });
	        $(inputWarpClass).parents().siblings().on('click',function(event){
	        	$(inputWarpClass).find('.stree_warp').hide();
	        	event.stopPropagation();
	        });
		}
	};

	function initDropTree(obj, treeData, selectData){
		if ($(obj).next().length == 0 || !$(obj).next().hasClass(".stree_warp")) {
			var treeId = $(obj).attr("id");
			if (treeId == undefined || treeId == "") {
				treeId = "tree" + Math.random() * 10000;
			} else {
				treeId = treeId + "tree";
			}
			$(obj).after('<div class="stree_warp select_tree">' +
					'<div class="stree_box" id="' + treeId + '">' + 
					'</div></div>');
		}
		streeSelect.init($(obj).parent(), '#' + treeId ,treeData, true, selectData);
		$(obj).next().hide();
	}
	
	$.fn.treeSelect = function(){
		initDropTree(this, arguments[0], arguments[1])
	};
	
})(jQuery);

function jsTreeChecked(currentNode, streeWarpId) {
	 // 获取当前节点
   var nodeId = currentNode.id;
   var text = currentNode.text;
   var checked = currentNode.state.selected;
   if (checked) {
   	$("#in1").append(addcontact(currentNode));
   } else {
   	$("#in1").find("#div_"+nodeId).remove();
   }
   var parentId = currentNode.parent;
   var ref = $(streeWarpId).jstree(true);
   //表示是根节点
   if (parentId == "#") {
   	var child = currentNode.children;
   	for (var i = 0; i < child.length; i ++) {
   		var id = child[i];
			$("#in1").find("#div_"+id).remove();
   	}
   } else {
   	//子节点
   	var node = ref.get_node(parentId);
   	var child = node.children;
   	var flag = true;
   	for (var i = 0; i < child.length; i ++) {
           var state = ref.get_node(child[i]).state.selected;
           if (!state) {
           	flag = false;
           }
   	}
   	//全部子节点被勾选，则移除单个子节点，加入整个父节点
   	if (flag) {
   		for (var i = 0; i < child.length; i ++) {
   			var id = child[i];
   			$("#in1").find("#div_"+id).remove();
   		}
   		$("#in1").append(addcontact(node));
   	} else {
   		var $parent = $("#in1").find("#div_"+parentId);
   		if($parent.length>0) {
				$parent.remove();
				for (var i = 0; i < child.length; i ++) {
					var childNode = ref.get_node(child[i]);
                   var state = childNode.state.selected;
                   if (state) {
                   	$("#in1").append(addcontact(childNode));
                   }
           	}
   		}
   	}
   }
}

function addcontact(node) {
	var div = "<div id='div_"+node.id+"' style='width:auto;float:left;'>"+
			"<a onclick=javascript:deleteJSTree('"+node.id+"');>"+node.text+"</a>;</div>";
	return div;
}

function deleteJSTree(id) {
	var ref = $('#classestree').jstree(true);
	$("#in1").find("#div_"+id).remove();
	ref.uncheck_node(ref.get_node(id));
}

function getSelectAll(id) {
	var ref = $("#"+id).jstree(true);
	var nodes = $("#"+id).jstree("get_checked");
	var toArray = new Array();
	for(var i=0;i<nodes.length;i++){
		var nodeInfo = ref.get_node(nodes[i]);
		if(ref.is_leaf(nodeInfo)){
			toArray.push(nodeInfo.id);
		}
	}
	return toArray;
}


