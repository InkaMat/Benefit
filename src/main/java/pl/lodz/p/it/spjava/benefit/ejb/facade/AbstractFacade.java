package pl.lodz.p.it.spjava.benefit.ejb.facade;

import java.util.List;
import javax.persistence.EntityManager;
import pl.lodz.p.it.spjava.benefit.exception.AppBaseException;

public abstract class AbstractFacade<T> {

    private Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public void create(T entity) throws AppBaseException {
        getEntityManager().persist(entity);
        getEntityManager().flush();
    }

    public void edit(T entity) throws AppBaseException {
        getEntityManager().merge(entity);
        getEntityManager().flush();
    }

    public void remove(T entity) throws AppBaseException {
        getEntityManager().remove(getEntityManager().merge(entity));
        getEntityManager().flush();
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);

    }

    public List<T> findAll() throws AppBaseException{
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }


}
