package com.andrey;

import com.andrey.entity.*;
import com.andrey.util.HibernateUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.Cleanup;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

class HibernateRunnerTest {

    @Test
    void checkManyToMany() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            User user = session.get(User.class, 10L);
            Chat chat = session.get(Chat.class, 1L);

            UserChat userChat = UserChat.builder()
                    .createdAt(Instant.now())
                    .createdBy(user.getUsername())
                    .build();

            userChat.setUser(user);
            userChat.setChat(chat);

            session.persist(userChat);

            session.getTransaction().commit();
        }
    }

    @Test
    void checkOneToOne() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            User user = session.get(User.class, 10L);
            System.out.println(user);

//            User user = User.builder()
//                    .username("max@gmail.com")
//                    .build();
//            Profile profile = Profile.builder()
//                    .language("RU")
//                    .street("Lenina")
//                    .build();
//
//            profile.setUser(user);
//            session.persist(user);

            session.getTransaction().commit();
        }
    }

    @Test
    void checkOrhanRemoval() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Company company = session.get(Company.class, 1);
            company.getUsers().removeIf(user -> user.getId().equals(4L));

            session.getTransaction().commit();
        }
    }

    @Test
    void testLazyInitialization() {
        Company company;

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            company = session.get(Company.class, 1);
            Hibernate.initialize(company.getUsers());

            session.getTransaction().commit();
        }
        Set<User> users = company.getUsers();
        System.out.println(users);
    }

    @Test
    void deleteCompany() {
        @Cleanup SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        Company company = session.get(Company.class, 4);
        session.remove(company);

        session.getTransaction().commit();
    }

    @Test
    void addUserToNewCompany() {
        @Cleanup SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        User user = User.builder()
                .username("sveta123")
                .build();

        Company company = Company.builder()
                .name("Nvidia")
                .build();

        company.addUser(user);

        session.persist(company);

        session.getTransaction().commit();
    }

    @Test
    void oneToMany() {
        @Cleanup SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        Company company = session.get(Company.class, 1);
        System.out.println(company);

        session.getTransaction().commit();
    }

    @Test
    void checkReflectionApi() throws SQLException, IllegalAccessException {
        User user = User.builder()
                .username("ivan123")
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