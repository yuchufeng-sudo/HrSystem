package com.ys.hr.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ys.common.core.utils.poi.ExcelUtil;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.common.security.annotation.RequiresPermissions;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.HrJob;
import com.ys.hr.domain.HrPosition;
import com.ys.hr.service.IHrJobService;
import com.ys.hr.service.IHrPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Job Management Controller
 *
 * @author ys
 * @date 2025-06-24
 */
@RestController
@RequestMapping("/job")
public class HrJobController extends BaseController
{
    @Autowired
    private IHrJobService hrJobService;

    /**
     * Query Job Management list
     */
    @RequiresPermissions("hr:job:list")
    @GetMapping("/list")
    public TableDataInfo list(HrJob hrJob)
    {
        startPage();
        hrJob.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        List<HrJob> list = hrJobService.selectHrJobList(hrJob);
        return getDataTable(list);
    }

    /**
     * Export Job Management list
     */
    @RequiresPermissions("hr:job:export")
    @Log(title = "Job Management", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrJob hrJob)
    {
        hrJob.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        List<HrJob> list = hrJobService.selectHrJobList(hrJob);
        ExcelUtil<HrJob> util = new ExcelUtil<HrJob>(HrJob.class);
        util.exportExcel(response, list, "Job Management Data");
    }

    /**
     * Get Job Management details
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(hrJobService.selectHrJobById(id));
    }

    /**
     * Add Job Management
     */
    @RequiresPermissions("hr:job:add")
    @Log(title = "Job Management", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrJob hrJob) {
        hrJob.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        return toAjax(hrJobService.insertHrJob(hrJob));
    }

    /**
     * Update Job Management
     */
    @RequiresPermissions("hr:job:edit")
    @Log(title = "Job Management", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrJob hrJob) {
        return toAjax(hrJobService.updateHrJob(hrJob));
    }

    @Resource
    private IHrPositionService hrPositionService;

    /**
     * Delete Job Management
     */
    @RequiresPermissions("hr:job:remove")
    @Log(title = "Job Management", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        for (Long id : ids) {
            QueryWrapper<HrPosition> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("job_id",id);
            long count = hrPositionService.count(queryWrapper);
            if (count>0) {
                HrJob hrJob = hrJobService.selectHrJobById(id);
                return AjaxResult.error(hrJob.getJobTitle()+" Already assigned, unable to delete");
            }
        }
        return toAjax(hrJobService.deleteHrJobByIds(ids));
    }
}
