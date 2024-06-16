package system.impl;

import java.util.Optional;
import java.util.Properties;

import datamodel.Article;
import datamodel.Customer;
import datamodel.Order;
import system.*;

/**
 * Implementation class of the {@link system.IoC} interface.
 * <p>
 * "Inversion-of-Control" (IoC) container creates and contains system component objects
 * such as {@link Calculator}, {@link DataStore}, {@link Formatter} and {@link Printer}.
 * </p>
 * 
 * @version <code style=color:green>{@value application.package_info#Version}</code>
 * @author <code style=color:blue>{@value application.package_info#Author}</code>
 */
public class IoC_Impl implements IoC {

    /**
     * Singleton instance of IoC component.
     */
    private final static IoC iocSingleton = new IoC_Impl();

    /**
     * Singleton instance of DataStore component.
     */
    private DataStore dataStore = null;

    /**
     * Singleton instance of Calculator component.
     */
    private final Calculator calculator;

    /**
     * Singleton instance of Formatter component.
     */
    private final Formatter formatter;

    /**
     * Singleton instance of Printer component.
     */
    private final Printer printer;


    /**
     * Private constructor to implement Singleton pattern of IoC instance.
     */
    private IoC_Impl() {
        this.calculator = new CalculatorImpl();    // replace mock with own class CalculatorImpl.java
        this.formatter = new FormatterImpl();      // replace mock with own class FormatterImpl.java
        //
        // inject dependencies into PrinterImpl constructor
        this.printer = new PrinterImpl(calculator, formatter);
    }

    /**
     * IoC component getter.
     *  
     * @return reference to IoC singleton instance. 
     */
    public static IoC getInstance() {
        return iocSingleton;
    }

    /**
     * DataStore component getter.
     *  
     * @return reference to DataStore singleton instance. 
     */
    @Override
    public DataStore getDataStore() {
        if(dataStore==null) {
            Properties properties = application.Runtime.getRuntime().properties();
            String customersPath = Optional.ofNullable(properties.getProperty("data.customers.file"))
                .map(p -> p).orElse("./customers.json");
            //
            String articlesPath = Optional.ofNullable(properties.getProperty("data.articles.file"))
                .map(p -> p).orElse("./articles.json");
            //
            String ordersPath = Optional.ofNullable(properties.getProperty("data.orders.file"))
                .map(p -> p).orElse("./orders.json");
            //
            DataFactories dataFactories = new DataFactories();
            JSONDataFactories jsonDataFactories = new JSONDataFactories();
            //
            var customersRepository = new JSONRepositoryImpl<Customer, Long>(
                    jsonDataFactories.JsonCustomerFactory(dataFactories.customerFactory()), customersPath, c -> c.getId());
            //
            var articlesRepository = new JSONRepositoryImpl<Article, String>(
                    jsonDataFactories.JsonArticleFactory(dataFactories.articleFactory()), articlesPath, a -> a.getId());
            //
            var ordersRepository = new JSONRepositoryImpl<Order, String>(
                    jsonDataFactories.JsonOrderFactory(dataFactories.orderFactory(), customersRepository, articlesRepository), ordersPath, o -> o.getId());
            //
            DataStoreImpl dataStoreImpl = new DataStoreImpl(dataFactories,
                    customersRepository, articlesRepository, ordersRepository);
            //
            customersRepository.load();
            articlesRepository.load();
            ordersRepository.load();
            //
            this.dataStore = dataStoreImpl;
        }
        return dataStore;
    }

    /**
     * Calculator component getter.
     *  
     * @return reference to Calculator singleton instance. 
     */
    @Override
    public Calculator getCalculator() {
        return calculator;
    }

    /**
     * Formatter component getter.
     *  
     * @return reference to Formatter singleton instance. 
     */
    @Override
    public Formatter getFormatter() {
        return formatter;
    }

    /**
     * Printer component getter.
     *  
     * @return reference to Printer singleton instance. 
     */
    @Override
    public Printer getPrinter() {
        return printer;
    }
}