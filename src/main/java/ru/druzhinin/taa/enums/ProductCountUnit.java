package ru.druzhinin.taa.enums;

import lombok.AllArgsConstructor;

/**
 * класс тип едениц измерения препарата
 */
@AllArgsConstructor
public enum ProductCountUnit
{
    NULL(""),
    ML("мл"),
    L("л"),
    KG("кг"),
    G("г"),
    C("шт");

    public final String title;

    public static ProductCountUnit fromString(String text) {
        for (ProductCountUnit unit : ProductCountUnit.values()) {
            if (unit.title.equalsIgnoreCase(text)) {
                return unit;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }

    @Override
    public String toString() {
        return title;
    }
}