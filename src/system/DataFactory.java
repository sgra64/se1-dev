package system;

import java.util.Optional;

import datamodel.Customer;
import datamodel.Order;

/**
 * {@link DataFactory} creates {@link datamodel} objects of classes of type {@code T}
 * that use an ID of type {@code ID}.
 * 
 * @param <T> type of object to create
 * @param <ID> type of ID used in object
 * 
 * @version <code style=color:green>{@value application.package_info#Version}</code>
 * @author <code style=color:blue>{@value application.package_info#Author}</code>
 */
public interface DataFactory<T, ID> {

    /**
     * Create object of type {@code T} that has a {@code name} attribute and an {@code id}
     * attribute of type {@code ID}. Created object is stored in the {@code DataStore}.
     * @param id {@code id} attribute set in created object
     * @param name {@code name} attribute set in created object
     * @return created object (stored in {@code DataStore})
     */
    T create(ID id, String name);

    /**
     * Create object of type {@link Order} that requires a {@link Customer} reference.
     * The created object is stored in the {@code DataStore}.
     * @param id {@code id} attribute set in created object
     * @param customer required reference to {@link Customer} object
     * @return created object (stored in {@code DataStore}) or empty Optional
     */
    Optional<T> create(ID id, Optional<Customer> customer);

}