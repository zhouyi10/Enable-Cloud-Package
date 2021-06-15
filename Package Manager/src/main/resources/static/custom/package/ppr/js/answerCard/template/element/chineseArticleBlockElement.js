import {BlockElement} from "./blockElement.js";

var chineseArticleBlockElement = function(question,section,index,flag,wordNumber){
    BlockElement.call(this,section,index, question);
    this.elementObject;
    this.flag=flag;
    this.wordNumber = wordNumber;
    this.style = section.answerCard.config.style.element.chineseArticle;
    /*if(flag){
        this.margin = this.style.words.margin;
    }else{
        this.margin = this.style.cells.margin;
    }*/
    this.margin = [10,0,0,0];



};

CommUtils.extendObj(chineseArticleBlockElement,BlockElement);

chineseArticleBlockElement.prototype.buildHtml = function(container,index){
    let _thisElement = this;
    if (_thisElement.flag) {
        let wordsConfigHtml = CommUtils.formatStr(' <div class="chinese-article-content" style="margin: {0}px">',this.style.words.margin.join("px ")) +
            '        <div class="title-number">' + _thisElement.question.externalNo + '.</div>\n' +
            '        <div class="chinese-article-words no-print">\n' +
            '            <span>字数</span>\n' +
            '            <span class="chinese-article-words-active">500</span>\n' +
            '            <span>800</span>\n' +
            '            <span>1000</span>\n' +
            '            <input class="article-words-input" placeholder="自定义"/>\n' +
            '        </div>\n' +
            '    </div>';
        let articleConfigDom = $(wordsConfigHtml);
        if (null != this.wordNumber) {
            articleConfigDom.find(".chinese-article-words span").removeClass("chinese-article-words-active");
            if (this.wordNumber == 500) {
                articleConfigDom.find(".chinese-article-words").find("span").eq(1).addClass("chinese-article-words-active");
            }else if (this.wordNumber == 800) {
                articleConfigDom.find(".chinese-article-words").find("span").eq(2).addClass("chinese-article-words-active");
            }else if (this.wordNumber == 1000) {
                articleConfigDom.find(".chinese-article-words").find("span").eq(3).addClass("chinese-article-words-active");
            }
            articleConfigDom.find(".chinese-article-words").find(".article-words-input").attr("value",this.wordNumber);
        }else{
            articleConfigDom.find(".chinese-article-words").find(".article-words-input").attr("value",500);
        }
        this.elementObject = articleConfigDom.appendTo(container);
        this.element = this.elementObject;
        this.element.find(".chinese-article-words").find("span:not(:first-child)").click(function(){
            var words = 　parseInt($(this).text());
            var inputValue = $(this).siblings(".article-words-input").attr("value");
            if (words == inputValue) {
                return ;
            }
            _thisElement.section.appendChineseArticle(_thisElement.question,_thisElement.section,words);
            _thisElement.changeElement();
        });

        this.element.find(".article-words-input").change(function (){
            let inputValue =  $(this).val();
            if (!(/^[1-9][0-9]*$/.test(inputValue))) {
                $(".article-words-input").tip("必须为数字!", 'top', 1000);
            }
            inputValue = Math.ceil(inputValue/23) * 23;
            let preInputValue = (inputValue + "").substr(0,(inputValue + "").length - 1);
            $(this).val(parseInt(preInputValue + "0"));
            inputValue = $(this).val()
            if (inputValue == 500) {
                $(this).prev().prev().prev().click();
            }else if (inputValue == 800) {
                $(this).prev().prev().click();
            }else if (inputValue == 1000) {
                $(this).prev().click();
            }
            _thisElement.section.appendChineseArticle(_thisElement.question,_thisElement.section,inputValue);
        });

        this.element.find(".article-words-input").bind('keypress',function(event){
            if(event.keyCode == "13") {
               $(this).blur();
            }

        })

    }else{
        let html =  CommUtils.formatStr(' <table border="1" id="essay-table-' + ($(".Essay_table").length + 1) + '" class="Essay_table" style="margin: {0}px">',this.style.cells.margin.join("px ")) +
            '                            <tr>\n' +
            '                                <td></td><td></td><td></td><td></td><td></td>\n' +
            '                                <td></td><td></td><td></td><td></td><td></td>\n' +
            '                                <td></td><td></td><td></td><td></td><td></td>\n' +
            '                                <td></td><td></td><td></td><td></td><td></td>\n' +
            '                                <td></td><td></td><td></td>\n' +
            '                            </tr>\n' +
            '                        </table>'+'<span class="article-sign-words"></span>';
        this.elementObject = $(html).appendTo(container);
        this.element = this.elementObject;
            if ($(".Essay_table").length >= 5) {
                var firstCellNumber = ($(".Essay_table").length - 1) * 23 + 1;
                var lastCellNumber = $(".Essay_table").length * 23;
                if ((lastCellNumber +"").length == 3) {
                    var hundredsDigit = (lastCellNumber +"").substr(0,1);
                    if (firstCellNumber <= parseInt(hundredsDigit) * 100 && parseInt(hundredsDigit) * 100 <= lastCellNumber) {
                        addWordsSign(firstCellNumber,hundredsDigit);
                    }
                }else if ((lastCellNumber +"").length == 4) {
                    var thousandsDigit = (lastCellNumber +"").substr(0,1);
                    var hundredsDigit = (lastCellNumber +"").substr(1,1);
                    if (firstCellNumber <= parseInt(thousandsDigit) * 1000 + parseInt(hundredsDigit) * 100 && parseInt(thousandsDigit) * 1000 + parseInt(hundredsDigit) * 100 <= lastCellNumber) {
                        addWordsSign(firstCellNumber,hundredsDigit,thousandsDigit);
                    }
                }
            }


    }
};

function addWordsSign(firstCellNumber,hundredsDigit,thousandsDigit) {
    var articleWordsDom = $("#essay-table-" + $(".Essay_table").length).siblings(".chinese-article-content");
    $("#essay-table-" + $(".Essay_table").length).next().css({"font-size":"8px","transform":"scale(0.7)","position":"absolute","display":"block"});
    if (articleWordsDom.length == 1) {
        var topOffset = (53 + 43 * $(".Essay_table").length -14) + "px";
        $("#essay-table-" + $(".Essay_table").length).next().css("top",topOffset);
    }else{
        var signWordsId = parseInt($("#essay-table-" + $(".Essay_table").length).attr("id").substr(12));
        var firstTableId = parseInt($("#essay-table-" + $(".Essay_table").length).parent().children(":first").attr("id").substr(12));
        var topOffset = ((signWordsId - firstTableId + 1) * 43 - 2) + "px";
        $("#essay-table-" + $(".Essay_table").length).next().css("top",topOffset);
    }
    let leftOffset = 0;
    if (thousandsDigit == undefined) {
        leftOffset = (parseInt(hundredsDigit) * 100 - firstCellNumber) * 29.2 +1;
        $("#essay-table-" + $(".Essay_table").length).next().text(parseInt(hundredsDigit) * 100 + "字");
    }else{
        leftOffset = ((parseInt(thousandsDigit) * 1000 + parseInt(hundredsDigit) * 100) - firstCellNumber) * 29.2 +1;
        $("#essay-table-" + $(".Essay_table").length).next().text((parseInt(thousandsDigit) * 1000 + parseInt(hundredsDigit) * 100) + "字");
    }
    $("#essay-table-" + $(".Essay_table").length).next().css("left",leftOffset);
}

chineseArticleBlockElement.prototype.initPosition = function () {
    let offsetTop = this.margin[0];
    let offsetLeft = this.margin[3];
    if(this.flag){
        this.width = this.style.words.width;
        this.height = this.style.words.height;
    }else{
        this.width = this.style.cells.width;
        this.height = this.style.cells.height;
    }
    this.offsetTop = offsetTop;
    this.offsetLeft = offsetLeft;
}

export {chineseArticleBlockElement as ChineseArticleBlockElement};
