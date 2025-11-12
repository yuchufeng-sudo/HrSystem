package com.ys.hr.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ys.common.core.aspect.RateLimit;
import com.ys.common.core.utils.DateUtils;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.*;
import com.ys.hr.service.*;
import com.ys.hr.sign.entiry.ResponseEntity;
import com.ys.hr.sign.entiry.SignType;
import com.ys.hr.sign.entiry.SignVo;
import com.ys.hr.sign.factory.SignApiStrategyFactory;
import com.ys.hr.sign.strategy.SignApiStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Slf4j
@RestController
@RequestMapping("/signContract")
public class SignContractController extends BaseController {

    @Autowired
    private IHrContractTemplateService hrContractTemplateService;

    @Autowired
    private IHrEmployeeContractService hrEmployeeContractService;

    @Resource
    private SignApiStrategyFactory signApiStrategyFactory;

    @Resource
    private IHrSignConfigService hrSignConfigService;

    @Autowired
    private IHrPositionService hrPositionService;

    @Autowired
    private IHrEmployeesService hrEmployeesService;

    @Autowired
    private IHrDocumentService hrDocumentService;

    @GetMapping("/get/{id}")
    public AjaxResult get(@PathVariable Long id){
        HrEmployeeContract employeeContract = new HrEmployeeContract();
        employeeContract.setBUserId(id);
        employeeContract.setCompanyId(SecurityUtils.getUserEnterpriseId());
        List<HrEmployeeContract> hrEmployeeContracts =
                hrEmployeeContractService.selectTbEmpContracts(employeeContract);
        if (hrEmployeeContracts.isEmpty()){
            return AjaxResult.success("Query Success",null);
        }else {
            HrEmployeeContract employeeContract1 = hrEmployeeContracts.get(0);
            return AjaxResult.success(employeeContract1);
        }
    }

    /**
     * QUERY  LIST
     * @param hrEmployeeContract
     * @return
     */
    @GetMapping("/getList")
    public AjaxResult getList(HrEmployeeContract hrEmployeeContract){
        hrEmployeeContract.setCompanyId(SecurityUtils.getUserEnterpriseId());
        List<HrEmployeeContract> hrEmployeeContracts =
                hrEmployeeContractService.selectTbEmpContracts(hrEmployeeContract);
        return AjaxResult.success(hrEmployeeContracts);
    }


    /**
     *
     * @return
     */
    @GetMapping("/templateList")
    public AjaxResult templateList(){
        HrContractTemplate contractTemplate = new HrContractTemplate();
        contractTemplate.setStatus("0");
        contractTemplate.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        List<HrContractTemplate> templates = hrContractTemplateService.selectHrContractTemplateList(contractTemplate);
        return AjaxResult.success(templates);
    }

    /**
     *
     * @return
     */
    @PostMapping(value = "/getTemplate")
    public AjaxResult getTemplate(@RequestBody HrEmployeeContract employee) {
        HrContractTemplate coTemplateManagement = hrContractTemplateService.getById(employee.getTemplateId());
        String templateContent = coTemplateManagement.getTemplateContent();
        coTemplateManagement.setTemplateContent(hrEmployeeContractService.replaceContent(employee,templateContent));
        return AjaxResult.success(coTemplateManagement);
    }

    /**
     * SendContract
     */
    @PostMapping("/sendLetters")
    @RateLimit(limit = 5, prefix = "sign_contract:send_letters")
    public AjaxResult sendLetters(@RequestBody HrEmployeeContract empContract) {
        empContract.setAUserId(SecurityUtils.getUserId());
        empContract.setCompanyId(SecurityUtils.getUserEnterpriseId());
        empContract.setBUserId(Long.valueOf(empContract.getEmpId()));
        empContract.setContractType("1");
        empContract.setCreateTime(DateUtils.getNowDate());
        int i = hrEmployeeContractService.eSign(empContract);
        HrEmployees employees = new HrEmployees();
        employees.setEmployeeId(empContract.getUserId());
        employees.setPosition(empContract.getPostId());
        hrEmployeesService.updateById(employees);
        hrEmployeeContractService.renewalContract(empContract);
        return toAjax(i);
    }

    @PostMapping("/paperContract")
    public AjaxResult paperContract(@RequestBody HrEmployeeContract empContract){
        empContract.setAUserId(SecurityUtils.getUserId());
        empContract.setCompanyId(SecurityUtils.getUserEnterpriseId());
        empContract.setBUserId(Long.valueOf(empContract.getEmpId()));
        empContract.setUserSign("1");
        empContract.setEnterpriseSign("1");
        empContract.setSignStatu("2");
        empContract.setCreateTime(DateUtils.getNowDate());
        boolean save = hrEmployeeContractService.save(empContract);
        hrEmployeeContractService.renewalContract(empContract);
        HrEmployees employees = new HrEmployees();
        employees.setEmployeeId(empContract.getUserId());
        employees.setPosition(empContract.getPostId());
        hrEmployeesService.updateEmployees(employees);
        hrDocumentService.saveContranctFile(empContract);
        return AjaxResult.success(save);
    }

    /**
     *
     * @param request
     */
    @PostMapping("/notifyUrl")
    public String notifyUrl(HttpServletRequest request) {
        String contractNo = request.getParameter("contractNo");
        String status = request.getParameter("status");
        log.debug("Electronic Signature Contract Callback after signing completion：contractNo:{}, status:{}", contractNo, status);
        hrEmployeeContractService.singSuccess(contractNo, status);
        return "ok";
    }

    /**
     *
     * @param request
     */
    @PostMapping("/userNotifyUrl")
    public String userNotifyUrl(HttpServletRequest request) {
        String contractNo = request.getParameter("contractNo");
        String status = request.getParameter("status");
        log.debug("Electronic Signature USER Callback upon completion of signing：contractNo:{}, status:{}", contractNo, status);
        hrEmployeeContractService.userSign(contractNo, status);
        return "ok";
    }

    /**
     *
     * @param userId
     * @return
     */
    @GetMapping("/hasSign/{userId}")
    public AjaxResult hasSign(@PathVariable("userId") Long userId){
        HrEmployeeContract employeeContract = new HrEmployeeContract();
        employeeContract.setBUserId(userId);
        employeeContract.setCompanyId(SecurityUtils.getUserEnterpriseId());
        List<HrEmployeeContract> hrEmployeeContracts =
                hrEmployeeContractService.selectTbEmpContracts(employeeContract);
        Map<String, Object> data = new HashMap<>();
        if (hrEmployeeContracts.isEmpty()){
            data.put("hasSign", true);
            return AjaxResult.success(data);
        }else {
            data.put("hasSign", false);
            data.put("contract", hrEmployeeContracts.get(0));
            return AjaxResult.success(data);
        }
    }

    /**
     * MODIFYStatus
     * @param employeeContract
     * @return
     */
    @PostMapping("/updateStatus")
    public AjaxResult updateStatus(@RequestBody HrEmployeeContract employeeContract){
        employeeContract.setCompanyId(SecurityUtils.getUserEnterpriseId());
        employeeContract.setBUserId(Long.parseLong(employeeContract.getUserId().toString()));
        employeeContract.setUserId(null);
        List<HrEmployeeContract> hrEmployeeContracts =
                hrEmployeeContractService.selectTbEmpContracts(employeeContract);
        if (hrEmployeeContracts.isEmpty()){
            return AjaxResult.warn("No electronic signing information");
        }
        HrEmployeeContract employeeContract1 = hrEmployeeContracts.get(0);
        SignType type = SignType.fromCode(employeeContract1.getPlatformType());
        SignApiStrategy<Object, Object> strategy = signApiStrategyFactory.createStrategy(type);
        HrSignConfig config = hrSignConfigService.selectConfigInfo(employeeContract1.getSignaturePlatformId());
        SignVo signVo = new SignVo();
        signVo.setEmployeeContract(employeeContract1);
        signVo.setUrl(config.getApiUrl());
        signVo.setConfig(JSON.parseObject(config.getSignConfig()));
        ResponseEntity<Object> responseEntity = strategy.getSignDetail(signVo);
        if (responseEntity.getStatusCode() != 200){
            return AjaxResult.error(responseEntity.getErrorMessage());
        }
        JSONObject jsonObject = JSONObject.parseObject(responseEntity.getBody().toString());
        employeeContract.setId(employeeContract1.getId());
        employeeContract.setSignUrl2(jsonObject.getString("companyUrl"));
        employeeContract.setEnterpriseSign(jsonObject.getString("enterpriseSign"));
        employeeContract.setSignUrl1(jsonObject.getString("userUrl"));
        employeeContract.setUserSign(jsonObject.getString("userSign"));
        if ("1".equals(employeeContract.getEnterpriseSign()) && "1".equals(employeeContract.getUserSign())){
            employeeContract.setSignStatu("2");
        }
        return toAjax(hrEmployeeContractService.updateById(employeeContract));
    }

    @GetMapping("/getPositionList")
    public AjaxResult getPositionList(){
        HrPosition position = new HrPosition();
        position.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        List<HrPosition> hrPositions = hrPositionService.selectHrPositionList(position);
        return AjaxResult.success(hrPositions);
    }

    @GetMapping("/getPlatformList")
    public AjaxResult getPlatformList(){
        HrSignConfig hrSignConfig = new HrSignConfig();
        hrSignConfig.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        List<HrSignConfig> hrSignConfigs = hrSignConfigService.selectHrSignConfigList(hrSignConfig);
        return AjaxResult.success(hrSignConfigs);
    }
}
