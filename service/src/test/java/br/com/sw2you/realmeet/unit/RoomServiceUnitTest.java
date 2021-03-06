package br.com.sw2you.realmeet.unit;

import static br.com.sw2you.realmeet.util.MapperUtils.roomMapper;
import static br.com.sw2you.realmeet.util.TestConstants.DEFAULT_ROOM_ID;
import static br.com.sw2you.realmeet.util.TestDataCreator.newCreateRoomDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import br.com.sw2you.realmeet.core.BaseUnitTest;
import br.com.sw2you.realmeet.domain.repository.RoomRepository;
import br.com.sw2you.realmeet.exception.RoomNotFoundException;
import br.com.sw2you.realmeet.service.RoomService;
import br.com.sw2you.realmeet.util.TestDataCreator;
import br.com.sw2you.realmeet.validator.RoomValidator;
import java.util.Optional;

class RoomServiceUnitTest extends BaseUnitTest {

    private RoomService victim;

    @Mock
    private RoomRepository repository;

    @Mock
    private RoomValidator validator;

    @BeforeEach
    void setupEach() {
        victim = new RoomService(repository, validator, roomMapper());
    }

    @Test
    void testGetRoomWithSuccess() {
        var room = TestDataCreator.newRoomBuilder().id(DEFAULT_ROOM_ID).build();
        when(repository.findByIdAndActiveIsTrue(DEFAULT_ROOM_ID)).thenReturn(Optional.of(room));

        var dto = victim.getRoom(DEFAULT_ROOM_ID);

        assertEquals(room.getId(), dto.getId());
        assertEquals(room.getName(), dto.getName());
        assertEquals(room.getSeats(), dto.getSeats());
    }

    @Test
    void testGetRoomNotFound() {
        when(repository.findByIdAndActiveIsTrue(DEFAULT_ROOM_ID)).thenReturn(Optional.empty());
        assertThrows(RoomNotFoundException.class, () -> victim.getRoom(DEFAULT_ROOM_ID));
    }

    @Test
    void testCreateRoomSuccess() {
        var createRoomDto = newCreateRoomDto();
        var roomDto = victim.createRoom(createRoomDto);

        assertEquals(createRoomDto.getName(), roomDto.getName());
        assertEquals(createRoomDto.getSeats(), roomDto.getSeats());
        verify(repository).save(any());
    }

}
