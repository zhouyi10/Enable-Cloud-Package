/**
 * 嵌入批阅插件
 */
(function(CanvasUtils){
    CanvasUtils.backgroundModule = {
        useBack: true,
        left: 0,
        top: 0,
        width: 0,
        height: 0,
        canvas: null,
        init : function(){
            // 初始化html
            $(CanvasUtils.container).find('.op_btn').prepend('<span class="ques_background"><input type="checkbox" checked="checked" /><span style="color: white;">题目背景</span></span>');
            this.initEvent();
            this.initQuesCanvasInfo();
        },
        initQuesCanvasInfo: function() {
            var _this = this, $canvasArea = CanvasUtils.options.$canvasArea;
            if ($canvasArea == null || $canvasArea == undefined) {
                return;
            }
            var $quesArea = $canvasArea.closest('.question_area');
            var offset = $quesArea.offset(), canvasOffset = $(CanvasUtils.container).find('canvas').offset();
            this.left = offset.left - canvasOffset.left;
            this.top = offset.top - canvasOffset.top;
            this.width = $quesArea.width();
            this.height = $quesArea.height();
            html2canvas($quesArea, {
                onrendered: function(canvas) {
                    _this.canvas = canvas;
                    var ctx = _this.canvas.getContext('2d');
                    var host = window.location.host;
                    ctx.save();
                    ctx.lineWidth = 1;
                    ctx.strokeStyle = '#b5b5b5';
                    $quesArea.find('img').each(function(i, img) {
                        if (img.src && img.src.indexOf('data')!= 0 && img.src.indexOf(host) < 0) {
                            var imgOffset = $(this).offset();
                            ctx.strokeRect(imgOffset.left - offset.left, imgOffset.top - offset.top, $(this).width(), $(this).height());
                        }
                    });
                    ctx.restore();
                },
                width: this.width,
                height: this.height
            });
        },
        initEvent : function(){
            var _this = this;
            _this.useBack = $(CanvasUtils.container).find('.op_btn .ques_background input').is(':checked');
            $(CanvasUtils.container).find('.op_btn .ques_background input').on('change', function() {
                _this.useBack = $(this).is(':checked');
            });
        },
        beforeSaveImg: function(imgObj, ctx) {
            var $canvas = $(CanvasUtils.container).find('canvas');
            if (this.useBack &&  imgObj.quesBack != true) {
                ctx.save();
                ctx.globalCompositeOperation = 'destination-over';
                ctx.drawImage(this.canvas,0, 0, this.width, this.height, this.left, this.top, this.width, this.height);
                ctx.restore();
                ctx.save();
                ctx.fillStyle = '#ffffff';
                ctx.globalCompositeOperation = 'destination-over';
                ctx.fillRect(0, 0, $canvas.attr('width'), $canvas.attr('height'));
                ctx.restore();
                imgObj.quesBack = true;
            }
        },
        beforeClear: function(imgObj, ctx) {
            imgObj.quesBack = false;
        },
        destroy : function(){ //插件销毁
            $(CanvasUtils.container + ' .op_btn .ques_background').remove();
            this.left= 0, this.top = 0, this.width = 0, this.height = 0, this.canvas = null;
        }
    }
})(window.CanvasUtils);