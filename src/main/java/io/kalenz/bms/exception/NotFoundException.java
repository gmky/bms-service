package io.kalenz.bms.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }

    public static NotFoundException of(String entity, String id) {
        return new NotFoundException(entity + " not found with id: " + id);
    }
}
