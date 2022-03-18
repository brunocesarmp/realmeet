package br.com.sw2you.realmeet.util;

import static br.com.sw2you.realmeet.util.TestConstants.*;

import br.com.sw2you.realmeet.api.model.CreateAllocationDTO;
import br.com.sw2you.realmeet.api.model.CreateRoomDTO;
import br.com.sw2you.realmeet.domain.entity.Allocation;
import br.com.sw2you.realmeet.domain.entity.Room;
import br.com.sw2you.realmeet.domain.model.Employee;

public final class TestDataCreator {

    private TestDataCreator() {
    }

    public static Room.Builder newRoomBuilder() {
        return Room.builder().name(DEFAULT_ROOM_NAME).seats(DEFAULT_ROOM_SEATS);
    }

    public static Allocation.Builder newAllocationBuilder(Room room) {
        return Allocation.builder()
                .subject(DEFAULT_ALLOCATION_SUBJECT)
                .room(room)
                .employee(Employee.builder().name(DEFAULT_ROOM_NAME).email(DEFAULT_EMPLOYEE_EMAIL).build())
                .startAt(DEFAULT_ALLOCATION_START_AT)
                .endAt(DEFAULT_ALLOCATION_END_AT);
    }

    public static CreateRoomDTO newCreateRoomDto() {
        return (CreateRoomDTO) new CreateRoomDTO().name(DEFAULT_ROOM_NAME).seats(DEFAULT_ROOM_SEATS);
    }

    public static CreateAllocationDTO newCreateAllocationDto() {
        return new CreateAllocationDTO().subject(DEFAULT_ALLOCATION_SUBJECT)
                .roomId(DEFAULT_ROOM_ID)
                .employeeName(DEFAULT_EMPLOYEE_NAME)
                .employeeEmail(DEFAULT_EMPLOYEE_EMAIL)
                .startAt(DEFAULT_ALLOCATION_START_AT)
                .endAt(DEFAULT_ALLOCATION_END_AT);
    }

}
