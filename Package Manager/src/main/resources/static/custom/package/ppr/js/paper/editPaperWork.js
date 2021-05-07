var VueConfig = {
    data : {
        'paper' : {},
        'gradeList': [],
        'subjectList': [],
        'questionTypeList': [],
        'materialVersionList': [],
        'difficultyList': [],
        'i18n': {},
        'isShowImages': false,
        'addQuestionBatchSelectNode': '',
        'curChooseKnowledgeQues': ''
    },
    mounted: function () {
        if (this.paper.materialVersion !== undefined && this.paper.materialVersion.trim() !== '') {
            this.initKnowledgeTree();
        }
    },
    computed : {
        questionCount : function() { //题目数量
            var quesCount = 0;
            var totalPoints = 0;
            $.each(this.paper.nodes, function(k, kNode) {
                if (CommUtils.isEmpty(kNode.children)) {
                    kNode.points = 0;
                    return true;
                }
                var kTotalPoints = 0;
                $.each(kNode.children, function(t, tNode) {
                    if (CommUtils.isEmpty(tNode.children)) {
                        tNode.points = 0;
                        return true;
                    }
                    var qTotalPoints = 0;
                    $.each(tNode.children, function (q, qNode) {
                        if (CommUtils.isNotEmpty(qNode.points)) {
                            qTotalPoints += parseFloat(qNode.points);
                        }
                    });
                    tNode.points = qTotalPoints;
                    kTotalPoints += parseFloat(qTotalPoints);

                    quesCount += tNode.children.length;
                });
                kNode.points = kTotalPoints;
                totalPoints += parseFloat(kTotalPoints);
            });
            this.paper.totalPoints = totalPoints;
            return quesCount;
        }
    },
    watch: {
        "paper.gradeName": function (newGradeName) {
            if (newGradeName && newGradeName !== i18n['please_select_grade']) {
                this.paper.name = newGradeName + i18n['paper_work_name'];
            } else {
                this.paper.name = '';
            }
            this.paper.isSaved = false;
        },
        "paper.subjectCode": function (newSubjectCode) {
            var paper = this.paper;
            if (this.paper.name) {
                var subject = EditPaperWorkPage.subjectMap[newSubjectCode];
                if (subject.subjectName && subject.subjectName !== i18n['please_select_subject']) {
                    paper.name = paper.gradeName + subject.subjectName + i18n['paper_work_name'];
                } else {
                    paper.name = paper.gradeName + i18n['paper_work_name'];
                }
            }
            this.paper.isSaved = false;
        },

    },
    methods : {
        savePaperWork: function(event) {
            if (!CommUtils.tryLock(event.target)) {
                return;
            }

            var _this = this;
            if (_this.paper.isSaved) {
                _this.tipBox(i18n['repeat_save_paper']);// 试卷信息已保存，请勿重复保存！
                return;
            }
            var canSave = _this.canSave(true);
            if (!canSave) {
                return ;
            }
            var loading= layer.load(0, {shade: [0.5, '#000'], anim: -1});
            var paper = JSON.parse(JSON.stringify(this.paper));
            paper.nodes = _this.buildNodes(paper.nodes);
            $.ajax({
                "url": EditPaperWorkPage.savePaperUrl,
                "data": JSON.stringify(paper),
                contentType: "application/json;charset=utf-8",
                type: "post",
                success: function (result) {
                    layer.close(loading);
                    if (result.status == 1) {
                        if (window.opener) {
                            var url = EditPaperWorkPage.listUrl + "?providerCode=" + Constants.PROVIDER_CODE_PERSONAL;
                            url += "&stageCode=" + _this.paper.stageCode + "&gradeCode=" + _this.paper.gradeCode + "&subjectCode=" + _this.paper.subjectCode + "&usageCode=paperwork";
                            window.opener.location.href = url;
                        }
                        _this.paper.paperId = result.data;
                        _this.paper.isSaved = true;
                        document.location.href = document.location.pathname + "?paperId=" + _this.paper.paperId;
                    } else {
                        _this.tipBox(i18n['save_paper_work_error']);
                    }
                },
                error : function(result) {
                    _this.tipBox(i18n['save_paper_work_error']);
                    layer.close(loading);
                }
            }).always(function() {
                CommUtils.unLock(event.target);
            });
        },
        buildAnswerAnalysis : function(analysusFileList) {
            if (CommUtils.isEmpty(analysusFileList) || analysusFileList.length < 1) {
                return null;
            }
            var answerAnalysis = "<p>";
            var answerAnalysiaImgT = "<img src=\"{0}\" title=\"{1}\" alt=\"{1}\" filesize=\"{2}\">"
            for (let analysusFile of analysusFileList) {
                answerAnalysis += CommUtils.formatStr(answerAnalysiaImgT, analysusFile.url, analysusFile.fileName, analysusFile.size);
            }
            answerAnalysis += "</p>"
            return answerAnalysis;
        },
        buildNodes : function(kNodes){// 构造节点信息
            var _this = this, sequencing = 1, nodes = [];
            kNodes.forEach(function(kNode, k) {
                var tNodes = kNode.children;
                kNode.sequencing = sequencing++;
                kNode.parentId = '';
                delete kNode.children;
                delete kNode.nodeSearchCode;
                nodes.push(kNode);
                tNodes.forEach(function(tNode, t) {
                    var qNodes = tNode.children;
                    tNode.sequencing = sequencing++;
                    tNode.parentId = kNode.nodeId;
                    delete tNode.children;
                    delete tNode.nodeSearchCode;
                    delete tNode.typeCode;
                    nodes.push(tNode);
                    qNodes.forEach(function(qNode, q) {
                        var cNodes = qNode.children;
                        qNode.sequencing = sequencing++;
                        qNode.parentId = tNode.nodeId;

                        // 转换解析图片格式
                        var question = qNode.question;
                        if (CommUtils.isNotEmpty(question) && CommUtils.isNotEmpty(question.answer)) {
                            if (CommUtils.isNotEmpty(question.answer.analysisFileList)) {
                                question.answer.analysis = _this.buildAnswerAnalysis(question.answer.analysisFileList)
                            } else {
                                question.answer.analysis = "";
                            }
                            delete question.answer.analysisFileList;
                        }

                        delete qNode.children;
                        delete qNode.nodeSearchCode;
                        //delete qNode.question.children;
                        nodes.push(qNode);
                        if (!CommUtils.isEmpty(cNodes)) {
                            cNodes.forEach(function(cNode, c) {
                                cNode.sequencing = sequencing++;
                                cNode.parentId = qNode.nodeId;
                                delete cNode.children;
                                delete cNode.nodeSearchCode;
                                nodes.push(cNode);
                            });
                        }
                    });
                });
            });
            return nodes;
        },
        changeQuestionNodeType: function(typeCode, question) {
            if (typeCode === "01" || typeCode === "02") {
                question.options = [
                    {'alias': 'A', 'lable': '', 'sequencing': '0', 'active': true},
                    {'alias': 'B', 'lable': '', 'sequencing': '1', 'active': false},
                    {'alias': 'C', 'lable': '', 'sequencing': '2', 'active': false},
                    {'alias': 'D', 'lable': '', 'sequencing': '3', 'active': false}
                ];
                question.answer.lable = 'A';
                question.answer.strategy = "<span class=\"answerHeader\" style=\"color:red;font-weight:bold;\">" + i18n['question_answer'] +"</span>" + question.answer.lable;
            } else if (typeCode === "03") {
                // 清空选项
                question.options = [];
                var blanks = [
                    '', '', ''
                ];
                Vue.set(question, "blanks", blanks);
                question.answer.lable = question.blanks.join("@#@");
                question.answer.strategy= "<p><span style=\"font-weight: bold;\">" + question.answer.lable + "</span></p>";
            } else if (typeCode === "04") {
                // 清空选项
                question.options = [];
                question.answer.lable = "T";
                question.answer.strategy = "T";
            } else {
                // 清空选项
                question.options = [];
                question.answer.lable = "";
                question.answer.strategy = "<span class=\"answerHeader\" style=\"color:red;font-weight:bold;\">" + i18n['question_answer'] +"</span>" + question.answer.lable;
            }
        },
        changeAnswer: function(value, question, typeCode, event) {
            var _this = this;
            if (typeCode === "01" || typeCode === "02") {
                if (typeCode === "01") {
                    question.answer.lable = value.alias;
                    question.answer.strategy = "<span class=\"answerHeader\" style=\"color:red;font-weight:bold;\">" + i18n['question_answer'] +"</span>" + value.alias;
                } else {
                    if (!value.active) {
                        value.active = true;
                        question.answer.lable += ("," + value.alias);
                        question.answer.strategy += ("," + value.alias);
                    } else {
                        value.active = false;
                        var answer = {
                            "lable": "",
                            "strategy": "<span class=\"answerHeader\" style=\"color:red;font-weight:bold;\">" + i18n['question_answer'] +"</span>",
                            "analysis": question.answer.analysis,
                            "analysisFileList": question.answer.analysisFileList
                        };
                        var sum = 0;
                        var lable = "";
                        $.each(question.options, function (index, option) {
                            if (option.active) {
                                sum ++;
                                lable += (option.alias + ",")
                            }
                        });
                        if (sum > 0) {
                            lable = lable.substring(0, lable.length-1);
                            answer.lable = lable;
                            answer.strategy = (answer.strategy + lable);
                            question.answer = answer;
                        } else {
                            value.active = true;
                            // 必须保留一个选项
                            $(event.target).tip(CommUtils.formatStr(_this.i18n['ques_answer_min_num'], 1), "top");
                        }
                    }
                }
            } else if (typeCode === "03") {
                var lable = question.blanks.join("@#@");
                question.answer.lable = lable;
                question.answer.strategy = "<p><span style=\"font-weight: bold;\">" + lable + "</span></p>";
            } else {
                question.answer.strategy = "<span class=\"answerHeader\" style=\"color:red;font-weight:bold;\">" + i18n['question_answer'] +"</span>" + question.answer.lable;
            }
        },
        changeOption: function(options, type, answer, event) {
            var _this = this;
            var option = CommUtils.clone(options[options.length-1]);
            if (type === 1) { // 判断题
                // 限制8个选项
                if (option.alias.charCodeAt(0) < 72) {
                    option.alias = String.fromCharCode(option.alias.charCodeAt(0) + 1);
                    option.sequencing = (parseInt(option.sequencing) + 1);
                    option.active = false;
                    options.push(option);
                } else {
                    $(event.target).tip(CommUtils.formatStr(_this.i18n['option_max_num'], 8), "top");
                }
            } else if (type === 2) {
                if (options.length > 2) {
                    if (option.alias === answer.lable) {
                        answer.lable = options[options.length-2].alias;
                    }
                    options.splice(options.length-1, 1);
                    //去掉的选项有可能包含之前选择的正确答案,所以要去掉
                    answer.lable = answer.lable.split(",").filter(e => e != option.alias).reduce((x, y) => x + "," + y);
                    answer.strategy = '<span class="answerHeader" style="color:red;font-weight:bold;">'+i18n['question_answer']+'</span>' + answer.lable;
                } else {
                    $(event.target).tip(CommUtils.formatStr(_this.i18n['option_min_num'], 2), "top");
                }
            } else if (type === 3 || type === 4) {
                if (type === 3) {
                    if (options.length < 8) {
                        options.push("");
                    } else {
                        $(event.target).tip(CommUtils.formatStr(_this.i18n['option_max_num'], 8), "top");
                    }
                } else {
                    if (options.length > 1) {
                        options.splice(options.length-1, 1);
                    } else {
                        $(event.target).tip(CommUtils.formatStr(_this.i18n['option_min_num'], 1), "top");
                    }
                }
                // 重新赋值
                var lable = options.join("@#@");
                answer.lable = lable;
                answer.strategy = "<p><span style=\"font-weight: bold;\">" + lable + "</span></p>";

            }
        },
        limitLength: function(event, node, type) {
            var _this = this;
            if (type === 'tNodeName') {
                if (node.name.length > 60) {
                    $(event.target).tip(i18n['ques_des_name_max_length'], 'right', 1500);
                    node.name = node.name.substring(0, 60);
                    CommUtils.moveCursorEnd(event.target);
                }
            } else if (type === 'paperName') {
                if (node.name.length > 32) {
                    $(event.target).tip(i18n['paper_des_name_max_length'], 'bottom', 1500);
                    node.name = node.name.substring(0, 32);
                    CommUtils.moveCursorEnd(event.target);
                }
            } else if (type === 'externalNo') {
                // 限制中文
                node.externalNo = CommUtils.filterString(node.externalNo, 1);
                if (node.externalNo.length > 10) {
                    $(event.target).tip(i18n['externalNo_des_name_max_length'], 'bottom', 1500);
                    node.externalNo = node.externalNo.substring(0, 10);
                    CommUtils.moveCursorEnd(event.target);
                }
            } else if (type === 'points') {
                // 不等数字类型
                if(isNaN(Number(node.points))) {
                    $(event.target).tip(i18n['please_input_num'], 'bottom', 1500);
                    if (node.points.length === 1) {
                        node.points = 1;
                    } else {
                        node.points = node.points.substring(0, node.points.length-1);
                    }
                } else {
                    if (node.points.indexOf(".") > 0) {
                        var pointsAttr = node.points.split(".");
                        if (pointsAttr[0].length > 1){
                            node.points = node.points.substring(0, 5);
                        } else {
                            node.points = node.points.substring(0, 4);
                        }
                    } else {
                        if (node.points.indexOf("0") === 0) {
                            node.points = parseInt(node.points);
                        } else {
                            node.points = node.points.substring(0, 2);
                        }
                    }
                }
            }
        },
        resetNode: function(params, msgTitle, callback, isInitTrue) {
            var _this = this;
            if (_this.paper.nodes.length > 0) {
                return layer.confirm(CommUtils.formatStr(_this.i18n['reset_paper_tNode'], msgTitle), {
                    btn: [_this.i18n['confirm'], _this.i18n['cancel']]
                }, function(index){
                    callback(_this, params, true);
                    _this.paper.nodes = [];
                    if (CommUtils.isNotEmpty(_this.paper.subjectCode) && CommUtils.isNotEmpty(_this.paper.materialVersion)) {
                        _this.paper.nodes.push(EditPaperWorkPage.renderKNode(_this.paper.nodes));
                        _this.initKnowledgeTree();
                    }
                    CommUtils.showSuccess(_this.i18n['reset_nodes']);
                    layer.close(index);
                }, function(index){
                    callback(_this, params, false);
                    layer.close(index);
                    _this.$nextTick(function () {
                        _this.paper.isSaved = true;
                    });
                });
            } else {
                callback(_this, params, true);
                if (isInitTrue) {
                    _this.paper.nodes.push(EditPaperWorkPage.renderKNode(_this.paper.nodes));
                    _this.initKnowledgeTree();
                }
            }
        },
        addQuestionBatch: function(tNode) {
            this.addQuestionBatchSelectNode = tNode;
            layer.open({
                type: 2,
                title: i18n['question_batch'],
                shadeClose: true,
                scrollbar: false,
                shade: 0.6,
                area: ['500px', '490px'],
                content: EditPaperWorkPage.addQuestionBatchUrl //url
            });
        },
        addQuestion: function(tNode) {
            var internalNo = tNode.children.length + 1;
            var questionTypeCode = tNode.children[tNode.children.length - 1].question.type.code;
            tNode.children.push(EditPaperWorkPage.renderQNode(1, internalNo, internalNo, internalNo, questionTypeCode, tNode));
            this.paper.isSaved = false;
        },
        addTNode: function(kNode, tIndex) {
            kNode.children.push(EditPaperWorkPage.renderTNode(kNode));
            this.paper.isSaved = false;
        },
        addKNode: function(nodes, kIndex) {
            nodes.push(EditPaperWorkPage.renderKNode(nodes));
            this.paper.isSaved = false;
        },
        removeImage: function(index, questionAnswer) {
            if (CommUtils.isEmpty(questionAnswer)) {
                this.paper.paperWorkFileList.splice(index, 1);
                if (this.paper.paperWorkFileList.length === 0) {
                    this.changeImageShow();
                }
            } else {
                questionAnswer.analysisFileList.splice(index, 1);
            }
            this.paper.isSaved = false;
        },
        removeNode: function(node, index, type) {
            var _this = this;
            var isSave = false;
            if (type === 1) {
                if (node.length > 1) { // kNode
                    node.splice(index, 1);
                    node[index].name = CommUtils.formatStr(i18n['kind_0'], index + 1);
                    isSave = true;
                }
            } else if (type === 2) { // tNode
                if (node.children.length > 1) {
                    node.children.splice(index, 1);
                    node = node.children[index];
                    var typeNameList = node.name.split("、");
                    node.name = CommUtils.numberToChinese(index + 1) + "、" + typeNameList[typeNameList.length-1];
                    isSave = true;
                }
            }else if(type === 3) { // qNode
                node.children.splice(index, 1);
                $.each(node.children, function (qIndex, qNode) {
                    if (qIndex >= index) {
                        // 自定义非数字题号不重写
                        if (!isNaN(Number(qNode.externalNo))) {
                            qNode.externalNo = (qIndex + 1);
                        }
                    }
                });
                isSave = true;
            } else if (type === 4) { // 知识点
                node.splice(index, 1);
                isSave = true;
            }

            if (isSave) {
                _this.paper.isSaved = false;
            } else {
                layer.alert(CommUtils.formatStr(_this.i18n['nodes_min_num'], 1), {
                    btn: [_this.i18n['confirm']]
                });
            }
        },
        chooseKnowledge : function(question, target) {
            var _this = this;
            if (question.chooseKnowledge) {
                Vue.set(question, 'chooseKnowledge', false);
                return;
            }
            var $this = $(target);
            // 判断是 i 还是span
            if ($this.find("i").length > 0) {
                $this = $this.find("i");
            }
            var $parentTags = $this.parent().parent();
            var $parentW = $parentTags.width();
            var $top = $this.offset().top + $parentTags.height();
            var $left = $parentTags.offset().left;
            var $bodyH = $('body').height();
            var $treeH = $("#fixed-tree-box").height();
            var s = $top + $treeH - $bodyH;
            if (s > 0) {
                $top = $top - $treeH - $this.parent().height();
            }
            $("#fixed-tree-box").css({'top': $top + 'px', 'width': $parentW + 'px', 'left': $left + 'px'}); //设置知识点弹窗样式
            var treeObj = $('#knowledgeTree').jstree(true);
            treeObj.uncheck_all();
            Vue.set(question, 'chooseKnowledge', true);
            _this.curChooseKnowledgeQues = question;
            treeObj.check_node($.map(question.knowledgeList, function(item){
                return item.knowledgeId;
            }));
        },
        copyPreviousKnowledge: function(tNode, index) {
            var _this = this;
            if (CommUtils.isEmpty(tNode.children) && CommUtils.isEmpty(index)
                && index === 0) {
                return;
            }
            var qNode = tNode.children[index];
            var previousQNode = tNode.children[index - 1];
            if (CommUtils.isNotEmpty(previousQNode.question) && CommUtils.isNotEmpty(qNode.question)
                && CommUtils.isNotEmpty(previousQNode.question.knowledgeList)) {
                qNode.question.knowledgeList = CommUtils.clone(previousQNode.question.knowledgeList);
            }
        },
        changeImageShow: function() {
            this.isShowImages = !this.isShowImages;
            if (this.isShowImages) {
                $(".paper-img-box").slideDown();
            } else {
                $(".paper-img-box").slideUp();
            }
        },
        verificationImageFiles: function(e, analysisFileList) {
            var _this = this;

            var uploadImagesMap = {};
            var verificationImageFileMap = {};
            var duplicateFileNames = "";
            var fileList = analysisFileList;
            if (CommUtils.isEmpty(fileList)) {
                fileList = _this.paper.paperWorkFileList;
            }
            $.each(fileList, function (index, file) {
                var fileKey = file.fileName + file.size;
                uploadImagesMap[fileKey] = file;
            });
            $.each(e.target.files, function (index, file) {
                var fileKey = file.name + file.size;
                if (uploadImagesMap[fileKey] || file.type.indexOf("image") < 0) {
                    verificationImageFileMap[fileKey] = file;
                    if (CommUtils.isNotEmpty(duplicateFileNames)) {
                        duplicateFileNames += (',' +  file.name);
                    } else {
                        duplicateFileNames += file.name;
                    }
                }
            });

            if (CommUtils.isNotEmpty(duplicateFileNames)) {
                CommUtils.showFail(i18n['duplicate_file'] + duplicateFileNames);
            }

            return verificationImageFileMap;
        },
        uploadConfig: function(e, questionAnswer) {
            var _this = this;
            var analysisFileList = CommUtils.isNotEmpty(questionAnswer) ? questionAnswer.analysisFileList : null;
            var verificationImageFiles = _this.verificationImageFiles(e, analysisFileList);
            var responses = CommUtils.uploadFile(e, EditPaperWorkPage.uploadPaperWorkImagesUrl, verificationImageFiles);
            if (CommUtils.isNotEmpty(responses) && CommUtils.isNotEmpty(responses.successFiles)) {
                // 添加排序字段
                $.each(responses.successFiles, function (index, response) {
                    $.extend(response, {
                        'sequencing': index
                    });
                    if (CommUtils.isEmpty(questionAnswer)) {
                        if (CommUtils.isEmpty(_this.paper.paperWorkFileList )) {
                            _this.paper.paperWorkFileList = [];
                        }
                        _this.paper.paperWorkFileList.push(response);
                    } else {
                        var analysisFileTemp = {
                            url: response.url,
                            fileName: response.fileName,
                            size: response.size
                        }
                        if (CommUtils.isEmpty(analysisFileList)) {
                            questionAnswer.analysisFileList = analysisFileList = [];
                        }
                        analysisFileList.push(analysisFileTemp);
                    }
                    _this.paper.isSaved = false;
                });
                if (!this.isShowImages && CommUtils.isEmpty(questionAnswer)) {
                    _this.changeImageShow();
                }
                CommUtils.showSuccess(i18n['file_upload_success']);
            }

            if (CommUtils.isNotEmpty(responses) && CommUtils.isNotEmpty(responses.errorFiles)) {
                var errorFileNames = '';
                $.each(responses.errorFiles, function (index, errorFile) {
                    if (CommUtils.isEmpty(errorFileNames)) {
                        errorFileNames += errorFile.fileName;
                    } else {
                        errorFileNames += "," + errorFile.fileName;
                    }
                });
                if (CommUtils.isNotEmpty(errorFileNames)) {
                    CommUtils.showFail(errorFileNames + i18n['file_upload_error']);
                }
            }
        },
        changeGrade: function(gradeCode) {
            this.resetNode(gradeCode, i18n['grade'],function(_this, gradeCode, isTrue) {
                if (isTrue) {
                    var grade = EditPaperWorkPage.gradeMap[gradeCode];
                    _this.paper.stageCode = grade.stageCode;
                    _this.paper.stageName = grade.stageName;
                    _this.paper.gradeName = grade.gradeName;
                    EditPaperWorkPage.paper.gradeCode = gradeCode;
                    EditPaperWorkPage.paper.gradeName = grade.gradeName;
                    _this.subjectList = EditPaperWorkPage.stageGradeSubjectsMap[gradeCode];
                    _this.paper.subjectCode = "";
                    _this.paper.subjectName = "";
                    _this.materialVersionList = EditPaperWorkPage.meterialVersionMap[""];
                    _this.paper.materialVersion = "";
                } else {
                    _this.paper.gradeCode = EditPaperWorkPage.paper.gradeCode;
                }
            });
        },
        changeSubject: function(subjectCode) {
            this.resetNode(subjectCode, i18n['subject'], function (_this, subjectCode, isTrue) {
                if (isTrue) {
                    var subject = EditPaperWorkPage.subjectMap[subjectCode];
                    _this.paper.subjectName = subject.subjectName;
                    EditPaperWorkPage.paper.subjectCode = subjectCode;
                    EditPaperWorkPage.paper.subjectName = subject.subjectName;
                    var materialVersionKey = _this.paper.gradeCode + "_" + subjectCode;
                    var materialVersionList = EditPaperWorkPage.meterialVersionMap[materialVersionKey];
                    if (CommUtils.isEmpty(materialVersionList)) {
                        materialVersionList = EditPaperWorkPage.meterialVersionMap[""];
                    }
                    _this.materialVersionList = materialVersionList;
                    // _this.questionTypeList = EditPaperWorkPage.subjectQuestionMap[subjectCode];
                    _this.paper.materialVersion = "";
                } else {
                    _this.paper.subjectCode = EditPaperWorkPage.paper.subjectCode;
                }
            });
        },
        changeMaterialVersion: function(materialVersion) {
            this.resetNode(materialVersion, i18n['materialVersion'], function (_this, materialVersion, isTrue) {
                if (isTrue) {
                    _this.paper.materialVersion = materialVersion;
                    EditPaperWorkPage.paper.materialVersion = materialVersion;
                    _this.paper.isSaved = false;
                } else {
                    _this.paper.materialVersion = EditPaperWorkPage.paper.materialVersion;
                }
            }, true);
        },
        viewKnowledgeStatistics : function() {
            var _this = this;
            var questions = EditPaperWorkPage.getQuestions();
            if (questions.length === 0) {
                layer.alert(i18n['paper_nodes_empty'], {
                    btn: [_this.i18n['confirm']]
                });
                return;
            } else {
                var url = EditPaperWorkPage.viewKnowledgeStatisticsUrl;
                this.frame(i18n['view_knowledge_statistics'], url, '500px', '650px');//"查看知识点"
            }
        },
        viewAnswerCard : function() {
            if (!this.paper.isSaved) {
                this.tipBox(i18n['save_paper_work_first']);//请先保存试卷！
                return;
            }
            var url = CommUtils.formatStr(EditPaperWorkPage.viewAnswerCardUrl, this.paper.paperId);
            this.frame(i18n['answer_card'], url, '92%', '96%');//答题卡
        },
        viewPaper : function() {
            if (!this.paper.isSaved) {
                this.tipBox(i18n['save_paper_work_first']);//"请先保存试卷！"
                return;
            }
            var url = CommUtils.formatStr(EditPaperWorkPage.viewPaperUrl, this.paper.paperId);
            this.frame(i18n['preview'], url, '92%', '96%');//预览
        },
        tipBox : function(content, callback) {
            var tip = layer.confirm(content, {
                title : i18n['tipBox_title'],
                anim: -1,//无动画
                closeBtn: 0,
                time: 20000,
                btn: [i18n['confirm']] //按钮
            }, function () {
                callback && callback();
                layer.close(tip);
            });
        },
        initKnowledgeTree : function() {
            var _this = this;
            $(':jstree').jstree('destroy');
            var param = {
                'subjectCode': this.paper.subjectCode,
                'gradeCode': this.paper.gradeCode,
                'materialVersion': this.paper.materialVersion,
                'providerCode': Constants.PROVIDER_CODE_PUBLIC
            };
            $.ajax({
                method: "post",
                url: EditPaperWorkPage.queryKnowledgeTreeUrl,
                cache: false,
                data: param,
                success: function (data) {
                    if (data && data !== "" && data !== "[]") {
                        knowledgeSetting.core.data = $.map(data.data, function(item){
                            return {
                                'id' : item.knowledgeId,
                                'parent' : CommUtils.ifEmpty(item.parentId, '#'),
                                'text' : item.knowledgeName,
                                'state' : {opened : false, disabled : false, selected : false},
                                'li_attr' : {
                                    'id':  item.knowledgeId, 'name' : item.knowledgeName, 'searchCode' : item.searchCode, 'outlineId' : item.outlineId
                                }
                            };
                        });
                        $('#knowledgeTree').unbind('changed.jstree').on('changed.jstree', function (e, data) {
                            if (data.action == "deselect_all") return;
                            var checkNode = data.instance.get_top_checked(true);
                            var knowledges = $.map(checkNode, function(item) {
                                return {
                                    'knowledgeId': item.li_attr.id,
                                    'knowledgeName': item.li_attr.name,
                                    'searchCode': item.li_attr.searchCode
                                };
                            });
                            _this.curChooseKnowledgeQues.knowledgeList = knowledges;
                            _this.paper.isSaved = false;
                        }).jstree(knowledgeSetting);
                    }
                }
            });
        },
        frame : function(title, url, height, width) { //弹出框
            layer.open({
                type: 2,
                title: title,
                shadeClose: true,
                scrollbar: false,
                shade: 0.6,
                area: [width, height],
                content: url //iframe的url
            });
        },
        canSave : function(scroll) {
            var _this = this;
            var paperBaseInfo = true;
            if (CommUtils.isEmpty(_this.paper.name)) {
                scroll && _this.validTip("paper_base_info_name", i18n['paper_name_empty'], "bottom"); // 试卷基础名称不能为空！
                paperBaseInfo = false;
            }
            if (CommUtils.isEmpty(_this.paper.gradeCode)) {
                scroll && _this.validTip("paper_base_info_grade", i18n['paper_grade_empty'], "bottom"); // 试卷基础信息不能为空！
                paperBaseInfo = false;
            }
            if (CommUtils.isEmpty(_this.paper.subjectCode)) {
                scroll && _this.validTip("paper_base_info_subject", i18n['paper_subject_empty'], "bottom"); // 试卷基础信息不能为空！
                paperBaseInfo = false;
            }
            if (CommUtils.isEmpty(_this.paper.materialVersion)) {
                scroll && _this.validTip("paper_base_info_materialVersion", i18n['paper_materialversion_empty'], "bottom"); // 试卷基础信息不能为空！
                paperBaseInfo = false;
            }
            var flag = !CommUtils.isEmpty(_this.paper.nodes);
            $.each(_this.paper.nodes, function(k, kNodeTemp) {
                var kNode = kNodeTemp;
                if (CommUtils.isEmpty(kNode.children)) {
                    flag = false;
                    scroll && _this.validTip(kNode.nodeSearchCode, CommUtils.formatStr(i18n['kind_empty'], kNode.name), "top"); //卷别[{0}]不能为空
                    document.getElementById(kNode.nodeSearchCode).scrollIntoView({block: "center"});
                }
                $.each(kNode.children, function (t, tNodeTemp) {
                    var tNode = tNodeTemp;
                    if (CommUtils.isEmpty(tNode.children) || CommUtils.isEmpty(tNode.name)) {
                        flag = false;
                        if (CommUtils.isEmpty(tNode.name)) {
                            scroll && _this.validTip(tNode.nodeSearchCode, i18n['paper_ques_type_name_empty'], "top"); //题型[{0}]不能为空
                        }
                        if (CommUtils.isEmpty(tNode.children)) {
                            scroll && _this.validTip(tNode.nodeSearchCode, i18n['paper_ques_type_empty'], "right"); //题型[{0}]不能为空
                        }
                        // 滚动至对应位置
                        document.getElementById(tNode.nodeSearchCode).scrollIntoView({block: "center"});
                    }
                    $.each(tNode.children, function(q, qNodeTemp) {
                        var qNode = qNodeTemp;
                        // 判断是否为占位题
                        if(CommUtils.isEmpty(qNode.question) || !CommUtils.isEmpty(qNode.question) && qNode.question.isTempQuestion) {
                            flag = false;
                            scroll && _this.validTip(tNode.nodeSearchCode, CommUtils.formatStr(i18n['ques_empty'], kNode.name, tNode.name, qNode.externalNo), "top"); // 题目不能为空
                            // 滚动至对应位置
                            document.getElementById(qNode.nodeSearchCode).scrollIntoView({block: "center"});
                        }

                        if (CommUtils.isEmpty(qNode.children)) {
                            if (CommUtils.isEmpty(qNode.externalNo)) {
                                flag = false;
                                scroll && _this.validTip(qNode.nodeSearchCode + "_externalNo", i18n['ques_externalno_empty'], "top"); //题目 {0} 分数不能为0
                                document.getElementById(qNode.nodeSearchCode).scrollIntoView({block: "center"});
                            }
                            if (CommUtils.isEmpty(qNode.points) || qNode.points == 0) {
                                flag = false;
                                scroll && _this.validTip(qNode.nodeSearchCode + "_points", i18n['ques_points_empty'], "top"); //题目 {0} 分数不能为0
                                document.getElementById(qNode.nodeSearchCode).scrollIntoView({block: "center"});
                            }
                        } else {
                            $.each(qNode.children, function(c, cNode) {
                                if (CommUtils.isEmpty(cNode.points) || cNode.points == 0) {
                                    flag = false;
                                    scroll && _this.validTip(cNode.nodeSearchCode, CommUtils.formatStr(i18n['ques_child_points_empty'], kNode.name, tNode.name, qNode.internalNo, cNode.internalNo), "top"); //题目 {0} 分数不能为0
                                    document.getElementById(cNode.nodeSearchCode).scrollIntoView({block: "center"});
                                }
                            });
                        }
                    });
                });
            });

            return flag&&paperBaseInfo;
        },
        validTip : function(nodeSearchCode, content, tipType) {
            nodeSearchCode && document.getElementById(nodeSearchCode).scrollIntoView({block: "center"});
            // content && this.tipBox(content);
            content && $(document.getElementById(nodeSearchCode)).tip(content, tipType, 12000);
        },
    }
};
var knowledgeSetting = {
    "core" : {
        "data" : [],
        "multiple" : true,
        "animation" : 0,
        "themes" : {
            "variant" : "large"
        }
    },
    "checkbox" : {
        "keep_selected_style" : false
    },
    "plugins" : [ "wholerow", "checkbox" ]
};
var EditPaperWorkPage = window.EditPaperWorkPage = {
    nodeSeq : 1, // 节点序列
    vm : null, //vue对象
    paper : null, //试卷信息
    teacherBaseInfoStr : '', //教师基础信息字符串
    stageGradeSubjects : [],
    stageMap: {},
    gradeMap: {},
    subjectMap: {},
    stageGradeSubjectsMap: {},
    subjectQuestionMap : {},
    meterialVersionMap : {},
    questionMap: {},
    difficulties : [],
    editBaseInfoUrl : '',
    init: function () {
        this.initData();
        this.initVue();
        this.initMessage();
        window.getPaperQuestions = this.getPaperQuestions;
        window.getPaperInfo = this.getPaperInfo;
    },
    initData: function () {
        var _this = this;
        _this.initStageGradeSubject();
        _this.initMaterialVersionMap();
        _this.initSubjectQuestionMap();
        _this.initPaperWork();
    },
    initPaperWork: function () {
        var _this = this;
        if (_this.paper == null) {
            return;
        }
        var paper = _this.paper;
        paper.isSaved = true;

        if (CommUtils.isEmpty(paper.paperId) && !CommUtils.isEmpty(_this.teacherBaseInfoStr)) {
            var baseInfo = _this.teacherBaseInfoStr;
            paper.stageCode = baseInfo.stageCode;
            paper.stageName = baseInfo.stageName;
            paper.gradeCode = baseInfo.gradeCode;
            paper.gradeName = baseInfo.gradeName;
            paper.subjectCode = baseInfo.subjectCode;
            paper.subjectName = baseInfo.subjectName;
            paper.school = {
                "id" : _this.teacherBaseInfoStr.schoolId,
                'name' : _this.teacherBaseInfoStr.schoolName
            };
            if (CommUtils.isEmpty(paper.name)) {
                _this.paper.name = paper.gradeName + paper.subjectName + i18n['paper_work_name'];
            }
            if (CommUtils.isEmpty(paper.materialVersion)) {
                paper.materialVersion = '';
            }
            paper.isSaved = false;
        }

        if (CommUtils.isEmpty(paper.stageCode)) {
            var baseInfo = _this.teacherBaseInfoStr;
            paper.stageCode = baseInfo.stageCode;
            paper.stageName = baseInfo.stageName;
            paper.isSaved = false;
        }

        // 添加试卷时默认值
        if (CommUtils.isEmpty(paper.nodes)) {
            paper.nodes = [];
            if (CommUtils.isNotEmpty(paper.materialVersion)) {
                var kindNode = _this.renderKNode(paper.nodes);
                paper.nodes.push(kindNode);
                paper.totalPoints = 0;
                paper.isSaved = false;
            }
        }
        // 节点检索码生成
        var lastKindNodeId='', lastTypeId = '', lastQuesId = '';
        var parentIdMap = {};
        var nodeMap = {};
        var quesChildren = [], quesList = [], typeList = [], kindList = [];
        $.each(paper.nodes, function(n, node) {
            switch (node.level) {
                case Constants.EXAM_KIND_LEVEL:
                    lastKindNodeId = node.nodeId;
                    node.nodeSearchCode = _this.generateNodeSearchCode(lastKindNodeId);
                    kindList.push(node);
                    // nodeMap 不为空时, 取出数据放到下级
                    if (nodeMap[node.nodeId]) {
                        node.children.push(nodeMap[node.nodeId]);
                    }
                    nodeMap[node.nodeId] = node;
                    break;
                case Constants.EXAM_QUES_TYPE_LEVEL:
                    // parentNodeId不为空时再使用之前的
                    if (node.parentNodeId) {
                        parentIdMap[node.nodeId] = node.parentNodeId;

                        if (nodeMap[node.parentNodeId]) {
                            if (!nodeMap[node.parentNodeId].children) {
                                nodeMap[node.parentNodeId].children = [];
                            }
                            nodeMap[node.parentNodeId].children.push(node);
                        } else {
                            nodeMap[node.parentNodeId] = node;
                        }

                        if (nodeMap[node.nodeId]) {
                            node.children.push(nodeMap[node.nodeId]);
                        }
                        nodeMap[node.nodeId] = node;

                        node.nodeSearchCode = _this.generateNodeSearchCode(node.parentNodeId, node.nodeId);
                    } else {
                        lastTypeId = node.nodeId;
                        node.nodeSearchCode = _this.generateNodeSearchCode(lastKindNodeId, lastTypeId);
                    }
                    break;
                case Constants.EXAM_QUES_LEVEL:
                    if (node.parentNodeId) {
                        var kindNodeId = parentIdMap[node.parentNodeId];
                        parentIdMap[node.nodeId] = node.parentNodeId;

                        if (nodeMap[node.parentNodeId]) {
                            if (!nodeMap[node.parentNodeId].children) {
                                nodeMap[node.parentNodeId].children = [];
                            }
                            nodeMap[node.parentNodeId].children.push(node);
                        } else {
                            nodeMap[node.parentNodeId] = node;
                        }

                        if (nodeMap[node.nodeId]) {
                            node.children.push(nodeMap[node.nodeId]);
                        }
                        nodeMap[node.nodeId] = node;
                        node.nodeSearchCode = _this.generateNodeSearchCode(kindNodeId, node.parentNodeId, node.nodeId);
                    } else {
                        lastQuesId = node.nodeId;
                        node.nodeSearchCode = _this.generateNodeSearchCode(lastKindNodeId, lastTypeId, lastQuesId);
                    }
                    if (CommUtils.isNotEmpty(node.question)) {
                        var question = node.question;
                        if (CommUtils.isNotEmpty(question.type.code) && question.type.code === "03") {
                            question.blanks = question.answer.lable.split("@#@");
                        }
                        if (CommUtils.isNotEmpty(question.difficulty) && question.difficulty.code === "") {
                            question.difficulty = _this.difficulties[0];
                        }
                        if (CommUtils.isNotEmpty(question.options) && CommUtils.isNotEmpty(node.question.answer) &&
                            CommUtils.isNotEmpty(question.answer.lable) && question.type.code === "02") {
                            $.each(question.options, function (index, option) {
                                if (question.answer.lable.indexOf(option.alias) >= 0) {
                                    option.active = true;
                                } else {
                                    option.active = false;
                                }
                            });
                        }
                        // 转换解析图片信息
                        if (CommUtils.isNotEmpty(node.question.answer) && CommUtils.isNotEmpty(node.question.answer.analysis)) {
                            node.question.answer.analysisFileList = _this.getAnalysisFileList(node.question.answer.analysis);
                        }
                    }
                    break;
                case Constants.EXAM_QUES_CHILD_LEVEL:
                    if (node.parentNodeId) {
                        var quesNodeId = parentIdMap[node.parentNodeId];
                        var typeNodeId = parentIdMap[quesNodeId];
                        var kindNodeId = parentIdMap[typeNodeId];

                        if (nodeMap[node.parentNodeId]) {
                            if (!nodeMap[node.parentNodeId].children) {
                                nodeMap[node.parentNodeId].children = [];
                            }
                            nodeMap[node.parentNodeId].children.push(node);
                        } else {
                            nodeMap[node.parentNodeId] = node;
                        }

                        if (nodeMap[node.nodeId]) {
                            node.children.push(nodeMap[node.nodeId]);
                        }
                        nodeMap[node.nodeId] = node;

                        node.nodeSearchCode = _this.generateNodeSearchCode(kindNodeId, typeNodeId, quesNodeId, node.nodeId);
                    } else {
                        node.nodeSearchCode = _this.generateNodeSearchCode(lastKindNodeId, lastTypeId, lastQuesId, node.nodeId);
                    }
                    break;
            }
        });
        if (kindList.length <= 0 || kindList.length > 0 && !kindList[0].children) {
            kindList = [];
            // 将子题目节点放入题目节点中
            for (var index = paper.nodes.length - 1; index >= 0; index--) {
                var node = paper.nodes[index];
                switch (node.level) {
                    case Constants.EXAM_KIND_LEVEL:
                        typeList.reverse();
                        node.children = CommUtils.clone(typeList);
                        typeList = [];
                        kindList.push(node);
                        break;
                    case Constants.EXAM_QUES_TYPE_LEVEL:
                        quesList.reverse();
                        node.children = CommUtils.clone(quesList);
                        if (!CommUtils.isEmpty(node.children) && node.children[0].question != null && node.children[0].question.type != null) {
                            node.typeCode = node.children[0].question.type.code;
                            node.typeName = node.children[0].question.type.name;
                        }
                        quesList = [];
                        typeList.push(node);
                        break;
                    case Constants.EXAM_QUES_LEVEL:
                        quesChildren.reverse();
                        node.children = CommUtils.clone(quesChildren);
                        quesChildren = [];
                        quesList.push(node);
                        break;
                    case Constants.EXAM_QUES_CHILD_LEVEL:
                        quesChildren.push(node);
                        break;
                }
            }
            kindList.reverse();
        }
        paper.nodes = kindList;
        _this.paper = paper;
    },
    initVue: function() {
        var _this = this;
        VueConfig.data.gradeList = this.stageGradeSubjects.gradeList;
        VueConfig.data.subjectList = this.stageGradeSubjectsMap[this.paper.gradeCode];
        var materialVersionKey = "";
        if (CommUtils.isNotEmpty(this.paper.gradeCode) && CommUtils.isNotEmpty(this.paper.subjectCode)) {
            materialVersionKey = this.paper.gradeCode + "_"+ this.paper.subjectCode;
        }
        var materialVersionList = this.meterialVersionMap[materialVersionKey];
        if (CommUtils.isEmpty(materialVersionList)) {
            materialVersionList = this.meterialVersionMap[""];
        } else {
            var isExist = false;
            $.each(materialVersionList, function (index, materialVersion) {
                if (materialVersion.code == _this.paper.materialVersion) {
                    isExist = true;
                    return false;
                }
            });
            if (!isExist) {
                _this.paper.materialVersion = "";
            }
        }
        VueConfig.data.materialVersionList = materialVersionList;
        /*var questionTypeList = _this.subjectQuestionMap[_this.paper.subjectCode];
        if (CommUtils.isEmpty(questionTypeList)) {
            questionTypeList = _this.subjectQuestionMap[''];
        }
        VueConfig.data.questionTypeList = questionTypeList;*/
        VueConfig.data.questionTypeList = EditPaperWorkPage.questionTypes;
        VueConfig.data.difficultyList = _this.difficulties;
        VueConfig.data.i18n = i18n;
        VueConfig.data.paper = CommUtils.clone(this.paper);
        this.vm = new Vue(VueConfig);
        this.vm.$mount("#paperWork");
    },
    initMessage: function() {
        var _this = this;
        CommUtils.onMessage(function(event) {
            var data = event.data;
            if (CommUtils.isEmpty(data)) {
                return;
            }
            if ('addQuestionBatch' === data.type) { // 批量添加题目
                var tNode = _this.vm.addQuestionBatchSelectNode;
                if (CommUtils.isEmpty(tNode)) {
                    layer.alert(i18n['get_question_batch_node_error'], {
                        btn: [_this.i18n['confirm']]
                    });
                    return;
                }
                var dataQuestion = data.data;
                if (dataQuestion.isCover === "Y") {
                    tNode.children = [];
                }
                var internalNo = tNode.children.length;
                $.each(dataQuestion.questions, function (index, questionType) {
                    for (var i = 1; i <= questionType.num; i++) {
                        var score = parseFloat(questionType.score);
                        var internalNoTemp = ++internalNo;
                        var qNode = _this.renderQNode(score, internalNoTemp, internalNoTemp, internalNoTemp, questionType.code, tNode);
                        tNode.points += score;
                        tNode.children.push(qNode);
                        _this.vm.paper.isSaved = false;
                    }
                });
            }
        });
    },
    renderKNode: function(nodes) {
        var _this = this;
        var internalNo = _this.nodeSeq++;
        var kindNode = _this.newNode(Constants.EXAM_KIND_LEVEL, CommUtils.formatStr(i18n['kind_0'], nodes.length + 1), 0, internalNo, "" + internalNo, 1);
        var typeNode = _this.renderTNode(kindNode);
        kindNode.children.push(typeNode);
        return kindNode;
    },
    renderTNode: function(kindNode) {
        var _this = this;
        // 获取默认题型
        /*var typeInfoList = _this.subjectQuestionMap[_this.paper.subjectCode];
        if (CommUtils.isEmpty(typeInfoList)) {
            typeInfoList = _this.subjectQuestionMap[''];
        }
        var typeInfo = typeInfoList[0];*/
        var typeInfo = EditPaperWorkPage.questionTypes[0];
        var typeInternalNo = kindNode.children.length + 1,sequencing = 0; //数据中有空的题型

        var typeNode = _this.newNode(Constants.EXAM_QUES_TYPE_LEVEL, CommUtils.numberToChinese(typeInternalNo) + "、" + CommUtils.ifEmpty(typeInfo.name), 0, typeInternalNo, typeInternalNo + "", ++sequencing);
        typeNode.parentId = kindNode.nodeId;
        typeNode.nodeSearchCode = _this.generateNodeSearchCode(kindNode.nodeId, typeNode.nodeId);
        /*var questionTypeList = _this.subjectQuestionMap[_this.paper.subjectCode];
        if (CommUtils.isEmpty(questionTypeList)) {
            questionTypeList = _this.subjectQuestionMap[''];
        }*/
        typeNode.children.push(_this.renderQNode(1, 1, 1, 1, typeInfo.code, typeNode));
        return typeNode;
    },
    renderQNode: function(points, internalNo, externalNo, sequencing, questionTypeCode, tNode) {
        var _this = this;
        var qNode = _this.newNode(Constants.EXAM_QUES_LEVEL, '', points, internalNo, externalNo, sequencing);
        var questionType = _this.questionMap[questionTypeCode];
        var stem = {"plaintext": i18n['paper_work_question_stem'],"richText": i18n['paper_work_question_stem']};
        var difficulty = CommUtils.clone(_this.difficulties[0]);
        var knowledgeList = [];
        if (CommUtils.isNotEmpty(tNode.children) && tNode.children.length > 0 ) {
            var childrenNode = tNode.children[tNode.children.length - 1];
            if (CommUtils.isNotEmpty(childrenNode.question) && CommUtils.isNotEmpty(childrenNode.question.knowledgeList)) {
                knowledgeList = CommUtils.clone(childrenNode.question.knowledgeList);
            }
        }
        qNode.question = _this.newQuestion(questionType, knowledgeList, points, stem, "", difficulty);
        qNode.nodeSearchCode = _this.generateNodeSearchCode(tNode.parentId, tNode.nodeId, qNode.nodeId);
        return qNode;
    },
    newQuestion : function (type, knowledgeList, points, stem, answer, difficulty) {
        var _this = this;
        var paper = _this.paper;
        // 多选单选 生成option
        var options = [];
        // 填空题
        var blanks = [];
        var lable = "";
        var answerTemp = {
            "lable": lable,
            "strategy": "<span class=\"answerHeader\" style=\"color:red;font-weight:bold;\">" + i18n['question_answer'] +"</span>" + lable,
            "analysis": null,
            "analysisFileList": [],
            "isTemp": true
        };

        if (CommUtils.isEmpty(answer)) {
            answer = answerTemp;
        } else if (CommUtils.isNotEmpty(answer.analysis) && answer.analysis.indexOf("http") >= 0) {
            answer.analysisFileList = _this.getAnalysisFileList(answer.analysis);
        }

        if (type.code === "01" || type.code === "02") {
            options = [
                {'alias': 'A', 'lable': '', 'sequencing': '0', 'active': true},
                {'alias': 'B', 'lable': '', 'sequencing': '1', 'active': false},
                {'alias': 'C', 'lable': '', 'sequencing': '2', 'active': false},
                {'alias': 'D', 'lable': '', 'sequencing': '3', 'active': false}
            ];
            if (answer.isTemp) {
                answer.lable = 'A';
                answer.strategy += 'A';
            }
        } else if (type.code === "03") {
            if (!answer.isTemp) {
                blanks = answer.split("@#@");
            } else {
                blanks = [
                    '', '', ''
                ];
                answer.lable = blanks.join("@#@");
                answer.strategy= "<p><span style=\"font-weight: bold;\">" + answer.lable + "</span></p>";
            }
        } else if (type.code === "04") {
            answer.lable = "T";
            answer.strategy = "T";
        }
        return {
            "type":{"code": type.code,"name": type.name},
            "stage":{"code": paper.stageCode,"name": paper.stageName},
            "grade":{"code": paper.gradeCode,"name": paper.gradeName},
            "subject":{"code": paper.stageCode,"name": paper.stageName},
            "knowledgeList": CommUtils.isEmpty(knowledgeList) ? [] : knowledgeList,
            "points": points,
            "stem":{"plaintext": stem.plaintext,"richText": stem.richText},
            "options":options,
            "blanks": blanks,
            "difficulty": difficulty,
            "answer": answer
        }
    },
    newNode : function(level, name, points, internalNo, externalNo, sequencing) {
        return {
            'level' : level,
            'internalNo' : internalNo,
            'externalNo' : externalNo,
            'name' : name,
            'points' : points || 0,
            'sequencing' : sequencing || 0,
            'nodeId' : this.nodeSeq++,
            'parentId' : '',
            'description' : '',
            'children' : []
        };
    },
    getAnalysisFileList : function(analysisString) {
        // 转换answer信息, 取出所有标签值
        // analysisString = "<p><img src=\"http://192.168.116.190/storage/attachment/2020/1/2/f583bb1a6e22429fa558aa00b4e9abf2.png\" title=\"1.png\" alt=\"1.png\"><img src=\"http://192.168.116.190/storage/attachment/2020/1/2/a3ae44211b2946e08e023a2f2ed34960.jpg\" title=\"1-543872364186734592-B.jpg\" alt=\"1-543872364186734592-B.jpg\"></p>";
        // 取出连接和文件名
        var reg= /=\"(.*?)\"/g;
        var i = 1;
        var analysisFileList = [];
        while (r=reg.exec(analysisString)){
            // 每个图片有一个地址, 一个title, 一个alt, 一个size, 四个切换一组
            let index = Math.floor((i-1)/4);
            let analysisFileTemp = analysisFileList[index];
            if (CommUtils.isEmpty(analysisFileTemp)) {
                analysisFileTemp = {
                    url: r[1],
                    fileName: "",
                    size: Math.floor(Math.random(Math.random()) * 10000)
                }
                analysisFileList[index] = analysisFileTemp;
            } else if (i%4 == 2) {
                analysisFileTemp.fileName = r[1];
            } else if (i%4 == 0 && i !== 0) {
                analysisFileTemp.size =  r[1];
            }
            i++;
        }
        return analysisFileList;
    },
    generateNodeSearchCode : function() {
        return Array.prototype.slice.call(arguments).map(function(item){return ""+item;}).join("_");
    },
    initSubjectQuestionMap: function() {
        var _this = this;
        var relationCode = '';
        $.each(_this.questionTypes, function (index, questionType) {
            if (index === 0) {
                relationCode = questionType.relationCode;
            }
            if (CommUtils.isEmpty(_this.subjectQuestionMap[questionType.relationCode])) {
                _this.subjectQuestionMap[questionType.relationCode] = [];
            }
            _this.subjectQuestionMap[questionType.relationCode].push(questionType);

            _this.questionMap[questionType.code] = questionType;

        });
        // 设置默认题型
        _this.subjectQuestionMap[''] = _this.subjectQuestionMap[relationCode];
    },
    initStageGradeSubject: function() {
        var _this = this;

        $.each(_this.stageGradeSubjects.stageList, function (index, stage) {
            _this.stageMap[stage.stageCode] = stage;
        });

        _this.stageGradeSubjects.gradeList.unshift({
            'gradeCode': '',
            'gradeName': i18n['please_select_grade'],
            'stageCode': '',
            'stageName': ''
        });
        $.each(_this.stageGradeSubjects.gradeList, function (index, grade) {
            var stage = _this.stageMap[grade.stageCode];
            if (!CommUtils.isEmpty(stage)) {
                grade = $.extend(grade, {
                    'stageName': stage.stageName
                });
            }
            _this.gradeMap[grade.gradeCode] = grade;
        });
        _this.subjectMap[''] = {
            'gradeName': i18n['please_select_grade'],
            'gradeCode': '',
            'subjectName': i18n['please_select_subject'],
            'subjectCode': '',
        };

        _this.stageGradeSubjectsMap[''] = [{
            'stageCode': '',
            'stageName': '',
            'gradeName': i18n['please_select_grade'],
            'gradeCode': '',
            'subjectName': i18n['please_select_subject'],
            'subjectCode': '',
        }];

        $.each(_this.stageGradeSubjects.subjectList, function (sIndex, subject) {
            var subjectList = _this.stageGradeSubjectsMap[subject.gradeCode];
            if (CommUtils.isEmpty(subjectList)) {
                subjectList = _this.stageGradeSubjectsMap[subject.gradeCode] = CommUtils.clone(_this.stageGradeSubjectsMap['']);
            }

            var grade = _this.gradeMap[subject.gradeCode];
            subject = $.extend(subject, {
                'gradeName' : grade.gradeName
            });
            _this.subjectMap[subject.subjectCode] = subject;

            if (!CommUtils.isEmpty(grade)) {
                subject = $.extend(CommUtils.clone(subject), {
                    'stageCode': grade.stageCode,
                    'stageName': grade.stageName
                });
            }
            subjectList.push(subject);
        });
    },
    initMaterialVersionMap: function () {
        var _this = this;
        _this.meterialVersionMap[''] = [{
            'relationCode': '',
            'code': '',
            'name': i18n['please_select_materialVersion']
        }];
        $.each(_this.meterialVersions, function (index, materialVersion) {
            var materialVersionList = _this.meterialVersionMap[materialVersion.relationCode];
            if (CommUtils.isEmpty(materialVersionList)) {
                materialVersionList = _this.meterialVersionMap[materialVersion.relationCode] = CommUtils.clone(_this.meterialVersionMap['']);
            }
            materialVersionList.push(materialVersion);
        });
    },
    getTypeArr : function() {
        return CommUtils.clone(EditPaperWorkPage.vm.questionTypeList);
    },
    getPaperInfo : function() {
        return JSON.parse(JSON.stringify(EditPaperWorkPage.vm.paper));
    },
    getPaperQuestions : function() {//获取所有question
        var questionMarket = EditPaperWorkPage.getTypeArr();

        var typeIndexMap = {};
        questionMarket.forEach(function(type, t){
            typeIndexMap[type.code] = t;
            type.questions = [];
            type.typeName = type.name;
            type.typeCode = type.code;
        });
        EditPaperWorkPage.getQuestions(questionMarket, typeIndexMap);
        return questionMarket;
    },
    getQuestions : function (questionMarket, typeIndexMap) {
        var paper = EditPaperWorkPage.vm.paper;
        var questions = [];
        if (!CommUtils.isEmpty(paper.nodes)) {
            $.each(paper.nodes, function (k, kNode) {
                $.each(kNode.children, function(t, tNode) {
                    if (CommUtils.isEmpty(tNode.children)) {
                        return true;
                    }
                    $.each(tNode.children, function(q, qNode) {
                        if (qNode.question != null) {
                            questions.push(qNode.question);
                            var quesChildren = [];
                            if (!CommUtils.isEmpty(qNode.children)) {
                                $.each(qNode.children, function(c, cNode){
                                    var childQues = JSON.parse(JSON.stringify(cNode.question));
                                    childQues.points = cNode.points;
                                    quesChildren.push(childQues);
                                });
                            }
                            if (qNode.question && qNode.question.type && qNode.question.type.code) {
                                var question = JSON.parse(JSON.stringify(qNode.question));
                                question.points = qNode.points;
                                question.children = quesChildren;
                                if (typeIndexMap) {
                                    questionMarket[typeIndexMap[qNode.question.type.code]].questions.push(question);
                                }
                            }
                        }
                    });
                });
            });
        }
        return questions;
    }
};