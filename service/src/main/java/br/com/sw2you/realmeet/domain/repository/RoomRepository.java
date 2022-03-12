package br.com.sw2you.realmeet.domain.repository;

import br.com.sw2you.realmeet.domain.entity.Room;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findByIdAndActiveIsTrue(Long id);

<<<<<<< Updated upstream
<<<<<<< Updated upstream
=======
    Optional<Room> findByNameAndActive(String name, Boolean active);

>>>>>>> Stashed changes
=======
    Optional<Room> findByNameAndActive(String name, Boolean active);

>>>>>>> Stashed changes
}
