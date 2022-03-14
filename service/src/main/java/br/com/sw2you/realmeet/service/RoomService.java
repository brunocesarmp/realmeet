package br.com.sw2you.realmeet.service;

import static java.util.Objects.requireNonNull;

import br.com.sw2you.realmeet.api.model.CreateRoomDTO;
import br.com.sw2you.realmeet.api.model.RoomDTO;
import br.com.sw2you.realmeet.domain.entity.Room;
import br.com.sw2you.realmeet.domain.repository.RoomRepository;
import br.com.sw2you.realmeet.exception.RoomNotFoundException;
import br.com.sw2you.realmeet.mapper.RoomMapper;
import br.com.sw2you.realmeet.validator.RoomValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoomService {

    private final RoomRepository repository;
    private final RoomValidator validator;
    private final RoomMapper mapper;

    public RoomService(RoomRepository repository, RoomValidator validator, RoomMapper mapper) {
        this.repository = repository;
        this.validator = validator;
        this.mapper = mapper;
    }

    public RoomDTO getRoom(Long id) {
        Room room = getActiveRoomOrThrow(id);
        return mapper.fromEntityToDto(room);
    }

    public RoomDTO createRoom(CreateRoomDTO createRoomDTO) {
        validator.validate(createRoomDTO);
        var room = mapper.fromCreateRoomDtoToEntity(createRoomDTO);
        repository.save(room);
        return mapper.fromEntityToDto(room);
    }

    @Transactional
    public void deleteRoom(Long id) {
        getActiveRoomOrThrow(id);
        repository.deactivate(id);
    }

    private Room getActiveRoomOrThrow(Long id) {
        requireNonNull(id);
        return repository
                .findByIdAndActiveIsTrue(id)
                .orElseThrow(() -> new RoomNotFoundException("Room not found: " + 1));
    }

}
