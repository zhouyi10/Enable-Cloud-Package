package com.enableets.edu.sdk.ppr.ppr.builder.handler;

import org.apache.commons.collections.CollectionUtils;

import com.enableets.edu.sdk.ppr.core.BeanUtils;
import com.enableets.edu.sdk.ppr.ppr.bo.card.AnswerCardBO;
import com.enableets.edu.sdk.ppr.ppr.bo.card.CardAxisBO;
import com.enableets.edu.sdk.ppr.ppr.bo.card.CardBO;
import com.enableets.edu.sdk.ppr.ppr.bo.card.action.ActionMapper;
import com.enableets.edu.sdk.ppr.ppr.bo.card.action.StepActionBO;
import com.enableets.edu.sdk.ppr.ppr.bo.card.answer.AnswerBO;
import com.enableets.edu.sdk.ppr.ppr.bo.card.answer.CanvasBO;
import com.enableets.edu.sdk.ppr.ppr.core.Constants;
import com.enableets.edu.sdk.ppr.ppr.core.FileItem;
import com.enableets.edu.sdk.ppr.ppr.core.Header;
import com.enableets.edu.sdk.ppr.ppr.core.Item;
import com.enableets.edu.sdk.ppr.ppr.core.Property;
import com.enableets.edu.sdk.ppr.ppr.core.paperCard.Action;
import com.enableets.edu.sdk.ppr.ppr.core.paperCard.ActionItem;
import com.enableets.edu.sdk.ppr.ppr.core.paperCard.Answer;
import com.enableets.edu.sdk.ppr.ppr.core.paperCard.AnswerCard;
import com.enableets.edu.sdk.ppr.ppr.core.paperCard.AnswerItem;
import com.enableets.edu.sdk.ppr.ppr.core.paperCard.Axis;
import com.enableets.edu.sdk.ppr.ppr.core.paperCard.Layout;
import com.enableets.edu.sdk.ppr.ppr.core.paperCard.LayoutQuestion;
import com.enableets.edu.sdk.ppr.ppr.core.paperCard.Timestamp;
import com.enableets.edu.sdk.ppr.ppr.core.paperCard.Trail;
import com.enableets.edu.sdk.ppr.utils.StringUtils;
import com.enableets.edu.sdk.ppr.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/01
 **/
public class PaperCardBuilder extends AbstractPaperCardBuilder {

    public PaperCardBuilder(){}

    public PaperCardBuilder(CardBO cardBO) {
        super(cardBO);
    }

    @Override
    public void createHeader() {
        Property property = this.getHeadProperty();
        paperCardXMl.setHeader(new Header(Constants.ENCODING, Constants.VERIFICATION, Constants.ENCRYPTION, Constants.COMPRESSION, property));
    }

    @Override
    public Layout createLayout(AnswerCardBO answerCard) {
        if (answerCard == null) return null;
        Layout layout = new Layout(answerCard.getPageType());
        Map<String, List<Axis>> questionAxisMap = new HashMap<>();
        for (CardAxisBO axis : answerCard.getAxises()) {
            String key = axis.getQuestionId();
            if (StringUtils.isNotBlank(axis.getParentId())) key = axis.getParentId();
            List<Axis> axises = questionAxisMap.get(key);
            if (axises == null) axises = new ArrayList<>();
            axises.add(new Axis(axis.getQuestionId(), axis.getXAxis(), axis.getYAxis(), axis.getWidth(), axis.getHeight(), axis.getPageNo()));
            questionAxisMap.put(key, axises);
        }
        List<LayoutQuestion> questions = new ArrayList<>();
        for (Map.Entry<String, List<Axis>> entry : questionAxisMap.entrySet()) {
            questions.add(new LayoutQuestion(entry.getKey(), entry.getValue()));
        }
        layout.setQuestions(questions);
        return layout;
    }

    @Override
    public Action createAction(List<StepActionBO> actions) {
        if (actions == null || actions.size() == 0) return null;
        List<ActionItem> items = new ArrayList<>();
        for (StepActionBO action : actions) {
            items.add(new ActionItem(action.getId(), ActionMapper.getActionName(action.getType()), action.getDescription() == null ? "" :  action.getDescription(), Utils.buildProperty(action)));
        }
        return new Action(items);
    }

    @Override
    public AnswerCard createAnswerCard(List<AnswerBO> answerBOs) {
        if (answerBOs == null || answerBOs.size() == 0) return null;
        List<AnswerItem> answerItems = new ArrayList<>();
        Long id = null;
        List<Answer> answers = null;
        for (AnswerBO answerBO : answerBOs) {
            if (answerBO.getParentId() == null || answerBO.getQuestionId().longValue() == answerBO.getParentId().longValue()){
                if (id != null) answerItems.add(new AnswerItem(id, answers));
                id = null;
                answers = new ArrayList<>();
                answers.add(transfer(answerBO));
                answerItems.add(new AnswerItem(answerBO.getQuestionId(), answers));
            } else {
                if (id == null){
                    id = answerBO.getParentId();
                    answers = new ArrayList<>();
                    answers.add(transfer(answerBO));
                }else{
                    if (id.longValue() != answerBO.getParentId().longValue()){
                        answerItems.add(new AnswerItem(id, answers));
                        id = answerBO.getParentId();
                        answers = new ArrayList<>();
                    }
                    answers.add(transfer(answerBO));
                }
            }
        }
        if (id != null){
            answerItems.add(new AnswerItem(id, answers));
        }
        return new AnswerCard(answerItems);
    }

    private Answer transfer(AnswerBO answerBO){
        Answer answer = new Answer(answerBO.getQuestionId(), answerBO.getAnswer() == null ? "" : answerBO.getAnswer()  , CollectionUtils.isEmpty(answerBO.getTrails()) ? null : new Trail(BeanUtils.convert(answerBO.getTrails(), Timestamp.class)));
        if (answerBO.getCanvases() != null && answerBO.getCanvases().size() > 0){
            List<FileItem> files = new ArrayList<>();
            //先不考虑 信息缺失问题
            for (CanvasBO canvas : answerBO.getCanvases()) {
                files.add(new FileItem(canvas.getFileName().substring(canvas.getFileName().lastIndexOf(".") + 1), canvas.getFileId(), canvas.getFileName(), canvas.getMd5(), canvas.getUrl()));
            }
            answer.setFiles(files);
        }
        return answer;
    }

    private Property getHeadProperty(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Item> items = new ArrayList<>();
        items.add(new Item("id", cardBO.getPaperId()));
        items.add(new Item("title", cardBO.getName()));
        items.add(new Item("description", cardBO.getName()));
        if (cardBO.getStage() != null) {
            items.add(new Item("stageCode", cardBO.getStage().getCode()));
            items.add(new Item("stageName", cardBO.getStage().getName()));
        }
        if (cardBO.getGrade() != null){
            items.add(new Item("gradeCode", cardBO.getGrade().getCode()));
            items.add(new Item("gradeName", cardBO.getGrade().getName()));
        }
        if (cardBO.getSubject() != null){
            items.add(new Item("subjectCode", cardBO.getSubject().getCode()));
            items.add(new Item("subjectName", cardBO.getSubject().getName()));
        }
        items.add(new Item("author", cardBO.getUser().getName()));
        items.add(new Item("createTime", sdf.format(cardBO.getCreateTime())));
        return new Property(items);
    }
}
