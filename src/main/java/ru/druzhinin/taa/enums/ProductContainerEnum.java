package ru.druzhinin.taa.enums;

import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * класс тип контейнера препарата
 */
@AllArgsConstructor
public enum ProductContainerEnum
{
    BOX("Контейнеры полимерные", ProductTypeEnum.SOLUTION),
    BOTTLE("Бутылки", Arrays.asList(ProductTypeEnum.SOLUTION, ProductTypeEnum.AMPOULE)),
    PILL("Таблетки", ProductTypeEnum.PILL),
    AMPOULE("Ампулы", ProductTypeEnum.AMPOULE);

    public final String title;
    public final List<ProductTypeEnum> relatedTypes;

    ProductContainerEnum(String title, ProductTypeEnum relatedType) {
        this(title, Collections.singletonList(relatedType));
    }

    @Override
    public String toString() {
        return title;
    }
}
