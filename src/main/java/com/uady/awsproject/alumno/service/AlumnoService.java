package com.uady.awsproject.alumno.service;

import com.uady.awsproject.alumno.model.Alumno;
import com.uady.awsproject.alumno.model.AlumnoSession;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AlumnoService {
    List<Alumno> getAll();
    Alumno getById(Long id);
    Alumno create(Alumno alumno);
    Alumno update(Long id, Alumno alumno);
    void delete(Long id);
    Alumno uploadProfilePhoto(Long id, MultipartFile file);
    void sendEmail(Long id);
    AlumnoSession login(Long id, String password);
    boolean verifySession(Long id, String sessionString);
    void logout(Long id, String sessionString);
}

