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

import java.util.Arrays;
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

        createUser("Gustavo", "gustavosiqueira082@gmail.com", passwordEncoder.encode(usersDefault), Const.ROLE_ADMIN);
        createUser("Brena", "brena@gmail.com", passwordEncoder.encode(usersDefault), Const.ROLE_ADMIN);
        createUser("Nataniel", "nataniel@gmail.com", passwordEncoder.encode(usersDefault), Const.ROLE_ADMIN);
        createUser("Matias", "matias@gmail.com", passwordEncoder.encode(usersDefault), Const.ROLE_ADMIN);
        createUser("Rogerio", "rogerio@gmail.com", passwordEncoder.encode(usersDefault), Const.ROLE_ADMIN);
        createUser("Felipe", "felipe@gmail.com", passwordEncoder.encode(usersDefault), Const.ROLE_ADMIN);


        if (friendshipList.isEmpty()) {
            User user1 = userRepository.findByEmail("gustavosiqueira082@gmail.com");
            User user2 = userRepository.findByEmail("brena@gmail.com");
            User user3 = userRepository.findByEmail("rogerio@gmail.com");

            Friendship friendship1 = new Friendship(user2, user3);
            Friendship friendship2 = new Friendship(user1, user3);

            friendshipRepository.save(friendship1);
            friendshipRepository.save(friendship2);
        }
    }

    public void createUser(String name, String email, String password, String roleName) {
        Role role = new Role(roleName);
        String imageUrl = "https://avatars.githubusercontent.com/u/79036409?s=400&u=ce42c584cbef8bfb4d2fd972eca98595feaa67d2&v=4";

        roleRepository.save(role);
        User user = new User(name, email, imageUrl, 1, 0, password, Arrays.asList(role));
        userRepository.save(user);
    }

}
