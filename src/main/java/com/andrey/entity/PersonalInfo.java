package com.andrey.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Data
public class PersonalInfo {

    private String firstname;
    private String lastname;
    private Birthday birthDay;

}
