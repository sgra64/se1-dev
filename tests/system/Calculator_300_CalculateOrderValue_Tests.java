package system;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;


/**
 * JUnit5 Tests for Calculator component.
 * 
 * @author sgra64
 *
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Calculator_300_CalculateOrderValue_Tests {

    /*
     * Unit under test.
     */
    private final Calculator calc = IoC.getInstance().getCalculator();


    @Test @Order(300)
    void test300_calculateOrderValue() {
        // TODO: implement test cases
        assertEquals(0L, 0L);
    }

    @Test @Order(390)
    void test390_calculateOrderValue_NullArgs() {
        IllegalArgumentException thrown =
            assertThrows(
                IllegalArgumentException.class, () -> {
                    calc.calculateOrderValue(null);
        });
        assertEquals("argument order is null.", thrown.getMessage());
    }
}
