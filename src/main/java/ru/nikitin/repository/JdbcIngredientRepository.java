package ru.nikitin.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.nikitin.entity.Ingredient;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcIngredientRepository implements IngredientRepository{
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcIngredientRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Iterable<Ingredient> findAll() {
        /*
             Метод findAll(), как ожидается, возвращает коллекцию объектов и использует метод query() экземпляра
        JdbcTemplate. Метод query() принимает SQL-запрос, а также реализацию RowMapper из фреймворка Spring для отображения каждой записи
        из набора результатов в объект.
         */
        return jdbcTemplate.query("select id, name, type from Ingredient", this::mapRowToIngredient);
    }

       @Override
    public Optional<Ingredient> findById(String id) {
        /*
        Запрос, который передается методу findById(), напротив, включает предложение where для сравнения значения
        столбца id со значением параметра id, передаваемого методу. Поэтому в вызов query() передается один
        дополнительный аргумент id. При выполнении запроса символ ? в инструкции SQL будет замещен значением этого аргумента.
         */
           List<Ingredient> results = jdbcTemplate.query("select id, name, type from Ingredient were id=?", this::mapRowToIngredient, id);
        return results.size() ==0 ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public Ingredient save(Ingredient ingredient) {
        jdbcTemplate.update("insert into Ingredient (id, name, type) values (?,?,?)",
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getType().toString());
        return ingredient;
    }
    private Ingredient mapRowToIngredient(ResultSet row, int rowNum) throws SQLException {
        return new Ingredient(row.getString("id"),
                row.getString("name"),
                Ingredient.Type.valueOf(row.getString("type")));
    }
}
