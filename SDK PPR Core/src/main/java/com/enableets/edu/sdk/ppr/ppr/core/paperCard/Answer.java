package com.enableets.edu.sdk.ppr.ppr.core.paperCard;

import com.enableets.edu.sdk.ppr.ppr.core.FileItem;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/29
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@XStreamAlias("answer")
public class Answer {

    public Answer(Long id, String text, Trail trail){
        this.id = id;
        this.text = text;
        this.trail = trail;
    }

    @XStreamAsAttribute
    private Long id;

    private String text;

    private List<FileItem> files;

    private Trail trail;
}
