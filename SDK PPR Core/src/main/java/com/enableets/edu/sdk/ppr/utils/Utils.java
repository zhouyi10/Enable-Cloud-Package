package com.enableets.edu.sdk.ppr.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;

import com.enableets.edu.sdk.ppr.adapter.PPRInterfaceAdapterException;
import com.enableets.edu.sdk.ppr.annotation.PaperProperties;
import com.enableets.edu.sdk.ppr.logging.Log;
import com.enableets.edu.sdk.ppr.logging.LogFactory;
import com.enableets.edu.sdk.ppr.ppr.core.Item;
import com.enableets.edu.sdk.ppr.ppr.core.Property;
import com.enableets.edu.sdk.ppr.xml.xpath.XNode;
import com.enableets.edu.sdk.ppr.xml.xpath.XPathParser;
import com.enableets.edu.sdk.ppr.xml.xpath.XPathParserFactory;

import cn.hutool.json.JSONObject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author caleb_liu@enable-ets.com
 * @date 2020/06/16 17:49
 */

public class Utils {

    private static Log log = LogFactory.getLog(Utils.class);

    private static final String LIST_TYPE = "java.util.List";

    public static final String getRandom(){
        return System.currentTimeMillis() + Math.round(new Random().nextDouble() * 1000000) + "";
    }

    /**
     * build header property by annotation
     * @param t
     * @return
     */
    public static <T> Property buildProperty(T t) {
        List<Item> items = new ArrayList<>();
        Field[] declaredFields = t.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            if (!field.isAnnotationPresent(PaperProperties.class)) continue;
            //Get all annotation on the Field
            Annotation[] declaredAnnotations = field.getDeclaredAnnotations();
            for (Annotation declaredAnnotation : declaredAnnotations) {
                if (!declaredAnnotation.annotationType().equals(PaperProperties.class)) continue;
                PaperProperties paperProperties = (PaperProperties) declaredAnnotation;
                String annotationVal = paperProperties.value();
                boolean nullNotShow = paperProperties.isNullNotShow();
                String propertyKey = StringUtils.isNotBlank(annotationVal) ? annotationVal : field.getName();
                try {
                    if ((field.get(t) == null || field.get(t).toString().equals("")) && nullNotShow) continue;
                    items.add(new Item(propertyKey, field.get(t) == null ? null :  field.get(t).toString()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return new Property(items);
    }

    public static <T> T create(Properties attributes, Class<T> t){
        T tt = null;
        try {
            tt = t.newInstance();
            Field[] declaredFields = t.getDeclaredFields();
            for (Field field : declaredFields) {
                field.setAccessible(true);
//                if (field.getGenericType().getTypeName().equals("java.lang.Float")){
//                    field.set(tt, Float.valueOf(attributes.getProperty(field.getName())));
//                }else{
//                    field.set(tt, attributes.getProperty(field.getName()));
//                }
//                if (LIST_TYPE.equals(field.getType().getName())) continue;
//                field.set(tt, ConvertUtils.convert(attributes.getProperty(field.getName()), field.getType()));
                field.set(tt, Convert.convert(field.getType(), attributes.getProperty(field.getName())));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tt;
    }

    public static void create(List<XNode> nodes, Object tt) throws Exception {
        if (CollectionUtil.isEmpty(nodes)) return;
        Map<String, XNode> nodeMap = new HashMap<>();
        for (XNode node : nodes) {
            nodeMap.put(node.getAttribute("key"), node);
        }
        try {
            Class<?> t = tt.getClass();
            Field[] declaredFields = t.getDeclaredFields();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                String propertyKey = field.getName();
                Annotation[] declaredAnnotations = field.getDeclaredAnnotations();
                for (Annotation declaredAnnotation : declaredAnnotations) {
                    if (!declaredAnnotation.annotationType().equals(PaperProperties.class)) continue;
                    PaperProperties paperProperties = (PaperProperties) declaredAnnotation;
                    String annotationVal = paperProperties.value();
                    if (StringUtils.isNotBlank(annotationVal)) {
                        propertyKey = annotationVal;
                        break;
                    }
                }
                XNode node = nodeMap.get(propertyKey);
                if (node == null) continue;
                field.set(tt, Convert.convert(field.getType(), node.getBody()));
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public static <T> T create(XNode node, Class<T> t) {
        if (node == null) return null;
        T tt = null;
        try {
            tt = t.newInstance();
            Field[] declaredFields = t.getDeclaredFields();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                if (!field.isAnnotationPresent(PaperProperties.class)) continue;
                Annotation[] declaredAnnotations = field.getDeclaredAnnotations();
                for (Annotation declaredAnnotation : declaredAnnotations) {
                    if (!declaredAnnotation.annotationType().equals(PaperProperties.class)) continue;
                    PaperProperties paperProperties = (PaperProperties) declaredAnnotation;
                    String annotationVal = paperProperties.value();
                    String propertyKey = StringUtils.isNotBlank(annotationVal) ? annotationVal : field.getName();
                    try {
                        field.set(tt, Convert.convert(field.getType(), node.getAttribute(propertyKey)));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tt;
    }

    public static <T> T createByAttributeBody(XNode node, Class<T> t) {
        if (node == null) return null;
        T tt = null;
        try {
            tt = t.newInstance();
            Field[] declaredFields = t.getDeclaredFields();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                String propertyKey = field.getName();
                Annotation[] declaredAnnotations = field.getDeclaredAnnotations();
                for (Annotation declaredAnnotation : declaredAnnotations) {
                    if (!declaredAnnotation.annotationType().equals(PaperProperties.class)) continue;
                    PaperProperties paperProperties = (PaperProperties) declaredAnnotation;
                    String annotationVal = paperProperties.value();
                    if (StringUtils.isNotBlank(annotationVal)) propertyKey = annotationVal;
                }
                try {
                    String attribute = node.getAttribute(propertyKey);
                    if (propertyKey == "value")  attribute = node.getBody();
                    field.set(tt, Convert.convert(field.getType(), attribute));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tt;
    }

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

    public static XNode creatRootXNodeToXMLFile(String xmlPath, String rootExpression) {
        if (StringUtils.isBlank(xmlPath) && FileUtil.isFile(xmlPath)) return null;
        XPathParser xpathParser = XPathParserFactory.buildToFile(xmlPath);
        return xpathParser.evalNode(rootExpression);
    }

    public static XNode creatRootXNodeToXMLString(String xmlString, String rootExpression) {
        if (StringUtils.isBlank(xmlString)) return null;
        XPathParser xpathParser = XPathParserFactory.buildToString(xmlString);
        return xpathParser.evalNode(rootExpression);
    }

    public static <T> T create(List<Item> items, Class<T> tClass) {
        if (CollectionUtil.isEmpty(items)) return null;
        Map<String, Item> itemMap = items.stream().collect(Collectors.toMap(Item::getKey, v -> v, (k1, k2) -> k1));
        T tt = null;
        try {
            tt = tClass.newInstance();
            for (Field field : tClass.getDeclaredFields()) {
                field.setAccessible(true);
                String propertyKey = field.getName();
                Annotation[] declaredAnnotations = field.getDeclaredAnnotations();
                for (Annotation declaredAnnotation : declaredAnnotations) {
                    if (!declaredAnnotation.annotationType().equals(PaperProperties.class)) continue;
                    PaperProperties paperProperties = (PaperProperties) declaredAnnotation;
                    String annotationVal = paperProperties.value();
                    if (StringUtils.isNotBlank(annotationVal)) propertyKey = annotationVal;
                }
                Item item = itemMap.get(propertyKey);
                if (item != null) {
                    field.set(tt, Convert.convert(field.getType(), item.getValue()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tt;
    }

    public static String responseDataHandler(String responseData){
        JSONObject json = new JSONObject(responseData);
        if (StringUtils.isNotBlank(responseData) && json.get("status").equals("success")){
            return json.getStr("data");
        }else{
            throw new PPRInterfaceAdapterException("Access interface is abnormal：status:" + json.get("statusCode"));
        }
    }
}
