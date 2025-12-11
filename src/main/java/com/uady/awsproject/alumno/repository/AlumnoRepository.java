package com.uady.awsproject.alumno.repository;

import com.uady.awsproject.alumno.model.Alumno;

import java.util.List;
import java.util.Optional;

public interface AlumnoRepository {
    List<Alumno> findAll();
    Optional<Alumno> findById(Long id);
    Alumno save(Alumno alumno);
    void deleteById(Long id);
    boolean existsById(Long id);
    boolean existsByMatricula(String matricula);
    void flush();
}

