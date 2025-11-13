package com.uady.awsproject.profesor.controller;

import com.uady.awsproject.profesor.model.Profesor;
import com.uady.awsproject.profesor.service.ProfesorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profesores")
public class ProfesorController {

    private final ProfesorService profesorService;

    public ProfesorController(ProfesorService profesorService) {
        this.profesorService = profesorService;
    }

    @GetMapping
    public ResponseEntity<List<Profesor>> getAllProfesores() {
        List<Profesor> profesores = profesorService.getAll();
        return ResponseEntity.ok(profesores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Profesor> getProfesorById(@PathVariable Long id) {
        Profesor profesor = profesorService.getById(id);
        return ResponseEntity.ok(profesor);
    }

    @PostMapping
    public ResponseEntity<Profesor> createProfesor(@Valid @RequestBody Profesor profesor) {
        Profesor createdProfesor = profesorService.create(profesor);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProfesor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Profesor> updateProfesor(@PathVariable Long id, @Valid @RequestBody Profesor profesor) {
        Profesor updatedProfesor = profesorService.update(id, profesor);
        return ResponseEntity.ok(updatedProfesor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfesor(@PathVariable Long id) {
        profesorService.delete(id);
        return ResponseEntity.ok().build();
    }
}

