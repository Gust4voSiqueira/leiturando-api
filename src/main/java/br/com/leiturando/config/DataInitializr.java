package br.com.leiturando.config;

import br.com.leiturando.domain.Const;
import br.com.leiturando.entity.Friendship;
import br.com.leiturando.entity.Role;
import br.com.leiturando.entity.User;
import br.com.leiturando.repository.FriendshipRepository;
import br.com.leiturando.repository.RoleRepository;
import br.com.leiturando.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Component
public class DataInitializr implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    FriendshipRepository friendshipRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent arg0) {
        final String usersDefault = "123456";
        List<Friendship> friendshipList = friendshipRepository.findAll();

        Role roleAdmin = new Role(Const.ROLE_ADMIN);
        Role roleClient = new Role(Const.ROLE_CLIENT);

        roleRepository.save(roleAdmin);
        roleRepository.save(roleClient);

        createUser("Gustavo", "gustavosiqueira082@gmail.com", "batman",passwordEncoder.encode(usersDefault), roleAdmin);
        createUser("Brena", "brena@gmail.com", "wonder",passwordEncoder.encode(usersDefault), roleClient);
        createUser("Jonas", "jonas@gmail.com", "joker",passwordEncoder.encode(usersDefault), roleClient);
        createUser("Henrique", "henrique@gmail.com", "superman",passwordEncoder.encode(usersDefault), roleClient);
        createUser("Natan", "natan@gmail.com", "harry",passwordEncoder.encode(usersDefault), roleClient);
        createUser("Felipe", "felipe@gmail.com", "thor",passwordEncoder.encode(usersDefault), roleClient);


        if (friendshipList.isEmpty()) {
            User user1 = userRepository.findByEmail("gustavosiqueira082@gmail.com");
            User user2 = userRepository.findByEmail("brena@gmail.com");
            User user3 = userRepository.findByEmail("jonas@gmail.com");
            User user4 = userRepository.findByEmail("henrique@gmail.com");

            Friendship friendship1 = new Friendship(user1, user2);
            Friendship friendship2 = new Friendship(user1, user3);
            Friendship friendship3 = new Friendship(user4, user2);
            Friendship friendship4 = new Friendship(user4, user3);

            friendshipRepository.save(friendship1);
            friendshipRepository.save(friendship2);
            friendshipRepository.save(friendship3);
            friendshipRepository.save(friendship4);
        }
    }

    public void createUser(String name, String email, String character, String password, Role role) {
        User user = User.builder()
                .name(name)
                .email(email)
                .image(character)
                .level(1)
                .breakthrough(80)
                .password(password)
                .roles(Collections.singletonList(role))
                .createdAt(LocalDateTime.now())
                .wrong(0)
                .correct(0)
                .matches(0)
                .dateOfBirth(LocalDateTime.of(2000, 11, 24, 22, 1))
                .build();
        userRepository.save(user);
    }

}
