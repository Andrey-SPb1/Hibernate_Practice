package com.andrey;

import com.andrey.entity.Birthday;
import com.andrey.entity.Role;
import com.andrey.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

class HibernateRunnerTest {

    @Test
    void checkReflectionApi() throws SQLException, IllegalAccessException {
        User user = User.builder()
                .username("ivan123")
                .firstname("Ivan")
                .lastname("Ivanov")
                .birthDay(new Birthday(LocalDate.of(1997, 8, 14)))
                .age(new Birthday(LocalDate.of(1997, 8, 14)).getAge())
                .role(Role.ADMIN)
                .build();

        String sql = """
                insert
                into
                %s
                (%s)
                values
                (%s)
                """;

        String table = Optional.ofNullable(user.getClass().getAnnotation(Table.class))
                .map(tableAnnotation -> tableAnnotation.schema() + "." + tableAnnotation.name())
                .orElse(user.getClass().getName());

        Field[] declaredFields = user.getClass().getDeclaredFields();

        String columnNames = Arrays.stream(declaredFields)
                .map(declaredField -> Optional.ofNullable(declaredField.getAnnotation(Column.class))
                        .map(Column::name)
                        .orElse(declaredField.getName()))
                .collect(Collectors.joining(", "));

        String columnValues = Arrays.stream(declaredFields)
                .map(declaredField -> "?")
                .collect(Collectors.joining(", "));

        System.out.printf((sql) + "%n", table, columnNames, columnValues);

        Connection connection = null;
        PreparedStatement preparedStatement = connection.prepareStatement(sql.formatted(table, columnNames, columnValues));

        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            preparedStatement.setObject(1, declaredField.get(user));
        }

    }

}