package com.enableets.edu.sdk.ppr.ppr.core.paperCard;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/30
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@XStreamAlias("answerCard")
public class AnswerCard {

    @XStreamImplicit
    private List<AnswerItem> answerItems;
}
