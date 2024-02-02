package com.andrey.entity;

import com.andrey.type.MyJsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.util.ArrayList;
import java.util.List;

@NamedQuery(name = "findUserByName", query = "select u from User u " +
        "join u.company c " +
        "where u.personalInfo.firstname = :firstname and c.name = :companyName " +
        "order by u.personalInfo.lastname desc ")
@Data
@ToString(exclude = {"company", "profile", "userChats"})
@EqualsAndHashCode(of = "username")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users", schema = "public")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User implements BaseEntity<Long> {

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToOne(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private Profile profile;

    //    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<UserChat> userChats = new ArrayList<>();

}
