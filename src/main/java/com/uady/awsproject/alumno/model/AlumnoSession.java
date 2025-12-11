package com.uady.awsproject.alumno.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;

@DynamoDbBean
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlumnoSession {

    private String id;
    private Long alumnoId;
    private Long fecha;
    private Boolean active;
    private String sessionString;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("id")
    public String getId() {
        return id;
    }

    @DynamoDbAttribute("alumnoId")
    public Long getAlumnoId() {
        return alumnoId;
    }

    @DynamoDbAttribute("fecha")
    public Long getFecha() {
        return fecha;
    }

    @DynamoDbAttribute("active")
    public Boolean getActive() {
        return active;
    }

    @DynamoDbAttribute("sessionString")
    public String getSessionString() {
        return sessionString;
    }
}

