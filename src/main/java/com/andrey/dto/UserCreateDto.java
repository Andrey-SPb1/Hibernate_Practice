package com.andrey.dto;

import com.andrey.entity.PersonalInfo;
import com.andrey.entity.Role;

public record UserCreateDto(PersonalInfo personalInfo,
                            String username,
                            Role role,
                            Integer companyId) {
}
