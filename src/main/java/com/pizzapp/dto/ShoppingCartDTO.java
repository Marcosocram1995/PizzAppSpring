package com.pizzapp.dto;

import com.pizzapp.data.ShoppingCart;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import static com.pizzapp.dto.PizzaDTO.convertPizzaToDTO;

@Data
public class ShoppingCartDTO {
    private String userName;
    private float userMoney;
    private float totalPrice;
    private List<PizzaDTO> pizzaList;

    public static ShoppingCartDTO convertShoppingCartToDTO(ShoppingCart shoppingCart) {
        ShoppingCartDTO shoppingCartDTO = new ShoppingCartDTO();
        shoppingCartDTO.userName = shoppingCart.getClient().getUsername();
        shoppingCartDTO.userMoney = shoppingCart.getClient().getMoney();
        List<PizzaDTO> pizzaDTOList = new ArrayList<>();
        shoppingCart.getPizzas().forEach(p -> pizzaDTOList.add(convertPizzaToDTO(p)));
        shoppingCartDTO.pizzaList = pizzaDTOList;
        shoppingCartDTO.totalPrice = shoppingCart.getTotalPrice();
        return shoppingCartDTO;
    }
}