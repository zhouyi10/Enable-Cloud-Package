package com.enableets.edu.sdk.ppr.ppr.bo.card.action;

import com.enableets.edu.sdk.ppr.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Step field action type corresponding action name
 * @author walle_yu@enable-ets.com
 * @since 2020/07/14
 **/
public class ActionMapper {

    private static final String RECEIVE_ACTIVITY_PAPER = "receive"; //Receive test paper step action  name

    private static final String ANSWER_PAPER = "answer";  //Answer action name

    private static final String SUBMIT_PAPER = "submit";  //hand in an examination paper action name

    private static final String RECEIVE_ANSWER = "receiveAnswer"; // receive answer action name

    private static final String ORGANIZE_PAPERS = "organize"; //organize test papers action name

    private static final String MARK_PAPER = "mark"; // Review papers action name

    private static final String CHECK_SCORE = "checkScore"; //check score action name

    private static final String PUBLISH_SCORE = "publish"; // Post results action name

    private static Map<String, String> dictionaryMap;

    public static String getActionName(String type){
        init();
        return dictionaryMap.get(type);
    }

    public static void init(){
        if (dictionaryMap == null || dictionaryMap.size() == 0) {
            dictionaryMap = new HashMap<>();
            dictionaryMap.put("2", RECEIVE_ACTIVITY_PAPER);
            dictionaryMap.put("3", ANSWER_PAPER);
            dictionaryMap.put("4", SUBMIT_PAPER);
            dictionaryMap.put("5", RECEIVE_ANSWER);
            dictionaryMap.put("6", ORGANIZE_PAPERS);
            dictionaryMap.put("7", MARK_PAPER);
            dictionaryMap.put("8", CHECK_SCORE);
            dictionaryMap.put("9", PUBLISH_SCORE);
        }
    }

    public static String getActionType(String actionName){
        init();
        if (StringUtils.isBlank(actionName)) return null;
        String type = null;
        for (Map.Entry<String, String> entry : dictionaryMap.entrySet()) {
            if (entry.getValue().equals(actionName)) {
                type = entry.getKey(); break;
            }
        }
        return type;
    }
}
