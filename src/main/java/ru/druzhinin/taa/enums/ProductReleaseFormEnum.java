package ru.druzhinin.taa.enums;

import lombok.AllArgsConstructor;

/**
 * класс тип вида производства препарата
 */
@AllArgsConstructor
public enum ProductReleaseFormEnum
{
    NULL(""),
    AMPOULES("Ампулы"),
    PILLS("Таблетки"),
    PILLS_COATED("Таблетки покрытые оболочкой"),
    SOLUTION_FOR_INFUSION("Раствор для инфузий"),
    SOLUTION_FOR_INJECTION("Раствор для инъекций");

    public final String title;

    public static ProductReleaseFormEnum fromString(String text) {
        for (ProductReleaseFormEnum form : ProductReleaseFormEnum.values()) {
            if (form.title.equalsIgnoreCase(text)) {
                return form;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }

    @Override
    public String toString() {
        return title;
    }
}
