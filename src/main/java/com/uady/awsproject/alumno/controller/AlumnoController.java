package com.uady.awsproject.alumno.controller;

import com.uady.awsproject.alumno.model.Alumno;
import com.uady.awsproject.alumno.service.AlumnoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/alumnos")
public class AlumnoController {

    private static final Logger log = LoggerFactory.getLogger(AlumnoController.class);

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

    @PostMapping("/{id}/fotoPerfil")
    public ResponseEntity<Alumno> uploadProfilePhoto(
            @PathVariable Long id,
            @RequestParam("foto") org.springframework.web.multipart.MultipartFile file) {
        log.info("Received request to upload profile photo for alumno ID: {}", id);
        log.debug("File parameter: {}", file != null ? file.getOriginalFilename() : "null");

        Alumno updatedAlumno = alumnoService.uploadProfilePhoto(id, file);
        log.info("Profile photo uploaded successfully for alumno ID: {}", id);
        return ResponseEntity.ok(updatedAlumno);
    }

    @PostMapping("/{id}/email")
    public ResponseEntity<Map<String, String>> sendEmail(@PathVariable Long id) {
        alumnoService.sendEmail(id);
        return ResponseEntity.ok(Map.of("message", "Email sent successfully"));
    }

    @PostMapping("/{id}/session/login")
    public ResponseEntity<Map<String, Object>> login(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        String password = request.get("password");
        if (password == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Password is required"));
        }
        try {
            var session = alumnoService.login(id, password);
            return ResponseEntity.ok(Map.of(
                    "sessionString", session.getSessionString(),
                    "id", session.getId(),
                    "alumnoId", session.getAlumnoId(),
                    "fecha", session.getFecha(),
                    "active", session.getActive()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/session/verify")
    public ResponseEntity<Map<String, Object>> verifySession(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        String sessionString = request.get("sessionString");
        if (sessionString == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "sessionString is required"));
        }
        boolean isValid = alumnoService.verifySession(id, sessionString);
        if (isValid) {
            return ResponseEntity.ok(Map.of("valid", true));
        } else {
            return ResponseEntity.badRequest().body(Map.of("valid", false, "error", "Invalid or expired session"));
        }
    }

    @PostMapping("/{id}/session/logout")
    public ResponseEntity<Map<String, String>> logout(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        String sessionString = request.get("sessionString");
        if (sessionString != null) {
            alumnoService.logout(id, sessionString);
        }
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

}