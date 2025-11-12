package com.ys.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrDocumentShare;

import java.util.List;

/**
 * Document sharing information Mapper Interface
 *
 * @author ys
 * @date 2025-05-27
 */
public interface HrDocumentShareMapper extends BaseMapper<HrDocumentShare>
{

    List<HrDocumentShare> selectHrDocumentShareByDocumentId(Long documentId);

    int deleteByUploadCandidateId(Long uploadCandidateId);
}
