import {Section} from "./section.js";
import {StemBlockElement} from "../element/stemBlockElement.js";

var stemSection = function (seciton, index) {
    Section.call(this, index, "stem");
    this.section = seciton;
}

CommUtils.extendObj(stemSection, Section);

stemSection.prototype.initBlockElement = function () {
    let _section = this;
    let i = 0;

    this.section.questions.forEach(element => {
        _section.blockElements.push(new StemBlockElement(element, _section,i++));
    });
};

stemSection.prototype.createNewSection = function(index,slicedBlockElement){
    let newSection = new stemSection(this.section,index);
    return newSection;
};

export {stemSection as StemSection};
