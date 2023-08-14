package br.com.leiturando.repository;

import br.com.leiturando.entity.FriendRequests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendRequestRepository extends JpaRepository<FriendRequests, Long> {
    List<FriendRequests> findAllByRequestedId(Long requestedId);
    List<FriendRequests> findAllByRequesterId(Long requesterId);

    @Query("select fr from FriendRequests fr where (requested_id = :requestedId and requester_id = :requesterId) OR (requested_id = :requesterId and requester_id = :requestedId)")
    FriendRequests findByRequestedAndRequester(@Param("requestedId") Long requestedId,@Param("requesterId") Long requesterId);
}
