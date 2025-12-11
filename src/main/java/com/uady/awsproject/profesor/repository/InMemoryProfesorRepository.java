package com.uady.awsproject.profesor.repository;

import com.uady.awsproject.profesor.model.Profesor;
import com.uady.awsproject.common.util.IdGenerator;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of ProfesorRepository.
 * This class is kept for reference and testing purposes.
 * The DatabaseProfesorRepository is now the primary implementation.
 */
public class InMemoryProfesorRepository implements ProfesorRepository {

    private final Map<Long, Profesor> storage = new ConcurrentHashMap<>();

    @Override
    public List<Profesor> findAll() {
        return List.copyOf(storage.values());
    }

    @Override
    public Optional<Profesor> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Profesor save(Profesor profesor) {
        if (profesor.getId() == null) {
            profesor.setId(IdGenerator.nextId());
        }
        storage.put(profesor.getId(), profesor);
        return profesor;
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
    public boolean existsByNumeroEmpleado(String numeroEmpleado) {
        return storage.values().stream()
                .anyMatch(profesor -> profesor.getNumeroEmpleado().equals(numeroEmpleado));
    }

    @Override
    public void flush() {
        // No operation needed for in-memory implementation
    }
}

