package com.andrey.dto;

import com.andrey.entity.PersonalInfo;
import com.andrey.entity.Role;

public record UserReadDto (Long id,
                           PersonalInfo personalInfo,
                           String username,
                           Role role,
                           CompanyReadDto companyReadDto) {
}
