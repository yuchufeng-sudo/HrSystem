package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrDocument;
import com.ys.hr.domain.HrEmployeeContract;
import com.ys.hr.domain.vo.HrDocumentShareVo;

import java.util.List;

/**
 * Document Management Service Interface
 *
 * @author ys
 * @date 2025-05-27
 */
public interface IHrDocumentService extends IService<HrDocument>
{
    /**
     * Query Document Management
     *
     * @param documentId Document Management primary key
     * @return Document Management
     */
    public HrDocument selectHrDocumentByDocumentId(Long documentId);

    /**
     * Query Document Management list
     *
     * @param hrDocument Document Management
     * @return Document Management collection
     */
    public List<HrDocument> selectHrDocumentList(HrDocument hrDocument);

    List<HrDocumentShareVo> selectHrDocumentShareList(HrDocument hrDocument);

    /**
     * Batch delete Document Management
     *
     * @param documentIds Document Management primary keys to be deleted
     * @return Result
     */
    public int deleteHrDocumentByDocumentIds(String[] documentIds);

    /**
     * Save paper contract documents
     * @param empContract
     */
    void saveContranctFile(HrEmployeeContract empContract);
}
