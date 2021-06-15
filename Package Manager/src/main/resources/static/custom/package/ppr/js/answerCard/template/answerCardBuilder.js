// 答题卡视图结构builder，用来将数据结构构建为视图结构
import {ExamHeadSection} from "./section/examHeadSection.js";
import {QuestionTypeSection} from "./section/questionTypeSection.js";
import {SelectQuestionSection} from "./section/selectQuestionSection.js";
import {BlankQuestionSection} from "./section/blanQuestionSection.js";
import {ChineseArticleSection} from "./section/chineseArticleSection.js";
import {SimpleQuestionSection} from "./section/simpleQuestionSection.js";
import {JudgementQuestionSection} from "./section/judgementQuestionSection.js";
import {StemSection} from "./section/stemSection.js";
import {ExamTypeSection} from "./section/examTypeSection.js";

var answerCardBuilder = function (answerCard) {
    this.answerCard = answerCard;
};
let index = 0;
answerCardBuilder.prototype = {
    build: function () {
        let _ans = this.answerCard;
        let _builder = this;
        let examHeadSection = new ExamHeadSection(this.answerCard, index++);
        examHeadSection.initBlockElement();
        _ans.sections.push(examHeadSection);
        _ans.data.sections.forEach(section => {
            if (section.sectionType == "examType") {
                _builder.buildExamType(section);
            } else if (section.sectionType == "questionType") {
                _builder.buildQuestionType(section);
            } else if (section.sectionType == "question") {
                _builder.buildQuestionSection(section);
            }
        });
    },
    buildExamType: function (section) {
        let _ans = this.answerCard;
        let questionType = new ExamTypeSection(section.text, index++);
        questionType.answerCard = _ans;
        questionType.initBlockElement();
        _ans.sections.push(questionType);
    },
    buildQuestionType: function (section) {
        let _ans = this.answerCard;
        let questionType = new QuestionTypeSection(section.text, index++);
        questionType.answerCard = _ans;
        questionType.initBlockElement();
        _ans.sections.push(questionType);
    },
    buildQuestionSection: function (section) {
        let _ans = this.answerCard;
        let _section;
        switch (section.questionType) { // 新增题型在此位置增加
            case "01":
                _section = new SelectQuestionSection(section, index++);
                break;
            case "02":
                _section = new SelectQuestionSection(section, index++);
                break;
            case "03":
                _section = new BlankQuestionSection(section, index++);
                break;
            case "08":
                _section = new ChineseArticleSection(section, index++);
                break;
            case "04":
                _section = new JudgementQuestionSection(section, index++);
                break;
            case "stem":
                _section = new StemSection(section,index++);
                break;
            default: // 一个题目一个section
                for (let question of section.questions) {
                    _section = new SimpleQuestionSection(question, index++);
                    _section.answerCard = _ans;
                    _section.initBlockElement();
                    _ans.sections.push(_section);
                }
                return;
        }
        _section.answerCard = _ans;
        _section.initBlockElement();
        _ans.sections.push(_section);
    }
};

export {answerCardBuilder as AnswerCardBuilder};
