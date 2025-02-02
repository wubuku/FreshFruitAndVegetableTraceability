package org.dddml.ffvtraceability.restful.config;

import org.dddml.ffvtraceability.specialization.DomainError;
import org.dddml.ffvtraceability.specialization.DomainErrorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(Exception.class)
    ProblemDetail handleAllExceptions(Exception ex) {
        logger.info(ex.getMessage(), ex);
        RuntimeException convertedException = DomainErrorUtils.convertException(ex);

        if (convertedException instanceof DomainError) {
            return ProblemDetail.forStatusAndDetail(
                    HttpStatus.BAD_REQUEST,
                    convertedException.getMessage()
            );
        }

        return ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                convertedException.getMessage()
        );
    }
}