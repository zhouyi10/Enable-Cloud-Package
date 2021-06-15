(function ($, window){
    var MarkV2Main = window.MarkV2Main={
        groupType : "1", //默认按学生
        lastQuestionId : "",
        currentQuestionId: "", //当前选中题目标识
        thresholdScore : 0.8, //  对错阀值
        questionMap : {},
        questionAnswerMap: {},
        answerId : "",
        userId : "",
        fullName : "",
        groupIds: "",
        markListAnswers: [],
        markListAnswer: {},
        isScoreChange: false,
        isCanvasChange: false,
        init : function(){
            let _this = this;
            this.initQuestionMap();
            this.initQuestionAnswerMap();
            MarkContent.buildMarkNav(); //初始化题号导航栏 并初始化默认题目
            MarkContent.buildContent();
            MarkContent.initEvent();
            this.refreshMarkingStatus();
            window.onbeforeunload = function () {
                $.get(_this.markStatusCleanlUrl + "?testId=" + _this.testId);
            };
            _this.initEvent();
            _this.resize();
            _this.verification();
        },
        refreshMarkingStatus : function () {
            let _this = this;
            setInterval(function () {
                $.post(_this.markStatusUpdatelUrl, {"testId": _this.testId, "markingCacheInfo" : _this.markingCacheInfo});
            }, 4000);
        },
        initEvent: function() {
            var _this = this;
            //批阅暂存
            $("#temporaryBtn").click(function(){
                _this.doSave("2", _this.test.answers);
            });
            //批阅完成
            /*$("#completeMarkBtn").click(function(){
                _this.doSave("1", _this.test.answers);
            });*/
            // 打分板加减分数
            $(".minus button").off("click").on("click",function () {
                MarkPage.setMarkScore("-1");
            });
            $(".plus button").off("click").on("click",function () {
                MarkPage.setMarkScore("+1");
            });

            // 手动修改分数
            $("#answerByScore").blur(function () {
                if ($(this).val() && ((Number)($(this).val()) !== MarkV2Main.markListAnswer.answerScore)) {
                    MarkPage.setMarkScore($(this).val());
                }
            });

            // 上一人/下一人 保存题目批阅信息
            $(".right .answerSwitch").off("click").on("click",function () {
                if ($(this).hasClass('disabled')) {
                    return;
                }

                // 获取当前答案的下标, 保存后可能被修改
                var trIndex = (Number)($(".markListRateTable tbody .active").attr("trIndex"));

                // 保存上一人的数据
                MarkContent.doSaveByOneQuestion();

                // 开始切换，判断操作
                if ($(this).hasClass("pre_student")) {
                    trIndex = trIndex - 1;
                } else if ($(this).hasClass("next_student")) {
                    // 最后一个修改并点击下一个， 跳转至第一个
                    trIndex = trIndex + 1;
                } else return;

                /*if (trIndex === 0) {
                    $(".right .switch .pre_student").addClass("disabled");
                } else if (trIndex === MarkV2Main.markListAnswers.length-1) {
                    $(".right .switch .next_student").addClass("disabled");
                }*/

                var markListRateTableTrs = $(".markListRateTable tbody tr");

                markListRateTableTrs.removeClass("active");

                var trLength = markListRateTableTrs.length;

                if (trIndex < 0) {
                    trIndex = trLength + trIndex;
                } else if (trIndex >= trLength) {
                    trIndex = trIndex - trLength;
                }

                var thisTr = markListRateTableTrs.eq(trIndex);
                // 获取对应数据
                var index = (Number)(thisTr.attr("answerIndex"));

                MarkV2Main.markListAnswer = MarkV2Main.markListAnswers[index];

                MarkPage.buildAnswer(trIndex);
                // 列表选中状态
                thisTr.addClass("active");

                // 该题目是否已全部批阅完成
                /*if (!_this.allMarked) {
                    var allMarked = true; //这道题目批了以后全部批阅完成
                    $.each(MarkV2Main.markListAnswers.answers, function(a, answer) {
                        if (answer.markStatus != '1') {
                            allMarked = false;
                            return false; //break circle
                        }
                    });
                    if (allMarked) {
                        _this.allMarked = true;
                        layer.tips(window.i18n['tip_all_stu_mark_in_ques'], this, { tips: [3, '#11bb00'],time: 3000});
                    }
                } else { // 最后一个  最前一个 提示
                    if (trIndex == 0 && !switchOperation.indexOf("next")) {
                        layer.tips(window.i18n['pre_to_first'], this, { tips: [3, '#ce430e'],time: 1500});
                    } else if(trIndex == trLength -1 && !switchOperation.indexOf("last")) {
                        layer.tips(window.i18n['back_to_last'], this, { tips: [3, '#ce430e'],time: 1500});
                    }
                }*/
            });

            // 批阅列表分数排序
            $(".right .markListTableTitle .markListRateScore .fa-sort").on("click", function () {
                var isSortAsc = $(this).data("asc");
                MarkPage.buildMarkList("answerScore", !isSortAsc);
                MarkPage.buildAnswer(0);
                $(this).data("asc", !isSortAsc);
            });
            // 批阅列表批阅状态排序
            $(".right .markListTableTitle .markListRateStatus .fa-sort").on("click", function () {
                var isSortAsc = $(this).data("asc");
                MarkPage.buildMarkList("markStatus", !isSortAsc);
                MarkPage.buildAnswer(0);
                $(this).data("asc", !isSortAsc);
            });
        },
        resize: function () {
            let height = $(window).height() - 15 - $(".container .top").outerHeight() - 15 - 10;  //批阅部分和菜单栏高度
            $('.container .content .l .left, .container .content .r .right').css('min-height', height);
            let markDiv = height - $('.container .content .l .left .student_answer').outerHeight() - $('.container .content .l .left .quetstion').outerHeight();
            $('.container .content .l .left .canvas_root_area').css('height', markDiv);
            $('.page_nav').css('height', height - $('.container .content .r .right .row:eq(0)').outerHeight() - $('.container .content .r .right .row:eq(1)').outerHeight() - 3);
            let cavasContainerHeight = height - $('.left .student_answer').outerHeight() - $('.left .quetstion').outerHeight() -
                $('.left .canvas_root_area .tools').outerHeight();
            $('.left .canvas_root_area .canvas_container').css('height', cavasContainerHeight);
        },
        verification: function () {
            var _this = this;

            var isExist = true;
            var message = "";
            if (CommUtils.isEmpty(_this.questionMap) || CommUtils.isEmpty(_this.questionMap.size) || _this.questionMap.size == 0) {
                isExist = false;
                message = i18n['error_not_questions'];
            } else if (CommUtils.isEmpty(_this.questionAnswerMap) || CommUtils.isEmpty(_this.questionAnswerMap.size) || _this.questionAnswerMap.size == 0) {
                isExist = false;
                message = i18n['error_not_answers']
            }

            if (!isExist) {
                layer.alert(message,{closeBtn : 0 ,title: i18n['tipBox_title'], btn: i18n['confirm']}, function(){
                    if (window.opener || window.parent) {
                        window.opener= null;
                        window.open("","_self");
                        window.close();
                    }
                    layer.closeAll();
                });
            }
        },
        //初始化题目Map(questionId, question)
        initQuestionMap : function(){
            let _this = this;
            let exam = this.exam;
            this.questionMap = new Map();
            $.each(exam.nodes, function(index, obj){
                if (obj.level == 3) {  //question
                    if (obj.children == null){  //不含子题目
                        let questionId = obj.question.questionId;
                        obj.question.questionScore = obj.points;
                        obj.question.answer = CommUtils.processAnswer(obj.question.answer);
                        _this.questionMap.set(questionId, obj.question);
                    }else{   //包含子题目
                        $.each(obj.children, function(index, child){
                            child.question.questionScore = child.points;
                            child.question.parentQuestionContent = obj.question.stem.richText;  //父题目的题干
                            child.question.answer = CommUtils.processAnswer(child.question.answer);
                            _this.questionMap.set(child.question.questionId, child.question);
                        });
                    }
                }
            });
        },
        initQuestionAnswerMap: function() {
            var _this = this;
            _this.questionAnswerMap = new Map();
            $.each(MarkV2Main.test.answers, function (index, answer) {
                var answerTemp = _this.questionAnswerMap.get(answer.questionId);
                if (CommUtils.isEmpty(answerTemp)) {
                    answerTemp = new Array();
                    _this.questionAnswerMap.set(answer.questionId, answerTemp);
                }
                answerTemp.push(answer);
            });
        },
        destroyBeforeunload: function(type) {
            if (type === "canvas") {
                this.isCanvasChange = false;
            } else {
                this.isScoreChange = false;
            }
            $(window).unbind('beforeunload');
        },
        createBeforeunload: function(type) {
            var isChange = true;
            if (type === "canvas") {
                isChange = this.isScoreChange;
                this.isCanvasChange = true;
            } else {
                isChange = this.isCanvasChange;
                this.isScoreChange = true;
            }
            if (!isChange) { // 如果之前另一个绑定过, 不再绑定
                $(window).on('beforeunload', function() { // 阻止关闭页面
                    return ""; // chrome 无法控制提示信息, 且这里面无法执行弹窗|耗时的ajax如保存答案数据等操作. IE可以显示消息
                });
            }
        },
        doSave : function(completeMarkStatus, answers){  //"0":点击下一题保存， "2":点击批阅完成保存
            if (CommUtils.isEmpty(answers)) {
                return;
            }
            let _this = this;
            let param = {
                "testId" : _this.test.testId,
                "examId" : _this.exam.paperId,
                "type" : "2", // 按题批阅全部使用暂存模式
                "answers" : answers
            };
            CommUtils.tryLock($(document.body));
            _this.doSaveCanvas();
            $.ajax({
                url: _this.completeMarkUrl,
                type: "POST",
                contentType: 'application/json',
                data:JSON.stringify(param),
                dataType: "json",
                success: function (data) {
                    CommUtils.unLock($(document.body));
                    MarkV2Main.destroyBeforeunload();
                    if (data.status == "1"){
                        if (completeMarkStatus == "2") {
                            layer.alert(i18n['tip_complete_mark'],{closeBtn : 0 ,title: i18n['tipBox_title'], btn: i18n['confirm']}, function(){
                                var result = {'type':'mark', 'data': 'success'};
                                if(typeof(WeixinJSBridge) != "undefined"){ //微信
                                    window.history.go(-1);
                                } else if (window.parent != window) { //子页面
                                    window.parent.postMessage(result, "*");
                                } else if (window.opener) { //新开窗口
                                    window.opener.postMessage(result, "*");
                                }
                                if (window.opener || window.parent) {
                                    window.opener= null;
                                    window.open("","_self");
                                    window.close();
                                }
                                layer.closeAll();
                            });
                        }
                    }else{
                        layer.alert(i18n['tip_save_err'], {btn: i18n['confirm']});//"操作失败，请稍后重试!"
                    }
                },
                error : function(){
                    CommUtils.unLock($(document.body));
                    layer.alert(i18n['common_error_msg'], {btn: i18n['confirm']});
                }
            });
        },
        doSaveCanvas: function() {
            // 保存当前题目图片信息
            var containerOp = $(LeftMarkArea.canvasContainerSelector).data('container');
            if (containerOp && containerOp.modify) {
                containerOp.save();
            }
        },
        markListAnswersSort: function (sortType, isSortAsc) { // 排序
            var _this = this;
            if (CommUtils.isEmpty(MarkV2Main.markListAnswers) || MarkV2Main.markListAnswers.length == 0) {
                return;
            }
            if (CommUtils.isEmpty(sortType)) sortType = "markStatus";
            if (CommUtils.isEmpty(isSortAsc)) isSortAsc = true;
            MarkV2Main.markListAnswers.reverse(); // 先倒叙, 不然for循环后会将原list倒叙
            for (var i = 0; i < MarkV2Main.markListAnswers.length; i++) {
                for (var j = i; j < MarkV2Main.markListAnswers.length; j++) {
                    var isTrue = false;
                    if (sortType === "markStatus") {
                        if (isSortAsc) {
                            isTrue = (MarkV2Main.markListAnswers[i].markStatus !== "0");
                        } else {
                            isTrue = (MarkV2Main.markListAnswers[i].markStatus !== "1");
                        }
                    } else if (sortType === "answerScore") {
                        if (isSortAsc) {
                            if (MarkV2Main.markListAnswers[i].markStatus !== "0") {
                                isTrue = (MarkV2Main.markListAnswers[i].answerScore <= MarkV2Main.markListAnswers[j].answerScore);
                            } else {
                                isTrue = true;
                            }
                        } else {
                            if (MarkV2Main.markListAnswers[i].markStatus !== "0") {
                                isTrue = true;
                            } else {
                                isTrue = (MarkV2Main.markListAnswers[i].answerScore > MarkV2Main.markListAnswers[j].answerScore);
                            }
                        }
                    } else {
                        return;
                    }
                    if (isTrue) {
                        var temp = MarkV2Main.markListAnswers[i];
                        MarkV2Main.markListAnswers[i] = MarkV2Main.markListAnswers[j];
                        MarkV2Main.markListAnswers[j] = temp;
                    }
                }
            }
        },
    };
    var MarkContent = {
        kindNameArray : ['一', '二', '三', '四', '五', '六', '七', '八', '九', '十'],
        buildMarkNav : function(){
            let _this = this;
            let exam = MarkV2Main.exam;
            let kindTemplate = $("#kind_template").html();
            let kindNameTemplate = i18n['kind_name_template'];
            $.each(exam.nodes, function(index, obj){
                if (obj.level == 1){   /* 卷别*/
                    $(".page_nav").append(
                        CommUtils.formatStr(kindTemplate, {
                            "name" : CommUtils.isEmpty(obj.name) ? kindNameTemplate.replace('{0}', _this.kindNameArray[index]) : obj.name,
                        })
                    );
                }
                if (obj.level == 2 ){  /* 题型 */
                    let name = obj.name == null ? "" : obj.name;
                    $(".page_nav .paper:last").append("<h5>"+name+"</h5><table class=\"squares\"></table>");
                }
                if (obj.level == 3){  /* 题目*/
                    if (obj.children == null){  //大题
                        let questionId = obj.question.questionId;

                        let totalCount = 0;
                        let questionAnswerList = MarkV2Main.questionAnswerMap.get(questionId);
                        if (!CommUtils.isEmpty(questionAnswerList)) {
                            totalCount = questionAnswerList.length;
                        }
                        if ($(".page_nav table.squares:last").find(".questions").length == 0   //新增一个<tr>
                         || $(".questions:last").find(".question").length == 3){
                            $(".page_nav table.squares:last").append("<tr class='questions'><td class='question' id='"+questionId+"'><div class=\"square\"><b>"+obj.externalNo+"</b><p><span class='marked_count'>0</span>/"+totalCount+"</p></div></td></tr>");
                        }else{
                            $(".questions:last").append("<td class='question' id='"+questionId+"'><div class=\"square\"><b>"+obj.externalNo+"</b><p><span class='marked_count'>0</span>/"+totalCount+"</p></div></td>");
                        }
                    }else{                      //小题
                        $.each(obj.children, function(index, child){
                            let questionId = child.question.questionId;
                            let totalCount = 0;
                            let questionAnswerList = MarkV2Main.questionAnswerMap.get(questionId);
                            if (!CommUtils.isEmpty(questionAnswerList)) {
                                totalCount = questionAnswerList.length;
                            }
                            if ($(".page_nav table.squares:last").find(".questions").length == 0   //新增一个<tr>
                                || $(".questions:last").find(".question").length == 3){
                                $(".page_nav table.squares:last").append("<tr class='questions'><td class='question' id='"+questionId+"'><div class=\"square\"><b>"+child.externalNo+"</b><p><span class='marked_count'>0</span>/"+totalCount+"</p></div></td></tr>");
                            }else{
                                $(".questions:last").append("<td class='question' id='"+questionId+"'><div class=\"square\"><b>"+child.externalNo+"</b><p><span class='marked_count'>0</span>/"+totalCount+"</p></div></td>");
                            }
                        });
                    }
                }
            });
            //如果题目不足,补充空的<td>,防止样式走样
            if ($(".questions:last").find(".question").length == 1){
                $(".questions:last").append("<td><div class=\"blank\"></div></td><td><div class=\"blank\"></div></td>");
            }
            if ($(".questions:last").find(".question").length == 2){
                $(".questions:last").append("<td><div class=\"blank\"></div></td>");
            }
        },
        buildContent: function() {
            let _this = this;
            //初始化已批阅人数
            this.buildMarkNum();
            //默认选中第一题
            $(".page_nav .question:eq(0)").find("div.square").addClass("ing");
            $(".pre_question").addClass("disabled");
            if ($(".page_nav .question").length < 2){
                $(".next_question").addClass("disabled");
            }
            MarkV2Main.currentQuestionId = MarkV2Main.lastQuestionId = $(".page_nav .question:eq(0)").attr("id");
            MarkPage.changeQuestion();
        },
        initEvent : function(){
            let _this = this;

            //绑定卷别展开收取事件
            $("[data-toggle='tooltip']").tooltip();
            $(".page_nav .kind").click(function(){
                $(this).next(".paper").toggle();
                $(this).find("i").toggleClass("fa-chevron-down");
            });
            //题号点击事件
            $(".question").click(function(){
                let $this = this;
                _this.changeQuestionTip(function(){
                    let firstQuestionId = $(".question:first").attr("id");
                    let lastQuestionId = $(".question:last").attr("id");
                    if (MarkV2Main.currentQuestionId == $(this).attr("id")) return;
                    MarkV2Main.lastQuestionId = MarkV2Main.currentQuestionId;
                    MarkV2Main.currentQuestionId = $($this).attr("id");
                    $(".question div.square").removeClass("ing");
                    $($this).find("div.square").addClass("ing");
                    $(".pre_question,.next_question").removeClass("disabled");
                    if ($($this).attr("id") == firstQuestionId){  //点击第一题，禁止上一题按钮
                        $(".pre_question").addClass("disabled");
                    }
                    if ($($this).attr("id") == lastQuestionId){ //点击最后一题，禁止下一题按钮
                        $(".next_question").addClass("disabled");
                    }
                    MarkPage.changeQuestion();
                });
            });
            //上一题事件
            $(".pre_question").click(function(){
                _this.changeQuestionTip(function(){
                    let firstQuestionId = $(".question:first").attr("id");
                    let selectIndex = 0;
                    $(".question").each(function(index){
                        if ($(this).find("div.square").hasClass('ing')) selectIndex = index;  //当前题目
                    });
                    $(".next_question").removeClass("disabled");
                    if (selectIndex - 1 < 0) return; //没有上一题
                    $(".question div.square").removeClass("ing").each(function(index){
                        if (selectIndex - 1 == index){
                            $(this).addClass("ing");
                            MarkV2Main.lastQuestionId = MarkV2Main.currentQuestionId;
                            MarkV2Main.currentQuestionId = $(this).closest("td").attr("id");
                            if (firstQuestionId == $(this).closest("td").attr("id")){  //第一题，禁止上一题按钮
                                $(".pre_question").addClass("disabled");
                            }
                        }
                    });
                    MarkPage.changeQuestion();
                });
            });
            //下一题事件
            $(".next_question").click(function(){
                _this.changeQuestionTip(function(){
                    let lastQuestionId = $(".question:last").attr("id");
                    let selectIndex = 0;
                    $(".question").each(function(index){
                        if ($(this).find("div.square").hasClass('ing')) selectIndex = index;  //当前题目
                    });
                    $(".pre_question").removeClass("disabled");
                    if (selectIndex == $(".question").length-1) return; //没有下一题
                    $(".question div.square").removeClass("ing").each(function(index){
                        if (selectIndex + 1 == index){
                            $(this).addClass("ing");
                            MarkV2Main.lastQuestionId = MarkV2Main.currentQuestionId;
                            MarkV2Main.currentQuestionId = $(this).closest("td").attr("id");
                            if (lastQuestionId == $(this).closest("td").attr("id")){  //最后一题，禁止下一题按钮
                                $(".next_question").addClass("disabled");
                            }
                        }
                    });
                    MarkPage.changeQuestion();
                });

            });
        },
        //初始化批阅人数
        buildMarkNum : function(){
            let _this = this;
            let markNumMap = new Map();
            $.each(MarkV2Main.test.answers, function(index, obj){
                if (CommUtils.isEmpty(markNumMap.get(obj.questionId))){
                    if (obj.markStatus == "1") markNumMap.set(obj.questionId, 1);
                    else markNumMap.set(obj.questionId, 0);
                }else{
                    if (obj.markStatus == "1") markNumMap.set(obj.questionId, markNumMap.get(obj.questionId)+1);
                }
            });
            $(".question").each(function(){
                let questionId = $(this).attr("id");
                let totalCount = 0;
                let questionAnswerList = MarkV2Main.questionAnswerMap.get(questionId);
                if (!CommUtils.isEmpty(questionAnswerList)) {
                    totalCount = questionAnswerList.length;
                }
                let markedCount = markNumMap.get(questionId);
                $(this).find(".marked_count").text(markedCount);
                if (totalCount == markedCount && totalCount != 0){
                    $(this).find("div.square").addClass("over");
                }
            });
        },
        doSaveByOneQuestion : function(){  //保存每一题的批阅结果, 相当于以前的暂存
            let answers = new Array();
            $.each(MarkV2Main.markListAnswers, function(index, obj){
                if (obj.questionId == MarkV2Main.currentQuestionId && obj.changeStatus != undefined && obj.changeStatus){
                    answers.push(obj);
                    obj.changeStatus = false;
                }
            });
            if (answers.length > 0){
                MarkV2Main.doSave("0", answers);
            } else {
                MarkV2Main.doSaveCanvas();
            }
        },
        changeQuestionTip : function(callback){    //切换题目, 提示上一题还有没有批阅的情况
            let _this = this;
            let noMarkName = new Array();
            $.each(MarkV2Main.markListAnswers, function(index, obj){
                if (MarkV2Main.currentQuestionId == obj.questionId && obj.markStatus == '0'){
                    // noMarkName.push(obj.userName);
                    noMarkName.push("*");
                }
            });
            if (noMarkName.length > 0){
                let questionNo = $("#"+MarkV2Main.currentQuestionId).find(".square b").text();
                let tip = i18n['hava_some_student_not_be_mark'].replace('{0}', questionNo).replace('{1}', noMarkName.join(","));
                let index = layer.confirm(tip, {
                    btn: [i18n['all_mark_error'],i18n['ignore_and_continue'] ] //按钮 全部判错  忽略继续
                }, function(){
                    $.each(MarkV2Main.markListAnswers, function(index, obj){
                        if (MarkV2Main.currentQuestionId == obj.questionId && obj.markStatus == '0'){
                           obj.markStatus = '1';   //已批阅
                           obj.answerScore = 0;    //得0分
                           obj.answerStatus = "1"; //回答错误
                           obj.changeStatus = true;
                        }
                    });
                    _this.buildMarkNum();
                    MarkContent.doSaveByOneQuestion();  //保存上一题批阅数据
                    layer.close(index);
                    callback && callback();
                }, function(){
                    MarkContent.doSaveByOneQuestion();  //保存上一题批阅数据
                    layer.close(index);
                    callback && callback();
                });

            }else{
                MarkContent.doSaveByOneQuestion();  //保存上一题批阅数据
                callback && callback();
            }
        }
    };
    var MarkPage = {
        changeQuestion : function(){
            this.changeQuestionContent();
            this.changeMarkAnswerArea();
        },
        changeQuestionContent : function(){    //换题切换题干
            let question = MarkV2Main.questionMap.get(MarkV2Main.currentQuestionId);
            let answer = this.getShownAnswer(question.answer, question.type.code);
            let questionContent = "";
            if (CommUtils.isEmpty(question.parentQuestionContent)){
                questionContent = question.stem.richText;
            }else{    //包含大小题
                questionContent = question.parentQuestionContent + question.stem.richText;
            }
            $(".quetstion .title").empty().append("<div class='question_content'>"+questionContent+"</div>");
            if (question.options.length > 0){
                let optionHtml = "<ul>";
                let isShowOption = true;
                $.each(question.options, function(index, obj){
                    if (CommUtils.isEmpty(obj.lable)) {
                        isShowOption = false;
                        return false;
                    }
                    optionHtml += "<li>"+obj.alias+"."+obj.lable+"</li>"
                });
                if (isShowOption) {
                    $(".quetstion .title .question_content").append(optionHtml + "</ul>");
                }
            }
            $(".quetstion .question_score").text(question.questionScore);
            $(".quetstion .question_answer").html(answer);
            $(".question_content, .title_bar").click(function(){        //题干点击展开收起事件
                $('.toggle').trigger("click")
            });
        },
        getShownAnswer : function(answer, typeCode) {
            if (answer == null || answer == undefined || answer == '') {
                return '';
            }
            var answerHtml, answerText;
            if (typeof answer == 'string') {
                answerHtml = answerText = answer;
            } else {
                answerHtml = answer.strategy, answerText = answer.lable;
            }
            if ("04" == typeCode) {
                return answerText == 'T' ? i18n['question_right'] : answerText == 'F' ? i18n['question_wrong'] : '';
            }
            return CommUtils.ifEmpty(answerHtml, answerText);
        },
        getShownAnswerV2 : function(answer) {
            if (answer == null || answer == undefined || answer == '') {
                return '';
            }
            var _this = this;
            var answerHtml = "";
            $.each(answer.split("@#@"),function (index,obj) {
                answerHtml += "<li>"+obj.replace(/\\/g, "")+"</li>";
            });
            return CommUtils.ifEmpty(answerHtml);
        },
        changeMarkAnswerArea : function(){
            let _this = this;
            $(".table_mark_area").empty();
            if(MarkV2Main.groupType == '2') {
                MarkV2Main.markListAnswers = MarkV2Main.questionAnswerMap.get(MarkV2Main.currentQuestionId);
                var markListCanvaseAnswers = new Array();
                $.each(MarkV2Main.markListAnswers, function(index, obj){
                    if (!CommUtils.isEmpty(obj.canvases)) {
                        markListCanvaseAnswers.push(obj);
                    }
                });
                // MarkImage.init(classifyObj[0], classifyObj[0].canvases[0].canvasId, "", classifyObj);
                // 渲染批阅进度
                var trIndex = _this.buildMarkList();
                _this.buildShortcutButton();
                if (markListCanvaseAnswers.length > 0) {
                    LeftMarkArea.init(markListCanvaseAnswers[0].answerId, markListCanvaseAnswers[0].canvases[0].canvasId, markListCanvaseAnswers);
                }
                _this.buildAnswer(trIndex);
            }
        },
        // 初始化批阅进度区域
        buildMarkList : function(sortType, isSortAsc, answerId) {
            var _this = this;

            // 初始化批阅区域
            $(".markListRateTable tbody tr").remove();
            $(".markListTitle h3").empty();

            // 已批阅统计
            var markCount = 0;

            var trIndex = 0;

            /*if (MarkV2Main.markListAnswers.length === 1) {
                $(".right .switch .pre_student").addClass("disabled");
                $(".right .switch .next_student").addClass("disabled");
            }*/

            MarkV2Main.markListAnswersSort(sortType, isSortAsc);

            // 遍历答案
            $.each(MarkV2Main.markListAnswers, function (index, answer) {
                var markStatusTitle = "";
                var answerScore = "--";
                if (answer.markStatus === "1") {
                    markCount ++;
                    markStatusTitle = "<td width=\"35%\"><span class=\"green\">" + i18n["marked"] + "</span>";
                    answer.answerScore = answerScore = CommUtils.formatScore((CommUtils.isEmpty(answer.answerScore) ? 0 : answer.answerScore));
                } else {
                    markStatusTitle = "<td width=\"35%\"><span class=\"red\">" + i18n["un_mark"] + "</span>";
                }

                // 添加
                var markAnswer = "<tr ";

                if (!answerId && index === 0) {
                    MarkV2Main.markListAnswer = answer;
                    markAnswer = "<tr class='active' ";
                    // $(".right .switch .pre_student").addClass("disabled");
                } else if (answer.answerId === answerId) {
                    markAnswer = "<tr class='active' ";
                    trIndex = index;
                    MarkV2Main.markListAnswer = answer;
                    /*if (index === MarkV2Main.markListAnswers.length-1) {
                        $(".right .switch .next_student").addClass("disabled");
                    }*/
                }

                markAnswer = markAnswer + " answerIndex="+ index.toString() + " trIndex=" + index.toString() + "><td width=\"30%\"> * </td><td width=\"35%\">" + answerScore + "</td>" + markStatusTitle + "</tr>";
                $(".markListRateTable tbody").append(markAnswer);
            });

            // 添加点击事件
            $(".markListRateTable tbody tr").off("click").on("click", function () {
                var answerIndex = $(this).attr("answerIndex");
                if (MarkV2Main.markListAnswers[answerIndex].answerId === MarkV2Main.markListAnswer.answerId) {
                    return;
                }
                // 保存上题答案
                //_this.doSaveAnswer($("#answerByName").attr("trIndex"));

                MarkV2Main.markListAnswer = MarkV2Main.markListAnswers[answerIndex];
                $(this).addClass('active').siblings().removeClass('active');
                _this.buildAnswer(answerIndex);
            });

            // 初始化批阅进度title
            var countAnswer = 0;
            if (!CommUtils.isEmpty(MarkV2Main.markListAnswers)) {
                countAnswer = MarkV2Main.markListAnswers.length;
            }
            var $markTitle = i18n['mark_loading'] + " " + markCount + "/" + countAnswer;
            $(".markListTitle h3").append($markTitle);

            return trIndex;
        },
        // 渲染答案信息
        buildAnswer : function(trIndex, answerId) {
            var _this = this;
            _this.buildAnswerHtml(MarkV2Main.markListAnswer);
            var thisTr = $(".markListRateTable tbody tr").eq(trIndex);
            var answerIndex = thisTr.attr("answerIndex");
            var answerScore = "";
            if (MarkV2Main.markListAnswer.markStatus === "1") {
                answerScore = MarkV2Main.markListAnswer.answerScore;
            }
            _this.setScoreBg(answerIndex, trIndex, MarkV2Main.markListAnswer.userName, answerScore);
            $('#answerByScore').focus();
            _this.scrollTo(thisTr);
            this.doMark(); //绑定批阅事件
        },
        // 批阅进度滚动条
        scrollTo : function(thisTr) {
            if (thisTr.length == 0) {
                return;
            }
            var tableTop = $(".markListRateTableList").offset().top;
            var trTop = $(thisTr).offset().top;
            $('.markListRateTableList').animate({
                scrollTop: (trTop - tableTop - 36)
            }, 200);
        },
        buildAnswerHtml: function(obj) {
            var _this = this;
            $(".table_mark_area").empty();
            if (obj == null || CommUtils.isEmpty(obj.answerId)) {
                return;
            }
            MarkV2Main.answerId = obj.answerId;
            MarkV2Main.userId = obj.userId;
            MarkV2Main.fullName = obj.userName[0];
            let $tr = "<tr data-markstatus='"+obj.markStatus+"' data-userid='"+obj.userId+"'>";
            var typeCode = MarkV2Main.questionMap.get(MarkV2Main.currentQuestionId).type.code;
            var answerHtml = "";
            var isCanvases = false;
            if( "31" == typeCode) {
                answerHtml = _this.getShownAnswerV2(obj.userAnswer);
            } else {
                answerHtml = _this.getShownAnswer(obj.userAnswer, typeCode);
            }
            if (obj.canvases != undefined && obj.canvases != null && obj.canvases.length > 0){  //学生答案
                $(".canvas_root_area").show();
                $tr += "<td class='user_answer_canvas'>";
                if (!CommUtils.isEmpty(obj.userAnswer)) {
                    $tr += ("&nbsp;" + answerHtml + "&nbsp;");
                }
                $tr += "<div class=\"imgs\"></div>";
                $tr += "</td>";
                isCanvases = true;
            }else{
                $tr += "<td><div style='margin-left: 4px;'>"+answerHtml+"</div></td>";
                $(".canvas_root_area").hide();
            }
            $tr += "<td width='80' align='center' style=\"border-right: 0px;\">";               //批阅
            if (obj.markStatus == 0){      //未批阅
                $tr += "<span class='right_icon'></span><span class='wrong_icon'></span></td>";
            }else{
                if (obj.answerStatus == "0"){    //答对
                    $tr += "<span class='right_icon rh'></span><span class='wrong_icon'></span></td>";
                }else{
                    $tr += "<span class='right_icon'></span><span class='wrong_icon wh'></span></td>";
                }
            }
            $tr += "</tr>";
            $(".table_mark_area").append($tr);
            if (isCanvases) {
                LeftMarkArea.change(MarkV2Main.markListAnswer.answerId);
            }
        },
        // 设置打分板信息
        setScoreBg : function (answerIndex, trIndex, name, score)  {
            var answerByName = $("#answerByName");
            answerByName.attr("trIndex", trIndex);
            answerByName.attr("answerIndex", answerIndex);
            answerByName.text(name);
            $("#answerByScore").val(score);
            $(".scoreBg .scores").removeAttr("bgClickScore");
        },
        // 生成快捷打分按钮
        buildShortcutButton : function () {
            var _this = this;

            var scoreBg = $(".right .scoreBg");

            // 清空原按钮
            scoreBg.empty();

            var question = MarkV2Main.questionMap.get(MarkV2Main.currentQuestionId);
            var score = question.questionScore;
            var difference = 1;
            if (score > 10) {
                difference = Math.ceil((score/10));
            }

            // 第一排固定 最大值/0/-0.5
            var firstTd = "<div><div class=\"col-lg-4\"><button type=\"button\" class=\"btn btn-default\">"+ score +"</button></div>" +
                "<div class=\"col-lg-4\"><button type=\"button\" class=\"btn btn-default\">0</button></div>" +
                "<div class=\"col-lg-4\"><button type=\"button\" class=\"btn btn-default\">-0.5</button></div></div>";
            scoreBg.append(firstTd);

            var index = 0;
            var tr = "<div>";
            while ((score -= difference) > 0) {
                tr += "<div class=\"col-lg-4\"><button type=\"button\" class=\"btn btn-default\">"+ score +"</button></div>";
                index ++;
                if ((index % 3) === 0) {
                    tr += "</div><div>";
                }
            }
            scoreBg.append(tr);

            scoreBg.find("button").off("click").on("click",function(){
                var bgClickScore = $(this).text();
                if (!CommUtils.isEmpty(MarkV2Main.markListAnswer.answerScore) && MarkV2Main.markListAnswer.answerScore === parseInt(bgClickScore)) {
                    return;
                } else if (bgClickScore !== "-0.5") {
                    $(".right .scoreBg").attr("bgClickScore", bgClickScore);
                } else {
                    $(".right .scoreBg").removeAttr("bgClickScore");
                }
                _this.setMarkScore(bgClickScore);
                $('#answerByScore').focus();
            });
            _this.resetMarkListHeight();
        },
        resetMarkListHeight: function() {
            let height = $(window).height() - 15 - $(".container .top").outerHeight() - 15 - 10;  //批阅部分和菜单栏高度
            let markListHeight = height - $(".right .switch").outerHeight() - $(".right .nums").outerHeight()
                - $(".right .scoreBg").outerHeight() - $(".right .markListTitle").outerHeight() - $(".right .markListTableTitle").outerHeight();
            $(".right .markListRateTableList").css("height", markListHeight);
        },
        //设置批阅分数
        setMarkScore : function (markScore) {
            var _this = this;
            var answerByScore = $("#answerByScore");
            var score = 0;
            if (markScore !== "0" && ( markScore.indexOf("+")>=0 || markScore.indexOf("-")>=0 )) {
                score = (Number)(markScore) + (Number)(answerByScore.val());
            } else {
                score = (Number)(markScore);
            }
            var question = MarkV2Main.questionMap.get(MarkV2Main.currentQuestionId);
            if (score > question.questionScore) {
                score = question.questionScore;
            } else if (score < 0) {
                score = 0;
            }

            var answerScore = score;
            answerByScore.val(answerScore);

            if (answerScore > (MarkV2Main.thresholdScore * question.questionScore)) {
                MarkV2Main.markListAnswer.answerStatus = "0";
                $(".table_mark_area tr span.right_icon").addClass("rh").siblings().removeClass("wh");  //修改√, × 图标
            } else {
                MarkV2Main.markListAnswer.answerStatus = "1";
                $(".table_mark_area tr span.wrong_icon").addClass("wh").siblings().removeClass("rh");  //修改√, × 图标
            }

            MarkV2Main.markListAnswer.answerScore = answerScore;

            // 修改答案批阅状态
            MarkV2Main.markListAnswer.markStatus = "1";
            MarkContent.buildMarkNum();
            // 修改答案变更状态
            MarkV2Main.markListAnswer.changeStatus = true;

            // 获取当前答案的下标
            var $thisTr = $(".markListRateTable tbody .active");
            //直接更新列表值   duffy_ding    只有一个人的时候按钮置为不能点击
            $thisTr.find("td").eq(1).html(MarkV2Main.markListAnswer.answerScore.toString());
            $thisTr.find("td").eq(2).find("span").removeClass("red").addClass("green").html(i18n["marked"]);
            MarkV2Main.createBeforeunload();
        },
        formatVal : function(str) {
            if (str == null || str == undefined) {
                return "";
            }
            str = str.replace(/\&/g, "&amp;"); //和符号
            str = str.replace(/\"/g, "&quot;"); //转义双引号
            str = str.replace(/\</g, "&lt;"); //小于号
            str = str.replace(/\>/g, "&gt;"); //大于号
            str = str.replace(/\'/g, "&apos;"); //单引号
            return str;
        },
        markSortFun : function(sort_type){
            if (CommUtils.isEmpty(sort_type)){
                sort_type = $(".mark_sort").data("type");
            }
            if (CommUtils.isEmpty(sort_type)){
                sort_type = "asc";
            }
            $(".mark_sort").data("type", sort_type);
            let index = 0;
            $(".table_mark_area").find("tr").each(function(){
                 if (sort_type == "asc"){  //未批阅放前面
                    if ($(this).data("markstatus") == 0){
                        $(this).insertBefore($(".table_mark_area tr:eq("+index+")"));
                        index++;
                    }
                 }else{
                     if ($(this).data("markstatus") == 1){
                         $(this).insertBefore($(".table_mark_area tr:eq("+index+")"));
                         index++;
                     }
                 }

            });
        },
        personCountSortFun : function(){
            let sort_type = "asc";
            if (!CommUtils.isEmpty($(".person_count_sort").data("type"))){
                sort_type = $(".person_count_sort").data("type") == "asc" ? "desc" : "asc";
            }
            $(".person_count_sort").data("type", sort_type);
            let sortNum = sort_type == "asc" ? 1 : -1;
            let $sortTr = $(".table_mark_area").find("tr");
            $sortTr.sort(function(a, b){
                let sort1 = a.getAttribute("data-total");
                let sort2 = b.getAttribute("data-total");
                if (sort1 > sort2) return 1 * sortNum;
                else if (sort1 < sort2) return -1 * sortNum;
                else return 0;
            });
            $sortTr.detach().appendTo(".table_mark_area");
        },
        doMark : function(){   //输入分数,对错点击事件
            let _this = this;
            $(".table_mark_area tr span.right_icon").unbind();
            $(".table_mark_area tr span.wrong_icon").unbind();
            $(".table_mark_area tr span.right_icon").click(function(){  //点击"√"
                let questionScore = MarkV2Main.questionMap.get(MarkV2Main.currentQuestionId).questionScore;
                $(this).addClass("rh").siblings().removeClass("wh");  //修改√, × 图标
                MarkPage.setMarkScore(questionScore.toString()); //修改批阅数据
                $(this).closest("tr").data("markstatus", "1");
                $(this).closest("tr").find("td:eq(0)").find("span").removeClass("red").addClass("green").find("span").text(i18n['marked']);
                return false;
            });
            $(".table_mark_area tr span.wrong_icon").click(function(){  //点击"×"
                $(this).addClass("wh").siblings().removeClass("rh");  //修改√, × 图标
                MarkPage.setMarkScore("0"); //修改批阅数据
                $(this).closest("tr").data("markstatus", "1");
                $(this).closest("tr").find("td:eq(0)").find("span").removeClass("red").addClass("green").find("span").text(i18n['marked']);
                return false;
            });
        },
        //输入分数校验
        validateScore : function(inputScore) {
            if (inputScore != "") {
                if (!/^(0|[1-9][0-9]{0,2})(\.\d{0,2})?$/.test(inputScore)) {
                    return false;
                }
            }
            return true;
        },
        formatScore : function(score){
            return parseFloat(score) == parseInt(score) ? parseInt(score) : parseFloat(score);
        }
    }
})(jQuery, window);
