package pl.lodz.p.it.spjava.benefit.ejb.facade;

import java.sql.SQLNonTransientConnectionException;
import java.util.List;
import javax.annotation.security.PermitAll;
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
import pl.lodz.p.it.spjava.benefit.exception.AccountException;
import pl.lodz.p.it.spjava.benefit.exception.AppBaseException;
import pl.lodz.p.it.spjava.benefit.model.Account;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors(LoggingInterceptor.class)
public class AccountFacade extends AbstractFacade<Account> {

    public static final String DB_UNIQUE_ACCOUNT_LOGIN = "UNIQUE_LOGIN";

    public static final String DB_F_KEY_ACCOUNT_AUTHORIZED_BY = "CCOUNTAUTHORIZEDBY";
    public static final String DB_F_KEY_ACCOUNT_MODIFICATED_BY = "CCOUNTMDIFICATEDBY";

    public static final String DB_F_KEY_REWARDS_CREATED_BY = "REWARDS_CREATED_BY";
    public static final String DB_F_KEY_REWARDS_MODIFICATED_BY = "RWARDSMDIFICATEDBY";

    public static final String DB_F_KEY_ORDERS_ORDERED_BY = "ORDERS_ORDERED_BY";
    public static final String DB_F_KEY_ORDERS_MODIFICATED_BY = "ORDERS_MODIFICATED_BY"; 

    @PersistenceContext(unitName = "BenefitPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AccountFacade() {
        super(Account.class);
    }

    @PermitAll
    @Override
    public void create(Account entity) throws AppBaseException {
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
            if (cause instanceof DatabaseException && cause.getMessage().contains(DB_UNIQUE_ACCOUNT_LOGIN)) {
                throw AccountException.loginAlreadyExistsException(pe, entity);
            } else {
                throw AppBaseException.databaseQueryProblemException(pe);
            }
        }
    }
    
    @PermitAll
    @Override
    public void edit(Account entity) throws AppBaseException {
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
        } catch (PersistenceException ole) {
            throw AppBaseException.databaseQueryProblemException(ole);
        }
    }

    @RolesAllowed({"Administrator"})
    @Override
    public void remove(Account entity) throws AppBaseException {
        try {
            super.remove(entity);
        } catch (DatabaseException de) {
            if (de.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.databaseConnectionProblemException(de);
            } else {
                throw AppBaseException.databaseQueryProblemException(de);
            }
        } catch (OptimisticLockException e) {
            throw AppBaseException.optimisticLockException(e);
        } catch (PersistenceException pe) {
            final Throwable cause = pe.getCause();
            if (cause instanceof DatabaseException && (cause.getMessage().contains(DB_F_KEY_REWARDS_CREATED_BY) || cause.getMessage().contains(DB_F_KEY_REWARDS_MODIFICATED_BY))) {
                throw AccountException.accountInUseInRewardException(pe, entity);
            }
            if (cause instanceof DatabaseException && (cause.getMessage().contains(DB_F_KEY_ACCOUNT_AUTHORIZED_BY) || cause.getMessage().contains(DB_F_KEY_ACCOUNT_MODIFICATED_BY))) {
                throw AccountException.accountInUseInOtherAccountException(pe, entity);
            }
            if (cause instanceof DatabaseException && (cause.getMessage().contains(DB_F_KEY_ORDERS_MODIFICATED_BY) || cause.getMessage().contains(DB_F_KEY_ORDERS_ORDERED_BY))) {
                throw AccountException.accountInUseInOrderException(pe, entity);
            } else {
                throw AppBaseException.databaseQueryProblemException(pe);
            }
        }
    }

    @PermitAll // access required for password reset
    public Account findByLogin(String login) throws AppBaseException {
            TypedQuery<Account> tq = em.createNamedQuery("Account.findByLogin", Account.class);
            tq.setParameter("login", login);
        try {
            return tq.getSingleResult();
        } catch (NoResultException nre) {
            throw AccountException.accountNotExistsException(nre);
        } catch (PersistenceException pe) {
            final Throwable cause = pe.getCause();
            if (cause instanceof DatabaseException && cause.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.databaseConnectionProblemException(pe);
            } else {
                throw AppBaseException.databaseQueryProblemException(cause);
            }
        }
    }

    @RolesAllowed({"Administrator"})
    public List<Account> findNewRegisteredAccounts() throws AppBaseException {
            TypedQuery<Account> tq = em.createNamedQuery("Account.findNewRegisteredAccount", Account.class);
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

    @RolesAllowed({"Administrator"})
    public List<Account> findAuthorizedAccount() throws AppBaseException {
            TypedQuery<Account> tq = em.createNamedQuery("Account.findAuthorizedAccount", Account.class);
        try {
            return tq.getResultList();
        } catch (PersistenceException pe) {
            if (pe.getCause() instanceof DatabaseException && pe.getCause().getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.databaseConnectionProblemException(pe);
            } else {
                throw AppBaseException.databaseQueryProblemException(pe);
            }
        }
    }

}
