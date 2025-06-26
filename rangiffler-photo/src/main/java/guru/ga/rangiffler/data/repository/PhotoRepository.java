package guru.ga.rangiffler.data.repository;

import guru.ga.rangiffler.data.PhotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PhotoRepository extends JpaRepository<PhotoEntity, UUID> {

    List<PhotoEntity> findAllByUserFirstname(String userFirstname);

    List<PhotoEntity> findAllById(UUID id);

    void removeById(UUID id);
}
