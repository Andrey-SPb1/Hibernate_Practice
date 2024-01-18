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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @SequenceGenerator(name = "user_gen", sequenceName = "users_id_seq", allocationSize = 1)
    private Long id;

    @Column(unique = true)
    private String username;

    @Embedded
    @AttributeOverride(name = "birthDay", column = @Column(name = "birth_day"))
    private PersonalInfo personalInfo;

    private Integer age;

    @Enumerated(EnumType.STRING)
    private Role role;

//    @JdbcTypeCode(SqlTypes.JSON)
    @Type(MyJsonType.class)
    private MyJson info;

}
