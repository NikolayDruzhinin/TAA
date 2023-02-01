package ru.druzhinin.taa.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ErrorsCodes {

    INVALID_OPERATION_INPUT("Ошибка ввода"),
    INVALID_FILE_NAME("Ошибка в названии файла"),
    RECORD_NOT_FOUND("Запись не найдена"),
    ERROR_UPDATE_TABLE("Невозможно обновить таблицу"),
    EMPTY_COMBOBOX("Не выбрано значение комбо-бокса"),
    EMPTY_TEXT_FIELD("Не заполнено значение текстового поля");

    public final String title;

    @Override
    public String toString() {
        return title;
    }
}
