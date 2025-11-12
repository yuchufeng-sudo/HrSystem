package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrDocumentShare;

import java.util.List;

/**
 * Document sharing information Service Interface
 *
 * @author ys
 * @date 2025-05-27
 */
public interface IHrDocumentShareService extends IService<HrDocumentShare>
{
    public List<HrDocumentShare> selectHrDocumentShareByDocumentId(Long documentId);

    public int deleteByUploadCandidateId(Long uploadCandidateId);
}
