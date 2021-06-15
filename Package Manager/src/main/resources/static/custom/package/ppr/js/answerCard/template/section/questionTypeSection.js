// 题型区
import {Section} from "./section.js";
import {TextBlockElement} from "../element/textBlockElement.js";

var questionTypeSection = function (typeName, index) {
    Section.call(this, index, "questionType");
    this.typeName = typeName;
};

CommUtils.extendObj(questionTypeSection, Section);

questionTypeSection.prototype.initBlockElement = function () {
    let _section = this;
    _section.blockElements.push(new TextBlockElement(_section, this.typeName));
};

export {questionTypeSection as QuestionTypeSection};

