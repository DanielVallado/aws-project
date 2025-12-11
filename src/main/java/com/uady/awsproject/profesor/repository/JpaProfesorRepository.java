package com.uady.awsproject.profesor.repository;

import com.uady.awsproject.profesor.model.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaProfesorRepository extends JpaRepository<Profesor, Long> {

    boolean existsByNumeroEmpleado(String numeroEmpleado);
}

