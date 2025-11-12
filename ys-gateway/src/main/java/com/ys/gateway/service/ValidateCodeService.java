package com.ys.gateway.service;

import java.io.IOException;
import com.ys.common.core.exception.CaptchaException;
import com.ys.common.core.web.domain.AjaxResult;

/**
 * 
 *
 * @author ruoyi
 */
public interface ValidateCodeService
{
    /**
     * 
     */
    public AjaxResult createCaptcha() throws IOException, CaptchaException;

    /**
     *  
     */
    public void checkCaptcha(String key, String value) throws CaptchaException;
}
