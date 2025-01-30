package com.cpierres.p3backspring.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;

//@Data //incompatible avec Auditable ! oblige à devoir créer hashcode et equals ? Dommage
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Setter
@Builder
@Entity
@Table(name = "MESSAGES")
@NoArgsConstructor
@AllArgsConstructor
public class Message extends Auditable {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rental_id")
    private Rental rental;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @CreatedBy
    private User user;

    @Column(name = "message", length = 2000)
    private String message;

}