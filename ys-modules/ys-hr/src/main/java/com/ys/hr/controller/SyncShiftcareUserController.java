package com.ys.hr.controller;

import com.ys.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.common.core.web.controller.BaseController;
import com.ys.sync.domain.SyncShiftcareUser;
import com.ys.sync.service.ISyncShiftcareUserService;
import org.springframework.validation.annotation.Validated;

/**
 * sync shiftcare user Controller
 *
 * @author ys
 * @date 2025-10-30
 */
@RestController
@RequestMapping("/shiftcareUser")
public class SyncShiftcareUserController extends BaseController
{
    @Autowired
    private ISyncShiftcareUserService syncShiftcareUserService;

    /**
     * Get sync shiftcare user details
     */
    @GetMapping()
    public AjaxResult getInfo() {
        return success(syncShiftcareUserService.selectSyncShiftcareUserByUserId(SecurityUtils.getUserId()));
    }

    /**
     * Add sync shiftcare user
     */
    @Log(title = "sync shiftcare user", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SyncShiftcareUser syncShiftcareUser) {
        syncShiftcareUser.setUserId(SecurityUtils.getUserId());
        return toAjax(syncShiftcareUserService.insertSyncShiftcareUser(syncShiftcareUser));
    }

    /**
     * Update sync shiftcare user
     */
    @Log(title = "sync shiftcare user", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SyncShiftcareUser syncShiftcareUser) {
        return toAjax(syncShiftcareUserService.updateSyncShiftcareUser(syncShiftcareUser));
    }
}
