package com.uady.awsproject.profesor.repository;

import com.uady.awsproject.profesor.model.Profesor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Primary
public class DatabaseProfesorRepository implements ProfesorRepository {

    private final JpaProfesorRepository jpaProfesorRepository;

    public DatabaseProfesorRepository(JpaProfesorRepository jpaProfesorRepository) {
        this.jpaProfesorRepository = jpaProfesorRepository;
    }

    @Override
    public List<Profesor> findAll() {
        return jpaProfesorRepository.findAll();
    }

    @Override
    public Optional<Profesor> findById(Long id) {
        return jpaProfesorRepository.findById(id);
    }

    @Override
    public Profesor save(Profesor profesor) {
        return jpaProfesorRepository.save(profesor);
    }

    @Override
    public void deleteById(Long id) {
        jpaProfesorRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaProfesorRepository.existsById(id);
    }

    @Override
    public boolean existsByNumeroEmpleado(String numeroEmpleado) {
        return jpaProfesorRepository.existsByNumeroEmpleado(numeroEmpleado);
    }

    @Override
    public void flush() {
        jpaProfesorRepository.flush();
    }

}