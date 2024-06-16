package datamodel;


import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;


/**
 * Tests for Order class: [100..199] Constructor tests.
 * 
 * @author sgra64
 *
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Order_100_Constructor_Tests {
    //
    final Customer c1 = new Customer().setId(1L);


    /*
     * Test case: Constructor with regular Customer argument.
     */
    @Test @Order(100)
    void test100_Constructor() {
        long justNow = System.currentTimeMillis();
        final var o1 = new datamodel.Order(c1); // Constructor with Customer as argument
        assertEquals(null, o1.getId());         // returns null for unassigned id
        assertEquals(c1, o1.getCustomer());     // test Customer getter
        long age = o1.getCreationDate() - justNow;   // time passed in ms
        assertTrue(age < 10000L);               // order just created must not be older than 10,000ms
        assertEquals(0, o1.itemsCount());       // 0 items
        Iterable<OrderItem> itemsIt = o1.getItems();
        assertNotNull(itemsIt);
        assertFalse(itemsIt.iterator().hasNext());
    }

    /*
     * Test case: Constructor with null argument.
     */
    @Test @Order(101)
    void test101_ConstructorWithNullArgument() {
        IllegalArgumentException thrown =
            assertThrows(
                IllegalArgumentException.class, () -> {
                    new datamodel.Order(null);
        });
        // test for correct exception message
        assertEquals("Customer empty.", thrown.getMessage());
    }

    /*
     * Test case: Constructor with Customer with invalid Customer-id.
     */
    @Test @Order(102)
    void test102_ConstructorWithCustomerWithInvalidIdArgument() {
        final Customer invalidIdCustomer = new Customer();  // Customer with invalid id
        IllegalArgumentException thrown =
            assertThrows(
                IllegalArgumentException.class, () -> {
                    new datamodel.Order(invalidIdCustomer);
        });
        // test for correct exception message
        assertEquals("Customer has invalid id.", thrown.getMessage());
    }

    /*
     * Test case: Constructor with empty name argument: new Order("")
     * throws IllegalArgumentException with message "name empty."
     */
    @Test @Order(103)
    void test103_ChainableSetters() {
        final var o1 = new datamodel.Order(c1);
        assertSame(o1, o1.setId("X222222"));    // setters must return same object reference
        assertSame(o1, o1.setCreationDate(o1.getCreationDate()));
        final var a1 = new Article("description", 1);
        assertSame(o1, o1.addItem(a1, 1));
    }
}
