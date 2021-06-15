import {BlockElement} from "./blockElement.js";

var judgementQuestionBlockElement = function (question, section, index) {
    BlockElement.call(this, section, index, question);
    this.elementObject;
    this.style = section.answerCard.config.style.element.judgementQuestion;
    this.margin = this.style.margin;
};

CommUtils.extendObj(judgementQuestionBlockElement, BlockElement);

//这里的index原想是当做append时候插入使用，使用重绘方案后没什么用了
judgementQuestionBlockElement.prototype.buildHtml = function (container, index) {
    let _thisElement = this;
    let isAnswer = false;
    if (this.question.answer != undefined && this.question.answer != "") {
        if (this.question.answer.trim() == "√" || this.question.answer.trim().toUpperCase() == "T") {
            isAnswer = true;
            this.question.answer = "T";
        } else {
            this.question.answer = "F";
        }
    }
    let html = CommUtils.formatStr(
        '<div class="judge-all-content" style="margin: {0}px">' +
        '        <div class="judge-ques-no">' + this.question.externalNo + '.</div>\n' +
        '        <div class="judge-content" style="display: {1}">\n' +
        '            <span>[&nbsp;</span>\n' +
        '            <span>T</span>\n' +
        '            <span>&nbsp;]</span>\n' +
        '        </div>\n' +
        '<div class="judge-answer-content-div" style="display: {2}"><span class="judge-answer-content"><span></div>' +
        '        <div class="judge-content" style="display: {3}">\n' +
        '            <span>[&nbsp;</span>\n' +
        '            <span>F</span>\n' +
        '            <span>&nbsp;]</span>\n' +
        '        </div>\n' +
        '<div class="judge-answer-content-div" style="display: {4}"><span class="judge-answer-content"><span></div>' +
        '</div>', this.style.margin.join("px "),
        isAnswer ? "none" : "unset",
        isAnswer ? "flex" : "none",
        !isAnswer ? "none" : "unset",
        !isAnswer ? "flex" : "none",
    );

    this.elementObject = $(html).appendTo(container);
    //this.id = index;
    this.element = this.elementObject;
    this.element.find(".judge-content").click(function (event) {
        if (!_thisElement.editAnswer) {
            return;
        }
        isAnswer = !isAnswer;
        let childrens = $(event.currentTarget).parent().children();
        if (isAnswer) {
            $(childrens[1]).hide();
            $(childrens[2]).css("display", "flex");
            $(childrens[3]).show();
            $(childrens[4]).hide();
            _thisElement.question.answer = "T";
        } else {
            $(childrens[1]).show();
            $(childrens[2]).hide();
            $(childrens[3]).hide();
            $(childrens[4]).css("display", "flex");
            _thisElement.question.answer = "F";
        }
    });
};

judgementQuestionBlockElement.prototype.initPosition = function () {
    var marginData = this.style.margin;
    let offsetTop = marginData[0];
    let offsetLeft = marginData[3];
    this.width = this.style.width;
    this.height = this.style.height;
    this.offsetTop = offsetTop;
    this.offsetLeft = offsetLeft;

}


export {judgementQuestionBlockElement as JudgementQuestionBlockElement};
