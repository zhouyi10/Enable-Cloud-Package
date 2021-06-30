package com.enableets.edu.pakage.framework.coursepackage.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import com.enableets.edu.framework.core.util.BeanUtils;
import com.enableets.edu.framework.core.util.StringUtils;
import com.enableets.edu.module.service.core.MicroServiceException;
import com.enableets.edu.pakage.framework.coursepackage.bo.*;
import com.enableets.edu.pakage.framework.coursepackage.dao.CoursePackageInfoDAO;
import com.enableets.edu.pakage.framework.coursepackage.po.CoursePackageInfoPO;
import com.enableets.edu.sdk.cloudclass.dto.QueryCoursePackageRelationGroupInfoResultDTO;
import com.enableets.edu.sdk.cloudclass.service.ICoursePackageService;
import com.enableets.edu.sdk.content.dto.ContentInfoDTO;
import com.enableets.edu.sdk.content.dto.Response;
import com.enableets.edu.sdk.content.service.IContentInfoService;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

/**
 * @Description: CoursePackageService
 * @Author: chris_cai@enable-ets.com
 * @Since: 2021-05-22
 */
@Service
@Transactional(value = "packageTransactionManager",rollbackFor = Exception.class)
public class CoursePackageService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CoursePackageService.class);

	private static final String STATUS_NORMAL = "0";

	public static final String STATUS_RECYCLE = "1";

	public static final String STATUS_DELETED = "2";

	@Autowired
	private IContentInfoService contentInfoServiceSDK;

	@Autowired(required = false)
	private CoursePackageInfoDAO coursePackageInfoDAO;

	@Autowired
	private ICoursePackageService coursePackageService;

	@Autowired
	private CoursePackagePlanInfoService coursePackagePlanInfoService;


	/**
	 * add course package
	 * @param coursePackageBO
	 * @return
	 */
	public String add(CoursePackageBO coursePackageBO) {
		ContentInfoDTO contentInfoDTO = buildContentInfoDTO(coursePackageBO);
		try {
			ContentInfoDTO data = contentInfoServiceSDK.add(contentInfoDTO).getData();
			if (data != null) {
				String contentId = String.valueOf(data.getContentId());
				String userId = data.getCreator();
				CoursePackageInfoPO coursePackageInfoPO = BeanUtils.convert(coursePackageBO.getCoursePackage(), CoursePackageInfoPO.class);
				coursePackageInfoPO.setContentId(contentId);
				coursePackageInfoPO.setStatus(STATUS_NORMAL);
				coursePackageInfoPO.setCreator(userId);
				coursePackageInfoPO.setCreateTime(new Date());
				coursePackageInfoPO.setUpdator(userId);
				coursePackageInfoPO.setUpdateTime(new Date());
				coursePackageInfoPO.setStageCode(coursePackageBO.getStageCode());
				coursePackageInfoPO.setStageName(coursePackageBO.getStageName());
				coursePackageInfoPO.setGradeCode(coursePackageBO.getGradeCode());
				coursePackageInfoPO.setGradeName(coursePackageBO.getGradeName());
				coursePackageInfoPO.setSubjectCode(coursePackageBO.getSubjectCode());
				coursePackageInfoPO.setSubjectName(coursePackageBO.getSubjectName());
				coursePackageInfoPO.setSearchCode(coursePackageBO.getSearchCode());
				coursePackageInfoPO.setTypeCode(coursePackageBO.getTypeCode());
				coursePackageInfoPO.setTypeName(coursePackageBO.getTypeName());
				if (CollectionUtils.isNotEmpty(coursePackageBO.getKnowledgeList())){
					KnowledgeInfoBO knowledgeInfoBO = coursePackageBO.getKnowledgeList().get(0);
					coursePackageInfoPO.setMaterialVersion(knowledgeInfoBO.getMaterialVersion());
					coursePackageInfoPO.setMaterialVersionName(knowledgeInfoBO.getMaterialVersionName());
					coursePackageInfoPO.setKnowledgeId(knowledgeInfoBO.getKnowledgeId());
					coursePackageInfoPO.setKnowledgeName(knowledgeInfoBO.getKnowledgeName());
					coursePackageInfoPO.setSearchCode(knowledgeInfoBO.getSearchCode());
				}
				coursePackageInfoDAO.insert(coursePackageInfoPO);
				//新增课程包计划
				coursePackagePlanInfoService.addCoursePackageGroupInfo(contentId,coursePackageBO.getPlans());
				return contentId;
			}
		} catch (Exception e) {
			LOGGER.error("Add contentInfo error:{}", e);
			throw new MicroServiceException("500", "Add contentInfo error");
		}
		return null;
	}

	/**
	 * build add contend Info
	 * @param coursePackageBO
	 * @return
	 */
	private ContentInfoDTO buildContentInfoDTO(CoursePackageBO coursePackageBO) {
		String userId = coursePackageBO.getCreator();
		LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String createTime = formatter.format(now);
		ContentInfoDTO contentInfoDTO = BeanUtils.convert(coursePackageBO, ContentInfoDTO.class);
		contentInfoDTO.setStatus(STATUS_NORMAL);
		contentInfoDTO.setTypeCode("C33");
		contentInfoDTO.setCreator(userId);
		contentInfoDTO.setCreateTime(createTime);
		return contentInfoDTO;
	}

	/**
	 * edit course info
	 * @param coursePackageBO
	 * @return
	 */
	public String edit(CoursePackageBO coursePackageBO) {
		try {
			String contentId = coursePackageBO.getContentId();
			String userId = coursePackageBO.getCreator();
			LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			String updateTime = formatter.format(now);
			ContentInfoDTO contentInfoDTO = this.buildContentInfoDTO(coursePackageBO);
			contentInfoDTO.setUpdateTime(updateTime);
			String newContentId = String.valueOf(contentInfoServiceSDK.edit(contentInfoDTO));

			CoursePackageInfoPO coursePackageInfoPO = new CoursePackageInfoPO();
			coursePackageInfoPO.setContentId(contentId);
			coursePackageInfoPO.setStatus(STATUS_DELETED);
			Example example = new Example(CoursePackageInfoPO.class);
			example.createCriteria().andEqualTo("contentId", contentId);
			coursePackageInfoDAO.updateByExampleSelective(coursePackageInfoPO, example);

			CoursePackageInfoPO po = BeanUtils.convert(coursePackageBO.getCoursePackage(), CoursePackageInfoPO.class);
			po.setContentId(newContentId);
			po.setStatus(STATUS_NORMAL);
			po.setCreator(userId);
			po.setCreateTime(new Date());
			po.setUpdator(userId);
			po.setUpdateTime(new Date());
			po.setStageCode(coursePackageBO.getStageCode());
			po.setStageName(coursePackageBO.getStageName());
			po.setGradeCode(coursePackageBO.getGradeCode());
			po.setGradeName(coursePackageBO.getGradeName());
			po.setSubjectCode(coursePackageBO.getSubjectCode());
			po.setSubjectName(coursePackageBO.getSubjectName());
			po.setSearchCode(coursePackageBO.getSearchCode());
			po.setTypeCode(coursePackageBO.getTypeCode());
			po.setTypeName(coursePackageBO.getTypeName());
			if (CollectionUtils.isNotEmpty(coursePackageBO.getKnowledgeList())){
				KnowledgeInfoBO knowledgeInfoBO = coursePackageBO.getKnowledgeList().get(0);
				po.setMaterialVersion(knowledgeInfoBO.getMaterialVersion());
				po.setMaterialVersionName(knowledgeInfoBO.getMaterialVersionName());
				po.setKnowledgeId(knowledgeInfoBO.getKnowledgeId());
				po.setKnowledgeName(knowledgeInfoBO.getKnowledgeName());
				po.setSearchCode(knowledgeInfoBO.getSearchCode());
			}
			coursePackageInfoDAO.insert(po);

//			//TODO 删除之前的课程计划
//			coursePackagePlanInfoService.deleteCoursePackagePlainByContentId(contentId);
			//TODO 新增新的课程计划
			coursePackagePlanInfoService.addCoursePackageGroupInfo(contentId,coursePackageBO.getPlans());
			return newContentId;
		} catch (Exception e) {
			LOGGER.error("Edit contentInfo error:{}", e);
			throw new MicroServiceException("500", "Edit contentInfo error");
		}
	}


	/**
	 * remove course info (substance update status)
	 * @param contentId
	 * @return
	 */
	public Boolean remove(String contentId) {
		try {
			Boolean flag = contentInfoServiceSDK.remove(Long.valueOf(contentId)).getData();
			if (flag) {
				Example example = new Example(CoursePackageInfoPO.class);
				example.createCriteria().andEqualTo("contentId", contentId);
				CoursePackageInfoPO coursePackageInfoPO = new CoursePackageInfoPO();
				coursePackageInfoPO.setContentId(contentId);
				coursePackageInfoPO.setStatus(STATUS_RECYCLE);
				coursePackageInfoDAO.updateByExampleSelective(coursePackageInfoPO, example);

				//删除课程计划
				//coursePackagePlanInfoService.deleteCoursePackagePlainByContentId(contentId);
				return Boolean.TRUE;
			}
		} catch (Exception e) {
			LOGGER.error("Remove contentInfo error,contentId:{},cause:{}", contentId, e);
			throw new MicroServiceException("500", "Remove contentInfo error");
		}
		return Boolean.FALSE;
	}

	/**
	 * query course package info by content id
	 * @param contentId
	 * @return
	 */
	public CoursePackageBO getById(String contentId) {
		ContentInfoDTO contentInfoDTO = contentInfoServiceSDK.get(Long.valueOf(contentId)).getData();
		Example example = new Example(CoursePackageInfoPO.class);
		example.createCriteria().andEqualTo("contentId", contentId);
		List<CoursePackageInfoPO> coursePackageInfoPOList = coursePackageInfoDAO.selectByExample(example);
		CoursePackageBO coursePackageBO = BeanUtils.convert(contentInfoDTO, CoursePackageBO.class);
		List<CoursePackagePlanInfoBO> coursePackagePlanInfoBOS = coursePackagePlanInfoService.queryCoursePackagePlainByContentId(contentId);
		CoursePackageAdditionalInfoBO additionalInfoBO = null;
		if (CollectionUtils.isNotEmpty(coursePackageInfoPOList)) {
			additionalInfoBO = BeanUtils.convert(coursePackageInfoPOList.get(0), CoursePackageAdditionalInfoBO.class);
		} else {
			LOGGER.error("Get coursePackageInfo is empty,contentId:{}", contentId);
		}
		coursePackageBO.setCoursePackage(additionalInfoBO);
		coursePackageBO.setPlans(coursePackagePlanInfoBOS);
		return coursePackageBO;
	}

	/**
	 * query course package info by condition
	 * @param coursePackageBO
	 * @return
	 */
	public List<CoursePackageBO> queryList(CoursePackageBO coursePackageBO) {

		if (StringUtils.isNotBlank(coursePackageBO.getTypeCode())) {
			coursePackageBO.setTypeCodes(Arrays.asList(StringUtils.split(coursePackageBO.getTypeCode(),",")));
		}

		List<CoursePackageInfoPO> additionalList = coursePackageInfoDAO.queryCoursePackageByCondition(coursePackageBO);

		if (CollectionUtils.isEmpty(additionalList)) {
			return null;
		}
		Set<Long> contentIds = additionalList.stream().map(c -> Long.parseLong(c.getContentId())).collect(Collectors.toSet());
		List<ContentInfoDTO> contentList = contentInfoServiceSDK.queryByIds(new ArrayList<>(contentIds)).getData();

		Map<String, CoursePackageInfoPO> map = new HashMap<>(); 
		if (CollectionUtils.isNotEmpty(additionalList)) {
			Map<String, List<CoursePackageInfoPO>> additionalMap = additionalList.stream().collect(Collectors.groupingBy(CoursePackageInfoPO::getContentId));
			additionalMap.forEach((k,v)->{
				map.put(k, v.get(0));
			});
		}

		List<QueryCoursePackageRelationGroupInfoResultBO> queryCoursePackageRelationGroupInfoResultBOS = coursePackagePlanInfoService.queryCoursePackageRelationGroupInfo(StringUtils.join(contentIds, ","));
		Map<String, List<QueryCoursePackageRelationGroupInfoResultBO.QueryGroupInfoResultBO>> coursePackageMap= new HashMap<>();
		if (CollectionUtils.isNotEmpty(queryCoursePackageRelationGroupInfoResultBOS)){
			Map<String, List<QueryCoursePackageRelationGroupInfoResultBO>> tempMap = queryCoursePackageRelationGroupInfoResultBOS.stream().collect(Collectors.groupingBy(QueryCoursePackageRelationGroupInfoResultBO::getContendId));
			tempMap.forEach((k,v)->{
				v.forEach(i->{
					if (coursePackageMap.containsKey(k)) {
						coursePackageMap.get(k).addAll(i.getPlans());
					}else {
						List<QueryCoursePackageRelationGroupInfoResultBO.QueryGroupInfoResultBO> tempRelationGroupInfo = new ArrayList<>();
						tempRelationGroupInfo.addAll(i.getPlans());
						coursePackageMap.put(k,tempRelationGroupInfo);
					}
				});
			});
		}
		List<CoursePackageBO> list = new ArrayList<>();
		for (ContentInfoDTO content : contentList) {
			CoursePackageBO packageBO = BeanUtils.convert(content, CoursePackageBO.class);
			CoursePackageAdditionalInfoBO coursePackageAdditionalInfoBO = BeanUtils.convert(map.get(content.getContentId()+""), CoursePackageAdditionalInfoBO.class);
			if (CollectionUtils.isNotEmpty(coursePackageMap.get(content.getContentId()+""))){
				packageBO.setPlans(BeanUtils.convert(coursePackageMap.get(content.getContentId()+""),CoursePackagePlanInfoBO.class));
			}
			packageBO.setCoursePackage(coursePackageAdditionalInfoBO);
			list.add(packageBO);
		}
		return list;
	}

	/**
	 * query course package count info by condition
	 * @param coursePackageBO
	 * @return
	 */
	public Integer countList(CoursePackageBO coursePackageBO) {
		/// 之前由于没有stageCode 所以调用account那边的sdk，现在在数据库加入了，不知道之后以后是否要恢复
		/*ContentInfoDTO condition = BeanUtils.convert(coursePackageBO, ContentInfoDTO.class);
		return contentInfoServiceSDK.count(condition);*/
		if (StringUtils.isNotBlank(coursePackageBO.getTypeCode())) {
			coursePackageBO.setTypeCodes(Arrays.asList(StringUtils.split(coursePackageBO.getTypeCode(),",")));
		}
		return coursePackageInfoDAO.queryCoursePackageCount(coursePackageBO);
	}
}
