package com.andrey.mapper;

import com.andrey.dao.CompanyRepository;
import com.andrey.dto.UserCreateDto;
import com.andrey.entity.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserCreateMapper implements Mapper<UserCreateDto, User> {

    private final CompanyRepository companyRepository;

    @Override
    public User mapFrom(UserCreateDto object) {
        return User.builder()
                .personalInfo(object.personalInfo())
                .username(object.username())
                .role(object.role())
                .company(companyRepository.findById(object.companyId()).orElseThrow(IllegalArgumentException::new))
                .build();
    }
}
