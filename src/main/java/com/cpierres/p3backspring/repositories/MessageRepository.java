package com.cpierres.p3backspring.repositories;

import com.cpierres.p3backspring.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Integer> {

}
