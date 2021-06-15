// 跟题目相关的section抽象基类, 请注意!!!!! 不需要记录答题卡坐标的Section请勿继承该QuestionSection
import {Section} from "./section.js";

var questionSection = function (index, sectionName, style) {
    var style = "border: 1px solid black;width: 100%;" + (style == undefined ? "" : style)
    Section.call(this, index, sectionName, style);
};

CommUtils.extendObj(questionSection, Section);

questionSection.prototype.slice = function (i) {
    //要拆分的block
    let sliced = this.blockElements[i];
    //创建新的section index+1
    let newSection = this.createNewSection(this.index + 1, sliced);
    //将i之后剩余的block移动到新的section中去
    let newIndex = 0;
    for (let j = i; j < this.blockElements.length; j++) {
        newSection.blockElements.push(this.blockElements[j]);
        //修改block的section归属
        this.blockElements[j].section = newSection;
        this.blockElements[j].index = newIndex++;
        //this.blockElements.splice(j,1);
    }
    //拆分的block从原section剔除
    let count = this.blockElements.length;
    for (let k = i; k < count; k++) {
        this.blockElements.pop();
    }
    newSection.sectionName = this.sectionName;
    return newSection;
};

export {questionSection as QuestionSection};

