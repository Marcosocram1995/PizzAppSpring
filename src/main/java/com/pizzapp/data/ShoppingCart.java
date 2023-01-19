package com.pizzapp.data;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    @JdbcTypeCode(SqlTypes.INTEGER)
    private Long id;

    @Column(name = "total_price", length = 5)
    @JdbcTypeCode(SqlTypes.FLOAT)
    private float totalPrice = 0;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name = "buyed")
    private Boolean buyed;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @ToString.Exclude
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(name = "shopping_cart_pizzas",
            joinColumns = @JoinColumn(name = "shopping_cart_"),
            inverseJoinColumns = @JoinColumn(name = "pizzas_id"))
    private Set<Pizza> pizzas;

    public List<Pizza> getPizzas() {
        return pizzas.stream().toList();
    }

    public void setPizzas(List<Pizza> pizzas) {
        this.pizzas.addAll(pizzas);
    }

    public boolean addPizza(Pizza pizza) {
        boolean added = false;
        if (pizza != null
                && pizza.getPrice() != null
                && pizza.getPrice() > 0
                && pizzas != null) {
            pizzas.add(pizza);
            totalPrice = totalPrice + pizza.getPrice();
            added = true;
        }
        return added;
    }

    public boolean removePizza(Pizza pizza) {
        boolean removed = false;
        if (pizza != null
                && pizza.getPrice() != null
                && pizza.getPrice() > 0
                && pizzas != null) {
            pizzas.forEach(p -> {
                if (Objects.equals(p.getId(), pizza.getId())) {
                    pizzas.remove(p);
                }
            });
            totalPrice = totalPrice - pizza.getPrice();
            removed = true;
        }
        return removed;
    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "id=" + id +
                ", totalPrice=" + totalPrice +
                ", client=" + client +
                ", buyed=" + buyed +
                ", pizzas=" + pizzas.stream().toString() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingCart that = (ShoppingCart) o;
        return Float.compare(that.totalPrice, totalPrice) == 0 && id.equals(that.id) && client.equals(that.client) && buyed.equals(that.buyed) && pizzas.equals(that.pizzas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, totalPrice, client, buyed, pizzas);
    }
}