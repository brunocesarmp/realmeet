package br.com.sw2you.realmeet.unit;

import static br.com.sw2you.realmeet.util.MapperUtils.allocationMapper;
import static br.com.sw2you.realmeet.util.TestDataCreator.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.sw2you.realmeet.core.BaseUnitTest;
import br.com.sw2you.realmeet.mapper.AllocationMapper;

class AllocationMapperUnitTest extends BaseUnitTest {

    private AllocationMapper victim;

    @BeforeEach
    void setupEach() {
        victim = allocationMapper();
    }

    @Test
    void testFromCreateAllocationDtoToEntity() {
        var createAllocationDto = newCreateAllocationDto();
        var room = newRoomBuilder().id(1L).build();
        var allocation = victim.fromCreateAllocationDtoToEntity(createAllocationDto, room);

        assertEquals(createAllocationDto.getSubject(), allocation.getSubject());
        assertEquals(createAllocationDto.getSubject(), allocation.getSubject());
        assertEquals(createAllocationDto.getEmployeeName(), allocation.getEmployee().getName());
        assertEquals(createAllocationDto.getEmployeeEmail(), allocation.getEmployee().getEmail());
        assertEquals(createAllocationDto.getStartAt(), allocation.getStartAt());
        assertEquals(createAllocationDto.getEndAt(), allocation.getEndAt());
    }

    @Test
    void testFromEntityToAllocationDto() {
        var room = newRoomBuilder().id(1L).build();
        var allocation = newAllocationBuilder(room).build();
        var allocationDto = victim.fromEntityToDto(allocation);

        assertEquals(allocation.getSubject(), allocationDto.getSubject());
        assertEquals(allocation.getId(), allocationDto.getId());
        assertNotNull(allocationDto.getRoomId());
        assertEquals(allocation.getRoom().getId(), allocationDto.getRoomId());
        assertEquals(allocation.getEmployee().getName(), allocationDto.getEmployeeName());
        assertEquals(allocation.getEmployee().getEmail(), allocationDto.getEmployeeEmail());
        assertEquals(allocation.getStartAt(), allocationDto.getStartAt());
        assertEquals(allocation.getEndAt(), allocation.getEndAt());
    }

}
