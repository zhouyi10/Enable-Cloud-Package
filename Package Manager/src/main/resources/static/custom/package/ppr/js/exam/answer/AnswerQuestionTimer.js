(function($){
	// 保存 当前题目对象及该题目开始作答时间
	var QuesCostTime = {
		param : null,
		startTime : null
	}
	
	// 单个题目耗时计算
	var AnswerQuestionTimer = window.AnswerQuestionTimer = {
		endCallback : function(param, totalMilliSecond) {
			
		},
		init : function(endCallback) {
			this.endCallback = endCallback;
		},
		start : function(param) {
			var curTime = new Date().getTime();
			if (QuesCostTime.param != null && QuesCostTime.param != undefined) {
				this.endCallback(QuesCostTime.param, QuesCostTime.startTime, curTime);
			}
			QuesCostTime.param = param;
			QuesCostTime.startTime = curTime;
		},
		end : function() {
			if (QuesCostTime.startTime != null) {
				var curTime = new Date().getTime();
				this.endCallback(QuesCostTime.param, QuesCostTime.startTime, curTime);
			}
			QuesCostTime.param = null;
			QuesCostTime.startTime = null;
		}
	}
	
	// param 传入为 index
	AnswerQuestionTimer.init(function($ques, startTime, endTime) {
		var totalMilliSecond = endTime - startTime;
		if ($ques != null && $ques.length > 0) {
			var data = $ques.data('questionPanel');
			// 3. 答题耗费时间
			var costTime = Math.ceil(totalMilliSecond / 1000);
			if (data.questionNode.userAnswer.answerCostTime != "") {
				costTime = costTime + parseInt(data.questionNode.userAnswer.answerCostTime);
			}
			data.questionNode.userAnswer.answerCostTime = "" + costTime;
			data.questionNode.userAnswer.answerStamp.push("" + startTime + ":" + totalMilliSecond);
			
			if (data.questionNode.userAnswer.childAnswerArr != null && data.questionNode.userAnswer.childAnswerArr.length > 0) {
				var childCostTime = Math.floor(costTime / data.questionNode.userAnswer.childAnswerArr.length);
				var s = startTime, costMiles = Math.floor(totalMilliSecond / data.questionNode.userAnswer.childAnswerArr.length);
				$.each(data.questionNode.userAnswer.childAnswerArr, function(c, child) {
					child.answerCostTime = childCostTime;
					child.answerStamp.push("" + s + ":" + costMiles);
					s += costMiles;
				});
			}
			$ques.data('questionPanel', data);
		}
	});
	
})(jQuery);
