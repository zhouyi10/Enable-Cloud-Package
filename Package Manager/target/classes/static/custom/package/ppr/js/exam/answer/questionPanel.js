/**
 * 手机竖屏答题页面题目插件
 */
(function($) {
	//初始化题目区域
	function initQuestionPanle(panel, settings){
		CommHtmlMethod.renderQuestion(panel, settings);
		AnswerMethod.initUserAnswer(panel, settings);
		EventsMethod.initEvent(panel, settings);
	}

	function ifEmpty(obj, defautStr) { //字符串是否为空时设置默认值
		if (defautStr == undefined) {
			defautStr = '';
		}
		if (obj == undefined || obj == null) {
			return defautStr;
		}
		return '' + obj;
	}

	function addNavHtml(isMobile, text, questionId, childQuestionId) {
		if (isMobile) { //手机页面
			$("#testCard").append("<li data-key='" + ifEmpty(childQuestionId, questionId) + "'><b>" + text + "</b></li>");
		} else {
			if ($('#testCard li[data-key="' + questionId + '"]').length == 0) {
				$('#testCard').append("<li data-key='" + questionId + "'></li>");
			}
			$('#testCard li[data-key="' + questionId + '"]').append("<div data-key='" + ifEmpty(childQuestionId, questionId) + "'><b>" + text + "</b></div>")
		}
	}
	
	var CommHtmlMethod = { //页面构造
		isMobile : true,
		renderQuestion : function(panel, settings){ //题目区域渲染
			this.isMobile = settings.isMobile;
			var htmlStr = "<div class=\"question_area\">";
			
			htmlStr += CommHtmlMethod.buildQuestionHtml(settings.questionNode);
			
			htmlStr + "</div>";
			// htmlStr = htmlStr.replace('<img ', '<img crossOrigin=anonymous ');
			panel.html(htmlStr);
            if (settings.questionNode.question.type != null && settings.questionNode.question.type.code == AnswerQuestion.collectLineQuestionType){   //处理连线题题干
                panel.find(".line_content").css("display", "none");
            }
		},
		buildQuestionHtml : function(questionNode) { //构造题目html
			var question = questionNode.question;
			question.questionId = question.questionId;
			var questionNo = questionNode.externalNo;
			var htmlStr = "";

			// 拖拽题清理题干选项 在answer区重新生成随机选项
			if (!CommUtils.isEmpty(question) && !CommUtils.isEmpty(question.type)
				&& question.type.code == "39") {
				var questionTempDom = $("<div class='questionTemp'>" + question.stem.richText + "</div>");
				questionTempDom.children().remove(".question_blank_image_option");
				question.stem.richText = questionTempDom.html();
			}

			// 2 题目信息
			htmlStr += "<div class='question_content'>"; // 题目信息，若分屏，则位于左侧11
			htmlStr += CommHtmlMethod.processStem(question.stem.richText, questionNo);
			// 3.0 判断是否有小题
			if (questionNode.children == undefined || questionNode.children == null || questionNode.children.length == 0) {
				// 3 选项信息
				htmlStr += CommHtmlMethod.buildOptions(question.options);
				// 4 答案选项
				htmlStr += "</div><div>"; // 答案及绘画框，若分屏，位于右侧
				htmlStr += CommHtmlMethod.buildAnswer(question);
				htmlStr += CommHtmlMethod.buildCanvasAreaHtml();
				htmlStr += "</div>";
				// 5 --------------- 构造答题卡按钮 ------------------
				addNavHtml(CommHtmlMethod.isMobile, questionNo, question.questionId);
			} else {
				htmlStr += "</div><div>"; // 子题目信息， 若分屏，位于右侧
				$.each(questionNode.children, function(i, childNode){
					htmlStr += CommHtmlMethod.buildChildQuestionHtml(childNode);
					// --------------- 构造答题卡按钮 ------------------
					addNavHtml(CommHtmlMethod.isMobile, childNode.externalNo, question.questionId, childNode.question.questionId);
				});
				htmlStr += "</div>";
			}
			return htmlStr;
		},
		buildChildQuestionHtml : function(childNode) { // 小题题目html
			var question = childNode.question;
			var questionNo = childNode.externalNo;
			var htmlStr = "<div class='child_question'>";
			// 2 题目信息
			htmlStr += "<div class='child_question_content'>" + CommHtmlMethod.processStem(question.stem.richText, questionNo, childNode.internalNo) + "</div>";
			// 3 选项信息
			htmlStr += CommHtmlMethod.buildOptions(question.options);
			// 4 答案选项
			htmlStr += CommHtmlMethod.buildAnswer(question);
			htmlStr += CommHtmlMethod.buildCanvasAreaHtml();
			htmlStr += "</div>";
			return htmlStr;
		},
		processStem : function(stemContent, questionNo, childInternalNo) { //处理题干
			var stemNoHtml = $("<div>" + stemContent + "</div>").text().trim();
			if (childInternalNo != undefined && childInternalNo != null) {
				var placeholder = '[' + childInternalNo + ']';
				if (stemNoHtml.indexOf(placeholder) == 0) {
					return stemContent.replace(placeholder, questionNo + " ");
				}
			}
			if (stemNoHtml.indexOf(questionNo) != 0) {
				// if (stemContent.startsWith('<p>')) {
				// 	return "<p><span>" + questionNo + " </span>" + stemContent.substring(3);
				// } else if(stemContent.startsWith('<div>')) {
				// 	return "<div><span>" + questionNo + " </span>" + stemContent.substring(5);
				// } else {
					return "<span>" + questionNo + " </span>" + stemContent;
				// }
			}
			return stemContent;
		},
		buildOptions : function(options) { //如果有选项信息，生成选项信息html
			var htmlStr = "";
			//若选项信息在option中
			if (options != null && options.length > 0 && options[0].lable != null && options[0].lable != '') {
				htmlStr += "<div class='question_options'>";
				for(var i = 0; i < options.length; i++){
					htmlStr += '<div><span>' + options[i].alias +'.&nbsp' + '</span>' + options[i].lable + '</div>';
				}
				htmlStr += "</div>";
			}
			htmlStr = htmlStr.replace(/\<questionoption/g,"<div").replace(/\/questionoption>/g,"/div>")
				.replace(/\<option/g,"<div").replace(/\/option>/g,"/div>");
			return htmlStr;
		},
		buildAnswer : function(question) { //构造用户作答区域
			var htmlStr = "";
			if (question == null || question.type == null) {
				return htmlStr;
			}
			switch(question.type.code) {
				case '01':
				case '27':
				case '28':
				case '11':
				case '12':
					// 如果选项为空 从提干信息中获取
					if (CommUtils.isEmpty(question.options)) {
						// 通过ASCII匹配提干选项信息, A的ASCII码为65
						var aAsc = 65;
						var tempOptionName = String.fromCharCode(aAsc);
						var index = 0;
						var tempOptionId = "option" + tempOptionName;
						while (question.stem.richText.indexOf(tempOptionId) >1 ) {
                            question.options[index] = {'alias': tempOptionName, 'lable': '', 'sequencing': index};
                            aAsc ++;
                            index ++;
                            tempOptionName = String.fromCharCode(aAsc);
                            tempOptionId = "option" + tempOptionName;
						}
					}

					htmlStr = CommHtmlMethod.buildChooseAnswerAreaHtml(question.options, question.questionId, "single_choose");
					break;
				case '02':
					htmlStr = CommHtmlMethod.buildChooseAnswerAreaHtml(question.options, question.questionId, "multiple_choose");
					break;
				case '04':
					htmlStr = $("#judgementAnswerTemplate").html().replace(/\{questionId\}/g, question.questionId);
					break;
				case '03':
				case '10':
				case '14':
					htmlStr = CommHtmlMethod.buildBlankAnswerAreaHtml(question.questionId, question.answer);
					break;
				case '31':
					htmlStr = CommHtmlMethod.buildOperationAnswerAreaHtml(question.questionId);
					break;
                case '40':
                    htmlStr = CommHtmlMethod.buildCollectLineAnswerAreaHtml(question);
                    break;
				case '39':
					QuestionDragDrop.tranToDragQuestion(question);
					htmlStr = question.stem.richTextOpt;
					break;
				default:
					htmlStr = $("#textAreaTemplate").html().replace(/\{questionId\}/g, question.questionId);
			}
			return htmlStr;
		},
		buildChooseAnswerAreaHtml : function(options, questionId, type) { //根据选项和类型生成选择题答案选项
			var html = "<ul class=\"selectBox answer " + type + " text_no_select\" data-key=\"" + questionId + "\" unselectable=\"on\">";
			//根据option添加答题按钮
			for(var i = 0; i < options.length; i++){
				//var optionTitle = String.fromCharCode(65 + options[i].options.optionOrder);
				var optionTitle = options[i].alias;
				html +="<li name=\"op" + questionId + "\" data-value=\"" + optionTitle + "\">" + optionTitle + "</li>";
			}
			html +="</ul>";
			return html;
		},
		buildBlankAnswerAreaHtml : function(questionId, answer) { //根据答案生成填空题答案选项
			var html = "<p class=\"text_answer answer blank\" data-key=\"" + questionId + "\">";
			
			var template = $("#blankAnswerTemplate").html();
			var blankAmount = ifEmpty(answer.lable).split("@*@")[0].split("@#@").length;
        	for (var i = 0; i < blankAmount; i++) {
        		html += template.replace(/\{questionId\}/g, questionId);	
        	}
        	
        	html += "</p>";
			return html;
		},
		buildCanvasAreaHtml : function() {
			return '<div class="canvas_area"><div><i class="fa fa-paint-brush"></i></div><div class="img_all"></div></div>';
		},
		buildOperationAnswerAreaHtml : function (questionId) {
			return $("#operationAnswerTemplate").html().replace(/\{questionId\}/g, questionId);
		},
        buildCollectLineAnswerAreaHtml : function(question){
            var _this = this;
            var data = JSON.parse(question.stem.plaintext);
            $.each(data.left, function(index, obj){
                $("#collectLineQuestionTemplate .show .showleft").append(CommUtils.formatStr(ConnectLineQuestion.itemTemplate, "L" + obj.order, obj.order, obj.url));
            });
            $.each(data.right, function(index, obj){
                $("#collectLineQuestionTemplate .show .showright").append(CommUtils.formatStr(ConnectLineQuestion.itemTemplate, "R" + obj.order, obj.order, obj.url));
            });
            return $("#collectLineQuestionTemplate").html().replace(/\{questionId\}/g, question.questionId);
        },
	};
		
	var AnswerMethod = {
		questionXml : "<userAnswer parentId='{parentId}' questionId='{questionId}' examQuestionId='{examQuestionId}' userAnswer='{userAnswer}' answerCostTime='{answerCostTime}' answerStamp='{answerStamp}' answerStatus='{answerStatus}' answerScore='{answerScore}' teacherNote='{teacherNote}' markStatus='{markStatus}'>{canvasXml}</userAnswer>",
		questionChildXml : "<userAnswer examQuestionId='{examQuestionId}' parentId='{parentId}' questionId='{questionId}' examQuestionChildId='{examQuestionChildId}' userAnswer='{userAnswer}' answerCostTime='{answerCostTime}' answerStamp='{answerStamp}' answerStatus='{answerStatus}' answerScore='{answerScore}' teacherNote='{teacherNote}' markStatus='{markStatus}'>{canvasXml}</userAnswer>",
		canvasXml : "<userAnswerCanvas answerId='' canvasAnswerType='0' canvasId='' canvasOrder='{order}' canvasType='0' content='' contentId='' fileId='{fileId}' fileName='{fileName}' url='{url}'></userAnswerCanvas>",
		initUserAnswer : function(panel, settings) {//初始化用户答案
			// 1 初始化题目答案信息
			var node = settings.questionNode;
			var questionId = node.question.questionId;
			var nodeId = node.nodeId;
			var userAnswer = AnswerMethod.buildUserAnswer(questionId, nodeId, questionId);
			
			// 2 无小题题目处理
			if (node.children == null || node.children.length == 0) {
				var $answer = panel.find(".answer");
				userAnswer.$answer = $answer; // 数据绑定 对象元素
				$answer.data("answer", userAnswer); // dom 元素绑定 数据
			} else { // 3 含小题题目处理
				$.each(node.children, function(i, childNode){
					// 3.1 生成小题题目答案信息
					var questionChildId = childNode.question.questionId;
					var questionChildNodeId = childNode.nodeId;
					var childUserAnswer = AnswerMethod.buildUserAnswer(questionId, nodeId, questionChildId, questionChildNodeId);
					// 3.2
					var $answer = panel.find(".answer").eq(i);
					childUserAnswer.$answer = $answer; // 数据绑定 对象元素
					$answer.data("answer", childUserAnswer); // dom 元素绑定 数据
					// 3.3
					userAnswer.childAnswerArr.push(childUserAnswer);
				});
			}
			settings.questionNode.userAnswer = userAnswer;
		},
		getUserAnswerXml : function(settings) {
			var userAnswer = settings.questionNode.userAnswer;
			if (settings.questionNode.question.type != null){
				if (settings.questionNode.question.type.code == AnswerQuestion.collectLineQuestionType) {
					userAnswer.userAnswer = ConnectLineQuestion.getUserAnswer(settings)
				} else if (settings.questionNode.question.type.code == '39') {
					userAnswer.userAnswer = QuestionDragDrop.getUserAnswer(settings)
				}
            }
			var answerArr = [];
			if (userAnswer.childAnswerArr.length == 0){
				answerArr.push( {
					"questionId" : userAnswer.questionId,
					"parentId" : userAnswer.parentId,
					"userAnswer" : AnswerMethod.formatVal(userAnswer.userAnswer),
					"answerCostTime" : userAnswer.answerCostTime,
					"answerStamp" : userAnswer.answerStamp,
					"canvases" : userAnswer.userAnswerCanvasList
				});
			}else{
				$.each(userAnswer.childAnswerArr, function(i, childAnswer){
					answerArr.push({
						"questionId" : childAnswer.questionId,
						"parentId" : childAnswer.parentId,
						"userAnswer" : AnswerMethod.formatVal(childAnswer.userAnswer),
						"answerCostTime" : childAnswer.answerCostTime,
						"answerStamp" : childAnswer.answerStamp,
						"canvases" : childAnswer.userAnswerCanvasList
					});
				});
			}
			return answerArr;

			// // 2 无小题题目处理
			// if (userAnswer.childAnswerArr.length == 0) {
			// 	return AnswerMethod.formatStr(AnswerMethod.questionXml, {
			// 		parentId : userAnswer.parentId,
			// 		questionId : userAnswer.questionId,
			// 		examQuestionId : userAnswer.examQuestionId,
			// 		userAnswer : AnswerMethod.formatVal(userAnswer.userAnswer),
			// 		answerCostTime : userAnswer.answerCostTime,
			// 		answerStatus : userAnswer.answerStatus,
			// 		answerScore : userAnswer.answerScore,
			// 		teacherNote : userAnswer.teacherNote,
			// 		markStatus : userAnswer.markStatus,
			// 		canvasXml : AnswerMethod.getCanvasXml(userAnswer.userAnswerCanvasList),
			// 		answerStamp : userAnswer.answerStamp.join(",")
			// 	});
			// } else { // 3 含小题题目处理
			// 	var answerXml = "";
			// 	$.each(userAnswer.childAnswerArr, function(i, childAnswer) {
			// 		answerXml += AnswerMethod.formatStr(AnswerMethod.questionChildXml, {
			// 			parentId : childAnswer.parentId,
			// 			questionId : childAnswer.questionId,
			// 			examQuestionId : childAnswer.examQuestionId,
			// 			examQuestionChildId : childAnswer.examQuestionChildId,
			// 			userAnswer : AnswerMethod.formatVal(childAnswer.userAnswer),
			// 			answerCostTime : childAnswer.answerCostTime,
			// 			answerStatus : childAnswer.answerStatus,
			// 			answerScore : childAnswer.answerScore,
			// 			teacherNote : childAnswer.teacherNote,
			// 			markStatus : childAnswer.markStatus,
			// 			canvasXml : AnswerMethod.getCanvasXml(childAnswer.userAnswerCanvasList),
			// 			answerStamp : childAnswer.answerStamp.join(",")
			// 		});
			// 	});
			// 	return answerXml;
			// }
		},
		getCanvasXml : function(userAnswerCanvasList) {
			var canvasXml = "";
			if (userAnswerCanvasList != undefined && userAnswerCanvasList.length > 0) {
				canvasXml += "<userAnswerCanvasList>";
				$.each(userAnswerCanvasList, function(i, canvasObj) {
					canvasXml += AnswerMethod.formatStr(AnswerMethod.canvasXml, canvasObj);
				});
				canvasXml += "</userAnswerCanvasList>";
			}
			return canvasXml;
		},
		formatStr : function(template, obj) {
			var temp = template;
			$.each(obj, function(key, value) {
				var reg = new RegExp("{" + key + "}", "g");
				temp = temp.replace(reg, value);
			});
			return temp;
		},
		formatVal : function(str) {
			if (str == null || str == undefined) {
				str = "";
			}
			str = str.replace(/\&/g, "&amp;"); //和符号
			str = str.replace(/\"/g, "&quot;"); //转义双引号
			str = str.replace(/\</g, "&lt;"); //小于号
			str = str.replace(/\>/g, "&gt;"); //大于号
			str = str.replace(/\'/g, "&apos;"); //单引号
			return str;
		},
		buildUserAnswer : function(parentQuestionId, examQuestionId, questionId, examQuestionChildId, userAnswer) {
			return {
				'parentId' : parentQuestionId || "",
				'questionId' : questionId || "",
				"examQuestionId" : examQuestionId || "",
				"examQuestionChildId" : examQuestionChildId || "",
				'userAnswer' : userAnswer || "[\"\"]",
				'answerCostTime' : '0',
				'answerStamp' : [],
				'answerStatus' : '1',//answerStatus	是否答對,0：对，1：错
				'answerScore' : '0',
				'teacherNote' : '',
				'markStatus' : '1',
				'childAnswerArr' : []
			};
		}
	}
	
	//初始化事件
	var EventsMethod = {
		initEvent : function(panel, settings) {
			var answerNavSelector = settings.isMobile ? "#testCard > li[data-key='{0}']" : "#testCard div[data-key='{0}']";
			panel.find(".operation").each(function (index, obj) {
				var id = $(this).attr("data-key");
				QuestionOperationInfo.initWebUploader(panel, settings,id);
			});
			panel.find(".answer").each(function(){
				var $answerArea = $(this);
				var $cardLi = $(answerNavSelector.replace("{0}", $answerArea.attr("data-key")));
				if ($answerArea.hasClass("single_choose")) {
					// 单选 '01' '27' '28' '11' '12'
					EventsMethod.bindSingleChooseAreaEvent($answerArea, $cardLi);
				} else if ($answerArea.hasClass("multiple_choose")) {
					// 多选 '02'
					EventsMethod.bindMutipleChooseAreaEvent($answerArea, $cardLi);
				} else if ($answerArea.hasClass("blank")) {
					// 填空 '03' '10' '14'
					EventsMethod.bindBlankAreaEvent($answerArea, $cardLi);
				} else if ($answerArea.hasClass("judge")) {
					// 判断 '04' 选项互斥，和单选相同
					EventsMethod.bindSingleChooseAreaEvent($answerArea, $cardLi);
				} else if ($answerArea.hasClass("collectline")){
					$answerArea.onLine({
						regainCanvas: true
					});
                }
				else if ($answerArea.hasClass("default")) {
					// 其他    如 简答、作文等
					EventsMethod.bindCommonAnswerAreaEvent($answerArea, $cardLi);
				}
			});
			panel.find('.canvas_area div:first-child i').on('click', function(){
				var $canvasArea = $(this).closest('.canvas_area');
				var imgObjs = [];
				$canvasArea.find('img').each(function() {
					var $img = $(this);
					imgObjs.push({
						'fileId' : $img.attr('fileId'),
						'fileName' : $img.attr('fileName'),
						'url' : $img.attr('src')
					});
				});
				CanvasUtils.init(imgObjs, {
					$canvasArea : $canvasArea,
					complete : function(result, settingData){
						$(settingData.$canvasArea.children()[1]).empty();
						var userAnswerCanvasList = [];
						if (result != null && result.length > 0) {
							$.each(result, function(i, imgObj){
								var fileId = imgObj.fileId, url = imgObj.url;
								if (imgObj.isModify) {
									fileId = imgObj.file.fileId;
									url =imgObj.file.url;
								}
								var imgHtml = '<div style="margin: 4px; float: left; width: 200px; max-height: 200px;border: 1px solid #c5c5c5;"><img src="' + url + '" fileId="' + fileId + '" fileName="' + imgObj.fileName + '" crossorigin="anonymous" /></div>';
								$(settingData.$canvasArea.children()[1]).append(imgHtml);
								userAnswerCanvasList.push({
									'order' : (i + 1),
									'fileId' : fileId,
									'fileName' : imgObj.fileName,
									'url' : url
								});
							});
						}
						var $answerArea = settingData.$canvasArea.prev('.answer');
						var userAnswer = $answerArea.data("answer");
						userAnswer.userAnswerCanvasList = userAnswerCanvasList;

						var $cardLi = $(answerNavSelector.replace("{0}", $answerArea.attr("data-key")));
						if (result != null && result.length > 0) {
							if (!$cardLi.hasClass("active_li")) {
								$cardLi.addClass("active_li");
							}
						} else {
							if (userAnswer.userAnswer == '' || JSON.parse(userAnswer.userAnswer).length == 0) {
								$cardLi.removeClass("active_li");
							} else {
								if (!$cardLi.hasClass("active_li")) {
									$cardLi.addClass("active_li");
								}
							}
						}
						$answerArea.data("answer", userAnswer);
					}
				});
			});
		},
		bindSingleChooseAreaEvent : function($answerArea, $cardLi){ //单选答案区域事件
			$answerArea.find("li").on("click", function(){
				var $li = $(this);
				// 1 样式变换
				$li.siblings().removeClass("active_li");
				$li.addClass("active_li");
				if (!$cardLi.hasClass("active_li")) {
					$cardLi.addClass("active_li");
				}
				// 2 答案数据保存
				var userAnswer = $answerArea.data("answer");
				userAnswer.userAnswer = JSON.stringify( [ $li.attr("data-value") ]);
				$answerArea.data("answer", userAnswer);
			});
		},
		bindMutipleChooseAreaEvent : function($answerArea, $cardLi) { //多选答案区域事件
			$answerArea.find("li").on("click", function(){
				var $li = $(this);
				// 1 样式变换
				if ($li.hasClass("active_li")) {
					$li.removeClass("active_li");
				} else {
					$li.addClass("active_li");
				}
				if ($answerArea.find("li.active_li").length > 0) {
					if (!$cardLi.hasClass("active_li")) {
						$cardLi.addClass("active_li");
					}
				} else {
					if ($answerArea.next('.canvas_area').find('img').length == 0) {
						$cardLi.removeClass("active_li");
					}
				}
				// 2 拼接数据
				var answer = [];
				$answerArea.find("li.active_li").each(function(){
					answer.push($(this).attr("data-value"));
				})
				// 3 答案数据保存
				var userAnswer = $answerArea.data("answer");
				userAnswer.userAnswer = JSON.stringify(answer);
				$answerArea.data("answer", userAnswer);
			});
		},
		bindBlankAreaEvent : function($answerArea, $cardLi) { // 填空答案区域事件
			$answerArea.find("input.text_answer_input").on("input", function(){
				// 1 拼接数据
				var answer = [], isAnswered = false;
				$answerArea.find("input.text_answer_input").each(function(){
					answer.push($(this).val().trim());
					if ($(this).val().trim().length > 0) {
						isAnswered = true;
					}
				});
				if (isAnswered) {
					if (!$cardLi.hasClass("active_li")) {
						$cardLi.addClass("active_li");
					}
				} else {
					if ($answerArea.next('.canvas_area').find('img').length == 0) {
						$cardLi.removeClass("active_li");
					}
				}
				// 2 答案数据保存
				var userAnswer = $answerArea.data("answer");
				userAnswer.userAnswer = JSON.stringify(answer);
				$answerArea.data("answer", userAnswer);
			});
		},
		bindCommonAnswerAreaEvent : function($answerArea, $cardLi) { // 公用默认答案区域事件
			$answerArea.find("textarea").on("input", function(){
				// 1 拼接数据
				var answer = [];
				answer.push($(this).val());
				if ($(this).val().trim().length > 0) {
					if (!$cardLi.hasClass("active_li")) {
						$cardLi.addClass("active_li");
					}
				} else {
					if ($answerArea.next('.canvas_area').find('img').length == 0) {
						$cardLi.removeClass("active_li");
					}
				}
				// 2 答案数据保存
				var userAnswer = $answerArea.data("answer");
				userAnswer.userAnswer = JSON.stringify(answer);
				$answerArea.data("answer", userAnswer);
			});
		}
	};
	
	// 通过字面量创造一个对象，存储我们需要的共有方法
    var methods = {
    	// 在字面量对象中定义每个单独的方法
        init: function(options) {
        	// 在每个元素上执行方法
        	// 为了更好的灵活性，对来自主函数，并进入每个方法中的选择器其中的每个单独的元素都执行代码
            return this.each(function() {
            	// 为每个独立的元素创建一个jQuery对象
                var $this = $(this);
                // 尝试去获取settings，如果不存在，则返回“undefined”
                var settings = $this.data('questionPanel');
                // 如果获取settings失败，则根据options和default创建它
                if(typeof(settings) == 'undefined') {
                    var defaults = {
                        questionNode:null
                    };
                    settings = $.extend({}, defaults, options);
                    // 保存新创建的settings
                    if(settings.questionNode!=null) settings.questionNode.panelObj = $this;
                    $this.data('questionPanel', settings);
                } else {
                    settings = $.extend({}, settings, options);
                }

                initQuestionPanle($this,settings);
            });
        },
        getAnswerXml : function() { //获取题目xml
        	var $panel = $(this), settings = $panel.data('questionPanel');
        	return AnswerMethod.getUserAnswerXml(settings);
        },
        destroy: function(options) {

        }
    };
	$.fn.questionPanel = function() {
		// 获取方法
		var method = arguments[0];
		// 检验方法是否存在
		if (methods[method]) {
			// 如果方法存在，存储起来以便使用
			// 注意：我这样做是为了等下更方便地使用each（）
			method = methods[method];
			// 我们的方法是作为参数传入的，把它从参数列表中删除，因为调用方法时并不需要它
			arguments = Array.prototype.slice.call(arguments, 1);
		} else if (typeof (method) == 'object' || !method) {// 如果方法不存在，检验对象是否为一个对象（JSON对象）或者method方法没有被传入
			// 如果我们传入的是一个对象参数，或者根本没有参数，init方法会被调用
			method = methods.init;
		} else {
			// 如果方法不存在或者参数没传入，则报出错误。需要调用的方法没有被正确调用
			$.error('Method ' + method + ' does not exist on jQuery.pluginName');
			return this;
		}
		// 用apply方法来调用我们的方法并传入参数
		return method.apply(this, arguments);
	};
})(jQuery);