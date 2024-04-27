package br.com.leiturando.repository;

import br.com.leiturando.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    @Query("SELECT u FROM User u WHERE u.name LIKE :filter%")
    List<User> filterUsers(String filter);

    List<User> findFirst5ByOrderByLevelDesc();
}
