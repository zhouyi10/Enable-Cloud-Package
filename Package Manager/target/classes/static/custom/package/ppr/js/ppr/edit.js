;(function(mainPage) {
    // when edit, extend some addition method
    $.extend(mainPage, {
        refreshQuestionNo: function(quesNode) {
            $.each(quesNode.questionPositionArr, function(q, questionPosition) {
                questionPosition.refreshQuestionNo();
            });
            this.cardArea.refreshCardQuestionNo(quesNode);
            if (this.questionNavTree) {
                var nodes = this.questionNavTree.getNodesByParam('id', quesNode.id);
                if (nodes.length > 0) {
                    $('#' + nodes[0].tId + '_span').text(quesNode.content);
                }
            }
        },
        refreshQuesNav: function() { // refresh nav tree
            this.questionNavTree && this.questionNavTree.destroy();
            var _this = this;
            var data = $.map(_this.typeNodeArr, function(type) {
                var children = $.map(type.children, function(child) {
                    return {id: child.id, name: child.content};
                })
                return {id: type.id, name: type.content, children: children, open: true};
            });
            var setting = {
                view: {
                    showLine: false,
                    addHoverDom: function(treeId, treeNode) {
                        if (treeNode.level == 0) return;
                        var $item = $("#" + treeNode.tId + "_a");
                        if ($item.find('.fa-crop').length > 0) return;

                        var editStr = '<span class="fa fa-crop" onclick="MainPage.startSelectQuestion(\''+treeNode.id+'\')" title="框选题目"></span><span class="fa fa-file-text-o" onclick="MainPage.showQuestionInfo(\''+treeNode.id+'\')" title="题目详情"></span>'
                        $item.append(editStr);
                    },
                    removeHoverDom: function(treeId, treeNode) {
                        if (treeNode.level == 0) return;
                        var $item = $("#" + treeNode.tId + "_a");
                        $item.find('.fa-crop').remove();
                        $item.find('.fa-file-text-o').remove();
                    }
                },
                callback: {
                    onClick: function(event, treeId, treeNode, clickFlag) {
                        if (treeNode.level == 0) {
                            _this.setUnSelected();
                        } else {
                            var id = treeNode.id;
                            _this.setSelected(id);
                        }
                    }
                }
            };
            this.questionNavTree = $.fn.zTree.init($('#nav-tree'), setting, data);
        },
        startSelectQuestion: function(quesNode) {
            quesNode = typeof quesNode == 'string' ? this.questionNodeMap[quesNode] : quesNode;
            var _this = this;
            _this.$container.addClass('_lock_exam');
            _this.cardArea.setSelected(quesNode);
            _this.examArea.beginSelectQuestion(quesNode).done(function() {
                _this.examArea.endSelectQuestion();
                _this.cardArea.setUnSelected(quesNode);
                _this.cardArea.refreshRelated(quesNode);
            });
        },
        showQuestionInfo: function(quesNode) {
            quesNode = typeof quesNode == 'string' ? this.questionNodeMap[quesNode] : quesNode;
            this.questionArea && this.questionArea.show(quesNode);
        },
        startRelateQuestion: function(questionPosition) {
            var _this = this;
            _this.$container.addClass('_lock_card');
            $('.card-ques-area:not(.related) .fa-check-circle-o').show();
            $('.card-ques-area.related').addClass('disable');
            this.curQuestionPosition = questionPosition;
            $(window).on('mouseup', function(e) {
                if (!$(e.target).hasClass('fa-check-circle-o') && !$(e.target).hasClass('fa-check-circle')) {
                    $(window).unbind('mouseup');
                    _this.$container.removeClass('_lock_card');
                    $('.card-ques-area:not(.related) .fa-check-circle-o').hide();
                    $('.card-ques-area.related').removeClass('disable');
                }
            });
        },
    });

    // when save, valid and rebuild data, finally save
    $.extend(mainPage, {
        valid: function() {
            if (CommUtils.isEmpty(this.baseInfo.name) || CommUtils.isEmpty(this.baseInfo.gradeCode) || CommUtils.isEmpty(this.baseInfo.subjectCode) ) {
                CommUtils.tipBoxV2('未设置试卷基础信息！', 3000);
                return false;
            }
            var quesIds = $.map(this.questionNodeMap, function(key, value) { return key;});
            if (quesIds.length == 0) {
                CommUtils.tipBoxV2('未设置答题卡信息！', 3000);
                return false;
            }
            return true;
        },
        buildDataV2: function() {
            return CommUtils.formatJson2FormParam(this.buildData());
        },
        buildData: function() {
            var _this = this;
            var param = $.extend({}, _this.baseInfo);
            if (CommUtils.isNotEmpty(this.paperId)) {
                param.paperId = this.paperId;
            }
            param.nodes = [];
            param.totalPoints = 0;
            param.userId = _this.userId;
            param.files = _this.examArea.getFiles();;
            var id = 1;
            var defaultKindNode = _this.newNode(id, null, 1, id++, 1, '', '');
            param.nodes.push(defaultKindNode);
            var cardAxises = [];
            $.each(_this.typeNodeArr, function(t, typeNode) {
                var defaultTypeNode = _this.newNode(id, defaultKindNode.nodeId, 2, id++, t + 1, '', typeNode.content);
                param.nodes.push(defaultTypeNode);
                $.each(typeNode.children, function(q, quesNode) {
                    var defaultQuestionNode = _this.newNode(id, defaultTypeNode.nodeId, 3, id++, q + 1, quesNode.content, '');
                    defaultQuestionNode.points = quesNode.points;
                    var quesAxises = $.map(quesNode.questionPositionArr, function(questionPosition) {
                        return {
                            fileId: questionPosition.parent.file.fileId,
                            xAxis: questionPosition._area.left,
                            yAxis: questionPosition._area.top,
                            width: questionPosition._area.width,
                            height: questionPosition._area.height,
                        };
                    });
                    var answer = quesNode._type == 'blank' ? quesNode.answer.join('@#@') : quesNode.answer.join(',');
                    defaultQuestionNode.question = {
                        'questionId': defaultQuestionNode.nodeId,
                        'type': { 'code': quesNode.typeCode, 'name': quesNode.typeName },
                        'stage': { 'code': _this.baseInfo.stageCode, 'name': _this.baseInfo.stageName },
                        'grade': { 'code': _this.baseInfo.gradeCode, 'name': _this.baseInfo.gradeName },
                        'subject': { 'code': _this.baseInfo.subjectCode, 'name': _this.baseInfo.subjectName },
                        'ability': { 'code': quesNode.ability.code, 'name': quesNode.ability.name },
                        'difficulty': { 'code': quesNode.difficulty.code, 'name': quesNode.difficulty.name },
                        'knowledges': quesNode.knowledges.length == 0 ? param.knowledges : quesNode.knowledges,
                        'stem': { 'richText': quesNode.stem || '', 'plaintext': '' },
                        'answer': { 'label': answer, 'strategy': answer, 'analysis': quesNode.analysis || '' },
                        'options': quesNode.options,
                        'axises': quesAxises,
                        'points': quesNode.points
                    };
                    param.nodes.push(defaultQuestionNode);
                    defaultTypeNode.points += defaultQuestionNode.points;

                    $.each(quesNode._blocks, function(_b, blk) {
                        var cardAxis = CommUtils.clone(blk);
                        $.extend(cardAxis, {
                            'nodeId': defaultQuestionNode.nodeId,
                            'questionId': defaultQuestionNode.nodeId,
                            'sequencing': _b + 1,
                            'typeCode': quesNode.typeCode,
                            'typeName': quesNode.typeName
                        });
                        if (quesNode._type == 'choose') {
                            cardAxis.optionCount = 4;
                        }
                        cardAxises.push(cardAxis);
                    });
                });
                defaultKindNode.points += defaultTypeNode.points;
            });
            param.totalPoints += defaultKindNode.points;
            param.answerCard = {
                'columnType': 1,
                'candidateNumberEdition': 1,
                'pageType': 'A4',
                'judgeStyle': '0',
                'sealingLineStatus': '0',
                'questionContentStatus': '0',
                'sealingLineStatus': '1',
                'status': '0',
                'creator': _this.userId,
                'pageCount': _this.cardArea.cards.length,
                'axises': cardAxises
            }
            return param;
        },
        save: function() {
            if (!this.valid()) {
                return;
            }
            CommUtils.showMask();
            var _this = this, data = _this.buildDataV2();
            $.post(_this.saveUrl, data).done(function(result) {
                CommUtils.tipBoxV2("保存成功！", 3000, function() {
                    window.location.assign(_this.listUrl);
                });
            }).fail(function() {
                CommUtils.tipBoxV2("保存失败，请重试！", 3000);
            }).always(function() {
                CommUtils.closeMask();
            });
        },
        newNode: function(id, parentNodeId, level, sequencing, internalNo, externalNo, name) {
            return {
                nodeId: id,
                parentNodeId: parentNodeId,
                sequencing: sequencing,
                level: level,
                internalNo: internalNo,
                externalNo: externalNo || '',
                name: name || '',
                points: 0
            };
        }
    });
})(window.MainPage);
;(function(mainPage) {
    var knowledgeTreeSetting = {
        view: { showLine: false },
        check: {
            enable: true,
            chkboxType: { "Y": "", "N": "" },
            chkStyle: "checkbox"
        },
        data: {
            simpleData: { enable: true }
        }
    };

    var questionArea = {
        curQuestion: null,
        init: function() {
            this.$container = $('.ques-panel');
            this.initEvent();
            this.refreshTree();
        },
        initEvent: function() {
            var _this = this;
            _this.$container.find('span.difficult').on('click', function() {
                if ($(this).hasClass('active')) return;
                $(this).addClass('active').siblings().removeClass('active');
            });
            _this.$container.find('span._sure').on('click', function() {
                var quesNo = $.trim(_this.$container.find('.question-no').val());
                if (_this.curQuestion.content != quesNo) {
                    _this.curQuestion.content = quesNo;
                    setTimeout(function() {
                        mainPage.refreshQuestionNo(_this.curQuestion);
                    }, 5);
                }
                _this.curQuestion.points = parseFloat($.trim(_this.$container.find('.points').val()) || '0');
                _this.curQuestion.analysis = _this.$container.find('.analysis-area textarea').val();
                _this.curQuestion.knowledges = _this._getCheckedKnowledgePoints();
                _this.curQuestion.answer = _this._getAnswer();
                var selectedDifficult = _this.$container.find('span.difficult.active');
                if (selectedDifficult) {
                    _this.curQuestion.difficulty = { code: selectedDifficult.attr('data-key'), name: selectedDifficult.attr('data-name') };
                }
                var selectedAbility = _this.$container.find('span.ability.active');
                if (selectedAbility) {
                    _this.curQuestion.ability = { code: selectedAbility.attr('data-key'), name: selectedAbility.attr('data-name') };
                }
                _this._hide();
            });
        },
        show: function(quesNode) {
            this.$container.find('.question-no').val(quesNode.content);
            this.$container.find('.points').val(quesNode.points);
            this.$container.find('.question-type').text(quesNode.typeName);
            this.$container.find('span.difficult[data-key="'+quesNode.difficulty.code+'"]').addClass('active').siblings().removeClass('active');
            this.$container.find('span.ability').removeClass('active');
            if (CommUtils.isNotEmpty(quesNode.ability)) {
                this.$container.find('span.ability[data-key="'+quesNode.ability.code+'"]').addClass('active');
            }
            this.$container.find('.analysis-area textarea').val(quesNode.analysis);
            this._clearCheckedKnowledgePoints();
            this._initAnswerArea(quesNode);
            this._checkKnowledgePoints(quesNode.knowledges);
            this._show();
            this.curQuestion = quesNode;
        },
        refreshAbility: function(subjectCode) {
            subjectCode = subjectCode || mainPage.baseInfo.subjectCode;
            this.$container.find('.ability-area btn').remove();
            if (CommUtils.isEmpty(subjectCode)) {
                return;
            }
            var _this = this;
            $.get(mainPage.getAbilityUrl, {subjectCode: subjectCode}, function(data) {
                if (data.status == '1' && CommUtils.isNotEmpty(data.data) ) {
                    data = data.data;
                } else {
                    data = [];
                }
                var html = '', template = '<span class="btn btn-toggle ability" data-key="{code}" data-name="{name}">{name}</span>';
                $.each(data, function(i, ability) {
                    if (ability.code && ability.name) {
                        html += CommUtils.formatStr(template, ability);
                    }
                });
                _this.$container.find('.ability-area').append(html);
                _this.$container.find('.ability-area span.ability').on('click', function() {
                    if ($(this).hasClass('active')) return;
                    $(this).addClass('active').siblings().removeClass('active');
                });
            }).fail(function() {

            });
        },
        refreshTree: function(baseInfo) {
            if (this.knowledgeTreeObj) {
                this.knowledgeTreeObj.destroy();
                this.knowledgeTreeObj = null;
            }
            baseInfo = baseInfo || mainPage.baseInfo;
            if (CommUtils.isEmpty(baseInfo.gradeCode) || CommUtils.isEmpty(baseInfo.subjectCode) || CommUtils.isEmpty(baseInfo.materialVersion)) {
                return;
            }
            var _this = this;
            $.post(mainPage.getKnowledgeTreeUrl, {'gradeCode' : baseInfo.gradeCode, 'subjectCode' : baseInfo.subjectCode, 'materialVersion': baseInfo.materialVersion}, function(data){
                if (data.status == '1' && CommUtils.isNotEmpty(data.data) ) {
                    data = data.data;
                } else {
                    data = [];
                }
                data = $.map(data, function(item) {
                    item.id = item.knowledgeId; item.name = item.knowledgeName; item.pId = item.parentId;
                    return item;
                });

                _this.knowledgeTreeObj = $.fn.zTree.init($('#knowledge-area'), knowledgeTreeSetting, data);
            });
        },
        _getAnswer: function() {
            var answer = [];
            var $answerArea = this.$container.find('.answer-area');
            switch (this.curQuestion._type) {
                case 'choose':
                case 'judge':
                    $answerArea.find('.btn-toggle.active').each(function() {
                        answer.push($(this).attr('data-key'));
                    });
                    break;
                case 'blank':
                    $answerArea.find('input').each(function() {
                        answer.push($(this).val());
                    });
                    break;
                default:
                    answer.push($answerArea.find('textarea').val());
            }
            return answer;
        },
        _initAnswerArea: function(quesNode) {
            var $answerArea = this.$container.find('.answer-area');
            $answerArea.empty();
            switch (quesNode._type) {
                case 'choose':
                    quesNode.optionCount = Math.min(quesNode.optionCount || 4, 26);
                    var html = '';
                    for (var i = 0; i< quesNode.optionCount; i++) {
                        var code = String.fromCharCode(65 + i);
                        html += '<span class="btn btn-toggle choose" data-key="'+code+'">'+code+'</span>';
                    }
                    $answerArea.html(html);
                    $.each(quesNode.answer, function(a, ans) {
                        $answerArea.find('.choose[data-key="'+ans+'"]').addClass('active');
                    });
                    if (quesNode.typeCode == '02') {
                        $answerArea.find('.btn-toggle').on('click', function() {
                            $(this).toggleClass('active');
                        })
                    } else {
                        $answerArea.find('.btn-toggle').on('click', function() {
                            $(this).addClass('active').siblings().removeClass('active');
                        });
                    }
                    break;
                case 'judge':
                    $answerArea.html('<span class="btn btn-toggle judge" data-key="T">对</span><span class="btn btn-toggle judge" data-key="F">错</span>');
                    $answerArea.find('.judge[data-key="'+quesNode.answer[0]+'"]').addClass('active');
                    $answerArea.find('.btn-toggle').on('click', function() {
                        $(this).addClass('active').siblings().removeClass('active');
                    });
                    break;
                case 'blank':
                    var html = '';
                    for (var i = 0; i< quesNode.answer.length; i++) {
                        html += '<input type="text" class="blank" />';
                    }
                    $answerArea.html(html);
                    $answerArea.find('input').each(function(i) {
                        $(this).val(quesNode.answer[i]);
                    });
                    break;
                default:
                    $answerArea.html('<textarea class="default"></textarea>');
                    $answerArea.find('textarea').val(quesNode.answer[0]);
            }
        },
        _getCheckedKnowledgePoints: function() {
            if (!this.knowledgeTreeObj) return [];
            return $.map(this.knowledgeTreeObj.getCheckedNodes(true) || [], function(item) {
                return {
                    "knowledgeId": item.knowledgeId, "knowledgeName": item.knowledgeName,"materialVersion": item.materialVersion,"materialVersionName": item.materialVersionName, "searchCode": item.searchCode
                };
            });
        },
        _checkKnowledgePoints: function(knowledgePoints) {
            if (!this.knowledgeTreeObj) return false;
            var _this = this;
            $.each(knowledgePoints || [], function(k, know) {
                var nodes = _this.knowledgeTreeObj.getNodesByParam('id', know.knowledgeId);
                if (nodes) {
                    _this.knowledgeTreeObj.checkNode(nodes[0], true);
                    if (nodes[0].getParentNode()) {
                        _this.knowledgeTreeObj.expandNode(nodes[0].getParentNode(), true);
                    }
                }
            });
            return true;
        },
        _clearCheckedKnowledgePoints: function() {
            if (!this.knowledgeTreeObj) return false;
            var _this = this;
            _this.knowledgeTreeObj.expandAll(false);
            $.each(_this.knowledgeTreeObj.getCheckedNodes(true), function(n, node) {
                _this.knowledgeTreeObj.checkNode(node, false);
            });
            return true;
        },
        _hide: function() {
            layer.close(this.curLayer);
            this.curLayer = null;
        },
        _show: function() {
            var _this = this;
            _this.curLayer = layer.open({
                type: 1,
                shadeClose : true,
                title: '题目信息',
                area: ['790px', '90%'],
                content: $('.ques-panel'),
                end: function() {
                    _this.curQuestion = null;
                    _this._clearCheckedKnowledgePoints();
                }
            });
        }
    };
    mainPage.questionArea = questionArea;
})(MainPage);
;(function(mainPage) {
    $.extend(mainPage.cardArea, {
        initLayer: function() {
            this.$container.prepend('<div class="card-operation"></div>');
            var $operation = this.$container.find('.card-operation');
            var html = '', template = $('#card-ques-area-template').html();
            $.each(mainPage.questionNodeMap, function(q, quesNode) {
                var blk = quesNode._blocks[0];
                html += CommUtils.formatStr(template, quesNode.id, quesNode.questionPositionArr.length>0?'related':'', blk.xAxis, (blk.pageNo - 1) * (1125 + 10) + 1 + blk.yAxis, blk.width, blk.height);
            });
            $operation.append(html);
            var _this = this;
            $operation.find('.card-ques-area .fa-crop').on('click', function() {
                mainPage.startSelectQuestion($(this).closest('.card-ques-area').attr('data-ques-id'));
            });
            $operation.find('.card-ques-area .fa-file-text-o').on('click', function() {
                mainPage.showQuestionInfo($(this).closest('.card-ques-area').attr('data-ques-id'));
            });
            $operation.find('.fa-check-circle').on('click', function() {
                if ($('._lock_card').length > 0) {
                    $(this).next().show();
                }
                var quesId = $(this).closest('.card-ques-area').attr('data-ques-id');
                var quesNode = mainPage.questionNodeMap[quesId];
                $.each(quesNode.questionPositionArr, function(p, pos) {
                    pos.questionIdArr.splice(pos.questionIdArr.indexOf(quesId), 1);
                    pos.refreshQuestionNo();
                    if (pos.questionIdArr.length == 0) {
                        pos.setUnRelated();
                    }
                });
                quesNode.questionPositionArr.splice(0);
                _this.setUnRelated(quesNode);
            });
            $operation.find('.fa-check-circle-o').on('click', function() {
                $(this).hide();
                var quesId = $(this).closest('.card-ques-area').attr('data-ques-id');
                var quesNode = mainPage.questionNodeMap[quesId];
                mainPage.curQuestionPosition.questionIdArr.push(quesId);
                mainPage.curQuestionPosition.refreshQuestionNo();
                mainPage.curQuestionPosition.setRelated();
                quesNode.questionPositionArr.push(mainPage.curQuestionPosition);
                _this.setRelated(quesNode);
            });
        },
        setRelated: function(quesNode) {
            var $ques_area = this.$container.find('.card-ques-area[data-ques-id="'+quesNode.id+'"]');
            if (!$ques_area.hasClass('related'))
                $ques_area.addClass('related');
        },
        setUnRelated: function(quesNode) {
            var $ques_area = this.$container.find('.card-ques-area[data-ques-id="'+quesNode.id+'"]');
            if ($ques_area.hasClass('related'))
                $ques_area.removeClass('related');
        },
        refreshRelated: function(quesNode) {
            var $ques_area = this.$container.find('.card-ques-area[data-ques-id="'+quesNode.id+'"]');
            if (quesNode.questionPositionArr.length > 0) {
                $ques_area.addClass('related');
            } else {
                $ques_area.removeClass('related');
            }
        },
        setSelected: function(quesNode) {
            var $ques_area = this.$container.find('.card-ques-area[data-ques-id="'+quesNode.id+'"]');
            $ques_area.addClass('selected');
        },
        setUnSelected: function(quesNode) {
            var $ques_area = this.$container.find('.card-ques-area[data-ques-id="'+quesNode.id+'"]');
            $ques_area.removeClass('selected');
        },
    });
})(window.MainPage)