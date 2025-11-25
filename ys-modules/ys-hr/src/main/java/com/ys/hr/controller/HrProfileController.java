package com.ys.hr.controller;

import com.ys.common.core.domain.R;
import com.ys.common.core.utils.StringUtils;
import com.ys.common.core.utils.file.FileTypeUtils;
import com.ys.common.core.utils.file.MimeTypeUtils;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.security.service.TokenService;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.HrEmployees;
import com.ys.hr.service.IHrEmployeesService;
import com.ys.system.api.RemoteFileService;
import com.ys.system.api.domain.SysFile;
import com.ys.system.api.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

/**
 *
 *
 * @author ys
 */
@RestController
@RequestMapping("/employees/profile")
public class HrProfileController extends BaseController
{

    @Autowired
    private IHrEmployeesService hrEmployeesService;

    @Autowired
    private RemoteFileService remoteFileService;
    @Autowired
    private TokenService tokenService;

    /**
     *
     */
    @GetMapping
    public AjaxResult profile()
    {
        HrEmployees hrEmployees = hrEmployeesService.selectHrEmployeesByUserId(SecurityUtils.getUserId());
        return success(hrEmployees);
    }

    /**
     *
     */
    @PostMapping
    public AjaxResult update(@RequestBody HrEmployees hrEmployees)
    {
        hrEmployees.setUserId(SecurityUtils.getUserId());
        hrEmployees.setAccessLevel(null);
        hrEmployeesService.updateEmployees(hrEmployees);
        LoginUser loginUser = SecurityUtils.getLoginUser();
        loginUser.getSysUser().setNickName(hrEmployees.getFullName());
        tokenService.setLoginUser(loginUser);
        return success(hrEmployees);
    }

    /**
     * MODIFYUSER
     */
    @PostMapping("/avatar")
    public AjaxResult avatar(@RequestParam("avatarfile") MultipartFile file)
    {
        if (!file.isEmpty())
        {
            String extension = FileTypeUtils.getExtension(file);
            if (!StringUtils.equalsAnyIgnoreCase(extension, MimeTypeUtils.IMAGE_EXTENSION))
            {
                return error("The file format is incorrect. Please upload it." + Arrays.toString(MimeTypeUtils.IMAGE_EXTENSION) + "Format");
            }
            R<SysFile> fileResult = remoteFileService.upload(file);
            if (StringUtils.isNull(fileResult) || StringUtils.isNull(fileResult.getData()))
            {
                return error("File Service exception. Please contact the management staff.");
            }
            String url = fileResult.getData().getUrl();
            HrEmployees employees = new HrEmployees();
            HrEmployees hrEmployees1 = hrEmployeesService.selectHrEmployeesByUserId(SecurityUtils.getUserId());
            employees.setEmployeeId(hrEmployees1.getEmployeeId());
            employees.setAvatarUrl(url);
            if (hrEmployeesService.updateAvatarUrl(employees)!=0)
            {
                LoginUser loginUser = SecurityUtils.getLoginUser();
                loginUser.getSysUser().setAvatar(url);
                tokenService.setLoginUser(loginUser);
                AjaxResult ajax = AjaxResult.success();
                ajax.put("imgUrl", url);
                return ajax;
            }
        }
        return error("Upload Picture exception. Please contact the management staff.");
    }
}
