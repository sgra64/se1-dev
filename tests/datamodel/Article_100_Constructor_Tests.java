package datamodel;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * Tests for Article class: [100..199] Constructor tests.
 * 
 * @author sgra64
 *
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Article_100_Constructor_Tests {
    //
    final String invalidDescrMsg = "invalid description (null or \"\").";

    /*
     * Test case: Constructor with no name argument.
     */
    @Test @Order(100)
    void test100_Constructor() {
        final Article a1 = new Article();       // Constructor with no name argument
        assertEquals(null, a1.getId());         // returns null for unassigned id
        assertEquals("", a1.getDescription());  // description: ""
        assertEquals(0L, a1.getUnitPrice());    // unitPrice: 0L
        assertEquals(Currency.EUR, a1.getCurrency());   // EUR is default currency
        assertEquals(TAX.GER_VAT, a1.getTax()); // default tax
    }

    /*
     * Test case: Constructor with regular name argument.
     */
    @Test @Order(101)
    void test101_ConstructorWithRegularArguments() {
        final Article a1 = new Article("article", 10L); // Constructor, regular arguments
        assertEquals(null, a1.getId());                 // returns null for unassigned id
        assertEquals("article", a1.getDescription());   // description: ""
        assertEquals(10L, a1.getUnitPrice());           // unitPrice: 0L
        assertEquals(Currency.EUR, a1.getCurrency());   // EUR is default currency
        assertEquals(TAX.GER_VAT, a1.getTax());         // default tax
    }

    /*
     * Test case: Constructor with empty name argument: new Article("", price)
     * throws IllegalArgumentException with message.
     */
    @Test @Order(102)
    void test102_ConstructorWithEmptyDescriptionArgument() {
        IllegalArgumentException thrown =
            assertThrows(
                IllegalArgumentException.class, () -> {
                    // must throw IllegalArgumentException
                    new Article("", 10L);
        });
        // test for correct exception message
        assertEquals(invalidDescrMsg, thrown.getMessage());
    }

    /*
     * Test case: Constructor with null name argument: new Article(null, price)
     * throws IllegalArgumentException with message.
     */
    @Test @Order(103)
    void test103_ConstructorWithNullArgument() {
        IllegalArgumentException thrown =
            assertThrows(
                IllegalArgumentException.class, () -> {
                    new Article(null, 10L);
        });
        // test for correct exception message
        assertEquals(invalidDescrMsg, thrown.getMessage());
    }

    /*
     * Test case: Constructor with null name argument: new Article("regular", -1)
     * throws IllegalArgumentException with message.
     */
    @Test @Order(104)
    void test104_ConstructorWithNegativePriceArgument() {
        IllegalArgumentException thrown =
            assertThrows(
                IllegalArgumentException.class, () -> {
                    new Article("regular", -1L);
        });
        // test for correct exception message
        assertEquals("invalid unitPrice ( < 0).", thrown.getMessage());
    }

    /*
     * Test case: Constructor with empty name argument: new Article("")
     * throws IllegalArgumentException with message "name empty."
     */
    @Test @Order(105)
    void test105_ChainableSetters() {
        final Article a1 = new Article();
        assertSame(a1, a1.setId("SKU-0000"));   // setter must return same object reference
        assertSame(a1, a1.setDescription("some text"));
        assertSame(a1, a1.setUnitPrice(100L));
        assertSame(a1, a1.setCurrency(Currency.EUR));
        assertSame(a1, a1.setTax(TAX.GER_VAT));
    }
}
