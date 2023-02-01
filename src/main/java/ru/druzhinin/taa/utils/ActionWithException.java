package ru.druzhinin.taa.utils;

@FunctionalInterface
public interface ActionWithException {
    void action() throws Exception;
}