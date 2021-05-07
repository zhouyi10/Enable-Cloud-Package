/*! PhotoSwipe - v4.1.2 - 2019-01-07
* http://photoswipe.com*/
//通过img元素获取图片的原始高度
function getNaturalSize(Domlement) {
	var natureSize = {};
	if(window.naturalWidth && window.naturalHeight) {
		natureSize.width = Domlement.naturalWidth;
		natureSizeheight = Domlement.naturalHeight;
	} else {
		var img = new Image();
		img.src = DomElement.src;
		natureSize.width = img.width;
		natureSize.height = img.height;
	}
	return natureSize;
}
//通过图片地址获取图片实际宽高
function getImgWH($src){
	var sizeWH = {};
	var imgs = new Image();
	imgs.src = $src;
	sizeWH.width = imgs.width;
	sizeWH.height = imgs.height;
	return sizeWH;
}

/*注：分享按钮下面的按钮在photoswipe-ui-default.js中设置shareButtons数组*/
var initPhotoSwipeFromDOM = function(imgParentWarp,imgParentClass) {
	/*页面插入预览的的模块mr-photoswipe*/
	if (!document.getElementById('mr-photoswipe')) {
		var previewModel = "<div class='pswp' id='mr-photoswipe' tabindex='-1' role='dialog' aria-hidden='true'>"
							    +"<div class='pswp__bg'></div>"
							    +"<div class='pswp__scroll-wrap'>"
							        +"<div class='pswp__container'>"
							            +"<div class='pswp__item'></div>"
							            +"<div class='pswp__item'></div>"
							            +"<div class='pswp__item'></div>"
							        +"</div>"
							        +"<div class='pswp__ui pswp__ui--hidden'>"
							            +"<div class='pswp__top-bar'>"
							                +"<div class='pswp__counter'></div>"
							                +"<button class='pswp__button pswp__button--close' title='close'></button>"
							                // +"<button class='pswp__button pswp__button--share' title='share'></button>"
							                +"<button class='pswp__button pswp__button--fs' title='switch'></button>"
							                +"<button class='pswp__button pswp__button--zoom' title='scale'></button>"
							                +"<div class='pswp__preloader'>"
							                    +"<div class='pswp__preloader__icn'>"
							                      +"<div class='pswp__preloader__cut'>"
							                        +"<div class='pswp__preloader__donut'></div>"
							                      +"</div>"
							                    +"</div>"
							                +"</div>"
							            +"</div>"
							            +"<div class='pswp__share-modal pswp__share-modal--hidden pswp__single-tap'>"
							                +"<div class='pswp__share-tooltip'></div> "
							            +"</div>"
							            +"<button class='pswp__button pswp__button--arrow--left' title='Previous (arrow left)'></button>"
							            +"<button class='pswp__button pswp__button--arrow--right' title='Next (arrow right)'></button>"
							            +"<div class='pswp__caption'>"
							                +"<div class='pswp__caption__center'></div>"
							            +"</div>"
						          	+"</div>"
						        +"</div>"
							+"</div>";
		var previewModelwarp = document.createElement('div');
		previewModelwarp.innerHTML = previewModel;
		document.body.appendChild(previewModelwarp);
	}
	
	var parseThumbnailElements = function(el) {
		var thumbElements = el.childNodes,
			numNodes = thumbElements.length,
			items = [],
			el,
			childElements,
			thumbnailEl,
			size,
			item;

		for(var i = 0; i < numNodes; i++) {
			el = thumbElements[i];

			// include only element nodes 
			if(el.nodeType !== 1) {
				continue;
			}

			childElements = el.children;
			size = el.getAttribute('data-size').split('x');

			// create slide object
			item = {
				src: el.getAttribute('href'),
				w: parseInt(size[0], 10),
				h: parseInt(size[1], 10),
				author: el.getAttribute('data-author')
			};

			item.el = el; // save link to element for getThumbBoundsFn

			if(childElements.length > 0) {
				item.msrc = childElements[0].getAttribute('src'); // thumbnail url
				if(childElements.length > 1) {
					item.title = childElements[1].innerHTML; // caption (contents of figure)
				}
			}

			var mediumSrc = el.getAttribute('data-med');
			if(mediumSrc) {
				size = el.getAttribute('data-med-size').split('x');
				// "medium-sized" image
				item.m = {
					src: mediumSrc,
					w: parseInt(size[0], 10),
					h: parseInt(size[1], 10)
				};
			}
			// original image
			item.o = {
				src: item.src,
				w: item.w,
				h: item.h
			};

			items.push(item);
		}

		return items;
	};

	// find nearest parent element
	var closest = function closest(el, fn) {
		debugger
		return el && (fn(el) ? el : closest(el.parentNode, fn));
	};

	var onThumbnailsClick = function(e) {
		debugger
		e = e || window.event;
		e.preventDefault ? e.preventDefault() : e.returnValue = false;
		var eTarget = e.target || e.srcElement;
		var clickedListItem = closest(eTarget, function(el) {
			return el.tagName === 'A';
		});

		if(!clickedListItem) {
			return;
		}

		var clickedGallery = clickedListItem.parentNode;

		if (CommUtils.isEmpty(clickedGallery)) {
			return;
		}

		var childNodes = clickedListItem.parentNode.childNodes,
			numChildNodes = childNodes.length,
			nodeIndex = 0,
			index;

		for(var i = 0; i < numChildNodes; i++) {
			if(childNodes[i].nodeType !== 1) {
				continue;
			}

			if(childNodes[i] === clickedListItem) {
				index = nodeIndex;
				break;
			}
			nodeIndex++;
		}

		if(index >= 0) {
			openPhotoSwipe(index, clickedGallery);
		}
		return false;
	};

	var photoswipeParseHash = function() {
		var hash = window.location.hash.substring(1),
			params = {};

		if(hash.length < 5) { // pid=1
			return params;
		}

		var vars = hash.split('&');
		for(var i = 0; i < vars.length; i++) {
			if(!vars[i]) {
				continue;
			}
			var pair = vars[i].split('=');
			if(pair.length < 2) {
				continue;
			}
			params[pair[0]] = pair[1];
		}

		if(params.gid) {
			params.gid = parseInt(params.gid, 10);
		}

		return params;
	};

	var openPhotoSwipe = function(index, galleryElement, disableAnimation, fromURL) {
		var pswpElement = document.querySelectorAll('.pswp')[0],
			gallery,
			options,
			items;

		items = parseThumbnailElements(galleryElement);

		// define options (if needed)
		options = {

			galleryUID: galleryElement.getAttribute('data-pswp-uid'),

			getThumbBoundsFn: function(index) {
				// See Options->getThumbBoundsFn section of docs for more info
				var thumbnail = items[index].el.children[0],
					pageYScroll = window.pageYOffset || document.documentElement.scrollTop,
					rect = thumbnail.getBoundingClientRect();

				return {
					x: rect.left,
					y: rect.top + pageYScroll,
					w: rect.width
				};
			},

			addCaptionHTMLFn: function(item, captionEl, isFake) {
				if(!item.title) {
					captionEl.children[0].innerText = '';
					return false;
				}
				captionEl.children[0].innerHTML = item.title + '<br/><small>Photo: ' + item.author + '</small>';
				return true;
			}

		};

		if(fromURL) {
			if(options.galleryPIDs) {
				// parse real index when custom PIDs are used 
				// http://photoswipe.com/documentation/faq.html#custom-pid-in-url
				for(var j = 0; j < items.length; j++) {
					if(items[j].pid == index) {
						options.index = j;
						break;
					}
				}
			} else {
				options.index = parseInt(index, 10) - 1;
			}
		} else {
			options.index = parseInt(index, 10);
		}

		// exit if index not found
		if(isNaN(options.index)) {
			return;
		}

		var radios = document.getElementsByName('gallery-style');
		for(var i = 0, length = radios.length; i < length; i++) {
			if(radios[i].checked) {
				if(radios[i].id == 'radio-all-controls') {

				} else if(radios[i].id == 'radio-minimal-black') {
					options.mainClass = 'pswp--minimal--dark';
					options.barsSize = {
						top: 0,
						bottom: 0
					};
					options.captionEl = false;
					options.fullscreenEl = false;
					options.shareEl = false;
					options.bgOpacity = 0.85;
					options.tapToClose = true;
					options.tapToToggleControls = false;
				}
				break;
			}
		}

		if(disableAnimation) {
			options.showAnimationDuration = 0;
		}

		// Pass data to PhotoSwipe and initialize it
		gallery = new PhotoSwipe(pswpElement, PhotoSwipeUI_Default, items, options);

		// see: http://photoswipe.com/documentation/responsive-images.html
		var realViewportWidth,
			useLargeImages = false,
			firstResize = true,
			imageSrcWillChange;

		gallery.listen('beforeResize', function() {

			var dpiRatio = window.devicePixelRatio ? window.devicePixelRatio : 1;
			dpiRatio = Math.min(dpiRatio, 2.5);
			realViewportWidth = gallery.viewportSize.x * dpiRatio;

			if(realViewportWidth >= 1200 || (!gallery.likelyTouchDevice && realViewportWidth > 800) || screen.width > 1200) {
				if(!useLargeImages) {
					useLargeImages = true;
					imageSrcWillChange = true;
				}

			} else {
				if(useLargeImages) {
					useLargeImages = false;
					imageSrcWillChange = true;
				}
			}

			if(imageSrcWillChange && !firstResize) {
				gallery.invalidateCurrItems();
			}

			if(firstResize) {
				firstResize = false;
			}

			imageSrcWillChange = false;

		});

		gallery.listen('gettingData', function(index, item) {
			if(useLargeImages) {
				item.src = item.o.src;
				item.w = item.o.w;
				item.h = item.o.h;
			} else {
				item.src = item.m.src;
				item.w = item.m.w;
				item.h = item.m.h;
			}
		});

		gallery.init();
	};

	// select all gallery elements
	var galleryElements = document.querySelectorAll(imgParentWarp);
	for(var i = 0, l = galleryElements.length; i < l; i++) {
		galleryElements[i].onclick = null; //先解绑点击事件
		setImgAttribute(galleryElements[i],i);//给每个图片的父元素设置属性  要么渲染图片时执行此方案 要么这里执行
	}
		
	//给每个图片的父元素设置属性
	function setImgAttribute($parent,$index){
		var l_ = $parent.querySelectorAll(imgParentClass).length;
		if (l_>0) {
			for (var j = 0; j < l_; j++) {
				setAttributeFn($parent.querySelectorAll(imgParentClass)[j],$parent,$index);
			}
		}
	}
	//设置属性
	function setAttributeFn($imgParent,$parent,$index){
		var img1 = $imgParent.getElementsByTagName('img')[0];
		var img1Src = img1.getAttribute('src'); //获取图片地址
		var imgwh = getImgWH(img1Src); //获取图片真实宽高
		$imgParent.setAttribute('data-size',imgwh.width+'x'+imgwh.height);//pc端size
		$imgParent.setAttribute('href',img1Src);//pc端src
		$imgParent.setAttribute('data-med-size',imgwh.width+'x'+imgwh.height); //移动端size
		$imgParent.setAttribute('data-med',img1Src);//移动端src
		//设置完属性以后再执行绑定后面的点击事件
		$parent.setAttribute('data-pswp-uid', $parent.getAttribute('id'));
		$parent.onclick = onThumbnailsClick;
	}

	// Parse URL and open gallery if it contains #&pid=3&gid=1
	var hashData = photoswipeParseHash();
	if(hashData.pid && hashData.gid) {
		openPhotoSwipe(hashData.pid, galleryElements[hashData.gid - 1], true, true);
	}
};

//initPhotoSwipeFromDOM('.demo-gallery','.img_gallery'); //容器类名   图片父元素类名

