var MainPage = window.MainPage = {
    zoomTool: null,
    examRuler: null,
    examArea: null,
    cardArea: null,
};
;(function(mainPage) {
    var zoomTool = {
        $zoomTarget: null, // the zoom target
        scale: 100, // default scale percent
        init: function(ele) { // init zoom info
            this.$container = $('.tool-zoom');
            this.$zoomTarget = $(ele);
            this.targetWidth = this.$zoomTarget.outerWidth(); // custom width, not 100 percent
            this.targetHeight = this.$zoomTarget.parent().height(); // just 100 percent
            this.initEvent();
            this.show();
            var scale = parseInt(this.$zoomTarget.parent().width() * 100 / this.targetWidth);
            this.scaleTo(scale);
        },
        show: function() {
            this.$container.show();
        },
        hide: function() {
            this.$container.hide();
        },
        reScaleLength: function(length) { // calculate length with scale
            return parseInt(parseInt(length) * 100 / this.scale);
        },
        initEvent: function() {
            var _this = this;
            _this.$container.find('i').on('click', function() { // control button
                switch ($(this).attr('class')) {
                    case 'zoom-in': _this.scale -= 10; break;
                    case 'zoom-out': _this.scale += 10; break;
                    case 'zoom-revert': _this.scale = 100; break;
                }
                _this.scaleTo();
            });
            _this.$container.parent().on('mousedown', function(e) { // drag parent panel
                var delta = {x: parseInt(e.clientX) - parseInt(_this.$container.parent().offset().left), y:  parseInt(e.clientY) - parseInt(_this.$container.parent().offset().top)};
                var moveEvent = function(e) {
                    _this.$container.parent().css({left: (e.clientX - delta.x) + 'px', top: (e.clientY - delta.y) + 'px' });
                }
                $(document.body).on('mousemove', moveEvent).one('mouseup', function(e) {
                    $(document.body).unbind('mousemove', moveEvent);
                });
            });
            $(window).on('resize', function() { // resize widow
                _this.targetHeight = _this.$zoomTarget.parent().height();
                _this.scaleTo();
            });
        },
        scaleTo: function(scale) { // scale target
            scale = scale ? scale : this.scale;
            scale = parseInt(scale / 10) * 10;
            this.scale = Math.min(Math.max(scale, 10), 180);
            this.$container.find('span').text(this.scale + '%');

            var left = - parseInt(this.targetWidth) / 200 * (100 - this.scale);
            var height = parseInt(parseInt(this.targetHeight) / this.scale * 100);
            var top = (parseInt(this.targetHeight) - height) / 2;
            this.$zoomTarget.css({
                'transform': 'scale(' + (this.scale/100) + ')',
                'left': left + 'px',
                'top': top + 'px',
                'height': height + 'px'
            });
        }
    }
    mainPage.zoomTool = zoomTool;
})(MainPage);
;(function(mainPage) {
    var examRuler = {
        rulerSelector: '.ruler-container .ruler-wrapper',
        shader: '.ruler-selected',
        length: 796,
        halfCursorWidth: 8 / 2 * 1.5,
        min: 10,
        max: 786,
        limitObjs: {},
        init: function() {
            this.resize();
            this.initEvent();
            this.show();
        },
        show: function() {
            $(this.toolSelector).show();
        },
        hide: function() {
            $(this.toolSelector).hide();
        },
        getSize: function() {
            return { left: this.min, right: this.max, width: this.max-this.min };
        },
        resize: function(min, max) { // resize the min and max value of ques area
            if (typeof min == "number" && min < this.length) {
                this.min = Math.max(min, 0);
            }
            if (typeof max == "number" && max > this.min) {
                this.max = Math.min(max, this.length);
            }
            var size = this.getSize();
            $(this.shader).css({left: size.left + 'px', width: size.width + 'px'});
            $('.ruler-limit[data-key="min"]').css('left',  (this.min - this.halfCursorWidth) + 'px');
            $('.ruler-limit[data-key="max"]').css('left', (this.max - this.halfCursorWidth) +'px');
            $.each(this.limitObjs, function(id, obj) {
                obj.refreshLeftAndWidth(size.left, size.width);
            });
        },
        addLimitObj: function(obj) { // add ques
            this.limitObjs[obj.id] = obj;
        },
        removeLimitObj: function(obj) { // remove ques
            delete this.limitObjs[obj.id];
        },
        initEvent: function() {
            var ruler = this;
            $('.ruler-limit').on('mousedown', function(e) { // adjust
                var isMinCursor = $(this).attr('data-key') == 'min';

                var moveFunc = function(e) {
                    var curValue = mainPage.zoomTool.reScaleLength(parseInt(e.clientX) - parseInt($(ruler.rulerSelector).offset().left));
                    if (isMinCursor) {
                        if (curValue < 0 || curValue > ruler.max - 30) {
                            return;
                        }
                        ruler.min = curValue;
                    } else {
                        if (curValue > ruler.length || curValue < ruler.min + 30) {
                            return;
                        }
                        ruler.max = curValue;
                    }
                    ruler.resize();
                };
                $(document.body).on('mousemove', moveFunc).one('mouseup', function(e) {
                    $(document.body).unbind('mousemove', moveFunc);
                });
            });
        }
    }
    mainPage.examRuler = examRuler;
})(MainPage);

;(function(mainPage) {
    mainPage.baseInfo = {
        "stageCode": "",
        "stageName": "",
        "gradeCode": "",
        "gradeName": "",
        "subjectCode": "",
        "subjectName": "",
        "name": "",
        "materialVersion": "",
        "knowledges": [ ]
    };
    mainPage.node = function(id, parentId) {
        this.id = id;
        this.parentId = parentId || null;
        this.index = null;
        this.content = '';
        this._type = '';
        this.typeCode = '';
        this.typeName = '';
        this.children = [];
        this.questionPositionArr = []; // ques objs in exam area
        this.cardPosition = null; // ques obj in card area;
        // attr in ques obj
        this.points = 1;
        this.question = '';
        this.options = null;
        this.difficulty = $.extend({}, mainPage.defaultDifficulty);
        this.ability = {};
        this.analysis = '';
        this.answer = [""];
        this.knowledges = [];

        // attr in ques card
        this.row = 1;
        this._blocks = [];
    }
    mainPage.node.prototype = {
        newOptions: function(optionCount) {
            var aCode= 65;
            var options = [];
            for (var i = 0; i < optionCount; i++) {
                options.push({'alias': String.fromCharCode(aCode + i), 'label': '', 'sequencing': i+1 })
            }
            this.options = options;
        }
    }
    $.extend(mainPage, {
        typeNodeArr: [],
        questionNodeMap: {},
        init: function() {
            this.initData();
            this.$container = $('.ppr-container');
            this.zoomTool.init('.container-wrapper');
            this.examRuler.init();
            this.examArea.init();
            this.cardArea.init();
            this.questionArea && this.questionArea.init();
            // mainPage.baseInfo = { "stageCode": "3", "stageName": "初中", "gradeCode": "3101", "gradeName": "七年级",  "subjectCode": "13", "subjectName": "语文", "name": "20201022七年级语文试卷01", "materialVersion": "VER131", "knowledges": [ ] };
            this.initBaseInfo();
            this.refreshCard();
            // this.examArea.addImage({ url: 'http://192.168.116.190/microservice/storageservice/v1/files/download/2020/10/13/b329587088954b3085b4db0a6ce4e082.jpg'});
            // this.refreshCard([{
            //     type: '01', typeName: '选择题', count: 10
            // }]);
            this.initEvent();
            this.initMessage();
        },
        initData: function() {
            var _this = this;
            _this.defaultDifficulty = {};
            if (CommUtils.isNotEmpty(_this.difficultyArr)) {
                var centerIdx = (_this.difficultyArr.length + 1) / 2 - 1;
                this.defaultDifficulty = { code: _this.difficultyArr[centerIdx].code, name: _this.difficultyArr[centerIdx].name };
            }
            _this.baseInfo = {
                "stageCode": _this.pprInfo.stageCode || '',
                "stageName":  _this.pprInfo.stageName || "",
                "gradeCode":  _this.pprInfo.gradeCode || "",
                "gradeName":  _this.pprInfo.gradeName || "",
                "subjectCode":  _this.pprInfo.subjectCode || "",
                "subjectName":  _this.pprInfo.subjectName || "",
                "name":  _this.pprInfo.name || "",
                "materialVersion":  _this.pprInfo.materialVersion || "",
                "knowledges": _this.pprInfo.knowledges == null ? [] : CommUtils.clone(_this.pprInfo.knowledges)
            }
            if (CommUtils.isEmpty(_this.pprInfo.paperId)) {
                if (CommUtils.isNotEmpty(_this.teacherBaseInfoStr)) {
                    var baseInfo = JSON.parse(_this.teacherBaseInfoStr);
                    $.each(baseInfo, function(key, value) {
                        if (CommUtils.isEmpty(_this.baseInfo[key]) && CommUtils.isNotEmpty(value)) {
                            _this.baseInfo[key] = value;
                        }
                    });
                    if (CommUtils.isEmpty(_this.baseInfo.name)) {
                        var date = CommUtils.formatDate(new Date(), 'yyyyMMdd');
                        _this.baseInfo.name = CommUtils.formatStr("{0}{1}{2}{3}" + i18n['paper'], date, _this.baseInfo.stageName, _this.baseInfo.gradeName, _this.baseInfo.subjectName);
                    }
                }
                return;
            }
            this.paperId = _this.pprInfo.paperId;
            var answerCardRowMap = {};
            $.each(this.pprInfo.answerCard.axises, function(a, axis) {
                if (typeof answerCardRowMap[axis.nodeId] == 'undefined') {
                    answerCardRowMap[axis.nodeId] = axis.rowCount;
                } else {
                    answerCardRowMap[axis.nodeId] += axis.rowCount;
                }
            });
            var typeNodeMap = {}, typeNodeArr = [], questionNodeMap = {};
            $.each(_this.pprInfo.nodes, function(n, node) {
                if (node.level == 2) {
                    var typeNode = new mainPage.node(node.nodeId);
                    typeNode.content = node.name;
                    typeNode.index = node.internalNo;

                    typeNodeMap[node.nodeId] = typeNode;
                    typeNodeArr.push(typeNode);
                } else if (node.level == 3) {
                    var typeNode = typeNodeMap[node.parentNodeId];
                    var quesNode = new mainPage.node(node.nodeId);
                    quesNode.index = node.internalNo;
                    quesNode.content = node.externalNo;
                    quesNode.typeCode = node.question.type.code;
                    quesNode.typeName = node.question.type.name;
                    quesNode._type = _this.getType(quesNode.typeCode, _this.baseInfo.subjectCode);
                    quesNode.options = node.question.options;
                    quesNode.points = node.points;
                    quesNode.question = node.question.stem.richText;
                    if (node.question.difficulty != null && CommUtils.isNotEmpty(node.question.difficulty.code)) {
                        quesNode.difficulty = $.extend({}, node.question.difficulty);
                    }
                    if (node.question.ability != null && CommUtils.isNotEmpty(node.question.ability.code)) {
                        quesNode.ability = $.extend({}, node.question.ability);
                    }
                    var answer = node.question.answer.label || '';
                    if (quesNode._type == 'choose') {
                        quesNode.answer = answer.split(',');
                    } else if (quesNode._type == 'blank') {
                        quesNode.answer = answer.split('@#@');
                    } else {
                        quesNode.answer = [ answer ];
                    }

                    quesNode.analysis = node.question.answer.analysis || '';
                    if (CommUtils.isNotEmpty(node.question.knowledges)) {
                        quesNode.knowledges = CommUtils.clone(node.question.knowledges);
                    }
                    quesNode.row = answerCardRowMap[quesNode.id] || 1;

                    typeNode._type = quesNode._type;
                    typeNode.typeCode = quesNode.typeCode;
                    typeNode.typeName = quesNode.typeName;
                    typeNode.children.push(quesNode);
                    questionNodeMap[quesNode.id] = quesNode;
                }
            });
            _this.questionNodeMap = questionNodeMap;
            _this.typeNodeArr = typeNodeArr;

            var questionPositions = {};
            var rulerLimit = null;
            $.each(_this.pprInfo.nodes, function(n, node) {
                if (node.level == 3) {
                    $.each(node.question.axises, function(a, axis) {
                        axis.xAxis = axis.xAxis || axis.xaxis;
                        axis.yAxis = axis.yAxis || axis.yaxis;
                        var key = '_' + axis.yAxis + '_' + axis.xAxis + '_' + axis.width + '_' + axis.height;
                        rulerLimit = {left: axis.xAxis, width: axis.width};
                        if (questionPositions[axis.fileId]) {
                            if (questionPositions[axis.fileId][key]) {
                                questionPositions[axis.fileId][key].questionIdArr.push(node.nodeId);
                                return true;
                            }
                        } else {
                            questionPositions[axis.fileId] = {};
                        }
                        questionPositions[axis.fileId][key] = {
                            'left': axis.xAxis, 'top': axis.yAxis,
                            'width': axis.width, 'height': axis.height,
                            questionIdArr: [ node.nodeId ]
                        };
                    });
                }
            });
            $.each(_this.pprInfo.files || [], function(f, file) {
                var positions = $.map(questionPositions[file.fileId] || {}, function(value,key) {return value;});
                _this.examArea.addImage(file, positions);
            });
            if (rulerLimit != null) {
                mainPage.examRuler.min = rulerLimit.left;
                mainPage.examRuler.max = rulerLimit.left + rulerLimit.width;
            }
        },
        initMessage: function() {
            var _this = this;
            CommUtils.onMessage(function(e) {
                let data = e.data;
                if (data.type == 'close') {
                    layer.closeAll();
                } else if (data.type == 'editBaseInfo') {
                    data = data.data;
                    _this.baseInfo = data;
                    _this.initBaseInfo();
                } else if (data.type == 'editCardInfo') {
                    _this.refreshCard(data.data);
                    layer.closeAll();
                }
            });
        },
        initEvent: function(){
            var _this = this;
            $('#baseInfo').on('click', function() {
                var url = CommUtils.formatStr('{0}?userId={1}&stageCode={2}&gradeCode={3}&subjectCode={4}&materialVersion={5}', _this.baseInfoUrl, _this.userId, _this.baseInfo.stageCode, _this.baseInfo.gradeCode, _this.baseInfo.subjectCode, _this.baseInfo.materialVersion);
                url += $.map(_this.baseInfo.knowledges, function(know) {return '&searchCodes=' + know.knowledgeId;}).join('');
                url += '&name=' + encodeURIComponent(_this.baseInfo.name);
                CommUtils.openFrame(i18n['base-info-layer-title'], url, '700px');
            });
            $('#cardInfo').on('click', function() {
                var json = $.map(_this.typeNodeArr, function(ty) { return {type: ty.typeCode, count: ty.children.length}; });
                CommUtils.openFrame(i18n['answer-card-layer-title'], _this.answerCardUrl, '700px',  '90%',function(layero, index) {
                    var iframeWin = layero.find('iframe')[0].contentWindow;
                    if (iframeWin) {
                        iframeWin.postMessage(json, '*');
                    }
                });
            });
            $('#chooseExamPic').on('click', function() {
                UploadFileUtils.uploadFile({
                    beforeAddFile: function(file) {
                        var isImage = file.type.indexOf('image') == 0;
                        isImage && CommUtils.showMask();
                        return isImage;
                    },
                    uploadProgress: function(file, percent) {

                    },
                    uploadSuccess: function(file, result) {
                        result.url = result.downloadUrls[0];
                        result.fileName = result.name;
                        result.fileExt = file.ext;
                        MainPage.examArea.addImage(result).init();
                    },
                    uploadError: function(file, errorCode) {

                    },
                    uploadComplete: function(file) {
                        CommUtils.closeMask();
                    }
                });
            });
            $('.box-header.toggle').on('click', function() {
                $(this).parent().toggleClass('active');
            });
            $('#save').on('click', function() {
                _this.save();
            });
        },
        refreshCard: function(json) {// refresh card
            if (json) {
                this._removeOldNodes(json);
                this._rebuildNodes(json);
            }

            this.refreshQuesNav && this.refreshQuesNav();
            this.examArea.refresh();
            this.cardArea.refresh();
        },
        _rebuildNodes: function(json) {
            var originTypeNodes = {};
            $.each(this.typeNodeArr || [], function(n, node) {
                originTypeNodes[node.typeCode] = node;
            });

            var _this = this, typeNodeArr = [], questionNodeMap = {};
            $.each(json, function(t, type) {
                if (type.count == 0) {
                    return true;
                }
                var typeNode = originTypeNodes[type.type];
                if (typeof typeNode == 'undefined') {
                    typeNode = new mainPage.node(_this.guid('type'));
                    typeNode.typeCode = type.type;
                    typeNode._type = _this.getType(typeNode.typeCode, _this.baseInfo.subjectCode);
                    typeNode.typeName = type.typeName;
                }
                typeNode.index = t + 1;
                typeNode.content = type.typeName;
                typeNodeArr.push(typeNode);

                if (type.count > typeNode.children.length) {
                    for (var i = typeNode.children.length; i < type.count; i++) {
                        var quesNode = new mainPage.node(_this.guid('ques'), typeNode.id);
                        quesNode.index = i + 1;
                        quesNode._type = typeNode._type;
                        switch (quesNode._type) {
                            case 'choose':
                                quesNode.newOptions(4);
                                break;
                            case 'chinese-article':
                                quesNode.row = parseInt((800 - 1) / 23) + 1;
                                break;
                            case 'english-article':
                                quesNode.row = 10;
                                break;
                            case 'default':
                                quesNode.row = 5;
                                break;
                        }
                        if (quesNode._type == 'choose') {
                            quesNode.newOptions(4);
                        }
                        quesNode.typeCode = typeNode.typeCode;
                        quesNode.typeName = typeNode.typeName;
                        quesNode.content = '' + quesNode.index;
                        quesNode.knowledges = CommUtils.clone(_this.baseInfo.knowledges);
                        typeNode.children.push(quesNode);
                    }
                }
                $.each(typeNode.children, function(q, ques) {
                    questionNodeMap[ques.id] = ques;
                });
            });
            _this.questionNodeMap = questionNodeMap;
            _this.typeNodeArr = typeNodeArr;
        },
        _removeOldNodes: function(json) { // before refresh card, remove unrelated node
            var removeQuestionNodeArr = [];
            var newTypeArr = [];
            var newTypeMap = {};
            $.each(json, function(t, type) {
                newTypeArr.push(type.type);
                newTypeMap[type.type] = type;
            });
            $.each(this.typeNodeArr || [], function(n, node) {
                if (newTypeArr.indexOf(node.typeCode) >= 0) { // type node exists
                    var newCount = newTypeMap[node.typeCode].count;
                    var oldCount = node.children.length;
                    if (newCount < oldCount) { // the questions has added
                        removeQuestionNodeArr = removeQuestionNodeArr.concat(node.children.splice(newCount, oldCount - newCount));
                    }
                } else { // type node is removed
                    removeQuestionNodeArr = removeQuestionNodeArr.concat(node.children);
                    delete node;
                }
            });
            $.each(removeQuestionNodeArr, function(n, node) {
                $.each(node.questionPositionArr, function(q, _q) {
                    _q.parent.removeQues(_q);
                });
            });
        },
        setUnSelected: function() {
            if (!this.selectedQuesNode) {
                return;
            }
            if (this.selectedQuesNode.questionPositionArr.length > 0) {
                this.selectedQuesNode.questionPositionArr[0].setUnSelected();
            }
            this.cardArea.setUnSelected(this.selectedQuesNode);
            this.selectedQuesNode = null;
        },
        setSelected: function(quesNode) {
            quesNode = typeof quesNode == 'string' ? this.questionNodeMap[quesNode] : quesNode;

            this.setUnSelected();
            this.selectedQuesNode = quesNode;
            if (this.selectedQuesNode.questionPositionArr.length > 0) {
                this.selectedQuesNode.questionPositionArr[0].setSelected();
            }
            this.cardArea.setSelected(this.selectedQuesNode);
        },
        initBaseInfo: function() {
            var data = this.baseInfo;
            $('.ppr-header-info').removeClass('no-content').empty();
            if (CommUtils.isEmpty(data.name)) {
                $('.ppr-header-info').addClass('no-content').html('未设置试卷名称!');
                return;
            }
            var html = '', template = '<label class="info-title">{0}：</label><b class="info-content" title="{1}">{1}</b>';
            if (data.gradeName) {
                html += CommUtils.formatStr(template, '年级', data.gradeName);
            }
            if (data.subjectName) {
                html += CommUtils.formatStr(template, '学科', data.subjectName);
            }
            html += CommUtils.formatStr(template, '考试名称', data.name);
            if (data.knowledges.length > 0) {
                html += CommUtils.formatStr(template, '知识点', $.map(data.knowledges, function(know) {return know.knowledgeName}).join('、'));
            }
            $('.ppr-header-info').append(html);
            this.questionArea && this.questionArea.refreshTree();
            this.questionArea && this.questionArea.refreshAbility();
            this.cardArea.refreshName(data.name);
            if (this.baseInfo.knowledges && this.baseInfo.knowledges.length > 0) {
                var knowledgePoints = this.baseInfo.knowledges.slice(0);
                $.each(this.questionNodeMap, function(id, question) {
                    if (question.knowledges.length == 0) {
                        question.knowledges = knowledgePoints.slice(0);
                    }
                });
            }
        },
        isEdit: function() {
            return this.mode != 'preview';
        },
        guid: function(prefix) {
            var id = prefix ? (prefix + '_') : '';
            id += new Date().getTime() + '_';
            id += String.fromCharCode(97 + parseInt(Math.random() * 26));
            id += String.fromCharCode(97 + parseInt(Math.random() * 26));
            id += String.fromCharCode(97 + parseInt(Math.random() * 26)) + '_';
            id += parseInt(Math.random() * 1000);
            return id;
        },
        getType : function(typeCode, subjectCode) {
            var chooseArr = ["01", "02", "11", "12", "27", "28"],
                blankArr = ["03", "10", "14"], articleTypeCode = '08', judgeTypeCode = '04', chineseSubjectCode = '13', englishSubjectCode = '40';
            if (typeCode == articleTypeCode) {
                if (subjectCode == chineseSubjectCode) {
                    return 'chinese-article';
                } else if (subjectCode == englishSubjectCode) {
                    return 'english-article';
                }
            } else if (chooseArr.indexOf(typeCode) >= 0) {
                return "choose";
            } else if (blankArr.indexOf(typeCode) >= 0) {
                return "blank";
            } else if (judgeTypeCode == typeCode) {
                return "judge";
            }
            return "default";
        }
    });
})(MainPage);
;(function(mainPage) {
    var questionItem = function(parent, top, minLimit, maxLimit) {
        this.id = mainPage.guid('ques-pos');
        this.parent = parent;
        this.questionIdArr = [];

        var rulerSize = mainPage.examRuler.getSize(), minHeight = 10;
        this._area = { left: rulerSize.left, top: top, width: rulerSize.width, height: minHeight, minHeight: minHeight, minLimit: minLimit || 0, maxLimit: maxLimit || 0};

        mainPage.examRuler.addLimitObj(this);
    }
    questionItem.prototype = {
        init: function() {
            var _this = this;
            var template = '<div class="ques-item" data-ques-id="{0}" style="left: {left}px; top: {top}px; width: {width}px; height: {height}px;"><div class="ques-item-control ques-item-header"></div><div class="ques-item-operation"><span class="_r">关联题目</span><span class="_d">删除</span></div><div class="ques-item-no"></div><div class="ques-item-control ques-item-footer"></div></div>';
            _this.$container = $(CommUtils.formatStr(CommUtils.formatStr(template, this.id), this._area));
            _this.parent.$operation.append(this.$container);
            _this.refreshQuestionNo();

            _this.$container.find('.ques-item-operation ._r').on('click', function() {
                mainPage.startRelateQuestion(_this);
            });
            _this.$container.find('.ques-item-operation ._d').on('click', function() {
                _this.parent.removeQuestionPosition(_this);
            });
            _this.$container.find('.ques-item-control').on('mousedown', function(e) {// resize question top or bottom border
                e.stopPropagation(); // avoid trigger move event
                var downClientY = parseInt(e.clientY);
                if ($(this).hasClass('ques-item-header')) { // top border
                    var bottom = _this._area.top + _this._area.height;
                    _this.parent.bindMouseMove(function(e) {
                        var delta = mainPage.zoomTool.reScaleLength(parseInt(e.clientY) - downClientY);
                        var top = _this._area.top + delta;
                        if (top < _this._area.minLimit) top = _this._area.minLimit; // top must great than min limit
                        if (top > bottom - _this._area.minHeight) top = bottom - _this._area.minHeight;// height must be great than min height

                        _this.$container.css('top', top + 'px');
                        _this.refreshHeight(bottom - top);
                    });
                } else { // bottom border
                    _this.parent.bindMouseMove(function(e) {
                        var delta = mainPage.zoomTool.reScaleLength(parseInt(e.clientY) - downClientY);
                        var height = _this._area.height + delta;
                        if (_this._area.top + height > _this._area.maxLimit) height = _this._area.maxLimit - _this._area.top; // bottom must less than max limit
                        if (height < _this._area.minHeight) height = _this._area.minHeight; // height must be great than min height
                        _this.refreshHeight(height);
                    });
                }
                $(document.body).one('mouseup', function() {
                    _this.endJudge();
                    delete _this._mouseDownAttr;
                });
            });
        },
        refreshHeight: function(height) {
            this.$container.css('height', Math.max(height, 10) + 'px');
        },
        refreshLeftAndWidth: function(left, width) {
            this._area.left = left;
            this._area.width = width;
            this.$container && this.$container.css({left: left + 'px', width: width + 'px'});
        },
        refreshQuestionNo: function() {
            var quesNo = $.map(this.questionIdArr, function(quesId) { return mainPage.questionNodeMap[quesId].content }).join(', ');
            this.$container && this.$container.find('.ques-item-no').text(quesNo);
            if (this.questionIdArr.length == 0) {
                this.setUnRelated();
            } else {
                this.setRelated();
            }
        },
        setSelected: function() {
            this.$container.addClass('selected');
        },
        setUnSelected: function() {
            this.$container.removeClass('selected');
        },
        setRelated: function() {
            this.$container.addClass('related');
        },
        setUnRelated: function() {
            this.$container.removeClass('related');
        },
        endJudge: function() { // end judge question area, refresh the question area info
            this._area.top = parseInt(this.$container.css('top').replace('px', ''));
            this._area.height = mainPage.zoomTool.reScaleLength(this.$container.height());
            var question = '<div style="width: {width}px; height: {height}px; background-image: url(' + this.parent.imgDataUrl + '); background-position: -{left}px -{top}px; background-color: white; background-repeat: no-repeat;"></div>';
            var left = this._area.left;
            if (this.parent.width > this.parent.actualWidth) {
                left -= (this.parent.actualWidth - this.parent.width)/ 2;
            }
            question = CommUtils.formatStr(question, {width: this._area.width, height: this._area.height, left: left, top: this._area.top});
            $.each(this.questionIdArr, function(q, questionId) {
                var quesNode = mainPage.questionNodeMap[questionId];
                quesNode.stem = question;
            });
            // this.parent.refreshQuesLimit(_this);
        },
        enableJudge: function() { // enable question judge
            var _this = this;
            _this.$container.on('mousedown', function(e) {
                var downClientY = parseInt(e.clientY);

                _this.parent.bindMouseMove(function(e) {
                    var delta = mainPage.zoomTool.reScaleLength(parseInt(e.clientY) - downClientY);
                    var top = _this._area.top + delta;
                    if (top < _this._area.minLimit) top = _this._area.minLimit; // top must great than min limit
                    if (top + _this._area.height >= _this._area.maxLimit) top = _this._area.maxLimit - _this._area.height; // bottom must less than max limit
                    _this.$container.css('top', top + 'px');
                });
                $(document.body).one('mouseup', function() {
                    _this.endJudge();
                });
            });
            _this.$container.bind('mouseenter', function(e) {
                $(this).addClass('hover');
            }).bind('mouseleave', function(e) {
                $(this).removeClass('hover');
            });
            this.$container.css('cursor', 'pointer');
        },
        disableJudge: function() { // disable question judge
            this.$container.unbind('mouseenter').unbind('mouseleave').unbind('mousedown').css('cursor', 'crosshair');
        },
        destroy: function() {
            this.$container.remove();
            var _this = this;
            $.each(this.questionIdArr, function(q, quesId) {
                var quesNode = mainPage.questionNodeMap[quesId];
                if (quesNode) {
                    quesNode.questionPositionArr.splice(quesNode.questionPositionArr.indexOf(_this), 1);
                    mainPage.cardArea.refreshRelated(quesNode);
                }
            });
        }
    };
    var examItem = function(examArea, file, questionPositionJson) {
        this.parent = examArea;
        this.loadComplete = false;
        this.file = file;
        this.imgDataUrl = file.url;
        this.width = 768;
        this.id = mainPage.guid();
        this.questions = [];
        this.initQuestions(questionPositionJson);
    }
    examItem.prototype = {
        init: function() {
            var _this = this;
            _this.initImage().done(function() {
                _this.$container = $('<div class="exam-item-file" id="'+ _this.id + '"><div class="file-operation"><div class="file-operation-header"><div class="btn-group"><span class="btn btn-toggle up-move">上移</span><span class="btn btn-toggle down-move">下移</span><span class="btn btn-toggle remove">删除</span></div></div></div></div>');
                _this.parent.$container.append(_this.$container);
                _this.$container.prepend(_this.img);
                _this.height = mainPage.zoomTool.reScaleLength(_this.$container.height());
                _this.$operation = _this.$container.find('.file-operation');
                if (!mainPage.isEdit()) {
                    return;
                }
                $.each(_this.questions, function(q, question) {
                    question._area.maxLimit = _this.height;
                    question.init();
                })
                _this.disableSelect();
                _this.bindHover();
                _this.$operation.find('.remove').on('click', function() {
                    _this.parent.removeExam(_this);
                });
                _this.$operation.find('.up-move').on('click', function() {
                    _this.parent.upMoveExam(_this);
                });
                _this.$operation.find('.down-move').on('click', function() {
                    _this.parent.downMoveExam(_this);
                });
                _this.loadComplete = true;
            });
        },
        removeQuestionPosition: function(questionPosition) {
            questionPosition.destroy();

            var idx = this.questions.indexOf(questionPosition);
            this.questions.splice(idx, 1);
        },
        initImage: function() {
            var result = $.Deferred();
            var _this = this;
            _this.img = new Image();
            _this.img.onload = function() {
                _this.actualWidth = this.width;
                _this.actualHeight = this.height;
                result.resolve();
            }
            _this.img.src = _this.imgDataUrl;
            return result;
        },
        initQuestions : function(jsons) {
            var _this = this;
            _this.questions = $.map(jsons || [], function(json) {
                var questionPosition = new questionItem(_this, json.top);
                questionPosition._area.height = json.height;
                questionPosition.questionIdArr = json.questionIdArr;
                $.each(questionPosition.questionIdArr, function(q, questionId) {
                    mainPage.questionNodeMap[questionId].questionPositionArr.push(questionPosition);
                });
                return questionPosition;
            });
        },
        enableSelect: function() {
            var _this = this;
            $.each(_this.questions, function(id, question) {
                question.disableJudge();
            });
            _this.unbindHover();
            _this.$container.css('cursor', 'crosshair');
            _this.$container.one('mousedown', function(e) {
                var downClientY = parseInt(e.clientY);
                var top = mainPage.zoomTool.reScaleLength(downClientY - parseInt(_this.$container.offset().top));
                var questionPosition = new questionItem(_this, top, 0, _this.height);
                questionPosition.questionIdArr.push(_this.parent.curQues.id);
                questionPosition.init();
                _this.questions.push(questionPosition);
                _this.parent.curQues.questionPositionArr.push(questionPosition);
                _this.bindMouseMove(function(e) {
                    var height = mainPage.zoomTool.reScaleLength(parseInt(e.clientY) - downClientY);
                    if (questionPosition._area.top + height > questionPosition._area.maxLimit) height = questionPosition._area.maxLimit - questionPosition._area.top;
                    questionPosition.refreshHeight(height);
                });
            });
        },
        disableSelect: function() {
            $.each(this.questions, function(id, question) {
                question.enableJudge();
            });
            this.$container.unbind('mousedown');
            this.$container.css('cursor', 'default');
            this.bindHover();
        },
        bindMouseMove: function(func) {
            var _this = this;
            _this.$container.on('mousemove', function(e) {
                func.call(this, e);
            });
            $(document.body).one('mouseup', function(e) {
                _this.$container.unbind('mousemove');
            });
        },
        bindHover: function() {
            this.$operation.bind('mouseenter', function() {
                $(this).addClass('hover');
            }).bind('mouseleave', function() {
                $(this).removeClass('hover');
            });
        },
        unbindHover: function() {
            this.$operation.unbind('mouseenter').unbind('mouseleave');
        },
        refresh: function() {
            var _this = this;
            if (!_this.loadComplete) return;
            $.each(_this.questions, function(q, questionPosition) {
                for (var i = questionPosition.questionIdArr.length - 1; i > 0; i--) {
                    var quesId = questionPosition.questionIdArr[i];
                    if (!mainPage.questionNodeMap[quesId]) {
                        questionPosition.questionIdArr.splice(i, 1);
                    }
                }
                questionPosition.refreshQuestionNo();
            });
        },
        destroy: function() {
            var _this = this;
            $.each(_this.questions, function(q, questionPosition) {
                _this.removeQuestionPosition(questionPosition);
            });
            this.$container.remove();
        }
    }
    var examArea = {
        exams: [],
        init: function() {
            this.$container = $('.exam-wrapper');
            $.each(this.exams, function(e, exam) {
                exam.init();
            });
        },
        addImage: function(file, questionPositions) {
            var exam = new examItem(this, file, questionPositions);
            this.exams.push(exam);
            return exam;
        },
        getFiles: function() {
            return $.map(this.exams, function(exam, e) {
                return {
                    fileId: exam.file.fileId,
                    fileName: exam.file.fileName,
                    fileExt: exam.file.fileExt,
                    url: exam.file.url,
                    md5: exam.file.md5,
                    size: exam.file.size,
                    sizeDisplay: exam.file.sizeDisplay,
                    fileOrder: e + 1
                }
            });
        },
        removeExam: function(exam) {
            exam.destroy();
            this.exams.splice(this.exams.indexOf(exam), 0);
        },
        upMoveExam: function(exam) {
            var idx = this.exams.indexOf(exam);
            if (idx > 0) {
                this.exams[idx-1].$container.before(this.exams[idx].$container);
                this.exams.splice(idx-1, 0, this.exams.splice(idx, 1)[0]);
            }
        },
        downMoveExam: function(exam) {
            var idx = this.exams.indexOf(exam);
            if (idx < this.exams.length - 1) {
                this.exams[idx].$container.before(this.exams[idx + 1].$container);
                var temp = this.exams.splice(idx, 1)[0];
                this.exams.splice(idx + 1, 0, temp);
            }
        },
        refresh: function() {
            $.each(this.exams, function(e, exam) {
                exam.refresh();
            });
        },
        beginSelectQuestion: function(quesNode, func) {
            var _this = this;
            _this.curQues = quesNode;
            $.each(_this.exams, function(e, exam) {
                exam.enableSelect();
            });
            var originQuestionPositionLength = _this.curQues.questionPositionArr.length;

            var def = $.Deferred();
            $(document.body).one('mouseup', function() {
                var hasAddPosition = _this.curQues.questionPositionArr.length > originQuestionPositionLength;
                if (hasAddPosition) {
                    var questionPosition = _this.curQues.questionPositionArr[_this.curQues.questionPositionArr.length - 1];
                    questionPosition.setRelated();
                    questionPosition.endJudge();
                }
                $('.ppr-container').removeClass('_lock_exam');
                delete _this.curQues;
                def.resolve(hasAddPosition);
            });
            return def.promise();
        },
        endSelectQuestion: function() {
            $.each(this.exams, function(e, exam) {
                exam.disableSelect();
            });
        }
    }
    mainPage.examArea = examArea;
})(MainPage);

;(function(mainPage) {
    var cardItem = function(parent, num) {
        this.parent = parent;
        this.num = num;
        this.blocks = [];
        var availableHeight = this.pageHeight - this.headerHeight - this.footerHeight;
        this.availableHeight = num > 1 ? availableHeight : (availableHeight - this.attentionHeight); // first page, reduce height of attention
    }
    cardItem.prototype = {
        pageHeight : 1123,
        headerHeight: 220,
        footerHeight: 46,
        attentionHeight: 88,
        initHtml: function($parent) {
            var _this = this;
            _this.$container = $('<div class="card-item"></div>');
            $parent.append(this.$container);
            _this.$container.append($('#card-item-template').html());
            var pageHtml = '';
            for (var i = 0; i< _this.num; i++) {
                pageHtml += '<div class="SquareBlack"></div>';
            }
            if (this.num > 1) {
                _this.$container.find('.Attention').remove();
            }
            _this.$container.find('.square_black_group').append(pageHtml);
            $.each(_this.blocks, function(b, block) {
                block.initHtml(_this.$container);
            });
        },
        addBlock: function(block) {
            block.pageNo = this.num;
            block.top = this.pageHeight - this.availableHeight - this.footerHeight;
            block.height = this.getBlockHeight(block);
            this.blocks.push(block);
            this.availableHeight -= block.height;
            this.initQuestionBlocks(block);
        },
        getBlockHeight: function(block) {
            var height = 0;
            height += block.title != '' ? 35 : 15;
            switch (block.type) {
                case 'choose':
                    height += parseInt(block.count / 20) * 123 + ((block.count % 20) > 5 ? 123 : ((block.count % 20) * 22 + 1 + 6 * 2)) + 15 * 2 + 1 * 2;
                    break;
                case 'judge':
                    height += (((block.count - 1) / 4) + 1) * 32 + 15 * 2 + 1 * 2;
                    break;
                case 'blank':
                    height += block.count * 40 + 15 + 1 * 2;
                    break;
                case 'chinese-article':
                    height += 15 + 1 * 2 + 1 * 2;// padding-bottom、outer-border、inner-border
                    height += block.addon ? 10 : 40; // padding-top | ques-no
                    height += block.row * 36;
                    break;
                case 'english-article':
                    height += 15 + 1 * 2;
                    if (!block.addon) height += 40;
                    height += block.row * 36;
                    break;
                case 'default':
                    height += block.row * 20 + (20 + 15 + 1 * 2);
                    break;
            }
            return height;
        },
        initQuestionBlocks: function(block) {
            var _this = this;
            var titleHeight = block.title != '' ? 35 : 15, baseLeft = 53, baseTop = block.top + titleHeight, height = block.height - titleHeight, width = 688;
            switch (block.type) {
                case 'choose':
                    var left = baseLeft + 1+ 15 + 6, top = baseTop + 1 + 6 + 15, _w = 151, _h = 23;
                    $.each(block.children, function(q, quesNode) {
                        var _l = left + parseInt((q % 20)/5) * 163;
                        var _t = top + parseInt(q /20) * 123 + (q % 5) * 22;
                        quesNode._blocks.push(_this.newQuesBlockAttr(_l, _t, _w, _h, block.pageNo, 1));
                    })
                    break;
                case 'judge':
                    var top = baseTop + 1+ 15 + 6, left = baseLeft + 1 + 15 + 6;
                    $.each(block.children, function(q, quesNode) {
                        var _l = left + 150 * (q % 4) + 34, _t = top + parseInt(q / 4) * 32;
                        quesNode._blocks.push(_this.newQuesBlockAttr(_l, _t, 100, 20, block.pageNo, 1));
                    });
                    break;
                case 'blank':
                    var top = baseTop;
                    $.each(block.children, function(q, quesNode) {
                        quesNode._blocks.push(_this.newQuesBlockAttr(baseLeft, top, width, 40, block.pageNo, 1));
                        top += 40;
                    });
                    break;
                default:
                    block.children[0]._blocks.push(_this.newQuesBlockAttr(baseLeft, baseTop, width, height, block.pageNo, block.row));
            }
        },
        newQuesBlockAttr: function(left, top, width, height, pageNo, rowCount) {
            return {
                'xAxis': left, 'yAxis': top,
                'width': width, 'height': height,
                'pageNo': pageNo, 'rowCount': rowCount,
            };
        }
    }
    var quesBlock = function(type) {
        this.type = type;
        this.title = '';
        this.count = 0;
        this.children = [];
        this.addon = false;
    }
    quesBlock.prototype = {
        newBlock: function() {
            return new quesBlock(this.type);
        },
        initHtml: function($parent) {
            var marginTop = 15, titleHtml = '';
            if (this.title != '') {
                marginTop = 0;
                titleHtml = '<h5 class="sectionName">' + this.title + '</h5>';
            }
            var html = '<div class="ContainerCenter"><div class="Numberone marginWidth" style="margin-top: '+marginTop+'px;">';
            html += titleHtml;
            switch (this.type) {
                case 'choose':
                    html += '<div class="AllTable Tb_com_1">';
                    var trTemplate = '<tr data-ques-id="{1}"><td class="ques_no">{0}</td> <td>[ <p class="ques_option">A</p> ]</td><td>[ <p class="ques_option">B</p> ]</td><td>[ <p class="ques_option">C</p> ]</td><td>[ <p class="ques_option">D</p> ]</td></tr>';
                    for (var i = 0; i< this.count; i++) {
                        if (i % 5 == 0) html += '<div><table border="1" class="AllTable_one"><tbody>'; // block start;
                        html += CommUtils.formatStr(trTemplate, this.children[i].content, this.children[i].id);
                        if (i == this.count - 1) {
                            html += '</tbody></table></div>';
                            break;
                        }
                        if (i % 5 == 4) html += '</tbody></table></div>';
                    }
                    html += '</div>';
                    break;
                case 'judge':
                    html += '<div class="Tb_com_1">';
                    var template = '<div class="judge_ques" data-ques-id="{1}"><div class="ques_no">{0}</div><div>[ <p class="judge_ques_option">√</p> ]</div><div>[ <p class="judge_ques_option">×</p> ]</div></div>';
                    for (var i = 0; i< this.count; i++) {
                        if (i%4 == 0) {
                            if (i != 0) {
                                html += '</div>';
                            }
                            html += '<div class="Four_font">';
                        }
                        html += CommUtils.formatStr(template, this.children[i].content, this.children[i].id);
                    }
                    html += '</div></div>';
                    break;
                case 'blank':
                    html += '<div class="Tb_com">';
                    var template = '<div class="BigBorder" data-ques-id="{1}"><div class="title_number">{0}.</div> <div class="AllWriteUnder"><div class="Write_text_s" style="width: 425px; margin-right: 0px;"></div></div></div>';
                    for (var i = 0; i< this.count; i++) {
                        html += CommUtils.formatStr(template, this.children[i].content, this.children[i].id);
                    }
                    html += '</div>';
                    break;
                case 'chinese-article':
                    if (!this.addon) {
                        html += '<div class="Tb_com" data-ques-id="'+this.children[0].id+'" style="padding-top: 0px;">';
                        html += CommUtils.formatStr('<p class="five_Essay_p"> {0}.</p>', this.children[0].content);
                    } else {
                        html += '<div class="Tb_com" data-ques-id="'+this.children[0].id+'" style="padding-top: 10px;">';
                    }
                    html += '<div class="All_table">';
                    var template = '<table border="1" class="Essay_table" cellspacing="10"><tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr></table>';
                    for (var i = 0; i< this.row; i++) {
                        html += template;
                    }
                    html += '</div></div>';
                    break;
                case 'english-article':
                    html += '<div class="Tb_com" data-ques-id="'+this.children[0].id+'">';
                    if (!this.addon) {
                        html += CommUtils.formatStr('<p class="five_Essay_p"> {0}.</p>', this.children[0].content);
                    }
                    html += '<ul class="English_border">';
                    var template = '<li></li>';
                    for (var i = 0; i< this.row; i++) {
                        html += template;
                    }
                    html += '</ul></div>';
                    break;
                case 'default':
                    html += '<div class="Tb_com"><div class="BigBorder textarea_h" data-ques-id="'+this.children[0].id+'">';
                    if (!this.addon) {
                        html += CommUtils.formatStr('<div class="ques_no">{0}.</div>', this.children[0].content);
                    }
                    var template = '<textarea cols="100%" rows="{0}" disabled="disabled" class="textarea"></textarea>';
                    html += CommUtils.formatStr(template, this.row);
                    html += '</div></div>';
                    break;
            }
            html += '</div></div>';
            $parent.find('.ContainerBottom').before(html);
        }
    }
    var cardArea = {
        questions: [],
        cards: [],
        init: function() {
            this.$container = $('.card-container');
        },
        refreshName: function(name) {
            name && (this.name = name);
            this.$container.find('.TestName').text(this.name);
        },
        refreshCardQuestionNo: function(quesNode) {
            var _this = this;
            $.each(quesNode._blocks, function(b, blk) {
                var $ques = _this.cards[blk.pageNo - 1].$container.find('[data-ques-id="' +quesNode.id+ '"]');
                switch (quesNode._type) {
                    case 'choose':
                    case 'judge':
                    case 'default':
                        $ques.find('.ques_no').text(quesNode.content);
                        break;
                    case 'blank':
                        $ques.find('.title_number').text(quesNode.content);
                        break;
                    case 'english-article':
                    case 'chinese-article':
                        $ques.find('.five_Essay_p').text(quesNode.content);
                        break;
                }
            });
        },
        refresh: function() {
            var _this = this;
            _this.clearCards();
            _this.addCard();
            $.each(mainPage.typeNodeArr || [], function(i, type) {
                if ('choose' == type._type || 'judge' == type._type || 'blank' == type._type) {
                    _this.addQuesBlock(type);
                } else {
                    _this.addRowBlock(type);
                }
            });
            _this.initLayer && _this.initLayer();
            $.each(_this.cards, function(c, card) {
                card.initHtml(_this.$container);
            });
            _this.refreshName();
        },
        addRowBlock: function(type) {
            var _this = this;
            var quesNodes = type.children;
            var block = new quesBlock(type._type);
            block.title = type.content;
            for (var i= 0; i< quesNodes.length; i++) {
                var quesNode = quesNodes.slice(i, i+1)[0];
                block.children.push(quesNode);
                var row = _this.calculateAvailableBlockRow(_this.lastCard(), block);
                if (row == 0) {
                    _this.addCard();
                    row = _this.calculateAvailableBlockRow(_this.lastCard(), block);
                }
                block.count = 1;
                block.row = Math.min(row, quesNode.row);
                _this.lastCard().addBlock(block);
                var offset = block.row;
                while (offset < quesNode.row) {
                    var tmpBlock = block.newBlock();
                    var row = _this.calculateAvailableBlockRow(_this.lastCard(), tmpBlock);
                    if (row == 0) {
                        _this.addCard();
                        row = _this.calculateAvailableBlockRow(_this.lastCard(), tmpBlock);
                    }
                    tmpBlock.count = 1;
                    tmpBlock.addon = true;
                    tmpBlock.children.push(quesNode);
                    tmpBlock.row = Math.min(row, quesNode.row - offset);
                    offset += tmpBlock.row;
                    _this.lastCard().addBlock(tmpBlock);
                }
                block = block.newBlock();
            }
        },
        addQuesBlock: function(type) {
            var _this = this;
            var offset = 0, quesNodes = type.children, total = quesNodes.length;
            var block = new quesBlock(type._type);
            block.title = type.content;
            var count = _this.calculateAvailableBlockQues(_this.lastCard(), block, quesNodes);
            if (count == 0) {
                _this.addCard();
                count = _this.calculateAvailableBlockQues(_this.lastCard(), block, quesNodes);
            }
            block.count = Math.min(total, count);
            block.children = quesNodes.slice(0, block.count);
            _this.lastCard().addBlock(block);
            offset += block.count;
            while (offset < total) {
                var tmpBlock = block.newBlock();
                var count = _this.calculateAvailableBlockQues(_this.lastCard(), tmpBlock, quesNodes.slice(offset, total));
                if (count == 0) {
                    _this.addCard();
                    count = _this.calculateAvailableBlockQues(_this.lastCard(), tmpBlock, quesNodes.slice(offset, total));
                }
                tmpBlock.count = Math.min(total - offset, count);
                tmpBlock.children = quesNodes.slice(offset, offset + tmpBlock.count);
                offset += tmpBlock.count;
                _this.lastCard().addBlock(tmpBlock);
            }
        },
        calculateAvailableBlockRow: function(card, block) {
            var availableHeight = card.availableHeight;
            if (block.title !='') availableHeight -= 35;
            else availableHeight -= 15;
            if (availableHeight <= 0) return 0;
            switch (block.type) {
                case 'chinese-article':
                    if (block.children.length > 0) { //part first
                        availableHeight -= 40 + 15 + 1 * 2; // ques-no、padding-bottom、border
                    } else { // other part
                        availableHeight -= 10 + 15 + 1 * 2; //padding-top、padding-bottom、border
                    }
                    availableHeight -= 1 * 2; // inner border
                    var row = parseInt(availableHeight / (31 + 1 * 2 + 3));// height、border、margin-bottom
                    return row < 5 ? 0 : row;
                case 'english-article':
                    if (block.children.length > 0) { //part first
                        availableHeight -= 40 + 15 + 1 * 2; // ques-no、padding-bottom、border
                    } else { // other part
                        availableHeight -= 15 + 1 * 2; //padding-top、padding-bottom、border
                    }
                    if (availableHeight < 36) return 0;
                    return parseInt(availableHeight / 36);
                case 'default':
                    availableHeight -= 20 + 2 * 2 + 15 + 1 * 2;//ques-no、area-padding、padding-bottom、border
                    if (availableHeight < 20) return 0;
                    return parseInt(availableHeight / 20);
            }
        },
        calculateAvailableBlockQues: function(card, block) {
            var availableHeight = card.availableHeight;
            if (block.title !='') availableHeight -= 35;
            else availableHeight -= 15;
            if (availableHeight < 0) return 0;
            switch (block.type) {
                case 'choose':
                    availableHeight -= 15 * 2 + 1 * 2;
                    if (availableHeight < 6 * 2 + 22 + 1) return 0;
                    if (availableHeight > 6 * 2 + 22 * 5 + 1) return parseInt(availableHeight / 123) * 20;
                    return parseInt((availableHeight - 6 * 2 - 1) / 22);
                case 'judge':
                    availableHeight -= 15 * 2 + 1 * 2;
                    if (availableHeight < 6 * 2 + 20) return 0;
                    return parseInt(availableHeight / 32) * 4;
                case 'blank':
                    availableHeight -= 15 + 1 * 2;
                    if (availableHeight < 40) return 0;
                    return availableHeight -= 15 + 1 * 2;
            }
        },
        addCard: function() {
            this.cards.push(new cardItem(this, this.cards.length + 1));
        },
        lastCard: function() {
            return this.cards.length == 0 ? null : this.cards[this.cards.length - 1];
        },
        clearCards: function() {
            this.cards = [];
            this.$container.empty();
            $.each(mainPage.questionNodeMap, function(quesId, quesNode) {
                delete quesNode._blocks;
                quesNode._blocks = [];
            });
        }
    }
    mainPage.cardArea = cardArea;
})(MainPage);