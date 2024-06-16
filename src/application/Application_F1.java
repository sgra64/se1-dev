package application;

import java.util.Properties;

import datamodel.Article;
import datamodel.TAX;
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

        buildObjects();

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

    private void buildObjects() {
        dataStore.builder().buildCustomers( (factory) -> {
            factory.create(892474L, "Eric Meyer")
                .addContact("eric98@yahoo.com")
                .addContact("eric98@yahoo.com") // ignore duplicate contact
                .addContact("(030) 3945-642298");

            factory.create(643270L, "Bayer, Anne")
                .addContact("anne24@yahoo.de")
                .addContact("(030) 3481-23352")
                .addContact("fax: (030)23451356");

            factory.create(286516L, "Tim Schulz-Mueller")
                .addContact("tim2346@gmx.de");

            factory.create(412396L, "Nadine-Ulla Blumenfeld")
                .addContact("+49 152-92454");

            factory.create(456454L, "Khaled Saad Mohamed Abdelalim")
                .addContact("+49 1524-12948210");

            factory.create(651286L, "Lena Neumann")
                .addContact("lena228@gmail.com");
        })
        //
        .buildArticles( (factory) -> {
            factory.create("SKU-458362", "Tasse").setUnitPrice(299);
            factory.create("SKU-693856", "Becher").setUnitPrice(149);
            factory.create("SKU-518957", "Kanne").setUnitPrice(1999);
            factory.create("SKU-638035", "Teller").setUnitPrice(649);
            //
            factory.create("SKU-278530", "Buch \"Java\"")
                    .setTax(TAX.GER_VAT_REDUCED)    // reduced tax rate on books
                    .setUnitPrice(4990);
            //
            factory.create("SKU-425378", "Buch \"OOP\"")
                    .setTax(TAX.GER_VAT_REDUCED)    // reduced tax rate on books
                    .setUnitPrice(7995);
            //
            factory.create("SKU-300926", "Pfanne").setUnitPrice(4999);
            factory.create("SKU-663942", "Fahrradhelm").setUnitPrice(16900);
            factory.create("SKU-583978", "Fahrradkarte").setUnitPrice(695)
                    .setTax(TAX.GER_VAT_REDUCED);   // reduced tax rate on maps;
        })
        //
        .buildOrders( (customers, articles, factory) -> {
            // need to look up customers and article for building orders
            var eric = customers.findById(892474L);
            var anne = customers.findById(643270L);
            var nadine = customers.findById(412396L);
            var lena = customers.findById(651286L);
            //
            Article teller  = articles.findById("SKU-638035").get();
            Article becher  = articles.findById("SKU-693856").get();
            Article tasse   = articles.findById("SKU-458362").get();
            Article kanne   = articles.findById("SKU-518957").get();
            Article buch_OOP = articles.findById("SKU-425378").get();
            Article buch_Java = articles.findById("SKU-278530").get();
            Article helm    = articles.findById("SKU-663942").get();
            Article karte   = articles.findById("SKU-583978").get();

            // Eric's 1st order
            factory.create("8592356245", eric)  // new order for Eric
                .map(order -> order
                    .addItem(teller, 4)     // + item: 4 Teller, 4x 6.49 €
                    .addItem(becher, 8)     // + item: 8 Becher, 8x 1.49 €
                    .addItem(buch_OOP, 1)   // + item: 1 Buch "OOP", 1x 79.95 €, 7% MwSt (5.23€)
                    .addItem(tasse, 4));     // + item: 4 Tassen, 4x 2.99 €
            //
            // Anne's order
            factory.create("3563561357", anne)
                .map(order -> order
                    .addItem(teller, 2)
                    .addItem(tasse, 2));
            //
            // Eric's 2nd order
            factory.create("5234968294", eric)
                .map(order -> order
                    .addItem(kanne, 1));
            //
            // Nadine's order
            factory.create("6135735635", nadine)
                .map(order -> order
                    .addItem(teller, 12)
                    .addItem(buch_Java, 1)
                    .addItem(buch_OOP, 1));
            //
            factory.create("6173043537", lena)
                .map(order -> order
                    .addItem(buch_Java, 1)
                    .addItem(karte, 1));
            //
            // Eric's 3rd order
            factory.create("7372561535", eric)
                .map(order -> order
                    .addItem(helm, 1)
                    .addItem(karte, 1));
            //
            // Eric's 4th order
            factory.create("4450305661", eric)
                .map(order -> order
                    .addItem(tasse, 3)
                    .addItem(becher, 3)
                    .addItem(kanne, 1));
        });
    }
}