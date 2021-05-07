(function (win, $) {
    var perView = win.PerView = {
        isChild: false,
        isDisplay: 0,
        isQuestionNo: "",
        myMap : {},
        printPdf : false,
        init: function () {
            this.default();
            this.buildPaper();
            this.hiddenAnswer();
            $("img").attr("crossorigin", "anonymous");
            if (this.printPdf){
                $("#btndiv").css("display", "none");
            }
        },
        /**
         * 控制答案的隐藏与显示
         */
        hiddenAnswer: function () {
             var _this = this;
            //页面初始化影藏
            $(".answerGroup").css('display', 'none');//隐藏
            $('#hide_btn').on('click', function () {
                if (PerView.isDisplay == 1) {
                    PerView.isDisplay = 0;
                    $(".answerGroup").css('display', 'none');//隐藏
                } else {
                    PerView.isDisplay = 1;
                    $(".answerGroup").css('display', 'table');//显示
                }

            });

            $("#exportPdf").on("click", function(){
                var url = document.URL + "?printPdf=true&window-status=completed";
                window.open(_this.expertPDFUrl + window.btoa(url),'_blank', '');
            });
        },
        default: function () {
            var _this = this;
            $('title').html(_this.paperInfo.name);
            $('.previewHead>div').text(_this.paperInfo.name);
            $('.previewHead').append("<div style='text-align: center;'>" + _this.i18n['paper_total_points'] + "：" +_this.paperInfo.totalPoints + "分</div>");

            $('#viewAnswerCard').on('click', function () {

            });

        },
        buildPaper: function () {
            var _this = this;
            $.each(_this.paperInfo.nodes, function (index, obj) {
                _this.addQuestion(obj);
            });
            window.status = "completed";
        },
        addQuestion: function (obj) {
            var _this = this;

            var volumeTTemplate = $("#volumeTTemplate").html();
            var volumeNTemplate = $("#volumeNTemplate").html();

            if (obj.level == 1) {
                $('.previewArea').append(
                    CommUtils.formatStr(volumeTTemplate, {
                        "paperId": obj.name,
                        "paperPoint":obj.points+"分"
                    })
                );
                return;
            }

            if (obj.level == 2) {
                $('.previewArea').append(
                    CommUtils.formatStr(volumeNTemplate, {
                        "questionTitle": obj.name,
                        "questionPoint":obj.points+"分"
                    })
                );
                return;
            }

            if (obj.level == 3 || obj.level == 4) {
                _this.rendQuestion(obj);
            }

        },
        // 渲染题目
        rendQuestion: function (obj) {
            var _this = this;
            var externalNo = "";
            // 大小提设置
            if (obj.level == 3) {
                _this.isChild = true;
                externalNo = _this.isQuestionNo = obj.externalNo;
            }

            if (obj.level >= 4) {
                externalNo = _this.isQuestionNo + "." + obj.internalNo;
                // 第一题时删除大题答案
                if (_this.isChild) {
                    _this.isChild = false;
                    $(".previewArea .answerCtn:last tr:last").remove();
                }
            }

            // 开始渲染题目
            var questionTemplate = "";
            if (obj.question.type.code == "01" || obj.question.type.code == "02") {
                // 选择题模板
                questionTemplate = $('#ops_template').html();
            } else {
                // 非选择题模板
                questionTemplate = $('#general_template').html();
            }

            // 过滤答案数据
            if (obj.question.answer != null) {
                var answer = (obj.question.answer.strategy == null || obj.question.answer.strategy == '') ? obj.question.answer.lable : obj.question.answer.strategy;

                // 替换填空和判断的字符
                var conditions = [];
                if (obj.question.type.code === '03') {
                    // 根据题型确认替换字符 /@\#@/g /@\*@/g
                    var condition = {
                        'source': '/@\\#@/g',
                        'target': ','
                    };
                    var condition2 = {
                        'source': '/@\\*@/g',
                        'target': ' ' + _this.i18n['question_or'] + ' '
                    };
                    conditions.push(condition);
                    conditions.push(condition2);
                } else if (obj.question.type.code === '04') {
                    var condition3 = {
                        'source': '/T/',
                        'target': _this.i18n['question_right']
                    };
                    var condition4 = {
                        'source': '/F/',
                        'target': _this.i18n['question_wrong']
                    };
                    conditions.push(condition3);
                    conditions.push(condition4);
                }
                if (!CommUtils.isEmpty(answer)) {
                    obj.question.answer.strategy = answer;
                    for (var i = 0; i < conditions.length; i++) {
                        obj.question.answer.strategy = obj.question.answer.lable = obj.question.answer.strategy.replace(eval(conditions[i].source), conditions[i].target);
                    }
                }
            }

            $('.previewArea').append(
                CommUtils.formatStr(questionTemplate, {
                    "questionContent": (obj.question.stem == null || CommUtils.isEmpty(obj.question.stem.richText) ? '' : obj.question.stem.richText),
                    "questionNo": externalNo,
                    "questionPoint": obj.points+"分",
                    "answer": (obj.question.answer == null || CommUtils.isEmpty(obj.question.answer.strategy) ? '' : obj.question.answer.strategy),
                    "answerAnalyse": obj.question.answer == null || CommUtils.isEmpty(obj.question.answer.analysis) ? '' : obj.question.answer.analysis,
                })
            ).append("<div class='fill_blank'></div>");

            // 子节点右移
            if (obj.level >= 4) {
                $(".previewArea .answerCtn:last").addClass("child");
            }

            if (obj.question.options != null && obj.question.options.length > 0) {
                // 如果选项在题目总, 则不输出选项
                if (!obj.question.stem.richText.indexOf('id="option')) {
                    return;
                }

                $.each(obj.question.options, function (index, opt) {
                    if (!CommUtils.isEmpty(opt.label)) {
                        /* 两个选项一行 */
                        if ((index + 1) % 2 == 1) {
                            $('.previewArea .answerCtn:last .opt_list>table').append("<tr></tr>");
                        }
                        $('.previewArea .answerCtn:last .opt_list>table tr:last').append(
                            CommUtils.formatStr("<td width=\"50%\"><radio>{alias}.&nbsp</radio>{label}</td>", {
                                "label": opt.label,
                                "alias": opt.alias,
                            })
                        );
                        $('.previewArea .answerCtn:last .opt_list').show();
                    }
                });
            }
        },
        openFrame: function (title, url, width, height) { //layer frame窗口
            this.layerWindow = layer.open({
                type: 2,
                anim: -1,
                resize: false,
                title: title,
                shadeClose: false,
                maxmin: true,
                shade: 0.6,
                area: [width || '90%', height || '90%'],
                full: function (dom) {  //最大化触发
                    $(dom).data("height", $(".layui-layer-content > iframe").height());
                    $(dom).find(".layui-layer-content > iframe").height($(dom).height() - $(dom).find(".layui-layer-title").height());
                },
                restore: function (dom) { //还原出发
                    $(dom).find(".layui-layer-content > iframe").height($(dom).data("height"));
                },
                content: url //iframe的url
            });
        },
    };
})(window, jQuery);