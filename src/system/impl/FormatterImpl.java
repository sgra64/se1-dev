package system.impl;

import java.util.Map;
import java.util.stream.StreamSupport;

import datamodel.*;
import system.Formatter;

/**
 * Non-public singleton {@link system} component that implements the {@link Formatter}
 * interface.
 * <p>
 * {@link Formatter} is a singleton {@link system} component that performs formatting
 * operations.
 * </p>
 * 
 * @version <code style=color:green>{@value application.package_info#Version}</code>
 * @author <code style=color:blue>{@value application.package_info#Author}</code>
 */
class FormatterImpl implements Formatter {

    /**
     * Map Currency-enum to Unicode-String.
     */
    private final Map<Currency, String> CurrencySymbol = Map.of(
        Currency.EUR, "\u20ac",     // Unicode: EURO
        Currency.USD, "$",          // ASCII: US Dollar
        Currency.GBP, "\u00A3",     // Unicode: UK Pound Sterling
        Currency.YEN, "\u00A5",     // Unicode: Japanese Yen
        Currency.BTC, "BTC"         // no Unicode for Bitcoin
    );


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
    @Override
    public String fmtCustomerName(Customer customer, int... fmt) {
        if(customer==null)
            throw new IllegalArgumentException("argument Customer null.");
        //
        String ln = customer.getLastName();
        String fn = customer.getFirstName();
        String fn1 = fn.substring(0, 1).toUpperCase();
        //
        final int ft = fmt.length > 0? fmt[0] : 0;  // 0 is default format
        switch(ft) {    // 0 is default
        case 0: return String.format("%s, %s", ln, fn);
        case 1: return String.format("%s %s", fn, ln);
        case 2: return String.format("%s, %s.", ln, fn1);
        case 3: return String.format("%s. %s", fn1, ln);
        case 4: return ln;
        case 5: return fn;
        //
        case 10: case 11: case 12: case 13: case 14: case 15:
            return fmtCustomerName(customer, ft - 10).toUpperCase();
        //
        default: return fmtCustomerName(customer, 0);
        }
    }

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
    @Override
    public String fmtCustomerContacts(Customer customer, int... fmt) {
        if(customer==null)
            throw new IllegalArgumentException("argument Customer null.");
        //
        var clen = customer.contactsCount();
        final int ft = fmt.length > 0? fmt[0] : 0;  // 0 is default format
        switch(ft) {    // 0 is default
        case 0:
            String contact = StreamSupport.stream(customer.getContacts().spliterator(), false)
                .findFirst().map(c -> c).orElse("");
            return String.format("%s", contact);

        case 1:
            String ext = clen > 1? String.format(", (+%d contacts)", clen - 1) : "";
            return String.format("%s%s", fmtCustomerContacts(customer, 0), ext);

        case 2:
            StringBuilder sb = new StringBuilder();
            StreamSupport.stream(customer.getContacts().spliterator(), false)
                .forEach(c -> sb.append(c).append(sb.length() > 0? ", " : ""));
            return sb.toString();
        //
        default: return fmtCustomerContacts(customer, 0);
        }
    }

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
    @Override
    public String fmtPrice(long price, int... fmt) {
        final int ft = fmt.length > 0? fmt[0] : 0;  // 0 is default format
        switch(ft) {
        case 0: return fmtDecimal(price, 2);
        case 1: return fmtDecimal(price, 2, " EUR");
        case 2: return fmtDecimal(price, 2, "EUR");
        case 3: return fmtDecimal(price, 2, CurrencySymbol.get(Currency.EUR));
        case 4: return fmtDecimal(price, 2, CurrencySymbol.get(Currency.USD));
        case 5: return fmtDecimal(price, 2, CurrencySymbol.get(Currency.GBP));
        case 6: return fmtDecimal(price, 0, CurrencySymbol.get(Currency.YEN));
        case 7: return fmtDecimal(price, 0);
        //
        default: return fmtPrice(price, 0);
        }
    }

    /**
     * Format long value to a decimal String with a specified number of digits.
     * 
     * @param value value to format to String in decimal format.
     * @param decimalDigits number of digits.
     * @param unit appended unit as String.
     * @return formatted decimal value as String.
     */
    @Override
    public String fmtDecimal(long value, int decimalDigits, String... unit) {
        final String unitStr = unit.length > 0? unit[0] : null;
        final Object[][] dec = {
            {      "%,d", 1L },     // no decimal digits:  16,000Y
            { "%,d.%01d", 10L },
            { "%,d.%02d", 100L },   // double-digit price: 169.99E
            { "%,d.%03d", 1000L },  // triple-digit unit:  16.999-
        };
        String result;
        String fmt = (String)dec[decimalDigits][0];
        if(unitStr != null && unitStr.length() > 0) {
            fmt += "%s";    // add "%s" to format for unit string
        }
        int decdigs = Math.max(0, Math.min(dec.length - 1, decimalDigits));
        //
        if(decdigs==0) {
            Object[] args = {value, unitStr};
            result = String.format(fmt, args);
        } else {
            long digs = (long)dec[decdigs][1];
            long frac = Math.abs( value % digs );
            Object[] args = {value/digs, frac, unitStr};
            result = String.format(fmt, args);
        }
        return result;
    }
}