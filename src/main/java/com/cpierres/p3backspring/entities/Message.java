package com.cpierres.p3backspring.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

//@Data //incompatible avec Auditable ! oblige à devoir créer hashcode et equals ? Dommage
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
    private User user;

    @Column(name = "message", length = 2000)
    private String message;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message msg = (Message) o;
        return Objects.equals(id, msg.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}