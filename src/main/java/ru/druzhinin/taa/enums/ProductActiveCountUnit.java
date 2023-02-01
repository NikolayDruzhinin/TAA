package ru.druzhinin.taa.enums;

import lombok.AllArgsConstructor;

/**
 * класс тип вида активного вещества препарата
 */
@AllArgsConstructor
public enum ProductActiveCountUnit
{
    NULL(""),
    MG_IODINE_PER_ML("мг йода/мл"),
    PERCENT("%"),
    MG("мг"),
    MG_PER_ML("мг/мл"),
    M_MOLE_PER_ML("ммоль/мл");

    public final String title;

    public static ProductActiveCountUnit fromString(String text) {
        for (ProductActiveCountUnit unit : ProductActiveCountUnit.values()) {
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
