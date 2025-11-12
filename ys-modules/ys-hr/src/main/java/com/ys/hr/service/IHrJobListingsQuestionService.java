package com.ys.hr.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrJobListingsQuestion;

/**
 * Job listings questions Service Interface
 *
 * @author ys
 * @date 2025-09-27
 */
public interface IHrJobListingsQuestionService extends IService<HrJobListingsQuestion> {

    /**
     * Query Job listings questions list
     *
     * @param hrJobListingsQuestion Job listings questions
     * @return Job listings questions collection
     */
    public List<HrJobListingsQuestion> selectHrJobListingsQuestionList(HrJobListingsQuestion hrJobListingsQuestion);
}
