package com.enableets.edu.pakage.framework.ppr.paper.service;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.enableets.edu.pakage.framework.ppr.bo.MakeOfflinePaperResultBO;
import com.enableets.edu.pakage.framework.ppr.bo.PaperInfoBO;
import com.enableets.edu.pakage.framework.ppr.core.AttachmentDownloadHandler;
import com.enableets.edu.pakage.framework.ppr.core.PPRConfigReader;
import com.enableets.edu.pakage.framework.ppr.utils.PaperInfoUtils;
import com.enableets.edu.pakage.framework.ppr.utils.Utils;
import com.enableets.edu.sdk.content.dto.ContentFileInfoDTO;
import com.enableets.edu.sdk.content.service.IContentInfoService;
import com.enableets.edu.sdk.filestorage.IFileService;
import com.enableets.edu.sdk.filestorage.dto.FileInfoDTO;
import com.enableets.edu.sdk.paper.util.PaperUtils;
import com.enableets.edu.sdk.paper.util.dto.Exam;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * .Paper Build Service
 */
@Service
public class MakeOfflinePaperService {

	private static final Logger logger = LoggerFactory.getLogger(MakeOfflinePaperService.class);

	@Autowired
	private PPRConfigReader pprConfigReader;
	
	/** Content SDK Client  */
	@Autowired
	private IContentInfoService contentInfoServiceSDK;

	@Autowired
	private IFileService fileServiceSDK;

	/** 
	 * Save test paper to resource library (generate offline test paper)
	 * @param paperInfo Paper information
	 */
	public MakeOfflinePaperResultBO makeOfflinePaper(PaperInfoBO paperInfo) {
		MakeOfflinePaperResultBO resultBO = new MakeOfflinePaperResultBO();
		List<File> fileList = getOfflinePaper(paperInfo);
		// 4 上传文件
		List<FileInfoDTO> fileBeans = uploadPaperFile(fileList);
		List<String> fileIds = new ArrayList<String>();
		for (FileInfoDTO f : fileBeans) {
			contentInfoServiceSDK.addFileToContent(translateFile2ContentFile(f, paperInfo));
			fileIds.add(f.getId());
		}
		// 9 删除临时文件夹
		if (!CollectionUtils.isEmpty(fileList)) {
			deletePaperDirPath(fileList.get(0).getParent());
		}
		resultBO.setFiles(fileBeans);
		return resultBO;
	}

	/**
	* Generate offline test paper
	*
	* @param paperInfo Paper information
	* @return java.util.List<java.io.File>
	* @author caleb_liu@enable-ets.com
	*/
	public List<File> getOfflinePaper(PaperInfoBO paperInfo) {
		// 1 Generate offline folder path
		String offLinePaperDirPath = getPaperDirPath();
		// 2 Copy Question file information
		copyOriginFile(pprConfigReader.getOfflinePaperOriginPath(), offLinePaperDirPath);
		String paperDirName = pprConfigReader.getOfflinePaperOriginPath().substring(pprConfigReader.getOfflinePaperOriginPath().lastIndexOf("/") + 1);
		// 3 Construct xml object information of test paper
		Exam exam = PaperInfoUtils.buildExamXmlBO(paperInfo);
		List<File> fileList = new ArrayList<File>();
		// 4 生成未处理原始exam.xml文件
		fileList.add(PaperUtils.buildExamXmlFile(exam, offLinePaperDirPath + "exam.xml"));
		// 5 生成离线试卷
		String paperTempDir = offLinePaperDirPath + paperDirName;
		// 5.2 生成离线试卷
		AttachmentDownloadHandler handler = new AttachmentDownloadHandler(paperTempDir);
		fileList.add(PaperUtils.buildOfflinePaperFile(paperTempDir, exam, handler));
		return fileList;
	}

	/**
	 * 删除临时打包文件夹
	 * @param offLinePaperDirPath
	 */
	public void deletePaperDirPath(String offLinePaperDirPath) {
		try {
			File randDir = new File(offLinePaperDirPath);
			FileUtils.deleteDirectory(randDir);
		} catch (IOException e) {
			logger.error("删除临时文件夹失败",e);
		}
	}

	/** 
	 * 添加文件到content
	 * @param fileList 文件list
	 * @return 
	 */
	private List<FileInfoDTO> uploadPaperFile(List<File> fileList) {
		try {
			if (!CollectionUtils.isEmpty(fileList)) {
				List<FileInfoDTO> list = new ArrayList<FileInfoDTO>();
				fileList.stream().forEach(file -> list.add(fileServiceSDK.upload(file)));
				return list;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return Collections.emptyList();
	}

	/** 
	 * 转换上传后的文件信息为3.0 资源文件信息
	 * @param fileBean 上传后的文件信息
	 * @param paperInfoBO 资源标识
	 * @return
	 */
	private ContentFileInfoDTO translateFile2ContentFile(FileInfoDTO fileBean, PaperInfoBO paperInfoBO) {
		ContentFileInfoDTO contentFile = new ContentFileInfoDTO();
		contentFile.setContentId(paperInfoBO.getContentId().toString());
		contentFile.setProviderCode(paperInfoBO.getProviderCode());
		contentFile.setDescription(fileBean.getDescription());
		contentFile.setDownloadNumber(0);
		contentFile.setFileId(fileBean.getId());
		contentFile.setFileName(fileBean.getName());
		if (fileBean.getName().contains(".")) {
			contentFile.setFileExt(fileBean.getName().substring(fileBean.getName().lastIndexOf(".") + 1));
		}
		contentFile.setMd5(fileBean.getMd5());
		if (fileBean.getSize() != null) {
			contentFile.setSize(fileBean.getSize());
			contentFile.setSizeDisplay(Utils.getFileSizeDisplay(contentFile.getSize()));
		}
		contentFile.setUploadDate(new Date());
		contentFile.setUrl(fileBean.getDownloadUrl());
		contentFile.setUrl(fileBean.getDownloadUrl());
		return contentFile;
	}

	/** 
	 * 拷贝答题页面离线文件到目标文件夹
	 * @param offlinePaperOriginPath 原始离线试卷文件路径
	 * @param offLinePaperDirPath 离线试卷文件夹路径
	 */
	private void copyOriginFile(String offlinePaperOriginPath, String offLinePaperDirPath) {
		File offlinePaperOriginDir = new File(offlinePaperOriginPath);
		if (!offlinePaperOriginDir.exists()){
			offlinePaperOriginDir.mkdirs();
		}
		try {
			FileUtils.copyDirectoryToDirectory(offlinePaperOriginDir, new File(offLinePaperDirPath));
		} catch (IOException e) {
			logger.error("页面文件拷贝出错" + e.getMessage(), e);
		}
	}

	/** 
	 * 获取生成离线文件文件夹路径
	 * @return
	 */
	private String getPaperDirPath() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		String offLinePaperDirPath = pprConfigReader.getOfflinePaperTempPath() + "/" + sdf.format(Calendar.getInstance().getTime()) + "_" + Utils.getRandomDir(8) + "/";
		File offLineForder = new File(offLinePaperDirPath);
		if (!offLineForder.exists()) {
			offLineForder.mkdirs();
		}
		return offLinePaperDirPath;
	}
}
