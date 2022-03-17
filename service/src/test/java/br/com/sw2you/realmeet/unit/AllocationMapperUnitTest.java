package br.com.sw2you.realmeet.unit;

import static br.com.sw2you.realmeet.util.MapperUtils.allocationMapper;
import static br.com.sw2you.realmeet.util.TestDataCreator.newCreateAllocationDto;
import static br.com.sw2you.realmeet.util.TestDataCreator.newRoomBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
        var room = newRoomBuilder().build();
        var allocation = victim.fromCreateAllocationDtoToEntity(createAllocationDto, room);

        assertEquals(createAllocationDto.getSubject(), allocation.getSubject());
        assertEquals(createAllocationDto.getEmployeeName(), allocation.getEmployee().getName());
        assertEquals(createAllocationDto.getEmployeeEmail(), allocation.getEmployee().getEmail());
        assertEquals(createAllocationDto.getStartAt(), allocation.getStartAt());
        assertEquals(createAllocationDto.getEndAt(), allocation.getEndAt());
    }

}
