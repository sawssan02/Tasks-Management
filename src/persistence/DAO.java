package persistence;

import org.bson.types.ObjectId;

public interface DAO<T> {
    public void create(T t);
    public T read(ObjectId id);
    public void update(T t,ObjectId id);
    public void delete(ObjectId id);
}
