package seedu.us.among.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.us.among.storage.JsonAdaptedEndpoint.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.us.among.testutil.Assert.assertThrows;
import static seedu.us.among.testutil.TypicalEndpoints.BENSON;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.us.among.commons.exceptions.IllegalValueException;
import seedu.us.among.model.endpoint.Address;
import seedu.us.among.model.endpoint.Name;

public class JsonAdaptedEndpointTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_TAG = "#friend";

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_ADDRESS = BENSON.getAddress().toString();
    private static final List<JsonAdaptedTag> VALID_TAGS = BENSON.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());

    @Test
    public void toModelType_validEndpointDetails_returnsEndpoint() throws Exception {
        JsonAdaptedEndpoint endpoint = new JsonAdaptedEndpoint(BENSON);
        assertEquals(BENSON, endpoint.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedEndpoint endpoint =
                new JsonAdaptedEndpoint(INVALID_NAME, VALID_ADDRESS, VALID_TAGS);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, endpoint::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedEndpoint endpoint = new JsonAdaptedEndpoint(null, VALID_ADDRESS, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, endpoint::toModelType);
    }

    @Test
    public void toModelType_invalidAddress_throwsIllegalValueException() {
        JsonAdaptedEndpoint endpoint =
                new JsonAdaptedEndpoint(VALID_NAME, INVALID_ADDRESS, VALID_TAGS);
        String expectedMessage = Address.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, endpoint::toModelType);
    }

    @Test
    public void toModelType_nullAddress_throwsIllegalValueException() {
        JsonAdaptedEndpoint endpoint = new JsonAdaptedEndpoint(VALID_NAME, null, VALID_TAGS);
        String expectedMessage = String.format(JsonAdaptedEndpoint.MISSING_FIELD_MESSAGE_FORMAT,
                Address.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, endpoint::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedEndpoint endpoint =
                new JsonAdaptedEndpoint(VALID_NAME, VALID_ADDRESS, invalidTags);
        assertThrows(IllegalValueException.class, endpoint::toModelType);
    }

}