package com.ys.hr.sign;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.ys.common.core.utils.uuid.UUID;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

@Slf4j
@Data
@Component
public class OssUtils {

    private String accessKeyId;

    private String accessKeySecret;

    private String endpoint;

    private String bucketName;

    private String domain;

    /**
     *  Upload File
     */
    public String uploadFile(MultipartFile file,String fileType) {
        // Create an OSSClient instance.。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        String url = "";
        try {
            String fileName = file.getOriginalFilename();
            assert fileName != null;
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            String path = "";
            path = fileType;
            String sourcePath = path + UUID.fastUUID() + "." + suffix;
            ossClient.putObject(bucketName, sourcePath, file.getInputStream());
            url = domain + "/" + sourcePath;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the OSSClient。
            ossClient.shutdown();
        }
        return url;
    }


   /**
     * Convert MultipartFile to File
     *
     * @param outFilePath parameter
     * @param multiFile parameter
     * @return execution result
     */
    public static File multipartFileToFile(String outFilePath, MultipartFile multiFile) {
        // Obtain file name
        if (null == multiFile) {
            return null;
        }
        String fileName = multiFile.getOriginalFilename();
        if (null == fileName) {
            return null;
        }
        try {
            File toFile;
            InputStream ins;
            ins = multiFile.getInputStream();
            // Specify the storage path
            toFile = new File(outFilePath.concat(File.separator).concat(multiFile.getOriginalFilename()));
            inputStreamToFile(ins, toFile);
            return toFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void inputStreamToFile(InputStream ins, File file) {
        try (OutputStream os = Files.newOutputStream(file.toPath())) {
            int bytesRead;
            int bytes = 8192;
            byte[] buffer = new byte[bytes];
            while ((bytesRead = ins.read(buffer, 0, bytes)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String uploadFile(File file,String fileType) {
        // Create an OSSClient instance.。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        String url = "";
        try {
            String fileName = file.getName();
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            String path = "";
            path = fileType;
            String sourcePath1 = path + UUID.fastUUID() + "." + suffix;
            ossClient.putObject(bucketName, sourcePath1, file);
            url = domain + "/" + sourcePath1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the OSSClient。
            ossClient.shutdown();
        }
        return url;
    }

}
