package com.enableets.edu.sdk.ppr.ppr.parse.xml;

import com.enableets.edu.sdk.ppr.exceptions.PPRVersionMismatchException;
import com.enableets.edu.sdk.ppr.ppr.core.question.Question;

import java.io.File;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/17
 **/
public class QuestionXmlIgnoreVersionParser extends QuestionXmlParser {

    public QuestionXmlIgnoreVersionParser(String questionXml){
        super(questionXml);
        setIgnoreVersion(Boolean.TRUE);
    }

    public QuestionXmlIgnoreVersionParser(File file){
        super(file);
        setIgnoreVersion(Boolean.TRUE);
    }

    @Override
    public Question parse() {
        try {
            return super.parse();
        } catch (PPRVersionMismatchException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Question parseBO() {
        try {
            return super.parse();
        } catch (PPRVersionMismatchException e) {
            e.printStackTrace();
        }
        return null;
    }
}
