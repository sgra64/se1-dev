package system.impl;

import java.util.function.Consumer;

import datamodel.*;
import system.Repository;
import system.DataBuilder;
import system.DataFactory;
import system.DataStore;

/**
 * Non-public singleton {@link system} component that implements the {@link DataStore}
 * interface.
 * <p>
 * {@link DataStore} stores collections of {@link datamodel} objects such as
 * objects of classes {@link Customer}, {@link Article} and  {@link Order}.
 * </p>
 * 
 * @version <code style=color:green>{@value application.package_info#Version}</code>
 * @author <code style=color:blue>{@value application.package_info#Author}</code>
 */
class DataStoreImpl implements DataStore, DataBuilder {

    /**
     * internal Customer-, Article- and Order-Factories, implement the
     * DataFactory<T, ID> interface
     */
    private final DataFactories dataFactories;

    /**
     * injected reference to repositories holding Customer-, Article- and Order objects
     */
    private final Repository<Customer, Long> customersRepository;
    private final Repository<Article, String> articlesRepository;
    private final Repository<Order, String> ordersRepository;


    /**
     * Constructor that creates internal {link DataFactory<T, ID>} and {link Repository<T, ID>}
     * objects of respective types {@code T} for {link Customer}, {link Article} and {link Order}.
     * @param customersRepository injected reference to customers repository
     * @param articlesRepository injected reference to articles repository
     * @param ordersRepository injected reference to orders repository
     */
    DataStoreImpl(
        DataFactories dataFactories,
        Repository<Customer, Long> customersRepository,
        Repository<Article, String> articlesRepository,
        Repository<Order, String> ordersRepository
    ) {
        this.dataFactories = dataFactories;
        this.customersRepository = customersRepository;
        this.articlesRepository = articlesRepository;
        this.ordersRepository = ordersRepository;
    }

    /**
     * Return Builder methods for {@link Customer}, {@link Article} and {@link Order} objects.
     * @return chainable self-reference
     */
    @Override
    public DataBuilder builder() {
        return this;
    }

    /**
     * Method provides access to the factory to create and customize (build)
     * {@code Customer} objects in the {@code DataStore}.
     * @param factory access to factory to create {@code Customer} objects in the {@code DataStore}
     * @return chainable self-reference
     */
    @Override
    public DataBuilder buildCustomers(Consumer<DataFactory<Customer, Long>> factory) {
        factory.accept(dataFactories.customerFactory());
        return this;
    }

    /**
     * Method provides access to the factory to create and customize (build)
     * {@code Article} objects in the {@code DataStore}.
     * @param factory access to factory to create {@code Article} objects in the {@code DataStore}
     * @return chainable self-reference
     */
    @Override
    public DataBuilder buildArticles(Consumer<DataFactory<Article, String>> factory) {
        factory.accept(dataFactories.articleFactory());
        return this;
    }

    /**
     * Method provides access to the factory to create and customize (build)
     * {@code Orders} objects in the {@code DataStore}.
     * @param factory access to factory to create {@code Orders} objects in the {@code DataStore}
     * @return chainable self-reference
     */
    @Override
    public DataBuilder buildOrders(OrdersFactory factory) {
        factory.accept(customersRepository, articlesRepository, dataFactories.orderFactory());
        return this;
    }

    /**
     * Return access to {@link Customer} objects through the {@code Repository<Customer, Long>} interface.
     * @return repository of customers.
     */
    @Override
    public Repository<Customer, Long> customers() {
        return customersRepository;
    }

    /**
     * Return access to {@link Article} objects through the {@code Repository<Article, String>} interface.
     * @return repository of articles.
     */
    @Override
    public Repository<Article, String> articles() {
        return articlesRepository;
    }

    /**
     * Return access to {@link Order} objects through the {@code Repository<Order, String>} interface.
     * @return repository of orders.
     */
    @Override
    public Repository<Order, String> orders() {
        return ordersRepository;
    }
}