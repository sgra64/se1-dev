package system;

import java.util.Optional;

/**
 * Interface to access a collection of objects of a {@link datamodel} class
 * of type {@code T} in a {@link DataStore}.
 * <p>
 * The interface provides CRUD operations (CRUD: Create, Read, Update, Delete).
 * </p>
 * <p>
 * The Repository interface is derived from
 * <a href="https://spring.io/projects/spring-boot">Spring Boot's</a>
 * <a href="https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/CrudRepository.html">
 * CRUDRepository</a> interface.
 * </p>
 * 
 * Authors of original
 * <a href="https://github.com/spring-projects/spring-data-commons/blob/main/src/main/java/org/springframework/data/repository/CrudRepository.java">
 * Spring Code </a>:
 * 
 * @author Oliver Gierke
 * @author Eberhard Wolff
 * @author Jens Schauder
 * 
 * @param <T> generic type of Repository objects (entities).
 * @param <ID> generic type of object identifier (id).
 */
public interface Repository<T, ID> {

    /**
     * Return the current number of objects in repository.
     *
     * @return number of entities.
     */
    long count();

    /**
     * Return all repository objects.
     * 
     * @return all repository objects.
     */
    Iterable<T> findAll();

    /**
     * Return result of a lookup of an object by its {@literal id}.
     * 
     * @param id {@literal id} of object to find.
     * @return result of lookup of an object by its {@literal id}.
     * @throws IllegalArgumentException {@literal id} is {@literal null}.
     */
    Optional<T> findById(ID id);

    /**
     * Return result of a lookup of an object by its {@literal id}.
     * 
     * @param id {@literal id} of object to find.
     * @return true if an object with {@literal id} exists in repository.
     * @throws IllegalArgumentException {@literal id} is {@literal null}.
     */
    boolean existsById(ID id);

    /**
     * Return result of a lookup of objects by a collection of {@literal ids}.
     * No object is included in result if {@literal id} could not be found.
     * 
     * @param ids collection of {@literal ids} for which objects are looked up.
     * @return result of lookup by a collection of {@literal ids}.
     * @throws IllegalArgumentException {@literal ids} is {@literal null}.
     */
    Iterable<T> findAllById(Iterable<ID> ids);

    /**
     * Save object (entity) to a repository. Object replaces a prior object
     * with the same {@literal id}.
     * 
     * @param <S> sub-class of {@code <T>}.
     * @param entity object saved to the repository.
     * @return the saved entity.
     * @throws IllegalArgumentException {@literal entity} or entity's {@literal id}
     * is {@literal null}.
     */
    <S extends T> S save(S entity);

    /**
     * Save a collection of objects (entities) to a repository. Objects replace
     * prior objects with the same {@literal id}.
     * 
     * @param <S> sub-class of {@code <T>}.
     * @param entities collection of objects (entities) saved to repository.
     * @return collection of saved objects.
     * @throws IllegalArgumentException {@literal entities} is {@literal null}.
     */
    <S extends T> Iterable<S> saveAll(Iterable<S> entities);

    /**
     * Delete object with id: {@literal id} from repository, if objects exists.
     * No change of repository if no object with id: {@literal id} exists.
     * 
     * @param id {@literal id} of entity to delete.
     * @throws IllegalArgumentException {@literal id} is {@literal null}.
     */
    void deleteById(ID id);

    /**
     * Delete object (entity) from repository. No change of repository if object
     * does not exist.
     * 
     * @param entity {@literal entity} to delete.
     * @throws IllegalArgumentException {@literal entity} is {@literal null}.
     */
    void delete(T entity);

    /**
     * Delete objects of matching collection of {@literal ids} from repository.
     * No change of repository if an {@literal id} does not exist.
     * 
     * @param ids collection of {@literal ids} to delete.
     * @throws IllegalArgumentException {@literal entity} is {@literal null}.
     */
    void deleteAllById(Iterable<? extends ID> ids);

    /**
     * Delete collection of objects (entities) from repository.
     * No change of repository if an {@literal object} does not exist.
     * 
     * @param entities collection of {@literal entities} to delete.
     * @throws IllegalArgumentException {@literal entities} is {@literal null}.
     */
    void deleteAll(Iterable<? extends T> entities);

    /**
     * Clear repository and delete all objects from repository.
     * 
     */
    void deleteAll();

}