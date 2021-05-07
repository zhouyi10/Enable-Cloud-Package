package com.enableets.edu.sdk.pakage.ppr.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import lombok.Data;

/**
 * Paper Info DTO
 * @author duffy_ding
 * @since 2018/01/02
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddPaperInfoDTO {
	
	/** Paper Name */
	private String name;

	/** Stage Info  */
	private CodeNameMapDTO stage;
	
	/** Grade Info  */
	private CodeNameMapDTO grade;
	
	/** Subject Info  */
	private CodeNameMapDTO subject;

	/** Paper Total Score */
	private Float totalPoints;

	/** Paper Node information  */
	private List<PaperNodeInfoDTO> nodes;
	
	/** Creator Info  */
	private IdNameMapDTO user;
	
	/** School Info  */
	private IdNameMapDTO school;

	/**  */
	private String providerCode;
	
	/** Textbook version*/
	private IdNameMapDTO materialVersion;

	/** Resource Id*/
	private String contentId;

	/** Zone Info*/
	private CodeNameMapDTO zone;

	private List<KnowledgeInfoDTO> knowledges;

}