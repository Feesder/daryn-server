package com.boyapcky.bookworld.service;

import com.boyapcky.bookworld.entity.RoleEntity;
import com.boyapcky.bookworld.entity.Status;
import com.boyapcky.bookworld.entity.UserEntity;
import com.boyapcky.bookworld.exception.WrongPasswordException;
import com.boyapcky.bookworld.model.authorization.*;
import com.boyapcky.bookworld.repository.RoleRepository;
import com.boyapcky.bookworld.repository.UserRepository;
import com.boyapcky.bookworld.security.JwtTokenProvider;
import com.boyapcky.bookworld.security.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            BCryptPasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public List<UserResponse> getAll() {
        ArrayList<UserResponse> users = new ArrayList<>();
        userRepository.findAll().forEach(
                userEntity -> {
                    users.add(UserResponse.toModel(userEntity));
                }
        );

        return users;
    }

    public UserResponse findByUsername(String username) {
        UserEntity userEntity = userRepository.findByUsername(username);
        return UserResponse.toModel(userEntity);
    }

    public UserResponse findById(Long id) {
        UserEntity userEntity = userRepository.findById(id).get();
        return UserResponse.toModel(userEntity);
    }

    public void delete(Long id) throws Exception {
        UserEntity userEntity = userRepository.findById(id).get();

        if (userEntity == null) {
            throw new Exception("User with id " + id + " not found");
        }

        userEntity.setUpdated(new Date());
        userEntity.setStatus(Status.DELETED);
        userRepository.save(userEntity);
    }
}
