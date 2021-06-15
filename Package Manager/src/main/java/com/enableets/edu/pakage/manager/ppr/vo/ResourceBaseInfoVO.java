package com.enableets.edu.pakage.manager.ppr.vo;

import lombok.*;
import lombok.experimental.Accessors;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
public class ResourceBaseInfoVO {

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
    private List<KnowledgeInfoVO> knowledges;

}
