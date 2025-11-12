package com.ys.hr.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrQuestionAnswer;
import com.ys.system.api.domain.SysUser;

/**
 * Candidate question answers table Service Interface
 *
 * @author ys
 * @date 2025-09-27
 */
public interface IHrQuestionAnswerService extends IService<HrQuestionAnswer>
{

    /**
     * Query Candidate question answers table list
     *
     * @param hrQuestionAnswer Candidate question answers table
     * @return Candidate question answers table collection
     */
    public List<HrQuestionAnswer> selectHrQuestionAnswerList(HrQuestionAnswer hrQuestionAnswer);

    List<SysUser> selectHrList(String enterpriseId);
}
