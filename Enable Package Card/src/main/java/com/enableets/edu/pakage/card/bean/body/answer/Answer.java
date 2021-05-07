package com.enableets.edu.pakage.card.bean.body.answer;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/30
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@XStreamAlias("answer")
public class Answer {

    @XStreamImplicit
    private List<AnswerItem> answerItems;
}
