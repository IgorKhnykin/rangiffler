package guru.ga.rangiffler.data.repository;

import guru.ga.rangiffler.data.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LikeRepository extends JpaRepository<LikeEntity, UUID> {
}