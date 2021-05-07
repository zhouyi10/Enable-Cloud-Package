package com.enableets.edu.sdk.ppr.ppr.builder.content;

import org.apache.commons.collections.CollectionUtils;

import com.enableets.edu.sdk.ppr.ppr.bo.PPRInfoBO;
import com.enableets.edu.sdk.ppr.ppr.bo.ppr.FileInfoBO;
import com.enableets.edu.sdk.ppr.ppr.builder.content.Exceptions.PPRContentException;
import com.enableets.edu.sdk.ppr.ppr.core.FileItem;
import com.enableets.edu.sdk.ppr.ppr.core.Href;
import com.enableets.edu.sdk.ppr.ppr.htmlparser.handler.AttachmentDownloadHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * PPR
 * @author walle_yu@enable-ets.com
 * @since 2020/10/27
 **/
public class PPRFileContent extends AbstractContent {

    public PPRFileContent(PPRInfoBO pprInfoBO) {
        super(pprInfoBO);
    }

    @Override
    public void downloadPaperFiles(String dirPath) {
        if (CollectionUtils.isNotEmpty(pprInfoBO.getPprBO().getFiles())){
            try {
                for (FileInfoBO file : pprInfoBO.getPprBO().getFiles()) {
                    AttachmentDownloadHandler handler = new AttachmentDownloadHandler();
                    handler.downloadFileRetry(file.getUrl(), new File(dirPath + "/" + getFileName(file.getFileName(), file.getFileExt()))).execute();
                }
            } catch (InterruptedException e) {
                throw new PPRContentException("Download PPR Paper File Failure!");
            }
        }
    }

    @Override
    public List<FileItem> buildBodyFilesWithOutQuestionXml() {
        List<FileItem> items = null;
        if (CollectionUtils.isNotEmpty(pprInfoBO.getPprBO().getFiles())){
            items = new ArrayList<>();
            List<String> exts = pprInfoBO.getPprBO().getFiles().stream().map(e -> e.getFileExt()).collect(Collectors.toList());
            for (String ext : exts) {
                List<Href> hrefs = new ArrayList<>();
                for (FileInfoBO file : pprInfoBO.getPprBO().getFiles()) {
                    if (file.getFileExt().equals(ext)){
                        hrefs.add(new Href("./files/" + getFileName(file.getFileName(), file.getFileExt())));
                    }
                }
                items.add(new FileItem(ext, hrefs));
            }
        }
        return items;
    }
}
