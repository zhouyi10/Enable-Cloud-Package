package com.enableets.edu.pakage.card.bean.body;

import com.enableets.edu.pakage.card.bean.body.action.Action;
import com.enableets.edu.pakage.card.bean.body.answer.Answer;
import com.enableets.edu.pakage.card.bean.body.layout.Layout;
import com.enableets.edu.pakage.card.bean.body.mark.Mark;
import com.enableets.edu.pakage.core.bean.Body;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/11/19
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@XStreamAlias("body")
public class CardBody extends Body {

    private Layout layout;

    private Answer answer;

    private Action action;

    private Mark mark;
}
