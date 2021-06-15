// 填空题元素块
import {BlockElement} from "./blockElement.js";
import {DocumentWriter} from "../documentWriter.js";

var blankQuestionBlockElement = function (question, section, questionIndex, blankOptionIndex) {
    BlockElement.call(this, section, section.blockElements.length, question);
    this.style = section.answerCard.config.style.element.blank;
    this.blankOptionMaxWidth = section.answerCard.config.style.answerCard.pageContainer.width - 5;
    this.blankOptionIndex = blankOptionIndex;
    this.questionIndex = questionIndex;
    this.isFocus = false;
    this.mousedownTimeout = 0;
};

CommUtils.extendObj(blankQuestionBlockElement, BlockElement);

blankQuestionBlockElement.prototype.buildHtml = function (container, index) {
    let _this = this;
    let htmlStrTemp = "";

    if (_this.blankOptionIndex == 0) {
        htmlStrTemp += CommUtils.formatStr(
            "<div class='blankTitle' style='width:{0}px;height:{1}px;margin:{2}px;text-align: right;'>{3}.</div>",
            _this.style.optionTitle.width,
            _this.style.optionTitle.height,
            _this.style.optionTitle.margin.join("px "),
            _this.question.externalNo);
    }
    let blankOption = _this.question.axises[_this.blankOptionIndex];

    if (CommUtils.isEmpty(blankOption.width)) {
        blankOption.width = _this.style.width;
    }

    let maxWidthTemp = _this.blankOptionMaxWidth;
    if (_this.blankOptionIndex == 0) {
        maxWidthTemp -= (_this.style.optionTitle.width + _this.style.optionTitle.margin[1] + _this.style.optionTitle.margin[3] +
            _this.style.optionBlank.margin[1] + _this.style.optionBlank.margin[3] + _this.style.optionBlank.resizeWidth);
    } else {
        maxWidthTemp -= (_this.style.optionBlank.margin[1] + _this.style.optionBlank.margin[3] + _this.style.optionBlank.resizeWidth);
    }

    if (blankOption.width > maxWidthTemp) blankOption.width = maxWidthTemp;
    htmlStrTemp += CommUtils.formatStr("<div class='blankAnswer'><div class='blankOption' contenteditable='{8}'" +
        "style='max-width:{0}px;min-width:{1}px;width:{2};height:{3}px;border-bottom: 1px solid black;margin:{4}px;" +
        "resize: horizontal;overflow: hidden; padding-right: 15px;font-size: 15px;outline:none;' optionIndex={5} elementIndex={6}>{7}</div>",
        maxWidthTemp,
        _this.style.optionBlank.minWidth,
        (_this.editAnswer && CommUtils.isNotEmpty(blankOption.answer) ?  "unset": (blankOption.width) + "px"),
        _this.style.optionBlank.height - 1,
        _this.style.optionBlank.margin.join("px "),
        _this.blankOptionIndex,
        _this.index,
        _this.editAnswer ? blankOption.answer : "",
        _this.editAnswer
    );
    if (_this.blankOptionIndex == _this.question.axises.length - 1) {
        htmlStrTemp += CommUtils.formatStr("<div class='optionTools' style='margin-top: 4px;width:{0}px;'>" +
            "<img class='addBlank' style='margin-right: 2px;' src='{1}'><img class='minusBlank' src='{2}'></div></div>",
            _this.style.optionBlank.toolsWidth,
            _this.section.answerCard.urls.addBlank,
            _this.section.answerCard.urls.minusBlank,
            );
        htmlStrTemp += "<div style='width: 100%;'></div>";
        _this.isBr = true;
    } else {
        htmlStrTemp += "</div>";
    }
    this.element = $(htmlStrTemp).appendTo(container);
    _this.initEvent();
};
blankQuestionBlockElement.prototype.initPosition = function () {
    let _this = this;
    _this.offsetTop = _this.style.optionBlank.margin[0];
    _this.offsetLeft = _this.style.optionBlank.margin[3];
    let blankOptionDom = this.element.find(".blankOption");
    if (_this.blankOptionIndex == 0) {
        _this.offsetLeft = _this.style.optionTitle.margin[3] + _this.style.optionTitle.width + _this.style.optionTitle.margin[1] +
            _this.style.optionBlank.margin[3];
    }
    let blankOption = _this.question.axises[_this.blankOptionIndex];
    if (_this.editAnswer && blankOption.answer != '') {
        _this.width = blankOptionDom.width() + _this.style.optionBlank.resizeWidth;
        blankOption.width = blankOptionDom.width();
    } else {
        _this.width = blankOption.width + _this.style.optionBlank.resizeWidth;
    }
    _this.height = _this.style.optionBlank.height;
    _this.margin = _this.style.optionBlank.margin;
};
blankQuestionBlockElement.prototype.initEvent = function () {
    let _this = this;

    let blankOption = $(this.element.find(".blankOption"));
    blankOption.on("input", function (e) {
        console.log("input");
        _this.isFocus = true;
        let currentTarget = $(e.currentTarget);
        let index = currentTarget.attr("optionIndex");
        let blankOption = _this.question.axises[index];
        blankOption.answer = currentTarget.text().trim();
    });
    blankOption.focus(function (e) {
        console.log("focus");
        _this.isFocus = true;
        let focusInterval = setInterval(function (){
            console.log("focus set isFocus false");
            clearInterval(focusInterval);
        }, 100);
    });
    blankOption.blur(function (e) {
        console.log("blur");
        if (_this.isFocus) {
            if (_this.setBlankOption($(e.currentTarget))) _this.changeElement();
        }
        _this.isFocus = false;
    });
    blankOption.mouseup(function (e) {
        console.log("mouseup");
        if (_this.isFocus) {
            console.log("mouseup isFocus true");
            return;
        }
        clearInterval(_this.mousedownTimeout);
        let currentTarget = $(e.currentTarget);
        if (_this.setBlankOption(currentTarget)) _this.changeElement();
    });
    blankOption.mousedown(function (e) {
        console.log("mousedown");
        if (_this.isFocus) {
            console.log("mousedown isFocus true");
            return;
        }
        let currentTarget = $(e.currentTarget);
        let mousedownTimeout = _this.mousedownTimeout = setInterval(function () {
            clearInterval(mousedownTimeout);
            console.log("mousedownTimeout");
            if (_this.isFocus) {
                console.log("mousedownTimeout isFocus true")
                return;
            }
            if (_this.setBlankOption(currentTarget)) _this.changeElement();
        }, 800);
    });

    let optionToolsDom = $(this.element.find(".optionTools"));
    let prevOptionIndex = parseInt(blankOption.attr("optionIndex"));
    let prevElementIndex = parseInt(blankOption.attr("elementIndex"));
    let prevBlankOption = _this.question.axises[prevOptionIndex];
    let prevBlockElement = _this.section.blockElements[prevElementIndex];
    optionToolsDom.find(".addBlank").on("click",function () {
        console.log("addBlank");
        _this.question.axises.push({
            width: (prevBlankOption.width < _this.style.optionBlank.minWidth ? _this.style.optionBlank.minWidth : prevBlankOption.width),
            answer: ""
        });
        _this.section.blockElements.splice(prevElementIndex + 1, 0, new blankQuestionBlockElement(_this.question, _this.section, _this.questionIndex, prevOptionIndex + 1));
        for (let i = prevElementIndex+1; i < _this.section.blockElements.length; i++) {
            let blockElementTemp = _this.section.blockElements[i];
            blockElementTemp.index = i;
        }
        _this.changeElement();
    });
    optionToolsDom.find(".minusBlank").on("click",function () {
        console.log("minusBlank");
        if (_this.question.axises.length <=1) return ;
        _this.question.axises.splice(prevOptionIndex, 1);
        _this.section.blockElements.splice(prevElementIndex, 1);
        _this.changeElement();
    });
};
blankQuestionBlockElement.prototype.setBlankOption = function (currentTarget) {
    let _this = this;
    let optionIndex = currentTarget.attr("optionIndex");
    let blankOption = _this.question.axises[optionIndex];

    // 过滤换行
    if (_this.editAnswer && blankOption.answer && CommUtils.isNotEmpty(blankOption.answer)) {
        currentTarget.css("width", "unset");
        if (blankOption.answer.length > 45) {
            blankOption.answer = blankOption.answer.substring(0, 44);
        }
        currentTarget.html(blankOption.answer);
        if (currentTarget.width() == blankOption.width) return false;
    }

    let maxWidthTemp = _this.blankOptionMaxWidth;
    if (_this.blankOptionIndex == 0) {
        maxWidthTemp -= (_this.style.optionTitle.width + _this.style.optionTitle.margin[1] + _this.style.optionTitle.margin[3]);
    }
    blankOption.width = currentTarget.width() > maxWidthTemp ? _this.blankOptionMaxWidth : currentTarget.width();
    return true;
}

export {blankQuestionBlockElement as BlankQuestionBlockElement};
