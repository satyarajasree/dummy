package com.rajasreeit.backend.imagesS3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class S3FileUploadService {

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${app.s3.bucket}")
    private String bucketName;

    public String uploadFile(String subDir, MultipartFile file, String fileName) throws IOException {
        // Construct the S3 key (path)
        String s3Key = subDir + "/" + fileName;

        // Set metadata (optional, can include file type, size, etc.)
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        amazonS3.putObject(new PutObjectRequest(bucketName, s3Key, file.getInputStream(), metadata));

        return amazonS3.getUrl(bucketName, s3Key).toString();
    }
}
