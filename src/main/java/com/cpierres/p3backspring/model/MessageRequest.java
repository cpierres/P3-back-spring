package com.cpierres.p3backspring.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MessageRequest {
    private Integer rental_id;
    private Integer user_id;
    public String message;
}