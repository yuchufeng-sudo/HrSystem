package com.ys.hr.controller;

import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.vo.CelebrationVo;
import com.ys.hr.service.IHrEmployeesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * User Dashboard
 */
@RestController
@RequestMapping("/userDashboard")
public class HrUserDashboardController {

    @Autowired
    private IHrEmployeesService employeesService;

    /**
     * Celebration
     * @return
     */
    @GetMapping("/getCelebration")
    public AjaxResult getCelebration(){
        Long userId = SecurityUtils.getUserId();
        List<CelebrationVo> list = employeesService.selectCelebrationList(userId);
        return AjaxResult.success(list);
    }
}
