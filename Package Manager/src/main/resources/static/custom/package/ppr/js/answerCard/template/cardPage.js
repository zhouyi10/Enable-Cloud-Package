
var cardPage = function (answerCard, index) {
    this.index = index;
    this.answerCard = answerCard;
    this.cardContainer = answerCard.container;
    this.pageContainer;
    this.pageObject;
    this.width = 210;
    this.height = 971.3;
    this.containerWidth = 180;
    this.containerHeight = 257;
    this.sections = [];
    this.prePage;
    this.nextPage;
    this.styleConfig = answerCard.config.style.answerCard;
};

cardPage.prototype = {
    init: function () {
        this.pageObject = $(this.createCardContainer()).appendTo(this.cardContainer)[0];
        this.pageContainer = $(this.createPageContainer()).appendTo(this.pageObject)[0];
    },
    destroy: function () {
        this.pageObject.remove();
    },
    createCardContainer: function () {
        let top = this.index * this.styleConfig.height + this.styleConfig.top + (this.index - 1) * this.styleConfig.top;
        let htmlTempStr = "<div class='cardContainer' style='top:{0}px;width:{1}px;height:{2}px;position: absolute;background-color: white;'>{3}{4}</div>";
        return CommUtils.formatStr(htmlTempStr, top, this.styleConfig.width, this.styleConfig.height, this.createPageIndex(), this.createPageAnChors());
    },
    createPageContainer: function () {
        return CommUtils.formatStr("<div class='pageContainer' style='top:{0}px;left:{1}px;width:{2}px;height:{3}px;position: absolute;'>",
            this.styleConfig.pageContainer.top,
            this.styleConfig.pageContainer.left,
            this.styleConfig.pageContainer.width,
            this.styleConfig.pageContainer.height
            );
    },
    createPageIndex: function () {
        let indexHtml = CommUtils.formatStr("<div class='pageIndexGroup' style='position:absolute; left:{0}px; top:{1}px;'>",
            this.styleConfig.pageIndexGroup.left,
            this.styleConfig.pageIndexGroup.top);
        let style = CommUtils.formatStr("width:{0}px; height:{1}px; margin:{2}px; float: left; background: black;",
            this.styleConfig.pageIndexGroup.pageIndex.width,
            this.styleConfig.pageIndexGroup.pageIndex.height,
            this.styleConfig.pageIndexGroup.pageIndex.margin.join("px "),
            );
        for (let i = 0; i <= this.index; i++) {
            indexHtml += ("<div class='pageIndex' style='" + style + "'></div>");
        }
        return indexHtml + "</div>"
    },
    createPageAnChors: function () {
        let style = CommUtils.formatStr("width:{0}px;height:{1}px;border-radius:{2}px;position: absolute;background: black;",
            this.styleConfig.pageAnChor.width,
            this.styleConfig.pageAnChor.height,
            this.styleConfig.pageAnChor.borderRadius
            );
        return CommUtils.formatStr("<div class='anchorPoint top-left' style='{0}top:{1}px;left:{4}px;'></div>" +
            "<div class='anchorPoint top-left' style='{0}top:{1}px;right:{2}px;'></div>" +
            "<div class='anchorPoint bottoom-right' style='{0}bottom:{3}px;left:{4}px;'></div>" +
            "<div class='anchorPoint bottoom-left' style='{0}bottom:{3}px; right:{2}px;'></div>",
            style,
            this.styleConfig.pageAnChor.top,
            this.styleConfig.pageAnChor.right,
            this.styleConfig.pageAnChor.bottom,
            this.styleConfig.pageAnChor.left,
        );
    }
};

export {cardPage as CardPage};
