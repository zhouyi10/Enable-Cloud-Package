package com.enableets.edu.pakage.core.core.xstream;

import org.apache.commons.lang3.StringUtils;

import com.enableets.edu.pakage.core.bean.IEnableXmlPackage;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import cn.hutool.core.io.FileUtil;
import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/16
 **/
public class EntityToXmlUtils {

    private static XStream xStream;

    private static final String XML_HEAD = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";

    private static void init(IEnableXmlPackage entity){
        xStream = new XStream(new DomDriver("UTF-8"));
        xStream.processAnnotations(entity.buildXmlClasses());
    }

    public static String toXml(IEnableXmlPackage entity){
        init(entity);
        return XML_HEAD + xStream.toXML(entity);
    }

    public static File toFile(IEnableXmlPackage entity, String path){
        if (StringUtils.isBlank(path)) return null;
        File file = new File(path);
        if (!FileUtil.isFile(file)) {
            FileUtil.touch(file);
        }
        String xmlString = toXml(entity);
        FileUtil.writeString(xmlString, file, StandardCharsets.UTF_8);
        return file;
    }
}
