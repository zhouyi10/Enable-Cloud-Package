(function(){
    /** 图片地址信息 */
    var CanvasUrl = {
        'getUrl' : '/download?fileId={0}', //获取原图
        'getThumbnailUrl' : '/thumbnail/download?fileId={0}&height=40&width=80', //获取原图缩略图
        'addCorrectionLayerUrl' : '/correctionlayer/add', //保存老师批阅图层信息
        'downloadCorrectionLayerUrl' : '/correctionlayer/download?fileId={0}&businessId={1}', //老师批阅图层图片地址url
        'addCorrectionResultUrl' : '/correctionresult/add?height=40&width=80', //保存批阅结果合成图信息url
        'downloadCorrectionResultUrl' : '/correctionresult/download?fileId={0}&businessId={1}', //批阅结果图片地址url
        'downloadCorrectionResultThumbnailUrl' : '/correctionresult/thumbnail/download?fileId={0}&businessId={1}&height=40&width=80', //批阅结果缩略图片地址url
        'editCanvasUrl' : '/manager/assessment/mark/canvas/file/update', //保存批阅结果信息url
        join : function(url) {
            return MarkV2Main.pictureUrl + url;
        }
    };

    /** 图片加载状态 */
    var ImageStatus = {
        'UNLOAD' : 'unload', //未加载
        'LOADING' : 'loading', //加载中
        'LOADED' : 'loaded', //已加载
        'ERROR' : 'error' //加载失败
    }

    /** 左侧图片批阅区域 */
    var LeftMarkArea = window.LeftMarkArea = {
        canvasRootSelector : '.canvas_root_area',
        canvasContainerSelector : '.canvas_container',
        userAnswerCanvasAreaSelector : '.user_answer_canvas .imgs',
        canvasToolsAreaSelector : '.tools',
        markedThumbnailImageTemplate : '<li><a href="javascript:;"><img src="{0}" dragable="false" data-canvasid="{1}" /></a></li>',
        tools : ['pencil', 'input', 'rectangle', 'color', 'eraser', 'clear', 'drag', 'zoom', 'rotate'], //工具事件绑定
        userAnswers : [],
        curUserAnswer : null,
        init : function(answerId, canvasId, userAnswers) { //初始化
            var _this = this;
            CanvasUrl.editCanvasUrl = _this.editCanvasUrl;
            _this.destroy();
            ImageObjFactory.init(userAnswers, answerId);
            _this.userAnswers = ImageObjFactory.userAnswers;
            _this.curUserAnswer = _this.getAnswer(answerId);
            _this.curUserAnswer.curCanvasId = canvasId;
            _this.initTools();
        },
        initHtml : function(userAnswer) { //初始化
            var _this = this;
            $(_this.userAnswerCanvasAreaSelector).empty().removeAttr('answer-id');
            $(_this.canvasContainerSelector).empty().removeAttr('img-rotate');
            $(_this.userAnswerCanvasAreaSelector).css('width', '0px');
            if (userAnswer == undefined) {
                if (_this.userAnswers.length == 0) {
                    return;
                }
                if (_this.curUserAnswer == undefined) {
                    _this.curUserAnswer = _this.userAnswers[0];
                }
            } else {
                _this.curUserAnswer = userAnswer;
            }
            ImageObjFactory.onloadUserAnswerImg(_this.curUserAnswer, function() {
                // 1. 以免重复加载
                if (_this.curUserAnswer.answerId == $(_this.userAnswerCanvasAreaSelector).attr('answer-id')) {
                    return;
                }
                $(_this.userAnswerCanvasAreaSelector).attr('answer-id', _this.curUserAnswer.answerId);

                // 2. 初始化批阅图片缩略图
                if (_this.curUserAnswer.imgObjArr == undefined || _this.curUserAnswer.imgObjArr.length == 0) {
                    return;
                }
                $.each(_this.curUserAnswer.imgObjArr, function(i, imgObj) {
                    var targetThumb = imgObj.targetBase64 || imgObj.targetThumbUrl;
                    $(_this.userAnswerCanvasAreaSelector).append(Utils.formatStr(_this.markedThumbnailImageTemplate, targetThumb, imgObj.attrs.canvasId));
                    $(_this.userAnswerCanvasAreaSelector).children(":last").data('img-obj', imgObj);
                });
                $(_this.userAnswerCanvasAreaSelector).css('width', 94 * _this.curUserAnswer.imgObjArr.length - 10 + 'px');
                // 3. 缩略图点击事件
                $(_this.userAnswerCanvasAreaSelector).children().unbind('click').on('click', function() {
                    if ($(this).find('a').hasClass('select')) {
                        return ;
                    }
                    $(this).parent().find('a.select').removeClass('select');
                    $(this).find('a').addClass('select');
                    // 1. 保存
                    var containerOp = $(_this.canvasContainerSelector).data('container');
                    if (containerOp && containerOp.modify) {
                        containerOp.save();
                    }
                    // 2. 清空
                    $(_this.canvasContainerSelector).removeAttr('img-rotate').removeData();
                    // 3. 加载下一个
                    var data = $(this).data('img-obj');
                    var containerOp = new CanvasContainerArea($(_this.canvasContainerSelector), data);
                    $(_this.canvasContainerSelector).data('container', containerOp);
                    containerOp.init();
                });

                var init = false;
                if (_this.curUserAnswer.curCanvasId) {
                    $(_this.userAnswerCanvasAreaSelector).children().each(function(){
                        var data = $(this).data('img-obj');
                        if (data.attrs.canvasId == _this.curUserAnswer.curCanvasId) {
                            $(this).click();
                            init = true;
                            return false;
                        }
                    });
                    delete _this.curUserAnswer.curCanvasId;
                }
                if (!init) {
                    // 默认选择第一个
                    $(_this.userAnswerCanvasAreaSelector).children(":first").click();
                }
            });
        },
        resetOrder : function(newUserAnswers) {
            var order = {};
            $.each(newUserAnswers, function(u, userAnswer) {
                order[userAnswer.answerId] = u;
            });
            ImageObjFactory.resetOrder(order);
        },
        initTools : function() {
            var _this = this;
            $.each(this.tools, function(t, tool) {
                _this[tool].init();
            });
            if ($(_this.canvasToolsAreaSelector + ' ul.l a.select').length == 0) {
                $(_this.canvasToolsAreaSelector + ' a.pencil').addClass('select');
            }
            this.setCurTool($(_this.canvasToolsAreaSelector + ' ul.l a.select').data('tool'));
        },
        complete : function(callback) {
            var _this = this;
            var containerOp = $(_this.canvasContainerSelector).data('container');
            if (containerOp == undefined) {
                callback && callback();
                return;
            }
            if (containerOp.modify) {
                containerOp.save();
            }
            if (callback) {
                _this.completeCallback = function() {
                    callback();
                    delete _this.completeCallback;
                }
            }

            var modifySaved = true;
            $.each(_this.userAnswers, function(i, userAnswer) {
                $.each(userAnswer.imgObjArr, function(i, imgObj) {
                    if (imgObj.modify) {
                        modifySaved = false;
                        if (imgObj.modifySavedCallback == undefined) {
                            imgObj.onModifySaved(function() {
                                _this.complete();
                            });
                        }
                    }
                });
            });
            if (modifySaved) {
                _this.completeCallback();
            }
        },
        getAnswer : function(answerId) {
            if (answerId) {
                for (var i = 0; i < this.userAnswers.length; i++) {
                    if (this.userAnswers[i].answerId == answerId) {
                        return this.userAnswers[i];
                    }
                }
            } else {
                return this.userAnswers[0];
            }
            return null;
        },
        change : function(answerId) { // 切换答案
            var containerOp = $(this.canvasContainerSelector).data('container');
            if (containerOp && containerOp.modify) {
                containerOp.save();
            }
            this.initHtml(this.getAnswer(answerId));
        },
        setCurTool : function(obj) { //设置当前工具
            this.curTool = obj;
        },
        getCurTool : function() { //获取当前工具
            return this.curTool;
        },
        destroy : function() { //销毁自己
            ImageObjFactory.destroy();
            var _this = this;
            $(_this.userAnswerCanvasAreaSelector).empty().removeAttr('answer-id');
            $(_this.canvasContainerSelector).empty().removeAttr('img-rotate');
            $(_this.userAnswerCanvasAreaSelector).css('width', '0px');
        }
    };

    /** 图片对象预加载工厂 */
    var ImageObjFactory = {
        userAnswers : [],
        imgObjArr : [],
        init : function(userAnswers, answerId) { //初始化绘图对象信息
            var _this = this;
            _this.userAnswers = [];
            for (var u = 0; u < userAnswers.length; u++) {
                var tempUserAnswer = $.extend({}, userAnswers[u]);
                tempUserAnswer.allImgLoadStatus = ImageStatus.UNLOAD;
                if (tempUserAnswer.canvases != null && tempUserAnswer.canvases.length > 0) {
                    tempUserAnswer.imgObjArr = [];

                    $.each(tempUserAnswer.canvases, function(c, canvas) {
                        if ("2" != canvas.canvasAnswerType) {
                            var originUrl = Utils.formatStr(CanvasUrl.join(CanvasUrl.getUrl), canvas.sourceFileId);//canvas.url;
                            var markLayerUrl = Utils.formatStr(CanvasUrl.join(CanvasUrl.downloadCorrectionLayerUrl), canvas.sourceFileId, canvas.canvasId);
                            var targetThumbUrl = Utils.formatStr(CanvasUrl.join(CanvasUrl.downloadCorrectionResultThumbnailUrl), canvas.sourceFileId, canvas.canvasId) + "&timestamp=" + new Date().getTime();
                            tempUserAnswer.imgObjArr.push(new ImageObj(originUrl, markLayerUrl, targetThumbUrl, canvas));
                        }
                    });
                }
                _this.userAnswers.push(tempUserAnswer);
            }
            if (answerId == '') {
                _this.load(_this.userAnswers[0].answerId);
            } else {
                _this.load(answerId);
            }
        },
        load : function(answerId, callback) { //加载对应答案图片信息，并预加载之后两个学生答案的图片信息
            for (var index = 0; index < this.userAnswers.length; index++) {
                if (this.userAnswers[index].answerId == answerId) {
                    if (callback != undefined) {
                        this.userAnswers[index].allImgLoadCallback = callback;
                    }
                    this.loadUserAnswerImg(this.userAnswers[index]);
                    if (index + 1 < this.userAnswers.length && this.userAnswers[index + 1].imgObjArr) {
                        this.preloadUserAnswerImg(this.userAnswers[index + 1]);
                        if (index + 2 < this.userAnswers.length && this.userAnswers[index + 2].imgObjArr) {
                            this.preloadUserAnswerImg(this.userAnswers[index + 2]);
                        }
                    }
                    break;
                }
            }
        },
        loadUserAnswerImg : function(userAnswer) { //加载答案图片
            var _this = this;
            if (userAnswer.allImgLoadStatus == ImageStatus.UNLOAD) {
                userAnswer.allImgLoadStatus = ImageStatus.LOADING;
                if (userAnswer.imgObjArr == undefined) {
                    userAnswer.allImgLoadStatus = ImageStatus.LOADED;
                    return;
                }
                var status = [];
                $.each(userAnswer.imgObjArr, function(i, imgObj) {
                    imgObj.onload(function(){
                        _this.onloadUserAnswerImg(userAnswer);
                    });
                });
            } else {
                userAnswer.allImgLoadCallback && userAnswer.allImgLoadCallback();
            }
        },
        preloadUserAnswerImg : function(userAnswer) { //预加载答案图片
            var _this = this;
            userAnswer.allImgLoadStatus = ImageStatus.LOADING;
            setTimeout(function() {
                if (userAnswer.imgObjArr == undefined) {
                    userAnswer.allImgLoadStatus = ImageStatus.LOADED;
                    return;
                }
                var status = [];
                $.each(userAnswer.imgObjArr, function(i, imgObj) {
                    imgObj.onload(function(){
                        _this.onloadUserAnswerImg(userAnswer);
                    });
                });
            }, 200);
        },
        onloadUserAnswerImg : function(userAnswer, callback) { //监听图片加载完成
            var _this = this;
            if (callback != undefined) {
                userAnswer.allImgLoadCallback = callback;
            }
            if (userAnswer.allImgLoadStatus == ImageStatus.UNLOAD) {
                _this.load(userAnswer.answerId, callback);
            } else if (userAnswer.allImgLoadStatus == ImageStatus.LOADING) {
                var result = true;
                $.each(userAnswer.imgObjArr, function(i, imgObj) {
                    if (imgObj.status != ImageStatus.LOADED && imgObj.status != ImageStatus.ERROR) {
                        result = false;
                        return false;
                    }
                });
                if (result) {
                    userAnswer.allImgLoadStatus = ImageStatus.LOADED;
                    _this.onloadUserAnswerImg(userAnswer);
                }
            } else {
                userAnswer.allImgLoadCallback && userAnswer.allImgLoadCallback.call(_this);
            }
        },
        resetOrder : function(order) {
            this.userAnswers.sort(function(ua1, ua2){
                return order[ua1] - order [ua2];
            });
        },
        destroy : function() { //销毁工厂
            $.each(this.userAnswers, function(u, userAnswer) {
               delete userAnswer.allImgLoadStatus;
               delete userAnswer.imgObjArr;
               delete userAnswer.allImgLoadCallback;
            });
        }
    };

    /**
     * 单张图片信息
     * @param url 图片地址
     * @param status 图片加载状态
     * @param imageObj 对应批阅图片信息对象
     * @constructor
     */
    var ImgInfo = function(url, imageObj, status) {
        this.url = url;
        this.status = status || ImageStatus.UNLOAD;
        this.imageObj = imageObj;
        this.load = function(){ //图片加载
            var _this = this;
            _this.status = ImageStatus.LOADING;
            setTimeout(function(){
                if (_this.url == null) {
                    _this.status = ImageStatus.LOADED;
                    return;
                }
                var img = new Image();
                img.onload = function() { //加载成功
                    _this.status = ImageStatus.LOADED;
                    _this.imageObj.onload();
                };
                img.onerror = function() { //加载失败
                    if (imageObj.originUrl == _this.url) {
                        imageObj.originUrl = null;
                    } else if (imageObj.markLayerUrl == _this.url) {
                        imageObj.markLayerUrl = null;
                    } else if (imageObj.targetThumbUrl == _this.url) {
                        imageObj.targetThumbUrl = null;
                    }
                    if (_this.status != ImageStatus.LOADED) {
                        _this.status = ImageStatus.ERROR;
                    }
                    _this.imageObj.onload();
                };
                img.src = _this.url;
            }, 0);
        }
    }

    /**
     * 批阅图像信息对象
     * @param originUrl 原始图片url
     * @param markLayerUrl 批阅信息url
     * @param targetThumbUrl 批阅合成缩略图url
     * @param attr 图像属性
     * @constructor
     */
    var ImageObj = function(originUrl, markLayerUrl, targetThumbUrl, attr) {
        this.originUrl = originUrl; //原始图片url
        this.markLayerUrl = markLayerUrl; //批阅图层url
        this.targetThumbUrl = targetThumbUrl; //批阅后合成图片缩略图
        this.status = ImageStatus.UNLOAD; // 图片状态
        this.imgs = [];//原图、批阅层、合成缩略图状态
        this.attrs = attr || {};
        this.modify = false;
        this.modifySaved = false;
        this.markLayerBase64 = '';
        this.targetBase64 = '';
        this.load = function(callback) { // 加载图片
            var _this = this;
            if (callback != undefined) {
                _this.callback = callback;
            }
            _this.status = ImageStatus.LOADING;
            _this.imgs.push(new ImgInfo(_this.originUrl, _this));
            _this.imgs.push(new ImgInfo(_this.markLayerUrl, _this));
            _this.imgs.push(new ImgInfo(_this.targetThumbUrl, _this));

            $.each(_this.imgs, function(i, imgInfo) {
                imgInfo.load();
            });
        };
        this.onload = function(callback) { //监听加载完成事件
            var _this = this;
            if (callback != undefined) { //外部监听图片加载状态
                _this.callback = callback;
            }
            if (_this.status == ImageStatus.UNLOAD) {
                _this.load();
            } else if(_this.status == ImageStatus.LOADED) {
                _this.callback && _this.callback.call(_this);
            } else if (_this.status == ImageStatus.LOADING) {
                var result = true;
                $.each(_this.imgs, function(i, imgInfo) {
                    if (imgInfo.status != ImageStatus.LOADED && imgInfo.status != ImageStatus.ERROR) {
                        result = false;
                        return false;
                    }
                });
                if (result) {
                    _this.status = ImageStatus.LOADED;
                    _this.onload();
                }
            }
        };
        this.onModifySaved = function(callback) { //监听修改的图片保存完成事件
            var _this = this;
            if (callback != undefined) {
                _this.modifySavedCallback = callback;
            }
            if (!this.modify) {
                _this.modifySavedCallback && _this.modifySavedCallback();
                delete _this.modifySavedCallback;
            }
        };
        this.replaceBase64 = function(markLayerBase64, targetBase64) { //替换图片base64,异步保存图片信息
            var _this = this;
            _this.markLayerBase64 = markLayerBase64;
            _this.targetBase64 = targetBase64;
            _this.modifySavedStatus = false;
            setTimeout(function(){
                _this.save();
            }, 0);
        };
        this.save = function() { //保存图片信息
            var _this = this;
            // 1. 保存批阅图层信息
            var formData = new FormData();
            var fileName = _this.attrs.fileId + '_correction_layer.png';
            formData.append('file', Utils.toBlob(_this.markLayerBase64), fileName);
            formData.append('fileName', fileName);
            formData.append('fileId', _this.attrs.sourceFileId);
            formData.append('businessId', _this.attrs.canvasId);
            $.ajax({
                url: CanvasUrl.join(CanvasUrl.addCorrectionLayerUrl),
                type: "POST",
                data: formData,
                async : true,
                contentType: false,
                processData: false,
                success: function(data){

                }
            });
            // 2. 保存批阅结果图信息
            formData = new FormData();
            fileName = _this.attrs.fileId + '_correction_result.png';
            formData.append('file', Utils.toBlob(_this.targetBase64), fileName);
            formData.append('fileName', fileName);
            formData.append('fileId', _this.attrs.sourceFileId);
            formData.append('businessId', _this.attrs.canvasId);
            $.ajax({
                url: CanvasUrl.join(CanvasUrl.addCorrectionResultUrl),
                type: "POST",
                data: formData,
                async : true,
                contentType: false,
                processData: false,
                success: function(data){
                    data = data.data;
                    // 3. 设置字段信息
                    _this.attrs.url = Utils.formatStr(CanvasUrl.join(CanvasUrl.downloadCorrectionResultUrl), _this.attrs.sourceFileId, _this.attrs.canvasId);
                    _this.targetThumbUrl = Utils.formatStr(CanvasUrl.join(CanvasUrl.downloadCorrectionResultThumbnailUrl), _this.attrs.sourceFileId, _this.attrs.canvasId) + '&timestamp=' + new Date().getTime();
                    _this.attrs.fileId = data.correctionFileId;
                    // 4. 修改绘图信息
                    var param = {
                        "canvasId" : _this.attrs.canvasId,
                        "fileId" : _this.attrs.fileId,
                        "fileName" : fileName,
                        "url" : _this.attrs.url
                    }
                    $.post(CanvasUrl.editCanvasUrl, param, function(data){ //保存图片数据
                        if (data.status == 1) {
                            _this.modify = false;
                            if (_this.modifySavedCallback != undefined) {
                                _this.modifySavedCallback();
                                delete _this.modifySavedCallback;
                            }
                            if (MarkV2Main.isCanvasChange !== undefined) {
                                MarkV2Main.destroyBeforeunload("canvas");
                            }
                        }
                    });
                }
            });
        };
    }

    /** 铅笔绘图 */
    var Pencil = LeftMarkArea.pencil = {
        lastPos : null,
        init : function() {
            this.$canvasContainer = $(LeftMarkArea.canvasContainerSelector);
            this.initEvent();
        },
        initEvent : function() {
            var _this = this;
            $('a.pencil').data('tool', this);
            $('a.pencil').unbind('click').on('click', function() {
                Drag.resetDraw();
                Eraser.resetDraw();
                if ($(this).hasClass('select')) {
                    return true;
                }
                $(this).closest('ul').find('a').removeClass('select');
                $(this).addClass('select');
                LeftMarkArea.setCurTool(_this);
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
    };


    /** 输入框 */
    var input = LeftMarkArea.input = {
        $input : null,
        fontSize : 25,
        fontFamily : 'Arial, 宋体, Helvetica, sans-serif',
        color : '',
        init : function() {
            this.$canvasContainer = $(LeftMarkArea.canvasContainerSelector);
            this.initEvent();
        },
        initEvent : function() {
            var _this = this;
            $('a.input').data('tool', this);
            $('a.input').unbind('click').on('click', function() {
                Drag.resetDraw();
                Eraser.resetDraw();
                if ($(this).hasClass('select')) {
                    return true;
                }
                $(this).closest('ul').find('a').removeClass('select');
                $(this).addClass('select');
                LeftMarkArea.setCurTool(_this);
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
            _this.$canvasContainer.append(_this.$input);
            var containterRect = _this.$canvasContainer.offset();
            _this.$input.css({
                'left' : position.clientX - containterRect.left,
                'top' : position.clientY - containterRect.top,
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
                var position = $(this).data('pos');
                if(position.left + position.width > $(this).position().left + $(this).width() + 4) {
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
            var drawFontSize = _this.fontSize / pos.scale;
            var ctx = _this.$input.data('ctx');
            ctx.save();
            ctx.font = drawFontSize + "px " + _this.fontFamily;
            ctx.fillStyle = _this.$input.data('color');
            ctx.translate(pos.offsetX, pos.offsetY);
            var imgRotate = new Number(_this.$canvasContainer.attr('img-rotate') || '0');
            if ((imgRotate + 90) % 180 == 0) { //旋转90度或270度
                imgRotate = (imgRotate + 180) % 360;
            }
            ctx.rotate(imgRotate * Math.PI / 180);
            $.each(_this.$input.val().split('\n'), function(i, text) {
                DrawingMethod.drawText(ctx, {'text' : text, 'startX' : 0, 'startY': (i+1) * drawFontSize});
                // startY += drawFontSize;
            });
            ctx.restore();
            _this.$input.remove();
            _this.$input = null;
        }
    }

    /** 矩形 */
    var Rectangle = LeftMarkArea.rectangle = {
        init : function() {
            this.$canvasContainer = $(LeftMarkArea.canvasContainerSelector);
            this.initEvent();
        },
        initEvent : function() {
            var _this = this;
            $('a.rectangle').data('tool', this);
            $('a.rectangle').unbind('click').on('click', function() {
                Drag.resetDraw();
                Eraser.resetDraw();
                if ($(this).hasClass('select')) {
                    return true;
                }
                $(this).closest('ul').find('a').removeClass('select');
                $(this).addClass('select');
                LeftMarkArea.setCurTool(_this);
            });
        },
        startDraw : function(ctx, brushAttr, position) {// ctx 画布上下文对象, brushAttr 画笔属性,  position 鼠标位置信息
            this.drawingTrack = new DrawingTrack(brushAttr, position, 'rectangle');
            this.$canvasContainer.find('div.canvas_layer').append(this.drawingTrack.$obj);
        },
        mouseMove : function(ctx, position) {// ctx 画布上下文对象,  position 鼠标位置信息
            this.drawingTrack.resetEndPoint(position.offsetX, position.offsetY, 'rectangle');
        },
        endDraw : function(ctx, position) {// ctx 画布上下文对象,  position 鼠标位置信息
            // var attrs = {
            //     startX: this.drawingTrack.offset.offsetX,
            //     startY: this.drawingTrack.offset.offsetY,
            //     width: position.offsetX - this.drawingTrack.offset.offsetX,
            //     height: position.offsetY - this.drawingTrack.offset.offsetY
            // };
            var attrs = DrawingMethod.recalCulateAttr('rectangle',
                this.drawingTrack.offset.offsetX, this.drawingTrack.offset.offsetY,
                position.offsetX, position.offsetY,
                position.left, position.top);
            var drawMethod = 'drawRectangle';
            DrawingMethod[drawMethod](ctx, attrs);
            this.drawingTrack.destroy();
        }
    };

    var Color = LeftMarkArea.color = {
        init : function() {
            this.$canvasContainer = $(LeftMarkArea.canvasContainerSelector);
            this.initEvent();
        },
        initEvent : function() {
            var _this = this;
            $('a.color').unbind('click').on('click', function() {
                $('ul.colors').toggle();
                $('ul.weights').hide();
            });
            $('ul.colors li').unbind('click').on('click', function(){
                $(this).parent().find('a').removeClass('select');
                $(LeftMarkArea.canvasToolsAreaSelector + ' a.color em').css('background', $(this).find('a').data('color'));
                $(this).find('a').addClass('select');
                var containerOp = $(LeftMarkArea.canvasContainerSelector).data('container');
                containerOp.setColor($(this).find('a').data('color'));
            });
        },
    };

    var Eraser = LeftMarkArea.eraser = {
        lastPos : null,
        drawBrushWidth : '',
        eraserWidth : '',
        init : function() {
            this.$canvasContainer = $(LeftMarkArea.canvasContainerSelector);
            this.initEvent();
        },
        initEvent : function() {
            var _this = this;
            $('a.eraser').data('tool', this);
            $('a.eraser').unbind('click').on('click', function() {
                Drag.resetDraw();
                if ($(this).hasClass('select')) {
                    return true;
                }
                $(this).closest('ul').find('a').removeClass('select');
                $(this).addClass('select');
                LeftMarkArea.setCurTool(_this);

                $('ul.colors').hide();
                $('ul.weights').show();
                _this.drawBrushWidth = _this.$canvasContainer.data('container').getBrushWidth();
                _this.eraserWidth = $('ul.weights li.select').data('weight');
            });
            $('ul.weights li').unbind('click').on('click', function(){
                $(this).parent().find('li').removeClass('select');
                $(this).addClass('select');
                _this.eraserWidth = $('ul.weights li.select').data('weight');
                // $(ImageOperationArea.canvasToolsAreaSelector + ' li.weight em').text($(this).text());
                // var containerOp = $(ImageOperationArea.canvasContainerSelector).data('container');
                // containerOp.setBrushWidth($(this).data('weight'));
            });
        },
        resetDraw: function(){
            $('ul.weights').hide();
            if (this.drawBrushWidth) {
                this.$canvasContainer.data('container').setBrushWidth(this.drawBrushWidth);
                this.drawBrushWidth = '';
            }
        },
        startDraw : function(ctx, brushAttr, position) {// ctx 画布上下文对象, brushAttr 画笔属性,  position 鼠标位置信息
            var containerOp = this.$canvasContainer.data('container');
            containerOp.setBrushWidth(this.eraserWidth);
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
            ctx.lineTo(position.offsetX, position.offsetY);
            ctx.stroke();
            var containerOp = $(LeftMarkArea.canvasContainerSelector).data('container');
            containerOp.setBrushWidth(this.drawBrushWidth);
            this.lastPos = null;
        }
    };

    /** 清空 */
    var Clear = LeftMarkArea.clear = {
        init : function() {
            this.$canvasContainer = $(LeftMarkArea.canvasContainerSelector);
            this.initEvent();
        },
        initEvent : function() {
            var _this = this;
            $('a.clear').unbind('click').on('click', function() {
                var containerOp = _this.$canvasContainer.data('container');
                containerOp.clearCanvas();
            });
        },
    };

    /** 拖动 */
    var Drag = LeftMarkArea.drag = {
        modes : ['drag', 'draw'],
        init : function() {
            this.$canvasContainer = $(LeftMarkArea.canvasContainerSelector);
            this.initEvent();
        },
        initEvent : function() {
            var _this = this;
            $('a.drag').unbind('click').on('click', function() {
                var containerOp = _this.$canvasContainer.data('container');
                $(this).toggleClass('select');
                if ($(this).hasClass('select')) {
                    containerOp.setMode(_this.modes[0]);
                } else {
                    containerOp.setMode(_this.modes[1]);
                }
            });
        },
        resetDraw : function() {
            $('a.drag').removeClass('select');
            var containerOp = this.$canvasContainer.data('container');
            containerOp.setMode(this.modes[1]);
        }
    }

    /** 缩放 */
    var Zoom = LeftMarkArea.zoom = {
        init : function() {
            this.$canvasContainer = $(LeftMarkArea.canvasContainerSelector);
            this.initEvent();
        },
        initEvent : function() {
            var _this = this;
            $('a[class^="zoom"]').unbind('click').on('click', function() {
                var containerOp = $(LeftMarkArea.canvasContainerSelector).data('container');
                var scale = containerOp.canvasAttr.scale;
                if (scale <= 0.1 && $(this).hasClass('zoomOut') || scale >= 1.5 && $(this).hasClass('zoomIn')) {
                    return;
                }
                containerOp.scale($(this).hasClass('zoomIn') ? 0.2 : -0.2);
            });
        }
    };

    /** 旋转 */
    var Rotate = LeftMarkArea.rotate = {
        init : function() {
            this.$canvasContainer = $(LeftMarkArea.canvasContainerSelector);
            this.initEvent();
        },
        initEvent : function() {
            var _this = this;
            $('a[class^="rotate"]').unbind('click').on('click', function() {
                // 该按钮旋转角度(90/180/270) + 原始角度(默认0)
                $(LeftMarkArea.canvasContainerSelector).data('container').rotate(new Number($(this).attr('class').replace('rotate', '')));
            });
        }
    };

    /** 绘图区域事件 */
    var CanvasContainerArea = function($container, imgObj) {
        this.$container = $container;
        this.imgObj = imgObj;
        this.$img = null;
        this.modify = false;
        this.canvasHtmlTemplate = '<div class="canvas_layer"><canvas class="img_op_canvas" style="left: {left}px; top: {top}px; width: {width}px; height: {height}px;" width="{attrWidth}" height="{attrHeight}" scale="{scale}"></canvas></div>';
        this.$canvas = null;
        this.mode = 'draw';

        var containerOffset = this.$container.offset();
        this.canvasAttr = {// 画布属性
            containerLeft: containerOffset.left,
            containerTop: containerOffset.top,
            left : 0,
            top : 0, // 左上角位置
            borderWidth : 1, //绘图区域边框
            width : 0,
            height : 0, // 样式宽高
            attrWidth: 0,
            attrHeight: 0, // 绘图实际宽高
            scale : 1 //canvas 比例
        };
        this.brushAttr = {//绘画笔刷属性
            strokeStyle: 'red',//画笔颜色
            lineWidth : '5',//画笔大小
            miterLimit : 2,//最大斜接长度
            lineCap : "round"//线条的每个末端添加圆形线帽
        };
        this.init = function() {
            this.$container.empty();
            this.initImage();
        };
        this.initImage = function() {
            var _this = this;
            var backImg = new Image();
            backImg.crossOrigin = "Anonymous";
            backImg.onload = function() {
                this.className = 'img_op_back_img';
                this.draggable = false;
                var rect = _this.recalculateCanvasRect(this.width, this.height, _this.$container.width(), _this.$container.height());
                $.extend(_this.canvasAttr, rect);
                _this.initCanvas();
                _this.resetBrush();
                _this.$img = $(this);
                _this.$container.prepend($(this));
                $(this).css({
                    'left' : rect.left,
                    'top' : rect.top,
                    'width' : rect.width,
                    'height' : rect.height
                });
                if (_this.imgObj.markLayerUrl != '' && _this.imgObj.markLayerUrl != null || _this.imgObj.markLayerBase64 != '') {
                    var img = new Image();
                    img.crossOrigin = "Anonymous";
                    img.onload = function() {
                        _this.drawImage(this, _this.$container);
                    };
                    img.src = _this.imgObj.markLayerBase64 || _this.imgObj.markLayerUrl;
                }
            }
            backImg.src = _this.imgObj.originUrl;
        };
        this.initCanvas = function() { // 初始化画布
            var _this = this;
            _this.$container.append(Utils.formatStr(_this.canvasHtmlTemplate, _this.canvasAttr));
            _this.$container.children('div').css({
                'height' : _this.$container.height() + 'px'
            })
            _this.$canvas = _this.$container.find('.img_op_canvas');
            _this.initEvent();
        };
        this.bindDrawEvent = function(eventNames, getPosFunc){
            var _this= this;
            _this.$container.find('div').on(eventNames[0], function(e) { // 鼠标按下事件
                if (e.target == _this.$canvas[0]) {
                    var pos = getPosFunc.call(_this, e);
                    var mode = _this.getMode(e);
                    _this.$canvas.data({
                        'mouseDown': true,
                        'mode' : mode,
                        'startX': pos.clientX,
                        'startY': pos.clientY
                    });
                    _this.ctx.globalCompositeOperation = 'source-over';//默认覆盖模式
                    if (mode == 'draw') {
                        LeftMarkArea.getCurTool() && LeftMarkArea.getCurTool().startDraw(_this.ctx, _this.brushAttr, pos);
                    }
                }
            }).on(eventNames[1], function(e) { //鼠标移动
                if (e.buttons == 0) {return;}
                if (_this.$canvas.data('mouseDown')) { // 鼠标按下
                    var pos = getPosFunc.call(_this, e);
                    var mode = _this.getMode(e);
                    if (mode == 'drag') { //拖动模式
                        var offset = _this.$canvas.offset();
                        _this.canvasAttr.left +=  pos.clientX - _this.$canvas.data('startX');
                        _this.canvasAttr.top +=  pos.clientY - _this.$canvas.data('startY');
                        offset.left += pos.clientX - _this.$canvas.data('startX');
                        offset.top += pos.clientY - _this.$canvas.data('startY');
                        _this.$canvas.offset(offset);
                        _this.$img.offset(offset);
                    } else if (mode == 'draw') { //画图模式
                        if ('CANVAS' == e.target.tagName.toUpperCase()) {
                            LeftMarkArea.getCurTool() && LeftMarkArea.getCurTool().mouseMove(_this.ctx, pos);
                            if (!_this.modify) {
                                _this.modify = true;
                                if (MarkV2Main.isCanvasChange !== undefined) {
                                    MarkV2Main.createBeforeunload("canvas");
                                }
                            }
                        }
                    }
                    _this.$canvas.data({
                        'startX': pos.clientX,
                        'startY': pos.clientY
                    });
                }
            }).on(eventNames[2], function(e){ // 鼠标抬起
                if (_this.$canvas.data('mode') == 'draw') {
                    LeftMarkArea.getCurTool() && LeftMarkArea.getCurTool().endDraw(_this.ctx, getPosFunc.call(_this, e));
                }
                _this.$canvas.removeData();
            });
        }
        this.initEvent = function() { //初始化事件
            var _this = this;
            _this.$container.find('div').unbind();
            _this.bindDrawEvent(['mousedown', 'mousemove', 'mouseup'], _this.getPosition);
            _this.bindDrawEvent(['touchstart', 'touchmove', 'touchend'], _this.getTouchPosition);

            this.$canvas.on('contextmenu', function(){ // 禁用绘图右键菜单
                return false;
            });
            Utils.onMouseWheel(_this.$canvas, function(e, direct) { //滚轮事件
                var mousePosition = {
                    clientX : e.clientX, // 鼠标绝对位置
                    clientY : e.clientY,
                    offsetX : e.offsetX,
                    offsetY : e.offsetY
                };
                _this.scale(direct == 'up' ? 0.1 : -0.1, mousePosition);
                e.preventDefault();
            });
        };
        this.save = function() {
            var _this = this;
            if (_this.modify) {
                _this.modify = false;
                _this.imgObj.modify = true;
                var correctionLayerBase64Data = _this.$canvas[0].toDataURL();
                _this.ctx.globalCompositeOperation = 'destination-over';
                var img = _this.$img[0];
                var attrs = {
                    'image' : img,
                    'imageStartX' : 0, 'imageStartY' : 0, 'imageWidth' : _this.canvasAttr.attrWidth, 'imageHeight' : _this.canvasAttr.attrHeight,
                    'startX' : 0, 'startY' : 0, 'width' : _this.canvasAttr.attrWidth, 'height' : _this.canvasAttr.attrHeight
                };
                DrawingMethod.drawImage(_this.ctx, attrs);
                var correctionResultBase64Data = _this.$canvas[0].toDataURL();
                $(Utils.formatStr('img[data-canvasid="{0}"]', _this.imgObj.attrs.canvasId)).attr('src', correctionResultBase64Data);
                _this.imgObj.replaceBase64(correctionLayerBase64Data, correctionResultBase64Data);
            }
        };
        this.getMode = function(e) { // 获取模式 drag  draw
            if (e != undefined) {
                if (e.ctrlKey || e.buttons == 2) {
                    return 'drag';
                }
            }
            return this.mode;
        };
        this.setMode = function(mode) { //设置模式  拖动/绘图
            if ('drag' == mode) {
                this.mode = mode;
                this.$canvas.css('cursor', 'move');
            } else {
                this.mode = 'draw';
                this.$canvas.css('cursor', 'crosshair');
            }
        };
        this.rotate = function(angle) { // 旋转
            this.canvasAttr.left = (this.$container.width() - this.canvasAttr.width) / 2;
            this.canvasAttr.top = (this.$container.height() - this.canvasAttr.height) / 2;
            this.$img.css({
                'left' : this.canvasAttr.left + 'px',
                'top' : this.canvasAttr.top + 'px'
            });
            this.$canvas.css({
                'left' : this.canvasAttr.left + 'px',
                'top' : this.canvasAttr.top + 'px'
            });
            // 该按钮旋转角度(90/180/270) + 原始角度(默认0)
            var rotate = (angle || 0) + new Number(this.$container.attr('img-rotate') || 0) + 360;
            rotate = rotate % 360;
            this.$container.attr({ //修改container属性，css中根据该属性旋转内部图片
                'img-rotate' : rotate
            });
        };
        this.scale = function(scaleOffset, scaleCenterPos) { // 缩放  *scaleOffset 缩放变化比例     *scaleCenterPos  缩放中心点
            var originScale = this.canvasAttr.scale;
            if (originScale <= 0.1 && scaleOffset < 0 || originScale >= 1.5 && scaleOffset > 0) {
                return;
            }
            var newScale = (originScale * 10 + scaleOffset * 10) / 10;
            newScale = newScale > 1.5 ? 1.5 : (newScale < 0.1 ? 0.1 : newScale);
            if (newScale == originScale) {
                return;
            }
            var canvasOffset = this.$canvas.position(); //position 取相对父元素的坐标         offset取相对页面左上角的坐标
            var canvasRect = {
                left : canvasOffset.left, // 元素相对位置左上
                top : canvasOffset.top,
                width : this.$canvas.width(), //元素实际长宽
                height : this.$canvas.height()
            };

            if (scaleCenterPos == undefined || scaleCenterPos == null) {
                scaleCenterPos = {
                    'clientX' : canvasRect.left + canvasRect.width / 2,
                    'clientY' : canvasRect.top + canvasRect.height / 2,
                    'offsetX' : canvasRect.width / 2,
                    'offsetY' : canvasRect.height / 2,
                };
            }
            if (newScale < originScale) {
                var newCanvasRect = {
                    left : canvasRect.left + scaleCenterPos.offsetX * (originScale - newScale) / originScale,
                    top : canvasRect.top + scaleCenterPos.offsetY * (originScale - newScale) / originScale,
                    width : canvasRect.width * newScale /originScale,
                    height : canvasRect.height * newScale / originScale
                }
                // 当图片移出操作区域时，定位到区域中间位置
                if (newCanvasRect.left >= this.$container.width() || newCanvasRect.top >= this.$container.height()
                    || newCanvasRect.left + newCanvasRect.width <= 0 || newCanvasRect.top + newCanvasRect.height <= 0) {
                    canvasRect.left = (this.$container.width() - canvasRect.width) / 2;
                    canvasRect.top = (this.$container.height() - canvasRect.height) / 2;
                }
            }
            canvasRect = {
                left : canvasRect.left + scaleCenterPos.offsetX * (originScale - newScale) / originScale,
                top : canvasRect.top + scaleCenterPos.offsetY * (originScale - newScale) / originScale,
                width : this.canvasAttr.attrWidth * newScale,
                height : this.canvasAttr.attrHeight * newScale
            };

            // 1.  旋转90后获取的长宽为实际占用的长宽，与之前设置的css中刚好相反
            // 2.  旋转后设置left top会默认为旋转前的left,top, css rotate() 为中心旋转，故移动中心坐标
            var isRotate90 = (new Number($(this.$container).attr('img-rotate')) - 90 ) % 180 == 0;
            if (isRotate90) {
                canvasRect.left += (canvasRect.height - canvasRect.width) / 2;
                canvasRect.top += (canvasRect.width - canvasRect.height) / 2;
            }
            $.extend(this.canvasAttr, canvasRect, {
                'scale' : newScale
            });

            $(this.$canvas).attr('scale', newScale);
            this.$img.css({
                'left' : canvasRect.left + 'px',
                'top' : canvasRect.top + 'px',
                'width' : canvasRect.width + 'px',
                'height' : canvasRect.height + 'px'
            });
            this.$canvas.css({
                'left' : canvasRect.left + 'px',
                'top' : canvasRect.top + 'px',
                'width' : canvasRect.width + 'px',
                'height' : canvasRect.height + 'px'
            });
        };
        this.drawImage = function(img, $container) { //canvas绘图
            //* img 图片元素		* $container 容器jquery对象		* rotate 旋转角度
            var _this = this;

            var imageRealSize = { 'width' : img.width, 'height' : img.height };
            var containerSize = { 'width' : $container.width(), 'height' : $container.height() };
            var canvasRect = _this.recalculateCanvasRect(imageRealSize.width, imageRealSize.height, containerSize.width,containerSize.height);
            _this.$canvas.attr({
                'width': canvasRect.attrWidth,
                'height': canvasRect.attrHeight,//原始图片大小
                'scale' : canvasRect.scale,
                'img-rotate' : 0
            });
            $.extend(_this.canvasAttr, canvasRect);
            _this.resetBrush();
            var attrs = {
                'image' : img,
                'imageStartX' : 0, 'imageStartY' : 0, 'imageWidth' : img.width, 'imageHeight' : img.height,
                'startX' : 0, 'startY' : 0, 'width' : img.width, 'height' : img.height
            };
            DrawingMethod.drawImage(_this.ctx, attrs);

            _this.$img.position(canvasRect);
            _this.$canvas.position(canvasRect);
            _this.$canvas.css({
                'width': canvasRect.width + 'px',
                'height': canvasRect.height + 'px'
            });
        };
        this.recalculateCanvasRect = function(imgWidth, imgHeight, containerWidth, containerHeight) { // 重新计算大小
            var canvasRect = {};
            var scale = Math.min(containerWidth / imgWidth, containerHeight / imgHeight);
            scale = scale > 1 ? 1 : (scale < 0.1 ? 0.1 : Math.floor(scale * 10) / 10);
            canvasRect.width = imgWidth * scale;
            canvasRect.height = imgHeight * scale;
            canvasRect.attrWidth = imgWidth;
            canvasRect.attrHeight = imgHeight;
            canvasRect.scale = scale;
            canvasRect.left = (containerWidth - canvasRect.width) / 2;
            canvasRect.top = (containerHeight - canvasRect.height) / 2;
            return canvasRect;
        };
        this.clearCanvas = function() { // 清空画布
            this.$canvas.attr('width', this.$canvas.attr('width'));
            this.$canvas.attr('height', this.$canvas.attr('height'));
            this.resetBrush();
            if (this.imgObj.markLayerUrl == '' && this.imgObj.markLayerBase64 == '') {
                // if (this.$container.attr('img-rotate') == '0') {
                this.modify = false;
                // }
            } else {
                this.modify = true;
            }
        };
        this.setColor = function(color) {  //设置颜色
            this.brushAttr.strokeStyle = color;
            this.resetBrush();
        };
        this.setBrushWidth = function(brushWidth) { //设置笔刷大小
            this.brushAttr.lineWidth = '' + brushWidth;
            this.resetBrush();
        };
        this.getBrushWidth = function() { //获取笔刷大小
            return new Number(this.brushAttr.lineWidth);
        };
        this.resetBrush = function() {//重置画笔数据
            this.ctx = this.$canvas[0].getContext('2d');
            $.extend(this.ctx, this.brushAttr);
        };
        this.getPosition = function(e){//鼠标位置信息 + 画布信息
            var scale = this.canvasAttr.scale;
            return $.extend({}, this.canvasAttr, {
                clientX : e.clientX,
                clientY : e.clientY,
                offsetX : e.offsetX / scale,
                offsetY : e.offsetY / scale
            });
        };
        this.getTouchPosition = function(e){//触摸 位置信息 + 画布信息
            var touchPos = e.originalEvent.changedTouches[0];
            var scale = this.canvasAttr.scale;
            return $.extend({}, this.canvasAttr, {
                clientX : touchPos.clientX,
                clientY : touchPos.clientY,
                offsetX : (touchPos.clientX - this.canvasAttr.containerLeft - this.canvasAttr.left - this.canvasAttr.borderWidth) / scale,
                offsetY : (touchPos.clientY - this.canvasAttr.containerTop - this.canvasAttr.top - this.canvasAttr.borderWidth) /scale
            });
        };
    };

    /** 绘图方法 */
    DrawingMethod = {
        drawLine: function(ctx, attrs) { // 画线段
            ctx.beginPath();
            ctx.moveTo(attrs.startX, attrs.startY);
            ctx.lineTo(attrs.endX, attrs.endY);
            ctx.stroke();
        },
        drawRectangle : function(ctx, attrs) {// 画矩形
            ctx.strokeRect(attrs.startX, attrs.startY, attrs.width, attrs.height);
        },
        drawText: function(ctx, attrs) { // 画文字信息
            ctx.fillText(attrs.text, attrs.startX, attrs.startY);
        },
        drawImage: function(ctx, attrs) { // 画图片信息
            ctx.drawImage(attrs.image,
                attrs.imageStartX, attrs.imageStartY, attrs.imageWidth, attrs.imageHeight,
                attrs.startX, attrs.startY, attrs.width, attrs.height);
        },
        recalCulateAttr: function(tool, startOffsetX, startOffsetY, endOffsetX, endOffsetY, canvasLeft, canvasTop) {
            var attrs = {
                startX: startOffsetX,
                startY: startOffsetY
            };
            var width = endOffsetX - startOffsetX;
            var height = endOffsetY - startOffsetY;
            switch (tool) {
                case 'line': //直线
                    attrs.endX = endOffsetX;
                    attrs.endY = endOffsetY;
                    break;
                case 'rectangle': //矩形
                    attrs.width = width;
                    attrs.height = height;
                    break;
            }
            return attrs;
        }
    }

    /** 绘图轨迹  (初始化与原始画布大小相同的临时画布作为轨迹画布) */
    var DrawingTrack = function(brushAttr, position) {
        this.$obj = $('<canvas class="drawing_track" style="position: absolute; cursor: crosshair;"></canvas>');
        this.brushAttr = brushAttr;
        this.offset = $.extend({}, position);
        this.$obj.attr({ 'width' : this.offset.attrWidth, 'height' : this.offset.attrHeight, 'scale' : this.offset.scale })
            .css({ 'width' : this.offset.width + 'px', 'height' : this.offset.height + 'px', 'left' : this.offset.left, 'top' : this.offset.top });

        this.ctx = this.$obj[0].getContext('2d');
        $.extend(this.ctx, this.brushAttr);

        this.resetEndPoint = function(endOffsetX, endOffsetY, tool){//重置结束点
            this.clearCanvas(tool);//清空画布
            $.extend(this.offset, {
                'endOffsetX' : endOffsetX,
                'endOffsetY' : endOffsetY
            });
            if (tool == undefined) {
                return;
            }
            // 获取绘图属性(参数：工具名称、开始位置、结束位置、)
            var attrs = DrawingMethod.recalCulateAttr(tool,
                this.offset.offsetX, this.offset.offsetY, endOffsetX, endOffsetY,
                this.offset.left, this.offset.top);
            $.extend(this, attrs);
            var word = tool.charAt(0).toUpperCase() + tool.substring(1),
                redrawMethod = 'draw' + word;
            DrawingMethod[redrawMethod](this.ctx, attrs);
        };
        this.clearCanvas = function(tool) {//清除绘画区域
            //this.$obj.attr('height', this.offset.canvasHeight);//方法一： 修改画布高度清空
            //this.ctx.clearRect(0, 0, this.offset.canvasWidth, this.canvasHeight); //方法二： 矩形清除画布所有内容
            // 有endClientX说明鼠标发生移动，已经绘过图形
            if (this.offset.endOffsetX == undefined) {
                return;
            }
            this.ctx.clearRect(
                Math.min(this.offset.endOffsetX, this.offset.offsetX) - this.brushAttr.lineWidth,
                Math.min(this.offset.endOffsetY, this.offset.offsetY) - this.brushAttr.lineWidth,
                Math.abs(this.offset.endOffsetX - this.offset.offsetX) + 2 * this.brushAttr.lineWidth,
                Math.abs(this.offset.endOffsetY - this.offset.offsetY) + 2 * this.brushAttr.lineWidth);
        };
        this.destroy = function() {
            this.$obj.remove();
            delete this;
        };
    }

    /** 工具类 */
    var Utils = {
        onMouseWheel : function($ele, doFunc) { // 监听元素鼠标滚轮滚动事件
            $ele.on('mousewheel', function(e) { // 其他通用
                var direct = e.originalEvent.wheelDelta > 0 ? 'up' : 'down';
                doFunc.call(this, e, direct);
            });
            $ele[0].addEventListener('DOMMouseScroll', function() { // 火狐
                var direct = e.originalEvent.wheelDelta > 0 ? 'up' : 'down';
                doFunc.call(this, e, direct);
            })
        },
        formatStr: function(template) { //替换字符串中的占位符
            var args = Array.prototype.slice.apply(arguments);
            args.splice(0, 1);
            if (args.length == 1 && typeof(args[0]) == 'object') {
                args = args[0];
            }
            if (args == null) {
                template = template.replace(/\{[^\}]*\}/g, '');
            } else {
                $.each(args, function(key, value) {
                    template = template.replace(eval("/({)" + key + "(})/g"), value == null ? '' : value);
                });
            }
            return template;
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
        }
    }
})();
