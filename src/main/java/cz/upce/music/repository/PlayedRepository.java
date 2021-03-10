package cz.upce.music.repository;

import cz.upce.music.entity.Played;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayedRepository extends JpaRepository<Played, Long> {
}
