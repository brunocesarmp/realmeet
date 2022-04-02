package br.com.sw2you.realmeet.integration;

import static br.com.sw2you.realmeet.util.DateUtils.now;
import static br.com.sw2you.realmeet.util.TestConstants.*;
import static br.com.sw2you.realmeet.util.TestDataCreator.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import br.com.sw2you.realmeet.api.facade.AllocationApi;
import br.com.sw2you.realmeet.core.BaseIntegrationTest;
import br.com.sw2you.realmeet.domain.repository.AllocationRepository;
import br.com.sw2you.realmeet.domain.repository.RoomRepository;
import br.com.sw2you.realmeet.email.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.HttpClientErrorException;

class AllocationApiIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private AllocationApi api;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private AllocationRepository allocationRepository;

    @MockBean
    private EmailSender emailSender;

    @Override
    protected void setupEach() throws Exception {
        setLocalhostBasePath(api.getApiClient(), "/v1");
    }

    @Test
    void testCreateRoomSuccess() {
        var room = roomRepository.saveAndFlush(newRoomBuilder().build());
        var requestDto = newCreateAllocationDto().roomId(room.getId());
        var responseDto = api.createAllocation(TEST_CLIENT_API_KEY, requestDto);

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

    @Test
    void testCreateAllocationValidationError() {
        var room = roomRepository.saveAndFlush(newRoomBuilder().build());
        var requestDto = newCreateAllocationDto()
                .roomId(room.getId())
                .subject(null);

        assertThrows(
                HttpClientErrorException.UnprocessableEntity.class,
                () -> api.createAllocation(TEST_CLIENT_API_KEY, requestDto)
        );
    }

    @Test
    void testCreateAllocationWhenRoomDoesNotExist() {
        assertThrows(
                HttpClientErrorException.NotFound.class,
                () -> api.createAllocation(TEST_CLIENT_API_KEY, newCreateAllocationDto())
        );
    }

    @Test
    void testDeleteAllocationSuccess() {
        var room = roomRepository.saveAndFlush(newRoomBuilder().build());
        var allocation = allocationRepository.saveAndFlush(newAllocationBuilder(room).build());

        api.deleteAllocation(TEST_CLIENT_API_KEY, allocation.getId());
        assertFalse(allocationRepository.findById(allocation.getId()).isPresent());
    }

    @Test
    void testDeleteAllocationInThePast() {
        var room = roomRepository.saveAndFlush(newRoomBuilder().build());
        var allocation = allocationRepository.saveAndFlush(
                newAllocationBuilder(room)
                        .startAt(now().minusDays(1))
                        .endAt(now().minusDays(1).plusHours(1))
                        .build());

        assertThrows(
                HttpClientErrorException.UnprocessableEntity.class,
                () -> api.deleteAllocation(TEST_CLIENT_API_KEY, allocation.getId())
        );
    }

    @Test
    void testDeleteAllocationDoesNotExist() {
        assertThrows(
                HttpClientErrorException.NotFound.class,
                () -> api.deleteAllocation(TEST_CLIENT_API_KEY, 1L)
        );
    }

    @Test
    void testUpdateAllocationSuccess() {
        var room = roomRepository.saveAndFlush(newRoomBuilder().build());
        var allocation = allocationRepository.saveAndFlush(newAllocationBuilder(room).build());
        var updateAllocationDto = newUpdatedAllocationDto()
                .subject(DEFAULT_ALLOCATION_SUBJECT + "-")
                .startAt(DEFAULT_ALLOCATION_START_AT.plusDays(1))
                .endAt(DEFAULT_ALLOCATION_END_AT.plusDays(1));

        api.updateAllocation(TEST_CLIENT_API_KEY, allocation.getId(), updateAllocationDto);

        var updatedAllocation = allocationRepository.findById(allocation.getId()).orElseThrow();

        assertEquals(updatedAllocation.getSubject(), updateAllocationDto.getSubject());
        assertTrue(updatedAllocation.getStartAt().isEqual(updateAllocationDto.getStartAt()));
        assertTrue(updatedAllocation.getEndAt().isEqual(updateAllocationDto.getEndAt()));
    }

    @Test
    void testUpdateAllocationDoesNotExists() {
        assertThrows(
                HttpClientErrorException.NotFound.class,
                () -> api.updateAllocation(TEST_CLIENT_API_KEY, 1L, newUpdatedAllocationDto()));
    }

    @Test
    void testUpdateRoomValidationError() {
        var room = roomRepository.saveAndFlush(newRoomBuilder().build());
        var allocation = allocationRepository.saveAndFlush(newAllocationBuilder(room).build());

        assertThrows(
                HttpClientErrorException.UnprocessableEntity.class,
                () -> api.updateAllocation(TEST_CLIENT_API_KEY, allocation.getId(), newUpdatedAllocationDto().subject(null)));
    }

}