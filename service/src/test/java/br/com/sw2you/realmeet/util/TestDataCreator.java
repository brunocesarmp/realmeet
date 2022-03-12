package br.com.sw2you.realmeet.util;

import static br.com.sw2you.realmeet.util.TestConstants.DEFAULT_ROOM_NAME;
import static br.com.sw2you.realmeet.util.TestConstants.DEFAULT_ROOM_SEATS;

<<<<<<< Updated upstream
<<<<<<< Updated upstream
=======
import br.com.sw2you.realmeet.api.model.CreateRoomDTO;
>>>>>>> Stashed changes
=======
import br.com.sw2you.realmeet.api.model.CreateRoomDTO;
>>>>>>> Stashed changes
import br.com.sw2you.realmeet.domain.entity.Room;

public final class TestDataCreator {

    private TestDataCreator() {
    }

    public static Room.Builder newRoomBuilder() {
        return Room.builder().name(DEFAULT_ROOM_NAME).seats(DEFAULT_ROOM_SEATS);
    }

<<<<<<< Updated upstream
<<<<<<< Updated upstream
=======
=======
>>>>>>> Stashed changes
    public static CreateRoomDTO newCreateRoomDto() {
        return new CreateRoomDTO().name(DEFAULT_ROOM_NAME).seats(DEFAULT_ROOM_SEATS);
    }

<<<<<<< Updated upstream
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
}
