package com.andrey.entity;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public record Birthdate(LocalDate birthDate) {

    public int getAge() {
        return (int) ChronoUnit.YEARS.between(birthDate, LocalDate.now());
    }

}
