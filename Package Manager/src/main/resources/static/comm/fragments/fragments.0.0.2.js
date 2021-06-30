
$(document).attr("title","三三云学堂");


var headerFn = function(){
    // logo图片
    $(".main .header .header_tp > img.logoImg").on('click', function() {
        window.location.href = window.location.href.replace(window.location.pathname, '');
    });
	$("#navigate dd").each(function(){
		//横向导航栏
		$(this).bind('click',function(){
			if (!$(this).hasClass('active_dd')) {
				$(this).addClass('active_dd').siblings().removeClass('active_dd');
			}
		});
	});

	//竖向导航栏展开
	$("#navigate dt").bind('click',function(){
		if ($(this).find('i').hasClass('Rotate_180')) {
			$(this).find('i').removeClass('Rotate_180');
			$("#asideNav").slideUp();
			$(this).css({"background":"#173b58"});
		}else{
			$(this).find('i').addClass('Rotate_180');
			$("#asideNav").slideDown();
			$(this).css({"background":"#1274c9"});
		}
	});

	//竖向导航栏展开子栏目操作
	$("#asideNav li a").each(function(){
		$(this).bind('click',function(){
			var $Index=$(this).parent('li').index();
			var $imgUrl=$(this).find('img').attr('src');
			var $imgUrl_on=$imgUrl.split('.png')[0]+'_on.png';
			var otherImgLength=$(this).parent('li').siblings().find('a').find('img');
			if (!$(this).hasClass('active_a')) {
				$(this).addClass('active_a');
				$(this).find('img').attr({src:$imgUrl_on});
				$(this).parent('li').siblings().find('a').removeClass('active_a');
				$("#asideNav li").not($("#asideNav li").eq($Index)).each(function(){
					var imgurl=$(this).find("a>img").attr('src');
					if (imgurl.indexOf('_on.png')!=-1) {
						imgurl=imgurl.split('_on.png')[0]+'.png';
						$(this).find("a>img").attr({src:imgurl});
					}
				})
			}
		})
	});
}

//滚动条高度判断返回顶部按钮是否出现
function scrolltopFn($scrollTops){
	//加入点击返回按钮
	var $backBtn=$("<div id='backtop-btn' class='backtop-btn'><i></i></div>");
	if ($("#backtop-btn").length == 0) {
		$("body").append($backBtn);
	}
	scrollFn($scrollTops);
	$("#backtop-btn").on('click',function(){
		$('body,html').animate({scrollTop:0},500);
	})
}

function scrollFn($scrollTops){
	if ($scrollTops>($(window).height()/1.5)) {
		$("#backtop-btn").show();
	}else{
		$("#backtop-btn").hide();
	}
}
$(document).ready(function(){
	$(window).scroll(function(){
		var $scrollTop=$(window).scrollTop();
		scrollFn($scrollTop)
	});
});

var header = window.header = {
    menuJson : null,
    menuMap : {
        left : {

        },
        top : {

        }
    },
    init : function() {
        var _this = this;
        if (_this.menuJson == null || _this.menuJson == '') {
            return;
        }
        if (typeof _this.menuJson == "string") {
            _this.menuJson = JSON.parse(_this.menuJson);
        }
        _this.initMenu();
        _this.initNavigation();
        headerFn();
    },
    initMenu : function() {
        var _this = this;
        var left_menu = _this.menuJson._left_menu_key_;
        var top_menu = _this.menuJson._top_menu_key_;
        var leftHtml = "";
        var topHtml = "";
        left_menu.forEach(function(elt, i, array) {
            var menuId = elt.menuId;
            _this.menuMap.left[menuId] = elt;
            var html = '<li><a href="javascript:header.leftClick(\''+menuId+'\');" menu-id="' + menuId +'"><img src="'+elt.icon1Url+'" />'+elt.name+'</a></li>';
            leftHtml += html;
        });
        $("#asideNav").append(leftHtml);
        top_menu.forEach(function(elt, i, array) {
            var menuId = elt.menuId;
            _this.menuMap.top[menuId] = elt;
            var html = '<dd><a href="'+elt.url+'"><img src="'+elt.icon1Url+'"/><span>'+elt.name+'</span></a></dd>';
            topHtml += html;
        });
        $("#navigate").append(topHtml);
    },
    initNavigation : function() {
        var _this = this;
        var menuTopKey = null, menuTopKey = null;
        if (sessionStorage) {
            menuTopKey = sessionStorage.getItem('_menu_top_key_');
            menuIndexLeftKey = sessionStorage.getItem('_menu_index_left_key_');
        }
        if (menuTopKey == null || menuTopKey == undefined  || menuTopKey.trim() == '') {
            menuTopKey = $.cookie('_menu_top_key_');
            if (menuTopKey != null && menuTopKey != undefined  && menuTopKey.trim() != '' && sessionStorage) {
                sessionStorage.setItem('_menu_top_key_', menuTopKey);
            }
        }
        if (menuIndexLeftKey == null || menuIndexLeftKey == undefined  || menuIndexLeftKey.trim() == '') {
            menuIndexLeftKey = $.cookie('_menu_index_left_key_');
            if (menuIndexLeftKey != null && menuIndexLeftKey != undefined  && menuIndexLeftKey.trim() != '' && sessionStorage) {
                sessionStorage.setItem('_menu_index_left_key_', menuIndexLeftKey);
            }
        }

        if(!$.isEmptyObject(menuTopKey) && menuTopKey != 'default') {
            var childrenMenu = _this.menuMap.top[menuTopKey];
            var navigationHtml = "";
            childrenMenu.forEach(function(elt, i, array) {
                elt.children.forEach(function(element){
                    navigationHtml += _this.appendNavigationHtml(element);
                });
            });
            $("#navigation").html(navigationHtml);
        } else if(!$.isEmptyObject(menuIndexLeftKey)) {
            _this.leftClick(_this.menuJson._left_menu_key_[menuIndexLeftKey-1].menuId);
        }
    },
    leftClick : function(menuId) {
        var _this = this;
        var menu = _this.menuMap.left[menuId];
        var childrenMenu = menu.children;
        var html = "<h4>" + $("#asideNav a[menu-id='" + menuId + "']").html() + "</h4>";
        html += '<div class="small_nav">';
        childrenMenu.forEach(function(elt, i, array) {
            html += _this.appendNavigationHtml(elt);
        });
        html += '</div>';
        $("#navigation").html(html);
        var imgurl= $("#navigation h4 img").attr('src');
        if (imgurl.indexOf('_on.png') == -1) {
            imgurl=imgurl.substring(0, imgurl.length-4)+'_on.png';
            $("#navigation h4 img").attr({src:imgurl});
        }
        _this.menuJson._left_menu_key_.forEach(function(elt, i, array) {
            if(elt.menuId == menuId) {
                $.cookie("_menu_index_left_key_", i+1, { path: '/'});
                if (sessionStorage) {
                    sessionStorage.setItem('_menu_index_left_key_', i+1);
                }
            }
        })
    },
    appendNavigationHtml : function(menu, isActive) {
        return '<a class="a_list ' + (isActive ? 'active' : '') + '" href="'+menu.url+'">'+menu.name+'</a>';
    }
};

// 底部
var footer = {
    "_logoSrc": "/comm/img/xclogo.png",
    "address": "学创教育科技有限公司&emsp;&emsp;地址：武汉市光谷国际广场B座20楼&emsp;&emsp;电话：+86-027-87107188",
    "version" : "三三云校园 V3.00",
    "copyright": "Copyright © 2016-2018 学创教育科技有限公司.All rights reserved.",
    "init": function() {
        var footer = '<div style="text-align: center;font-size: 12px;">';
        footer += '<p>' + this.version + '</p>';
        footer += '<p>' + this.copyright + '</p>';
        footer += '</div>';
        $(".footer").append(footer);
    }
}