/**
 * 此js放在comm目录下，通过nginx做静态资源代理，各个web项目共用一份
 */

$(document).attr("title","ETS-School");

// 头部
var header = {
	"_logoSrc": "/comm/img/logo.png",	// logo图片的位置
	"_dropDownArrowSrc": "/comm/img/icon_ZhanKai.png",	// 下拉箭头的图片位置
	"_defaultPhotoUrl": "/comm/img/touxiang.png",	// 默认头像的图片位置
	"userName": "",	// 用户姓名，传入
	"userPhotoUrl": "",	// 用户头像，传入
	"menuList": null, // 个人信息下拉菜单
	"init": function() {
		var header_lt = '<div class="header_lt">' + '<img src="' + this._logoSrc + '"/>' + '</div>';
		var header_rt = '<div class="header_rt"><div class="system_out"><span class="syetemCloseImg"></span></div>';
		header_rt += '<div class="user_box">';
		header_rt += '<div class="user_box_tp">';
		header_rt += '<img class="nav_status" src="' + this._dropDownArrowSrc + '"/>';
		header_rt += '<span class="user_name" id="user_name" title="' + this.userName + '">' + this.userName + '</span>';
		header_rt += '<img class="user_photo" src="' + this.userPhotoUrl + '"/>';
		header_rt += '</div>';
		var drop_down = '<div class="user_box_bt">';
		drop_down += '<ul class="shezhiNav">';
		drop_down += '<div class="user_box_triangle"></div>';
		drop_down += '</ul>';
		drop_down += '</div>';
		drop_down += '</div>';
		$(".header").append(header_lt + header_rt + drop_down);

		if (this.menuList != null && this.menuList.length > 0) {
			for (var i = 0;i < this.menuList.length;i++) {
				var menu = this.menuList[i];
				var menuHtml = '<li><img src="' + menu.icon1Url + '"/><a href="' + menu.url + '">'+ menu.name + '</a></li>';
				$(".shezhiNav").append(menuHtml);
			}
		}
		$(".system_out .syetemCloseImg").mouseenter(function(){
		}).mouseleave(function(){
			$(this).removeClass('clickactive');
		}).click(function(){
			$(this).addClass('clickactive');
		});

		$(".user_box").mouseenter(function(){
			$(".user_box_bt").show();
		}).mouseleave(function(){
			$(".user_box_bt").hide();
		})

		$(".shezhiNav li").each(function(){
			$(this).bind('click',function(){
				$(".user_box_bt").hide();
			})
		});
	}
}
/**
 * 解决系统总览下拉菜单数多的情况下显示不全；
 * 20190220
 * lemon
 */
function setMinHeightV2(Number){
	function MinHeight(minHight){
		var footerh=$("#footer").outerHeight();
		minHight=minHight-footerh;
		var menu2Height= $(".asideNav").find("li").length*46; //二级菜单高度
		if($(".mainContent").length > 0){
			$(".mainContent").css('min-height',minHight);
			var mainContentHeight = $(".mainContent").height();
			if(menu2Height > mainContentHeight){
				$(".mainContent").css({
					"minHeight" : menu2Height+'px'
				});
			}
		}
		/**
		 * //解决学习任务与学习工具和教材的页面主体结构不同，
		 * 20190220
		 * lemon
		 */
		if($(".Con_Main").length > 0){
			$(".Con_Main").css('min-height',minHight);
			var conMainHeight = $(".Con_Main").height();
			if(menu2Height > conMainHeight){
				$(".Con_Main").css({
					"minHeight" : menu2Height+'px'
				});
				$(".Con_Main").css({
					"background" : "#fff"
				});
			}
		}
	}
	var minHight=$(window).height()-$(".header").outerHeight()-Number;
	MinHeight(minHight);
}
$(document).ready(function(){
	//主体部分设置一个最低高度(项目有头部需设置主题最小高度为系统总览菜单下二级菜单的总高度)
	if($(".asideNav").length>0){
		setMinHeightV2(0);
		scrolltopFn($(window).scrollTop());
	}
});

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
$(window).scroll(function(){
	var $scrollTop=$(window).scrollTop();
	scrollFn($scrollTop)
});

function scrollFn($scrollTops){
	if ($scrollTops>($(window).height()/1.5)) {
		$("#backtop-btn").show();
	}else{
		$("#backtop-btn").hide();
	}
}
// 底部
var footer = {
	"_logoSrc": "/comm/img/xclogo.png",
	"address": "学创教育科技有限公司&emsp;&emsp;地址：武汉市光谷国际广场B座20楼&emsp;&emsp;电话：+86-027-87107188",
	"version" : "三三云校园 V3.00",
	"copyright": "Copyright © 2016-2018 学创教育科技有限公司.All rights reserved.",
	"init": function() {
//			var footer = '<div class="footer_lt">';
//			footer += '<a href="javascript:;"><img src="' + this._logoSrc + '"/></a>';
//			footer += '</div>';
//			footer += '<div class="footer_rt">';
//			footer += '<p>' + this.address + '</p>';
//			footer += '<p>' + this.copyright + '</p>'
		var footer = '<div style="text-align: center;font-size: 12px;">';
		footer += '<p>' + this.version + '</p>';
		footer += '<p>' + this.copyright + '</p>';
		footer += '</div>';
		$(".footer").append(footer);
	}
}
