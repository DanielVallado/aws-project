package com.uady.awsproject.alumno.repository;

import com.uady.awsproject.alumno.model.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaAlumnoRepository extends JpaRepository<Alumno, Long> {

    boolean existsByMatricula(String matricula);

}
