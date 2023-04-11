package ru.nikitin.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.nikitin.entity.Ingredient;
import ru.nikitin.entity.Ingredient.Type;
import ru.nikitin.entity.Taco;
import ru.nikitin.entity.TacoOrder;
import ru.nikitin.repository.IngredientRepository;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("tacoOrder")
public class DesignTacoController {
    private final IngredientRepository ingredientRepo;

    @Autowired
    public DesignTacoController(IngredientRepository ingredientRepo){
        this.ingredientRepo = ingredientRepo;
    }

    /*
    Метод addIngredientsToModel() извлекает все ингредиенты из базы данных, вызывая метод findAll()
    внедренного экземпляра IngredientRepository. Затем он фильтрует их по типам ингредиентов и добавляет в модель.
     */
@ModelAttribute
    public void addIngredientsToModel(Model model){
//        Не нужно, поскольку используется база данных
//    List<Ingredient> ingredients = Arrays.asList(
//            new Ingredient("FLTO", "Flour Tortilla", Type.WRAP),
//            new Ingredient("COTO", "Corn Tortilla", Type.WRAP),
//            new Ingredient("GRBF", "Ground Beef", Type.PROTEIN),
//            new Ingredient("CARN", "Carnitas", Type.PROTEIN),
//            new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES),
//            new Ingredient("LETC", "Lettuce", Type.VEGGIES),
//            new Ingredient("CHED", "Cheddar", Type.CHEESE),
//            new Ingredient("JACK", "Monterrey Jack", Type.CHEESE),
//            new Ingredient("SLSA", "Salsa", Type.SAUCE),
//            new Ingredient("SRCR", "Sour Cream", Type.SAUCE)
//        );
    Iterable<Ingredient> ingredients = ingredientRepo.findAll();
//    List<Ingredient> ingredients = (List<Ingredient>) ingredientRepo.findAll();
        Type[] types = Ingredient.Type.values();
        for (Type type: types) {
             model.addAttribute(type.toString().toLowerCase(),
             filterByType((List<Ingredient>) ingredients, type));
        }
    }

    @ModelAttribute(name = "tacoOrder")
    public TacoOrder order(){
    return new TacoOrder();
    }
    @ModelAttribute(name = "taco")
    public Taco taco(){
    return new Taco();
    }

    @GetMapping
    public String showDesignForm(){
    return "design";
    }
    @PostMapping
    public String processTaco(@Valid Taco taco, Errors errors, @ModelAttribute TacoOrder tacoOrder){ //с проверкой на валидность
    /* Аннотация @Valid требует выполнить проверку отправленного
    объекта Taco после его привязки к данным в отправленной форме, но
    до начала выполнения тела метода processTaco(). Если обнаружатся
    какие-либо ошибки, то сведения о них будут зафиксированы в объекте Errors, который передается в processTaco(). Первые несколько
    строк в processTaco() проверяют наличие ошибок, вызывая метод
    hasErrors() объекта Errors. Если ошибки есть, то метод processTaco()
    завершает работу без обработки Taco и возвращает имя представления "design", чтобы повторно отобразить форму.
    */
    if(errors.hasErrors()){
        return "design";
    }
    tacoOrder.addTaco(taco);
    log.info("Processing taco: {}", taco);
    return "redirect:/orders/current";
    }

    private Iterable<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
        return ingredients.stream().filter(x->x.getType().equals(type))
            .collect(Collectors.toList());
    }
}
