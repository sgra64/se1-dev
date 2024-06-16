package system.impl;

import java.util.*;
import java.util.function.Function;
import java.util.stream.StreamSupport;

import system.Repository;

/**
 * Implementation class of {@code Repository<T, ID>}.
 *
 * @param <T> generic type of Repository objects (entities).
 * @param <ID> generic type of object identifier (id).
 */
class RepositoryImpl<T, ID> implements Repository<T, ID> {

    /**
     * Map that actually stores {@link datamodel} objects of type T.
     */
    private final Map<ID, T> map = new HashMap<>();

    /**
     * Externally provided function that obtains id from entity. 
     */
    private final Function<T, ID> getIdFunc;


    /**
     * Constructor with function argument to obtain the id from an entity of type T.
     * 
     * @param getIdFunc function that obtains id from entity of type T.
     */
    RepositoryImpl(Function<T, ID> getIdFunc) {
        if(getIdFunc==null)
            throw new IllegalArgumentException("argument getIdFunc is null.");
        //
        this.getIdFunc = getIdFunc;
    }

    /**
     * Return the current number of objects in repository.
     *
     * @return number of entities.
     */
    @Override
    public long count() {
        return map.size();
    }

    /**
     * Return all repository objects.
     * 
     * @return all repository objects.
     */
    @Override
    public Iterable<T> findAll() {
        return map.values();
    }

    /**
     * Return result of a lookup of an object by its {@literal id}.
     * 
     * @param id {@literal id} of object to find.
     * @return result of lookup of an object by its {@literal id}.
     * @throws IllegalArgumentException {@literal id} is {@literal null}.
     */
    @Override
    public Optional<T> findById(ID id) {
        if(id==null)
            throw new IllegalArgumentException("argument id is null.");
        //
        return Optional.ofNullable(map.get(id));
    }

    /**
     * Return result of a lookup of an object by its {@literal id}.
     * 
     * @param id {@literal id} of object to find.
     * @return true if an object with {@literal id} exists in repository.
     * @throws IllegalArgumentException {@literal id} is {@literal null}.
     */
    @Override
    public boolean existsById(ID id) {
        return findById(id).isPresent();
    }

    /**
     * Return result of a lookup of objects by a collection of {@literal ids}.
     * No object is included in result if {@literal id} could not be found.
     * 
     * @param id collection of {@literal ids} for which objects are looked up.
     * @return result of lookup by a collection of {@literal ids}.
     * @throws IllegalArgumentException {@literal ids} is {@literal null}.
     */
    @Override
    public Iterable<T> findAllById(Iterable<ID> ids) {
        if(ids==null)
            throw new IllegalArgumentException("argument ids is null.");
        //
        return StreamSupport.stream(ids.spliterator(), false)
            .filter(id -> id != null)
            .map(id -> map.get(id))
            .filter(e -> e != null).toList();
    }

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
    @Override
    public <S extends T> S save(S entity) {
        if(entity==null)
            throw new IllegalArgumentException("argument entity is null.");
        //
        ID id = getIdFunc.apply(entity);
        if(id != null) {
            // found id from entity, test whether id is already present in map
            Optional.ofNullable(map.get(id)).ifPresentOrElse(e2 -> {
                // entity e2 with id already exists
                // - policy 1: replace with new object
                // - policy 2: keep object and update values
                map.put(id, entity);    // use policy 1
            //
            }, () -> map.put(id, entity));  // id was not present, add
        //
        } else
            throw new IllegalArgumentException("entity.id is null.");
        //
        return entity;
    }

    /**
     * Save a collection of objects (entities) to a repository. Objects replace
     * prior objects with the same {@literal id}.
     * 
     * @param <S> sub-class of {@code <T>}.
     * @param entities collection of objects (entities) saved to repository.
     * @return collection of saved objects.
     * @throws IllegalArgumentException {@literal entities} is {@literal null}.
     */
    @Override
    public <S extends T> Iterable<S> saveAll(Iterable<S> entities) {
        if(entities==null)
            throw new IllegalArgumentException("argument entities is null.");
        //
        List<S> saved = new ArrayList<>();
        entities.forEach(e -> saved.add(save(e)));
        return saved;
    }

    /**
     * Delete object with id: {@literal id} from repository, if objects exists.
     * No change of repository if no object with id: {@literal id} exists.
     * 
     * @param id {@literal id} of entity to delete.
     * @throws IllegalArgumentException {@literal id} is {@literal null}.
     */
    @Override
    public void deleteById(ID id) {
        if(id==null)
            throw new IllegalArgumentException("argument id is null.");
        //
        map.remove(id);
    }

    /**
     * Delete object (entity) from repository. No change of repository if object
     * does not exist.
     * 
     * @param entity {@literal entity} to delete.
     * @throws IllegalArgumentException {@literal entity} is {@literal null}.
     */
    @Override
    public void delete(T entity) {
        if(entity==null)
            throw new IllegalArgumentException("argument entity is null.");
        //
        ID id = getIdFunc.apply(entity);
        if(id != null) {
            map.remove(id);
        //
        } else
            throw new IllegalArgumentException("entity.id is null.");
    }

    /**
     * Delete objects of matching collection of {@literal ids} from repository.
     * No change of repository if an {@literal id} does not exist.
     * 
     * @param ids collection of {@literal ids} to delete.
     * @throws IllegalArgumentException {@literal entity} is {@literal null}.
     */
    @Override
    public void deleteAllById(Iterable<? extends ID> ids) {
        if(ids==null)
            throw new IllegalArgumentException("argument ids is null.");
        //
        ids.forEach(id -> {
            map.remove(id);
        });
    }

    /**
     * Delete collection of objects (entities) from repository.
     * No change of repository if an {@literal object} does not exist.
     * 
     * @param entities collection of {@literal entities} to delete.
     * @throws IllegalArgumentException {@literal entities} is {@literal null}.
     */
    @Override
    public void deleteAll(Iterable<? extends T> entities) {
        if(entities==null)
            throw new IllegalArgumentException("argument entities is null.");
        //
        entities.forEach(e -> {
            map.remove(e);
        });
    }

    /**
     * Clear repository and delete all objects from repository.
     * 
     */
    @Override
    public void deleteAll() {
        map.clear();
    }
}