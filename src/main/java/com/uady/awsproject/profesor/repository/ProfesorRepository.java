package com.uady.awsproject.profesor.repository;

import com.uady.awsproject.profesor.model.Profesor;

import java.util.List;
import java.util.Optional;

public interface ProfesorRepository {
    List<Profesor> findAll();
    Optional<Profesor> findById(Long id);
    Profesor save(Profesor profesor);
    void deleteById(Long id);
    boolean existsById(Long id);
    boolean existsByNumeroEmpleado(String numeroEmpleado);
}

