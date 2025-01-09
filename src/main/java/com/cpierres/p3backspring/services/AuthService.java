package com.cpierres.p3backspring.services;

import com.cpierres.p3backspring.entities.User;
import com.cpierres.p3backspring.mappers.UserMapper;
import com.cpierres.p3backspring.model.LoginRequest;
import com.cpierres.p3backspring.model.RegisterRequest;
import com.cpierres.p3backspring.model.UserDto;
import com.cpierres.p3backspring.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public Integer login(LoginRequest loginRequest) {
        log.debug("*** AuthService.login ***");
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Email utilisateur non trouvé!"));

        // Vérification du mot de passe
        if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            Integer userId = user.getId();
            log.debug("*** AuthService.login: OK *** => userId = " + userId);
            return user.getId();
        } else {
            log.debug("*** AuthService.login: KO ***");
            throw new RuntimeException("Mot de passe incorrect !");
        }
    }

    public void registerNewUser(RegisterRequest request) {
        // Vérifier si l'utilisateur existe déjà
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Un utilisateur avec cet email existe déjà !");
        }

//        // Créer un nouvel utilisateur avec le mot de passe haché ; mapping MANUEL (lourd et non centralisé)
//        User newUser = new User();
//        newUser.setEmail(request.getEmail());
//        newUser.setName(request.getName() != null ? request.getName() : request.getEmail()); // nom par défaut
//        newUser.setPassword(passwordEncoder.encode(request.getPassword())); // Encrypter le mot de passe

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        //Mapper le RegisterRequest vers entité User via mappping AUTOMATIQUE (mapstruct)
        //en tenant compte du traitement particulier sur le password
        User newUser = userMapper.registerRequestToUser(request,encodedPassword);

        // Sauvegarder en base
        userRepository.save(newUser);
    }

    public UserDto getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé!"));
        //log.debug("*** AuthService.getAuthenticatedUser: OK *** => userDto = " + userMapper.userToUserDto(user));
        return userMapper.userToUserDto(user);
    }
}
