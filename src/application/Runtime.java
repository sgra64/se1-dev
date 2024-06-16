package application;

import java.io.*;
import java.util.*;
import java.util.function.*;


/**
 * Simple Runtime system that controls application instance creation and
 * loading application properties.
 * 
 * @version <code style=color:green>{@value application.package_info#Version}</code>
 * @author <code style=color:blue>{@value application.package_info#Author}</code>
 */
public class Runtime {

    /**
     * Singleton Runtime instance.
     */
    private static Runtime runtime = null;

    /**
     * Properties instance.
     */
    private final Properties properties = new Properties();


    /**
     * Default constructor (required by Javadoc)
     */
    private Runtime() { }

    /**
     * Return singleton Runtime instance.
     * @return Runtime instance
     */
    public static Runtime getRuntime() {
        if(runtime==null) {
            runtime = new Runtime();
        }
        return runtime;
    }

    private void run(String[] args) {
        loadPropertiesFromFile("application.properties");

        // instantiate main class or supply default instance
        Runnable instance = create(new String[] {
              //
              // instantiate class from application.properties with priority
              (String)properties.get("java.application.main"),
              //
              // attempt to instantiate the first existing class
              "application.Application_G2", "application.Application_G1",
              "application.Application_F2", "application.Application_F1",
              "application.Application_E2", "application.Application_E1",
              "application.Application_D2", "application.Application_D1_sol",
              "application.Application_D1_complete", "application.Application_D1",
              "application.Application_C4", "application.Application_C3",
              "application.Application_C2", "application.Application_C1",
          },
          cls -> Runnable.class.isAssignableFrom(cls),    // probe class implements Runnable interface
          properties, args,                               // constructor parameters for instantiation
          () -> new Application(properties, args));       // supply default instance, if other attempts fail

          instance.run();
    }

    /**
     * JavaVM entry method that creates and runs an application instance
     * that implements the {@link Runnable} interface.
     * @param args arguments passed from command line 
     */
    public static void main(String[] args) {
        getRuntime()
            .run(args);
    }

    /**
     * Return properties from "application.properties" file.
     * @return properties structure
     */
    public Properties properties() {
        return properties;
    }

    /**
     * Factory method that creates an instance of type {@code T} from
     * a list of class names. Returns instance of first class name found in the
     * list that can be instantiated or uses alternative supplier.
     * 
     * @param <T> type of created instance
     * @param classNames list of fully-qualified class names from which instances are attempted to be created
     * @param assignable functional interface to test class of instance to create is assignable from {@code T}
     * @param properties properties from application.properties file passed to created instance with constructor
     * @param args command line arguments passed to created instance with constructor
     * @param alt alternative instance supplier when creation from {@code classNames} yields no result
     * @return instance that is assignable from type {@code T}
     */
    @SuppressWarnings("unchecked")
    public <T> T create(String[] classNames, Function<Class<?>, Boolean> assignable,
        Properties properties, String[] args, Supplier<T> alt
    ) {
        if(classNames==null || alt==null)
            throw new IllegalArgumentException("classNames[] null or no alternative instance supplier provided (null)");
        //
        return Arrays.asList(classNames).stream()
            .filter(cn -> cn != null)
            .map(cn -> {
                try { // load class from class name and verify class implements the Runnable interface
                    var cls = Application.class.getClassLoader().loadClass(cn);
                    if(assignable != null && assignable.apply(cls))
                        return Optional.of(cls);
                } catch (ClassNotFoundException | NoClassDefFoundError e) { }
                return Optional.ofNullable((Class<T>)null);
            })
            .filter(opt -> opt.isPresent())
            .findFirst()
            .map(opt -> opt.get())  // unpack class from Optional
            .map(cls -> instByConstructor(() -> {
                    // instantiate class from constructor
                    var ctor = cls.getConstructor(String[].class);
                    return (T)ctor.newInstance(new Object[] {args});
                })
                .or(() -> instByConstructor(() -> {
                    var ctor = cls.getConstructor(Properties.class, String[].class);
                    return (T)ctor.newInstance(properties, args);
                }))
                .or(() -> instByConstructor(() -> {
                    var ctor = cls.getConstructor();    // attempt default constructor
                    return (T)ctor.newInstance();
                }))
            ).map(opt -> opt.get())     // unpack instance from Optional or, if empty,
            .orElse(alt.get());         // retrieve instance from alternative supplier
    }

    /**
     * Enable {@link Supplier<T>} method {@code T get()} to throw exceptions.
     * @param <T> generic type of result obtained from supplier
     */
    @FunctionalInterface
    interface SupplierWithExceptions<T> {
        /**
         * Retrive result from supplier.
         * @return result from supplier
         * @throws Exception an excpetion thrown by {@code T get()}
         */
        T get() throws Exception;
    }

    /**
     * Wrapper to catch exceptions when supplier is invoked.
     * 
     * @param <T> generic type of result obtained from supplier
     * @param supplier Supplier that creates instance and may throw exceptions
     * @return Optional with created instance or empty Optional
     */
    <T> Optional<T> instByConstructor(SupplierWithExceptions<T> supplier) {
        try {
            return Optional.of((T)supplier.get());
        } catch (Exception e
        /* NoSuchMethodException, SecurityException, InstantiationException,
            IllegalAccessException, InvocationTargetException*/
        ) { }
        return Optional.empty();
    }

    /**
     * Load Properties from file.
     * 
     * @param fileName file to load Properties from
     * @return Properties with values loaded from file (or empty)
     */
    Properties loadPropertiesFromFile(String fileName) {
        try (InputStream is = List.of(fileName, "resources/" + fileName).stream()
            // reading properties from within a jar file requires "resources"-prefix
            .map(fis -> Optional.ofNullable(ClassLoader.getSystemClassLoader().getResourceAsStream(fis)))
            .filter(s -> s.isPresent())
            .findFirst().map(opt -> opt.get()).orElse(new /*empty*/InputStream() { public int read() { return -1; }});
        ) {
            properties.load(is);    // load properties from InputStream
        } catch(IOException e) { }
        return properties;
    }
}