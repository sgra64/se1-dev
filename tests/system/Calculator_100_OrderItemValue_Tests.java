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
public class Calculator_100_OrderItemValue_Tests {

    /*
     * Unit under test.
     */
    private final Calculator calc = IoC.getInstance().getCalculator();


    @Test @Order(100)
    void test100_calculateOrderItemValue() {
        // TODO: implement test cases
        assertEquals(0L, 0L);
    }

    @Test @Order(190)
    void test190_calculateOrderItemValue_NullArgs() {
        IllegalArgumentException thrown =
            assertThrows(
                IllegalArgumentException.class, () -> {
                    calc.calculateOrderItemValue(null);
        });
        assertEquals("argument item is null.", thrown.getMessage());
    }
}
