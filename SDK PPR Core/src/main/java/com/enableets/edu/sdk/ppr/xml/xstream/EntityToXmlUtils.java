package com.enableets.edu.sdk.ppr.xml.xstream;

import cn.hutool.core.io.FileUtil;

import com.enableets.edu.sdk.ppr.ppr.core.question.Option;
import com.enableets.edu.sdk.ppr.ppr.core.question.QuestionOption;
import com.enableets.edu.sdk.ppr.utils.StringUtils;
import com.enableets.edu.sdk.ppr.logging.Log;
import com.enableets.edu.sdk.ppr.logging.LogFactory;
import com.enableets.edu.sdk.ppr.ppr.core.paper.PaperXML;
import com.enableets.edu.sdk.ppr.ppr.core.paperCard.PaperCardXML;
import com.enableets.edu.sdk.ppr.ppr.core.question.Question;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/16
 **/
public class EntityToXmlUtils {

    private final static Log log = LogFactory.getLog(EntityToXmlUtils.class);

    private static XStream xStream;

    private static final String XML_HEAD = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";

    private static void init(){
        if (xStream == null){
            xStream = new XStream(new DomDriver("UTF-8"));
            xStream.processAnnotations(getEntityChildrenClass());
        }
    }

    public static String toXml(Object entity){
        init();
        return XML_HEAD + xStream.toXML(entity);
    }

    public static File toFile(EntityToXml entity, String path){
        if (StringUtils.isBlank(path)) return null;
        File file = new File(path);
        if (!FileUtil.isFile(file)) {
            FileUtil.touch(file);
        }
        String xmlString = toXml(entity);
        FileUtil.writeString(xmlString, file, StandardCharsets.UTF_8);
        return file;
    }

    public static PaperXML fromXML(File file) {
        init();
        return (PaperXML) xStream.fromXML(file);
    }

    public static PaperXML fromXML(String file) {
        init();
        return (PaperXML) xStream.fromXML(file);
    }

    /**
    * get the subclass class of entityToXml
    *
    * @return subclass class array
    * @author caleb_liu@enable-ets.com
    * @date 2020/06/17 09:11
    */
    private static Class[] getEntityChildrenClass() {
        /* Android is not support Reflections */
        /*Reflections reflections = new Reflections(ROOT_PACKAGE_PATH);
        Set<Class<? extends EntityToXml>> subTypes = reflections.getSubTypesOf(EntityToXml.class);
        Class[] classes = new Class[subTypes.size()];
        int i = 0;
        for (Class<? extends EntityToXml> subType : subTypes) {
            classes[i] = subType;
            i++;
        }
        return classes;*/
        Class[] classes = {
                PaperXML.class,
                Question.class,
                PaperCardXML.class,
                Option.class,
                QuestionOption.class
        };
        return classes;
    }
}
