package com.pizzapp.data;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    @JdbcTypeCode(SqlTypes.INTEGER)
    private Long id;

    @Column(length = 150, unique = true)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String username;

    @Column(name = "money", length = 5)
    @JdbcTypeCode(SqlTypes.FLOAT)
    private float money;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "client", cascade = CascadeType.PERSIST, orphanRemoval = true)
    @ToString.Exclude
    private List<ShoppingCart> shoppingCarts = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Client client = (Client) o;
        return id != null && Objects.equals(id, client.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}