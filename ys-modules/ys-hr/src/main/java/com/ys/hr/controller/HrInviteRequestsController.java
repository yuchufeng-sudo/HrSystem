package com.ys.hr.controller;

import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.common.security.annotation.RequiresPermissions;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.HrInviteCode;
import com.ys.hr.domain.HrInviteRequests;
import com.ys.hr.service.IHrInviteRequestsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 *  INVITATION LINK REQUEST HANDLING Controller
 *
 * @author ys
 * @date 2025-05-21
 */
@RestController
@RequestMapping("/requests")
public class HrInviteRequestsController extends BaseController
{
    @Autowired
    private IHrInviteRequestsService hrInviteRequestsService;

    /**
     * QUERY INVITATION LINK REQUEST HANDLING   LIST
     */
    @RequiresPermissions("hr:employees:list")
    @GetMapping("/list")
    public TableDataInfo list(HrInviteRequests hrInviteRequests)
    {
        startPage();
        hrInviteRequests.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        List<HrInviteRequests> list = hrInviteRequestsService.selectHrInviteRequestsList(hrInviteRequests);
        return getDataTable(list);
    }

    /**
     * ADD INVITATION LINK REQUEST HANDLING
     */
    @Log(title = " INVITATION LINK REQUEST HANDLING ", businessType = BusinessType.INSERT)
    @PostMapping("")
    public AjaxResult add(@RequestBody HrInviteRequests hrInviteRequests) {
        hrInviteRequests.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        return toAjax(hrInviteRequestsService.save(hrInviteRequests));
    }

    @PostMapping("/invite")
    public AjaxResult invite(@RequestBody HrInviteRequests hrInviteRequests) {
        String inviteCode = hrInviteRequests.getInviteCode();
        if (inviteCode==null){
            return AjaxResult.error("Invitation does not exist");
        }
        HrInviteCode inviteCode1 = hrInviteRequestsService.selectInviteCodeStatus(inviteCode);
        if (inviteCode1==null) {
            return AjaxResult.error("Invitation does not exist");
        } else if(inviteCode1.getStatus()==1){
            return AjaxResult.error("The invitation has been submitted and cannot be resubmitted");
        }else {
            hrInviteRequestsService.updateInviteCodeStatus(inviteCode);
            hrInviteRequests.setEnterpriseId(inviteCode1.getEnterpriseId());
            return toAjax(hrInviteRequestsService.save(hrInviteRequests));
        }
    }

    /**
     * By  Request
     */
    @RequiresPermissions("hr:employees:list")
    @Log(title = " INVITATION LINK REQUEST HANDLING ", businessType = BusinessType.DELETE)
    @PostMapping("/accept")
    public AjaxResult accept(@RequestBody List<HrInviteRequests> hrInviteRequests) {
        return toAjax(hrInviteRequestsService.acceptHrInviteRequests(hrInviteRequests));
    }

    @RequiresPermissions("hr:employees:list")
    @Log(title = " INVITATION LINK REQUEST HANDLING ", businessType = BusinessType.DELETE)
    @PostMapping("/decline")
    public AjaxResult decline(@RequestBody Long[] requestIds) {
        return toAjax(hrInviteRequestsService.declineHrInviteRequests(requestIds));
    }

    /**
     * DELETE INVITATION LINK REQUEST HANDLING
     */
    @RequiresPermissions("hr:employees:list")
    @Log(title = " INVITATION LINK REQUEST HANDLING ", businessType = BusinessType.DELETE)
    @DeleteMapping("/{requestIds}")
    public AjaxResult remove(@PathVariable Long[] requestIds) {
        return toAjax(hrInviteRequestsService.removeByIds(Arrays.asList(requestIds)));
    }

    @GetMapping("/getEid")
    public AjaxResult getEid()
    {
        return AjaxResult.success(SecurityUtils.getUserEnterpriseId());
    }

    @GetMapping("getCode")
    public AjaxResult getCode(){
        String userEnterpriseId = SecurityUtils.getUserEnterpriseId();
        return AjaxResult.success(hrInviteRequestsService.getInviteCode(userEnterpriseId));
    }
}
