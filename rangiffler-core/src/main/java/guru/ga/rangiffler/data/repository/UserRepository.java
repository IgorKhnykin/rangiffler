package guru.ga.rangiffler.data.repository;

import guru.ga.rangiffler.data.UserEntity;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    @Nonnull
    Optional<UserEntity> findByFirstname(String firstname);

    @Query("select distinct u from UserEntity u join FriendshipEntity f on u = f.requester where f.requester = :requester and f.status = 'ACCEPTED'")
    List<UserEntity> findFriends(@Param("requester") UserEntity requester);
}
