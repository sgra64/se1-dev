package system.impl;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import datamodel.*;
import system.*;

/**
 * Non-public singleton {@link system} component that implements the {@link Printer}
 * interface.
 * <p>
 * {@link Printer} is a singleton {@link system} component that prints collections
 * of {@link datamodel} objects into a {@link StringBuilder} in a table-format.
 * </p>
 * 
 * @version <code style=color:green>{@value application.package_info#Version}</code>
 * @author <code style=color:blue>{@value application.package_info#Author}</code>
 */
class PrinterImpl implements Printer {

    /**
     * Reference to Calculator component (dependency). 
     */
    private final Calculator calculator;

    /**
     * Reference to Formatter component (dependency). 
     */
    private final Formatter formatter;


    /**
     * Constructor that injects dependencies.
     * 
     * @param calculator dependency to Calculator component.
     * @param formatter dependency to Formatter component.
     */
    PrinterImpl(Calculator calculator, Formatter formatter) {
        this.calculator = calculator;
        this.formatter = formatter;
    }

    /**
     * Print customers in table format into a StringBuilder.
     * 
     * @param customers collection of customers to print.
     * @return StringBuilder with customers rendered as table.
     */
    @Override
    public StringBuilder printCustomers(Iterable<Customer> customers) {
        if(customers==null)
            throw new IllegalArgumentException("argument customers is null.");
        //
        final TableFormatter tf = new TableFormatter(
                // column specification
                // "| %8s ", "| %-32s", "| %-36s |"
                "| %8s ", "| %-27s", "| %-36s |"
            )
            .line()
            .row("Kund.-ID", "Name", "Kontakt")  // table header
            .line();
        //
        StreamSupport.stream(customers.spliterator(), false).forEach(c -> {
            String id = String.format("%d", c.getId());
            String name = formatter.fmtCustomerName(c);
            String contact = formatter.fmtCustomerContacts(c, 1);
            //
            tf.row(id, name, contact);  // write row into table
        });
        tf.line();
        //
        return tf.get();
    }

    /**
     * Print articles in table format into a StringBuilder.
     * 
     * @param articles collection of articles to print.
     * @return StringBuilder with customers rendered as table.
     */
    @Override
    public StringBuilder printArticles(Iterable<Article> articles) {
        if(articles==null)
            throw new IllegalArgumentException("argument articles is null.");
        //
        final TableFormatter tf = new TableFormatter(
                // column specification
                // "|%-10s", "| %-32s", "| %10s", "%4s", "|  %-18s  |"
                "|%-10s", "| %-27s", "| %10s", "%4s", "|  %-18s  |"
            )
            .line()
            .row("Artikel-ID", "Beschreibung", "Preis", "CUR", "Mehrwertsteuersatz")  // table header
            .line();
        //
        StreamSupport.stream(articles.spliterator(), false).forEach(a -> {
            String id = a.getId();
            String description = a.getDescription();
            String price = formatter.fmtPrice(a.getUnitPrice());
            String currency = a.getCurrency().toString();
            double rate = calculator.value(a.getTax());
            String vat = String.format("%4s%s %s", String.valueOf(rate), "%", a.getTax());
            //
            tf.row(id, description, price, currency, vat);  // write row into table
        });
        tf.line();
        //
        return tf.get();
    }

    /**
     * Print orders in table format into a StringBuilder.
     * 
     * @param orders collection of orders to print.
     * @return StringBuilder with orders rendered as table.
     */
    @Override
    public StringBuilder printOrders(Iterable<Order> orders) {
        if(orders==null)
            throw new IllegalArgumentException("argument orders is null.");
        //
        long[] totals = new long[] {0L, 0L};

        final var tf = new TableFormatter(
                // column specification
                // "|%-10s|", " %-25s", " %8s", "%1s", " %12s", "| %8s", " %12s|"
                "|%-10s|", " %-20s", " %8s", "%1s", " %12s", "| %8s", " %12s|"
            )
            .line()         // table header
            .row("Bestell-ID", "Bestellungen", "MwSt", "*", "Preis", "MwSt", "Gesamt")
            .line();

        StreamSupport.stream(orders.spliterator(), false)
            .map(order -> {
                printOrder(order, tf).line();
                return order;
            })
            .forEach(order -> {
                totals[0] += calculator.calculateOrderValue(order);
                totals[1] += calculator.calculateOrderVAT(order);
            });
        //
        tf.row(null, null, null, null, "Gesamt:", formatter.fmtPrice(totals[1]), formatter.fmtPrice(totals[0], 1));
        tf.line(null, null, null, null, null, "=", "=");
        //
        return tf.get();
    }

    /**
     * Print one order in table format into a StringBuilder, used by printOrders().
     * 
     * @param order order to print into table.
     * @param tf table formatter used by printOrders().
     * @return table formatter used by printOrders().
     */
    TableFormatter printOrder(Order order, TableFormatter tf) {
        if(order==null || tf==null)
            throw new IllegalArgumentException("order or table formatter tf is null.");
        //
        var id = Long.valueOf(order.getId()).toString();
        // limit name length so 'Bestellung' is not cut off
        var name = String.format("%.11s%s", order.getCustomer().getFirstName(), "'s Bestellung:");
        //
        tf.row(id, name, "", "", "", "", "");   // heading row with order id and name
        //
        var it = order.getItems().iterator();
        for(int i=0; it.hasNext(); i++) {
            var item = it.next();
            String descr = item.getArticle().getDescription();
            int unitsOrdered = item.getUnitsOrdered();
            long unitPrice = item.getArticle().getUnitPrice();
            long value = calculator.calculateOrderItemValue(item);
            long vat = calculator.calculateOrderItemVAT(item);
            // "*" in column 4 indicates reduced VAT rate
            String reducedTax = item.getArticle().getTax()==TAX.GER_VAT_REDUCED? "*" : "";
            String itemDescr = String.format(" - %d %s%s",
                unitsOrdered, descr, unitsOrdered > 1?
                    String.format(", %dx %s", unitsOrdered, formatter.fmtPrice(unitPrice)) :
                    String.format("")
            );
            String[] totals = i < order.itemsCount() - 1?   // last row?
                new String[] { "", ""} :
                new String[] { formatter.fmtPrice(calculator.calculateOrderVAT(order)),
                        formatter.fmtPrice(calculator.calculateOrderValue(order), 1) };
            //
            // item rows with item description, VAT, value and totals in the last row
            tf.row("", itemDescr, formatter.fmtPrice(vat), reducedTax, formatter.fmtPrice(value, 1), totals[0], totals[1]);
        }
        return tf;
    }

    /**
     * <p>
     * Generic print method for a list of objects of datamodel classes of type {@code <T>}
     * in table format into a StringBuilder.
     * </p><p>
     * {@code T} can be any of class: {@link Customer}, {@link Article} or {@link Order}.
     * </p><p>
     * Method will invoke the underlying print-methods for these classes.
     * </p><p>
     * Before, the list of objects is converted to a Stream{@code <T>} passed back to
     * the caller allowing to rearrange the Stream{@code <T>} for printing.
     * </p>
     * 
     * Print orders in table format into a StringBuilder. Parameter streamCallout passes
     * the stream of orders to caller to perform stream-operation such as filter(), sort(),
     * or limit().
     * 
     * @param <T> generic type of datamodel classes, can be: {@link Customer}, {@link Article} or {@link Order}.
     * @param collection collection of objects of type {@code <T>} to print.
     * @param callout function for the caller to manipulate the stream before printing. 
     * @return StringBuilder with printed objects rendered as table.
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> StringBuilder printStreamed(Iterable<T> collection, Function<Stream<T>, Stream<T>> callout) {
        if(collection==null)
            throw new IllegalArgumentException("argument collection is null.");
        //
        StringBuilder sb = null;
        var it = collection.iterator();
        if(it.hasNext()) {
            T t = it.next();
            //
            collection = callout==null? collection : // pass as stream to caller
                callout.apply(StreamSupport.stream(collection.spliterator(), false)).toList();
            //
            sb = t instanceof Customer? printCustomers((List<Customer>)collection) :
                 t instanceof Article? printArticles((List<Article>)collection) :
                 t instanceof Order? printOrders((List<Order>)collection) : null;
        }
        return Optional.ofNullable(sb).orElse(new StringBuilder());
    }
}