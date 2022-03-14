package br.com.sw2you.realmeet.integration;

import static br.com.sw2you.realmeet.util.TestConstants.DEFAULT_ROOM_ID;
import static br.com.sw2you.realmeet.util.TestDataCreator.newCreateRoomDto;
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
    private RoomApi api;

    @Autowired
    private RoomRepository roomRepository;

    @Override
    protected void setupEach() throws Exception {
        setLocalhostBasePath(api.getApiClient(), "/v1");
    }

    @Test
    void testGetRoomSuccess() {
        var room = newRoomBuilder().build();
        roomRepository.saveAndFlush(room);

        assertNotNull(room.getId());
        assertTrue(room.getActive());

        var dto = api.getRoom(room.getId());

        assertEquals(room.getId(), dto.getId());
        assertEquals(room.getName(), dto.getName());
        assertEquals(room.getSeats(), dto.getSeats());
    }

    @Test
    void testGetRoomInactive() {
        var room = newRoomBuilder().active(false).build();
        roomRepository.saveAndFlush(room);

        assertFalse(room.getActive());
        assertThrows(HttpClientErrorException.NotFound.class, () -> api.getRoom(room.getId()));
    }

    @Test
    void testGetRoomDoesNotExist() {
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
    }

    @Test
    void testDeleteRoomSuccess() {
        var roomId = roomRepository.saveAndFlush(newRoomBuilder().build()).getId();
        api.deleteRoom(roomId);

        assertFalse(roomRepository.findById(roomId).orElseThrow().getActive());
    }

    @Test
    void testDeleteRoomDoesNotExists() {
        assertThrows(HttpClientErrorException.NotFound.class, () -> api.deleteRoom(1L));
    }

}
