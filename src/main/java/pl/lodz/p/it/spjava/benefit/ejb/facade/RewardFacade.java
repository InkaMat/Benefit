package pl.lodz.p.it.spjava.benefit.ejb.facade;

import java.sql.SQLNonTransientConnectionException;
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
import pl.lodz.p.it.spjava.benefit.exception.RewardException;
import pl.lodz.p.it.spjava.benefit.model.Reward;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors(LoggingInterceptor.class)
public class RewardFacade extends AbstractFacade<Reward> {

    public static final String DB_UNIQUE_CONSTRAINT_REWARD_NAME = "UNIQUE_REW_NAME";
    public static final String DB_UNIQUE_CONSTRAINT_REWARD_TYPE_AND_FREQUENCY_OF_USE = "UNIQUE_R_TYPE_FREQ";
    public static final String DB_ORDERS_ORDERED_REWARD = "RDERSORDEREDREWARD";

    @PersistenceContext(unitName = "BenefitPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RewardFacade() {
        super(Reward.class);
    }

    @RolesAllowed({"Manager"})
    @Override
    public void create(Reward entity) throws AppBaseException {
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
            if (cause instanceof DatabaseException && cause.getMessage().contains(DB_UNIQUE_CONSTRAINT_REWARD_NAME)) {
                throw RewardException.rewardNameAlreadyExistsException(pe, entity);
            }
            if (cause instanceof DatabaseException && cause.getMessage().contains(DB_UNIQUE_CONSTRAINT_REWARD_TYPE_AND_FREQUENCY_OF_USE)) {
                throw RewardException.rewardTypeAndFrequencyAlreadyExistsException(pe, entity);
            } else {
                throw AppBaseException.databaseQueryProblemException(pe);
            }
        }
    }

    @RolesAllowed({"Manager"})
    @Override
    public void edit(Reward entity) throws AppBaseException {
        try {
            super.edit(entity);
        } catch (DatabaseException de) {
            if (de.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.databaseConnectionProblemException(de);
            } else {
                throw AppBaseException.databaseQueryProblemException(de);
            }
        } catch (OptimisticLockException ole) {
            throw AppBaseException.optimisticLockException(ole);
        } catch (PersistenceException pe) {
            final Throwable cause = pe.getCause();
            if (cause instanceof DatabaseException && cause.getMessage().contains(DB_ORDERS_ORDERED_REWARD)) {
                throw RewardException.rewardInOrderException(pe, entity);
            }
            if (cause instanceof DatabaseException && cause.getMessage().contains(DB_UNIQUE_CONSTRAINT_REWARD_TYPE_AND_FREQUENCY_OF_USE)) {
                throw RewardException.rewardTypeAndFrequencyAlreadyExistsException(pe, entity);
            } else {
                throw AppBaseException.databaseQueryProblemException(pe);
            }
        }
    }

    @RolesAllowed({"Manager"})
    @Override
    public void remove(Reward entity) throws AppBaseException {
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
            final Throwable cause = pe.getCause();
            if (cause instanceof DatabaseException && cause.getMessage().contains(DB_ORDERS_ORDERED_REWARD)) {
                throw RewardException.rewardInOrderException(pe, entity);
            } else {
                throw AppBaseException.databaseQueryProblemException(pe);
            }
        }
    }

    @RolesAllowed({"Manager", "Employee"})
    public Reward findByRewardName(String rewardName) throws AppBaseException {
        TypedQuery<Reward> tq = em.createNamedQuery("Reward.findByRewardName", Reward.class);
        tq.setParameter("rewardName", rewardName);
        try {
            return tq.getSingleResult();
        } catch (NoResultException nre) {
            throw RewardException.createExceptionRewardNotExist(nre);
        } catch (PersistenceException pe) {
            final Throwable cause = pe.getCause();
            if (cause instanceof DatabaseException && cause.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.databaseConnectionProblemException(pe);
            } else {
                throw AppBaseException.databaseQueryProblemException(cause);
            }
        }
    }

    @RolesAllowed({"Manager"})
    @Override
    public List<Reward> findAll() throws AppBaseException {
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
    public List<Reward> findActiveReward() throws AppBaseException {
        TypedQuery<Reward> tq = em.createNamedQuery("Reward.findActive", Reward.class);
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

}
