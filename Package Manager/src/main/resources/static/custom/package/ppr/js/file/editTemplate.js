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
        changeQuestionResource: function(providerCode) {
            this.curChooseQuestionResourceParam.providerCode = providerCode;
        },
        searchQuestion: function() {
            var _this = this;
            var param = {
                'stageCode': EditPaperTemplatePage.vm.paper.stageCode,
                'gradeCode': EditPaperTemplatePage.vm.paper.gradeCode,
                'subjectCode': EditPaperTemplatePage.vm.paper.subjectCode,
                //'providerCode': _this.curChooseQuestionResourceParam.providerCode,
                //'materialVersion': EditPaperTemplatePage.vm.paper.materialVersion,
            };
            window.parent.getRecommendationQuestion(param, EditPaperTemplatePage);
        },
        getSearchQuestionMap : function () {
            var _this = this;
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
            return searchQuestionMap;
        }
    }
};
var EditPaperTemplatePage = {
    nodeSeq : 1,
    t : 0,
    init : function() {
        this.initPaperTemplate();
        this.initVue();
        //this.initDifficulty();
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