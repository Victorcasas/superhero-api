package com.vcasas.superheroapi.exception;

public class SuperHeroeException extends RuntimeException {

    public SuperHeroeException() {
        super();
    }
    public SuperHeroeException(long id) {
        super(("SuperHeroe with id = "+id+" Not Found"));
    }
    public SuperHeroeException(RuntimeException e) {
        super(e.getLocalizedMessage());
    }
    public SuperHeroeException(String message) {
        super(message);
    }
    public SuperHeroeException(Throwable cause) {
        super(cause);
    }
}   