package system;

import datamodel.TAX;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;


/**
 * JUnit5 Tests for Calculator component.
 * 
 * @author sgra64
 *
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Calculator_600_CalculateTAXRate_Tests {

    /*
     * Unit under test.
     */
    private final Calculator calc = IoC.getInstance().getCalculator();


    @Test @Order(600)
    void test600_calculateTaxRateValue() {
        assertEquals( 0.0, calc.value(TAX.TAXFREE));
        assertEquals(19.0, calc.value(TAX.GER_VAT));
        assertEquals( 7.0, calc.value(TAX.GER_VAT_REDUCED));
    }

    @Test @Order(690)
    void test690_calculateTaxRateValue_NullArgs() {
        IllegalArgumentException thrown =
            assertThrows(
                IllegalArgumentException.class, () -> {
                    calc.value(null);
        });
        assertEquals("argument taxRate is null.", thrown.getMessage());
    }
}
