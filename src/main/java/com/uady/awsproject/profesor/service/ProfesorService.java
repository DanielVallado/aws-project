package com.uady.awsproject.profesor.service;

import com.uady.awsproject.profesor.model.Profesor;

import java.util.List;

public interface ProfesorService {
    List<Profesor> getAll();
    Profesor getById(Long id);
    Profesor create(Profesor profesor);
    Profesor update(Long id, Profesor profesor);
    void delete(Long id);
}

