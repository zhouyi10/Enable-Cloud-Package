package com.enableets.edu.pakage.core.core.http;

import org.apache.commons.lang3.StringUtils;

import com.enableets.edu.pakage.core.core.Configuration;
import com.enableets.edu.pakage.core.core.logging.Log;
import com.enableets.edu.pakage.core.core.logging.LogFactory;

import cn.hutool.crypto.digest.DigestUtil;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * URL signature
 * @author walle_yu@enable-ets.com
 * @since 2020/06/23
 **/
public class SignatureUtils {

    private static final Log LOGGER = LogFactory.getLog(SignatureUtils.class);

    public static String getSignature(String appKey, String secret, Map<String, String> params)
            throws UnsupportedEncodingException {
        // Dictionary sorting of parameter names
        String[] keyArray = params.keySet().toArray(new String[0]);
        Arrays.sort(keyArray);
        // Prefix with appKey and suffix appSecret, splicing parameters
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(appKey);
        for (String key : keyArray) {
            // body  data={}
            if (StringUtils.isNotEmpty(params.get(key))) {
                stringBuilder.append(key).append(params.get(key));
            }
        }
        stringBuilder.append(secret);
        String codes = stringBuilder.toString();
        // Calculate signature
        return DigestUtil.sha1Hex(codes.getBytes("UTF-8")).toUpperCase();
    }


    public static String sign(String url, String bodyStr, Configuration conf){
        Map<String, String> paramMap = new HashMap<>();
        try {
            URI uri = new URI(url);
            String rawQuery = uri.getRawQuery();
            if (StringUtils.isNotBlank(rawQuery)) {
                String queryParams = URLDecoder.decode(rawQuery, "utf-8");
                String[] params = queryParams.split("&");
                for (int i = 0; i < params.length; i++) {
                    String[] paramPair = params[i].split("=");
                    if (paramPair.length == 2) {
                        paramMap.put(String.valueOf(paramPair[0]), String.valueOf(paramPair[1]));
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("Failed to parse get request parameter!", e);
            throw new packageHttpClientException("Failed to parse get request parameter!");
        }
        if (StringUtils.isNotBlank(bodyStr)) paramMap.put("data", bodyStr);
        try {
            String sign = getSignature(conf.getClientId(), conf.getClientSecret(), paramMap);
            return buildSignUrl(url, sign, conf.getClientId());
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("sign error!", e);
            e.printStackTrace();

        }
        return null;
    }

    private static String buildSignUrl(String url,String sign, String clientId){
        StringBuilder sb = new StringBuilder(url);
        if (url.indexOf("?") == -1) sb.append("?");
        if (sb.toString().endsWith("?")) sb.append("clientId=");
        else sb.append("&clientId=");
        return sb.append(clientId).append("&sign=").append(sign).toString();
    }
}
