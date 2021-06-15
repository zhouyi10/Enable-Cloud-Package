// 答题卡faced门面接口
import {AnswerCardBuilder} from "./answerCardBuilder.js";
import {DocumentWriter} from "./documentWriter.js";
import {QuestionSection} from "./section/questionSection.js";

var answerCard = function () {
    this.data;
    this.container = null;
    this.pages = [];
    this.sections = [];
    this.builder = new AnswerCardBuilder(this);
    this.blankResizeTimeout = 0;
    this.paperInfo;
    this.answerCardInfo;
    this.showAxis = CommUtils.getUrlParam("showAxis", window.parent);
    this.editAnswer = CommUtils.getUrlParam("editAnswer", window.parent);
    this.userId = CommUtils.getUrlParam("userId", window.parent);
    this.config = {
        columnType: 1,
        pageType: 'A4',
        candidateNumberEdition: '1',
        questionContentStatus: "0",
        judgeStyle: "0",
        sealingLineStatus: "0",
        editAnswer: false, // word == true
        editType: "paper", // word
    };
    this.style = {
        answerCard: {
            top: 0,
            width: 798,
            height: 1106,
            pageIndexGroup: {
                top: 28,
                left: 56,
                pageIndex: {
                    width: 10,
                    height: 10,
                    margin: [0, 10, 0, 0]
                }
            },
            pageAnChor: {
                top: 20,
                right: 20,
                bottom: 20,
                left: 20,
                width: 26,
                height: 26,
                borderRadius: 14
            },
            pageContainer: {
                top: 75,
                left: 56,
                width: 680,
                height: 790
            }
        },
        element: {
            examType: {
                margin: CommUtils.formatCssValue([10, 0]),
                fontSize: "x-large"
            },
            select: {
                margin: CommUtils.formatCssValue([14]),
                td: {
                    height: 22,
                    width: 28,
                    margin: CommUtils.formatCssValue([0])
                }
            },
            blank: {
                margin: CommUtils.formatCssValue([2]),
                width: 100,
                optionTitle: {
                    width: 30,
                    height: 20,
                    margin: [5, 2, 5, 5]
                },
                optionBlank: {
                    height: 20,
                    minWidth: 50,
                    margin: [5, 0, 5, 5],
                    defaultWidth: 100,
                    resizeWidth: 15,
                    toolsWidth: 45,
                }
            },
            chineseArticle: {
                margin: CommUtils.formatCssValue([10, 0, 0, 0]),
                width: 680,
                height: 33,
                words: {
                    margin: CommUtils.formatCssValue([10, 0, 10, 0]),
                    width: 680,
                    height: 33
                },
                cells: {
                    margin: CommUtils.formatCssValue([10, 0, 10, 0]),
                    width: 680,
                    height: 33
                }
            },
            simple: {
                line: {
                    width: 100,
                    height: 30,
                    margin: CommUtils.formatCssValue([0])
                },
                tools: {
                    width: 100,
                    height: 35,
                    margin: [0, 0, 5, 0],
                    option: {
                        height: 35,
                        inputWidth: 100,
                        inputHeight: 31,
                    }
                }
            },
            judgementQuestion: {
                margin: CommUtils.formatCssValue([10]),
                width: 150,
                height: 20
            }
        }
    };
    this.pdf = {};
};

answerCard.prototype = {
    init: function () {
        let _this = this;
        _this.extendPrototypes();
        _this.initData();
        _this.initEvent();
    },
    initData: function () {
        if (this.isPdf) {
            this.initPdf();
            window.status = "completed";
        }
    },
    initPdf: function () {
        this.submitData({
            type: "modifyData",
            data: {
                answerCardInfo: this.pdf.answerCardInfo,
                paperInfo: this.pdf.paperInfo,
            },
            config: this.config
        });
    },
    initBuild: function () {
        this.container = $("#container")[0];
        $(this.container).empty();
        this.pages = [];
        this.sections = [];
        this.builder.build();
        let writer = new DocumentWriter(this);
        writer.write();
    },
    initReload: function () {
        let writer = new DocumentWriter(this);
        writer.reload();
    },
    submitData: function (data) {
        let _this = this;
        if (data.type == "modifyConfig") {
            _this.setConfig(data.config);
            _this.initReload();
        } else if (data.type == "modifyData") {
            _this.setConfig(data.config);
            _this.answerCardInfo = data.data.answerCardInfo;
            _this.paperInfo = data.data.paperInfo;
            _this.data = _this.tranData();
            _this.initBuild();
        }
    },
    initEvent: function () {
        let _this = this;
    },
    changeAnswerCard: function () {
        window.parent.postMessage({"type": "changeAnswerCard", data: true}, "*");
    },
    setConfig: function (config) {
        this.config = config;
        this.config.showAxises = (this.showAxis == "true");
        if (CommUtils.isNotEmpty(this.editAnswer)) this.config.editAnswer = (this.editAnswer == "true");
        this.config.style = this.style;
    },
    extendPrototypes: function () {
        Array.prototype.remove = function (val) {
            var index = this.indexOf(val);
            if (index > -1) {
                this.splice(index, 1);
            }
        };
    },
    getAnswerCardInfo: function () {
        let _this = this;
        let addAnswerCardTemp = {
            answerCardId: _this.data.answerCardId,
            examId: _this.data.examId,
            columnType: _this.config.columnType,
            candidateNumberEdition: _this.config.candidateNumberEdition,
            sealingLineStatus: _this.config.sealingLineStatus,
            judgeStyle: _this.config.judgeStyle,
            pageType: _this.config.pageType,
            questionContentStatus: _this.config.questionContentStatus,
            pageCount: _this.pages.length,
            creator: _this.userId,
            editType: _this.config.editType,
            axises: []
        };
        let pageEleMapTemp = new Map();
        for (let sectionTemp of _this.sections) {
            if (sectionTemp.__proto__ instanceof QuestionSection) {
                let page = sectionTemp.cardPage;
                for (let sectionElementTemp of sectionTemp.blockElements) {
                    let axises = sectionElementTemp.buiildAxises();
                    if (sectionElementTemp.questions && sectionElementTemp.questions instanceof Array) {
                        if (axises.length != sectionElementTemp.questions.length) {
                            layer.msg("axises data build error!");
                            return false;
                        }
                        for (let i = 0; i < axises.length; i++) {
                            let question = sectionElementTemp.questions[i];
                            let axise = axises[i];
                            _this.buildAxise(pageEleMapTemp, addAnswerCardTemp.axises, page, question, axise)
                            addAnswerCardTemp.axises.push();
                        }
                    } else {
                        _this.buildAxise(pageEleMapTemp, addAnswerCardTemp.axises, page, sectionElementTemp.question, axises[0]);
                    }
                }
                ;
            }
        }
        console.log(addAnswerCardTemp);
        return addAnswerCardTemp;
    },
    buildAxise: function (pageEleMapTemp, axises, page, questionTemp, axise) {
        let axiseKey = page.index + "_" + questionTemp.nodeId;
        let axiseTemp = pageEleMapTemp.get(axiseKey);
        let answer = questionTemp.answer ? questionTemp.answer : "";
        let optionCount = questionTemp.optionCount ? questionTemp.optionCount : 0;
        if (questionTemp.typeCode == "01" || questionTemp.typeCode == "02" || questionTemp.typeCode == "03") {
            if (questionTemp.typeCode == "01" || questionTemp.typeCode == "02") {
                answer = questionTemp.answers.join(",");
            } else if (questionTemp.typeCode == "03") {
                let answersTemp = [];
                for (let blank of questionTemp.axises) {
                    if (!blank.answer) blank.answer = "";
                    answersTemp.push(blank.answer);
                }
                answer = answersTemp.join("@*@");
                optionCount = answersTemp.length;
            }
        } else if (axiseTemp) {
            axiseTemp.height += axise.height;
        }
        axiseTemp = {
            externalNo: questionTemp.externalNo,
            questionId: questionTemp.questionId,
            xAxis: axise.offsetTop,
            yAxis: axise.offsetLeft,
            width: axise.width,
            height: axise.height,
            sequencing: axises.length,
            parentId: questionTemp.parentId ? questionTemp.nodeId : "",
            nodeId: questionTemp.nodeId ? questionTemp.nodeId : "",
            parentNodeId: questionTemp.parentNodeId ? questionTemp.parentNodeId : "",
            pageNo: page.index + 1,
            typeCode: questionTemp.typeCode,
            typeName: questionTemp.typeName,
            answer: answer,
            optionCount: optionCount,
            axisId: "",
            answerCardId: "",
        };
        pageEleMapTemp.set(axiseKey, axiseTemp);
        axises.push(axiseTemp);
    },
    showAxises: function () {
        let _this = this;
        if (!_this.config.showAxises) return;
        for (let sectionTemp of _this.sections) {
            if (sectionTemp.__proto__ instanceof QuestionSection) {
                for (let sectionElementTemp of sectionTemp.blockElements) {
                    sectionElementTemp.buiildAxises();
                }
            }
        }
    },
    tranData: function () {
        let _this = this;
        if (_this.answerCardInfo) {
            return _this.tranAnswerCardInfo();
        } else {
            return _this.tranPaperInfo();
        }
    },
    tranPaperInfo: function (questionIdAxissMap) {
        let _this = this;
        let answerCard = {
            answerCardId: _this.answerCardInfo ? _this.answerCardInfo.answerCardId : "",
            examId: _this.paperInfo.paperId,
            title: _this.paperInfo.name,
            sections: [],
        };
        let questionSection = undefined;
        let currentParentNodeId = null;
        let stemSection = undefined;
        for (let i = 0; i < _this.paperInfo.nodes.length; i++) {
            let node = _this.paperInfo.nodes[i];
            switch (node.level) {
                case 1: // 卷别
                    answerCard.sections.push({
                        sectionType: "examType",
                        text: node.name
                    });
                    questionSection = undefined;
                    break;
                case 2: // 大题
                    answerCard.sections.push({
                        sectionType: "questionType",
                        text: node.name
                    });
                    questionSection = undefined;
                    break;
                case 3: // 题目
                    let questionTemp = node.question;
                    if (!stemSection) {
                        currentParentNodeId = node.parentNodeId;
                        stemSection = {
                            sectionType: "question",
                            questionType: 'stem',
                            questions: []
                        };
                        answerCard.sections.push(stemSection);
                    }
                    if (currentParentNodeId == node.parentNodeId) {
                        stemSection.questions.push(_this.buildSectionStem(node));
                    } else {
                        stemSection = {
                            sectionType: "question",
                            questionType: 'stem',
                            questions: []
                        };
                        answerCard.sections.push(stemSection);
                        stemSection.questions.push(_this.buildSectionStem(node));
                        currentParentNodeId = node.parentNodeId;
                    }
                    if (!questionSection) {
                        questionSection = {
                            sectionType: "question",
                            questionType: questionTemp.type.code, // 01,单选 02多选
                            questions: []
                        };
                        answerCard.sections.push(questionSection);
                    }
                    questionSection.questions.push(_this.buildSectionQuestion(questionSection, node, questionIdAxissMap ? questionIdAxissMap.get(questionTemp.questionId) : undefined));
                    break;
                case 4:
                    let questionTemp1 = node.question;
                    let preNodeId = questionSection.questions[questionSection.questions.length - 1].nodeId;
                    if (preNodeId == node.parentNodeId || preNodeId == node.parentId) {
                        questionSection.questions = questionSection.questions.slice(0, questionSection.questions.length - 1);
                        questionSection.questionType = questionTemp1.type.code;
                        stemSection.questions = stemSection.questions.slice(0, stemSection.questions.length - 1);
                    }
                    stemSection.questions.push(_this.buildSectionStem(node));
                    questionSection.questions.push(_this.buildSectionQuestion(questionSection, node, questionIdAxissMap ? questionIdAxissMap.get(questionTemp1.questionId) : undefined));
                    break;
            }
        }
        return answerCard;
    },
    tranAnswerCardInfo: function () {
        let _this = this;

        _this.config.columnType = _this.answerCardInfo.columnType;
        _this.config.pageType = _this.answerCardInfo.pageType;
        _this.config.candidateNumberEdition = _this.answerCardInfo.candidateNumberEdition;
        _this.config.questionContentStatus = _this.answerCardInfo.questionContentStatus;
        _this.config.sealingLineStatus = _this.answerCardInfo.sealingLineStatus;
        _this.config.judgeStyle = _this.answerCardInfo.judgeStyle;

        let questionIdAxisMap = new Map();
        for (let axise of _this.answerCardInfo.axises) {
            let axisesTemp = questionIdAxisMap.get(axise.questionId);
            if (!axisesTemp) {
                axisesTemp = [];
                questionIdAxisMap.set(axise.questionId, axisesTemp)
            }
            axisesTemp.push(axise);
        }
        return _this.tranPaperInfo(questionIdAxisMap);
    },
    buildSectionQuestion: function (questionSection, node, axisesTemp) {
        let questionTemp = node.question;
        let sectionQuestion;
        let axises = [];
        if (axisesTemp) {
            for (let axiseTemp of axisesTemp) {
                axises.push({
                    xAxis: axiseTemp.xAxis,
                    yAxis: axiseTemp.yAxis,
                    width: axiseTemp.width,
                    height: axiseTemp.height,
                    rowCount: axiseTemp.rowCount,
                    sequencing: axisesTemp.sequencing
                });
            }
        }
        if (questionTemp.type.code == "01" || questionTemp.type.code == "02") {
            sectionQuestion = {
                nodeId: node.nodeId,
                parentNodeId: node.parentNodeId,
                questionId: questionTemp.questionId,
                parentId: questionTemp.parentId,
                points: node.points, // 分数
                typeCode: questionTemp.type.code, //题目类型编号
                typeName: questionTemp.type.name, //题目类型名称
                answer: questionTemp.answer.label != undefined ? questionTemp.answer.label : questionTemp.answer.lable,
                externalNo: node.externalNo, // 题号
                optionCount: questionTemp.options.length, // 选择题|填空题设置选项个数|空个数
                sequencing: questionSection.questions.length, // 排序
                axises: axises
            };
        } else if (questionTemp.type.code == "03") {
            sectionQuestion = {
                nodeId: node.nodeId,
                parentNodeId: node.parentNodeId,
                questionId: questionTemp.questionId,
                parentId: questionTemp.parentId,
                points: node.points, // 分数
                typeCode: questionTemp.type.code, //题目类型编号
                typeName: questionTemp.type.name, //题目类型名称
                answer: questionTemp.answer.label != undefined ? questionTemp.answer.label : questionTemp.answer.lable,
                externalNo: node.externalNo, // 题号
                optionCount: axises ? axises.length : 1, // 选择题|填空题设置选项个数|空个数
                sequencing: questionSection.questions.length, // 排序
                axises: axises
            };
            if (!sectionQuestion.axises) {
                sectionQuestion.axises = [];
                if (CommUtils.isNotEmpty(sectionQuestion.answer)) {
                    let index = 0;
                    for (let answer of sectionQuestion.answer.split("@*@")) {
                        sectionQuestion.axises.push({
                            xAxis: "",
                            yAxis: "",
                            width: "",
                            height: "",
                            rowCount: "",
                            answer: answer.trim(),
                            sequencing: index
                        });
                        index++;
                    }
                } else {
                    sectionQuestion.axises.push({
                        xAxis: "",
                        yAxis: "",
                        width: "",
                        height: "",
                        rowCount: "",
                        answer: "",
                        sequencing: 0
                    })
                }
            } else if (sectionQuestion.answer != undefined) {
                let index = 0;
                for (let answer of sectionQuestion.answer.split("@*@")) {
                    let qaxisTemp = sectionQuestion.axises[index];
                    if (qaxisTemp) {
                        qaxisTemp.answer = answer.trim();
                    } else {
                        sectionQuestion.axises.push({
                            xAxis: "",
                            yAxis: "",
                            width: "",
                            height: "",
                            rowCount: "",
                            answer: answer.trim(),
                            sequencing: sectionQuestion.axises.length
                        })
                    }
                    index++;
                }
            }
        } else if (questionTemp.type.code == "04") {
            sectionQuestion = {
                nodeId: node.nodeId,
                parentNodeId: node.parentNodeId,
                questionId: questionTemp.questionId,
                parentId: questionTemp.parentId,
                points: node.points, // 分数
                typeCode: questionTemp.type.code, //题目类型编号
                typeName: questionTemp.type.name, //题目类型名称
                answer: questionTemp.answer.label != undefined ? questionTemp.answer.label : questionTemp.answer.lable,
                externalNo: node.externalNo, // 题号
                optionCount: 0,
                sequencing: questionSection.questions.length, // 排序
                axises: axises
            };
        } else {
            sectionQuestion = {
                nodeId: node.nodeId,
                parentNodeId: node.parentNodeId,
                questionId: questionTemp.questionId,
                parentId: questionTemp.parentId,
                points: node.points, // 分数
                typeCode: questionTemp.type.code, //题目类型编号
                typeName: questionTemp.type.name, //题目类型名称
                answer: questionTemp.answer.label != undefined ? questionTemp.answer.label : questionTemp.answer.lable,
                externalNo: node.externalNo, // 题号
                optionCount: axises.length == 0 ? 3 : axises.length, // 选择题|填空题设置选项个数|空个数
                sequencing: questionSection.questions.length, // 排序
                axises: axises
            };
        }
        return sectionQuestion;
    },
    buildSectionStem: function (node) {
        return {externalNo: node.externalNo, stem: node.question.stem}
    }
};

export {answerCard as AnswerCard};
