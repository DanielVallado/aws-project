package com.uady.awsproject.common.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

@Service
public class SnsService {

    private final SnsClient snsClient;

    @Value("${aws.sns.topic.arn}")
    private String topicArn;

    public SnsService(SnsClient snsClient) {
        this.snsClient = snsClient;
    }

    public void publishMessage(String subject, String message) {
        PublishRequest request = PublishRequest.builder()
                .topicArn(topicArn)
                .subject(subject)
                .message(message)
                .build();

        PublishResponse result = snsClient.publish(request);
        System.out.println("Message sent. Status: " + result.messageId());
    }
}

