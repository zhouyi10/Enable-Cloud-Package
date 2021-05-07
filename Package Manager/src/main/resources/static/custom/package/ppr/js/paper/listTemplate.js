var ListTemplate = {
    paper: window.parent.EditPaperPage.vm.paper,
    init: function () {
        this.initVue();
    },
    initVue: function () {
        $.extend(VueConfig.data, {
            'paper' : CommUtils.clone(this.paper),
            'paperTemplateTitle' : this.paper.stageName + "-" + this.paper.gradeName + "-" + this.paper.subjectName
        });
        this.vm = new Vue(VueConfig);
        this.vm.$mount("#paperTemplateList");
    }
};

var VueConfig = {
    data: {
        'paper': {},
        'paperTemplateList': [],
        'paperTemplateTitle': "",
        'activePaperTemplate': undefined,
        'search': {
            pageNo: 1
        },
    },
    mounted: function () {
        var _this = this;
        layer.ready(function () {
            _this.doSearch();
        });
    },
    methods: {
        doSearch: function (isTurnPage) {
            var _this = this;
            var searchParameter = _this.getParameters(isTurnPage);
            var index = layer.load(3, {
                shade: [0.2,'#000'], //0.1透明度的白色背景
                zIndex: 30
            });
            // 查询分页
            _this.turnPage(searchParameter, this);
            $.post(ListTemplate.queryListTempLateUrl, searchParameter, function (data) {
                if (data.length > 0) {
                    _this.paperTemplateList = data;
                    _this.activePaperTemplate = _this.paperTemplateList[0];
                }
                layer.close(index);
            });
        },
        turnPage: function(param, _this){   //翻页
            $.post(ListTemplate.countListTempLateUrl, param, function(result){
                if (result > 0) {
                    $("#noPaperTemplate").hide();
                } else {
                    $("#noPaperTemplate").show();
                }
                _this.search.count = result;
                $("#pageOne").initPage(_this.search.count, _this.search.pageNo, function(pageNum) {//1、所有数据条数（自动每页十条）2、初次加载显示的页数 3、所执行函数
                    _this.search.pageNo = pageNum;
                    _this.doSearch(true);
                });
            });
        },
        getParameters: function () {
            return {
                "stageCode": this.paper.stageCode,
                "gradeCode": this.paper.gradeCode,
                "subjectCode": this.paper.subjectCode,
                "offset" : (this.search.pageNo - 1) * 10,
                "rows": 10
            }
        },
        selectPaperTemplate: function (paperTemplate) {
            var _this = this;
            _this.activePaperTemplate = paperTemplate;
        },
        savePaperTemplate: function () {
            var _this = this;
            // 获取模板详情
            var paperTemplateId = _this.activePaperTemplate.paperTemplateId;
            $.get(ListTemplate.getPaperTempLateUrl + paperTemplateId, function (data) {
                if (data) {
                    //  字段转换
                    data.stageCode = data.stage.code;
                    data.stageName = data.stage.name;
                    data.gradeCode = data.grade.code;
                    data.gradeName = data.grade.name;
                    data.subjectCode = data.subject.code;
                    data.subjectName = data.subject.name;
                    window.parent.EditPaperPage.vm.paperTemplate = data;
                    window.parent.EditPaperPage.vm.paperTemplate.TemplateBuildType = 1;
                    CommUtils.closeLayer();
                    window.parent.EditPaperPage.vm.editPaperTemplate();
                }
            }).fail(function () {
                layer.alert(i18n['error_query_paper_template'])
            });
        }
    }
};