(function ($, window){
    var MarkImage =  window.MarkImage = {
        answers : [], //当前题目答案信息
        question : {}, // 当前题目信息
        canvasId : "", // 前台页面传入图片id
        sortAsc : "", // 排序方式
        answer : {}, // 当前批阅人的答案信息
        questionWidth: 0, // 上级question区域宽度

        // 图片批阅初始化
        init: function (answer, canvasId, question, answers) {
            var _this = this;
            _this.question = question;
            _this.canvasId = canvasId;
            _this.answers = answers;
            _this.sortAsc = true;

            _this.initPageStatus();
            // 初始化答案列表， 默认按批阅状态排序  移动到上面方法中
            // _this.initAnswersSort("markStatus");
            _this.answer = answer;

            // 初始化批阅页面
            _this.initMarkImage(_this.answer.answerId);

            // 初始化题目答案图片模块
            LeftMarkArea.init(_this.answer.answerId, canvasId, answers);

            // 初始化绑定事件
            _this.initEvent();
        },
        initPageStatus: function() {
            var _this = this;
            // pre next button status;
            $(".answerSwitch button").removeClass('disabled');
            if (this.answers.length <= 1) {
                $(".answerSwitch button").addClass('disabled');
            }
            // this ques all marked
            _this.allMarked = true;
            $.each(_this.answers, function(a, answer) {
                if (answer.markStatus != '1') {
                    _this.allMarked = false;
                    return false; //break circle
                }
            });
        },

        initMarkImage : function(answerId) {
            var _this = this;

            // 初始化批阅进度区域
            var trIndex = _this.initMarkList(answerId);

            // 初始化打分面板(按题目批阅, 打分面板只初始化一次)
            _this.buildShortcutButton();

            // 初始化答案数据
            _this.buildAnswer(trIndex, answerId);
        },

        // 初始化答案列表， 默认按批阅状态排序
        initAnswersSort: function (condition) {
            var _this = this;
            for (var i = 0; i < _this.answers.length; i++) {
                for (var j = i; j < _this.answers.length; j++) {
                    var isTrue = false;
                    if (condition === "markStatus") {
                        if (_this.sortAsc) {
                            isTrue = (_this.answers[i].markStatus !== "0");
                        } else {
                            isTrue = (_this.answers[i].markStatus !== "1");
                        }
                    } else if (condition === "answerScore") {
                        if (_this.sortAsc) {
                            if (_this.answers[i].markStatus !== "0") {
                                isTrue = (_this.answers[i].answerScore <= _this.answers[j].answerScore);
                            } else {
                                isTrue = true;
                            }
                        } else {
                            if (_this.answers[i].markStatus !== "0") {
                                isTrue = true;
                            } else {
                                isTrue = (_this.answers[i].answerScore > _this.answers[j].answerScore);
                            }
                        }
                    } else {
                        return;
                    }
                    if (isTrue) {
                        var temp = _this.answers[i];
                        _this.answers[i] = _this.answers[j];
                        _this.answers[j] = temp;
                    }
                }
            }
            LeftMarkArea.resetOrder(_this.answers);
            _this.answer = _this.answers[0];
        },

        // 初始化批阅进度区域
        initMarkList : function(answerId) {
            var _this = this;

            // 初始化批阅区域
            $(".markListRateTable tbody tr").remove();
            $(".markListTitle h3").empty();

            // 已批阅统计
            var markCount = 0;

            var trIndex = 0;

            // 遍历答案
            $.each(_this.answers, function (index, answer) {
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
                    markAnswer = "<tr class='active' ";
                } else if (answer.answerId === answerId) {
                    markAnswer = "<tr class='active' ";
                    trIndex = index;
                }

                markAnswer = markAnswer + " answerIndex="+ index.toString() + " trIndex=" + index.toString() + "><td width=\"35%\">" + answer.userName + "</td><td width=\"30%\">" + answerScore + "</td>" + markStatusTitle + "</tr>";
                $(".markListRateTable tbody").append(markAnswer);
            });

            // 添加点击事件
            $(".markListRateTable tbody tr").off("click").on("click", function () {
                var answerIndex = $(this).attr("answerIndex");
                if (_this.answers[answerIndex].answerId === _this.answer.answerId) {
                    return;
                }
                // 保存上题答案
                _this.doSaveAnswer($("#answerByName").attr("trIndex"));

                _this.answer = _this.answers[answerIndex];
                $(this).addClass('active').siblings().removeClass('active');
                _this.buildAnswer($(this).attr("trIndex"));
            });

            // 初始化批阅进度title
            var $markTitle = i18n['mark_loading'] + " " + markCount + "/" + _this.answers.length;
            $(".markListTitle h3").append($markTitle);

            return trIndex;
        },

        // 渲染答案信息
        buildAnswer : function(trIndex, answerId) {
            var _this = this;
            if (!answerId) {
                LeftMarkArea.change(_this.answer.answerId);
            }
            var thisTr = $(".markListRateTable tbody tr").eq(trIndex);
            var answerIndex = thisTr.attr("answerIndex");
            var answerScore = "";
            if (_this.answer.markStatus === "1") {
                answerScore = _this.answer.answerScore;
            }
            _this.setScoreBg(answerIndex, trIndex, _this.answer.userName, answerScore);
            $('#answerByScore').focus();
            _this.scrollTo(thisTr);
        },

        // 初始化事件
        initEvent: function () {
            var _this = this;

            //分数排序事件
            $(".markListRateScore").off("click").on("click",function(){
                _this.sortAsc = !(_this.sortAsc);
                _this.initAnswersSort("answerScore");
                _this.initMarkList();
                _this.buildAnswer(0);
            });

            //批阅状态排序事件
            $(".markListRateStatus").off("click").on("click",function(){
                _this.sortAsc = !(_this.sortAsc);
                _this.initAnswersSort("markStatus");
                _this.initMarkList();
                _this.buildAnswer(0);
            });

            // 打分板加减分数
            $(".minus button").off("click").on("click",function () {
                _this.setMarkScore("-1");
            });
            $(".plus button").off("click").on("click",function () {
                _this.setMarkScore("+1");
            });

            // 手动修改分数
            $("#answerByScore").blur(function () {
                if ($(this).val() && ((Number)($(this).val()) !== _this.answer.answerScore)) {
                    _this.setMarkScore($(this).val());
                }
            });

            // 上一题/下一题 保存题目批阅信息
            $(".answerSwitch button").off("click").on("click",function () {
                if ($(this).hasClass('disabled')) {
                    return;
                }
                // 获取当前答案的下标, 保存后可能被修改
                var trIndex = (Number)($("#answerByName").attr("trIndex"));
                var changeStatus = _this.answer.changeStatus;

                // 保存上一题数据
                _this.doSaveAnswer(trIndex);

                var markListRateTableTrs = $(".markListRateTable tbody tr");

                markListRateTableTrs.removeClass("active");

                var trLength = markListRateTableTrs.length;

                // 开始切换，判断操作
                var switchOperation = $(this).attr("switch-operation");
                if (!switchOperation.indexOf("last")) {
                    if (!changeStatus) {
                        trIndex = trIndex - 1;
                    }
                } else if (!switchOperation.indexOf("next")) {
                    // 最后一个修改并点击下一个， 跳转至第一个
                    if (!changeStatus || trIndex === (trLength-1)) {
                        trIndex = trIndex + 1;
                    }
                } else return;

                if (trIndex < 0) {
                    trIndex = trLength + trIndex;
                } else if (trIndex >= trLength) {
                    trIndex = trIndex - trLength;
                }

                var thisTr = markListRateTableTrs.eq(trIndex);
                // 获取对应数据
                var index = (Number)(thisTr.attr("answerIndex"));

                _this.answer = _this.answers[index];

                _this.buildAnswer(trIndex);
                // 列表选中状态
                thisTr.addClass("active");

                // 该题目是否已全部批阅完成
                if (!_this.allMarked) {
                    var allMarked = true; //这道题目批了以后全部批阅完成
                    $.each(_this.answers, function(a, answer) {
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
                }
            });

            // 保存按钮
            $("#saveAnswers").off("click").on("click",function () {
                _this.doSaveAnswers();
            });

            // 模态框关闭事件
            // $('#myModal').off("hide.bs.modal").on("hide.bs.modal", function () {
            //     _this.doSaveAnswers(true);
            // });

            // 模态框加载完毕执行
            $('#myModal').off('shown.bs.modal').on('shown.bs.modal', function (e) {
                // 自适应宽高
                MarkImage.questionWidth = 0;
                _this.selfAdaption();
                LeftMarkArea.initHtml();
                var thisTr = _this.getThisTr();
                _this.scrollTo(thisTr);
                $('#answerByScore').focus();
            });

            $(window).bind('resize', function(){
                // 只做模态框自适应
                if($('body').hasClass('modal-open')){
                    _this.selfAdaption();
                }
            });
        },

        selfAdaption : function() {
            // 获取窗口大小
            var minHeight = 760;
            var windowWidth = $(window).width();
            var windowHeight = minHeight;

            if ($(window).height() > minHeight) {
                windowHeight = $(window).height();
            }

            // 获取模态框宽高
            var modalWidth = windowWidth * 0.7;
            var modalHeight = windowHeight * 0.9;

            if (windowWidth <= 1366) {
                modalWidth = windowWidth * 0.9;
            }

            // 模态框
            $(".modal-dialog").width(modalWidth).height(modalHeight);
            $(".modal-content").width(modalWidth).height(modalHeight);

            // 左右侧
            var modalContentLeftWidth = modalWidth * 0.8;
            var modalContentRightWidth = modalWidth - modalContentLeftWidth -2;
            $(".modal-content .left").width(modalContentLeftWidth).height(modalHeight);
            $(".modal-content .right").width(modalContentRightWidth).height(modalHeight);

            /* 右侧 */
            var leftCanvasRootAreaHeight = modalHeight * 0.85;
            if (MarkImage.questionWidth === 0) {
                MarkImage.questionWidth = $(".left .quetstion .title").width();
            }
            $(".left .quetstion .title").width(modalContentLeftWidth * 0.89);
            // 绘图区
            $(".left .canvas_root_area").width(modalContentLeftWidth).height(leftCanvasRootAreaHeight);

            // 画图
            var toolHeight = $('.canvas_root_area .tools').outerHeight();
            var imgNavHeight = $('.canvas_root_area .user_answer_canvas').outerHeight();
            // var quesAreaHeight = $('.quetstion').outerHeight();
            var quesAreaHeight = 121;
            console.log(quesAreaHeight);
            var canvasContainerHeight = ($('.modal-content').height() - toolHeight - imgNavHeight - quesAreaHeight);
            $(".canvas_container, .canvas_container .canvas_layer").css({'height': canvasContainerHeight + 'px', 'width' : $('.modal-content').width() * 0.8 + 'px'});

            var scoresHeight = $(".scoreBg-1 .scores-1").height() + 10;
            if (scoresHeight > 141) {
                $(".scoreBg-1").css({"height" : 141, "overflow": "auto"});
            }
            var newButtonHeight = $(".right .bgButton .row .new").height();
            $(".right .bgButton").width(modalContentRightWidth).height(newButtonHeight + scoresHeight);

            // 批阅进度
            var markListRateHeight = modalHeight - $(".right .notMarkListRate").height();
            $(".right .markListRate").width(modalContentRightWidth).height(markListRateHeight).css({
                "display": "initial",
                "text-align": "center"
            });
            $(".right .markListRate .markListTitle").width(modalContentRightWidth).height(36).css("text-align", "left");
            // $(".right .markListRate .markListTableTitle").width(modalContentRightWidth).height(36);
            var markListRateTableListHeight = markListRateHeight - (36 * 2);
            $(".right .markListRate .markListRateTableList").width(modalContentRightWidth).height(markListRateTableListHeight);

            // 面板按钮自适应 css
            /*if (windowWidth > 1366) {
                $('.modal-dialog .modal-content .modal-body .right .scoreBg .scores td:nth-child(2)').css({
                    "padding-left": "7%",
                    "padding-right": "7%"
                });
                $('.modal-dialog .modal-content .modal-body .right .scoreBg .scores td').css({
                    "padding-bottom": "5%"
                });
            } else {
                $('.modal-dialog .modal-content .modal-body .right .scoreBg .scores td:nth-child(2)').css({
                    "padding-left": "2%",
                    "padding-right": "2%"
                });
                $('.modal-dialog .modal-content .modal-body .right .scoreBg .scores td').css({
                    "padding-bottom": "4%"
                });
            }*/
        },

        // 返回批阅页
        doSaveAnswers : function(isClose) {
            var _this = this;

            LeftMarkArea.complete(function () {
                // 保存所有答案
                MarkV2Main.doSave(0, _this.answers);

                // 渲染批阅页列表
                MarkV2Main.canvasMarkCallBack(_this.answers);

                if (isClose) {
                    $(".left .quetstion .title").width(MarkImage.questionWidth);
                }

                CommUtils.showSuccess(i18n['save_success']);
            });
        },

        // 保存单个答案， 执行保存操作后， 将答案修改状态该为false
        doSaveAnswer : function(trIndex) {
            var _this = this;

            // 有修改的，保存数据，并将该数组移动至末尾。
            if (_this.answer.changeStatus) {
                MarkV2Main.doSave(0, _this.answers);
                _this.answer.changeStatus = false;
                var thisTrs = $(".markListRateTable tbody tr");
                var thisTr = thisTrs.eq(trIndex);
                $(thisTr).find("td").eq(1).html(_this.answer.answerScore.toString());
                $(thisTr).find("td").eq(2).find("span").removeClass("red").addClass("green").html(i18n["marked"]);
                $(thisTr).insertAfter(thisTrs.last());
                // 重新分配trindex
                $(".markListRateTable tbody tr").each(function (index, obj) {
                    $(obj).attr("trIndex", index);
                });
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

            var scoreBg = $(".scoreBg-1 .scores-1");

            // 清空原按钮
            scoreBg.empty();

            var score = _this.question.questionScore;
            var difference = 1;
            if (score > 10) {
                difference = Math.ceil((score/10));
            }
            
            
            // 第一排固定 最大值/0/-0.5
            var firstTd = "<div><div class=\"col-lg-4\"><button style='width: 55px' type=\"button\" class=\"btn btn-default\">"+ score +"</button></div>" +
                "<div class=\"col-lg-4\"><button style='width: 55px' type=\"button\" class=\"btn btn-default\">0</button></div>" +
                "<div class=\"col-lg-4\"><button style='width: 55px' type=\"button\" class=\"btn btn-default\">-0.5</button></div></div>";
            scoreBg.append(firstTd);

            var index = 0;
            var tr = "<div>";
            while ((score -= difference) > 0) {
                tr += "<div class=\"col-lg-4\"><button style='width: 55px' type=\"button\" class=\"btn btn-default\">"+ score +"</button></div>";
                index ++;
                if ((index % 3) === 0) {
                    tr += "</div><div>";
                }
            }
            scoreBg.append(tr);

            scoreBg.find("button").off("click").on("click",function(){
                var bgClickScore = $(this).text();
                var bgClickScoreAttr = scoreBg.attr("bgClickScore");
                if (!CommUtils.isEmpty(bgClickScoreAttr) && bgClickScoreAttr === bgClickScore) {
                    return;
                } else if (bgClickScore !== "-0.5") {
                    $(".scoreBg .scores").attr("bgClickScore", bgClickScore);
                } else {
                    $(".scoreBg .scores").removeAttr("bgClickScore");
                }
                _this.setMarkScore(bgClickScore);
                $('#answerByScore').focus();
            });
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

            if (score > _this.question.questionScore) {
                score = _this.question.questionScore;
            } else if (score < 0) {
                score = 0;
            }

            var answerScore = score;
            answerByScore.val(answerScore);

            if (answerScore > (MarkV2Main.thresholdScore * _this.question.questionScore)) {
                _this.answer.answerStatus = "0";
            } else {
                _this.answer.answerStatus = "1";
            }

            _this.answer.answerScore = answerScore;

            // 修改答案批阅状态
            _this.answer.markStatus = "1";
            // 修改答案变更状态
            _this.answer.changeStatus = true;

            // 获取当前答案的下标
            var index = (Number)($("#answerByName").attr("answerIndex"));
            // 修改后放回answers, 上一题/下一题或关闭时进行保存
            _this.answers[index] = _this.answer;

            //直接更新列表值   duffy_ding    只有一个人的时候按钮置为不能点击
            var $thisTr = $(".markListRateTable tbody tr[answerindex='" + index + "']");
            $thisTr.find("td").eq(1).html(_this.answer.answerScore.toString());
            $thisTr.find("td").eq(2).find("span").removeClass("red").addClass("green").html(i18n["marked"]);
        },

        // 批阅进度滚动条
        scrollTo : function(thisTr) {
            var tableTop = $(".markListRateTableList").offset().top;
            var trTop = $(thisTr).offset().top;
            $('.markListRateTableList').animate({
                scrollTop: (trTop - tableTop - 36)
            }, 200);
        },

        // 获取当前数据对应tr
        getThisTr : function() {
            var trIndex = (Number)($("#answerByName").attr("trIndex"));
            var markListRateTableTrs = $(".markListRateTable tbody tr");
            return markListRateTableTrs.eq(trIndex);
        }

    }
})(jQuery, window);
