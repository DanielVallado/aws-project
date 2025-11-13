package com.uady.awsproject.profesor.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
public class Profesor {
    private Long id;

    @NotBlank(message = "numeroEmpleado cannot be empty")
    @Size(min = 1, max = 50, message = "numeroEmpleado must be between 1 and 50 characters")
    private String numeroEmpleado;

    @NotBlank(message = "nombres cannot be empty")
    @Size(min = 1, max = 100, message = "nombres must be between 1 and 100 characters")
    private String nombres;

    @NotBlank(message = "apellidos cannot be empty")
    @Size(min = 1, max = 100, message = "apellidos must be between 1 and 100 characters")
    private String apellidos;

    @NotNull(message = "horasClase cannot be null")
    @Min(value = 0, message = "horasClase must be greater than or equal to 0")
    @Max(value = 168, message = "horasClase must be less than or equal to 168 (hours in a week)")
    private Integer horasClase;
}

