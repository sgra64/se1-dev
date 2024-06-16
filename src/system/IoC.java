package system;

/**
 * Interface of an "Inversion-of-Control" (IoC) container, which creates
 * and contains system component objects such as {@link Calculator},
 * {@link DataStore}, {@link Formatter} and {@link Printer}.
 * 
 * @version <code style=color:green>{@value application.package_info#Version}</code>
 * @author <code style=color:blue>{@value application.package_info#Author}</code>
 */
public interface IoC {

    /**
     * IoC component getter.
     *  
     * @return reference to IoC singleton instance. 
     */
    static IoC getInstance() {
        return system.impl.IoC_Impl.getInstance();
    }

    /**
     * DataStore component getter.
     *  
     * @return reference to DataStore singleton instance. 
     */
    DataStore getDataStore();

    /**
     * Calculator component getter.
     *  
     * @return reference to Calculator singleton instance. 
     */
    Calculator getCalculator();

    /**
     * Formatter component getter.
     *  
     * @return reference to Formatter singleton instance. 
     */
    Formatter getFormatter();

    /**
     * Printer component getter.
     *  
     * @return reference to Printer singleton instance. 
     */
    Printer getPrinter();

}