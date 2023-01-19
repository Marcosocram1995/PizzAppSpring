package com.pizzapp;

import com.pizzapp.data.*;
import com.pizzapp.service.ClientService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
class ClientServiceTest {
    @Autowired
    private ClientService clientService;
    private static Client client;
    private ShoppingCart shoppingCart;

    @BeforeAll
    public static void createClient() {
        long id = 1;
        String name = "Marcos";
        float money = 15f;
        client = Client.builder()
                .id(id)
                .username(name)
                .money(money)
                .build();
    }

    @BeforeEach
    void save() {
        long id = 1;
        assert clientService != null;
        clientService.saveClient(client);
        shoppingCart = ShoppingCart.builder()
                .id(id)
                .client(client)
                .buyed(false)
                .pizzas(new HashSet<>())
                .build();
        client.setShoppingCarts(new ArrayList<>(Arrays.asList(shoppingCart)));
        clientService.saveShoppingCart(shoppingCart);
    }

    @Test
    void findById() {
        assert clientService != null && client != null;
        Assertions.assertTrue(clientService.getClientById(client.getId()).isPresent());
    }

    @Test
    void deleteClientById() {
        assert clientService != null && client != null;
        Assertions.assertTrue(clientService.deleteClientById(client.getId()));
    }

    @Test
    void findByUsername() {
        assert clientService != null && client != null;
        Assertions.assertTrue(clientService.getClientByUsername(client.getUsername()).isPresent());
    }

    @Test
    void getClientByShoopingCartId() {
        assert clientService != null && shoppingCart != null;
        Assertions.assertTrue(clientService.getClientByShoopingCartId(shoppingCart.getId()).isPresent());
    }

    @Test
    void getShoppingCartsByClientId() {
        assert clientService != null && client != null;
        Optional<List<ShoppingCart>> shoppingCartList = clientService.getShoppingCartsByClientId(client.getId());
        assert shoppingCartList.isPresent();
        Assertions.assertTrue(shoppingCartList.get().contains(shoppingCart));
    }

    @Test
    void addPizza() {
        assert clientService != null && shoppingCart != null;
        Pizza pizza = Pizza.builder()
                .ingredients(List.of(Ingredient.TOMATO, Ingredient.CHEDDAR))
                .dough(Dough.NEAPOLITAN)
                .build();
        pizza.setPrice(pizza.calculateRecommendedPrice());
        clientService.addPizzaToShoppingCart(shoppingCart.getId(), pizza);
        Optional<ShoppingCart> optionalShoppingCart = clientService.getShoopingCartById(shoppingCart.getId());
        assert optionalShoppingCart.isPresent();
        Assertions.assertFalse(optionalShoppingCart.get().getPizzas().isEmpty());
    }

    @Test
    void removePizza() {
        assert clientService != null && shoppingCart != null;
        addPizza();
        Optional<ShoppingCart> optionalShoppingCart = clientService.getShoopingCartById(shoppingCart.getId());
        assert optionalShoppingCart.isPresent();
        Pizza pizza = optionalShoppingCart.get().getPizzas().get(0);
        clientService.removePizzaToShoppingCart(shoppingCart.getId(), pizza);
        optionalShoppingCart = clientService.getShoopingCartById(shoppingCart.getId());
        assert optionalShoppingCart.isPresent();
        Assertions.assertTrue(optionalShoppingCart.get().getPizzas().isEmpty());
    }

    @Test
    void testBuy() {
        assert shoppingCart != null && client != null & clientService != null;
        addPizza();
        Optional<ShoppingCart> optionalShoppingCart = clientService.getShoopingCartById(shoppingCart.getId());
        assert optionalShoppingCart.isPresent();
        float diffenceMoney = client.getMoney() - optionalShoppingCart.get().getTotalPrice();
        clientService.buy(shoppingCart.getId());
        Optional<Client> optionalClient = clientService.getClientById(client.getId());
        assert optionalClient.isPresent();
        Assertions.assertEquals(optionalClient.get().getMoney(), diffenceMoney);
    }

    @Test
    void testState() {
        assert clientService != null && shoppingCart != null;
        addPizza();
        clientService.buy(shoppingCart.getId());
        Optional<ShoppingCart> optionalShoppingCart = clientService.getShoopingCartById(shoppingCart.getId());
        assert optionalShoppingCart.isPresent();
        Assertions.assertTrue(optionalShoppingCart.get().getBuyed());
    }

    @Test
    void addIngredient() {
        assert clientService != null && shoppingCart != null;
        addPizza();
        Optional<ShoppingCart> optionalShoppingCart = clientService.getShoopingCartById(shoppingCart.getId());
        assert optionalShoppingCart.isPresent();
        Pizza firstPizza = optionalShoppingCart.get().getPizzas().get(0);
        clientService.addIngredient(firstPizza.getId(), Ingredient.JALAPENO, shoppingCart.getId());
        optionalShoppingCart = clientService.getShoopingCartById(shoppingCart.getId());
        assert optionalShoppingCart.isPresent();
        Pizza secondPizza = optionalShoppingCart.get().getPizzas().get(0);
        Assertions.assertEquals(firstPizza.getIngredients().size() + 1, secondPizza.getIngredients().size());
    }

    @Test
    void removeIngredient() {
        assert clientService != null && shoppingCart != null;
        addPizza();
        Optional<ShoppingCart> optionalShoppingCart = clientService.getShoopingCartById(shoppingCart.getId());
        assert optionalShoppingCart.isPresent();
        Pizza firstPizza = optionalShoppingCart.get().getPizzas().get(0);
        Ingredient removedIngredient = Ingredient.CHEDDAR;
        assert firstPizza.getIngredients().contains(removedIngredient);
        clientService.removeIngredient(firstPizza.getId(), removedIngredient, shoppingCart.getId());
        optionalShoppingCart = clientService.getShoopingCartById(shoppingCart.getId());
        assert optionalShoppingCart.isPresent();
        Pizza secondPizza = optionalShoppingCart.get().getPizzas().get(0);
        Assertions.assertEquals(firstPizza.getIngredients().size() - 1, secondPizza.getIngredients().size());
    }
}