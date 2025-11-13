package com.uady.awsproject.alumno.repository;

import com.uady.awsproject.alumno.model.Alumno;
import com.uady.awsproject.common.util.IdGenerator;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryAlumnoRepository implements AlumnoRepository {

    private final Map<Long, Alumno> storage = new ConcurrentHashMap<>();

    @Override
    public List<Alumno> findAll() {
        return List.copyOf(storage.values());
    }

    @Override
    public Optional<Alumno> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Alumno save(Alumno alumno) {
        if (alumno.getId() == null) {
            alumno.setId(IdGenerator.nextId());
        }
        storage.put(alumno.getId(), alumno);
        return alumno;
    }

    @Override
    public void deleteById(Long id) {
        storage.remove(id);
    }

    @Override
    public boolean existsById(Long id) {
        return storage.containsKey(id);
    }

    @Override
    public boolean existsByMatricula(String matricula) {
        return storage.values().stream()
                .anyMatch(alumno -> alumno.getMatricula().equals(matricula));
    }

}