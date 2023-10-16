package ru.skillbox.diplom.group40.social.network.impl.resource.advice;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.skillbox.diplom.group40.social.network.impl.exception.AuthException;

@ControllerAdvice
public class AdviceController {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleException(EntityNotFoundException e) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<String> handleException(AuthException e) {
        System.out.println("error e");
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
