// 试卷头部信息区 试卷名称, 考生信息,  注意事项, 考号
import { Section } from "./section.js";
import { ExamHeadBlockElement } from "../element/examHeadBlockElement.js";

var examHeadSection = function (answerCard, index) {
    Section.call(this, index, "examHead");
    this.answerCard = answerCard;
    this.examName = this.answerCard.data.title;
};

CommUtils.extendObj(examHeadSection, Section);

examHeadSection.prototype.initBlockElement = function () {
    let _section = this;
    _section.blockElements.push(new ExamHeadBlockElement(_section, this.examName));
};

export { examHeadSection as ExamHeadSection };
