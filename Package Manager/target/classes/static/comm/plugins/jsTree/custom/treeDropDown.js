//			var DropDownTree = window.DropDownTree = {
//				obj : null,
//				treeDiv : null,
//				init : function() {
//					if (this.treeDiv == null) {
//						var thisId = $(this.obj).attr("id");
//						var id = "tree" + (thisId == undefined ? (Math.random() * 10000) : thisId);
//						var width = $(this.obj).parent().width();
//						$(this.obj).parent().append("<div id='" + id + "' style='position:fixed;background-color: white;z-index:2;width:" + width + "px;' ></div>");
//						this.treeDiv = $(this.obj).parent().find("div#" + id);
//
//						var _this =this;
//						this.treeDiv.on("changed.jstree", function (e, data) {
//							if(data.selected.length) {
//								$(_this.obj).parent().find("input").val(data.instance.get_node(data.selected[0]).text);
//// 								alert('The selected node is: ' + data.instance.get_node(data.selected[0]).text);
//							}
//						}).jstree({
//				            'core' : {  
//				                "multiple" : false,  
//				                'data' : treeData,  
//				                'dblclick_toggle': false          //禁用tree的双击展开  
//				            },  
//				            "plugins" : ["search"]  
//						});
//						this.treeDiv.hide();
//					}
//				},
//				toggle : function(obj) {
//					this.obj = $(obj);
//					this.init();
//					if ($(this.treeDiv).is(":visible")) {
//						$(this.treeDiv).hide();
//					} else {
//						$(this.treeDiv).show();
//					}
//				}
//			}

/**
 * 
 */
(function($){
	
	var streeSelect = {
		//inputWarpClass外层类名  ，streeWarpId放置tree的层，Datas数据(此处为url，取消data里面的注释部分即可使用)
		//treePlugins单选多选的展现方式      selectType为选择方式（单选false或者多选true）
		init : function(inputWarpClass,streeWarpId,Datas,selectType, changeCallBack){
			this.streeInitCheckbox(inputWarpClass,streeWarpId,Datas,selectType, changeCallBack);
			this.inputClick(inputWarpClass);
			
		},
		inputClick:function(inputWarpClass){
			$(inputWarpClass).find('input').on("click",function(e){
				e.stopPropagation();
				//console.log(Datas)
				$(inputWarpClass).find('.stree_warp').toggle();
			}).on("focus", function(){
				this.blur();
			});
			
		},
		streeInitCheckbox:function(inputWarpClass,streeWarpId,Datas,selectType, changeCallBack){
			var plugins=[];
			if (selectType) {
				plugins=["wholerow","checkbox","types","changed"];
			} else{
				plugins=["wholerow","types","changed"];
			}
		    //console.log()
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
               var checkDiv = $(streeWarpId).html();
               var array = checkDiv.split(",");
               var nodeIds = data.node.children;
             
               for(var i=0;i<nodeIds.length;i++){
                   for(var j=0;j<array.length;j++){ 
                       if(array[j] == nodeIds[i]){
                           $(this).andSelf().removeClass("jstree-unchecked jstree-undetermined").addClass("jstree-clicked");
                       }
                   } 
               }
            }).on("changed.jstree",function(e,data){
	        	e.stopPropagation();
	        	if (selectType) {
	        		var selectValues=[];
	        		var selectedId=$(this).jstree().get_checked(true);
	        	    //console.log(selectedId.length);
	        	    if (selectedId.length>0) {
		        		for (var i=0;i<selectedId.length;i++) {
			        		var selectNode = data.instance.get_node(data.selected[i]);
			        		if ( selectNode.children.length == 0){
			        			//console.log(selectNode.text);
			        			selectValues.push(selectNode.text);
			        		}	
			        	}
		        		console.log(selectValues.length)
			        	//如果改变的选中值
			        	$(this).siblings('.streeControl_box').find('.streeSure').on('click',function(){
			        		if (selectValues.length>0) {
			        			$(inputWarpClass).find('input').val(selectValues);
				        		selectedId=0;
				        		selectValues=[];
				        		$(inputWarpClass).find('.stree_warp').hide();
			        		}
			        	});
		        	}	
	        	} else{
	        		var selectValues=[];
	        		var ClickId=$(this).jstree().get_bottom_selected(true);
	        	    //console.log(ClickId.length);
	        	    if (ClickId.length>0) {
		        		for (var i=0;i<ClickId.length;i++) {
			        		var ClickNode = data.instance.get_node(data.selected[i]);
			        		if ( ClickNode.children.length == 0){
			        			//console.log(ClickNode.text);
			        			selectValues.push(ClickNode.text);
			        		}	
			        	}
		        		//console.log(selectValues)
			        	//如果改变的选中值
			        	$(inputWarpClass).find('input').val(selectValues);
	        			changeCallBack && changeCallBack(data.instance.get_node(data.selected[0]));
		        		selectedId=0;
		        		selectValues=[];
		        		$(inputWarpClass).find('.stree_warp').hide();
		        	}	
	        	}
	        	
//		        	if (selectedId.length>0) {
//		        		for (var i=0;i<selectedId.length;i++) {
//			        		var selectNode = data.instance.get_node(data.selected[i]);
//			        		if ( selectNode.children.length == 0){
//			        			//console.log(selectNode.text);
//			        			selectValues.push(selectNode.text);
//			        		}	
//			        	}
//		        		console.log(selectValues.length)
//			        	//如果改变的选中值
//		        		//console.log(data.instance.get_node(data.selected[0]).text)
//		        		if (selectType) {
//		        			$(this).siblings('.streeControl_box').find('.streeSure').on('click',function(){
//				        		if (selectValues.length>0) {
//				        			$(this).parents(inputWarpClass).find('input').val(selectValues);
//					        		selectedId=0;
//					        		selectValues=[];
//					        		$(inputWarpClass).find('.stree_warp').hide();
//				        		}
//				        	});
//		        		} else if (!selectType) {
//		        			$(this).parents(inputWarpClass).find('input').val(selectValues);
//			        		selectedId=0;
//			        		selectValues=[];
//			        		$(inputWarpClass).find('.stree_warp').hide();
//		        		}
//			        	
//		        	}
	        	
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

	function initDropTree(treeData, selectType, changeCallBack){
		selectType = selectType == 'single' ? false : true;
		if ($(this).next().length == 0 || !$(this).next().hasClass(".stree_warp")) {
			var treeId = $(this).attr("id");
			if (treeId == undefined || treeId == "") {
				treeId = "tree" + Math.random() * 10000;
			} else {
				treeId = treeId + "tree";
			}
			var html = '';
			if(selectType == true) {
				html = '<div class="stree_warp select_tree">' +
				'<div class="stree_box" id="' + treeId + '">' + 
				'</div><div class="streeControl_box"><a class="streeSure">确定</a><a class="streeCancel">取消</a></div></div>';
			}else if(selectType == false) {
				html = '<div class="stree_warp select_tree">' +
				'<div class="stree_box" id="' + treeId + '">' + 
				'</div></div>';
			}
			$(this).after(html);
		}
		$(this).next().hide();
		
		streeSelect.init($(this).parent(), '#' + treeId ,treeData, selectType, changeCallBack);
	}
	
	$.fn.treeSelect = function(){
		initDropTree.apply(this, arguments)
//		var method = arguments[0];
//		if(methods[method]){
//			method = methods[method];
//			arguments = Array.protoType.slice.call(arguments,1);
//		}else if( typeof(method) == 'object' || !method ){
//			method = methods.init;
//		}else{
//			$.error( 'Method ' +  method + ' does not exist on jQuery.collectStar' );
//            return this;
//		}
//		return method.apply(this,arguments);
	};
	
})(jQuery);