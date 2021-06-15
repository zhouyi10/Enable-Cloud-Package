// 填空题section
import {QuestionSection} from "./questionSection.js";
import {BlankQuestionBlockElement} from "../element/blankQuestionBlockElement.js";

var blankQuestionSection = function (section, index) {
    QuestionSection.call(this, index, "blankQuestion", "display: flex;flex-wrap: wrap;");
    this.section = section;
};

CommUtils.extendObj(blankQuestionSection, QuestionSection);

blankQuestionSection.prototype.initBlockElement = function () {
    let _section = this;
    let questionIndex = 0;
    this.section.questions.forEach(question => {
        for (let blankOptionIndex = 0; blankOptionIndex < question.axises.length; blankOptionIndex++) {
            _section.blockElements.push(new BlankQuestionBlockElement(question, _section, questionIndex, blankOptionIndex));
        }
        questionIndex++;
    });
};

blankQuestionSection.prototype.createNewSection = function (index, slicedBlockElement) {
    let newSection = new blankQuestionSection(this.section, index);
    return newSection;
};

export {blankQuestionSection as BlankQuestionSection};

