(function($){
    let VideoAnswer = window.VideoAnswer = {
        userId : "",
        examId : "",
        stepId : "",
        testId : "",
        paper : {},
        questionTimer : [],
        currentQuestionId : '',
        currentQuestion: {},
        videoObj : null,
        videoReady : false,
        questionMap : null,
        startAnswerTime : null,
        timerInterval : null,
        originWidth: null,
        originHeight : null,
        init : function(){
            this.startAnswerTime = new Date();
            this.paper.files.forEach((file) => {
                if (file.fileExt == ".mp4" || file.fileExt == "mp4"){
                    $(".video_box .video_btns").before('<video id="video" preload="auto" controls="controls" src="'+file.url+'"></video>');
                    $(".video_box .video_btns").css("width", $("#video").width());
                }
            });
            this.videoObj = document.getElementById("video");
            this.initQuestions();
            this.initAnswerCard();
            this.initAnswerArea();
            this.initQuestionTimer();
            this.initEvent();
        },
        initQuestions: function(){
            let _this = this;
            this.questionMap = new Map();
            this.paper.nodes.forEach((node) => {
                if ((node.level == 3 || node.level == 4) && node.question != null) {
                    node.question.externalNo = node.externalNo;
                    _this.questionMap.set(node.question.questionId, node.question);
                }
            })
        },

        initAnswerCard : function(){
            let typeTemplate = '<li class="item type">{0}</li>';
            let questionTemplate = '<li class="item question" id="{1}"><div class="question_no">{0}</div></li>'
            this.paper.nodes.forEach((node) => {
                if (node.level == 2) {
                    $(".answer_card .question-list").append(CommUtils.formatStr(typeTemplate, node.name));
                }else if (node.level == 3) {
                    $(".answer_card .question-list").append(CommUtils.formatStr(questionTemplate, node.externalNo, node.question.questionId));
                }
            });
            this.currentQuestionId = $(".answer_card .question-list .item.question").attr("id");
        },
        initAnswerArea : function(){
            let _this = this;
            let $question = $(".answer_question_area .answer_question .question_container .question");
            let template = $("#questionTemplate").html();
            this.paper.nodes.forEach((node) => {
                let question = node.question;
                if (node.level == 3) {
                    $question.append(CommUtils.formatStr(template, question.stem.richText, question.questionId));
                    let $answerArea = $question.find(".answer_area:last");
                    if (question.type != null && CommUtils.isNotEmpty(question.type.code)) {
                        if ('01, 02, 11, 12, 27, 28'.indexOf(question.type.code) > -1) { //choice
                            $answerArea.append('<ul class="choice"></ul>');
                            question.options.forEach((option) => {
                                $answerArea.find("ul.choice").append("<li data-value='"+option.alias+"'>"+option.alias+"</li>");
                            });
                            $answerArea.find(".choice li").on("click", function(){
                                if (question.type.code == '02') $(this).addClass("active");  //Multiple choice
                                else $(this).addClass("active").siblings().removeClass("active");
                            });
                        }else if ('04' == question.type.code) {  //judgement
                            $answerArea.append('<ul class="judgement">' +
                                '<li data-value="T">对</li><li data-value="F">错</li></ul>');
                            $answerArea.find(".judgement li").on("click", function(){
                                $(this).addClass("active").siblings().removeClass("active");
                            });
                        }else if ('03, 10, 14, 43'.indexOf(question.type.code) > -1) {  //blank
                            let blankAmount = _this.ifEmpty(question.answer.label).split("@*@")[0].split("@#@").length;
                            for (let i = 0; i < blankAmount; i++) {
                                $answerArea.append("<input class='answer_input' placeholder='点击输入答案' type='text'/>");
                            }
                        }else {   //Short answer
                            $answerArea.append("<textarea cols='5'></textarea>")
                        }
                    }else {
                        $answerArea.append("<textarea cols='5'></textarea>")
                    }
                }
            });
        },
        initQuestionTimer : function(){
            let _this = this;
            this.paper.answerCard.timelines.forEach((time) => {
                _this.questionTimer.push({"questionId" : time.questionId, "start": time.triggerTime, "end": null});
            })
            for (let i = 0; i < _this.questionTimer.length; i++) {
                if (i > 0 && i < _this.questionTimer.length) {
                    _this.questionTimer[i - 1].end = _this.questionTimer[i].start;
                }
            }
        },
        chooseQuestion : function(init) {
            let _this = this;
            this.questionTimer.forEach((question) => {
                if (question.questionId == _this.currentQuestionId) {
                    if (init == undefined || init) {
                        _this.videoObj.currentTime = question.start;
                    }
                    _this.currentQuestion = question;
                }
            })
            $("#" + _this.currentQuestionId).addClass("active").siblings().removeClass("active");
            let question = this.questionMap.get(_this.currentQuestionId);
            $(".answer_question_area .answer_question .title .q_no").text('第{0}题'.replace('{0}', question.externalNo));
            $(".answer_question_area .question_container .question .item[data-id='"+_this.currentQuestionId+"']").removeClass("hidden").siblings().addClass("hidden");
        },
        getUserAnswer : function(){
            let _this = this;
            let $answerAreas = $(".answer_question_area .answer_question .question_container .question .item");
            let userAnswers = [];
            $answerAreas.each(function(){
                let $answerArea = $(this).find(".answer_area");
                let userAnswer = "";
                if ($answerArea.find("ul.choice").length > 0 || $answerArea.find("ul.judgement").length > 0 ) {
                    if ($answerArea.find("ul > li.active").length > 0)  {
                        $answerArea.find("ul > li.active").each(function(){
                            userAnswer += $(this).attr("data-value");
                        })
                    }
                }else if ($answerArea.find("textarea").length > 0 ) {
                    userAnswer = $answerArea.find("textarea").text();
                }else if ($answerArea.find("input.answer_input").length > 0 ) {
                    let blankAnswer = [];
                    $answerArea.find("input.answer_input").each(function(){
                        if (CommUtils.isNotEmpty($(this).val())) {
                            blankAnswer.push($(this).val().trim());
                        }
                    })
                    userAnswer = JSON.stringify(blankAnswer);
                }
                userAnswers.push({
                    "questionId" : $(this).attr("data-id"),
                    "parentId" : _this.questionMap.get($(this).attr("data-id")).parentId,
                    "userAnswer" : userAnswer
                });
            });
            return userAnswers;
        },
        initEvent : function(){
            let _this = this;
            this.videoObj.addEventListener("durationchange", function(e){
                _this.videoReady = true;
                _this.originWidth = $("#video").width();
                _this.originHeight = $("#video").height();
                $(".video_box .resize").css({"top": _this.originHeight ,"left": _this.originWidth});
                $(".video_box .resize").on("mousedown", function(e1){
                    let top = $(this).position().top;
                    let left = $(this).position().left;
                    let maxWidth = $(".video_box").width() * 0.7;
                    $(".video").on("mousemove", function(e2){
                        if (left + e2.clientX - e1.clientX < _this.originWidth || left + e2.clientX - e1.clientX > maxWidth) return;
                        $(".video_box .resize").css({
                            "top": top + e2.clientY - e1.clientY + "px",
                            "left": left + e2.clientX - e1.clientX + "px"
                        });
                        $("#video").css({
                            "height": top + e2.clientY - e1.clientY + "px",
                            "width": left + e2.clientX - e1.clientX + "px"
                        });
                        $(".video_box .video_btns").css("width", $("#video").width());
                    });
                    $(document).one('mouseup', function(e2) {
                        e2.stopPropagation();
                        $(".video").unbind('mousemove');
                    });
                });

                _this.chooseQuestion(_this.currentQuestionId);
                //_this.questionTimer[_this.questionTimer.length - 1].end = parseInt( _this.videoObj.duration);
                _this.videoObj.onplay = function () {
                    let videoInternal = setInterval(function(){
                        let startTime = _this.videoObj.currentTime;
                        if (!_this.videoObj.paused && _this.currentQuestion.end != null && (parseInt(_this.videoObj.currentTime) == _this.currentQuestion.end && startTime != parseInt(_this.videoObj.currentTime))){
                            _this.videoObj.pause();
                            _this.showAnswerPanel();
                        }
                    }, 200);
                    _this.videoObj.onpause = function(){
                        clearInterval(videoInternal);
                        if (_this.currentQuestion.end == null && _this.videoObj.currentTime == _this.videoObj.duration){ //最后一题
                            _this.showAnswerPanel();
                        }
                    }
                }
                _this.videoObj.ontimeupdate = function(){
                    if (_this.videoObj.paused) {
                        let currentViewTime = _this.videoObj.currentTime;
                        if (parseInt(currentViewTime) ==  _this.currentQuestion.end){

                        }else if (currentViewTime < _this.currentQuestion.start || currentViewTime > _this.currentQuestion.end){
                            _this.questionTimer.forEach((question) => {
                                if (question.questionId != _this.currentQuestionId) {
                                    if (question.end == null) {
                                        if (question.start <= currentViewTime) {
                                            _this.currentQuestion = question;
                                            _this.currentQuestionId = question.questionId;
                                        }
                                    }else{
                                        if (question.start <= currentViewTime && currentViewTime < question.end) {
                                            _this.currentQuestion = question;
                                            _this.currentQuestionId = question.questionId;
                                        }
                                    }
                                    _this.chooseQuestion(false);
                                }
                            })
                        }
                    }
                }

            });
            $(".answer_card .question-list .item.question").on("click", function(){
                if (_this.videoReady) {
                    $(this).addClass("active").siblings().removeClass("active");
                    _this.currentQuestionId = $(this).attr("id");
                    _this.hideAnswerPanel();
                    _this.chooseQuestion();
                }else {
                    alert("视频加载中...");
                }
            });

            $(".answer_question_area .answer_question .close").on("click", function(){
                _this.hideAnswerPanel();
            });

            let timerInterval = setInterval(function(){
                let time = new Date().getTime() - _this.startAnswerTime.getTime();
                let itemStr = _this.calculateTimeStr(parseInt(time/1000));
                $(".timer_area b").text(itemStr);
            }, 1000);
        },
        showAnswerPanel : function(){
            $(".answer_question_area").css("display", "block");
        },
        hideAnswerPanel : function(){
            $(".answer_question_area").css("display", "none");
        },
        reView:function(){
            this.videoObj.currentTime = this.currentQuestion.start;
            this.hideAnswerPanel();
            this.videoObj.play();
        },
        nextQuestion : function(){
            let $next = $("#" + this.currentQuestionId).next();
            let nextQuestionId = '';
            if (!$next.hasClass("question")) {
                nextQuestionId = $next.next().attr("id");
            }else{
                nextQuestionId = $next.attr("id");
            }
            if (CommUtils.isEmpty(nextQuestionId)) {
                this.hideAnswerPanel();
            }else {
                this.currentQuestionId = nextQuestionId;
                this.chooseQuestion();
                this.hideAnswerPanel();
                this.videoObj.play();
            }
        },
        ifEmpty: function(obj, defautStr) {
            if (defautStr == undefined) {
                defautStr = '';
            }
            if (obj == undefined || obj == null) {
                return defautStr;
            }
            return '' + obj;
        },
        submit : function(){
            let _this = this;
            if (this.timerInterval != null) clearInterval(this.timerInterval);
            let userAnswers = this.getUserAnswer();
            let param = {
                'answers' : userAnswers,
                'testId': _this.testId,
                'paperId' : _this.examId,
                'userId' : _this.userId,
                'stepId' : _this.stepId,
                'fileId' : _this.fileId,
                'startTime': CommUtils.formatDate(_this.startAnswerTime, "yyyy-MM-dd HH:mm:ss"),
                'endTime': CommUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss")
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
                    if(data.status == "0" || !data.data || CommUtils.isEmpty(data.data.businessId)){
                        if (!CommUtils.isEmpty(data.message)) {
                            CommUtils.tipBoxV2(data.message, 3000, function(){

                            });
                        } else {
                            CommUtils.tipBoxV2(i18n['submit_failed'], 3000, function(){

                            });
                        }
                    }else{
                        // CommUtils.tipBoxV2(i18n['submit_success'], 3000, function(){
                        //     data.status = "success";
                        //     data.type = "answer";
                        //     window.opener && window.opener.postMessage(data, "*");
                        //     window.parent.postMessage(data, "*");
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
        },
        doPlayPause: function(){
            if (this.videoObj.paused) this.videoObj.play();
            else this.videoObj.pause();
        },
        resize : function(type){
            if (type == 0) { //narrow
                if ($("#video").width() == this.originWidth) return;
                else if ($("#video").width() * 0.9 < this.originWidth) {
                    $("#video").css({"width": this.originWidth, "height" : this.originHeight});
                }
                else $("#video").css({"width": $("#video").width() * 0.9, "height" : $("#video").height() * 0.9});
            }else {
                let maxWidth = $(".video_box").width() * 0.7;
                if ($("#video").width() * 1.1 > maxWidth) return;
                else {
                    $("#video").css({"width": $("#video").width() * 1.1, "height" : $("#video").height() * 1.1});
                }
            }
            $(".video_box .video_btns").css("width", $("#video").width());
            $(".video_box .resize").css({"top": $("#video").height(), "left": $("#video").width()});
        }
    }
})(jQuery);