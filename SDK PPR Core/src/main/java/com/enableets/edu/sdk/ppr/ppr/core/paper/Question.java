package com.enableets.edu.sdk.ppr.ppr.core.paper;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Data;

import java.util.List;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/16
 **/
@Data
@XStreamAlias("question")
public class Question {

    public Question() {}

    public Question(String id, String value, List<QuestionHref> hrefs) {
        this.id = id;
        this.value = value;
        this.hrefs = hrefs;
    }

    public Question(String id, String value, List<QuestionHref> hrefs, List<Question> children) {
        this.id = id;
        this.value = value;
        this.hrefs = hrefs;
        this.children = children;
    }

    @XStreamAsAttribute
    private String id;

    @XStreamAsAttribute
    private String value;

    @XStreamAlias("hrefs")
    private List<QuestionHref> hrefs;

    @XStreamAlias("children")
    private List<Question> children;
}
