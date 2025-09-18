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

        // DEBUG: check which key is used
        System.out.println("Using AWS access key: " + accessKey);

        Scanner scanner = new Scanner(System.in);
        // S3Client s3Client = new S3Client(); //need dependencies
        S3Client s3Client = S3Client.builder()
                .region(software.amazon.awssdk.regions.Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .build();

        // initiera services
        S3Service s3Service = new S3Service();
        menuService menu = new menuService(s3Service, bucketName);

        // starta meny
        menu.startMenu(s3Client);
    }
}

