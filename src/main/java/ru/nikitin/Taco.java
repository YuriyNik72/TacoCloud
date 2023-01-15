package ru.nikitin;

import lombok.Data;
import org.assertj.core.internal.bytebuddy.implementation.bytecode.StackManipulation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class Taco {
    @NotNull
    @Size(min=5,message="Name must be at least 5 characters long")
    private String name;
    @NotNull
    @Size(min=1, message="You mast choose at least 1 ingredient")
    private List<Ingredient> ingredients;



}
