package com.uady.awsproject.alumno.service;

import com.uady.awsproject.alumno.model.Alumno;
import com.uady.awsproject.alumno.model.AlumnoSession;
import com.uady.awsproject.alumno.repository.AlumnoRepository;
import com.uady.awsproject.common.exception.NotFoundException;
import com.uady.awsproject.common.service.DynamoDbSessionService;
import com.uady.awsproject.common.service.S3Service;
import com.uady.awsproject.common.service.SnsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class AlumnoServiceImpl implements AlumnoService {

    private static final Logger log = LoggerFactory.getLogger(AlumnoServiceImpl.class);

    private final AlumnoRepository alumnoRepository;
    private final S3Service s3Service;
    private final SnsService snsService;
    private final DynamoDbSessionService sessionService;

    public AlumnoServiceImpl(
            AlumnoRepository alumnoRepository,
            S3Service s3Service,
            SnsService snsService,
            DynamoDbSessionService sessionService) {
        this.alumnoRepository = alumnoRepository;
        this.s3Service = s3Service;
        this.snsService = snsService;
        this.sessionService = sessionService;
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
        // Ignore incoming id - database will generate it
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

    @Override
    @Transactional
    public Alumno uploadProfilePhoto(Long id, MultipartFile file) {
        log.info("Starting uploadProfilePhoto for alumno ID: {}", id);
        log.debug("File details - name: {}, size: {}, contentType: {}, empty: {}",
            file != null ? file.getOriginalFilename() : "null",
            file != null ? file.getSize() : 0,
            file != null ? file.getContentType() : "null",
            file != null ? file.isEmpty() : true);

        Alumno alumno = alumnoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Alumno with id " + id + " not found"));

        // Validate file
        if (file == null || file.isEmpty()) {
            log.error("File is null or empty");
            throw new IllegalArgumentException("File cannot be empty");
        }

        try {
            // Upload to S3
            log.info("Uploading file to S3 for alumno ID: {}", id);
            String photoUrl = s3Service.uploadFile(file, "alumnos");
            log.info("File uploaded successfully, URL: {}", photoUrl);

            // Update alumno
            alumno.setFotoPerfilUrl(photoUrl);
            Alumno saved = alumnoRepository.save(alumno);

            // Flush to ensure immediate persistence
            alumnoRepository.flush();

            log.info("Alumno profile photo updated successfully for ID: {}", id);

            // Return the saved entity to ensure the URL is persisted
            return saved;
        } catch (Exception e) {
            log.error("Error uploading profile photo for alumno ID: {}", id, e);
            throw new RuntimeException("Error uploading profile photo: " + e.getMessage(), e);
        }
    }

    @Override
    public void sendEmail(Long id) {
        Alumno alumno = alumnoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Alumno with id " + id + " not found"));

        String subject = "Calificación de Alumno - " + alumno.getNombres() + " " + alumno.getApellidos();
        String message = String.format(
                "Nombre: %s %s\nMatrícula: %s\nPromedio: %.2f",
                alumno.getNombres(),
                alumno.getApellidos(),
                alumno.getMatricula(),
                alumno.getPromedio()
        );

        snsService.publishMessage(subject, message);
    }

    @Override
    public AlumnoSession login(Long id, String password) {
        Alumno alumno = alumnoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Alumno with id " + id + " not found"));

        if (alumno.getPassword() == null || !alumno.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid password");
        }

        return sessionService.createSession(id);
    }

    @Override
    public boolean verifySession(Long id, String sessionString) {
        return sessionService.isSessionValid(sessionString, id);
    }

    @Override
    public void logout(Long id, String sessionString) {
        sessionService.deactivateSession(sessionString, id);
    }

    private void validateNamesWithoutNumbers(String name, String fieldName) {
        if (name != null && name.matches(".*\\d.*")) {
            throw new IllegalArgumentException(fieldName + " cannot contain numbers");
        }
    }

}