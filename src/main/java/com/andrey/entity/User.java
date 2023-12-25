package com.andrey.entity;

import com.andrey.type.MyJsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users", schema = "public")
public class User {

    @Id
    private String username;
    private String firstname;
    private String lastname;
//    @Convert(converter = BirthdayConverter.class)
    @Column(name = "birth_day")
    private Birthday birthDay;
    private Integer age;
    @Enumerated(EnumType.STRING)
    private Role role;
//    @JdbcTypeCode(SqlTypes.JSON)
    @Type(MyJsonType.class)
    private MyJson info;

}
