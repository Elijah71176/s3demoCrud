package se.elijah.s3demo;

import software.amazon.awssdk.services.s3.S3Client;

import java.util.Scanner;


public class menuService {
    private final S3Service s3Service;
    private Scanner scanner;
    private String bucketName;

    //constructor with the attribute
    public menuService(S3Service s3Service, String bucketName) {
        this.s3Service = s3Service;
        this.scanner = new Scanner(System.in);
        this.bucketName = bucketName;
    }

    public void startMenu(S3Client s3Client) {
        while (true) {
            System.out.println("\n==== Welcome to S3 demo ====");
            System.out.println("1 list, 2 Upload files, 3 Download, 4 Delete, 5 Search, 6 Change bucket, 7 Upload folder as zip, 8 Exit");
            System.out.println("1. List files");
            System.out.println("2. Upload file");
            System.out.println("3. Download file");
            System.out.println("4. Delete file");
            System.out.println("5. Search files");
            System.out.println("6. Change bucket");
            System.out.println("7. Upload folder as zip");
            System.out.println("8. Exit");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid choice, Try again");
                continue;
            }
            switch (choice) {
                case 1 -> s3Service.listFiles(s3Client, bucketName);
                case 2 -> {
                    System.out.println("Enter file path:");
                    s3Service.uploadFile(s3Client, bucketName, scanner.nextLine());
                }
                case 3 -> {
                    System.out.println("Enter file key to download:");
                    String fileKey = scanner.nextLine();
                    System.out.println("Enter destination path:");
                    String destination = scanner.nextLine();
                    s3Service.downloadFile(s3Client, bucketName, fileKey, destination);
                }
                case 4 -> {
                    System.out.println("Enter file key to delete:");
                    String key = scanner.nextLine();

                    s3Service.deleteFile(s3Client, bucketName, key);
                    System.out.println("✅ File '" + key + "' was successfully deleted from bucket: " + bucketName);

                }

                case 5 -> {
                    System.out.println("Enter search term:");
                    s3Service.searchFiles(s3Client, bucketName, scanner.nextLine());
                }
                case 6 -> {
                    System.out.println("Current bucket: " + bucketName);
                    System.out.println("Enter new bucket:");
                    String newBucket = scanner.nextLine();
                    if (!newBucket.isEmpty()) {
                        bucketName = newBucket;
                        System.out.println("Now using bucket: " + bucketName);
                    }
                }
                case 7 -> {
                    System.out.println("Enter folder path:");
                    s3Service.uploadFolderAsZip(s3Client, bucketName, scanner.nextLine());
                }
                case 8 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> {
                    System.out.println("Invalid choice, Try again");
                }
            }
        }
    }

}
