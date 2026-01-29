package com.carpooling.service;

import com.carpooling.dto.request.UserRegisterRequest;
import com.carpooling.dto.response.UserResponse;
import com.carpooling.entity.User;

public interface UserService {
    UserResponse register(UserRegisterRequest request);
    UserResponse getUserById(Long userId);
    User getUserByUsername(String username);
}
