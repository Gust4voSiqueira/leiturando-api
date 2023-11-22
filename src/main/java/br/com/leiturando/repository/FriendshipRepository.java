package br.com.leiturando.repository;

import br.com.leiturando.entity.Friendship;
import br.com.leiturando.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    @Query("SELECT f FROM Friendship f WHERE f.user = :user OR f.friend = :user")
    List<Friendship> findAllByUserOrFriend(@Param("user") User user);

    @Query("select f from Friendship f WHERE f.user = :myUser AND friend = :idFriend OR user = :idFriend AND friend = :myUser")
    Optional<Friendship> findFriendshipById(User idFriend, User myUser);

    @Query("select f from Friendship f WHERE f.user = :myUser ORDER BY f.friend.level DESC")
    List<Friendship> findFirst5UserByOrderByLevelDesc(User myUser);

    @Query("select f from Friendship f WHERE f.friend = :myUser ORDER BY f.user.level DESC")
    List<Friendship> findFirst5FriendByOrderByLevelDesc(User myUser);

    @Query("SELECT f FROM Friendship f WHERE (f.user = :myUser OR f.friend = :myUser) ORDER BY CASE WHEN f.user = :myUser THEN f.friend.level ELSE f.user.level END DESC")
    List<Friendship> findFirst4UsersAndFriendsOrderByLevelDesc(@Param("myUser") User myUser, Pageable pageable);
}
