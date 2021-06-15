// 选择题元素块
import {BlockElement} from "./blockElement.js";
//这里的section指的是视图对象section
var selectQuestionBlockElement = function (questions, section, index) {
    BlockElement.call(this, section, index);
    this.section = section;
    this.questions = questions;
    this.elementObject;
    this.style = section.answerCard.config.style.element.select;
    this.margin = CommUtils.clone(this.style.margin);
    this.maxOption = 0;
};

CommUtils.extendObj(selectQuestionBlockElement, BlockElement);

selectQuestionBlockElement.prototype.buildHtml = function (container) {
    let html = CommUtils.formatStr("<div class='questionGroup' style='margin: {0}px;padding: 0px;'>", this.margin.join("px ")) +
        "<table class='questionTable' border='0' cellspacing='0' style='font-size: 10px;'><tbody>";
    let tdStyle = CommUtils.formatStr("height:{0}px;width:{1}px;padding:0px;margin:{2}px;text-align: center;",
        this.style.td.height,
        this.style.td.width,
        this.style.td.margin.join("px ")
    );
    let pStyle = "display:inline-block;font-family:Futura,Trebuchet MS,Arial,sans-serif;margin:0px;padding:0px 2px";
    for (let i = 0; i < this.questions.length; i++) {
        let questionTemp = this.questions[i];
        questionTemp.answers = [];
        if (CommUtils.isNotEmpty(questionTemp.answer)) {
            questionTemp.answers = questionTemp.answer.split(",");
        }
        if (questionTemp.optionCount > this.maxOption) this.maxOption = questionTemp.optionCount;
        let charTemp = 65;
        html += CommUtils.formatStr("<tr index='{2}'><td style='{0}' class='ques_no'>{1}</td>",
            tdStyle, questionTemp.externalNo, i);
        for (let j = 0; j < questionTemp.optionCount; j++) {
            let answerStrTemp = String.fromCharCode(charTemp + j);
            html += (CommUtils.formatStr("<td class='questionTd' style='{0};'>" +
                "<div class='ques_option' style='display: {3};'>" +
                "[ <p style='{1}'>{2}</p> ]" +
                "</div>" +
                "<div class='answerArea' style='display: {4}; float: left;background-color: black;width: 80%;height: 50%;margin-left: 3px;margin-top: 2px;'>" +
                "</div>" +
                "</td>",
                tdStyle, pStyle, answerStrTemp,
                this.editAnswer && questionTemp.answers.includes(answerStrTemp) ? "none" : "unset",
                this.editAnswer && questionTemp.answers.includes(answerStrTemp) ? "unset" : "none"
            ));
        }
        html += "</tr>";
    }
    html += "</tbody></table></div>";
    this.elementObject = $(html).appendTo(container);
    this.element = this.elementObject;
    this.initEvent();
};

selectQuestionBlockElement.prototype.initPosition = function () {

    this.offsetTop = this.margin[0];
    this.offsetLeft = this.margin[3];

    // questionTd
    let tdHeight = this.style.td.height + this.style.td.margin[0] + this.style.td.margin[2];
    let tdWidth = this.style.td.width + this.style.td.margin[1] + this.style.td.margin[3];

    this.width += (tdWidth * (this.maxOption + 1));
    this.height += (tdHeight * this.questions.length);

};

selectQuestionBlockElement.prototype.getAxises = function () {
    let axises = [];
    for (let i = 0; i < this.questions.length; i++) {
        let axise = {
            offsetTop: this.section.offsetTop + this.offsetTop + this.style.td.height * i,
            offsetLeft: this.offsetLeft,
            width: this.width,
            height: this.style.td.height
        };
        axises.push(axise);
    }
    return axises;
}

selectQuestionBlockElement.prototype.initEvent = function () {
    let _this = this;
    if (_this.editAnswer) {
        _this.element.find(".questionTd").on("click", function (e) {
            _this.changeElement($(e.currentTarget));
        });
    }
};
selectQuestionBlockElement.prototype.changeData = function (tdDom) {
    let indexTemp = parseInt(tdDom.parent().attr("index"));
    let questionTemp = this.questions[indexTemp];
    let answerTemp = tdDom.find(".ques_option p").text().trim();
    if (questionTemp.answers.includes(answerTemp)) {
        questionTemp.answers.remove(answerTemp);
        tdDom.find(".answerArea").hide();
        tdDom.find(".ques_option").show();
    } else {
        questionTemp.answers.push(answerTemp);
        tdDom.find(".ques_option").hide();
        tdDom.find(".answerArea").show();
    }
    questionTemp.answer = questionTemp.answers.join(",");
};
export {selectQuestionBlockElement as SelectQuestionBlockElement};
