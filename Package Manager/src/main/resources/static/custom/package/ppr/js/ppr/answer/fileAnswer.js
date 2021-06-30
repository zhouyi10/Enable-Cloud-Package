(function($,window) {
	/** 主逻辑 */
	var AnswerQuestion = window.AnswerQuestion = {
			userId : "",
			examId : "",
		    stepId : "",
			testId : "",
			getExamUrl : "",
			getTimeUrl : "",
			startTestTime : null,//default from background
			paperInfo : null,
			originQuestionOrder : [],
			curQuestionId : "",
 			init : function(){
				this.questionReOrder();
				this.initFile();
				this.renderPage();
				ListenAudio.init();
				this.initEvent();
				this.startTestTime = CommUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
				$(".question_area .question_item:first-child").click();
                this.uploader();
				PaperTestTimer.init(this.testInfo, this.getTimeUrl);
			},
            uploader : function() {
                $('.operation').each(function () {
                    var id = $(this).attr("data-key");
                    QuestionOperationInfo.initWebUploader(id,$(this));
                });
            },
			questionReOrder : function(){
				var _this = this;
				$.each(_this.paperInfo.nodes, function(index, node){
					if (node.level == 3) {
						_this.originQuestionOrder.push(node.question.questionId);
					}
				});
			},

            initFile : function() {
			    var _this = this;
				$('.testName').text(_this.testInfo.testName);
			    if(_this.paperInfo.files != null && _this.paperInfo.files.length > 0) {
                    var file = _this.paperInfo.files[0];
					var testUrl = "http://cc.enable-ets.com/storage/attachment/2019/8/26/074cf02442544db48154017c5a12bfce.doc";
					var src = CommUtils.formatStr(_this.fileViewUrl, testUrl);
					var fileHtml = '<iframe  src="' + src + '" width="140%" height="100%" style="overflow: scroll; border:none;" id="fileview" ></iframe>';
					$('.imgList').append(fileHtml);
              }
            },
			//render question info list
			renderPage : function(){
				var _this = this, html = '';
				var newQuestionNo = 1;
				$.each(_this.paperInfo.nodes, function(index, node){
					if (node.level == 3){
						var typeNme = node.question.type == null ? null : node.question.type.name;
						if (node.question.type.code === "39") {
							var questionTempDom = $("<div class='questionTemp'>" +node.question.stem.richText + "</div>");
							questionTempDom.children().remove(".question_blank_image_option");
							node.question.stem.richText = questionTempDom.html();
						}
						var quesHtml = '<div class="question_answer_area">';
						quesHtml += '<div class="question_stem"><span style="display: block;">' + newQuestionNo + '.(' + typeNme + ')' + '</span></div>';
						if(CommUtils.isNotEmpty(node.question.affixId)) {
							quesHtml += CommUtils.formatStr($("#audioTemplate").html(), node.question.affixId);
						}
						if (node.childAmount > 0) {
							$.each(node.children, function(c, child) {
								quesHtml += '<div class="question_stem _child">' + child.externalNo + '.(' + typeNme + ')' + '</div>';
								quesHtml += '<div class="question_answer _child" id="ques_' + child.question.questionId + '" child-question-id="' + child.question.questionId + '" exam-child-question-id="' + child.nodeId + '">' + Utils.processAnswer(child) + '</div>';
								quesHtml += '<div class="answer_canvas_area"><div><i class="fa fa-paint-brush"></i></div><div class="img_all"></div></div>';
							});
						} else {
							quesHtml += '<div class="question_answer">' + Utils.processAnswer(node) + '</div>';
							quesHtml += '<div class="answer_canvas_area"><div><i class="fa fa-paint-brush"></i></div><div class="img_all"></div></div>';
						}
                        quesHtml += '</div>';
						html += '<div class="question_item" title="请点击题目开始作答" id="ques_' + node.question.questionId + '" question-id="' + node.question.questionId + '" exam-question-id="' + node.nodeId + '" answer-stamp="">' + quesHtml + '</div>';
						newQuestionNo++;
					}
				});
				$('#question_area').html(html);
			},
			initEvent : function() {
				var _this = this;
				$('.question_answer_wrapper .choose_op').on('click', function() {
					if ($(this).closest('.question_answer_wrapper').hasClass('multi')) {
						$(this).toggleClass('active');
					} else {
						$(this).siblings().removeClass('active');
						$(this).addClass('active');
					}
					var $ques = $(this).closest('.question_item');
					var answer = [];
					$(this).closest('.question_answer').find('.choose_op.active').each(function() {
						answer.push($(this).attr('data-value'));
					});
					if ($ques.find('.question_answer._child').length == 0) {
						$ques.attr('user-answer', JSON.stringify(answer));
					} else {
						$(this).closest('.question_answer._child').attr('user-answer', JSON.stringify(answer));
					}
				});

				$('.question_answer_wrapper input, .question_answer_wrapper textarea').on('input propertyChange', function() {
					$(this).closest('.ans').removeClass('active');
					if ($.trim($(this).val()) != '') {
						$(this).closest('.ans').addClass('active');
					}
					var $ques = $(this).closest('.question_item');
					var answer = [];
					$(this).closest('.question_answer').find('.ans').each(function() {
						answer.push($(this).val());
					});
					if ($ques.find('.question_answer._child').length == 0) {
						$ques.attr('user-answer', JSON.stringify(answer));
					} else {
						$(this).closest('.question_answer._child').attr('user-answer', JSON.stringify(answer));
					}
				});
				$('.question_area').find('.answer_canvas_area div:first-child i').on('click',function () {
					var $curQues = $(this).closest('.answer_canvas_area');
					var imgObjs = [];
					$curQues.find('img').each(function() {
						imgObjs.push({ 'fileId' : $(this).attr('file-id'), 'fileName' : $(this).attr('file-name'), 'url' : $(this).attr('src')  });
					});
					CanvasUtils.init(imgObjs, {
						$canvasArea : $curQues,
						complete : function(result, settingData){
							$(settingData.$canvasArea.children()[1]).empty();
							if (result != null && result.length > 0) {
								var html = '';
								$.each(result, function(i, imgObj){
									var fileId = imgObj.fileId, url = imgObj.url;
									if (imgObj.isModify) {
										fileId = imgObj.file.fileId;
										url =imgObj.file.url;
									}
									html += '<div style="margin: 4px; float: left; width: 202px; max-height: 202px;border: 1px solid #c5c5c5;"><img src="' + url + '" file-id="' + fileId + '" file-name="' + imgObj.fileName + '" crossorigin="anonymous" /></div>';
								});
								$(settingData.$canvasArea.children()[1]).append(html);
							}
						}
					});
				});
				$('.question_area').find(".question_item").on("click",function () {
					if ($(this).attr("question-id") == _this.curQuestionId) {
						return;
					}
					_this.curQuestionId = $(this).attr("question-id");
					var $curQuesItem = $('.question_item#ques_' + _this.curQuestionId);
					AnswerQuestionTimer.start($curQuesItem);
                    $('.imgList').find('img').nextAll().remove();
                    $.each(_this.paperInfo.nodes,function (index, node) {
                        if(node.level == 3) {
                            if(_this.curQuestionId == node.question.questionId) {
                                var axisHtml = '';
                                if(node.question.axises != null && node.question.axises.length > 0) {
                                   /* $.each(node.question.axises,function (index1, axise) {
                                        axisHtml += CommUtils.formatStr(axisHtmlTem,axise.xaxis,axise.yaxis,axise.width,axise.height);
                                        axisHtml += CommUtils.formatStr(axisHtmlTem_,axise.xaxis,0,axise.width, axise.yaxis);
                                        axisHtml += CommUtils.formatStr(axisHtmlTem_,axise.xaxis,axise.yaxis + axise.height,axise.width,1127 - axise.yaxis - axise.height);
                                    })
									$('.imgList').append(axisHtml);*/
									$('.imgList').animate({scrollTop:node.question.axises[0].yaxis - window.scrollY} ,600);
                                } else {
									$('.imgList').append(CommUtils.formatStr(axisHtmlTem_,10,0,776,1127));
								}
                            }
                        }
                    });
                });
			},
			save: function() {
				var _this = this;
				_this.endTestTime = CommUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
				var userAnswers = UserAnswer.joinUserAnswer();
				CommUtils.tryLock($(document.body));
				var param = {
					'answers' : userAnswers,
					'testId': _this.testId,
					'paperId' : _this.examId,
					'userId' : _this.userId,
					'stepId' : _this.stepId,
					'fileId' : _this.fileId,
					'startTime': PaperTestTimer.startTestTimeStr,
					'endTime': _this.endTestTime
				};
				$.ajax({
					type:'post',
					contentType: 'application/json',
					data:JSON.stringify(param),
					dataType : "json",
					url: _this.saveUrl,
					success:function(data){
						CommUtils.closeMask();
						CommUtils.unLock($(document.body));
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
					error: function () {
						CommUtils.closeMask();
						CommUtils.unLock($(document.body));
						CommUtils.tipBoxV2(i18n['submit_failed'], 3000, function(){
						});
					}
				});
			},
			//save user answer
			savePaper : function(){
				var _this = this;
				if(this.paperInfo == null) {
					return ;
				}
				//PaperTestTimer.endAnswer();
				_this.confirmBox(i18n['confirm_submit']  , function(){
					AnswerQuestionTimer.end();
					_this.save();
				});
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
			},
	};

	var Utils = {
		singleArr: ['01', '27', '28', '11', '12'],
		multiArr: ['02'],
		judgeArr: ['04'],
		blankArr: ['03', '10', '14'],
		collectLine : '40',
		buildOptions: function(options, type) {
			if (options == null) return '';
			var opHtmls = '';
			/*if (CommUtils.isNotEmpty(type) && type == '0') {
				options.sort(function () {
					return Math.random() > 0.5 ? -1 : 1;
				});
			}*/
			for(var i = 0; i < options.length; i++){
				var optionTitle = options[i].alias;
				opHtmls += '<li class="ans choose_op" data-value="' + optionTitle + '">' + optionTitle+ '</li>';
			}
			return opHtmls;
		},
		buildMultiOptions: function(options, type) {
			if (options == null) return '';
			var opHtmls = '';
			/*if (CommUtils.isNotEmpty(type) && type == '0') {
				options.sort(function () {
					return Math.random() > 0.5 ? -1 : 1;
				});
			}*/
			for(var i = 0; i < options.length; i++){
				var optionTitle = options[i].alias;
				opHtmls += '<li class="ans choose_op" data-value="' + optionTitle + '"><span class="choose_btn multi"></span><div>' + optionTitle+ '</div></li>';
			}
			return opHtmls;
		},
		processAnswer: function(node) {
			var typeCode = node.question.type.code;
			var html = '';
			if (this.singleArr.indexOf(typeCode) >= 0) {
				html += '<ul class="question_answer_wrapper single">' + this.buildOptions(node.question.options, '0') + '</ul>';
			} else if (this.multiArr.indexOf(typeCode) >= 0) {
				html += '<div class="question_answer_wrapper multi">' + this.buildMultiOptions(node.question.options, '0') + '</div>';
			} else if (this.judgeArr.indexOf(typeCode) >= 0) {
				var options = [{'alias': '对', 'label': '对'},{'alias': '错', 'label': '错'}];
				html += '<div class="question_answer_wrapper judge">' + this.buildOptions(options, null) + '</div>';
			} else if (this.blankArr.indexOf(typeCode) >= 0) {
				var blankAmount = CommUtils.ifEmpty(node.question.answer.label).split("@*@")[0].split("@#@").length;
				var blankHtml = '';
				for (var i = 0; i< blankAmount; i++) {
					blankHtml += '<div class="blank_item"><input class="ans"  placeholder="'+i18n["put_answer"]+'" onpaste="return false" oncontextmenu="return false" oncopy="return false" oncut="return false"/></div>';
				}
				html += '<div class="question_answer_wrapper blank">' + blankHtml + '</div>';
			} else if(typeCode == "39") {
				QuestionDragDrop.tranToDragQuestion(node.question)
				html += node.question.stem.richTextOpt
			}else if (this.collectLine == typeCode){
				var template = "<span class='showitem' data-id='{0}' order='{1}'><img style='' src='{2}'/></span>";
				var data = JSON.parse(node.question.stem.plaintext);
				$("#collectLineQuestionTemplate .show .showleft").empty();
				$.each(data.left, function(index, obj){
					$("#collectLineQuestionTemplate .show .showleft").append(CommUtils.formatStr(template, "L" + obj.order, obj.order, obj.url));
				});
				$("#collectLineQuestionTemplate .show .showright").empty();
				$.each(data.right, function(index, obj){
					$("#collectLineQuestionTemplate .show .showright").append(CommUtils.formatStr(template, "R" + obj.order, obj.order, obj.url));
				});
				html += $("#collectLineQuestionTemplate").html().replace("{questionId}", node.question.questionId);
			} else if(typeCode == "31") {
				html += $("#operationAnswerTemplate").html().replaceAll("{questionId}", node.question.questionId);
			} else {
				html += '<div class="question_answer_wrapper default"><div><textarea class="ans" placeholder="'+i18n["put_answer"]+'" onpaste="return false" oncontextmenu="return false" oncopy="return false" oncut="return false"></textarea></div></div>'
			}
			return html;
		},
		processStem : function(stemContent, questionNo, childInternalNo, typeName) {
			var stemNoHtml = $("<div>" + stemContent + "</div>").text().trim();
			if (childInternalNo != undefined && childInternalNo != null) {
				var placeholder = '[' + childInternalNo + ']';
				if (stemNoHtml.indexOf(placeholder) == 0) {
					var _questionNo = typeName == null ? questionNo : questionNo + "("+typeName+")";
					return stemContent.replace(placeholder, _questionNo + " ");
				}
			}
			if (stemNoHtml.indexOf(questionNo) != 0) {
				var _questionNo = typeName == null ? questionNo : questionNo + ".("+typeName+")";
				return "<span style='display: block'>" + _questionNo + " </span>" + stemContent;
			}
			return stemContent;
		}
	};

	var PaperTestTimer = { //exam cost time
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
			this.endAnswerTime.setSeconds(this.endAnswerTime.getSeconds() - 10);
			if (this.endAnswerTime <= this.startTestTime) {
				this.endAnswerTime = new Date(this.startTestTime);
				this.endAnswerTime.setSeconds(this.endAnswerTime.getSeconds() + 1);
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
					AnswerQuestion.save();
				}
			});
			_this.submitLastSecondInterval = setInterval(function() {
				if (--total > 0) {
					$('#auto_force_commit').html(CommUtils.formatStr(i18n['auto_submit_last_second_tip'], total));
				} else {
					CommUtils.closeMask();
					clearInterval(_this.submitLastSecondInterval);
					layer.close(_this.forceCommitDialog);
					AnswerQuestion.save();
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
	};

	var ListenAudio = {
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
		switch : function(affix) {
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

	var UserAnswer = {
		joinUserAnswer: function() {
			var _this = this;
			var answerArr = [];
			$.each(AnswerQuestion.originQuestionOrder, function(index, id){
				answerArr = answerArr.concat(_this.getUserAnswer($("#ques_" + id)));
			});
			return answerArr;
		},
		getUserAnswer : function($ques) {
			var _this = this, parentId = $ques.attr('question-id');
			var answerStamp = $ques.attr('answer-stamp');
			var answerArr = [];
			if ($ques.find('.question_answer._child').length == 0) {
				var costTime = 0;
				var answerStamps = [];
				if (answerStamp.length > 0) {
					answerStamp = answerStamp.substring(1);
					$.each(answerStamp.split(','), function(a, stampInfo) {
						costTime += parseInt(stampInfo.split(':')[1]);
						answerStamps.push(stampInfo);
					});
					costTime = Math.ceil(costTime / 1000);
				}
				var userAnswer = "";
				if ($ques.find(".question_stem .stem.question_blank_image_area").length > 0) {
					userAnswer = QuestionDragDrop.getUserAnswer($ques.find(".question_stem"));
				}else if($ques.find(".collectline ").length > 0){
					var answerArr = [];
					var ret = false;
					$ques.find(".show .showright .showitem").each(function(){
						if ($(this).hasClass("addstyle")){
							var pair = $(this).attr("pair");
							answerArr.push($ques.find(".showleft .showitem[pair='"+pair+"']").attr("order"));
							ret = true;
						}else{
							answerArr.push(" ");
						}
					});
					userAnswer = ret ? answerArr.join(",") : '';
				} else if($ques.find(".answer_list ").length > 0) {
					var answerArr_ = [];
					var ret_ = false;
					$ques.find("a").each(function(index,obj){
						answerArr_.push(obj.outerHTML.replace(/</g, "&lt;"));
						ret_ = true;
					});
					userAnswer = ret_ ? answerArr_.join(",") : '';
				} else {
					userAnswer = $ques.attr('user-answer');
				}
				var canvases = _this.getCanvas($ques.find('.answer_canvas_area'));
				answerArr.push( {
					"questionId" : parentId,
					"parentId" : parentId,
					"userAnswer" : UserAnswer.formatVal(userAnswer),
					"answerCostTime" : '' + costTime,
					"answerStamp" : answerStamps,
					"canvases" : canvases
				});
			} else {
				var childLength = $ques.find('.question_answer._child').length;
				var costTime = 0, answerStampArr = [];
				if (answerStamp.length > 0) {
					answerStamp = answerStamp.substring(1);
					$.each(answerStamp.split(','), function(a, stampInfo) {
						var arr = stampInfo.split(':');
						var time = parseInt(arr[1]);
						answerStampArr.push({'start': parseInt(arr[0]), 'time': time });
						costTime += time;
					});
					costTime = Math.ceil(costTime / 1000);
				}
				var childTimeInfo = [], childCostTime = Math.floor(costTime/childLength);
				for(var ci = 0; ci < childLength; ci++) {
					childTimeInfo.push({'answerStamp': [], 'answerCostTime': childCostTime});
				}
				$.each(answerStampArr, function(i, stamp) {
					var costMiles = Math.floor(stamp.time / childLength);
					var start = stamp.start;
					for(var ci = 0; ci < childLength; ci++) {
						childTimeInfo[ci].answerStamp.push('' + start + ':' + costMiles);
						start += costMiles;
					}
				});
				$ques.find('.question_answer._child').each(function(i) {
					var questionId= $(this).attr('child-question-id');
					var canvases = _this.getCanvas($("#ques_" + questionId).next());
					answerArr.push({
						"questionId" : questionId,
						"parentId" : parentId,
						"userAnswer" : UserAnswer.formatVal($(this).attr('user-answer')),
						"answerCostTime" : childTimeInfo[i].answerCostTime,
						"answerStamp" : childTimeInfo[i].answerStamp,
						"canvases" : canvases
					});
				});
			}
			return answerArr;
		},
		getCanvas : function($canvasArea) {
			var canvases = [];
			if ($canvasArea.find('img').length > 0) {
				$canvasArea.find('img').each(function(index) {
					canvases.push({
						"order": index + 1,
						"fileId" : $(this).attr('file-id'),
						"fileName":  $(this).attr('file-name'),
						"url" : $(this).attr('src')
					})
				});
			}
			return canvases;
		},
		formatVal : function(str) {
			if (str == null || str == undefined) {
				str = "";
			}
			str = str.replace(/\&/g, "&amp;");
			str = str.replace(/\"/g, "&quot;");
			str = str.replace(/\</g, "&lt;");
			str = str.replace(/\>/g, "&gt;");
			str = str.replace(/\'/g, "&apos;");
			return str;
		},
	};

})(jQuery,window);

(function($){

	var QuesCostTime = {
		param : null,
		startTime : null
	}

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

	AnswerQuestionTimer.init(function($ques, startTime, endTime) {
		var totalMilliSecond = endTime - startTime;
		if ($ques != null && $ques.length > 0) {
			var answerStamp = $ques.attr('answer-stamp') + "," + startTime + ":" + totalMilliSecond;
			$ques.attr('answer-stamp', answerStamp);
		}
	});

})(jQuery);
