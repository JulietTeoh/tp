package seedu.us.among.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.us.among.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.us.among.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.us.among.logic.commands.CommandTestUtil.showEndpointAtIndex;
import static seedu.us.among.testutil.TypicalEndpoints.getTypicalEndpointList;
import static seedu.us.among.testutil.TypicalIndexes.INDEX_FIRST_ENDPOINT;
import static seedu.us.among.testutil.TypicalIndexes.INDEX_SECOND_ENDPOINT;

import org.junit.jupiter.api.Test;

import seedu.us.among.commons.core.Messages;
import seedu.us.among.commons.core.index.Index;
import seedu.us.among.model.Model;
import seedu.us.among.model.ModelManager;
import seedu.us.among.model.UserPrefs;
import seedu.us.among.model.endpoint.Endpoint;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteCommand}.
 */
public class DeleteCommandTest {

    private Model model = new ModelManager(getTypicalEndpointList(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Endpoint endpointToDelete = model.getFilteredEndpointList().get(INDEX_FIRST_ENDPOINT.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_ENDPOINT);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_ENDPOINT_SUCCESS, endpointToDelete);

        ModelManager expectedModel = new ModelManager(model.getEndpointList(), new UserPrefs());
        expectedModel.deleteEndpoint(endpointToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredEndpointList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_ENDPOINT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showEndpointAtIndex(model, INDEX_FIRST_ENDPOINT);

        Endpoint endpointToDelete = model.getFilteredEndpointList().get(INDEX_FIRST_ENDPOINT.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_ENDPOINT);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_ENDPOINT_SUCCESS, endpointToDelete);

        Model expectedModel = new ModelManager(model.getEndpointList(), new UserPrefs());
        expectedModel.deleteEndpoint(endpointToDelete);
        showNoEndpoint(expectedModel);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showEndpointAtIndex(model, INDEX_FIRST_ENDPOINT);

        Index outOfBoundIndex = INDEX_SECOND_ENDPOINT;
        // ensures that outOfBoundIndex is still in bounds of API endpoint list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getEndpointList().getEndpointList().size());

        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_ENDPOINT_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        DeleteCommand deleteFirstCommand = new DeleteCommand(INDEX_FIRST_ENDPOINT);
        DeleteCommand deleteSecondCommand = new DeleteCommand(INDEX_SECOND_ENDPOINT);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteCommand deleteFirstCommandCopy = new DeleteCommand(INDEX_FIRST_ENDPOINT);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different endpoint -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoEndpoint(Model model) {
        model.updateFilteredEndpointList(p -> false);

        assertTrue(model.getFilteredEndpointList().isEmpty());
    }
}