// 答题卡书写器
import {CardPage} from "./cardPage.js";

var writer = function(card){
    this.card = card;
    this.offset = 0;
};


writer.prototype = {
    write : function(){
        let sections = this.card.sections;
        this.card.pages = [];
        let _card = this.card;
        let _w = this;
        var page = new CardPage(_card,0);
        page.init();
        _card.pages.push(page);
        let i = 0;
        let j = 1;
        while(i<sections.length){
            let section;
            if ((page,sections[i].sectionName.indexOf("examType") > -1 && (j == 1 && i > 1))) {
                section = sections[i];
            } else {
                section = this.appendToPage(page,sections[i]);
            }
            if(section != null){
                page = new CardPage(_card,j++);
                page.init();
                _card.pages.push(page);
                if(section != sections[i]) {
                    sections.splice(i+1,0,section);
                }else{ i-- }
                _w.reIndex(sections);
                this.offset = 0 ;
            }
            i++;
        }
        this.card.showAxises();
    },
    appendToPage : function(page,section){
        section.cardPage = page;
        section.answerCard = page.answerCard;
        section.init();
        let ret = section.render(this.offset);
        this.offset +=section.innerHeight() + 5;
        return ret;
    },
    /*reload : function(pageIndex,sectionIndex){
        // 重做第几个页面，从第几个section开始重绘
        let _w = this;
        let abandoned = [];
        for(let i = pageIndex;i<this.card.pages.length;i++){
            abandoned.push(this.card.pages[i]);
        }
        let i = sectionIndex;
        let sections = this.card.sections;
        let _card = this.card;
        var page = new CardPage(_card,pageIndex++);
        page.init();
        _card.pages.push(page);
        while(i<sections.length){
            let section = this.appendToPage(page,sections[i]);
            if(section != null){
                page = new CardPage(_card,pageIndex++);
                page.init();
                _card.pages.push(page);
                sections.splice(i+1,0,section);
                _w.reIndex(sections);
                this.offset = 0 ;
            }
            i++;
        }
        for(let k=0;k<abandoned.length;k++){
            abandoned[k].destroy();
        }
    },*/
    reload : function(){
        console.log("writer reload...")
        let abandoned = [];
        for(let i = 0;i<this.card.pages.length;i++){
            abandoned.push(this.card.pages[i]);
        }
        if ($(".Essay_table").length != 0) {
            $(".Essay_table").remove();
        }
        this.reUnion();
        this.write();
        
        for(let i = 0;i<abandoned.length;i++){
            abandoned[i].destroy();
        }
    },
    reUnion : function(){
        //合并拆分后的section用于重绘
        let presection = this.card.sections[0];
        for(let i=1;i<this.card.sections.length;i++){
            //
            let thissection = this.card.sections[i];
            //如果前一个名称等于当前的名称则做合并
            if(presection.sectionName == thissection.sectionName){
                for(let j=0;j<thissection.blockElements.length;j++){
                    let thisblock = thissection.blockElements[j];
                    thisblock.section = presection;
                    presection.blockElements.push(thisblock);
                }
                //reindex
                for(let j=0;j<presection.blockElements.length;j++){
                    presection.blockElements[j].index = j;
                }
                //把当前被合并的section搞掉
                this.card.sections.splice(i,1);
                //这时第i个是下一个了，所以i-- 免得漏过去了
                i--;
            }else{
                //如果当前不是一样的
                presection = this.card.sections[i];
            }
        }
    },
    reIndex : function(sections){
        for(let i=0;i<sections.length;i++){
            sections[i].index = i;
            for(let j=0;j<sections[i].blockElements.length;j++){
                sections[i].blockElements[j].index = j;
            }
        }
    }
};

export {writer as DocumentWriter};
