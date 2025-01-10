package com.cpierres.p3backspring.services;

import com.cpierres.p3backspring.entities.User;
import com.cpierres.p3backspring.mappers.UserMapper;
import com.cpierres.p3backspring.model.UserDto;
import com.cpierres.p3backspring.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    /**
     * Récupérer les détails d'un utilisateur via son ID.
     *
     * @param id ID de l'utilisateur.
     * @return UserDetailDto contenant les informations utilisateur.
     */
    public UserDto getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable pour l'ID : " + id));
        return userMapper.userToUserDto(user);
    }
}
