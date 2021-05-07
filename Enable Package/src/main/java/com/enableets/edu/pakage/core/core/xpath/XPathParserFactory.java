package com.enableets.edu.pakage.core.core.xpath;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/28
 **/
public class XPathParserFactory {

    public static XPathParser buildToFile(String path){
        InputStream inputStream = null;
        try{
            inputStream = new FileInputStream(new File(path));
        }catch (FileNotFoundException e){
            //throw new PPRXPathException("File[\""+path+"\"] not Found!");
        }
        return new XPathParser(inputStream);
    }

    public static XPathParser buildToString(String xmlString){
        return new XPathParser(xmlString);
    }
}
