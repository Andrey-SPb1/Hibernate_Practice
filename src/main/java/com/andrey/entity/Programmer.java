package com.andrey.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("programmer")
public class Programmer extends User {

    @Enumerated(EnumType.STRING)
    private Language language;

    @Builder
    public Programmer(Long id, PersonalInfo personalInfo, String username, Integer age, Role role, MyJson info, Company company, Profile profile, List<UserChat> userChats, Language language) {
        super(id, personalInfo, username, age, role, info, company, profile, userChats);
        this.language = language;
    }
}
