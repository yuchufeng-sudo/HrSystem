package com.ys.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ys.common.core.utils.DateUtils;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.common.security.annotation.RequiresPermissions;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.system.domain.SysNotice;
import com.ys.system.domain.SysNoticeRead;
import com.ys.system.service.ISysNoticeReadService;
import com.ys.system.service.ISysNoticeService;
import com.ys.system.service.ISysNoticeUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 *  ANNOUNCEMENT  INFORMATIONOPERATION
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/notice")
public class SysNoticeController extends BaseController
{
    @Autowired
    private ISysNoticeService noticeService;
    @Autowired
    private ISysNoticeReadService noticeReadService;


    /**
     * OBTAIN  Notice  ANNOUNCEMENT  LIST
     */
    @RequiresPermissions("system:notice:list")
    @GetMapping("/list")
    public TableDataInfo list(SysNotice notice)
    {
        startPage();
        Set<String> roles = SecurityUtils.getLoginUser().getRoles();
        notice.setUserId(SecurityUtils.getUserId());
        if (!roles.contains("hr")&&!roles.contains("admin")) {
            notice.setUid(SecurityUtils.getUserId());
        }
        notice.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        List<SysNotice> list = noticeService.selectNoticeList(notice);
        return getDataTable(list);
    }

    /**
     * According to  Notice  ANNOUNCEMENT IDOBTAIN DETAILEDLY INFORMATION
     */
    @GetMapping(value = "/{noticeId}")
    public AjaxResult getInfo(@PathVariable Long noticeId)
    {
        return success(noticeService.selectNoticeById(noticeId));
    }

    /**
     * ADD Notice  ANNOUNCEMENT
     */
    @PostMapping("/read/{noticeId}")
    public AjaxResult read(@PathVariable Long noticeId)
    {
        Long userId = SecurityUtils.getUserId();
        SysNoticeRead sysNoticeRead = new SysNoticeRead();
        sysNoticeRead.setIsRead("1");
        sysNoticeRead.setUserId(userId);
        sysNoticeRead.setNoticeId(noticeId);
        sysNoticeRead.setIsLiked("0");
        QueryWrapper<SysNoticeRead> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("notice_id", noticeId);
        queryWrapper.eq("user_id", userId);
        return toAjax(noticeReadService.saveOrUpdate(sysNoticeRead,queryWrapper));
    }

    @PostMapping("/liked/{noticeId}")
    public AjaxResult liked(@PathVariable Long noticeId,@RequestBody String isLiked)
    {
        Long userId = SecurityUtils.getUserId();
        SysNoticeRead sysNoticeRead = new SysNoticeRead();
        sysNoticeRead.setUserId(userId);
        sysNoticeRead.setNoticeId(noticeId);
        sysNoticeRead.setIsLiked(isLiked);
        sysNoticeRead.setIsRead("0");
        if (isLiked.equals("1")){
            sysNoticeRead.setLikeTime(DateUtils.getNowDate());
        }
        QueryWrapper<SysNoticeRead> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("notice_id", noticeId);
        queryWrapper.eq("user_id", userId);
        return toAjax(noticeReadService.saveOrUpdate(sysNoticeRead,queryWrapper));
    }

    /**
     * ADD Notice  ANNOUNCEMENT
     */
    @RequiresPermissions("system:notice:add")
    @Log(title = " Notice ANNOUNCEMENT", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysNotice notice)
    {
        notice.setCreateBy(SecurityUtils.getUsername());
        notice.setCreateTime(DateUtils.getNowDate());
        notice.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        if (notice.getNoticeId()!=null) {
            noticeService.deleteNoticeById(notice.getNoticeId());
            notice.setNoticeId(null);
        }
        return toAjax(noticeService.insertNotice(notice));
    }

    @RequiresPermissions("system:notice:add")
    @GetMapping("getDraft")
    public AjaxResult getDraft()
    {
        QueryWrapper<SysNotice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("create_by",SecurityUtils.getUserId().toString());
        queryWrapper.eq("status","2");
        List<SysNotice> list = noticeService.list(queryWrapper);
        if (!list.isEmpty()){
            return AjaxResult.success(list.get(0));
        }
        return AjaxResult.success();
    }

    @RequiresPermissions("system:notice:add")
    @Log(title = " Notice  ANNOUNCEMENT", businessType = BusinessType.UPDATE)
    @PutMapping("saveDraft")
    public AjaxResult saveDraft(@Validated @RequestBody SysNotice notice)
    {
        QueryWrapper<SysNotice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("create_by",SecurityUtils.getUserId().toString());
        queryWrapper.eq("status","2");
        notice.setCreateBy(SecurityUtils.getUserId().toString());
        notice.setStatus("2");
        return toAjax(noticeService.saveOrUpdate(notice,queryWrapper));
    }

    /**
     * MODIFY Notice  ANNOUNCEMENT
     */
    @RequiresPermissions("system:notice:add")
    @Log(title = " Notice  ANNOUNCEMENT", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysNotice notice)
    {
        notice.setUpdateBy(SecurityUtils.getUsername());
        notice.setUpdateTime(DateUtils.getNowDate());
        return toAjax(noticeService.updateNotice(notice));
    }

    /**
     * DELETE Notice  ANNOUNCEMENT
     */
    @RequiresPermissions("system:notice:remove")
    @Log(title = " Notice  ANNOUNCEMENT", businessType = BusinessType.DELETE)
    @DeleteMapping("/{noticeIds}")
    public AjaxResult remove(@PathVariable Long[] noticeIds)
    {
        return toAjax(noticeService.deleteNoticeByIds(noticeIds));
    }

    /**
     * QUERYUSER
     * @return
     */
    @GetMapping("/getUserNotice")
    public TableDataInfo getUserNotice(SysNotice notice){
        notice.setUserId(SecurityUtils.getUserId());
        startPage();
        List<SysNotice> list = noticeService.getUserNotice(notice);
        return getDataTable(list);
    }
}
