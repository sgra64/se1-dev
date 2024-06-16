package system.impl;

import java.util.Map;
import java.util.stream.StreamSupport;

import datamodel.*;
import system.Calculator;

/**
 * Non-public singleton {@link system} component that implements the {@link Calculator}
 * interface.
 * <p>
 * {@link Calculator} is a singleton {@link system} component that performs calculations.
 * </p>
 * 
 * @version <code style=color:green>{@value application.package_info#Version}</code>
 * @author <code style=color:blue>{@value application.package_info#Author}</code>
 */
class CalculatorImpl implements Calculator {

    /**
     * Map TAX enum to tax rate as number.
     */
    private final Map<TAX, Double> taxRateMapper = Map.of(
        TAX.TAXFREE,             0.0,   // tax free rate
        TAX.GER_VAT,            19.0,   // German VAT tax (MwSt) 19.0%
        TAX.GER_VAT_REDUCED,     7.0    // German reduced VAT tax (MwSt) 7.0%
    );


    /**
     * Calculate value of an order item, which is calculated by:
     * article.unitPrice * number of units ordered. 
     * 
     * @param item to calculate value for.
     * @return value of item.
     */
    @Override
    public long calculateOrderItemValue(OrderItem item) {
        if(item==null)
            throw new IllegalArgumentException("argument item is null.");
        //
        return item.getArticle().getUnitPrice() * item.getUnitsOrdered();
    }

    /**
     * Calculate the included VAT of an order item calculated by the
     * applicable tax rate and the calculated order item value.
     * 
     * @param item to calculate VAT for.
     * @return VAT for ordered item.
     */
    @Override
    public long calculateOrderItemVAT(OrderItem item) {
        if(item==null)
            throw new IllegalArgumentException("argument item is null.");
        //
        var tax = item.getArticle().getTax();
        return calculateVAT(calculateOrderItemValue(item), tax);
    }

    /**
     * Calculate value of an order, which is comprised of the value of each
     * ordered item. The value of each item is calculated with
     * calculateOrderItemValue(item).
     * 
     * @param order to calculate value for.
     * @return value of order.
     */
    @Override
    public long calculateOrderValue(Order order) {
        if(order==null)
            throw new IllegalArgumentException("argument order is null.");
        //
        return StreamSupport.stream(order.getItems().spliterator(), false)
            .map(item -> calculateOrderItemValue(item))
            .reduce(0L, (compound, vat) -> compound + vat);
    }

    /**
     * Calculate VAT of an order, which is comprised of the VAT of each
     * ordered item. The VAT of each item is calculated with
     * calculateOrderItemVAT(item).
     * 
     * @param order to calculate VAT tax for.
     * @return VAT calculated for order.
     */
    @Override
    public long calculateOrderVAT(Order order) {
        if(order==null)
            throw new IllegalArgumentException("argument order is null.");
        //
        return StreamSupport.stream(order.getItems().spliterator(), false)
            .map(item -> calculateOrderItemVAT(item))
            .reduce(0L, (compound, vat) -> compound + vat);
    }

    /**
     * Calculate included VAT (Value-Added Tax) from a gross price/value based on
     * a tax rate (VAT is called <i>"Mehrwertsteuer" (MwSt.)</i> in Germany).
     * 
     * @param grossValue value that includes the tax.
     * @param taxRate applicable tax rate in percent.
     * @return tax included in gross value (0 if negative).
     */
    @Override
    public long calculateVAT(long grossValue, TAX taxRate) {
        if(taxRate==null)
            throw new IllegalArgumentException("argument taxRate is null.");
        //
        if(grossValue <= 0)  // no VAT on negative values
            return 0L;
        //
        // formula included VAT: gross × 0,19 / 1,19
        //
        double pct = taxRateMapper.get(taxRate) / 100.0;
        double vat_double = grossValue * pct / (1.0 + pct);
        double vat_rounded = Math.round(vat_double);
        long vat = Double.valueOf(vat_rounded).longValue();
        return vat;
    }

    /**
     * Return taxRate as double value, e.g. 19.0(%) for taxRate: TAX.GER_VAT
     * or 7.0(%) for taxRate: TAX.GER_VAT_REDUCED.
     * 
     * @param taxRate applicable tax rate in percent.
     * @return taxRate as double value.
     */
    @Override
    public double value(TAX taxRate) {
        if(taxRate==null)
            throw new IllegalArgumentException("argument taxRate is null.");
        //
        return taxRateMapper.get(taxRate);
    }
}