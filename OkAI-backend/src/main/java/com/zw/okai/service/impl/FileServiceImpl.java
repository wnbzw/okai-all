package com.zw.okai.service.impl;

import com.zw.okai.common.MinioProperties;
import com.zw.okai.common.ErrorCode;
import com.zw.okai.exception.BusinessException;
import com.zw.okai.service.FileService;
import io.minio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    
    @Autowired
    private MinioProperties minioProperties;

    @Autowired
    private MinioClient client;

    @Override
    public String upload(MultipartFile file) {

        try {
            boolean bucketExists = client.bucketExists(BucketExistsArgs.builder().bucket(minioProperties.getBucketName()).build());
            if (!bucketExists) {
                client.makeBucket(MakeBucketArgs.builder().bucket(minioProperties.getBucketName()).build());
                client.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(minioProperties.getBucketName()).config(createBucketPolicyConfig(minioProperties.getBucketName())).build());
            }

           String filename = new SimpleDateFormat("yyyyMMdd").format(new Date()) + "/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
//            String filename=file.getOriginalFilename();
            client.putObject(PutObjectArgs.builder().
                    bucket(minioProperties.getBucketName()).
                    object(filename).
                    stream(file.getInputStream(), file.getSize(), -1).
                    contentType(file.getContentType()).build());
            return String.join("/", minioProperties.getEndpoint(), minioProperties.getBucketName(), filename);

        } catch (Exception e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "上传文件失败");
        }
    }

    private String createBucketPolicyConfig(String bucketName) {
        return "{\n" +
                "  \"Statement\" : [ {\n" +
                "    \"Action\" : \"s3:GetObject\",\n" +
                "    \"Effect\" : \"Allow\",\n" +
                "    \"Principal\" : \"*\",\n" +
                "    \"Resource\" : \"arn:aws:s3:::" + bucketName + "/*\"\n" +
                "  } ],\n" +
                "  \"Version\" : \"2012-10-17\"\n" +
                "}";
    }
}