package com.globo.application.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.globo.application.models.RoleModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
public class UserDto {

    private String username;

    private String phoneNumber;

    private String address;

    private String email;

    private String password;

    private String oldPassword;

    private String imageUrl;

    private List<RoleModel> roles;

    private boolean disable;
}
