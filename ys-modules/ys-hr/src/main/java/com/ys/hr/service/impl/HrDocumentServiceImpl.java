package com.ys.hr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.common.core.utils.DateUtils;
import com.ys.common.core.utils.StringUtils;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.HrDocument;
import com.ys.hr.domain.HrDocumentShare;
import com.ys.hr.domain.HrEmployeeContract;
import com.ys.hr.domain.HrEmployees;
import com.ys.hr.domain.vo.HrDocumentShareVo;
import com.ys.hr.mapper.HrDocumentMapper;
import com.ys.hr.mapper.HrEmployeesMapper;
import com.ys.hr.service.IHrDocumentService;
import com.ys.hr.service.IHrDocumentShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Document Management Service Implementation
 *
 * @author ys
 * @date 2025-05-27
 */
@Service
public class HrDocumentServiceImpl extends ServiceImpl<HrDocumentMapper, HrDocument> implements IHrDocumentService
{
    @Autowired
    private HrEmployeesMapper hrEmployeesMapper;

    @Resource
    private IHrDocumentShareService hrDocumentShareService;

    /**
     * Query Document Management
     *
     * @param documentId Document Management primary key
     * @return Document Management
     */
    @Override
    public HrDocument selectHrDocumentByDocumentId(Long documentId)
    {
        return baseMapper.selectHrDocumentByDocumentId(documentId);
    }

    /**
     * Query Document Management list
     *
     * @param hrDocument Document Management
     * @return Document Management
     */
    @Override
    public List<HrDocument> selectHrDocumentList(HrDocument hrDocument)
    {
        hrDocument.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        return baseMapper.selectHrDocumentList(hrDocument);
    }

    @Override
    public List<HrDocumentShareVo> selectHrDocumentShareList(HrDocument hrDocument) {
        return baseMapper.selectHrDocumentShareList(hrDocument);
    }

    /**
     * Batch delete Document Management
     *
     * @param documentIds Document Management primary keys to be deleted
     * @return Result
     */
    @Override
    public int deleteHrDocumentByDocumentIds(String[] documentIds)
    {
        return baseMapper.deleteBatchIds(Arrays.asList(documentIds));
    }

    @Override
    @Transactional
    public void saveContranctFile(HrEmployeeContract empContract) {
        if (StringUtils.isNotEmpty(empContract.getAccessory())){
            HrDocument document = new HrDocument();
            URL url = null;
            HttpURLConnection conn = null;
            try {
                url = new URL(empContract.getAccessory());
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                conn.setRequestProperty("Range", "bytes=0-0");
                conn.connect();

                long size = conn.getContentLengthLong();
                document.setStatus("1");
                document.setFileSize(size);

                String path = url.getPath();

                String fileName = Paths.get(path).getFileName().toString();
                fileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8.name());

                // file name
                document.setFileName(fileName);
                document.setFilePath(empContract.getAccessory());

                String extension = "";
                int dotIndex = fileName.lastIndexOf(".");
                if (dotIndex != -1 && dotIndex < fileName.length() - 1) {
                    extension = fileName.substring(dotIndex + 1).toLowerCase();
                }
                String fileType = getFileType(extension);
                document.setFileType(fileType);
                document.setCreateTime(DateUtils.getNowDate());
                document.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
                document.setUploadUserId(SecurityUtils.getUserId());
                document.setUploadDate(DateUtils.getNowDate());
                baseMapper.insert(document);
                HrEmployees employees = hrEmployeesMapper.selectHrEmployeesById(empContract.getBUserId());
                document.setUserId(employees.getUserId());
                insertShareHrDocument(document, hrDocumentShareService);
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }

        }
    }

    public static void insertShareHrDocument(HrDocument document, IHrDocumentShareService hrDocumentShareService) {
        QueryWrapper<HrDocumentShare> queryWrapper = new QueryWrapper<>();
        Long documentId = document.getDocumentId();
        saveShareDocument(hrDocumentShareService, queryWrapper, documentId, document.getUserId());
    }

    static void saveShareDocument(IHrDocumentShareService hrDocumentShareService, QueryWrapper<HrDocumentShare> queryWrapper, Long documentId, Long userId) {
        queryWrapper.eq("document_id", documentId);
        hrDocumentShareService.remove(queryWrapper);
        HrDocumentShare documentShare = new HrDocumentShare();
        documentShare.setDocumentId(documentId);
        documentShare.setSharedTo(userId);
        documentShare.setSharedBy(SecurityUtils.getUserId());
        documentShare.setPermissionType("2");
        documentShare.setCreateTime(DateUtils.getNowDate());
        hrDocumentShareService.save(documentShare);
    }

    private String getFileType(String extension) {
        switch (extension) {
            // Word
            case "doc":
            case "docx":
                return "word";
            // Excel
            case "xls":
            case "xlsx":
                return "excel";
            // PPT
            case "ppt":
            case "pptx":
                return "ppt";
            // PDF
            case "pdf":
                return "pdf";
            default:
                return null;
        }
    }
}
