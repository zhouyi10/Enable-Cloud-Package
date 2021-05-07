/**
 * 嵌入批阅插件
 */
(function(CanvasUtils){
	CanvasUtils.markModule = {
		STATUS_MARKED : '1', // 已批阅
		STATUS_UNMARKED : '0', // 未批阅
		init : function(){
			var globalParam = CanvasUtils.options.param;
			// 初始化html
			$(CanvasUtils.container).append('<div class="module mark_module"><input autofocus="autofocus" /><div class="canvas_mark_btn"><i class="right"></i><i class="wrong"></i></div></div>');
			// input 框设置值
			if (globalParam.score != undefined && globalParam.score != null) {
				globalParam.score = 0;
			}
			$('.mark_module input').val(globalParam.score).data('score', globalParam.score);
			// 初始化对错状态
			this.initStatus();
			// 初始化事件
			this.initEvent();
		},
		initEvent : function(){
			var _this = this, globalParam = CanvasUtils.options.param;
			$('.mark_module input').on('input propertychange', function(){ //input 输入框值改变事件
				var score = null;
				if ($(this).val() == '') {
					score = 0;
				} else if (!/^(0|[1-9][0-9]{0,2})(\.\d{0,2})?$/.test($(this).val())) {
					$(this).val($(this).data('score'));
                	return ;
            	} else {
            		score = parseFloat($(this).val())
            	}
				if (score > globalParam.totalScore) {
					score = globalParam.totalScore;
					$(this).val(score);
				}
				globalParam.markStatus = _this.STATUS_MARKED;
				globalParam.score = score;
				$(this).data('score', score);
				_this.initStatus();
			});
			$('.mark_module i').on('click', function(){ //批阅按钮
				if ($(this).hasClass('on')) { //重复点击
					return ;
				}
				if ($(this).hasClass('right')) { //正确
					globalParam.answerStatus = 'right';
					globalParam.score = globalParam.totalScore;
				} else { //错误
					globalParam.answerStatus = 'wrong';
					globalParam.score = 0;
				}
				globalParam.markStatus = _this.STATUS_MARKED; //已批阅
				$('.mark_module input').val(globalParam.score); // 设置分数
				$(this).siblings().removeClass('h'); //添加样式
				$(this).addClass('h');
			});
		},
		initStatus : function() { //初始化批阅按钮状态
			var globalParam = CanvasUtils.options.param;
			$('.mark_module i').removeClass('h');
			if (this.STATUS_MARKED == globalParam.markStatus) {
				if (parseFloat($('.mark_module input').val()) >= globalParam.totalScore * globalParam.thresholdScore) {
					$('.mark_module i.right').addClass('h');
					globalParam.answerStatus = 'right';
				} else {
					$('.mark_module i.wrong').addClass('h');
					globalParam.answerStatus = 'wrong';
				}
			}
		},
		destroy : function(){ //插件销毁
			$(CanvasUtils.container + ' .mark_module').remove();
		}
	}
})(window.CanvasUtils);