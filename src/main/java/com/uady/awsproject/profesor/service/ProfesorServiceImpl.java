package com.uady.awsproject.profesor.service;

import com.uady.awsproject.profesor.model.Profesor;
import com.uady.awsproject.profesor.repository.ProfesorRepository;
import com.uady.awsproject.common.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfesorServiceImpl implements ProfesorService {

    private final ProfesorRepository profesorRepository;

    public ProfesorServiceImpl(ProfesorRepository profesorRepository) {
        this.profesorRepository = profesorRepository;
    }

    @Override
    public List<Profesor> getAll() {
        return profesorRepository.findAll();
    }

    @Override
    public Profesor getById(Long id) {
        return profesorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Profesor with id " + id + " not found"));
    }

    @Override
    public Profesor create(Profesor profesor) {
        profesor.setId(null);

        validateNamesWithoutNumbers(profesor.getNombres(), "nombres");
        validateNamesWithoutNumbers(profesor.getApellidos(), "apellidos");

        if (profesorRepository.existsByNumeroEmpleado(profesor.getNumeroEmpleado())) {
            throw new IllegalArgumentException("Profesor with numeroEmpleado " + profesor.getNumeroEmpleado() + " already exists");
        }

        return profesorRepository.save(profesor);
    }

    @Override
    public Profesor update(Long id, Profesor profesor) {
        Profesor existingProfesor = profesorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Profesor with id " + id + " not found"));

        validateNamesWithoutNumbers(profesor.getNombres(), "nombres");
        validateNamesWithoutNumbers(profesor.getApellidos(), "apellidos");

        if (!existingProfesor.getNumeroEmpleado().equals(profesor.getNumeroEmpleado())
                && profesorRepository.existsByNumeroEmpleado(profesor.getNumeroEmpleado())) {
            throw new IllegalArgumentException("Profesor with numeroEmpleado " + profesor.getNumeroEmpleado() + " already exists");
        }

        existingProfesor.setNumeroEmpleado(profesor.getNumeroEmpleado());
        existingProfesor.setNombres(profesor.getNombres());
        existingProfesor.setApellidos(profesor.getApellidos());
        existingProfesor.setHorasClase(profesor.getHorasClase());

        return profesorRepository.save(existingProfesor);
    }

    @Override
    public void delete(Long id) {
        if (!profesorRepository.existsById(id)) {
            throw new NotFoundException("Profesor with id " + id + " not found");
        }
        profesorRepository.deleteById(id);
    }

    private void validateNamesWithoutNumbers(String name, String fieldName) {
        if (name != null && name.matches(".*\\d.*")) {
            throw new IllegalArgumentException(fieldName + " cannot contain numbers");
        }
    }
}

