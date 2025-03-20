package com.globo.application.services;

import com.globo.application.dtos.RecoveryJwtTokenDto;
import com.globo.application.dtos.UserDto;
import com.globo.application.models.UserModel;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;
import java.util.UUID;

public interface UserService {

    Page<UserModel> findAll(Specification<UserModel> spec,Pageable pageable);

    Optional<UserModel> findById(UUID userId);

    void delete(UserModel userModel);

    void save(UserModel userModel);

    boolean existsByUserName(String username);

    boolean existsByEmail(String email);

    void createUser(UserDto userDto);

    RecoveryJwtTokenDto authenticateUser(UserDto loginUserDto);

    UserModel getLoggedInUsername(HttpServletRequest request);
}
