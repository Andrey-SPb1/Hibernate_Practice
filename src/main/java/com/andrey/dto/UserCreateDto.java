package com.andrey.dto;

import com.andrey.entity.PersonalInfo;
import com.andrey.entity.Role;
import com.andrey.validation.UpdateCheck;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public record UserCreateDto(
        @Valid
        PersonalInfo personalInfo,
        @NotNull
        String username,
        @NotNull(groups = UpdateCheck.class)
        Role role,
        Integer companyId) {
}
