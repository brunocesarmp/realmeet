package br.com.sw2you.realmeet.integration;

import static br.com.sw2you.realmeet.util.TestDataCreator.newCreateAllocationDto;
import static br.com.sw2you.realmeet.util.TestDataCreator.newRoomBuilder;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import br.com.sw2you.realmeet.api.facade.AllocationApi;
import br.com.sw2you.realmeet.core.BaseIntegrationTest;
import br.com.sw2you.realmeet.domain.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;

class AllocationApiIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private AllocationApi api;

    @Autowired
    private RoomRepository roomRepository;

    @Override
    protected void setupEach() throws Exception {
        setLocalhostBasePath(api.getApiClient(), "/v1");
    }

    @Test
    void testCreateRoomSuccess() {
        var room = roomRepository.saveAndFlush(newRoomBuilder().build());
        var requestDto = newCreateAllocationDto().roomId(room.getId());
        var responseDto = api.createAllocation(requestDto);

        assertEquals(room.getId(), responseDto.getRoomId());
        assertNotNull(responseDto.getId());
        assertNotNull(responseDto.getRoomId());
        assertEquals(requestDto.getRoomId(), responseDto.getRoomId());
        assertEquals(requestDto.getSubject(), responseDto.getSubject());
        assertEquals(requestDto.getEmployeeName(), responseDto.getEmployeeName());
        assertEquals(requestDto.getEmployeeEmail(), responseDto.getEmployeeEmail());
        assertTrue(requestDto.getStartAt().isEqual(responseDto.getStartAt()));
        assertTrue(requestDto.getEndAt().isEqual(responseDto.getEndAt()));

    }


}
