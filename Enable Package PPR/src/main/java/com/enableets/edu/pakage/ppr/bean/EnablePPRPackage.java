package com.enableets.edu.pakage.ppr.bean;

import com.enableets.edu.pakage.core.bean.Files;
import com.enableets.edu.pakage.core.bean.Header;
import com.enableets.edu.pakage.core.bean.IEnableXmlPackage;
import com.enableets.edu.pakage.core.bean.Property;
import com.enableets.edu.pakage.ppr.bean.body.PPRBody;
import com.enableets.edu.pakage.ppr.bean.body.Question;
import com.enableets.edu.pakage.ppr.bean.body.QuestionHref;
import com.enableets.edu.pakage.ppr.bean.body.Section;
import com.enableets.edu.pakage.ppr.bean.body.TestPart;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/11/17
 **/
@Data
@XStreamAlias("package")
@NoArgsConstructor
@AllArgsConstructor
public class EnablePPRPackage implements IEnableXmlPackage {

    private Header header;

    private PPRBody body;

    private Files files;

    @Override
    public Class[] buildXmlClasses(){
        return new Class[]{EnablePPRPackage.class, Header.class, Property.class,
                PPRBody.class, TestPart.class, Section.class, Question.class, QuestionHref.class,
                Files.class
        };
    }

}
