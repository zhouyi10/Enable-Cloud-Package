package com.enableets.edu.pakage.framework.ppr.paper.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.enableets.edu.pakage.framework.ppr.bo.MakeOfflinePaperResultBO;
import com.enableets.edu.pakage.framework.ppr.bo.PaperInfoBO;

/**
 * Build Paper .paper Document
 */
@Service
public class OfflinePaperService {

	@Autowired
	private MakeOfflinePaperService makeOfflinePaperService;
	
	public MakeOfflinePaperResultBO makeOfflinePaper(PaperInfoBO paperInfo){
		return makeOfflinePaperService.makeOfflinePaper(paperInfo);
	}
}
