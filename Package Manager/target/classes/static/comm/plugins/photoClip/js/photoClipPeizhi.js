/*详细配置参考https://www.npmjs.com/package/photoclip*/
function showPhotoClipTips (tiptext) {
	clearInterval(timer);
	var tip = document.getElementById('photoClip-tips');
	tip.style.display = 'block';
	tip.innerHTML = tiptext;
	var timer = setTimeout(function(){
		tip.innerHTML = '<b>'+pluginText[photoClipPeizhi.language].t_4+'</b>';
		//tip.style.display = 'none';
		clearInterval(timer);
	},2000);
}
//插件所有文字展示中文简体zh-chs、繁体zh-cht
var pluginText = {
	'zh_CN':{
		't_1':'上传图片',
		't_2':'截取并预览',
		't_3':'图片预览',
		't_4':'如不满意则请重新截取并预览',
		't_5':'开始读取图片',
		't_6':'图片读取完成',
		't_7':'图片上传错误请重试',
		't_8':'图片裁剪成功，点击确定获取图片地址！',
		't_9':'确定',
		't_10':'取消',
		't_11':'您还未截取图片，请先截取您所需的图片',
		't_12':"沒有可裁剪的圖片！"
	},
	'zh_TW':{
		't_1':'上傳圖片',
		't_2':'擷取並預覽',
		't_3':'圖片預覽',
		't_4':'如不滿意則請重新擷取並預覽',
		't_5':'開始讀取圖片',
		't_6':'圖片讀取完成',
		't_7':'圖片上傳錯誤請重試',
		't_8':'圖片裁剪成功，點擊確定獲取圖片地址！',
		't_9':'確定',
		't_10':'取消',
		't_11':'您還未截取圖片，請先截取您所需的圖片',
		't_12':"没有可裁剪的图片！"
	}
}


var photoClipDataURL = '';
var photoClipDataName = '';
var photoClipPeizhi = {
	language:'zh-CN', /*zh-CN中文简体，zh-TW中文繁体，默认中文简体*/
	size: 140,   /*可为数组[260,420]即为宽高截取框大小。当值为数字时，表示截取框为宽高都等于该值的正方形。默认值为 [100,100]  */
	outputSize: [0,0], /*输出图像大小。当值为数字时，表示输出宽度，此时高度根据截取框比例自适应。当值为数组时，数组中索引 [0] 和 [1] 所对应的值分别表示宽和高，若宽或高有一项值无效，则会根据另一项等比自适应。默认值为[0,0]，表示输出图像原始大小。*/
	//adaptive: ['60%', '80%'], /*截取框自适应。设置该选项后，size 选项将会失效，此时 size 进用于计算截取框的宽高比例。*/
	file: '#clipFile',/*上传图片的 <input type="file"> 控件的选择器或者DOM对象。如果有多个，可使用英文逗号隔开的选择器字符串，或者DOM对象数组。*/
	view: '#clipView',/*显示截取后图像的容器的选择器或者DOM对象。如果有多个，可使用英文逗号隔开的选择器字符串，或者DOM对象数组。*/
	ok: '#clipBtn',/*确认截图按钮的选择器或者DOM对象。如果有多个，可使用英文逗号隔开的选择器字符串，或者DOM对象数组。*/
	outputType:'png', /*指定输出图片的类型，可选 'jpg' 和 'png' 两种种类型，默认为 'jpg'。*/
	//img: './img/2.jpg',/*需要裁剪图片的url地址。该参数表示当前立即开始读取图片，不需要使用 file 控件获取。注意，加载的图片必须要与本程序同源，如果图片跨域，则无法截图。*/
	//outputQuality:0.8, /*图片输出质量，仅对 jpeg 格式的图片有效，取值 0 - 1，默认为0.8。（这个质量并不是图片的最终质量，而是在经过 lrz 插件压缩后的基础上输出的质量。*/
	maxZoom: 1, /*图片的最大缩放比，默认为 1。*/
	//rotateFree:false, /*是否启用图片自由旋转。由于安卓浏览器上存在性能问题，因此在安卓设备上默认关闭。*/
	style:{ /*样式配置。以下为子属性：*/
		maskColor:'rgba(0,0,0,.3)', /*遮罩层的颜色。默认为 'rgba(0,0,0,.5)'。*/
		//maskBorder:'2px dashed #ddd', /*遮罩层的 border 样式。默认为 '2px dashed #ddd'。*/
		//jpgFillColor:'', /*当输出 jpg 格式时透明区域的填充色。默认为 '#fff'。*/
	},
	//errorMsg:{ /*错误信息对象，包含各个阶段出错时的文字说明。以下为子属性：*/
	//	noSupport:'', /*浏览器无法支持本插件。将会使用 alert 弹出该信息，若不希望弹出，可将该值置空。*/
	//	imgError:'', /*使用 file 控件读取图片格式错误时的错误信息。将会在 loadError 回调的错误信息中输出。*/
	//	imgHandleError:'', /*lrz 压缩插件处理图片失败时的错误信息。将会在 loadError 回调的错误信息中输出。*/
	//	imgLoadError:'', /*图片加载出错时的错误信息。将会在 loadError 回调的错误信息中输出。*/
	//	noImg:'', /*没有加载完成的图片时，执行截图操作时的错误信息。将会在 fail 回调的失败信息中输出。*/
	//	clipError:'' /*截图出错时的错误信息。将会在 fail 回调的失败信息中输出。*/
	//},
	loadStart: function(file) {/*图片开始加载的回调函数。this 指向当前 PhotoClip 的实例对象，并将正在加载的 file 对象作为参数传入。（如果是使用非 file 的方式加载图片，则该参数为图片的 url）*/
		// console.log(file);
		if (typeof(file) == 'object') {
			photoClipDataName = file.name;
		}
		if(typeof(file) == 'string'){
			photoClipDataName = file.substring(file.lastIndexOf("/")+1,file.length);
		}
		showPhotoClipTips(pluginText[photoClipPeizhi.language].t_5);
	},
	loadComplete: function(img) {/*图片加载完成的回调函数。this 指向当前 PhotoClip 的实例对象，并将图片的 <img> 对象作为参数传入。*/
		//console.log('照片读取完成');
		showPhotoClipTips(pluginText[photoClipPeizhi.language].t_6);
	},
	loadError: function(){ /*图片加载失败的回调函数。this 指向当前 PhotoClip 的实例对象，并将错误信息作为第一个参数传入，如果还有其它错误对象或者信息会作为第二个参数传入。*/
		//alert('上传错误请重试！');
		showPhotoClipTips(pluginText[photoClipPeizhi.language].t_7);
	},
	done: function(dataURL) {/*裁剪完成的回调函数。this 指向当前 PhotoClip 的实例对象，会将裁剪出的图像数据DataURL作为参数传入。*/
		showPhotoClipTips(pluginText[photoClipPeizhi.language].t_8);
		photoClipDataURL = dataURL;
		//console.log(dataURL); 
	},
	fail: function(msg) {/*裁剪失败的回调函数。this 指向当前 PhotoClip 的实例对象，会将失败信息作为参数传入。*/
		//alert(msg);
		showPhotoClipTips(msg);
	}
}

var photoClipFn = function(photoClipPeizhi,fn) {
	var textobj = pluginText[photoClipPeizhi.language];
	//初始化配置
	if (!document.getElementById('photoClip-box')) {
		var photoClipHtml = "<section class='photoClip-box-cont'>"
								+"<nav class='clip-head'><span class='box-title'>"+textobj.t_1+"</span><i class='clip-close' id='clip-close'></i></nav>"
								+"<section class='photoClip-box-cont-body'>"
									+"<article class='photoClip-btn-box'><span class='btn_ clipFilebtn'>"+textobj.t_1+"<input class='clipinput' type='file' id='clipFile'></span><span class='btn_ clipBtn' id='clipBtn'>"+textobj.t_2+"</span></article>"
									+"<section class='clipArea' id='clipArea'></section>"
									//+"<p class='photoClip-tips' id='photoClip-tips'></p>"
									+"<h2 class='photoClip-title'>"+textobj.t_3+"（<span class='photoClip-tips' id='photoClip-tips'><b>"+textobj.t_4+"</b></span>）</h2>"
									+"<section class='clipView-warp'><section class='clipView' id='clipView'></section></section>"
									+"<section class='photoClip-btn-box2'><span class='btn_' id='clip-sure-btn'>"+textobj.t_9+"</span><span class='btn_' id='clip-cancle-btn'>"+textobj.t_10+"</span></section>"
								+"</section>"
							+"</section>";
		var photoClipbox = document.createElement('section'); //新建元素
		photoClipbox.id = 'photoClip-box';
		photoClipbox.innerHTML = photoClipHtml;
		document.body.appendChild(photoClipbox);	
		document.getElementById("photoClip-box").classList.add("photoClip-box");
		var photoClip = new PhotoClip('#clipArea', photoClipPeizhi); //初始化元素
		
		//关闭
		document.getElementById('clip-close').onclick = function(){
			document.getElementById("photoClip-box").style.display = 'none';
		}
		//确定
		document.getElementById('clip-sure-btn').onclick = function(){
			if (photoClipDataURL.split('').length>0) {
				fn(photoClipDataURL,photoClipDataName);
				document.getElementById("photoClip-box").style.display = 'none';
			}else{
				showPhotoClipTips('您还未截取图片，请先截取您所需的图片！');
			}
		}
		//取消
		document.getElementById('clip-cancle-btn').onclick = function(){
			document.getElementById("photoClip-box").style.display = 'none';
		}
		
	}
	
}