package br.com.sw2you.realmeet.unit;

import static br.com.sw2you.realmeet.util.MapperUtils.roomMapper;
import static br.com.sw2you.realmeet.util.TestConstants.DEFAULT_ROOM_ID;
import static br.com.sw2you.realmeet.util.TestDataCreator.newCreateRoomDto;
import static br.com.sw2you.realmeet.util.TestDataCreator.newRoomBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.sw2you.realmeet.core.BaseUnitTest;
import br.com.sw2you.realmeet.mapper.RoomMapper;

class RoomMapperUnitTest extends BaseUnitTest {

    private RoomMapper victim;

    @BeforeEach
    void setupEach() {
        victim = roomMapper();
    }

    @Test
    void testFromEntityToDto() {
        var room = newRoomBuilder().id(DEFAULT_ROOM_ID).build();
        var dto = victim.fromEntityToDto(room);

        assertEquals(room.getId(), dto.getId());
        assertEquals(room.getName(), dto.getName());
        assertEquals(room.getSeats(), dto.getSeats());
    }

    @Test
    void testFromCreateRoomDtoToEntity() {
        var createRoomDto = newCreateRoomDto();
        var room = victim.fromCreateRoomDtoToEntity(createRoomDto);

        assertEquals(createRoomDto.getName(), room.getName());
        assertEquals(createRoomDto.getSeats(), room.getSeats());
    }

}
