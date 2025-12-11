package com.uady.awsproject.alumno.repository;

import com.uady.awsproject.alumno.model.Alumno;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Primary
public class DatabaseAlumnoRepository implements AlumnoRepository {

    private final JpaAlumnoRepository jpaAlumnoRepository;

    public DatabaseAlumnoRepository(JpaAlumnoRepository jpaAlumnoRepository) {
        this.jpaAlumnoRepository = jpaAlumnoRepository;
    }

    @Override
    public List<Alumno> findAll() {
        return jpaAlumnoRepository.findAll();
    }

    @Override
    public Optional<Alumno> findById(Long id) {
        return jpaAlumnoRepository.findById(id);
    }

    @Override
    public Alumno save(Alumno alumno) {
        return jpaAlumnoRepository.save(alumno);
    }

    @Override
    public void deleteById(Long id) {
        jpaAlumnoRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaAlumnoRepository.existsById(id);
    }

    @Override
    public boolean existsByMatricula(String matricula) {
        return jpaAlumnoRepository.existsByMatricula(matricula);
    }

    @Override
    public void flush() {
        jpaAlumnoRepository.flush();
    }

}