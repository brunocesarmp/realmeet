package br.com.sw2you.realmeet.unit;

import static br.com.sw2you.realmeet.util.TestConstants.DEFAULT_ROOM_NAME;
import static br.com.sw2you.realmeet.util.TestDataCreator.newCreateRoomDto;
import static br.com.sw2you.realmeet.util.TestDataCreator.newRoomBuilder;
import static br.com.sw2you.realmeet.validator.ValidatorConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import br.com.sw2you.realmeet.core.BaseUnitTest;
import br.com.sw2you.realmeet.domain.repository.RoomRepository;
import br.com.sw2you.realmeet.exception.InvalidRequestException;
import br.com.sw2you.realmeet.validator.RoomValidator;
import br.com.sw2you.realmeet.validator.ValidationError;
import java.util.Optional;

class RoomValidatorUnitTest extends BaseUnitTest {

    private RoomValidator victim;

    @Mock
    private RoomRepository roomRepository;

    @BeforeEach
    void setupEach() {
        victim = new RoomValidator(roomRepository);
    }

    @Test
    void testValidateWhenRoomIsValid() {
        victim.validate(newCreateRoomDto());
    }

    @Test
    void testValidateWhenRoomNameIsMissing() {
        var createRoomDto = newCreateRoomDto().name(null);
        var exception = assertThrows(InvalidRequestException.class, () -> victim.validate(createRoomDto));

        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(new ValidationError(ROOM_NAME, ROOM_NAME + MISSING), exception.getValidationErrors().getError(0));
    }

    @Test
    void testValidateWhenRoomNameExceedsLength() {
        var createRoomDto = newCreateRoomDto().name(StringUtils.rightPad(ROOM_NAME, ROOM_NAME_MAX_LENGTH + 1, "x"));
        var exception = assertThrows(InvalidRequestException.class, () -> victim.validate(createRoomDto));

        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(new ValidationError(ROOM_NAME, ROOM_NAME + EXCEEDS_MAX_LENGTH), exception.getValidationErrors().getError(0));
    }

    @Test
    void testValidateWhenRoomSeatsIsMissing() {
        var createRoomDto = newCreateRoomDto().seats(null);
        var exception = assertThrows(InvalidRequestException.class, () -> victim.validate(createRoomDto));

        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(new ValidationError(ROOM_SEATS, ROOM_SEATS + MISSING), exception.getValidationErrors().getError(0));
    }

    @Test
    void testValidateWhenRoomSeatsAreLessThanMinValue() {
        var createRoomDto = newCreateRoomDto().seats(ROOM_SEATS_MIN_VALUE - 1);
        var exception = assertThrows(InvalidRequestException.class, () -> victim.validate(createRoomDto));

        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(new ValidationError(ROOM_SEATS, ROOM_SEATS + BELOW_MIN_VALUE), exception.getValidationErrors().getError(0));
    }

    @Test
    void testValidateWhenRoomSeatsAreGreaterThanMaxValue() {
        var createRoomDto = newCreateRoomDto().seats(ROOM_SEATS_MAX_VALUE + 1);
        var exception = assertThrows(InvalidRequestException.class, () -> victim.validate(createRoomDto));

        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(new ValidationError(ROOM_SEATS, ROOM_SEATS + EXCEEDS_MAX_VALUE), exception.getValidationErrors().getError(0));
    }

    @Test
    void testValidateWhenRoomNameIsDuplicate() {
        given(roomRepository.findByNameAndActive(DEFAULT_ROOM_NAME, true)).willReturn(Optional.of(newRoomBuilder().build()));

        var createRoomDto = newCreateRoomDto();
        var exception = assertThrows(InvalidRequestException.class, () -> victim.validate(createRoomDto));

        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(new ValidationError(ROOM_NAME, ROOM_NAME + DUPLICATE), exception.getValidationErrors().getError(0));
    }

}
