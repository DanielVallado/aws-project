package com.uady.awsproject.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

    @Value("${aws.region}")
    private String region;

    @Value("${aws.access.key.id}")
    private String accessKeyId;

    @Value("${aws.secret.access.key}")
    private String secretAccessKey;

    @Value("${aws.session.token:}")
    private String sessionToken;

    @Bean
    public S3Client s3Client() {
        StaticCredentialsProvider credentialsProvider;

        if (sessionToken != null && !sessionToken.isEmpty()) {
            // Use session credentials (for AWS Academy)
            AwsSessionCredentials sessionCredentials = AwsSessionCredentials.create(
                    accessKeyId,
                    secretAccessKey,
                    sessionToken
            );
            credentialsProvider = StaticCredentialsProvider.create(sessionCredentials);
        } else {
            // Use basic credentials (for regular AWS accounts)
            AwsBasicCredentials basicCredentials = AwsBasicCredentials.create(
                    accessKeyId,
                    secretAccessKey
            );
            credentialsProvider = StaticCredentialsProvider.create(basicCredentials);
        }

        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(credentialsProvider)
                .build();
    }

}