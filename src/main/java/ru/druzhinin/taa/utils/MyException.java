package ru.druzhinin.taa.utils;

import ru.druzhinin.taa.enums.ErrorsCodes;

public class MyException extends Exception{
    private ErrorsCodes error;
    private String description;

    public MyException(ErrorsCodes error) {
        this.error = error;
        this.description = "";
    }

    public MyException(ErrorsCodes error, String description) {
        this.error = error;
        this.description = description;
    }

    @Override
    public String toString() {
        return "exception:" + error + "(" + description + ")";
    }
}