package com.pizzapp.dto;

import com.pizzapp.data.Ingredient;
import com.pizzapp.data.Pizza;
import lombok.Data;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class PizzaDTO {
    private String name;
    private String price;
    private List<String> ingredients;
    private String dough;
    private String image;

    public static PizzaDTO convertPizzaToDTO(Pizza pizza) {
        PizzaDTO pizzaDTO = new PizzaDTO();
        pizzaDTO.name = "";
        if (pizza.getName() != null) {
            pizzaDTO.name = capitalize(pizza.getName());
        }
        pizzaDTO.ingredients = convertToStringList(pizza.getIngredients());
        pizzaDTO.dough = capitalize(pizza.getDough().name());
        DecimalFormat dfrmt = new DecimalFormat("#,##0.00");
        pizzaDTO.price = dfrmt.format(pizza.getPrice()) + "€";
        pizzaDTO.image = pizza.getImage();
        return pizzaDTO;
    }

    private static List<String> convertToStringList(List<Ingredient> ingredientList) {
        List<String> stringList = new ArrayList<>();
        ingredientList.forEach(p -> stringList.add(" " + capitalize(p.name())));
        return stringList;
    }

    private static String capitalize(String string) {
        String capitalicedWord = "";
        if (string.contains(" ")) {
            capitalicedWord = capitalizeMultipleWords(string);
        } else {
            capitalicedWord = capitalizeSingleWord(string);
        }
        return capitalicedWord;
    }

    private static String capitalizeSingleWord(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
    }

    private static String capitalizeMultipleWords(String string) {
        StringBuilder reformatedString = new StringBuilder();
        String[] strings = string.split(" ");
        for (String s : strings) {
            reformatedString.append(capitalizeSingleWord(s)).append(" ");
        }
        return reformatedString.toString();
    }

    public static List<PizzaDTO> convertPizzaToDTO(List<Pizza> pizzaList) {
        List<PizzaDTO> pizzaDTOList = new ArrayList<>();
        pizzaList.forEach(p -> pizzaDTOList.add(PizzaDTO.convertPizzaToDTO(p)));
        return pizzaDTOList;
    }

    public static PizzaDTO convertPizzaToDTO(Optional<Pizza> optionalPizza) {
        PizzaDTO pizzaDTO = new PizzaDTO();
        if (optionalPizza.isPresent()) {
            pizzaDTO.name = optionalPizza.get().getName();
            pizzaDTO.price = String.valueOf(optionalPizza.get().getPrice());
            pizzaDTO.dough = optionalPizza.get().getDough().name();
            pizzaDTO.ingredients = convertToStringList(optionalPizza.get().getIngredients());
            pizzaDTO.image = optionalPizza.get().getImage();
        }
        return pizzaDTO;
    }
}