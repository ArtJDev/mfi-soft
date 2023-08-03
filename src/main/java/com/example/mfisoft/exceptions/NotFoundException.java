package com.example.mfisoft.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException() {
        super("Статья не найдена");
    }
}
