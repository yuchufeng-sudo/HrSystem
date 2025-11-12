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
import com.ys.hr.domain.HrCandidateInfo;
import com.ys.hr.domain.HrEmployees;
import com.ys.hr.domain.HrPosition;
import com.ys.hr.domain.HrQuota;
import com.ys.hr.service.IHrEmployeesService;
import com.ys.hr.service.IHrPositionService;
import com.ys.hr.service.IHrQuotaService;
import com.ys.hr.service.ITbCandidateInfoService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Position Management Controller
 *
 * @author ys
 * @date 2025-06-23
 */
@RestController
@RequestMapping("/position")
public class HrPositionController extends BaseController
{
    @Autowired
    private IHrPositionService hrPositionService;

    @Autowired
    private ITbCandidateInfoService tbCandidateInfoService;

    @Autowired
    private IHrQuotaService hrQuotaService;

    @Autowired
    private IHrEmployeesService employeesService;

    /**
     * Query Position Management list
     */
    @RequiresPermissions("hr:position:list")
    @GetMapping("/list")
    public TableDataInfo list(HrPosition hrPosition)
    {
        startPage();
        hrPosition.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        List<HrPosition> list = hrPositionService.selectHrPositionList(hrPosition);
        if(ObjectUtils.isNotEmpty(hrPosition.getFlag())){
            List<HrPosition> listTarger = new ArrayList<>();
            for (HrPosition position : list) {
                    HrCandidateInfo tbCandidateInfo = new HrCandidateInfo();
                    tbCandidateInfo.setJobInformation(position.getId());
                    tbCandidateInfo.setCandidateStatus("3");
                HrQuota hrQuota = new HrQuota();
                hrQuota.setPostId(position.getId());
                List<HrQuota> hrQuotas = hrQuotaService.selectHrQuotaList(hrQuota);
                if(!hrQuotas.isEmpty()){
                    List<HrCandidateInfo> hrCandidateInfos = tbCandidateInfoService.selectTbCandidateInfoList(tbCandidateInfo);
                    if(!(hrCandidateInfos.size()>=hrQuotas.get(0).getQuotaNumber())){
                        listTarger.add(position);
                    }
                }else{
                    listTarger.add(position);
                }
            }
            return getDataTable(listTarger);
        }
        return getDataTable(list);
    }

    @GetMapping("/qute/list")
    public AjaxResult quteList(HrPosition hrPosition)
    {
        hrPosition.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        List<HrPosition> list = hrPositionService.selectHrPositionList(hrPosition);
        List<HrPosition> listTarger = new ArrayList<>();
            for (HrPosition position : list) {
                HrCandidateInfo tbCandidateInfo = new HrCandidateInfo();
                tbCandidateInfo.setJobInformation(position.getId());
                tbCandidateInfo.setCandidateStatus("3");
                HrQuota hrQuota = new HrQuota();
                hrQuota.setPostId(position.getId());
                List<HrQuota> hrQuotas = hrQuotaService.selectHrQuotaList(hrQuota);
                if(hrQuotas.isEmpty()){
                    listTarger.add(position);
                }
            }
        return AjaxResult.success(listTarger);
    }

    /**
     * Export Position Management list
     */
    @RequiresPermissions("hr:position:export")
    @Log(title = "Position Management", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrPosition hrPosition)
    {
        hrPosition.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        List<HrPosition> list = hrPositionService.selectHrPositionList(hrPosition);
        ExcelUtil<HrPosition> util = new ExcelUtil<HrPosition>(HrPosition.class);
        util.exportExcel(response, list, "Position Management Data");
    }

    /**
     * Get Position Management details
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(hrPositionService.selectHrPositionById(id));
    }

    /**
     * Add Position Management
     */
    @RequiresPermissions("hr:position:add")
    @Log(title = "Position Management", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrPosition hrPosition) {
        hrPosition.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        return toAjax(hrPositionService.insertHrPosition(hrPosition));
    }

    /**
     * Update Position Management
     */
    @RequiresPermissions("hr:position:edit")
    @Log(title = "Position Management", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrPosition hrPosition) {
        return toAjax(hrPositionService.updateHrPosition(hrPosition));
    }

    /**
     * Delete Position Management
     */
    @RequiresPermissions("hr:position:remove")
    @Log(title = "Position Management", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        for (Long id : ids) {
            HrPosition hrPosition = hrPositionService.selectHrPositionById(id);
            QueryWrapper<HrEmployees> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("position",id.toString());
            long count = employeesService.count(queryWrapper);
            if (count!=0){
                return AjaxResult.error(hrPosition.getPositionName()+"in use, unable to delete");
            }
            QueryWrapper<HrCandidateInfo> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("job_information",id);
            long count1 = tbCandidateInfoService.count(queryWrapper1);
            if (count1!=0){
                return AjaxResult.error(hrPosition.getPositionName()+"in use, unable to delete");
            }
            QueryWrapper<HrQuota> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("post_id",id);
            long count2 = hrQuotaService.count(queryWrapper2);
            if (count2!=0){
                return AjaxResult.error(hrPosition.getPositionName()+"in use, unable to delete");
            }
        }
        return toAjax(hrPositionService.removeByIds(Arrays.asList(ids)));
    }
}
