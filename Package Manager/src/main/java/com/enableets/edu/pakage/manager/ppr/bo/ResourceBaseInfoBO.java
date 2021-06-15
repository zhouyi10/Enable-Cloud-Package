package com.enableets.edu.pakage.manager.ppr.bo;

import com.enableets.edu.pakage.manager.mark.bo.KnowledgeInfoBO;
import com.enableets.edu.pakage.manager.ppr.vo.KnowledgeInfoVO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.dozer.Mapping;

import java.util.List;

/**
 * @author caleb_liu@enable-ets.com
 * @date 2021/06/02
 **/

@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel(value = "Resource baes Info", description = "Resource baes Info")
public class ResourceBaseInfoBO {

    /** Stage Info  */
    @Mapping(value = "stage.code")
    private String stageCode;

    @Mapping(value = "stage.name")
    private String stageName;

    /** Grade Info  */
    @Mapping(value = "grade.code")
    private String gradeCode;

    @Mapping(value = "grade.name")
    private String gradeName;

    /** Subject Info  */
    @Mapping(value = "subject.code")
    private String subjectCode;

    @Mapping(value = "subject.name")
    private String subjectName;

    @Mapping(value = "materialVersion.id")
    private String materialVersion;

    /** */
    @Mapping(value = "materialVersion.name")
    private String materialVersionName;

    /** knowledge list  */
    private List<KnowledgeInfoBO> knowledges;

}
