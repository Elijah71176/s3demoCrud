package se.elijah.s3demo;

import io.github.cdimascio.dotenv.Dotenv;
import
        org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.identity.spi.AwsCredentialsIdentity;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

@SpringBootApplication
public class S3demoApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(S3demoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        Dotenv dotenv = Dotenv.load();
        String bucketName = dotenv.get("BUCKET_NAME");
        String accessKey = dotenv.get("AWS_ACCESS_KEY_ID");
        String secretKey = dotenv.get("AWS_SECRET_ACCESS_KEY");
        String region = dotenv.get("AWS_DEFAULT_REGION");

        Scanner scanner = new Scanner(System.in);
        // S3Client s3Client = new S3Client(); //need dependencies
        S3Client s3Client = S3Client.builder()
                .region(software.amazon.awssdk.regions.Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .build();

//        while (true) {
//            System.out.println("Make a choice by click in number and follow up instructions");
//            System.out.println("1, List files");
//            System.out.println("2, Upload file");
//            System.out.println("3, Download file");
//            System.out.println("4, Exit");
//
//
//            int choice;
//            try {
//                choice = Integer.parseInt(scanner.nextLine());
//            } catch (Exception e) {
//                System.out.println("Invalid choice");
//                continue;
//            }
//
//
//            switch (choice) {
//                case 1:
//                    System.out.println("List all files in an S3 bucket");
//                    listfiles(s3Client, bucketName);
//                    break;
//                case 2:
//                    System.out.println("Upload a file to a bucket");
//                    String fileNamePath = scanner.nextLine();
//                    uploadFile(s3Client, bucketName, fileNamePath);
//                    break;
//                case 3:
//                    System.out.println("Download a file from a bucket");
//                    String fileKey = scanner.nextLine();
//                    System.out.println("Enter destination path");
//                    String destinationPath = scanner.nextLine();
//                    downLoadFile(s3Client, bucketName, fileKey, destinationPath);
//                    break;
//                case 4:
//                    System.out.println("Exits");
//                    return; // exits the run() method, stopping the app
//                default:
//                    System.out.println("Ogiltigt val, försök igen.");
//            }
//        }
//    }
//
//    private void listfiles(S3Client s3Client, String bucketName) {
//        try {
//            ListObjectsV2Request request = ListObjectsV2Request.builder()
//                    .bucket(bucketName)
//                    .build();
//            ListObjectsV2Response result = s3Client.listObjectsV2(request);
//            if (result.contents().isEmpty()) {
//                System.out.println("No files found in bucket.");
//            } else {
//                System.out.println("Files in bucket:");
//                result.contents().forEach(obj -> System.out.println(obj.key()));
//            }
//        } catch (S3Exception e) {
//            System.err.println("Error listing files: " + e.awsErrorDetails().errorMessage());
//        }
//    }
//
//    private void uploadFile(S3Client s3Client, String bucketName, String filePath) {
//        try {
//            File file = new File(filePath);
//            if (!file.exists()) {
//                System.out.println("File not found: " + filePath);
//                return;
//            }
//            s3Client.putObject(PutObjectRequest.builder()
//                            .bucket(bucketName)
//                            .key(file.getName())
//                            .build(),
//                    RequestBody.fromFile(file));
//            System.out.println("File uploaded successfully: " + file.getName());
//        } catch (S3Exception e) {
//            System.err.println("Error uploading file: " + e.awsErrorDetails().errorMessage());
//        }
//
//    }
//
//    private void downLoadFile(S3Client s3Client, String bucketName, String fileName, String destinationPath) {
//        try {
//            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(GetObjectRequest.builder()
//                    .bucket(bucketName)
//                    .key(fileName)
//                    .build());
//            //RequestBody.fromFile(file));
//            try (FileOutputStream fos = new FileOutputStream(destinationPath)) {
//                fos.write(s3Object.readAllBytes());
//
//            }
//            System.out.println("File downloaded successfully: " + destinationPath);
//
//        } catch (S3Exception | IOException e) {
//            System.err.println("Error downloading file: " + e.getMessage());
//        }

        // initiera services
        S3Service s3Service = new S3Service();
        menuService menu = new menuService(s3Service, bucketName);

        // starta meny
        menu.startMenu(s3Client);
    }
}

