package com.salon.appointment.mapper;

import com.salon.appointment.dto.response.UserResponse;
import com.salon.appointment.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toUserResponse(User user){
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setName(user.getName());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhone(user.getPhone());
        userResponse.setRole(user.getRole());
        user.setActive(user.isActive());
        userResponse.setCreatedAt(user.getCreatedAt());
        return userResponse;
    }
}
