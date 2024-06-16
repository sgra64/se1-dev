package system;

import datamodel.*;

/**
 * {@link Calculator} is a singleton {@link system} component that performs calculations.
 * 
 * @version <code style=color:green>{@value application.package_info#Version}</code>
 * @author <code style=color:blue>{@value application.package_info#Author}</code>
 */
public interface Calculator {

    /**
     * Calculate value of an order item, which is calculated by:
     * article.unitPrice * number of units ordered. 
     * 
     * @param item to calculate value for.
     * @return value of item.
     */
    long calculateOrderItemValue(OrderItem item);

    /**
     * Calculate the included VAT of an order item calculated by the
     * applicable tax rate and the calculated order item value.
     * 
     * @param item to calculate VAT for.
     * @return VAT for ordered item.
     */
    long calculateOrderItemVAT(OrderItem item);

    /**
     * Calculate value of an order, which is comprised of the value of each
     * ordered item. The value of each item is calculated with
     * calculateOrderItemValue(item).
     * 
     * @param order to calculate value for.
     * @return value of order.
     */
    long calculateOrderValue(Order order);

    /**
     * Calculate VAT of an order, which is comprised of the VAT of each
     * ordered item. The VAT of each item is calculated with
     * calculateOrderItemVAT(item).
     * 
     * @param order to calculate VAT tax for.
     * @return VAT calculated for order.
     */
    long calculateOrderVAT(Order order);

    /**
     * Calculate included VAT (Value-Added Tax) from a gross price/value based on
     * a tax rate (VAT is called <i>"Mehrwertsteuer" (MwSt.)</i> in Germany).
     * 
     * @param grossValue value that includes the tax.
     * @param taxRate applicable tax rate in percent.
     * @return tax included in gross value.
     */
    long calculateVAT(long grossValue, TAX taxRate);

    /**
     * Return taxRate as double value, e.g. 19.0(%) for taxRate: TAX.GER_VAT
     * or 7.0(%) for taxRate: TAX.GER_VAT_REDUCED.
     * 
     * @param taxRate applicable tax rate in percent.
     * @return taxRate as double value.
     */
    double value(TAX taxRate);

}