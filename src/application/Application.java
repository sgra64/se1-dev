package application;

import java.util.Properties;
import java.util.stream.Stream;


/**
 * Class that implements the {@link Runnable} interface with the
 * {@code run()}-method called as main entry point.
 * 
 * @version <code style=color:green>{@value application.package_info#Version}</code>
 * @author <code style=color:blue>{@value application.package_info#Author}</code>
 */
public class Application implements Runnable {

    /*
     * properties from "application.properties" file
     */
    final Properties properties;

    /*
     * arguments passed from command line
     */
    final String[] args;

    /**
     * Public {@code (Properties properties, String[] args)} constructor.
     * @param properties from "application.properties" file
     * @param args arguments passed from command line
     */
    public Application(Properties properties, String[] args) {
        this.properties = properties;
        this.args = args;
    }


    /**
     * Method of {@link Runnable} interface called on created application instance,
     * actual program execution starts here.
     */
    @Override
    public void run() {
        System.out.println(String.format("Hello, %s", this.getClass().getSimpleName()));
        Stream.of(args)
            .map(arg -> String.format("arg: %s", arg))
            .forEach(System.out::println);
    }

    /**
     * JavaVM entry method that invokes the {@link Runtime} class, which creates
     * an application instance that implements the {@link Runnable} interface.
     * @param args arguments passed from command line 
     */
    public static void main(String[] args) {
        Runtime.main(args);
    }
}