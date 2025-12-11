package com.uady.awsproject.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

@Configuration
public class SnsConfig {

    @Value("${aws.region}")
    private String region;

    @Value("${aws.access.key.id}")
    private String accessKeyId;

    @Value("${aws.secret.access.key}")
    private String secretAccessKey;

    @Value("${aws.session.token:}")
    private String sessionToken;

    @Bean
    public SnsClient snsClient() {
        StaticCredentialsProvider credentialsProvider;

        if (sessionToken != null && !sessionToken.isEmpty()) {
            AwsSessionCredentials sessionCredentials = AwsSessionCredentials.create(
                    accessKeyId,
                    secretAccessKey,
                    sessionToken
            );
            credentialsProvider = StaticCredentialsProvider.create(sessionCredentials);
        } else {
            AwsBasicCredentials basicCredentials = AwsBasicCredentials.create(
                    accessKeyId,
                    secretAccessKey
            );
            credentialsProvider = StaticCredentialsProvider.create(basicCredentials);
        }

        return SnsClient.builder()
                .region(Region.of(region))
                .credentialsProvider(credentialsProvider)
                .build();
    }

}