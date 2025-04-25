package com.follysitou.movieflix_backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(MovieNotFoundException.class)
        public ProblemDetail handleMovieNotFoundException(MovieNotFoundException ex) {
            return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        }

        @ExceptionHandler(FileExistsException.class)
        public ProblemDetail handleFileExistsException(FileExistsException ex) {
            return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        }

        @ExceptionHandler(EmptyFileException.class)
        public ProblemDetail handleEmptyFileException(EmptyFileException ex) {
            return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        }

        @ExceptionHandler(ResourceNotFoundException.class)
        public ProblemDetail handleResourceNotFoundException(ResourceNotFoundException ex) {
            return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        }

        @ExceptionHandler(TokenExpirationException.class)
        public ProblemDetail handleTokenExpirationExceptionException(TokenExpirationException ex) {
            return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
        }

        @ExceptionHandler(InvalidResourceException.class)
        public ProblemDetail handleInvalidResourceException(InvalidResourceException ex) {
            return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
        }

        @ExceptionHandler(ResourceAlreadyExistsException.class)
        public ProblemDetail handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex) {
            return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        }

        @ExceptionHandler(BadCredentialsException.class)
        public ProblemDetail handleBadCredentialsException(BadCredentialsException ex) {
            return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
}
