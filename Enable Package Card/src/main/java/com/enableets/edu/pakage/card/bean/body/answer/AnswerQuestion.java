package com.enableets.edu.pakage.card.bean.body.answer;

import com.enableets.edu.pakage.card.bean.body.FileItem;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/29
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@XStreamAlias("question")
public class AnswerQuestion {

    public AnswerQuestion(String id, String text, Trail trail){
        this.id = id;
        this.text = text;
        this.trail = trail;
    }

    @XStreamAsAttribute
    private String id;

    private String text;

    private List<FileItem> files;

    private Trail trail;
}
