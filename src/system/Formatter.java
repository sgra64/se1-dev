package system;

import datamodel.*;

/**
 * {@link Formatter} is a singleton {@link system} component that performs formatting
 * operations.
 * 
 * @version <code style=color:green>{@value application.package_info#Version}</code>
 * @author <code style=color:blue>{@value application.package_info#Author}</code>
 */
public interface Formatter {

    /**
     * Format Customer name according to a format (0 is default):
     * <pre>
     * fmt: 0: "Meyer, Eric"  10: "MEYER, ERIC"
     *      1: "Eric Meyer"   11: "ERIC MEYER"
     *      2: "Meyer, E."    12: "MEYER, E."
     *      3: "E. Meyer"     13: "E. MEYER"
     *      4: "Meyer"        14: "MEYER"
     *      5: "Eric"         15: "ERIC"
     * </pre>
     * 
     * @param customer Customer object.
     * @param fmt name formatting style.
     * @return formatted Customer name.
     */
    String fmtCustomerName(Customer customer, int... fmt);

    /**
     * Format Customer contacts according to a format (0 is default):
     * <pre>
     * fmt: 0: first contact: "anne24@yahoo.de"
     *      1: first contact with extension indicator: "anne24@yahoo.de, (+2 contacts)"
     *      2: all contacts as list: "anne24@yahoo.de, (030) 3481-23352, fax: (030)23451356"
     * </pre>
     * 
     * @param customer Customer object.
     * @param fmt name formatting style.
     * @return formatted Customer contact information.
     */
    String fmtCustomerContacts(Customer customer, int... fmt);

    /**
     * Format long value to price according to a format (0 is default):
     * <pre>
     * Example: long value: 499
     * fmt:   0: "4.99"
     *        1: "4.99 EUR"
     *        2: "4.99EUR"
     *        3: "4.99€"
     *        4: "4.99$"
     *        5: "4.99£"
     *        6:  "499¥"
     *        7:  "499"
     * </pre>
     * 
     * @param price long value as price.
     * @param fmt price formatting style.
     * @return formatted price according to style.
     */
    String fmtPrice(long price, int... fmt);

    /**
     * Format long value to a decimal String with a specified number of digits.
     * 
     * @param value value to format to String in decimal format.
     * @param decimalDigits number of digits.
     * @param unit appended unit as String.
     * @return formatted decimal value as String.
     */
    String fmtDecimal(long value, int decimalDigits, String... unit);

}