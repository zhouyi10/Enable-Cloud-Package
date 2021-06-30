/**
 * Examination Information Components
 * Prevent {{}} splash screen of vue, put all operations into the component
 */
"use strict";
Vue.config.devtools = true;
var VueConfig = {
    data: {
        "paper": {},
        'paperTemplate': {}
    },
    computed: {
        canSave: function (scroll) {
            var _this = this;
            return function (scroll) {
                if (CommUtils.isEmpty(_this.paper.stageCode) || CommUtils.isEmpty(_this.paper.gradeCode) || CommUtils.isEmpty(_this.paper.subjectCode)) {
                    scroll && _this.validTip(null, i18n['paper_base_info_empty']); // The basic information of the test paper cannot be empty!
                    return false;
                }
                var flag = !CommUtils.isEmpty(_this.paper.nodes);
                $.each(_this.paper.nodes, function (k, kNodeTemp) {
                    var kNode = kNodeTemp;
                    if (CommUtils.isEmpty(kNode.children)) {
                        flag = false;
                        scroll && _this.validTip(kNode.nodeSearchCode, CommUtils.formatStr(i18n['kind_empty'], kNode.name)); //Volume [{0}] cannot be empty
                        return false;
                    }
                    $.each(kNode.children, function (t, tNodeTemp) {
                        var tNode = tNodeTemp;
                        if (CommUtils.isEmpty(tNode.children)) {
                            flag = false;
                            scroll && _this.validTip(tNode.nodeSearchCode, CommUtils.formatStr(i18n['paper_ques_type_empty'], kNode.name, tNode.name)); //Question type [{0}] cannot be empty
                            return false;
                        }
                        $.each(tNode.children, function (q, qNodeTemp) {
                            var qNode = qNodeTemp;
                            if (CommUtils.isEmpty(qNode.question) || !CommUtils.isEmpty(qNode.question) && qNode.question.isTempQuestion) {
                                flag = false;
                                scroll && _this.validTip(qNode.nodeSearchCode, CommUtils.formatStr(i18n['ques_empty'], kNode.name, tNode.name, qNode.externalNo)); // Title cannot be empty
                                return false;
                            }

                            if (CommUtils.isEmpty(qNode.children)) {
                                if (CommUtils.isEmpty(qNode.points) || qNode.points == 0) {
                                    flag = false;
                                    scroll && _this.validTip(qNode.nodeSearchCode, CommUtils.formatStr(i18n['ques_points_empty'], kNode.name, tNode.name, qNode.externalNo)); //Title {0} score cannot be 0
                                    return false;
                                }
                            } else {
                                $.each(qNode.children, function (c, cNode) {
                                    if (CommUtils.isEmpty(cNode.points) || cNode.points == 0) {
                                        flag = false;
                                        scroll && _this.validTip(cNode.nodeSearchCode, CommUtils.formatStr(i18n['ques_child_points_empty'], kNode.name, tNode.name, qNode.externalNo, cNode.externalNo)); //Title {0} score cannot be 0
                                        return false;
                                    }
                                });
                            }
                            if (!flag) return false;
                        });
                        if (!flag) return false;
                    });
                    if (!flag) return false;
                });
                return flag;
            };
        },
        filterNode: function (nodeSearchCode) {
            var _this = this;
            return function (nodeSearchCode) {
                nodeSearchCode += '_';
                _this.node.filter(function (item) {
                    if (item.nodeSearchCode.startsWith(nodeSearchCode)) {
                        return item;
                    }
                })
            };
        },
        questionCount: function () { //Number of questions
            var quesCount = 0;
            $.each(this.paper.nodes, function (k, kNode) {
                if (CommUtils.isEmpty(kNode.children)) {
                    return true;
                }
                $.each(kNode.children, function (t, tNode) {
                    if (CommUtils.isEmpty(tNode.children)) {
                        return true;
                    }
                    quesCount += tNode.children.length;
                });
            });
            return quesCount;
        }
    },
    methods: {
        editBaseInfo: function (event) {
            var url = CommUtils.formatStr('{0}?stageCode={1}&gradeCode={2}&subjectCode={3}&name={4}&userId={5}',
                EditFilePage.editBaseInfoUrl,
                this.ifEmpty(this.paper.stageCode), this.ifEmpty(this.paper.gradeCode),
                this.ifEmpty(this.paper.subjectCode), this.ifEmpty(this.paper.name),
                this.ifEmpty(this.paper.userId));
            this.frame(i18n['paper_info'], url, '400px', '550px');
        },
        addTNode: function (event) {
            var url = CommUtils.formatStr('{0}?stageCode={1}&gradeCode={2}&subjectCode={3}',
                EditFilePage.editPaperTemplateUrl,
                this.ifEmpty(this.paper.stageCode), this.ifEmpty(this.paper.gradeCode), this.ifEmpty(this.paper.subjectCode)
            );
            this.frame(i18n['edit_paper_template'], url, '92%', '850px');
        },
        ifEmpty: function (arg, defaultStr) {
            if (defaultStr == undefined) {
                defaultStr = '';
            }
            if (arg == undefined || arg == null) {
                return defaultStr;
            }
            return arg;
        },
        frame: function (title, url, height, width) {
            layer.open({
                type: 2,
                title: title,
                shadeClose: false,
                scrollbar: false,
                shade: 0.6,
                area: [width, height],
                content: url //iframe的url
            });
        },
    }
};

var EditFilePage = window.EditFilePage = {
    isSaved: false,
    nodeSeq: 1, // node sequence
    vm: null, //vue obj
    questionTypes: [],
    paper: null, //paper info
    teacherBaseInfoStr: '', //The str about teacher info
    files: [],// file info
    editBaseInfoUrl: '',
    answerCardInfo: undefined,
    iframeAnswerCard: undefined,
    answerCard: {},
    init: function () {
        this.initPaper();
        this.initVue();
        this.initAnswerCard();
        this.initIframe();
        this.initMessage();
        this.initEvent();
        window.getPaperTemplate = this.getPaperTemplate;
        window.getRecommendationQuestion = this.getRecommendationQuestion;
        window.getPaperQuestions = this.getPaperQuestions;
    },
    initVue: function () {
        VueConfig.data.paper = CommUtils.clone(this.paper);
        //VueConfig.data.editType = this.editType;
        this.vm = new Vue(VueConfig);
        this.vm.$mount("#file");
    },
    initEvent: function () {
        var _this = this;
        $('#upload').on('click', function () {
            UploadFileUtils.uploadFile({
                /* beforeAddFile: function(file) {
                     var isImage = file.type.indexOf('image') == 0;
                     isImage && CommUtils.showMask();
                     return isImage;
                 },*/
                uploadProgress: function (file, percent) {

                },
                uploadSuccess: function (file, result) {
                    var paper = _this.paper;
                    result.url = result.downloadUrls[0];
                    result.fileName = result.name;
                    result.fileExt = file.ext;
                    _this.files[0] = result;
                    paper.files = _this.files;
                    Vue.set(_this.vm, "paper", paper);
                    _this.initExamArea(result)
                },
                uploadError: function (file, errorCode) {

                },
                uploadComplete: function (file) {
                    CommUtils.closeMask();
                }
            });
        });
        $("#remove").on('click', function () {
            var paper = _this.paper;
            _this.files = []
            paper.files = _this.files;
            Vue.set(_this.vm, "paper", paper);
            $("#fileview").remove();
            $("#upload").show();
            $("#remove").hide();
        });
        $("#addKNode").on("click", function () {
            var paper = _this.paper;
            var internalNo = paper.nodes.length + 1;
            if (internalNo > 3) {
                layer.alert(i18n['kind_max_four'], {btn: i18n['confirm']});
                return;
            }
            var kindNode = _this.newNode(Constants.EXAM_KIND_LEVEL, CommUtils.formatStr(i18n['kind_0'], internalNo), 0, internalNo, "" + internalNo, 1);
            paper.nodes.push(kindNode);
            _this.initAnswerCardInfo(paper);
        });
        $("#saveFile").on("click", function () {
            if (_this.isSaved){
                return;
            }
            var paper = CommUtils.clone(_this.paper);
            if (CommUtils.isEmpty(paper.files) || (paper.files.length < 1)){
                layer.alert(i18n['file_no_empty'], {btn: i18n['confirm']});
                return;
            }
            var loading = layer.load(1,{
                shadeClose: false,
                shade: [0.5,'#000']
            });
            _this.initQuestion(paper);
            $.ajax({
                url: _this.saveUrl,
                type: "POST",
                data: JSON.stringify(paper),
                dataType: "json",
                contentType: 'application/json',
                async: true,
                success: function (result) {
                    var data = result.data;
                    if (data.paperId) {
                        _this.paper = data;
                        _this.initPaper();
                        _this.isSaved = true;
                        _this.buildSaved();
                    }
                    layer.close(loading);
                    layer.msg(i18n['success'], {time: 1000});
                },
                error: function (){
                    layer.close(loading);
                    layer.msg(i18n['fail'], {time: 1000});
                }
            });
        })
        window.addEventListener("resize", function () {
            var height = $("#exam").height();
            $("#upload").css("margin-top", 0.45 * height);
        })
    },
    initMessage: function () {
        var _this = this;
        CommUtils.onMessage(function (event) {
                var data = event.data;
                if (CommUtils.isEmpty(data)) {
                    return;
                }
                if ('editBaseInfo' === data.type) {
                    data = data.data;
                    $.each(data, function (key, value) {
                        Vue.set(_this.vm.paper, key, value);
                        Vue.set(_this.vm.paperTemplate, key, value);
                    });
                    _this.paper = CommUtils.clone(_this.vm.paper)
                    _this.changeAnswerCard("modifyData");
                } else if ("changeAnswerCard" === data.type) {
                    debugger
                    if (_this.isSaved){
                        _this.isSaved = false;
                        _this.buildSaved();
                    }
                }
            }
        )

    },
    initExamArea: function (file) {
        var _this = this;
        //var src = CommUtils.formatStr(url, file.url);
        var testUrl = "http://cc.enable-ets.com/storage/attachment/2019/8/26/074cf02442544db48154017c5a12bfce.doc";
        var src = CommUtils.formatStr(_this.fileViewSrc, testUrl);
        $("#upload").hide();
        $("#remove").show();
        $("#exam").append(
            '<iframe  src="' + src + '" width="140%" height="100%" frameborder="no" border="0" marginwidth="0"  marginheight="0" style="overflow: scroll" id="fileview" ></iframe>'
        )
    },
    initPaper: function () { //init paper info
        var _this = this;
        if (_this.paper == null) {
            return;
        }
        var paper = _this.paper;
        //1：(word, pdf, mp3)等附件试卷
        paper.paperType = 1;
        if (CommUtils.isEmpty(EditFilePage.type)) {
            paper.isSaved = true;
        }
        if (_this.isCertify) {
            $(document).attr("title", "探寶森林");
        }
        if (CommUtils.isEmpty(paper.paperId) && !CommUtils.isEmpty(_this.teacherBaseInfoStr)) {
            var baseInfo = JSON.parse(_this.teacherBaseInfoStr);
            $.each(baseInfo, function (key, value) {
                if (CommUtils.isEmpty(paper[key])) {
                    paper[key] = value;
                }
            });

            if (CommUtils.isEmpty(paper.name)) {
                var date = CommUtils.formatDate(new Date(), 'yyyyMMdd');
                paper.name = CommUtils.formatStr("{0}{1}{2}{3}" + i18n['paper'], date, paper.stageName, paper.gradeName, paper.subjectName);
            }
            paper.isSaved = false;
        }
        // Default value when adding test paper
        if (CommUtils.isEmpty(paper.nodes)) {
            paper.nodes = [];
            var internalNo = _this.nodeSeq++;
            var kindNode = _this.newNode(Constants.EXAM_KIND_LEVEL, CommUtils.formatStr(i18n['kind_0'], internalNo), 0, internalNo, "" + internalNo, 1);
            paper.nodes.push(kindNode);
            paper.totalPoints = 0;
            paper.isSaved = false;
        }
        // node searchCode build
        var lastKindNodeId = '', lastTypeId = '', lastQuesId = '';
        var parentIdMap = {};
        var nodeMap = {};
        var question = {};
        var quesChildren = [], quesList = [], typeList = [], kindList = [];

        $.each(paper.nodes, function (n, node) {
            switch (node.level) {
                case Constants.EXAM_KIND_LEVEL:
                    lastKindNodeId = node.nodeId;
                    node.nodeSearchCode = _this.generateNodeSearchCode(lastKindNodeId);
                    kindList.push(node);
                    // nodeMap When not empty, take the data to the lower level
                    if (nodeMap[node.nodeId]) {
                        node.children.push(nodeMap[node.nodeId]);
                    }
                    nodeMap[node.nodeId] = node;
                    break;
                case Constants.EXAM_QUES_TYPE_LEVEL:
                    // parentNodeId Use the previous one when it is not empty
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
                    /*if (!CommUtils.isEmpty(node.question)) {
                        _this.originalQuestions.push(node.question.questionId);
                    }*/
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
            // Put sub-topic nodes into topic nodes
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
    initIframe: function () {
        let _this = this;
        
        var height = $("#exam").height();
        $("#upload").css("margin-top", 0.45 * height);

        $("#answerCardTemplate").attr("src", _this.answerCardTemplateUrl);
        $("#answerCardTemplate").on("load", function () {
            _this.iframeAnswerCard = document.getElementById("answerCardTemplate").contentWindow.answerCard;
            _this.changeAnswerCard("modifyData");
            // _this.changeIframeHeight();
        });
    },
    initAnswerCard: function () {
        var _this = this;
        var answerCard = _this.answerCard;
        answerCard.type = "modefyData";// modifyData 修改题目数据 modifyConfig 修改配置
        answerCard.data = [];
        answerCard.config = {
            cardType: "1",
            columnType: 1,
            pageType: 'A4',
            candidateNumberEdition: '1',
            questionContentStatus: "0",
            judgeStyle: "0",
            sealingLineStatus: "0",
            editAnswer: true, // word == true
            editType: "word", // word
            userId: _this.paper.userId,
        }
    },
    generateNodeSearchCode: function () {
        return Array.prototype.slice.call(arguments).map(function (item) {
            return "" + item;
        }).join("_");
    },
    newNode: function (level, name, points, internalNo, externalNo, sequencing) {
        // Question score is 1 point by default
        if (level === Constants.EXAM_QUES_LEVEL || level === Constants.EXAM_QUES_CHILD_LEVEL) {
            if (CommUtils.isEmpty(points) || points === 0) {
                points = 1;
            }
        }
        return {
            'level': level,
            'internalNo': internalNo,
            'externalNo': externalNo,
            'name': name,
            'points': points || 0,
            'sequencing': sequencing || 0,
            'nodeId': this.nodeSeq++,
            'parentId': '',
            'description': '',
            'children': []
        };
    },
    getPaperTemplate: function () {
        // TemplateBuildType 1: Generated by template 2: Generated by test paper
        // EditFilePage.vm.paperTemplate Use test paper to generate template when empty
        if (!EditFilePage.vm.paperTemplate.TemplateBuildType) {
            EditFilePage.vm.paperTemplate = CommUtils.clone(EditFilePage.vm.paper);
            EditFilePage.vm.paperTemplate.TemplateBuildType = 2;
            return EditFilePage.vm.paperTemplate;
        } else {
            if (EditFilePage.vm.paperTemplate.TemplateBuildType === 2) {
                return EditFilePage.vm.paperTemplate;
            } else if (EditFilePage.vm.paperTemplate.TemplateBuildType === 1) {
                if (!EditFilePage.vm.paperTemplate.isInit) {
                    EditFilePage.paper = EditFilePage.vm.paperTemplate;
                    EditFilePage.initPaper();
                    EditFilePage.vm.paperTemplate.isInit = true;
                    EditFilePage.vm.paperTemplate = EditFilePage.paper;
                }
                return EditFilePage.vm.paperTemplate;
            }
        }
    },
    getRecommendationQuestion: function (param, EditPaperTemplatePage) {
        layer.closeAll();
        var _this = this.EditFilePage;
        _this.paper = CommUtils.clone(EditPaperTemplatePage.vm.paper);
        _this.initAnswerCardInfo(_this.paper);
        /*Vue.set(_this.vm, 'paper', templatePaper);
        Vue.set(_this.vm, 'paperTemplate', templatePaper);*/
    },
    getPaperQuestions: function () {
        var paper = EditFilePage.vm.paperTemplate;
        var questionMarket = EditFilePage.getTypeArr();
        if (CommUtils.isEmpty(paper.nodes)) {
            return questionMarket;
        }
        var typeIndexMap = {};
        questionMarket.forEach(function (type, t) {
            typeIndexMap[type.typeCode] = t;
        });
        if (!CommUtils.isEmpty(paper.nodes)) {
            $.each(paper.nodes, function (k, kNode) {
                $.each(kNode.children, function (t, tNode) {
                    if (CommUtils.isEmpty(tNode.children)) {
                        return true;
                    }
                    $.each(tNode.children, function (q, qNode) {
                        if (qNode.question != null && (!CommUtils.isEmpty(qNode.question.questionId) || !CommUtils.isEmpty(qNode.question.questionNo))) {
                            var quesChildren = [];
                            if (!CommUtils.isEmpty(qNode.children)) {
                                $.each(qNode.children, function (c, cNode) {
                                    if (CommUtils.isEmpty(cNode.question)) return;
                                    var childQues = JSON.parse(JSON.stringify(cNode.question));
                                    childQues.points = cNode.points;
                                    quesChildren.push(childQues);
                                });
                            }
                            if (qNode.question && qNode.question.type && qNode.question.type.code) {
                                if (CommUtils.isEmpty(qNode.question)) return;
                                var question = JSON.parse(JSON.stringify(qNode.question));
                                question.points = qNode.points;
                                question.children = quesChildren;
                                questionMarket[typeIndexMap[qNode.question.type.code]].questions.push(question);
                            }
                        }
                    });
                });
            });
        }
        return questionMarket;
    },
    getTypeArr: function () {
        return CommUtils.clone(this.questionTypes);
    },
    removeKNode: function (event) {
        var _this = this;
        var number = parseInt($(event.target).attr("value"));
        var paper = _this.paper;
        if (number == (paper.nodes.length - 1)) {
            paper.nodes.splice(number, 1);
        } else {
            paper.nodes.splice(number, 1);
            $.each(paper.nodes, function (index, kNode) {
                if (kNode.internalNo > (number + 1)) {
                    kNode.internalNo = kNode.internalNo - 1
                    kNode.name = CommUtils.formatStr(i18n['kind_0'], kNode.internalNo)
                }
            })
        }
        _this.initAnswerCardInfo(paper);
    },
    removeTNode: function (event) {
        var _this = this;
        var paper = _this.paper;
        var tNode = $(event.target).attr("value");
        if (CommUtils.isEmpty(tNode)) {
            tNode = $(event.target.parentElement).attr("value");
        }
        tNode = JSON.parse(tNode);
        var kNodeSearchCode = tNode.nodeSearchCode.split("_")[0];
        var kNode = {};
        var i = 0;
        $.each(paper.nodes, function (index, kNode_) {
            if (kNodeSearchCode == kNode_.nodeSearchCode) {
                kNode = kNode_;
                i = index;
                return false;
            }
        })
        $.each(kNode.children, function (index, tNode_) {
            if ((tNode.nodeId) === (tNode_.nodeId)) {
                kNode.children.splice(index, 1);
                // 刷新分数
                kNode.points = kNode.points - tNode_.points;
                return false;
            }
        })
        //刷新题型名字
        $.each(kNode.children, function (index, tNode_) {
            tNode_.name = CommUtils.numberToChinese(index + 1) + "、" + tNode_.type.name;
        })
        _this.initQuestionNode(kNode);
        paper.nodes[i] = kNode;
        _this.initAnswerCardInfo(paper);
    },
    addQuestion: function (event) {
        var _this = this;
        var tNode = JSON.parse($(event.target).attr("value"));
        if (CommUtils.isEmpty(tNode)) {
            return;
        }

        var quesNode = _this.newNode(Constants.EXAM_QUES_LEVEL, '', 1, tNode.children.length + 1, tNode.children.length + 1 + "");
        quesNode.question = {};
        quesNode.parentId = tNode.nodeId;
        quesNode.nodeSearchCode = tNode.nodeSearchCode + '_' + quesNode.nodeId;
        tNode.children.push(quesNode);
        tNode.points = tNode.points + quesNode.points;

        var kNodeId = tNode.parentId;
        var paper = _this.paper;
        var nodes = paper.nodes;
        $.each(nodes, function (index, kNode) {
            if (kNode.nodeId === kNodeId) {
                $.each(kNode.children, function (index, tNode_) {
                    if (tNode_.nodeSearchCode === tNode.nodeSearchCode) {
                        kNode.children[index] = tNode;
                        kNode.points = kNode.points + quesNode.points;
                        return false;
                    }
                })
                _this.initQuestionNode(kNode);
                return false;
            }
        });
        paper.nodes = nodes;
        _this.initAnswerCardInfo(paper);
    },
    removeQuestion: function () {
        var _this = this;
        var tNode = JSON.parse($(event.target).attr("value"));
        if (CommUtils.isEmpty(tNode) || CommUtils.isEmpty(tNode.children) || tNode.children.length <= 0) {
            return;
        }
        var qNodes = tNode.children;
        var points = qNodes[qNodes.length - 1].points;
        qNodes.splice((qNodes.length - 1), 1);
        var kNodeId = tNode.parentId;
        var paper = _this.paper;
        var nodes = paper.nodes;
        $.each(nodes, function (index, kNode) {
            if (kNode.nodeId === kNodeId) {
                $.each(kNode.children, function (index, tNode_) {
                    if (tNode_.nodeSearchCode === tNode.nodeSearchCode) {
                        tNode.points = tNode.points - points;
                        tNode.children = qNodes;
                        kNode.children[index] = tNode;
                        kNode.points = kNode.points - points;
                        return false;
                    }
                })
                _this.initQuestionNode(kNode);
                return false;
            }
        });
        paper.nodes = nodes;
        _this.initAnswerCardInfo(paper);
    },
    initQuestionNode: function (kindNode) {
        var nodes = [];
        if (CommUtils.isNotEmpty(kindNode)) {
            $.each(kindNode.children, function (index, node) {
                if (CommUtils.isNotEmpty(node.children)) {
                    $.each(node.children, function (index1, node1) {
                        nodes.push(node1);
                    })
                }
            })
        }
        if (CommUtils.isNotEmpty(nodes)) {
            $.each(nodes, function (index2, node2) {
                node2.internalNo = index2 + 1;
                node2.externalNo = index2 + 1 + "";
            })
        }
    },
    initAnswerCardInfo: function (paper) {
        var _this = this;
        var data = _this.initData(CommUtils.clone(paper.nodes));
        var answerCard = _this.answerCard;
        if (!CommUtils.isEmpty(data)) {
            answerCard.data = data;
        } else {
            answerCard.data = [];
        }
        Vue.set(_this.vm.paper, "nodes", paper.nodes);
        Vue.set(_this.vm, "paperTemplate", paper);
        _this.changeAnswerCard("modifyData");

    },
    initData: function (nodes) {
        var _this = this;
        var data = [];
        $.each(nodes, function (index, kNode) {
            data.push(kNode);
            if (CommUtils.isEmpty(kNode.children)) {
                return true;
            }
            $.each(kNode.children, function (index, tNode) {
                tNode.parentNodeId = kNode.nodeId;
                data.push(tNode);
                if (CommUtils.isEmpty(tNode.children)) {
                    return true;
                }
                var type = tNode.type;
                $.each(tNode.children, function (index, qNode) {
                    if (CommUtils.isEmpty(qNode.question.questionId)){
                        var question = qNode.question;
                        question.questionId = Date.parse(new Date());
                        question.parentId = qNode.parentId;
                        question.externalNo = qNode.externalNo;
                        question.nodeId = qNode.nodeId;
                        question.parentNodeId = tNode.nodeId;
                        question.type = type;
                        question.answer = {
                            label: ""
                        };
                        question.options = [];
                        if (type.code === "01" || type.code === "02") {
                            // 选择题默认4个 单选 多选
                            question.options = _this.getQuestionOption();
                            // 答案默认为A
                            question.answer.label = "A";
                        } else if (type.code === "03") {
                            // 填空题默认1个
                            question.options = [
                                {
                                    lable: "",
                                    optionId: new Date().getTime(),
                                    sequencing: "0"
                                }
                            ];
                        } else {
                            // 其他默认为0
                            question.options.length = 0;
                        }
                        qNode.parentNodeId = tNode.nodeId;
                    }
                    data.push(qNode);
                });
            });
        });
        return data;
    },
    changeAnswerCard: function (type) {
        var paper = CommUtils.clone(this.paper);
        var answerCard = this.answerCard;
        if (answerCard.data.length > 0) {
            paper.nodes = answerCard.data;
        }
        let data = {
            type: type,
            config: answerCard.config
        };
        if (type == "modifyData") data.data = {
            paperInfo: paper,
            answerCardInfo: this.answerCardInfo
        };
        this.iframeAnswerCard.submitData(data);
    },
    initQuestion: function (paper) {
        var _this = this;
        var fileId = paper.files[0].fileId;
        var answerCard = CommUtils.clone(_this.iframeAnswerCard.getAnswerCardInfo());
        var axises = answerCard.axises;
        var data = _this.initData(CommUtils.clone(paper.nodes));
        $.each(data, function (i, node) {
            var question = node.question;
            if (!CommUtils.isEmpty(question)){
                question.axises = [];
            }else {
                return true;
            }
            $.each(axises, function (k, axis) {
                if (node.nodeId === axis.nodeId) {
                    question.questionId = axis.questionId;
                    axis.parentId = axis.questionId;
                    axis.fileId = fileId;
                    var type = question.type;
                    if (type.code === "01" || type.code === "02") {
                        // 题目占用行数
                        axis.rowCount = "1";
                    }else {
                        axis.rowCount = axis.optionCount;
                    }
                    question.axises.push(axis);
                    question.stem = {
                        plaintext: "",
                        richText: "",
                    };
                    question.difficulty = {
                        code: "",
                        name: ""
                    };
                    question.answer = {
                        label : axis.answer
                    }

                }
            })

        });
        paper.nodes = data;
        paper.answerCard = answerCard;
    },
    getQuestionOption: function () {
        var options = [];
        var str = "ABCD";
        for (let i = 0; i < 4; i++) {
            var option = {
                alias: str.substr(i, 1),
                optionId: new Date().getTime(),
                sequencing: i
            }
            options.push(option);
        }
        return options;
    },
    buildSaved: function (){
        var _this = this;
        if (_this.isSaved){
            $("#saveFile").css("background-color","#a1a5ad");
        }else {
            $("#saveFile").css("background-color","#1C63D0");
        }
    }
}
