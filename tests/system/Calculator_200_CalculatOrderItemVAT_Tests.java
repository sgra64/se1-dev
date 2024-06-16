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
public class Calculator_200_CalculatOrderItemVAT_Tests {

    /*
     * Unit under test.
     */
    private final Calculator calc = IoC.getInstance().getCalculator();


    @Test @Order(200)
    void test200_calculateOrderItemVAT() {
        // TODO: implement test cases
        assertEquals(0L, 0L);
    }

    @Test @Order(290)
    void test290_calculateOrderVAT_NullArgs() {
        IllegalArgumentException thrown =
            assertThrows(
                IllegalArgumentException.class, () -> {
                    calc.calculateOrderVAT(null);
        });
        assertEquals("argument order is null.", thrown.getMessage());
    }
}
