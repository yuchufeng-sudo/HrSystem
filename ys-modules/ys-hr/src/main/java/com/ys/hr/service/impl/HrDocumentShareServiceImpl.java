package com.ys.hr.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.hr.domain.HrDocumentShare;
import com.ys.hr.mapper.HrDocumentShareMapper;
import com.ys.hr.service.IHrDocumentShareService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Document sharing information Service Implementation
 *
 * @author ys
 * @date 2025-05-27
 */
@Service
public class HrDocumentShareServiceImpl extends ServiceImpl<HrDocumentShareMapper, HrDocumentShare> implements IHrDocumentShareService
{
    @Override
    public List<HrDocumentShare> selectHrDocumentShareByDocumentId(Long documentId) {
        return baseMapper.selectHrDocumentShareByDocumentId(documentId);
    }

    @Override
    public int deleteByUploadCandidateId(Long uploadCandidateId) {
        return baseMapper.deleteByUploadCandidateId(uploadCandidateId);
    }
}
