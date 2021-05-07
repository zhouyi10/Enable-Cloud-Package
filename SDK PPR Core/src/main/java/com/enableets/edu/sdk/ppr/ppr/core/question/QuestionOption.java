package com.enableets.edu.sdk.ppr.ppr.core.question;

import com.enableets.edu.sdk.ppr.xml.xstream.EntityToXml;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/18
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@XStreamAlias("questionOption")
public class QuestionOption extends EntityToXml{

    @XStreamAsAttribute
    private String optionId;

    @XStreamAsAttribute
    private String questionId;

    @XStreamAlias("options")
    private Option option;
}
