package com.ys.hr.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrQuestionAnswer;
import com.ys.system.api.domain.SysUser;
import org.apache.ibatis.annotations.Param;

/**
 * Candidate question answers table Mapper Interface
 *
 * @author ys
 * @date 2025-09-27
 */
public interface HrQuestionAnswerMapper extends BaseMapper<HrQuestionAnswer>
{
    /**
     * Query Candidate question answers table
     *
     * @param id Candidate question answers table primary key
     * @return Candidate question answers table
     */
    public HrQuestionAnswer selectHrQuestionAnswerById(String id);
    
    /**
     * Query Candidate question answers table list
     *
     * @param hrQuestionAnswer Candidate question answers table
     * @return Candidate question answers table collection
     */
    public List<HrQuestionAnswer> selectHrQuestionAnswerList(HrQuestionAnswer hrQuestionAnswer);

    List<SysUser> selectHrList(@Param("enterpriseId") String enterpriseId);
}