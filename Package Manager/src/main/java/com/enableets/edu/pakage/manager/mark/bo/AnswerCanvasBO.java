package com.enableets.edu.pakage.manager.mark.bo;

/**
 * 作答绘图
 * @Author walle_yu@enable-ets.com
 * @since 2018/12/21
 */
public class AnswerCanvasBO {

    private String canvasId;

    /** 绘图作答类型  0，绘图答案  1拍照答案  2 视频/录屏 */
    private String canvasAnswerType;

    private Integer canvasOrder;

    private String answerId;

    private String fileId;

    private String fileName;

    private String contentId;

    private String url;

    /** 原答题绘图标识*/
    private String sourceFileId;

    public String getCanvasId() {
        return canvasId;
    }

    public void setCanvasId(String canvasId) {
        this.canvasId = canvasId;
    }

    public String getCanvasAnswerType() {
        return canvasAnswerType;
    }

    public void setCanvasAnswerType(String canvasAnswerType) {
        this.canvasAnswerType = canvasAnswerType;
    }

    public Integer getCanvasOrder() {
        return canvasOrder;
    }

    public void setCanvasOrder(Integer canvasOrder) {
        this.canvasOrder = canvasOrder;
    }

    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
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

    public String getSourceFileId() {
        return sourceFileId;
    }

    public void setSourceFileId(String sourceFileId) {
        this.sourceFileId = sourceFileId;
    }
}
