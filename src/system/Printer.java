package system;

import java.util.stream.*;
import java.util.function.Function;

import datamodel.*;

/**
 * {@link Printer} is a singleton {@link system} component that prints collections
 * of {@link datamodel} objects into a {@link StringBuilder} in a table-format.
 * 
 * @version <code style=color:green>{@value application.package_info#Version}</code>
 * @author <code style=color:blue>{@value application.package_info#Author}</code>
 */
public interface Printer {

    /**
     * Print customers in table format into a StringBuilder.
     * 
     * @param customers collection of customers to print.
     * @return StringBuilder with customers rendered as table.
     */
    StringBuilder printCustomers(Iterable<Customer> customers);

    /**
     * Print articles in table format into a StringBuilder.
     * 
     * @param articles collection of articles to print.
     * @return StringBuilder with customers rendered as table.
     */
    StringBuilder printArticles(Iterable<Article> articles);

    /**
     * Print orders in table format into a StringBuilder.
     * 
     * @param orders collection of orders to print.
     * @return StringBuilder with orders rendered as table.
     */
    StringBuilder printOrders(Iterable<Order> orders);

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
    <T> StringBuilder printStreamed(Iterable<T> collection, Function<Stream<T>,Stream<T>> callout);

}