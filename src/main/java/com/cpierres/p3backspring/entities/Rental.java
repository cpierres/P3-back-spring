package com.cpierres.p3backspring.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

//@Data//incompatible avec Auditable ! oblige à devoir créer hashcode et equals ?
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "RENTALS")
@Entity
public class Rental extends Auditable {
//public class Rental {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "surface", precision = 10)
    private BigDecimal surface;

    @Column(name = "price", precision = 10)
    private BigDecimal price;

    @Column(name = "picture")
    private String picture;

    @Column(name = "description", length = 2000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    //TODO val par défaut temporaire à revoir une fois que j'aurai le user connecté
    private User owner = User.builder().id(7).build();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rental rental = (Rental) o;
        return Objects.equals(id, rental.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}