/**
 * Module contains classes that implement the {@link Runnable} interface launched
 * from the {@code main()} - method in {@link application.Application}.java.
 *  
 * @version <code style=color:green>{@value application.package_info#Version}</code>
 * @author <code style=color:blue>{@value application.package_info#Author}</code>
 */
module se1.bestellsystem {
    opens application;
    exports application;                // export application package to enable Javadoc
    exports datamodel;
    exports system;

    requires org.junit.jupiter.api;     // JUnit-5 test module
    requires java.logging;              // JUL logging module
    requires com.fasterxml.jackson.databind;    // Json libraries
}