import {BlockElement} from "./blockElement.js";

var stemBlockElement = function(question,section, index){
    BlockElement.call(this,section,index, question);
    this.elementObject;
    // this.style = section.answerCard.config.style.element.judgementQuestion;
    // this.margin = this.style.margin;
};

CommUtils.extendObj(stemBlockElement,BlockElement);

stemBlockElement.prototype.buildHtml = function(container,index){
    if (this.section.answerCard.config.questionContentStatus == "0") return ;
    let html = this.question.stem.richText;
    html = '<span>' + this.question.externalNo + '.&nbsp;' + '</span>' + html;
    html = '<div class="stem-div" style="display: flex;">' + html +'</div>' + '<br>';
    this.elementObject = $(html).appendTo(container);
    this.element = this.elementObject;
}

export {stemBlockElement as StemBlockElement};
