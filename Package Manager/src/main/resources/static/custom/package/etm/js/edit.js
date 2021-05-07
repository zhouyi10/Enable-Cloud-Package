(function (win, $) {

    var i = 0;
    var j = 0;

    var ETS = win.ETS = {

        etmBookInfo: {
            "etmBookId": "",
            "textBookName": "",
            "isbn": "",
            "stageCode": "",
            "stageName": "",
            "gradeCode": "",
            "gradeName": "",
            "subjectCode": "",
            "subjectName": "",
            "textBookVersionCode": "",
            "textBookVersionName": "",
            "termCode": "",
            "termName": "",
            "coverId": "",
            "coverUrl": "",
            "coverImgName": "",
            "contentId": "",
            "userId": ""
        },
        pageList: [],
        pageInfo: {
            "pageInfoId": '',
            "sequence": '',
            "pageInfoImgId": '',
            "pageInfoImgName": '',
            "pageInfoImgWidth": '',
            "pageInfoImgHeight": '',
            "pageInfoImgRealUrl": '',
            "pageInfoMp3Id": '',
            "pageInfoMp3Name": '',
            "pageInfoMp3LoadUrl": '',
            "status": '',
            semtenceInfoList: []
        },
        semtenceInfo: {
            "semtenceId": '',
            "sequence": '',
            "semtenceInfoMp3Id": '',
            "semtenceInfoMp3Name": '',
            "semtenceInfoMp3LoadUrl": '',
            "semtenceInfoText": '',
            semtenceInfoCoordinateList: [],
        },
        semtenceInfoCoordinate: {
            "regionId": "",
            "x": "",
            "y": "",
            "height": "",
            "width": "",
            "semtenceInfoDiv": '',
            "semtenceYtop": ""
        },

        init: function () {
            this.initDefault();
        },

        showPages: function (pageNumber, totalPages) {

            pageNumber = parseInt(pageNumber);
            if (totalPages <= 12) {  //显示 11 条
                for (let k = 1; k <= totalPages; k++) {
                    $("#pageCode" + k).css("display", "none")
                }

                if ($("#pageCode0").length > 0) {
                    $("#pageCode0").remove()
                }

                if ($("#pageCodeMax").length > 0) {
                    $("#pageCodeMax").remove()
                }

                for (let k = 1; k <= 12; k++) {
                    $("#pageCode" + k).css("display", "inline-block")
                }

            } else if (totalPages > 12) {
                if (pageNumber <= 7) {
                    for (let k = 1; k <= totalPages; k++) {
                        $("#pageCode" + k).css("display", "none")
                    }

                    for (let k = 1; k <= 11; k++) {
                        $("#pageCode" + k).css("display", "inline-block")
                    }

                    if ($("#pageCode0").length > 0) {
                        $("#pageCode0").remove()
                    }

                    if ($("#pageCodeMax").length > 0) {
                        $("#pageCodeMax").remove()
                    }

                    $("#pageCode" + totalPages).before("<span id='pageCodeMax' style='margin-left: 5px' class='pprPager' >" + "..." + "</span>");

                    $("#pageCode" + totalPages).css("display", "inline-block")

                } else if (pageNumber > 7 && pageNumber < totalPages - 6) {

                    for (let k = 1; k <= totalPages; k++) {
                        $("#pageCode" + k).css("display", "none")
                    }

                    $("#pageCode" + 1).css("display", "inline-block")

                    if ($("#pageCode0").length > 0) {
                        $("#pageCode0").remove()
                    }
                    $("#pageCode1").after("<span id='pageCode0' style='margin-left: 5px' class='pprPager' >" + "..." + "</span>");

                    for (let k = pageNumber - 5; k <= pageNumber + 5; k++) {
                        $("#pageCode" + k).css("display", "inline-block")
                    }

                    if ($("#pageCodeMax").length > 0) {
                        $("#pageCodeMax").remove()
                    }
                    $("#pageCode" + totalPages).before("<span id='pageCodeMax' style='margin-left: 5px' class='pprPager' >" + "..." + "</span>");

                    $("#pageCode" + totalPages).css("display", "inline-block")

                } else if (pageNumber > 7 && pageNumber >= totalPages - 6) {

                    for (let k = 1; k <= totalPages; k++) {
                        $("#pageCode" + k).css("display", "none")
                    }
                    $("#pageCode" + 1).css("display", "inline-block")
                    if ($("#pageCode0").length > 0) {
                        $("#pageCode0").remove()
                    }
                    $("#pageCode1").after("<span id='pageCode0' style='margin-left: 5px' class='pprPager' >" + "..." + "</span>");

                    if ($("#pageCodeMax").length > 0) {
                        $("#pageCodeMax").remove()
                    }
                    for (let k = totalPages - 10; k <= totalPages; k++) {
                        $("#pageCode" + k).css("display", "inline-block")
                    }
                }
            }
        },

        showSemtencePages: function (pageNumber, totalPages) {

            pageNumber = parseInt(pageNumber);
            if (totalPages <= 12) {
                for (let k = 1; k <= totalPages; k++) {
                    $("#semtencePageCode" + k).css("display", "none")
                }

                if ($("#semtencePageCode0").length > 0) {
                    $("#semtencePageCode0").remove()
                }

                if ($("#semtencePageCodeMax").length > 0) {
                    $("#semtencePageCodeMax").remove()
                }

                for (let k = 1; k <= 12; k++) {
                    $("#semtencePageCode" + k).css("display", "inline-block")
                }

            } else if (totalPages > 12) {
                if (pageNumber <= 7) {
                    for (let k = 1; k <= totalPages; k++) {
                        $("#semtencePageCode" + k).css("display", "none")
                    }

                    for (let k = 1; k <= 11; k++) {
                        $("#semtencePageCode" + k).css("display", "inline-block")
                    }

                    if ($("#semtencePageCode0").length > 0) {
                        $("#semtencePageCode0").remove()
                    }

                    if ($("#semtencePageCodeMax").length > 0) {
                        $("#semtencePageCodeMax").remove()
                    }
                    $("#semtencePageCode" + totalPages).before("<span id='semtencePageCodeMax' style='margin-left: 5px' class='pprPager' >" + "..." + "</span>");

                    $("#semtencePageCode" + totalPages).css("display", "inline-block")

                } else if (pageNumber > 7 && pageNumber < totalPages - 6) {

                    for (let k = 1; k <= totalPages; k++) {
                        $("#semtencePageCode" + k).css("display", "none")
                    }

                    $("#semtencePageCode" + 1).css("display", "inline-block")

                    if ($("#semtencePageCode0").length > 0) {
                        $("#semtencePageCode0").remove()
                    }
                    $("#semtencePageCode1").after("<span id='semtencePageCode0' style='margin-left: 5px' class='pprPager' >" + "..." + "</span>");

                    for (let k = pageNumber - 5; k <= pageNumber + 5; k++) {
                        $("#semtencePageCode" + k).css("display", "inline-block")
                    }

                    if ($("#semtencePageCodeMax").length > 0) {
                        $("#semtencePageCodeMax").remove()
                    }
                    $("#semtencePageCode" + totalPages).before("<span id='semtencePageCodeMax' style='margin-left: 5px' class='pprPager' >" + "..." + "</span>");

                    $("#semtencePageCode" + totalPages).css("display", "inline-block")

                } else if (pageNumber > 7 && pageNumber >= totalPages - 6) {

                    for (let k = 1; k <= totalPages; k++) {
                        $("#semtencePageCode" + k).css("display", "none")
                    }
                    $("#semtencePageCode" + 1).css("display", "inline-block")
                    if ($("#semtencePageCode0").length > 0) {
                        $("#semtencePageCode0").remove()
                    }
                    $("#semtencePageCode1").after("<span id='semtencePageCode0' style='margin-left: 5px' class='pprPager' >" + "..." + "</span>");

                    if ($("#semtencePageCodeMax").length > 0) {
                        $("#semtencePageCodeMax").remove()
                    }
                    for (let k = totalPages - 10; k <= totalPages; k++) {
                        $("#semtencePageCode" + k).css("display", "inline-block")
                    }
                }
            }
        },

        initDefault: function () {
            this.editPage();
            this.uploadFile();
            this.playPageMp3();
            this.playSemtenceMp3();
            this.initPage();
            this.editSemtence();
            this.editBookInfo();

        },

        playSemtenceMp3: function () {
            var _this = this;
            var status = true;
            $("#listen").on("click", function () {
                var audio = $("#semFre audio")[0];
                if (/*audio.paused*/ status) {
                    audio.play();
                    _this.t = setInterval(function () {
                        if ($("#play_music").hasClass("fa-volume-down")) {
                            $("#play_music").removeClass("fa-volume-down").addClass("fa-volume-up");
                        } else {
                            $("#play_music").removeClass("fa-volume-up").addClass("fa-volume-down");
                        }
                    }, 500);
                    $("#listen").text("点击暂停");
                } else {
                    clearInterval(_this.t);
                    audio.pause();
                    $("#listen").text("点击播放");
                }
                if (status) {
                    status = false;
                } else {
                    status = true;
                }
            });
        },

        playPageMp3: function () {
            var _this = this;
            var status = true;
            $("#pageListen").on("click", function () {
                var audio = $("#pageFre  audio")[0];
                if (/*audio.paused*/ status) {
                    audio.play();
                    _this.tp = setInterval(function () {
                        if ($("#pl_music").hasClass("fa-volume-down")) {
                            $("#pl_music").removeClass("fa-volume-down").addClass("fa-volume-up");
                        } else {
                            $("#pl_music").removeClass("fa-volume-up").addClass("fa-volume-down");
                        }
                    }, 500);
                    $("#pageListen").text("点击暂停");
                } else {
                    clearInterval(_this.tp);
                    audio.pause();
                    $("#pageListen").text("点击播放");
                }
                if (status) {
                    status = false;
                } else {
                    status = true;
                }
            });
        },

        initPage: function () {
            var _this = this;
            if (!CommUtils.isEmpty(_this.etmBookInfo)) {
                $("#isExit").css("display", "none")
                $("#grade").text(_this.etmBookInfo.gradeName)
                $("#grade").attr("title", _this.etmBookInfo.gradeName);
                $("#bookName").text(_this.etmBookInfo.textBookName)
                $("#bookName").attr("title", _this.etmBookInfo.textBookName);
                $("#subject").text(_this.etmBookInfo.subjectName)
                $("#subject").attr("title", _this.etmBookInfo.subjectName);
                if (_this.etmBookInfo.pageList && _this.etmBookInfo.pageList.length != 0) {

                    var tem = _this.etmBookInfo.pageList.length;
                    _this.pageList = _this.etmBookInfo.pageList;

                    _this.pageList.forEach(function (pageInfo) {
                        pageInfo.semtenceInfoList.forEach(function (semtenceInfo) {
                            semtenceInfo.semtenceInfoCoordinateList.forEach(function (semtenceInfoCoordinate) {
                                semtenceInfoCoordinate.x = parseInt(semtenceInfoCoordinate.x)
                                semtenceInfoCoordinate.y = parseInt(semtenceInfoCoordinate.y)
                                semtenceInfoCoordinate.semtenceYtop = parseInt(semtenceInfoCoordinate.semtenceYtop)
                                semtenceInfoCoordinate.semtenceInfoDiv = parseInt(semtenceInfoCoordinate.semtenceInfoDiv)
                                semtenceInfoCoordinate.width = parseInt(semtenceInfoCoordinate.width)
                                semtenceInfoCoordinate.height = parseInt(semtenceInfoCoordinate.height)
                            })
                        })
                        pageInfo.status = "1";
                    })

                    for (i = 1; i <= tem; i++) {
                        $("#add").before("<div id='pageCode" + i + "' style='margin-left: 5px' class='pprPager'  >" + i + "</div>");
                        $("#pageCode" + i).click(function (e) {

                            $("#pageCode > div").css("borderColor", "#03A9F4");
                            if (_this.etmBookInfo.pageList && _this.etmBookInfo.pageList.length != 0) {
                                _this.etmBookInfo.pageList.forEach(function (page, index) {
                                    if (page.status == "1") {
                                        $("#pageCode" + (index + 1)).css("borderColor", "green");
                                    }
                                })
                            }
                            $(e.target).css("borderColor", "red");
                            if (_this.pageList[i - 1].semtenceInfoList) {
                                for (var k = 1; k <= _this.pageList[i - 1].semtenceInfoList.length; k++) {
                                    for (var coorIndex = 1; coorIndex <= _this.pageList[i - 1].semtenceInfoList[k - 1].semtenceInfoCoordinateList.length; coorIndex++) {
                                        $("div[title=" + _this.pageList[i - 1].semtenceInfoList[k - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceInfoDiv + "]").css("display", "none");
                                        $("div[title=" + _this.pageList[i - 1].semtenceInfoList[k - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceInfoDiv + "]").css('opacity', 0.4)
                                    }
                                }
                            }
                            i = $(e.target).text();
                            _this.showPages(i, _this.pageList.length)

                            $("#imgName").attr("value", "");
                            $("#frequencyName").attr("value", "");

                            _this.pageInfo = _this.pageList[i - 1];
                            if (_this.pageInfo.pageInfoMp3Name) {
                                $("#frequencyName").attr("value", _this.pageInfo.pageInfoMp3Name);
                            }
                            $("#pageFre audio").attr("src", "")
                            $("#img").attr("src", "")
                            if (_this.pageInfo.pageInfoImgRealUrl) {
                                $("#img").attr("src", _this.pageInfo.pageInfoImgRealUrl)
                                $("#imgName").attr("value", _this.pageInfo.pageInfoImgName);
                            } else {
                                $("#img").attr("src", "/custom/package/etm/img/etm/default.png")
                            }
                            if (_this.pageInfo.pageInfoMp3LoadUrl) {
                                $("#selectFrequency").css("display", "none");
                                $("#pageFre").css("display", "inline-block")
                                $("#pageFre audio").attr("src", _this.pageInfo.pageInfoMp3LoadUrl)
                                $("#pageFre > .fa-window-close-o").click(function () {

                                    $("#selectFrequency").css("display", "inline-block");
                                    $("#pageFre").css("display", "none");


                                    clearInterval(_this.tp);
                                    var audio = $("#pageFre  audio")[0];
                                    audio.pause();
                                    $("#pageListen").text("点击播放");

                                    _this.pageList[i - 1].pageInfoMp3LoadUrl = "";

                                    _this.pageList[i - 1].pageInfoMp3Name = "";
                                    $("#frequencyName").attr("value", _this.pageList[i - 1].pageInfoMp3Name);

                                })
                            } else {
                                $("#selectFrequency").css("display", "inline-block");
                                $("#pageFre").css("display", "none")
                            }

                            for (var k = 1; k <= _this.pageList[i - 1].semtenceInfoList.length; k++) {
                                for (var coorIndex = 1; coorIndex <= _this.pageList[i - 1].semtenceInfoList[k - 1].semtenceInfoCoordinateList.length; coorIndex++) {
                                    $("div[title=" + _this.pageList[i - 1].semtenceInfoList[k - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceInfoDiv + "]").css("top",
                                        _this.pageList[i - 1].semtenceInfoList[k - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceYtop - document.getElementById('coordiv1').scrollTop + 'px')
                                    $("div[title=" + _this.pageList[i - 1].semtenceInfoList[k - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceInfoDiv + "]").css("display", "inline-block");
                                }
                            }
                            var widthScale;
                            var heightScale;
                            var imgWidth
                            var imgHeight
                            document.getElementById("img").onload = function (e) {

                                imgWidth = $("#img").width()
                                imgHeight = $("#img").height()
                                //宽的比例
                                widthScale = _this.pageList[i - 1].pageInfoImgWidth / imgWidth;
                                //高的比例
                                heightScale = _this.pageList[i - 1].pageInfoImgHeight / imgHeight;
                                if (_this.pageList[i - 1].semtenceInfoList) {
                                    for (j = 1; j <= _this.pageList[i - 1].semtenceInfoList.length; j++) {
                                        for (var coorIndex = 1; coorIndex <= _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList.length; coorIndex++) {
                                            if (!$("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceInfoDiv + "]").text()) {
                                                var active_box = document.createElement("div");
                                                active_box.className = "box";
                                                active_box.style.zIndex = 300;
                                                _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceYtop = Math.ceil(_this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[coorIndex - 1].y / heightScale + 100)
                                                active_box.property = coorIndex
                                                active_box.title = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceInfoDiv;
                                                active_box.style.top = Math.ceil(_this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[coorIndex - 1].y / heightScale + 100) + 'px';
                                                active_box.style.left = Math.ceil(_this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[coorIndex - 1].x / widthScale) + 'px';
                                                active_box.style.width = Math.floor(_this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[coorIndex - 1].width / widthScale) + "px";
                                                active_box.style.height = Math.floor(_this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[coorIndex - 1].height / heightScale) + "px";
                                                active_box.innerText = _this.pageList[i - 1].semtenceInfoList[j - 1].sequence;
                                                active_box.style.opacity = 0.4
                                                if (j == 1) {
                                                    active_box.style.opacity = 1
                                                }
                                                document.getElementById("coordiv").appendChild(active_box);
                                                if (_this.pageList[i - 1].semtenceInfoList[j - 1]) {
                                                    var coordiv = document.getElementById('coordiv')
                                                    //记录鼠标偏移量
                                                    var _y;
                                                    var _x;

                                                    //用来记录偏移，看它是偏移还是伸缩
                                                    var up_x;
                                                    var up_y;

                                                    //定时器任务
                                                    var listenWidth;
                                                    var startX;
                                                    var startY;
                                                    $("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceInfoDiv + "]").on("mousedown", function (e) {
                                                        e.stopPropagation()
                                                        if (j == $(e.target).text()) {
                                                            var div = $("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]");
                                                            div.css("resize", "both")
                                                            div.css("overflow", "auto")
                                                            div.css("z-index", 333)
                                                            startX = div.offset().left;
                                                            startY = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop
                                                            //判断鼠标是否落在右下角边缘，如果是，那就进行伸缩，如果不是，就进行移动
                                                            if (e.offsetX < div.width() - 5 && e.offsetY < div.height() - 5) {
                                                                /* 获取需要拖动节点的坐标 */
                                                                var offset_x = $(this)[0].offsetLeft;//x坐标
                                                                var offset_y = $(this)[0].offsetTop;//y坐标

                                                                /* 获取当前鼠标的坐标 */
                                                                var mouse_x = event.pageX;
                                                                var mouse_y = event.pageY;

                                                                /* 绑定拖动事件 */
                                                                /* 由于拖动时，可能鼠标会移出元素，所以应该使用全局（document）元素 */
                                                                $(document).bind("mousemove", function (ev) {
                                                                    /* 计算鼠标移动了的位置 */
                                                                    _x = event.pageX - mouse_x;
                                                                    _y = event.pageY - mouse_y;

                                                                    if (_x <= 0 && Math.abs(_x) <= startX) {
                                                                        /* 设置移动后的元素坐标 */
                                                                        var now_x = (offset_x + _x);

                                                                    } else if (_x > 0 && _x <= $("#img").width() - div.outerWidth() - startX) {
                                                                        /* 设置移动后的元素坐标 */
                                                                        var now_x = (offset_x + _x);

                                                                    } else {
                                                                        var now_x;
                                                                        if (_x > 0) {
                                                                            now_x = $("#img").width() - div.outerWidth();
                                                                        } else {
                                                                            now_x = 0;
                                                                        }
                                                                    }

                                                                    if (_y <= 0 && Math.abs(_y) <= _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop - coordiv.offsetTop) {
                                                                        /* 设置移动后的元素坐标 */
                                                                        var now_y = (offset_y + _y);
                                                                    } else if (_y >= 0 && _y <= $("#img").height() - div.outerHeight() - (_this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop - coordiv.offsetTop)) {
                                                                        /* 设置移动后的元素坐标 */
                                                                        var now_y = (offset_y + _y);
                                                                    } else {
                                                                        var now_y;
                                                                        if (_y > 0) {
                                                                            now_y = $("#img").height() - div.outerHeight() + coordiv.offsetTop - document.getElementById("coordiv1").scrollTop
                                                                        } else {
                                                                            now_y = coordiv.offsetTop;
                                                                        }
                                                                    }
                                                                    $("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").css({
                                                                        top: now_y + "px",
                                                                        left: now_x + "px"
                                                                    });
                                                                });

                                                            }
                                                            var widthDiv = div.outerWidth();
                                                            var heightDiv = div.outerHeight();
                                                            //设置一个定时器，监控画的div的伸缩有没有超过边界
                                                            $(function () {
                                                                listenWidth = setInterval(showTime, 20);

                                                                function showTime() {
                                                                    if (widthDiv != div.outerWidth()) {
                                                                        if ($("#img").width() <= div.outerWidth() + div.offset().left) {
                                                                            div.css("resize", "")
                                                                            div.css("overflow", "")
                                                                            div.css("width", $("#img").width() - startX + "px")
                                                                            //清除定时器任务
                                                                            window.clearInterval(listenWidth);

                                                                            $("#x_y").attr("value", "")
                                                                            $("#x_y").attr("value", Math.floor((startX - coordiv.offsetLeft) * widthScale) + "," + Math.floor((startY - coordiv.offsetTop) * heightScale) + "," + Math.ceil(($("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").outerWidth()) * widthScale) + "," + Math.ceil(($("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").outerHeight()) * heightScale))
                                                                            if (_this.pageList[i - 1].semtenceInfoList[j - 1]) {
                                                                                var semtenceInfoCoordinate = {};
                                                                                semtenceInfoCoordinate.semtenceInfoDiv = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv
                                                                                semtenceInfoCoordinate.semtenceYtop = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop
                                                                                semtenceInfoCoordinate.x = Math.floor((startX - coordiv.offsetLeft) * widthScale);
                                                                                semtenceInfoCoordinate.y = Math.floor((startY - coordiv.offsetTop) * heightScale);
                                                                                semtenceInfoCoordinate.width = Math.ceil(($("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").outerWidth()) * widthScale);
                                                                                semtenceInfoCoordinate.height = Math.ceil(($("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").outerHeight()) * heightScale);
                                                                                _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1] = semtenceInfoCoordinate
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            })

                                                            $(e.target).on("mouseleave", function (e) {

                                                                if (j == $(e.target).text()) {
                                                                    var div = $("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]")
                                                                    div.css("resize", "")
                                                                    div.css("overflow", "")
                                                                    div.css("z-index", 300)
                                                                    startX = div.offset().left
                                                                    startY = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop
                                                                    if (_x != up_x || _y != up_y) {
                                                                        // _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceYtop = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceYtop + _y;
                                                                        //     startY = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceYtop;
                                                                        if (_this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop - coordiv.offsetTop + _y <= 0) {
                                                                            _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop = coordiv.offsetTop;
                                                                            startY = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop

                                                                        } else if (_this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop - coordiv.offsetTop + $("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").outerHeight() + _y >= $("#img").height()) {
                                                                            _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop = coordiv.offsetTop + $("#img").height() - $("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").outerHeight();
                                                                            startY = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop;
                                                                        } else {
                                                                            _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop + _y;
                                                                            startY = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop;
                                                                        }

                                                                        if (startX + _x <= 0) {
                                                                            startX = 0;
                                                                        } else if (startX + _x + div.outerWidth() >= $("#img").width()) {
                                                                            startX = $("#img").width() - div.outerWidth()
                                                                        } else {
                                                                            startX = startX + _x
                                                                        }
                                                                    }
                                                                    up_x = _x;
                                                                    up_y = _y;
                                                                    $(document).unbind("mousemove");
                                                                    $("#x_y").attr("value", "")
                                                                    $("#x_y").attr("value", Math.floor((startX - coordiv.offsetLeft) * widthScale) + "," + Math.floor((startY - coordiv.offsetTop) * heightScale) + "," + Math.ceil(($("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").outerWidth()) * widthScale) + "," + Math.ceil(($("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").outerHeight()) * heightScale))
                                                                    if (_this.pageList[i - 1].semtenceInfoList[j - 1]) {
                                                                        var semtenceInfoCoordinate = {};
                                                                        semtenceInfoCoordinate.semtenceInfoDiv = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv
                                                                        semtenceInfoCoordinate.semtenceYtop = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop
                                                                        semtenceInfoCoordinate.x = Math.floor((startX - coordiv.offsetLeft) * widthScale);
                                                                        semtenceInfoCoordinate.y = Math.floor((startY - coordiv.offsetTop) * heightScale);
                                                                        semtenceInfoCoordinate.width = Math.ceil(($("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").outerWidth()) * widthScale);
                                                                        semtenceInfoCoordinate.height = Math.ceil(($("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").outerHeight()) * heightScale);
                                                                        _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1] = semtenceInfoCoordinate
                                                                    }
                                                                    window.clearInterval(listenWidth);
                                                                    $(e.target).unbind("mouseleave");
                                                                }
                                                            })

                                                            $(e.target).on("mouseup", function (e) {

                                                                if (j == $(e.target).text()) {
                                                                    var div = $("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]")
                                                                    div.css("resize", "")
                                                                    div.css("overflow", "")
                                                                    div.css("z-index", 300)
                                                                    if (_x != up_x || _y != up_y) {
                                                                        // _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceYtop = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceYtop + _y;
                                                                        //     startY = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceYtop;
                                                                        if (_this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop - coordiv.offsetTop + _y <= 0) {
                                                                            _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop = coordiv.offsetTop;
                                                                            startY = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop

                                                                        } else if (_this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop - coordiv.offsetTop + $("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").outerHeight() + _y >= $("#img").height()) {
                                                                            _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop = coordiv.offsetTop + $("#img").height() - $("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").outerHeight();
                                                                            startY = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop;
                                                                        } else {
                                                                            _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop + _y;
                                                                            startY = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop;
                                                                        }

                                                                        if (startX + _x <= 0) {
                                                                            startX = 0;
                                                                        } else if (startX + _x + div.outerWidth() >= $("#img").width()) {
                                                                            startX = $("#img").width() - div.outerWidth()
                                                                        } else {
                                                                            startX = startX + _x
                                                                        }
                                                                    }
                                                                    up_x = _x;
                                                                    up_y = _y;
                                                                    $(document).unbind("mousemove");
                                                                    $("#x_y").attr("value", "")
                                                                    $("#x_y").attr("value", Math.floor((startX - coordiv.offsetLeft) * widthScale) + "," + Math.floor((startY - coordiv.offsetTop) * heightScale) + "," + Math.ceil(($("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").outerWidth()) * widthScale) + "," + Math.ceil(($("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").outerHeight()) * heightScale))
                                                                    if (_this.pageList[i - 1].semtenceInfoList[j - 1]) {
                                                                        var semtenceInfoCoordinate = {};
                                                                        semtenceInfoCoordinate.semtenceInfoDiv = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv
                                                                        semtenceInfoCoordinate.semtenceYtop = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop
                                                                        semtenceInfoCoordinate.x = Math.floor((startX - coordiv.offsetLeft) * widthScale);
                                                                        semtenceInfoCoordinate.y = Math.floor((startY - coordiv.offsetTop) * heightScale);
                                                                        semtenceInfoCoordinate.width = Math.ceil(($("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").outerWidth()) * widthScale);
                                                                        semtenceInfoCoordinate.height = Math.ceil(($("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").outerHeight()) * heightScale);
                                                                        _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1] = semtenceInfoCoordinate
                                                                    }
                                                                    $(e.target).unbind("mouseup");
                                                                }
                                                            })

                                                        }
                                                    })

                                                    $("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceInfoDiv + "]").on("dblclick", function (e) {

                                                        if (j == $(e.target).text()) {

                                                            $("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").remove();

                                                            _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList.splice(e.target.property - 1, 1)

                                                            for (var coorIndex = e.target.property; coorIndex <= _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList.length; coorIndex++) {

                                                                $("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceInfoDiv + "]").get(0).property = coorIndex;

                                                            }
                                                        }

                                                    })

                                                }
                                            }
                                        }
                                    }
                                }
                                _this.iniSemtence();
                            }
                        });
                    }
                    i = 1;

                    _this.showPages(i, _this.pageList.length)

                    $("#imgName").attr("value", "");
                    $("#frequencyName").attr("value", "");
                    _this.pageInfo = _this.pageList[i - 1];
                    if (_this.pageInfo.pageInfoMp3Name) {
                        $("#frequencyName").attr("value", _this.pageInfo.pageInfoMp3Name);
                    }
                    $("#pageFre audio").attr("src", "")
                    $("#img").attr("src", "")
                    if (_this.pageInfo.pageInfoImgRealUrl) {
                        $("#img").attr("src", _this.pageInfo.pageInfoImgRealUrl)
                        $("#imgName").attr("value", _this.pageInfo.pageInfoImgName);
                    } else {
                        $("#img").attr("src", "/custom/package/etm/img/etm/default.png")
                    }
                    if (_this.pageInfo.pageInfoMp3LoadUrl) {
                        $("#selectFrequency").css("display", "none");
                        $("#pageFre").css("display", "inline-block")
                        $("#pageFre audio").attr("src", _this.pageInfo.pageInfoMp3LoadUrl)
                        $("#pageFre > .fa-window-close-o").click(function () {

                            $("#selectFrequency").css("display", "inline-block");
                            $("#pageFre").css("display", "none");

                            clearInterval(_this.tp);
                            var audio = $("#pageFre  audio")[0];
                            audio.pause();
                            $("#pageListen").text("点击播放");

                            _this.pageList[i - 1].pageInfoMp3LoadUrl = "";

                            _this.pageList[i - 1].pageInfoMp3Name = "";
                            $("#frequencyName").attr("value", _this.pageList[i - 1].pageInfoMp3Name);

                        })
                    } else {
                        $("#selectFrequency").css("display", "inline-block");
                        $("#pageFre").css("display", "none")
                    }

                    if (_this.pageList[i - 1].semtenceInfoList) {
                        for (var k = 1; k <= _this.pageList[i - 1].semtenceInfoList.length; k++) {
                            for (var coorIndex = 1; coorIndex <= _this.pageList[i - 1].semtenceInfoList[k - 1].semtenceInfoCoordinateList.length; coorIndex++) {
                                $("div[title=" + _this.pageList[i - 1].semtenceInfoList[k - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceInfoDiv + "]").css("top",
                                    _this.pageList[i - 1].semtenceInfoList[k - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceYtop - document.getElementById('coordiv1').scrollTop + 'px')
                            }
                        }
                        if (_this.pageList[i - 1].pageInfoImgRealUrl) {
                            $("#img").attr("src", _this.pageList[i - 1].pageInfoImgRealUrl);
                            var widthScale;
                            var heightScale;
                            var imgWidth
                            var imgHeight
                            document.getElementById("img").onload = function () {
                                imgWidth = $("#img").width()
                                imgHeight = $("#img").height()
                                //宽的比例
                                widthScale = _this.pageList[i - 1].pageInfoImgWidth / imgWidth;
                                //高的比例
                                heightScale = _this.pageList[i - 1].pageInfoImgHeight / imgHeight;
                                if (_this.pageList[i - 1].semtenceInfoList) {
                                    for (j = 1; j <= _this.pageList[i - 1].semtenceInfoList.length; j++) {
                                        for (var coorIndex = 1; coorIndex <= _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList.length; coorIndex++) {
                                            if (!$("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceInfoDiv + "]").text()) {
                                                var active_box = document.createElement("div");
                                                active_box.className = "box";
                                                active_box.style.float = "left"
                                                active_box.style.zIndex = 300;
                                                active_box.property = coorIndex
                                                active_box.title = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceInfoDiv;
                                                _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceYtop = Math.ceil(_this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[coorIndex - 1].y / heightScale + 100)
                                                active_box.style.top = Math.ceil(_this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[coorIndex - 1].y / heightScale + 100) + 'px';
                                                active_box.style.left = Math.ceil(_this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[coorIndex - 1].x / widthScale) + 'px';
                                                active_box.style.width = Math.floor(_this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[coorIndex - 1].width / widthScale) + "px";
                                                active_box.style.height = Math.floor(_this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[coorIndex - 1].height / heightScale) + "px";
                                                active_box.innerText = _this.pageList[i - 1].semtenceInfoList[j - 1].sequence;
                                                active_box.style.opacity = 0.4;
                                                if (j == 1) {
                                                    active_box.style.opacity = 1
                                                }
                                                document.getElementById("coordiv").appendChild(active_box);
                                                if (_this.pageList[i - 1].semtenceInfoList[j - 1]) {
                                                    var coordiv = document.getElementById('coordiv')
                                                    //记录鼠标偏移量
                                                    var _y;
                                                    var _x;
                                                    //用来记录偏移，看它是偏移还是伸缩
                                                    var up_x;
                                                    var up_y;
                                                    //定时器任务
                                                    var listenWidth;
                                                    var startX;
                                                    var startY;
                                                    $("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceInfoDiv + "]").on("mousedown", function (e) {
                                                        e.stopPropagation()
                                                        if (j == $(e.target).text()) {
                                                            var div = $("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]");
                                                            div.css("resize", "both")
                                                            div.css("overflow", "auto")
                                                            div.css("z-index", 333)
                                                            startX = div.offset().left;
                                                            startY = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop

                                                            //判断鼠标是否落在右下角边缘，如果是，那就进行伸缩，如果不是，就进行移动
                                                            if (e.offsetX < div.width() - 5 && e.offsetY < div.height() - 5) {
                                                                /* 获取需要拖动节点的坐标 */
                                                                var offset_x = $(this)[0].offsetLeft;//x坐标
                                                                var offset_y = $(this)[0].offsetTop;//y坐标
                                                                /* 获取当前鼠标的坐标 */
                                                                var mouse_x = event.pageX;
                                                                var mouse_y = event.pageY;
                                                                /* 绑定拖动事件 */
                                                                /* 由于拖动时，可能鼠标会移出元素，所以应该使用全局（document）元素 */
                                                                $(document).bind("mousemove", function (ev) {
                                                                    /* 计算鼠标移动了的位置 */
                                                                    _x = event.pageX - mouse_x;
                                                                    _y = event.pageY - mouse_y;
                                                                    if (_x <= 0 && Math.abs(_x) <= startX) {
                                                                        /* 设置移动后的元素坐标 */
                                                                        var now_x = (offset_x + _x);

                                                                    } else if (_x > 0 && _x <= $("#img").width() - div.outerWidth() - startX) {
                                                                        /* 设置移动后的元素坐标 */
                                                                        var now_x = (offset_x + _x);

                                                                    } else {
                                                                        var now_x;
                                                                        if (_x > 0) {
                                                                            now_x = $("#img").width() - div.outerWidth();
                                                                        } else {
                                                                            now_x = 0;
                                                                        }
                                                                    }

                                                                    if (_y <= 0 && Math.abs(_y) <= _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop - coordiv.offsetTop) {
                                                                        /* 设置移动后的元素坐标 */
                                                                        var now_y = (offset_y + _y);
                                                                    } else if (_y >= 0 && _y <= $("#img").height() - div.outerHeight() - (_this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop - coordiv.offsetTop)) {
                                                                        /* 设置移动后的元素坐标 */
                                                                        var now_y = (offset_y + _y);
                                                                    } else {
                                                                        var now_y;
                                                                        if (_y > 0) {
                                                                            now_y = $("#img").height() - div.outerHeight() + coordiv.offsetTop - document.getElementById("coordiv1").scrollTop
                                                                        } else {
                                                                            now_y = coordiv.offsetTop;
                                                                        }
                                                                    }
                                                                    $("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").css({
                                                                        top: now_y + "px",
                                                                        left: now_x + "px"
                                                                    });
                                                                });
                                                            }
                                                            var widthDiv = div.outerWidth();
                                                            var heightDiv = div.outerHeight();
                                                            //设置一个定时器，监控画的div的伸缩有没有超过边界
                                                            $(function () {
                                                                listenWidth = setInterval(showTime, 20);

                                                                function showTime() {
                                                                    if (widthDiv != div.outerWidth()) {
                                                                        if ($("#img").width() <= div.outerWidth() + div.offset().left) {
                                                                            div.css("resize", "")
                                                                            div.css("overflow", "")
                                                                            div.css("width", $("#img").width() - startX + "px")
                                                                            //清除定时器任务
                                                                            window.clearInterval(listenWidth);
                                                                            $("#x_y").attr("value", "")
                                                                            $("#x_y").attr("value", Math.floor((startX - coordiv.offsetLeft) * widthScale) + "," + Math.floor((startY - coordiv.offsetTop) * heightScale) + "," + Math.ceil(($("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").outerWidth()) * widthScale) + "," + Math.ceil(($("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").outerHeight()) * heightScale))
                                                                            if (_this.pageList[i - 1].semtenceInfoList[j - 1]) {
                                                                                var semtenceInfoCoordinate = {}
                                                                                semtenceInfoCoordinate.semtenceInfoDiv = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv
                                                                                semtenceInfoCoordinate.semtenceYtop = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop
                                                                                semtenceInfoCoordinate.x = Math.floor((startX - coordiv.offsetLeft) * widthScale);
                                                                                semtenceInfoCoordinate.y = Math.floor((startY - coordiv.offsetTop) * heightScale);
                                                                                semtenceInfoCoordinate.width = Math.ceil(($("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").outerWidth()) * widthScale);
                                                                                semtenceInfoCoordinate.height = Math.ceil(($("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").outerHeight()) * heightScale);
                                                                                _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1] = semtenceInfoCoordinate
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            })
                                                            $(e.target).on("mouseLeave", function (e) {

                                                                if (j == $(e.target).text()) {
                                                                    var div = $("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]")
                                                                    div.css("resize", "")
                                                                    div.css("overflow", "")
                                                                    div.css("z-index", 300)
                                                                    startX = div.offset().left
                                                                    startY = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop
                                                                    if (_x != up_x || _y != up_y) {
                                                                        // _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceYtop = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceYtop + _y;
                                                                        //     startY = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceYtop;
                                                                        if (_this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop - coordiv.offsetTop + _y <= 0) {
                                                                            _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop = coordiv.offsetTop;
                                                                            startY = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop

                                                                        } else if (_this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop - coordiv.offsetTop + $("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").outerHeight() + _y >= $("#img").height()) {
                                                                            _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop = coordiv.offsetTop + $("#img").height() - $("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").outerHeight();
                                                                            startY = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop;
                                                                        } else {
                                                                            _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop + _y;
                                                                            startY = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop;
                                                                        }

                                                                        if (startX + _x <= 0) {
                                                                            startX = 0;
                                                                        } else if (startX + _x + div.outerWidth() >= $("#img").width()) {
                                                                            startX = $("#img").width() - div.outerWidth()
                                                                        } else {
                                                                            startX = startX + _x
                                                                        }
                                                                    }
                                                                    up_x = _x;
                                                                    up_y = _y;
                                                                    $(document).unbind("mousemove");
                                                                    $("#x_y").attr("value", "")
                                                                    $("#x_y").attr("value", Math.floor((startX - coordiv.offsetLeft) * widthScale) + "," + Math.floor((startY - coordiv.offsetTop) * heightScale) + "," + Math.ceil(($("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").outerWidth()) * widthScale) + "," + Math.ceil(($("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").outerHeight()) * heightScale))
                                                                    if (_this.pageList[i - 1].semtenceInfoList[j - 1]) {
                                                                        var semtenceInfoCoordinate = {};
                                                                        semtenceInfoCoordinate.semtenceInfoDiv = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv
                                                                        semtenceInfoCoordinate.semtenceYtop = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop
                                                                        semtenceInfoCoordinate.x = Math.floor((startX - coordiv.offsetLeft) * widthScale);
                                                                        semtenceInfoCoordinate.y = Math.floor((startY - coordiv.offsetTop) * heightScale);
                                                                        semtenceInfoCoordinate.width = Math.ceil(($("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").outerWidth()) * widthScale);
                                                                        semtenceInfoCoordinate.height = Math.ceil(($("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").outerHeight()) * heightScale);
                                                                        _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1] = semtenceInfoCoordinate
                                                                    }
                                                                    window.clearInterval(listenWidth)
                                                                    $(e.target).unbind("mouseLeave");
                                                                }
                                                            })
                                                            $(e.target).on("mouseup", function (e) {
                                                                e.stopPropagation()
                                                                if (j == $(e.target).text()) {
                                                                    var div = $("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]")
                                                                    div.css("resize", "")
                                                                    div.css("overflow", "")
                                                                    div.css("z-index", 300)
                                                                    if (_x != up_x || _y != up_y) {
                                                                        // _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceYtop = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceYtop + _y;
                                                                        //     startY = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceYtop;
                                                                        if (_this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop - coordiv.offsetTop + _y <= 0) {
                                                                            _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop = coordiv.offsetTop;
                                                                            startY = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop

                                                                        } else if (_this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop - coordiv.offsetTop + $("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").outerHeight() + _y >= $("#img").height()) {
                                                                            _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop = coordiv.offsetTop + $("#img").height() - $("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").outerHeight();
                                                                            startY = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop;
                                                                        } else {
                                                                            _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop + _y;
                                                                            startY = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop;
                                                                        }

                                                                        if (startX + _x <= 0) {
                                                                            startX = 0;
                                                                        } else if (startX + _x + div.outerWidth() >= $("#img").width()) {
                                                                            startX = $("#img").width() - div.outerWidth()
                                                                        } else {
                                                                            startX = startX + _x
                                                                        }
                                                                    }
                                                                    up_x = _x;
                                                                    up_y = _y;
                                                                    $(document).unbind("mousemove");
                                                                    $("#x_y").attr("value", "")
                                                                    $("#x_y").attr("value", Math.floor((startX - coordiv.offsetLeft) * widthScale) + "," + Math.floor((startY - coordiv.offsetTop) * heightScale) + "," + Math.ceil(($("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").outerWidth()) * widthScale) + "," + Math.ceil(($("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").outerHeight()) * heightScale))
                                                                    if (_this.pageList[i - 1].semtenceInfoList[j - 1]) {
                                                                        var semtenceInfoCoordinate = {}
                                                                        semtenceInfoCoordinate.semtenceInfoDiv = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv
                                                                        semtenceInfoCoordinate.semtenceYtop = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop
                                                                        semtenceInfoCoordinate.x = Math.floor((startX - coordiv.offsetLeft) * widthScale);
                                                                        semtenceInfoCoordinate.y = Math.floor((startY - coordiv.offsetTop) * heightScale);
                                                                        semtenceInfoCoordinate.width = Math.ceil(($("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").outerWidth()) * widthScale);
                                                                        semtenceInfoCoordinate.height = Math.ceil(($("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").outerHeight()) * heightScale);
                                                                        _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1] = semtenceInfoCoordinate
                                                                    }

                                                                    $(e.target).unbind("mouseup")

                                                                }
                                                            })
                                                        }

                                                    })

                                                    $("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceInfoDiv + "]").on("dblclick", function (e) {

                                                        if (j == $(e.target).text()) {

                                                            $("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").remove();

                                                            _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList.splice(e.target.property - 1, 1)

                                                            for (var coorIndex = e.target.property; coorIndex <= _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList.length; coorIndex++) {

                                                                $("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceInfoDiv + "]").get(0).property = coorIndex;

                                                            }
                                                        }

                                                    })

                                                }
                                            }
                                        }
                                    }
                                    j = 1;
                                }
                                _this.iniSemtence();

                            }

                        }
                    }
                    $("#pageCode > div").css("borderColor", "#03A9F4");
                    if (_this.etmBookInfo.pageList && _this.etmBookInfo.pageList.length != 0) {
                        _this.etmBookInfo.pageList.forEach(function (page, index) {
                            if (page.status == "1") {
                                $("#pageCode" + (index + 1)).css("borderColor", "green");
                            }
                        })
                    }
                    $("#pageCode" + i).css("borderColor", "red");
                }
            } else {
                $("#baseBookInfo").css("display", "none")
            }
        },

        iniSemtence: function () {

            var _this = this;
            $("#semtenceMp3Name").attr("value", "")
            $("#semtenceMp3").css("display", "inline-block");
            $("#semFre").css("display", "none");
            $("#x_y").attr("value", "");
            $("#semtenceText").val("")

            if (_this.pageList[i - 1]) {
                if (_this.pageList[i - 1].semtenceInfoList.length != 0) {
                    for (var k = 1; k <= _this.pageList[i - 1].semtenceInfoList.length; k++) {
                        for (var coorIndex = 1; coorIndex <= _this.pageList[i - 1].semtenceInfoList[k - 1].semtenceInfoCoordinateList.length; coorIndex++) {
                            $("div[title=" + _this.pageList[i - 1].semtenceInfoList[k - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceInfoDiv + "]").css("display", "inline-block");
                            $("div[title=" + _this.pageList[i - 1].semtenceInfoList[k - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceInfoDiv + "]").css("opacity", 0.4);
                        }
                    }
                }
            }

            var lengthtem = $("#semtencePageCode > div").length - 1;
            var arr = $("#semtencePageCode > div");
            for (var tem = 0; tem < lengthtem; tem++) {
                arr.eq(tem).remove();
            }
            if (_this.pageList[i - 1].semtenceInfoList.length == 0) {
                $("#deleteSemtence").css("display", "none")
            } else {
                $("#deleteSemtence").css("display", "inline-block")
            }


            for (var tem = 1; tem <= _this.pageList[i - 1].semtenceInfoList.length; tem++) {
                $("#addSemtence").before("<div id=" + 'semtencePageCode' + tem + " style=\"margin-left: 5px; border-color: rgb(3, 169, 244);\" class=\"pprPager\">" + tem + "</div>")
                $("#semtencePageCode" + tem).click(function (event) {
                    $("#semtencePageCode > div").css("borderColor", "#03A9F4");
                    $(event.target).css("borderColor", "red");

                    if (_this.pageList[i - 1].semtenceInfoList[j - 1]) {
                        for (var coorIndex = 1; coorIndex <= _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList.length; coorIndex++) {
                            $("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceInfoDiv + "]").css("opacity", 0.4);
                        }
                    }

                    for (var coorIndex = 1; coorIndex <= _this.pageList[i - 1].semtenceInfoList[0].semtenceInfoCoordinateList.length; coorIndex++) {
                        $("div[title=" + _this.pageList[i - 1].semtenceInfoList[0].semtenceInfoCoordinateList[coorIndex - 1].semtenceInfoDiv + "]").css("opacity", 0.4);
                    }

                    j = $(event.target).text();
                    _this.showSemtencePages(j, _this.pageList[i - 1].semtenceInfoList.length)

                    if (_this.pageList[i - 1].semtenceInfoList[j - 1]) {
                        for (var coorIndex = 1; coorIndex <= _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList.length; coorIndex++) {

                            $("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceInfoDiv + "]").css("opacity", 1);
                            if (_this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceYtop) {
                                $("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceInfoDiv + "]").css("top",
                                    _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceYtop - document.getElementById('coordiv1').scrollTop + 'px')
                            }
                        }
                    }

                    $("#semtenceMp3Name").attr("value", "")
                    $("#semtenceText").val("");
                    $("#x_y").attr("value", "")
                    $("#semFre audio").attr("src", "");
                    $("#semFre audio").attr("src", _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoMp3LoadUrl);

                    $("#semtenceMp3").css("display", "inline-block");
                    $("#semFre").css("display", "none");
                    $("#semtenceMp3Name").attr("value", _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoMp3Name)
                    if (_this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoText) {

                        $("#semtenceText").val(_this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoText)

                    }
                    if (_this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoMp3LoadUrl) {
                        $("#semtenceMp3").css("display", "none");
                        $("#semFre").css("display", "inline-block");

                        $("#semFre > .fa-window-close-o").click(function () {
                            $("#semtenceMp3").css("display", "inline-block");
                            $("#semFre").css("display", "none");
                            _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoMp3Name = "";
                            $("#semtenceMp3Name").attr("value", _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoMp3Name);
                            var audio = $("#semFre audio")[0];
                            clearInterval(_this.t);
                            audio.pause();
                            $("#listen").text("点击播放");
                            _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoMp3LoadUrl = ""
                        })
                    }

                    if (_this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList) {
                        for (var coorIndex = 1; coorIndex < _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList.length; coorIndex++) {
                            if (_this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceYtop) {
                                $("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceInfoDiv + "]").css("top",
                                    _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceYtop - document.getElementById('coordiv1').scrollTop + 'px')
                            }
                        }
                    }
                });
            }

            j = 1;
            if (_this.pageList[i - 1].semtenceInfoList[j - 1]) {
                $("#semtencePageCode" + j).css("borderColor", "red");
                $("#semtenceText").val("")
                if (_this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoMp3Name) {

                    $("#semtenceMp3Name").attr("value", _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoMp3Name)

                    $("#semFre audio").attr("src", _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoMp3LoadUrl);

                    $("#semtenceMp3").css("display", "none");
                    $("#semFre").css("display", "inline-block");

                    $("#semFre > .fa-window-close-o").click(function () {
                        $("#semtenceMp3").css("display", "inline-block");
                        $("#semFre").css("display", "none");
                        _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoMp3Name = "";
                        $("#semtenceMp3Name").attr("value", _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoMp3Name);
                        var audio = $("#semFre audio")[0];
                        clearInterval(_this.t);
                        audio.pause();
                        $("#listen").text("点击播放");
                        _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoMp3LoadUrl = ""
                    })
                }

                if (_this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList) {
                    for (var coorIndex = 1; coorIndex < _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList.length; coorIndex++) {
                        if (_this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceYtop) {
                            $("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceInfoDiv + "]").css("top",
                                _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceYtop - document.getElementById('coordiv1').scrollTop + 'px')
                        }

                    }
                }

                if (j != 0 && _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoText) {
                    $("#semtenceText").val(_this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoText)
                }
                for (var coorIndex = 1; coorIndex <= _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList.length; coorIndex++) {
                    if (j != 0 && _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceInfoDiv) {
                        $("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceInfoDiv + "]").css("opacity", 1);
                    }
                }
            }

            _this.showSemtencePages(j, _this.pageList[i - 1].semtenceInfoList.length)

        },

        editPage: function () {
            var _this = this;
            $("#add").click(function () {
                if (_this.pageList[i - 1]) {
                    for (var k = 1; k <= _this.pageList[i - 1].semtenceInfoList.length; k++) {
                        for (var coorIndex = 1; coorIndex <= _this.pageList[i - 1].semtenceInfoList[k - 1].semtenceInfoCoordinateList.length; coorIndex++) {
                            $("div[title=" + _this.pageList[i - 1].semtenceInfoList[k - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceInfoDiv + "]").css("display", "none")
                        }
                    }
                }
                i++;
                j = 0;
                _this.pageInfo = {};
                _this.pageList.splice(i - 1, 0, _this.pageInfo);
                _this.pageList[i - 1].semtenceInfoList = [];
                _this.pageList[i - 1].sequence = i;
                $("#imgName").attr("value", "");
                $("#frequencyName").attr("value", "");
                $("img").attr("src", "/custom/package/etm/img/etm/default.png");
                $("#semtenceMp3Name").attr("value", "")
                $("#x_y").attr("value", "")
                $("#semtenceText").val("")
                $("#deleteSemtence").css("display", "none")
                $("#selectFrequency").css("display", "inline-block");
                $("#pageFre").css("display", "none")
                $("#semtenceMp3").css("display", "inline-block");
                $("#semFre").css("display", "none");

                var lengthtem = $("#semtencePageCode > div").length - 1;
                var arr = $("#semtencePageCode > div");
                for (var tem = 0; tem < lengthtem; tem++) {
                    arr.eq(tem).remove();
                }

                if ($("#pageCode" + (i - 1)).text()) {
                    $("#pageCode" + (i - 1)).after("<div id='pageCode" + i + "' style='margin-left: 5px' class='pprPager' >" + i + "</div>");
                } else {
                    $("#add").before("<div id='pageCode" + i + "' style='margin-left: 5px' class='pprPager'  >" + i + "</div>");
                }
                $("#pageCode > div").css("borderColor", "#03A9F4");
                if (_this.pageList) {
                    _this.pageList.forEach(function (page, index) {
                        if (page.status == "1") {
                            $("#pageCode" + (index + 1)).css("borderColor", "green");
                        }
                    })
                }
                $("#pageCode" + i).css("borderColor", "red");
                $("#pageCode" + i).click(function (event) {
                    $("#pageCode > div").css("borderColor", "#03A9F4");
                    if (_this.pageList) {
                        _this.pageList.forEach(function (page, index) {
                            if (page.status == "1") {
                                $("#pageCode" + (index + 1)).css("borderColor", "green");
                            }
                        })
                    }
                    $(event.target).css("borderColor", "red");

                    if (_this.pageList[i - 1].semtenceInfoList) {
                        for (var p = 1; p <= _this.pageList[i - 1].semtenceInfoList.length; p++) {
                            for (var coorIndex = 1; coorIndex <= _this.pageList[i - 1].semtenceInfoList[p - 1].semtenceInfoCoordinateList.length; coorIndex++) {
                                $("div[title=" + _this.pageList[i - 1].semtenceInfoList[p - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceInfoDiv + "]").css("display", "none");
                                $("div[title=" + _this.pageList[i - 1].semtenceInfoList[p - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceInfoDiv + "]").css("opacity", 0.4);
                            }
                        }
                    }

                    i = $(event.target).text();

                    $("#imgName").attr("value", "");
                    $("#frequencyName").attr("value", "");
                    _this.pageInfo = _this.pageList[i - 1];
                    if (_this.pageInfo.pageInfoMp3Name) {
                        $("#frequencyName").attr("value", _this.pageInfo.pageInfoMp3Name);
                    }
                    $("#pageFre audio").attr("src", "")
                    $("#img").attr("src", "")
                    if (_this.pageInfo.pageInfoImgRealUrl) {
                        $("#img").attr("src", _this.pageInfo.pageInfoImgRealUrl)
                        $("#imgName").attr("value", _this.pageInfo.pageInfoImgName);
                    } else {
                        $("#img").attr("src", "/custom/package/etm/img/etm/default.png")
                    }
                    if (_this.pageInfo.pageInfoMp3LoadUrl) {
                        $("#selectFrequency").css("display", "none");
                        $("#pageFre").css("display", "inline-block")
                        $("#pageFre audio").attr("src", _this.pageInfo.pageInfoMp3LoadUrl)
                        $("#pageFre > .fa-window-close-o").click(function () {

                            $("#selectFrequency").css("display", "inline-block");
                            $("#pageFre").css("display", "none");


                            clearInterval(_this.tp);
                            var audio = $("#pageFre  audio")[0];
                            audio.pause();
                            $("#pageListen").text("点击播放");

                            _this.pageList[i - 1].pageInfoMp3LoadUrl = "";

                            _this.pageList[i - 1].pageInfoMp3Name = "";
                            $("#frequencyName").attr("value", _this.pageList[i - 1].pageInfoMp3Name);

                        })
                    } else {
                        $("#selectFrequency").css("display", "inline-block");
                        $("#pageFre").css("display", "none")
                    }

                    for (var k = 1; k <= _this.pageList[i - 1].semtenceInfoList.length; k++) {
                        for (var coorIndex = 1; coorIndex <= _this.pageList[i - 1].semtenceInfoList[k - 1].semtenceInfoCoordinateList.length; coorIndex++) {
                            $("div[title=" + _this.pageList[i - 1].semtenceInfoList[k - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceInfoDiv + "]").css("top",
                                _this.pageList[i - 1].semtenceInfoList[k - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceYtop - document.getElementById('coordiv1').scrollTop + 'px')
                            $("div[title=" + _this.pageList[i - 1].semtenceInfoList[k - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceInfoDiv + "]").css("display", "inline-block");
                        }
                    }
                    _this.showPages(i, _this.pageList.length);

                    _this.iniSemtence();
                });

                for (var tem = i; tem < $("#pageCode > div").length - 1; tem++) {
                    $("#pageCode > div").eq(tem).text(tem + 1)
                    $("#pageCode > div").eq(tem).attr("id", "pageCode" + (tem + 1))
                    _this.pageList[tem].sequence = tem + 1;
                }

                _this.showPages(i, _this.pageList.length);
            });
            $("#deletePage").click(function () {
                if (i == 0) {
                    layer.confirm(i18n['UI_70_02_01_022'], {
                        title: i18n['UI_70_02_01_019'],
                        btn: i18n['UI_70_02_01_021'],//按钮
                    });
                } else {
                    var firm = false;
                    layer.confirm(i18n['UI_70_02_01_023'], {
                        title: i18n['UI_70_02_01_019'],
                        btn: [i18n['UI_70_02_01_021'], i18n['UI_70_02_01_027']],//按钮
                    }, function (index) {
                        layer.close(index);
                        var load = layer.load(2,{
                            shade:false
                        })
                        _this.changeBookStatus();
                        var pageStatus = _this.pageList[i - 1].status;
                        var pageInfoId = _this.pageList[i - 1].pageInfoId;
                        if (pageStatus == "1") {
                            $.ajax({
                                type: 'POST',
                                url: '/manager/package/etm/deleteOnePage',
                                contentType: 'application/json',
                                data: pageInfoId,
                                //   dataType: "application/json",
                                async: false,
                                success: function (result) {
                                    if (result.status == 1) {
                                        firm = result.data;
                                    }
                                }
                            });
                        } else {
                            firm = true;
                        }
                        if (firm) {
                            for (var k = 0; k <= _this.pageList[i - 1].semtenceInfoList.length - 1; k++) {
                                for (var coorIndex = 1; coorIndex <= _this.pageList[i - 1].semtenceInfoList[k].semtenceInfoCoordinateList.length; coorIndex++) {
                                    $("div[title=" + _this.pageList[i - 1].semtenceInfoList[k].semtenceInfoCoordinateList[coorIndex - 1].semtenceInfoDiv + "]").remove()
                                }
                            }
                            _this.pageList.splice(i - 1, 1)
                            $("#pageCode" + i).remove();
                            for (var q = i; q < $("#pageCode > div").length; q++) {
                                $("#pageCode > div").eq(q - 1).text(q)
                                $("#pageCode > div").eq(q - 1).attr("id", "pageCode" + q)
                                _this.pageList[q - 1].sequence = q;
                            }

                            if (i == 1) {

                                $("#pageCode > div").css("borderColor", "#03A9F4");
                                if (_this.pageList) {
                                    _this.pageList.forEach(function (page, index) {
                                        if (page.status == "1") {
                                            $("#pageCode" + (index + 1)).css("borderColor", "green");
                                        }
                                    })
                                }
                                $("#pageCode" + i).css("borderColor", "red");
                                if (_this.pageList[i - 1]) {
                                    $("#imgName").attr("value", "");
                                    $("#frequencyName").attr("value", "");
                                    _this.pageInfo = _this.pageList[i - 1];
                                    if (_this.pageInfo.pageInfoMp3Name) {
                                        $("#frequencyName").attr("value", _this.pageInfo.pageInfoMp3Name);
                                    }
                                    $("#pageFre audio").attr("src", "")
                                    $("#img").attr("src", "")
                                    if (_this.pageInfo.pageInfoImgRealUrl) {
                                        $("#img").attr("src", _this.pageInfo.pageInfoImgRealUrl)
                                        $("#imgName").attr("value", _this.pageInfo.pageInfoImgName);
                                    } else {
                                        $("#img").attr("src", "/custom/package/etm/img/etm/default.png")
                                    }
                                    if (_this.pageInfo.pageInfoMp3LoadUrl) {
                                        $("#selectFrequency").css("display", "none");
                                        $("#pageFre").css("display", "inline-block")
                                        $("#pageFre audio").attr("src", _this.pageInfo.pageInfoMp3LoadUrl)
                                        $("#pageFre > .fa-window-close-o").click(function () {

                                            $("#selectFrequency").css("display", "inline-block");
                                            $("#pageFre").css("display", "none");


                                            clearInterval(_this.tp);
                                            var audio = $("#pageFre  audio")[0];
                                            audio.pause();
                                            $("#pageListen").text("点击播放");

                                            _this.pageList[i - 1].pageInfoMp3LoadUrl = "";

                                            _this.pageList[i - 1].pageInfoMp3Name = "";
                                            $("#frequencyName").attr("value", _this.pageList[i - 1].pageInfoMp3Name);

                                        })
                                    } else {
                                        $("#selectFrequency").css("display", "inline-block");
                                        $("#pageFre").css("display", "none")
                                    }

                                    _this.iniSemtence();
                                } else {
                                    $("#imgName").attr("value", "");
                                    $("#frequencyName").attr("value", "");
                                    $("#semtenceMp3Name").attr("value", "")
                                    $("#semtenceText").val("");
                                }
                            } else {
                                i--;
                                $("#pageCode > div").css("borderColor", "#03A9F4");
                                if (_this.pageList) {
                                    _this.pageList.forEach(function (page, index) {
                                        if (page.status == "1") {
                                            $("#pageCode" + (index + 1)).css("borderColor", "green");
                                        }
                                    })
                                }
                                $("#pageCode" + i).css("borderColor", "red");
                                $("#imgName").attr("value", "");
                                $("#frequencyName").attr("value", "");
                                $("#semtenceMp3Name").attr("value", "")
                                $("#semtenceText").val("");

                                $("#imgName").attr("value", "");
                                $("#frequencyName").attr("value", "");
                                _this.pageInfo = _this.pageList[i - 1];
                                if (_this.pageInfo.pageInfoMp3Name) {
                                    $("#frequencyName").attr("value", _this.pageInfo.pageInfoMp3Name);
                                }
                                $("#pageFre audio").attr("src", "")
                                $("#img").attr("src", "")
                                if (_this.pageInfo.pageInfoImgRealUrl) {
                                    $("#img").attr("src", _this.pageInfo.pageInfoImgRealUrl)
                                    $("#imgName").attr("value", _this.pageInfo.pageInfoImgName);
                                } else {
                                    $("#img").attr("src", "/custom/package/etm/img/etm/default.png")
                                }
                                if (_this.pageInfo.pageInfoMp3LoadUrl) {
                                    $("#selectFrequency").css("display", "none");
                                    $("#pageFre").css("display", "inline-block")
                                    $("#pageFre audio").attr("src", _this.pageInfo.pageInfoMp3LoadUrl)
                                    $("#pageFre > .fa-window-close-o").click(function () {

                                        $("#selectFrequency").css("display", "inline-block");
                                        $("#pageFre").css("display", "none");


                                        clearInterval(_this.tp);
                                        var audio = $("#pageFre  audio")[0];
                                        audio.pause();
                                        $("#pageListen").text("点击播放");

                                        _this.pageList[i - 1].pageInfoMp3LoadUrl = "";

                                        _this.pageList[i - 1].pageInfoMp3Name = "";
                                        $("#frequencyName").attr("value", _this.pageList[i - 1].pageInfoMp3Name);

                                    })
                                } else {
                                    $("#selectFrequency").css("display", "inline-block");
                                    $("#pageFre").css("display", "none")
                                }
                                _this.iniSemtence();
                            }

                            if ($("#pageCode > div").length == 1) {
                                $("#img").attr("src", "")
                                $("#img").attr("src", "/custom/package/etm/img/etm/default.png")
                                $("#deleteSemtence").css("display", "none")
                                $("#pageFre").css("display", "none")
                                $("#selectFrequency").css("display", "inline-block")
                                $("#semFre").css("display", "none")
                                $("#semtenceMp3").css("display", "inline-block")
                                $("#x_y").attr("value", "")
                                $("#semtenceText").attr("value", "")

                                var tem = $("#semtencePageCode >div").length;
                                for (var k = tem - 2; k >= 0; k--) {
                                    $("#semtencePageCode >div").eq(k).remove();
                                }
                                i--;
                            }
                            layer.close(load);
                            layer.msg(i18n['UI_70_02_01_024'], {time: 1000})
                            _this.showPages(i, _this.pageList.length);
                        } else {
                            layer.close(load);
                            layer.msg(i18n['UI_70_02_01_034'], {time: 1000})
                        }
                    });
                }
            })
            $("#nextPage").click(function () {
                if (i == $("#pageCode > div").length - 1 || i == 0) {
                    layer.msg(i18n['UI_70_02_01_025'], {time: 1000})
                } else {
                    if (_this.pageList[i - 1]) {
                        if (_this.pageList[i - 1].semtenceInfoList) {
                            for (var p = 1; p <= _this.pageList[i - 1].semtenceInfoList.length; p++) {
                                for (var coorIndex = 1; coorIndex <= _this.pageList[i - 1].semtenceInfoList[p - 1].semtenceInfoCoordinateList.length; coorIndex++) {
                                    $("div[title=" + _this.pageList[i - 1].semtenceInfoList[p - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceInfoDiv + "]").css("display", "none");
                                    $("div[title=" + _this.pageList[i - 1].semtenceInfoList[p - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceInfoDiv + "]").css("opacity", 0.4);
                                }
                            }
                        }
                    }

                    i++;
                    $("#imgName").attr("value", "");
                    $("#frequencyName").attr("value", "");
                    _this.pageInfo = _this.pageList[i - 1];
                    if (_this.pageInfo.pageInfoMp3Name) {
                        $("#frequencyName").attr("value", _this.pageInfo.pageInfoMp3Name);
                    }
                    $("#pageFre audio").attr("src", "")
                    $("#img").attr("src", "")
                    if (_this.pageInfo.pageInfoImgRealUrl) {
                        $("#img").attr("src", _this.pageInfo.pageInfoImgRealUrl)
                        $("#imgName").attr("value", _this.pageInfo.pageInfoImgName);
                    } else {
                        $("#img").attr("src", "/custom/package/etm/img/etm/default.png")
                    }
                    if (_this.pageInfo.pageInfoMp3LoadUrl) {
                        $("#selectFrequency").css("display", "none");
                        $("#pageFre").css("display", "inline-block")
                        $("#pageFre audio").attr("src", _this.pageInfo.pageInfoMp3LoadUrl)
                        $("#pageFre > .fa-window-close-o").click(function () {

                            $("#selectFrequency").css("display", "inline-block");
                            $("#pageFre").css("display", "none");


                            clearInterval(_this.tp);
                            var audio = $("#pageFre  audio")[0];
                            audio.pause();
                            $("#pageListen").text("点击播放");

                            _this.pageList[i - 1].pageInfoMp3LoadUrl = "";

                            _this.pageList[i - 1].pageInfoMp3Name = "";
                            $("#frequencyName").attr("value", _this.pageList[i - 1].pageInfoMp3Name);

                        })
                    } else {
                        $("#selectFrequency").css("display", "inline-block");
                        $("#pageFre").css("display", "none")
                    }
                    _this.iniSemtence();
                    $("#pageCode > div").css("borderColor", "#03A9F4");
                    if (_this.pageList) {
                        _this.pageList.forEach(function (page, index) {
                            if (page.status == "1") {
                                $("#pageCode" + (index + 1)).css("borderColor", "green");
                            }
                        })
                    }
                    $("#pageCode" + i).css("borderColor", "red");
                }

                _this.showPages(i, _this.pageList.length);
            })
            $("#prePage").click(function () {
                if (i == 1 || i == 0) {
                    layer.msg(i18n['UI_70_02_01_026'], {time: 1000})
                } else {
                    if (_this.pageList[i - 1]) {
                        if (_this.pageList[i - 1].semtenceInfoList) {
                            for (var p = 1; p <= _this.pageList[i - 1].semtenceInfoList.length; p++) {
                                for (var coorIndex = 1; coorIndex <= _this.pageList[i - 1].semtenceInfoList[p - 1].semtenceInfoCoordinateList.length; coorIndex++) {
                                    $("div[title=" + _this.pageList[i - 1].semtenceInfoList[p - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceInfoDiv + "]").css("display", "none");
                                    $("div[title=" + _this.pageList[i - 1].semtenceInfoList[p - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceInfoDiv + "]").css("opacity", 0.4);
                                }
                            }
                        }
                    }
                    i--;
                    $("#imgName").attr("value", "");
                    $("#frequencyName").attr("value", "");
                    _this.pageInfo = _this.pageList[i - 1];
                    if (_this.pageInfo.pageInfoMp3Name) {
                        $("#frequencyName").attr("value", _this.pageInfo.pageInfoMp3Name);
                    }
                    $("#pageFre audio").attr("src", "")
                    $("#img").attr("src", "")
                    if (_this.pageInfo.pageInfoImgRealUrl) {
                        $("#img").attr("src", _this.pageInfo.pageInfoImgRealUrl)
                        $("#imgName").attr("value", _this.pageInfo.pageInfoImgName);
                    } else {
                        $("#img").attr("src", "/custom/package/etm/img/etm/default.png")
                    }
                    if (_this.pageInfo.pageInfoMp3LoadUrl) {
                        $("#selectFrequency").css("display", "none");
                        $("#pageFre").css("display", "inline-block")
                        $("#pageFre audio").attr("src", _this.pageInfo.pageInfoMp3LoadUrl)
                        $("#pageFre > .fa-window-close-o").click(function () {

                            $("#selectFrequency").css("display", "inline-block");
                            $("#pageFre").css("display", "none");


                            clearInterval(_this.tp);
                            var audio = $("#pageFre  audio")[0];
                            audio.pause();
                            $("#pageListen").text("点击播放");

                            _this.pageList[i - 1].pageInfoMp3LoadUrl = "";

                            _this.pageList[i - 1].pageInfoMp3Name = "";
                            $("#frequencyName").attr("value", _this.pageList[i - 1].pageInfoMp3Name);

                        })
                    } else {
                        $("#selectFrequency").css("display", "inline-block");
                        $("#pageFre").css("display", "none")
                    }
                    _this.iniSemtence();
                    $("#pageCode > div").css("borderColor", "#03A9F4");
                    if (_this.pageList) {
                        _this.pageList.forEach(function (page, index) {
                            if (page.status == "1") {
                                $("#pageCode" + (index + 1)).css("borderColor", "green");
                            }
                        })
                    }
                    $("#pageCode" + i).css("borderColor", "red");
                }

                _this.showPages(i, _this.pageList.length);
            })
            $("#savePage").click(function () {
                if (i == 0) {
                    layer.confirm(i18n['UI_70_02_01_022'], {
                        title: i18n['UI_70_02_01_019'],
                        btn: i18n['UI_70_02_01_021'] //按钮
                    });
                } else {
                    _this.changeBookStatus();
                    if (CommUtils.isNotEmpty(_this.pageList[i - 1].pageInfoImgId)) {
                        var etmBookInfo = {
                            "textBookName": "",
                            "isbn": "",
                            "stageCode": "",
                            "stageName": "",
                            "gradeCode": "",
                            "gradeName": "",
                            "subjectCode": "",
                            "subjectName": "",
                            "textBookVersionCode": "",
                            "textBookVersionName": "",
                            "termCode": "",
                            "termName": "",
                            "coverId": "",
                            "coverUrl": "",
                            "coverImgName": "",
                            "userId": "",
                        };
                        etmBookInfo = _this.etmBookInfo;
                        etmBookInfo.page = _this.pageList[i - 1];
                        if (etmBookInfo.textBookName && etmBookInfo.subjectName && etmBookInfo.gradeName && etmBookInfo.termName && etmBookInfo.coverUrl && etmBookInfo.textBookName && etmBookInfo.isbn) {
                            var load = layer.load(2,{
                                shade:false
                            })
                            etmBookInfo.userId = _this.userId;
                            $.ajax({
                                type: 'POST',
                                url: '/manager/package/etm/editOnePage',
                                contentType: 'application/json',
                                data: JSON.stringify(etmBookInfo),
                                //   dataType: "application/json",
                                async: false,
                                success: function (result) {
                                    if (result.status == 1) {
                                        var pageList = result.data.pageList;
                                        if (CommUtils.isEmpty(_this.etmBookInfo.etmBookId)) {
                                            _this.etmBookInfo.etmBookId = result.data.etmBookId;
                                        }
                                        _this.pageList[i - 1].pageInfoId = pageList[0].pageInfoId;
                                        var semtenceInfoList = pageList[0].semtenceInfoList;
                                        semtenceInfoList.forEach(function (semtenceInfo, index1) {
                                            _this.pageList[i - 1].semtenceInfoList[index1].semtenceId = semtenceInfo.semtenceId;
                                            var semtenceInfoCoordinateList = semtenceInfo.semtenceInfoCoordinateList;
                                            semtenceInfoCoordinateList.forEach(function (region, index2) {
                                                _this.pageList[i - 1].semtenceInfoList[index1].semtenceInfoCoordinateList[index2].regionId = region.regionId;
                                            })
                                        })

                                        _this.pageList[i - 1].status = "1";
                                        layer.close(load);
                                        layer.msg("保存成功", {time: 2000})
                                    }
                                }
                            });
                        } else {
                            layer.confirm(i18n['UI_70_02_01_020'], {
                                title: i18n['UI_70_02_01_019'],
                                btn: i18n['UI_70_02_01_021'] //按钮
                            });
                        }
                    } else {
                        layer.confirm(i18n['UI_70_02_01_032'], {
                            title: i18n['UI_70_02_01_019'],
                            btn: i18n['UI_70_02_01_021'] //按钮
                        });
                    }

                }

            })
            $("#saveETM").click(function () {
                if (i == 0) {
                    layer.confirm(i18n['UI_70_02_01_022'], {
                        title: i18n['UI_70_02_01_019'],
                        btn: i18n['UI_70_02_01_021'] //按钮
                    });
                } else {
                    var isSave = true;
                    _this.pageList.forEach(function (page) {
                        var status = page.status;
                        if (status != "1") {
                            isSave = false;
                            return;
                        }
                    })
                    if (isSave) {
                        _this.changeBookStatus();
                        _this.etmBookInfo.pageList = _this.pageList;
                        if (_this.etmBookInfo.textBookName && _this.etmBookInfo.subjectName && _this.etmBookInfo.gradeName && _this.etmBookInfo.termName && _this.etmBookInfo.coverUrl && _this.etmBookInfo.textBookName && _this.etmBookInfo.isbn) {
                            var load = layer.load(2,{
                                shade:false
                            })
                            _this.etmBookInfo.userId = _this.userId;
                            $.ajax({
                                type: 'POST',
                                url: '/manager/package/etm/addEtmBookInfo',
                                contentType: 'application/json',
                                data: JSON.stringify(_this.etmBookInfo),
                                //   dataType: "application/json",
                                async: false,
                                success: function (result) {
                                    if (result.status == 1) {
                                        layer.close(load);
                                        layer.msg("保存成功", {time: 2000})
                                        window.history.back(-1);
                                    }
                                }
                            });
                        } else {
                            layer.confirm(i18n['UI_70_02_01_020'], {
                                title: i18n['UI_70_02_01_019'],
                                btn: i18n['UI_70_02_01_021'] //按钮
                            });
                        }
                    } else {
                        layer.confirm(i18n['UI_70_02_01_033'], {
                            title: i18n['UI_70_02_01_019'],
                            btn: i18n['UI_70_02_01_021'] //按钮
                        });
                    }
                }
            })
            var t = 0;
            $("#img").click(function (_e) {

                var e = _e;
                // diffX, diffY 为鼠标初始坐标与 box 左上角坐标之差，用于拖动
                var startX, startY, diffX, diffY, tem;
                // 是否拖动，初始为 false
                var dragging = false;
                var coordiv = document.getElementById('coordiv');
                // if (j > 0 && _this.pageList[i - 1].pageInfoImgRealUrl) {
                if (t == 0) {
                    $("#coordiv").on("mousedown", function (e) {
                        startX = e.pageX;
                        startY = e.pageY;

                        if (j > 0 && _this.pageList[i - 1].pageInfoImgRealUrl && _this.pageList[i - 1].semtenceInfoList[j - 1]) {
                            // 如果鼠标在 box 上被按下,坐标判定防止在box之外
                            if (startY <= coordiv.offsetTop + coordiv.offsetHeight && startY >= coordiv.offsetTop && startX >= coordiv.offsetLeft && startX <= coordiv.offsetLeft + coordiv.offsetWidth) {

                                var tem;
                                if (_this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList) {
                                    tem = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList.length + 1;
                                } else {
                                    tem = 1;
                                }
                                // 在页面创建 box
                                ramdomTem = new Date().valueOf();
                                _this.semtenceInfoCoordinate = {};
                                _this.semtenceInfoCoordinate.semtenceInfoDiv = ramdomTem;
                                var active_box = document.createElement("div");
                                active_box.id = "active_box";
                                active_box.className = "box";
                                active_box.style.float = "left"
                                active_box.title = _this.semtenceInfoCoordinate.semtenceInfoDiv;
                                active_box.property = tem;
                                active_box.style.zIndex = 300;
                                active_box.style.top = startY + 'px';
                                active_box.style.left = startX + 'px';
                                active_box.innerText = j;
                                active_box.style.opacity = 1
                                document.getElementById("coordiv").appendChild(active_box);
                                active_box = null;
                                _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[tem - 1] = _this.semtenceInfoCoordinate

                            }

                            $("#coordiv").on("mousemove", function (e) {
                                if (e.pageY <= coordiv.offsetTop + coordiv.offsetHeight && e.pageY >= coordiv.offsetTop && e.pageX >= coordiv.offsetLeft && e.pageX <= coordiv.offsetLeft + coordiv.offsetWidth) {
                                    // 更新 box 尺寸
                                    if (document.getElementById("active_box") !== null)//如果document中有active_box,就改变box大小
                                    {
                                        var ab = document.getElementById("active_box");
                                        ab.style.left = Math.min(e.pageX, startX) + "px"
                                        ab.style.top = Math.min(e.pageY, startY) + "px"
                                        ab.style.width = Math.abs(e.pageX - startX) + 'px';
                                        ab.style.height = Math.abs(e.pageY - startY) + 'px';
                                    }
                                }
                            })

                            $(document).on("mouseup", function (e) {
                                e.stopPropagation()
                                startX = Math.min(e.pageX, startX);
                                startY = Math.min(e.pageY, startY)

                                // 禁止拖动
                                dragging = false;
                                _this.semtenceInfoCoordinate.semtenceYtop = startY + document.getElementById('coordiv1').scrollTop

                                if (document.getElementById("active_box") !== null) {
                                    var ab = document.getElementById("active_box");
                                    ab.removeAttribute("id");
                                    //计算原图和在页面进行图片的一个比例，将对应的框，在原图对应的坐标进行写入
                                    var img = new Image();
                                    img.src = _this.pageList[i - 1].pageInfoImgRealUrl;
                                    //宽的比例
                                    var widthScale;
                                    //高的比例
                                    var heightScale;
                                    img.onload = function () {
                                        widthScale = img.width / $("#img").width();
                                        heightScale = img.height / $("#img").height();

                                        $("#x_y").attr("value", "")
                                        $("#x_y").attr("value", Math.floor((startX - coordiv.offsetLeft) * widthScale) + "," + Math.floor((startY - coordiv.offsetTop + document.getElementById('coordiv1').scrollTop) * heightScale) + "," + Math.ceil(($("div[title=" + _this.semtenceInfoCoordinate.semtenceInfoDiv + "]").outerWidth()) * widthScale) + "," + Math.ceil(($("div[title=" + _this.semtenceInfoCoordinate.semtenceInfoDiv + "]").outerHeight()) * heightScale))

                                        if (_this.pageList[i - 1].semtenceInfoList[j - 1]) {
                                            _this.semtenceInfoCoordinate.x = Math.floor((startX - coordiv.offsetLeft) * widthScale);
                                            _this.semtenceInfoCoordinate.y = Math.floor((startY - coordiv.offsetTop + document.getElementById('coordiv1').scrollTop) * heightScale);
                                            _this.semtenceInfoCoordinate.width = Math.ceil(($("div[title=" + _this.semtenceInfoCoordinate.semtenceInfoDiv + "]").outerWidth()) * widthScale);
                                            _this.semtenceInfoCoordinate.height = Math.ceil(($("div[title=" + _this.semtenceInfoCoordinate.semtenceInfoDiv + "]").outerHeight()) * heightScale);
                                            _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[tem - 1] = _this.semtenceInfoCoordinate
                                        }
                                    }
                                }
                                //记录鼠标偏移量
                                var _y;
                                var _x;

                                //用来记录偏移，看它是偏移还是伸缩
                                var up_x;
                                var up_y;
                                //定时器任务
                                var listenWidth;
                                $("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[tem - 1].semtenceInfoDiv + "]").on("mousedown", function (e) {
                                    e.stopPropagation()
                                    if (j == $(e.target).text()) {
                                        var div = $("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]");
                                        div.css("resize", "both")
                                        div.css("overflow", "auto")
                                        div.css("z-index", 333)
                                        startX = div.offset().left;
                                        startY = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop;
                                        //判断鼠标是否落在右下角边缘，如果是，那就进行伸缩，如果不是，就进行移动
                                        if (e.offsetX < div.width() - 5 && e.offsetY < div.height() - 5) {
                                            /* 获取需要拖动节点的坐标 */
                                            var offset_x = $(this)[0].offsetLeft;//x坐标
                                            var offset_y = $(this)[0].offsetTop;//y坐标
                                            /* 获取当前鼠标的坐标 */
                                            var mouse_x = event.pageX;
                                            var mouse_y = event.pageY;
                                            /* 绑定拖动事件 */
                                            /* 由于拖动时，可能鼠标会移出元素，所以应该使用全局（document）元素 */
                                            $(document).bind("mousemove", function (ev) {
                                                /* 计算鼠标移动了的位置 */
                                                _x = event.pageX - mouse_x;
                                                _y = event.pageY - mouse_y;
                                                if (_x <= 0 && Math.abs(_x) <= startX) {
                                                    /* 设置移动后的元素坐标 */
                                                    var now_x = (offset_x + _x);

                                                } else if (_x > 0 && _x <= $("#img").width() - div.outerWidth() - startX) {
                                                    /* 设置移动后的元素坐标 */
                                                    var now_x = (offset_x + _x);

                                                } else {
                                                    var now_x;
                                                    if (_x > 0) {
                                                        now_x = $("#img").width() - div.outerWidth();
                                                    } else {
                                                        now_x = 0;
                                                    }
                                                }

                                                if (_y <= 0 && Math.abs(_y) <= _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop - coordiv.offsetTop) {
                                                    /* 设置移动后的元素坐标 */
                                                    var now_y = (offset_y + _y);
                                                } else if (_y >= 0 && _y <= $("#img").height() - div.outerHeight() - (_this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop - coordiv.offsetTop)) {
                                                    /* 设置移动后的元素坐标 */
                                                    var now_y = (offset_y + _y);
                                                } else {
                                                    var now_y;
                                                    if (_y > 0) {
                                                        now_y = $("#img").height() - div.outerHeight() + coordiv.offsetTop - document.getElementById("coordiv1").scrollTop
                                                    } else {
                                                        now_y = coordiv.offsetTop;
                                                    }
                                                }
                                                $("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").css({
                                                    top: now_y + "px",
                                                    left: now_x + "px"
                                                });
                                            });
                                        }
                                        var widthDiv = div.outerWidth();
                                        var heightDiv = div.outerHeight();
                                        //设置一个定时器，监控画的div的伸缩有没有超过边界
                                        $(function () {
                                            listenWidth = setInterval(showTime, 20);

                                            function showTime() {
                                                if (widthDiv != div.outerWidth()) {
                                                    if ($("#img").width() <= div.outerWidth() + div.offset().left) {
                                                        div.css("resize", "")
                                                        div.css("overflow", "")
                                                        div.css("width", $("#img").width() - startX + "px")
                                                        //清除定时器任务
                                                        window.clearInterval(listenWidth);
                                                        // div.css("resize", "both")
                                                        // div.css("overflow", "auto")

                                                        $("#x_y").attr("value", "")
                                                        $("#x_y").attr("value", Math.floor((startX - coordiv.offsetLeft) * widthScale) + "," + Math.floor((startY - coordiv.offsetTop) * heightScale) + "," + Math.ceil(($("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").outerWidth()) * widthScale) + "," + Math.ceil(($("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").outerHeight()) * heightScale))
                                                        if (_this.pageList[i - 1].semtenceInfoList[j - 1]) {
                                                            var semtenceInfoCoordinate = {}
                                                            semtenceInfoCoordinate.semtenceInfoDiv = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv
                                                            semtenceInfoCoordinate.semtenceYtop = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop
                                                            semtenceInfoCoordinate.x = Math.floor((startX - coordiv.offsetLeft) * widthScale);
                                                            semtenceInfoCoordinate.y = Math.floor((startY - coordiv.offsetTop) * heightScale);
                                                            semtenceInfoCoordinate.width = Math.ceil(($("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").outerWidth()) * widthScale);
                                                            semtenceInfoCoordinate.height = Math.ceil(($("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").outerHeight()) * heightScale);
                                                            _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1] = _semtenceInfoCoordinate
                                                        }
                                                    }
                                                }
                                            }
                                        })

                                        $(e.target).on("mouseleave", function (e) {

                                            if (j == $(e.target).text()) {

                                                var div = $("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]")
                                                div.css("resize", "")
                                                div.css("overflow", "")
                                                startX = div.offset().left;
                                                startY = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop;
                                                if (_x != up_x || _y != up_y) {
                                                    // _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceYtop = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceYtop + _y;
                                                    //     startY = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceYtop;
                                                    if (_this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop - coordiv.offsetTop + _y <= 0) {
                                                        _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop = coordiv.offsetTop;
                                                        startY = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop

                                                    } else if (_this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop - coordiv.offsetTop + $("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").outerHeight() + _y >= $("#img").height()) {
                                                        _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop = coordiv.offsetTop + $("#img").height() - $("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").outerHeight();
                                                        startY = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop;
                                                    } else {
                                                        _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop + _y;
                                                        startY = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop;
                                                    }

                                                    if (startX + _x <= 0) {
                                                        startX = 0;
                                                    } else if (startX + _x + div.outerWidth() >= $("#img").width()) {
                                                        startX = $("#img").width() - div.outerWidth()
                                                    } else {
                                                        startX = startX + _x
                                                    }
                                                }
                                                up_x = _x;
                                                up_y = _y;
                                                $(document).unbind("mousemove");
                                                $("#x_y").attr("value", "")
                                                $("#x_y").attr("value", Math.floor((startX - coordiv.offsetLeft) * widthScale) + "," + Math.floor((startY - coordiv.offsetTop) * heightScale) + "," + Math.ceil(($("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").outerWidth()) * widthScale) + "," + Math.ceil(($("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").outerHeight()) * heightScale))
                                                if (_this.pageList[i - 1].semtenceInfoList[j - 1]) {
                                                    var semtenceInfoCoordinate = {}
                                                    semtenceInfoCoordinate.semtenceInfoDiv = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv
                                                    semtenceInfoCoordinate.semtenceYtop = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop
                                                    semtenceInfoCoordinate.x = Math.floor((startX - coordiv.offsetLeft) * widthScale);
                                                    semtenceInfoCoordinate.y = Math.floor((startY - coordiv.offsetTop) * heightScale);
                                                    semtenceInfoCoordinate.width = Math.ceil(($("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").outerWidth()) * widthScale);
                                                    semtenceInfoCoordinate.height = Math.ceil(($("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").outerHeight()) * heightScale);
                                                    _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1] = semtenceInfoCoordinate
                                                }
                                                div.css("z-index", 300)

                                            }
                                            window.clearInterval(listenWidth);
                                            $(e.target).unbind("mouseleave")
                                        })

                                        $(e.target).on("mouseup", function (e) {

                                            if (j == $(e.target).text()) {
                                                var div = $("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]")
                                                div.css("resize", "")
                                                div.css("overflow", "")

                                                if (_x != up_x || _y != up_y) {
                                                    // _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceYtop = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceYtop + _y;
                                                    //     startY = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceYtop;
                                                    if (_this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop - coordiv.offsetTop + _y <= 0) {
                                                        _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop = coordiv.offsetTop;
                                                        startY = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop

                                                    } else if (_this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop - coordiv.offsetTop + $("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").outerHeight() + _y >= $("#img").height()) {
                                                        _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop = coordiv.offsetTop + $("#img").height() - $("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").outerHeight();
                                                        startY = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop;
                                                    } else {
                                                        _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop + _y;
                                                        startY = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop;
                                                    }

                                                    if (startX + _x <= 0) {
                                                        startX = 0;
                                                    } else if (startX + _x + div.outerWidth() >= $("#img").width()) {
                                                        startX = $("#img").width() - div.outerWidth()
                                                    } else {
                                                        startX = startX + _x
                                                    }
                                                }
                                                up_x = _x;
                                                up_y = _y;
                                                $(document).unbind("mousemove");
                                                $("#x_y").attr("value", "")
                                                $("#x_y").attr("value", Math.floor((startX - coordiv.offsetLeft) * widthScale) + "," + Math.floor((startY - coordiv.offsetTop) * heightScale) + "," + Math.ceil(($("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").outerWidth()) * widthScale) + "," + Math.ceil(($("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").outerHeight()) * heightScale))
                                                if (_this.pageList[i - 1].semtenceInfoList[j - 1]) {
                                                    var semtenceInfoCoordinate = {}
                                                    semtenceInfoCoordinate.semtenceInfoDiv = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv
                                                    semtenceInfoCoordinate.semtenceYtop = _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceYtop
                                                    semtenceInfoCoordinate.x = Math.floor((startX - coordiv.offsetLeft) * widthScale);
                                                    semtenceInfoCoordinate.y = Math.floor((startY - coordiv.offsetTop) * heightScale);
                                                    semtenceInfoCoordinate.width = Math.ceil(($("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").outerWidth()) * widthScale);
                                                    semtenceInfoCoordinate.height = Math.ceil(($("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").outerHeight()) * heightScale);
                                                    _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1] = semtenceInfoCoordinate
                                                }
                                                div.css("z-index", 300)
                                            }
                                            $(e.target).unbind("mouseup")
                                        })

                                    }
                                })

                                $("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[tem - 1].semtenceInfoDiv + "]").on("dblclick", function (e) {

                                    if (j == $(e.target).text()) {

                                        $("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[e.target.property - 1].semtenceInfoDiv + "]").remove();

                                        _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList.splice(e.target.property - 1, 1)

                                        for (var coorIndex = e.target.property; coorIndex <= _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList.length; coorIndex++) {

                                            $("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceInfoDiv + "]").get(0).property = coorIndex;

                                        }
                                    }

                                })

                                $(document).unbind("mouseup");
                            })
                        } else {
                            layer.msg(i18n['UI_70_02_01_030'], {time: 1000})
                        }
                    })
                    t++;
                }
                // }
            })
            $('#coordiv1').scroll(function () {
                if (i > 0) {
                    for (var k = 1; k <= _this.pageList[i - 1].semtenceInfoList.length; k++) {
                        for (var coorIndex = 1; coorIndex <= _this.pageList[i - 1].semtenceInfoList[k - 1].semtenceInfoCoordinateList.length; coorIndex++) {
                            $("div[title=" + _this.pageList[i - 1].semtenceInfoList[k - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceInfoDiv + "]").css("top",
                                _this.pageList[i - 1].semtenceInfoList[k - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceYtop - document.getElementById('coordiv1').scrollTop + 'px')
                        }
                    }
                }
            })

        },

        uploadFile: function () {
            var _this = this;
            $("#selectImg").click(function () {
                if (i == 0) {
                    layer.confirm(i18n['UI_70_02_01_022'], {
                        title: i18n['UI_70_02_01_019'],
                        btn: i18n['UI_70_02_01_021'] //按钮
                    });
                } else {
                    UploadFileUtils.uploadFile({
                        beforeAddFile: function (file) {
                            var isImage = file.type.indexOf('image') == 0;
                            if (!isImage) {
                                layer.msg(i18n['UI_70_02_01_028'], {time: 1000})
                            }
                            isImage && CommUtils.showMask();
                            return isImage;
                        },
                        uploadProgress: function (file, percent) {
                        },
                        uploadSuccess: function (file, result) {
                            _this.pageList[i - 1].pageInfoImgName = file.name;
                            $("#imgName").attr("value", _this.pageList[i - 1].pageInfoImgName);

                            _this.pageList[i - 1].pageInfoImgId = result.id
                            _this.pageList[i - 1].pageInfoImgRealUrl = result.downloadUrls[0];
                            $("#img").attr("src", _this.pageList[i - 1].pageInfoImgRealUrl);
                            var img = new Image();
                            img.src = _this.pageList[i - 1].pageInfoImgRealUrl;
                            img.onload = function () {
                                _this.pageList[i - 1].pageInfoImgWidth = img.width;
                                _this.pageList[i - 1].pageInfoImgHeight = img.height;
                            }
                            if (_this.pageList[i - 1]) {
                                for (var k = 1; k <= _this.pageList[i - 1].semtenceInfoList.length; k++) {
                                    $("div[title=" + _this.pageList[i - 1].semtenceInfoList[k - 1].semtenceInfoDiv + "]").css("display", "none");
                                }
                            }
                        },
                        uploadError: function (file, errorCode) {
                            layer.msg("上传失败请重新上传", {time: 1000})
                        },
                        uploadComplete: function (file) {
                            CommUtils.closeMask();
                        }
                    });
                }

            })

            $("#selectFrequency").click(function () {
                if (i == 0) {
                    layer.confirm(i18n['UI_70_02_01_022'], {
                        title: i18n['UI_70_02_01_019'],
                        btn: i18n['UI_70_02_01_021'] //按钮
                    });
                } else {
                    UploadFileUtils.uploadFile({
                        beforeAddFile: function (file) {
                            var isAudio = file.type.indexOf('audio/mpeg') == 0
                            if (!isAudio) {
                                layer.msg(i18n['UI_70_02_01_029'], {time: 1000})
                            }
                            return isAudio;
                        },
                        uploadProgress: function (file, percent) {

                        },
                        uploadSuccess: function (file, result) {

                            _this.pageList[i - 1].pageInfoMp3Name = file.name;
                            $("#frequencyName").attr("value", _this.pageList[i - 1].pageInfoMp3Name);

                            _this.pageList[i - 1].pageInfoMp3Id = result.id;
                            _this.pageList[i - 1].pageInfoMp3LoadUrl = result.downloadUrls[0];
                            $("#pageFre > audio").attr("src", _this.pageList[i - 1].pageInfoMp3LoadUrl)
                            $("#selectFrequency").css("display", "none");
                            $("#pageFre").css("display", "inline-block");
                            $("#pageFre > .fa-window-close-o").click(function () {
                                $("#selectFrequency").css("display", "inline-block");
                                $("#pageFre").css("display", "none");
                                clearInterval(_this.tp);
                                var audio = $("#pageFre  audio")[0];
                                audio.pause();
                                $("#pageListen").text("点击播放");
                                _this.pageList[i - 1].pageInfoMp3LoadUrl = "";
                                _this.pageList[i - 1].pageInfoMp3Name = "";
                                $("#frequencyName").attr("value", _this.pageList[i - 1].pageInfoMp3Name);
                            })
                        },
                        uploadError: function (file, errorCode) {

                            layer.msg("上传失败请重新上传", {time: 1000})
                        },
                        uploadComplete: function (file) {
                            CommUtils.closeMask();
                        }
                    });
                }
            })
            $("#semtenceMp3").click(function () {

                if (j == 0) {
                    layer.confirm(i18n['UI_70_02_01_022'], {
                        title: i18n['UI_70_02_01_019'],
                        btn: i18n['UI_70_02_01_021'] //按钮
                    });
                } else {
                    UploadFileUtils.uploadFile({
                        beforeAddFile: function (file) {
                            var isAudio = file.type.indexOf('audio/mpeg') == 0
                            if (!isAudio) {
                                layer.msg(i18n['UI_70_02_01_029'], {time: 1000})
                            }
                            return isAudio;
                        },
                        uploadProgress: function (file, percent) {
                        },
                        uploadSuccess: function (file, result) {
                            _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoMp3Name = file.name
                            $("#semtenceMp3Name").attr("value", _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoMp3Name);

                            _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoMp3Id = result.id;
                            _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoMp3LoadUrl = result.downloadUrls[0]
                            $("#semFre >audio").attr("src", _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoMp3LoadUrl)
                            $("#semtenceMp3").css("display", "none");
                            $("#semFre").css("display", "inline-block");
                            $("#semFre > .fa-window-close-o").click(function () {
                                $("#semtenceMp3").css("display", "inline-block");
                                $("#semFre").css("display", "none");
                                _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoMp3Name = "";
                                $("#semtenceMp3Name").attr("value", _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoMp3Name);
                                var audio = $("#semFre audio")[0];
                                clearInterval(_this.t);
                                audio.pause();
                                $("#listen").text("点击播放");
                                _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoMp3LoadUrl = ""
                            })
                        },
                        uploadError: function (file, errorCode) {

                            layer.msg("上传失败请重新上传", {time: 1000})
                        },
                        uploadComplete: function (file) {
                            CommUtils.closeMask();
                        }
                    });
                }
            })
        },

        editSemtence: function () {
            var _this = this;
            $("#addSemtence").click(function () {
                if (i == 0) {
                    layer.confirm(i18n['UI_70_02_01_022'], {
                        title: i18n['UI_70_02_01_019'],
                        btn: i18n['UI_70_02_01_021'] //按钮
                    });
                } else {

                    if (_this.pageList[i - 1].semtenceInfoList[j - 1]) {
                        for (var coorIndex = 1; coorIndex <= _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList.length; coorIndex++) {
                            $("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceInfoDiv + "]").css("opacity", 0.4);
                        }
                    }
                    if (_this.pageList[i - 1].semtenceInfoList && _this.pageList[i - 1].semtenceInfoList.length == 0) {
                        j = 0;
                    }

                    j++;

                    _this.semtenceInfo = {}
                    _this.semtenceInfo.semtenceInfoCoordinateList = []
                    _this.pageList[i - 1].semtenceInfoList.splice(j - 1, 0, _this.semtenceInfo);
                    _this.pageList[i - 1].semtenceInfoList[j - 1].sequence = j;

                    _this.showSemtencePages(j, _this.pageList[i - 1].semtenceInfoList.length)

                    $("#deleteSemtence").css("display", "inline-block")
                    if ($("#semtencePageCode" + (j - 1)).text()) {
                        $("#semtencePageCode" + (j - 1)).after("<div id='semtencePageCode" + j + "' style='margin-left: 5px' class='pprPager' >" + j + "</div>");
                    } else {
                        $("#addSemtence").before("<div id='semtencePageCode" + j + "' style='margin-left: 5px' class='pprPager'  >" + j + "</div>");
                    }

                    $("#semtencePageCode > div").css("borderColor", "#03A9F4");
                    $("#semtencePageCode" + j).css("borderColor", "red");

                    $("#semtencePageCode" + j).click(function (event) {
                        $("#semtencePageCode > div").css("borderColor", "#03A9F4");
                        $(event.target).css("borderColor", "red");

                        if (_this.pageList[i - 1].semtenceInfoList[j - 1]) {
                            for (var coorIndex = 1; coorIndex <= _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList.length; coorIndex++) {
                                $("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceInfoDiv + "]").css("opacity", 0.4);
                            }
                        }

                        j = $(event.target).text();
                        _this.showSemtencePages(j, _this.pageList[i - 1].semtenceInfoList.length)

                        if (_this.pageList[i - 1].semtenceInfoList[j - 1]) {
                            for (var coorIndex = 1; coorIndex <= _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList.length; coorIndex++) {
                                $("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceInfoDiv + "]").css("opacity", 1);
                                if (_this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceYtop) {
                                    $("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceInfoDiv + "]").css("top",
                                        _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceYtop - document.getElementById('coordiv1').scrollTop + 'px')
                                }
                            }
                        }
                        $("#semtenceMp3Name").attr("value", "")
                        $("#semtenceText").val("");

                        $("#semtenceMp3Name").attr("value", _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoMp3Name)
                        if (_this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoText) {
                            $("#semtenceText").val(_this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoText)
                        }

                        $("#semtenceMp3").css("display", "inline-block");
                        $("#semFre").css("display", "none");
                        if (_this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoMp3LoadUrl) {
                            $("#semtenceMp3").css("display", "none");
                            $("#semFre").css("display", "inline-block");
                        }
                        $("#semFre audio").attr("src", "");
                        $("#semFre audio").attr("src", _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoMp3LoadUrl);
                    });

                    for (var tem = j; tem < $("#semtencePageCode > div").length - 1; tem++) {
                        $("#semtencePageCode > div").eq(tem).text(tem + 1)
                        $("#semtencePageCode > div").eq(tem).attr("id", "semtencePageCode" + (tem + 1))

                        for (var coorIndex = 1; coorIndex <= _this.pageList[i - 1].semtenceInfoList[tem].semtenceInfoCoordinateList.length; coorIndex++) {
                            $("div[title=" + "" + _this.pageList[i - 1].semtenceInfoList[tem].semtenceInfoCoordinateList[coorIndex - 1].semtenceInfoDiv + "]").text(tem + 1);
                        }

                        _this.pageList[i - 1].semtenceInfoList[tem].sequence = tem + 1;
                    }
                    $("#semtenceMp3Name").attr("value", "");
                    $("#x_y").attr("value", "");
                    $("#semFre audio").attr("src", "");
                    $("#semtenceMp3").css("display", "inline-block");
                    $("#semFre").css("display", "none");
                    $("#semtenceText").val("")
                }
            });
            $("#deleteSemtence").click(function () {
                $("#semtencePageCode" + j).remove();

                for (var coorIndex = 1; coorIndex <= _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList.length; coorIndex++) {
                    $("div[title=" + "" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceInfoDiv + "]").remove();
                }

                _this.pageList[i - 1].semtenceInfoList.splice(j - 1, 1)
                for (var q = j; q < $("#semtencePageCode > div").length; q++) {
                    $("#semtencePageCode > div").eq(q - 1).text(q)
                    $("#semtencePageCode > div").eq(q - 1).attr("id", "semtencePageCode" + q)
                    _this.pageList[i - 1].semtenceInfoList[q - 1].sequence = q;
                    for (var coorIndex = 1; coorIndex <= _this.pageList[i - 1].semtenceInfoList[q - 1].semtenceInfoCoordinateList.length; coorIndex++) {
                        $("div[title=" + "" + _this.pageList[i - 1].semtenceInfoList[q - 1].semtenceInfoCoordinateList[coorIndex - 1].semtenceInfoDiv + "]").text(q);
                    }
                }
                if (j == 1) {
                    $("#semtencePageCode > div").css("borderColor", "#03A9F4");
                    $("#semtencePageCode" + j).css("borderColor", "red");
                    $("#semtenceMp3Name").attr("value", "");
                    $("#semtenceText").val("")
                    if (_this.pageList[i - 1].semtenceInfoList[j - 1]) {
                        $("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoDiv + "]").css("opacity", 1);
                        $("#semtenceMp3Name").attr("value", _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoMp3Name)

                        $("#semtenceText").val(_this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoText)
                    }
                } else {
                    j--;
                    $("#semtencePageCode > div").css("borderColor", "#03A9F4");
                    $("#semtencePageCode" + j).css("borderColor", "red");
                    $("div[title=" + _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoDiv + "]").css("opacity", 1);
                    $("#semtenceMp3Name").attr("value", "");
                    $("#semtenceText").val("")
                    $("#semtenceMp3Name").attr("value", _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoMp3Name)
                    if (_this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoText) {
                        $("#semtenceText").val(_this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoText)
                    }
                }

                _this.showSemtencePages(j, _this.pageList[i - 1].semtenceInfoList.length)
                if ($("#semtencePageCode > div").length == 1) {
                    $("#semtenceMp3").css("display", "inline-block");
                    $("#semFre").css("display", "none");
                    $("#x_y").val("");
                    $("#deleteSemtence").css("display", "none")
                    j--;
                }
            })
            $("#semtenceText").mouseleave(function () {
                if (i != 0 && j != 0 && _this.pageList[i - 1].semtenceInfoList[j - 1]) {
                    _this.pageList[i - 1].semtenceInfoList[j - 1].semtenceInfoText = $("#semtenceText").val();
                }
            })
        },

        editBookInfo: function () {
            var _this = this
            if (CommUtils.isEmpty(_this.etmBookInfo)) {
                _this.etmBookInfo = {
                    "textBookName": "",
                    "isbn": "",
                    "stageCode": "",
                    "stageName": "",
                    "gradeCode": "",
                    "gradeName": "",
                    "subjectCode": "",
                    "subjectName": "",
                    "textBookVersionCode": "",
                    "textBookVersionName": "",
                    "termCode": "",
                    "termName": "",
                    "coverId": "",
                    "coverUrl": "",
                    "coverImgName": "",
                    "userId": ""

                };
            }
            $("#save").click(function () {
                var url = "/manager/package/etm/baseinfo/preedit?" +
                    "userId=" + _this.userId +
                    "&stageCode=" + _this.etmBookInfo.stageCode +
                    "&gradeCode=" + _this.etmBookInfo.gradeCode + "" +
                    "&subjectCode=" + _this.etmBookInfo.subjectCode + "" +
                    "&termCode=" + _this.etmBookInfo.termCode + "" +
                    "&textBookVersionCode=" + _this.etmBookInfo.textBookVersionCode + "" +
                    "&textBookName=" + _this.etmBookInfo.textBookName +
                    "&isbn=" + _this.etmBookInfo.isbn +
                    "&coverId=" + _this.etmBookInfo.coverId +
                    "&coverUrl=" + _this.etmBookInfo.coverUrl +
                    "&coverImgName=" + _this.etmBookInfo.coverImgName;

                CommUtils.openFrame("书籍信息", url, "700px");

            })
            CommUtils.onMessage(function (event) {
                var data = event.data;
                if (CommUtils.isEmpty(data)) {
                    return;
                }
                if ('editBaseInfo' === data.type) {

                    data = data.data;

                    $("#grade").text("");
                    $("#grade").attr("title", "")

                    $("#bookName").text("");
                    $("#bookName").attr("title", "");

                    $("#subject").text("");
                    $("#subject").attr("title", "")

                    $("#baseBookInfo").css("display", "inline-block")
                    $("#isExit").css("display", "none")

                    _this.etmBookInfo.isbn = data.isbn;
                    _this.etmBookInfo.gradeCode = data.gradeCode
                    _this.etmBookInfo.gradeName = data.gradeName;
                    _this.etmBookInfo.textBookName = data.name;
                    _this.etmBookInfo.textBookVersionCode = data.textBookVersionCode;
                    _this.etmBookInfo.textBookVersionName = data.textBookVersionName;
                    _this.etmBookInfo.coverId = data.coverId;
                    _this.etmBookInfo.coverUrl = data.coverUrl;
                    _this.etmBookInfo.coverImgName = data.coverImgName;
                    _this.etmBookInfo.stageCode = data.stageCode;
                    _this.etmBookInfo.stageName = data.stageName;
                    _this.etmBookInfo.subjectCode = data.subjectCode;
                    _this.etmBookInfo.subjectName = data.subjectName;
                    _this.etmBookInfo.termCode = data.termCode;
                    _this.etmBookInfo.termName = data.termName;
                    _this.etmBookInfo.userId = _this.userId;

                    $("#grade").text(_this.etmBookInfo.gradeName);
                    $("#grade").attr("title", _this.etmBookInfo.gradeName)

                    $("#bookName").text(_this.etmBookInfo.textBookName);
                    $("#bookName").attr("title", _this.etmBookInfo.textBookName);

                    $("#subject").text(_this.etmBookInfo.subjectName);
                    $("#subject").attr("title", _this.etmBookInfo.subjectName)

                }

            })
        },

        changeBookStatus: function () {
            var _this = this;
            if (_this.etmBookInfo.contentId) {
                $.ajax({
                    type: 'POST',
                    url: '/manager/package/etm/addEtmBookInfo',
                    contentType: 'application/json',
                    data: JSON.stringify(_this.etmBookInfo),
                    //   dataType: "application/json",
                    async: false,
                    success: function (result) {
                        if (result.status == 1) {
                            var data = result.data;
                            _this.etmBookInfo = data;
                            if (_this.etmBookInfo.pageList && _this.etmBookInfo.pageList.length != 0) {
                                _this.pageList = _this.etmBookInfo.pageList;
                                _this.pageList.forEach(function (pageInfo) {
                                    pageInfo.status = "1";
                                })
                            }
                        }
                    }
                })
            }
        }
    }
})(window, jQuery);