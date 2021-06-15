package com.enableets.edu.sdk.pakage.ppr.feign;

import com.enableets.edu.sdk.pakage.ppr.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.enableets.edu.sdk.core.Response;

import java.util.List;

/**
 * Package-PPR Test Client
 * @author walle_yu@enable-ets.com
 * @since 2020/10/19
 **/
@FeignClient(name = "${sdk.package-microservice.name:package-microservice}")
public interface IPPRTestInfoUserServiceFeignClient {

    @RequestMapping(value = "/microservice/packageservice/ppr/tests/users/{testUserId}/answer/track", method = RequestMethod.GET)
    public Response<List<QueryUserAnswerTrackResultDTO>> getAnswerTracks(@PathVariable("testUserId") String testUserId);

    @GetMapping(value = "/microservice/packageservice/ppr/tests/users/answers")
    public Response<List<QueryTestUserResultDTO>> queryAnswer(@RequestParam(value = "testId", required = false) String testId,
                                                              @RequestParam(value = "stepId", required = false) String stepId,
                                                              @RequestParam(value = "fileId", required = false) String fileId,
                                                              @RequestParam(value = "examId", required = false) String examId,
                                                              @RequestParam(value = "userId", required = false) String userId,
                                                              @RequestParam(value = "groupIds", required = false) String groupIds,
                                                              @RequestParam(value = "questionIds", required = false) String questionIds);

    @PostMapping(value = "/microservice/packageservice/ppr/tests/users/mark")
    public Response<TestMarkResultInfoDTO> mark(@RequestBody MarkInfoDTO markInfoDTO);

    @PutMapping(value = "/microservice/packageservice/ppr/tests/users/answer/canvas")
    public Response<Boolean> editCanvas(@RequestBody EditCanvasInfoDTO editCanvasInfo);

}
