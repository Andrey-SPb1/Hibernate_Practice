package com.andrey.entity;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "id")
public class Manager extends User {

    private String ProjectName;

    @Builder
    public Manager(Long id, PersonalInfo personalInfo, String username, Integer age, Role role, MyJson info, Company company, Profile profile, List<UserChat> userChats, String projectName) {
        super(id, personalInfo, username, age, role, info, company, profile, userChats);
        ProjectName = projectName;
    }
}
