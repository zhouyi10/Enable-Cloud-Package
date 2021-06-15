import {QuestionSection} from "./questionSection.js";
import {JudgementQuestionBlockElement} from "../element/judgementQuestionBlockElement.js";
var judgementQuestionSection = function (seciton, index) {
    let style = "display:flex;flex-wrap:wrap;"
    QuestionSection.call(this, index, "judgementQuestion",style);
    this.section = seciton;
}

CommUtils.extendObj(judgementQuestionSection, QuestionSection);

judgementQuestionSection.prototype.initBlockElement = function () {
    let _section = this;
    let i = 0;

    this.section.questions.forEach(element => {
        _section.blockElements.push(new JudgementQuestionBlockElement(element,_section,i++));
    });
};

judgementQuestionSection.prototype.createNewSection = function(index,slicedBlockElement){
    let newSection = new judgementQuestionSection(this.section,index);
    return newSection;
};

export {judgementQuestionSection as JudgementQuestionSection};
