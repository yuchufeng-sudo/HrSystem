package com.ys.hr.service;

import com.ys.hr.domain.vo.EnterpriseVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "ys-system", contextId = "enterpriseService")
public interface EnterpriseService {

    /**
     * Query enterprise information based on Enterprise ID
     */
    @GetMapping("/enterpriseOrder/{id}")
    EnterpriseVo getEnterpriseById(@PathVariable("id") String id);

    /**
     * Update enterprise information
     */
    @PutMapping("/enterpriseOrder/{id}")
    int updateEnterprise(@PathVariable("id") String id, @RequestBody EnterpriseVo enterpriseVo);
}
