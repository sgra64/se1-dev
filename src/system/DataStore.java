package system;

import datamodel.*;

/**
 * {@link DataStore} is a singleton {@link system} component that stores collections
 * of {@link datamodel} objects of classes {@link Customer}, {@link Article} and {@link Order}.
 * <p>
 * The {@link DataStore} interface offers methods to create and access objects
 * through {@link DataBuilder} and {@link Repository} methods.
 * </p>
 * 
 * @version <code style=color:green>{@value application.package_info#Version}</code>
 * @author <code style=color:blue>{@value application.package_info#Author}</code>
 */
public interface DataStore {

    /**
     * Return Builder methods for {@link Customer}, {@link Article} and {@link Order} objects.
     * @return chainable self-reference
     */
    DataBuilder builder();

    /**
     * Return access to {@link Customer} objects through the {@code Repository<Customer, Long>} interface.
     * @return repository of customers.
     */
    Repository<Customer, Long> customers();

    /**
     * Return access to {@link Article} objects through the {@code Repository<Article, String>} interface.
     * @return repository of articles.
     */
    Repository<Article, String> articles();

    /**
     * Return access to {@link Order} objects through the {@code Repository<Order, String>} interface.
     * @return repository of orders.
     */
    Repository<Order, String> orders();

}