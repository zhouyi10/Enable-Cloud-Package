package com.enableets.edu.pakage.manager.mark.bo;

/**
 *  作答绘图信息
 *	@author walle_yu@enable-ets.com
 *  @2017/6/5
 */
public class CanvasInfoBO implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;

	/**
	 * 绘图主键id
	 */
	public String canvasId;
	
	/**
	 * 绘图的排序
	 */
	public Integer canvasOrder;
	
	/**
	 * 绘图类型 ：老师，学生
	 */
	public String canvasType;

	/**
	 * 绘图作答类型  0，绘图答案  1拍照答案  2 视频/录屏
	 */
	private String canvasAnswerType;
	
	/**
	 * 文件id
	 */
	public String fileId;
	
	/**
	 * 文件名
	 */
	public String fileName;
	
	/**
	 * 资源id
	 */
	public String contentId;

	/**
	 * 资源地址
	 */
	public String url;

	/**
	 * 用户标识
	 */
	private String  userId;

	public String getCanvasId() {
		return canvasId;
	}

	public void setCanvasId(String canvasId) {
		this.canvasId = canvasId;
	}

	public Integer getCanvasOrder() {
		return canvasOrder;
	}

	public void setCanvasOrder(Integer canvasOrder) {
		this.canvasOrder = canvasOrder;
	}

	public String getCanvasType() {
		return canvasType;
	}

	public void setCanvasType(String canvasType) {
		this.canvasType = canvasType;
	}

	public String getCanvasAnswerType() {
		return canvasAnswerType;
	}

	public void setCanvasAnswerType(String canvasAnswerType) {
		this.canvasAnswerType = canvasAnswerType;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "CanvasInfoBO{" +
				"canvasId='" + canvasId + '\'' +
				", canvasOrder=" + canvasOrder +
				", canvasType='" + canvasType + '\'' +
				", fileId='" + fileId + '\'' +
				", fileName='" + fileName + '\'' +
				", contentId='" + contentId + '\'' +
				", url='" + url + '\'' +
				'}';
	}
}
