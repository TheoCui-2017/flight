package flight.spider.web.dao.impl;

import java.io.Serializable;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import flight.spider.web.dao.IbaseDAO;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

import javax.annotation.PreDestroy;


@Repository
public class baseDAO <T, ID extends Serializable> implements IbaseDAO<T, ID>{
	@Autowired
	protected SessionFactory sessionFactory;
	protected Class<T> entityClass;
	
	protected Class getEntityClass() {
		if (entityClass == null) {
			entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
			//logger.debug("T class = " + entityClass.getName());
		}
		return entityClass;
	}

	
	@Override
	public void save(T t) throws DataAccessException {
		this.getSession().save(t);
	}
	
	
	@Override
	public void saveOrUpdate(T t) throws DataAccessException {
		this.getSession().saveOrUpdate(t);
	}

	
	@Override
	public T load(ID id) throws DataAccessException {
		T load = (T) this.getSession().load(getEntityClass(), id);
		return load;
	}

	
	@Override
	public T get(ID id) throws DataAccessException {
		T load = (T) this.getSession().get(getEntityClass(), id);
		return load;
	}
	 
	
	@Override
	public boolean contains(T t) throws DataAccessException {
		return this.getSession().contains(t);
	}

	
	@Override
	public void delete(T t) throws DataAccessException {
		this.getSession().delete(t);
	}

	
	@Override
	public void deleteAll(Collection<T> entities) throws DataAccessException {
		for(Object entity : entities) {
			this.getSession().delete(entity);
		}
	}

	
	@Override
	public List<T> loadByHQL(String hqlString)throws DataAccessException {
		return this.getSession().createQuery(hqlString).list();
	}

	
	@Override
	public List<T> loadBySQL(String sqlString) throws DataAccessException {
		return this.getSession().createSQLQuery(sqlString).list();
	}

	@Override
	public void update(T t) throws DataAccessException {
		this.getSession().update(t);
	}
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}
}
