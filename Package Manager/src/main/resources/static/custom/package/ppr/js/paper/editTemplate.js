Vue.config.devtools = true;
Vue.directive('edit', {
    isPoints : function(str) { //判断分数
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
        if (obj.tagName.toLowerCase() == 'input') return;
        $(obj).focus(); //解决ff不获取焦点无法定位问题
        var range = window.getSelection();//创建range
        range.selectAllChildren(obj);//range 选择obj下所有子内容
        range.collapseToEnd();//光标移至最后
    },
    getValue : function(el) { //获取可编辑元素值
        if (el.tagName.toLowerCase() == 'input') return el.value;
        else return el.innerHTML;
    },
    setValue : function(el, value) { //设置可编辑元素值
        if (el.tagName.toLowerCase() == 'input') el.value = value;
        else el.innerHTML = value;
    },
    bind: function (el, binding, vnode) {
        var valueKey = binding.arg, nodeSearchCode = binding.value['nodeSearchCode'], node = binding.value;
        var defFunc = binding.def;
        if (binding.modifiers['score']) {
            $(el).on('input', function(e){
                var value = defFunc.getValue(this);
                var points = 0;
                if (valueKey === "points") {
                    points = node.points;
                }

                if (value == "") {
                    value = 0;
                    Vue.set(node, valueKey, value);
                } else if (value.length > 6 || !defFunc.isPoints(value) || isNaN(parseFloat(value))) {
                    defFunc.setValue(this, node[valueKey]);
                    layer.tips(i18n['input_points_error'], this,{ tips: [2, '#1b92e2'], time: 1500, anim: 6 });
                    defFunc.moveCursorEnd(this);
                } else {
                    Vue.set(node, valueKey, parseFloat(value));
                }

                if (points !== parseFloat(value)) {
                    // 更新父级节点分数
                    var nodeSearchCodeMap = EditPaperTemplatePage.vm.nodeSearchCodeMap;
                    var parentNodeKey = node.nodeSearchCode;
                    var parentNodePointsTemp = 0;
                    while (parentNodeKey.indexOf("_") > 0) {
                        parentNodeKey = parentNodeKey.substring(0, parentNodeKey.lastIndexOf("_"));
                        var parentNode = nodeSearchCodeMap[parentNodeKey];
                        parentNodePointsTemp = parentNode.points - points + node.points;

                        if (parentNodeKey.length === 1) {
                            var paper = EditPaperTemplatePage.vm.paper;
                            var totalPoints = paper.totalPoints - parentNode.points + parentNodePointsTemp;
                            Vue.set(paper, "totalPoints", parseFloat(totalPoints));
                        }

                        Vue.set(parentNode, valueKey, parseFloat(parentNodePointsTemp));
                    }
                }
            }).on('blur', function(){
                if (defFunc.getValue(this) == "") {
                    defFunc.setValue(this, 0);
                }
            });
        }
        if (binding.modifiers['not-empty']) {
            $(el).on('blur', function(){
                if (defFunc.getValue(this) == "") {
                    layer.tips(content, this,{ tips: [2, '#1b92e2'], time: 1500, anim: 6 });
                }
            });
        }
    },
    inserted : function(el, binding, vnode) {
        if (!$(el).attr('contenteditable')) {
            $(el).attr('contenteditable', 'true');
        }
        binding.def.setValue(el, binding.value[binding.arg]);
    },
    update : function(el, binding, vnode) {
        if (!$(el).is(":focus")) {
            binding.def.setValue(el, binding.value[binding.arg]);
        }
    },
});
/**
 * 左侧添加题目区域组件
 */
Vue.component('question-add-area', {
    template : '#question-add-area',
    props : ['questionTypes'],
    data : function() {
        return {
            'types' : $.map(this.questionTypes, function(item){
                return {
                    'code' : item.code,
                    'name' : item.name,
                    'count' : '0'
                }
            })
        };
    },
    computed : {
        vilidCount : function(count) { //是否
            return function (count) {
                if (/[0-9]+/g.test(count)) {
                    if (parseInt(count) > 0) {
                        return true;
                    }
                }
                return false;
            };
        }
    },
    methods : {
        plusQuestionCount : function(typeInfo) {
            if (/[0-9]+/g.test(typeInfo.count)) {
                Vue.set(typeInfo, 'count', parseInt(typeInfo.count) + 1);
            } else {
                Vue.set(typeInfo, 'count', '1');
            }
        },
        reduceQuestionCount : function(typeInfo) {
            if (/[0-9]+/g.test(typeInfo.count)) {
                var count = parseInt(typeInfo.count);
                if (count > 0) {
                    Vue.set(typeInfo, 'count', typeInfo.count - 1);
                }
            } else {
                Vue.set(typeInfo, 'count', '0');
            }
        },
        inputCount : function(typeInfo, event) {
            if (/^[0-9]*$/g.test(typeInfo.count)) {
                var count = CommUtils.ifEmpty(typeInfo.count, "0");
            } else {
                $(event.target).focus();
                EditPaperTemplatePage.tips(i18n['ques_count_only_number'], event.target, 'up');
            }
        },
        addQuestion : function(typeInfo, event) {
            if (!this.vilidCount(typeInfo.count)) {
                return;
            }
            var result = JSON.parse(JSON.stringify(typeInfo));
            Vue.set(typeInfo, 'count', 0);
            this.$emit('add-question', result);
        }
    }
});
var VueConfig = {
    data: {
        'paper': {},
        'questionTypes' : [],
        'questionResourceTypes' : [],
        'curKindNodeSearchCode' : '',
        'curChooseKnowledgeQues' : null,
        'curChooseQuestionResourceParam': {
            'providerCode': "R00"
        },
        'layerIndex': 1
    },
    mounted: function () {
        if (this.paper.materialVersion != undefined && this.paper.materialVersion.trim() != '') {
            this.initKnowledgeTree();
        }
    },
    computed : {
        nodeSearchCodeMap : function() {
            var nodeSearchCodeMap = {};
            $.each(this.paper.nodes, function(k, kNode) {
                nodeSearchCodeMap[kNode.nodeSearchCode] = kNode;
                if(CommUtils.isEmpty(kNode.children)) {
                    return true;
                }
                $.each(kNode.children, function(t, tNode) {
                    nodeSearchCodeMap[tNode.nodeSearchCode] = tNode;
                    if(CommUtils.isEmpty(tNode.children)) {
                        return true;
                    }
                    $.each(tNode.children, function(q, qNode) {
                        nodeSearchCodeMap[qNode.nodeSearchCode] = qNode;
                        if(CommUtils.isEmpty(qNode.children)) {
                            return true;
                        }
                        $.each(qNode.children, function(c, cNode) {
                            nodeSearchCodeMap[cNode.nodeSearchCode] = cNode;
                        });
                    });
                });
            });
            return nodeSearchCodeMap;
        }
    },
    methods : {
        validChangeMeterial : function(callback) {
            var _this = this;
            var valid = true;
            $.each(_this.nodeSearchCodeMap, function(n, node) {
                if (Constants.EXAM_QUES_LEVEL == node.level) {
                    if (!CommUtils.isEmpty(node.knowledges) && node.knowledges.length > 0) {
                        valid = false;
                        return false;
                    }
                }
            });
            if (valid) {
                callback && callback();
            } else {
                EditPaperTemplatePage.confirmBox(i18n['change_material_version_clear_knowledge'], function() {
                    $.each(_this.nodeSearchCodeMap, function(n, node) {
                        if (Constants.EXAM_QUES_LEVEL == node.level) {
                            Vue.set(node, 'knowledges', []);
                        }
                    });
                    callback && callback();
                }, function() {
                    $('select[name="materialVersion"]').val(this.paper.materialVersion);
                });
            }
        },
        changeMeterialVersion : function() {
            var _this = this;
            _this.validChangeMeterial(function() {
                $(':jstree').jstree('destroy');
                _this.initKnowledgeTree();
            });
        },
        initKnowledgeTree : function() {
            var param = {
                'subjectCode': this.paper.subjectCode,
                'gradeCode': this.paper.gradeCode,
                'materialVersion': this.paper.materialVersion,
                'providerCode': Constants.PROVIDER_CODE_PUBLIC
            };
            $.ajax({
                method: "post",
                url: EditPaperTemplatePage.queryKnowledgeTreeUrl,
                cache: false,
                data: param,
                success: function (data) {
                    if (data && data !== "" && data !== "[]") {
                        knowledgeSetting.core.data = $.map(data.data, function(item){
                            return {
                                'id' : item.knowledgeId,
                                'parent' : CommUtils.ifEmpty(item.parentId, '#'),
                                'text' : item.knowledgeName,
                                'state' : {opened : false, disabled : false, selected : false},
                                'li_attr' : {
                                    'id':  item.knowledgeId, 'name' : item.knowledgeName, 'searchCode' : item.searchCode, 'outlineId' : item.outlineId
                                }
                            };
                        });
                        $('#knowledgeTree').unbind('changed.jstree').on('changed.jstree', function (e, data) {
                            if (data.action == "deselect_all") return;
                            var checkNode = data.instance.get_top_checked(true);
                            var knowledges = $.map(checkNode, function(item) {
                                return {
                                    'knowledgeId': item.li_attr.id,
                                    'knowledgeName': item.li_attr.name,
                                    'searchCode': item.li_attr.searchCode
                                };
                            });
                            Vue.set(EditPaperTemplatePage.vm.curChooseKnowledgeQues, 'knowledges', knowledges);
                        }).jstree(knowledgeSetting);
                    }
                }
            });
        },
        addTemplateKind : function(event) {
            if (this.paper.nodes.length >= 4) {
                EditPaperTemplatePage.tips(i18n['kind_max_four'], event.target, 'up');
                return;
            }
            var internalNo = this.paper.nodes.length + 1;
            var kindNode = EditPaperTemplatePage.newNode(Constants.EXAM_KIND_LEVEL, CommUtils.formatStr(i18n['kind_0'], internalNo), 0, internalNo, "" + internalNo);
            kindNode.nodeSearchCode = "" + kindNode.nodeId;
            this.paper.nodes.splice(this.paper.nodes.length, 0, kindNode);
            this.curKindNodeSearchCode = kindNode.nodeSearchCode;
        },
        removeTemplateKind : function(nodeArr, index) {
            // 刷新分数
            Vue.set(this.paper, "totalPoints", parseFloat(this.paper.totalPoints - nodeArr[index].points));

            if (this.curKindNodeSearchCode == nodeArr[index].nodeSearchCode && nodeArr.length > 1) {
                this.curKindNodeSearchCode = nodeArr[0].nodeSearchCode;
            }
            nodeArr.splice(index, 1);
            nodeArr.forEach(function(node, nodeIndex){
                Vue.set(node, 'name', CommUtils.formatStr(i18n['kind_0'], nodeIndex + 1));
                Vue.set(node, 'internalNo', nodeIndex + 1);
                Vue.set(node, 'externalNo', nodeIndex + 1 + "");
            });
        },
        removeNode : function(nodeArr, index) {
            nodeArr.splice(index, 1);
        },
        addQuestion : function(typeInfo) {
            var addQuestionCount = parseInt(typeInfo.count);
            if (CommUtils.isEmpty(typeInfo) || addQuestionCount <= 0) {
                return;
            }
            var questionTypes = this.questionTypes;
            var kindNode = this.getNodeBySearchCode(this.curKindNodeSearchCode);
            kindNode.points = parseFloat(kindNode.points += addQuestionCount);
            var typeNode = null;
            $.each(kindNode.children, function(t, tNode) {
                if ((tNode.type && tNode.type.code == typeInfo.code) || (tNode.typeCode == typeInfo.code)) {
                    typeNode = tNode;
                    return false;
                }
            });
            if (typeNode == null) {
                var index = kindNode.children.length;
                $.each(kindNode.children, function(t, tNode) {
                    var flag = false;
                    $.each(questionTypes, function(q, qType) {
                        if ((tNode.type && tNode.type.code == qType.code) || (tNode.typeCode == qType.code)) {
                            return false;
                        }
                        if ((tNode.type && tNode.type.code == qType.code) || (tNode.typeCode == qType.code)) {
                            index = t;
                            flag = true;
                            return false;
                        }
                    });
                    if (flag) return false;
                });
                var typeInfoName = CommUtils.numberToChinese(kindNode.children.length + 1) + "、" + typeInfo.name;
                typeNode = EditPaperTemplatePage.newNode(Constants.EXAM_QUES_TYPE_LEVEL, typeInfoName, addQuestionCount);
                typeNode.parentId = kindNode.nodeId;
                typeNode.nodeSearchCode = kindNode.nodeSearchCode + '_' + typeNode.nodeId;
                typeNode.type.code = typeInfo.code;
                typeNode.type.name = typeInfo.name;
                kindNode.children.push(typeNode);
            } else {
                typeNode.points = parseFloat(typeNode.points += addQuestionCount);
            }

            for (var i = 0; i< addQuestionCount; i++) {
                var quesNode = EditPaperTemplatePage.newNode(Constants.EXAM_QUES_LEVEL, '',1, typeNode.children.length + 1, typeNode.children.length + 1 + "");
                quesNode.parentId = typeNode.nodeId;
                quesNode.nodeSearchCode = typeNode.nodeSearchCode + '_' + quesNode.nodeId;
                quesNode.difficulty = {
                    'code' : '2',
                    'name' : EditPaperTemplatePage.difficulties['2']
                };
                typeNode.children.push(quesNode);
            }
            this.paper.totalPoints += typeNode.points;
            EditPaperTemplatePage.initQuestionNo(kindNode);
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
        changeDifficulty : function(code, difficulty) {
            difficulty.name = EditPaperTemplatePage.difficulties[code];
            difficulty.code = code;
        },
        copyQuestionCondition : function(kNode, tNode, questions, qIndex) {
            if (parseFloat(questions[qIndex - 1].points) !== parseFloat(questions[qIndex].points)) {
                var subtractPoints = questions[qIndex - 1].points - questions[qIndex].points ;
                // 刷新上级分数
                var tempPoints = kNode.points + subtractPoints;
                Vue.set(kNode, 'points', parseFloat(tempPoints));
                tempPoints = tNode.points + subtractPoints;
                Vue.set(tNode, 'points', parseFloat(tempPoints));
                tempPoints = this.paper.totalPoints + subtractPoints;
                Vue.set(this.paper, 'totalPoints', parseFloat(tempPoints));
            }

            Vue.set(questions[qIndex], 'points', parseFloat(questions[qIndex - 1].points));
            Vue.set(questions[qIndex], 'difficulty', JSON.parse(JSON.stringify(questions[qIndex - 1].difficulty)));
            Vue.set(questions[qIndex], 'knowledges', JSON.parse(JSON.stringify(questions[qIndex - 1].knowledges)));

        },
        copyQuestion : function(kNode, tNode, questions, qIndex) {
            // 刷新上级分数
            var tempPoints = kNode.points += questions[qIndex].points;
            Vue.set(kNode, 'points', parseFloat(tempPoints));
            tempPoints = tNode.points += questions[qIndex].points;
            Vue.set(tNode, 'points', parseFloat(tempPoints));
            tempPoints = this.paper.totalPoints += questions[qIndex].points;
            Vue.set(this.paper, 'totalPoints', parseFloat(tempPoints));

            var qNode = JSON.parse(JSON.stringify(questions[qIndex]));
            qNode.nodeId = EditPaperTemplatePage.nodeSeq++;
            qNode.nodeSearchCode = qNode.nodeSearchCode.substring(0, qNode.nodeSearchCode.lastIndexOf('_') + 1) + qNode.nodeId;
            delete qNode.question;
            qNode.children = [];

            var lastInternalNo = questions.length + 1;
            var lastExternalNo = /^[0-9]+$/g.test(questions[questions.length - 1].externalNo) ? parseInt(questions[questions.length - 1].externalNo) + 1 + '' : '1';
            if (qIndex == questions.length - 1) {
                qNode.internalNo = lastInternalNo;
                qNode.externalNo = lastExternalNo;
            } else {
                qNode.internalNo = questions[qIndex + 1].internalNo;
                qNode.externalNo = questions[qIndex + 1].externalNo;
                for (var tmpIndex = qIndex + 1; tmpIndex + 1 < questions.length; tmpIndex++) {
                    this.changeQuestionNo(questions[tmpIndex], questions[tmpIndex + 1].externalNo, questions[tmpIndex + 1].internalNo);
                }
                questions[questions.length - 1].internalNo = lastInternalNo;
                questions[questions.length - 1].externalNo = lastExternalNo;
            }
            questions.splice(qIndex+1, 0, qNode);
        },
        removeQuestion : function(kNode, tNode, questions, qIndex) {
            // 刷新上级分数
            var tempPoints = kNode.points - questions[qIndex].points;
            Vue.set(kNode, 'points', parseFloat(tempPoints));
            tempPoints = tNode.points - questions[qIndex].points;
            Vue.set(tNode, 'points', parseFloat(tempPoints));
            tempPoints = this.paper.totalPoints - questions[qIndex].points;
            Vue.set(this.paper, 'totalPoints', parseFloat(tempPoints));

            if (tNode.children.length == 1) {
                var kindNode = this.getNodeBySearchCode(tNode.nodeSearchCode.split('_')[0]);
                var index = kindNode.children.indexOf(tNode);
                kindNode.children.splice(index, 1);

                // 刷新大题题目名称
                $.each(kindNode.children, function (index, tNode) {
                    tNode.name = CommUtils.numberToChinese(index + 1) + "、" + tNode.type.name;
                })
            } else {
                for (var tmpIndex = tNode.children.length - 1; tmpIndex > qIndex; tmpIndex--) {
                    this.changeQuestionNo(tNode.children[tmpIndex], tNode.children[tmpIndex - 1].externalNo, tNode.children[tmpIndex - 1].internalNo);
                }
                tNode.children.splice(qIndex, 1);
            }
            EditPaperTemplatePage.initQuestionNo(kNode);
        },
        knowledgeSplice: function(qNodeKnowledges, knowledgeIndex) {
            var treeObj = $('#knowledgeTree').jstree(true);
            treeObj.uncheck_node(qNodeKnowledges[knowledgeIndex].knowledgeId);
            qNodeKnowledges.splice(knowledgeIndex, 1);
        },
        chooseKnowledge : function(qNode, target) {
            var $this = $(target).closest('.l_3').find('.r_i');
            var $top = $this.offset().top;
            var $left = $this.offset().left + 14;
            var $w = $this.width();
            var $bodyH = $('body').height();
            var $treeH = $("#fixed-tree-box").height();
            var $windH = $(window).height();
            var s = $top + $treeH - $bodyH
            var top2 = 9;
            if (s>-38) {
                $top = $top - s - 80;
                top2 = s + 88;
            }else{
                top2 = 9;
                if(s>=-38){
                    console.log(s)
                    top2 = top2 + 42;
                    $top = $top - 42;
                }
            }
            console.log($top + '---' + $left + '---' + $w + '---' + $bodyH+ '---' + $treeH);
            $("#fixed-tree-box").css({'top':$top+'px','left':($left + $w)+'px'}); //设置知识点弹窗样式
            $("#fixed-tree-box .jiantou_").css({'top':top2+'px'}); //设置知识点弹窗三角样式

            var treeObj = $('#knowledgeTree').jstree(true);
            treeObj.uncheck_all();

            this.curChooseKnowledgeQues = qNode;
            treeObj.check_node($.map(qNode.knowledges, function(item){
                return item.knowledgeId;
            }));

            $("#fixed-tree-box").show();
        },
        getNodeBySearchCode : function(nodeSearchCode) {
            if (!this.nodeSearchCodeMap[nodeSearchCode]) {
                this.addTemplateKind();
                nodeSearchCode = this.curKindNodeSearchCode
            }
            return this.nodeSearchCodeMap[nodeSearchCode];
        },
        getParentNode : function(node) {
            var searchCode = node;
            if (typeof node == 'obj') {
                searchCode = node.nodeSearchCode;
            }
            if (searchCode.indexOf('_') < 0) {
                return null;
            }
            return this.nodeSearchCodeMap[searchCode.substring(0, searchCode.lastIndexOf('_'))];
        },
        saveAsPaperTemplate : function () {
            var _this = this;

            if (Object.keys(_this.nodeSearchCodeMap).length > 1) {
                //另存
                EditPaperTemplatePage.vm.layerIndex = layer.open({
                    type: 1,
                    title: i18n['save_other_paper_template'],
                    scrollbar: false,
                    shade: 0.6,
                    area: ['500px', '265px'],
                    content: $('#otherSaveTemplate') //iframe的url
                });
            } else {
                CommUtils.showFail(i18n['question_not_empty']);
            }
        },
        addPaperTemplate : function(event) {
            if (!CommUtils.tryLock(event.target)) {
                return;
            }
            var _this = this;
            // 保存模板至页面
            window.parent.EditPaperPage.vm.paperTemplate = EditPaperTemplatePage.vm.paper;
            var paperTemplate = _this.tranPaperToPaperTemplate(_this.paper);

            $.ajax({
                "url": EditPaperTemplatePage.addPaperTemplateUrl,
                "data": JSON.stringify(paperTemplate),
                contentType: "application/json;charset=utf-8",
                type: "post",
                success: function (result) {
                    if (result && result.paperTemplateId) {
                        CommUtils.showSuccess(i18n['paper_template_save_success']);
                        layer.close(_this.layerIndex);
                    } else {
                        CommUtils.showSuccess(i18n['paper_template_save_error']);
                    }
                },
                error: function (e) {
                    CommUtils.showSuccess(i18n['paper_template_save_exception']);
                }
            }).always(function() {
                CommUtils.unLock(event.target);
            });
        },
        tranPaperToPaperTemplate: function(paper) {
            var _this = this;
            var paperTemplate = {
                'paperTemplateName': paper.name,
                'stage': {
                    'code': paper.stageCode,
                    'name': paper.stageName
                },
                'grade': {
                    'code': paper.gradeCode,
                    'name': paper.gradeName
                },
                'subject': {
                    'code': paper.subjectCode,
                    'name': paper.subjectName
                },
                'materialVersion': paper.materialVersion,
                'totalPoints': paper.totalPoints,
                'nodes': []
            };

            var type = {};
            var nodeIndex = [];
            $.each(_this.nodeSearchCodeMap, function (index, node) {
                var level = node.level;

                if (nodeIndex.length < level) {
                    nodeIndex.push(0);
                } else {
                    nodeIndex[level-1] += 1;
                }

                if (node.level === 2) {
                    if (node.type) {
                        type = node.type;
                    } else if (node.typeCode) {
                        type = {
                            "code": node.typeCode,
                            "name": node.typeName
                        }
                    }
                } else if (node.level === 1) {
                    type = "";
                }

                var tempNode = _this.tranPaperNodeTemplate(node, nodeIndex[level-1], type);
                paperTemplate.nodes.push(tempNode);
            });
            return paperTemplate;
        },
        tranPaperNodeTemplate : function (node, sequencing, type) {

            // 知识点
            var knowledges = [];
            $.each(node.knowledges, function (index, knowledge) {
                knowledges.push({
                    'nodeId': node.nodeId,
                    'knowledgeId': knowledge.knowledgeId,
                    'knowledgeName': knowledge.knowledgeName,
                    'searchCode': knowledge.searchCode
                });
            });

            var PaperNodeTemplate = {
                'nodeId': node.nodeId,
                'name': node.name,
                'parentNodeId': node.parentId,
                'description': node.description ? node.description : '',
                'level': node.level,
                'sequencing': sequencing,
                'internalNo': node.internalNo,
                'externalNo': node.externalNo,
                'points': node.points
            };

            if (node.level === 2) {
                PaperNodeTemplate.type = EditPaperTemplatePage.questionAllTypes[type.code];
            }

            if (node.level === 3) {
                PaperNodeTemplate.type = EditPaperTemplatePage.questionAllTypes[type.code];
                PaperNodeTemplate.difficulty = node.difficulty;
                PaperNodeTemplate.knowledges = knowledges;
            }

            return PaperNodeTemplate
        },
        selectQuestion : function () {
            var _this = this;
            // 保存模板至页面
            if (Object.keys(_this.nodeSearchCodeMap).length > 1) {
                window.parent.EditPaperPage.vm.paperTemplate = EditPaperTemplatePage.vm.paper;
                _this.queryCountQuestionAllResource(
                    function (index) {
                        _this.questionResourceTypes = [];
                        var param = {
                            'stageCode': _this.paper.stageCode,
                            'gradeCode': _this.paper.gradeCode,
                            'subjectCode': _this.paper.subjectCode,
                            'materialVersion': _this.paper.materialVersion
                        };
                        $.get(EditPaperTemplatePage.countQuestionAllResourceUrl, param, function (data) {
                            if (data && data[0].providerCode) {
                                for (var i = 0; i < data.length; i++) {
                                    var questionResourceType = data[i];
                                    if (i === 0) {
                                        _this.curChooseQuestionResourceParam.providerCode = questionResourceType.providerCode;
                                    }
                                    questionResourceType["name"] = i18n[questionResourceType.providerCode];
                                    questionResourceType["url"]= EditPaperTemplatePage.questionResourceTypePrefixUrl+(i+1)+".png";
                                    _this.questionResourceTypes.push(questionResourceType);
                                }
                            } else {
                                layer.alert(i18n['query_question_count_error'], {
                                    icon: 5,
                                    title: i18n['prompt']
                                }, function () {
                                    layer.close(layer.index);
                                    layer.close(layer.index - 2);
                                });
                            }
                        }).fail(function () {
                            layer.alert(i18n['query_question_count_error'], {
                                icon: 5,
                                title: i18n['prompt']
                            }, function () {
                                layer.close(layer.index);
                                layer.close(layer.index - 2);
                            });
                        }).always(function () {
                            layer.close(index);
                        });
                    }
                );
            } else {
                CommUtils.showFail(i18n['question_not_empty']);
            }
        },
        queryCountQuestionAllResource: function(callback) {
            EditPaperTemplatePage.vm.layerIndex = layer.open({
                type: 1,
                title: i18n['choose_question_source'],
                scrollbar: false,
                shade: 0.6,
                area: ['500px', '330px'],
                content: $('#selectQuestionTemplate'), //iframe的url
            });
            callback(CommUtils.showLoad(i18n['query_question']));
        },
        changeQuestionResource: function(providerCode) {
            this.curChooseQuestionResourceParam.providerCode = providerCode;
        },
        searchQuestion: function() {
            var _this = this;
            // 判断当前题库是否有题目
            var resourceNotQuestions = false;
            $.each(_this.questionResourceTypes, function (index, questionResourceType) {
                if (questionResourceType.providerCode === _this.curChooseQuestionResourceParam.providerCode) {
                    if (questionResourceType.count === 0) {
                        CommUtils.showFail(i18n['resource_not_questions']);
                        resourceNotQuestions = true;
                        return false;
                    }
                }
            });

            if (resourceNotQuestions) {
                return;
            }

            var searchQuestionMap = {};
            // 合并题型
            for(var nodeKey in _this.nodeSearchCodeMap) {
                var node = _this.nodeSearchCodeMap[nodeKey];
                // 合并条件
                if (nodeKey.lastIndexOf("_") > 2 && node.level === 3) {
                    var typeCode;
                    var parentNode = _this.nodeSearchCodeMap[nodeKey.substring(0, nodeKey.lastIndexOf("_"))];
                    if (parentNode.type) {
                        typeCode = parentNode.type.code;
                    } else if (parentNode.typeCode) {
                        typeCode = parentNode.typeCode;
                    }

                    var knowledges = "";
                    $.each(node.knowledges, function (index, knowledge) {
                        if (knowledge.searchCode) {
                            knowledges += knowledge.searchCode;
                            if (index !== node.knowledges.length-1) {
                                knowledges += ",";
                            }
                        }
                    });

                    var mapKey = typeCode + "_" + knowledges + "_" + node.difficulty.code;

                    if (searchQuestionMap[mapKey]) {
                        searchQuestionMap[mapKey].push(nodeKey);
                    } else {
                        searchQuestionMap[mapKey] = [nodeKey];
                    }
                }
            }

            var param = {
                'stageCode': EditPaperTemplatePage.vm.paper.stageCode,
                'gradeCode': EditPaperTemplatePage.vm.paper.gradeCode,
                'subjectCode': EditPaperTemplatePage.vm.paper.subjectCode,
                'providerCode': _this.curChooseQuestionResourceParam.providerCode,
                'materialVersion': EditPaperTemplatePage.vm.paper.materialVersion,
                'searchQuestionMap': searchQuestionMap
            };
            window.parent.getRecommendationQuestion(param, EditPaperTemplatePage);
        }
    }
};

var knowledgeSetting = {
    "core" : {
        "data" : [],
        "multiple" : true,
        "animation" : 0,
        "themes" : {
            "variant" : "large"
        }
    },
    "checkbox" : {
        "keep_selected_style" : false
    },
    "plugins" : [ "wholerow", "checkbox" ]
};
var EditPaperTemplatePage = {
    nodeSeq : 1,
    t : 0,
    init : function() {
        this.initPaperTemplate();
        // this.initPaper();
        this.initVue();
        this.initDifficulty();
        this.initQuestionTypeAll();
    },
    initVue : function(){
        // 题型为空, 弹出提示并关闭窗口
        if (CommUtils.isEmpty(this.questionTypes) || this.questionTypes.length === 0) {
            layer.alert(i18n['questionTypes_empty'], {
                btn: [i18n['confirm']]
            }, function () {
                window.parent.layer.closeAll();
            });
        }

        $.extend(VueConfig.data, {
            'paper' : CommUtils.clone(this.paper),
            'questionTypes' : this.questionTypes,
            'curKindNodeSearchCode' : this.paper.nodes[0].nodeSearchCode  //当前卷别
        });
        this.vm = new Vue(VueConfig);
        this.vm.$mount("#paperTemplate");
        $('.fixed-tree-box > .close_').on('click', function() { $('.fixed-tree-box').hide(); });
        $(window).on('click', function(e) {
            if ($('.fixed-tree-box').is(':visible')) {
                if ($(e.target).closest('.fixed-tree-box').length == 0
                    && $(e.target).closest('.l_3').length == 0 && !$(e.target).hasClass('l_3')) {
                    $('.fixed-tree-box').hide();
                }
            }
        });
    },
    initPaperTemplate: function() {
        var _this = this;
        var paperTemplate = window.parent.getPaperTemplate();

        paperTemplate.sgsTitle = (CommUtils.isEmpty(paperTemplate.stageName) ? '' : (paperTemplate.stageName + '-')) + (CommUtils.isEmpty(paperTemplate.gradeName) ? '' : (paperTemplate.gradeName + '-')) + CommUtils.ifEmpty(paperTemplate.subjectName);
        if (CommUtils.isEmpty(paperTemplate.materialVersion) && $('select[name="materialVersion"] option').length > 0) {
            paperTemplate.materialVersion = $('select[name="materialVersion"] option:first').attr('value');
        }

        if (!CommUtils.isEmpty(paperTemplate.nodes)) {
            $.each(paperTemplate.nodes, function(k, kNode){
                kNode.nodeId = _this.nodeSeq++;
                kNode.nodeSearchCode = "" + kNode.nodeId;
                if (CommUtils.isEmpty(kNode.children) || kNode.children && kNode.children.length < 1) {
                    return true;
                }
                $.each(kNode.children, function(t, tNode){
                    tNode.parentId = kNode.nodeId;
                    tNode.nodeId = _this.nodeSeq++;
                    tNode.nodeSearchCode = kNode.nodeSearchCode + '_' + tNode.nodeId;
                    tNode.isUnFold = true;

                    if (CommUtils.isEmpty(tNode.children) || tNode.children.length < 1) {
                        return true;
                    }
                    $.each(tNode.children, function(q, qNode){
                        qNode.parentId = tNode.nodeId;
                        qNode.nodeId = _this.nodeSeq++;
                        qNode.nodeSearchCode = tNode.nodeSearchCode + '_' + qNode.nodeId;

                        // qNode.difficulty 为空时 从试卷中获取
                        if (!qNode.difficulty && !CommUtils.isEmpty(qNode.question) && qNode.question.difficulty) {
                            qNode.difficulty = {
                                "code": qNode.question.difficulty.code
                            };
                        }

                        if (CommUtils.isEmpty(qNode.children) || qNode.children.length < 1) {
                            return true;
                        }
                        $.each(qNode.children, function(c, cNode){
                            cNode.parentId = qNode.nodeId;
                            cNode.nodeId = _this.nodeSeq++;
                            cNode.nodeSearchCode = qNode.nodeSearchCode + '_' + cNode.nodeId;
                        });
                    });
                });
            });
        }
        this.paper = paperTemplate;
    },
    initPaper : function() {
        var _this = this;
        var paper = window.parent.getPaperInfo();
        if (!CommUtils.isEmpty(paper.template)) {
            window.parent.mergePaperWithTemplate(paper.template);
        }
        paper.sgsTitle = (CommUtils.isEmpty(paper.stageName) ? '' : (paper.stageName + '-')) + (CommUtils.isEmpty(paper.gradeName) ? '' : (paper.gradeName + '-')) + CommUtils.ifEmpty(paper.subjectName);
        if (CommUtils.isEmpty(paper.materialVersion) && $('select[name="materialVersion"] option').length > 0) {
            paper.materialVersion = $('select[name="materialVersion"] option:first').attr('value');
        }

        if (!CommUtils.isEmpty(paper.nodes)) {
            $.each(paper.nodes, function(k, kNode){
                kNode.nodeId = _this.nodeSeq++;
                kNode.nodeSearchCode = "" + kNode.nodeId;
                if (CommUtils.isEmpty(kNode.children) || kNode.children.length < 1) {
                    return true;
                }
                $.each(kNode.children, function(t, tNode){
                    tNode.parentId = kNode.nodeId;
                    tNode.nodeId = _this.nodeSeq++;
                    tNode.nodeSearchCode = kNode.nodeSearchCode + '_' + tNode.nodeId;
                    tNode.isUnFold = true;

                    if (CommUtils.isEmpty(tNode.children) || tNode.children.length < 1) {
                        return true;
                    }
                    $.each(tNode.children, function(q, qNode){
                        qNode.parentId = tNode.nodeId;
                        qNode.nodeId = _this.nodeSeq++;
                        qNode.nodeSearchCode = tNode.nodeSearchCode + '_' + qNode.nodeId;

                        if (!CommUtils.isEmpty(qNode.question)) {
                            qNode.difficulty = {
                                "code": qNode.question.difficulty.code
                            };
                        }

                        if (CommUtils.isEmpty(qNode.children) || qNode.children.length < 1) {
                            return true;
                        }
                        $.each(qNode.children, function(c, cNode){
                            cNode.parentId = qNode.nodeId;
                            cNode.nodeId = _this.nodeSeq++;
                            cNode.nodeSearchCode = qNode.nodeSearchCode + '_' + cNode.nodeId;
                        });
                    });
                });
            });
        }
        this.paper = paper;
    },
    initDifficulty : function() {
        var obj = {};
        $.each(this.difficulties, function(d, difficulty) {
            obj[difficulty.code] = difficulty.name;
        });
        this.difficulties = obj;
    },
    initQuestionTypeAll: function() {
        var obj = {};
        $.each(window.parent.getPaperQuestions(), function (index, questionType) {
            obj[questionType.typeCode] = {
                'name' : questionType.typeName,
                'code' : questionType.typeCode
            };
        });
        this.questionAllTypes = obj;
    },
    confirmBox : function(content, callback, cancel) {
        var tip = layer.confirm(content, {
            title: i18n['message'],
            anim: -1,//无动画
            closeBtn: 0,
            time: 2500,
            closeBtn: 0,
            btn: [i18n['confirm'], i18n['cancel']] //按钮
        }, function () {
            callback && callback();
            layer.close(tip);
        }, function() {
            layer.close(tip);
        });
    },
    tips : function(content, obj, direct) {
        var directNo = ['', 'up', 'right', 'down', 'left'].indexOf(CommUtils.ifEmpty(direct, 'down'));
        layer.tips(content, obj,{
            tips: [directNo, '#1b92e2'],
            time: 1500,
            anim: 6
        });
    },
    newNode : function(level, name, points, internalNo, externalNo) {
        return {
            'level' : level,
            'internalNo' : internalNo,
            'externalNo' : externalNo,
            'name' : name,
            'points' : parseFloat(points) || 0,
            'description' : '',
            'isUnFold' : true, //题型节点使用
            'type' : {}, //题型节点使用
            'nodeSearchCode' : '',
            'nodeId' : this.nodeSeq++,
            'parentId' : '',
            'sequencing' : 0,
            'question' : {}, //题目节点使用
            'knowledges' : [], //题目节点使用
            'difficulty' : {}, //题目节点使用
            'children' : []
        };
    },
    initQuestionNo : function (kindNode) {
        var _this = this;
        var nodes = [];
        if (CommUtils.isNotEmpty(kindNode)) {
            $.each(kindNode.children,function (index,node) {
                if (CommUtils.isNotEmpty(node.children)) {
                    $.each(node.children,function (index1,node1) {
                        nodes.push(node1);
                    })
                }
            })
        }
        if(CommUtils.isNotEmpty(nodes)) {
            $.each(nodes,function (index2, node2) {
                node2.internalNo = index2 + 1;
                node2.externalNo = index2 + 1 + "";
            })
        }
    }
};