package com.andrey.entity;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.*;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.util.ArrayList;
import java.util.List;

@NamedEntityGraph(name = "withCompany",
        attributeNodes = {
            @NamedAttributeNode("company"),
        })
@NamedEntityGraph(name = "withCompanyAndChat",
        attributeNodes = {
            @NamedAttributeNode("company"),
            @NamedAttributeNode(value = "userChats", subgraph = "chats")
        },
        subgraphs = {
            @NamedSubgraph(name = "chats", attributeNodes = @NamedAttributeNode("chat"))
        })
@FetchProfile(name = "withCompanyAndPayments", fetchOverrides = {
        @FetchProfile.FetchOverride(
                entity = User.class, association = "company", mode = FetchMode.JOIN
        ),
        @FetchProfile.FetchOverride(
                entity = User.class, association = "payments", mode = FetchMode.JOIN
        )
})
@NamedQuery(name = "findUserByName", query = "select u from User u " +
        "join u.company c " +
        "where u.personalInfo.firstname = :firstname and c.name = :companyName " +
        "order by u.personalInfo.lastname desc ")
@Data
@Builder
@ToString(exclude = {"company", "userChats", "payments"})
@EqualsAndHashCode(of = "username")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users", schema = "public")
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Users")
public class User implements BaseEntity<Long>, Comparable<User> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Valid
    @AttributeOverride(name = "birthDate", column = @Column(name = "birth_date"))
    private PersonalInfo personalInfo;

    @NotNull
    @Column(unique = true)
    private String username;

    private Integer age;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
//    @Fetch(value = FetchMode.JOIN)
    private Company company;

//    @OneToOne(
//            mappedBy = "user",
//            cascade = CascadeType.ALL,
//            fetch = FetchType.LAZY)
//    private Profile profile;

    @Builder.Default
    @OneToMany(mappedBy = "user")
    @NotAudited
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private List<UserChat> userChats = new ArrayList<>();

    @Builder.Default
//    @BatchSize(size = 3)
//    @Fetch(value = FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "receiver")
    @NotAudited
    private List<Payment> payments = new ArrayList<>();

    @Override
    public int compareTo(User o) {
        return username.compareTo(o.username);
    }

    public String fullName() {
        return getPersonalInfo().getFirstname() + " " + getPersonalInfo().getLastname();
    }
}
