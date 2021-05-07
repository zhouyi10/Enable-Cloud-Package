package com.enableets.edu.pakage.core.utils;

import org.apache.commons.lang3.StringUtils;

import com.enableets.edu.pakage.core.bean.Header;
import com.enableets.edu.pakage.core.bean.Item;
import com.enableets.edu.pakage.core.bean.Property;
import com.enableets.edu.pakage.core.core.logging.Log;
import com.enableets.edu.pakage.core.core.logging.LogFactory;
import com.enableets.edu.pakage.core.core.xpath.XNode;

import cn.hutool.core.io.FileUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/11/18
 **/
public class Utils {

    private static Log log = LogFactory.getLog(Utils.class);

    public static void downloadFile(String urlPath, String filePath) throws Exception {
        try {
            int byteread = 0;
            if (!StringUtils.isEmpty(urlPath)) {
                URL url = new URL(urlPath);
                InputStream inStream = url.openStream();
                File newFile = new File(filePath);
                if (!newFile.exists()) {
                    FileUtil.touch(newFile);
                }
                FileOutputStream fs = new FileOutputStream(filePath);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                fs.close();
            }
        } catch (Exception e) {
            log.error(String.format("file [%s] [%s] download error! %s", urlPath, filePath, e.getMessage()));
            if (FileUtil.isFile(filePath)) FileUtil.del(filePath);
            throw new Exception(String.format("file [%s] [%s] download error! %s", urlPath, filePath, e.getMessage()));
        }
    }

    public static Header parseHeader(XNode xNode){
        Header header = new Header();
        if (xNode == null) return header;
        header.setVersion(xNode.evalString("./version"));
        header.setEncoding(xNode.evalString("./encoding"));
        header.setVerification(xNode.evalString("./verification"));
        header.setEncryption(xNode.evalString("./encryption"));
        header.setCompression(xNode.evalString("./compression"));
        header.setProperty(parseProperty(xNode.evalNode("./property")));
        return header;
    }

    public static Property parseProperty(XNode xNode){
        if (xNode == null) return new Property();
        List<Item> items = new ArrayList<>();
        List<XNode> xNodes = xNode.evalNodes("./item");
        for (XNode itemXNode : xNodes) {
            items.add(new Item(itemXNode.getAttribute("key"),  itemXNode.getBody()));
        }
        return new Property(items);
    }
}
