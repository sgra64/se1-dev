package system;

import java.util.function.Consumer;

import datamodel.Article;
import datamodel.Customer;
import datamodel.Order;

/**
 * {@link DataBuilder} is used to create and customize (build) {@link datamodel} objects
 * of classes {@link Customer}, {@link Article} and {@link Order}.
 * 
 * @version <code style=color:green>{@value application.package_info#Version}</code>
 * @author <code style=color:blue>{@value application.package_info#Author}</code>
 */
public interface DataBuilder {

    /**
     * Creating objects of type {@link Order} requires access to {@link Customer} and
     * {@link Article} objects, which is provided by respective repositories passed to
     * the {@code factory} lambda in {@code buildOrders(OrdersFactory factory);}.
     */
    @FunctionalInterface
    interface OrdersFactory {

        /**
         * Invokes the method on given arguments.
         * @param customers access to the {@link Customer} repository
         * @param articles access to the {@link Article} repository
         * @param factory access to factory to create {@code Order} objects
         */
        void accept(Repository<Customer, Long>customers, Repository<Article, String>articles, DataFactory<Order, String> factory);
    }

    /**
     * Method provides access to the factory to create and customize (build)
     * {@code Customer} objects in the {@code DataStore}.
     * @param factory access to factory to create {@code Customer} objects in the {@code DataStore}
     * @return chainable self-reference
     */
    DataBuilder buildCustomers(Consumer<DataFactory<Customer, Long>> factory);

    /**
     * Method provides access to the factory to create and customize (build)
     * {@code Article} objects in the {@code DataStore}.
     * @param factory access to factory to create {@code Article} objects in the {@code DataStore}
     * @return chainable self-reference
     */
    DataBuilder buildArticles(Consumer<DataFactory<Article, String>> factory);

    /**
     * Method provides access to the factory to create and customize (build)
     * {@code Orders} objects in the {@code DataStore}.
     * @param factory access to factory to create {@code Orders} objects in the {@code DataStore}
     * @return chainable self-reference
     */
    DataBuilder buildOrders(OrdersFactory factory);

}