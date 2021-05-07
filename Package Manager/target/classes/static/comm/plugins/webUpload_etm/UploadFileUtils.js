/**
 * upload file utils
 */
(function() {
    window.UploadFileUtils = {};
})();

/** default param */
(function(utils){
    var defaultParam = {
        // swf path
        swf: '../webUploader/Uploader.swf',
        // server url
        server: '',
        // the btn
        pick: null,
        // no zip
        resize: false,
        autoMd5: true,
        events: {}
    };

    utils._options = {
        param: defaultParam
    };

    utils.global = function(obj) {
        if (!utils.checkEnv()) return;
        if (obj == undefined) return $.extend({}, utils._options.param);
        if (typeof obj != 'object') return utils._options.param[obj];
        $.extend(utils._options.param, obj);
        if (obj.uploadUrl) {
            utils._options.param.server = obj.uploadUrl;
        }
    };
})(UploadFileUtils);

/** error code */
(function(utils){
    var errorCode = {
        FailedWhenMd5: 'md5',
        FailedWhenCheckExist: 'checkFileExist',
        FailedWhenGetToken: 'getToken',
        FailedWhenUpload: 'upload',
    };

    utils.ErrorCode = errorCode;
})(UploadFileUtils);

/** check environment */
(function(utils) {
    utils.checkEnv = function() {
        if (typeof $ == 'undefined') {
            console.error("no dependency to jquery or zepto!");
            return false;
        }
        if (typeof WebUploader == "undefined") {
            console.error("no dependency to webUploader!");
            return false;
        }
        return true;
    }
})(UploadFileUtils);

(function(utils){

    /**
     * check the file exists
     * @param fileName file name
     * @param md5 md5
     * @param checkUrl url
     * @returns {*}
     */
    function checkExist(fileName, md5, checkUrl) {
        var def = $.Deferred();
        checkUrl = checkUrl || utils.global('checkUrl');
        if (checkUrl) {
            $.get(checkUrl, {"fileName" : fileName, "md5" : md5}, function(result){
                if (result.status == 1) {
                    result = result.data;
                    def.resolve(result);
                } else {
                    def.reject(result);
                }
            }).fail(function(result) {
                def.reject(result);
            });
        } else {
            def.resolve(null);
        }
        return def.promise();
    }

    /**
     * get upload token
     * @param tokenUrl token url
     * @returns {*}
     */
    function getUploadToken(tokenUrl,md5,size) {
        var def = $.Deferred();
        tokenUrl = tokenUrl || utils.global('tokenUrl');
        if (tokenUrl) {
            $.ajax({
                url: tokenUrl,
                data: {
                    md5: md5,
                    size: size
                },
                async: false,
                type: 'GET',
                success: function(result) {
                    if (result && result.status == 1) {
                        def.resolve(result.data);
                    } else {
                        def.reject(result);
                    }
                },
                error: function(result) {
                    def.reject(result);
                }
            });
        } else {
            def.resolve('');
        }
        return def.promise();
    }

    function md5File(file) {
        file.ruid = '' + new Date().getTime();
        file.getSource = function() {return this};
        var deferred = $.Deferred();
        var md5 = new WebUploader.Lib.Md5();
        md5.on( 'progress load', function( e ) {
            e = e || {};
            deferred.notify( e.total ? e.loaded / e.total : 1 );
        });

        md5.on( 'complete', function() {
            deferred.resolve( md5.getResult() );
        });

        md5.on( 'error', function( reason ) {
            deferred.reject( reason );
        });
        md5.loadFromBlob( file );

        return deferred.promise();
    }

    $.extend(utils, {
        md5File: md5File,
        checkExist: checkExist,
        getUploadToken: getUploadToken
    });
})(UploadFileUtils);

(function(utils, ErrorCode) {

    function dialogChooseFile(func) {
        var $fileInput = $('<input type="file" style="display: none;" />')
        $(document.body).append($fileInput);
        $fileInput.on('change', function() {
            var file = this.files[0];
            $fileInput.remove();
            func(file);
        });
        $fileInput.click();
    }

    function doUpload(file, param) {
        var def = $.Deferred();
        if (param.tokenUrl) {
            utils.getUploadToken(param.tokenUrl, param._file.md5, param._file.size).done(function (fileCode) {
                fileCode && (param._file.fileCode = fileCode);
                def.resolve();
            }).fail(function() {
                def.reject();
            });
        } else {
            def.resolve();
        }

        var uploadProgress = param.events.uploadProgress, uploadSuccess =  param.events.uploadSuccess, uploadError =  param.events.uploadError;
        def.done(function() {
            var uploadUrl = param.uploadUrl;
            uploadUrl += '?fileName=' + encodeURIComponent(param._file.name);
            param._file.fileCode && (uploadUrl += '&fileCode=' + param._file.fileCode);

            var formData = new FormData();
            formData.append('file', file);
            $.ajax({
                url: uploadUrl,
                type: 'post',
                data: formData,
                processData : false,
                contentType : false,
                xhr: function(){
                    var myXhr = $.ajaxSettings.xhr();
                    if(myXhr.upload && uploadProgress){
                        myXhr.upload.onprogress = function(e){
                            uploadProgress(file, e.loaded / e.total);
                        };
                    }
                    return myXhr;
                },
                success: function(result) {
                    uploadSuccess && uploadSuccess(param._file, result.data);
                },
                error: function() {
                    uploadError && uploadError(param._file, ErrorCode.FailedWhenUpload);
                },
                complete: function() {
                    param.events.uploadComplete && param.events.uploadComplete(param._file);
                }
            });
        }).fail(function() {
            uploadError && uploadError(file, ErrorCode.FailedWhenGetToken);
            param.events.uploadComplete && param.events.uploadComplete(param._file);
        });
    }

    function uploadFile(file, events) {
        file = file || null;
        if (typeof file == 'object' && typeof events == 'undefined' && !(file instanceof window.File)) {
            events = file;
            file = null;
        }
        if (!file) {
            dialogChooseFile(function(f) {
                uploadFile(f, events);
            });
            return;
        }
        var param = $.extend(utils.global(), {events: events || {}, _file: {name: file.name, ext: '', size: file.size, type: file.type}});
        param.events.uploadUrl && (param.uploadUrl = param.events.uploadUrl, delete param.events.uploadUrl);
        param.events.tokenUrl && (param.tokenUrl = param.events.tokenUrl, delete param.events.tokenUrl);
        param.events.checkUrl && (param.checkUrl = param.events.checkUrl, delete param.events.checkUrl);
        if (param._file.name.indexOf('.') > 0) {
            param._file.ext = param._file.name.substring(param._file.name.lastIndexOf('.') + 1);
        }
        var beforeAddFile = param.events.beforeAddFile, uploadSuccess = param.events.uploadSuccess, uploadError = param.events.uploadError;
        if (beforeAddFile) {
            var result = beforeAddFile(param._file);
            if (result == false) return;
        }
        utils.md5File(file).done(function(md5) {
            md5 && (param._file.md5 = md5);
            if (!!param.checkUrl) {
                utils.checkExist(param._file.name, param._file.md5, param.checkUrl).done(function(result) {
                    if (result != null) {
                        uploadSuccess && uploadSuccess(param._file, result);
                        param.events.uploadComplete && param.events.uploadComplete(param._file);
                    } else {
                        doUpload(file, param);
                    }
                }).fail(function(){
                    doUpload(file, param);
                });
            } else {
                doUpload(file, param);
            }
        }).fail(function() {
            uploadError && uploadError(param._file, ErrorCode.FailedWhenMd5);
            param.events.uploadComplete && param.events.uploadComplete(param._file);
        });
    }
    utils.uploadFile = uploadFile;
})(UploadFileUtils, UploadFileUtils.ErrorCode);

(function(utils) {
    function upload(uploader, file, callbackIfExist) {
        var checkUrl = uploader.options.checkUrl;
        if (checkUrl == undefined || checkUrl == null || $.trim(checkUrl) == '') {
            uploader.upload(file);
            return;
        }
        if (typeof file != 'object') {
            file = uploader.getFile(file);
        }
        var def = $.Deferred();
        if (file.md5 == undefined) {
            uploader.md5File(file).then(function (md5) {
                file.md5 = md5;
                def.resolve();
            });
        } else {
            def.resolve();
        }
        def.done(function() {
            utils.checkExist(file.name, file.md5, checkUrl).done(function(result) {
                if (result != null) {
                    uploader.skipFile(file);
                    callbackIfExist && callbackIfExist(file, result);
                } else {
                    uploader.upload(file);
                }
            }).fail(function(){
                uploader.upload(file);
            });
        });
    }

    utils.upload = upload;
})(UploadFileUtils);
/**
 * param    options
 *      - autoMd5         true/false      auto md5 file when file add to queue
 *      - beforeAddFile   func(fileInfo):boolean  you can check you file here,
 *      - ...
 */
(function(utils, ErrorCode){

    function create(options, events) {
        if (!utils.checkEnv()) return;
        var param = $.extend({}, utils.global());
        if (options) {
            $.extend(param, options);
            if (options.uploadUrl) {
                param.server = options.uploadUrl;
            }
        }
        if (events) {
            $.extend(param.events, events);
        }
        if (param.server == null || param.server == '') {
            console.error("server cannot be empty");
            return;
        }
        var uploader = WebUploader.create(param);

        uploader.on( 'beforeFileQueued', function( file ) {
            if (events.beforeAddFile != undefined && typeof events.beforeAddFile == 'function') {
                var result = events.beforeAddFile(file);
                return result == undefined ? true : result;
            }
            return true;
        });

        uploader.on( 'fileQueued', function( file ) {
            var promise = null;
            if (param.autoMd5) {
                promise = uploader.md5File(file);
            } else {
                var def = $.Deferred();
                def.resolve(null);
                promise = def.promise();
            }
            promise.then(function(md5) {
                md5 && (file.md5 = md5);
                if (events.addFile != undefined && typeof events.addFile == 'function') {
                    events.addFile(file, md5);
                }
            });
        });

        uploader.on('uploadStart', function(file) {
            var returned = null;
            if (events.uploadStart != undefined && typeof events.uploadProgress == 'function') {
                returned = events.uploadStart(file);
            }
            file.firstUpload = true;
        });

        uploader.on( 'uploadBeforeSend', function(object, data, headers ) {
            object.transport.options.server += '?fileName=' + encodeURIComponent(data.name);
            if (object.file.firstUpload) {// avoid re request token
                delete object.file.firstUpload;
                return utils.getUploadToken(param.tokenUrl, object.file.md5, object.file.size).done(function (fileCode) {
                    fileCode && (object.file.fileCode = fileCode,
                        object.transport.options.server += '&fileCode=' + fileCode);
                }).fail(function() {
                    if (events.uploadError != undefined && typeof events.uploadError == 'function') {
                        events.uploadError(object.file, ErrorCode.FailedWhenGetToken);
                    }
                });
            }
        });

        uploader.on( 'uploadProgress', function(file , percentage) {
            if (events.uploadProgress != undefined && typeof events.uploadProgress == 'function') {
                events.uploadProgress(file, percentage);
            }
        });

        var _this = this;
        uploader.on( 'uploadSuccess', function(file, response) {
            if (response && response.statusCode == '200' && response.data && response.data.fileId) {
                if (events.uploadSuccess != undefined && typeof events.uploadSuccess == 'function') {
                    response = response.data;
                    events.uploadSuccess(file, response);
                }
            } else if (events.uploadError != undefined && typeof events.uploadError == 'function') {
                events.uploadError(file, "System Err.");
            }
        });
        uploader.on( 'uploadError', function( file, reason) {
            if (events.uploadError != undefined && typeof events.uploadError == 'function') {
                events.uploadError(file, reason);
            }
        });
        uploader.on( 'uploadComplete', function( file ) {
            if (events.uploadComplete != undefined && typeof events.uploadComplete == 'function') {
                events.uploadComplete(file);
            }
        });
        return uploader;
    }

    utils.create = create;
})(UploadFileUtils, UploadFileUtils.ErrorCode);