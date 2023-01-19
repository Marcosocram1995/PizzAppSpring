package com.pizzapp.repository;


import com.pizzapp.data.Ingredient;
import com.pizzapp.data.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PizzaRepository extends JpaRepository<Pizza, Long> {
    Optional<Pizza> findByName(String name);

    Optional<Pizza> findByPrice(Float price);

    List<Pizza> findAllByPriceBetween(Float priceStart, Float priceEnd);

    List<Pizza> findByIngredientsContains(Ingredient ingredients);
}
