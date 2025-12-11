package com.uady.awsproject.alumno.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "alumno")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Alumno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "nombres cannot be empty")
    @Size(min = 1, max = 100, message = "nombres must be between 1 and 100 characters")
    @Column(name = "nombres", nullable = false, length = 100)
    private String nombres;

    @NotBlank(message = "apellidos cannot be empty")
    @Size(min = 1, max = 100, message = "apellidos must be between 1 and 100 characters")
    @Column(name = "apellidos", nullable = false, length = 100)
    private String apellidos;

    @NotBlank(message = "matricula cannot be empty")
    @Size(min = 1, max = 50, message = "matricula must be between 1 and 50 characters")
    @Column(name = "matricula", nullable = false, unique = true, length = 50)
    private String matricula;

    @NotNull(message = "promedio cannot be null")
    @Column(name = "promedio", nullable = false)
    private Double promedio;

    @Column(name = "password", length = 255)
    private String password;

    @Column(name = "foto_perfil_url", length = 500)
    private String fotoPerfilUrl;

}