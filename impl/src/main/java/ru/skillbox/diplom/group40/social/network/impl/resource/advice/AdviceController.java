package ru.skillbox.diplom.group40.social.network.impl.resource.advice;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.skillbox.diplom.group40.social.network.impl.exception.AccountException;
import ru.skillbox.diplom.group40.social.network.impl.exception.AuthException;
import ru.skillbox.diplom.group40.social.network.impl.exception.TokenException;
@Slf4j
@ControllerAdvice
public class AdviceController {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleException(EntityNotFoundException e) {
        return ResponseEntity.status(404).body(e.getMessage());
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<String> handleException(AuthException e) {
        return ResponseEntity.status(401).body(e.getMessage());
    }

    @ExceptionHandler(AccountException.class)
    public ResponseEntity<String> handleException(AccountException e) {
        log.error(e.toString());
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<String> handleException(TokenException e) {
        return ResponseEntity.status(401).body(e.getMessage());
    }

}
