var AnswerCardIndex = window.AnswerCardIndex = {
    config: {
        cardType: "1",
        columnType: 1,
        pageType: 'A4',
        candidateNumberEdition: '1',
        questionContentStatus: "0",
        judgeStyle: "0",
        sealingLineStatus: "0",
        editAnswer: false, // word == true
        editType: "paper", // word
        userId: "",
    },
    answerCardTemplateUrl: "",
    saveAnswerCardUrl: "",
    paperInfo: undefined,
    answerCardInfo: undefined,
    iframeAnswerCard: undefined,
    isSaved: false,
    init: function () {
        this.initData();
        this.initIframe();
        this.initEvent();
    },
    initData: function () {
        this.isSaved = this.answerCardInfo == undefined ? true : false;
        this.buildSaved();
        this.resizeIframeMinHeighr();
        this.initConfig();
        this.initBaseInfo();
    },
    initBaseInfo: function () {
        let countQuestion = 0;
        for (let node of this.paperInfo.nodes) {
            if (node.level == 3) {
                countQuestion ++;
            }
        }
        $(".marginFixBot .count_question").text(countQuestion);
        $(".marginFixBot .total_score").text(this.paperInfo.totalPoints);
    },
    buildSaved: function () {
        if (this.isSaved) {
            $("#saveBtn").removeClass("un_reach");
            $("#pdfBtn").addClass("un_reach");
            $("#printBtn").addClass("un_reach");
        } else {
            $("#saveBtn").addClass("un_reach");
            $("#pdfBtn").removeClass("un_reach");
            $("#printBtn").removeClass("un_reach");
        }
    },
    initIframe: function () {
        let _this = this;
        $("#answerCardTemplate").attr("src", _this.answerCardTemplateUrl);
        $("#answerCardTemplate").on("load", function () {
            _this.iframeAnswerCard = document.getElementById("answerCardTemplate").contentWindow.answerCard;
            _this.changeAnswerCard("modifyData")
        });
    },
    changeAnswerCard: function (type) {
        let data = {
            type: type,
            config: this.config
        };
        if (type == "modifyData") data.data = {
            paperInfo: this.paperInfo,
            answerCardInfo: this.answerCardInfo
        };
        this.iframeAnswerCard.submitData(data)
    },
    initEvent: function () {
        let _this = this;

        $("#saveBtn").click(function (event) {
            if (!_this.isSaved) return;
            _this.saveAnswerCard(_this.iframeAnswerCard.getAnswerCardInfo())
        });
        window.addEventListener('message', function (event) {
            let data = event.data;
            if (data.type == "changeAnswerCard") {
                _this.isSaved = data.data;
                _this.changeDom();
            }
        });
        $(window).resize(function () {
            _this.resizeIframeMinHeighr();
        });

        // 设置事件
        $("#baseInfo .ac_config").click(function (event) {
            let configType = $(this).data("type");
            console.log(configType);
            if (",cardType,columnType,candidateNumberEdition,".indexOf(configType) >= 0) {
                if ($(this).hasClass("active") || _this.config[configType] == $(this).data("value")) return;
                _this.config[configType] = $(this).data("value");
                $(this).addClass('active').siblings().removeClass('active');

                _this.changeAnswerCard("modifyConfig");
                _this.isSaved = true;
                _this.buildSaved();
            } else if (",judgeStyle,sealingLineStatus,questionContentStatus,".indexOf(configType) >= 0) {
                _this.config[configType] = (_this.config[configType] == "0" ? "1" : "0");
                _this.changeOnOffDom($(this), _this.config[configType] == "0");
                _this.changeAnswerCard("modifyConfig");
                _this.isSaved = true;
                _this.buildSaved();
            }
        });

        $("#printBtn").click(function () {
            _this.browserPrint();
        });
    },
    changeDom: function () {
        let _this = this;
        _this.buildSaved();
        // _this.changeIframeHeight();
    },
    resizeIframeMinHeighr: function () {
        $("#answerCardTemplate").css("min-height", window.innerHeight - 36 - 61);
    },
    saveAnswerCard: function (answerCardInfo) {
        let _this = this;
        var loading = layer.load(2, {
            shade: false
        });
        $.ajax({
            "url": this.saveAnswerCardUrl,
            "data": JSON.stringify(answerCardInfo),
            contentType: "application/json;charset=utf-8",
            type: "post",
            success: function (result) {
                layer.close(loading);
                if (result.status == 1) {
                    _this.isSaved = true;
                    _this.buildSaved(_this.isSaved);
                    location.reload();
                } else {
                    _this.tipBox("保存答题卡信息失败，请检查网络或联系管理员重试！");
                }
            },
            error: function (result) {
                _this.tipBox("保存答题卡信息失败，请检查网络或联系管理员重试！");
                layer.close(loading);
            }
        }).always(function () {
            CommUtils.unLock(event.target);
        });
    },
    tipBox: function (content, callback) {
        var tip = layer.confirm(content, {
            title: i18n['tipBox_title'],
            anim: -1,
            closeBtn: 0,
            time: 20000,
            btn: [i18n['confirm']]
        }, function () {
            callback && callback();
            layer.close(tip);
        });
    },
    initConfig: function () {
        if (this.answerCardInfo) {
            this.config.columnType = this.answerCardInfo.columnType;
            this.config.columnType = this.answerCardInfo.columnType;
            this.config.candidateNumberEdition = this.answerCardInfo.candidateNumberEdition;
            this.config.judgeStyle = this.answerCardInfo.judgeStyle;
            this.config.sealingLineStatus = this.answerCardInfo.sealingLineStatus;
            this.config.questionContentStatus = this.answerCardInfo.questionContentStatus;
        }
        $(".ac_config[data-type='cardType'][data-value='" + this.config.cardType +"']").addClass("active");
        $(".ac_config[data-type='columnType'][data-value='" + this.config.columnType +"']").addClass("active");
        $(".ac_config[data-type='candidateNumberEdition'][data-value='" + this.config.candidateNumberEdition +"']").addClass("active");

        this.changeOnOffDom( $(".ac_config[data-type='judgeStyle']"), this.config.judgeStyle == "0");
        this.changeOnOffDom( $(".ac_config[data-type='sealingLineStatus']"), this.config.sealingLineStatus == "0");
        this.changeOnOffDom( $(".ac_config[data-type='questionContentStatus']"), this.config.questionContentStatus == "0");
    },
    browserPrint: function (){
        document.getElementById("answerCardTemplate").contentWindow.print();
    },
    changeOnOffDom: function (dom, isBoolean) {
        if (isBoolean) {
            $(dom).removeClass("radius_bor").addClass("No_radius");
            $(dom).children().removeClass("ok").addClass("no");
        } else {
            $(dom).removeClass("No_radius").addClass("radius_bor");
            $(dom).children().removeClass("no").addClass("ok");
        }
    }

}
