package ru.nikitin.repository;

import ru.nikitin.entity.Ingredient;

import java.util.Optional;

/*
Наш репозиторий для хранения объектов Ingredient должен поддерживать следующие операции:
 получение всех ингредиентов в виде коллекции объектов Ingredient;
 получение одного ингредиента по идентификатору;
 сохранение объекта Ingredient.
 */
public interface IngredientRepository {
    Iterable<Ingredient> findAll();
    Optional<Ingredient> findById(String id);
    Ingredient save(Ingredient ingredient);
}
