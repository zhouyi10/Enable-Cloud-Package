import {QuestionSection} from "./questionSection.js";
import {ChineseArticleBlockElement} from "../element/chineseArticleBlockElement.js";
import {DocumentWriter} from "../documentWriter.js";

var chineseArticleSection = function (seciton, index) {
    QuestionSection.call(this, index, "chineseArticle");
    this.section = seciton;
}

CommUtils.extendObj(chineseArticleSection, QuestionSection);

chineseArticleSection.prototype.initBlockElement = function () {
    let _section = this;
    let i = 0;

    this.section.questions.forEach(element => {
        if (element.axises.length > 0) {
            let rows = (element.axises.length - 1) * 23 + "";
            let words = parseInt(rows.substring(0,rows.length - 1) + 0);
            _section.blockElements.push(new ChineseArticleBlockElement(element, _section, 0,true,words));
            for (var j = 1; j< element.axises.length; j++) {
                _section.blockElements.push(new ChineseArticleBlockElement(element, _section, j,false));
            }
        }else{
            for (var j = 1; j< Math.ceil(500/23 + 1); j++) {
                _section.blockElements.push(new ChineseArticleBlockElement(element, _section, j,false));
            }
        }
    });
};


chineseArticleSection.prototype.createNewSection = function(index,slicedBlockElement){
    let newSection = new chineseArticleSection(this.section,index);
    return newSection;
};
chineseArticleSection.prototype.appendChineseArticle = function (element,section,words){
    let currentSectionName = this.sectionName;
    this.blockElements = [];
    for (let i=this.index; i<this.answerCard.sections.length;i++) {
        if (answerCard.sections[i].section != undefined) {
            if (answerCard.sections[i].section.questionType == "08") {
                if (currentSectionName != answerCard.sections[i].sectionName) {
                    break;
                }
                answerCard.sections[i].blockElements = [];
            }else{
                break;
            }
        }else {
            break;
        }
    }
    this.blockElements.push(new ChineseArticleBlockElement(element, section, 1,true,words));
    for (var j = 0; j< Math.ceil(words/23); j++) {
        this.blockElements.push(new ChineseArticleBlockElement(element, section, 0,false));
    }

    // reindex
    for(let j = 0;j<this.blockElements.length;j++){
        this.blockElements[j].index = j;
    }
    let writer = new DocumentWriter(this.answerCard);
    writer.reload();
};

export {chineseArticleSection as ChineseArticleSection};
