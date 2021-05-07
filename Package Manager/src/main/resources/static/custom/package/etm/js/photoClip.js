//base64转Blob
function base64ToBlob(urlData) {
    var arr = urlData.split(',');
    var mime = arr[0].match(/:(.*?);/)[1] || 'image/png';
    // 去掉url的头，并转化为byte
    var bytes = window.atob(arr[1]);
    // 处理异常,将ascii码小于0的转换为大于0
    var ab = new ArrayBuffer(bytes.length);
    // 生成视图（直接针对内存）：8位无符号整数，长度1个字节
    var ia = new Uint8Array(ab);
    for (var i = 0; i < bytes.length; i++) {
        ia[i] = bytes.charCodeAt(i);
    }
    return new Blob([ab], {
        type: mime
    });
}

/**
 * 上传blob对象
 * @param dataURL   文件url
 * @param dataName  文件名
 * @param isload    是否上传到服务器
 */
function blobUpload(dataURL,dataName,isload){
    if (isload) {
        var fileBlob = base64ToBlob(dataURL);
        var f = new File([fileBlob], dataName, {type: 'image/jpg'});
        var uploader = new WebUploader.Uploader();
        var wuFile = new WebUploader.Lib.File(WebUploader.guid('rt_'),f);
        var file = new WebUploader.File(wuFile);
        uploader.md5File(file).then(function (md5) {
            check(md5, file, function () {
                getToken(md5, f, uploadCover);
            });
        });
    }else{
        var imgArr = {
            "downloadUrl":dataURL,
            "sizeDisplay":'',
            "imgName":dataName
        }
        creactimgbox('upload',imgArr);//新增图片
    }
}

function check(md5,file,fn) {
    $.ajax({
        type:'GET',
        url: checkUrl,
        data:{md5:md5, fileName: file.name},
        success:function (ret) {
            if (ret.data) {
                var file = ret.data;
                var imgArr = {
                    "fileId":file['fileId'],
                    "fileName":file['fileName'],
                    "size":file['size'],
                    "sizeDisplay":file['sizeDisplay'],
                    "downloadUrl": file['url'],
                    "md5": file['md5']
                };
                //上传成功以后并返回服务器上的图片的相关信息，此时dataURL为服务器上图片地址
                creactimgbox('upload',imgArr);//新增图片
            } else {
                fn && fn();
            }
        },
        error:function (ret) {
            console.log(ret);
        }
    })
}

function uploadCover(file,fileCode) {
    var fd = new FormData();
    fd.append("file", file);
    var url = uploadUrl+"?fileName="+file.name+"&fileCode="+fileCode;
    $.ajax({
        type: 'POST',
        ignoreXSRF: true,
        url: encodeURI(url),
        data: fd,
        //不读取缓存中的结果 true的话会读缓存  其实post本身就不会读取缓存中的结构
        cache: false,
        //默认情况下，通过data选项传递进来的数据，如果是一个对象(技术上讲只要不是字符串)，都会处理转化成一个查询字符串，以配合默认内容类型 "application/x-www-form-urlencoded"。如果要发送 DOM 树信息或其它不希望转换的信息，请设置为 false。
        processData: false,
        //数据编码格式不使用jquery的方式 为了避免 JQuery 对其操作，从而失去分界符，而使服务器不能正常解析文件。
        contentType: false,
        success:function (ret) {
            if (ret) {
                var file = ret.data;
                var imgArr = {
                    "fileId":file['fileId'],
                    "fileName":file['name'],
                    "size":file['size'],
                    "sizeDisplay":file['sizeDisplay'],
                    "downloadUrl": file['downloadUrls'][0],
                    "md5": file['md5']
                };
                //上传成功以后并返回服务器上的图片的相关信息，此时dataURL为服务器上图片地址
                creactimgbox('upload',imgArr);//新增图片
            }
        },
        error:function () {
            console.log("upload error")
        }
    })
}

function getToken(md5, file, fn) {
    $.ajax({
        type: 'GET',
        url: tokenUrl,
        data:{md5:md5, size: file.size},
        success:function (ret) {
            if (ret && ret.status) {
                fn && fn(file,ret.data);
            }
        },
        error:function (ret) {
            console.log(ret);
        }
    })
}

//生成封面
function creactimgbox($clickId,imgArr){
    //var imgSrc = 'https://i1.mifile.cn/a4/xmad_15460105665204_tIHOo.jpg';
   /* var n = nowDateFn(7);*/

    var imgbox = document.createElement('a');
    imgbox.setAttribute('href',imgArr.downloadUrl);
    imgbox.setAttribute('data-fileId',imgArr.fileId);
    imgbox.setAttribute('data-fileName',imgArr.fileName);
   /* imgbox.setAttribute('data-fileExt',imgArr.fileName.substring(imgArr.fileName.lastIndexOf(".")+1,imgArr.fileName.length));*/
    imgbox.setAttribute('data-fileSize',imgArr.size);
    imgbox.setAttribute('data-sizeDisplay',imgArr.sizeDisplay);
    imgbox.setAttribute('data-md5',imgArr.md5);
    imgbox.classList.add('imgbox','img_gallery');
    /*imgbox.classList.add('imgbox-'+n); //给每张图片添加不一样的类名*/
    imgbox.innerHTML = "<em class='removeSelf' onclick='removeSelf(this)'><i class='iconfont icon-zengjia-01'>X</i></em><img  id='download' src='"+imgArr.downloadUrl+"' />";
    document.getElementById($clickId).nextElementSibling.appendChild(imgbox);
    setTimeout(function(){
        initPhotoSwipeFromDOM('.img-box-gallery','.img_gallery'); //容器类名   图片父元素类名
    },500);
    $(".img-box-gallery").removeAttr("hidden")
}

//删除图片
function removeSelf(obj) {
    $(obj).parent().remove();
    $(".img-box-gallery").empty();
    $(".img-box-gallery").attr("hidden","hidden");
    $("#cover").empty();
    $("#cover").append(
        '+'
    );
    $("#cover").removeClass("font_upload");
    $("#cover").addClass("img_upload");

}

