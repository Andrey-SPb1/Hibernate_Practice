package com.andrey.entity;

import com.andrey.type.MyJsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.util.HashSet;
import java.util.Set;

@Data
@ToString(exclude = {"company", "profile", "chats"})
@EqualsAndHashCode(of = "username")
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToOne(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            optional = false)
    private Profile profile;

    @ManyToMany
    @Builder.Default
    @JoinTable(
            name = "users_chat",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "chat_id")
    )
    private Set<Chat> chats = new HashSet<>();

    public void addChat(Chat chat) {
        chats.add(chat);
        chat.getUsers().add(this);
    }
}
