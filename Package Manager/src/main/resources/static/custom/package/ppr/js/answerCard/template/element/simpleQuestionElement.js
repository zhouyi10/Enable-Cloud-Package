import {BlockElement} from "./blockElement.js";

let simpleQuestionElement = function (question, section, lineIndex) {
    BlockElement.call(this, section, section.blockElements.length, question);
    this.style = section.answerCard.config.style.element.simple;
    this.maxWidth = section.answerCard.config.style.answerCard.pageContainer.width;
    this.container = undefined;
    this.lineIndex = lineIndex;
}

CommUtils.extendObj(simpleQuestionElement, BlockElement);

simpleQuestionElement.prototype.buildHtml = function (container) {
    let _this = this;
    _this.container = container;
    let htmlStrTemp = "";
    if (_this.index == 0 && _this.lineIndex == 0) {
        htmlStrTemp = CommUtils.formatStr("<div class='tools no-print' style='width: {0}%; height:{1}px; margin:{2}px;'>",
            _this.style.tools.width,
            _this.style.tools.height,
            _this.style.tools.margin.join("px ")
        );
        htmlStrTemp += (CommUtils.formatStr("<div class='title' style='float: left;'>{0}</div>",
            _this.question.externalNo + "."));
        htmlStrTemp += (CommUtils.formatStr(
            "<div class='option' style='" +
            "float:right;display: flex;justify-content: center;align-items: center;align-content: center;'>" +
            "<div class='lineOption' style='font-size: 14px;margin-right: 2px;'>行数</div>" +
            "<div class='removeLine' style='border: 1px solid #CBCBCB;height:{2}px;'>" +
            "<img src='{0}'/>" +
            "</div>" +
            "<input class='inputOption' type='number' style='height:{4}px;width:{3}px; text-align: center;' value='{5}'>" +
            "<div class='addLine' style='border: 1px solid #CBCBCB;height:{2}px;'>" +
            "<img src='{1}'/>" +
            "</div>" +
            "</div>",
            _this.section.answerCard.urls.removeLine,
            _this.section.answerCard.urls.addLine,
            _this.style.tools.option.height,
            _this.style.tools.option.inputWidth,
            _this.style.tools.option.inputHeight,
            _this.question.optionCount,
        ));
        htmlStrTemp += "</div>";

        htmlStrTemp += CommUtils.formatStr("<div class='contentArea' style='outline:none;overflow: hidden;line-height: 24px;font-size: 18px;width: {0}%; height:{1}px; margin:0px;' contenteditable={2}></div>",
            _this.style.line.width,
            _this.style.line.height,
            _this.editAnswer
        );
    } else if (_this.section.choiseIndex && _this.index == 0) {
        htmlStrTemp += CommUtils.formatStr("<div class='contentArea' style='outline:none;overflow: hidden;line-height: 24px;font-size: 18px;width: {0}%; height:{1}px; margin:0px;' contenteditable={2}></div>",
            _this.style.line.width,
            _this.style.line.height,
            _this.editAnswer
        );
    }

    if (_this.index == 0) {
        _this.elementObject = $(htmlStrTemp).appendTo(container);
        _this.element = _this.elementObject;
        _this.initEvent();
    } else {
        let contentAreaDom = $(container).find(".contentArea")
        contentAreaDom.height((_this.index + 1) * _this.style.line.height);
    }
};

simpleQuestionElement.prototype.clear = function () {
    let _this = this;
    if (_this.index == 0) {
        _this.element.remove();
    } else {
        let contentAreaDom = $(_this.container).find(".contentArea");
        let countLine = _this.index;
        if (_this.question.choiseIndex && _this.question.choiseIndex < _this.index) countLine = _this.question.choiseIndex;
        contentAreaDom.height((countLine) * _this.style.line.height);
        _this.question.choiseIndex = countLine;
    }
}

simpleQuestionElement.prototype.initEvent = function () {
    let _this = this;
    _this.element.find(".removeLine").on("click", function () {
        if (_this.question.optionCount <= 3) return;
        _this.question.optionCount -= 1;
        if (_this.section.blockElements.length == 1) { // 唯一的第一元素不能删除, 否则合并时找不到父节点
            for (let sectionTemp of _this.section.answerCard.sections) {
                if (sectionTemp.sectionName == _this.section.sectionName && sectionTemp != _this.section) {
                    sectionTemp.blockElements.splice(sectionTemp.blockElements.length - 1, 1);
                    break;
                }
            }
        } else {
            _this.section.blockElements.splice(_this.section.blockElements.length - 1, 1);
        }
        _this.changeElement();
    });
    _this.element.find(".addLine").on("click", function () {
        _this.section.blockElements.push(new simpleQuestionElement(_this.question, _this.section, _this.question.optionCount));
        _this.question.optionCount += 1;
        _this.changeElement();
    });
    _this.element.find(".inputOption").keyup(function (event) {
        console.log(event.keyCode)
        if(event.keyCode ==13){
            let val = parseInt($(event.currentTarget).val() == "" ? 0 : $(event.currentTarget).val());
            if (val == 0 || val < 3) {
                val = 3;
                $(event.currentTarget).val(val);
            }
            if (val == _this.question.optionCount) return ;
            if (val > _this.question.optionCount) {
                for (let i = _this.question.optionCount; i < val; i++) {
                    _this.section.blockElements.push(new simpleQuestionElement(_this.question, _this.section));
                }

            } else if (val < _this.question.optionCount) {
                _this.section.blockElements.splice(val, _this.question.optionCount - val);
            }
            _this.question.optionCount = val;
            _this.changeElement();
        }
    });
    _this.element.find(".inputOption").on("change", function (event) {
        let val = parseInt($(event.currentTarget).val() == "" ? 0 : $(event.currentTarget).val());
        if (val == 0 || val < 3) {
            val = 3;
            $(event.currentTarget).val(val);
        }
        if (val == _this.question.optionCount) return ;
        if (val > _this.question.optionCount) {
            for (let i = _this.question.optionCount; i < val; i++) {
                _this.section.blockElements.push(new simpleQuestionElement(_this.question, _this.section));
            }

        } else if (val < _this.question.optionCount) {
            _this.section.blockElements.splice(val, _this.question.optionCount - val);
        }
        _this.question.optionCount = val;
        _this.changeElement();
    });
};

simpleQuestionElement.prototype.initPosition = function () {
    let _this = this;
    _this.offsetTop = 0;
    _this.offsetLeft = 0;
    _this.height = _this.style.line.height;
    _this.width = _this.maxWidth;
    if (_this.lineIndex == 0) {
        _this.height += (_this.style.tools.height + _this.style.tools.margin[2]);
    }
    _this.margin = _this.style.line.margin;
};

export {simpleQuestionElement as SimpleQuestionElement};
