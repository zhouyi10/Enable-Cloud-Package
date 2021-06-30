package com.enableets.edu.pakage.framework.coursepackage.dao;

import com.enableets.edu.framework.core.dao.BaseDao;
import com.enableets.edu.pakage.framework.coursepackage.bo.CoursePackageBO;
import com.enableets.edu.pakage.framework.coursepackage.po.CoursePackageInfoPO;

import java.util.List;
import java.util.Map;

/**
 * @Description: CoursePackageInfoDAO
 * @Author: chris_cai@enable-ets.com
 * @Since: 2021-05-24
 */
public interface CoursePackageInfoDAO extends BaseDao<CoursePackageInfoPO> {
    /**
     * 根据添加查询课程包信息
     * @param coursePackageBO
     * @return
     */
    List<CoursePackageInfoPO> queryCoursePackageByCondition(CoursePackageBO coursePackageBO);

    /**
     * 查询课程包总数
     * @param coursePackageBO
     * @return
     */
    Integer queryCoursePackageCount(CoursePackageBO coursePackageBO);
}
