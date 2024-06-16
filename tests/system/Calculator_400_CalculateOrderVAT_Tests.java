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
public class Calculator_400_CalculateOrderVAT_Tests {

    /*
     * Unit under test.
     */
    private final Calculator calc = IoC.getInstance().getCalculator();


    @Test @Order(400)
    void test400_calculateOrderVAT() {
        // TODO: implement test cases
        assertEquals(0L, 0L);
    }

    @Test @Order(490)
    void test490_calculateOrderVAT_NullArgs() {
        IllegalArgumentException thrown =
            assertThrows(
                IllegalArgumentException.class, () -> {
                    calc.calculateOrderVAT(null);
        });
        assertEquals("argument order is null.", thrown.getMessage());
    }
}
