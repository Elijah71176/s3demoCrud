package se.elijah.s3demo;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.ScatteringByteChannel;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class S3Service {
    public void listFiles(S3Client s3Client, String bucketName) {
        try {
            ListObjectsV2Response result = s3Client.listObjectsV2(
                    ListObjectsV2Request.builder().bucket(bucketName).build()
            );
            if (result.contents().isEmpty()) {
                System.out.println("No related objects.");

            } else {
                result.contents().forEach(obj -> System.out.println(obj.key()));

            }
        } catch (S3Exception e) {
            System.err.println("Error listing files: " + e.awsErrorDetails().errorMessage());

        }
    }

    public void uploadFile(S3Client s3Client, String bucketName, String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("File not found: " + filePath);
            return;
        }
        try {
            s3Client.putObject(PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(file.getName())
                            .build(),
                    RequestBody.fromFile(file));
            System.out.println("Uploaded: " + file.getName());
        } catch (Exception e) {
            System.err.println("Uploading error: " + e.getMessage());

        }

    }

    public void downloadFile(S3Client s3Client, String bucketName, String fileKey, String destinationPath) {
        try (ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(
                GetObjectRequest.builder().bucket(bucketName).key(fileKey).build());
             FileOutputStream fos = new FileOutputStream(destinationPath)) {
            fos.write(s3Object.readAllBytes());
            System.out.println("Downloaded to: " + destinationPath);

        } catch (Exception e) {
            System.err.println("Error downloading: " + e.getMessage());

        }


    }

    public void deleteFile(S3Client s3Client, String bucketName, String fileKey) {
        try {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileKey)
                    .build());
            System.out.println("Deleted: " + fileKey);
        } catch (Exception e) {
            System.err.println("Error downloading file: " + e.getMessage());
        }

    }

    public void searchFiles(S3Client s3Client, String bucketName, String searchTerm) {
        try {
            ListObjectsV2Response result = s3Client.listObjectsV2(
                    ListObjectsV2Request.builder().bucket(bucketName).build()
            );
            result.contents().stream()
                    .filter(obj -> obj.key().contains(searchTerm))
                    .forEach(obj -> System.out.println("Match: " + obj.key()));

        } catch (Exception e) {
            System.out.println("Error searching files: " + e.getMessage());
        }
    }
    public void uploadFolderAsZip(S3Client s3Client, String bucketName, String folderPath) {
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Invalid folder: " + folderPath);
            return;
        }

        String zipName = folder.getName() + ".zip";
        File zipFile = new File(zipName);

        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {
            for (File file : folder.listFiles()) {
                try (FileInputStream fis = new FileInputStream(file)) {
                    zos.putNextEntry(new ZipEntry(file.getName()));
                    zos.write(fis.readAllBytes());
                    zos.closeEntry();
                }
            }
        } catch (IOException e) {
            System.err.println("Error zipping: " + e.getMessage());
            return;
        }

        uploadFile(s3Client, bucketName, zipFile.getAbsolutePath());
    }
}