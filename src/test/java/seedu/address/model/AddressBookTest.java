package seedu.address.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.TypicalRestaurants.ALICE;
import static seedu.address.testutil.TypicalRestaurants.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.restaurant.Restaurant;
import seedu.address.model.restaurant.exceptions.DuplicateRestaurantException;
import seedu.address.testutil.RestaurantBuilder;

public class AddressBookTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final AddressBook addressBook = new AddressBook();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), addressBook.getRestaurantList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        addressBook.resetData(null);
    }

    @Test
    public void resetData_withValidReadOnlyAddressBook_replacesData() {
        AddressBook newData = getTypicalAddressBook();
        addressBook.resetData(newData);
        assertEquals(newData, addressBook);
    }

    @Test
    public void resetData_withDuplicateRestaurants_throwsDuplicateRestaurantException() {
        // Two restaurants with the same identity fields
        Restaurant editedAlice = new RestaurantBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        List<Restaurant> newRestaurants = Arrays.asList(ALICE, editedAlice);
        AddressBookStub newData = new AddressBookStub(newRestaurants);

        thrown.expect(DuplicateRestaurantException.class);
        addressBook.resetData(newData);
    }

    @Test
    public void hasRestaurant_nullRestaurant_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        addressBook.hasRestaurant(null);
    }

    @Test
    public void hasRestaurant_restaurantNotInAddressBook_returnsFalse() {
        assertFalse(addressBook.hasRestaurant(ALICE));
    }

    @Test
    public void hasRestaurant_restaurantInAddressBook_returnsTrue() {
        addressBook.addRestaurant(ALICE);
        assertTrue(addressBook.hasRestaurant(ALICE));
    }

    @Test
    public void hasRestaurant_restaurantWithSameIdentityFieldsInAddressBook_returnsTrue() {
        addressBook.addRestaurant(ALICE);
        Restaurant editedAlice = new RestaurantBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(addressBook.hasRestaurant(editedAlice));
    }

    @Test
    public void getRestaurantList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        addressBook.getRestaurantList().remove(0);
    }

    @Test
    public void addListener_withInvalidationListener_listenerAdded() {
        SimpleIntegerProperty counter = new SimpleIntegerProperty();
        InvalidationListener listener = observable -> counter.set(counter.get() + 1);
        addressBook.addListener(listener);
        addressBook.addRestaurant(ALICE);
        assertEquals(1, counter.get());
    }

    @Test
    public void removeListener_withInvalidationListener_listenerRemoved() {
        SimpleIntegerProperty counter = new SimpleIntegerProperty();
        InvalidationListener listener = observable -> counter.set(counter.get() + 1);
        addressBook.addListener(listener);
        addressBook.removeListener(listener);
        addressBook.addRestaurant(ALICE);
        assertEquals(0, counter.get());
    }

    /**
     * A stub ReadOnlyAddressBook whose restaurants list can violate interface constraints.
     */
    private static class AddressBookStub implements ReadOnlyAddressBook {
        private final ObservableList<Restaurant> restaurants = FXCollections.observableArrayList();

        AddressBookStub(Collection<Restaurant> restaurants) {
            this.restaurants.setAll(restaurants);
        }

        @Override
        public ObservableList<Restaurant> getRestaurantList() {
            return restaurants;
        }

        @Override
        public void addListener(InvalidationListener listener) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void removeListener(InvalidationListener listener) {
            throw new AssertionError("This method should not be called.");
        }
    }

}
