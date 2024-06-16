package datamodel;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * Tests for Article class: [500..599] setCurrency() tests.
 * 
 * @author sgra64
 *
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Article_500_Currency_Tests {
    //
    final Article a1 = new Article("coffee maker", 100L);


    /*
     * Test cases 100: set currency with regular value.
     */
    @Test @Order(100)
    void test100_setCurrencyRegularValue() {
        assertEquals(Currency.EUR, a1.getCurrency());
        a1.setCurrency(Currency.USD);
        assertEquals(Currency.USD, a1.getCurrency());
    }

    /*
     * Test cases 110: set currency with null value, throws IllegalArgumentException.
     */
    @Test @Order(110)
    void test110_setCurrencyNullArgument() {
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class, () -> {
                a1.setCurrency(null);   // setCurrency(null) must throw IllegalArgumentException
        });
        // test for correct exception message
        assertEquals("invalid currency (null).", thrown.getMessage());
        //
        assertEquals(Currency.EUR, a1.getCurrency());   // currency remains unchanged
    }
}
