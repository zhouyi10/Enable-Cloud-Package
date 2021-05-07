package com.enableets.edu.pakage.microservice.ppr.restful;

import com.enableets.edu.module.service.controller.ServiceControllerAdapter;
import com.enableets.edu.module.service.core.Response;
import com.enableets.edu.pakage.framework.workflow.po.VacTask;
import com.enableets.edu.pakage.framework.workflow.po.VacationPO;
import com.enableets.edu.pakage.framework.workflow.service.VacationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author tony_liu@enable-ets.com
 * @since 2021/2/20
 **/

@Api(value = "Activiti Info Api", tags = "Activiti Info Api")
@RestController
@RequestMapping(value = "/microservice/packageservice/ppr/activiti/")
public class ActivitiInfoRestful extends ServiceControllerAdapter<String> {

    @Autowired
    private VacationService vacationService;

    @ApiOperation(value = "start vacation", notes = "start vacation")
    @RequestMapping(value = "/start/vac", method = RequestMethod.GET)
    public Response<Boolean> startVac(
            @ApiParam(value = "userId", required = true) @RequestParam(value = "userId", required = true) String userId,
            @ApiParam(value = "days", required = true) @RequestParam(value = "days", required = true) String days,
            @ApiParam(value = "reason", required = true) @RequestParam(value = "reason", required = true) String reason) {
        vacationService.startVac(userId,days,reason);
        return responseTemplate.format(Boolean.TRUE);
    }

    @ApiOperation(value = "query vacation by userId", notes = "query vacation by userId")
    @RequestMapping(value = "/query/vac", method = RequestMethod.GET)
    public Response<List<VacationPO>> getVacByUserId(
            @ApiParam(value = "userId", required = true) @RequestParam(value = "userId", required = true) String userId) {
        return responseTemplate.format(vacationService.getVacByUserId(userId));
    }

    @ApiOperation(value = "query my audit", notes = "query my audit")
    @RequestMapping(value = "/my/audit", method = RequestMethod.GET)
    public Response<List<VacTask>> myAudit(
            @ApiParam(value = "userId", required = true) @RequestParam(value = "userId", required = true) String userId) {
        return responseTemplate.format(vacationService.myAudit(userId));
    }

    @ApiOperation(value = "pass audit", notes = "pass audit")
    @RequestMapping(value = "/pass/audit", method = RequestMethod.GET)
    public Response<Boolean> passAudit(
            @ApiParam(value = "userId", required = true) @RequestParam(value = "userId", required = true) String userId,
            @ApiParam(value = "taskId", required = true) @RequestParam(value = "taskId", required = true) String taskId,
            @ApiParam(value = "auditRuslt", required = true) @RequestParam(value = "auditRuslt", required = true) String auditResult) {
        vacationService.passAudit(userId,taskId,auditResult);
        return responseTemplate.format(Boolean.TRUE);
    }

    @ApiOperation(value = "my vac record", notes = "my vac record")
    @RequestMapping(value = "/my/vac/record", method = RequestMethod.GET)
    public Response<List<VacationPO>> myVacRecord(
            @ApiParam(value = "userId", required = true) @RequestParam(value = "userId", required = true) String userId) {
        return responseTemplate.format(vacationService.myVacRecord(userId));
    }

    @ApiOperation(value = "my audit record", notes = "my audit record")
    @RequestMapping(value = "/my/audit/record", method = RequestMethod.GET)
    public Response<List<VacationPO>> myAuditRecord(
            @ApiParam(value = "userId", required = true) @RequestParam(value = "userId", required = true) String userId) {
        return responseTemplate.format(vacationService.myAuditRecord(userId));
    }
}
