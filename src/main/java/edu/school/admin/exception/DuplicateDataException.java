package edu.school.admin.exception;

public class DuplicateDataException extends RuntimeException{
    public DuplicateDataException(String msg) {
        super(msg);
    }
}
