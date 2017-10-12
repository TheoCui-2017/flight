package flight.spider.web.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;
public interface IbaseDAO <T, ID extends Serializable>{

	public abstract void save(T t) throws DataAccessException;

	public abstract void saveOrUpdate(T t) throws DataAccessException;

	public abstract T load(ID id) throws DataAccessException;

	public abstract T get(ID id) throws DataAccessException;
	    
	public abstract boolean contains(T t) throws DataAccessException;

	public abstract void delete(T t) throws DataAccessException;

	public abstract void deleteAll(Collection<T> entities)
			throws DataAccessException;

	public abstract List<T> loadByHQL(String hqlString)
			throws DataAccessException;

	public abstract List<T> loadBySQL(String sqlString)
			throws DataAccessException;

	public abstract void update(T t) throws DataAccessException;
}
