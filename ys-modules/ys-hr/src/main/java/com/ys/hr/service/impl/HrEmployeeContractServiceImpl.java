package com.ys.hr.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.common.core.constant.SecurityConstants;
import com.ys.common.core.exception.ServiceException;
import com.ys.common.core.utils.DateUtils;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.*;
import com.ys.hr.mapper.*;
import com.ys.hr.service.IHrEmployeeContractService;
import com.ys.hr.service.IHrSignConfigService;
import com.ys.hr.sign.entiry.ResponseEntity;
import com.ys.hr.sign.entiry.SignType;
import com.ys.hr.sign.entiry.SignVo;
import com.ys.hr.sign.factory.SignApiStrategyFactory;
import com.ys.hr.sign.strategy.SignApiStrategy;
import com.ys.system.api.RemoteMessageService;
import com.ys.system.api.RemoteUserService;
import com.ys.system.api.domain.SysMessage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.*;

/**
 *
 *
 * @author collection
 * @date 2023-02-26
 */
@Service
public class HrEmployeeContractServiceImpl extends ServiceImpl<HrEmployeeContractMapper, HrEmployeeContract>
        implements IHrEmployeeContractService {
    @Resource
    private HrEmployeeContractMapper hrEmployeeContractMapper;

    @Resource
    private HrContractTemplateMapper hrContractTemplateMapper;

    @Resource
    private HrSignConfigMapper hrSignConfigMapper;

    @Resource
    private SignApiStrategyFactory signApiStrategyFactory;

    @Resource
    private HrMaturityContractMapper hrMaturityContractMapper;

    @Resource
    private RemoteMessageService remoteMessageService;

    @Resource
    private HrEmployeesMapper employeesMapper;

    @Resource
    private HrMaturityContractMapper maturityContractMapper;

    @Resource
    private HrRenewalContractMapper renewalContractMapper;

    @Resource
    private IHrSignConfigService hrSignConfigService;

    /**
     * Query Employee Contract list
     *
     *
     * @param employeeContract Employee Contract
     * @return Employee Contract
     */
    @Override
    public List<HrEmployeeContract> selectTbEmpContractList(HrEmployeeContract employeeContract) {
        return baseMapper.selectTbEmpContractList(employeeContract);
    }

    /**
     * Query Employee Contract list
     *
     *
     * @param tbEmpContract Employee Contract
     * @return Employee Contract
     */
    @Override
    public List<HrEmployeeContract> selectTbEmpContractList1(HrEmployeeContract tbEmpContract) {
        return baseMapper.selectTbEmpContractList1(tbEmpContract);
    }

    /**
     *
     *
     * @param employeeContract
     * @return
     */
    @Override
    public List<HrEmployeeContract> selectTbEmpContracts(HrEmployeeContract employeeContract) {
        return baseMapper.selectTbEmpContracts(employeeContract);
    }

    @Override
    public int eSign(HrEmployeeContract empContract) {
        HrSignConfig signConfig = hrSignConfigMapper.selectConfigInfo(empContract.getSignaturePlatformId());
        if (ObjectUtils.isEmpty(signConfig)) {
            throw new RuntimeException("The electronic signature platform is not yet integrated!");
        }
        HrContractTemplate contractTemplate = hrContractTemplateMapper
                .selectHrContractTemplateByTemplateId(empContract.getTemplateId());
        SignType type = SignType.fromCode(signConfig.getTypeValue());
        SignApiStrategy<Object, Object> strategy = signApiStrategyFactory.createStrategy(type);
        SignVo signVo = new SignVo(empContract.getTemplateContent(), JSONObject.parseObject(signConfig.getSignConfig()),
                signConfig.getApiUrl(), contractTemplate.getTemplateName(), empContract);
        ResponseEntity<Object> responseEntity = strategy.send(signVo);
        if (responseEntity.getStatusCode() != 200) {
            throw new RuntimeException(responseEntity.getErrorMessage());
        }
        JSONObject body = (JSONObject) responseEntity.getBody();
        empContract.setFileId(body.getString("contractNumber"));
        empContract.setPlatformType(signConfig.getTypeValue());
        if (empContract.getId() == null) {
            int insert = baseMapper.insert(empContract);
            return insert;
        } else {
            int i = baseMapper.updateById(empContract);
            return i;
        }
    }

    /**
     *
     *
     * @return
     */
    @Override
    public List<HrEmployeeContract> getExpiringContracts() {
        return baseMapper.selectExpiringContracts();
    }

    /**
     * Message Notice
     *
     * @param employeeContract
     */
    @Override
    public void messageNotification(HrEmployeeContract employeeContract) {
        HrEmployees hrEmployees = employeesMapper.selectById(employeeContract.getBUserId());
        HrMaturityContract contract = new HrMaturityContract();
        contract.setContractId(employeeContract.getId());
        contract.setEnterpriseId(employeeContract.getCompanyId());
        contract.setCreateTime(DateUtils.getNowDate());
        int insert = hrMaturityContractMapper.insert(contract);
        if (insert > 0) {
            HrEmployeeContract contract1 = new HrEmployeeContract();
            contract1.setId(employeeContract.getId());
            contract1.setIsNotify("2");
            contract1.setIsMaturity("2");
            hrEmployeeContractMapper.updateById(contract1);
            // Send Notice
            // Employee
            AjaxResult info = remoteMessageService.getInfo(hrEmployees.getUserId(), SecurityConstants.INNER);
            Map<String,String> setting = (Map<String, String>) info.get("data");
            String contractPeriodAlerts = setting.get("contractPeriodAlerts");
            if ("1".equals(contractPeriodAlerts)) {
                SysMessage sysMessage = new SysMessage();
                sysMessage.setMessageRecipient((hrEmployees.getUserId()));
                sysMessage.setMessageStatus("0");
                sysMessage.setMessageType(18);
                sysMessage.setCreateTime(DateUtils.getNowDate());
                Map<String, Object> map1 = new HashMap<>();
                Map<String, Object> map2 = new HashMap<>();
                map1.put("name", employeeContract.getCompanyName());
                sysMessage.setMap1(map1);
                sysMessage.setMap2(map2);
                remoteMessageService.sendMessageByTemplate(sysMessage, SecurityConstants.INNER);
            }

            AjaxResult info1 = remoteMessageService.getInfo(employeeContract.getAUserId(), SecurityConstants.INNER);
            Map<String,String> setting1 = (Map<String, String>) info1.get("data");
            String contractPeriodAlerts1 = setting1.get("contractPeriodAlerts");
            if ("1".equals(contractPeriodAlerts1)) {
                SysMessage sysMessage = new SysMessage();
                sysMessage.setMessageRecipient((employeeContract.getAUserId()));
                sysMessage.setMessageStatus("0");
                sysMessage.setMessageType(18);
                sysMessage.setCreateTime(DateUtils.getNowDate());
                Map<String, Object> map1 = new HashMap<>();
                Map<String, Object> map2 = new HashMap<>();
                map1.put("name", employeeContract.getName());
                sysMessage.setMap1(map1);
                sysMessage.setMap2(map2);
                remoteMessageService.sendMessageByTemplate(sysMessage, SecurityConstants.INNER);
            }
        }
    }

    @Override
    public void renewalContract(HrEmployeeContract empContract) {
        if (ObjectUtils.isNotEmpty(empContract.getMaturityContractId())) {
            HrMaturityContract contract1 = new HrMaturityContract();
            contract1.setId(empContract.getMaturityContractId());
            contract1.setIsRenewal("2");
            contract1.setUpdateTime(DateUtils.getNowDate());
            maturityContractMapper.updateById(contract1);
            HrRenewalContract hrRenewalContract = new HrRenewalContract();
            hrRenewalContract.setContractId(empContract.getId());
            hrRenewalContract.setEnterpriseId(empContract.getCompanyId());
            hrRenewalContract.setCreateTime(DateUtils.getNowDate());
            renewalContractMapper.insert(hrRenewalContract);
        }
    }

    @Override
    public boolean updateStatus(HrEmployeeContract employeeContract) {
        employeeContract.setCompanyId(SecurityUtils.getUserEnterpriseId());
        employeeContract.setBUserId(Long.parseLong(employeeContract.getUserId().toString()));
        employeeContract.setUserId(null);
        List<HrEmployeeContract> hrEmployeeContracts = selectTbEmpContracts(employeeContract);
        if (hrEmployeeContracts.isEmpty()){
            throw new ServiceException("No electronic signing information");
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
            throw new ServiceException(responseEntity.getErrorMessage());
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
        return updateById(employeeContract);
    }

    private final Random random = new Random();

    @Override
    public String replaceContent(HrEmployeeContract employee, String content) {
        int number = 10000000 + random.nextInt(90000000);
        employee.setId(String.valueOf(number));
        Field[] fields = HrEmployeeContract.class.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            f.setAccessible(true);
            try {
                content = content.replaceAll("\\$\\{" + f.getName() + "}",
                        ObjectUtils.isNotNull(f.get(employee)) ? f.get(employee).toString() : "");
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        // Replace time with today
        // content = content.replaceAll("\\$\\{today}",
        // DateUtils.parseDateToStr("YYYYMMdd", DateUtils.getNowDate()));
        return content;
    }

    /**
     * User Sign Contract
     *
     * @param contractNo
     * @param status
     */
    @Override
    public void userSign(String contractNo, String status) {
        if ("2".equals(status)) {
            HrEmployeeContract tbEmpContract = hrEmployeeContractMapper.selectByFileId(contractNo);
            if (!ObjectUtils.isEmpty(tbEmpContract)) {
                tbEmpContract.setUserSign("1");
                // Determine if the company has signed
                if ("1".equals(tbEmpContract.getEnterpriseSign())) {
                    tbEmpContract.setSignStatu("2");
                } else {
                    tbEmpContract.setSignStatu("1");
                }
                tbEmpContract.setUpdateTime(DateUtils.getNowDate());
                hrEmployeeContractMapper.updateById(tbEmpContract);
            }
        }
    }

    /**
     * Contract Signed Successfully
     *
     * @param contractNo
     * @param status
     */
    @Override
    public void singSuccess(String contractNo, String status) {
        if ("2".equals(status)) {
            HrEmployeeContract tbEmpContract = hrEmployeeContractMapper.selectByFileId(contractNo);
            if (!ObjectUtils.isEmpty(tbEmpContract)) {
                tbEmpContract.setEnterpriseSign("1");
                // Determine if the User has signed
                if ("1".equals(tbEmpContract.getUserSign())) {
                    tbEmpContract.setSignStatu("2");
                } else {
                    tbEmpContract.setSignStatu("1");
                }
                tbEmpContract.setUpdateTime(DateUtils.getNowDate());
                hrEmployeeContractMapper.updateById(tbEmpContract);
            }
        }
    }

    /**
     * Query Contract Information
     *
     * @param tbEmpContract
     * @return
     */
    @Override
    public HrEmployeeContract selectEmpContractInfo(HrEmployeeContract tbEmpContract) {
        return hrEmployeeContractMapper.selectEmpContractInfo(tbEmpContract);
    }

}
