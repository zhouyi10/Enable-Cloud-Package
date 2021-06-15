// 选择题section
import {QuestionSection} from "./questionSection.js";
import {SelectQuestionBlockElement} from "../element/selectQuestionBlockElement.js";
// 这里的section指的是数据模型里面的section
//{sectionType: "question", questionType :"selectQuestionSection",questions : [......
var selectQuestionSection = function (section, index) {
    let style = "display:flex;flex-wrap:wrap;"
    QuestionSection.call(this, index, "selectQuestion", style);
    this.section = section;
};

CommUtils.extendObj(selectQuestionSection, QuestionSection);

selectQuestionSection.prototype.initBlockElement = function () {
    let _section = this;
    let index = 0;
    let i = 1;
    let ques = [];
    this.section.questions.forEach(element => {
        //5个一组搞里头
        if (i % 6 == 0) {
            _section.blockElements.push(new SelectQuestionBlockElement(ques, _section, index++));
            i = 1;
            ques = [];
        }
        i++;
        ques.push(element);
    });
    //最后没有分完的单独成组
    if (ques.length > 0) {
        _section.blockElements.push(new SelectQuestionBlockElement(ques, _section, index++));
    }
};

selectQuestionSection.prototype.createNewSection = function (index, slicedBlockElement) {
    let newSection = new selectQuestionSection(this.section, index);
    return newSection;
}

export {selectQuestionSection as SelectQuestionSection};

