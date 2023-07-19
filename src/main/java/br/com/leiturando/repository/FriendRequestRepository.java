package br.com.leiturando.repository;

import br.com.leiturando.entity.FriendRequests;
import br.com.leiturando.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRequestRepository extends JpaRepository<FriendRequests, Long> {
    List<FriendRequests> findAllByRequestedId(Long requestedId);
    List<FriendRequests> findAllByRequesterId(Long requesterId);

    FriendRequests findByRequestedAndRequester(User requested, User requester);
}
