package com.ys.system.api;

import com.ys.common.core.constant.ServiceNameConstants;
import com.ys.common.core.domain.R;
import com.ys.system.api.domain.SysFile;
import com.ys.system.api.factory.RemoteFileFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * File Service
 */
@FeignClient(contextId = "remoteFileService", value = ServiceNameConstants.FILE_SERVICE, fallbackFactory = RemoteFileFallbackFactory.class)
public interface RemoteFileService
{
    /**
     *  Upload File
     *
     * @param file File INFORMATION
     * @return Result
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R<SysFile> upload(@RequestPart(value = "file") MultipartFile file);

    /**
     * Picture base64
     *
     * @param file File INFORMATION
     * @return Result
     */
    @PostMapping(value = "/toBase64")
    public R<String> toBase64(@RequestBody SysFile file);
}
