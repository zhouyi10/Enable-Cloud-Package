package com.enableets.edu.pakage.ppr.bean.body;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.util.List;
import lombok.Data;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/16
 **/
@Data
@XStreamAlias("question")
public class Question {

    public Question() {}

    public Question(String id, String value, String title, float score, List<QuestionHref> hrefs) {
        this.id = id;
        this.title = title;
        this.value = value;
        this.hrefs = hrefs;
    }

    public Question(String id, String value, String title, float score, List<QuestionHref> hrefs, List<Question> children) {
        this.id = id;
        this.value = value;
        this.title = title;
        this.score = score;
        this.hrefs = hrefs;
        this.children = children;
    }

    @XStreamAsAttribute
    private String id;

    @XStreamAsAttribute
    private String value;

    @XStreamAsAttribute
    private String title;

    @XStreamAsAttribute
    private Float score;

    @XStreamAlias("hrefs")
    private List<QuestionHref> hrefs;

    @XStreamAlias("children")
    private List<Question> children;
}
