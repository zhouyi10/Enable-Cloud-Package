// section抽象基类
import {DocumentWriter} from "../documentWriter.js";

var section = function (index, sectionName, style) {
    this.index = index;
    this.width = 0;
    this.height = 0;
    this.top = 0;
    this.offsetTop = 0;
    this.sectionContainer;
    this.sectionName = sectionName+"Section_"+index;
    this.cardPage;
    this.answerCard;
    this.blockElements = [];
    this.style = (style == undefined ? "" : style);
    this.lastElementPosition = {}
};

section.prototype = {
    init: function () { // render 前的初始化工作
        this.lastElementPosition = {};
    },
    hitTest: function () {
        // 对当前的section进行页面底部的碰撞检测
        let containerHeight = this.sectionContainer.offsetHeight;
        let currentHeight = this.offsetTop + containerHeight;
        if (currentHeight > this.cardPage.height) return true;
        return false;
    },
    render: function (offset) {
        // 对当前section做渲染，将element逐个放入，做碰撞检测，如果发生碰撞则进行切分
        this.offsetTop = offset;
        let _section = this;
        let sliceSection = null;
        this.newSection(offset);
        for (let i = 0; i < this.blockElements.length; i++) {
            let ele = this.blockElements[i];
            ele.init();
            //_section.sectionContainer.append(ele.render());
            ele.render(_section.sectionContainer);
            if (_section.hitTest()) {
                ele.clear();
                if (i == 0) {
                    // 如果是第一个元素就触发分裂，则整个section搞掉
                    this.clear();
                }
                sliceSection = _section.slice(i);
                break;
            }
        }
        return sliceSection;
    },
    newSection: function (offset) {
        let div = $(CommUtils.formatStr("<div class='{0} section' style='position:absolute;left:0px;top:{1}px;{2}'>",
            this.sectionName,
            offset,
            this.style
        )).appendTo(this.cardPage.pageContainer);
        this.sectionContainer = div[0];
    },
    initBlockElement: function () {
    },
    append: function (element, i) {
        // 插入新的元素的时候使用section的append方法，做碰撞检测，如果发生碰撞则进行切分
        this.blockElements.splice(i + 1, 0, element);
        // reindex
        for (let j = 0; j < this.blockElements.length; j++) {
            this.blockElements[j].index = j;
        }

        /*
        //
        element.render(this.sectionContainer,i);
        //碰撞检测并拆分，逐个拿掉，然后拆分
        if(this.hitTest()){
            let sliceSection = this.trySlice();
            this.answerCard.sections.splice(this.index+1,0,sliceSection);
            let writer = new DocumentWriter(this.answerCard);
            writer.reIndex(this.answerCard.sections);
            writer.reload(this.cardPage.index+1,sliceSection.index);
        }
        */

        let writer = new DocumentWriter(this.answerCard);
        writer.reload();
    },
    trySlice: function () {
        let sliceds = [];
        // 重后往前一个一个减，直到不发生碰撞
        let i = this.blockElements.length - 1;
        let j = 0;
        for (; i >= 0; i--, j++) {
            if (this.hitTest()) {
                //总是加到最后面
                this.blockElements[i].clear();
                sliceds.splice(sliceds.length, 0, this.blockElements[i]);
                break;
            }
        }
        //移走的block都搞掉
        for (; j >= 0; j--) {
            this.blockElements.pop();
        }
        let newsection = this.createNewSection(this.index + 1, sliceds[0]);
        // 移除的block一个一个都加到新section里面
        for (let k = 0; k < sliceds.length; k++) {
            newsection.blockElements.push(sliceds[k]);

        }
        //newsection.blockElements = sliceds;
        return newsection;
    },
    innerHeight: function () {
        let height = this.sectionContainer.offsetHeight;
        return height;
    },
    remove: function (index) {
        this.blockElements.splice(index, 1);
        // reindex
        let writer = new DocumentWriter(this.answerCard);
        writer.reload();
    },
    clear: function () {
        $(this.sectionContainer).remove();
        //this.answerCard.sections.splice(this.index,1);
    },
    slice: function (i) {
        // 如果是普通的section就不做分裂，返回自己，直接整个挪到下一页
        /*let newSection = new section();
        for(let j=i;j<this.blockElements.length;j++){
            newSection.blockElements.push(this.blockElements[j]);
        }
        return newSection;*/
        return this;
    },
    relocation: function () {
        // 新增或者删除元素后连锁触发每个section的重新定位，重新定位会逐个hittest并有可能触发slice

    },
    createNewSection: function (index) {
        return new section(index);
    }
};

export {section as Section};
