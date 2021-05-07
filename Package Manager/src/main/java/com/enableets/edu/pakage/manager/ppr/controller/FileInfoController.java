package com.enableets.edu.pakage.manager.ppr.controller;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.enableets.edu.framework.core.controller.OperationResult;
import com.enableets.edu.framework.core.util.BeanUtils;
import com.enableets.edu.framework.core.util.JsonUtils;
import com.enableets.edu.framework.core.util.StringUtils;
import com.enableets.edu.pakage.manager.core.AjaxRequestResponse;
import com.enableets.edu.pakage.manager.core.PackageConfigReader;
import com.enableets.edu.sdk.content.dto.ContentFileInfoDTO;
import com.enableets.edu.sdk.core.SignatureUtils;
import com.enableets.edu.sdk.filestorage.IFileService;
import com.enableets.edu.sdk.filestorage.dto.FileInfoDTO;
import com.enableets.edu.sdk.filestorage.dto.SignRandomCodeDTO;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/10/13
 **/
@Controller
@RequestMapping(value = "/manager/package/file")
public class FileInfoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileInfoController.class);

    @Autowired
    private IFileService fileServiceSDK;

    @Autowired
    private PackageConfigReader packageConfigReader;

    @RequestMapping("/exists")
    @ResponseBody
    public OperationResult checkFileByMd5(String fileName, String md5) {
        try {
            List<FileInfoDTO> files = fileServiceSDK.query(md5, fileName);
            if (CollectionUtils.isNotEmpty(files)) {
                if (StringUtils.isNotBlank(files.get(0).getId())) {
                    return new OperationResult(files.get(0));
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return new OperationResult(OperationResult.STATUS_ERROR, null, null);
    }

    @ResponseBody
    @RequestMapping(value = "/request", method = RequestMethod.GET)
    public AjaxRequestResponse getCode(String md5, String size) throws UnsupportedEncodingException {
        String sign = getSign(md5,size);
        SignRandomCodeDTO signRandomCodeDTO = fileServiceSDK.uploadRequest(packageConfigReader.getClientId(), sign, md5, size);
        return new AjaxRequestResponse(signRandomCodeDTO.getFileCode());
    }

    private String getSign(String md5, String size) throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<>();
        map.put("md5", md5);
        map.put("size", size);
        return SignatureUtils.getSignature(packageConfigReader.getClientId(), packageConfigReader.getClientSecret(), map);
    }

    @RequestMapping(value = "/upload")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file, @RequestParam("fileName") String fileName){
        FileInfoDTO upload = fileServiceSDK.upload(file);
        ContentFileInfoDTO fileInfo = new ContentFileInfoDTO();
        fileInfo.setFileId(upload.getId());
        fileInfo.setMd5(upload.getMd5());
        fileInfo.setFileName(upload.getName());
        if (org.apache.commons.lang3.StringUtils.isNotBlank(upload.getName())) { // 页面展示使用
            fileInfo.setFileExt(upload.getName().substring(upload.getName().lastIndexOf(".") + 1));
        }
        fileInfo.setSize(upload.getSize());
        fileInfo.setSizeDisplay(upload.getSizeDisplay());
        fileInfo.setDescription(upload.getDescription());
        fileInfo.setUrl(upload.getDownloadUrl());
        @SuppressWarnings("unchecked")
        Map<String, Object> result = BeanUtils.convert(fileInfo, HashMap.class);
        result.put("state", "SUCCESS");//ueditor需要属性
        result.put("title", upload.getName());
        return JsonUtils.convert(result);
    }
}
