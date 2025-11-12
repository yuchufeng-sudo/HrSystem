package com.ys.hr.sign.entiry;

import com.alibaba.fastjson.JSONObject;
import com.ys.hr.domain.HrEmployeeContract;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model Parameters
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignVo {

    /**
     * Contract Content
     */
    private String content;


    /**
     * Configuration Parameters
     */
    private JSONObject config;

    /**
     *  RequestAddress
     */
    private String url;

    /**
     * Contract Template Name
     */
    private String templateName;

    /**
     * Contract Object
     */
    private HrEmployeeContract employeeContract;
}
