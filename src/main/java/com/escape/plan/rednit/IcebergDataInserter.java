package com.escape.plan.rednit;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.regions.Region;  // ✅ Import this


import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


public class IcebergDataInserter {
    public static void main(String[] args) throws IOException {
        // Set up Hadoop Configuration for MinIO
        String minioEndpoint = "http://127.0.0.1:9000";
        String accessKey = "admin";
        String secretKey = "admin123";
        String bucketName = "iceberg-bucket";
        String objectKey = "data/blog_posts.json";
        Region region = Region.US_EAST_1;

        // Initialize MinIO S3 client
        S3Client s3 = null;
        try {
            s3 = S3Client.builder()
                    .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                    .endpointOverride(java.net.URI.create(minioEndpoint))
                    .forcePathStyle(true)
                    .region(region)// Required for MinIO
                    .build();
        }
        catch (Exception e) {
            System.out.println(e);
        }


        // Create sample JSON data
        String jsonData = """
            [
                { "id": 2, "title": "MinIO Upload", "content": "Writing to MinIO directl2!", "created_at": "2024-03-31T10:00:00Z" }
            ]
        """;

        // Write JSON data to a temporary file
        java.nio.file.Path tempFile = Files.createTempFile("blog_posts", ".json");
        Files.write(tempFile, jsonData.getBytes(StandardCharsets.UTF_8));

        // Upload file to MinIO
        s3.putObject(PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(objectKey)
                        .build(),
                RequestBody.fromFile(tempFile.toFile()));

        System.out.println("✅ File uploaded successfully to MinIO at: " + minioEndpoint + "/" + bucketName + "/" + objectKey);

        // Cleanup: Delete the temp file
        Files.delete(tempFile);
    }
}


