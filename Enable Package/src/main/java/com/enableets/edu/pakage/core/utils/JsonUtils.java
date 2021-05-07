package com.enableets.edu.pakage.core.utils;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/11/18
 **/
public class JsonUtils {
    private static ObjectMapper mapper = new ObjectMapper();

    public JsonUtils() {
    }

    public static <T> T convert(String jsonStr, Class<T> clazz) {
        try {
            T object = mapper.readValue(jsonStr, clazz);
            return object;
        } catch (JsonParseException var3) {
            throw new RuntimeException(var3);
        } catch (JsonMappingException var4) {
            throw new RuntimeException(var4);
        } catch (IOException var5) {
            throw new RuntimeException(var5);
        }
    }

    public static String convert(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException var2) {
            throw new RuntimeException(var2);
        }
    }

    public static String convertList(List list) {
        StringBuffer jsonStr = new StringBuffer("");
        if (list != null && list.size() > 0) {
            Iterator var2 = list.iterator();

            while(var2.hasNext()) {
                Object obj = var2.next();
                jsonStr.append(convert(obj) + ",");
            }
        }

        String json = jsonStr.toString();
        if (StringUtils.isNotEmpty(json)) {
            json = json.substring(0, json.length() - 1);
        }

        return "[" + json + "]";
    }

    public static <T> List<T> convert2List(String json, Class<T> beanClass) {
        try {
            return (List)mapper.readValue(json, getCollectionType(List.class, beanClass));
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static JavaType getCollectionType(Class<?> collectionClass, Class... elementClasses) {
        return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    static {
        mapper.setTimeZone(TimeZone.getDefault());
    }
}
