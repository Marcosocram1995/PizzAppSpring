package com.pizzapp;

import com.pizzapp.data.Ingredient;
import com.pizzapp.data.Pizza;
import com.pizzapp.data.Dough;
import com.pizzapp.service.PizzaService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class PizzaServiceTest {
    @Autowired
    private PizzaService pizzaService;

    private static Pizza pizza;

    private static List<Pizza> pizzaList;

    private static boolean savedTwoPizzas;

    @BeforeAll
    public static void createPizzaBefore() {
        String name = "JALAPENNIS";
        List<Ingredient> ingredients = List.of(Ingredient.JALAPENO, Ingredient.TOMATO);
        Dough dough = Dough.DEEPISH;
        pizza = Pizza.builder()
                .name(name)
                .ingredients(ingredients)
                .dough(dough)
                .build();
        pizza.setPrice(pizza.calculateRecommendedPrice());
        savedTwoPizzas = false;
        pizzaList = new ArrayList<>();
    }

    @Test
    void savePizza() {
        assert pizzaService != null && pizza != null;
        Assertions.assertTrue(pizzaService.savePizza(pizza));
    }

    @BeforeEach
    void saveTwoPizzas() {
        assert pizzaList != null;
        if (!savedTwoPizzas) {
            pizzaList.add(pizza);
            savePizza();
            String name = "DON PEPPERONI";
            List<Ingredient> ingredients = List.of(Ingredient.CHEDDAR, Ingredient.PEPPERONI, Ingredient.TOMATO);
            Dough dough = Dough.NEAPOLITAN;
            pizza = Pizza.builder()
                    .name(name)
                    .ingredients(ingredients)
                    .dough(dough)
                    .build();
            pizza.setPrice(pizza.calculateRecommendedPrice());
            pizzaList.add(pizza);
            savePizza();
            savedTwoPizzas = true;
        }
    }

    @Test
    void getAll() {
        assert pizzaList != null && pizzaService != null;
        Assertions.assertEquals(pizzaList.size(), pizzaService.getAll().size());
    }

    @Test
    void getPizzaById() {
        assert pizza != null && pizzaService != null;
        Assertions.assertTrue(pizzaService.getPizzaById(pizza.getId()).isPresent());
    }

    @Test
    void getPizzaByName() {
        assert pizza != null && pizzaService != null;
        Assertions.assertTrue(pizzaService.getPizzaByName(pizza.getName()).isPresent());
    }

    @Test
    void getPizzaByPrice() {
        assert pizza != null && pizzaService != null;
        Assertions.assertTrue(pizzaService.getPizzaByPrice(pizza.getPrice()).isPresent());
    }


    @Test
    void getPizzaByRange() {
        assert pizzaList != null && pizzaService != null;
        float minorRange = 0f;
        float mayorRange = 20f;
        List<Pizza> pizzasByRange = pizzaService.getPizzasByRange(minorRange, mayorRange);
        Assertions.assertEquals(pizzasByRange.size(), pizzaList.size());
    }

    @Test
    void getPizzasByIngredient() {
        assert pizzaList != null && pizzaService != null;
        Ingredient findIngredient = Ingredient.TOMATO;
        List<Pizza> pizzasByIngredients = pizzaService.getPizzasByIngredient(findIngredient);
        Assertions.assertEquals(pizzaList.size(), pizzasByIngredients.size());
    }

    @Test
    void addIngredient() {
        assert pizza != null && pizzaService != null;
        pizzaService.addIngredient(pizza.getId(), Ingredient.JALAPENO);
        Optional<Pizza> pizzaOptional = pizzaService.getPizzaById(pizza.getId());
        assert pizzaOptional.isPresent();
        Assertions.assertEquals(pizza.getIngredients().size() + 1, pizzaOptional.get().getIngredients().size());
    }

    @Test
    void removeIngredient() {
        assert pizza != null && pizzaService != null;
        Optional<Pizza> pizzaOptional = pizzaService.getPizzaById(pizza.getId());
        Ingredient removedIngredient = Ingredient.CHEDDAR;
        assert pizzaOptional.isPresent() && pizzaOptional.get().getIngredients().contains(removedIngredient);
        pizzaService.removeIngredient(pizza.getId(), removedIngredient);
        pizzaOptional = pizzaService.getPizzaById(pizza.getId());
        Assertions.assertEquals(pizza.getIngredients().size() - 1, pizzaOptional.get().getIngredients().size());
    }

    @Test
    void createPizza() {
        assert pizzaService != null;
        Pizza pizzaCreated = new Pizza();
        Pizza pizzaServicePizza = pizzaService.createPizza();
        Assertions.assertEquals(pizzaCreated.getClass(), pizzaServicePizza.getClass());
    }

    @Test
    void setDough() {
        assert pizza != null && pizzaService != null;
        Dough doughSetted = Dough.ROMAN;
        pizzaService.setDough(pizza.getId(), doughSetted);
        Optional<Pizza> pizzaOptional = pizzaService.getPizzaById(pizza.getId());
        assert pizzaOptional.isPresent();
        Assertions.assertEquals(doughSetted.name(), pizzaOptional.get().getDough().name());
    }

    @Test
    void getDoughs() {
        assert pizzaService != null;
        long doughSize = Arrays.stream(Dough.values()).count();
        long serviceDoughSize = pizzaService.getDough().size();
        Assertions.assertEquals(doughSize, serviceDoughSize);
    }

    @Test
    void getIngredients() {
        assert pizzaService != null;
        long ingredientSize = Arrays.stream(Ingredient.values()).count();
        long serviceIngredientSize = pizzaService.getIngredients().size();
        Assertions.assertEquals(ingredientSize, serviceIngredientSize);
    }
}