/**
 * 绘图工具
 * 说明 ：  
 *      CanvasUtils  为起始调用位置
 *      每一个工具声明单独的对象来处理对应的事件，并绑定到CanvasUtils上
 *      提供绘画专门接口支持绘图
 */
(function($){
    var globalSetting = {
        aside : false, //是否有折叠显示按钮
        uploadUrl : '', // 上传图片地址
        toolLoc : 'bottom',//工具栏位置
        tools : ['pencil', 'line', 'shape', 'input', 'eraser', 'clear', 'splitor', 'color', 'weight', 'alpha', 'photo'/*, 'camera'*/],
        zIndex : 100, //浮动层
        multiple : true, //是否支持多图，
        fileNamePrefix : '', //文件名称前缀
        plugins : [], //额外插件，如批阅的批改评判等
        imageData : [], //绘图信息
        color : null,
        alpha : 0.7,
        weight : 1,
        complete : function(result, settings){}, //点击完成回调事件    绘图结果信息， 设置信息
        clone: function() {
            var result = {};
            $.each(this, function(key, value) {
                if (typeof value != 'function') {
                    result[key] = value;
                }
            });
            return result;
        }
    }
    var CanvasUtils = window.CanvasUtils = {
        i18n : {
        },
        options : {}, //选项信息
        baseImageObj : {url : null, backgroundUrl : null, 'fileName' : '', 'fileId' : '', 'img' : null, 'backgroundImg' : null, 'isModify': false}, //基础图片对象属性
        container : '.canvas_main',
        toolsSelector : '.canvas_main .canvas_body .canvas_op .tools',//工具栏css选择器
        canvasAreaSelector : '.canvas_main .canvas_body .canvas_area',//绘图区域css选择器,子元素为canvas
        global: function(opt) {
            if (opt != undefined && opt != undefined && typeof opt == 'object' && !(opt instanceof Array)) {
                $.extend(globalSetting, opt);
            }
        },
        init : function(data, globalOp) {
            if (window.i18n != undefined && window.i18n != null) {
                $.extend(this.i18n, window.i18n);
            }
            this.options = globalSetting;
            if (globalOp != undefined && typeof globalOp == 'object') {
                $.extend(this.options, globalOp);
            }
            // 元素不可选中，不可复制
            $(document.body).addClass('text_no_select').attr('unselectable', 'on');
            this.initCanvasHtml(); //初始化绘图主体html
            this.initTools(); //初始化绘图工具栏
            this.initImage(data); //转换绘图信息数据
            this.initEvent(); //初始化事件
            this.initPlugins();
            DrawUtil.init(this.options.imageData[0]);
        },
        initCanvasHtml : function() { //初始化绘图主体html
            if ($('.canvas_main').length == 0) {
                //主体html
                $(document.body).append('<div class="canvas_curtain"></div><div id="canvastools" class="canvas_main"></div>');
                // 左侧展开/折叠按钮
                $('.canvas_main').append('<div class="canvas_left"><div class="tBrush" id="tBrush"></div></div>');
                // 主题绘画区域
                $('.canvas_main').append('<div class="canvas_body"><div class="canvas_area"><canvas></canvas></div><div class="canvas_op"><div class="canvas_op_wrapper"><ul class="tools"></ul><div class="op_btn"><button id="canvas_cancle">' + this.i18n['cancle'] + '</button><button id="canvas_confirm">' + this.i18n['confirm'] + '</button></div></div></div></div>');
                // 右侧多绘图信息
                $('.canvas_main').append('<ul class="canvas_right"><li class="add"></li></ul>');
                if (this.options.toolLoc == 'top') {
                	$('.canvas_main').addClass('tool_top');//工具栏在上方
                	$(CanvasUtils.canvasAreaSelector).before($(CanvasUtils.canvasAreaSelector).next());
                }
            }
            if (this.options.aside) { //显示靠边按钮
                $('.canvas_main .canvas_left').show();
                $('.canvas_main #canvas_cancle').hide();
            } else {
                $('.canvas_main .canvas_left').hide();
                $('.canvas_main #canvas_cancle').show();
            }
            if (this.options.multiple) { //是否为多图模式
                $('.canvas_main').removeClass('single');
                $('.canvas_main .canvas_right').show();
            } else {
            	$('.canvas_main').addClass('single'); //单图模式
                $('.canvas_main .canvas_right').hide();
            }
            if (typeof this.options.alpha == 'number') {
            	$(CanvasUtils.canvasAreaSelector).css('background', 'rgba(255, 255, 255, ' + this.options.alpha + ')');
            }
            $('.canvas_curtain, .canvas_main').css('z-index', this.options.zIndex);
            $('.canvas_curtain, .canvas_main').show();
        },
        initTools : function() { //初始化绘图工具栏
            var _this = this;
            $(CanvasUtils.toolsSelector).empty();
            $.each(_this.options.tools, function(i, tool){
                CanvasUtils[tool].init();
            });
            $(CanvasUtils.toolsSelector + ' > li:first-child').addClass('on');
        },
        initEvent : function() { //初始化事件
            var _this = this;
            $('.canvas_main .canvas_left .tBrush').unbind('click').on('click', function() {// 左侧展开/折叠按钮点击事件
                $('.canvas_main').toggleClass('off');
                if ($('.canvas_main').hasClass('off')) {
                    $(document.body).removeClass('text_no_select').removeAttr('unselectable');
                    this.options.imageData = []; //清空所有图片
                    $('.canvas_main .canvas_right :not(.add)').remove();
                }
            });
            if (_this.options.multiple) {//多图
                $('.canvas_main .canvas_right li.add').unbind('click').on('click', function(){// 新增canvas
                    var $last = _this.addImage();
                    _this.options.imageData.push($last.data('imgObj'));
                    $last.click();
                });
            }
            $('.canvas_main #canvas_cancle').unbind('click').on('click', function(){ //取消按钮
                _this.cancle();//关闭窗口
            });
            $(window).unbind('resize').on('resize', function() {
                DrawUtil.clearCanvas(); // 清除绘图信息
            });
            $('.canvas_main #canvas_confirm').unbind('click').on('click', function(){ // 确定按钮
            	if ($(this).data('clickable') == 'false') {
            		return false;
            	}
            	$(this).data('clickable', 'false');
                DrawUtil.saveImage(function(){
                	_this.confirm();
                });
            });
        },
        confirm : function(){
            var _this = this, result = [];
            ProcessImageFlow.process(function(result){//图片保存循环处理，， ajax不能完全同步，拿不到结果
                _this.cancle();//关闭窗口
                _this.options.complete(result, _this.options);
            });
        },
        cancle : function() { //关闭绘画窗口
        	var _this = this;
        	if (_this.options.aside) {
                $('.canvas_main .canvas_left .tBrush').click();
            } else {
                $('.canvas_curtain, .canvas_main').hide();
            }
            // 底部内容可选中可复制
            $(document.body).removeClass('text_no_select').removeAttr('unselectable');
            _this.destroyPlugins(); //插件清除
            _this.options.imageData = []; //清空所有图片
            DrawUtil.clearCanvas(); //清空画布
            $('.canvas_main .canvas_right :not(.add)').remove(); //元素删除
            $('.canvas_main #canvas_confirm').removeData();
        },
        initImage : function(data) { //初始化绘图信息
            var _this = this;
            if (data == null || data == undefined) { //未传入图片，初始化空画布
                _this.options.imageData.push($.extend({}, _this.baseImageObj));
            } else if (typeof data == 'object') {
                if (!(data instanceof Array)) { //  传入单张图片
                    _this.options.imageData.push($.extend({}, _this.baseImageObj, data));
                } else if (data.length > 0) { // 传入图片数组
                    for (var i in data) {
                        _this.options.imageData.push($.extend({}, _this.baseImageObj, data[i]));
                    }
                } else { //传入其他
                    _this.options.imageData.push($.extend({}, _this.baseImageObj));
                }
            }
            $.each(_this.options.imageData, function(i, image){
                if (image.backgroundUrl != null) {
                	if (image.url == null) {
                		image.url = image.backgroundUrl;
                	}
                    image.backgroundImg = new Image();
                    image.backgroundImg.src = image.backgroundUrl;
                }
                if (image.url != null) {
                    image.img = new Image();
                    image.img.src = image.url;
                }
                _this.addImage(image);
            });
            $('.canvas_main .canvas_right li:not(.add):first').addClass('on'); //默认选中第一幅图
        },
        initPlugins : function() { //初始化插件
        	$.each(this.options.plugins, function(i, plugin) {
        		CanvasUtils.requireJS('plugins/canvas.' + plugin + '.js', function(){
        			CanvasUtils[plugin +'Module'].init();
        		});
        	});
        },
        destroyPlugins : function() { //销毁插件
        	$.each(this.options.plugins, function(i, plugin) {
        		CanvasUtils[plugin +'Module'].destroy();
        	});
        },
        toBlob : function(base64png) {// base64转blob
            var arr = base64png.split(','),
                mime = arr[0].match(/:(.*?);/)[1],
                binary = atob(arr[1]);
            var view = new Uint8Array(binary.length);
            for (var i = 0; i < binary.length; i++) {
                view[i] = binary.charCodeAt(i);
            }
            var blob = new Blob([view], {'type' : mime});
            return blob;
        },
        getCurTool : function(){//获取当前工具
            return this[$(this.toolsSelector + ' li.tool.on').data('tool')];
        }
    }
    
    /** 引入js */
    CanvasUtils.requireJS = function(jsUrl, callback) {
    	var exists = false, baseUrl = '';
    	$.each(document.getElementsByTagName('script'), function(i, sc){
    		if (sc.src.indexOf(jsUrl) >= 0) {
    			exists = true;
    			return false;
    		} else if (sc.src.indexOf('CanvasUtils.js') >=0) {
    			baseUrl = sc.src.substring(0, sc.src.indexOf('CanvasUtils.js'));
    		}
    	});
    	if (exists) {
    		callback && callback();
    		return;
    	}
    	var script = document.createElement("script"); 
    	script.setAttribute("type", "text/javascript");
    	script.onload = function(){
    		callback && callback();
    	}
    	script.setAttribute("src", baseUrl + jsUrl); 
    	var heads = document.getElementsByTagName("head"); 
    	if(heads.length > 0) 
    	    heads[0].appendChild(script); 
    	else 
    	    document.documentElement.appendChild(script);
    }
    
    /** 重置绘图插件宽高 未使用 */
    CanvasUtils.resetRect = function(){ //修改样式后同步修改此处宽高计算,画布  宽高比 16:9
        $('.canvas_main').css('z-index', this.options.zIndex);
        var canvasHeight = $(window).height() * 0.9 - $('.canvas_main .canvas_body .canvas_op').height(),
            canvasWidth = $(window).width() * 0.8 - $('.canvas_main .canvas_right').width();
        var wScale = canvasWidth / 16, hScale = canvasHeight / 9;
        if (wScale > hScale) {
            var width = hScale * 16 + $('.canvas_main .canvas_right').width();
            var horizonMargin = ($(window).width() - width) / 2;
            $('.canvas_main').css({'width' : width + 'px', 'margin-left' : horizonMargin + 'px', 'margin-right' : horizonMargin + 'px'});
        } else {
            var height = wScale * 9 + $('.canvas_main .canvas_body .canvas_op').height();
            var verticalMargin = ($(window).height() - height) / 2;
            verticalMargin = Math.min(verticalMargin, 100);
            $('.canvas_main').css({'height' : height + 'px', 'margin-top' : verticalMargin + 'px'});
        }
    }

    /** 多图时新增绘图信息 */
    CanvasUtils.addImage = function(imgObj) {
        var _this = this;
        var $last = $('<li><div><div class="canvas_del_btn"></div></div></li>');
        $('.canvas_main .canvas_right').append($last);
        if (imgObj == undefined || imgObj == null) {
            imgObj = $.extend({}, _this.baseImageObj);
        }
        if (imgObj.img != null) {
            $last.append(imgObj.img);
        }
        $last.data('imgObj', imgObj);
        $last.on('click', function(){ //点击切换绘图
            if ($(this).hasClass('on')) {
                return;
            }
            var $this = $(this);
            DrawUtil.saveImage(function(){
                if ($('.canvas_main .canvas_right li.on img').length == 0) {
                    $('.canvas_main .canvas_right li.on').append($('.canvas_main .canvas_right li.on').data('imgObj').img);
                } else if (DrawUtil.isModify) {
                    $('.canvas_main .canvas_right li.on img').remove();
                    $('.canvas_main .canvas_right li.on').append($('.canvas_main .canvas_right li.on').data('imgObj').img);
                }
                $this.addClass('on').siblings().removeClass('on');
                DrawUtil.init($this.data('imgObj'));
            });
        });
        $last.find('.canvas_del_btn').on('click', function(e){// 删除绘图
            e.preventDefault();
            _this.options.imageData.splice($(this).parent().parent().index() - 1,1);
            if ($(this).closest('li').hasClass('on')) {
                if (!$(this).closest('li').prev().hasClass('add')) {
                    $(this).closest('li').prev().addClass('on');
                } else if ($(this).closest('li').next().length > 0) {
                    $(this).closest('li').next().addClass('on');
                }
            }
            $(this).closest('li').remove();
            if ($('.canvas_main .canvas_right li:not(.add)').length == 0) {
                _this.cancle();
                _this.options.complete([], _this.options);
                return false;
            }
            DrawUtil.init($('.canvas_main .canvas_right li.on').data('imgObj'));
            return false;
        });
        return $last;
    }

    /** 铅笔绘图 */
    var pencil = CanvasUtils.pencil = {
        $obj : null,
        lastPos : null,
        init : function() {
            $(CanvasUtils.toolsSelector).append('<li class="tool pencil" data-tool="pencil"></li>');
            this.$obj = $(CanvasUtils.toolsSelector).find('li.pencil');
            this.initEvent();
        },
        initEvent : function() {
            this.$obj.on('click', function() {
                if ($(this).hasClass('on')) {
                    return true;
                }
                $(this).siblings().removeClass('on');
                $(this).addClass('on');
            });
        },
        startDraw : function(ctx, brushAttr, position) {// ctx 画布上下文对象, brushAttr 画笔属性,  position 鼠标位置信息
            ctx.beginPath();
            ctx.moveTo(position.offsetX, position.offsetY);
            this.lastPos = [position.offsetX, position.offsetY];
        },
        mouseMove : function(ctx, position) {// ctx 画布上下文对象,  position 鼠标位置信息
            ctx.lineTo(position.offsetX, position.offsetY);
            ctx.stroke();
            this.lastPos = [position.offsetX, position.offsetY];
        },
        endDraw : function(ctx, position) {// ctx 画布上下文对象,  position 鼠标位置信息
            this.lastPos = null;
        }
    }

    /** 直线绘图 */
    var line = CanvasUtils.line = {
        $obj : null,
        init : function() {
            $(CanvasUtils.toolsSelector).append('<li class="tool line" data-tool="line"></li>');
            this.$obj = $(CanvasUtils.toolsSelector).find('li.line');
            this.initEvent();
        },
        initEvent : function() {
            this.$obj.on('click', function() {
                if ($(this).hasClass('on')) {
                    return true;
                }
                $(this).siblings().removeClass('on');
                $(this).addClass('on');
            });
        },
        startDraw : function(ctx, brushAttr, position) {// ctx 画布上下文对象, brushAttr 画笔属性,  position 鼠标位置信息
            this.drawingTrack = new DrawingTrack(brushAttr, position);
            $(CanvasUtils.canvasAreaSelector).append(this.drawingTrack.$obj);
        },
        mouseMove : function(ctx, position) {// ctx 画布上下文对象,  position 鼠标位置信息
            this.drawingTrack.resetEndPoint(position.clientX, position.clientY, 'line');
        },
        endDraw : function(ctx, position) {// ctx 画布上下文对象,  position 鼠标位置信息
            ctx.beginPath();
            ctx.moveTo(this.drawingTrack.offset.clientX - position.canvasLeft, this.drawingTrack.offset.clientY - position.canvasTop);
            ctx.lineTo(this.drawingTrack.offset.endClientX - position.canvasLeft, this.drawingTrack.offset.endClientY - position.canvasTop);
            ctx.stroke();
            this.drawingTrack.destroy();
        }
    }

    /** 图形 */
    var shape = CanvasUtils.shape = {
        $obj : null,
        shapes : ['rectangle', 'square', 'circle', 'ellipse', 'parallelogram'],
        curShape : '',
        init : function() {
            $(CanvasUtils.toolsSelector).append('<li class="tool shape" data-tool="shape"><ul class="tool_detail"></ul></li>');
            this.$obj = $(CanvasUtils.toolsSelector + ' li.shape');
            this.initShape();
            this.initEvent();
        },
        initShape : function() {
            var _this = this;
            $.each(_this.shapes, function(s, shape){
                _this.$obj.find('ul').append('<li class="' + shape + '" data-shape="' + shape + '"></li>')
            });
            _this.$obj.addClass(_this.shapes[0]);
            _this.curShape = _this.shapes[0];
            _this.$obj.find('ul li:first-child').addClass('on');
        },
        initEvent : function() {
            var _this = this;
            _this.$obj.on('click', function() {
                if ($(this).hasClass('on')) {
                    return true;
                }
                $(this).siblings().removeClass('on');
                $(this).addClass('on');
            });
            _this.$obj.find('.tool_detail li').on('click', function(e){
                e.preventDefault();
                var shape = $(this).data('shape');
                if (shape != _this.curShape) {
                    _this.setShape(shape);
                }
                return false;
            });
        },
        setShape : function(shape) {
            var _this = this;
            _this.$obj.removeClass(_this.curShape);
            _this.curShape = shape;
            _this.$obj.addClass(shape);
            _this.$obj.find('.tool_detail li').removeClass('on');
            _this.$obj.find('.tool_detail li.' + shape).addClass('on');
        },
        startDraw : function(ctx, brushAttr, position) {// ctx 画布上下文对象, brushAttr 画笔属性,  position 鼠标位置信息
            this.drawingTrack = new DrawingTrack(brushAttr, position, this.curShape);
            $(CanvasUtils.canvasAreaSelector).append(this.drawingTrack.$obj);
        },
        mouseMove : function(ctx, position) {// ctx 画布上下文对象,  position 鼠标位置信息
            this.drawingTrack.resetEndPoint(position.clientX, position.clientY, this.curShape);
        },
        endDraw : function(ctx, position) {// ctx 画布上下文对象,  position 鼠标位置信息
            var attrs = DrawingMethod.recalCulateAttr(this.curShape, 
                this.drawingTrack.offset.clientX, this.drawingTrack.offset.clientY, 
                this.drawingTrack.offset.endClientX, this.drawingTrack.offset.endClientY,
                position.canvasLeft, position.canvasTop);
            var drawMethod = 'draw' + this.curShape.charAt(0).toUpperCase() + this.curShape.substring(1);
            DrawingMethod[drawMethod](ctx, attrs);
            this.drawingTrack.destroy();
        }
    }

    /** 输入框 */
    var input = CanvasUtils.input = {
        $obj : null,
        $input : null,
        fontSize : 25,
        fontFamily : 'Arial, 宋体, Helvetica, sans-serif',
        color : '',
        init : function() {
            $(CanvasUtils.toolsSelector).append('<li class="tool input" data-tool="input"></li>');
            this.$obj =  $(CanvasUtils.toolsSelector + ' li.input');
            this.initEvent();
        },
        initEvent : function() {
            this.$obj.on('click', function() {
                if ($(this).hasClass('on')) {
                    return true;
                }
                $(this).siblings().removeClass('on');
                $(this).addClass('on');
            });
        },
        startDraw : function(ctx, brushAttr, position) {
            this.color = brushAttr.strokeStyle;
        },
        mouseMove : function(ctx, position) {
        },
        endDraw : function(ctx, position) {
            var _this = this;
            if (_this.$input != null) {
                setTimeout(function(){
                    if (_this.$input != null) {
                        _this.draw();
                    }
                }, 200);
            }
            _this.$input = $('<textarea class="input_area" autofocus="autofocus"></textarea>');
            $(CanvasUtils.canvasAreaSelector).after(_this.$input);
            _this.$input.css({
                'left' : position.offsetX, 
                'top' : position.offsetY, 
                'height' : _this.fontSize + 'px',
                'font-size' : _this.fontSize + 'px', 
                'line-height' : _this.fontSize + 'px', 
                'font-family' : _this.fontFamily,
                'color' : _this.color,
                'background' : 'rgba(255, 255, 255, 0)'
            });
            _this.$input.data({
                'pos' : position,
                'ctx' : ctx,
                'color' : _this.color
            });
            _this.$input.on('input', function() {
                $(this).height(($(this).val().split('\n').length) * _this.fontSize);
                if(position.canvasLeft + position.canvasWidth > $(this).offset().left + $(this).width() + 4) {
                    if (this.scrollHeight > $(this).height() + 4) {
                        $(this).width($(this).width() + _this.fontSize);
                    }
                    if (position.canvasLeft + position.canvasWidth <= $(this).offset().left + $(this).width() + 4) {
                        $(this).width(position.canvasLeft + position.canvasWidth - $(this).offset().left - 4);
                    }
                } else {
                    var oldInput = $(this).data('val') || '';
                    $(this).val(oldInput);
                }
                $(this).data('val', this.value);
            }).on('blur', function(){
                _this.draw();
            });
            _this.$input.focus();
        },
        draw : function() {
            var _this = this;
            $(document).unbind('click');
            var pos = _this.$input.data('pos');
            var ctx = _this.$input.data('ctx');
            ctx.font = _this.fontSize + "px " + _this.fontFamily;
            ctx.fillStyle = _this.$input.data('color');
            var startX = pos.offsetX + 2, startY = pos.offsetY + _this.fontSize - 2;
            $.each(_this.$input.val().split('\n'), function(i, text) {
                DrawingMethod.drawText(ctx, {'text' : text, 'startX' : startX, 'startY': startY});
                startY += _this.fontSize;
            });
            _this.$input.remove();
            _this.$input = null;
        }
    }

    /** 橡皮差 */
    var eraser = CanvasUtils.eraser = {
        $obj : null,
        init : function() {
            $(CanvasUtils.toolsSelector).append('<li class="tool eraser" data-tool="eraser"></li>');
            this.$obj =  $(CanvasUtils.toolsSelector + ' li.eraser');
            this.initEvent();
        },
        initEvent : function() {
            this.$obj.on('click', function() {
                if ($(this).hasClass('on')) {
                    return true;
                }
                $(this).siblings().removeClass('on');
                $(this).addClass('on');
            });
        },
        startDraw : function(ctx, brushAttr, position) {// ctx 画布上下文对象, brushAttr 画笔属性,  position 鼠标位置信息
            ctx.globalCompositeOperation = 'destination-out';//擦除模式，
            ctx.beginPath();
            ctx.moveTo(position.offsetX, position.offsetY);
            this.lastPos = [position.offsetX, position.offsetY];
        },
        mouseMove : function(ctx, position) {// ctx 画布上下文对象,  position 鼠标位置信息
            ctx.lineTo(position.offsetX, position.offsetY);
            ctx.stroke();
            this.lastPos = [position.offsetX, position.offsetY];
        },
        endDraw : function(ctx, position) {// ctx 画布上下文对象,  position 鼠标位置信息
            this.lastPos = null;
        }
    }

    /** 清空画布 */
    var clear = CanvasUtils.clear = {
        $obj : null,
        init : function() {
            $(CanvasUtils.toolsSelector).append('<li class="tool clear"></li>');
            this.$obj =  $(CanvasUtils.toolsSelector + ' li.clear');
            this.initEvent();
        },
        initEvent : function() {
            this.$obj.on('click', function(){
                CanvasUtils.beforeClear(DrawUtil.imgObj);
                DrawUtil.clearCanvas();
            });
        }
    }

    /** 分割线 */
    var splitor = CanvasUtils.splitor = {
        $obj : null,
        init : function() {
            $(CanvasUtils.toolsSelector).append('<li class="tool splitor"></li>')
        }
    }

    /** 画笔颜色 */
    var color = CanvasUtils.color = {
        $obj : null,
        colors : ['black', 'red', 'yellow', 'lightblue', 'green', 'blue', 'white'],
        init : function() {
            $(CanvasUtils.toolsSelector).append('<li class="brush color"><ul class="tool_detail"></ul><div></div></li>');
            this.$obj =  $(CanvasUtils.toolsSelector + ' li.color');
            this.initColor();
            this.initEvent();
        },
        initColor : function() { //初始化颜色子菜单
            var _this = this;
            $.each(_this.colors, function(c, color){
                _this.$obj.find('ul').append('<li class="' + color + '" data-color="' + color + '"><div></div></li>')
            });
            if (CanvasUtils.options.color == undefined || CanvasUtils.options.color == null || CanvasUtils.options.color == '') {
                CanvasUtils.options.color = _this.colors[0];
            }
            _this.$obj.addClass(CanvasUtils.options.color);
            _this.$obj.find('ul li.' + CanvasUtils.options.color).addClass('on');
        },
        initEvent : function() { //初始化时间
            var _this = this;
            _this.$obj.on('click', function() {
                $(this).toggleClass('on');
                $(this).siblings('.brush').removeClass('on');
            });
            _this.$obj.find('.tool_detail li').on('click', function(e){
                e.preventDefault();
                var color = $(this).data('color');
                if (color != CanvasUtils.options.color) {
                    _this.setColor(color);
                }
                return false;
            });
        },
        setColor : function(color) {//修改颜色
            var _this = this;
            _this.$obj.removeClass(CanvasUtils.options.color);
            CanvasUtils.options.color = color;
            _this.$obj.addClass(color);
            _this.$obj.find('.tool_detail li').removeClass('on');
            _this.$obj.find('.tool_detail li.' + color).addClass('on');
            DrawUtil.resetBrush();
        }
    }

    /** 画笔粗细 */
    var weight = CanvasUtils.weight = {
        $obj : null,
        weights : [1, 3, 5, 7, 9],
        init : function() {
            $(CanvasUtils.toolsSelector).append('<li class="brush weight"><ul class="tool_detail"></ul></li>');
            this.$obj =  $(CanvasUtils.toolsSelector + ' > li.weight');
            this.initWeight();
            this.initEvent();
        },
        initWeight : function() {
            var _this = this;
            $.each(_this.weights, function(w, weight){
                _this.$obj.find('ul').append('<li class="w' + weight + '" data-weight="' + weight + '"><div></div></li>')
            });
            if (CanvasUtils.options.weight == undefined || CanvasUtils.options.weight == null) {
                CanvasUtils.options.weight = 1;
            }
            _this.$obj.find('ul li.w' + CanvasUtils.options.weight).addClass('on');
        },
        initEvent : function() {
            var _this = this;
            _this.$obj.on('click', function() {
                $(this).toggleClass('on');
                $(this).siblings('.brush').removeClass('on');
            });
            _this.$obj.find('.tool_detail li').on('click', function(e){
                e.preventDefault();
                var weight = $(this).data('weight');
                if (weight != CanvasUtils.options.weight) {
                    _this.setWeight(weight);
                }
                return false;
            });
        },
        setWeight : function(weight) {
            var _this = this;
            _this.$obj.removeClass('w' + CanvasUtils.options.weight);
            CanvasUtils.options.weight = weight;
            _this.$obj.addClass('w' + weight);
            _this.$obj.find('.tool_detail li').removeClass('on');
            _this.$obj.find('.tool_detail li.w' + weight).addClass('on');
            DrawUtil.resetBrush();
        }
    }

    /* 背景透明度 */
    var alpha = CanvasUtils.alpha = {
        $obj : null,
        init : function() {// 初始化
            $(CanvasUtils.toolsSelector).append('<li class="tool alpha"><div class="slider"></div></li>');
            this.$obj =  $(CanvasUtils.toolsSelector + ' li.alpha');
            this.initAlpha();
            this.initEvent();
        },
        initAlpha : function() {//初始化透明度
            if (CanvasUtils.options.alpha == null || CanvasUtils.options.alpha == undefined || CanvasUtils.options.alpha == '') {
                CanvasUtils.options.alpha = 0.7;
            }
            var width = this.$obj.width();
            this.slide({
                'left' : ((1 - CanvasUtils.options.alpha) * width + 5) * 100 / width + '%',
                'opacity' : CanvasUtils.options.alpha
            });
        }, 
        initEvent : function() {
            var _this = this;
            _this.$obj.on('click', function(e) { //点击事件，移动游标
                _this.slide(_this.getData(e));
            });
            _this.$obj.find('.slider').on('mousedown touchstart', function() { // 游标按下事件
                _this.$obj.on('mousemove touchmove', function(e) {
                    if (e.buttons == 0) {
                        _this.$obj.unbind('mousemove touchmove').unbind('mouseup touchend');
                        return;
                    }
                    _this.slide(_this.getData(e));
                }).on('mouseup touchend', function(){
                    _this.$obj.unbind('mousemove touchend').unbind('mouseup touchend');
                });
            });
        },
        slide : function(data) {
            this.$obj.find('.slider').css('left', data.left);
            $(CanvasUtils.canvasAreaSelector).css('background', 'rgba(255, 255, 255, ' + data.opacity + ')');
        },
        getData : function(e) {
            var left = this.$obj.offset().left;
            var width = this.$obj.width();
            //获取鼠标当前位置, 两边留有空白5px
            var x = e.clientX > (left + width - 5) ? (left + width - 5) : e.clientX < left + 5 ? left + 5 : e.clientX;
            return {
                'left' : (x - left - 5) * 100 / width + '%',
                'opacity' : 1 - (x - left - 5) / (width - 10)
            };
        }
    }

    /** 选择图片 */
    var photo = CanvasUtils.photo = {
        $obj : null,
        init : function() {
            $(CanvasUtils.toolsSelector).append('<li class="tool photo"></li>');
            this.$obj =  $(CanvasUtils.toolsSelector + ' li.photo');
            this.initEvent();
        },
        initEvent : function(){
            var _this = this;
            _this.$obj.on('click', function() {
                var $file = $('<input type="file" style="display: none" />');
                $(document.body).append($file);
                $file.on('change', function() {
                    _this.resetImg(this);
                    $(this).remove();
                });
                $file.click();
            });
        },
        resetImg : function(obj) {
            var img = new Image();
            img.src = window.URL.createObjectURL(obj.files.item(0));
            DrawUtil.clearCanvas();
            DrawUtil.drawImage(img);
        }
    }

    /** 拍照 */
    var camera = CanvasUtils.camera = {
        $obj : null,
        init : function() {
            $(CanvasUtils.toolsSelector).append('<li class="tool camera"></li>');
            this.$obj =  $(CanvasUtils.toolsSelector + ' li.camera');
            this.initEvent();
        },
        initEvent : function() {
            var _this = this;
            this.$obj.on('click', function(){
                if ($(this).hasClass('on')) {
                    DrawUtil.clearCanvas(); //清空画布
                    DrawUtil.drawImage(_this.$video[0]);
                    if (_this.localMediaStream != null) {
                        _this.localMediaStream.stop();
                    }
                    _this.$video.remove();
                    DrawUtil.$obj.show();
                    $(this).removeClass('on');
                } else {
                    DrawUtil.clearCanvas();
                    DrawUtil.$obj.hide();
                    _this.$video = $('<video autoplay="autoplay"></video>');
                    $(CanvasUtils.canvasAreaSelector).append(_this.$video);
                    var videoObj = {"video":true};
                    if(navigator.getUserMedia){
                        navigator.getUserMedia(videoObj,function(stream){
                            localMediaStream = stream;
                            video.src = stream;
                            video.play();
                        }, function(err){});
                    }else if(navigator.webkitGetUserMedia){
                        navigator.webkitGetUserMedia(videoObj,function(stream){
                            localMediaStream = stream;
                            video.src = window.webkitURL.createObjectURL(stream);
                            video.play();
                        }, function(err){});
                    }else if(navigator.mozGetUserMedia){
                        navigator.mozGetUserMedia(videoObj,function(stream){
                            localMediaStream = stream;
                            video.src = window.URL.createObjectURL(stream);
                            video.play();
                        }, function(err){});
                    }else{
                        CameraUtils.isUseCamera = false;
                    }
                    $(this).addClass('on');
                }
            });
        }
    }

    CanvasUtils.beforeSaveImg = function(imgObj, ctx) {
        if (this.options.plugins == null || this.options.plugins.length == 0) {
            return;
        }
        var plugins = [];
        $.each(this.options.plugins, function(p, plugin) {
            plugins.push(CanvasUtils[plugin + 'Module']);
        });
        plugins[0].beforeSaveImg && plugins[0].beforeSaveImg(imgObj, ctx);
    };

    CanvasUtils.afterSaveImg = function(imgObj) {

    };
    CanvasUtils.beforeClear = function(imgObj) {
        if (this.options.plugins == null || this.options.plugins.length == 0) {
            return;
        }
        var plugins = [];
        $.each(this.options.plugins, function(p, plugin) {
            plugins.push(CanvasUtils[plugin + 'Module']);
        });
        plugins[0].beforeClear && plugins[0].beforeClear(imgObj);
    };

    /** 绘图区域操作工具 */
    var DrawUtil = window.DrawUtil = {
        ctx : null, //画布操作对象
        $obj : null, //画布对象
        isModify : false, //画布是否修改
        isMouseDown : false, //鼠标是否按下
        imgObj : null, //当前绘图图形信息对象
        canvasAttr : {},// 画布属性
        brushAttr : {//绘画笔刷属性
            strokeStyle: '',//画笔颜色
            lineWidth : '',//画笔大小
            miterLimit : 2,//最大斜接长度
            lineCap : "round"//线条的每个末端添加圆形线帽
        },
        init : function(imgObj) { //初始化
            this.$obj = $(CanvasUtils.canvasAreaSelector).find('canvas');
            this.imgObj = imgObj;
            this.clearCanvas();
            this.initEvent();
            this.initBackground(this.imgObj.backgroundImg);
            this.drawImage(this.imgObj.img, function(){
            	DrawUtil.isModify = false;
            });
        },
        initBackground : function(backgroundImg) {
            $(CanvasUtils.canvasAreaSelector).parent().children('img').remove();
            if (backgroundImg != undefined && backgroundImg != null) {
                var img = new Image();
                img.src = backgroundImg.src;
                img.setAttribute('class', 'background_img');
                // img.style.maxWidth = this.canvasAttr.canvasWidth;
                // img.style.maxHeight = this.canvasAttr.canvasHeight;
                $(CanvasUtils.canvasAreaSelector).parent().prepend(img);
                $(CanvasUtils.canvasAreaSelector).parent()
                    .children('img').css({"max-width" : this.canvasAttr.canvasWidth, "max-height" : this.canvasAttr.canvasHeight});
            }
        },
        initEvent : function() { // 事件绑定
            var _this = this;
            $(CanvasUtils.canvasAreaSelector).unbind();//解绑所有事件
            $(CanvasUtils.canvasAreaSelector).on('mousedown', function(e){
                _this.isModify = true;//按下即为修改
                _this.isMouseDown = true; //记录鼠标状态
                _this.ctx.globalCompositeOperation = 'source-over';//默认覆盖模式
                CanvasUtils.getCurTool().startDraw(_this.ctx, _this.brushAttr, _this.getPosition(e));
            }).on('mousemove', function(e){
                //logPos(e);// demo页面鼠标位置打印
                if (e.buttons == 0) {return;}
                if (_this.isMouseDown) {
                    CanvasUtils.getCurTool().mouseMove(_this.ctx, _this.getPosition(e));
                }
            }).on('mouseup', function(e){
                _this.isMouseDown = false;
                CanvasUtils.getCurTool().endDraw(_this.ctx, _this.getPosition(e));
            });
            $(CanvasUtils.canvasAreaSelector).on('touchstart', function(e){//触屏 "ontouchstart" in document.documentElement
                _this.isModify = true;//按下即为修改
                _this.isMouseDown = true; //记录鼠标状态
                _this.ctx.globalCompositeOperation = 'source-over';//默认覆盖模式
                CanvasUtils.getCurTool().startDraw(_this.ctx, _this.brushAttr, _this.getTouchPosition(e));
            }).on('touchmove', function(e){
                e.preventDefault();//禁止网页滚动
                if (_this.isMouseDown) {
                    CanvasUtils.getCurTool().mouseMove(_this.ctx, _this.getTouchPosition(e));
                }
            }).on('touchend', function(e){
                _this.isMouseDown = false;
                CanvasUtils.getCurTool().endDraw(_this.ctx, _this.getTouchPosition(e));
            });
        },
        getPosition : function(e){//鼠标位置信息 + 画布信息
            return $.extend({}, this.canvasAttr, {
                clientX : e.clientX,
                clientY : e.clientY,
                offsetX : e.offsetX,
                offsetY : e.offsetY
            });
        },
        getTouchPosition : function(e){//触摸 位置信息 + 画布信息
            var touchPos = e.originalEvent.changedTouches[0];
            return $.extend({}, this.canvasAttr, {
                clientX : touchPos.clientX,
                clientY : touchPos.clientY,
                offsetX : touchPos.clientX - this.canvasAttr.canvasLeft + this.canvasAttr.canvasBorderWidth,
                offsetY : touchPos.clientY - this.canvasAttr.canvasTop + this.canvasAttr.canvasBorderWidth
            });
        },
        saveImage : function(callback) { //保存绘图信息
            var _this = this;
            if (_this.imgObj.img == null || DrawUtil.isModify) {//绘图信息是否存在或是否修改
                _this.imgObj.isModify = true;
                CanvasUtils.beforeSaveImg(_this.imgObj, _this.ctx);
                if (_this.imgObj.backgroundImg != null) {
                    _this.ctx.globalCompositeOperation = 'destination-over';//默认覆盖模式
                    _this.drawImage(_this.imgObj.backgroundImg, function(){
                        var base64png = _this.$obj[0].toDataURL();
                        var img = new Image();
                        img.src = base64png;
                        _this.imgObj.img = img;
                        CanvasUtils.afterSaveImg(_this.imgObj);
                        callback && callback(_this.imgObj);
                    });
                } else {
                    var base64png = _this.$obj[0].toDataURL();
                    var img = new Image();
                    img.src = base64png;
                    _this.imgObj.img = img;
                    CanvasUtils.afterSaveImg(_this.imgObj);
                    callback && callback(_this.imgObj);
                }
            } else {
                callback && callback(_this.imgObj);
            }
        },
        drawImage : function(img, callback) { //绘制图片
            if (img == undefined || img == null) {
                return;
            }
            var _this = this;
            _this.isModify = true; // 画布数据是否修改
            var attrs = {
                'imageStartX' : 0, 'imageStartY' : 0,
                'startX' : 0, 'startY' : 0, 'width' : _this.canvasAttr.canvasWidth, 'height' : _this.canvasAttr.canvasHeight
            };
            var tmp = new Image(); //复制一个新对象,以免就img影响图片宽高属性
            tmp.crossOrigin = "Anonymous";
            tmp.onload = function(){ // 图片初始化完成后绘制图片
                attrs.image = this;
                attrs.imageWidth = this.width;
                attrs.imageHeight = this.height;
                if (attrs.imageWidth > attrs.width || attrs.imageHeight > attrs.height) {
                    var scale = Math.min(attrs.width/attrs.imageWidth, attrs.height/attrs.imageHeight);
                    attrs.width = attrs.imageWidth * scale;
                    attrs.height = attrs.imageHeight * scale;
                } else {
                    attrs.width = attrs.imageWidth;
                    attrs.height = attrs.imageHeight;
                }
                DrawingMethod.drawImage(_this.ctx, attrs);
                callback && callback();
            };
            tmp.src = img.src;
        },
        resetCanvas : function() { //重置画布信息
            this.isModify = true;
            // 长度、和宽度与样式保持一致，鼠标位置1:1对应
            //重置高度属性即可清空画布(个人考虑：修改高度信息会重置画布上下文信息对象)
            this.$obj.attr('width', this.$obj.width())//.width(this.$obj.width());
            this.$obj.attr('height', this.$obj.height())//.height(this.$obj.height());
            // 如果有背景图，重置背景图大小
            if ($(CanvasUtils.canvasAreaSelector).parent().children('img').length > 0) {
            	$(CanvasUtils.canvasAreaSelector).parent()
            		.children('img').css({"max-width" : this.$obj.width(), "max-height" : this.$obj.height()});
            		// .attr('width', this.$obj.width())
            		// .attr('height', this.$obj.height());
            }
            this.canvasAttr = {
                canvasLeft : this.$obj.offset().left,
                canvasTop : this.$obj.offset().top,
                canvasBorderWidth : 1, //绘图区域边框
                canvasWidth : this.$obj.width(),
                canvasHeight : this.$obj.height()
            }
        },
        resetBrush: function() {//重置画笔数据
            this.brushAttr.strokeStyle = CanvasUtils.options.color;
            this.brushAttr.lineWidth = CanvasUtils.options.weight;
            this.ctx = this.getContext();
            $.extend(this.ctx, this.brushAttr);
        },
        clearCanvas : function() {// 清空画布
            this.resetCanvas(); //重置画布信息
            this.resetBrush();//清空后画布重置，需重新获取画布上下文以及设置笔刷属性
        },
        getContext : function(){ //获取绘图对象上下文
            return this.$obj[0].getContext('2d');
        }
    }

    /** 绘图轨迹  (初始化与原始画布大小相同的临时画布作为轨迹画布) */
    var DrawingTrack = function(brushAttr, position) {
        this.$obj = $('<canvas class="drawing_track"></canvas>');
        this.brushAttr = brushAttr;
        this.offset = $.extend({}, position);
        this.$obj.width(this.offset.canvasWidth).attr('width', this.offset.canvasWidth);
        this.$obj.height(this.offset.canvasHeight).attr('height', this.offset.canvasHeight);
        
        this.ctx = this.$obj[0].getContext('2d');
        $.extend(this.ctx, this.brushAttr);

        this.resetEndPoint = function(endClientX, endClientY, tool){//重置结束点
            this.clearCanvas(tool);//清空画布
            $.extend(this.offset, {
                'endClientX' : endClientX,
                'endClientY' : endClientY
            });
            if (tool == undefined) {
                return;
            }
            // 获取绘图属性(参数：工具名称、开始位置、结束位置、)
            var attrs = DrawingMethod.recalCulateAttr(tool, 
                this.offset.clientX, this.offset.clientY, endClientX, endClientY,
                this.offset.canvasLeft, this.offset.canvasTop);
            $.extend(this, attrs);
            var word = tool.charAt(0).toUpperCase() + tool.substring(1),
                redrawMethod = 'draw' + word;
            DrawingMethod[redrawMethod](this.ctx, attrs);
        }
        this.clearCanvas = function(tool) {//清除绘画区域
            //this.$obj.attr('height', this.offset.canvasHeight);//方法一： 修改画布高度清空
            //this.ctx.clearRect(0, 0, this.offset.canvasWidth, this.canvasHeight); //方法二： 矩形清除画布所有内容
            // 有endClientX说明鼠标发生移动，已经绘过图形
            if (this.offset.endClientX == undefined) {
                return;
            }
            // 方法三： 矩形清除绘画区域
            if (tool == 'circle' || tool == 'ellipse') {
                var radius = this.radius;
                if (this.radiusWidth != undefined) {
                    radius = Math.max(this.radiusWidth, this.radiusHeight);
                }
                this.ctx.clearRect(
                    this.offset.offsetX - radius - this.brushAttr.lineWidth,
                    this.offset.offsetY - radius - this.brushAttr.lineWidth,
                    radius * 2 + 2 * this.brushAttr.lineWidth,
                    radius * 2 + 2 * this.brushAttr.lineWidth);
            } else {
                this.ctx.clearRect(
                    Math.min(this.offset.endClientX - this.offset.canvasLeft, this.offset.offsetX) - this.brushAttr.lineWidth,
                    Math.min(this.offset.endClientY - this.offset.canvasTop, this.offset.offsetY) - this.brushAttr.lineWidth,
                    Math.abs(this.offset.endClientX - this.offset.clientX) + 2 * this.brushAttr.lineWidth,
                    Math.abs(this.offset.endClientY - this.offset.clientY) + 2 * this.brushAttr.lineWidth);
            }
        }
        this.destroy = function() {
            this.$obj.remove();
            delete this;
        }
    }

    /** 绘图方法 */
    DrawingMethod = {
        drawLine : function(ctx, attrs) { // 画线段()
            ctx.beginPath();
            ctx.moveTo(attrs.startX, attrs.startY);
            ctx.lineTo(attrs.endX, attrs.endY);
            ctx.stroke();
        },
        drawRectangle : function(ctx, attrs) {// 画矩形
            ctx.strokeRect(attrs.startX, attrs.startY, attrs.width, attrs.height);
        },
        drawSquare :function(ctx, attrs) {// 画正方形
            this.drawRectangle(ctx, attrs);
        },
        drawCircle : function(ctx, attrs) {// 画圆形
            ctx.beginPath();
            ctx.arc(attrs.startX, attrs.startY, attrs.radius, 0, 2 *Math.PI);
            ctx.stroke();
            ctx.moveTo(attrs.startX, attrs.startY);
            ctx.arc(attrs.startX, attrs.startY, 0, 0, 2 *Math.PI);
            ctx.stroke();
        },
        drawEllipse : function(ctx, attrs) {// 画椭圆
            ctx.beginPath();
            var step = attrs.radiusWidth > attrs.radiusHeight ? 1 / attrs.radiusWidth : 1 / attrs.radiusHeight;
            ctx.moveTo(attrs.startX + attrs.radiusWidth, attrs.startY); //从椭圆的左端点开始绘制
            for (var i = 0; i < 2 * Math.PI; i += step)
            {
                ctx.lineTo(attrs.startX + attrs.radiusWidth * Math.cos(i), attrs.startY + attrs.radiusHeight * Math.sin(i));
            }
            ctx.closePath();
            ctx.stroke();
            ctx.moveTo(attrs.startX, attrs.startY);
            ctx.arc(attrs.startX, attrs.startY, 0, 0, 2 *Math.PI);
            ctx.stroke();
        },
        drawParallelogram : function(ctx, attrs) { // 画平行四边形
            ctx.beginPath();
            var width = attrs.endX - attrs.startX,
            height = attrs.endY - attrs.startY,
            offsetWidth = Math.min(Math.abs(height), Math.abs(width)) / 2;//默认为高度或宽度的一半
            ctx.moveTo(attrs.startX, attrs.startY);
            ctx.lineTo(attrs.endX + (width > 0 ? -offsetWidth : offsetWidth), attrs.startY);
            ctx.lineTo(attrs.endX, attrs.endY);
            ctx.lineTo(attrs.startX + (width > 0 ? offsetWidth : -offsetWidth), attrs.endY);
            ctx.closePath();
            ctx.stroke();
        },
        drawText : function(ctx, attrs) { // 画文字信息
            ctx.fillText(attrs.text, attrs.startX, attrs.startY);
        },
        drawImage : function(ctx, attrs) { // 画图片信息
            ctx.drawImage(attrs.image, 
                attrs.imageStartX,  attrs.imageStartY, attrs.imageWidth, attrs.imageHeight, 
                attrs.startX, attrs.startY, attrs.width, attrs.height);
        },
        recalCulateAttr : function(tool, startClientX, startClientY, endClientX, endClientY, canvasLeft, canvasTop) {
            var attrs = {
                startX : startClientX - canvasLeft,
                startY : startClientY - canvasTop
            };
            var width = endClientX - startClientX;
            var height = endClientY - startClientY;
            switch (tool) {
                case 'line': //直线
                case 'parallelogram': //平行四边形
                    attrs.endX = endClientX - canvasLeft;
                    attrs.endY = endClientY - canvasTop;
                    break;
                case 'rectangle': //矩形
                    attrs.width = width;
                    attrs.height = height;
                    break;
                case 'square': //正方形
                    var squareWidth = Math.min(Math.abs(width), Math.abs(height));
                    attrs.width = width < 0 ? -squareWidth : squareWidth;
                    attrs.height = height < 0 ? -squareWidth : squareWidth;
                    break;
                case 'circle': //圆形
                    attrs.radius = Math.sqrt(width * width + height * height);
                    break;
                case 'ellipse': //椭圆形
                    attrs.radiusWidth = Math.abs(width);
                    attrs.radiusHeight = Math.abs(height);
                    break;
            }
            return attrs;
        }
    }
    
    var ProcessImageFlow = { //图片保存流处理，， ajax不能完全同步，拿不到结果
        result : [],
        endCallback : null,
        process : function(endCallback){
            this.result = [];
            this.endCallback = endCallback;
            var rootNode = this.convertArr2LinkedNode(CanvasUtils.options.imageData);
            this.processImg(rootNode);
        },
        end : function() { //保存完成
            this.endCallback && this.endCallback(this.result);
        },
        processImg : function(node) { //回调串行处理图片
            var _this = this;
            if (!node.isModify) {
                _this.result.push(_this.getNodeData(node));
                if (node.hasNext) {
                    _this.processImg(node.next);
                } else {
                    _this.end();
                }
            } else {
                var formData = new FormData();
                var fileName = _this.guid() + '.png';
                node.fileName = fileName;
                formData.append('file', CanvasUtils.toBlob(node.img.src), fileName);
                formData.append('fileName', fileName);
                $.ajax({
                    url: CanvasUtils.options.uploadUrl + '?fileName=' + fileName,
                    type: "POST",
                    data: formData,
                    async : true,
                    contentType: false,
                    processData: false,
                    success: function(data){
                    	node.file = JSON.parse(data);
                        _this.result.push(_this.getNodeData(node));
                        if (node.hasNext) {
                            _this.processImg(node.next);
                        } else {
                            _this.end();
                        }
                    },
                });
            }
        },
        getNodeData : function(node){ // 获取节点数据
        	var newNode = $.extend({}, node);
        	delete newNode.img;
        	delete newNode.backgroundImg;
        	delete newNode.index;
        	delete newNode.hasNext;
        	delete newNode.next;
        	return newNode;
        },
        convertArr2LinkedNode : function(arr) { //将数组转化为 链表
            if (arr == null || arr.length == 0) {
                return null;
            }
            var tmpResult = [];
            $.each(arr, function(i, item){
                var hasNext = true;
                if (i == arr.length - 1) {
                    hasNext = false;
                }
                tmpResult.push($.extend({}, item, {'hasNext': hasNext, 'index' : i, 'next' : null}));
            });
            for (var i = tmpResult.length - 2; i >= 0; i--) {
                tmpResult[i].next = tmpResult[i + 1];
            }
            return tmpResult[0];
        },
        guid: function() {
            return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
                var r = Math.random()*16|0, v = c == 'x' ? r : (r&0x3|0x8);
                return v.toString(16);
            });
        }
    }
})(jQuery);
