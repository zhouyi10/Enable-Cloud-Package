// 题型区
import {Section} from "./section.js";
import {TextBlockElement} from "../element/textBlockElement.js";

var examTypeSection = function (typeName, index) {
    Section.call(this, index, "examType", "width: 100%;text-align: center;font-size: x-large;");
    this.typeName = typeName;
};

CommUtils.extendObj(examTypeSection, Section);

examTypeSection.prototype.initBlockElement = function () {
    let _section = this;
    let style = _section.answerCard.config.style.element.examType;
    let textStyle = CommUtils.formatStr("margin: {0}px; font-size:{1};", style.margin.join("px "), style.fontSize);
    _section.blockElements.push(new TextBlockElement(_section, this.typeName, textStyle));
};

export {examTypeSection as ExamTypeSection};
