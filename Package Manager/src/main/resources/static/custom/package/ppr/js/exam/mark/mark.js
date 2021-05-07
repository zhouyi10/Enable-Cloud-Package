(function ($, window){
    var MarkV2Main = window.MarkV2Main={
        groupType : "1", //默认按学生
        lastQuestionId : "",
        currentQuestionId: "", //当前选中题目标识
        thresholdScore : 0.8, //  对错阀值
        questionMap : {},
        answerId : "",
        userId : "",
        fullName : "",
        groupIds: "",
        markingInterval : null,
        init : function(){
            let _this = this;
            _this.initMarkClassInfo();
           // _this.updateMarkStatus();
            window.onbeforeunload = function () {
                $.get(_this.markStatusCleanlUrl + "?testId=" + _this.testId);
            };
            this.initQuestionMap();
            MarkNav.initMarkNav(); //初始化题号导航栏
            MarkPage.initMarkPage(); //初始化批阅区域
            this.saveExcellentAnswer();

            //批阅暂存
            $("#temporaryBtn").click(function(){
                _this.doSave("2", _this.test.answers);
            });
            //批阅完成
            $("#completeMarkBtn").click(function(){
                _this.doSave("1", _this.test.answers);
            });
        },
        initMarkClassInfo: function() {
            var _this = this;
            if(!CommUtils.isEmpty(MarkV2Main.exam.sendPaperGroups)) {
                var $htmlStr = "<span>"+i18n['className']+":";
                if(_this.actionType == '1') {
                    $.each(MarkV2Main.exam.sendPaperGroups,function (index,obj) {
                        var id = obj.classId !=null ? obj.classId : obj.groupId;
                        var name = obj.className !=null ? obj.className : obj.groupName;
                        $htmlStr +='<lable class="groupsName"><input name="className" type="checkbox" value="'+id+'" data-key="'+id+'"><span>'+name+'</span></lable>'
                    });
                } else {
                    $.each(MarkV2Main.exam.sendPaperGroups,function (index,obj) {

                        var id = obj.classId !=null ? obj.classId : obj.groupId;
                        var name = obj.className !=null ? obj.className : obj.groupName;
                        $htmlStr +='<span class="groupsName">'+name+'</span>'
                    });
                }
                $htmlStr += "</span>";
                $(".container .top ul").append($htmlStr);
            }

            var groupIds = [];
            if(!CommUtils.isEmpty(_this.groupId)) {
                groupIds = _this.groupId.split(",");
            }
            $.each(groupIds,function (index1,obj1) {
                $("input[name='className'][value='"+obj1+"']").prop("checked",true);
            });
            $(".groupsName input").click(function () {
                var length = $(this).parent().parent().find('input[type=checkbox]:checked').length;
                if(length == 0) {
                    layer.alert(i18n['selectOneClass'],{
                        btn:i18n['confirm']
                    });
                    $(this).prop("checked",true);
                    return;
                }
                //_this.doSave("2", _this.test.answers);
                _this.changeMarkClass();
            });
            if(!CommUtils.isEmpty(MarkV2Main.exam.sendPaperGroups)) {
                $.each(MarkV2Main.exam.sendPaperGroups,function (index,obj) {
                    var id = obj.classId !=null ? obj.classId : obj.groupId;
                    if(obj.markStatus == 0) {
                        if($("input[name='className'][value='"+id+"']").is(':checked')){
                            $("input[name='className'][value='"+id+"']").next().prop("style","color:#ffd400");
                        } else {
                            $("input[name='className'][value='"+id+"']").next().prop("style","color:red");
                        }
                    } else if(obj.markStatus == 1) {
                        $("input[name='className'][value='"+id+"']").next().prop("style","color:#ffd400");
                    } else {
                        $("input[name='className'][value='"+id+"']").next().prop("style","color:#11bb00");
                    }
                });
            }
            if($("input[name='className']").length == 1){
                $("input[name='className']").prop("style","display:none");
            }
        },
        changeMarkClass : function () {
            var _this = this;
            $(".container .top ul").find('input[type=checkbox]:checked').each(function (index,obj) {
                if(index < 1){
                    _this.groupIds = $(this).attr('data-key');
                } else {
                    _this.groupIds += ( "," + $(this).attr('data-key'));
                }
            });
            var url = _this.doMarkUrl + "?testId=" + _this.testId;
            if (!CommUtils.isEmpty(_this.groupIds)) url += "&groupIds=" + _this.groupIds;
            url += "&actionType=" + _this.actionType;
            window.location.href = url;
        },
        updateMarkStatus: function () {
            let _this = this;
            return setInterval(function () {
                $.post(_this.markStatusUpdatelUrl, {"testId": _this.testId, "markingCacheInfo" : _this.markingCacheInfo});
            }, 4000);
        },
        getKey: function () {
             //let _this = this;
            //             // var param = {};
            //             // if (_this.testId && !_this.groupId) {
            //             //     param['key'] =  _this.testId;
            //             //     param['type'] = 'mark';
            //             // }
            //             // if (_this.testId && _this.groupId) {
            //             //     param['key'] = _this.testId + "_" + _this.groupId;
            //             //     param['type'] = 'markGroup';
            //             // }
            //             // if (_this.activityId && _this.fileId && _this.userId) {
            //             //     param['key'] = _this.activityId + "_" + _this.fileId + "_" + _this.userId;
            //             //     param['type'] = 'markStudyTask';
            //             // }
            //             // if(!CommUtils.isEmpty(_this.markUserInfo)) {
            //             //     param['markUserInfo'] = _this.markUserInfo;
            //             // }
            //             // return param;
        },
        saveExcellentAnswer : function() {
          var _this = this;
          $("#good").click(function () {
              var param = {
                  'answerId' : _this.answerId,
                  'questionId' : _this.currentQuestionId,
                  'userId' : _this.userId,
                  'fullName' : _this.fullName
              };
              /*if($("#good")[0].className == "good_d good_p"){
                  $.ajax({
                      url:_this.saveExcellentAnswerUrl,
                      type:'post',
                      data:param,
                      success :function () {
                      }
                  })
              } else {
                  $.ajax({
                      url:_this.deleExcellentAnswerUrl+ "?questionId=" + param.questionId + "&userId=" + param.userId,
                      type:'get',
                      success :function () {
                      }
                  })
              }*/
          });
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
        //点击图形批阅事件，手动打开模态框
        canvasMark : function(){
            let _this = this;
            $(".table_mark_area").find(".img-div").click(function(){
                let canvasId = $(this).find("img").attr('data-canvasid'); //$(this).data('canvasid');
                if (CommUtils.isEmpty(canvasId) && $(this).find("img").hasClass('video-wrapper')) {
                    CommUtils.openFrame(i18n['preview'], CommUtils.formatStr(MarkV2Main.previewFileUrl, $(this).find("img").attr('data-file-id')), '820px', '520px');
                    return;
                }
                let answers = new Array();
                let answer = "";
                $.each(_this.test.answers, function (index, obj) {
                    if (obj.questionId == _this.currentQuestionId && obj.canvases != null) {
                        answers.push(obj);
                        $.each(obj.canvases, function (index, canvase) {
                            // 获取初始化answer
                            if (canvase.canvasId === canvasId) {
                                answer = obj;
                            }
                        });
                    }
                });
                var userIds = [];
                $(".table_mark_area").find("tr").each(function() {
                    var users = $(this).attr('data-userid');
                    $.each(users.split(','), function(i, u) {
                        userIds.push(u);
                    })
                });
                answers.sort(function (a, b) {
                    return userIds.indexOf(a.userId) - userIds.indexOf(b.userId);
                });
                // 倒序一下保证顺序一致
                // answers.reverse();
                MarkImage.init(answer, canvasId, _this.questionMap.get(_this.currentQuestionId), answers);
                // 收起关闭
                if ($('.toggle').siblings(".title").hasClass("h100")) {
                    $('.toggle').trigger('click');
                } else {
                    $('.toggle').find("i").text(i18n['question_unfold']);
                }
                $('#myModal').modal("show");
            });
        },
        canvasMarkCallBack : function(answers){
            let _this = this;
            if (answers == undefined ||answers == null || answers.length == 0) return;
            $.each(_this.test.answers, function(index1, obj1){
                $.each(answers, function(index2, obj2){
                    if (obj2.canvases == null) return true;
                    if (obj1.answerId == obj2.answerId){
                        // if (obj1.markStatus == '0' && obj2.changeStatus){ //初始化状态未匹配，批图后一定是已批阅
                        //     var $dom = $("#" + obj1.questionId).find(".marked_count");
                        //     $dom.text(parseInt($dom.text()) + 1);
                        //     obj2.markStatus = '1';
                        // }
                        obj1 = obj2;
                    }
                });
            });
            $(".mark_sort").data("type", "asc");
            MarkPage.changeMarkAnswerArea();
            MarkNav.initMarkNum();
            MarkPage.doMark();
            _this.canvasMark();
        },
        doSave : function(completeMarkStatus, answers){  //"0":点击下一题保存， "1":点击批阅完成保存
            if (CommUtils.isEmpty(answers)) {
                return;
            }
            let _this = this;
            let param = {
                "testId" : _this.test.testId,
                "examId" : _this.exam.paperId,
                "type" : completeMarkStatus,
                "answers" : answers
            }
            CommUtils.tryLock($(document.body));
            $.ajax({
                url: _this.completeMarkUrl,
                type: "POST",
                contentType: 'application/json',
                data:JSON.stringify(param),
                dataType: "json",
                success: function (data) {
                    CommUtils.unLock($(document.body));
                    if (data.status == "1"){
                        if (completeMarkStatus == "1") { //批阅完成
                            layer.alert(i18n['tip_complete_mark'],{closeBtn : 0 ,title: i18n['tipBox_title'], btn: i18n['confirm']}, function(){
                                var result = {'type':'mark', 'data': 'success'};
                                if(typeof(WeixinJSBridge) != "undefined"){ //微信
                                    window.history.go(-1);
                                } else if (window.parent != window) { //子页面
                                    window.parent.postMessage(result, "*");
                                } else if (window.opener) { //新开窗口
                                    window.opener.postMessage(result, "*");
                                }
                                if(_this.actionType == 1){
                                    _this.changeMarkClass();
                                } else {
                                    window.opener= null;
                                    window.open("","_self");
                                    window.close();
                                }
                            });
                        } else if(completeMarkStatus == "2"){ //批阅暂存
                            layer.alert(i18n['tip_temporary_save'], {btn: i18n['confirm']});
                        } else { //暂存,下一题保存
                            //layer.alert(i18n['tip_temporary_save']);
                            $.each(MarkV2Main.test.answers, function(index, obj){
                                if (obj.changeStatus != undefined) obj.changeStatus = false;
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
        }
    };
    var MarkNav = {
        kindNameArray : ['一', '二', '三', '四', '五', '六', '七', '八', '九', '十'],
        initMarkNav : function(){
            let _this = this;
            let exam = MarkV2Main.exam;
            let kindTemplate = $("#kind_template").html();
            let kindNameTemplate = i18n['kind_name_template'];
            let totalCount = MarkV2Main.test.submitCount;
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
                        if ($(".page_nav table.squares:last").find(".questions").length == 0   //新增一个<tr>
                         || $(".questions:last").find(".question").length == 3){
                            $(".page_nav table.squares:last").append("<tr class='questions'><td class='question' id='"+questionId+"'><div class=\"square\"><b>"+obj.externalNo+"</b><p><span class='marked_count'>0</span>/"+totalCount+"</p></div></td></tr>");
                        }else{
                            $(".questions:last").append("<td class='question' id='"+questionId+"'><div class=\"square\"><b>"+obj.externalNo+"</b><p><span class='marked_count'>0</span>/"+totalCount+"</p></div></td>");
                        }
                    }else{                      //小题
                        $.each(obj.children, function(index, child){
                            let questionId = child.question.questionId;
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
            _this.initEvent();
        },
        initEvent : function(){
            let _this = this;
            //初始化已批阅人数
            this.initMarkNum();
            //默认选中第一题
            $(".page_nav .question:eq(0)").find("div.square").addClass("ing");
            $(".pre_question").addClass("disabled");
            if ($(".page_nav .question").length < 2){
                $(".next_question").addClass("disabled");
            }
            MarkV2Main.currentQuestionId = MarkV2Main.lastQuestionId = $(".page_nav .question:eq(0)").attr("id");
            MarkPage.changeQuestion();
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
        initMarkNum : function(){
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
            let totalCount = MarkV2Main.test.submitCount;
            $(".question").each(function(){
                let markedCount = markNumMap.get($(this).attr("id"));
                $(this).find(".marked_count").text(markedCount);
                if (totalCount == markedCount){
                    $(this).find("div.square").addClass("over");
                }
            });
        },
        doSaveByOneQuestion : function(){  //保存每一题的批阅结果, 相当于以前的暂存
            let answers = new Array();
            $.each(MarkV2Main.test.answers, function(index, obj){
                if (obj.questionId == MarkV2Main.currentQuestionId && obj.changeStatus != undefined && obj.changeStatus){
                    answers.push(obj);
                    obj.changeStatus = false;
                }
            });
            if (answers.length > 0){
                MarkV2Main.doSave("0", answers);
            }
        },
        changeQuestionTip : function(callback){    //切换题目, 提示上一题还有没有批阅的情况
            let _this = this;
            let noMarkName = new Array();
            $.each(MarkV2Main.test.answers, function(index, obj){
                if (MarkV2Main.currentQuestionId == obj.questionId && obj.markStatus == '0'){
                    noMarkName.push(obj.userName);
                }
            });
            if (noMarkName.length > 0){
                let questionNo = $("#"+MarkV2Main.currentQuestionId).find(".square b").text();
                let tip = i18n['hava_some_student_not_be_mark'].replace('{0}', questionNo).replace('{1}', noMarkName.join(","));
                let index = layer.confirm(tip, {
                    btn: [i18n['all_mark_error'],i18n['ignore_and_continue'] ] //按钮 全部判错  忽略继续
                }, function(){
                    $.each(MarkV2Main.test.answers, function(index, obj){
                        if (MarkV2Main.currentQuestionId == obj.questionId && obj.markStatus == '0'){
                           obj.markStatus = '1';   //已批阅
                           obj.answerScore = 0;    //得0分
                           obj.answerStatus = "1"; //回答错误
                           obj.changeStatus = true;
                        }
                    });
                    _this.initMarkNum();
                    MarkNav.doSaveByOneQuestion();  //保存上一题批阅数据
                    layer.close(index);
                    callback && callback();
                }, function(){
                    MarkNav.doSaveByOneQuestion();  //保存上一题批阅数据
                    layer.close(index);
                    callback && callback();
                });

            }else{
                MarkNav.doSaveByOneQuestion();  //保存上一题批阅数据
                callback && callback();
            }
        }
    };
    var MarkPage = {
        initMarkPage : function(){
            let _this = this;
            //批阅排序事件
            $(".mark_sort").click(function(){
                var sortType = $(".mark_sort").data("type");
                if (CommUtils.isEmpty(sortType) || sortType == 'desc') {
                    sortType = 'asc';
                } else {
                    sortType = 'desc';
                }
                _this.markSortFun(sortType);
            });
            //人数排序事件
            $(".person_count_sort").click(function(){
                _this.personCountSortFun();
            });
        },
        changeQuestion : function(){
            this.changeQuestionContent();
            this.changeMarkAnswerArea();
            this.doMark(); //绑定批阅事件
            MarkV2Main.canvasMark(); //绑定canvas批阅
        },
        changeQuestionContent : function(){    //换题切换题干
            let question = MarkV2Main.questionMap.get(MarkV2Main.currentQuestionId);
            let answer = this.getShownAnswer(question.answer, question.type.code);
            let questionContent = "";
            if (MarkV2Main.exam.paperType == '4') {
                if (MarkV2Main.exam.files != null && MarkV2Main.exam.files.length > 0 && question.axises != null && question.axises.length > 0) {

                }else {
                    questionContent = "略";
                }
            }
            else {
                if (CommUtils.isEmpty(question.parentQuestionContent)) {
                    questionContent = question.stem.richText;
                } else {    //包含大小题
                    questionContent = question.parentQuestionContent + question.stem.richText;
                }
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
            if (MarkV2Main.groupType == '1'){   //按答案批阅
                let classifyObj = new Array();
                $.each(MarkV2Main.test.answers, function(index, obj){
                    if (obj.questionId == MarkV2Main.currentQuestionId){
                        if (obj.canvases != null){  //有绘图,都算不同答案
                            let userNameArray = new Array();
                            userNameArray.push(obj.userName);
                            let answerObj = {
                                "answerId" : obj.answerId,
                                "userAnswer" : obj.userAnswer,
                                "answerScore"  : obj.answerScore,
                                "answerStatus" : obj.answerStatus,
                                "userId" : obj.userId,
                                "userName" : userNameArray,
                                "markStatus" : obj.markStatus,
                                "total" : 1,
                                "canvases" : obj.canvases
                            }
                            classifyObj.push(answerObj);
                        }else{
                            var flag = false; //表示没有相同答案
                            $.each(classifyObj, function(index, answerObj){    //相同答案放一起
                                if (answerObj.canvases != undefined || answerObj.canvases != null) return true;
                                if (!CommUtils.isEmpty(answerObj.userAnswer) && answerObj.userAnswer == obj.userAnswer){
                                    flag = true;
                                    answerObj.answerId = answerObj.answerId + "," + obj.answerId;
                                    if (obj.answerStatus == '1') answerObj.answerStatus = '1'; //发现有一人答错，设置都答错
                                    if (obj.markStatus == '0') answerObj.markStatus = '0';//发现有一人未批，设置都没批
                                    answerObj.userId = answerObj.userId + "," + obj.userId;
                                    answerObj.userName.push(obj.userName);
                                    answerObj.total += 1;
                                }
                            });
                            if (!flag){    //不同答案添加到数据
                                let userNameArray = new Array();
                                userNameArray.push(obj.userName);
                                var answerObj = {
                                    "answerId" : obj.answerId,
                                    "userAnswer" : obj.userAnswer,
                                    "answerScore"  : obj.answerScore,
                                    "answerStatus" : obj.answerStatus,
                                    "userId" : obj.userId,
                                    "userName" : userNameArray,
                                    "markStatus" : obj.markStatus,
                                    "total" : 1
                                }
                                classifyObj.push(answerObj);
                            }
                        }
                    }
                });
                //初始化批阅区域
                $.each(classifyObj, function(index, obj){
                    MarkV2Main.answerId = obj.answerId;
                    MarkV2Main.userId = obj.userId;
                    MarkV2Main.fullName = obj.userName[0];
                    let $tr = "<tr data-markstatus='"+obj.markStatus+"' data-total='"+obj.total+"' data-userid='"+obj.userId+"'>";
                    if (obj.markStatus == 0){   //未批阅
                        $tr += "<td width='110'><span class=\"red\"><i class=\"fa fa-circle\" aria-hidden=\"true\"></i><span>"+i18n['un_mark']+"</span></span></td>";
                    }else{
                        $tr += "<td width='110'><span class=\"green\"><i class=\"fa fa-circle\" aria-hidden=\"true\"></i><span>"+i18n['marked']+"</span></span></td>";
                    }
                    $tr += "<td width=\"75\">"+obj.total+"</td>";  //批阅人数
                    $tr += " <td width=\"185\"><div class=\"names\" data-placement=\"bottom\" title=\""+obj.userName.join(" ")+"\">";   //批阅学生详情
                    $.each(obj.userName, function(index, value){
                        $tr += "<span>" + value + "</span>";
                    });
                    $tr += "</div></td>";
                    var typeCode = MarkV2Main.questionMap.get(MarkV2Main.currentQuestionId).type.code;
                    var answerHtml = "";
                    if( "31" == typeCode) {
                       answerHtml = _this.getShownAnswerV2(obj.userAnswer);
                    } else {
                       answerHtml = _this.getShownAnswer(obj.userAnswer, typeCode);
                    }
                    if (obj.canvases != undefined && obj.canvases != null && obj.canvases.length > 0){  //学生答案
                        let pictureUrl = MarkV2Main.pictureUrl + "/correctionresult/thumbnail/download?height=40&width=80&fileId={0}&businessId={1}&timestamp=" + new Date().getTime();
                        $tr += "<td>";
                        if (!CommUtils.isEmpty(obj.userAnswer)) {
                            $tr += ("&nbsp;" + answerHtml + "&nbsp;");
                        }
                        $.each(obj.canvases, function(canvasIndex, canvas){
                            if ('2' == canvas.canvasAnswerType || '3' == canvas.canvasAnswerType) { // 2 视频 3 音频
                                $tr += "<div class=\"img-div\"><img data-file-id='" + canvas.fileId + "' class='video-wrapper' src='" + MarkV2Main.videoImgUrl + "' /></div>"
                            } else { // 0 绘图 1 拍照
                                $tr += "<div class=\"img-div\"><img data-toggle=\"modal\" data-canvasid='"+canvas.canvasId+"' class=\"data\" src='"+CommUtils.formatStr(pictureUrl, canvas.sourceFileId, canvas.canvasId)  +"' alt=\"\"></div>";
                            }
                        });
                        $tr += "</td>";
                    }else{
                        $tr += "<td><div style='overflow: auto;max-width: 340px;'>"+answerHtml+"</div></td>";
                    }
                    if (obj.markStatus == 0){
                        $tr += "<td width='80'><input type=\"text\" class=\"score\" value='' /></td>";
                    }else{
                        $tr += "<td width='80'><input type=\"text\" class=\"score\" value="+_this.formatScore(obj.answerScore)+" /></td>";
                    }
                    $tr += "<td width='80' align='center'>";               //批阅
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
                   /* $("#good").removeClass("good_p");
                    $.ajax({
                        url : MarkV2Main.getExcellentAnswerUrl+"?answerId="+obj.answerId + "&userId="+ obj.userId,
                        type : "get",
                        success : function (data) {
                            if(!CommUtils.isEmpty(data)){
                                if($("#good")[0].className == "good_d"){
                                    $("#good").addClass("good_p");
                                }
                            }
                        }
                    })*/

                });
                _this.markSortFun();
            }
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
            $(".table_mark_area tr span.right_icon").click(function(){  //点击"√"
                let questionScore = MarkV2Main.questionMap.get(MarkV2Main.currentQuestionId).questionScore;
                $(this).addClass("rh").siblings().removeClass("wh");  //修改√, × 图标
                $(this).closest("td").prev().find("input").val(questionScore);  //修改分数输入框分数
                _this.setMarkData($(this).closest("tr").data("userid"), questionScore, questionScore); //修改批阅数据
                MarkNav.initMarkNum();
                $(this).closest("tr").data("markstatus", "1");
                $(this).closest("tr").find("td:eq(0)").find("span").removeClass("red").addClass("green").find("span").text(i18n['marked']);
                return false;
            });
            $(".table_mark_area tr span.wrong_icon").click(function(){  //点击"×"
                let questionScore = MarkV2Main.questionMap.get(MarkV2Main.currentQuestionId).questionScore;
                $(this).addClass("wh").siblings().removeClass("rh");  //修改√, × 图标
                $(this).closest("td").prev().find("input").val(0);  //修改分数输入框分数
                _this.setMarkData($(this).closest("tr").data("userid"), 0, questionScore); //修改批阅数据
                MarkNav.initMarkNum();
                $(this).closest("tr").data("markstatus", "1");
                $(this).closest("tr").find("td:eq(0)").find("span").removeClass("red").addClass("green").find("span").text(i18n['marked']);
                return false;
            });
            $(".table_mark_area input.score").bind("input propertychange",function(){
                let questionScore = MarkV2Main.questionMap.get(MarkV2Main.currentQuestionId).questionScore;
                let inputVal = $(this).val();
                if (_this.validateScore(inputVal)){
                    if (CommUtils.isEmpty(inputVal)) inputVal = '';
                    else if (parseFloat(inputVal) < 0) inputVal = 0;
                    else if (parseFloat(inputVal) > questionScore) inputVal = questionScore;
                    _this.setMarkData($(this).closest("tr").attr("data-userid"), parseFloat(inputVal));
                    $(this).val(inputVal);
                    MarkNav.initMarkNum(); //修改导航已批阅人数
                    $(this).closest("tr").data("markstatus", "1");
                    $(this).closest("tr").find("td:eq(0)").find("span").removeClass("red").addClass("green").find("span").text(i18n['marked']);
                    if (CommUtils.isEmpty(inputVal)) inputVal = 0; //不输入或者删除分数，按照0分算，但是显示还是显示空，鼠标移除才显示0
                    _this.setMarkData($(this).closest("tr").data("userid"), inputVal, questionScore); //修改批阅数据
                    if (parseFloat(inputVal) >= questionScore * MarkV2Main.thresholdScore) {
                        $(this).closest("tr").find("td:last").find("span.right_icon").addClass("rh").siblings().removeClass("wh");
                    } else {
                        $(this).closest("tr").find("td:last").find("span.wrong_icon").addClass("wh").siblings().removeClass("rh");
                    }
                }else{
                    $(this).val('');
                    $(this).closest("tr").find("td:last").find("span.wrong_icon").addClass("wh").siblings().removeClass("rh");
                }

            }).focus(function(){
                if (CommUtils.isEmpty($(this).val()) || parseFloat($(this).val()) == 0){
                    $(this).select();
                }
            }).click(function(){
                return false; //防止冒泡
            }).blur(function(){
                if (CommUtils.isEmpty($(this).val())){
                    $(this).val(0);
                }else{
                    $(this).val(parseFloat($(this).val()));
                }
            });
        },
        //修改answers中的分数以及批阅状态
        setMarkData : function(userIds, score, questionScore){
            var _this = this;
            userIds = "" + userIds; //确保为字符串
            $.each(MarkV2Main.test.answers, function(index, obj){
                if (userIds.indexOf(obj.userId) > -1 && obj.questionId == MarkV2Main.currentQuestionId){
                    obj.answerScore = score;
                    obj.markStatus = "1";
                    if (score >= questionScore * MarkV2Main.thresholdScore) obj.answerStatus = '0'; //正确
                    else obj.answerStatus = "1";
                    obj.changeStatus = true;  //设置一个标志位，表名这个题目是新批阅的需要再单击下一题的回传后台进行保存
                }
            })
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
