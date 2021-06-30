/* 知识点选题 */

// 搜索条件组件
Vue.component('search-condition-counter', {
    template: '#search-condition-template',
    props: ['searchdata', 'condition', 'parent_this'],
    data: function () {
        return {
            searchdata: this.searchdata,
            condition: this.condition,
            provider: {
                list: this.searchdata.providers,
                activeCode: this.condition.providerCode,
                show: this.condition.providerShow
            },
            questionMarket: {
                selectQuestions: this.parent_this.selectQuestions
            },
            stage: {
                list: this.searchdata.stages,
                activeCode: this.condition.stageCode,
                show: this.condition.stageShow
            },
            grade: {
                list: this.searchdata.gradeListMap[this.condition.stageCode],
                activeCode: this.condition.gradeCode,
                show: this.condition.gradeShow
            },
            subject: {
                list: this.searchdata.subjectListMap[this.condition.gradeCode],
                activeCode: this.condition.subjectCode,
                show: this.condition.subjectShow
            },
            materialVersion: {
                list: this.searchdata.materialversionListMap[this.condition.materialVersionInitCode],
                activeCode: this.condition.materialVersionCode,
                show: this.condition.materialVersionShow,
                isZK: this.condition.isZK
            },
            questionType: {
                list: this.searchdata.questionTypeListMap[this.condition.subjectCode],
                activeCode: this.condition.questionTypeCode,
                show: this.condition.questionTypeShow
            },
            ability: {
                list: this.searchdata.abilityListMap[this.condition.subjectCode],
                activeCode: this.condition.abilityCode,
                show: this.condition.abilityShow
            },
            difficulty: {
                list: this.searchdata.difficultys,
                activeCode: this.condition.difficultyCode,
                show: this.condition.difficultyShow
            },
            textTitle: {
                stageName: this.condition.stageName,
                gradeName: this.condition.gradeName,
                subjectName: this.condition.subjectName,
                questionTypeName: this.condition.questionTypeName,
                difficultyName: this.condition.difficultyName,
                materialVersionName: this.condition.materialVersionName,
                noSelect: this.condition.noSelect
            },
            knowledge: {
                activeCode: this.condition.knowledgeActiveCode,
                knowledgeSetting: this.condition.knowledgeSetting
            },
            search: {
                count: 0,
                pageNo: 1,
                type: "1",
                text: "",
                questionNo: "",
                placeholder: this.parent_this.i18n['please_input_question_text']
            }
        }
    },
    methods: {
        textSearchClick: function () {
            if (this.search.type === "2") {
                this.search.questionNo = this.search.text;
            } else {
                this.search.questionNo = '';
            }
            this.doSearch();
        },
        switchProvider: function(provider) {
            this.provider.activeCode = provider.code;
            this.doSearch();
        },
        switchStage: function(stage) {
            this.stage.activeCode = stage.stageCode;
            if (stage.stageCode === '') {
                if (this.materialVersion.isZK) {
                    this.textTitle.stageName = '';
                } else {
                    this.textTitle.stageName = this.parent_this.i18n['please_select_filter']
                }
            } else {
                this.textTitle.stageName = stage.stageName;
            }
        },
        switchGrade: function(grade) {
            this.grade.activeCode = grade.gradeCode;
            this.textTitle.gradeName = grade.gradeName;
        },
        switchSubject: function(subject) {
            this.subject.activeCode = subject.subjectCode;
            if (this.subject.activeCode) {
                this.textTitle.subjectName = subject.subjectName;
            } else {
                this.textTitle.subjectName = "";
            }
        },
        switchMaterialVersion: function(materialVersion) {
            this.materialVersion.activeCode =  materialVersion.code;
            this.textTitle.materialVersionName = materialVersion.name;
            initKnowledgeData(this.grade.activeCode, this.subject.activeCode, materialVersion.code, this.knowledge.knowledgeSetting, this.materialVersion.isZK);
        },
        switchDifficulty: function (difficulty) {
            this.difficulty.activeCode = difficulty.code;
            this.textTitle.difficultyName = difficulty.name;
            this.doSearch();
        },
        switchQuestionType: function (questionType) {
            this.questionType.activeCode = questionType.code;
            this.textTitle.questionTypeName = questionType.name;
        },
        switchAbility: function (ability) {
            this.ability.activeCode = ability.code;
            this.doSearch();
        },
        switchKnowledge: function (event, treeId, treeNode) {
            this.knowledge.activeCode = treeNode.searchCode;
        },
        switchSearchType: function(ele) {
            this.search.type = ele.target.value;
            if (this.search.type === '2') {
                this.search.placeholder = this.parent_this.i18n['please_input_question_no']
            } else {
                this.search.placeholder = this.parent_this.i18n['please_input_question_text']
            }
        },
        doSearch: function (isTurnPage) {
            var _this = this;
            var searchParameter = _this.searchParameter(isTurnPage);
            var index = layer.load(3, {
                shade: [0.2,'#000'], //0.1透明度的白色背景
                zIndex: 30
            });
            // console.log(layer.zIndex);
            // 查询分页
            _this.turnPage(searchParameter, this);
            $.post(_this.parent_this.questionChooseUrl, searchParameter, function (data) {
                if (data.status === 1 && data.data != null) {
                    _this.buildQuestionList(data.data, index);
                } else {
                    layer.close(index);
                }
            }).always(function(){
                layer.close(index)
            });
        },
        turnPage: function(param, _this){   //翻页
            $.post(_this.parent_this.questionChooseCountUrl, param, function(result){
                _this.search.count = result.data;
                $("#pageOne").initPage(_this.search.count, _this.search.pageNo, function(pageNum) {//1、所有数据条数（自动每页十条）2、初次加载显示的页数 3、所执行函数
                    _this.search.pageNo = pageNum;
                    _this.doSearch(true);
                });
            });
        },
        buildQuestionList: function(data, index) {
            if (data.length === 0) {
                $("#noQuestion").show()
            } else {
                $("#noQuestion").hide();
                for (var obj of data) {
                    obj.stem.richTextShow = obj.stem.richText;
                }
            }
            this.parent_this.resultVue.parentQuestionList = data;
            layer.close(index);
        },
        searchParameter: function (isTurnPage) {
            var textSearch = '';
            if (this.search.type === "1") {
                textSearch = this.search.text;
            }

            if (!isTurnPage) {
                this.search.pageNo = 1;
            }

            return {
                providerCode: this.provider.activeCode,
                stageCode: this.stage.activeCode,
                gradeCode: this.materialVersion.activeCode ? "" : this.grade.activeCode, // 教材版本不为空, 查询不带年级字段
                subjectCode: this.subject.activeCode,
                materialVersion: this.materialVersion.activeCode,
                difficultyCode: this.difficulty.activeCode,
                typeCode: this.questionType.activeCode,
                abilityCode: this.ability.activeCode,
                keyword: textSearch,
                questionNo: this.search.questionNo,
                searchCode: this.knowledge.activeCode,
                creator : this.condition.userId,
                offset: (this.search.pageNo - 1) * 10,
                rows: 10
            }
        },
        addAllToPaper: function() {
            for (var i = 0; i < this.parent_this.resultVue.parentQuestionList.length; i++) {
                var questionItem = this.parent_this.resultVue.$children[i];
                if (!questionItem.isSelect) {
                    var question = questionItem.question;
                    var questionType = this.parent_this.selectQuestionsTypeMap[question.type.code];
                    if (questionType) {
                        var questionList = this.parent_this.selectQuestions[questionType.index];
                        questionList.questions.push(question);
                        this.parent_this.questionCount += 1;
                        this.parent_this.selectQuestionsMap[question.questionId] = {
                            index: questionType.index+'_' + question.type.code + '_' + questionType.name + '_' + questionList.questions.length-1,
                            question: question
                        };
                        questionItem.isSelect = 1;
                    } else {
                        console.log("题型:" + this.question.type.code + " 不存在! ");
                    }
                }
            }
        },
        emptyQuestionMarket: function(selectQuestionType) {
            var _this = this;
            if (selectQuestionType !== '') {
                var questionList = selectQuestionType.questions;
                for (var index = 0; index < questionList.length; index++) {
                    delete _this.parent_this.selectQuestionsMap[questionList[index].questionId];
                    $.each(_this.parent_this.resultVue.$children, function (questionItemIndex, questionItem) {
                        if (questionItem.question.questionId === questionList[index].questionId) {
                            questionItem.isSelect = 0;
                        }
                    });
                    _this.parent_this.questionCount -= 1;
                }
                selectQuestionType.questions = [];
            } else {
                $.each(_this.parent_this.selectQuestions, function (index, selectQuestionType) {
                    if (selectQuestionType.questions.length > 0) {
                        $.each(selectQuestionType.questions, function (index, question) {
                            $.each(_this.parent_this.resultVue.$children, function (questionItemIndex, questionItem) {
                                if (questionItem.question.questionId === question.questionId) {
                                    questionItem.isSelect = 0;
                                }
                            });
                        });
                        selectQuestionType.questions = [];
                    }
                });
                _this.parent_this.selectQuestionsMap = {};
                _this.parent_this.questionCount = 0;
            }
        },
        completeChooseQuestion: function () {
            parent.postMessage({"type" : "chooseQuestion", "data" : this.parent_this.selectQuestions}, "*");
            if (window.opener){
                window.opener.postMessage({"type" : "chooseQuestion", "data" : this.parent_this.selectQuestions}, "*");
                window.close();
            }else{
                var index = parent.layer.getFrameIndex(window.name);
                parent.layer.close(index);
            }
        }
    },
    watch: {
        'stage.activeCode': function (newActiveCode) {
            if (!this.materialVersion.isZK) {
                this.grade.list = this.searchdata.gradeListMap[newActiveCode];
                if (this.grade.activeCode === '') {
                    this.doSearch();
                }
            } else {
                this.doSearch();
            }
        },
        'grade.activeCode': function (newActiveCode) {
            var _this = this;
            _this.subject.list = _this.searchdata.subjectListMap[newActiveCode];
            if (newActiveCode === '') {
                _this.textTitle.gradeName = '';
            }
            if (_this.subject.activeCode === '') {
                _this.doSearch();
            } else {
                var exist = false;
                $.each(_this.subject.list, function (index, subject) {
                    if (subject.subjectCode === _this.subject.activeCode) {
                        exist = true;
                    }
                });
                if (exist) {
                    _this.doSearch();
                } else {
                    _this.subject.activeCode = '';
                }
            }
            initKnowledgeData(newActiveCode, this.subject.activeCode, this.materialVersion.activeCode, this.knowledge.knowledgeSetting, this.materialVersion.isZK);
        },
        'subject.activeCode': function (newActiveCode) {
            if (this.parent_this.condition.questionSwitch !== 1) {
                // 题型
                var questionTypeList = this.searchdata.questionTypeListMap[newActiveCode];
                if (!questionTypeList) {
                    this.questionType.list = this.searchdata.questionTypeListMap[''];
                } else {
                    this.questionType.list = questionTypeList;
                }
                this.questionType.activeCode = '';
            }

            // 能力值
            var abilityList = this.searchdata.abilityListMap[newActiveCode];
            if (!abilityList) {
                this.ability.list = this.searchdata.abilityListMap[''];
            } else {
                this.ability.list = abilityList;
            }
            this.ability.activeCode = '';
            this.knowledge.activeCode = '';

            if (!this.materialVersion.isZK) {
                // 教材版本
                if (this.grade.activeCode !== '' && newActiveCode !== '') {
                    var materialVersionKey = this.grade.activeCode + '_' + newActiveCode;
                    var list = this.searchdata.materialversionListMap[materialVersionKey];
                    if (!list) {
                        this.materialVersion.list = this.searchdata.materialversionListMap[""];
                    } else {
                        this.materialVersion.list = list;
                    }
                } else {
                    this.materialVersion.list = this.searchdata.materialversionListMap[""];
                }

                this.materialVersion.activeCode = '';
                this.doSearch();
            } else {
                initKnowledgeData("", this.subject.activeCode, this.materialVersion.activeCode, this.knowledge.knowledgeSetting, this.materialVersion.isZK);
                this.doSearch();
            }
        },
        'materialVersion.activeCode': function (newActiveCode) {
            if (newActiveCode === '') {
                this.textTitle.materialVersionName = this.parent_this.i18n['please_select_materialVersion'];
                $.fn.zTree.destroy();
                this.textTitle.noSelect = true;
                this.knowledge.activeCode = '';
            }
            if (this.subject.activeCode !== '') {
                this.doSearch();
            }
        },
        'questionType.activeCode': function () {
            if (this.subject.activeCode !== '') {
                this.doSearch();
            }
        },
        'knowledge.activeCode': function () {
            if (this.materialVersion.activeCode !== '') {
                this.doSearch();
            }
        },
        'grade.list': function (newList) {
            var _this = this;
            var tempActiveCode = false;
            $.each(newList, function (index, grade) {
                if (grade.gradeCode === _this.grade.activeCode) {
                    tempActiveCode = true;
                }
            });
            if (!tempActiveCode) {
                this.grade.activeCode = '';
            }
        },
        'subject.list': function (newList) {
            var _this = this;
            var tempActiveCode = false;
            $.each(newList, function (index, subject) {
                if (subject.subjectCode === _this.subject.activeCode) {
                    tempActiveCode = true;
                }
            });
            if (!tempActiveCode) {
                this.subject.activeCode = '';
            }
        },
        'materialVersion.list': function () {
            this.materialVersion.activeCode = '';
        },
        'questionType.list': function () {
            this.questionType.activeCode = '';
        },
        'ability.list': function () {
            this.ability.activeCode = '';
        },
        'textTitle.stageName': function (newStageName) {
            if (!this.materialVersion.isZK) {
                this.textTitle.noSelect = newStageName === this.parent_this.i18n['please_select_filter'];
                if (this.parent_this.condition.questionSwitch !== 1) {
                    this.textTitle.questionTypeName = this.parent_this.i18n['no_limit'];
                }
            }
        },
        'textTitle.gradeName': function (newGradeName) {
            if (newGradeName === ''){
                this.textTitle.subjectName = ''
            }
        }
    },
    created: function () {
        // 设置知识点回调函数
        this.knowledge.knowledgeSetting.callback = {
            onClick: this.switchKnowledge
        };
        // 初始化知识点
        if (this.materialVersion.isZK) {
            initKnowledgeData("", this.condition.subjectCode, this.materialVersion.activeCode, this.knowledge.knowledgeSetting, this.materialVersion.isZK);
        } else {
            initKnowledgeData(this.condition.gradeCode, this.condition.subjectCode, this.condition.materialVersionCode, this.knowledge.knowledgeSetting, this.materialVersion.isZK);
        }
        this.doSearch();
    }
});

// 搜索结果组件
Vue.component('result-table-counter', {
    template: '#result-table-template',
    props: ['question', 'parent_this'],
    data: function () {
        return {
            question: this.question,
            isActive: false,
            isSelect: this.parent_this.selectQuestionsMap[this.question.questionId] ? 1 + this.parent_this.condition.questionSwitch : 0,
            childrenActive: false
        }
    },
    methods: {
        questionClick: function () {
            this.isActive = !this.isActive;
            if (this.question.children && this.question.children.length > 0) {
                this.childrenActive = false;
            } else {
                this.childrenActive = true;
            }
            if (this.parent_this.tempQuestion && this !== this.parent_this.tempQuestion) {
                this.parent_this.tempQuestion.isActive = false;
                this.parent_this.tempQuestion.childrenActive = false;
            }
            this.parent_this.tempQuestion = this;
        },
        addToPaper: function () {
            var _this = this;

            // 换题时直接返回数据
            if (_this.parent_this.condition.questionSwitch === 1) {
                var questionSwitchIndex = _this.parent_this.condition.questionSwitchIndex;

                // 判断题型
                var selectQuestions = _this.parent_this.selectQuestions[questionSwitchIndex[0]];

                if (selectQuestions.typeCode === _this.question.type.code) {
                    // _this.question.switchQuestionId = _this.parent_this.condition.questionId;
                    // selectQuestions.questions[questionSwitchIndex[1]] = _this.question;
                    var result = {
                        "question": _this.question,
                        "switchQuestionId": _this.parent_this.condition.questionId
                    }
                    parent.postMessage({"type": "changeQuestion", "data": result}, "*");
                    var index = parent.layer.getFrameIndex(window.name);
                    parent.layer.close(index);
                } else {
                    layer.alert(i18n['switch_question_not_type'], {icon: 7, anim: 6});
                    return;
                }
            }

            var questionType = _this.parent_this.selectQuestionsTypeMap[_this.question.type.code];
            if (questionType) {
                var questionList = _this.parent_this.selectQuestions[questionType.index];
                questionList.questions.push(this.question);
                _this.parent_this.questionCount += 1;
                _this.parent_this.selectQuestionsMap[_this.question.questionId] = {
                    index: questionType.index+'_' + _this.question.type.code + '_' + questionType.name + '_' + questionList.questions.length-1,
                    question: _this.question
                };
                _this.isSelect = 1;
            } else {
                console.log("题型:" + _this.question.type.code + " 不存在! ");
            }
        },
        removeToPaper: function () {
            var _this = this;
            var questionType = this.parent_this.selectQuestionsTypeMap[this.question.type.code];
            var questionList = this.parent_this.selectQuestions[questionType.index].questions;
            $.each(questionList, function (index, question) {
                if (question.questionId === _this.question.questionId) {
                    questionList.splice(index,1);
                    delete _this.parent_this.selectQuestionsMap[_this.question.questionId];
                    _this.parent_this.questionCount -= 1;
                    _this.isSelect = 0;
                    return false;
                }
            });
        }
    }
});


function resizeConditionAreaHeight() {
    $('.topic-page .asider-left .asider-head .condition-list').css('max-height',  $('.topic-page .asider-left').height() + 'px');
}

// 页面加载完毕后加载
$(function () {
    var _this = ChapterChooseQuestion;

    initData();

    // search result
    _this.resultVue = new Vue({
        el: '#result_table',
        data: {
            parentQuestionList: [],
            parent: _this
        }
    });

    // search condition
    _this.searchConditionVue = new Vue({
        el: '#search_condition',
        data: {
            parentSearchData: _this.parentSearchData,
            parentCondition: _this.condition,
            parent: _this
        },
        mounted: function() {
            this.$nextTick(function() {
                resizeConditionAreaHeight();
                $(window).on('resize', resizeConditionAreaHeight);
            });
        },
    });

    /* data init */
    function initData() {
        _this.parentSearchData = {};

        _this.noLimit = {
            code: '',
            name: _this.i18n['no_limit']
        };

        // Default query condition
        _this.condition = conditionDefault();

        // Change questions, hide questions
        if (_this.condition.questionId) {
            _this.condition.questionSwitch = 1;
            _this.condition.questionTypeShow = false;
        } else {
            _this.condition.questionSwitch = 0;
        }

        // Senior high school entrance examination / knowledge point selection
        if (_this.condition.isZK) {
            _this.condition.materialVersionCode = "VER132";
            _this.condition.materialVersionInitCode = "";
            _this.condition.gradeCode = "";
            _this.condition.gradeName = "";
            _this.condition.gradeShow = false;
            _this.condition.stageCode = "";
            _this.condition.stageShow = false;
            _this.condition.stageName = "";
        }

        // Resource library type, add three or three cloud question bank
        _this.providers = _this.providers.splice(0,3);
        // if (_this.cloudResourceEnable) {
        //     _this.providers.push({
        //         code: "R03",
        //         name: _this.i18n['cloud_question_repository']
        //     });
        // }
        _this.parentSearchData.providers = _this.providers;

        if (_this.difficultys) {
            //Degree of difficulty
            _this.difficultys.unshift(_this.noLimit);
            _this.parentSearchData.difficultys = _this.difficultys;
            for (var i = 0; i < _this.difficultys.length; i++) {
                if (_this.condition.difficultyCode === _this.difficultys[i].code) {
                    _this.condition.difficultyName = _this.difficultys[i].name;
                }
            }
        }

        // stage/grade/subject/question_type/materialVersion/ability
        _this.parentSearchData.stages = [];
        _this.parentSearchData.gradeListMap = {};
        _this.parentSearchData.subjectListMap = {};
        _this.parentSearchData.questionTypeListMap = {};
        _this.parentSearchData.materialversionListMap = {};
        _this.parentSearchData.abilityListMap = {};

        initStages();
        initGradeListMap();
        initSubjectListMap();

        $.ajaxSettings.async = false;
        initQuestionTypeListMap();
        initMaterialVersionListMap();
        initAbilityListMap();
        $.ajaxSettings.async = true;
        initQuestionMarket();

    }

    function initQuestionMarket() {
        // Get parent test paper questions
        if (window.parent && window.parent.getPaperQuestions) {
            _this.selectQuestions = window.parent.getPaperQuestions();
        }else{
            debugger
            console.log(_this.questionMarkets);
            var questionMarkets = JSON.parse(_this.questionMarkets);
            if(questionMarkets.length>0){
                _this.selectQuestions = questionMarkets;
            }else{
                _this.selectQuestions = initQuestionTypes();
            }
        }
        _this.questionCount = 0;
        _this.selectQuestionsMap = {};
        _this.selectQuestionsTypeMap = {};
        if (_this.selectQuestions == null) return;
        $.each(_this.selectQuestions, function (index, questionType) {
            _this.questionCount += questionType.questions.length;
            $.each(questionType.questions, function (questionIndex, question) {
                var id = "";
                if (question.questionId) {
                    id = question.questionId;
                } else if (question.questionNo){
                    // High exam paper question no questionId
                    id = question.questionNo;
                } else {
                    return;
                }
                _this.selectQuestionsMap[id] = {
                    index: index+'_' + questionType.typeCode + '_' + questionType.typeName + '_' + questionIndex,
                    question: question
                };

                // If it is the title of the change mark
                if (id === _this.condition.questionId) {
                    _this.condition.questionTypeCode = questionType.typeCode;
                    _this.condition.questionTypeName = questionType.typeName;
                    _this.condition.questionSwitchIndex = [index, questionIndex];
                }
            });
            _this.selectQuestionsTypeMap[questionType.typeCode] = {
                index: index,
                name: questionType.typeName
            };
        });
    }

    function initQuestionTypes(){
        var questionTypes = [];
        questionTypes =_this.questionTypes.map(function(item){
            return {
                'typeCode' : item.code,
                'typeName' : item.name,
                'questions' : [],
            }
        });
        return questionTypes;
    }

    function initStages() {
        if (!_this.condition.isZK) {
            _this.stageGradeSubjects.stages.unshift({
                stageCode: '',
                stageName: _this.i18n['no_limit']
            });

            _this.parentSearchData.stages = _this.stageGradeSubjects.stages;
            // Initialize the grade map, each grade must contain a grade
            for (var i = 0; i < _this.stageGradeSubjects.stages.length; i++) {
                var stage = _this.stageGradeSubjects.stages[i];
                if (!_this.parentSearchData.gradeListMap[stage.stageCode]) {
                    _this.parentSearchData.gradeListMap[stage.stageCode] = [{
                        gradeCode: '',
                        gradeName: _this.i18n['no_limit'],
                        stageCode: stage.stageCode
                    }];
                }

                if (_this.condition.stageCode !== '' && stage.stageCode === _this.condition.stageCode) {
                    _this.condition.stageName = stage.stageName;
                }
            }
        }
    }

    function initGradeListMap() {
        if (!_this.condition.isZK) {
            _this.stageGradeSubjects.grades.unshift({
                gradeCode: '',
                gradeName: _this.i18n['no_limit']
            });

            // Fill the grade map, initialize the subject map
            for (var i = 0; i < _this.stageGradeSubjects.grades.length; i++) {
                var grade = _this.stageGradeSubjects.grades[i];
                if (!_this.parentSearchData.gradeListMap[grade.stageCode]) {
                    _this.parentSearchData.gradeListMap[grade.stageCode] = [{
                        gradeCode: '',
                        gradeName: _this.i18n['no_limit'],
                        stageCode: grade.stageCode
                    }, grade];
                } else {
                    var gradeList = _this.parentSearchData.gradeListMap[grade.stageCode];
                    gradeList.push(grade);
                }

                if (!_this.parentSearchData.subjectListMap[grade.gradeCode]) {
                    _this.parentSearchData.subjectListMap[grade.gradeCode] = [{
                        subjectCode: '',
                        subjectName: _this.i18n['no_limit'],
                        gradeCode: grade.gradeCode
                    }];
                }

                if (_this.condition.gradeCode !== '' && grade.gradeCode === _this.condition.gradeCode) {
                    _this.condition.gradeName = grade.gradeName;
                }
            }
        }
    }

    function initSubjectListMap() {
        if (_this.condition.isZK) {
            _this.parentSearchData.subjectListMap[""] = _this.zkSubjects;
            _this.condition.subjectName = _this.condition.subjectName ? _this.condition.subjectName : _this.zkSubjects[0].subjectName;
            _this.condition.subjectCode = _this.condition.subjectCode ? _this.condition.subjectCode : _this.zkSubjects[0].subjectCode;
        } else {
            var isSubjectTrue = false;
            // branch of learning
            for (var i = 0; i < _this.stageGradeSubjects.subjects.length; i++) {
                var subject = _this.stageGradeSubjects.subjects[i];
                if (_this.parentSearchData.subjectListMap[subject.gradeCode]) {
                    _this.parentSearchData.subjectListMap[subject.gradeCode].push(subject)
                }
                if (subject.subjectCode === _this.condition.subjectCode) {
                    _this.condition.subjectName = subject.subjectName;
                    isSubjectTrue = true;
                } else if(i === _this.stageGradeSubjects.subjects.length - 1 && !isSubjectTrue) {
                    _this.condition.subjectName = "";
                    _this.condition.subjectCode = "";
                }
            }
        }

    }

    function initQuestionTypeListMap() {
        $.getJSON(_this.queryQuestionTypeUrl, "", function(data){
            if(data.status === 1){
                data.data.unshift({
                    code: '',
                    name: _this.i18n['no_limit'],
                    relationCode: ''
                });
                $.each(data.data, function(index, obj){
                    var questionTypeList = _this.parentSearchData.questionTypeListMap[obj.relationCode];
                    if(!questionTypeList) {
                        if (obj.relationCode) {
                            questionTypeList = [{
                                code: '',
                                name: _this.i18n['no_limit'],
                                relationCode: obj.relationCode
                            }, obj];
                        } else {
                            questionTypeList = [obj];
                        }
                        _this.parentSearchData.questionTypeListMap[obj.relationCode] = questionTypeList;
                    } else {
                        questionTypeList.push(obj);
                    }

                    if (obj.code === _this.condition.questionTypeCode) {
                        _this.condition.questionTypeName = obj.name;
                    }
                });
            }
        });
    }
    
    function initMaterialVersionListMap() {
        // knowledge of High school entrance examination
        if (_this.condition.isZK) {
            _this.parentSearchData.materialversionListMap[""] = [{
                code: 'VER132',
                name: _this.i18n['zk_knowledge_name'],
            }];
            _this.condition.materialVersionName = _this.i18n['zk_knowledge_name'];
        } else {
            var relationCodeTemp = _this.condition.gradeCode + "_" + _this.condition.subjectCode;
            $.getJSON(_this.queryMaterailVersionUrl,'', function(data){
                if (data.status === 1){
                    data.data.unshift({
                        code: '',
                        name: _this.i18n['no_limit'],
                        relationCode: ''
                    });
                    $.each(data.data, function(index, obj){
                        var materialVersionList = _this.parentSearchData.materialversionListMap[obj.relationCode];
                        if(!materialVersionList) {
                            if (obj.relationCode) {
                                materialVersionList = [{
                                    code: '',
                                    name: _this.i18n['no_limit'],
                                    relationCode: obj.relationCode
                                }, obj];
                            } else {
                                materialVersionList = [obj];
                            }
                            _this.parentSearchData.materialversionListMap[obj.relationCode] = materialVersionList;
                        } else {
                            materialVersionList.push(obj);
                        }
                        if (_this.condition.materialVersionCode !== '' && relationCodeTemp === obj.relationCode && obj.code === _this.condition.materialVersionCode) {
                            _this.condition.materialVersionName = obj.name;
                        }
                    });
                }
            });
            // If the textbook version is not matched during initialization, reset the Material Version Code to empty
            if (_this.condition.materialVersionCode !== '' && _this.condition.materialVersionName === _this.i18n['please_select_materialVersion']) {
                _this.condition.materialVersionCode = '';
            }

            if (CommUtils.isNotEmpty(_this.parentSearchData.materialversionListMap[relationCodeTemp])) {
                _this.condition.materialVersionInitCode = relationCodeTemp;
            }
        }

    }

    function initAbilityListMap() {
        $.getJSON(_this.queryAbilityUrl, '', function (data) {
            if (data.status === 1) {
                data.data.unshift({
                    code: '',
                    name: _this.i18n['no_limit'],
                    relationCode: ''
                });
                $.each(data.data, function (index, obj) {
                    var abilityList = _this.parentSearchData.abilityListMap[obj.relationCode];
                    if (!abilityList) {
                        if (obj.relationCode) {
                            abilityList = [{
                                code: '',
                                name: _this.i18n['no_limit'],
                                relationCode: obj.relationCode
                            }, obj];
                        } else {
                            abilityList = [obj];
                        }
                        _this.parentSearchData.abilityListMap[obj.relationCode] = abilityList;
                    } else {
                        abilityList.push(obj);
                    }

                    if (obj.code === _this.condition.abilityCode) {
                        _this.condition.abilityName = obj.name;
                    }
                });
            }
        });
    }
    
    function conditionDefault() {
        return {
            userId : _this.condition.userId,
            providerCode: !CommUtils.isEmpty(_this.condition.providerCode) ? _this.condition.providerCode : Constants.PROVIDER_CODE_PERSONAL,
            providerShow: !CommUtils.isEmpty(_this.condition.providerShow) ? _this.condition.providerShow : true,
            difficultyCode: !CommUtils.isEmpty(_this.condition.difficultyCode) ? _this.condition.difficultyCode : '',
            difficultyShow: !CommUtils.isEmpty(_this.condition.difficultyShow) ? _this.condition.difficultyShow : true,
            difficultyName: !CommUtils.isEmpty(_this.condition.difficultyName) ? _this.condition.difficultyName : '',
            stageCode: !CommUtils.isEmpty(_this.condition.stageCode) ? _this.condition.stageCode : '',
            stageShow: !CommUtils.isEmpty(_this.condition.stageShow) ? _this.condition.stageShow : true,
            stageName: !CommUtils.isEmpty(_this.condition.gradeName) ? '' : _this.i18n['please_select_filter'],
            noSelect: !CommUtils.isEmpty(_this.condition.stageCode),
            gradeCode: !CommUtils.isEmpty(_this.condition.gradeCode) ? _this.condition.gradeCode : '',
            gradeShow: !CommUtils.isEmpty(_this.condition.gradeShow) ? _this.condition.gradeShow : true,
            gradeName: !CommUtils.isEmpty(_this.condition.gradeName) ? _this.condition.gradeName : '',
            subjectCode: !CommUtils.isEmpty(_this.condition.subjectCode) ? _this.condition.subjectCode : '',
            subjectShow: !CommUtils.isEmpty(_this.condition.subjectShow) ? _this.condition.subjectShow : true,
            subjectName: !CommUtils.isEmpty(_this.condition.subjectName) ? _this.condition.subjectName : '',
            isZK: !CommUtils.isEmpty(_this.condition.isZK) ? _this.condition.isZK : false,
            materialVersionCode: !CommUtils.isEmpty(_this.condition.materialVersion) ? _this.condition.materialVersion : '',
            materialVersionShow: !CommUtils.isEmpty(_this.condition.materialVersionShow) ? _this.condition.materialVersionShow : true,
            materialVersionInitCode: !CommUtils.isEmpty(_this.condition.gradeCode) && !CommUtils.isEmpty(_this.condition.subjectCode) ? _this.condition.gradeCode+'_'+_this.condition.subjectCode : '',
            materialVersionName: _this.i18n['please_select_materialVersion'],
            questionId: !CommUtils.isEmpty(_this.condition.questionId) ? _this.condition.questionId : '',
            questionTypeCode: !CommUtils.isEmpty(_this.condition.questionTypeCode) ? _this.condition.questionTypeCode : '',
            questionTypeShow: !CommUtils.isEmpty(_this.condition.questionTypeShow) ? _this.condition.questionTypeShow : true,
            questionTypeName: !CommUtils.isEmpty(_this.condition.questionTypeName) ? _this.condition.questionTypeName : '',
            abilityCode: !CommUtils.isEmpty(_this.condition.abilityCode) ? _this.condition.abilityCode : '',
            abilityShow: !CommUtils.isEmpty(_this.condition.abilityShow) ? _this.condition.abilityShow : true,
            knowledgeData: [],
            knowledgeSelectCode: '',
            knowledgeSetting : {
                edit: {
                    enable: false,
                    drag: {
                        prev: true,
                        next: true,
                        inner: false
                    }
                },
                data: {
                    key: {
                        name : "knowledgeName"
                    },
                    simpleData: {
                        enable: true,
                        idKey: "knowledgeId",
                        pIdKey: "parentId",
                        rootPId: null,
                        searchCode: "searchCode",
                        outlineId: "outlineSearchCode"
                    }
                },
                check: {
                    enable: false
                },
                view : {
                    expandSpeed:"fast",
                    selectedMulti: false,
                    showTitle : true,
                    showLine : false
                },
                callback: {
                    beforeClick: null,
                    beforeExpand: null,
                    onClick: function(event, treeId, treeNode) {//click to choose question
                        //Get some basic data
                        //note ：Modification of search conditions, if there is outline Id, use knowledge points and syllabus to match together
                        ChooseQuestion.selectCondition[7] = treeNode.searchCode+","+treeNode.outlineId;
                        ChooseQuestion.selectConditionName[7] = treeNode.knowledgeName;
                        $('#knowledge_selector').empty().append('<li><a href="javascript:void(0);">' +treeNode.knowledgeName+ '<span class="removeliBtn"></span></a></li>')
                        $('.knowledge_box i').removeClass('fa-angle-double-down').addClass('fa-angle-double-up');
                        $('.tree-body').hide();
                        $('.removeliBtn').on('click', function(){
                            ChooseQuestion.selectCondition[7] = '';
                            $(this).parent().parent().remove();
                        });
                    }
                }
            }
        }
    }
});

function initKnowledgeData(gradeCode, subjectCode, materialVersionCode, knowledgeSetting, isZK) {
    if ((gradeCode !== '' && subjectCode && materialVersionCode && materialVersionCode !== "") || isZK) {
        $.ajax({
            method: "post",
            url: ChapterChooseQuestion.queryKnowledgeTree,
            cache: false,
            data: {
                subjectCode: subjectCode,
                gradeCode: gradeCode,
                materialVersion: materialVersionCode,
                providerCode: 'R01',
                selectedKnowledges: '',
            },
            success: function (data) {
                if (data && data !== "" && data !== "[]") {
                    ChapterChooseQuestion.searchConditionVue.$children[0].textTitle.noSelect = false;
                    $.fn.zTree.init($("#knowledgeTree"), knowledgeSetting, data.data);
                }
            }
        });
    }
}
