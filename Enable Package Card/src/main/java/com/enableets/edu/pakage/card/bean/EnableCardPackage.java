package com.enableets.edu.pakage.card.bean;

import org.apache.commons.collections.CollectionUtils;

import com.enableets.edu.pakage.card.bean.body.CardBody;
import com.enableets.edu.pakage.card.bean.body.action.Action;
import com.enableets.edu.pakage.card.bean.body.action.ActionItem;
import com.enableets.edu.pakage.card.bean.body.answer.Answer;
import com.enableets.edu.pakage.card.bean.body.answer.AnswerItem;
import com.enableets.edu.pakage.card.bean.body.answer.AnswerQuestion;
import com.enableets.edu.pakage.card.bean.body.answer.Timestamp;
import com.enableets.edu.pakage.card.bean.body.answer.Trail;
import com.enableets.edu.pakage.card.bean.body.layout.Axis;
import com.enableets.edu.pakage.card.bean.body.layout.Layout;
import com.enableets.edu.pakage.card.bean.body.layout.LayoutQuestion;
import com.enableets.edu.pakage.card.bean.body.mark.Mark;
import com.enableets.edu.pakage.card.bean.body.mark.MarkItem;
import com.enableets.edu.pakage.card.bean.body.mark.MarkResult;
import com.enableets.edu.pakage.core.bean.AbstractEnablePackage;
import com.enableets.edu.pakage.core.bean.Files;
import com.enableets.edu.pakage.core.bean.Header;
import com.enableets.edu.pakage.core.bean.IEnableXmlPackage;
import com.enableets.edu.pakage.core.bean.Item;
import com.enableets.edu.pakage.core.bean.Property;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/11/19
 **/
@Data
@XStreamAlias("package")
@NoArgsConstructor
public class EnableCardPackage extends AbstractEnablePackage<CardBody> implements IEnableXmlPackage {

    private Header header;

    private CardBody body;

    private Files files;

    @Override
    public Class[] buildXmlClasses() {
        return new Class[]{
              EnableCardPackage.class, Header.class, Property.class,
              CardBody.class, Layout.class, LayoutQuestion.class, Axis.class,
              Action.class, ActionItem.class, Answer.class, AnswerItem.class, AnswerQuestion.class, Timestamp.class,
              Trail.class, Mark.class, MarkItem.class, MarkResult.class,
              Files.class
        };
    }

    public String readRefId(){
        if (header != null && header.getProperty() != null && CollectionUtils.isNotEmpty(header.getProperty().getItems())){
            for (Item item : header.getProperty().getItems()) {
                if (item.getKey().equals("ref")) return item.getValue();
            }
        }
        return null;
    }
}
