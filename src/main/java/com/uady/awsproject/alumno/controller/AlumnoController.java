package com.uady.awsproject.alumno.controller;

import com.uady.awsproject.alumno.model.Alumno;
import com.uady.awsproject.alumno.service.AlumnoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alumnos")
public class AlumnoController {

    private final AlumnoService alumnoService;

    public AlumnoController(AlumnoService alumnoService) {
        this.alumnoService = alumnoService;
    }

    @GetMapping
    public ResponseEntity<List<Alumno>> getAllAlumnos() {
        List<Alumno> alumnos = alumnoService.getAll();
        return ResponseEntity.ok(alumnos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Alumno> getAlumnoById(@PathVariable Long id) {
        Alumno alumno = alumnoService.getById(id);
        return ResponseEntity.ok(alumno);
    }

    @PostMapping
    public ResponseEntity<Alumno> createAlumno(@Valid @RequestBody Alumno alumno) {
        Alumno createdAlumno = alumnoService.create(alumno);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAlumno);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Alumno> updateAlumno(@PathVariable Long id, @Valid @RequestBody Alumno alumno) {
        Alumno updatedAlumno = alumnoService.update(id, alumno);
        return ResponseEntity.ok(updatedAlumno);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlumno(@PathVariable Long id) {
        alumnoService.delete(id);
        return ResponseEntity.ok().build();
    }
}

