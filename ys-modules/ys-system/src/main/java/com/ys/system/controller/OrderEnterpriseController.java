package com.ys.system.controller;

import com.ys.common.core.utils.bean.BeanUtils;
import com.ys.common.core.web.controller.BaseController;
import com.ys.system.api.domain.SysEnterprise;
import com.ys.system.domain.vo.EnterpriseVo;
import com.ys.system.service.ISysEnterpriseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author：hzz
 * @Date ：2025/6/23 14:07
 */
@RestController
@RequestMapping("/enterpriseOrder")
public class OrderEnterpriseController extends BaseController {

    @Autowired
    private ISysEnterpriseService enterpriseService;

    @GetMapping("/{id}")
    public EnterpriseVo getEnterpriseById(@PathVariable("id") String id) {
        SysEnterprise enterprise = enterpriseService.selectSysEnterpriseById(id);
        EnterpriseVo enterpriseVo = new EnterpriseVo();
        BeanUtils.copyProperties(enterprise, enterpriseVo);
        return enterpriseVo;
    }

    @PutMapping("/{id}")
    public int updateEnterprise(@PathVariable("id") String id, @RequestBody EnterpriseVo enterpriseVo) {
        SysEnterprise enterprise = new SysEnterprise();
        BeanUtils.copyProperties(enterpriseVo, enterprise);
        return enterpriseService.updateSysEnterprise(enterprise);
    }
}
