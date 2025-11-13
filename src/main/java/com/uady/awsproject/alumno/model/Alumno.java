package com.uady.awsproject.alumno.model;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Alumno {
    private Long id;

    @NotBlank(message = "nombres cannot be empty")
    @Size(min = 1, max = 100, message = "nombres must be between 1 and 100 characters")
    private String nombres;

    @NotBlank(message = "apellidos cannot be empty")
    @Size(min = 1, max = 100, message = "apellidos must be between 1 and 100 characters")
    private String apellidos;

    @NotBlank(message = "matricula cannot be empty")
    @Size(min = 1, max = 50, message = "matricula must be between 1 and 50 characters")
    private String matricula;

    @NotNull(message = "promedio cannot be null")
    @DecimalMin(value = "0.0", message = "promedio must be greater than or equal to 0.0")
    @DecimalMax(value = "10.0", message = "promedio must be less than or equal to 10.0")
    private Double promedio;
}

