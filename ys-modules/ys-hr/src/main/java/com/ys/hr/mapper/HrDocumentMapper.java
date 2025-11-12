package com.ys.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrDocument;
import com.ys.hr.domain.vo.HrDocumentShareVo;

import java.util.List;

/**
 * Document Management Mapper Interface
 *
 * @author ys
 * @date 2025-05-27
 */
public interface HrDocumentMapper extends BaseMapper<HrDocument>
{
    /**
     * Query Document Management
     *
     * @param documentId Document Management primary key
     * @return Document Management
     */
    HrDocument selectHrDocumentByDocumentId(Long documentId);

    /**
     * Query Document Management list
     *
     * @param hrDocument Document Management
     * @return Document Management collection
     */
    List<HrDocument> selectHrDocumentList(HrDocument hrDocument);

    List<HrDocumentShareVo> selectHrDocumentShareList(HrDocument hrDocument);
}
