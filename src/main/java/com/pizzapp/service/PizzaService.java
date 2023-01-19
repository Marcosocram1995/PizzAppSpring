package com.pizzapp.service;

import com.pizzapp.dto.PizzaDTO;
import com.pizzapp.data.Ingredient;
import com.pizzapp.data.Pizza;
import com.pizzapp.data.Dough;
import com.pizzapp.repository.PizzaRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class PizzaService {

    @Autowired
    PizzaRepository pizzaRepository;

    public boolean savePizza(Pizza pizza) {
        boolean saved = false;
        if (pizza != null
                && !pizza.getName().equals("")
                && !pizza.getIngredients().isEmpty()
                && pizza.getDough() != null
                && pizza.getPrice() >= 0) {
            pizzaRepository.save(pizza);
            saved = true;
        }
        return saved;
    }

    public List<PizzaDTO> getAll() {
        return PizzaDTO.convertPizzaToDTO(pizzaRepository.findAll());
    }

    public PizzaDTO getPizzaByIdDTO(long id) {
        Optional<Pizza> pizza = Optional.empty();
        if (id > 0) {
            pizza = pizzaRepository.findById(id);
        }
        return PizzaDTO.convertPizzaToDTO(pizza);
    }

    public Optional<Pizza> getPizzaById(long id) {
        Optional<Pizza> pizza = Optional.empty();
        if (id > 0) {
            pizza = pizzaRepository.findById(id);
        }
        return pizza;
    }

    public Optional<Pizza> getPizzaByName(String name) {
        Optional<Pizza> pizza = Optional.empty();
        if (name != null) {
            pizza = pizzaRepository.findByName(name);
        }
        return pizza;
    }

    public Optional<Pizza> getPizzaByPrice(Float price) {
        Optional<Pizza> pizza = Optional.empty();
        if (price >= 0) {
            pizza = pizzaRepository.findByPrice(price);
        }
        return pizza;
    }

    public List<Pizza> getPizzasByRange(float minorRange, float mayorRange) {
        List<Pizza> pizzaList = new ArrayList<>();
        if (minorRange >= 0 && mayorRange > minorRange) {
            pizzaList = pizzaRepository.findAllByPriceBetween(minorRange, mayorRange);
        }
        return pizzaList;
    }

    public List<Pizza> getPizzasByIngredient(Ingredient ingredient) {
        List<Pizza> pizzaList = new ArrayList<>();
        if (ingredient != null) {
            pizzaList = pizzaRepository.findByIngredientsContains(ingredient);
        }
        return pizzaList;
    }

    public Pizza createPizza() {
        return Pizza.builder().build();
    }

    public boolean addIngredient(long pizzaId, Ingredient ingredient) {
        boolean added = false;
        if (pizzaId > 0 && ingredient != null) {
            Optional<Pizza> optionalPizza = getPizzaById(pizzaId);
            if (optionalPizza.isPresent()) {
                optionalPizza.get().addIngredient(ingredient);
                savePizza(optionalPizza.get());
                added = true;
            }
        }
        return added;
    }

    public boolean removeIngredient(long pizzaId, Ingredient ingredient) {
        boolean removed = false;
        if (pizzaId > 0 && ingredient != null) {
            Optional<Pizza> optionalPizza = getPizzaById(pizzaId);
            if (optionalPizza.isPresent()) {
                optionalPizza.get().removeIngredient(ingredient);
                savePizza(optionalPizza.get());
                removed = true;
            }
        }
        return removed;
    }

    public boolean setDough(long pizzaId, Dough dough) {
        boolean doughSetted = false;
        if (pizzaId > 0 && dough != null) {
            Optional<Pizza> optionalPizza = getPizzaById(pizzaId);
            if (optionalPizza.isPresent()) {
                optionalPizza.get().setDough(dough);
                savePizza(optionalPizza.get());
                doughSetted = true;
            }
        }
        return doughSetted;
    }

    public List<Dough> getDough() {
        return Arrays.stream(Dough.values()).toList();
    }

    public List<Ingredient> getIngredients() {
        return Arrays.stream(Ingredient.values()).toList();
    }

    public void defaultPizzas() {
        Pizza pizza = Pizza.builder()
                .name("Don Peperonni")
                .ingredients(new ArrayList<>(List.of(Ingredient.PEPPERONI, Ingredient.CHEDDAR, Ingredient.TOMATO)))
                .dough(Dough.NEAPOLITAN)
                .image("https://riotfest.org/wp-content/uploads/2016/10/Pepperoni_1.jpg")
                .build();
        pizza.setPrice(pizza.calculateRecommendedPrice());
        savePizza(pizza);

        pizza = Pizza.builder()
                .name("BIG CHEDDAR")
                .ingredients(new ArrayList<>(List.of(Ingredient.CHEDDAR, Ingredient.CHEDDAR, Ingredient.TOMATO)))
                .dough(Dough.DEEPISH)
                .image("https://pizzanova.com/wp-content/uploads/2016/06/PN_Classic_BnqtCheddar_1920x1440.jpg")
                .build();
        pizza.setPrice(pizza.calculateRecommendedPrice());
        savePizza(pizza);

        pizza = Pizza.builder()
                .name("Jalarpennis")
                .ingredients(new ArrayList<>(List.of(Ingredient.JALAPENO, Ingredient.CHEDDAR, Ingredient.TOMATO)))
                .dough(Dough.DEEPISH)
                .image("https://i.pinimg.com/originals/c0/58/27/c058270cf6eef69568d88d7630336b49.jpg")
                .build();
        pizza.setPrice(pizza.calculateRecommendedPrice());
        savePizza(pizza);

        pizza = Pizza.builder()
                .name("DOUBLE TOMATO")
                .ingredients(new ArrayList<>(List.of(Ingredient.TOMATO, Ingredient.CHEDDAR, Ingredient.TOMATO)))
                .dough(Dough.ROMAN)
                .image("https://i.pinimg.com/originals/4c/2c/0f/4c2c0f446e60deeec1043e8475b4c38f.jpg")
                .build();
        pizza.setPrice(pizza.calculateRecommendedPrice());
        savePizza(pizza);
    }
}