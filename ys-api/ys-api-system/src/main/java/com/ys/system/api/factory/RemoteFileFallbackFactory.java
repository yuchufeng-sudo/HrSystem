package com.ys.system.api.factory;

import com.ys.common.core.domain.R;
import com.ys.system.api.RemoteFileService;
import com.ys.system.api.domain.SysFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * File Service Degradation Handling
 */
@Component
public class RemoteFileFallbackFactory implements FallbackFactory<RemoteFileService>
{
    private static final Logger log = LoggerFactory.getLogger(RemoteFileFallbackFactory.class);

    @Override
    public RemoteFileService create(Throwable throwable)
    {
        log.error("File Service Invocation Failure:{}", throwable.getMessage());
        return new RemoteFileService()
        {
            @Override
            public R<SysFile> upload(MultipartFile file)
            {
                return R.fail(" Upload File Failure:" + throwable.getMessage());
            }

            @Override
            public R<String> toBase64(SysFile file) {
                return R.fail("Picture Conversion Failure:" + throwable.getMessage());
            }
        };
    }
}
