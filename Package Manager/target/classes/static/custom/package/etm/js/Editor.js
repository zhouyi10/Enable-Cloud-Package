var Editor = {
    ueditorId : null, // editor id
    ueditor : null, // editor obj
    curObj : null, // The object that is using the editor, while editing,,,,hover does not count
    curObjOld : null,
    isFocus : false, // whether the editor is editing
    init : function(id, callback) {//initialize the editor
        var _this = this;
        _this.ueditorId = id;
        //initialize the editor
        _this.ueditor = UE.getEditor(_this.ueditorId, {
            autoHeight: false
        });
        _this.ueditor.ready(function(){
            //bind global click function
            $(window).on('click', function(e){
                if (!_this.isFocus) {
                    return;
                }
                if ($(e.target).closest('[class^="edui-"]').length == 0
                    && $(e.target).closest('[edit="ueditor"]').length == 0) {//click the blank space to hide the editor
                    _this.blur();
                }
            });
         /*   $(".ContainerWarp")[0].onscroll=function(){
                _this.blur();
            };*/
            // load completion callback method
            callback && callback();
        });
    },
    addObj : function(obj) {// add using editor object bind event
        var _this = this;
        $(obj).each(function(){
            var $that = $(this);
            $that.attr('edit', 'ueditor');
            $that.parent().on('mouseover',function(){
                //When the mouse moves and switches among child elements，will also trigger a mouseover event
                if ($(this).attr('status') == 'edit' || _this.isFocus) {//here this points to the parent element
                    return;
                }
                //set the mouse has moved into the logo
                $(this).attr('status', 'edit');
                _this.render($that);//render editor
            }).on('mouseleave', function(){
                if (_this.isFocus) {
                    return;
                }
                $(this).removeAttr('status');//here this points to the parent element
                _this.destroy();
            }).click(function(){
                // determine whether to switch edit box
                if (_this.curObj!=null && $(this).children().is(_this.curObjOld)) {
                    _this.curObjOld = null;
                    return;
                }
                //visible edit box means editing
                if ($('.edui-editor > .edui-editor-iframeholder').is(":visible")) {// save data
                    _this.curObj.parent().removeAttr("status");
                    _this.curObj.html(_this.ueditor.getContent());
                    _this.destroy();
                    $(this).attr('status', 'edit');
                }
                //click to set the current operating element
                _this.curObj = $($(this).children(0));
                _this.render(_this.curObj);
                $('.edui-editor > .edui-editor-iframeholder').show(0, function(){//show edit box
                    _this.ueditor.setContent($that.html());
                    _this.ueditor.focus(true);
                    _this.isFocus = true;
                });
                _this.curObjOld = $(this).children();
            });
        });
    },
    removeObj : function(obj){ //remove the editor object and unbind the event
        $(obj).removeAttr('edit'); //use editor logo
        // The event is bound to the parent element of the object
        $(obj).parent().removeAttr('status').unbind('mouseover').unbind('mouseout').unbind('click');
    },
    render : function(obj) { // render editor
        // Set the editor width, hide the edit box, and display it when clicked
        // only enable custom buttons on the stem
        window.QuestionDragInfo.isblur = false;
        if ($(obj).attr("class") !== "question_stem") {
            window.QuestionDragInfo.disableBtn();
        } else if(window.BaseInfo.condition.type.code == "39") {
            window.QuestionDragInfo.enableBtn();
            window.QuestionDragInfo.isblur = true;
        }
        var height = $(obj).parent().innerHeight(), width = $(obj).parent().innerWidth(), offset = $(obj).parent().offset();
        $('.edui-editor, .edui-editor > div').css('width', width);
        $('.edui-editor > .edui-editor-iframeholder').hide();//hide editor editing area
        //The editor flashes when the mouse is placed on the border of the current element, the reason is that the edit box toolbar is blocked by the current element, so it moves up 2px
        this.ueditor.setHeight(height + 2);// set the editor editing area height
        // set editor location
        offset['top'] -= $(".edui-editor > .edui-editor-toolbarbox").height() + 2;
        $('#' + this.ueditorId).parent().css(offset);
    },
    destroy : function() { // Hide the editor (absolutely positioned to an invisible position)
        $('#' + this.ueditorId).parent().css('top', '-2000px');
        $('.edui-default .edui-editor-toolbarbox').removeAttr('style');
        this.curObj = null;
    },
    blur : function() {
        var _this = this;
        if (!_this.isFocus) {
            return;
        }
        _this.isFocus = false;
        if (_this.curObj != null && _this.curObj.length > 0) {// save_data
            _this.curObj.parent().removeAttr("status");
            _this.curObj.html(_this.ueditor.getContent());
            _this.destroy();
        }
    }
};