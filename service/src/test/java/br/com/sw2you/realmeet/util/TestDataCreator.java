package br.com.sw2you.realmeet.util;

import static br.com.sw2you.realmeet.util.TestConstants.DEFAULT_ROOM_NAME;
import static br.com.sw2you.realmeet.util.TestConstants.DEFAULT_ROOM_SEATS;

import br.com.sw2you.realmeet.domain.entity.Room;

public final class TestDataCreator {

    private TestDataCreator() {
    }

    public static Room.Builder newRoomBuilder() {
        return Room.builder().name(DEFAULT_ROOM_NAME).seats(DEFAULT_ROOM_SEATS);
    }

}
