package com.uady.awsproject.common.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3Service {

    private static final Logger log = LoggerFactory.getLogger(S3Service.class);

    private final S3Client s3Client;

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    @Value("${aws.region}")
    private String region;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadFile(MultipartFile file, String folder) {
        try {
            log.info("Uploading file: {} to bucket: {}, folder: {}", file.getOriginalFilename(), bucketName, folder);

            String fileName = generateFileName(file.getOriginalFilename());
            String key = folder + "/" + fileName;

            log.debug("Generated key: {}, contentType: {}, size: {}", key, file.getContentType(), file.getSize());

            // Upload without ACL - rely on bucket policy instead
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .contentType(file.getContentType())
                            .build(),
                    RequestBody.fromBytes(file.getBytes())
            );

            String url = getFileUrl(key);
            log.info("File uploaded successfully. URL: {}", url);
            return url;

        } catch (IOException e) {
            log.error("IOException while uploading file: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to upload file to S3: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("S3 upload error: {}", e.getMessage(), e);
            throw new RuntimeException("S3 upload error: " + e.getMessage(), e);
        }
    }

    private String generateFileName(String originalFileName) {
        String extension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        return UUID.randomUUID().toString() + extension;
    }

    private String getFileUrl(String key) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, key);
    }
}

