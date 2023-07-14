package br.com.leiturando.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@Table(name = "User_entity")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String imageUrl;
    private String name;
    @Column(unique = true)
    private String email;
    @JsonIgnore
    private String password;
    private Integer level;
    private Integer breakthrough;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="user_role",
            joinColumns=@JoinColumn(name="user_id"),
            inverseJoinColumns=@JoinColumn(name="role_id")
    )
    private List<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Friendship> friendships = new ArrayList<>();

    public User(User user) {
        super();
        this.name = user.getName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.roles = user.getRoles();
        this.id = user.getId();
    }

    public User(String name, String email, String imageUrl, Integer level, Integer breakthrough, String password, List<Role> roles) {
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.level = level;
        this.breakthrough = breakthrough;
        this.roles = roles;
        this.password = password;
    }

    public User() {}
}