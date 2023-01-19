package com.pizzapp.data;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Pizza {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;

    @Column(unique = true, length = 50)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String name;

    @Column(name = "price")
    @JdbcTypeCode(SqlTypes.FLOAT)
    private Float price;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "ingredient")
    @CollectionTable(name = "pizza_ingredients", joinColumns = @JoinColumn(name = "ingredient_id"))
    private List<Ingredient> ingredients;

    @Column(name = "image")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String image;

    @Enumerated
    @Column(name = "pizza_dough")
    private Dough dough;
    @Transient
    private static final int MAXIMUMINGREDIENTS = 4;

    @Override
    public String toString() {
        return "Pizza{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", ingredients=" + ingredients +
                ", image='" + image + '\'' +
                ", dough=" + dough +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pizza pizza = (Pizza) o;
        return Objects.equals(id, pizza.id) && Objects.equals(name, pizza.name) && Objects.equals(price, pizza.price) && Objects.equals(ingredients, pizza.ingredients) && Objects.equals(image, pizza.image) && dough == pizza.dough;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, ingredients, image, dough);
    }

    public Pizza(Long id, String name, Float price, List<Ingredient> ingredients, String image, Dough dough) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.ingredients = new ArrayList<>();
        if (ingredients != null) {
            this.ingredients = new ArrayList<>(ingredients);
        }
        this.image = image;
        this.dough = dough;
    }

    public boolean addIngredient(Ingredient ingredient) {
        boolean added = false;
        if (ingredient != null) {
            this.ingredients.add(ingredient);
            added = true;
        }
        return added;
    }

    public boolean removeIngredient(Ingredient ingredient) {
        boolean removed = false;
        if (this.ingredients.contains(ingredient)) {
            this.ingredients.remove(ingredient);
            removed = true;
        }
        return removed;
    }

    public float calculateRecommendedPrice() {
        final float[] recommendedPrice = new float[1];
        this.getIngredients().forEach(ingredient -> {
            switch (ingredient) {
                case JALAPENO -> recommendedPrice[0] = recommendedPrice[0] + Ingredient.JALAPENO_PRICE;
                case CHEDDAR -> recommendedPrice[0] = recommendedPrice[0] + Ingredient.CHEDDAR_PRICE;
                case TOMATO -> recommendedPrice[0] = recommendedPrice[0] + Ingredient.TOMATO_PRICE;
                case PEPPERONI -> recommendedPrice[0] = recommendedPrice[0] + Ingredient.PEPPERONI_PRICE;
            }
        });
        switch (this.dough) {
            case DEEPISH -> recommendedPrice[0] = recommendedPrice[0] + Dough.DEEPISH_PRICE;
            case NEAPOLITAN -> recommendedPrice[0] = recommendedPrice[0] + Dough.NEAPOLITAN_PRICE;
            case ROMAN -> recommendedPrice[0] = recommendedPrice[0] + Dough.ROMAN_PRICE;
        }
        return recommendedPrice[0];
    }
}