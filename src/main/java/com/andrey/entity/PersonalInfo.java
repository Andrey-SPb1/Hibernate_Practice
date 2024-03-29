package com.andrey.entity;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

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
    @NotNull
    private LocalDate birthDate;

}
