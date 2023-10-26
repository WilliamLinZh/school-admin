package edu.school.admin.filter;

import edu.school.admin.dto.response.ErrorMessage;
import edu.school.admin.exception.DuplicateDataException;
import edu.school.admin.exception.InvalidException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(DuplicateDataException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorMessage handleDuplicateDataRequestException(DuplicateDataException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(ex.getMessage());

        return message;
    }

    @ExceptionHandler(InvalidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage handleInvalidException(InvalidException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(ex.getMessage());

        return message;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleOtherException(Exception ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage("Service encounter internal error.");

        return message;
    }
}
