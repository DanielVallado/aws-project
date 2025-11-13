package com.uady.awsproject.alumno.service;

import com.uady.awsproject.alumno.model.Alumno;
import com.uady.awsproject.alumno.repository.AlumnoRepository;
import com.uady.awsproject.common.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlumnoServiceImpl implements AlumnoService {

    private final AlumnoRepository alumnoRepository;

    public AlumnoServiceImpl(AlumnoRepository alumnoRepository) {
        this.alumnoRepository = alumnoRepository;
    }

    @Override
    public List<Alumno> getAll() {
        return alumnoRepository.findAll();
    }

    @Override
    public Alumno getById(Long id) {
        return alumnoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Alumno with id " + id + " not found"));
    }

    @Override
    public Alumno create(Alumno alumno) {
        alumno.setId(null);

        validateNamesWithoutNumbers(alumno.getNombres(), "nombres");
        validateNamesWithoutNumbers(alumno.getApellidos(), "apellidos");

        if (alumnoRepository.existsByMatricula(alumno.getMatricula())) {
            throw new IllegalArgumentException("Alumno with matricula " + alumno.getMatricula() + " already exists");
        }

        return alumnoRepository.save(alumno);
    }

    @Override
    public Alumno update(Long id, Alumno alumno) {
        Alumno existingAlumno = alumnoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Alumno with id " + id + " not found"));

        validateNamesWithoutNumbers(alumno.getNombres(), "nombres");
        validateNamesWithoutNumbers(alumno.getApellidos(), "apellidos");

        if (!existingAlumno.getMatricula().equals(alumno.getMatricula())
                && alumnoRepository.existsByMatricula(alumno.getMatricula())) {
            throw new IllegalArgumentException("Alumno with matricula " + alumno.getMatricula() + " already exists");
        }

        existingAlumno.setNombres(alumno.getNombres());
        existingAlumno.setApellidos(alumno.getApellidos());
        existingAlumno.setMatricula(alumno.getMatricula());
        existingAlumno.setPromedio(alumno.getPromedio());

        return alumnoRepository.save(existingAlumno);
    }

    @Override
    public void delete(Long id) {
        if (!alumnoRepository.existsById(id)) {
            throw new NotFoundException("Alumno with id " + id + " not found");
        }
        alumnoRepository.deleteById(id);
    }

    private void validateNamesWithoutNumbers(String name, String fieldName) {
        if (name != null && name.matches(".*\\d.*")) {
            throw new IllegalArgumentException(fieldName + " cannot contain numbers");
        }
    }
}

