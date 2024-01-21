package com.andrey.entity;

import com.andrey.type.MyJsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

@Data
@ToString(exclude = "company")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users", schema = "public")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @AttributeOverride(name = "birthDay", column = @Column(name = "birth_day"))
    private PersonalInfo personalInfo;

    @Column(unique = true)
    private String username;


    private Integer age;

    @Enumerated(EnumType.STRING)
    private Role role;

//    @JdbcTypeCode(SqlTypes.JSON)
    @Type(MyJsonType.class)
    private MyJson info;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "company_id")
    private Company company;

}
