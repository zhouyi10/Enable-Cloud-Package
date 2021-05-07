/**
 * js base utils
 */
if (window.BaseUtils == undefined || window.BaseUtils == null) {
    window.BaseUtils = {};
}

(function(BaseUtils) {
    var utils = {
        ajaxPreRequestFilterMap: {}, // ajax request map
        getCookie: function(name) { //get cookie by name use js
            var arr, reg = new RegExp("(^| )"+name+"=([^;]*)(;|$)");
            if(arr = document.cookie.match(reg))
                return unescape(arr[2]);
            else
                return null;
        },
        submitForm: function(form) { // submit form , disabled resubmit by defaults
            var $form = form, _this = this;
            if (typeof $form == 'string' || $form.attr == undefined) {
                $form = $(form);
            }
            if ($form.length == 0) throw "no form ele!";
            if ($form.attr('disabled') == 'disabled') {
                return;
            }
            $form.attr('disabled', 'disabled');
            if ($form[0].tagName.toLowerCase() != 'form') {// the param is not a form, maybe just a child btn
                $form = $form.closest('form');
                $form.attr('disabled', 'disabled');
            }
            if ($form.find('input[name="_csrf"]').length == 0) {
                $form.append('<input type="hidden" name="_csrf" value="" />');
            }
            $form.find('input[name="_csrf"]').val(_this.getCookie("XSRF-TOKEN"));
            $form[0].submit();
        },
        preAjax: function() {
            /*ajax预处理过滤*/
            var _this = this;
            $.ajaxPrefilter(function (options, originalOptions, jqXHR) {
                if (options.type == undefined || options.type == null || typeof options.type != 'string') {
                    return;
                }
                if (["get", "options", "head", "trace"].indexOf($.trim(options.type)) >=0) {
                    return;
                }
                jqXHR.setRequestHeader("X-XSRF-TOKEN", _this.getCookie("XSRF-TOKEN"));
                //判断请求是否为文件上传
                var contentType = options.contentType;
                var i = -1;
                if (contentType != null && typeof contentType == "string") {
                    if (contentType.toLowerCase().indexOf('multipart/form-data'.toLowerCase()) >= 0) {
                        return;
                    }
                }
                //如果不是上传文件，过滤请求
                var key = options.url;
                if (options.data != null && options.data != undefined && !(options.data instanceof FormData)) {
                    if (typeof options.data == 'string') {
                        key += options.data;
                    } else if (typeof options.data == 'object') {
                        try {
                            key += JSON.parse(options.data);
                        } catch(e) {}
                    }
                }
                if (_this.ajaxPreRequestFilterMap[key] != null && _this.ajaxPreRequestFilterMap[key] != undefined) {
                    options.error = options.complete = function () {
                    };
                    jqXHR.abort();//discard repeated triggered commits
                    console.log("intercept repeated request!");
                    return;
                }
                _this.ajaxPreRequestFilterMap[key] = { 'xhr': jqXHR };
                _this.ajaxPreRequestFilterMap[key].success = options.success;
                options.success = function (jqXHR, textStatus) {
                    var oldSuccess = _this.ajaxPreRequestFilterMap[key].success;
                    _this.ajaxPreRequestFilterMap[key] = null;
                    oldSuccess && oldSuccess.apply(this, arguments);
                }
                _this.ajaxPreRequestFilterMap[key].error = options.error;
                options.error = function (jqXHR, textStatus) {
                    var oldError = _this.ajaxPreRequestFilterMap[key].error;
                    _this.ajaxPreRequestFilterMap[key] = null;
                    oldError && oldError.apply(this, arguments);
                }
            })
        }
    };

    utils.preAjax();

    $.extend(BaseUtils, utils);
})(window.BaseUtils);

(function(BaseUtils) {
    $.extend(BaseUtils, {
        _markObj: null,
        showMask: function(mobile) { //show mask , use layer
            if (mobile == undefined || mobile == null || mobile == false) {
                this._markObj = layer.load(0, { shade: [0.5, '#000'], anim: -1 }); //无动画
            } else {
                this._markObj = layer.open({ type: 2 });
            }
            return this._markObj;
        },
        closeMask: function() {//close mask , use layer
            if (this._markObj != undefined && this._markObj != null) {
                layer.close(this._markObj);
            }
            this._markObj = null;
        }
    });
})(window.BaseUtils);
