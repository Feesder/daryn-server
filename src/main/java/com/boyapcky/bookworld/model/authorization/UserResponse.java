package com.boyapcky.bookworld.model.authorization;

import com.boyapcky.bookworld.entity.RoleEntity;
import com.boyapcky.bookworld.entity.UserEntity;
import com.boyapcky.bookworld.model.Role;

import java.util.ArrayList;
import java.util.List;

public class UserResponse {
    Long id;
    String username;
    String email;
    List<Role> roles;

    public UserResponse(Long id, String username, String email, List<Role> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

    public static UserResponse toModel(UserEntity userEntity) {
        return new UserResponse(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getEmail(),
                convertToRoleList(userEntity.getRoles())
        );
    }

    private static List<Role> convertToRoleList(List<RoleEntity> roles) {
        ArrayList<Role> result = new ArrayList<>();
        roles.forEach(roleEntity -> result.add(Role.toModel(roleEntity)));
        return result;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
