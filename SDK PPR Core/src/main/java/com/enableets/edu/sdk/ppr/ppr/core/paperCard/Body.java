package com.enableets.edu.sdk.ppr.ppr.core.paperCard;

import com.thoughtworks.xstream.annotations.XStreamAlias;
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
@XStreamAlias("body")
public class Body {

    private Layout layout;

    private Action action;

    private AnswerCard answerCard;
}
