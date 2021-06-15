(function($,window) {
	/** 主逻辑 */
	var AnswerQuestion = window.AnswerQuestion = {
			mySwiper : null,
			userId : "",
			examId : "",
		    stepId : "",
			testId : "",
			getExamUrl : "",
			getTimeUrl : "",
			startTestTime : null,//default from background
			paperInfo : null,
			init : function(){
				this.initPaper();
				this.renderPage();
				ListenAudioV2.init();
				this.initSwiper();
				this.initEvent();
				PaperTestTimer.init(this.testInfo, this.getTimeUrl);
			},
			//获取测验数据
			initPaper : function(){
				var _this = this;
				$.ajax({
					url : _this.getExamUrl,
					type : "get",
					data : {examId : _this.examId},
					async : false,
					success : function(data){
						if (data == null || data == "") {
							_this.paperInfo = null;
						} else {
							_this.paperInfo = data.data;
						}
					}
				});
			},
			//渲染页面
			renderPage : function(){
				var _this = this;
				if(_this.paperInfo == null) {
					return ;
				}
				$("#title").text(_this.paperInfo.name);
				if (!CommUtils.isEmpty(_this.paperInfo.nodes)) {
					var quesNo = 0; //题目序号，，第几个题目
					$.each(_this.paperInfo.nodes, function(n, node){
						if (node.level == 1 || node.level == 2) {
							if (!_this.isMobile && !CommUtils.isEmpty(node.name)) { //当不是手机页面时，导航栏添加题型信息
								$("#testCard").append(CommUtils.formatStr('<li level="{1}">{0}</li>', CommUtils.ifEmpty(node.name), node.level));
							}
						} else if (node.level == 3) {
							// if (CommUtils.isEmpty(node.question.affixId)) { //处理听力文件信息
							// 	if (ListenAudio.config.firstNoAffixQuesIndex == -1) {
							// 		ListenAudio.config.firstNoAffixQuesIndex = quesNo;
							// 	}
							// } else {
							// 	ListenAudio.config.questionAffix.push([quesNo, node.question.affixId]);
							// }
							$("#questionSwiper").append("<div class=\"swiper-slide\"></div>");
							$("#questionSwiper > .swiper-slide:last").questionPanel({'questionNode' : node, 'isMobile' : _this.isMobile});
							quesNo++;
						}
					});
				}
			},
			//初始化滑动区域
			initSwiper : function(){
				var _this = this;
				_this.mySwiper = new Swiper('#swiperList>.swiper-container', {
		      		pagination: {
				        el: '.swiper-pagination',
				        type: 'fraction',
			      	},
		      		//autoHeight: true,
		      		navigation: {
				        nextEl: '.test_next',
				        prevEl: '.test_prev',
			      	},
					noSwiping: true,
					noSwipingClass: 'swiper-no-swiping',
					noSwipingSelector: _this.isMobile ? '.answer.collectline > .show , .question_blank_image_analysis.answer.option > span > img' : '.answer>input, .answer>textarea, .answer.selectBox > li, .answer.collectline > .show, .question_blank_image_option > span > img',
			      	on: {
			      		init : function(){
			      			var $thisIndex = this.activeIndex * 1 + 1;
							// if (ListenAudio.config.questionAffix.length > 0 && ListenAudio.config.questionAffix[0][0] != $thisIndex) {
							// 	this.slideTo(ListenAudio.config.questionAffix[0][0], 1000, true);
							// 	$thisIndex = ListenAudio.config.questionAffix[0][0];
							// }

			      			var $swiperSlide = $("#swiperList .swiper-slide").length;
			      			$(".test_tips .test_number > b").html($thisIndex);
			      			$(".test_tips .test_total > b").html($swiperSlide);
			      			var $curQues = $("#swiperList .swiper-slide:eq(" + this.activeIndex +")");
			      			AnswerQuestionTimer.start($curQues);
							ListenAudioV2.switch($curQues.data('questionPanel').questionNode.question.affixId);
							if (!_this.isMobile) { //网页端设置当前题目导航背景色
								$("#testCard li[data-key='" + $curQues.data('questionPanel').questionNode.question.questionId + "']").addClass("active_li");
							}
			      		},
					    slideChangeTransitionEnd : function(event){
					    	var $thisIndex = this.activeIndex * 1 + 1;
					      	$(".test_tips .test_number > b").html($thisIndex);
							var $curQues = $("#swiperList .swiper-slide:eq(" + this.activeIndex +")");
			      			AnswerQuestionTimer.start($curQues);
							ListenAudioV2.switch($curQues.data('questionPanel').questionNode.question.affixId);
							var id = $("#swiperList .swiper-slide:eq(" + this.activeIndex +") .answer:first").attr("data-key");
			      			if (!_this.isMobile) {//网页端设置当前题目导航背景色
								var quesLi = $("#testCard div[data-key='" + id + "']").closest('li');
								quesLi.siblings().removeClass('active_li');
								quesLi.addClass('active_li');
								var quesTop = quesLi.offset().top, quesBottom = quesTop + quesLi.height();
								var navTop = $("#testCard").offset().top, navBottom = navTop + $("#testCard").height();
								if (quesTop < navTop || quesBottom > navBottom) {  //若当前题目导航未显示，滚动到当前题目
									$("#testCard").scrollTop(0);
									var scrollTop = quesLi.offset().top - $("#testCard").offset().top;
									$("#testCard").scrollTop(scrollTop);
								}
							}
					    },
						"touchStart" : function(e) {// 分屏后,拖动改变答题及题目区域宽度
							if ($(e.target).hasClass("splitor") && $(e.target).prev('.question_content').length > 0) {
								$(e.target).siblings().addClass("text_no_select").attr("unselectable", "on"); //题目和答案区域不能选择
								AnswerQuestion.mySwiper.allowSlidePrev = false; // 不能向前滚动
								AnswerQuestion.mySwiper.allowSlideNext = false; // 不能向后滚动
								var data = {
									"lastX" : e.pageX,
									"drag" : true,
									"curSplitor" : $(e.target)
								}
								data.totleWidth = $(e.target).parent().width();
								data.rangeLeft = $(e.target).parent().offset().left + data.totleWidth * 0.25;
								data.rangeRight = $(e.target).parent().offset().left + data.totleWidth * 0.75;

								$("#swiperList").data(data);
								return false;
							}
						},
						"touchMove" : function(e){// 分屏后,拖动改变答题及题目区域宽度
							if($("#swiperList").data('drag')){
								var data = $("#swiperList").data();
								var moveX = e.pageX - data.lastX;
								var preWidth = $(data.curSplitor).prev().width();
								var nextWidth = $(data.curSplitor).next().width();
								if(e.pageX > data.rangeLeft && e.pageX < data.rangeRight){
									$(data.curSplitor).prev().width(((preWidth+moveX)/data.totleWidth)*100+"%");
									$(data.curSplitor).next().width(((nextWidth-moveX)/data.totleWidth)*100+"%");
									data.lastX = e.pageX;
								}
								$("#swiperList").data(data);
								return false;
							}
						},
						"touchEnd" : function(){// 分屏后,拖动改变答题及题目区域宽度
							if($("#swiperList").data('drag')) {
								$("#swiperList").data('drag', false);
								var curSplitor = $("#swiperList").data('curSplitor');
								var prevWidth = $(curSplitor).prev().width(), nextWidth = $(curSplitor).next().width();
								$("#swiperList .question_area .splitor").each(function(){
									$(this).prev().width(prevWidth);
									$(this).next().width(nextWidth);
								});
								//恢复可选择
								$($("#swiperList").data(curSplitor)).siblings().removeClass("text_no_select").removeAttr("unselectable", "on");
								// 恢复向前向后滚动
								AnswerQuestion.mySwiper.allowSlidePrev = true;
								AnswerQuestion.mySwiper.allowSlideNext = true;
							}
						}
				  	}
			    });
			},
			initEvent : function() {
				var _this = this;
				if (_this.isMobile) {
					//点击右上角答题卡图标显示答题卡(web端不显示)
					$(".testCard > i").bind("click",function(){
						if ($(".testCardLists").is(":hidden")) {
							$(".testCardLists").slideDown();
						} else{
							$(".testCardLists").slideUp();
						}
					});
					//答题卡导航栏每一道题目点击事件
					$("#testCard > li").on("click", function () { //手机端打开时隐藏答题栏
						$(".testCardLists").slideUp();
						var index = $(".answer[data-key='" + $(this).attr("data-key") + "']").closest(".swiper-slide").index();
						if (_this.mySwiper != null) {
							_this.mySwiper.slideTo(index, 200, true);
						}
					});
					$(".backBtn").on("click", function(){
						_this.confirmBox(i18n["confirm_return_without_submit"], function(){//'试卷信息未提交,确定返回？'
							if(typeof(WeixinJSBridge)!="undefined"){
								window.history.go(-1);
							} else {
								window.close();
							}
						});
					});
				} else { //web端
					$("#testCard > li").on("click", function () {
						//web端li节点为题目，div为作答题目
						var index = 0;
						if ('1' == $(this).attr('level') || '2' == $(this).attr('level')) { //处理卷别、题型
							var tmp = $(this);
							while (tmp.next().length > 0) {
								tmp = tmp.next();
								if (tmp.attr('data-key') != undefined && tmp.attr('data-key') != '') {
									index = $(".answer[data-key='" + tmp.find('div:first').attr("data-key") + "']").closest(".swiper-slide").index();
									break;
								}
							}
						} else {
							index = $(".answer[data-key='" + $(this).find('div:first').attr("data-key") + "']").closest(".swiper-slide").index();
						}
						if (_this.mySwiper != null) {
							_this.mySwiper.slideTo(index, 200, true);
						}
					});
					$(".screen_splitor").on("click", function(){
						$(this).toggleClass("active_li");
						if ($(this).hasClass("active_li")) {
							$("#swiperList").addClass("splite");
							$("#swiperList .question_area .question_content").each(function(){
								$(this).after("<div class='splitor'></div>");
							});
						} else {
							$("#swiperList").removeClass("splite");
							$("#swiperList .question_area .splitor").remove();
							$("#swiperList .question_area > *").removeAttr("style");
						}
					});
					$(document.body).on('keydown', function (e) {
						var key = e.key + "";
						if (key.toUpperCase() == 'TAB') {
							return false;
						}
					})
				}
			},
			//保存试卷
			savePaper : function(){
				var _this = this;
				if(this.paperInfo == null) {
					return ;
				}
				PaperTestTimer.endAnswer();
				_this.confirmBox(i18n['confirm_submit']  , function(){//确定交卷？
					_this.doSave();
				});
			},
			doSave: function() {
				var _this = this;
				AnswerQuestionTimer.end();
				CommUtils.tryLock($(document.body));
				var answerXml = _this.joinAnswerXml();
				var loading = CommUtils.showMask();
				var param = {
					'answers' : answerXml,
					'testId': _this.testId,
					'paperId' : _this.examId,
					'userId' : _this.userId,
					'stepId' : _this.stepId,
					'fileId' : _this.fileId,
					'startTime': PaperTestTimer.startTestTimeStr,
					'endTime': PaperTestTimer.endAnswerTimeStr
				};
				$.ajax({
					type:'post',
					contentType: 'application/json',
					data:JSON.stringify(param),
					dataType : "json",
					url: _this.saveUrl,
					success:function(data){
						if(data.status == "0" || !data.data){
							if (!CommUtils.isEmpty(data.message)) {
								CommUtils.tipBoxV2(data.message, 3000, function(){

								});
							} else {
								CommUtils.tipBoxV2(i18n['submit_failed'], 3000, function(){

								});
							}
						}else{
							// CommUtils.tipBoxV2(i18n['submit_success'], 3000, function(){
							// 	data.status = "success";
							// 	data.type = "answer";
							// 	window.opener && window.opener.postMessage(data, "*");
							// 	window.parent.postMessage(data, "*");
							// });
							data.status = "success";
							data.type = "answer";
							window.opener && window.opener.postMessage(data, "*");
							window.parent.postMessage(data, "*");
						}
					},
					error: function (returndata) {
						CommUtils.closeMask();
						CommUtils.unLock($(document.body));
						//_this.tipBox(i18n['submit_failed']);//交卷失败
						CommUtils.tipBoxV2(i18n['submit_failed'], 3000, function(){

						});
					}
				});
			},
			//拼接答案xml
			joinAnswerXml : function(){
				var answerArr = [];
				var score = 0;
				$(".swiper-slide").each(function(){
					answerArr = answerArr.concat($(this).questionPanel("getAnswerXml"));
				});
				return answerArr;
			},
			confirmBox : function(content, callback) {
				layer.open({
					'content' : content,
					btn : [ i18n["confirm"], i18n["cancel"] ],
					title: i18n['alert_title'],
					yes : function(index) {
						layer.close(index);
						callback && callback();
					}
				});
			},
			tipBox : function(content, callback, showClose) {
				layer.open({
					'content' : content,
					closeBtn: showClose ? 0 : 1,
					btn : i18n["confirm"],
					title: i18n['alert_title'],
					yes : function(index) {
						layer.close(index);
						callback && callback();
					}
				});
			}
	};

	var PaperTestTimer = {//考试计时
		timer : null,
		getTimeUrl: '',
		startTestTimeStr: '',
		startTestTime: null,
		endAnswerTime: null,
		endAnswerTimeStr: '',
		delaySubmitMinute: -1,
		testBeginTime: null,
		testEndTime: null,
		curTime: null,
		init : function(testInfo, getTimeUrl) {
			// if ($('.timer_area').length == 0) { //手机端无计时
			// 	return;
			// }
			this.getTimeUrl = getTimeUrl;
			this.initTime(testInfo);
			var _this = this;
			this.requestTime(function() {
				_this.initSyncTimer();
				_this.initTimerArea();
			});
		},
		initTime: function(testInfo) {
			var _this = this;
			this.testBeginTimeStr = CommUtils.ifEmpty(testInfo.beginTime, testInfo.startTime);
			if (!CommUtils.isEmpty(this.testBeginTimeStr)) {
				this.testBeginTime = new Date(this.testBeginTimeStr.replace(/-/g,"/"));
			}
			this.testEndTimeStr = testInfo.endTime;
			if (!CommUtils.isEmpty(this.testEndTimeStr)) {
				this.testEndTime = new Date(this.testEndTimeStr.replace(/-/g,"/"));
			}
			this.delaySubmitMinute = CommUtils.ifEmpty(testInfo.delaySubmit, -1);
		},
		initSyncTimer: function() {
			var _this = this;
			// sync time from server
			_this.syncTimer = setInterval(function() {
				_this.requestTime();
			}, 60000);
			// time auto increment
			_this.nowTimer = setInterval(function(){
				_this.refreshCurTime();
			}, 1000);
		},
		requestTime: function(callback) {
			var _this = this;
			$.ajax({
				url: _this.getTimeUrl,
				success: function(result) {
					var curTimeStr = CommUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
					if (result.status == 1) {
						curTimeStr = result.data;
					}
					_this.syncTime(curTimeStr);
					callback && callback();
				},
				error: function() {
					var curTimeStr = CommUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
					_this.syncTime(curTimeStr);
					callback && callback();
				}
			});
		},
		syncTime: function(curTimeStr) {
			var _this = this;
			if (CommUtils.isEmpty(_this.startTestTimeStr)) {
				_this.startTestTimeStr = curTimeStr;
				_this.startTestTime = new Date(curTimeStr.replace(/-/g,"/"));
			}
			_this.refreshCurTime(curTimeStr);
		},
		initTimerArea: function() {
			var _this = this;
			_this.timer = setInterval(function() {
				var currentTime = _this.getCurTime();
				if (_this.curTime == null) { return; }
				currentTime.setSeconds(currentTime.getSeconds() + 1);
				if (_this.testEndTime == null) {
					var seconds = parseInt(currentTime - _this.startTestTime);
					var timeStr = _this.calculateTimeStr(seconds);
					$('.timer_area').html("<b>" + timeStr + "</b>");
					return;
				}
				if (currentTime <= _this.testEndTime) { //in normal time
					var seconds = parseInt((_this.testEndTime - currentTime)/1000);
					if (seconds == 300) {
						AnswerQuestion.tipBox(CommUtils.formatStr(i18n['submit_last_minute_tip'], '5'));
					} else if (seconds == 180) {
						AnswerQuestion.tipBox(CommUtils.formatStr(i18n['submit_last_minute_tip'], '3'));
					} else if (seconds == 60) {
						AnswerQuestion.tipBox(CommUtils.formatStr(i18n['submit_last_minute_tip'], '1'));
					}
					var timeStr = _this.calculateTimeStr(seconds);
					$('.timer_area').html("<b>" + timeStr + "</b>");
				} else if (currentTime > _this.testEndTime) { // in delay time
					if (_this.startTestTime > _this.testEndTime) {
						var seconds = parseInt((currentTime - _this.startTestTime)/1000);
						var timeStr = _this.calculateTimeStr(seconds);
						$('.timer_area').html("<b>" + timeStr + "</b>");
					} else {
						var seconds = parseInt((currentTime - _this.testEndTime)/1000);
						var timeStr = _this.calculateTimeStr(seconds);
						$('.timer_area').html("<span>" + i18n['delay_submit'] + "</span><b>" + timeStr + "</b>");
					}
				}
				if (_this.delaySubmitMinute != -1) {
					if (currentTime - _this.testEndTime == _this.delaySubmitMinute * 60 * 1000) {
						// tip force submit
						_this.forceCommit();
					}
				}
			}, 1000);
		},
		opTimeLock: false,
		refreshCurTime: function(timeStr) {
			while(this.opTimeLock) {}
			this.opTimeLock = true;
			if (timeStr == undefined) {
				this.curTime.setSeconds(this.curTime.getSeconds() + 1);
			} else {
				this.curTime = new Date(timeStr.replace(/-/g,"/"));
			}
			this.opTimeLock = false;
		},
		getCurTime: function() {
			if (this.curTime == null) return null;

			while(this.opTimeLock) {}
			this.opTimeLock = true;
			var currentTime = new Date(this.curTime);
			this.opTimeLock = false;
			return currentTime;
		},
		endAnswer: function() {
			this.endAnswerTime = this.getCurTime();
			this.endAnswerTime.setSeconds(this.endAnswerTime.getSeconds() - 10); //不要卡点
			if (this.endAnswerTime <= this.startTestTime) {
				this.endAnswerTime = new Date(this.startTestTime);
				this.endAnswerTime.setSeconds(this.endAnswerTime.getSeconds() + 1); //不要卡点
			}
			this.endAnswerTimeStr = CommUtils.formatDate(this.endAnswerTime, "yyyy-MM-dd HH:mm:ss");
		},
		forceCommitDialog: null,
		submitLastSecondIntervalsubmitLastSecondInterval : null,
		forceCommit: function() {
			var _this = this;
			clearInterval(_this.timer);
			clearInterval(_this.syncTimer);
			_this.endAnswer();
			AnswerQuestionTimer.end();
			CommUtils.showMask();
			var total = 15;
			_this.forceCommitDialog = layer.open({
				'id': 'auto_force_commit',
				'content' : CommUtils.formatStr(i18n['auto_submit_last_second_tip'], total),
				closeBtn: 0,
				btn : [ i18n['immediately_hand_paper'] ]
				,yes : function () {
					clearInterval(_this.submitLastSecondInterval);
					layer.close(_this.forceCommitDialog);
					AnswerQuestion.doSave();
				}
			});
			_this.submitLastSecondInterval = setInterval(function() {
				if (--total > 0) {
					$('#auto_force_commit').html(CommUtils.formatStr(i18n['auto_submit_last_second_tip'], total));
				} else {
					CommUtils.closeMask();
					clearInterval(_this.submitLastSecondInterval);
					layer.close(_this.forceCommitDialog);
					AnswerQuestion.doSave();
				}
			}, 1000);
		},
		calculateTimeStr: function(seconds) {
			var timeStr = '';
			if (seconds >= 3600) {
				var hour = parseInt(seconds / 3600);
				var hoursStr = '' + hour;
				while(hoursStr.length < 2) {
					hoursStr = '0'+ hoursStr;
				}
				timeStr += hoursStr + ':';
				seconds -= hour * 3600;
			}
			var minutes = parseInt(seconds / 60);
			var minutesStr = '' + minutes;
			while(minutesStr.length < 2) {
				minutesStr = '0'+ minutesStr;
			}
			timeStr += minutesStr + ':';
			seconds -= minutes * 60;
			var secondsStr = '' + seconds;
			while(secondsStr.length < 2) {
				secondsStr = '0'+ secondsStr;
			}
			timeStr += secondsStr;
			return timeStr;
		}
	}

	var ListenAudioV2 = {
		init : function() {
			var _this = this;
			$('#audio').jPlayer({
				swfPath: jPlayerSWFPath,
				ready: function () {
				},
				ended : function(e) {
				}
			});
		},
		hide : function() {
			$('#jp_container_1').hide();
		},
		switch : function(affix) {//设置听力文件, 听力切换后同时切换题目
			$("#audio").jPlayer("stop");
			if (CommUtils.isEmpty(affix)) {
				this.hide();
				return;
			}
			$("#audio").jPlayer("stop");
			$("#audio").jPlayer("setMedia", { mp3: affix});
			$('#jp_container_1').show();
			return $("#audio");
		}
	}

	var ListenAudio = { //听力播放器
		config : { // 配置信息，  遍历题目时设置
			questionAffix : [], // 题目听力信息，每一项都为[quesIndex, url]  题目位置， 听力文件url
			firstNoAffixQuesIndex : -1 //第一个没有听力题的题目位置
		},
		init : function() {
			var _this = this;
			$('#audio').jPlayer({
				swfPath: jPlayerSWFPath,
				ready: function () {
					if (_this.config.questionAffix.length == 0) {
						$('#jp_container_1').remove();//jplayer 播放器container
						return;
					}
					$('#jp_container_1').css({"display": "block"});
					_this.switch();
					$('.jp-stop, .jp-seek-bar, .jp-play-bar').unbind('click');
				},
				ended : function(e) {
					if (_this.config.questionAffix.length > 0) {
						_this.switch().jPlayer('play');
					} else {
						$('#jp_container_1').remove();//jplayer 播放器container
						if (_this.config.firstNoAffixQuesIndex != -1) {
							setTimeout(function(){
								AnswerQuestion.mySwiper.slideTo(_this.config.firstNoAffixQuesIndex, 1000, true);
							}, 2000);
						}
					}
				}
			});
		},
		switch : function() {//设置听力文件, 听力切换后同时切换题目
			var _this = this;
			var quesAffix = _this.config.questionAffix.splice(0, 1)[0]; //返回的为数组所以取第一个
			if (AnswerQuestion.mySwiper.activeIndex != quesAffix[0]) { //判断swipper是否为当前的
				AnswerQuestion.mySwiper.slideTo(quesAffix[0], 1000, true);
			}
			$("#audio").jPlayer("stop");
			$("#audio").jPlayer("setMedia", { mp3: quesAffix[1]});
			return $("#audio");
		}
	}
	
	Date.prototype.Format = function(format) {
		var o = {
			"M+" : this.getMonth() + 1, //month
			"d+" : this.getDate(), //day
			"h+" : this.getHours(), //hour
			"m+" : this.getMinutes(), //minute
			"s+" : this.getSeconds(), //second
			"q+" : Math.floor((this.getMonth() + 3) / 3), //quarter
			"S" : this.getMilliseconds()
		//millisecond
		}
		if (/(y+)/.test(format))
			format = format.replace(RegExp.$1, (this.getFullYear() + "")
					.substr(4 - RegExp.$1.length));
		for ( var k in o)
			if (new RegExp("(" + k + ")").test(format))
				format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
						: ("00" + o[k]).substr(("" + o[k]).length));
		return format;
	}
})(jQuery,window);
