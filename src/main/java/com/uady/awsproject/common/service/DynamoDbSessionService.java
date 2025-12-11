package com.uady.awsproject.common.service;

import com.uady.awsproject.alumno.model.AlumnoSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;
import java.util.UUID;

@Service
public class DynamoDbSessionService {

    private final DynamoDbTable<AlumnoSession> sessionTable;

    public DynamoDbSessionService(DynamoDbEnhancedClient enhancedClient,
                                   @Value("${aws.dynamodb.table-name}") String tableName) {
        this.sessionTable = enhancedClient.table(tableName, TableSchema.fromBean(AlumnoSession.class));
    }

    public AlumnoSession createSession(Long alumnoId) {
        String sessionId = UUID.randomUUID().toString();
        String sessionString = generateRandomString(128);
        Long timestamp = System.currentTimeMillis() / 1000; // Unix timestamp in seconds

        AlumnoSession session = AlumnoSession.builder()
                .id(sessionId)
                .alumnoId(alumnoId)
                .fecha(timestamp)
                .active(true)
                .sessionString(sessionString)
                .build();

        sessionTable.putItem(session);
        return session;
    }

    public AlumnoSession findBySessionString(String sessionString, Long alumnoId) {
        // Scan the table filtering by sessionString and alumnoId
        return sessionTable.scan(ScanEnhancedRequest.builder()
                        .filterExpression(software.amazon.awssdk.enhanced.dynamodb.Expression.builder()
                                .expression("sessionString = :ss AND alumnoId = :aid")
                                .expressionValues(Map.of(
                                        ":ss", AttributeValue.builder().s(sessionString).build(),
                                        ":aid", AttributeValue.builder().n(alumnoId.toString()).build()
                                ))
                                .build())
                        .build())
                .items()
                .stream()
                .findFirst()
                .orElse(null);
    }

    public void deactivateSession(String sessionString, Long alumnoId) {
        AlumnoSession session = findBySessionString(sessionString, alumnoId);
        if (session != null) {
            session.setActive(false);
            sessionTable.updateItem(session);
        }
    }

    public boolean isSessionValid(String sessionString, Long alumnoId) {
        AlumnoSession session = findBySessionString(sessionString, alumnoId);
        return session != null && Boolean.TRUE.equals(session.getActive());
    }

    private String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            result.append(chars.charAt((int) (Math.random() * chars.length())));
        }
        return result.toString();
    }
}

