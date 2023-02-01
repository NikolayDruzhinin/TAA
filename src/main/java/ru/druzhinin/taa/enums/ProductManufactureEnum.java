package ru.druzhinin.taa.enums;

import lombok.AllArgsConstructor;

/**
 * класс тип производства препарата
 */
@AllArgsConstructor
public enum ProductManufactureEnum
{
    STERILE("ПРОИЗВОДСТВО СТЕРИЛЬНЫХ ПРЕПАРАТОВ"),
    NOT_STERILE("УЧАСТОК НЕСТЕРИЛЬНОГО ПРОИЗВОДСТВА");

    public final String title;

    @Override
    public String toString() {
        return title;
    }
}
