package com.uady.awsproject.common.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {
    private int status;
    private String message;
    private String path;
    private Instant timestamp;
}

