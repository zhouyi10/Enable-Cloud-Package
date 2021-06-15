/**
 * tools
 */
"use strict";
var CommUtils = window.CommUtils = {
    _markObj: null,
    showMask: function (mobile) {
        if (CommUtils.isEmpty(mobile)) {
            this._markObj = layer.load(0, {shade: [0.5, '#000'], anim: -1});
        } else {
            this._markObj = layer.open({type: 2});
        }
        return this._markObj;
    },
    closeMask: function () {
        if (this._markObj != undefined && this._markObj != null) {
            layer.close(this._markObj);
        }
        this._markObj = null;
    },
    formatStr: function () {
        var args = Array.prototype.slice.apply(arguments);
        var that = this;
        if (args.length == 0) {
            return null;
        } else if (args.length == 1) {
            return args[0];
        } else {
            var template = args[0];
            args.splice(0, 1);
            if (typeof args[0] == 'object') {
                $.each(args[0], function (key, value) {
                    template = template.replace(new RegExp("{" + key + "}", "g"), that.isEmpty(value) ? '' : value);
                })
            } else {
                $.each(args, function (key, value) {
                    template = template.replace(eval("/({)" + key + "(})/g"), that.isEmpty(value) ? '' : value);
                })
            }
            return template;
        }
    },
    calcPageHeight: function (doc) {
        var cHeight = Math.max(doc.body.clientHeight, doc.documentElement.clientHeight)
        var sHeight = Math.max(doc.body.scrollHeight, doc.documentElement.scrollHeight)
        var height = Math.max(cHeight, sHeight)
        return height
    },
    getUrlParam: function (queryName, win) {
        if (!win) win = window;
		var query = decodeURI(win.location.search.substring(1));
		var vars = query.split("&");
		for (var i = 0; i < vars.length; i++) {
			var pair = vars[i].split("=");
			if (pair[0] == queryName) { return pair[1]; }
		}
		return null;
    },
    formatCssValue: function (margin) {
        if (margin.length == 1) {
            let marginTemp = margin[0];
            margin = [marginTemp, marginTemp, marginTemp, marginTemp];
        } else if (margin.length == 2) {
            let marginTemp = margin[0];
            let marginTemp_T = margin[1];
            margin = [marginTemp, marginTemp_T, marginTemp, marginTemp_T];
        } else if (margin.length == 4) {
            margin = [margin[0], margin[1], margin[2], margin[3]];
        }
        return margin;
    },
    createObj: function (o) {
        function F() {
        };
        F.prototype = o;
        return new F();
    },
    extendObj: function (child, parent) {
        var prototype = this.createObj(parent.prototype);
        prototype.constructor = child;
        child.prototype = prototype;
    },
    initTip: function () {
        $.fn.tip = function (content, direct, time) {
            if (direct == undefined) direct = 'bottom';
            direct = ['top', 'right', 'bottom', 'left'].indexOf(direct) + 1;
            var index = layer.tips(content, this, {
                tips: [direct, '#f5c564'],
                time: time ? time : 2000,
                anim: 6,
                tipsMore: true
            });
            if (direct == 3) {
                $('.layui-layer-tips[times="' + index + '"] .layui-layer-content').css('top', '-8px');
            } else if (direct == 2) {
                $('.layui-layer-tips[times="' + index + '"] .layui-layer-content').css('left', '-8px');
            } else if (direct == 1) {
                $('.layui-layer-tips[times="' + index + '"] .layui-layer-content').css('top', '8px');
            } else if (direct == 4) {
                $('.layui-layer-tips[times="' + index + '"] .layui-layer-content').css('left', '8px');
            }
        }
    },
    openFrame: function (title, url, width, height, success) {
        this.layerWindow = layer.open({
            type: 2,
            anim: -1,
            resize: false,
            title: title,
            shadeClose: false,
            maxmin: true,
            shade: 0.6,
            area: [width || '90%', height || '90%'],
            content: url,
            success: function (layero, index) {
                success && success(layero, index);
            }
        });
    },
    ifEmpty: function (obj, defaultValue) {
        return this.isEmpty(obj) ? this.isEmpty(defaultValue) ? '' : defaultValue : obj;
    },
    isEmpty: function (arg) {
        if (arg == undefined || arg == null) {
            return true;
        }
        if (typeof arg == 'string') {
            return arg == '' ? true : false;
        } else if (typeof arg == 'object') {
            if (arg instanceof Array && arg.length == 0) {
                return true;
            } else {
                return false;
            }
        } else if (typeof arg == 'number') {
            return false;
        } else if (typeof arg == 'boolean') {
            return false;
        } else {
            return !!arg;
        }
    },
    isNotEmpty: function (arg) {
        return !this.isEmpty(arg);
    },
    getFileSize: function (size) {
        var unit = ["B", "KB", "MB", "GB", "TB"];
        var result = size, i = 0;
        for (; i <= 4; i++) {
            if (result > 1024) {
                result = result / 1024;
                result = result.toFixed(2);
            } else {
                break;
            }
        }
        if (i > 4) {
            i = 4;
        }
        return result + unit[i];
    },
    formatDate: function (date, fmt) { //author: meizz
        var o = {
            "M+": date.getMonth() + 1,
            "d+": date.getDate(),
            "H+": date.getHours(),
            "m+": date.getMinutes(),
            "s+": date.getSeconds(),
            "q+": Math.floor((date.getMonth() + 3) / 3),
            "S": date.getMilliseconds()
        };
        if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    },
    ajax: function (url, param, success, method, async) {
        var _this = this;
        if (async == undefined) {
            async = true;
        }
        $.ajax({
            url: url,
            data: param,
            type: method || "get",
            async: async,
            success: function (data) {
                if (data.status == 1) {
                    success && success(data.data);
                    return;
                }
                if (data.message) {
                    _this.tipBox(data.message);
                } else {
                    _this.tipBox(i18n['systemErr']);
                }
            },
            error: function (data) {
                _this.tipBox(i18n['systemErr']);
            }
        });
    },
    clone: function (data) {
        if (data == null) return null;
        var newData = null, _this = this;
        if (typeof data == 'object') {
            if (data.length != undefined) {
                newData = [];
                $.each(data, function (i, value) {
                    newData.push(_this.clone(value));
                });
            } else {
                newData = {};
                $.each(data, function (key, value) {
                    newData[key] = _this.clone(value);
                });
            }
        } else {
            newData = data;
        }
        return newData;
    },
    mapTranList: function (mapTemp) {
        var listTemp = [];
        for (var key in mapTemp) {
            listTemp.push(mapTemp[key]);
        }
        return listTemp;
    },
    uploadFile: function (event, uploadFileUrl, verificationImageFileMap) {
        var response = {
            'successFiles': [],
            'errorFiles': []
        };
        var tempFileName = '';
        for (var i = 0; i < event.target.files.length; i++) {
            var formData = new FormData();
            var file = event.target.files[i];
            tempFileName = file.name;
            var fileKey = tempFileName + file.size;
            if (this.isEmpty(verificationImageFileMap) || this.isEmpty(verificationImageFileMap[fileKey])) {
                formData.append('file', file);
                formData.append("name", tempFileName);
                $.ajax({
                    url: uploadFileUrl,
                    type: 'POST',
                    data: formData,
                    // Tell jQuery not to process the data sent
                    processData: false,
                    // Tell jQuery not to set the Content-Type request header
                    contentType: false,
                    async: false,
                    beforeSend: function () {
                    },
                    success: function (responseStr) {
                        response.successFiles.push(responseStr);
                    },
                    error: function (responseStr) {
                        response.errorFiles.push({
                            'fileName': tempFileName
                        });
                    }
                });
            }
        }
        event.target.value = '';
        return response;
    },

    /* layui wait */
    showLoad: function (msg) {
        return layer.msg(msg, {
            icon: 16,
            shade: [0.5, '#f5f5f5'],
            scrollbar: false,
            offset: 'auto',
            time: 100000
        });
    },
    closeLoad: function (index) {
        layer.close(index);
    },
    showSuccess: function (msg) {
        layer.msg(msg, {time: 2000, offset: 'auto', icon: 1});
    },
    showFail: function (msg) {
        layer.msg(msg, {time: 2000, offset: 'auto', icon: 2});
    },
    closeLayer: function () {
        if (!!this.layerWindow) {
            layer.close(this.layerWindow);
        } else {
            var index = window.parent.layer.getFrameIndex(window.name)
            window.parent.layer.close(index);
        }
    },
    onMessage: function (callback) { //Monitor messages sent to this window
        if (window.addEventListener) {
            window.addEventListener('message', function (event) {
                callback && callback(event);
            }, false);
        } else if (window.attachEvent) {
            window.attachEvent("onmessage", function (event) {
                callback && callback(event);
            });
        }
    },
    filterString: function (content, type) {
        var _this = this;
        if (type === 1) {
            return content.replace(/[\u4e00-\u9fa5]/g, '')
        } else if (type === 2) {
            return _this.numberToChinese(content);
        }
    },
    numberToChinese: function (number) {
        if (isNaN(Number(number))) {
            console.error("this is not number!");
            return number;
        }
        var chnNumChar = ["零", "一", "二", "三", "四", "五", "六", "七", "八", "九"];
        var chnUnitChar = ["", "十", "百", "千", "万", "亿", "万亿", "亿亿"];
        var strIns = '', chnStr = '';
        var unitPos = 0;
        var zero = true;
        while (number > 0) {
            var v = number % 10;
            if (v === 0) {
                if (!zero) {
                    zero = true;
                    chnStr = chnNumChar[v] + chnStr;
                }
            } else {
                zero = false;
                strIns = chnNumChar[v];
                if (unitPos === 1 && v === 1) {
                    strIns = chnUnitChar[unitPos];
                } else {
                    strIns += chnUnitChar[unitPos]
                }
                chnStr = strIns + chnStr;
            }
            unitPos++;
            number = Math.floor(number / 10);
        }
        return chnStr;
    },
    formatJson2FormParam: function (data, prefix) {
        var _this = this;
        if (_this.isEmpty(data)) {
            return {};
        }
        if (!(typeof data == 'object')) { //Not an object or an array
            if (_this.isEmpty(prefix)) {
                return null;
            } else {
                var result = {};
                result[prefix] = obj;
                return result;
            }
        }
        if (_this.isEmpty(prefix)) { //undefined Handle
            prefix = "";
        }
        var result = {};
        if (data instanceof Array) {
            if (_this.isEmpty(prefix)) { // If it is an array, it cannot be without a prefix
                console.error("no prefix!");
                return;
            }
            $.each(data, function (i, value) {
                if (typeof value == 'object') {
                    $.extend(result, _this.formatJson2FormParam(value, prefix + '[' + i + ']'))
                } else {
                    result[prefix + '[' + i + ']'] = value;
                }
            });
        } else {
            var pre = prefix == '' ? '' : (prefix + '.');
            $.each(data, function (key, value) {
                if (typeof value == 'object') {
                    $.extend(result, _this.formatJson2FormParam(value, pre + key));
                } else {
                    result[pre + key] = value;
                }
            });
        }
        return result;
    },
    // After modifying the edited character, the cursor moves to the end
    moveCursorEnd: function (obj) {
        obj.focus(); //Solve the problem that ff can not locate the focus
        var range = window.getSelection();//Create range
        range.selectAllChildren(obj);//range Select all sub-contents under obj
        range.collapseToEnd();//Cursor to the end
    },
    tryLock: function ($obj) {
        if ($($obj).data('unclickable')) {
            return false;
        }
        $($obj).data('unclickable', true);
        return true;
    },
    unLock: function ($obj) {
        $($obj).removeData('unclickable');
    },
    httpString: function (s) {
        var reg = /(https?|http|ftp|file):\/\/[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]/g;
        s = s.match(reg);
        return (s)
    },
    analysisBlankAnswer: function (s) {
        if (this.isEmpty(s)) {
            return;
        } else {
            s = s.replace(/@#@/g, ",").replace(/@*@/g, ";").replace(/\[answerType2\]/g, "");
        }
        return s;
    },
    tipBoxV2: function (content, options, ok) {
        var op = {
            'title': '訊息',
            'btn': '確定',
            'time': -1,
            'interval': null,
            delayTip: '({0}s後自動關閉)',
            'index': null,
            'ok': function () {
            }
        };
        if (window.i18n) {
            if (i18n['delayTip'] != undefined) op.delayTip = i18n['delayTip'];
            if (i18n['info'] != undefined) op.title = i18n['info'];
            if (i18n['confirm'] != undefined) op.btn = i18n['confirm'];
        }
        if (options != undefined && options != null) {
            if (typeof (options) == "number") op.time = options;
            else if (typeof options == "function") op.ok = options;
            else if (typeof options == "object") $.extend(op, options);
        }
        if (ok != undefined && ok != null && typeof ok == "function") {
            op.ok = ok;
        }
        $(document.body).data('delayLayer', op);
        op.index = layer.confirm(content, {
            anim: -1,
            title: op.title,
            btn: [op.btn],
            success: function ($layer, index) {
                op.index = index;
                if (op.time != -1) {
                    var seconds = parseInt(op.time / 1000);
                    $layer.find('.layui-layer-title').html(op.title + CommUtils.formatStr(op.delayTip, seconds));
                    op.interval = setInterval(function () {
                        seconds--;
                        if (seconds == 0) {
                            window.clearInterval(op.interval);
                            op.ok && op.ok();
                            layer.close(op.index);
                        }
                        $layer.find('.layui-layer-title').html(op.title + CommUtils.formatStr(op.delayTip, seconds));
                    }, 1000);
                }
            }
        }, function () {
            // var op = $(document.body).data('delayLayer');
            // $(document.body).removeData('delayLayer');
            if (op.interval != null) {
                window.clearInterval(op.interval);
                op.interval = null;
            }
            op.ok && op.ok();
            layer.close(op.index);
        });
    },
    formatScore: function (score) {
        return parseFloat(score) == parseInt(score) ? parseInt(score) : parseFloat(score);
    },
    processAnswer: function (answer) {
        if (answer == null || answer == undefined) {
            return null;
        }
        if (!this.isEmpty(answer.lable)) {
            answer.lable = answer.lable.replace(/@#@/g, ",").replace(/@\*@/g, ";").replace(/\[answerType2\]/g, "");
        }
        if (!this.isEmpty(answer.strategy)) {
            answer.strategy = answer.strategy.replace(/@#@/g, ",").replace(/@\*@/g, ";").replace(/\[answerType2\]/g, "");
        }
        return answer;
    }
};
var StorageOperation = CommUtils.StorageOp = {
    get: function (key) {
        if (window.localStorage) {
            var prefix = this.getPrefix();
            var item = window.localStorage.getItem(prefix + key);
            if (item == undefined || item == undefined || item.trim() == '') {
                return null;
            }
            item = JSON.parse(item);
            if (item.end < new Date().getMilliseconds()) {
                window.localStorage.removeItem(prefix + key);
                return null;
            }
            return item.data;
        }
    },
    set: function (key, value, timeoutMills) {
        if (CommUtils.isEmpty(timeoutMills)) {
            timeoutMills = 604800000;
        }
        if (window.localStorage) {
            var prefix = this.getPrefix();
            var data = {
                'end': new Date().getMilliseconds() + timeoutMills,
                'data': value
            }
            window.localStorage.setItem(prefix + key, JSON.stringify(data));
        }
    },
    getPrefix: function () {
        return 'timeout_storage_';
    }

};
