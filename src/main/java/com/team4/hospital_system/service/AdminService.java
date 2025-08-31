package com.team4.hospital_system.service;

import com.team4.hospital_system.dto.response.UserDto;

import java.util.List;

public interface AdminService {
    List<UserDto> listUsers(String role);
    UserDto getUserById(long userId);
    void deleteUser(long adminId, long userId);
}
