package br.com.leiturando.repository;

import br.com.leiturando.entity.FriendRequests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequests, Long> {
    Optional<List<FriendRequests>> findAllByRequestedId(Long requestedId);
    Optional<List<FriendRequests>> findAllByRequesterId(Long requesterId);

    @Query("select fr from FriendRequests fr where (requested_id = :requestedId and requester_id = :requesterId) OR (requested_id = :requesterId and requester_id = :requestedId)")
    Optional<FriendRequests> findByRequestedAndRequester(@Param("requestedId") Long requestedId,@Param("requesterId") Long requesterId);
}
