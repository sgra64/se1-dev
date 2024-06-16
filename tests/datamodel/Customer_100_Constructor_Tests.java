package datamodel;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * Tests for Customer class: [100..199] Constructor tests.
 * 
 * @author sgra64
 *
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Customer_100_Constructor_Tests {

    /*
     * Test case: Constructor with no name argument.
     */
    @Test @Order(100)
    void test100_Constructor() {
        final Customer c1 = new Customer();     // Constructor with no name argument
        assertEquals(c1.getId(), null);         // returns null for unassigned id
        assertEquals(c1.getLastName(), "");     // lastName: ""
        assertEquals(c1.getFirstName(), "");    // firstName: ""
        assertEquals(c1.contactsCount(), 0);    // 0 contacts
        assertEquals(c1.getContacts().iterator().hasNext(), false);
    }

    /*
     * Test case: Constructor with regular name argument.
     */
    @Test @Order(101)
    void test101_ConstructorWithRegularNameArgument() {
        final Customer c1 = new Customer("Meyer");  // Constructor with no single name argument
        assertEquals(c1.getId(), null);             // returns null for unassigned id
        assertEquals(c1.getLastName(), "Meyer");    // lastName: "Meyer"
        assertEquals(c1.getFirstName(), "");        // firstName: ""
        assertEquals(c1.contactsCount(), 0);        // 0 contacts
        assertEquals(c1.getContacts().iterator().hasNext(), false);
    }

    /*
     * Test case: Constructor with empty name argument: new Customer("")
     * throws IllegalArgumentException with message "name empty."
     */
    @Test @Order(102)
    void test102_ConstructorWithEmptyNameArgument() {
        IllegalArgumentException thrown =
            assertThrows(
                IllegalArgumentException.class, () -> {
                    new Customer("");
        });
        assertEquals("name empty.", thrown.getMessage());
    }

    /*
     * Test case: Constructor with empty name argument: new Customer(null)
     * throws IllegalArgumentException with message "name null."
     */
    @Test @Order(103)
    void test103_ConstructorWithNullArgument() {
        IllegalArgumentException thrown =
            assertThrows(
                IllegalArgumentException.class, () -> {
                    new Customer(null);
        });
        assertEquals("name null.", thrown.getMessage());
    }

    /*
     * Test case: Default Constructor
     * throws IllegalArgumentException with message "name empty."
     */
    @Test @Order(104)
    void test104_ChainableSetters() {
        final Customer c1 = new Customer();
        assertSame(c1, c1.setId(0L));   // setter must return same object reference
        IllegalArgumentException thrown =
            assertThrows(
                IllegalArgumentException.class, () -> {
                    assertSame(c1, c1.setName(""));
        });
        assertEquals("name empty.", thrown.getMessage());
        assertSame(c1, c1.setName("",""));
        assertSame(c1, c1.addContact("eric@gmail.com"));
    }
}