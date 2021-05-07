/*
操作题js
* */
var QuestionOperationInfo = window.QuestionOperationInfo = {
    uploader : null,
    initWebUploader : function(panel, settings,id) {
        var activeUpLoaderFileList = {};
        var _this = this;
        _this.uploader = UploadFileUtils.create({
            uploadUrl : AnswerQuestion.fileUploadUrl,
            checkUrl: AnswerQuestion.check_file_exist_url,
            tokenUrl: AnswerQuestion.fileTokenUrl,
            pick: {id: '#upload_file_'+id, multiple: false}
        }, {
            beforeAddFile : function(file){
                if (CommUtils.isEmpty(activeUpLoaderFileList)) {
                    activeUpLoaderFileList = {};
                }
                if (activeUpLoaderFileList[file.name]) {
                    AnswerQuestion.tipBox(i18n['uploaded']);
                    return false;
                }
                return true;
            },
            addFile : function(file, fileMd5){
                activeUpLoaderFileList[file.name] = file.name;
                UploadFileUtils.upload(_this.uploader, file, function(file, result){
                    _this.addQuestionAnswer(activeUpLoaderFileList, result.name, result.downloadUrl, panel, settings);
                })
            },
            uploadSuccess : function(file, response){
                _this.addQuestionAnswer(activeUpLoaderFileList, response.name, response.downloadUrls[0],panel,settings);
            }
        });
    },
    addQuestionAnswer : function (activeUpLoaderFileList,fileName,downloadUrl,panel,settings){
        var _this = this;
        var $list= "<li>"
            +"<a href='"+downloadUrl+"'>"+fileName+"</a>"
            +"<div class='remove-btn'>" + i18n['delete'] + "</div>"
            +"</li>";
        panel.find(".answer_list").append($list);
        $(".remove-btn").on("click",function () {
            $(this).parent('li').remove();
            if (activeUpLoaderFileList && activeUpLoaderFileList[fileName]) {
                delete activeUpLoaderFileList[fileName];
            }
            _this.getUserAnswer(panel,settings);
        });
        _this.getUserAnswer(panel,settings);
    },
    getUserAnswer : function (panel,settings) {
        var answerList = [];
        $.each($(".test_cont").find("a"),function (index, obj) {
            answerList.push(obj.outerHTML);
        });
        // 2 答案数据保存
        var $answerArea = panel.find(".answer");
        var userAnswer = $answerArea.data("answer");
        userAnswer.userAnswer = answerList.join(",");
        $answerArea.data("answer", userAnswer);
        var answerNavSelector = settings.isMobile ? "#testCard > li[data-key='{0}']" : "#testCard div[data-key='{0}']";
        var $cardLi = $(answerNavSelector.replace("{0}", $answerArea.attr("data-key")));
        if(!CommUtils.isEmpty(answerList)) {
            if (!$cardLi.hasClass("active_li")) {
                $cardLi.addClass("active_li");
            }
        } else {
            if ($cardLi.hasClass("active_li")) {
                $cardLi.removeClass("active_li");
            }
        }
    }
};