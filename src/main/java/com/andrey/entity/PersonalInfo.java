package com.andrey.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Data
public class PersonalInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1199309855326684942L;

    private String firstname;
    private String lastname;
    private Birthday birthDay;

}
