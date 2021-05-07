/*
* 拖拽题 公用工具
* */
var QuestionDragDrop = window.QuestionDragDrop = {
    dropImage: function(event) {
        var _this = this;
        event.preventDefault();
        var srcHtml = event.dataTransfer.getData("text/html");
        var type = $(srcHtml).attr("type");
        if (type == "answerImage") {
            var srcImgUrl = $(srcHtml).attr("src");
            var answerId = $(srcHtml).attr("answerId");
            $(event.target).attr("src", srcImgUrl);
            $(event.target).attr("answerId", answerId);
            $("#testCard li.active_li").find("div").addClass("active_li");
        }
    },
    allowDropImage: function(event) {
        event.preventDefault();
    },
    getUserAnswer: function (settings) {
        var $pParentDom = settings.questionNode.userAnswer.$answer.prevObject;
        var answerArr = [];
        var ret = false;
        $pParentDom.find(".question_content .stem.question_blank_image_area").each(function(){
            var answerid = $(this).attr("answerid");
            if (!CommUtils.isEmpty(answerid)){
                answerArr.push(answerid);
                ret = true;
            }else{
                answerArr.push(" ");
            }
        });
        return ret ? answerArr.join(",") : '';
    },
    tranToDragQuestion: function(question, isMerge) { // 拖拽题处理, 生成随机选项
        var _this = this;
        if (CommUtils.isEmpty(question) || question.type.code !== "39" ||
            CommUtils.isEmpty(question.stem.plaintext) ||
            CommUtils.isEmpty(question.answer.label)) return question;
        var blankImageMap = JSON.parse(question.stem.plaintext);
        var liTemplate = "<span style='display: inline; width: 200px; height: 100px; margin: 0px 10px 50px 10px;' " +
            "class='question_blank_image_area'>" +
            "<img style='max-width: 230px !important; max-height: 100px; border: 1px solid #42a3f5; margin-right: 20px; margin-bottom: 20px;' " +
            "type='answerImage' answerId='{0}' src='{1}'>" +
            "</span>";
        var keys = [];
        if (question.answer.label.indexOf("@") > 0) {
            keys = question.answer.label.split("@#@");
        } else if (question.answer.label.indexOf(",") > 0) {
            keys = question.answer.label.split(",");
        }
        keys.sort(function () {
            return Math.random() > 0.5 ? -1 : 1;
        });
        var richText = "";
        $.each(keys,function (index,key) {
            var blankImage = blankImageMap[key];
            richText += CommUtils.formatStr(liTemplate, key, blankImage.file.url);
        });
        if (isMerge) {
            question.stem.richText += ("<div style='margin: 25px 5px 5px 5px; width: 100%;' class='question_blank_image_option answer' data-key='" + question.questionId +"'>" + richText + "</div>");
        } else {
            question.stem.richTextOpt = ("<div style='margin: 25px 5px 5px 5px; width: 100%;' class='question_blank_image_option answer' data-key='" + question.questionId +"'>" + richText + "</div>");
        }
        return question;
    },
    textBecomeImg: function (text, fontcolor, fontsize, imgWidth, x, y){ // 使用canvas将文字转图片
        var canvas = document.createElement('canvas');
        //小于32字加1  小于60字加2  小于80字加4    小于100字加6
        var buHeight = 0;
        if(fontsize <= 32){ buHeight = 1; }
        else if(fontsize > 32 && fontsize <= 60 ){ buHeight = 2;}
        else if(fontsize > 60 && fontsize <= 80 ){ buHeight = 4;}
        else if(fontsize > 80 && fontsize <= 100 ){ buHeight = 6;}
        else if(fontsize > 100 ){ buHeight = 10;}
        //对于g j 等有时会有遮挡，这里增加一些高度
        canvas.height=fontsize + buHeight ;
        var context = canvas.getContext('2d');
        // 擦除(0,0)位置大小为200x200的矩形，擦除的意思是把该区域变为透明
        context.clearRect(0, 0, canvas.width, canvas.height);
        context.fillStyle = fontcolor;
        context.font=fontsize+"px Arial";
        //top（顶部对齐） hanging（悬挂） middle（中间对齐） bottom（底部对齐） alphabetic是默认值
        context.textBaseline = 'middle';
        context.fillText(text,0,fontsize/2)

        //如果在这里直接设置宽度和高度会造成内容丢失 , 暂时未找到原因 , 可以用以下方案临时解决
        //canvas.width = context.measureText(text).width;


        //方案一：可以先复制内容  然后设置宽度 最后再黏贴
        //方案二：创建新的canvas,把旧的canvas内容黏贴过去
        //方案三： 上边设置完宽度后，再设置一遍文字

        //方案一： 这个经过测试有问题，字体变大后，显示不全,原因是canvas默认的宽度不够，
        //如果一开始就给canvas一个很大的宽度的话，这个是可以的。
        //var imgData = context.getImageData(0,0,canvas.width,canvas.height);  //这里先复制原来的canvas里的内容
        //canvas.width = context.measureText(text).width;  //然后设置宽和高
        //context.putImageData(imgData,0,0); //最后黏贴复制的内容

        //方案三：改变大小后，重新设置一次文字
        if (imgWidth) {
            canvas.width = imgWidth;
        } else {
            canvas.width = context.measureText(text).width;
        }
        context.fillStyle = fontcolor;
        context.font=fontsize+"px Arial";
        context.textBaseline = 'middle';
        if (x && y) {
            context.fillText(text,x,y)
        } else {
            context.fillText(text,0,fontsize/2)
        }
        var dataUrl = canvas.toDataURL('image/png');//注意这里背景透明的话，需要使用png
        return dataUrl;
    },
}