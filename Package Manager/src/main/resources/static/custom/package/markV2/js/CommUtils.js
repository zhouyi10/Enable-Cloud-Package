/**
 * 工具类
 */
var CommUtils = window.CommUtils = {
	_markObj: null,
	showMask: function(mobile) { //遮罩
		if (CommUtils.isEmpty(mobile)) {
			this._markObj = layer.load(0, { shade: [0.5, '#000'], anim: -1 }); //无动画
		} else {
			this._markObj = layer.open({ type: 2 });
		}
		return this._markObj;
	},
	closeMask: function() { //取消遮罩
		if (this._markObj != undefined && this._markObj != null) {
			layer.close(this._markObj);
		}
		this._markObj = null;
	},
	formatFloat: function(decimals, length, round) {
		decimals = decimals.toString();
		if (decimals.indexOf(".") > -1) {
			var decimalsSplits = decimals.split(".");
			var dTmpl = decimalsSplits[1];
			if (round) {
				dTmpl = Math.floor(dTmpl);
			}
			decimals = decimalsSplits[0] + "." + dTmpl.toString().substring(0, length);
		} else {
			return decimals;
		}
		return decimals;
	},
	formatStr : function(){ //字符串动态替换
		var args = Array.prototype.slice.apply(arguments);
		var that = this;
		if (args.length == 0) {
			return null;
		} else if (args.length == 1) {
			return args[0];
		} else {
			var template = args[0];
			args.splice(0,1);
			if (typeof args[0] == 'object') {
				$.each(args[0], function(key, value) {
					template = template.replace(new RegExp("({)" + key + "(})", "g"), that.isEmpty(value) ? '' : value);
				})
			} else {
				$.each(args, function(key, value) {
					template = template.replace(eval("/({)" + key + "(})/g"), that.isEmpty(value) ? '' : value);
				})
			}
			return template;
		}
	},
	ifEmpty : function(arg, defaultStr) { //为空默认值
		if (defaultStr == undefined || defaultStr == null) {
			defaultStr = '';
		}
		if (this.isEmpty(arg)) {
			return defaultStr;
		}
		return arg;
	},
	isEmpty : function(arg) { //判断对象、字符创等是否为空
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
		} else{
			return !!arg;
		}
	},
	getFileSize : function(size){ //根据大小计算文件大小
		var unit = ["B", "KB", "MB", "GB", "TB"];
		var result = size,i = 0;
		for (; i <= 4; i++) {
			if (result > 1024) {
				result = result / 1024;
				result = result.toFixed(2);
			} else {
				break;
			}
		}
		if (i > 4) { i= 4;}
		return result + unit[i];
	},
	formatDate : function (date, fmt) { //author: meizz 
	    var o = {
	        "M+": date.getMonth() + 1, //月份 
	        "d+": date.getDate(), //日 
			"h+": date.getHours(), //小时
			"H+": date.getHours(), //小时
	        "m+": date.getMinutes(), //分 
	        "s+": date.getSeconds(), //秒 
	        "q+": Math.floor((date.getMonth() + 3) / 3), //季度 
	        "S": date.getMilliseconds() //毫秒 
	    };
	    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
	    for (var k in o)
	    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	    return fmt;
	},
	ajax : function(url, param, success, method, async) { //异步请求
		var _this = this;
		if (async == undefined) {
			async = true;
		}
		$.ajax({
			url : url,
			data : param,
			type : method || "get",
			async : async,
			success : function(data) {
				if (data.status == 1) {
					success && success(data.data);
					return ;
				}
				if (data.message) {
					_this.tipBox(data.message);
				} else {
					_this.tipBox(i18n['systemErr']);
				}
			},
			error : function(data) {
				_this.tipBox(i18n['systemErr']);
			}
		});
	},
	clone : function(data){ //对象深拷贝
		var newData = null, _this = this;
		if (typeof data == 'object') {
			if (data instanceof Array) {
				newData = [];
				$.each(data, function(i, value) {
					newData.push(_this.clone(value));
				});
			} else {
				newData = {};
				$.each(data, function(key, value) {
					newData[key] = _this.clone(value);
				});
			}
		} else {
			newData = data;
		}
		return newData;
	},
	click : function(selector, clickFunc) { //绑定点击事件，避免重复点击
		if (this.isEmpty(selector) || selector == undefined || typeof clickFunc != 'function') {
			console.error('bind click event to 0 error!'.replace("0", selector));
			return;
		}
		$(selector).on('click', function(){
			if ($(this).data('unclickable')) {
				return;
			}
			$(this).data('unclickable', true);
			clickFunc.call(this);
			$(this).removeData('unclickable');
		});
	},
	tryLock : function($obj) {
		if ($($obj).data('unclickable')) {
			return false;
		}
		$($obj).data('unclickable', true);
		return true;
	},
	unLock : function($obj) {
		$($obj).removeData('unclickable');
	},
	onMessage : function(callBack){ //消息监听
		if (window.addEventListener){
			window.addEventListener('message',function(event){
				callBack(event);
			}, false);
	   }else if(window.attachEvent){
			window.attachEvent("onmessage", function(event){
				callBack(event);
			});
	   }
	},
	/* layui 等待 */
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
        layer.msg(msg, {time: 1000, offset: 'auto'});
    },
	openFrame : function(title, url, width, height) { //layer frame窗口
		this.layerWindow = layer.open({
			type: 2,
			anim: -1,
			resize: false,
			title: title,
			shadeClose: false,
			maxmin: true,
			shade: 0.6,
			area: [width || '90%', height || '90%'],
			content: url //iframe的url
		});
	},
    formatScore : function(score){
        return parseFloat(score) == parseInt(score) ? parseInt(score) : parseFloat(score);
    },
	processAnswer: function(answer) {
		if (answer == null || answer == undefined) {
			return null;
		}
		if (!this.isEmpty(answer.lable)) {
			answer.lable = answer.lable.replace(/@#@/g, ",").replace(/@\*@/g, ";").replace(/\[answerType2\]/g, "").replace(/#@#/g, ",");
		}
		if (!this.isEmpty(answer.strategy)) {
			answer.strategy = answer.strategy.replace(/@#@/g, ",").replace(/@\*@/g, ";").replace(/\[answerType2\]/g, "").replace(/#@#/g, ",");
		}
		return answer;
	},
	cmpareTo: function (pro, asc) {
		return function (obj1, obj2) {
			var val1 = obj1[pro];
			var val2 = obj2[pro];
			if (asc) {
				return val1 - val2;
			} else {
				return val2 - val1;
			}
		}
	},
	tipBoxV2: function(content, options, ok) {
		var op = {'title': '訊息', 'btn':'確定', 'time': -1, 'interval': null, delayTip: '({0}s後自動關閉)', 'index': null, 'ok': function(){} };
		if (window.i18n) {
			if (i18n['delayTip'] != undefined) op.delayTip = i18n['delayTip'];
			if (i18n['info'] != undefined) op.title = i18n['info'];
			if (i18n['confirm'] != undefined) op.btn = i18n['confirm'];
		}
		if (options != undefined && options != null) {
			if (typeof(options) == "number") op.time = options;
			else if (typeof options == "function") op.ok = options;
			else if (typeof options == "object") $.extend(op, options);
		}
		if (ok != undefined && ok != null && typeof ok == "function") {
			op.ok = ok;
		}
		$(document.body).data('delayLayer', op);
		op.index = layer.confirm(content, {
			anim : -1,//无动画
			title : op.title,
			btn: [ op.btn ],//['确定'] //按钮
			success: function($layer, index){
				op.index = index;
				if (op.time != -1) {
					var seconds = parseInt(op.time / 1000);
					$layer.find('.layui-layer-title').html(op.title + CommUtils.formatStr(op.delayTip, seconds));
					op.interval = setInterval(function() {
						seconds--;
						if(seconds == 0) {
							window.clearInterval(op.interval);
							op.ok && op.ok();
							layer.close(op.index);
						}
						$layer.find('.layui-layer-title').html(op.title + CommUtils.formatStr(op.delayTip, seconds));
					}, 1000);
				}
			}
		}, function(){
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
	formatJson2FormParam : function(data, prefix) {
		var _this = this;
		if (_this.isEmpty(data)) {
			return {};
		}
		if (!(typeof data == 'object')) { //不是对象或数组
			if (_this.isEmpty(prefix)) {
				return null;
			} else {
				var result = {};
				result[prefix] = obj;
				return result;
			}
		}
		if (_this.isEmpty(prefix)) { //undefined处理
			prefix = "";
		}
		var result = {};
		if (data instanceof Array) {
			if(_this.isEmpty(prefix)) { // 如果为数组，不能无前缀
				console.error("no prefix!");
				return;
			}
			$.each(data, function(i, value) {
				if (typeof value == 'object') {
					$.extend(result, _this.formatJson2FormParam(value, prefix + '[' + i + ']'))
				} else {
					result[prefix + '[' + i + ']'] = value;
				}
			});
		} else {
			$.each(data, function(key, value) {
				if (typeof value == 'object') {
					$.extend(result, _this.formatJson2FormParam(value, prefix + '.' + key));
				} else {
					result[prefix + '.' + key] = value;
				}
			});
		}
		return result;
	},
};
