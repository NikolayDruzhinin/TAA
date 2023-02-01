package ru.druzhinin.taa.enums;

import lombok.AllArgsConstructor;

/**
 * класс тип препарата
 */
@AllArgsConstructor
public enum ProductTypeEnum
{
    SOLUTION("Инфузии", ProductManufactureEnum.STERILE), //old: Растворы
    PILL("Таблетки", ProductManufactureEnum.NOT_STERILE),
    AMPOULE("Инъекции", ProductManufactureEnum.STERILE); //old: Ампулы

    public final String title;
    public final ProductManufactureEnum relatedManufacture;

    @Override
    public String toString() {
        return title;
    }
}
