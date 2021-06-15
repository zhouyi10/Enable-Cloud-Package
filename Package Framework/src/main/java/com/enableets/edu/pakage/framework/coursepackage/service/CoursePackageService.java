package com.enableets.edu.pakage.framework.coursepackage.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.enableets.edu.framework.core.util.BeanUtils;
import com.enableets.edu.framework.core.util.StringUtils;
import com.enableets.edu.module.service.core.MicroServiceException;
import com.enableets.edu.pakage.framework.coursepackage.bo.CoursePackageAdditionalInfoBO;
import com.enableets.edu.pakage.framework.coursepackage.bo.CoursePackageBO;
import com.enableets.edu.pakage.framework.coursepackage.bo.GroupBO;
import com.enableets.edu.pakage.framework.coursepackage.dao.CoursePackageInfoDAO;
import com.enableets.edu.pakage.framework.coursepackage.po.CoursePackageInfoPO;
import com.enableets.edu.sdk.cloudclass.dto.QueryCoursePackageRelationGroupInfoResultDTO;
import com.enableets.edu.sdk.cloudclass.service.ICoursePackageService;
import com.enableets.edu.sdk.content.dto.ContentInfoDTO;
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

	@Autowired
	private CoursePackageInfoDAO coursePackageInfoDAO;

	@Autowired
	private ICoursePackageService coursePackageService;


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
				coursePackageInfoDAO.insert(coursePackageInfoPO);
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
			coursePackageInfoPO.setStatus(STATUS_NORMAL);
			coursePackageInfoPO.setCreator(userId);
			coursePackageInfoPO.setCreateTime(new Date());
			coursePackageInfoPO.setUpdator(userId);
			coursePackageInfoPO.setUpdateTime(new Date());
			coursePackageInfoDAO.insert(po);
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
		CoursePackageAdditionalInfoBO additionalInfoBO = null;
		if (CollectionUtils.isNotEmpty(coursePackageInfoPOList)) {
			additionalInfoBO = BeanUtils.convert(coursePackageInfoPOList.get(0), CoursePackageAdditionalInfoBO.class);
		} else {
			LOGGER.error("Get coursePackageInfo is empty,contentId:{}", contentId);
		}
		coursePackageBO.setCoursePackage(additionalInfoBO);
		return coursePackageBO;
	}

	/**
	 * query course package info by condition
	 * @param coursePackageBO
	 * @return
	 */
	public List<CoursePackageBO> queryList(CoursePackageBO coursePackageBO) {
		ContentInfoDTO condition = BeanUtils.convert(coursePackageBO, ContentInfoDTO.class);
		List<ContentInfoDTO> contentList = contentInfoServiceSDK.query(condition);
		if (CollectionUtils.isEmpty(contentList)) {
			return null;
		}
		Set<Long> contentIds = contentList.stream().map(c -> c.getContentId()).collect(Collectors.toSet());
		Example example = new Example(CoursePackageInfoPO.class);
		example.createCriteria().andIn("contentId", contentIds);
		List<CoursePackageInfoPO> additionalList = coursePackageInfoDAO.selectByExample(example);
		Map<String, CoursePackageInfoPO> map = new HashMap<>(); 
		if (CollectionUtils.isNotEmpty(additionalList)) {
			Map<String, List<CoursePackageInfoPO>> additionalMap = additionalList.stream().collect(Collectors.groupingBy(CoursePackageInfoPO::getContentId));
			additionalMap.forEach((k,v)->{
				map.put(k, v.get(0));
			});
		}

		List<QueryCoursePackageRelationGroupInfoResultDTO> queryCoursePackageRelationGroupInfoResultDTOS = coursePackageService.queryCoursePackageRelationGroupInfo(StringUtils.join(contentIds, ","));
		Map<String, List<QueryCoursePackageRelationGroupInfoResultDTO.QueryGroupInfoResultDTO>> coursePackageMap= new HashMap<>();
		if (CollectionUtils.isNotEmpty(queryCoursePackageRelationGroupInfoResultDTOS)){
			Map<String, List<QueryCoursePackageRelationGroupInfoResultDTO>> tempMap = queryCoursePackageRelationGroupInfoResultDTOS.stream().collect(Collectors.groupingBy(QueryCoursePackageRelationGroupInfoResultDTO::getContendId));
			tempMap.forEach((k,v)->{
				coursePackageMap.put(k,v.get(0).getGroupInfoResults());
			});
		}
		List<CoursePackageBO> list = new ArrayList<>();
		for (ContentInfoDTO content : contentList) {
			CoursePackageBO packageBO = BeanUtils.convert(content, CoursePackageBO.class);
			CoursePackageAdditionalInfoBO coursePackageAdditionalInfoBO = BeanUtils.convert(map.get(content.getContentId()+""), CoursePackageAdditionalInfoBO.class);
			if (CollectionUtils.isNotEmpty(coursePackageMap.get(content.getContentId()+""))){
				coursePackageAdditionalInfoBO.setGroups(BeanUtils.convert(coursePackageMap.get(content.getContentId()+""), GroupBO.class));
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
		ContentInfoDTO condition = BeanUtils.convert(coursePackageBO, ContentInfoDTO.class);
		return contentInfoServiceSDK.count(condition);
	}
}
