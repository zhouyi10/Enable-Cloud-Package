// 简答题section
import {QuestionSection} from "./questionSection.js";
import {SimpleQuestionElement} from "../element/simpleQuestionElement.js";

var simpleQuestionSection = function(section, index, choiseIndex){
    QuestionSection.call(this, index, "simpleQuestion");
    this.section = section;
    this.choiseIndex = choiseIndex;
};

CommUtils.extendObj(simpleQuestionSection,QuestionSection);

simpleQuestionSection.prototype.initBlockElement = function () {
    let _section = this;
    let sumCount = this.choiseIndex ? this.section.optionCount - this.choiseIndex : this.section.optionCount;
    for (let i = 0; i < sumCount; i++) {
        _section.blockElements.push(new SimpleQuestionElement(this.section, _section, i));
    }
};

simpleQuestionSection.prototype.createNewSection = function (index, slicedBlockElement) {
    let choiseIndex = this.section.choiseIndex;
    let newSection = new simpleQuestionSection(this.section, index, choiseIndex);
    delete this.section.choiseIndex;
    return newSection;
};

export {simpleQuestionSection as SimpleQuestionSection};

