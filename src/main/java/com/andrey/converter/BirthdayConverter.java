package com.andrey.converter;

import com.andrey.entity.Birthdate;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.sql.Date;
import java.util.Optional;

@Converter(autoApply = true)
public class BirthdayConverter implements AttributeConverter<Birthdate, Date> {
    @Override
    public Date convertToDatabaseColumn(Birthdate attribute) {
        return Optional.ofNullable(attribute)
                .map(Birthdate::birthDate)
                .map(Date::valueOf)
                .orElse(null);
    }

    @Override
    public Birthdate convertToEntityAttribute(Date dbData) {
        return Optional.ofNullable(dbData)
                .map(Date::toLocalDate)
                .map(Birthdate::new)
                .orElse(null);
    }
}
