package datamodel;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Tests for Article class: [300..399] setDescription() and setUnitPrice() tests.
 * 
 * @author sgra64
 *
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Article_400_UnitPrice_Tests {
    //
    final String descr1 = "coffee maker";
    final String descr2 = "egg boiler";
    final String descrSmall = "c";
    final String descrLarge = descr1.repeat(100000);    // huge description string
    //
    final Article a1 = new Article(descr1, 100L);


    /*
     * Test cases 100: set unit price with regular value.
     */
    @Test @Order(100)
    void test100_setUnitPriceRegularValue() {
        assertEquals(descr1, a1.getDescription());
        a1.setDescription(descr2);
        assertEquals(descr2, a1.getDescription());
    }
}
