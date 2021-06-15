/**
 * Examination Information Components
 * Prevent {{}} splash screen of vue, put all operations into the component
 */
"use strict";
Vue.config.devtools = true;
var VueConfig = {
    data : {
        'paper' : {},
        'paperTemplate': {},
        'editType' : '_DEFAULT',
        'changingNodeSearchCode' : ''
    },
    mounted: function () {
        this.refreshContentEditDom();
    },
    computed : {
        canSave : function(scroll) {
            var _this = this;
            return function (scroll) {
                if (CommUtils.isEmpty(_this.paper.stageCode) || CommUtils.isEmpty(_this.paper.gradeCode) || CommUtils.isEmpty(_this.paper.subjectCode)) {
                    scroll && _this.validTip(null, i18n['paper_base_info_empty']); // The basic information of the test paper cannot be empty!
                    return false;
                }
                var flag = !CommUtils.isEmpty(_this.paper.nodes);
                $.each(_this.paper.nodes, function(k, kNodeTemp) {
                    var kNode = kNodeTemp;
                    if (CommUtils.isEmpty(kNode.children)) {
                        flag = false;
                        scroll && _this.validTip(kNode.nodeSearchCode, CommUtils.formatStr(i18n['kind_empty'], kNode.name)); //Volume [{0}] cannot be empty
                        return false;
                    }
                    $.each(kNode.children, function (t, tNodeTemp) {
                        var tNode = tNodeTemp;
                        if (CommUtils.isEmpty(tNode.children)) {
                            flag = false;
                            scroll && _this.validTip(tNode.nodeSearchCode, CommUtils.formatStr(i18n['paper_ques_type_empty'], kNode.name, tNode.name)); //Question type [{0}] cannot be empty
                            return false;
                        }
                        $.each(tNode.children, function(q, qNodeTemp) {
                            var qNode = qNodeTemp;
                            if(CommUtils.isEmpty(qNode.question) || !CommUtils.isEmpty(qNode.question) && qNode.question.isTempQuestion) {
                                flag = false;
                                scroll && _this.validTip(qNode.nodeSearchCode, CommUtils.formatStr(i18n['ques_empty'], kNode.name, tNode.name, qNode.externalNo)); // Title cannot be empty
                                return false;
                            }

                            if (CommUtils.isEmpty(qNode.children)) {
                                if (CommUtils.isEmpty(qNode.points) || qNode.points == 0) {
                                    flag = false;
                                    scroll && _this.validTip(qNode.nodeSearchCode, CommUtils.formatStr(i18n['ques_points_empty'], kNode.name, tNode.name, qNode.externalNo)); //Title {0} score cannot be 0
                                    return false;
                                }
                            } else {
                                $.each(qNode.children, function(c, cNode) {
                                    if (CommUtils.isEmpty(cNode.points) || cNode.points == 0) {
                                        flag = false;
                                        scroll && _this.validTip(cNode.nodeSearchCode, CommUtils.formatStr(i18n['ques_child_points_empty'], kNode.name, tNode.name, qNode.externalNo, cNode.externalNo)); //Title {0} score cannot be 0
                                        return false;
                                    }
                                });
                            }
                            if (!flag) return false;
                        });
                        if (!flag) return false;
                    });
                    if (!flag) return false;
                });
                return flag;
            };
        },
        filterNode : function(nodeSearchCode) {
            var _this = this;
            return function(nodeSearchCode) {
                nodeSearchCode += '_';
                _this.node.filter(function(item) {
                    if (item.nodeSearchCode.startsWith(nodeSearchCode)) {
                        return item;
                    }
                })
            };
        },
        questionCount : function() { //Number of questions
            var quesCount = 0;
            $.each(this.paper.nodes, function(k, kNode) {
                if (CommUtils.isEmpty(kNode.children)) {
                    return true;
                }
                $.each(kNode.children, function(t, tNode) {
                    if (CommUtils.isEmpty(tNode.children)) {
                        return true;
                    }
                    quesCount += tNode.children.length;
                });
            });
            return quesCount;
        }
    },
    methods : {
        viewKnowledgeStatistics : function() {
            var url = EditPaperPage.viewKnowledgeStatisticsUrl+"?subjectCode=" + this.paper.subjectCode;
            this.frame(i18n['view_knowledge_statistics'], url, '500px', '650px');
        },
        viewAnswerCard : function() {
            if (!this.paper.isSaved) {
                this.tipBox(i18n['save_paper_first']);
                return;
            }
            var url = EditPaperPage._contextPathprr+'/answercard/preedit?paperId='+this.paper.paperId+'&userId='+ this.paper.userId;
            this.frame(i18n['answer_card'], url, '92%', '96%');

        },
        previewPaper : function(event,value) {
           /* if (!CommUtils.tryLock(event.target)) {
                return;
            }*/
            //console.log("进")
            var _this = this;
            /*if (_this.paper.isSaved && CommUtils.isEmpty(value)) {
                _this.tipBox(i18n['repeat_save_paper']);
                CommUtils.unLock(event.target);
                return;
            }*/
            if (_this.editType == '_PROFESSIONAL'){
                var isRight = true;
                var errorQuestion = new Array();
                $.each(_this.paper.nodes, function(index1, kNode){
                    $.each(kNode.children, function(index2, tNode){
                        $.each(tNode.children, function(index3, qNode){
                            if (parseFloat(qNode.points) != parseFloat(qNode.question.points)){
                                errorQuestion.push({"type": tNode.name, "qNo": qNode.externalNo});
                                isRight = false;
                                $("#" + qNode.nodeSearchCode).parent().css("border", "1px solid red");
                            }
                        });
                    });
                });
                if (!isRight) {
                    var tips = i18n['question_point_not_matching_error'];
                    var error = '';
                    $.each(errorQuestion, function(x, xobj){
                        if (error.indexOf(xobj.type) > -1) return true;
                        error += error == ''? xobj.type + "[": "," + xobj.type+ "["
                        $.each(errorQuestion, function(y, yobj){
                            error+= y == 0 ? yobj.qNo : ","+yobj.qNo;
                        });
                        error += "]";
                    });
                    layer.alert(tips.replace('{0}', error));
                    CommUtils.unLock(event.target);
                    return;
                }
            }
            var canSave = _this.canSave(true);
            /*if (!canSave) {
                CommUtils.unLock(event.target);
                return ;
            }*/
            var loading= layer.load(0, {shade: [0.5, '#000'], anim: -1});
            var paper = JSON.parse(JSON.stringify(this.paper));
            paper.nodes = _this.buildNodes(paper.nodes);

            var nodeLength = paper.nodes.length;
            var nodeLast = paper.nodes[nodeLength-1];
            while (1) {
                if (nodeLast.level === 3 && nodeLast.question && nodeLast.question.knowledgeList && nodeLast.question.knowledgeList.length > 0 && nodeLast.question.knowledgeList[0].materialVersion) {
                    paper.materialVersion = nodeLast.question.knowledgeList[0].materialVersion;
                    break;
                } else if (nodeLength === 0) {
                    break
                } else {
                    nodeLength = nodeLength-1;
                    nodeLast = paper.nodes[nodeLength];
                }
            }
            if(CommUtils.isNotEmpty(EditPaperPage.materialVersion)) {
                paper.materialVersion = EditPaperPage.materialVersion;
            }

                //url = EditPaperPage.savePaperUrl+"?saveType="+value;

            var paperNewQuestion = EditPaperPage.getPaperNewQuestion();
            if (CommUtils.isNotEmpty(paperNewQuestion)){
                $.post(EditPaperPage.increaseUsedNumberUrl, {"questionId" : paperNewQuestion}, function(){

                });
            }
            var table=document.getElementById("rscTable");
            var sumBigtopic = table.rows[0].cells.length-2;
            var mattersAttention =  $(".mattersAttention").text();
            var secrecySymbol =  $("#secrecySymbol").text();
            var subtitle =  $(".subtitle").text();
            var testInfo =  $(".testInfo").text();
            var stuInfoColumn =  $(".stuInfoColumn").text();
            var recordScoreColumn =  $(".recordScoreColumn").text();
            var examStypeinfoPO = {};
            examStypeinfoPO.mattersAttention=mattersAttention;
            examStypeinfoPO.secrecySymbol=secrecySymbol;
            examStypeinfoPO.subtitle=subtitle;
            examStypeinfoPO.testInfo=testInfo;
            examStypeinfoPO.stuInfoColumn=stuInfoColumn;
            examStypeinfoPO.recordScoreColumn=recordScoreColumn;
            paper.examStypeinfoPO=JSON.stringify(examStypeinfoPO);
            paper.sumBigtopic=sumBigtopic;
            //var url = CommUtils.formatStr(EditPaperPage.previewPaperUrl);
            var jsonData = JSON.stringify(paper);
            var paperStr = encodeURIComponent(jsonData);
            var url = EditPaperPage._contextPathprr;
            layer.open({
                type: 2,//弹出框类型
                title: i18n['preview'],
                shadeClose: false, //点击遮罩关闭层
                scrollbar: false,//屏蔽浏览器滚动条
                area: ["96%", "92%"],
                shift:0.6,
                content: url+'/previewPaper?paperStr='+paperStr,
                success: function(layero, index){
                },
                cancel: function(){
                    layer.closeAll();
                }

            });
           // this.frame(i18n['preview'], url+'/previewPaper?paperStr='+paperStr, '92%', '96%');
        },
        viewPaper : function() {
            if (!this.paper.isSaved) {
                this.tipBox(i18n['save_paper_first']);
                return;
            }
            var url = CommUtils.formatStr(EditPaperPage.viewPaperUrl, this.paper.paperId);
           // console.log(url);
            this.frame(i18n['preview'], url, '92%', '96%');
        },
        editBaseInfo : function(event) {

            var url = EditPaperPage.editBaseInfoUrl;
            var paper = JSON.parse(JSON.stringify(this.paper));
            //console.log(paper.userId);
            if(CommUtils.isNotEmpty(paper.knowledges)) {
                var knowledgeIds = [];
                $.each(paper.knowledges,function (index, obj) {
                    knowledgeIds.push(obj.knowledgeId);
                });
            }
            if(CommUtils.isNotEmpty(paper.paperId) && CommUtils.isEmpty(EditPaperPage.materialVersion)) {
                //console.log(paper);
                //console.log("stagecode"+this.paper.stageCode);
                url += "?stageCode=" + this.ifEmpty(this.paper.stageCode) + "&gradeCode=" + this.ifEmpty(this.paper.gradeCode) + "&subjectCode=" + this.ifEmpty(this.paper.subjectCode) +
                    "&name=" + this.ifEmpty(this.paper.name) + "&materialVersion=" + this.ifEmpty(paper.materialVersion) + "&searchCodes=" + this.ifEmpty(knowledgeIds);
            } else {
                url += "?stageCode=" + this.ifEmpty(this.paper.stageCode) + "&gradeCode=" + this.ifEmpty(this.paper.gradeCode) + "&subjectCode=" + this.ifEmpty(this.paper.subjectCode) +
                    "&name=" + this.ifEmpty(this.paper.name) + "&materialVersion=" + this.ifEmpty(EditPaperPage.materialVersion) + "&searchCodes=" + this.ifEmpty(knowledgeIds);
            }
            //console.log(url);
            url += url.indexOf("?") > -1 ? '&userId=' + paper.userId : "?userId=" + paper.userId;
            //console.log(url);
            this.frame(i18n['paper_info'], url, '600px', '550px');
        },
        editBasetop : function(event) {
            var mattersAttention =  $(".mattersAttention").text();
            var secrecySymbol =  $("#secrecySymbol").text();
            var subtitle =  $(".subtitle").text();
            var testInfo =  $(".testInfo").text();
            var stuInfoColumn =  $(".stuInfoColumn").text();
            var recordScoreColumn =  $(".recordScoreColumn").text();
            var examStypeinfoPO = {};
            examStypeinfoPO.mattersAttention=mattersAttention;
            examStypeinfoPO.secrecySymbol=secrecySymbol;
            examStypeinfoPO.subtitle=subtitle;
            examStypeinfoPO.testInfo=testInfo;
            examStypeinfoPO.stuInfoColumn=stuInfoColumn;
            examStypeinfoPO.recordScoreColumn=recordScoreColumn;

            var jsonData = JSON.stringify(examStypeinfoPO);
            examStypeinfoPO = encodeURIComponent(jsonData);
            var url = EditPaperPage._contextPathprr;
            //console.log(url+'/paperTopEdit?examStypeinfoPO='+examStypeinfoPO);
            layer.open({
                type: 2,//弹出框类型
                title: i18n['paper_top'],
                shadeClose: false, //点击遮罩关闭层
                scrollbar: false,//屏蔽浏览器滚动条
                area: ['550px','200px'],
                shift:0.6,
                content: url+'/paperTopEdit?examStypeinfoPO='+examStypeinfoPO,//将结果页面放入layer弹出层
                btn: ['确定', '取消'],
                yes: function(index, layero){
                    var body = layer.getChildFrame('body', index);
                    body.html();
                    var secrecySymbolBoxflag = body.find('#secrecySymbolBox').is(':checked');
                    var subtitleBoxflag = body.find('#subtitleBox').is(':checked');
                    var testInfoBoxflag = body.find('#testInfoBox').is(':checked');
                    var stuInfoColumnBoxflag = body.find('#stuInfoColumnBox').is(':checked');
                    var recordScoreColumnBoxflag = body.find('#recordScoreColumnBox').is(':checked');
                    var mattersAttentionBoxflag = body.find('#mattersAttentionBox').is(':checked');
                    if (secrecySymbolBoxflag == true){
                        $("#secrecySymbol").text("1");
                        document.getElementById("secrecySymbolDiv").style.display="";
                    }else if (secrecySymbolBoxflag == false) {
                        $("#secrecySymbol").text("0");
                        document.getElementById("secrecySymbolDiv").style.display="none";
                    }
                    if (subtitleBoxflag == true){
                        if (subtitle == null && subtitle =="" && subtitle ==undefined){
                            $("#subtitle").text("请编辑考试副标题");
                        }
                        document.getElementById("subtitleDiv").style.display="";
                    }else if (subtitleBoxflag == false) {
                        $("#subtitle").text(null);
                        document.getElementById("subtitleDiv").style.display="none";
                    }
                    if (testInfoBoxflag == true){
                        if (testInfo == null && testInfo =="" && testInfo ==undefined){
                            $("#testInfo").text("考试范围：；考试时间：  分钟；命题人：  ");
                        }
                        document.getElementById("testInfoDiv").style.display="";
                    }else if (testInfoBoxflag == false) {
                        $("#testInfo").text(null);
                        document.getElementById("testInfoDiv").style.display="none";
                    }
                    if (stuInfoColumnBoxflag == true){
                        $("#stuInfoColumn").text("1");
                        document.getElementById("stuInfoColumnDiv").style.display="";
                    }else if (stuInfoColumnBoxflag == false) {
                        $("#stuInfoColumn").text("0");
                        document.getElementById("stuInfoColumnDiv").style.display="none";
                    }
                    if (recordScoreColumnBoxflag == true){
                        $("#recordScoreColumn").text("1");
                        document.getElementById("recordScoreColumnDiv").style.display="";
                    }else if (recordScoreColumnBoxflag == false) {
                        $("#recordScoreColumn").text("0");
                        document.getElementById("recordScoreColumnDiv").style.display="none";
                    }
                    if (mattersAttentionBoxflag == true){
                        if (mattersAttention == null && mattersAttention =="" && mattersAttention ==undefined){
                            $("#mattersAttention").text("注意事项:请输入");
                        }
                        document.getElementById("mattersAttentionDiv").style.display="";
                    }else if (mattersAttentionBoxflag == false) {
                        $("#mattersAttention").text(null);
                        document.getElementById("mattersAttentionDiv").style.display="none";
                    }

                    layer.close(index); //如果设定了yes回调需进行手工关闭
                },
                btn2: function(index, layero){

                    layer.close(index)
                }
            });

        },
        chooseQuestionWithKnowledge : function(event) {
            var url = CommUtils.formatStr('{0}?stageCode={1}&stageName={2}&gradeCode={3}&gradeName={4}&subjectCode={5}&subjectName={6}&userId={7}',
                EditPaperPage.chooseQuestionWithKnowledgeUrl,
                this.ifEmpty(this.paper.stageCode), this.ifEmpty(this.paper.stageName),
                this.ifEmpty(this.paper.gradeCode), this.ifEmpty(this.paper.gradeName),
                this.ifEmpty(this.paper.subjectCode), this.ifEmpty(this.paper.subjectName),this.ifEmpty(this.paper.userId)
            );
            this.frame(i18n['choose_question_with_knowledge'], url, '92%', '96%');
        },
        chooseQuestionWithChapter : function(event) {
            var materialVersion = this.paper.materialVersion;
            if (EditPaperPage.isCertify) {
                materialVersion = "VER93";
            }
            var url = CommUtils.formatStr('{0}?stageCode={1}&stageName={2}&gradeCode={3}&gradeName={4}&subjectCode={5}&subjectName={6}&materialVersion={7}&userId={8}',
                EditPaperPage.chooseQuestionWithChapterUrl,
                this.ifEmpty(this.paper.stageCode), this.ifEmpty(this.paper.stageName),
                this.ifEmpty(this.paper.gradeCode), this.ifEmpty(this.paper.gradeName),
                this.ifEmpty(this.paper.subjectCode), this.ifEmpty(this.paper.subjectName),
                this.ifEmpty(materialVersion), this.ifEmpty(this.paper.userId)
            );
            this.frame(i18n['choose_question_with_chapter'], url, '92%', '96%');
        },
        chooseQuestionWithPaper : function(event) {
            var url = CommUtils.formatStr('{0}?paperId={1}&stageCode={2}&stageName={3}&gradeCode={4}&gradeName={5}&subjectCode={6}&subjectName={7}&materialVersion={8}&userId={9}',
                EditPaperPage.chooseQuestionWithPaperUrl, this.ifEmpty(this.paper.paperId),
                this.ifEmpty(this.paper.stageCode), this.ifEmpty(this.paper.stageName),
                this.ifEmpty(this.paper.gradeCode), this.ifEmpty(this.paper.gradeName),
                this.ifEmpty(this.paper.subjectCode), this.ifEmpty(this.paper.subjectName),
                this.ifEmpty(this.paper.materialVersion), this.ifEmpty(this.paper.userId)
            );
            this.frame(i18n['choose_question_with_paper'], url, '92%', '96%');
        },
        chooseQuestionWithCollection : function(event) {
            var url = EditPaperPage.chooseQuestionWithCollectionUrl;
            url += "?stageCode=" + this.ifEmpty(this.paper.stageCode) + "&gradeCode=" + this.ifEmpty(this.paper.gradeCode) + "&subjectCode=" + this.ifEmpty(this.paper.subjectCode);
            this.frame(i18n['choose_question_with_collection'], url, '92%', '96%');
        },
        chooseQuestionWithTeachingAssistant : function(event){
            var url = CommUtils.formatStr('{0}?stageCode={1}&stageName={2}&gradeCode={3}&gradeName={4}&subjectCode={5}&subjectName={6}&materialVersion={7}&userId={8}',
                EditPaperPage.chooseQuestionWithTeachingAssistantUrl,
                this.ifEmpty(this.paper.stageCode), this.ifEmpty(this.paper.stageName),
                this.ifEmpty(this.paper.gradeCode), this.ifEmpty(this.paper.gradeName),
                this.ifEmpty(this.paper.subjectCode), this.ifEmpty(this.paper.subjectName),
                this.ifEmpty(this.paper.materialVersion), this.ifEmpty(this.paper.userId)
            );
            this.frame(i18n['choose_question_with_teaching_assistant'], url, '92%', '96%');
        },
        importPaperTemplate : function(event) {
            /*var url = CommUtils.formatStr('{0}?paperId={1}&stageCode={2}&stageName={3}&gradeCode={4}&gradeName={5}&subjectCode={6}&subjectName={7}&materialVersion={7}',
                EditPaperPage.importPaperTemplateUrl, this.ifEmpty(this.paper.paperId),
                this.ifEmpty(this.paper.stageCode), this.ifEmpty(this.paper.stageName),
                this.ifEmpty(this.paper.gradeCode), this.ifEmpty(this.paper.gradeName),
                this.ifEmpty(this.paper.subjectCode), this.ifEmpty(this.paper.subjectName),
                this.ifEmpty(this.paper.materialVersion)
            );*/
            this.frame(i18n['import_paper_template'], EditPaperPage.importPaperTemplateUrl, '500px', '680px');
        },
        editPaperTemplate : function(event) {
            var url = CommUtils.formatStr('{0}?stageCode={1}&gradeCode={2}&subjectCode={3}',
                EditPaperPage.editPaperTemplateUrl,
                this.ifEmpty(this.paper.stageCode), this.ifEmpty(this.paper.gradeCode), this.ifEmpty(this.paper.subjectCode)
            );
            this.frame(i18n['edit_paper_template'], url, '92%', '1180px');
        },
        inputPaperName : function(event) {
            var text = $(event.target).text();
            if (text.length > 32) {
                layer.alert(i18n['paper_name_max']);
                text = text.substring(0, 32);
                $(event.target).text(text);
            }
            Vue.set(this.paper, 'name', text);
            this.paper.isSaved = false;
        },
        inputNodeName : function(event, model) {
            var text = $(event.target).text();
            if (text.length > 10) {
                if (model === 1) {
                    layer.alert(i18n['paper_knode_name_max']);
                } else if (model === 2) {
                    layer.alert(i18n['paper_type_question_name_max']);
                }
                text = text.substring(0, 10);
                $(event.target).text(text);
            }
            var nodeSearchCode = $(event.target).attr('data-node-search-code');
            Vue.set(this.getNodeBySearchCode(nodeSearchCode), 'name', text);
            this.paper.isSaved = false;
        },
        removeKindNode : function(nodeSearchCode) {
            var _this = this;
            var isLast = true;
            var index;
            $.each(_this.paper.nodes, function (k, kNode) {
                if (kNode.nodeId == nodeSearchCode) {
                    if (_this.paper.nodes.length !== (k+1)) {
                        isLast = false;
                    }
                    index = k;
                } else if (!isLast) {
                    kNode.name = kNode.name.replace(/\d+/g, k);
                }
            });
            if (index !== undefined) {
                _this.paper.nodes.splice(index, 1);
                if (_this.paper.nodes.length === 0) {
                    var internalNo = 1;
                    var kindNode = EditPaperPage.newNode(Constants.EXAM_KIND_LEVEL, CommUtils.formatStr(i18n['kind_0'], internalNo), 0, internalNo, "" + internalNo, 1);
                    _this.paper.nodes.push(kindNode);
                    _this.paper.totalPoints = 0;
                    _this.paper.isSaved = false;
                }
                Vue.set(_this.paper, 'nodes', _this.paper.nodes);
                _this.refreshContentEditDom();
            }
        },
        removeTypeNode : function(nodeSearchCode) {
            var _this = this;
            var nodeSearchCodes = nodeSearchCode.split('_');
            var opFlag = false;
            $.each(_this.paper.nodes, function(k, kNode) {
                if (kNode.nodeId != nodeSearchCodes[0]) {
                    return true;
                }
                $.each(kNode.children, function (t, tNode) {
                    if (tNode.nodeId != nodeSearchCodes[1]) {
                        return true;
                    }
                    _this.paper.isSaved = false;
                    Vue.set(kNode, 'points', kNode.points - tNode.points);
                    _this.resetPoints(nodeSearchCodes[0], kNode.points + tNode.points, kNode.points);
                    _this.refreshContentEditDom();
                    var index = t+1;
                    if (kNode.children.length > index) {
                        for(var index1 = 0; index1 < kNode.children.length - index; index1++) {
                            var textNo = CommUtils.numberToChinese(index1 + index);
                            var tempNode = kNode.children[index1 + index];
                            var tempNodeNoIndex = tempNode.name.lastIndexOf("、");
                            if (tempNodeNoIndex > 0) {
                                tempNode.name = textNo + tempNode.name.substring(tempNodeNoIndex, tempNode.name.length);
                            } else {
                                tempNode.name = textNo + "、" + tempNode.name;
                            }
                        }
                    }
                    kNode.children.splice(t, 1);
                    EditPaperPage.resetNodeIndex();
                    //自动调节誉分栏
                    var table=document.getElementById("rscTable");
                    for(var i=0;i<table.rows.length;i++){
                        table.rows[i].deleteCell(table.rows[0].cells.length-2);
                        if (table.rows[0].cells.length==2){
                            table.rows[1].cells[0].innerText = "得分";
                        }
                    }
                    opFlag = true;
                    return false;
                });
                if (opFlag) return false;
            });
        },
        removeQuestion : function(nodeSearchCode) {
            var vm = this;
            var nodeSearchCodes = nodeSearchCode.split('_');
            var opFlag = false;
            $.each(vm.paper.nodes, function(k, kNode) {
                if (kNode.nodeId != nodeSearchCodes[0]) {
                    return true;
                }
                $.each(kNode.children, function(t, tNode) {
                    if (tNode.nodeId != nodeSearchCodes[1]) {
                        return true;
                    }
                    $.each(tNode.children, function (q, qNode) {
                        if (qNode.nodeId == nodeSearchCodes[2]) {
                            var internalNo = qNode.internalNo, externalNo = qNode.externalNo;
                            /*for (var qIndex = q + 1; qIndex < tNode.children.length; qIndex++) {
                                var tmpInternalNo = tNode.children[qIndex].internalNo, tmpExternalNo = tNode.children[qIndex].externalNo;
                                vm.changeQuestionNo(tNode.children[qIndex], externalNo, internalNo);
                                internalNo = tmpInternalNo;
                                externalNo = tmpExternalNo;
                            }*/

                            Vue.set(tNode, 'points', tNode.points - qNode.points);
                            vm.resetPoints(nodeSearchCode.substring(0, nodeSearchCode.lastIndexOf('_')), tNode.points, tNode.points - qNode.points);
                            tNode.children.splice(q, 1);
                            EditPaperPage.resetNodeIndex();
                            // Vue.set(tNode, 'children', tNode.children);
                            vm.refreshContentEditDom();
                            opFlag = true;
                            return false;
                        }
                    });
                    if (opFlag) return false;
                });
                if (opFlag) return false;
            });
        },
        moveQuestion : function(nodeSearchCode, direct, nodeType) {
            var vm = this;
            var nodeSearchCodes = nodeSearchCode.split('_');
            var opFlag = false;
            $.each(vm.paper.nodes, function(k, kNode) {
                if (kNode.nodeId != nodeSearchCodes[0]) {
                    return true;
                }
                $.each(kNode.children, function(t, tNode) {
                    if (tNode.nodeId != nodeSearchCodes[1]) {
                        return true;
                    }

                    if (nodeType === 'tNode') {
                        if (direct === 'down') {
                            t += 1;
                        }

                        var moveNode = kNode.children[t-1];
                        var internalNo = moveNode.internalNo;
                        var externalNo = moveNode.externalNo;
                        var nameSuffix = moveNode.name;

                        var tempNode = kNode.children.splice(t, 1)[0];

                        if (nameSuffix.indexOf("、") === 1) {
                            nameSuffix = nameSuffix.substring(1,nameSuffix.length);
                            moveNode.name = CommUtils.numberToChinese(t + 1) + nameSuffix;
                        }

                        moveNode.internalNo = tempNode.internalNo;
                        moveNode.externalNo = tempNode.externalNo;

                        tempNode.internalNo = internalNo;
                        tempNode.externalNo = externalNo;
                        var tempNodeNameSuffix = tempNode.name;
                        if (tempNodeNameSuffix.indexOf("、") === 1) {
                            tempNodeNameSuffix = tempNodeNameSuffix.substring(1,tempNodeNameSuffix.length);
                            tempNode.name = CommUtils.numberToChinese(t) + tempNodeNameSuffix;
                        }

                        kNode.children.splice(t-1, 0, tempNode);
                        EditPaperPage.resetNodeIndex();
                        vm.refreshContentEditDom();
                        vm.paper.isSaved = false;
                        opFlag = true;
                        return false;
                    } else {
                        for (var qIndex = 0; qIndex < tNode.children.length; qIndex++) {
                            if(tNode.children[qIndex].nodeId == nodeSearchCodes[2]) {
                                if(direct == 'down') {
                                    qIndex += 1;
                                }
                                var internalNo = tNode.children[qIndex-1].internalNo;
                                var externalNo = tNode.children[qIndex-1].externalNo;
                                vm.changeQuestionNo(tNode.children[qIndex - 1], tNode.children[qIndex].externalNo, tNode.children[qIndex].internalNo);
                                vm.changeQuestionNo(tNode.children[qIndex], externalNo, internalNo);

                                var qNode = tNode.children.splice(qIndex, 1)[0];
                                tNode.children.splice(qIndex-1, 0, qNode);
                                // vm.$emit('refresh-edit-dom');
                                vm.refreshContentEditDom();
                                vm.paper.isSaved = false;
                                opFlag = true;
                                return false;
                            }
                        }
                    }
                    if (opFlag) return false;
                });
                if (opFlag) return false;
            });
        },
        changeQuestion : function(question, obj) {
            $('.node_code[changing]').removeAttr('changing');
            var id = question.questionId ? question.questionId : question.questionNo;
            var url = CommUtils.formatStr('{0}?stageCode={1}&stageName={2}&gradeCode={3}&gradeName={4}&subjectCode={5}&subjectName={6}&materialVersion={7}&questionId={8}&userId={9}',
                EditPaperPage.chooseQuestionWithChapterUrl,
                this.ifEmpty(this.paper.stageCode), this.ifEmpty(this.paper.stageName),
                this.ifEmpty(this.paper.gradeCode), this.ifEmpty(this.paper.gradeName),
                this.ifEmpty(this.paper.subjectCode), this.ifEmpty(this.paper.subjectName),
                this.ifEmpty(this.paper.materialVersion), this.ifEmpty(id), this.ifEmpty(this.paper.userId)
            );

            this.frame(i18n['change_question'], url, '92%', '96%');
        },
        inputQuestion : function(questionId, obj) {
            $('.node_code[changing]').removeAttr('changing');
            this.changingNodeSearchCode = $(obj).closest('.node_code').data('nodeSearchCode');
            var url = EditPaperPage.preEditQuestionUrl;
            url += "?questionContentId=" + this.ifEmpty(questionId);
            this.frame(i18n['input_question'], url, '92%', '96%');
        },
        changeQuestionNo : function(qNode, newExternalNo, newInternalNo) {
            if (!CommUtils.isEmpty(qNode.children)) {
                var defaultChildQuesExternalNoPrefix = qNode.externalNo + '.';
                $.each(qNode.children, function(c, cQues) {
                    if (cQues.externalNo.startsWith(defaultChildQuesExternalNoPrefix)) {
                        Vue.set(cQues, 'externalNo', cQues.externalNo.replace(defaultChildQuesExternalNoPrefix, newExternalNo + '.'));
                    }
                });
            }
            Vue.set(qNode, 'internalNo', newInternalNo);
            Vue.set(qNode, 'externalNo', newExternalNo);
        },
        inputExternalNo : function(event) {
            var text = $(event.target).text();
            var nodeSearchCode = $(event.target).closest('.node_code').data('nodeSearchCode');

            var node = this.getNodeBySearchCode(nodeSearchCode);
            if (text.length <= 60) {
                Vue.set(node, 'externalNo', text);
            } else {
                $(event.target).text(node.externalNo);
                this.moveCursorEnd(event.target);
            }
            this.paper.isSaved = false;
        },
        resetPoints : function(nodeSearchCode, oldPoints, newPoints) {
            if (oldPoints == newPoints) {
                return;
            }
            //var intervalValue = parseFloat(newPoints) - parseFloat(oldPoints);
            while (nodeSearchCode.lastIndexOf('_') > 0) {
                nodeSearchCode = nodeSearchCode.substring(0, nodeSearchCode.lastIndexOf('_'));
                var node = this.getNodeBySearchCode(nodeSearchCode);
                var totalPoints = 0;
                $.each(node.children, function (index, cNode) {
                    totalPoints+=cNode.points;
                });
                if (this.editType == '_PROFESSIONAL'){
                    $("#"+node.nodeSearchCode).parent().css("border", "0px");
                }
                if (node.question){
                    Vue.set(node.question, 'points', new Number(totalPoints));
                }
                Vue.set(node, 'points', new Number(totalPoints));
            }
            var paperPoints = 0;
            $.each(this.paper.nodes, function(index, obj){
                paperPoints += obj.points;
            });
            Vue.set(this.paper, 'totalPoints', new Number(paperPoints));
            this.paper.isSaved = false;
        },
        blurPoints : function(event) {
            var text = $(event.target).text();
            if (text.trim() == '') {
                $(event.target).text(0);
            }
        },
        inputPoints : function(event) {
            var text = $(event.target).text();
            var nodeSearchCode = $(event.target).closest('.node_code').data('nodeSearchCode');
            var node = this.getNodeBySearchCode(nodeSearchCode);
            var oldPoints = node.points;
            if (text == "") {
                Vue.set(node, 'points', 0);
            } else if (text.length > 6 || !this.isPoints(text) || isNaN(parseFloat(text))) {
                $(event.target).text(node.points);
                this.tips(i18n['input_points_error'], event.target, 'right');
                this.moveCursorEnd(event.target);
            } else {
                Vue.set(node, 'points', parseFloat(text));
            }
            this.resetPoints(nodeSearchCode, oldPoints, node.points);
        },

        savePaper : function(event,value) {
            if (!CommUtils.tryLock(event.target)) {
                return;
            }
            var _this = this;
            if (_this.paper.isSaved && CommUtils.isEmpty(value)) {
                _this.tipBox(i18n['repeat_save_paper']);
                CommUtils.unLock(event.target);
                return;
            }
            //console.log("55");
            if (_this.editType == '_PROFESSIONAL'){
                var isRight = true;
                var errorQuestion = new Array();
                $.each(_this.paper.nodes, function(index1, kNode){
                    $.each(kNode.children, function(index2, tNode){
                        $.each(tNode.children, function(index3, qNode){
                            if (parseFloat(qNode.points) != parseFloat(qNode.question.points)){
                                errorQuestion.push({"type": tNode.name, "qNo": qNode.externalNo});
                                isRight = false;
                                $("#" + qNode.nodeSearchCode).parent().css("border", "1px solid red");
                            }
                        });
                    });
                });
                if (!isRight) {
                    var tips = i18n['question_point_not_matching_error'];
                    var error = '';
                    $.each(errorQuestion, function(x, xobj){
                        if (error.indexOf(xobj.type) > -1) return true;
                        error += error == ''? xobj.type + "[": "," + xobj.type+ "["
                        $.each(errorQuestion, function(y, yobj){
                            error+= y == 0 ? yobj.qNo : ","+yobj.qNo;
                        });
                        error += "]";
                    });
                    layer.alert(tips.replace('{0}', error));
                    CommUtils.unLock(event.target);
                    return;
                }
            }
            var canSave = _this.canSave(true);
            //console.log(canSave);
            if (!canSave) {
                CommUtils.unLock(event.target);
                return ;
            }
            var loading= layer.load(0, {shade: [0.5, '#000'], anim: -1});
            var paper = JSON.parse(JSON.stringify(this.paper));
            paper.nodes = _this.buildNodes(paper.nodes);

            var nodeLength = paper.nodes.length;
            var nodeLast = paper.nodes[nodeLength-1];
            while (1) {
                if (nodeLast.level === 3 && nodeLast.question && nodeLast.question.knowledgeList && nodeLast.question.knowledgeList.length > 0 && nodeLast.question.knowledgeList[0].materialVersion) {
                    paper.materialVersion = nodeLast.question.knowledgeList[0].materialVersion;
                    break;
                } else if (nodeLength === 0) {
                    break
                } else {
                    nodeLength = nodeLength-1;
                    nodeLast = paper.nodes[nodeLength];
                }
            }
            if(CommUtils.isNotEmpty(EditPaperPage.materialVersion)) {
                paper.materialVersion = EditPaperPage.materialVersion;
            }
            var url = "";
            if(CommUtils.isEmpty(value)) {
                url = EditPaperPage.savePaperUrl;
            } else {
                url = EditPaperPage.savePaperUrl+"?saveType="+value;
            }
            var paperNewQuestion = EditPaperPage.getPaperNewQuestion();
            if (CommUtils.isNotEmpty(paperNewQuestion)){
                $.post(EditPaperPage.increaseUsedNumberUrl, {"questionId" : paperNewQuestion}, function(){

                });
            }
            var table=document.getElementById("rscTable");
            var sumBigtopic = table.rows[0].cells.length-2;
            var mattersAttention =  $(".mattersAttention").text();
            var secrecySymbol =  $("#secrecySymbol").text();
            var subtitle =  $(".subtitle").text();
            var testInfo =  $(".testInfo").text();
            var stuInfoColumn =  $(".stuInfoColumn").text();
            var recordScoreColumn =  $(".recordScoreColumn").text();
            var examStypeinfoPO = {}
            examStypeinfoPO.mattersAttention=mattersAttention;
            examStypeinfoPO.secrecySymbol=secrecySymbol;
            examStypeinfoPO.subtitle=subtitle;
            examStypeinfoPO.testInfo=testInfo;
            examStypeinfoPO.stuInfoColumn=stuInfoColumn;
            examStypeinfoPO.recordScoreColumn=recordScoreColumn;
            examStypeinfoPO.sumBigtopic=sumBigtopic;
            paper.examStypeinfoPO=JSON.stringify(examStypeinfoPO);
            $.ajax({
                "url": url,
                "data": JSON.stringify(paper),
                contentType: "application/json;charset=utf-8",
                type: "post",
                success: function (result) {
                    layer.close(loading);
                    if (result.status == 1) {
                        console.log(result)
                        if (EditPaperPage.paper.opener){
                            _this.getContentJSON(result.data);
                        } else {
                            var paperId=result.data;
                            layer.msg('保存成功', {
                                icon: 1,
                                time: 2000 //2秒关闭（如果不配置，默认是3秒）
                            }, function(){
                                var url = EditPaperPage._contextPathprr+"/preedit" + "?userId=" + paper.userId;
                                window.location.href = url + "&paperId=" + paperId;
                            });
                            //var url = EditPaperPage.listUrl + "?providerCode=" + Constants.PROVIDER_CODE_PERSONAL;
                            //window.location.href = url + "&stageCode=" + _this.paper.stageCode + "&gradeCode=" + _this.paper.gradeCode + "&subjectCode=" + _this.paper.subjectCode;
                        }
                        // if (window.opener) {
                        //     result = $.extend(result, {
                        //         "providerCode" : Constants.PROVIDER_CODE_PERSONAL,
                        //         "stageCode" : paper.stageCode,
                        //         "gradeCode" : paper.gradeCode,
                        //         "subjectCode" : paper.subjectCode,
                        //         "materialVersion" : paper.materialVersion
                        //     });
                        //     _this.getContentJSON(result.data);
                        //     window.opener.postMessage({type: "makePaper", data: result}, "*");
                        // }else {
                        //     _this.paper.paperId = result.data;
                        //     _this.paper.isSaved = true;
                        //     _this.tipBox(i18n['save_paper_success']);
                        //     var url = EditPaperPage.listUrl + "?providerCode=" + Constants.PROVIDER_CODE_PERSONAL;
                        //     window.location.href = url + "&stageCode=" + _this.paper.stageCode + "&gradeCode=" + _this.paper.gradeCode + "&subjectCode=" + _this.paper.subjectCode;
                        // }
                    } else {
                        _this.tipBox(i18n['save_paper_error']);
                    }
                },
                error : function(result) {
                    _this.tipBox(i18n['save_paper_error']);
                    layer.close(loading);
                }
            }).always(function() {
                CommUtils.unLock(event.target);
            });
        },
        getContentJSON: function(paperId) {
            $.get(EditPaperPage.getContentJsonUrl+"/"+paperId, function (result) {
                window.opener.postMessage({type: "paperContentJson", data: result}, "*");
                layer.msg(i18n['save_paper_success'],{
                    time: 3000,
                    end:function () {
                        window.close();
                    }
                });
            });
        },
        buildNodes : function(kNodes){
            var _this = this, sequencing = 1, nodes = [];
            kNodes.forEach(function(kNode, k) {
                var tNodes = kNode.children;
                kNode.sequencing = sequencing++;
                kNode.parenNodetId = '';
                delete kNode.children;
                delete kNode.nodeSearchCode;
                nodes.push(kNode);
                tNodes.forEach(function(tNode, t) {
                    var qNodes = tNode.children;
                    tNode.sequencing = sequencing++;
                    tNode.parentNodeId = kNode.nodeId;
                    delete tNode.children;
                    delete tNode.nodeSearchCode;
                    delete tNode.typeCode;
                    nodes.push(tNode);
                    qNodes.forEach(function(qNode, q) {
                        var cNodes = qNode.children;
                        qNode.sequencing = sequencing++;
                        qNode.parentNodeId = tNode.nodeId;
                        delete qNode.children;
                        delete qNode.nodeSearchCode;
                        //delete qNode.question.children;
                        nodes.push(qNode);
                        if (!CommUtils.isEmpty(cNodes)) {
                            cNodes.forEach(function(cNode, c) {
                                cNode.sequencing = sequencing++;
                                cNode.parentNodeId = qNode.nodeId;
                                delete cNode.children;
                                delete cNode.nodeSearchCode;
                                nodes.push(cNode);
                            });
                        }
                        EditPaperPage.newPaperQuestions.push(qNode.question.questionId);
                    });
                });
            });
            return nodes;
        },
        isPoints : function(str) {
            var isInt = true;
            for (var i = 0; i < str.length; i++) {
                if (str[i] === ".") {
                    if (isInt) { isInt = false; continue; } else {return false;}
                }
                if (str[i] < "0" || str[i] > "9") { return false; }
            }
            return true;
        },
        moveCursorEnd : function(obj) {
            obj.focus(); //Solve the problem that ff can not locate the focus
            var range = window.getSelection();//Create range
            range.selectAllChildren(obj);//range: Select all sub-contents under obj
            range.collapseToEnd();//Cursor to the end
        },
        validTip : function(nodeSearchCode, content) {
            nodeSearchCode && document.getElementById(nodeSearchCode) && document.getElementById(nodeSearchCode).scrollIntoView();
            content && this.tipBox(content);
        },
        tips : function(content, obj, direct) {
            var directNo = ['', 'up', 'right', 'down', 'left'].indexOf(CommUtils.ifEmpty(direct, 'down'));
            layer.tips(content, obj,{
                tips: [directNo, '#1b92e2'],
                time: 1500,
                anim: 6
            });
        },
        tipBox : function(content, callback) {
            var tip = layer.confirm(content, {
                title : i18n['tipBox_title'],
                anim: -1,
                closeBtn: 0,
                time: 20000,
                btn: [i18n['confirm']]
            }, function () {
                callback && callback();
                layer.close(tip);
            });
        },
        frame : function(title, url, height, width) {
            layer.open({
                type: 2,
                title: title,
                shadeClose: false,
                scrollbar: false,
                shade: 0.6,
                area: [width, height],
                content: url //iframe的url
            });
        },
        ifEmpty : function(arg, defaultStr) {
            if (defaultStr == undefined) {
                defaultStr = '';
            }
            if (arg == undefined || arg == null) {
                return defaultStr;
            }
            return arg;
        },
        refreshContentEditDom : function() {
            // Refresh the question No.
            this.$nextTick(function () {
                $('[contenteditable="true"][edit-text]').each(function(){
                    $(this).html($(this).attr('edit-text'));
                });
            });
        },
        getNodeBySearchCode : function(nodeSearchCode) {
            var nodeSearchCodes = nodeSearchCode.split('_');
            var tmpNode = null;
            $.each(this.paper.nodes, function(k, kNode) {
                if (kNode.nodeId == nodeSearchCodes[0]) {
                    tmpNode = kNode;
                    return false;
                }
            });
            for (var index = 1; index < nodeSearchCodes.length; index++) {
                $.each(tmpNode.children, function(c, child) {
                    if (child.nodeId == nodeSearchCodes[index]) {
                        tmpNode = child;
                        return false;
                    }
                });
            }
            return tmpNode;
        }
    }
};

var EditPaperPage = window.EditPaperPage = {
    nodeSeq : 1, // node sequence
    vm : null, //vue obj
    paper : null, //paper info
    teacherBaseInfoStr : '', //The str about teacher info
    questionTypes : [],
    editBaseInfoUrl : '',
    materialVersion : '',
    originalQuestions : [],
    newPaperQuestions : [],
    init : function() {
        this.initPaper();
        this.initVue();
        this.initMessage();
        this.initQuestionTypeObj();
        window.getPaperQuestions = this.getPaperQuestions;
        window.getRecommendationQuestion = this.getRecommendationQuestion;
        window.getPaperInfo = this.getPaperInfo;
        window.getPaperTemplate = this.getPaperTemplate;
    },
    initQuestionTypeObj : function() {
        this.questionTypes = this.questionTypes.map(function(item){
            return {
                'typeCode' : item.code,
                'typeName' : item.name,
                'questions' : [],
            }
        });
    },
    getTypeArr : function() {
        return CommUtils.clone(this.questionTypes);
    },
    initMessage : function() {
        var _this = this;
        CommUtils.onMessage(function(event){
            var data = event.data;
            if (CommUtils.isEmpty(data)) {
                return;
            }
            if ('editBaseInfo' === data.type) {
                data =  data.data;
                $.each(data, function(key, value){
                    Vue.set(_this.vm.paper, key, value);
                    Vue.set(_this.vm.paperTemplate, key, value);
                });
                Vue.set(_this.vm.paperTemplate, "materialVersion", "");
                Vue.set(_this.vm.paper, "materialVersion", "");
                _this.materialVersion = data.materialVersion;
            } else if ('changeQuestion' === data.type) {
                /*data =  data.data;
                var nodeSearchCode = $('.node_code[changing]').data('nodeSearchCode');
                var quesNode = _this.vm.getNodeBySearchCode(nodeSearchCode);
                Vue.set(quesNode, 'question', data);*/
                data = data.data;
                var isSwitch = true;
                var changePoints = 0;
                $.each(_this.vm.paper.nodes, function (k, kNode) {
                    $.each(kNode.children, function (t, tNode) {
                        $.each(tNode.children, function (q, qNode) {
                            if (!CommUtils.isEmpty(qNode.question) & qNode.question.questionId === data.switchQuestionId) {
                                qNode.question = data.question;
                                if (_this.editType == '_PROFESSIONAL'){   //Template group roll distribution processing
                                    if (qNode.points != data.question.points){
                                        if (CommUtils.isEmpty(data.question.children)){  //For questions without sub-questions, assign template scores to the questions
                                            qNode.question.points = qNode.points;
                                        }else{
                                            var index = layer.alert(i18n['question_point_not_matching_node'], {icon: 7}, function () {
                                                $("html").scrollTop($("#" + qNode.nodeSearchCode).offset().top);
                                                layer.close(index);
                                            });
                                        }
                                    }
                                }
                                qNode.children = [];
                                var scoreSum = 0;
                                if (!CommUtils.isEmpty(data.question.children)) {
                                    $.each(data.question.children, function(c, childQuestion){
                                        var quesChildExternalNo = CommUtils.formatStr("{0}.{1}", qNode.internalNo, c+ 1);
                                        var quesChildNode = _this.newNode(Constants.EXAM_QUES_CHILD_LEVEL, "", childQuestion.points, c + 1, quesChildExternalNo, sequencing++);
                                        quesChildNode.nodeSearchCode = _this.generateNodeSearchCode(kNode.nodeId, tNode.nodeId, qNode.nodeId, quesChildNode.nodeId);
                                        quesChildNode.question = childQuestion;
                                        qNode.children.push(quesChildNode);
                                        scoreSum += quesChildNode.points;
                                    });
                                } else {
                                    scoreSum = data.question.points;
                                }
                                var changePoints = (scoreSum - qNode.points);
                                qNode.points = scoreSum;
                                // Modify parent node score
                                tNode.points += changePoints;
                                kNode.points += changePoints;
                                _this.vm.paper.totalPoints += changePoints;
                                isSwitch = false;
                                return false;
                            }
                        });
                        return isSwitch;
                    });
                    return isSwitch;
                });

                Vue.set(_this.vm, 'paper', _this.vm.paper);
            } else if ('chooseQuestion' === data.type) {
                data =  data.data;
                var kindNode = _this.vm.paper.nodes[_this.vm.paper.nodes.length - 1];
                var kindNodePoints = kindNode.points;
                Vue.set(kindNode, 'points', 0);

                var typeNodeMap = {};
                var typeTNodeIndexMap = {};
                /*if (!CommUtils.isEmpty(kindNode.children)) {
                    $.each(kindNode.children, function (t, tNode) {
                        if (!CommUtils.isEmpty(tNode.typeCode)) {
                            typeNodeMap[tNode.typeCode] = tNode;
                            typeTNodeIndexMap[tNode.typeCode] = t;
                        }
                    });
                }*/
                var typeInternalNo = 1,sequencing = 1; //There are empty question types in the data
                var index_ = 1;
                var typeNodes = [];
                $.each(data, function(t, typeInfo){
                    if (CommUtils.isEmpty(typeInfo.questions)) {
                        if (CommUtils.isNotEmpty(typeNodeMap[typeInfo.typeCode])) {
                            var tNodeIndex = typeTNodeIndexMap[typeInfo.typeCode];
                            var tNodeIndexTemp = tNodeIndex;
                            while (tNodeIndexTemp < typeNodes.length-1) { // Update big question number
                                tNodeIndexTemp += 1;
                                var textNo = CommUtils.numberToChinese(tNodeIndexTemp);
                                var tempNode = typeNodes[tNodeIndexTemp];
                                var tempNodeNoIndex = tempNode.name.lastIndexOf("、");
                                if (tempNodeNoIndex > 0) {
                                    tempNode.name = textNo + tempNode.name.substring(tempNodeNoIndex, tempNode.name.length);
                                } else {
                                    tempNode.name = textNo + "、" + tempNode.name;
                                }
                            }
                            typeNodes.splice(tNodeIndex, 1);
                        }
                        return true;
                    }
                    var typeNode = typeNodeMap[typeInfo.typeCode];
                    if (CommUtils.isEmpty(typeNode)) {
                        typeNode = _this.newNode(Constants.EXAM_QUES_TYPE_LEVEL, CommUtils.numberToChinese(typeInternalNo) + "、" + CommUtils.ifEmpty(typeInfo.typeName), 0, typeInternalNo, typeInternalNo + "", sequencing++);
                        typeNode.parentId = kindNode.nodeId;
                        typeNode.typeCode = typeInfo.typeCode;
                        typeNode.nodeSearchCode = _this.generateNodeSearchCode(kindNode.nodeId, typeNode.nodeId);
                        typeNodes.push(typeNode);

                        //var newString = JSON.stringify(typeNode);
                        //console.log(newString);
                        //console.log(typeNode.name);
                    } else {
                        typeNode.children = [];
                        typeNode.points = 0;
                    }

                    $.each(typeInfo.questions, function(q, question){
                        var quesNode = _this.newNode(Constants.EXAM_QUES_LEVEL, "", question.points, index_, index_ + "",sequencing++);
                        index_ += 1;
                        if (!CommUtils.isEmpty(question.children)) {
                            $.each(question.children, function(c, childQuestion){
                                var quesChildExternalNo = CommUtils.formatStr("{0}.{1}", quesNode.internalNo, c+ 1);
                                var quesChildNode = _this.newNode(Constants.EXAM_QUES_CHILD_LEVEL, "", childQuestion.points, c + 1, quesChildExternalNo, sequencing++);
                                quesChildNode.nodeSearchCode = _this.generateNodeSearchCode(kindNode.nodeId, typeNode.nodeId, quesNode.nodeId, quesChildNode.nodeId);
                                quesChildNode.question = childQuestion;
                                quesNode.children.push(quesChildNode);
                            });
                        }
                        quesNode.question = question;
                        quesNode.nodeSearchCode = _this.generateNodeSearchCode(kindNode.nodeId, typeNode.nodeId, quesNode.nodeId);
                        typeNode.points += quesNode.points;
                        typeNode.children.push(quesNode);
                    });

                    Vue.set(kindNode, 'points', kindNode.points + typeNode.points);
                    // kindNode.children.push(typeNode);
                    typeInternalNo++;
                });
                var bigtopic = typeInternalNo -1;
                var table = document.getElementById("rscTable");
                var len=table.rows.length;
                for(var i=0;i<len;i++){
                    table.deleteRow(0);//也可以写成table.deleteRow(0);
                }
                var titlerow = document.createElement("tr");
                titlerow.height = "40px";
                var td = document.createElement("td");
                td.width = "60px";
                td.innerHTML = "题号";
                titlerow.appendChild(td);
                var scrow = document.createElement("tr");
                var td1 = document.createElement("td");
                td1.innerHTML = "得分";
                scrow.appendChild(td1);
                for (var i = 1; i <= bigtopic; i++) {
                    var td0 = document.createElement("td");
                    td0.width = "60px";
                    switch(i) {
                        case 1:
                            td0.innerHTML = "一";
                            break;
                        case 2:
                            td0.innerHTML = "二";
                            break;
                        case 3:
                            td0.innerHTML = "三";
                            break;
                        case 4:
                            td0.innerHTML = "四";
                            break;
                        case 5:
                            td0.innerHTML = "五";
                            break;
                        case 6:
                            td0.innerHTML = "六";
                            break;
                        case 7:
                            td0.innerHTML = "七";
                            break;
                        case 8:
                            td0.innerHTML = "八";
                            break;
                        case 9:
                            td0.innerHTML = "九";
                            break;
                        default:
                            td0.innerHTML = "...";
                    }
                    titlerow.appendChild(td0);
                    scrow.appendChild(document.createElement("td"));
                }
                var td3 = document.createElement("td");
                td3.innerHTML = "总分";
                td3.width = "80px";
                titlerow.appendChild(td3);
                var td4 = document.createElement("td");
                scrow.appendChild(td4);
                table.appendChild(titlerow);
                table.appendChild(scrow);
                //console.log(typeInternalNo);
                Vue.set(kindNode, 'children', typeNodes);
                Vue.set(_this.vm.paper, 'totalPoints', _this.vm.paper.totalPoints + kindNode.points - kindNodePoints);
            } else if ("recommendationQuestion" === data.type) {
                data = data.data;
                var recommendationQuestionsMap = {};
                $.each(data.recommendationQuestions, function (index, recommendationQuestion) {
                    recommendationQuestionsMap[recommendationQuestion.nodeSearchCode] = recommendationQuestion.questionInfo;
                });
                var templatePaper = data.paper;
                var isTempQuestion = false;
                $.each(data.paper.nodes, function (index, kNode) {
                    $.each(kNode.children, function (indexLevel2, tNode) {
                        $.each(tNode.children, function (indexLevel3, qNode) {
                            var recommendationQuestion = recommendationQuestionsMap[qNode.nodeSearchCode];
                            if (recommendationQuestion) {
                                var question = recommendationQuestionsMap[qNode.nodeSearchCode];
                                qNode.question = question;
                                qNode.question.points = qNode.points;  //Assign the template value to the title (the template score cannot be changed)
                            } else {
                                // No questions found
                                var text = (new Date()).valueOf() + Math.ceil(Math.random()*100) + "";
                                qNode.question = {
                                    "questionId": text, // To change a question, you must have id and type
                                    "type": tNode.type,
                                    "points": 0,
                                    "childAmount": 0,
                                    "isTempQuestion": true
                                };
                                isTempQuestion = true;
                            }
                            if (!qNode.children) {
                                qNode.children = [];
                            }
                        })
                    })
                });
                Vue.set(_this.vm, 'paper', templatePaper);
                if (isTempQuestion) {
                    layer.alert(i18n['question_recommendation_query_some_empty']);
                }
            }
            // Non-edit mode does not modify test paper status
            if (event.data.type) {
                _this.vm.paper.isSaved = false;
            }
            _this.vm.refreshContentEditDom();
        });
    },
    getRecommendationQuestion : function(param, EditPaperTemplatePage) {
        // begin to mark paper
        layer.closeAll();
        $('#loadingTemplate').show();
        $.ajax({
            url: EditPaperTemplatePage.queryQuestionRecommendationUrl,
            type:"POST",
            data: JSON.stringify(param),
            contentType:"application/json; charset=utf-8",
            dataType:"json",
            async: true,
            success: function(data){
                $('#loadingTemplate').hide();
                var result = {
                    'paper': CommUtils.clone(EditPaperTemplatePage.vm.paper),
                    'recommendationQuestions': data
                };
                if (data.length === 0) {
                    layer.alert(i18n['question_recommendation_query_empty'], {
                        title: i18n['tipBox_title']
                    });
                } else {
                    window.postMessage({"type" : "recommendationQuestion", "data" : result}, "*");
                }
            },
            error: function () {
                $('#loadingTemplate').hide();
                layer.alert(i18n['question_recommendation_query_error'], {
                    title: i18n['tipBox_title']
                });
            }
        });
    },
    initVue : function() {
        VueConfig.data.paper = CommUtils.clone(this.paper);
        VueConfig.data.editType = this.editType;
        this.vm = new Vue(VueConfig);
        this.vm.$mount("#paper");
    },
    getPaperQuestions : function() {
        var paper = EditPaperPage.vm.paper;
        var questionMarket = EditPaperPage.getTypeArr();
        if (CommUtils.isEmpty(paper.nodes)) {
            return questionMarket;
        }
        var typeIndexMap = {};
        questionMarket.forEach(function(type, t){
            typeIndexMap[type.typeCode] = t;
        });
        if (!CommUtils.isEmpty(paper.nodes)) {
            $.each(paper.nodes, function (k, kNode) {
                $.each(kNode.children, function(t, tNode) {
                    if (CommUtils.isEmpty(tNode.children)) {
                        return true;
                    }
                    $.each(tNode.children, function(q, qNode) {
                        if (qNode.question != null && (!CommUtils.isEmpty(qNode.question.questionId) || !CommUtils.isEmpty(qNode.question.questionNo))) {
                            var quesChildren = [];
                            if (!CommUtils.isEmpty(qNode.children)) {
                                $.each(qNode.children, function(c, cNode){
                                    if (CommUtils.isEmpty(cNode.question)) return;
                                    var childQues = JSON.parse(JSON.stringify(cNode.question));
                                    childQues.points = cNode.points;
                                    quesChildren.push(childQues);
                                });
                            }
                            if (qNode.question && qNode.question.type && qNode.question.type.code) {
                                if (CommUtils.isEmpty(qNode.question)) return;
                                var question = JSON.parse(JSON.stringify(qNode.question));
                                question.points = qNode.points;
                                question.children = quesChildren;
                                questionMarket[typeIndexMap[qNode.question.type.code]].questions.push(question);
                            }
                        }
                    });
                });
            });
        }
        return questionMarket;
    },
    getPaperTypeQuestions: function() {
        var _this = this;
        var paperQuestionTypeMap = {};
        var paperQuestions = _this.getPaperQuestions();
        for (var i = 0; i < paperQuestions.length; i++) {
            paperQuestions[i].questions = [];
            paperQuestionTypeMap[paperQuestions[i].typeCode] = i;
        }

        return {
            "paperQuestionTypeMap": paperQuestionTypeMap,
            "paperQuestions": paperQuestions
        };
    },
    getPaperInfo : function() {
        return JSON.parse(JSON.stringify(EditPaperPage.vm.paper));
    },
    getPaperTemplate : function() {
        // TemplateBuildType 1: Generated by template 2: Generated by test paper
        // EditPaperPage.vm.paperTemplate Use test paper to generate template when empty
        if (!EditPaperPage.vm.paperTemplate.TemplateBuildType) {
            EditPaperPage.vm.paperTemplate = CommUtils.clone(EditPaperPage.vm.paper);
            EditPaperPage.vm.paperTemplate.TemplateBuildType = 2;
            return EditPaperPage.vm.paperTemplate;
        } else {
            if (EditPaperPage.vm.paperTemplate.TemplateBuildType === 2) {
                return EditPaperPage.vm.paperTemplate;
            } else if (EditPaperPage.vm.paperTemplate.TemplateBuildType === 1){
                if (!EditPaperPage.vm.paperTemplate.isInit) {
                    EditPaperPage.paper = EditPaperPage.vm.paperTemplate;
                    EditPaperPage.initPaper();
                    EditPaperPage.vm.paperTemplate.isInit = true;
                    EditPaperPage.vm.paperTemplate = EditPaperPage.paper;
                }
                return EditPaperPage.vm.paperTemplate;
            }
        }
    },
    initPaper : function() { //init paper info
        var _this = this;
        if (_this.paper == null) {
            return;
        }
        var paper = _this.paper;
        paper.isSaved = true;
        if(_this.isCertify){$(document).attr("title","探寶森林");}
        if (CommUtils.isEmpty(paper.paperId) && !CommUtils.isEmpty(_this.teacherBaseInfoStr)) {
            var baseInfo = JSON.parse(_this.teacherBaseInfoStr);
            $.each(baseInfo, function(key, value) {
                if (CommUtils.isEmpty(paper[key])) {
                    paper[key] = value;
                }
            });

            if (CommUtils.isEmpty(paper.name)) {
                var date = CommUtils.formatDate(new Date(), 'yyyyMMdd');
                paper.name = CommUtils.formatStr("{0}{1}{2}{3}" + i18n['paper'], date, paper.stageName, paper.gradeName, paper.subjectName);
            }
            paper.isSaved = false;
        }
        // Default value when adding test paper
        if (CommUtils.isEmpty(paper.nodes)) {
            paper.nodes = [];
            var internalNo = _this.nodeSeq++;
            var kindNode = _this.newNode(Constants.EXAM_KIND_LEVEL, CommUtils.formatStr(i18n['kind_0'], internalNo), 0, internalNo, "" + internalNo, 1);
            paper.nodes.push(kindNode);
            paper.totalPoints = 0;
            paper.isSaved = false;
        }
        // node searchCode build
        var lastKindNodeId='', lastTypeId = '', lastQuesId = '';
        var parentIdMap = {};
        var nodeMap = {};
        var question = {};
        var quesChildren = [], quesList = [], typeList = [], kindList = [];

        $.each(paper.nodes, function(n, node) {
            node.children = [];
            switch (node.level) {
                case Constants.EXAM_KIND_LEVEL:
                    lastKindNodeId = node.nodeId;
                    node.nodeSearchCode = _this.generateNodeSearchCode(lastKindNodeId);
                    kindList.push(node);
                    // nodeMap When not empty, take the data to the lower level
                    if (nodeMap[node.nodeId]) {
                        node.children.push(nodeMap[node.nodeId]);
                    }
                    nodeMap[node.nodeId] = node;
                    break;
                case Constants.EXAM_QUES_TYPE_LEVEL:
                    // parentNodeId Use the previous one when it is not empty
                    if (node.parentNodeId) {
                        parentIdMap[node.nodeId] = node.parentNodeId;

                        if (nodeMap[node.parentNodeId]) {
                            nodeMap[node.parentNodeId].children.push(node);
                        } else {
                            nodeMap[node.parentNodeId] = node;
                        }

                        if (nodeMap[node.nodeId]) {
                            node.children.push(nodeMap[node.nodeId]);
                        }
                        nodeMap[node.nodeId] = node;

                        node.nodeSearchCode = _this.generateNodeSearchCode(node.parentNodeId, node.nodeId);
                    } else {
                        lastTypeId = node.nodeId;
                        node.nodeSearchCode = _this.generateNodeSearchCode(lastKindNodeId, lastTypeId);
                    }
                    break;
                case Constants.EXAM_QUES_LEVEL:
                    if (node.parentNodeId) {
                        var kindNodeId = parentIdMap[node.parentNodeId];
                        parentIdMap[node.nodeId] = node.parentNodeId;
                        if (nodeMap[node.parentNodeId]) {
                            nodeMap[node.parentNodeId].children.push(node);
                        } else {
                            nodeMap[node.parentNodeId] = node;
                        }

                        if (nodeMap[node.nodeId]) {
                            node.children.push(nodeMap[node.nodeId]);
                        }
                        nodeMap[node.nodeId] = node;

                        node.nodeSearchCode = _this.generateNodeSearchCode(kindNodeId, node.parentNodeId, node.nodeId);
                    } else {
                        lastQuesId = node.nodeId;
                        node.nodeSearchCode = _this.generateNodeSearchCode(lastKindNodeId, lastTypeId, lastQuesId);
                    }
                    _this.originalQuestions.push(node.question.questionId);
                    break;
                case Constants.EXAM_QUES_CHILD_LEVEL:
                    if (node.parentNodeId) {
                        var quesNodeId = node.parentNodeId;
                        var typeNodeId = parentIdMap[quesNodeId];
                        var kindNodeId = parentIdMap[typeNodeId];

                        if (nodeMap[node.parentNodeId]) {
                            nodeMap[node.parentNodeId].children.push(node);
                        } else {
                            nodeMap[node.parentNodeId] = node;
                        }

                        if (nodeMap[node.nodeId]) {
                            node.children.push(nodeMap[node.nodeId]);
                        }
                        nodeMap[node.nodeId] = node;

                        node.nodeSearchCode = _this.generateNodeSearchCode(kindNodeId, typeNodeId, quesNodeId, node.nodeId);
                    } else {
                        node.nodeSearchCode = _this.generateNodeSearchCode(lastKindNodeId, lastTypeId, lastQuesId, node.nodeId);
                    }
                    break;
            }
        });
        if (kindList.length <= 0 || kindList.length > 0 && !kindList[0].children) {
            kindList = [];
            // Put sub-topic nodes into topic nodes
            for (var index = paper.nodes.length - 1; index >= 0; index--) {
                var node = paper.nodes[index];
                switch (node.level) {
                    case Constants.EXAM_KIND_LEVEL:
                        typeList.reverse();
                        node.children = CommUtils.clone(typeList);
                        typeList = [];
                        kindList.push(node);
                        break;
                    case Constants.EXAM_QUES_TYPE_LEVEL:
                        quesList.reverse();
                        node.children = CommUtils.clone(quesList);
                        if (!CommUtils.isEmpty(node.children) && node.children[0].question != null && node.children[0].question.type != null) {
                            node.typeCode = node.children[0].question.type.code;
                            node.typeName = node.children[0].question.type.name;
                        }
                        quesList = [];
                        typeList.push(node);
                        break;
                    case Constants.EXAM_QUES_LEVEL:
                        quesChildren.reverse();
                        node.children = CommUtils.clone(quesChildren) || [];
                        quesChildren = [];
                        quesList.push(node);
                        break;
                    case Constants.EXAM_QUES_CHILD_LEVEL:
                        quesChildren.push(node);
                        break;
                }
            }
            kindList.reverse();
        }
        paper.nodes = kindList;
        _this.paper = paper;
    },
    generateNodeSearchCode : function() {
        return Array.prototype.slice.call(arguments).map(function(item){return ""+item;}).join("_");
    },
    newNode : function(level, name, points, internalNo, externalNo, sequencing) {
        // Question score is 1 point by default
        if (level === Constants.EXAM_QUES_LEVEL || level === Constants.EXAM_QUES_CHILD_LEVEL) {
            if (CommUtils.isEmpty(points) || points === 0) {
                points = 1;
            }
        }
        return {
            'level' : level,
            'internalNo' : internalNo,
            'externalNo' : externalNo,
            'name' : name,
            'points' : points || 0,
            'sequencing' : sequencing || 0,
            'nodeId' : this.nodeSeq++,
            'parentId' : '',
            'description' : '',
            'children' : []
        };
    },
    resetNodeIndex: function() {
        var sequencing = 0;
        var questionCount = 0;
        $.each(VueConfig.data.paper.nodes, function (kIndex, kNode) {
            sequencing += 1;
            kNode.sequencing = sequencing;
            $.each(kNode.children, function (tIndex, tNode) {
                sequencing += 1;
                tNode.sequencing = sequencing;
                $.each(tNode.children, function (qIndex, qNode) {
                    sequencing += 1;
                    questionCount += 1;
                    qNode.sequencing = sequencing;
                    qNode.internalNo = questionCount;
                    qNode.externalNo = questionCount + "";
                    $.each(qNode.children, function (cIndex, cNode) {
                        sequencing += 1;
                        cNode.sequencing = sequencing;
                        cNode.internalNo = cIndex+1;
                        cNode.externalNo = qNode.externalNo + "." + (cIndex+1);
                    })
                })
            })
        })
    },
    getPaperNewQuestion : function(){
        if (this.originalQuestions.length == 0) return this.newPaperQuestions.join(",");
        else{
            for (var i = 0; i < this.newPaperQuestions.length; i++) {
                for(var j = 0; j < this.originalQuestions.length; j++){
                    if (this.newPaperQuestions[i] == this.originalQuestions[j]){
                        this.newPaperQuestions.splice(i, 1);
                        i--;
                    }
                }
            }
        }
        return this.newPaperQuestions.join(",");
    }

};
