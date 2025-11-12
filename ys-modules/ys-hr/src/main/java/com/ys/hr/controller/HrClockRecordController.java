package com.ys.hr.controller;

import com.ys.common.core.utils.DateUtils;
import com.ys.common.core.utils.poi.ExcelUtil;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.HrClockRecord;
import com.ys.hr.mqtt.MQTTConnect;
import com.ys.hr.service.IHrAttendanceDeviceInfoService;
import com.ys.hr.service.IHrClockRecordService;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 *  Clock in RecordController
 *
 * @author ys
 * @date 2024-11-29
 */
@RestController
@RequestMapping("/clockRecord")
public class HrClockRecordController extends BaseController
{

    @Resource
    private MQTTConnect mqttConnect;

    @Autowired
    private IHrAttendanceDeviceInfoService hrAttendanceDeviceInfoService;

    @Autowired
    private IHrClockRecordService hrClockRecordService;

    /**
     * Query the Clock in Record list
     */
    @GetMapping("/list")
    public TableDataInfo list(HrClockRecord HrClockRecord)
    {
        HrClockRecord.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        startPage();
        List<HrClockRecord> list = hrClockRecordService.selectHrClockRecordList(HrClockRecord);
        return getDataTable(list);
    }

    /**
     * Export the Clock in Record list
     */
    @Log(title = " Clock in Record", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrClockRecord HrClockRecord)
    {
        List<HrClockRecord> list = hrClockRecordService.selectHrClockRecordList(HrClockRecord);
        ExcelUtil<HrClockRecord> util = new ExcelUtil<HrClockRecord>(HrClockRecord.class);
        util.exportExcel(response, list, " Clock-in Record data");
    }

    /**
     * Get Clock in Record Detailed Information
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id) {
        return AjaxResult.success(hrClockRecordService.getById(id));
    }

    /**
     * Add  Clock in Record
     */
    @Log(title = " Clock in Record", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody HrClockRecord HrClockRecord) {
        return toAjax(hrClockRecordService.save(HrClockRecord));
    }

    /**
     * Modify  Clock in Record
     */
    @Log(title = " Clock in Record", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrClockRecord HrClockRecord) {
        return toAjax(hrClockRecordService.updateById(HrClockRecord));
    }

    /**
     *  Clock in Record
     */
    @Log(title = " Clock in Record", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids) {
        return toAjax(hrClockRecordService.removeByIds(Arrays.asList(ids)));
    }
    @PostMapping("/replace")
    public AjaxResult replace() throws MqttException {
        List<String> deviceSns = hrAttendanceDeviceInfoService.selectAllSn();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtils.getNowDate());
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        Date startTime = calendar.getTime();
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,59);
        Date endTime = calendar.getTime();

        for (String sn : deviceSns) {
            mqttConnect.getClockRecord(startTime, endTime, sn);
            System.out.println("replace: " + sn);
        }
        return AjaxResult.success();
    }

    /*
    *
    * */
    @PostMapping("/openDoor")
    public AjaxResult openDoor(@RequestBody Map<String, String> payload) throws MqttException {
        String sn = payload.get("sn");
        mqttConnect.openDoor(sn);
        return AjaxResult.success();
    }

    /*
     * Take a Photo
     * */
    @PostMapping("/photograph")
    public AjaxResult photograph(@RequestBody Map<String, String> payload) throws MqttException {
        String sn = payload.get("sn");
        mqttConnect.photograph(sn);
        return AjaxResult.success();
    }
}
