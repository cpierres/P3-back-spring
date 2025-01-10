package com.cpierres.p3backspring.controller;

import com.cpierres.p3backspring.model.UserDto;
import com.cpierres.p3backspring.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * récupérer les détails d'un utilisateur via son ID.
     *
     * @param id ID de l'utilisateur à rechercher.
     * @return Les détails de l'utilisateur sous la forme d'un UserDetailDto.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Integer id) {
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
}
