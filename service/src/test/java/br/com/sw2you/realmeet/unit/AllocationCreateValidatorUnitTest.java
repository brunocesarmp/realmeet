package br.com.sw2you.realmeet.unit;

import static br.com.sw2you.realmeet.util.DateUtils.DEFAULT_TIMEZONE;
import static br.com.sw2you.realmeet.util.DateUtils.now;
import static br.com.sw2you.realmeet.util.TestDataCreator.*;
import static br.com.sw2you.realmeet.validator.ValidatorConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import br.com.sw2you.realmeet.core.BaseUnitTest;
import br.com.sw2you.realmeet.domain.repository.AllocationRepository;
import br.com.sw2you.realmeet.exception.InvalidRequestException;
import br.com.sw2you.realmeet.validator.AllocationValidator;
import br.com.sw2you.realmeet.validator.ValidationError;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.List;

class AllocationCreateValidatorUnitTest extends BaseUnitTest {

    private AllocationValidator victim;

    @Mock
    private AllocationRepository allocationRepository;

    @BeforeEach
    void setupEach() {
        victim = new AllocationValidator(allocationRepository);
    }

    @Test
    void testValidateWhenRoomIsValid() {
        victim.validate(newCreateAllocationDto());
    }

    @Test
    void testValidateWhenSubjectIsMissing() {
        var exception = assertThrows(
                InvalidRequestException.class,
                () -> victim.validate(newCreateAllocationDto().subject(null))
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(new ValidationError(ALLOCATION_SUBJECT, ALLOCATION_SUBJECT + MISSING), exception.getValidationErrors().getError(0));
    }

    @Test
    void testValidateWhenSubjectExceedsLengthIsMissing() {
        var exception = assertThrows(
                InvalidRequestException.class,
                () -> victim.validate(newCreateAllocationDto().subject(StringUtils.rightPad("x", ALLOCATION_SUBJECT_MAX_LENGTH + 1, 'x')))
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(new ValidationError(ALLOCATION_SUBJECT, ALLOCATION_SUBJECT + EXCEEDS_MAX_LENGTH), exception.getValidationErrors().getError(0));
    }

    @Test
    void testValidateWhenEmployeeNameIsMissing() {
        var exception = assertThrows(
                InvalidRequestException.class,
                () -> victim.validate(newCreateAllocationDto().employeeName(null))
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(new ValidationError(ALLOCATION_EMPLOYEE_NAME, ALLOCATION_EMPLOYEE_NAME + MISSING), exception.getValidationErrors().getError(0));
    }

    @Test
    void testValidateWhenEmployeeNameExceedsLengthIsMissing() {
        var exception = assertThrows(
                InvalidRequestException.class,
                () -> victim.validate(newCreateAllocationDto().employeeName(StringUtils.rightPad("x", ALLOCATION_SUBJECT_MAX_LENGTH + 1, 'x')))
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(new ValidationError(ALLOCATION_EMPLOYEE_NAME, ALLOCATION_EMPLOYEE_NAME + EXCEEDS_MAX_LENGTH), exception.getValidationErrors().getError(0));
    }

    @Test
    void testValidateWhenEmployeeEmailIsMissing() {
        var exception = assertThrows(
                InvalidRequestException.class,
                () -> victim.validate(newCreateAllocationDto().employeeEmail(null))
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(new ValidationError(ALLOCATION_EMPLOYEE_EMAIL, ALLOCATION_EMPLOYEE_EMAIL + MISSING), exception.getValidationErrors().getError(0));
    }

    @Test
    void testValidateWhenEmployeeEmailExceedsLengthIsMissing() {
        var exception = assertThrows(
                InvalidRequestException.class,
                () -> victim.validate(newCreateAllocationDto().employeeEmail(StringUtils.rightPad("x", ALLOCATION_SUBJECT_MAX_LENGTH + 1, 'x')))
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(new ValidationError(ALLOCATION_EMPLOYEE_EMAIL, ALLOCATION_EMPLOYEE_EMAIL + EXCEEDS_MAX_LENGTH), exception.getValidationErrors().getError(0));
    }

    @Test
    void testValidateWhenStartAtIsMissing() {
        var exception = assertThrows(
                InvalidRequestException.class,
                () -> victim.validate(newCreateAllocationDto().startAt(null))
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(new ValidationError(ALLOCATION_START_AT, ALLOCATION_START_AT + MISSING), exception.getValidationErrors().getError(0));
    }

    @Test
    void testValidateWhenEndAtIsMissing() {
        var exception = assertThrows(
                InvalidRequestException.class,
                () -> victim.validate(newCreateAllocationDto().endAt(null))
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(new ValidationError(ALLOCATION_END_AT, ALLOCATION_END_AT + MISSING), exception.getValidationErrors().getError(0));
    }

    @Test
    void testValidateWhenDateOrderIsInvalid() {
        var exception = assertThrows(
                InvalidRequestException.class,
                () -> victim.validate(newCreateAllocationDto().startAt(now().plusDays(1)).endAt(now().plusDays(1).minusMinutes(30)))
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(new ValidationError(ALLOCATION_START_AT, ALLOCATION_START_AT + INCONSISTENT), exception.getValidationErrors().getError(0));
    }

    @Test
    void testValidateWhenDateIsInThePast() {
        var exception = assertThrows(
                InvalidRequestException.class,
                () -> victim.validate(newCreateAllocationDto().startAt(now().minusMinutes(30)).endAt(now().plusMinutes(30)))
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(new ValidationError(ALLOCATION_START_AT, ALLOCATION_START_AT + IN_THE_PAST), exception.getValidationErrors().getError(0));
    }

    @Test
    void testValidateWhenDateIntervalExceedsMaxDuration() {
        var exception = assertThrows(
                InvalidRequestException.class,
                () -> victim.validate(newCreateAllocationDto().startAt(now().plusDays(1)).endAt(now().plusDays(1).plusSeconds(ALLOCATION_MAX_DURATION_SECONDS + 1)))
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(new ValidationError(ALLOCATION_END_AT, ALLOCATION_END_AT + EXCEEDS_DURATION), exception.getValidationErrors().getError(0));
    }

    @Test
    void testValidateDateIntervals() {
        assertTrue(isScheduledAllowed(tomorrow(4), tomorrow(5), tomorrow(1), tomorrow(2)));
        assertTrue(isScheduledAllowed(tomorrow(4), tomorrow(5), tomorrow(6), tomorrow(7)));
        assertTrue(isScheduledAllowed(tomorrow(4), tomorrow(5), tomorrow(3), tomorrow(4)));
        assertTrue(isScheduledAllowed(tomorrow(4), tomorrow(5), tomorrow(5), tomorrow(6)));

        assertFalse(isScheduledAllowed(tomorrow(4), tomorrow(7), tomorrow(4), tomorrow(7)));
        assertFalse(isScheduledAllowed(tomorrow(4), tomorrow(7), tomorrow(4), tomorrow(5)));
        assertFalse(isScheduledAllowed(tomorrow(4), tomorrow(7), tomorrow(6), tomorrow(7)));
        assertFalse(isScheduledAllowed(tomorrow(4), tomorrow(7), tomorrow(3), tomorrow(5)));
        assertFalse(isScheduledAllowed(tomorrow(4), tomorrow(7), tomorrow(6), tomorrow(8)));
        assertFalse(isScheduledAllowed(tomorrow(4), tomorrow(7), tomorrow(3), tomorrow(8)));
    }

    private OffsetDateTime tomorrow(int hour) {
        return OffsetDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(hour, 0), DEFAULT_TIMEZONE);
    }

    private boolean isScheduledAllowed(OffsetDateTime scheduledAllocationStart, OffsetDateTime scheduledAllocationEnd,
                                       OffsetDateTime newAllocationStart, OffsetDateTime newAllocationEnd) {

        given(allocationRepository.findAllWithFilters(any(), any(), any(), any()))
                .willReturn(List.of(
                        newAllocationBuilder(newRoomBuilder().build())
                                .startAt(scheduledAllocationStart)
                                .endAt(scheduledAllocationEnd)
                                .build()));

        try {
            victim.validate(newCreateAllocationDto().startAt(newAllocationStart).endAt(newAllocationEnd));
            return true;
        } catch (InvalidRequestException e) {
            return false;
        }
    }

}
