package ru.nikitin.entity;

import lombok.Data;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.relational.core.mapping.Table;
//import ru.nikitin.data.IngredientRef;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Taco {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date createdAt = new Date();
    @NotNull
    @Size(min=5,message="Name must be at least 5 characters long")
    private String name;
    @ManyToMany()
    @Size(min=1, message="You mast choose at least 1 ingredient")
    private List<Ingredient> ingredients = new ArrayList<>();

    public void addIngredient(Ingredient ingredient){
        this.ingredients.add(ingredient);
    }

}
