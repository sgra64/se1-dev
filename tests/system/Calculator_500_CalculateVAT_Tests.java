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
public class Calculator_500_CalculateVAT_Tests {

    /*
     * Unit under test.
     */
    private final Calculator calc = IoC.getInstance().getCalculator();


    @Test @Order(500)
    void test500_calculateVAT_RegularCasesGER_VAT() {
        // gross × 0,19 / 1,19 = gross * 0.1596638655462185
        assertEquals(20L, calc.calculateVAT(125L, TAX.GER_VAT));
        assertEquals(95L, calc.calculateVAT(597L, TAX.GER_VAT));
        assertEquals(191L, calc.calculateVAT(1196L, TAX.GER_VAT));
        assertEquals(1243L, calc.calculateVAT(7788L, TAX.GER_VAT));
    }

    @Test @Order(501)
    void test501_calculateVAT_RegularCasesGER_VAT() {
        // gross × 0,19 / 1,19 = gross * 0.1596638655462185
        assertEquals(0L, calc.calculateVAT(1L, TAX.GER_VAT));
        assertEquals(2L, calc.calculateVAT(10L, TAX.GER_VAT));
        assertEquals(16L, calc.calculateVAT(100L, TAX.GER_VAT));
        assertEquals(160L, calc.calculateVAT(1000L, TAX.GER_VAT));
        assertEquals(1597L, calc.calculateVAT(10000L, TAX.GER_VAT));
        assertEquals(15966L, calc.calculateVAT(100000L, TAX.GER_VAT));
        assertEquals(159664L, calc.calculateVAT(1000000L, TAX.GER_VAT)); 
    }

    @Test @Order(510)
    void test510_calculateVAT_RegularCasesGER_VAT_REDUCED() {
        // gross × 0,07 / 1,07 = gross * 0.0654205607476636
        assertEquals(8L, calc.calculateVAT(125L, TAX.GER_VAT_REDUCED));
        assertEquals(39L, calc.calculateVAT(597L, TAX.GER_VAT_REDUCED));
        assertEquals(78L, calc.calculateVAT(1196L, TAX.GER_VAT_REDUCED));
        assertEquals(509L, calc.calculateVAT(7788L, TAX.GER_VAT_REDUCED));
    }

    @Test @Order(511)
    void test511_calculateVAT_RegularCasesGER_VAT_REDUCED() {
        // gross × 0,07 / 1,07 = gross * 0.0654205607476636
        assertEquals(0L, calc.calculateVAT(1L, TAX.GER_VAT_REDUCED));
        assertEquals(1L, calc.calculateVAT(10L, TAX.GER_VAT_REDUCED));
        assertEquals(7L, calc.calculateVAT(100L, TAX.GER_VAT_REDUCED));
        assertEquals(65L, calc.calculateVAT(1000L, TAX.GER_VAT_REDUCED));
        assertEquals(654L, calc.calculateVAT(10000L, TAX.GER_VAT_REDUCED));
        assertEquals(6542L, calc.calculateVAT(100000L, TAX.GER_VAT_REDUCED));
        assertEquals(65421L, calc.calculateVAT(1000000L, TAX.GER_VAT_REDUCED)); 
    }

    @Test @Order(520)
    void test520_calculateVAT_RegularCasesTAXFREE() {
        assertEquals(0.0, calc.calculateVAT(597L, TAX.TAXFREE));
    }

    @Test @Order(521)
    void test521_calculateVAT_RegularCasesTAXFREE() {
        // gross × 0,07 / 1,07 = gross * 0.0654205607476636
        assertEquals(0L, calc.calculateVAT(1L, TAX.TAXFREE));
        assertEquals(0L, calc.calculateVAT(10L, TAX.TAXFREE));
        assertEquals(0L, calc.calculateVAT(100L, TAX.TAXFREE));
        assertEquals(0L, calc.calculateVAT(1000L, TAX.TAXFREE));
        assertEquals(0L, calc.calculateVAT(10000L, TAX.TAXFREE));
        assertEquals(0L, calc.calculateVAT(100000L, TAX.TAXFREE));
        assertEquals(0L, calc.calculateVAT(1000000L, TAX.TAXFREE)); 
    }
 
    @Test @Order(570)
    void test570_calculateVAT_NegValues() {
        assertEquals(0.0, calc.calculateVAT(-10L, TAX.TAXFREE));
        assertEquals(0.0, calc.calculateVAT(-100L, TAX.GER_VAT));
        assertEquals(0.0, calc.calculateVAT(-1000L, TAX.GER_VAT_REDUCED));
    }

    @Test @Order(580)
    void test580_calculateVAT_ZeroValues() {
        assertEquals(0.0, calc.calculateVAT(0L, TAX.GER_VAT));
        assertEquals(0.0, calc.calculateVAT(0L, TAX.GER_VAT_REDUCED));
        assertEquals(0.0, calc.calculateVAT(0L, TAX.TAXFREE));
    }

    @Test @Order(590)
    void test590_calculateVAT_NullArgs() {
        IllegalArgumentException thrown =
            assertThrows(
                IllegalArgumentException.class, () -> {
                    calc.calculateVAT(0L, null);
        });
        assertEquals("argument taxRate is null.", thrown.getMessage());
    }
}
