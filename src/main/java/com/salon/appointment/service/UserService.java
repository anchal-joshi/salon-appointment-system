package com.salon.appointment.service;

import com.salon.appointment.dto.request.ChangePasswordRequest;
import com.salon.appointment.dto.request.CreateStaffRequest;
import com.salon.appointment.dto.request.UpdateProfileRequest;
import com.salon.appointment.dto.response.UserResponse;
import com.salon.appointment.entity.User;
import com.salon.appointment.enums.Role;
import com.salon.appointment.exception.AppException;
import com.salon.appointment.mapper.UserMapper;
import com.salon.appointment.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public UserResponse getMyProfile(User currentUser){
        return userMapper.toUserResponse(currentUser);
    }

    @Transactional
    public UserResponse updateMyProfile(User currentUser, UpdateProfileRequest request){
        currentUser.setName(request.getName());
        currentUser.setPhone(request.getPhone());
        userRepository.save(currentUser);
        return userMapper.toUserResponse(currentUser);
    }

    @Transactional
    public void changePassword(User currentUser, ChangePasswordRequest request){
        if (!passwordEncoder.matches(request.getCurrentPassword(), currentUser.getPassword())){
            throw new AppException("Current Password is incorrect", HttpStatus.BAD_REQUEST);
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())){
            throw new AppException("New password and confirm password do not match", HttpStatus.BAD_REQUEST);
        }

        if (passwordEncoder.matches(request.getNewPassword(), currentUser.getPassword())){
            throw new AppException("New password must differ from current password", HttpStatus.BAD_REQUEST);
        }

        currentUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(currentUser);
    }


    public List<UserResponse>getAllCustomers(){
        return userRepository.findAllByRole(Role.CUSTOMER)
                .stream()
                .map(user -> userMapper.toUserResponse(user))
                .toList();
    }

    public List<UserResponse> getAllStaff(){
        return userRepository.findAllByRole(Role.STAFF)
                .stream()
                .map(user -> userMapper.toUserResponse(user))
                .toList();
    }

    public UserResponse getUserById(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
        return userMapper.toUserResponse(user);
    }

    @Transactional
    public UserResponse createStaff(CreateStaffRequest request){
        if (userRepository.existsByEmail(request.getEmail())){
            throw new AppException("Email already registered", HttpStatus.CONFLICT);
        }
        User staff = new User();
        staff.setName(request.getName());
        staff.setEmail(request.getEmail());
        staff.setPhone(request.getPhone());
        staff.setPassword(passwordEncoder.encode(request.getPassword()));
        staff.setRole(Role.STAFF);
        staff.setActive(true);
        userRepository.save(staff);
        return userMapper.toUserResponse(staff);
    }

    @Transactional
    public void deactiveUser(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new AppException("User not found", HttpStatus.NOT_FOUND));
        if (!user.isActive()){
            throw new AppException("User is already inactive", HttpStatus.BAD_REQUEST);
        }
        user.setActive(false);
        userRepository.save(user);
    }

    @Transactional
    public void reactiveUser(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException("User not found",HttpStatus.NOT_FOUND));

        if (user.isActive()){
            throw new AppException("User is already active", HttpStatus.BAD_REQUEST);
        }
        user.setActive(true);
        userRepository.save(user);
    }


    @Transactional
    public UserResponse changeUserRole(Long id, Role newRole){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
        user.setRole(newRole);
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }
}
