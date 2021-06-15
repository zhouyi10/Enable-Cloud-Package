// 元素块基类

import {DocumentWriter} from "../documentWriter.js";

var blockElement = function (section, index, question) {
    this.element;//元素块的jquery对象
    this.section = section;//section视图模型对象
    this.index = index;//索引
    this.editAnswer = section.answerCard.config.editAnswer;
    this.margin = [0, 0, 0, 0];
    this.question = question;
}

blockElement.prototype = {
    init: function () { // render 前的初始化工作
        this.width = 0;
        this.height = 0;
        this.offsetTop = 0;
        this.offsetLeft = 0;
        this.isBr = false; // 强制换行
    },
    // element 渲染流程控制
    render: function (container, index) {
        this.buildHtml(container, index);
        this.buildPosition();
    },
    clear: function () {
        this.element.remove();
    },
    // 空build方法，丢给子类实现
    buildHtml: function (container, index) {
    },
    // 需要子类实现, 计算出当前Element的相对位置和大小, 只需要计算宽高, 相对位置的坐标都是固定的.
    initPosition: function () {
    },
    // 计算当前Element真实的坐标和大小, 并更新当前section中的element的最大高度,最大宽度和最后一个element(当前element)的真实位置信息
    buildPosition: function () {
        let containerWidth = this.section.answerCard.config.style.answerCard.pageContainer.width;
        this.initPosition();
        let lastElementPosition = this.section.lastElementPosition;
        if (lastElementPosition == undefined || lastElementPosition.width == undefined) {
            this.section.lastElementPosition = lastElementPosition = {
                width: this.width,
                height: this.height,
                offsetTop: this.offsetTop,
                offsetLeft: this.offsetLeft,
                isBr: this.isBr
            }
        } else {
            if (((lastElementPosition.offsetLeft + lastElementPosition.width + this.margin[1] +
                this.offsetLeft + this.width + this.margin[1]) > containerWidth) || lastElementPosition.isBr) {
                this.offsetTop = lastElementPosition.offsetTop = (lastElementPosition.offsetTop + lastElementPosition.height + this.margin[2] + this.margin[0]);
                if (lastElementPosition.isBr) {
                    this.offsetLeft = lastElementPosition.offsetLeft = this.offsetLeft;
                } else {
                    this.offsetLeft = lastElementPosition.offsetLeft = this.margin[3];
                }
                lastElementPosition.width = this.width;
                lastElementPosition.height = this.height;
            } else {
                this.offsetTop = lastElementPosition.offsetTop;
                lastElementPosition.offsetLeft = this.offsetLeft = (lastElementPosition.offsetLeft + lastElementPosition.width + this.margin[1] + this.margin[3]);
                lastElementPosition.width = this.width;
                lastElementPosition.height = this.height;
            }
        }
        lastElementPosition.isBr = this.isBr
    },
    getAxises: function () {
        let axise = {
            offsetTop: this.section.offsetTop + this.offsetTop,
            offsetLeft: this.offsetLeft,
            width: this.width,
            height: this.height
        };
        return [axise];
    },
    buiildAxises: function () {
        let axises = this.getAxises();
        if (this.section.answerCard.config.showAxises) this.drawAxises(axises);
        return axises
    },
    drawAxises: function (axises) {
        let domName = "drawAxises_" + this.section.index + "_" + this.index
        $(this.section.cardPage.pageContainer).find("." + domName).remove();
        let htmlTemp = "<div class='no-print " + domName + "'>";
        let divTemp = "<div style='top:{0}px;left:{1}px;width:{2}px;height:{3}px;position: absolute;border: 1px red solid;'></div>"
        for (let axise of axises) {
            htmlTemp += CommUtils.formatStr(divTemp, axise.offsetTop, axise.offsetLeft, axise.width, axise.height);
        }
        htmlTemp += "</div>"
        $(htmlTemp).appendTo(this.section.cardPage.pageContainer)
    },
    changeElement: function (data) {
        this.changeData(data);
        this.section.answerCard.changeAnswerCard();
    },
    changeData: function (data){
        let writer = new DocumentWriter(this.section.answerCard);
        writer.reload();
    }, // 修改数据
};

export {blockElement as BlockElement};
