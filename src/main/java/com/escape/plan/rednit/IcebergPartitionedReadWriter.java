package com.escape.plan.rednit;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class IcebergPartitionedReadWriter {
    public static void main(String[] args) {
        S3Client s3 = S3Client.builder()
                .region(Region.US_EAST_1) // MinIO doesn't use regions, but it's required for AWS SDK
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("admin", "admin123")))
                .endpointOverride(java.net.URI.create("http://127.0.0.1:9000")) // MinIO endpoint
                .build();

        String bucketName = "iceberg-bucket";
        String objectKey = "blog_posts/data/00001-1-eea72d91-7c13-4824-b6df-2d824a0038e2-00001.parquet"; // Change as needed
        String localPath = "./data.parquet";
        String updatedPath = "./updated_data.parquet";



        // Download the Parquet file
        s3.getObject(GetObjectRequest.builder().bucket(bucketName).key(objectKey).build(),
                Paths.get(localPath));

        System.out.println("Downloaded Parquet file to: " + localPath);

        try (Connection conn = DriverManager.getConnection("jdbc:duckdb:")) {
            Statement stmt = conn.createStatement();

            // Load and query the Parquet file
            ResultSet rs = stmt.executeQuery("SELECT * FROM read_parquet('data.parquet');");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + ", Title: " + rs.getString("title"));
            }
            stmt.execute("CREATE TABLE temp_table AS SELECT * FROM read_parquet('data.parquet')");



            // Perform update
            stmt.execute("UPDATE temp_table SET title = 'Updated Title2' where id=1");




            // Write back to a new Parquet file
            stmt.execute("COPY temp_table TO 'updated_data.parquet' (FORMAT 'parquet')");

            System.out.println("Update successful! Saved as 'updated_data.parquet'");



        } catch (Exception e) {
            e.printStackTrace();
        }

        s3.putObject(PutObjectRequest.builder().bucket(bucketName).key(objectKey).build(),
                Paths.get(updatedPath));

        System.out.println("Updated Parquet file uploaded to MinIO.");

        }
}
