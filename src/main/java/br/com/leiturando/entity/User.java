package br.com.leiturando.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static br.com.leiturando.domain.Const.SCORE_TO_ADVANCE_LEVEL;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "User_entity")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String image;
    private String name;
    private LocalDateTime dateOfBirth;
    @Column(unique = true)
    private String email;
    @JsonIgnore
    private String password;
    private Integer level;
    private Integer breakthrough;
    private LocalDateTime createdAt;
    private Integer matches;
    private Integer correct;
    private Integer wrong;

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

    public void updateUserStatistics(int correctWordCount, int incorrectWordCount, int score) {
        boolean isAdvancedLevel = this.getBreakthrough() + score >= SCORE_TO_ADVANCE_LEVEL;

        if (isAdvancedLevel) {
            this.setLevel(this.getLevel() + 1);
            this.setBreakthrough(this.getBreakthrough() + score - SCORE_TO_ADVANCE_LEVEL);
        } else {
            this.setBreakthrough(this.getBreakthrough() + score);
        }

        this.setMatches(this.getMatches() + 1);
        this.setCorrect(this.getCorrect() + correctWordCount);
        this.setWrong(this.getWrong() + incorrectWordCount);
    }
}