package br.com.sw2you.realmeet.integration;

import static br.com.sw2you.realmeet.util.TestConstants.DEFAULT_ROOM_ID;
<<<<<<< Updated upstream
=======
import static br.com.sw2you.realmeet.util.TestDataCreator.newCreateRoomDto;
>>>>>>> Stashed changes
import static br.com.sw2you.realmeet.util.TestDataCreator.newRoomBuilder;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import br.com.sw2you.realmeet.api.facade.RoomApi;
import br.com.sw2you.realmeet.core.BaseIntegrationTest;
import br.com.sw2you.realmeet.domain.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

class RoomApiIntegrationTest extends BaseIntegrationTest {

    @Autowired
<<<<<<< Updated upstream
    private RoomApi roomApi;
=======
    private RoomApi api;
>>>>>>> Stashed changes

    @Autowired
    private RoomRepository roomRepository;

    @Override
    protected void setupEach() throws Exception {
<<<<<<< Updated upstream
        setLocalhostBasePath(roomApi.getApiClient(), "/v1");
=======
        setLocalhostBasePath(api.getApiClient(), "/v1");
>>>>>>> Stashed changes
    }

    @Test
    void testGetRoomSuccess() {
        var room = newRoomBuilder().build();
        roomRepository.saveAndFlush(room);

        assertNotNull(room.getId());
        assertTrue(room.getActive());

<<<<<<< Updated upstream
        var dto = roomApi.getRoom(room.getId());
=======
        var dto = api.getRoom(room.getId());
>>>>>>> Stashed changes
        assertEquals(room.getId(), dto.getId());
        assertEquals(room.getName(), dto.getName());
        assertEquals(room.getSeats(), dto.getSeats());
    }

    @Test
    void testGetRoomInactive() {
        var room = newRoomBuilder().active(false).build();
        roomRepository.saveAndFlush(room);

        assertFalse(room.getActive());
<<<<<<< Updated upstream
        assertThrows(HttpClientErrorException.NotFound.class, () -> roomApi.getRoom(room.getId()));
=======
        assertThrows(HttpClientErrorException.NotFound.class, () -> api.getRoom(room.getId()));
>>>>>>> Stashed changes
    }

    @Test
    void testGetRoomDoesNotExist() {
<<<<<<< Updated upstream
        assertThrows(HttpClientErrorException.NotFound.class, () -> roomApi.getRoom(DEFAULT_ROOM_ID));
=======
        assertThrows(HttpClientErrorException.NotFound.class, () -> api.getRoom(DEFAULT_ROOM_ID));
    }

    @Test
    void testCreateRoomSuccess() {
        var createRoomDto = newCreateRoomDto();
        var roomDto = api.createRoom(createRoomDto);

        assertEquals(createRoomDto.getName(), roomDto.getName());
        assertEquals(createRoomDto.getSeats(), roomDto.getSeats());
        assertNotNull(roomDto.getId());

        var room = roomRepository.findById(roomDto.getId()).orElseThrow();
        assertEquals(roomDto.getName(), room.getName());
        assertEquals(roomDto.getSeats(), room.getSeats());
    }

    @Test
    void testCreateRoomValidationError() {
        assertThrows(HttpClientErrorException.UnprocessableEntity.class,
                () -> api.createRoom(newCreateRoomDto().name(null)));
>>>>>>> Stashed changes
    }

}
