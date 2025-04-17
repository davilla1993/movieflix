package com.follysitou.movieflix_backend.exceptions;

public class FileExistsException  extends RuntimeException {

    public FileExistsException(String message) {
        super(message);
    }
}
