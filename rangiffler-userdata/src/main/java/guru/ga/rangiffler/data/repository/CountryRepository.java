package guru.ga.rangiffler.data.repository;

import guru.ga.rangiffler.data.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CountryRepository extends JpaRepository<CountryEntity, UUID> {
}
