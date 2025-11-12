package com.ys.hr.controller;

import com.ys.common.core.utils.poi.ExcelUtil;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.common.security.annotation.RequiresPermissions;
import com.ys.hr.domain.HrEmailTemplate;
import com.ys.hr.domain.HrEmployees;
import com.ys.hr.service.IHrEmailTemplateService;
import com.ys.hr.service.IHrEmployeesService;
import com.ys.utils.email.EmailUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Email Templates Controller
 *
 * @author ys
 * @date 2025-09-09
 */
@RestController
@RequestMapping("/template")
public class HrEmailTemplateController extends BaseController {
    @Autowired
    private IHrEmailTemplateService hrEmailTemplateService;

    @Autowired
    private IHrEmployeesService hrEmployeesService;

    @Autowired
    private EmailUtils emailUtils;
    /**
     * Query Email Templates list
     */
    @GetMapping("/list")
    public TableDataInfo list(HrEmailTemplate hrEmailTemplate) {
        startPage();
//        hrEmailTemplate.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        List<HrEmailTemplate> list = hrEmailTemplateService.selectHrEmailTemplateList(hrEmailTemplate);
        return getDataTable(list);
    }

    /**
     * Export Email Templates list
     */
    @RequiresPermissions("system:template:export")
    @Log(title = "Email Templates", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrEmailTemplate hrEmailTemplate) {
//        hrEmailTemplate.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        List<HrEmailTemplate> list = hrEmailTemplateService.selectHrEmailTemplateList(hrEmailTemplate);
        ExcelUtil<HrEmailTemplate> util = new ExcelUtil<HrEmailTemplate>(HrEmailTemplate.class);
        util.exportExcel(response, list, "Email Templates Data");
    }

    /**
     * Get Email Templates details
     */
    @GetMapping(value = "/{templateId}")
    public AjaxResult getInfo(@PathVariable("templateId") Long templateId) {
        return success(hrEmailTemplateService.selectHrEmailTemplateByTemplateId(templateId));
    }

    /**
     * Add Email Templates
     */
    @RequiresPermissions("system:template:add")
    @Log(title = "Email Templates", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrEmailTemplate hrEmailTemplate) {
//        hrEmailTemplate.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        return toAjax(hrEmailTemplateService.insertHrEmailTemplate(hrEmailTemplate));
    }

    /**
     * Update Email Templates
     */
    @RequiresPermissions("system:template:edit")
    @Log(title = "Email Templates", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrEmailTemplate hrEmailTemplate) {
        return toAjax(hrEmailTemplateService.updateHrEmailTemplate(hrEmailTemplate));
    }

    @PutMapping("/send")
    public AjaxResult sendEmail(@RequestBody HrEmailTemplate hrEmailTemplate) {
        HashMap<String, Object> map = new HashMap<>();
        if (ObjectUtils.isNotEmpty(hrEmailTemplate.getSendTo())) {
            ArrayList<File> files = convertToFileList(hrEmailTemplate.getAttachments());
            HrEmployees hrEmployees = new HrEmployees();
            hrEmployees.setEmail(hrEmailTemplate.getSendTo());
            String[] sendTo = new String[1];
            String[] sendCc = null;
            String[] sendBcc = null;
            List<HrEmployees> hrEmployees1 = hrEmployeesService.selectHrEmployeesList(hrEmployees);
            if (ObjectUtils.isNotEmpty(hrEmployees1)) {
                map.put("name", hrEmployees1.get(0).getFullName());
            } else {
                map.put("name", "No details yet");
            }
            sendTo[0] = hrEmailTemplate.getSendTo();
            emailUtils.sendEmailByEmailTemplate(map, sendTo, hrEmailTemplate.getTemplateBody(), map, hrEmailTemplate.getTemplateSubject(), sendCc, sendBcc, files);
        }
        return toAjax(1);
    }

    public static ArrayList<File> convertToFileList(List<String> attachments) {
        ArrayList<File> files = new ArrayList<>();

        if (attachments == null || attachments.isEmpty()) {
            return files;
        }

        for (String path : attachments) {
            try {
                File file;
                if (path.startsWith("http://") || path.startsWith("https://")) {
                    // URL 下载
                    file = downloadFileFromUrl(path);
                } else {
                    // 本地路径
                    file = new File(path);
                }

                if (file != null && file.exists()) {
                    files.add(file);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return files;
    }

    private static File downloadFileFromUrl(String urlPath) throws IOException {
        String encodedUrl = encodeUrl(urlPath);
        URL url = new URL(encodedUrl);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new IOException("Failed to download file, response code = " + responseCode);
        }

        String fileName = new File(url.getPath()).getName();
        File tempFile = File.createTempFile("download_", "_" + fileName);

        try (InputStream in = conn.getInputStream();
             FileOutputStream out = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[8192];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
        }
        return tempFile;
    }

    private static String encodeUrl(String urlPath) throws UnsupportedEncodingException {
        // 找到最后一个 / ，把前缀和文件名分开
        int lastSlash = urlPath.lastIndexOf("/");
        String prefix = urlPath.substring(0, lastSlash + 1);
        String fileName = urlPath.substring(lastSlash + 1);

        // 只编码文件名，避免中文/空格
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString())
                .replace("+", "%20"); // 避免空格变 +

        return prefix + encodedFileName;
    }

    /**
     * Delete Email Templates
     */
    @RequiresPermissions("system:template:remove")
    @Log(title = "Email Templates", businessType = BusinessType.DELETE)
    @DeleteMapping("/{templateIds}")
    public AjaxResult remove(@PathVariable Long[] templateIds) {
        return toAjax(hrEmailTemplateService.removeByIds(Arrays.asList(templateIds)));
    }
}
