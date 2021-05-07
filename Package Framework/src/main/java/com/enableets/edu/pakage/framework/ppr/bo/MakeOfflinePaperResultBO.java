package com.enableets.edu.pakage.framework.ppr.bo;

import com.enableets.edu.sdk.filestorage.dto.FileInfoDTO;

import java.util.List;
import lombok.Data;

/**
 * 生成离线试卷返回BO
 * @author walle_yu@enable-ets.com
 * @since 2018年4月28日
 */
@Data
public class MakeOfflinePaperResultBO {
	
	/** 资源标识*/
	private Long contentId;
	
	/** 文件*/
	private List<FileInfoDTO> files;

}
