package com.uady.awsproject.alumno.service;

import com.uady.awsproject.alumno.model.Alumno;

import java.util.List;

public interface AlumnoService {
    List<Alumno> getAll();
    Alumno getById(Long id);
    Alumno create(Alumno alumno);
    Alumno update(Long id, Alumno alumno);
    void delete(Long id);
}

