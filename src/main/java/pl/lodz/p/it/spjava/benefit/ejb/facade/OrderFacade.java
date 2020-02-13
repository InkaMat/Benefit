package pl.lodz.p.it.spjava.benefit.ejb.facade;

import java.sql.SQLNonTransientConnectionException;
import java.util.Date;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import org.eclipse.persistence.exceptions.DatabaseException;
import pl.lodz.p.it.spjava.benefit.ejb.interceptor.LoggingInterceptor;
import pl.lodz.p.it.spjava.benefit.exception.AppBaseException;
import pl.lodz.p.it.spjava.benefit.exception.OrderException;
import pl.lodz.p.it.spjava.benefit.model.Order;
import pl.lodz.p.it.spjava.benefit.model.Reward;
import pl.lodz.p.it.spjava.benefit.model.Employee;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors(LoggingInterceptor.class)
public class OrderFacade extends AbstractFacade<Order> {

    public static final String DB_UNIQUE_ORDERED_BY_THE_EMPLOYEE_FOR_THAT_MONTH = "UNIQUE_EMPL_MONTH";

    @PersistenceContext(unitName = "BenefitPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OrderFacade() {
        super(Order.class);
    }

    @RolesAllowed({"Employee"})
    @Override
    public void create(Order entity) throws AppBaseException {
        try {
            super.create(entity);
        } catch (DatabaseException de) {
            if (de.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.databaseConnectionProblemException(de);
            } else {
                throw AppBaseException.databaseQueryProblemException(de);
            }
        } catch (PersistenceException pe) {
            final Throwable cause = pe.getCause();
            if (cause instanceof DatabaseException && cause.getMessage().contains(DB_UNIQUE_ORDERED_BY_THE_EMPLOYEE_FOR_THAT_MONTH)) {
                throw OrderException.employeeOrderedForThisDateException(pe, entity);
            } else {
                throw AppBaseException.databaseQueryProblemException(pe);
            }
        }
    }


    @RolesAllowed({"Manager", "Employee"})
    @Override
    public void remove(Order entity) throws AppBaseException {
        try {
            super.remove(entity);
        } catch (DatabaseException de) {
            if (de.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.databaseConnectionProblemException(de);
            } else {
                throw AppBaseException.databaseQueryProblemException(de);
            }
        } catch (OptimisticLockException ole) {
            throw AppBaseException.optimisticLockException(ole);
        } catch (PersistenceException pe) {
            throw AppBaseException.databaseQueryProblemException(pe);
        }
    }

    @RolesAllowed({"Manager"})
    @Override
    public List<Order> findAll() throws AppBaseException {
        try {
            return super.findAll();
        } catch (PersistenceException pe) {
            final Throwable cause = pe.getCause();
            if (cause instanceof DatabaseException && cause.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.databaseConnectionProblemException(pe);
            } else {
                throw AppBaseException.databaseQueryProblemException(cause);
            }
        }
    }

    @RolesAllowed({"Employee"})
    public List<Order> findMyOrders(Employee orderedBy) throws AppBaseException {
        TypedQuery<Order> tq = em.createNamedQuery("Order.findMyOrders", Order.class
        );
        tq.setParameter("orderedBy", orderedBy);
        try {
            return tq.getResultList();
        } catch (PersistenceException pe) {
            final Throwable cause = pe.getCause();
            if (cause instanceof DatabaseException && cause.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.databaseConnectionProblemException(pe);
            } else {
                throw AppBaseException.databaseQueryProblemException(cause);
            }
        }
    }

    @RolesAllowed({"Manager", "Employee"})
    public Order
            findByRewardAndDate(Reward orderedReward, Date orderMonth) throws AppBaseException {
        TypedQuery<Order> tq = em.createNamedQuery("Order.findByRewardAndDate", Order.class
        );
        tq.setParameter("orderedReward", orderedReward);
        tq.setParameter("orderMonth", orderMonth);
        try {
            return tq.getSingleResult();
        } catch (NoResultException nre) {
            throw OrderException.noOrderFoundException(nre);
        } catch (PersistenceException pe) {
            final Throwable cause = pe.getCause();
            if (cause instanceof DatabaseException && cause.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.databaseConnectionProblemException(pe);
            } else {
                throw AppBaseException.databaseQueryProblemException(cause);
            }
        }

    }

}
