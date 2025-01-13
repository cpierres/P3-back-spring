package com.cpierres.p3backspring.entities;

import jakarta.persistence.*;
import lombok.*;

//@Data//incompatible avec Auditable
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
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
    private Integer surface;

    @Column(name = "price", precision = 10)
    private Integer price;

    @Column(name = "picture")
    private String picture;

    @Column(name = "description", length = 2000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

}