package com.cpierres.p3backspring.services;

import com.cpierres.p3backspring.entities.User;
import com.cpierres.p3backspring.exception.ResourceAlreadyExistException;
import com.cpierres.p3backspring.exception.ResourceNotFoundException;
import com.cpierres.p3backspring.mappers.UserMapper;
import com.cpierres.p3backspring.model.LoginRequest;
import com.cpierres.p3backspring.model.RegisterRequest;
import com.cpierres.p3backspring.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    /**
     * Authentifie un utilisateur en fonction de son e-mail et de son mot de passe.
     * Si l'e-mail de l'utilisateur n'est pas trouvé ou bien si le mot de passe fourni est incorrect,
     * la méthode renvoie null sans lever d'exception.
     * On ne veut pas donner d'indication précise sur la raison précise qui a empêché l'authentification.
     *
     * @param loginRequest La demande de connexion contenant l'e-mail et le mot de passe de l'utilisateur.
     * @return L'ID de l'utilisateur s'il est authentifié avec succès, ou null si ce n'est pas le cas.
     */
    public Integer login(LoginRequest loginRequest) {
        Optional<User> user = userRepository.findByEmail(loginRequest.getEmail());

        if (!user.isEmpty()) {
            // Vérification du mot de passe
            if (passwordEncoder.matches(loginRequest.getPassword(), user.get().getPassword())) {
                Integer userId = user.get().getId();
                log.debug("*** AuthService.login: OK *** => userId = " + userId);
                return user.get().getId();
            }
        }
        return null;
    }

    public User registerNewUser(RegisterRequest request) {
        // Vérifier si l'utilisateur existe déjà
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResourceAlreadyExistException("Un utilisateur avec cet email existe déjà !");
        }

//        // Créer un nouvel utilisateur avec le mot de passe haché ; mapping MANUEL (lourd et non centralisé)
//        User newUser = new User();
//        newUser.setEmail(request.getEmail());
//        newUser.setName(request.getName() != null ? request.getName() : request.getEmail()); // nom par défaut
//        newUser.setPassword(passwordEncoder.encode(request.getPassword())); // Encrypter le mot de passe

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        //Mapper le RegisterRequest vers entité User via mappping AUTOMATIQUE (mapstruct)
        //en tenant compte du traitement particulier sur le password
        User newUser = userMapper.registerRequestToUser(request, encodedPassword);

        // Sauvegarder en base
        return userRepository.save(newUser);
    }

    public User getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        //log.debug("*** AuthService.getAuthenticatedUser: OK *** => userDto = " + userMapper.userToUserDto(user));
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé!"));
    }
}
