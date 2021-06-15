package com.enableets.edu.pakage.manager.ppr.service;

import com.enableets.edu.pakage.framework.ppr.paper.dao.ExamStypeInfoDAO;
import com.enableets.edu.pakage.framework.ppr.paper.po.ExamStypeInfoPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author tony_liu@enable-ets.com
 * @since 2021/5/17
 **/
@Service
public class ExamStypeInfoService {



    @Autowired
    private ExamStypeInfoDAO examStypeInfoDao;


    public ExamStypeInfoPO querybyid(String examId){
        ExamStypeInfoPO examStypeinfoPO = examStypeInfoDao.selectByPrimaryKey(examId);
        return examStypeinfoPO;
    }

    public Integer addExamStypeinfoPO(ExamStypeInfoPO examStypeinfoPO){
        return examStypeInfoDao.insertSelective(examStypeinfoPO);
    }


}
