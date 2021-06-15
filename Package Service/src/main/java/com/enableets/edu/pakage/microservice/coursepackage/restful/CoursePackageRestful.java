package com.enableets.edu.pakage.microservice.coursepackage.restful;

import java.util.List;

import com.enableets.edu.framework.core.util.BeanUtils;
import com.enableets.edu.module.service.controller.ServiceControllerAdapter;
import com.enableets.edu.module.service.core.MicroServiceException;
import com.enableets.edu.module.service.core.Response;
import com.enableets.edu.pakage.framework.coursepackage.bo.CoursePackageBO;
import com.enableets.edu.pakage.framework.coursepackage.service.CoursePackageService;
import com.enableets.edu.pakage.microservice.coursepackage.vo.AddCoursePackageVO;
import com.enableets.edu.pakage.microservice.coursepackage.vo.EditCoursePackageVO;
import com.enableets.edu.pakage.microservice.coursepackage.vo.PageItem;
import com.enableets.edu.pakage.microservice.coursepackage.vo.QueryCoursePackageResultVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: CoursePackageRestful
 * @Author: chris_cai@enable-ets.com
 * @Since: 2021-05-22
 */
@Api(value = "[1]CoursePackage Api", tags = "CoursePackage Api", position = 1)
@RequestMapping(value = "/microservice/packageservice/v1/coursepackages")
@RestController
public class CoursePackageRestful extends ServiceControllerAdapter<Object> {

	@Autowired
	private CoursePackageService coursePackageService;

	@PostMapping("")
	@ApiOperation(value = "add course package", notes = "add course package")
	public Response<String> add(@ApiParam(value = "addCoursePackageVO", required = true)@RequestBody AddCoursePackageVO addCoursePackageVO) {
		try {
			String contentId = coursePackageService.add(BeanUtils.convert(addCoursePackageVO, CoursePackageBO.class));
			return responseTemplate.format(contentId);
		} catch (MicroServiceException e) {
			return responseTemplate.format(null, null, e.getErrorCode(), e.getMessage());
		}
	}

	@PutMapping("/{contentId}")
	@ApiOperation(value = "edit course package", notes = "edit course package")
	public Response<String> edit(@ApiParam(value = "contentId", required = true)@PathVariable("contentId") String contentId,
								 @ApiParam(value = "editCoursePackageVO", required = true)@RequestBody EditCoursePackageVO editCoursePackageVO) {
		try {
			editCoursePackageVO.setContentId(contentId);
			String newContentId = coursePackageService.edit(BeanUtils.convert(editCoursePackageVO, CoursePackageBO.class));
			return responseTemplate.format(newContentId);
		} catch (MicroServiceException e) {
			return responseTemplate.format(null, null, e.getErrorCode(), e.getMessage());
		}
	}

	@DeleteMapping("/{contentId}")
	@ApiOperation(value = "delete course package", notes = "delete course package")
	public Response<Boolean> remove(@ApiParam(value = "contentId", required = true)@PathVariable("contentId") String contentId) {
		try {
			Boolean flag = coursePackageService.remove(contentId);
			return responseTemplate.format(flag);
		} catch (MicroServiceException e) {
			return responseTemplate.format(Boolean.FALSE, null, e.getErrorCode(), e.getMessage());
		}
	}

	@GetMapping("/{contentId}")
	@ApiOperation(value = "query single course package", notes = "query single course package")
	public Response<QueryCoursePackageResultVO> getById(@ApiParam(value = "contentId", required = true)@PathVariable("contentId") String contentId) {
		CoursePackageBO coursePackageBO = coursePackageService.getById(contentId);
		return responseTemplate.format(BeanUtils.convert(coursePackageBO, QueryCoursePackageResultVO.class));
	}

	@GetMapping("")
	@ApiOperation(value = "query course package list", notes = "query course package list")
	public Response<PageItem<QueryCoursePackageResultVO>> queryList(
			@ApiParam(value = "Resource ID", required = false) @RequestParam(value = "contentId", required = false) String contentId,
			@ApiParam(value = "school id", required = false) @RequestParam(value = "schoolId", required = false) String schoolId,
			@ApiParam(value = "stage code", required = false) @RequestParam(value = "stageCode", required = false) String stageCode,
			@ApiParam(value = "grade code", required = false) @RequestParam(value = "gradeCode", required = false) String gradeCode,
			@ApiParam(value = "subject code", required = false) @RequestParam(value = "subjectCode", required = false) String subjectCode,
			@ApiParam(value = "Resource type", required = false) @RequestParam(value = "typeCode", required = false) String typeCode,
			@ApiParam(value = "Source of resources", required = true) @RequestParam(value = "providerCode", required = true) String providerCode,
			@ApiParam(value = "Textbook version", required = false) @RequestParam(value = "materialVersion", required = false) String materialVersion,
			@ApiParam(value = "Knowledge point parent-child relationship coding", required = false) @RequestParam(value = "searchCode", required = false) String searchCode,
			@ApiParam(value = "keyword", required = false) @RequestParam(value = "keyword", required = false) String keyword,
			@ApiParam(value = "Creator ID", required = false) @RequestParam(value = "creator", required = false) String creator,
			@ApiParam(value = "Sort by", required = false) @RequestParam(value = "orderType", required = false) String orderType,
			@ApiParam(value = "startTime", required = false) @RequestParam(value = "startTime", required = false) String startTime,
			@ApiParam(value = "endTime", required = false) @RequestParam(value = "endTime", required = false) String endTime,
			@ApiParam(value = "Page start position", required = true) @RequestParam(value = "offset", required = true) Integer offset,
			@ApiParam(value = "Number of items per page", required = true) @RequestParam(value = "rows", required = true) Integer rows) {
		CoursePackageBO coursePackageBO = new CoursePackageBO();
		coursePackageBO.setContentId(contentId);
		coursePackageBO.setSchoolId(schoolId);
		coursePackageBO.setStageCode(stageCode);
		coursePackageBO.setGradeCode(gradeCode);
		coursePackageBO.setSubjectCode(subjectCode);
		coursePackageBO.setTypeCode(typeCode);
		coursePackageBO.setProviderCode(providerCode);
		coursePackageBO.setMaterialVersion(materialVersion);
		coursePackageBO.setSearchCode(searchCode);
		coursePackageBO.setKeyword(keyword);
		coursePackageBO.setCreator(creator);
		coursePackageBO.setOrderType(orderType);
		coursePackageBO.setStartTime(startTime);
		coursePackageBO.setEndTime(endTime);
		coursePackageBO.setOffset(offset == null ? 0 : offset);
		coursePackageBO.setRows(rows == null ? 10 : rows);
		List<CoursePackageBO> coursePackageBOList = coursePackageService.queryList(coursePackageBO);
		Integer count = coursePackageService.countList(coursePackageBO);
		return responseTemplate.format(PageItem.format(BeanUtils.convert(coursePackageBOList, QueryCoursePackageResultVO.class), count, offset, rows));
	}
}
