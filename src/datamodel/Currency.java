package datamodel;

/**
 * Enumeration type for currencies.
 * 
 * Currency is the unit in which price is quoted.
 * 
 * @version <code style=color:green>{@value application.package_info#Version}</code>
 * @author <code style=color:blue>{@value application.package_info#Author}</code>
 */
public enum Currency {

    /**
     * Euro, legal tender in many countries in the European Union
     */
    EUR,

    /**
     * US Dollar, legal tender in the United States of America
     */
    USD,

    /**
     * Great Britain Pound (or Pound Sterling), legal tender in the United Kingdom
     */
    GBP,

    /**
     * Japanese Yen, legal tender in Japan
     */
    YEN,

    /**
     * Crypto currency, no legal tender
     */
    BTC
}