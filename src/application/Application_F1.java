package application;

import java.util.Properties;

import system.DataStore;
import system.IoC;
import system.Printer;


/**
 * Runnable application class for the {@link se1.bestellsystem}.
 * <p>
 * In task C3, customers are created and printed using the Customer class
 * from the {@link datamodel}.
 * </p>
 * 
 * @version <code style=color:green>{@value application.package_info#Version}</code>
 * @author <code style=color:blue>{@value application.package_info#Author}</code>
 */

public class Application_F1 implements Runnable {

    /*
     * variable with reference to DataStore component
     */
    private final DataStore dataStore;

    /*
     * variable with reference to Printer component
     */
    private final Printer printer;


    /**
     * Public {@code (Properties properties, String[] args)} constructor.
     * @param properties from "application.properties" file
     * @param args arguments passed from command line
     */
    public Application_F1(Properties properties, String[] args) {
        this.dataStore = IoC.getInstance().getDataStore();
        this.printer = IoC.getInstance().getPrinter();
    }


    /**
     * Method of {@link Runnable} interface called on created application instance,
     * actual program execution starts here.
     */
    @Override
    public void run() {
        System.out.println(String.format("Hello, %s", this.getClass().getSimpleName()));

        System.out.println(String.format(
            "(%d) Customer objects added.\n" +
            "(%d) Article objects added.\n" +
            "(%d) Order objects added.\n---",
            dataStore.customers().count(),
            dataStore.articles().count(),
            dataStore.orders().count()
        ));

        StringBuilder sb = printer.printCustomers(dataStore.customers().findAll());
        System.out.println(sb.insert(0, "Kunden:\n").toString());   // print table from returned StringBuilder

        sb = printer.printArticles(dataStore.articles().findAll());
        System.out.println(sb.insert(0, "Artikel:\n").toString());

        sb = printer.printOrders(dataStore.orders().findAll());
        System.out.println(sb.insert(0, "Bestellungen:\n").toString());
    }
}