/*
* Drag and drop questions
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
        }
    },
    allowDropImage: function(event) {
        event.preventDefault();
    },
    getUserAnswer: function (settings) {
        var $this = settings.questionNode.userAnswer.$answer;
        var questionArea = $($this).parent().parent();
        var answerArr = [];
        var ret = false;
        questionArea.find(".question_content img.question_blank_image_area").each(function(){
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
    tranToDragQuestion: function(question, isMerge) { // Drag and drop question processing, generate random options
        var _this = this;
        if (CommUtils.isEmpty(question) || question.type.code !== "39" ||
            CommUtils.isEmpty(question.stem.plaintext) ||
            CommUtils.isEmpty(question.answer.lable)) return question;
        var blankImageMap = JSON.parse(question.stem.plaintext);
        var liTemplate = "<span style='display: inline; width: 200px; height: 100px; margin: 0px 10px 50px 10px;' " +
            "class='question_blank_image_area'>" +
            "<img style='max-width: 230px !important; max-height: 100px; border: 1px solid #42a3f5; margin-right: 20px; margin-bottom: 20px;' " +
            "type='answerImage' answerId='{0}' src='{1}'>" +
            "</span>";
        var keys = [];
        if (question.answer.lable.indexOf("@") > 0) {
            keys = question.answer.lable.split("@#@");
        } else if (question.answer.lable.indexOf(",") > 0) {
            keys = question.answer.lable.split(",");
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
            question.stem.richText += ("<div style='margin: 5px 5px 5px 5px; width: 100%;' class='question_blank_image_option' data-key='" + question.questionId +"'>" + richText + "</div>");
        } else {
            question.stem.richTextOpt = ("<div style='margin: 5px 5px 5px 5px; width: 100%;' class='question_blank_image_option' data-key='" + question.questionId +"'>" + richText + "</div>");
        }
        return question;
    },
    textBecomeImg: function (text, fontcolor, fontsize, imgWidth, x, y){ // use canvas to convert text to pictures
        var canvas = document.createElement('canvas');
        //Less than 32 words plus 1, less than 60 words plus 2, less than 80 words plus 4, less than 100 words plus 6
        var buHeight = 0;
        if(fontsize <= 32){ buHeight = 1; }
        else if(fontsize > 32 && fontsize <= 60 ){ buHeight = 2;}
        else if(fontsize > 60 && fontsize <= 80 ){ buHeight = 4;}
        else if(fontsize > 80 && fontsize <= 100 ){ buHeight = 6;}
        else if(fontsize > 100 ){ buHeight = 10;}
        //For g j etc. sometimes there will be occlusion, here add some height
        canvas.height=fontsize + buHeight ;
        var context = canvas.getContext('2d');
        //Erase the rectangle with the position size of (0,0) 200 x 200, erase means to make the area transparent
        context.clearRect(0, 0, canvas.width, canvas.height);
        context.fillStyle = fontcolor;
        context.font=fontsize+"px Arial";
        //top (top alignment) hanging (middle alignment) bottom (bottom alignment) alphabetic is the default
        context.textBaseline = 'middle';
        context.fillText(text,0,fontsize/2)

        //If the width and height are set directly here, the content will be lost, and the cause has not been found for the time being, you can temporarily solve it with the following scheme
        //canvas.width = context.measureText(text).width;


        //Option 1: You can copy the content first, then set the width, and then paste
        //Option 2: Create a new canvas and paste the old canvas content
        //Option 3: After setting the width on the top, set the text again

        //Solution 1: After testing, there is a problem. After the font becomes larger, the display is not complete. The reason is that the default width of the canvas is not enough.
        //If you give the canvas a large width at the beginning, this is fine.
        //var imgData = context.getImageData(0,0,canvas.width,canvas.height);  first copy the content in the original canvas
        //canvas.width = context.measureText(text).width;  then set the width and height
        //context.putImageData(imgData,0,0); //paste the copied content last

        //Option 3: After changing the size, reset the text once
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
        var dataUrl = canvas.toDataURL('image/png');//Note that if the background is transparent, you need to use png
        return dataUrl;
    },
}