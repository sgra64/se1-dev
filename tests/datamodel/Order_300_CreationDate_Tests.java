package datamodel;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;


/**
 * Tests for Order class: [300..399] setCreationDate() tests.
 * 
 * @author sgra64
 *
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Order_300_CreationDate_Tests {

    final Instant instant = Instant.now();

    final Customer c1 = new Customer().setId(1L);

    final datamodel.Order o1 = new datamodel.Order(c1);

    /**
     * Bounds for valid Order creation date. An Order must be created after {@code 01/01/2020, 00:00:00}
     * and before {@code today +1 day, 23:59:59}.
     */
    final LocalDateTime nowLDT = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
    // lower bound of valid order creation date-time is 01/01/2020, 00:00:00
    final LocalDateTime lowerLDT = LocalDateTime.of(2020, 1, 1, 0, 0, 0);
    final LocalDateTime upperLDT = LocalDateTime.of(
        nowLDT.get(ChronoField.YEAR),
        nowLDT.get(ChronoField.MONTH_OF_YEAR),
        nowLDT.get(ChronoField.DAY_OF_MONTH),
        23, 59, 59)
    //
    .plusDays(1);   // upper bound is tomorrow 23:59:59

    final long now = instant.toEpochMilli();
    final long lowerBound = lowerLDT.toInstant(ZoneOffset.UTC).toEpochMilli();
    final long upperBound = upperLDT.toInstant(ZoneOffset.UTC).toEpochMilli();

    final String outOfBoundsMsg = "invalid datetime argument (outside bounds 01/01/2020, 00:00:00 <= datetime <= today +1 day, 23:59:59).";


    /*
     * Test case: Constructor creation date.
     */
    @Test @Order(300)
    void test300_Constructor() {
        final var o1 = new datamodel.Order(c1);
        long age = o1.getCreationDate() - now;  // time passed since creation in ms
        assertTrue(age < 10000L);   // order just created must not be older than 10s
    }


    /*
     * Test case: set creation date with regular value.
     */
    @Test @Order(310)
    void test310_setCreationDateRegularCase() {
        long created = o1.getCreationDate();
        // -2 days is valid range
        long newCreationDate = created - 2*(24*60*60*1000L);
        o1.setCreationDate(newCreationDate);
        assertEquals(newCreationDate, o1.getCreationDate());
    }

    /*
     * Test case: set creation date with regular, lower bounds value.
     */
    @Test @Order(311)
    void test311_setCreationDateRegularLowerBounds() {
        // hitting lower bound is valid range
        o1.setCreationDate(lowerBound);
        assertEquals(lowerBound, o1.getCreationDate());
    }

    /*
     * Test case: set creation date with regular, upper bounds value.
     */
    @Test @Order(312)
    void test312_setCreationDateRegularUpperBounds() {
        // hitting upper bound is valid range
        o1.setCreationDate(upperBound);
        assertEquals(upperBound, o1.getCreationDate());
    }


    /*
     * Test case: set creation date with invalid lower value.
     */
    @Test @Order(320)
    void test320_setCreationDateInvalidLowerDateTimeRange() {
        long initialValue = o1.getCreationDate();
        var thrown = assertThrows(
            IllegalArgumentException.class, () -> {
                // Long.MIN_VALUE must throw IllegalArgumentException
                o1.setCreationDate(Long.MIN_VALUE);
        });
        // test for correct exception message
        assertEquals(outOfBoundsMsg, thrown.getMessage());
        assertEquals(initialValue, o1.getCreationDate());    // time unchanged
    }

    /*
     * Test case: set creation date with invalid upper value.
     */
    @Test @Order(321)
    void test321_setCreationDateInvalidUpperDateTimeRange() {
        long initialValue = o1.getCreationDate();
        var thrown = assertThrows(
            IllegalArgumentException.class, () -> {
                // Long.MAX_VALUE must throw IllegalArgumentException
                o1.setCreationDate(Long.MAX_VALUE);
        });
        // test for correct exception message
        assertEquals(outOfBoundsMsg, thrown.getMessage());
        assertEquals(initialValue, o1.getCreationDate());   // time unchanged
    }

    /*
     * Test case: set creation date with largest invalid value from lower range.
     */
    @Test @Order(322)
    void test322_setCreationDateInvalidLowerDateTimeBoundary() {
        long initialValue = o1.getCreationDate();
        var thrown = assertThrows(
            IllegalArgumentException.class, () -> {
                // lowerBound -1 msec must throw IllegalArgumentException
                o1.setCreationDate(lowerBound - 1L);
        });
        // test for correct exception message
        assertEquals(outOfBoundsMsg, thrown.getMessage());
        assertEquals(initialValue, o1.getCreationDate());   // time unchanged
    }

    /*
     * Test case: set creation date with smallest invalid value from upper range.
     */
    @Test @Order(323)
    void test323_setCreationDateInvalidUpperDateTimeRange() {
        long initialValue = o1.getCreationDate();
        var thrown = assertThrows(
            IllegalArgumentException.class, () -> {
                // upperBound +1 msec must throw IllegalArgumentException
                o1.setCreationDate(upperBound + 1L);
        });
        // test for correct exception message
        assertEquals(outOfBoundsMsg, thrown.getMessage());
        assertEquals(initialValue, o1.getCreationDate());   // time unchanged
    }
}
