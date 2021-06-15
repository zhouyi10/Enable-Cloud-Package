// 纯文本元素块
import {BlockElement} from "./blockElement.js";

var textBlockElement = function (section, text, textStyle) {
    BlockElement.call(this, section);
    this.text = text;
    this.textStyle = (textStyle == undefined ? "" : textStyle);
    this.elementObject;
};

CommUtils.extendObj(textBlockElement, BlockElement);

textBlockElement.prototype.buildHtml = function (container) {
    let html = CommUtils.isNotEmpty(this.textStyle) ? CommUtils.formatStr("<div {0}></div>", "style='" + this.textStyle + "'") : "<div></div>";
    this.elementObject = $(html).appendTo(container);
    this.elementObject.html(this.text);
    this.element = this.elementObject;
};


export {textBlockElement as TextBlockElement};
